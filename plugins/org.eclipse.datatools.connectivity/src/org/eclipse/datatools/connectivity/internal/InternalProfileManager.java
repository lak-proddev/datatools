/*******************************************************************************
 * Copyright (c) 2004-2008 Sybase, Inc.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: shongxum - initial API and implementation
 *     IBM Corporation -  fix for defect #223855
 ******************************************************************************/
package org.eclipse.datatools.connectivity.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.datatools.connectivity.ConnectionProfileConstants;
import org.eclipse.datatools.connectivity.ConnectionProfileException;
import org.eclipse.datatools.connectivity.ICategory;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.IManagedConnection;
import org.eclipse.datatools.connectivity.IProfileListener;
import org.eclipse.datatools.connectivity.IProfileListener1;
import org.eclipse.datatools.connectivity.IPropertySetChangeEvent;
import org.eclipse.datatools.connectivity.IPropertySetListener;
import org.eclipse.datatools.connectivity.ProfileManager;
import org.eclipse.datatools.connectivity.drivers.DriverInstance;
import org.eclipse.datatools.connectivity.drivers.DriverManager;
import org.eclipse.datatools.connectivity.internal.repository.IConnectionProfileRepository;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

/**
 * ProfileManger is a singleton class serving as a helper class for connection
 * profiles access. It also caches connection profiles and only persists to
 * storage when changes made and at Eclipse shutdown.
 * 
 * @author shongxum
 */
public class InternalProfileManager {
	
	public static String PROFILE_PATH_SEPARATOR = "::"; //$NON-NLS-1$

	private static InternalProfileManager mManager = null;

	private IConnectionProfile[] mProfiles = null;
	
	private Set mRepositories = new HashSet();

	private boolean mIsDirty = false;
	
	private ListenerList mProfileListeners = new ListenerList();
	
	private static final String PLUGIN_STATE_LOCATION = "Plugin_State_Location"; //$NON-NLS-1$
	private boolean loadLocal = true;  // do we need to load locally registered db's?
	
	private IPropertySetListener mPropertySetListener = new IPropertySetListener() {
		public void propertySetChanged(IPropertySetChangeEvent event) {
			setDirty(true);
			saveChanges();
			// Pass this through to any listeners on the manager
			fireProfileChanged(event.getConnectionProfile(),null,null,null);
		}
	};
	
	private IProfileListener1 mProfileChangeListener = new IProfileListener1() {

		public void profileChanged(IConnectionProfile profile, String oldName,
				String oldDesc, Boolean oldAutoConnect) {
			removeOldFailureMarkers(oldName);
		}

		public void profileAdded(IConnectionProfile profile) {
		}

		public void profileChanged(IConnectionProfile profile) {
			removeOldFailureMarkers(profile.getName());
		}

		public void profileDeleted(IConnectionProfile profile) {
			removeOldFailureMarkers(profile.getName());
		}
		
	};

	private InternalProfileManager() {
		// Singleton class
	}

	public static InternalProfileManager getInstance() {
		if (mManager == null)
			synchronized( InternalProfileManager.class ) {
				if (mManager == null)
					mManager = new InternalProfileManager();
			}
		return mManager;
	}

	/**
	 * Get all connection profiles persisted in workspace
	 * 
	 * @return connection profiles
	 */
	public IConnectionProfile[] getProfiles(boolean searchRepositories) {
		ArrayList cps = new ArrayList();
		if (mProfiles == null) {
			loadProfiles();
		}
		
		cps.addAll(Arrays.asList(mProfiles));
		
		if (searchRepositories) {
			for (Iterator it = mRepositories.iterator(); it.hasNext(); ) {
				cps.addAll(Arrays.asList(((IConnectionProfileRepository)it.next()).getProfiles()));
			}
		}
		return (IConnectionProfile[])cps.toArray(new IConnectionProfile[0]);
	}

	/**
	 * Get category by name
	 * 
	 * @param catID
	 * @return ICategory
	 */
	public ICategory getCategory(String catID) {
		ConnectionProfileManager cpm = ConnectionProfileManager.getInstance();
		CategoryProvider cp = cpm.getCategory(catID);
		return cp == null ? null : cp.createCategory(null);
	}

	/**
	 * Get root categories whose parent category is null
	 * 
	 * @return ICategory[]
	 */
	public ICategory[] getRootCategories() {
		Collection col = ConnectionProfileManager.getInstance().getCategories()
				.values();
		ArrayList cats = new ArrayList(col.size());
		for (Iterator itr = col.iterator(); itr.hasNext();) {
			CategoryProvider cp = (CategoryProvider) itr.next();
			if (cp.getParent() == null)
				cats.add(cp.createCategory(null));
		}
		return (ICategory[]) cats.toArray(new ICategory[cats.size()]);
	}

	/**
	 * Get connection profiles by category
	 * 
	 * @param catID
	 * @return IConnectionProfile[]
	 */
	public IConnectionProfile[] getProfilesByCategory(String catID, boolean searchRepositories) {
		ArrayList cps = new ArrayList();
		IConnectionProfile[] profiles = getProfiles(false);
		if (catID == null)
			return profiles;
		for (int i = 0; i < profiles.length; i++) {
			if (profiles[i].getProvider().getCategory() != null
					&& profiles[i].getProvider().getCategory().getId().equals(
							catID))
				cps.add(profiles[i]);
		}
		if (searchRepositories) {
			for (Iterator it = mRepositories.iterator(); it.hasNext(); ) {
				cps.addAll(Arrays.asList(((IConnectionProfileRepository)it.next()).getProfilesByCategory(catID)));
			}
		}
		return (IConnectionProfile[]) cps.toArray(new IConnectionProfile[0]);
	}

