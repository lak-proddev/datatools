/*******************************************************************************
 * Copyright (c) 2008 Sybase, Inc.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sybase - initial API and implementation
 ******************************************************************************/
package org.eclipse.datatools.enablement.sybase.asa.ddl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.datatools.connectivity.sqm.core.definition.DataModelElementFactory;
import org.eclipse.datatools.connectivity.sqm.core.definition.DatabaseDefinition;
import org.eclipse.datatools.modelbase.sql.expressions.QueryExpression;
import org.eclipse.datatools.modelbase.sql.expressions.QueryExpressionDefault;
import org.eclipse.datatools.modelbase.sql.expressions.SQLExpressionsPackage;
import org.eclipse.datatools.modelbase.sql.tables.ViewTable;

public class SybaseASADdlParser {

	public SybaseASADdlParser(DatabaseDefinition def){
		this.def = def;
	}

	public void parseView (ViewTable view,String viewText){
    	DataModelElementFactory factory = this.def.getDataModelElementFactory();

    	String body = viewText;
    	Pattern pattern = Pattern.compile(".*[\\s]+?AS[\\s]+?([(\\s]*SELECT.*)",Pattern.CASE_INSENSITIVE|Pattern.DOTALL); //$NON-NLS-1$
    	Matcher matcher = pattern.matcher(viewText);
    	if (matcher.matches()) {
    		body = matcher.group(1);
    		pattern = Pattern.compile("(.*)[ \t]+?WITH[ \t]+?.*",Pattern.CASE_INSENSITIVE|Pattern.DOTALL); //$NON-NLS-1$
    		matcher = pattern.matcher(body);
    		if (matcher.matches()) {
    			body = matcher.group(1).trim();
    		}
    	}

    	QueryExpression queryExpression = (QueryExpression) factory.create(SQLExpressionsPackage.eINSTANCE.getQueryExpressionDefault());
		((QueryExpressionDefault)queryExpression).setSQL(body);
		view.setQueryExpression(queryExpression);
    }
	
	private DatabaseDefinition def;
}