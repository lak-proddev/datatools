//
//Confidential property of Sybase, Inc.
//(c) Copyright Sybase, Inc. 2004.
//All rights reserved
//
package org.eclipse.datatools.enablement.sybase;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

/**
 * Utility class which helps managing messages
 */
public class Messages extends NLS{
	private static final String RESOURCE_BUNDLE= "org.eclipse.datatools.enablement.sybase.messages";//$NON-NLS-1$
	private static ResourceBundle bundle = ResourceBundle.getBundle(RESOURCE_BUNDLE);
	
/**
 * Constructor
 */
private Messages(){
	// prevent instantiation of class
}
/**
 * Returns the formatted message for the given key in
 * the resource bundle. 
 *
 * @param key the resource name
 * @param args the message arguments
 * @return the string
 */	
public static String format(String key, Object[] args) {
	return MessageFormat.format(getString(key),args);
}
/**
 * Returns the resource object with the given key in
 * the resource bundle. If there isn't any value under
 * the given key, the key is returned.
 *
 * @param key the resource name
 * @return the string
 */	
public static String getString(String key) {
	try {
		return bundle.getString(key);
	} catch (MissingResourceException e) {
		return key;
	}
}
/**
 * Returns the resource object with the given key in
 * the resource bundle. If there isn't any value under
 * the given key, the default value is returned.
 *
 * @param key the resource name
 * @param def the default value
 * @return the string
 */	
public static String getString(String key, String def) {
	try {
		return bundle.getString(key);
	} catch (MissingResourceException e) {
		return def;
	}
}
	
	public static String plugin_internal_error;
    public static String PrimaryKey_folder_name;
    public static String UniqueConstraint_folder_name;
    public static String CheckConstraint_folder_name;
    public static String ForeignKey_folder_name;
    public static String Parameters_folder_name;
}