	/**
	 * Get connection profile by name
	 * 
	 * @param name
	 * @return IConnectionProfile
	 */
	public IConnectionProfile getProfileByName(String name,
			boolean checkRepositories) {
		IConnectionProfile[] cps = getProfiles(false);
		IConnectionProfile cp = null;
		for (int i = 0; i < cps.length; i++) {
			if (cps[i].getName().equals(name)) {
				cp = cps[i];
				break;
			}
		}
		if (checkRepositories) {
			for (Iterator it = mRepositories.iterator(); cp == null
					&& it.hasNext();) {
				cp = ((IConnectionProfileRepository) it.next())
						.getProfileByName(name);
			}
		}
		return cp;
	}
	
	public IConnectionProfile getProfileByPath(String path) {
		if (path == null || path.length() == 0) {
			return null;
		}

		int separator = path.indexOf(PROFILE_PATH_SEPARATOR);
		if (separator < 0) {
			return getProfileByName(path, false);
		}
		return getProfileByPath(getProfileByName(path.substring(0, separator),
				false), path.substring(separator
				+ PROFILE_PATH_SEPARATOR.length()));
	}

	/**
	 * Find a connection profile by the full path. Typically this path is 
	 * 	REPOSITORY::PROFILE
	 * @param path
	 * @return
	 */
	public IConnectionProfile getProfileByFullPath ( String path ) {
		if (path != null) {
			String[] tokens = tokenize(path, PROFILE_PATH_SEPARATOR);
			if (tokens != null && tokens.length > 0) {
				String testForProfile = tokens[0];
				boolean hasRepository = false;
				IConnectionProfile testRepo = this.getProfileByName(testForProfile, false);
				if (testRepo != null && mRepositories.contains(getRepositoryByProfile(testRepo))) {
					hasRepository = true;
				}
				if (testRepo != null && mRepositories.contains(testRepo)) {
					hasRepository = true;
				}
				String shortPath = tokens[tokens.length - 1];
				if (hasRepository) {
					shortPath = this.getProfileByName(testForProfile, false).getName() +
						PROFILE_PATH_SEPARATOR + shortPath;
				}
				IConnectionProfile foundProfile = getProfileByPath(shortPath);
				if (foundProfile != null) 
					return foundProfile;
			}
		}
		return null;
	}
	
	private IConnectionProfileRepository getRepositoryByProfile(IConnectionProfile profile) {
		if (profile != null) {
			IManagedConnection imc = profile.getManagedConnection(IConnectionProfileRepository.class.getName());
			if (imc != null && imc.getConnection() != null) {
				return (IConnectionProfileRepository)imc.getConnection().getRawConnection();
			}
		}
		return null;
	}

	/**
	 * Retokenize by putting an array back together with the path separator between items
	 * @param tokens
	 * @return
	 */
	public String unTokenize(String[] tokens) {
		StringBuffer buffer = new StringBuffer();
		if (tokens != null && tokens.length > 0) {
			for (int i = 0; i < tokens.length; i++) {
				buffer.append(tokens[i]);
				if (i < (tokens.length - 1)) {
					buffer.append(InternalProfileManager.PROFILE_PATH_SEPARATOR);
				}
			}
		}
		return buffer.toString();
	}
	
	/**
	 * Break a delimited string into an array
	 * @param input
	 * @param delim
	 * @return
	 */
	public String[] tokenize (String input, String delim) {
		char[] chars = input.toCharArray();
		ArrayList list = new ArrayList();
		String temp = ""; //$NON-NLS-1$
		boolean skip = false;
		for (int i = 0; i < chars.length; i++) {
			char test = chars[i];
			char test2 = ' ';
			if (i < (chars.length - 1)) {
				test2 = chars[i+1];
			}
			String testStr = "" + test + test2; //$NON-NLS-1$
			if (testStr.equals(delim)) {
				list.add(temp.trim());
				temp = ""; //$NON-NLS-1$
				skip = true;
			}
			else if (!skip) {
				temp = temp + test;
			}
			else {
				skip = false;
			}
			if (i == (chars.length - 1))
				list.add(temp.trim());
		}
		return (String[]) list.toArray(new String[list.size()]);
	}
	
	/**
	 * Return the delimited path for a profile (REPO::PROFILE)
	 * @param profile
	 * @return
	 */
	public String getProfileFullPath ( IConnectionProfile profile ) {
		String path = null;
		
		if (profile != null) {
			path = profile.getName();
			
			if (profile.getCategory() != null) {
				ICategory category = profile.getCategory();
				while (category != null) {
					path = category.getId() + PROFILE_PATH_SEPARATOR + path;
					category = category.getParent();
				}
				
				IConnectionProfileRepository repository =
					getRepositoryForProfile(profile);
				if (repository != null) {
					path = repository.getRepositoryProfile().getName() + 
						PROFILE_PATH_SEPARATOR + path;
				}
			}
		}
		return path;
	}
	
