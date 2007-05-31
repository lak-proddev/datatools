
/*******************************************************************************
 * Copyright (c) 2004, 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *******************************************************************************/
package org.eclipse.datatools.connectivity.oda.flatfile;

import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.flatfile.i18n.Messages;
import org.eclipse.datatools.connectivity.oda.flatfile.util.querytextutil.ColumnsInfoUtil;

/**
 * A helper class that helps ResultSetMetaData to parse column information and
 * provide a public access to ResultSet's original column names.
 */

public class ResultSetMetaDataHelper
{
	String savedSelectedColumnsInfoString;
	String[] columnNames;
	String[] columnTypes;
	String[] originalColumnNames;
	String[] columnLabels;
	
	/**
	 * Constructor
	 * 
	 * @param colNames
	 * @param colTypes
	 * @param colLabels
	 * @throws OdaException
	 */
	ResultSetMetaDataHelper( String[] colNames, String[] colTypes, String[] colLabels ) throws OdaException
	{
		if ( colNames == null )
			throw new OdaException( Messages.getString( "common_ARGUMENT_CANNOT_BE_NULL" ) ); //$NON-NLS-1$

		this.columnNames = colNames;
		this.columnTypes = colTypes;
		this.columnLabels = colLabels;
		this.originalColumnNames = colNames;
	}
	
	/**
	 * 
	 * @param savedSelectedColumnsInfoString
	 */
	ResultSetMetaDataHelper( String savedSelectedColumnsInfoString )
	{
		this.savedSelectedColumnsInfoString = savedSelectedColumnsInfoString;
		this.columnNames = ColumnsInfoUtil.getColumnNames( savedSelectedColumnsInfoString );
		this.columnTypes = ColumnsInfoUtil.getColumnTypeNames( savedSelectedColumnsInfoString );
		this.originalColumnNames = ColumnsInfoUtil.getOriginalColumnNames( savedSelectedColumnsInfoString );
		this.columnLabels = this.columnLabels==null? this.columnNames : this.columnLabels;
	}

	/**
	 * 
	 * @return
	 */
	String[] getColumnNames( )
	{
		
		return this.columnNames;
	}

	/**
	 * 
	 * @return
	 */
	String[] getColumnTypes( )
	{
		return this.columnTypes;
	}

	/**
	 * 
	 * @return
	 */
	String[] getColumnLabels( )
	{
		return this.columnLabels;
	}

	/**
	 * Get the orignal column names
	 * @return
	 */
	public String[] getOriginalColumnNames( )
	{
		return this.originalColumnNames;
	}

	/**
	 * Get the original column name of the specified column name
	 * @return
	 */
	public String getOriginalColumnName( String columnName )
	{
		String originName = null;

		for ( int i = 0; i < columnNames.length; i++ )
		{
			if ( columnName.equals( columnNames[i] ) )
			{
				originName = originalColumnNames[i];
			}
		}

		return originName;
	}
	
}
