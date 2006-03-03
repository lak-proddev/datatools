/*******************************************************************************
 * Copyright (c) 2004-2005 Sybase, Inc.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: rcernich, shongxum - initial API and implementation
 ******************************************************************************/
package org.eclipse.datatools.connectivity.internal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.datatools.connectivity.ICategory;
import org.eclipse.datatools.connectivity.IConfigurationType;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.IConnectionProfileProvider;
import org.eclipse.datatools.connectivity.IWizardCategoryProvider;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.wizard.IWizard;

/**
 * @author rcernich, shongxum
 * 
 * Created on Jan 15, 2004
 */
public class ConnectionProfileManager {

	private static ConnectionProfileManager sInstance = new ConnectionProfileManager();

	public static final String EXTENSION_ID = "org.eclipse.datatools.connectivity.connectionProfile"; //$NON-NLS-1$

	public static final String EXT_ELEM_CATEGORY = "category"; //$NON-NLS-1$

	public static final String EXT_ELEM_CONFIGURATION_TYPE = "configurationType"; //$NON-NLS-1$

	public static final String EXT_ELEM_CONNECTION_FACTORY = "connectionFactory"; //$NON-NLS-1$

	public static final String EXT_ELEM_PROFILE_EXTENSION = "profileExtension"; //$NON-NLS-1$

	public static final String EXT_ELEM_CONNECTION_PROFILE = "connectionProfile"; //$NON-NLS-1$

	public static final String EXT_ELEM_NEW_WIZARD = "newWizard"; //$NON-NLS-1$

	public static final String EXT_ELEM_WIZARD_CATEGORY = "wizardCategory"; //$NON-NLS-1$

	private Map mProviders = null; // mProviders shouldn't be null after
	// parsing

	private Map mCategories = null;

	private Map mConfigurationTypes = null;

	private Map mNewWizards = null;

	private Map mWizardCategories = null;

	public static ConnectionProfileManager getInstance() {
		return sInstance;
	}

	private ConnectionProfileManager() {
		super();
	}

	public Map getProviders() {
		if (mProviders == null)
			processExtensions();
		return mProviders;
	}

	public IConnectionProfileProvider getProvider(String id) {
		return (IConnectionProfileProvider) getProviders().get(id);
	}

	public Map getCategories() {
		if (mProviders == null)
			processExtensions();
		return mCategories;
	}

	public ICategory getCategory(String id) {
		return (ICategory) getCategories().get(id);
	}

	public Map getWizardCategories() {
		if (mProviders == null)
			processExtensions();
		return mWizardCategories;
	}

	public IWizardCategoryProvider getWizardCategory(String id) {
		return (IWizardCategoryProvider) getWizardCategories().get(id);
	}

	public Map getNewWizards() {
		if (mProviders == null)
			processExtensions();
		return mNewWizards;
	}

	public IWizard getNewWizard(String id) {
		Object profileWizard = 
			(ProfileWizardProvider) getNewWizards().get(id);
		if (profileWizard == null)
			return null;
		return ((ProfileWizardProvider) getNewWizards().get(id)).getWizard();
	}

	public IConfigurationType getConfigurationType(String id) {
		if (mProviders == null)
			processExtensions();
		return (IConfigurationType) mConfigurationTypes.get(id);
	}

	public List getConnectionProfilesByCategory(ICategory category,
			boolean recurse, IProject project) {
		return null;
	}

	public List getConnectionProfilesByConfigurationType(
			IConfigurationType type, IProject project) {
		return null;
	}

	public IConnectionProfile getConnectionProfile(IResource resource) {
		return null;
	}