	private IConnectionProfile getProfileByPath(IConnectionProfile parent,
			String path) {
		if (parent == null
				|| parent.getProvider().getConnectionFactory(
						IConnectionProfileRepository.class.getName()) == null
				|| path == null || path.length() == 0) {
			return null;
		}

		IManagedConnection imc = parent
				.getManagedConnection(IConnectionProfileRepository.class
						.getName());
		if (imc == null || !imc.isConnected() || imc.getConnection() == null) {
			return null;
		}

		IConnectionProfileRepository repo = (IConnectionProfileRepository) imc
				.getConnection().getRawConnection();
		int separator = path.indexOf(PROFILE_PATH_SEPARATOR);
		if (separator < 0) {
			return repo.getProfileByName(path);
		}
		return getProfileByPath(repo.getProfileByName(path.substring(0,
				separator)), path.substring(separator
				+ PROFILE_PATH_SEPARATOR.length()));
	}

	/**
	 * Get connection profile by instance ID
	 * 
	 * @param id
	 * @return IConnectionProfile
	 */
	public IConnectionProfile getProfileByInstanceID(String id) {
		IConnectionProfile[] cps = getProfiles(false);
		IConnectionProfile cp = null;
		for (int i = 0; i < cps.length; i++) {
			if (cps[i].getInstanceID().equals(id)) {
				cp = cps[i];
				break;
			}
		}
		for (Iterator it = mRepositories.iterator(); cp == null && it.hasNext(); ) {
			cp = ((IConnectionProfileRepository)it.next()).getProfileByInstanceID(id);
		}
		return cp;
	}

	/**
	 * Get connection profiles by connection profile(cp) provider ID associated
	 * with each cp
	 * 
	 * @param ID
	 * @return IConnectionProfile[]
	 */
	public IConnectionProfile[] getProfileByProviderID(String ID, boolean searchRepositories) {
		IConnectionProfile[] cps = getProfiles(false);
		ArrayList cpset = new ArrayList();
		for (int i = 0; i < cps.length; i++) {
			if (cps[i].getProviderId().equals(ID)) {
				cpset.add(cps[i]);
			}
		}
		if (searchRepositories) {
			for (Iterator it = mRepositories.iterator(); it.hasNext(); ) {
				cpset.addAll(Arrays.asList(((IConnectionProfileRepository)it.next()).getProfileByProviderID(ID)));
			}
		}
		return (IConnectionProfile[]) cpset.toArray(new IConnectionProfile[0]);
	}

	/**
	 * Create connection profile
	 * 
	 * @param name
	 * @param description
	 * @param providerID
	 * @param baseProperties
	 * @throws ConnectionProfileException
	 */
	public IConnectionProfile createProfile(String name, String description,
			String providerID, Properties baseProperties)
			throws ConnectionProfileException {
		return createProfile(name, description, providerID, baseProperties, "", false); //$NON-NLS-1$
	}

	/**
	 * Create connection profile
	 * 
	 * @param name
	 * @param description
	 * @param providerID
	 * @param baseProperties
	 * @param parentProfile
	 * @throws ConnectionProfileException
	 */
	public IConnectionProfile createProfile(String name, String description,
			String providerID, Properties baseProperties, String parentProfile)
			throws ConnectionProfileException {
		return createProfile(name, description, providerID, baseProperties,
				parentProfile, false);
	}

	/**
	 * Create connection profile
	 * 
	 * @param name
	 * @param description
	 * @param providerID
	 * @param baseProperties
	 * @param parentProfile
	 * @param autoConnect
	 * @throws ConnectionProfileException
	 */
	public IConnectionProfile createProfile(String name, String description,
			String providerID, Properties baseProperties, String parentProfile,
			boolean autoConnect) throws ConnectionProfileException {
		ConnectionProfile profile = new ConnectionProfile(name, description,
				providerID, parentProfile, autoConnect, UUID.createUUID()
						.toString());
		profile.setBaseProperties(baseProperties);
		
		IConnectionProfileRepository repo = getRepositoryForProfile(profile);
		if (repo == null) {
			addProfile(profile);
		}
		else {
			repo.addProfile(profile);
		}
		profile.setCreated();
		return profile;
	}

	/**
	 * Duplicate a connection profile
	 * 
	 * @param profile
	 * @throws ConnectionProfileException
	 */
	public String duplicateProfile(IConnectionProfile profile)
			throws ConnectionProfileException {
		ConnectionProfile cp = (ConnectionProfile) profile;
		String profileName;
		int i = 0;
		do {
			profileName = ConnectivityPlugin.getDefault().getResourceString(
					"duplicate.profile.name", //$NON-NLS-1$
					new Object[] { cp.getName(), new Integer(i)});
			i++;
		}
		while (getProfileByName(profileName,false) != null);
		IConnectionProfile newProfile = copyProfile(profile,profile.getParentProfile(),profileName);
		return newProfile.getName();
	}
	
