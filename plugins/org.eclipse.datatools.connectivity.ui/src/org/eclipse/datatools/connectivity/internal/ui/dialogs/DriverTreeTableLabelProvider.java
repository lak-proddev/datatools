/*******************************************************************************
 * Copyright (c) 2008 Sybase, Inc.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: brianf - initial API and implementation
 ******************************************************************************/
package org.eclipse.datatools.connectivity.internal.ui.dialogs;

import org.eclipse.datatools.connectivity.drivers.jdbc.IJDBCDriverDefinitionConstants;
import org.eclipse.datatools.connectivity.drivers.models.CategoryDescriptor;
import org.eclipse.datatools.connectivity.drivers.models.TemplateDescriptor;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class DriverTreeTableLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof TemplateDescriptor) {
			TemplateDescriptor td = (TemplateDescriptor) element;

			String name = td.getName();
			String vendor = 
				td.getPropertyValueFromId(IJDBCDriverDefinitionConstants.DATABASE_VENDOR_PROP_ID);
			String version = 
				td.getPropertyValueFromId(IJDBCDriverDefinitionConstants.DATABASE_VERSION_PROP_ID);
			
			if (vendor == null)
				vendor = ""; //$NON-NLS-1$
			if (version == null)
				version = ""; //$NON-NLS-1$
			switch (columnIndex) {
			case 0:
				return name;
			case 1: 
				return vendor;
			case 2:
				return version;
			default:
				return ""; //$NON-NLS-1$
			}
		}
		else if (element instanceof CategoryDescriptor) {
			CategoryDescriptor cd = (CategoryDescriptor) element;
			String name = cd.getName();
			switch (columnIndex) {
				case 0:
					return name;
				default:
					return ""; //$NON-NLS-1$
			}
		}
		return new String();
	}

	public String getText(Object element) {
		if (element instanceof TemplateDescriptor) {
			TemplateDescriptor td = (TemplateDescriptor) element;
			String name = td.getName();
			return name;
		}
		else if (element instanceof CategoryDescriptor) {
			CategoryDescriptor cd = (CategoryDescriptor) element;
			String name = cd.getName();
			return name;
		}
		return super.getText(element);
	}

}