	private void processExtensions() {
		mProviders = new HashMap();
		mCategories = new HashMap();
		mConfigurationTypes = new HashMap();
		mNewWizards = new HashMap();
		mWizardCategories = new HashMap();

		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint exp = registry.getExtensionPoint(EXTENSION_ID);
		IExtension[] exts = exp.getExtensions();
		List extExts = new Vector(exts.length);
		for (Iterator xit = Arrays.asList(exts).iterator(); xit.hasNext();) {
			IExtension ext = (IExtension) xit.next();
			IConfigurationElement[] elems = ext.getConfigurationElements();
			for (Iterator eit = Arrays.asList(elems).iterator(); eit.hasNext();) {
				IConfigurationElement elem = (IConfigurationElement) eit.next();
				String elemName = elem.getName();
				if (EXT_ELEM_CONNECTION_PROFILE.equals(elemName)) {
					processConnectionProfile(elem);
				}
				else if (EXT_ELEM_CATEGORY.equals(elemName)) {
					processCategory(elem);
				}
				else if (EXT_ELEM_CONFIGURATION_TYPE.equals(elemName)) {
					processConfigurationType(elem);
				}
				else if (EXT_ELEM_NEW_WIZARD.equals(elemName)) {
					processNewWizard(elem);
				}
				else if (EXT_ELEM_WIZARD_CATEGORY.equals(elemName)) {
					processWizardCategory(elem);
				}
				else {
					extExts.add(elem);
				}
			}
		}

		for (Iterator eit = extExts.iterator(); eit.hasNext();) {
			IConfigurationElement elem = (IConfigurationElement) eit.next();
			String elemName = elem.getName();
			if (EXT_ELEM_CONNECTION_FACTORY.equals(elemName)) {
				ConnectionProfileProvider p = (ConnectionProfileProvider) mProviders
						.get(elem
								.getAttribute(ConnectionFactoryProvider.ATTR_PROFILE));
				Assert.isTrue(p != null, ConnectivityPlugin.getDefault()
						.getResourceString("assert.invalid.profile", //$NON-NLS-1$
								new Object[] { elem.toString()}));
				p.addConnectionFactory(elem);
			}
			else if (EXT_ELEM_PROFILE_EXTENSION.equals(elemName)) {
				ConnectionProfileProvider p = (ConnectionProfileProvider) mProviders
						.get(elem
								.getAttribute(ProfileExtensionProvider.ATTR_PROFILE));
				Assert.isTrue(p != null, ConnectivityPlugin.getDefault()
						.getResourceString("assert.invalid.profile", //$NON-NLS-1$
								new Object[] { elem.toString()}));
				p.addProfileExtension(elem);
			}
		}
	}

	private void processConnectionProfile(IConfigurationElement element) {
		ConnectionProfileProvider p = new ConnectionProfileProvider(element);
		Assert.isTrue(!mProviders.containsKey(p.getId()), ConnectivityPlugin
				.getDefault().getResourceString("assert.invalid.profile", //$NON-NLS-1$
						new Object[] { element.toString()}));
		mProviders.put(p.getId(), p);
	}

	private void processCategory(IConfigurationElement element) {
		CategoryProvider c = new CategoryProvider(element);
		if (!mCategories.containsKey(c.getId()))
			mCategories.put(c.getId(), c);
	}

	private void processConfigurationType(IConfigurationElement element) {
		ConfigurationTypeProvider c = new ConfigurationTypeProvider(element);
		Assert.isTrue(!mConfigurationTypes.containsKey(c.getId()),
				ConnectivityPlugin.getDefault().getResourceString(
						"assert.invalid.profile", new Object[] { element //$NON-NLS-1$
								.toString()}));
		mConfigurationTypes.put(c.getId(), c);
	}

	private void processNewWizard(IConfigurationElement element) {
		ProfileWizardProvider c = new ProfileWizardProvider(element);
		Assert.isTrue(!mNewWizards.containsKey(c.getId()), ConnectivityPlugin
				.getDefault().getResourceString(
						"assert.invalid.profile", new Object[] { element //$NON-NLS-1$
								.toString()}));
		mNewWizards.put(c.getId(), c);
	}

	private void processWizardCategory(IConfigurationElement element) {
		WizardCategoryProvider c = new WizardCategoryProvider(element);
		Assert.isTrue(!mWizardCategories.containsKey(c.getId()),
				ConnectivityPlugin.getDefault().getResourceString(
						"assert.invalid.profile", new Object[] { element //$NON-NLS-1$
								.toString()}));
		mWizardCategories.put(c.getId(), c);
	}
}