	/**
	 * Copy a connection profile
	 * 
	 * @param source
	 * @param repo
	 * @param newName
	 * @return
	 * @throws ConnectionProfileException
	 */
	public IConnectionProfile copyProfile(IConnectionProfile source,
			IConnectionProfile repo, String newName)
			throws ConnectionProfileException {
		Properties props = (Properties) source.getBaseProperties().clone();
		IConnectionProfile newProfile = createProfile(newName, source
				.getDescription(), source.getProviderId(), props,
				repo == null ? new String() : repo.getInstanceID(), source
						.isAutoConnect());

		// now that we have the base profile and its properties set,
		// walk through any extended properties and grab those also
		Set extensionIDs = ((ConnectionProfile) source).getPropertiesMap()
				.keySet();
		Iterator iter = extensionIDs.iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			Properties oldProps = source.getProperties(key);
			Properties newProps = (Properties) oldProps.clone();
			newProfile.setProperties(key, newProps);
		}
		return newProfile;
	}

	/**
	 * Duplicate a connection profile but won't add it into ProfileManager or IConnectionProfileRepository.
	 * 
	 * @param source
	 * @param repo
	 * @param newName
	 * @return IConnectionProfile
	 * 
	 */
	public IConnectionProfile cloneProfile(IConnectionProfile source,
			IConnectionProfile repo, String newName){
		Properties props = (Properties) source.getBaseProperties().clone();
		ConnectionProfile newProfile = new ConnectionProfile(newName, source.getDescription(),
				source.getProviderId(), repo == null ? new String() : repo.getInstanceID(), source
						.isAutoConnect() , UUID.createUUID().toString());
		newProfile.setBaseProperties(props);

		// now that we have the base profile and its properties set,
		// walk through any extended properties and grab those also
		Set extensionIDs = ((ConnectionProfile) source).getPropertiesMap()
				.keySet();
		Iterator iter = extensionIDs.iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			Properties oldProps = source.getProperties(key);
			Properties newProps = (Properties) oldProps.clone();
			newProfile.setProperties(key, newProps);
		}
		return newProfile;			
	}
	
	/**
	 * Add a connection profile object to the profiles cache. Throws
	 * ConnectionProfileException if the new profile's name already exists in
	 * cache.
	 * 
	 * @param profile
	 * @throws ConnectionProfileException
	 */
	public void addProfile(IConnectionProfile profile)
			throws ConnectionProfileException {
		addProfile(profile, false);
	}

	/**
	 * Add a connection profile object to the profiles cache. If the new
	 * profile's name already exists in cache, replace the cached profile with
	 * the given profile provided the replaceExisting flag is true; otherwise,
	 * throws ConnectionProfileException.
	 * 
	 * @param profile
	 * @param replaceExisting
	 * @throws ConnectionProfileException
	 */
	public void addProfile(IConnectionProfile profile, boolean replaceExisting)
			throws ConnectionProfileException {
		// check if the new profile's name already exists in profiles cache
		IConnectionProfile[] cps = getProfiles(false);
		for (int i = 0; i < cps.length; i++) {
			if (cps[i].getName().equals(profile.getName())) {
				if (!replaceExisting)
					throw new ConnectionProfileException(ConnectivityPlugin
							.getDefault().getResourceString(
									"profile.duplicate", //$NON-NLS-1$
									new Object[] { profile.getName()}));

				// replace existing cached profile of same name with the new
				// profile
				if (cps[i] != profile)
					modifyProfile(profile);
				return;
			}
		}
		
		((ConnectionProfile)profile).setRepository(null);

		// add new profile to profile caches

		mProfiles = new IConnectionProfile[cps.length + 1];
		if (cps.length != 0)
			System.arraycopy(cps, 0, mProfiles, 0, cps.length);
		mProfiles[cps.length] = profile;
		
		profile.addPropertySetListener(mPropertySetListener);

		mIsDirty = true;

		fireProfileAdded(profile);
		saveChanges();
		
		if (profile.isAutoConnect()) {
			profile.connect(null);
		}
	}
	
	/**
	 * Remove a profile from the list of profiles
	 * 
	 * @param profile
	 * @throws ConnectionProfileException
	 */
	public void removeProfile(IConnectionProfile profile)
			throws ConnectionProfileException {
		if (profile.getParentProfile() != null) {
			return;
		}

		IConnectionProfile[] oldProfiles = mProfiles;
		mProfiles = new IConnectionProfile[oldProfiles.length - 1];
		int oldIndex = 0, oldCount = oldProfiles.length;
		for (int newIndex = 0, newCount = oldCount - 1; oldIndex < oldCount
				&& newIndex < newCount; ++oldIndex, ++newIndex) {
			if (oldProfiles[oldIndex].equals(profile)) {
				// remove this profile
				--newIndex;
				continue;
			}
			mProfiles[newIndex] = oldProfiles[oldIndex];
		}
		if (oldIndex != oldProfiles.length
				&& !oldProfiles[oldProfiles.length-1].equals(profile)) {
			// Don't know how this happened, but the profile is not contained by
			// this repository. All that work for nothing.
			mProfiles = oldProfiles;
			return;
		}

		mIsDirty = true;

		fireProfileDeleted(profile);

		saveChanges();
	}

	/**
	 * Delete a connection profile object from the profiles cache
	 * 
	 * @param profile
	 * @throws ConnectionProfileException
	 */
	public void deleteProfile(IConnectionProfile profile)
			throws ConnectionProfileException {
	    
	    IStatus status = profile.disconnect();
	    if (status == Status.CANCEL_STATUS)
        {
            return;
        }
	    
	    IConnectionProfileRepository repo = getRepositoryForProfile(profile);
		if (repo != null) {
			repo.deleteProfile(profile);
			return;
		}

		IConnectionProfile[] cps = getProfiles(false);
		ArrayList cpList = new ArrayList();
		boolean found = false;
		int index = -1;
		for (int i = 0; i < cps.length; i++) {
			cpList.add(cps[i]);
			if (cps[i].getName().equals(profile.getName())) {
				found = true;
				index = i;
			}
		}

		if (!found || cps.length == 0)
			throw new ConnectionProfileException(ConnectivityPlugin
					.getDefault().getResourceString("profile.notexist", //$NON-NLS-1$
							new Object[] { profile.getName()}));

		if (index >= 0)
			cpList.remove(index);
		mProfiles = (IConnectionProfile[]) cpList
				.toArray(new IConnectionProfile[0]);

		mIsDirty = true;
		
		fireProfileDeleted(profile);
		saveChanges();
	}

	/**
	 * Remove connection failure markers for the profile when we 
	 * delete the profile.
	 * 
	 * @param oldProfileName
	 */
	private void removeOldFailureMarkers(String oldProfileName) {
		IResource resource = ResourcesPlugin.getWorkspace().getRoot();
		try {
			IMarker[] markers = resource.findMarkers(
					"org.eclipse.datatools.connectivity.ui.profileFailure", true, //$NON-NLS-1$
					IResource.DEPTH_INFINITE);
			for (int i = 0; i < markers.length; i++) {
				if (markers[i].getAttribute(IMarker.LOCATION, new String())
						.equals(oldProfileName)) {
					markers[i].delete();
				}
			}
		}
		catch (CoreException e) {
		}
	}

	/**
	 * Modify an existing connection profile
	 * 
	 * @param profile
	 * @throws ConnectionProfileException
	 */
	public void modifyProfile(IConnectionProfile profile)
			throws ConnectionProfileException {
		IConnectionProfileRepository repo = getRepositoryForProfile(profile);
		if (repo == null) {
			modifyProfile(profile, null, null, null);
		}
		else {
			repo.modifyProfile(profile, null, null, null);
		}
	}

	/**
	 * Modify an existing connection profile, plus it's name and description We
	 * don't expose a setName and setDescription in IConnectionProfile, so
	 * instead we expose this api for that same purpose.
	 * 
	 * @param profile
	 * @throws ConnectionProfileException
	 */
	public void modifyProfile(IConnectionProfile profile, String newName,
			String newDesc) throws ConnectionProfileException {
		IConnectionProfileRepository repo = getRepositoryForProfile(profile);
		if (repo == null) {
			modifyProfile(profile, newName, newDesc, null);
		}
		else {
			repo.modifyProfile(profile, newName, newDesc, null);
		}
	}

	/**
	 * Modify an existing connection profile, plus it's name and description We
	 * don't expose a setName and setDescription in IConnectionProfile, so
	 * instead we expose this api for that same purpose.
	 * 
	 * @param profile
	 * @throws ConnectionProfileException
	 */
	public void modifyProfile(IConnectionProfile profile, String newName,
			String newDesc, Boolean autoConnect)
			throws ConnectionProfileException {
		IConnectionProfileRepository repo = getRepositoryForProfile(profile);
		if (repo != null) {
			repo.modifyProfile(profile, newName, newDesc, autoConnect);
			return;
		}

		IConnectionProfile[] cps = getProfiles(false);
		boolean found = false;
		boolean foundnew = false;
		int index = 0;
		for (int i = 0; i < cps.length; i++) {
			if (cps[i].getName().equals(profile.getName())) {
				found = true;
				index = i;
			}
			if (cps[i].getName().equals(newName)) {
				foundnew = true;
			}
		}

		if (!found)
			throw new ConnectionProfileException(ConnectivityPlugin
					.getDefault().getResourceString("profile.notexist", //$NON-NLS-1$
							new Object[] { profile.getName()}));

		if (foundnew && !profile.getName().equals(newName))
			throw new ConnectionProfileException(ConnectivityPlugin
					.getDefault().getResourceString("profile.duplicate", //$NON-NLS-1$
							new Object[] { newName}));

		ConnectionProfile internalProfile = (ConnectionProfile) profile;
		String oldName = profile.getName();
		String oldDesc = profile.getDescription();
		Boolean oldAutoConnect = new Boolean(profile.isAutoConnect());
		if (newName != null && !newName.equals(oldName))
			internalProfile.setName(newName);
		if (newDesc != null && !newDesc.equals(oldDesc))
			internalProfile.setDescription(newDesc);
		if (autoConnect != null && !autoConnect.equals(oldAutoConnect)) {
			internalProfile.setAutoConnect(autoConnect.booleanValue());
			if (autoConnect.booleanValue()
					&& internalProfile.getConnectionState() == IConnectionProfile.DISCONNECTED_STATE) {
				internalProfile.connect(null);
			}
		}
		cps[index] = profile;

		mIsDirty = true;

		fireProfileChanged(profile, oldName, oldDesc, oldAutoConnect);
		saveChanges();
	}

	/**
	 * It's called during plugin unloading process, not intended for client use.
	 */
	public void saveChanges() {
		if (mIsDirty) {
			try {
				ConnectionProfileMgmt.saveCPs(getProfiles(false));

				// backup to default backup file
				backupProfilesData(null);
				
				setDirty(false);
			}
			catch (Exception e) {
				ConnectivityPlugin.getDefault().log(e);
			}
		}
	}

	/**
	 * Register a listener for profile operation
	 * 
	 * @param listener
	 */
	public void addProfileListener(IProfileListener listener) {
		mProfileListeners.add(listener);
	}

	/**
	 * Remove the listener for profile operation
	 * 
	 * @param listener
	 */
	public void removeProfileListener(IProfileListener listener) {
		mProfileListeners.remove(listener);
	}

	private synchronized void loadProfiles() {
		if (mProfiles != null )
		{
			return;
		}
		File serverFile = ConnectivityPlugin.getDefault().getStateLocation()
				.append(ConnectionProfileMgmt.FILENAME).toFile();
		File defaultFile = null;

		try {
			URL url = ConnectivityPlugin.getDefault().getBundle().getEntry(
					ConnectionProfileMgmt.DEFAULTCP_FILENAME);
			if (url != null) {
				defaultFile = new File(FileLocator.toFileURL(url).getFile());
			}
		}
		catch (IOException e) {
			ConnectivityPlugin.getDefault().log(e);
		}

		IConnectionProfile[] scps;
		IConnectionProfile[] dcps;

		// see if the default server file exists or we made a backup
		if (serverFile.exists() || backupFileExists()) {
			try {
				
				// If we crashed and the main file is gone, restore the backup
				if (!serverFile.exists() && backupFileExists()) {
					restoreFromBackupProfilesData();
				}
				
				// Save a checkpoint in case of a crash
				backupProfilesData(serverFile);
				
				scps = ConnectionProfileMgmt.loadCPs(serverFile);
			}
			catch (Exception e) {
				ConnectivityPlugin.getDefault().log(e);
				scps = new IConnectionProfile[0];
			}
		}
		else {
			scps = new IConnectionProfile[0];
		}
		if (defaultFile != null && defaultFile.exists()
				&& defaultFile.lastModified() > serverFile.lastModified()) {
			try {

				dcps = ConnectionProfileMgmt.loadCPs(defaultFile);
				
				// save a checkpoint, which automatically creates
				// a backup
				saveChanges();
				
			}
			catch (Exception e) {
				ConnectivityPlugin.getDefault().log(e);
				dcps = new IConnectionProfile[0];
			}
		}
		else {
			dcps = new IConnectionProfile[0];
		}

		Map nameToProfileMap = new HashMap(scps.length + dcps.length);
		for (int i = 0; i < dcps.length; ++i) {
			if (dcps[i].getName() != null) {
				nameToProfileMap.put(dcps[i].getName(), dcps[i]);
			}
		}

		List defaultProfiles = new ArrayList(nameToProfileMap.values());
		for (int i = 0; i < scps.length; ++i) {
			if (scps[i].getName() != null) {
				// Don't need to worry if it already exists.
				// We don't want to use the default if the user has a
				// profile with this name.
				Object replacedProfile = nameToProfileMap.put(
						scps[i].getName(), scps[i]);
				if (replacedProfile != null) {
					defaultProfiles.remove(replacedProfile);
				}
			}
		}

		
		if (loadLocal) {
			loadLocal = false;
			IConnectionProfile localConnProfile = loadLocalRegisteredDatabases();
			if (localConnProfile != null) {
				nameToProfileMap.put(localConnProfile.getName(), localConnProfile);
			
			}
		}
		
		for (Iterator it = nameToProfileMap.values().iterator(); it.hasNext(); ) {
			ConnectionProfile profile = (ConnectionProfile)it.next();

			// Have any profiles migrate themselves from older versions.
			profile.migrate();

			profile.addPropertySetListener(mPropertySetListener);
		}
		
		
		mProfiles = (IConnectionProfile[]) nameToProfileMap.values().toArray(
				new IConnectionProfile[nameToProfileMap.size()]);

		for (Iterator it = defaultProfiles.iterator(); it.hasNext();) {
			fireProfileAdded((IConnectionProfile) it.next());
		}

		autoConnectProfiles();
		addProfileListener(mProfileChangeListener);
	}
	
	private void backupProfilesData ( File ioFile ) {
		if (ioFile != null && ioFile.exists()) {
			File backupFile = ConnectivityPlugin.getDefault().getStateLocation()
				.append(ConnectionProfileMgmt.BACKUP_FILENAME).toFile();
			try {
				copy(ioFile, backupFile);
			} catch (IOException e) {
				ConnectivityPlugin.getDefault().log(e);
			}
		}
		else {
			File serverFile = ConnectivityPlugin.getDefault().getStateLocation()
				.append(ConnectionProfileMgmt.FILENAME).toFile();
			backupProfilesData(serverFile);
		}
	}
	
    private void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
    
        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

	private void restoreFromBackupProfilesData() {
		File serverFile = ConnectivityPlugin.getDefault().getStateLocation()
			.append(ConnectionProfileMgmt.FILENAME).toFile();
		if (!serverFile.exists()) {
			File backupFile = ConnectivityPlugin.getDefault().getStateLocation()
				.append(ConnectionProfileMgmt.BACKUP_FILENAME).toFile();
			try {
				copy(backupFile, serverFile);
			} catch (IOException e) {
				ConnectivityPlugin.getDefault().log(e);
			}
		}
	}
	
	private boolean backupFileExists() {
		File backupFile = ConnectivityPlugin.getDefault().getStateLocation()
			.append(ConnectionProfileMgmt.BACKUP_FILENAME).toFile();
		return backupFile.exists();
	}
	
	public void fireProfileAdded(IConnectionProfile profile) {
		Object[] ls = mProfileListeners.getListeners();
		for (int i = 0; i < ls.length; ++i) {
			((IProfileListener) ls[i]).profileAdded(profile);
		}
	}

	public void fireProfileDeleted(IConnectionProfile profile) {
		Object[] ls = mProfileListeners.getListeners();
		for (int i = 0; i < ls.length; ++i) {
			((IProfileListener) ls[i]).profileDeleted(profile);
		}
	}

	public void fireProfileChanged(IConnectionProfile profile, String oldName,
			String oldDesc, Boolean oldAutoConnect) {
		Object[] ls = mProfileListeners.getListeners();
		for (int i = 0; i < ls.length; ++i) {
			if (ls[i] instanceof IProfileListener1) {
				((IProfileListener1) ls[i]).profileChanged(profile, oldName,
						oldDesc, oldAutoConnect);
			}
			else {
				((IProfileListener) ls[i]).profileChanged(profile);
			}
		}
	}

	private void autoConnectProfiles() {
		for (int index = 0, count = mProfiles.length; index < count; ++index) {
			if (mProfiles[index].isAutoConnect()) {
				mProfiles[index].connect(null);
			}
		}
	}

	/* package */void dispose() {
		if (mProfiles == null) {
			return;
		}
		mProfileListeners.clear();
		saveChanges();
		for (int index = 0, count = mProfiles.length; index < count; ++index) {
			((ConnectionProfile) mProfiles[index]).dispose();
		}
		mManager = null;
	}

	/**
	 * If dirty attribute is set to true, then the cached profiles will be
	 * persisted.
	 * 
	 * @param isDirty
	 */
	public void setDirty(boolean isDirty) {
		mIsDirty = isDirty;
	}
	
	/**
	 * Add a repository to the cache
	 * @param repository
	 */
	public void addRepository(IConnectionProfileRepository repository) {

		checkDuplicatedRepository(repository);

		mRepositories.add(repository);
	}

	/**
	 * Since every time the client requests a connection from a repository, a
	 * new repository instance is generated and in turns calls addRepository()
	 * method to register itself into the list of the ProfileManager. However,
	 * this would bring about dozens of duplicated instance of repositories and possibly
	 * with different phases(e.g. after user modify the configuration and ping
	 * again). The following method was mainly added to prevent such cases.
	 * 
	 * @param repository
	 */
	private void checkDuplicatedRepository(
			IConnectionProfileRepository repository) {
		
		Collection cachedRepositoryList = new HashSet();
		cachedRepositoryList.addAll(mRepositories);
		for (Iterator itr = cachedRepositoryList.iterator(); itr.hasNext();) {
			IConnectionProfileRepository eRepository = (IConnectionProfileRepository) itr
					.next();
			if (eRepository.equals(repository)) {
				mRepositories.remove(eRepository);
			}
		}
	}

	/**
	 * Remove a repository from the cache
	 * @param repository
	 */
	public void removeRepository(IConnectionProfileRepository repository) {
		mRepositories.remove(repository);
	}
	
	/**
	 * Return the repository for a given profile
	 * @param profile
	 * @return
	 */
	public IConnectionProfileRepository getRepositoryForProfile(IConnectionProfile profile) {
		IConnectionProfile parentProfile = profile.getParentProfile();
		if (parentProfile != null) {
			IManagedConnection imc = parentProfile.getManagedConnection(IConnectionProfileRepository.class.getName());
			if (imc != null && imc.getConnection() != null) {
				return (IConnectionProfileRepository)imc.getConnection().getRawConnection();
			}
		}
		return null;
	}
	

	/**
	 * Will load the locally registered databases
	 * @return IConnectionProfile
	 */

	public IConnectionProfile loadLocalRegisteredDatabases()
	{

		IConnectionProfile newProfile = null;
		IExtensionRegistry pluginRegistry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = pluginRegistry.getExtensionPoint("org.eclipse.datatools.connectivity.ProfileManagerInitializationProvider"); //$NON-NLS-1$
		IExtension[] extensions = extensionPoint.getExtensions();
		for (int i = 0; i < extensions.length; ++i)
		{
			IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
			for (int j = 0; j < configElements.length; ++j)
			{
				if (configElements[j].getName().equals("connection")) //$NON-NLS-1$
				{
				
					String jarList = configElements[j].getAttribute("jarList"); //$NON-NLS-1$
					String connectionProfileName = configElements[j].getAttribute("connectionProfileName"); //$NON-NLS-1$
					String connectionProfileID = configElements[j].getAttribute("connectionProfileID"); //$NON-NLS-1$
					String driverDefinitionName = configElements[j].getAttribute("driverDefinitionName"); //$NON-NLS-1$
					String driverTemplateID = configElements[j].getAttribute("driverTemplateID"); //$NON-NLS-1$
					String initializationClass = configElements[j].getAttribute("connectionInitializer"); //$NON-NLS-1$
				
					if (initializationClass != null)
					{
						initializeLocalDatabase(configElements[j], initializationClass);
					}
					newProfile = enableLocalDatabase(configElements[j].getChildren("property"), jarList, connectionProfileName, connectionProfileID, driverDefinitionName, driverTemplateID); //$NON-NLS-1$
				}
			}
		}
		return newProfile;
	}
	
	
	/**
	 * Initialize the local database
	 * @param config
	 * @param initializationClass
	 */
	private void initializeLocalDatabase (IConfigurationElement config, String initializationClass)
	{
		try
		{
			config.createExecutableExtension("connectionInitializer"); //$NON-NLS-1$
		}
		catch (CoreException e)
		{
			ConnectivityPlugin.getDefault().log(e);
		}
	}
	

	/**
	 * Enable the local database - set up the properties and create the ConnectionProfile
	 * @param configElements
	 * @param jarList
	 * @param connProfileName
	 * @param connProfileID
	 * @param driverDefName
	 * @param driverTemplateID
	 * @return returns newly created IConnectionProfile
	 */
	private IConnectionProfile enableLocalDatabase(IConfigurationElement[] configElements, String jarList, String connProfileName, String connProfileID,
			String driverDefName, String driverTemplateID)
	{
		
		    IConnectionProfile newProfile = null;
			if (isBundleActivated (configElements[0])) {
			 
				DriverInstance driverInstance = getDriverInstance(driverDefName, driverTemplateID, jarList);

				if (driverInstance != null) {
				
					Properties connectionProfileProperties = driverInstance
						.getPropertySet().getBaseProperties();
					connectionProfileProperties.setProperty(
						ConnectionProfileConstants.PROP_DRIVER_DEFINITION_ID,
						driverInstance.getId());
					List propList = new ArrayList();

					for (int i = 0, n = configElements.length; i < n; i++)
					{

						String propertyID = configElements[i].getAttribute("id"); //$NON-NLS-1$
						propList.add(propertyID);
						String propertyValue = configElements[i].getAttribute("value"); //$NON-NLS-1$
						propertyValue = substituteLocationDirectory(propertyValue, configElements[i]);
						connectionProfileProperties.setProperty(propertyID, propertyValue);
					}
					newProfile = getProfileInstance(connProfileName, connProfileID, connectionProfileProperties, propList);
			    
				}
			}
			return newProfile;

	}
	
	
	private boolean isBundleActivated (IConfigurationElement element)
	{
	    try
        {
            Bundle bundle = Platform.getBundle(element.getDeclaringExtension().getNamespace());
            if (bundle.getState() == Bundle.RESOLVED)
            {
                bundle.start();
            }
            return bundle.getState() == Bundle.ACTIVE;
        }
        catch (BundleException e)
        {
			ConnectivityPlugin.getDefault().log(e);
        }
        return false;
	}
	
	
	private String substituteLocationDirectory (String logicalPath, IConfigurationElement element)
	{
	    String stateLocation = Platform.getStateLocation(Platform.getBundle(element.getDeclaringExtension().getNamespace())).toOSString();
		int index = logicalPath.indexOf(PLUGIN_STATE_LOCATION);
		if (index != -1)
		{
		    logicalPath = logicalPath.substring(0, index) + stateLocation + logicalPath.substring(index + PLUGIN_STATE_LOCATION.length());
		}
		return logicalPath;
	}
	
	/**
	 * Get the driver instance for this connection.  If a driver already exists with the associated properties, don't create a new one.
	 * If a driver with the same name, but different properties exists, modify the name and create a new one.
	 * @param inName
	 * @param driverTemplateID
	 * @param jarList
	 * @return DriverInstance
	 */
	private DriverInstance getDriverInstance(String inName, String driverTemplateID, String jarList) {

		String defName = inName;
		DriverInstance driverInstance = DriverManager.getInstance().getDriverInstanceByName(inName);
		if (driverInstance != null) {
			if ((driverInstance.getTemplate().getId().equals(driverTemplateID)) && (driverInstance.getJarList().equals(jarList))) {
					return driverInstance;
			} else {
				defName = determineUniqueDriverName(defName);
				
			}
		}
		return DriverManager.getInstance()
			.createNewDriverInstance(
				driverTemplateID, 								
				defName, jarList);
	}
	
	/**
	 * Determine a unique name for the new driver
	 * @param inName
	 * @return String driverName
	 */
	private String determineUniqueDriverName(String inName) {
		int index = 1;
		String testName = inName + String.valueOf(index);
		while (DriverManager.getInstance().getDriverInstanceByName(testName) != null) {
			index++;
			testName = inName + String.valueOf(index);
		}
		return testName;
	}
	
	
	/**
	 * Get the connection profile for this connection.  If it already exists, use the existing connection;  else, create a new one
	 * @param profileName
	 * @param profileID
	 * @param connProperties
	 * @param propList
	 * @return IConnectionProfile
	 */
	private IConnectionProfile getProfileInstance(String profileName, String profileID, Properties connProperties, List propList) {

		String profName = profileName;
		IConnectionProfile connProfile = ProfileManager.getInstance().getProfileByName(profileName);
		if (connProfile != null) {
			Properties props = connProfile.getBaseProperties();
			if (connProfile.getProviderId().equals(profileID)) {
				boolean match = true;
				for (int i = 0; i < propList.size(); i++) {
					String propName = (String)propList.get(i);
					if (!connProperties.get(propName).equals(props.get(propName))) {
						match = false;
					}
				}
				    if (match) {
				    	return connProfile;
				    }
			} else {
				profName = determineUniqueProfileName(profName);
				
			}
		}
		try {
			ProfileManager
				.getInstance()
				.createProfile(
						profName,
						"", //$NON-NLS-1$
						profileID,
						connProperties, "", false); //$NON-NLS-1$
		
				connProfile = ProfileManager.getInstance().getProfileByName(profName);
		} catch (ConnectionProfileException e) {
			ConnectivityPlugin.getDefault().log(e);
		}
		return connProfile;
	}

	/**
	 * Determine unique connection profile name for the new connection
	 * @param inName
	 * @return String profile name
	 */
	private String determineUniqueProfileName(String inName) {
		int index = 1;
		String testName = inName + String.valueOf(index);
		while (ProfileManager.getInstance().getProfileByName(testName) != null) {
			index++;
			testName = inName + String.valueOf(index);
		}
		return testName;
	}


}