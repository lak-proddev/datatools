/*******************************************************************************
 * Copyright (c) 2007 Sybase, Inc.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Brian Fitzpatrick - initial API and implementation
 ******************************************************************************/
package org.eclipse.datatools.enablement.hsqldb.catalog;

import org.eclipse.datatools.connectivity.sqm.core.rte.ICatalogObject;
import org.eclipse.datatools.connectivity.sqm.loader.IConnectionFilterProvider;
import org.eclipse.datatools.connectivity.sqm.loader.JDBCSchemaLoader;
import org.eclipse.datatools.modelbase.sql.schema.Schema;

/**
 * @author brianf
 *
 */
public class HSQLDBSchemaLoader extends JDBCSchemaLoader {

	/**
	 * @param catalogObject
	 */
	public HSQLDBSchemaLoader(ICatalogObject catalogObject) {
		super(catalogObject);
	}

	/**
	 * @param catalogObject
	 * @param connectionFilterProvider
	 */
	public HSQLDBSchemaLoader(ICatalogObject catalogObject,
			IConnectionFilterProvider connectionFilterProvider) {
		super(catalogObject, connectionFilterProvider);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.sqm.loader.JDBCSchemaLoader#createSchema()
	 */
	protected Schema createSchema() {
		return new HSQLDBCatalogSchema();
	}

}
