/*
 * Licensed Materials - Property of IBM
 * com.ibm.db.models.sql.query
 * (C) Copyright IBM Corporation 2002, 2005. All Rights Reserved.
 *
 * Note to U.S. Government Users Restricted Rights:
 *   Use, duplication or disclosure restricted 
 *   by GSA ADP Schedule Contract with IBM Corp.
 *
 * %W%
 * @version %I% %H%
 */
package org.eclipse.datatools.modelbase.sql.query.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.datatools.modelbase.sql.query.ColumnName;
import org.eclipse.datatools.modelbase.sql.query.CursorReference;
import org.eclipse.datatools.modelbase.sql.query.GroupingExpression;
import org.eclipse.datatools.modelbase.sql.query.GroupingSets;
import org.eclipse.datatools.modelbase.sql.query.GroupingSetsElement;
import org.eclipse.datatools.modelbase.sql.query.GroupingSpecification;
import org.eclipse.datatools.modelbase.sql.query.OrderBySpecification;
import org.eclipse.datatools.modelbase.sql.query.OrderByValueExpression;
import org.eclipse.datatools.modelbase.sql.query.Predicate;
import org.eclipse.datatools.modelbase.sql.query.PredicateBasic;
import org.eclipse.datatools.modelbase.sql.query.PredicateBetween;
import org.eclipse.datatools.modelbase.sql.query.PredicateExists;
import org.eclipse.datatools.modelbase.sql.query.PredicateInValueList;
import org.eclipse.datatools.modelbase.sql.query.PredicateInValueRowSelect;
import org.eclipse.datatools.modelbase.sql.query.PredicateInValueSelect;
import org.eclipse.datatools.modelbase.sql.query.PredicateIsNull;
import org.eclipse.datatools.modelbase.sql.query.PredicateLike;
import org.eclipse.datatools.modelbase.sql.query.PredicateQuantifiedRowSelect;
import org.eclipse.datatools.modelbase.sql.query.PredicateQuantifiedValueSelect;
import org.eclipse.datatools.modelbase.sql.query.QueryCombined;
import org.eclipse.datatools.modelbase.sql.query.QueryExpressionBody;
import org.eclipse.datatools.modelbase.sql.query.QueryExpressionRoot;
import org.eclipse.datatools.modelbase.sql.query.QueryResultSpecification;
import org.eclipse.datatools.modelbase.sql.query.QuerySearchCondition;
import org.eclipse.datatools.modelbase.sql.query.QuerySelect;
import org.eclipse.datatools.modelbase.sql.query.QuerySelectStatement;
import org.eclipse.datatools.modelbase.sql.query.QueryStatement;
import org.eclipse.datatools.modelbase.sql.query.QueryUpdateStatement;
import org.eclipse.datatools.modelbase.sql.query.QueryValueExpression;
import org.eclipse.datatools.modelbase.sql.query.QueryValues;
import org.eclipse.datatools.modelbase.sql.query.ResultColumn;
import org.eclipse.datatools.modelbase.sql.query.ResultTableAllColumns;
import org.eclipse.datatools.modelbase.sql.query.SQLQueryFactory;
import org.eclipse.datatools.modelbase.sql.query.SQLQueryObject;
import org.eclipse.datatools.modelbase.sql.query.SearchConditionCombined;
import org.eclipse.datatools.modelbase.sql.query.SearchConditionNested;
import org.eclipse.datatools.modelbase.sql.query.SuperGroup;
import org.eclipse.datatools.modelbase.sql.query.SuperGroupElement;
import org.eclipse.datatools.modelbase.sql.query.TableCorrelation;
import org.eclipse.datatools.modelbase.sql.query.TableExpression;
import org.eclipse.datatools.modelbase.sql.query.TableFunction;
import org.eclipse.datatools.modelbase.sql.query.TableInDatabase;
import org.eclipse.datatools.modelbase.sql.query.TableJoined;
import org.eclipse.datatools.modelbase.sql.query.TableNested;
import org.eclipse.datatools.modelbase.sql.query.TableReference;
import org.eclipse.datatools.modelbase.sql.query.UpdateAssignmentExpression;
import org.eclipse.datatools.modelbase.sql.query.UpdateSourceExprList;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionCaseElse;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionCaseSearch;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionCaseSearchContent;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionCaseSimple;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionCaseSimpleContent;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionCast;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionColumn;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionCombined;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionDefaultValue;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionFunction;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionLabeledDuration;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionNested;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionNullValue;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionScalarSelect;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionSimple;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionVariable;
import org.eclipse.datatools.modelbase.sql.query.ValuesRow;
import org.eclipse.datatools.modelbase.sql.query.WithTableReference;
import org.eclipse.datatools.modelbase.sql.query.WithTableSpecification;
import org.eclipse.datatools.modelbase.sql.query.util.SQLQuerySourceFormat;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.datatools.modelbase.sql.constraints.Constraint;
import org.eclipse.datatools.modelbase.sql.constraints.ForeignKey;
import org.eclipse.datatools.modelbase.sql.constraints.PrimaryKey;
import org.eclipse.datatools.modelbase.sql.datatypes.DataType;
import org.eclipse.datatools.modelbase.sql.expressions.SearchCondition;
import org.eclipse.datatools.modelbase.sql.expressions.ValueExpression;
import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.datatools.modelbase.sql.tables.BaseTable;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.datatools.modelbase.sql.tables.Table;


/**
 * @author nbhatia, ckadner
 *
 * Provides utility functions related to tables
 *
 */
public class TableHelper {
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  /**TODO: make that DELIMITED_IDENTIFIER_QUOTE String flexible with Database info
   * String constant for the quote that are used for delimited identifiers
   * like "Col1" where this delimited identifier should not be modified to or
   * be compared equal to COL1 or col1, value: "\"", the character " */
  public static final String DELIMITED_IDENTIFIER_QUOTE = "\"";
  
  
  //QMP-CK
  /**
   * Returns the <code>ValueExpressionColumn</code> from the given
   * <code>TableReference</code> matching the given <code>columnName</code>.
   * If the given <code>TableReference</code> is of type
   * <code>QueryExpressionBody</code> <code>ResultColumn</code>s of the
   * <code>QuerySelect</code> statement will be regarded too.
   * <p>
   * <b>NOTE:</b>
   * If there is more than one <code>ValueExpressionColumn</code> with the same
   * name (both representing one and the same real <code>Column</code>), the
   * first one found will be returned. 
   * </p>
   *
   * @param tableRef
   *          the TableReference seached for the matching column
   * @param columnName
   *          the String name of the column to be searched
   * @return the found ValueExpressionColumn or <code>null</code>
   */
  static public ValueExpressionColumn getColumnExpressionForName(TableReference tableRef,
      String columnName)
  {
      if (tableRef != null) {
          if (tableRef instanceof TableExpression) {
            TableExpression tableExpr = (TableExpression) tableRef;
            // there has to be a method getColumnExpressionForName(TableExpression, String) !!! or we'll get an endless loop
            return getColumnExpressionForName(tableExpr, columnName);
          }
          if (tableRef instanceof TableNested) {
            TableNested nest = (TableNested) tableRef;
            return getColumnExpressionForName(nest.getNestedTableRef(), columnName);
          }
          if (tableRef instanceof TableJoined) {
            TableJoined tableJoined = (TableJoined) tableRef;
            TableReference leftTableRef = tableJoined.getTableRefLeft();
            TableReference rightTableRef = tableJoined.getTableRefRight();

            ValueExpressionColumn leftColumn = getColumnExpressionForName(leftTableRef, columnName);
            
            if (leftColumn != null) {
              return leftColumn;
            } else {
                ValueExpressionColumn rightColumn = getColumnExpressionForName(rightTableRef, columnName);
                return rightColumn;
            }
          }
        }

        return null;
  }

	//QMP-CK
  /**
   * Returns the <code>true</code> if the given <code>tableExpr</code>
   * is referenced by another <code>ValueExpressionColumn</code>
   * with the same name as the given <code>referencedByColumnName</code>
   * 
   * 
   * @param tableExpr
   *            the TableReference seached for the matching column name reference
   * @param referencedByColumnName
   *            the String name of the ValueExpressionColumn to be searched in the
   * list of column references to the given <code>tableExpr</code>
   * @return <code>true</code>
   *   if another column was found to reference the given <code>tableExpr</code>
   * 
   */
  static public boolean isTableReferencedByColumnWithName(TableExpression tableExpr,
                                                          String referencedByColumnName)
  {
      boolean colExprFound = false;

      // try to find column in the columnList of the
      if (tableExpr != null && referencedByColumnName != null)
      {
          List colRefs = tableExpr.getValueExprColumns();
          for (Iterator colIt = colRefs.iterator(); colIt.hasNext();)
          {
              ValueExpressionColumn colExpr = (ValueExpressionColumn) colIt
                              .next();
              if (referencedByColumnName.equals(colExpr.getName()))
              {
                  colExprFound = true;
                  break;
              }
          }

      }
      return colExprFound;
  }
      

	//QMP-CK
  /**
   * Returns the <code>ValueExpressionColumn</code> from the given
   * <code>TableExpression</code>, if the <code>ValueExpressionColumn</code>'s
   * name matches the given <code>columnName</code>.
   * <p>
   * <b>NOTE: </b> If there is more than one
   * <code>ValueExpressionColumn</code> with the same name (both
   * representing one and the same real <code>Column</code>), the first one
   * found will be returned.
   * </p>
   * 
   * @param tableExpr
   *            the TableReference seached for the matching column name
   * @param columnName
   *            the String name of the ValueExpressionColumn to be searched
   * @return column the found ValueExpressionColumn or <code>null</code>
   */
  static public ValueExpressionColumn getColumnExpressionForName(TableExpression tableExpr,
                                                                 String columnName)
  {
      ValueExpressionColumn colExprFound = null;

      // try to find column in the columnList of the
      if (tableExpr != null && columnName != null)
      {
          List colList = tableExpr.getColumnList();
          for (Iterator colIt = colList.iterator(); colIt.hasNext();)
          {
              ValueExpressionColumn colExpr =
                  (ValueExpressionColumn) colIt.next();
              if (columnName.equals(colExpr.getName()))
              {
                  colExprFound = colExpr;
                  break;
              }
          }

      }
      return colExprFound;
  }
      


  	//QMP-CK
    /**
     * Returns the <code>ValueExpressionColumn</code> from the given
     * <code>TableExpression</code>, if the <code>ValueExpressionColumn</code>'s
     * name matches the given <code>columnName</code>.
     * If the given <code>TableExpression</code> is of type
     * <code>QueryExpressionBody</code>, this method will recursively analyze
     * its effective result columns, e.g. the <code>ResultColumn</code>s of a
     * nested <code>QuerySelect</code> statement will be considered, too.
     * <p>
     * <b>NOTE: </b> If there is more than one
     * <code>ValueExpressionColumn</code> with the same name (both
     * representing one and the same real <code>Column</code>), the first one
     * found will be returned.
     * </p>
     * 
     * @param tableExpr
     *            the TableReference seached for the matching column name
     * @param columnName
     *            the String name of the ValueExpressionColumn to be searched
     * @return column the found ValueExpressionColumn or <code>null</code>
     */
    static public ValueExpressionColumn getColumnExpressionForNameRecursively(TableExpression tableExpr,
                                                                   String columnName)
    {
        ValueExpressionColumn colExprFound = null;

        // try to find column in the columnList of the
        if (tableExpr != null && columnName != null)
        {
            colExprFound = getColumnExpressionForName(tableExpr, columnName);

        }

        if (colExprFound == null)
        {
            // for queryExprBody consider result spec col list

            if (tableExpr instanceof QueryExpressionBody)
            {
                
                
                QueryExpressionBody tableQuery = (QueryExpressionBody) tableExpr;
                ResultColumn resultColFound = getResultColumnForAliasOrColumnName(tableQuery,columnName);
                
                if (resultColFound != null) {
	                // now we found a ResultColumn in the this subquery
	                // but obviously there is not yet a representing columnExpression
	                // for this TableExpression which is a Subquery, we create one
	                ValueExpressionColumn resultColExpr = SQLQueryFactory.eINSTANCE.createValueExpressionColumn();
	                resultColExpr.setName(columnName);
	                tableQuery.getColumnList().add(resultColExpr);
	                
	                colExprFound = resultColExpr;
                }
                
                
            } 
            else if (tableExpr instanceof WithTableReference)
            {
                WithTableReference withTable = (WithTableReference) tableExpr;
                
                //throw new UnsupportedOperationException(
                StatementHelper.logError(
                                TableHelper.class.getName()+
                                "#getColumnExpressionForName(TableExpression,String) not implemented for TableExpression of type "+
                                withTable.getClass().getName());
            }
            else if (tableExpr instanceof TableFunction)
            {
                TableFunction tableFunc = (TableFunction) tableExpr;
                
                throw new UnsupportedOperationException(TableHelper.class.getName()+"#getColumnExpressionForName(TableExpression,String) not implemented for TableExpression of type "+
                                tableFunc.getClass().getName());
            } 
            else if (tableExpr instanceof TableInDatabase)
            {
                TableInDatabase tableInDB = (TableInDatabase) tableExpr;
                Column dbColumn = getColumnForName(tableInDB, columnName);
                if (dbColumn != null) {
                    colExprFound = getColumnExpressionForColumn(tableInDB, dbColumn);
                }
            }
            else
            {
                throw new UnsupportedOperationException(TableHelper.class.getName()+"#getColumnExpressionForName(TableExpression,String) not implemented for TableExpression of type "+
                                tableExpr.getClass().getName());
            }
        }

        return colExprFound;
    }

  	//QMP-CK
    /**
     * Returns the <code>ResultColumn</code> contained in the
     * <code>selectClause</code> of the given <code>QueryExpressionBody</code>,
     * whose <code>name</code> matches the given <code>columnName</code>.
     * 
     * @param tableQuery
     *            the TableReference seached for the matching column name
     * @param columnName
     *            the String name of the ValueExpressionColumn to be searched
     * @return column the found ValueExpressionColumn or <code>null</code>
     */
    static public ResultColumn getResultColumnForName(QueryExpressionBody tableQuery,
                                                                   String columnName)
    {
        ResultColumn resultColFound = null;

        if (tableQuery != null && columnName != null)
        {
            List resultColList = getResultColumnsOfQueryExpression(tableQuery);
            for (Iterator resultColIt = resultColList.iterator(); resultColIt.hasNext();)
            {
                ResultColumn resultCol = (ResultColumn) resultColIt.next();
                
                // check with regards to delimited identifier
                if (columnName.equals(resultCol.getName()) || 
                                (!columnName.startsWith(DELIMITED_IDENTIFIER_QUOTE) && columnName.equalsIgnoreCase(resultCol.getName())))
                {
                    resultColFound = resultCol;
                    break;
                }
        	}         
        }

        return resultColFound;
    }

  	//QMP-CK
    /**
     * Returns the <code>ResultColumn</code> contained in the
     * <code>selectClause</code> of the given <code>QueryExpressionBody</code>,
     * whose <code>name</code> matches the given <code>columnName</code>
     * or whose referenced <code>ValueExpression</code> is of type
     * <code>ValueExpressionColumn</code> and has a <code>name</code> matching
     * the given <code>columnName</code>.
     * 
     * @param tableQuery
     *            the TableReference seached for the matching column name
     * @param columnName
     *            the String name of the ValueExpressionColumn to be searched
     * @return column the found <code>ResultColumn</code> <code>null</code>
     */
    public static ResultColumn getResultColumnForAliasOrColumnName(QueryExpressionBody tableQuery,
                                                                   String columnName)
    {
        ResultColumn resultColFound = null;

        if (tableQuery != null && columnName != null)
        {
            List resultColList = getResultColumnsOfQueryExpression(tableQuery);
            for (Iterator resultColIt = resultColList.iterator(); resultColIt.hasNext();)
            {
                ResultColumn resultCol = (ResultColumn) resultColIt.next();
                ValueExpression resultExpr = resultCol.getValueExpr();
                
                // check with regards to delimited identifier
                if (columnName.equals(resultCol.getName()) || 
                                (!columnName.startsWith(DELIMITED_IDENTIFIER_QUOTE) && columnName.equalsIgnoreCase(resultCol.getName())))
                {
                    resultColFound = resultCol;
                    break;
                }
                else if (resultExpr != null && resultExpr instanceof ValueExpressionColumn) 
                {
                    ValueExpressionColumn resultColExpr = (ValueExpressionColumn) resultExpr;
                    
                    // check with regards to delimited identifier
                    if (columnName.equals(resultColExpr.getName()) || 
                                    (!columnName.startsWith(DELIMITED_IDENTIFIER_QUOTE) && columnName.equalsIgnoreCase(resultColExpr.getName())))
                    {
                        resultColFound = resultCol;
                        break;
                    }
                    
                }
        	}         
        }

        return resultColFound;
    }

    
  	//QMP-CK
    /**
     * Returns the <code>ResultColumn</code>s of the given
     * <code>QueryExpressionBody queryExpr</code>.
     * <p>
	 * <b>NOTE:</b>
	 * If the given <code>queryExpr</code>'s 
	 * <code>QueryResultSpecification</code> is of type
	 * <code>ResultTableAllColumns</code> ('SELECT * FROM ...') an empty List
	 * will be returned.
	 * </p>
	 * 
     * @param queryExpr
     *            the <code>QueryExpressionBody</code> which's
     * 		<code>ResultColumn</code>s will be returned
     * @return List with the <code>ResultColumn</code>s of the given
     * 		<code>queryExpr</code>
     */
    static public List getResultColumnsOfQueryExpression(QueryExpressionBody queryExpr)
    {
        List resultColList = new ArrayList();

        // try to find column in the columnList of the
        if (queryExpr != null)
        {
            
            if (queryExpr instanceof QuerySelect) 
            {
                QuerySelect select = (QuerySelect) queryExpr;
	            List resultSpecList = select.getSelectClause();
	            
	            for (Iterator resultSpecIt = resultSpecList.iterator(); resultSpecIt.hasNext();)
	            {
	                QueryResultSpecification resultSpec = (QueryResultSpecification) resultSpecIt.next();
	                
	                if (resultSpec instanceof ResultColumn)
                    {
                        ResultColumn resultCol = (ResultColumn) resultSpec;
                        
    	                resultColList.add(resultCol);
                        
                    }

	            }
            } 
            else if (queryExpr instanceof QueryCombined) 
            {
                QueryCombined combinedQ = (QueryCombined) queryExpr;
                
                resultColList.addAll(getResultColumnsOfQueryExpression(combinedQ.getLeftQuery()));
                resultColList.addAll(getResultColumnsOfQueryExpression(combinedQ.getRightQuery()));
            }
            else
            {
                throw new UnsupportedOperationException(TableHelper.class.getName()+"#getColumnExpressionForName(QueryExpressionBody,String) not implemented for TableExpression of type "+
                                queryExpr.getClass().getName());
            }
	
        }


        return resultColList;
    }


  //QMP-NB
  /**
   * Returns the Column matching the columnName from the given TableExpression
   *
   * @param tableExpr
   *          the TableExpression seached for the matching column name
   * @param columnName
   *          the String name of the column to be searched
   * @return column
   * @deprecated use {@link #getColumnForName(TableInDatabase, String)}
   */
  static public Column getColumnForName(TableExpression tableExpr,
      String columnName) {
    if (tableExpr == null) {
        return null;
  	}
  	else if (tableExpr instanceof TableInDatabase) {
      TableInDatabase tableInDB = (TableInDatabase) tableExpr;
      return getColumnForName(tableInDB, columnName);
    }

    // TODO: get Columns in WithTables queries also!!
    else if (tableExpr instanceof WithTableReference) {
        
        // might have no Column, but ValueExpressionColumn!
        
        //WithTableReference withTableRef = (WithTableReference) tableExpr;
        //return getColumnForName(withTableRef, columnName);
    }

    
    return null;
  }

    //QMP-CK
    /**
     * Returns the Column matching the columnName from the given TableInDatabase
     * 
     * @param tableInDB
     *            the TableInDatabase seached for the matching column name
     * @param columnName
     *            the String name of the column to be searched
     * @return column
     */
    static public Column getColumnForName(TableInDatabase tableInDB,
                                          String columnName)
    {
        Column columnFound = null;
        Table table = tableInDB.getDatabaseTable();
        if (table != null && columnName != null)
        {
            List columnList = table.getColumns();
            Iterator columnIter = columnList.iterator();
            while (columnIter.hasNext() && columnFound == null)
            {
                Column column = (Column) columnIter.next();
                if (columnName.equals(column.getName()) 
                                || (!columnName.startsWith(DELIMITED_IDENTIFIER_QUOTE) 
                                                && columnName.equalsIgnoreCase(column.getName()))) 
                {
                    columnFound = column; 
                    break;
                }
            }
        }

        return columnFound;
    }

    //QMP-CK
    /**
     * Returns the Column matching the columnName from the given
     * <code>WithTableReference</code>.
     * 
     * @param withTableRef
     *            the <code>WithTableReference</code> seached in for the
     * 			  matching column name
     * @param columnName
     *            the String name of the column to be searched
     * @return column
     */
    static public Column getColumnForName(WithTableReference withTableRef,
                                          String columnName)
    {
        Column columnFound = null;
        if (withTableRef != null && columnName != null && 
                        withTableRef.getWithTableSpecification() != null)
        {
            WithTableSpecification withTable = 
                withTableRef.getWithTableSpecification();
            
            // columnName matches either one of the declared columns
            //   then find the corresponding valueExpression in the query result
            // or columnName matches one of the implicit result columns of 
            //   the AS query
            
            StatementHelper.logError(TableHelper.class.getName()+
                            "#getColumnForName(WithTableReference,String) is not implemented.");
            //TODO: implement if it makes sense, we needed the datatype but that
            //      could be different from the original column!
        }

        return columnFound;
    }


    
  //QMP-CK
  /**
   * Returns the Column matching the columnName from the given TableReference
   *
   * @param tableRef
   *          the TableReference seached for the matching column name
   * @param columnName
   *          the String name of the column to be searched
   * @return column
   */
  static public Column getColumnForName(TableReference tableRef,
      String columnName) {
    if (tableRef != null) {
      if (tableRef instanceof TableExpression) {
        TableExpression tableExpr = (TableExpression) tableRef;
        return getColumnForName(tableExpr, columnName);
      }
      if (tableRef instanceof TableNested) {
        TableNested nest = (TableNested) tableRef;
        return getColumnForName(nest.getNestedTableRef(), columnName);
      }
      if (tableRef instanceof TableJoined) {
        TableJoined tableJoined = (TableJoined) tableRef;
        TableReference leftTableRef = tableJoined.getTableRefLeft();
        TableReference rightTableRef = tableJoined.getTableRefRight();

        Column leftColumn = getColumnForName(leftTableRef, columnName);
        Column rightColumn = getColumnForName(rightTableRef, columnName);

        if (leftColumn != null) {
          return leftColumn;
        } else {
          return rightColumn;
        }
      }
    }

    return null;
  }

  /**
   * Returns the Column matching the name of the SQLValueExpressionColumn from
   * the given SQLTableExpression
   *
   * @param tableExpr
   *          the SQLTableExpression seached for the matching
   *          SQLValueExpressionColumn
   * @param colExpr
   *          the SQLValueExpressionColumn which needs to be searched
   * @return column
   */
  static public Column getColumnForColumnExpression(TableExpression tableExpr,
      ValueExpressionColumn colExpr) {
    if (colExpr != null) {
      String columnName = colExpr.getName();
      return getColumnForName(tableExpr, columnName);
    } else {
      return null;
    }

  }

  /**
   * Creates and Returns SQLRDBTable for the given Table and initializes its
   * list of SQLValueExpressColumn corresponding to each column of the table.
   *
   * @param table
   *          the Table for which we need a SQLRDBTable
   * @return the SQLRDBTable that is created
   */
  public static TableInDatabase createTableExpressionForTable(Table table) {
    TableInDatabase tableInDB = null;
    if (table != null) {
      List rdbColumnList;
      List cList;
      //SQLQueryFactory factory = new SQLQueryFactoryImpl();
      SQLQueryFactory factory = SQLQueryFactory.eINSTANCE;
      tableInDB = factory.createTableInDatabase();
      tableInDB.setDatabaseTable(table);
      String tableName = table.getName();
      tableInDB.setName( tableName );
      rdbColumnList = tableInDB.getColumnList();
      cList = table.getColumns();
      Iterator columnItr = cList.iterator();
      while (columnItr.hasNext()) {
        Column col = (Column) columnItr.next();
        ValueExpressionColumn valueExprColumn = factory.createValueExpressionColumn();
        valueExprColumn.setName(col.getName());
        valueExprColumn.setDataType(ValueExpressionHelper.copyDataType(col.getDataType()));
        rdbColumnList.add(valueExprColumn);
      }
    }
    return tableInDB;
  }

  /**
   * Populates the list of ValueExpressionColumn in the given table expression
   * using the columns in the given Table object
   *
   * @param tableInDB
   *          the TableInDatabase to be populated
   * @param databaseTable
   *          the Table object used in populating the tableExpression
   */
  public static void populateTableExpressionColumns(TableInDatabase tableInDB,
      Table databaseTable) {
      
    if (databaseTable == null || tableInDB == null) { return; }

    List rdbColumnList = databaseTable.getColumns();
    Iterator colItr = rdbColumnList.iterator();
    List columnList = tableInDB.getColumnList();
    columnList.clear(); // clean out the previously populated columns
    SQLQueryFactory factory = SQLQueryFactory.eINSTANCE;
    while (colItr.hasNext()) {
      Column col = (Column) colItr.next();
      ValueExpressionColumn valueExprColumn = null;
      
      // we don't need that anymore, since we'll have a list of only the exposed
      // columns vs the list of clumn references to the table
//      ValueExpressionColumn valueExprColumn = TableHelper.getColumnExpressionForColumn(tableExpr, col);
//      if (valueExprColumn == null) {
      
        valueExprColumn = factory.createValueExpressionColumn();
        valueExprColumn.setName(col.getName());
        DataType type = ValueExpressionHelper.copyDataType(col.getDataType());
        valueExprColumn.setDataType(type);
        
        // if this tableInDB will be used in a nested query, e.g.
        //  "select t1.col1 as c1 from (select col1 from table1) as t1;"
        // the effective result column of the outer/big select will be "c1"
        // and we want to be able to know "c1" was derived from table1
        // we are setting the TableInDatabase that this column was
        // originally derived from, this reference will be passed on through all
        // levels of nested queries. We should use a reference called
        // "derivedFromTableInDatabase"
        valueExprColumn.setTableInDatabase(tableInDB);

        columnList.add(valueExprColumn);
//      }
    }
  }

  /**
   * Returns true if the given column is part of a primary key constraint.
   *
   * @param column
   *          the Column that needs to check for the primary key constraint
   * @return the Boolean result
   */
  public static boolean isPrimaryKey(Column column) {
    boolean retValue = false;
    Table table = column.getTable();
    List primarykeyCols = getPrimaryKeyColumns(table);
    Iterator pkcolItr = primarykeyCols.iterator();
    while (pkcolItr.hasNext() && retValue == false) {
      if (column == (Column) pkcolItr.next()) {
        retValue = true;
      }
    }
    return retValue;
  }

  /**
   * Returns true if the given ValueExpressionColumn is part of a primary key constraint.
   *
   * @param colExpr
   *          the ValueExpressionColumn that needs to check for the primary key constraint
   * @return the Boolean result
   */
  public static boolean isPrimaryKey(ValueExpressionColumn colExpr) {
    boolean retValue = false;
    if (colExpr != null && colExpr.getTableExpr() instanceof TableInDatabase) {
    	Column column = getColumnForColumnExpression( colExpr.getTableExpr(), colExpr) ;
    	if (column != null) {
    		retValue = isPrimaryKey(column) ;
    	}
    }
    return retValue;
  }
  
  
  /**
   * Gets the SQLValueExpressionColumn corresponding to the given Column, in the
   * given table
   *
   * @param tableExpr
   *          the table to search upon
   * @param column
   *          the column to be looked for
   * @return the SQLValueExpressionColumn corresponding to the given Column , or
   *         null if there is no match
   */
  public static ValueExpressionColumn getColumnExpressionForColumn(
      TableExpression tableExpr, Column column) {
    ValueExpressionColumn colExpr = null;
    if (column != null && tableExpr != null) {
      String colName = column.getName();
      Iterator exprListItr = tableExpr.getColumnList().iterator();
      ValueExpressionColumn tempExpr;
      boolean found = false;
      while (exprListItr.hasNext() && found == false) {
        tempExpr = (ValueExpressionColumn) exprListItr.next();
        if (tempExpr.getName().equals(colName)) {
          colExpr = tempExpr;
          found = true;
        }
      }
    }
    return colExpr;
  }

  /**
   * Returns the list of Columns that are part of the primary key constraint for
   * the given table.
   *
   * @param table
   *          the Table for which list of primary key columns needed
   * @return the list of columns
   */
  public static List getPrimaryKeyColumns(Table table) {
    List primarykeyCols = new ArrayList();
    if (table instanceof BaseTable) {
      List list = ((BaseTable) table).getConstraints();
      Iterator conItr = list.iterator();
      while (conItr.hasNext()) {
        Constraint con = (Constraint) conItr.next();
        if (con instanceof PrimaryKey) {
          primarykeyCols.addAll(((PrimaryKey) con).getMembers());
        }
      }
    }
    return primarykeyCols;
  }

  /**
   * Returns true if the given column is part of Foreign key constraint.
   *
   * @param column
   *          the Column that needs to check for the Foreign key constraint
   * @return the Boolean result
   */
  public static boolean isForeignKey(Column column) {
    boolean retValue = false;
    Table table = column.getTable();
    List fkCols = getForeignKeyColumns(table);
    Iterator fkcolItr = fkCols.iterator();
    while (fkcolItr.hasNext() && retValue == false) {
      if (column == (Column) fkcolItr.next()) {
        retValue = true;
      }
    }
    return retValue;
  }

  /**
   * Returns true if the given ValueExpressionColumn is part of Foreign key constraint.
   *
   * @param colExpr
   *          the ValueExpressionColumn that needs to check for the Foreign key constraint
   * @return the Boolean result
   */
  public static boolean isForeignKey(ValueExpressionColumn colExpr) {
    boolean retValue = false;
    if (colExpr != null && colExpr.getTableExpr() instanceof TableInDatabase) {
    	Column column = getColumnForColumnExpression( colExpr.getTableExpr(), colExpr) ;
    	if (column != null) {
    		retValue = isForeignKey(column) ;
    	}
    }
    return retValue;
  }

  
  /**
   * Returns the list of Columns that are part of the Foreign key constraint for
   * the given table.
   *
   * @param table
   *          the Table for which list of Foreign key columns needed
   * @return the list of columns
   */
  public static List getForeignKeyColumns(Table table) {
    List fkCols = new ArrayList();
    if (table instanceof BaseTable) {
      List list = ((BaseTable) table).getConstraints();
      Iterator conItr = list.iterator();
      while (conItr.hasNext()) {
        Constraint con = (Constraint) conItr.next();
        if (con instanceof ForeignKey) {
          fkCols.addAll(((ForeignKey) con).getMembers());
        }
      }
    }
    return fkCols;
  }

  /**
   * Returns the name or the alias name of the given
   * <code>TableExpression</code> <code>tableExpr</code> depending on, whether
   * or not the <code>tableExpr</code> has a
   * <code>TableCorrelation</code>.
   * 
   * @param tableExpr
   *          the TableExpression for which the exposed name is needed
   * @return the exposed table name
   */
  public static String getExposedTableName(TableExpression tableExpr) {
    String tablename = null;
    if (tableExpr != null) {
	    if (tableExpr.getTableCorrelation() != null) {
	        tablename = tableExpr.getTableCorrelation().getName();
	    } else {
	        tablename = tableExpr.getName();
	    }
    }
    return tablename;
  }

  
  
  /**
   * Returns fully qualified name for the given table.
   *
   * @param table
   *          the Table for which the fully qualified name is needed
   * @return the fully qualified table name
   */
  public static String getFullTableName(Table table) {
    String tablename = table.getName();
    if (table.getSchema() != null) {
      tablename = table.getSchema().getName() + "." + tablename;
    }
    return tablename;
  }

  /**
   * Returns the Table from the given SQLTableExpression if there is one
   * otherwise return null.
   *
   * @param tableExpr
   *          the SQLTableExpression for which the table is needed
   * @return the table
   */
  static public Table getTableForTableExpression(TableExpression tableExpr) {
    Table table = null;
    if (tableExpr != null && (tableExpr instanceof TableInDatabase)) {
      TableInDatabase tableInDB = (TableInDatabase) tableExpr;
      table = tableInDB.getDatabaseTable();
    }

    return table;
  }

  /**
   * Gets the table object from the given list of table references that is
   * associated with (contains) a column with the given table and column name.
   * If the table name is null or blank, look through all the tables for a match
   * on the column name alone.
   *
   * @param aTableName
   *          a table name to use to search for a table reference
   * @param aColName
   *          a column name to use to search for a table reference
   * @param aTableExprList
   *          a list of table references to search
   * @return TableExpression the table expression associated with the column.
   *         Null if not found
   */
  public static TableExpression getTableExpressionForNamedColumn(
      String aTableName, String aColName, List aTableExprList) {
    TableExpression tableExpr = null;
    Column rdbColumn = null;

    // Scan the list of tables.
    boolean foundIt = false;
    Iterator tableExprListIter = aTableExprList.iterator();
    while (tableExprListIter.hasNext() && foundIt == false) {
      // See if we have a match by table name.
      tableExpr = (TableExpression) tableExprListIter.next();
      if (aTableName != null && aTableName.length() > 0) {
        String tableExprName = tableExpr.getName();
        String tableExprCorrName = null;

        if (tableExpr.getTableCorrelation() != null) {
          tableExprCorrName = tableExpr.getTableCorrelation().getName();
        }

        if (aTableName.equals(tableExprName)
            || aTableName.equals(tableExprCorrName)) {
          foundIt = true;
        }
      }
      // If no table name, use the column to try to find the table.
      else {
        if (tableExpr instanceof TableInDatabase) {
          TableInDatabase dbTable = (TableInDatabase) tableExpr;
          rdbColumn = getColumnForName(dbTable, aColName);
          if (rdbColumn != null) {
            foundIt = true;
          }
        }
      }
    }

    if (foundIt == false) {
      tableExpr = null;
    }

    return tableExpr;
  }

  /**
   * Gets the TableExpression object from the given list of table references by
   * name or correlation name ("AS"-alias). 
   * @param aTableName
   *          a table name or alias name to use to search for a table reference
   * @param aTableExprList
   *          a list of table references to search
   * @return TableExpression the table expression associated with the column.
   *         Null if not found
   */
  public static TableExpression getTableExpressionFromTableExprList(String aTableName, List aTableExprList) {
    TableExpression retTableExpr = null;

    // Scan the list of tables.
    boolean foundIt = false;
    Iterator tableExprListIter = aTableExprList.iterator();
    while (tableExprListIter.hasNext() && foundIt == false) {
      // See if we have a match by table name.
      TableExpression tableExpr = null;	
      tableExpr = (TableExpression) tableExprListIter.next();
      if (aTableName != null && aTableName.length() > 0) {
        String tableExprName = tableExpr.getName();
        String tableExprCorrName = null;

        if (tableExpr.getTableCorrelation() != null) {
          tableExprCorrName = tableExpr.getTableCorrelation().getName();
        }
		//if (aTableName.equalsIgnoreCase(tableExprName) || aTableName.equalsIgnoreCase(tableExprCorrName)) {
        if (aTableName.equals(tableExprName) ||
                        aTableName.equals(tableExprCorrName)) {
        	retTableExpr = tableExpr ;
        	foundIt = true;
        }
      }
    }
    return retTableExpr;
  }
  
  
  private class ColumnInTwoTablesException extends Exception {

  }

  /**
   * Returns a Set containing all <code>ValueExpressionColumn</code> s found
   * in the given <code>GroupingExpression</code>.
   */
    public static Set findColumnReferencesInGroupingExpression(
                                                               GroupingExpression groupingExpr)
    {
        if (groupingExpr == null) { return Collections.EMPTY_SET; }

        HashSet columnSet = new HashSet();
        columnSet.addAll(findColumnReferencesInValueExpression(groupingExpr
                        .getValueExpr()));

        return columnSet;
    }

    /**
     * Returns a Set containing all <code>ValueExpressionColumn</code> s found
     * in the given <code>QueryResultSpecification</code> list.
     */
    public static Set findColumnReferencesInQueryResultSpecificationList(
                                                                         List queryResultSpecList)
    {
        if (queryResultSpecList == null) { return Collections.EMPTY_SET; }
        HashSet columnSet = new HashSet();
        for (Iterator it = queryResultSpecList.iterator(); it.hasNext();)
        {
            QueryResultSpecification resultSpec = (QueryResultSpecification) it
                            .next();
            columnSet
                            .addAll(findColumnReferencesInQueryResultSpecification(resultSpec));
        }
        return columnSet;
    }

    /**
     * Returns a Set containing all <code>ValueExpressionColumn</code> s found
     * in the given <code>QueryResultSpecification</code>.
     */
    public static Set findColumnReferencesInQueryResultSpecification(
                                                                     QueryResultSpecification queryResult)
    {
        if (queryResult == null) { return Collections.EMPTY_SET; }
        HashSet columnSet = new HashSet();
        if (queryResult instanceof ResultColumn)
        {
            ResultColumn resultCol = (ResultColumn) queryResult;
            columnSet.addAll(findColumnReferencesInValueExpression(resultCol
                            .getValueExpr()));
        }
        return columnSet;
    }

    /**
     * Returns a Set containing all <code>ValueExpressionColumn</code> s found
     * in the given List of <code>OrderBySpecification</code>.
     */
     public static Set findColumnReferencesInOrderBySpecificationList(List orderBySpecList)
     {
         if (orderBySpecList == null) { return Collections.EMPTY_SET; }

         HashSet columnSet = new HashSet();

         for (Iterator orderIt = orderBySpecList.iterator(); orderIt.hasNext();)
         {
             OrderBySpecification orderBySpec = 
                 (OrderBySpecification) orderIt.next();

             columnSet.addAll(findColumnReferencesInOrderBySpecification(orderBySpec));
         }

         return columnSet;
     }
    

    
    /**
     * Returns a Set containing all <code>ValueExpressionColumn</code> s found
     * in the given <code>OrderBySpecification</code>.
     */
    public static Set findColumnReferencesInOrderBySpecification(
                                                                 OrderBySpecification orderBySpec)
    {
        if (orderBySpec == null) { return Collections.EMPTY_SET; }
        HashSet columnSet = new HashSet();
        
        if (orderBySpec instanceof OrderByValueExpression)
        {
            OrderByValueExpression orderByValue = (OrderByValueExpression) orderBySpec;
            columnSet.addAll(findColumnReferencesInValueExpression(orderByValue
                            .getValueExpr()));
        }

        // Note:
        //  column references in ResultSpecification should be found by specific
        //  method and not here via OrderByResultColumn -> ResultColumn -> ValueExpressionColumn
        //  would be very incomplete or duplicate effort
        
        return columnSet;
    }

    /**
     * Returns a Set containing all <code>ValueExpressionColumn</code> s found
     * in the given <code>UpdateSourceExprList</code>.
     */
    public static Set findColumnReferencesInUpdateSourceExprList(UpdateSourceExprList updateSourceExprList)
    {
        if (updateSourceExprList == null) { return Collections.EMPTY_SET; }
        
        HashSet columnSet = new HashSet();
        List exprList = updateSourceExprList.getValueExprList();
        columnSet.addAll(findColumnReferencesInValueExpressionList(exprList));
        return columnSet;
    }

    /**
     * Returns a Set containing all <code>ValueExpressionColumn</code>s found in
     * the given <code>ValuesRow</code>.
     */
    public static Set findColumnReferencesInValuesRow(ValuesRow valuesRow)
    {
        if (valuesRow == null) { return Collections.EMPTY_SET; }
        HashSet columnSet = new HashSet();
        columnSet.addAll(findColumnReferencesInValueExpressionList(valuesRow
                        .getExprList()));
        return columnSet;
    }


  /**
   * Returns a Set of not neccessarily distinct
   * <code>ValueExpressionColumn</code>s found in the given 
   * <code>SearchCondition</code>, means you have to expect duplicates regarding
   * column names.
   *
   * @param searchCond
   * @return
   */
  public static Set findColumnReferencesInSearchCondition(QuerySearchCondition searchCond) {
    if (searchCond == null) {
      return Collections.EMPTY_SET;
    }

    HashSet columnSet = new HashSet();

    if (searchCond instanceof Predicate) {
      Predicate pred = (Predicate) searchCond;
      return findColumnReferencesInPredicate(pred);
    }
    else if (searchCond instanceof SearchConditionCombined) {
      SearchConditionCombined combined = (SearchConditionCombined) searchCond;
      columnSet.addAll(findColumnReferencesInSearchCondition(combined.getLeftCondition()));
      columnSet.addAll(findColumnReferencesInSearchCondition(combined.getRightCondition()));
      return columnSet;
    }
    else if (searchCond instanceof SearchConditionNested) {
      SearchConditionNested nest = (SearchConditionNested) searchCond;
      return findColumnReferencesInSearchCondition(nest.getNestedCondition());
    }

    return columnSet;
  }

  /**
   * Returns a Set containing all, not neccessarily distinct,
   * <code>ValueExpressionColumn</code> s found in the given
   * <code>Predicate</code>, means you have to expect duplicate
   * <code>ValueExpressionColumn</code> s regarding their column names.
   *
   * TODO: consider all possible <code>Predicate</code> types here, that
   * contain <code>QueryValueExpression</code>s. State:2004-11-01
   *
   * @param predicate
   * @return
   */
  /** Returns a Set containing all <code>ValueExpressionColumn</code>s found in the given <code>Predicate</code>. */
  public static Set findColumnReferencesInPredicate(Predicate predicate) {
    if (predicate == null) {
      return Collections.EMPTY_SET;
    }

    HashSet columnSet = new HashSet();

    if (predicate instanceof PredicateBasic) {
      PredicateBasic basic = (PredicateBasic) predicate;
      columnSet.addAll(findColumnReferencesInValueExpression(basic.getRightValueExpr()));
      columnSet.addAll(findColumnReferencesInValueExpression(basic.getLeftValueExpr()));
      return columnSet;
    }

    else if (predicate instanceof PredicateBetween) {
      PredicateBetween between = (PredicateBetween) predicate;
      columnSet.addAll(findColumnReferencesInValueExpression(between.getRightValueExpr1()));
      columnSet.addAll(findColumnReferencesInValueExpression(between.getRightValueExpr2()));
      columnSet.addAll(findColumnReferencesInValueExpression(between.getLeftValueExpr()));
      return columnSet;
    }

    else if (predicate instanceof PredicateExists) {
      PredicateExists exists = (PredicateExists) predicate;
      columnSet.addAll(findColumnReferencesInQueryExpressionBody(exists.getQueryExpr()));
      return columnSet;
    }

    else if (predicate instanceof PredicateInValueList) {
      PredicateInValueList inValueList = (PredicateInValueList) predicate;
      columnSet.addAll(findColumnReferencesInValueExpression(inValueList.getValueExpr()));
      columnSet.addAll(findColumnReferencesInValueExpressionList(inValueList.getValueExprList()));
      return columnSet;
    }

    else if (predicate instanceof PredicateInValueRowSelect) {
      PredicateInValueRowSelect inRowSelect = (PredicateInValueRowSelect) predicate;
      columnSet.addAll(findColumnReferencesInQueryExpressionRoot(inRowSelect.getQueryExpr()));
      columnSet.addAll(findColumnReferencesInValueExpressionList(inRowSelect.getValueExprList()));
      return columnSet;
    }

    else if (predicate instanceof PredicateInValueSelect) {
      PredicateInValueSelect inValueSelect = (PredicateInValueSelect) predicate;
      columnSet.addAll(findColumnReferencesInQueryExpressionRoot(inValueSelect.getQueryExpr()));
      columnSet.addAll(findColumnReferencesInValueExpression(inValueSelect.getValueExpr()));
      return columnSet;
    }

    else if (predicate instanceof PredicateIsNull) {
      PredicateIsNull isNull = (PredicateIsNull) predicate;
      columnSet.addAll(findColumnReferencesInValueExpression(isNull.getValueExpr()));
      return columnSet;
    }

    else if (predicate instanceof PredicateLike) {
      PredicateLike like = (PredicateLike) predicate;
      columnSet.addAll(findColumnReferencesInValueExpression(like.getMatchingValueExpr()));
      columnSet.addAll(findColumnReferencesInValueExpression(like.getPatternValueExpr()));
      columnSet.addAll(findColumnReferencesInValueExpression(like.getEscapeValueExpr()));
      return columnSet;
    }

    else if (predicate instanceof PredicateQuantifiedRowSelect) {
      PredicateQuantifiedRowSelect rowSelect = (PredicateQuantifiedRowSelect) predicate;
      columnSet.addAll(findColumnReferencesInQueryExpressionRoot(rowSelect.getQueryExpr()));
      columnSet.addAll(findColumnReferencesInValueExpressionList(rowSelect.getValueExprList()));
      return columnSet;
    }

    else if (predicate instanceof PredicateQuantifiedValueSelect) {
      PredicateQuantifiedValueSelect inValueSelect = (PredicateQuantifiedValueSelect) predicate;
      columnSet.addAll(findColumnReferencesInQueryExpressionRoot(inValueSelect.getQueryExpr()));
      columnSet.addAll(findColumnReferencesInValueExpression(inValueSelect.getValueExpr()));
      return columnSet;
    }

    return columnSet;
  }

  /**
   * Returns a Set containing all <code>ValueExpressionColumn</code> s found
   * in the given <code>QueryValueExpression</code>.
   *
   * @param valueExpr
   * @return a Set of <code>ValueExpressionColumn</code>s
   */
  public static Set findColumnReferencesInValueExpression(
      QueryValueExpression valueExpr) {

// Client: Lin 2 Xu   for ValueExpressionFunctions
      
// TODO: consider all possible QueryValueExpression types here, that contain
// QueryValueExpressions. State: 2004-10-31

    if (valueExpr == null)
      return Collections.EMPTY_SET;

    HashSet columnSet = new HashSet();

    if (valueExpr instanceof ValueExpressionColumn) {
      columnSet.add(valueExpr);
      return columnSet;
    }
    // catch these types for performance! are very likely to occure!
    else if (valueExpr instanceof ValueExpressionSimple) {
        return Collections.EMPTY_SET;
    }
    else if (valueExpr instanceof ValueExpressionCombined) {
        ValueExpressionCombined combi = (ValueExpressionCombined) valueExpr;
        columnSet.addAll(findColumnReferencesInValueExpression(combi.getRightValueExpr()));
        columnSet.addAll(findColumnReferencesInValueExpression(combi.getLeftValueExpr()));
        return columnSet;
    }
    // catch these types for performance! are very likely to occure!
    else if (valueExpr instanceof ValueExpressionDefaultValue
                    || valueExpr instanceof ValueExpressionNullValue
                    || valueExpr instanceof ValueExpressionVariable) {
        return Collections.EMPTY_SET;
    }
    else if (valueExpr instanceof ValueExpressionNested) {
      ValueExpressionNested nest = (ValueExpressionNested) valueExpr;
      return findColumnReferencesInValueExpression(nest.getNestedValueExpr());
    }

    else if (valueExpr instanceof ValueExpressionCaseSearch) {
      ValueExpressionCaseSearch caseSearch = (ValueExpressionCaseSearch) valueExpr;
      for (Iterator searchContentList = caseSearch.getSearchContentList()
          .iterator(); searchContentList.hasNext();) {
        ValueExpressionCaseSearchContent searchContent = (ValueExpressionCaseSearchContent) searchContentList.next();
        columnSet.addAll(findColumnReferencesInValueExpression(searchContent.getValueExpr()));
        // TODO: correct?
        columnSet.addAll(findColumnReferencesInSearchCondition(searchContent.getSearchCondition()));
      }
      ValueExpressionCaseElse caseElse = (ValueExpressionCaseElse) caseSearch.getCaseElse();
      if (caseElse != null) {
          columnSet.addAll(findColumnReferencesInValueExpression(caseElse.getValueExpr()));
      }
      return columnSet;
    }

    else if (valueExpr instanceof ValueExpressionCaseSimple) {
      ValueExpressionCaseSimple caseSimple = (ValueExpressionCaseSimple) valueExpr;
      
      columnSet.addAll( findColumnReferencesInValueExpression(caseSimple.getValueExpr()) );
      
      for (Iterator simpleContentList = caseSimple.getContentList().iterator(); simpleContentList.hasNext();) {
        ValueExpressionCaseSimpleContent simpleContent = (ValueExpressionCaseSimpleContent) simpleContentList.next();
        columnSet.addAll(findColumnReferencesInValueExpression(simpleContent.getResultValueExpr()));
        columnSet.addAll(findColumnReferencesInValueExpression(simpleContent.getWhenValueExpr()));
      }

      ValueExpressionCaseElse caseElse = caseSimple.getCaseElse();
      if (caseElse != null) {
          columnSet.addAll(findColumnReferencesInValueExpression(caseElse.getValueExpr()));
      }
      return columnSet;
    }

    else if (valueExpr instanceof ValueExpressionCast) {
      ValueExpressionCast cast = (ValueExpressionCast) valueExpr;
      return findColumnReferencesInValueExpression(cast.getValueExpr());
    }

    else if (valueExpr instanceof ValueExpressionFunction) {
      ValueExpressionFunction func = (ValueExpressionFunction) valueExpr;
      return findColumnReferencesInValueExpressionList(func.getParameterList());
    }

    else if (valueExpr instanceof ValueExpressionLabeledDuration) {
      ValueExpressionLabeledDuration dur = (ValueExpressionLabeledDuration) valueExpr;
      return findColumnReferencesInValueExpression(dur.getValueExpr());
    }

    else if (valueExpr instanceof ValueExpressionScalarSelect) {
        // we don't want the column references in scalar subselect,
        // these are only valid and resolved within the context of its QueryExpressionRoot
        //**DON'T**//ValueExpressionScalarSelect scalar = (ValueExpressionScalarSelect) valueExpr;
        //**DON'T**//return findColumnReferencesInQueryExpressionRoot(scalar.getQueryExpr());
    }

    return columnSet;
  }

  /**
   * Returns a Set containing all <code>ValueExpressionColumn</code> s found
   * in the given <code>ValueExpression</code> list.
   */
    public static Set findColumnReferencesInValueExpressionList(
                                                                List valueExprList)
    {
        HashSet columnSet = null;

        if (valueExprList != null)
        {
            columnSet = new HashSet();

            for (Iterator valIt = valueExprList.iterator(); valIt.hasNext();)
            {
                QueryValueExpression valueExpr = (QueryValueExpression) valIt
                                .next();
                columnSet
                                .addAll(findColumnReferencesInValueExpression(valueExpr));
            }
        }
        return columnSet;
    }

    /**
     * Returns a Set containing all <code>ValueExpressionColumn</code> s found
     * in the given <code>GroupingSpecification</code> list.
     */
    public static Set findColumnReferencesInGroupingSpecificationList(
                                                                      List groupingSpecList)
    {
        HashSet columnSet = new HashSet();

        if (groupingSpecList != null)
        {
            for (Iterator it = groupingSpecList.iterator(); it.hasNext();)
            {
                GroupingSpecification groupingSpec = (GroupingSpecification) it
                                .next();
                columnSet
                                .addAll(findColumnReferencesInGroupingSpecification(groupingSpec));
            }
        }
        return columnSet;

    }

    /**
     * Returns a Set containing all <code>ValueExpressionColumn</code> s found
     * in the given <code>GroupingSpecification</code>.
     */
    public static Set findColumnReferencesInGroupingSpecification(
                                                                  GroupingSpecification groupingSpec)
    {
        HashSet columnSet = new HashSet();

        if (groupingSpec != null)
        {

            if (groupingSpec instanceof GroupingExpression)
            {
                GroupingExpression groupingExpr = (GroupingExpression) groupingSpec;
                columnSet
                                .addAll(findColumnReferencesInValueExpression(groupingExpr
                                                .getValueExpr()));
            }
            else if (groupingSpec instanceof SuperGroup)
            {
                Class refWanted = ValueExpressionColumn.class;
                Class[] refsToCheck = new Class[] {
                                GroupingSpecification.class,
                                SuperGroupElement.class };
                
                Set groupColumns = StatementHelper.getReferencesViaSpecificReferencePaths(groupingSpec, refWanted, refsToCheck);
                      
                columnSet.addAll(groupColumns);
                
/*                //throw new UnsupportedOperationException(
                StatementHelper.logError(
                                TableHelper.class.getName()
                                                + "#findColumnReferencesInGroupingSpecification() not implemented for "
                                                + groupingSpec.getClass()
                                                                .getName()
                                                + ".");
*/
            }
            else if (groupingSpec instanceof GroupingSets)
            {
                Class refWanted = ValueExpressionColumn.class;
                Class[] refsToCheck = new Class[] {
                                GroupingSpecification.class,
                                GroupingSetsElement.class,
                                SuperGroupElement.class };
                
                Set groupColumns = StatementHelper.getReferencesViaSpecificReferencePaths(groupingSpec, refWanted, refsToCheck);
                      
                columnSet.addAll(groupColumns);

/*                
                // throw new UnsupportedOperationException(
                StatementHelper.logError(
                                TableHelper.class.getName()
                                                + "#findColumnReferencesInGroupingSpecification() not implemented for "
                                                + groupingSpec.getClass()
                                                                .getName()
                                                + ".");
*/            
            }
            else
            {
                throw new UnsupportedOperationException(
                                TableHelper.class.getName()
                                                + "#findColumnReferencesInGroupingSpecification() not implemented for "
                                                + groupingSpec.getClass()
                                                                .getName()
                                                + ".");
            }
        }
        return columnSet;
    }




  /**
   * TODO: Developer Note: not completely implemented.
   *
   * Returns a Set containing all <code>ValueExpressionColumn</code>s found in
   * the given <code>QueryExpressionBody</code>.
   */
   public static Set findColumnReferencesInQueryExpressionBody(
                                                              QueryExpressionBody queryExprBody) {
    if (queryExprBody == null) {
      return Collections.EMPTY_SET;
    }

    HashSet columnSet = new HashSet();

    if (queryExprBody instanceof QuerySelect) {
      QuerySelect select = (QuerySelect) queryExprBody;
      columnSet.addAll(findColumnReferencesInQueryResultSpecificationList(select.getSelectClause()));
      columnSet.addAll(findColumnReferencesInSearchCondition(select.getWhereClause()));
      columnSet.addAll(findColumnReferencesInSearchCondition(select.getHavingClause()));
      columnSet.addAll(findColumnReferencesInGroupingSpecificationList(select.getGroupByClause()));

    }
    else if (queryExprBody instanceof QueryCombined) {
      QueryCombined combined = (QueryCombined) queryExprBody;
      columnSet.addAll(findColumnReferencesInQueryExpressionBody(combined.getLeftQuery()));
      columnSet.addAll(findColumnReferencesInQueryExpressionBody(combined.getRightQuery()));
    }
    else if (queryExprBody instanceof QueryValues) {
        // nothing to do here, no column references possible in VALUES clause
    }
    else {
        throw new UnsupportedOperationException(
            TableHelper.class.getName()
                + "#findColumnReferencesInQueryExpressionBody( ("+queryExprBody.getClass().getName()+") QueryExpressionBody ) not implemented.");
    }
      

    return columnSet;
  }

  /**
   * Returns a Set containing all <code>ValueExpressionColumn</code>s found in
   * the given <code>QueryExpressionRoot</code>.
   */
   public static Set findColumnReferencesInQueryExpressionRoot(
      QueryExpressionRoot queryExprRoot) {
    if (queryExprRoot == null) {
      return Collections.EMPTY_SET;
    }

    HashSet columnSet = new HashSet();

    columnSet.addAll(findColumnReferencesInQueryExpressionBody(queryExprRoot.getQuery()));
    
    // we don't want to find column references in TableWithSpecification
    // cause they are resolved within the context of the TableWithSpecification's query
    //columnSet.addAll(findColumnReferencesInTableWithSpecificationList(queryExprRoot.getWithClause()));

    return columnSet;
  }

   
   /**
    * Returns a Set containing all <code>ValueExpressionColumn</code> s found
    * in the given <code>QuerySelectStatement</code>.
    */
    public static Set findColumnReferencesInQuerySelectStatement(
                                                                 QuerySelectStatement querySelect)
    {
        if (querySelect == null) { return Collections.EMPTY_SET; }

        HashSet columnSet = new HashSet();

        columnSet.addAll(findColumnReferencesInQueryExpressionRoot(querySelect
                        .getQueryExpr()));

        List orderBySpecList = querySelect.getOrderByClause();
        columnSet.addAll( findColumnReferencesInOrderBySpecificationList(orderBySpecList));

        return columnSet;
    }
   
  
  /**
   * Returns a Set containing all <code>ValueExpressionColumn</code> s found
   * in the given <code>QueryUpdateStatement</code>.
   */
   public static Set findColumnReferencesInQueryUpdateStatement(
                                                                QueryUpdateStatement updateStmt)
   {
       if (updateStmt == null) { return Collections.EMPTY_SET; }

       HashSet columnSet = new HashSet();

       columnSet.addAll(findColumnReferencesInUpdateAssignmentExprList(updateStmt.getAssignmentClause()));
       columnSet.addAll(findColumnReferencesInSearchCondition(updateStmt.getWhereClause()));

       if (updateStmt.getWhereCurrentOfClause() != null) 
       {
           StatementHelper.logError(TableHelper.class.getName()
                           +" implement findColumnReferencesInQueryUpdateStatement(QueryUpdateStatement)" 
                           +" for "+CursorReference.class.getName());
       }

       return columnSet;
   }
  
   /**
    * Returns a Set containing all <code>ValueExpressionColumn</code> s found
    * in the given List of <code>UpdateAssignmentExpression</code>s.
    * 
    * @param updateAssignmentExprList
    *            the list of <code>UpdateAssignmentExpression</code> s to
    *            search in
    * @return Set containing all <code>ValueExpressionColumn</code> s found
    */
    public static Set findColumnReferencesInUpdateAssignmentExprList(
                                                                     List updateAssignmentExprList)
    {
        Set columnSet = new HashSet();

        for (Iterator assignIt = updateAssignmentExprList.iterator(); assignIt.hasNext();)
        {
            UpdateAssignmentExpression upAssEx = 
                (UpdateAssignmentExpression) assignIt.next();

            columnSet.addAll(findColumnReferencesInUpdateAssignmentExpr(upAssEx));
        }
        

        return columnSet;
    }

    /**
     * Returns a Set containing all <code>ValueExpressionColumn</code> s found
     * in the given List of <code>UpdateAssignmentExpression</code>.
     * 
     * @param updateAssignmentExpr
     *            the <code>UpdateAssignmentExpression</code> to search in
     * @return Set containing all <code>ValueExpressionColumn</code> s found
     */
     public static Set findColumnReferencesInUpdateAssignmentExpr(UpdateAssignmentExpression updateAssignmentExpr)
     {
         Set columnSet = new HashSet();

         columnSet.addAll(updateAssignmentExpr.getTargetColumnList());
         
         // we only want column references in updateSourceExpressionList,
         //  NOT in UpdateSourceQuery, cause the columns conatained there are localy
         //  resolved within the context of the source Query
         if (updateAssignmentExpr.getUpdateSource() instanceof UpdateSourceExprList)
         {
             UpdateSourceExprList updateSrcExprList = 
                 (UpdateSourceExprList) updateAssignmentExpr.getUpdateSource();
             columnSet.addAll(findColumnReferencesInUpdateSourceExprList(updateSrcExprList));
         }

         return columnSet;
     }
  
  /**
   * NOT IMPLEMENTED!
   *
   * @deprecated NO, but not implemented
   *
   * Associates the column refernces in the given search condition to the tables
   * in the list of TableExpressions.
   *
   * @param searchCondition
   *          the <code>SearchCondition</code> that contains
   *          <code>ValueExpressionColumn</code> s with column refernces to
   *          <code>TableExpression</code> s
   * @param tableExpressionList
   *          a List of <code>TableExpression</code>s, that
   *          <code>ValueExpressionColumn</code> s in the given
   *          <code>SearchCondition</code> should be associated with
   */
  public static void resolveTableReferencesInSearchCondition(
      SearchCondition searchCondition, List tableExpressionList)
      throws ColumnInTwoTablesException {

    // what comes in:
    // search condition with column expressions
    // column exprs must not have a table expression/ correlation name

    // get the column expressions in the search condition

    // iterate over column expressions

    // if column has table expression, get table name
    //   find table refernce in tableExpressionList with matching name
    //     if this found table has a column with a name matching to the
    //       name of the column we try to resolve, then unhook all the refernces
    //       of the column that is to be resolved and hook those refernces to
    //       the existing column in the table of the tableExpressionList
    //       (merge refernces, should be distinct though)
    //     if this found table has no column with the column name (should not
    // occure)
    //       add this column to the table's column list
    // if column has no table expression or table expression with no name
    // (could be when parser accepts partial statements and search conditon was
    // parsed isolated)
    //   go through all the table expressions in the tableExpressionList
    //   and look for columns with a matching name
    //     if found exactly one in all tables merge refernces (see above)
    //     if found more than one table having a same name column throw Exception
    //

    throw new UnsupportedOperationException(
        TableHelper.class.getName()
            + "# resolveTableReferencesInSearchCondition(SearchCondition searchCondition, List tableExpressionList) not implemented.");
    //throw new TableHelper().new ColumnInTwoTablesException();

  }

  /**
   * Creates the From Clause with the tables used in the given SQLStatement.
   *
   * @param stmt
   *          the QueryStatement from which is used to get list of tables
   * @return the From Clause string
   */
  public static String createFromClauseForStatement(QueryStatement stmt) {
    StringBuffer sqlBuf = new StringBuffer();

    List tableRefList = StatementHelper.getTablesForStatement(stmt);
    TableExpression tblExpr;

    for (Iterator tableIt = tableRefList.iterator(); tableIt.hasNext();) {
      tblExpr = (TableExpression) tableIt.next();
      if (sqlBuf.length() > 0) {
        sqlBuf.append(", ");
      }
      //retVal = getTableForTableExpression(tblExpr).getName();
      String tblExprSQL = tblExpr.getSQL();
      sqlBuf.append(tblExprSQL);
      //TableCorrelation tblCorr = tblExpr.getTableCorrelation();
      //if (tblCorr != null) {
      //  String corrName = tblCorr.getName();
      //  retVal = retVal.concat(" as ").concat(corrName);
      //}
    }

    if (sqlBuf.length() > 0) {
      sqlBuf.insert(0, "From ");
    }
    
    return sqlBuf.toString();
  }

  /**
   * Creates the From Clause with the tables used in the given SQLStatement.
   *
   * @param stmt
   *          the QueryStatement from which is used to get list of tables
   * @return the From Clause string
   */
  public static String createFromClauseForStatement(SQLQueryObject stmt) {
    String retVal = "";
    
    if (stmt instanceof QueryStatement) {
    	retVal = createFromClauseForStatement((QueryStatement)stmt);
    } else if (stmt instanceof QuerySelect) {
        StringBuffer sqlBuf = new StringBuffer();
		List tableRefList = StatementHelper.getTablesForStatement(stmt);
		TableExpression tblExpr;
		
		for (Iterator tableIt = tableRefList.iterator(); tableIt.hasNext();) {
		    tblExpr = (TableExpression) tableIt.next();
		    if (sqlBuf.length() > 0) {
		        sqlBuf.append(", ");
		    }
            String tblExprSQL = tblExpr.getSQL(); 
		    sqlBuf.append(tblExprSQL);
		}
		if (sqlBuf.length() > 0) {
		    sqlBuf.insert(0, "FROM ");
        }
        retVal = sqlBuf.toString();
    }
    
    return retVal;
  }
  
  
  /**
   * Finds in the given List of <code>TableExpression</code> s the one
   * <code>TableExpression</code> with a name that matches the given
   * <code>tableName</code> and, if a <code>schemaName</code> is
   * given, with a <code>Schema</code> whose name matches the
   * <code>schemaName</code>. If no <code>schemaName</code> is given,
   * the first <code>TableExpression</code> found without regard of its
   * <code>Schema</code>, will be returned.
   * 
   * @param schemaName
   *          optional the schema name of the table to find
   * @param tableName
   *          the name of the table to find
   * @param tableExprList
   *          the list of <code>TableExpression</code> s to search
   * @return the matching <code>TableExpression</code> or <code>null</code>
   */
  public static TableExpression findTableExpressionInTableExpressionList(
                                                                           String schemaName,
                                                                           String tableName,
                                                                           List tableExprList)
    {
        TableExpression foundTableExpr = null;
        SQLQuerySourceFormat sourceFormat = null;

        if (tableName != null && tableExprList != null)
        {

            for (Iterator tableIt = tableExprList.iterator(); tableIt.hasNext();)
            {
                TableExpression tableExpr = (TableExpression) tableIt.next();

                // get the delimited identifier quote from the SourceFormat
                if (sourceFormat == null
                                && tableExpr.getSourceInfo() != null
                                && tableExpr.getSourceInfo().getSqlFormat() != null)
                {
                    sourceFormat = tableExpr.getSourceInfo().getSqlFormat();
                }
                

                if (tableName.equals(tableExpr.getName()))
                {
                    String tableExprSchemaName = getSchemaNameForTableExpression(tableExpr);

                    if (schemaName == null
                                    || schemaName.equals(tableExprSchemaName))
                    {
                        foundTableExpr = tableExpr;
                        break;
                    }
                }
            }

        }

        return foundTableExpr;
    }

  /**
   * Finds in the given List of <code>TableExpression</code> s the one
   * <code>TableExpression</code> with a name that matches the given
   * <code>tableName</code> and, if given a <code>schemaName</code> is
   * given, a <code>Schema</code> that name matches the
   * <code>schemaName</code>. If no <code>schemaName</code> is given, only
   * a <code>TableExpression</code> without a schema name or with the
   * current default <code>Schema</code> can match.
   * (see {@link SQLQueryObject#getSourceInfo()},
   * {@link org.eclipse.datatools.modelbase.sql.query.util.SQLQuerySourceInfo#getSqlFormat()},
   * {@link org.eclipse.datatools.modelbase.sql.query.util.SQLQuerySourceFormat#getOmitSchema()})
   * <b>Note:</b> a table can only match if it has no alias, which would cascade
   * the table name and schema.
   *
   * @param schemaName
   *          optional the schema name of the table to find
   * @param tableName
   *          the name of the table to find
   * @param tableExprList
   *          the list of <code>TableExpression</code> s to search
   * @param defaultSchemaName
   * @return the matching <code>TableExpression</code> or <code>null</code>
   */
  private static TableExpression findTableExpressionInTableExpressionList(
                                                                           String schemaName,
                                                                           String tableName,
                                                                           List tableExprList,
                                                                           String defaultSchemaName)
    {
        TableExpression foundTableExpr = null;
        SQLQuerySourceFormat sourceFormat = null;

        if (tableName != null && tableExprList != null)
        {

            for (Iterator tableIt = tableExprList.iterator(); tableIt.hasNext();)
            {
                TableExpression tableExpr = (TableExpression) tableIt.next();
                String tableExprName = tableExpr.getName();

                // get the omitSchema from the table
                if (tableExpr.getSourceInfo() != null
                                && tableExpr.getSourceInfo().getSqlFormat() != null)
                {
                    sourceFormat = tableExpr.getSourceInfo().getSqlFormat();
                    if (sourceFormat.getOmitSchema() != null
                                    && sourceFormat.getOmitSchema().length() > 0)
                    {
                        defaultSchemaName = sourceFormat.getOmitSchema();
                    }
                }

                if (tableName.equals(tableExprName))
                {

                    // attention: no schemaName given can match any schema name
                    // of
                    // the tables here (doesn't mean the defaultSchema as the
                    // column
                    // trying to be resolved doesn't have to be qualified with
                    // the
                    // schema), but if the schemaName was given it is binding
                    // and
                    // we assume the given

                    if (schemaName != null)
                    {
                        String tableExprSchemaName = getSchemaName(tableExpr);

                        if (tableExprSchemaName == null)
                        {
                            tableExprSchemaName = defaultSchemaName;
                        }

                        // how to generate the SQL for column?
                        // first find out if column is in more than one table:
                        // if column is unique, don't qualify else
                        // if columns table has alias use alias as qualifier:
                        // t1.col1
                        // else compare two or more tables by name,
                        // if two same name tables, qualify with schema if
                        // that's
                        // different

                        // table alias must be null
                        if (schemaName.equals(tableExprSchemaName)
                                        && tableExpr.getTableCorrelation() == null)
                        {
                            foundTableExpr = tableExpr;
                            break;
                        }
                    }
                    else
                    // no schema to compare
                    {
                        // table alias must be null
                        if (tableExpr.getTableCorrelation() == null)
                        {
                            // give priority to previously found
                            //if (foundTableExpr == null)
                            //    foundTableExpr = tableExpr;
                            
                            // the schema name has to match exactly or has to be
                            // absent
                            String tableSchema = getSchemaName(tableExpr);
                            if (tableSchema == null
                                            || tableSchema.equals(defaultSchemaName))
                            {
                                // we have the table in the default schema or in
                                // a schema not specified (implicit default)
                                // that's the one, don't look further
                                foundTableExpr = tableExpr;
                                break;
                            }

                        }

                    }

                }
            }
        }

        return foundTableExpr;
    }


    /**
     * Finds in the given List of <code>TableExpression</code> s the
     * <code>TableExpression</code> with a name that matches the given
     * <code>tableNameOrAlias</code> or a <code>tableCorrelation</code> with
     * a name that matches <code>tableNameOrAlias</code>. If multiple tables
     * are found, preference is given to the first. <b>Note: </b> if no table is
     * found by alias name but more than one table is found by name, the table
     * that is in the default Schema specified by <code>defaultSchemaName</code>
     * will be returned, or, if no <code>defaultSchemaName</code> is given the
     * table to be returned must have no <code>Schema</code> reference.
     * If you are searching for a table with name and schema, use
     * {@link #findTableExpressionInTableExpressionList(String, String, List)}
     * <b>Note:</b> If only one table is found with the given
     * <code>tableNameOrAlias</code>, but it is Schema-qualified, it will be
     * returned also.
     * 
     * Example: <br>
     * for <code>tableNameOrAlias = "T1"</code>
     * and the given <code>tableExprList</code> contains:
     * <table border='1' width='250'>
     * <tr><th><b><code>tableExprList*</code></b></th><th><b>return</b></th>
     * </tr>
     * <tr><td> T1<br> T2<br></td><td>T1</td>
     * </tr>
     * <tr><td> S1.T1<br> T1<br></td><td>T1</td>
     * </tr>
     * <tr><td> S1.T1 AS T1<br> S2.T1<br></td><td>S1.T1</td>
     * </tr>
     * <tr><td> S1.T1<br> S1.T2<br></td><td>S1.T1</td>
     * </tr>
     * <tr><td> S1.T2<br> S1.T3<br></td><td><code>null</code></td>
     * </tr>
     * <tr><td> S1.T1<br> S2.T1<br></td><td><code>null</code></td>
     * </tr>
     * </table>
     * * S1,S2 are Schema names; T1,T2,T3 are Table names
     * 
     * @param tableNameOrAlias
     *            the name of the table to find or its correlation name
     * @param tableExprList
     *            the list of <code>TableExpression</code> s to search
     * @param defaultSchemaName
     *            optional, if given and no table is found by alias name but
     *            tables, that have no alias are found by name, the table to be
     *            returned has to be in the default Schema
     * 
     * @return the matching <code>TableExpression</code> or <code>null</code>
     */
    public static TableExpression findTableExpressionsByNameOrAlias(String tableNameOrAlias,
                                                         List tableExprList,
                                                         String defaultSchemaName)
    {
        TableExpression tableExprFound = null;
        SQLQuerySourceFormat sourceFormat = null;
        
        // if we find tables that match the given tableNameOrAlias but are schema
        // qualified we will check in the end if only one of that was found and take it
        List matchingSchemaQualifiedTables = new ArrayList();
        
        if (tableNameOrAlias != null && tableExprList != null)
        {
            for (Iterator tableIt = tableExprList.iterator(); tableIt.hasNext();)
            {
                TableExpression tableExpr = (TableExpression) tableIt.next();
                TableCorrelation tableCorr = tableExpr.getTableCorrelation();
                
                String tableName = tableExpr.getName();
                String tableAlias = null;
                
                if (tableCorr != null) 
                {
                    tableAlias = tableCorr.getName();
                }
                
                // get the omitSchema from the table
                if (tableExpr.getSourceInfo() != null
                                && tableExpr.getSourceInfo().getSqlFormat() != null)
                {
                    sourceFormat = tableExpr.getSourceInfo().getSqlFormat();
                    if (sourceFormat.getOmitSchema() != null
                                    && sourceFormat.getOmitSchema().length()>0)
                    {
                        defaultSchemaName = sourceFormat.getOmitSchema();
                    }
                }
      
                boolean equalNames = tableNameOrAlias.equals(tableName);
                boolean equalAlias = tableNameOrAlias.equals(tableAlias);
                
                // the alias-name wins over the name
                if (equalAlias)
                {
                    tableExprFound = tableExpr;
                    break;
                }
                else if (equalNames && tableAlias == null)
                {
                    // the table can only be the one, if it is in the default
                    // schema or has no schemas specified
                    // or else (forgiving the user his inproper input) we check
                    // for schema-qualified tables in the FROM-clause and if
                    // only one matches by name (no like-named tables in
                    // different schemas) we return the "best match"
                    String tableSchema = getSchemaName(tableExpr);
                    if (tableSchema == null
                                    || tableSchema.equals(defaultSchemaName))
                    {
                        tableExprFound = tableExpr;
                        // don't break as we could find another table that
                        // matches by alias name, and that would be prioritized
                    }
                    else
                    {
                        matchingSchemaQualifiedTables.add(tableExpr);
                    }
                }
            }
        }

        if (tableExprFound == null && matchingSchemaQualifiedTables.size() == 1)
        {
            tableExprFound = (TableExpression) matchingSchemaQualifiedTables.get(0);
        }
        
        return tableExprFound;
    }


  
  /**
   * Returns the name of the <code>Schema</code> that the given
   * <code>TableExpression</code> is part of or <code>null</code>, if the
   * given <code>TableExpression</code> has no reference to a
   * <code>Schema</code> or the referenced <code>Schema</code>'s name is
   * <code>null</code>.
   *
   * @param tableExpr
   * @return the <code>Schema</code> name or <code>null</code>
   */
  public static String getSchemaNameForTableExpression(TableExpression tableExpr) {
    String schemaName = null;
    if (tableExpr instanceof TableInDatabase) {
      TableInDatabase tableInDB = (TableInDatabase) tableExpr;
      Table table = tableInDB.getDatabaseTable();
      if (table != null) {
        Schema schema = table.getSchema();
        if (schema != null) {
          schemaName = schema.getName();
        }
      }
    }
    return schemaName;
  }

  /**
   * Retrieves a List of <code>TableExpression</code> s from the given List of
   * <code>TableReference</code>s.
   *
   * @param tableReferenceList
   * @return
   */
  public static List getTableExpressionsInTableReferenceList(
      List tableReferenceList) {
    List tableExprList = new ArrayList();

    for (Iterator tableRefIt = tableReferenceList.iterator(); tableRefIt.hasNext();) {
      TableReference tableRef = (TableReference) tableRefIt.next();

      tableExprList.addAll(getTableExpressionsInTableReference(tableRef));
    }

    return tableExprList;
  }

  /**
   * Retrieves the <code>TableInDatabase</code> objects contained in the given
   * List of <code>TableReference</code>s. This method might be useful to
   * find all the references to database tables in the FROM-clause of a
   * <code>QuerySelect</code>.
   * 
   * @param tableReferenceList List of <code>TableReference</code> s
   * @return List of <code>TableInDatabase</code> s contained in the given
   *         <code>tableReferenceList</code>
   */
  public static List getTableInDatabaseInTableReferenceList(List tableReferenceList) {
    List tableExprList = new ArrayList();

    for (Iterator tableRefIt = tableReferenceList.iterator(); tableRefIt.hasNext();) {
      TableReference tableRef = (TableReference) tableRefIt.next();
      tableExprList.addAll(getTableExpressionsInTableReference(tableRef));
    }
    
    // filter out TableInDatabase
    for (Iterator tableExprIt = tableExprList.iterator(); tableExprIt.hasNext();) {
        if (!(tableExprIt.next() instanceof TableInDatabase)) {
            tableExprIt.remove();
        }
    }
    
    return tableExprList;
  }

  /**
   * Retrieves a List of <code>TableExpression</code> s from the given
   * <code>TableReference</code>.
   *
   * @param tableRef
   * @return
   */
  public static List getTableExpressionsInTableReference(TableReference tableRef) {
    List tableExprList = new ArrayList();

    if (tableRef instanceof TableExpression) {
      TableExpression tableExpr = (TableExpression) tableRef;
      tableExprList.add(tableExpr);
    }

    if (tableRef instanceof TableNested) {
      TableNested nest = (TableNested) tableRef;
      tableExprList.addAll(getTableExpressionsInTableReference(nest.getNestedTableRef()));
    }

    if (tableRef instanceof TableJoined) {
      TableJoined join = (TableJoined) tableRef;
      tableExprList.addAll(getTableExpressionsInTableReference(join.getTableRefLeft()));
      tableExprList.addAll(getTableExpressionsInTableReference(join.getTableRefRight()));

    }

    return tableExprList;
  }


  /**
   * Removes a columnExpression from its tableExpression if it has
   * no other refernces to objects other than its tableExpression.
   * 
   * @param columnExpr
   */
  public static void removeColumnExpressionFromTableIfNotReferenced(ValueExpressionColumn col) {
      boolean isReferenced = false;
      
      // if col has it's table reference and all the columns refernces are only 1
      // this one ref is the table ref and will be deleted
      if (col != null && col.getTableExpr() != null) {

  		for(Iterator refIt = col.eClass().getEAllReferences().iterator();refIt.hasNext();) {
			EReference ref = (EReference) refIt.next();
			if(ref.isMany()) {
				EList refList = (EList)col.eGet(ref);
				// is there an refernce
				if (refList.size() > 0) {
				    isReferenced = true;
				}
			} else {
				EObject reference = (EObject) col.eGet(ref);
				// is there some reference besides its table
				if (reference != null && reference != col.getTableExpr()
				                && reference != col.getTableInDatabase()) {
				    isReferenced = true;
				}
			}
			
		}
  		
  		if (!isReferenced) {
  		    col.setTableExpr(null);
  		}

      }
  }


/**
 * Removes a columnExpression from its tableExpression if its tableExpr has
 * another columnExpression with the same name.
 * 
 * <B>Note:</B> only call this method if the columnExpr has no references to
 * objects other than its tableExpression
 * 
 * @param columnExpr
 * @deprecated odd method not of practical use anymore, use
 * <code>columnExpr.setTableExpr(null)</code> since 
 * {@link TableExpression#getColumnList()} does reflect only the exposed
 * columns of a <code>TableExpression</code> where as
 * {@link TableExpression#getValueExprColumns()} is the list containing the
 * arbitrary references to <code>ValueExpressionColumn</code>s which are
 * referencing to that <code>TableExpression</code> by
 * {@link ValueExpressionColumn#getTableExpr()()}
 */
public static void removeColumnExpressionFromTableIfDuplicate(ValueExpressionColumn columnExpr)
{
    // TODO Auto-generated method stub
    ValueExpressionColumn sameNameColExpr = null;

    // try to find other same name column in the columnList of the columns table
    if (columnExpr != null && columnExpr.getName() != null 
                    && columnExpr.getTableExpr() != null)
    {
        String columnName = columnExpr.getName();
        List colList = columnExpr.getTableExpr().getValueExprColumns();
        for (Iterator colIt = colList.iterator(); colIt.hasNext();)
        {
            ValueExpressionColumn otherColumnExpr = (ValueExpressionColumn) colIt
                            .next();
            if (otherColumnExpr != columnExpr) 
            {
                if (columnName.equals(otherColumnExpr.getName())) {
                    sameNameColExpr = otherColumnExpr;
                    break;
                }
            }
        }

    }
    // if there is another columnExpr with the same name, delete the given one
    // from its table 
    if (sameNameColExpr != null) {
        columnExpr.setTableExpr(null);
    }
    
}



	/**
	 * Link table references in column expressions to tables in the given
	 * list of tables (that is, the From clause),
	 * copies <code>DataType</code>s as well.
	 * 
	 * <p><b>Note:</b> the given set of <code>ValueExpressionColumn</code>s 
	 * <code>aColumnSet</code> will be modified directly. The resolved
	 * columns will be removed from the set.
	 * </p>
	 * 
	 * <p><b>Note:</b> <code>TableExpression</code>s directly contained in the given
	 * <code>aTableRefList</code> will be given preference before
	 * <code>TableExpression</code>s found contained indeirectly within
	 * <code>TableNested</code>s or <code>TableJoined</code>s, when two
	 * <code>TableReference</code>s with the same <code>name</code> are found.
	 * </p>
	 * 
	 * @param unresolvedColumns will be modified, see NOTE
	 * 
	 * @param aTableRefList a list of tables to use to resolve column references
	 */
	public static void resolveColumnTableReferences( Collection unresolvedColumns, List aTableRefList ) {

	    if (unresolvedColumns == null) { return; }
	    
	    // expand joined tables
	    List tableExprList = getTableExpressionsInTableReferenceList(aTableRefList);
    	
        ValueExpressionColumn col = null;	// column to resolve
        String colName = null; // the name of column to resolve
        TableExpression colTblRef = null; 	// table referenced by the column
        TableCorrelation colTblRefCorr = null; // correlation of table referenced by the column, if column was resolved before
        String colTblRefSchemaName = null;  // the Schema that might be qualified for the column  schema1.table1.col1
        String colQualifier = null;  // the table qualifier of the column
        
        TableExpression refTableFound = null;  	// table to match the column
        
        for (Iterator colIt = unresolvedColumns.iterator(); colIt.hasNext();)
        {
            col = (ValueExpressionColumn) colIt.next();
            colName = col.getName();
            colTblRef = col.getTableExpr();

            
            // first finding table candidate refTableFound
            
            if (colTblRef == null)
            {
                // there is only one tableExpr, it must be it
                if (tableExprList.size() == 1)
                {
                    refTableFound = (TableExpression) tableExprList.get(0);
                    // don't check here if the table found really 
                    // has a column with that name, part of semantic check
                }
                else
                {
                    // maybe one of the tables has a column with the same name
                    // only one!
                    
                    // if we have the real database columns resolved, we check
                    // for other column in the tables exposed columnList
                    // if we run without database connected, we might have no
                    // columns in the tables exposed columnList (tables not resolved)
                    // in that case we are checking for other columns that refer
                    // to that table : check disconnected mode
                    refTableFound = findTableExpressionForColumnName(tableExprList, colName, true);
                }
            }
            // was the column previously resolved?
            else if (StatementHelper.getQuerySelectForTableReference(colTblRef) != null)
            {
                // column was already resolved, we checked that the referenced
                // tableExpr is part of a fromClause of a QuerySelect
                refTableFound = colTblRef;
            }
            else
            {
                colQualifier = colTblRef.getName();
                colTblRefCorr = colTblRef.getTableCorrelation();
                
                // if the column to resolve was resolved already we must 
                // consider the alias of the columns table ref, if there are
                // two FROM-tables with the same name, but different aliases
                // select t1.col1 from table1 t1, table1 t2;
                // befor first resolving:
                //   col1.getTable() == t1 and
                //   col1.getTable().getCorrelation() == null
                // after first resolving:
                //   col1.getTable() == table1 and 
                //   col1.getTable().getCorrelation() == t1
                if (colTblRefCorr != null)
                {
                    colQualifier = colTblRefCorr.getName();
                }
                
                
                // for "select table1.col1 from table1 t1, table1 t2"
                //  we want "col1" to be associated with "table1 t1"
                // for "select s2.table1.col1 from s1.table1 t1, s2.table1 t2"
                //  we want "col1" to be associated with "table1 t2"
                // for "select table1.col1 from s1.table1 t1, s2.table1 t2"
                //  where the default Schema is "s1" we want "col1" to be 
                //  associated with "table1 t1" as we assume no Schema is default schema
                // for "select t1.col1 from t2 as t1, t1 as t2"
                //  we want "col1" to be associated with "t2 as t1"
                //  where in "select s1.t1.col1 from s1.t2 as t1, s1.t1 as t2"
                //  we want "col1" associated with "s1.t1 as t2"
                

                // if the user qualified the column name with schema and table
                // "schema.table.column"
                // we assume that the table qualifier is the actual table name
                // rather than its alias/correlation name, like in  
                // "select s1.t1.col1 from s1.t2, s1.t1", and not likely
                // "select s1.t1.col1 from s1.t2 as t1, s1.t1 as t2"
                // and the column would refer to the 2nd table reference "s1.t1"
                // "select s1.t1.col1 from s2.t1, s1.t1" or if
                // default schema "s1": "select t1.col1 from s2.t1, t1" 
                colTblRefSchemaName = getSchemaName(colTblRef);
                if (colTblRefSchemaName != null)
                {
                    //colTblRefSchemaName = getDefaultSchemaName(colTblRef);
                    // col ref without schema doesn't mean the col belongs to
                    // default schema!
                    // "select t2.col1 from table1 as t1, s2.table1 as t2"  with
                    // "s1" as default schema still allows "t2.col1" to be in
                    // schema "s2"
                    
                    refTableFound = findTableExpressionInTableExpressionList(
                                    colTblRefSchemaName, colTblRef.getName(),
                                    tableExprList,
                                    getDefaultSchemaName(col));
                }
                else
                {
                    refTableFound = findTableExpressionsByNameOrAlias(
                                    colQualifier, tableExprList,
                                    getDefaultSchemaName(col));
                }
            }
            
            if (refTableFound != null)
            {
                            
                // if refTableFound has been resolved already, it has
                // a list of exposed columns and we should find
                // a correlated column with same name to retrieve its
                // DataType and the derived-from TableInDatabase
                ValueExpressionColumn corrExposedTableCol =
                    getColumnExpressionForName(refTableFound, colName);
                
                if (corrExposedTableCol != null)
                {
                    DataType dataType = 
                        corrExposedTableCol.getDataType();
                    // copy by value, cause EMF owner relationship
                    DataType colDataType = 
                        ValueExpressionHelper.copyDataType(dataType);
                
                    if (colDataType != null)
                    {
                        col.setDataType(colDataType);
                    }
                    
                    // set the table that this column is ultimately derived from
                    if (corrExposedTableCol.getTableInDatabase() != null)
                    {
                        col.setTableInDatabase(corrExposedTableCol.getTableInDatabase());
                    }
                }
                
                col.setTableExpr(refTableFound);
                // remove the column from the unresolved column set
                colIt.remove();
           }


        } // end for columnSet iteration

	}




    /**
     * Returns the one <code>TableExpression</code> from the given
     * <code>tableExprList</code>, that has a
     * <code>ValueExpressionColumn</code> in its <code>columnList</code>
     * whose name matches the given <code>columnName</code>. If there is more
     * than one <code>TableExpression</code> found with a matching column,
     * <code>null</code> will be returned.
     * @param tableExprList
     *            the List of tables to check for the specified column
     * @param columnName
     *            the name of the column that a <code>TableExpression</code>
     *            is searched for
     * 
     * @return the <code>TableExpression</code> that has a column with the
     *         given <code>columnName</code>,<code>null</code> if more
     *         than one or no such table was found in the given
     *         <code>tableExprList</code>
     */
    private static TableExpression findTableExpressionForColumnName(List tableExprList, String columnName)
    {
        return findTableExpressionForColumnName(tableExprList, columnName, false);
    }



    /**
     * Returns the one <code>TableExpression</code> from the given
     * <code>tableExprList</code>, that has a
     * <code>ValueExpressionColumn</code> in its <code>columnList</code>
     * whose name matches the given <code>columnName</code>. If there is more
     * than one <code>TableExpression</code> found with a matching column,
     * <code>null</code> will be returned.
     * 
     * @param tableExprList
     *            the List of tables to check for the specified column
     * @param columnName
     *            the name of the column that a <code>TableExpression</code>
     *            is searched for
     * @param checkDisconnected
     *            if <code>true</code> a <code>TableExpression</code> will
     *            be returned, if it is referenced by another column with the
     *            same name as the given <code>columnName</code>, that is
     *            useful if the given <code>tableExprList</code>'s
     *            <code>TableExpression</code> s have not been populated with
     *            column in their exposed <code>columnList</code> (if no
     *            database was connected this information can be missing)
     * 
     * @return the <code>TableExpression</code> that has a column with the
     *         given <code>columnName</code>,<code>null</code> if more
     *         than one or no such table was found in the given
     *         <code>tableExprList</code>
     */
    private static TableExpression findTableExpressionForColumnName(List tableExprList, String columnName, boolean checkDisconnected)
    {
        TableExpression tableFound = null;
        
        List tablesReferencedByColumn = null;
        
        if (checkDisconnected)
        {
            tablesReferencedByColumn = new ArrayList();
        }

        for (Iterator it = tableExprList.iterator(); it.hasNext();)
        {
            TableExpression tableExpr = (TableExpression) it.next();
            
            if (checkDisconnected
                            && isTableReferencedByColumnWithName(tableExpr,
                                            columnName))
            {
                tablesReferencedByColumn.add(tableExpr);
            }
            
            ValueExpressionColumn columnFound = 
                getColumnExpressionForName(tableExpr, columnName);
            
            if (columnFound != null)
            {
	            // if we did not find one other table already, we found it
	            // if we found one already, but it was the same real table like in
	            // select col1 from table1, table1; we keep the one previously found
	            // but if we found one already and the one we found now are not
	            // representing the same real table, then delete the previously found
	            // and break
	            if (tableFound == null)
	            {
	                tableFound = tableExpr;
	            }
	            else 
	            {
	                if (tableFound.getName() != null
                            && tableFound.getName().equals(tableExpr.getName()))
		            {
	                    // we have the same column name in two different tables
	                    // that means we can't tell what table the column is of
		                tableFound = null;
		                break;
		            }
	                else
	                {
	                    // check if we have the same table in the same schema
	                    // twice, in that case we keep the first table as
	                    // the one found
	                    // select col1 from table1, table1; or
	                    // select col1 from s1.table1 t1, s1.table1 t2;
	                    // NOT select col1 from s1.table1, s2.table1;
	                    
	                    String tableExprSchema = getSchemaName(tableExpr);
	                    String tableFoundSchema = getSchemaName(tableFound);
	                    String defaultSchema = getDefaultSchemaName(tableExpr);
	                    
	                    if (tableExprSchema == null)
	                    {
	                        tableExprSchema = defaultSchema;
	                    }
	                    if (tableFoundSchema == null)
	                    {
	                        tableFoundSchema = defaultSchema;
	                    }
	                    
	                    if (equalIdentifiersOrNull(tableExprSchema,tableFoundSchema))
	                    {
		                    // we have the same column name in two tables with
	                        // the same name but in different schemas
		                    // that means we can't tell what table the column is of
			                tableFound = null;
			                break;
	                    }
	                    
		            }
	            }
            }
        }
        
        // if we didn't find a table maybe the reason is that all the
        // TableInDatabases were not populated because no database is connected
        // in that case we want to be smart enough to find out about other
        // columns refering to the table
        // e.g. "select col1, col2 from tbl1 JOIN tbl2 ON tbl1.col1 = tbl2.col2"
        // we know that "col1" belongs to "tbl1" and "col2" to "tbl2", right?
        if (tableFound == null && checkDisconnected)
        {
            if (tablesReferencedByColumn != null
                            && tablesReferencedByColumn.size() == 1)
            {
                tableFound = (TableExpression) tablesReferencedByColumn.get(0);
            }
        }
        
        return tableFound;
    }


    /**
     * returns <code>ident1.equals(ident2)</code> or
     * <p>
     * <b>Note: </b> returns <code>true</code> if both <code>ident1</code>
     * and <code>ident2</code> are <code>null</code>!
     * 
     * @param ident1
     *            one identifier
     * @param ident2
     *            another identifier to be compared to
     *            <code>ident1</code>
     * @return <code>true</code> if both arguments are <code>null</code> or are
     *   <code>String.equals()</code> equal
     */
    protected static boolean equalIdentifiersOrNull(String ident1,
                                                    String ident2)
    {
        if (ident1 == null && ident2 == null)
        {
            return true;
        }
        else if (ident1 != null)
        {
            return ident1.equals(ident2);
        }
        else
        {
            return false;
        }
    }

    
    /**
     * Returns the default <code>Schema</code> valid for the context of the
     * <code>QueryStatement</code> that the given <code>queryObject</code> is
     * part of.
     * 
     * @param queryObject
     * @return the default <code>Schema</code> name that might be omitted
     */
    private static String getDefaultSchemaName(SQLQueryObject queryObject)
    {
        String defaultSchema = null;
        
        if (queryObject.getSourceInfo() != null &&
                        queryObject.getSourceInfo().getSqlFormat() != null)
        {
            defaultSchema =
                queryObject.getSourceInfo().getSqlFormat().getOmitSchema();
        }
        
        return defaultSchema;
    }


    /**
     * Convenience method:
     * Returns the Schema name of the given <code>TableExpression</code> if the
     * given <code>tableExpr</code> is of type {@link TableInDatabase}, or
     * <code>null</code> if the given <code>tableExpr</code> has no
     * <code>Schema</code> (or no reference to a real <code>Table</code>)
     * or the given <code>tableExpr</code> was not of type
     * <code>TableInDatabase</code>.
     * 
     * @param tableExpr
     * @return the <code>tableExpr</code>s <code>Schema</code> name or <code>null</code>
     */
    private static String getSchemaName(TableExpression tableExpr)
    {
        String schemaName = null;

        if (tableExpr instanceof TableInDatabase)
        {
            TableInDatabase tableInDB = (TableInDatabase) tableExpr;
            Table dbTable = tableInDB.getDatabaseTable();

            if (dbTable != null)
            {
                Schema schema = dbTable.getSchema();
                if (schema != null)
                {
                    schemaName = schema.getName();
                }
            }
        }
        return schemaName;
    }


    /**
     * @param tableInDB
     * @return
     */
    public static Schema getSchemaForTableInDatabase(TableInDatabase tableInDB)
    {
        Schema colTblRefSchema = null;

        if (tableInDB.getDatabaseTable() != null
                        && tableInDB.getDatabaseTable().getSchema() != null)
        {
            colTblRefSchema = tableInDB.getDatabaseTable().getSchema();
        }
        
        return colTblRefSchema;
    }


    /**
     * Resolves the <code>TableReference</code> s of the
     * <code>ResultTableAllColumns</code> in the given
     * <code>resultTableList</code> with the <code>TableExpression</code> s
     * in the given <code>tableExprList</code>.
     * 
     * @param resultTableList
     *            a List of {@link ResultTableAllColumns}
     * @param tableExprList
     *            a List of {@link TableExpression}s
     */
    public static void resolveResultTableReferences(List resultTableList, List tableExprList)
    {
        if (resultTableList == null || resultTableList.isEmpty())
        {
            return;
        }
        
        for (Iterator resultTableIt = resultTableList.iterator(); resultTableIt.hasNext();)
        {
            ResultTableAllColumns resultTable = (ResultTableAllColumns) resultTableIt.next();
	            
            if (resultTable != null && resultTable.getName() != null) 
            {
	            String resultTableName = resultTable.getName();
	            
	            // TODO: check w/ Brian if only named TableReferences with name, ergo TableExpressions, otherwise we have to work with TableRefernces here
	            
	            TableExpression tableExpr = getTableExpressionFromTableExprList(resultTableName, tableExprList);
	            
	            if (tableExpr != null) {
	                resultTable.setTableExpr(tableExpr);
	            }
        	}
        }
    }

	/**
	 * Assigns the alias to the given TableExpression. 
	 * @param tableExpr the TableExpression for which alias needs to be set
	 * @param alias the String value for the alias
	 */
	public static void setTableAliasInTableExpression(TableExpression tableExpr, String alias) {
		if (alias.trim().length() > 0) {
	        TableCorrelation tableCorr = SQLQueryFactory.eINSTANCE.createTableCorrelation() ;
	        tableCorr.setName(alias.trim().toUpperCase());
	        tableExpr.setTableCorrelation(tableCorr) ;
		}
	}




    /**
     * Adds a <code>ValueExpressionColumn</code> to the given
     * <code>querySelect</code> for each of its <code>ResultColumn</code>s that
     * have either a <code>name</code> or reference a
     * <code>ValueExpressionColumn</code> that has a <code>name</code>. That is
     * useful in case the given <code>querySelect</code> is used as a nested
     * query as a <code>TableReference</code> within another
     * <code>QuerySelect</code>.
     * If the given <code>querySelect</code> already exposes some or all the
     * columns of its <code>selectClause</code>, these columns will not be
     * duplicated.
     * <p><b>Note:</b> 
     * If the <code>querySelect</code>'s <code>selectClause</code> does include
     * <code>ResultColumn</code>s without a <code>name</code> a
     * <code>ValueExpressionColumn</code> without a <code>name</code> will be
     * exposed, in order to correctly reflect order and number of resulting
     * columns for the given <code>QuerySelect</code>.
     * If the <code>querySelect</code>'s <code>selectClause</code> does include
     * a <code>ResultTableAllColumns</code>, or no <code>ResultColumn</code>s at
     * all, the number of <code>ValueExpressionColumn</code>s to be exposed is
     * determined by the total number of columns in the
     * <code>querySelect</code>'s <code>fromClause</code>.
     * </p>
     * 
     * @param querySelect
     * @return List of already or newly exposed <code>ValueExpressionColumn</code>s
     */
    private static List exposeResultColumnsOfQuerySelect(QuerySelect querySelect)
    {
        List exposedColumns = new ArrayList();
        
        if (querySelect == null) { return exposedColumns; }
        
        List resultSpecList = querySelect.getSelectClause();
        
        // do we have a "SELECT * FROM ...", star query
        if (resultSpecList == null || resultSpecList.isEmpty())
        {
            //StatementHelper.logError(TableHelper.class.getName()
            //                +" implement exposeResultColumnsOfQuerySelect(QuerySelect)" 
            //                +" for star queries (\"SELECT * FROM ...\")");
            
            // determin the number of columns implied by table, tables or joins
            // in the select's from-clause, expose as many columnExpressions with
            // name = null to make it correct for further resolving
            for (Iterator it = querySelect.getFromClause().iterator(); it.hasNext();)
            {
                TableReference tableRef = (TableReference) it.next();
                
                List tableRefCols = getEffectiveResultColumns(tableRef);
                // Not: exposedColumns.addAll(tableRefCols);
                // deep copy by value the returned exposed columnList
                //(EMF ownership doesn't allow shallow copy by reference)
                exposedColumns.addAll(copyColumnExprList(tableRefCols));	                
            }
        }
        else 
        {
	        for (Iterator resultIt = resultSpecList.iterator(); resultIt.hasNext();)
	        {
	            
	            QueryResultSpecification resultSpec = (QueryResultSpecification) resultIt.next();
	            
	            // do we have a "SELECT t1.*, ... FROM table AS t1, ..."
	            if (resultSpec instanceof ResultTableAllColumns)
	            {
	                //StatementHelper.logError(TableHelper.class.getName()
		            //        +" implement exposeResultColumnsOfQuerySelect(QuerySelect)" 
		            //        +" for table-star queries (\"SELECT t1.* ... FROM table1 AS t1...\")");
	                
	                // determin the number of columns implied by table, tables or joins
	                // in the select's from-clause, expose as many columnExpressions with
	                // name = null to make it correct for further resolving
	                
	                ResultTableAllColumns tableAll = (ResultTableAllColumns) resultSpec;
	                TableExpression tableExpr = tableAll.getTableExpr();
	                
	                // has the tableExpr already been resolved? check 
	                //  if tableAll.getTableExpr() only returns a place holder
	                // we assume a resolved tableExpression would have a exposed
	                // columnList, therefor if columnList is empty we only have
	                // a place holder table
	                if (tableExpr.getColumnList().isEmpty())
	                {
		                String tableNameOrAlias = tableAll.getName();
		                List tableExprList = getTableExpressionsInTableReferenceList(querySelect.getFromClause());
		                tableExpr = getTableExpressionFromTableExprList(tableNameOrAlias, tableExprList);
	                }
	                
	                List tableExprCols = getEffectiveResultColumns(tableExpr);
	                // Not: exposedColumns.addAll(tableExprCols);
	                // deep copy by value the returned exposed columnList
	                //(EMF ownership doesn't allow shallow copy by reference)
	                exposedColumns.addAll(copyColumnExprList(tableExprCols));	                

	            }
	            else if (resultSpec instanceof ResultColumn) 
	            {

	            
		            ResultColumn resultCol = (ResultColumn) resultSpec;
		            QueryValueExpression resultColExpr = resultCol.getValueExpr();
		            String exposedColumnName = null;
		            TableInDatabase derivedFromTableInDB = null;
		            
		            // try to get the alias name of the result column
		            if (resultCol.getName() != null)
		            {
		                exposedColumnName = resultCol.getName();
		            }
		            // if it doesn't have an alias, it might have a columnExpr with a name
		            else if (resultColExpr != null 
		                            && resultColExpr instanceof ValueExpressionColumn)
		            {
		                ValueExpressionColumn colExpr = 
		                    (ValueExpressionColumn) resultColExpr;
		                //exposedColumnName = colExpr.getName();
		                // what if "select t1.col1, t2.col1 from table1 t1, table2 t2;"
		                // then we expose not only COL1 but T1_COL1 and T2_COL1 or so?
		                // what if we have column name duplications
		                // we don't do anything about duplicate column names
		                // that will be different from application to application
		                // and can't be referenced by name anyway
		                exposedColumnName = colExpr.getName();
		            }

		            
		            // if we found no name, we still must expose a new columnExpr
		            // how do we generate a column name, ...we don't that's not
		            // specified by database either, names will be generated
		            // by application and can therefore not be referenced too
//		            if (exposedColumnName == null)
//		            {
//		                exposedColumnName = "_"+String.valueOf(exposedColumns.size() + 1);
//		            }

		            
		            
		            // check if column table reference was resolved already
		            if (resultColExpr != null 
		                            && resultColExpr instanceof ValueExpressionColumn)
		            {
		                ValueExpressionColumn colExpr = 
		                    (ValueExpressionColumn) resultColExpr;
		                // was column resolved already?
		                if (colExpr.getDataType() == null)
		                {
		                    // if not resolve column reference
		                    HashSet unresolvedColumns = new HashSet();
		                    unresolvedColumns.add(colExpr);
		                    List tableRefList = querySelect.getFromClause();
		                    resolveColumnTableReferences(unresolvedColumns, tableRefList);
		                }
		            }

		            
		            
		            // if the result column was derived from a TableInDB column
		            // keep this information
		            if (resultColExpr != null 
		                            && resultColExpr instanceof ValueExpressionColumn)
		            {
		                ValueExpressionColumn colExpr = 
		                    (ValueExpressionColumn) resultColExpr;
		                
		                // we use the columnExpr's tableRefernce, not parentTable!!!
		                // tableRef is just an reference, the column's parentTable
		                // is always just one owner tableExpression that will have
		                // the column in its columnList, this columnList is not
		                // a random collection of columns that refer to that table
		                // but it reflects the exact list of columns in order like
		                // in the database below
		                // here: we should have a special refernce to a TableInDB
		                // with name "derivedFromTableInDatabase"
		                
		                // get corresponding exposed column of the colExpr's table
		                ValueExpressionColumn colExprsTableExposedColumn =
		                    getColumnExpressionForName(colExpr.getTableExpr(), colExpr.getName());
		                // and the tableInDB that it was derived from
		                if (colExprsTableExposedColumn != null
                                        && colExprsTableExposedColumn
                                                        .getTableInDatabase() != null)
                        {
                            derivedFromTableInDB = colExprsTableExposedColumn
                                            .getTableInDatabase();
                        }
		                
		            }
		            


		            
		            // create a new ValueExpressionColumn and associate with this
		            // querySelect or find a already exposed column
	                ValueExpressionColumn exposedColumn = 
	                    getOrCreateColumnExpression(exposedColumnName, querySelect);
		            
		            
		            // check for datatype
		            if (resultColExpr != null)
		            {
		                DataType resultColDataType = resultColExpr.getDataType();
		                //was the datatype resolved already?
		                if (resultColDataType == null) {
		                    ValueExpressionHelper.resolveValueExpressionDatatypeRecursively(resultColExpr);
		                    resultColDataType = resultColExpr.getDataType();
		                }
		                DataType exposedColumnDataType = 
		                    ValueExpressionHelper.copyDataType(resultColDataType);
		                exposedColumn.setDataType(exposedColumnDataType); 
		            }

		            // we are setting the TableInDatabase that this column was
		            // originally derived from, we should use a reference called
		            // "derivedFromTableInDatabase"
		            exposedColumn.setTableInDatabase(derivedFromTableInDB);
		            
		            
		            exposedColumns.add(exposedColumn);
	            }
	        }
        }
        // all the previously associated columns (if previously exposed)
        // are in exposedColumns @see #getOrCreateColumnExpression()
        // so clear the old columnList to not duplicate and preserve ordering
        querySelect.getColumnList().clear();
        querySelect.getColumnList().addAll(exposedColumns);
        
        return exposedColumns;
    }


    /**
     * Returns a new List with new <code>ValueExpressionColumn</code>s
     * reflecting the given <code>origColExprList</code> of
     * <code>ValueExpressionColumn</code>s. This method returns a result List
     * different from the List that would result of
     * <code>
     *   List newList = ArrayList(); // could be any List implementation
     *   newList.addAll(origColExprList);
     * </code>
     * 
     * @param origColExprList List of <code>ValueExpressionColumn</code>s to be
     *  copied by value
     * @return a new List containing new copied <code>ValueExpressionColumn</code>
     *  objects
     */
    private static List copyColumnExprList(List origColExprList)
    {
        //return (List)EcoreUtil.copyAll(origColExprList); // didn't copy the 
        // reference to the derived TableInDatabase
        List result = new ArrayList(origColExprList.size());
        for (Iterator it = origColExprList.iterator(); it.hasNext();)
        {
            ValueExpressionColumn original = (ValueExpressionColumn) it.next();
            ValueExpressionColumn copy = 
                (ValueExpressionColumn)EcoreUtil.copy(original);
            
            // copy the refernce to the derived TableInDatabase
            // we should have a reference called "derivedFromTableInDatabase"
            copy.setTableInDatabase(original.getTableInDatabase());
            
            result.add(copy);
        }
        return result;
   }


    /**
     * Adds <code>ValueExpressionColumn</code>s to the given
     * <code>queryCombined</code> for each of its queries' exposed columns, see
     * {@link #exposeEffectiveResultColumns(QueryExpressionBody)}, if both
     * combined queries have the same number of exposed columns (explizitly by
     * <code>ResultColumn</code>s or implizit by
     * <code>ResultTableAllColumns</code> or no
     * <code>QueryResultSpecification</code> at all - "select * from ...")
     * If the related <code>ValueExpressionColumn</code> names of both combined
     * queries don't match, the exposed <code>ValueExpressionColumn</code> will
     * get the <code>leftQuery</code>'s name.
     * That methods is used for resolving purposes and useful in case the given
     * <code>queryCombined</code> is used as a nested query as a
     * <code>TableReference</code> within another <code>QuerySelect</code>.
     * 
     * @param queryCombined
     * @return List of already or newly exposed <code>ValueExpressionColumn</code>s
     */
    private static List exposeResultColumnsOfQueryCombined(QueryCombined queryCombined)
    {
        List exposedColumns = new ArrayList();
        
        if (queryCombined == null) {return exposedColumns;}
        
        List exposedColListLeft = getEffectiveResultColumns(queryCombined.getLeftQuery());
        List exposedColListRight = getEffectiveResultColumns(queryCombined.getRightQuery());
        
        // only if the two combined queries have the same number of columns
        if (exposedColListLeft != null && exposedColListRight != null
                        && exposedColListLeft.size() == exposedColListRight.size())
        {
            for (int i = 0; i < exposedColListLeft.size(); i++)
            {
                ValueExpressionColumn resultColExprLeft = 
                    (ValueExpressionColumn) exposedColListLeft.get(i);
                ValueExpressionColumn resultColExprRight = 
                    (ValueExpressionColumn) exposedColListRight.get(i);
                
                String leftName = resultColExprLeft.getName();
                String rightName = resultColExprRight.getName();
                
                
                String exposedColumnName = null;
                
                // the result name is always(?) the left name
                // except we have a QueryValues on the left side (has no names)
                if (queryCombined.getLeftQuery() instanceof QueryValues &&
                               !(queryCombined.getRightQuery() instanceof QueryValues))
                {
                    exposedColumnName = rightName;
                }
                else
                {
                    exposedColumnName = leftName;
                }
                
                // get previously exposed columns or create new one
                ValueExpressionColumn exposedColumn = 
                    getOrCreateColumnExpression(exposedColumnName, queryCombined);
                
                // get the best fitting inclusive DataType
                DataType exposedColumnDataType = 
                    ValueExpressionHelper.resolveCombinedDataType(
                                    resultColExprLeft.getDataType(),
                                    resultColExprRight.getDataType());
                
                // there is likely to be no common derived tableInDB for the 
                // here exposed column, but check if both exposed columns of
                // the underlying combined query are derived from the same
                // database table
                if (resultColExprLeft.getTableInDatabase() 
                                == resultColExprRight.getTableInDatabase())
                {
                    exposedColumn.setTableInDatabase(resultColExprLeft.getTableInDatabase());
                }
                
                exposedColumns.add(exposedColumn);
            }
            
        }
        // all the previously associated columns (if previously exposed)
        // are in exposedColumns @see #getOrCreateColumnExpression()
        //so clear the old columnList to not duplicate and preserve ordering
        queryCombined.getColumnList().clear();
        queryCombined.getColumnList().addAll(exposedColumns);
        
        return exposedColumns;
    }

    /**
     * Adds <code>ValueExpressionColumn</code>s to the given
     * <code>queryValues</code> for each of its values in the first
     * <code>ValuesRow</code>.
     * @see #exposeEffectiveResultColumns(QueryExpressionBody)
     * 
     * That methods is used for resolving purposes and useful in case the given
     * <code>queryValues</code> is used as a nested query as a
     * <code>TableReference</code> within another <code>QuerySelect</code> or as
     * part of a <code>QueryCombined</code>.
     * 
     * @param queryValues
     * @return List of already or newly exposed <code>ValueExpressionColumn</code>s
     */
    private static List exposeResultColumnsOfQueryValues(QueryValues queryValues)
    {
        List exposedColumns = new ArrayList();

        if (queryValues == null || queryValues.getValuesRowList().isEmpty()) {
            return exposedColumns;
        }
        
        ValuesRow firstRow = (ValuesRow) queryValues.getValuesRowList().get(0);
        List firstRowValues = firstRow.getExprList();
        
        for (int i = 0; i < firstRowValues.size(); i++)
        {
            QueryValueExpression valueExpr = 
                (QueryValueExpression) firstRowValues.get(i);
            
            // figure out the naming of unnamed columns in QueryValues
            // if we found no name, we still must expose a new columnExpr
            // how do we generate a column name, ...we don't that's not
            // specified by database either, names will be generated
            // by application and can therefore not be referenced too
//            ValueExpressionColumn exposedCol = 
//                StatementHelper.createColumnExpression("_"+(i+1));
	        ValueExpressionColumn exposedCol = 
	            StatementHelper.createColumnExpression(null);
            
            // was the DataType resolved already?
            if (valueExpr.getDataType() == null)
            {
                ValueExpressionHelper.resolveValueExpressionDatatypeRecursively(valueExpr);
            }
            
            DataType exposedDataType = valueExpr.getDataType();
            
            exposedCol.setDataType(exposedDataType);
            
            exposedColumns.add(exposedCol);
        }
        queryValues.getColumnList().clear();
        queryValues.getColumnList().addAll(exposedColumns);
        
        return exposedColumns;
    }

    /**
     * Adds a <code>ValueExpressionColumn</code> to the given
     * <code>nestedQuery</code> for each of its <code>QuerySelect</code>'s
     * <code>ResultColumn</code>s that
     * have either a <code>name</code> or reference a
     * <code>ValueExpressionColumn</code> that has a <code>name</code>. That is
     * useful in case the given <code>QueryExpressionBody</code> is used as a
     * nested query - as a <code>TableReference</code> - within another
     * <code>QuerySelect</code>, so that nested full-select can be treated like
     * any other <code>TableReference</code> with its <code>columnList</code>.
     * 
     * @param nestedQuery
     * @return List of already or newly exposed <code>ValueExpressionColumn</code>s
     */
    public static List exposeEffectiveResultColumns(QueryExpressionBody nestedQuery)
    {
        List exposedColumns = new ArrayList();
        
        if (nestedQuery instanceof QuerySelect)
        {
            QuerySelect select = (QuerySelect) nestedQuery;
            exposedColumns = exposeResultColumnsOfQuerySelect(select);
        }
        else if (nestedQuery instanceof QueryCombined)
        {
            QueryCombined combined = (QueryCombined) nestedQuery;
            exposedColumns = exposeResultColumnsOfQueryCombined(combined);
        }
        else if (nestedQuery instanceof QueryValues)
        {
            QueryValues values = (QueryValues) nestedQuery;
            exposedColumns = exposeResultColumnsOfQueryValues(values);
        }
        
        return exposedColumns;
    }

    //ckadner
    /**
     * Populates the given <code>tableExpr</code>'s <code>columnList</code> with
     * <code>ValueExpressionColumn</code>s, with <code>name</code>,
     * <code>dataType</code> and in ordering, so the given
     * <code>tableExpr</code> can be handled like a <code>TableInDatabase</code>.
     * 
     * @param tableExpr
     * @return List of already or newly exposed <code>ValueExpressionColumn</code>s
     */
    public static List exposeEffectiveResultColumns(TableExpression tableExpr) {
        List resultColExprList = new ArrayList();
        
        if (tableExpr == null) {
            return null;
        }
        else if (tableExpr instanceof QueryExpressionBody)
        {
            QueryExpressionBody query = (QueryExpressionBody) tableExpr;
            // no column List copy, just hand it through
            resultColExprList = exposeEffectiveResultColumns(query);
        }
        else if (tableExpr instanceof TableInDatabase) 
        {
            // should already have been done! but check anyways
            TableInDatabase tableInDB = (TableInDatabase) tableExpr;
            Table dbTable = tableInDB.getDatabaseTable();
            if (dbTable != null && 
                            dbTable.getColumns().size() != 
                                tableInDB.getColumnList().size()) 
            {
                populateTableExpressionColumns(tableInDB, dbTable);
            }
            //no column List copy, just hand it through
            resultColExprList = tableInDB.getColumnList();
        } 
        else if (tableExpr instanceof WithTableReference) 
        {
            WithTableReference withTableRef = (WithTableReference) tableExpr;
            resultColExprList = exposeEffectiveResultColumns(withTableRef);
        }
        else if (tableExpr instanceof TableFunction) 
        {
            StatementHelper.logError(TableHelper.class.getName()+
                            ": implement exposeEffectiveResultColumns(TableExpression) for type"+
                            TableFunction.class.getName());
        }
        else
        {
            StatementHelper.logError(TableHelper.class.getName()+
                            ": implement exposeEffectiveResultColumns(TableExpression) for type"+
                            tableExpr.getClass().getName());
        }
        
        
        return resultColExprList;
    }

    //ckadner
    /**
     * Returns a List of <code>ValueExpressionColumn</code>s,
     * with <code>name</code>, <code>dataType</code> and in ordering, 
     * so the given <code>tableRef</code> can be handled like a
     * <code>TableInDatabase</code>.
     * 
     * @param tableRef
     * @return List of exposed <code>ValueExpressionColumn</code>s
     */
    public static List getEffectiveResultColumns(TableReference tableRef) {
        
        // must be a copy of the exposed result columns of the given tableRef
        // to not allow minipulations on it to effect the underlying tableRefs
        List resultColExprList = new ArrayList();
        
        if (tableRef == null) {
            return null;
        }
        else if (tableRef instanceof TableExpression)
        {
            TableExpression tableExpr = (TableExpression) tableRef;
            // if we not already exposed the columns, expose them here
            if (tableExpr.getColumnList() == null ||
                            tableExpr.getColumnList().isEmpty()) 
            {
                exposeEffectiveResultColumns(tableExpr);
            }
            // add the exposed effective result columns from the tableExpression
            resultColExprList.addAll(tableExpr.getColumnList());
        }
        else if (tableRef instanceof TableJoined) 
        {
            // TODO: naming rules for result columns in the higher star-query
            //   different Databases will have different naming schemes!!!
            //   hand-in a String pattern or a formatter object that does it
            //   e.g. assume TABLE_A (col1, col2, col3) and TABLE_B (col1,col5)
            //   duplicated column name "col1" what are the result columns of
            //   select * from TABLE_A as A join TABLE_B as B on A.col1 = B.col1
            //   -> col1, col2, col3, col1, col5
            //      ^                 ^      
            TableJoined tableJoin = (TableJoined) tableRef;
            
            List leftCols = 
                getEffectiveResultColumns(tableJoin.getTableRefLeft());
            List rightCols = 
                getEffectiveResultColumns(tableJoin.getTableRefRight());
            resultColExprList.addAll(leftCols); 
            resultColExprList.addAll(rightCols); 
        } 
        else if (tableRef instanceof TableNested)
        {
            TableNested tableNest = (TableNested) tableRef;
            resultColExprList = 
                getEffectiveResultColumns(tableNest.getNestedTableRef());
        }
        else
        {
            StatementHelper.logError(TableHelper.class.getName()+
                            ": implement getEffectiveResultColumns(TableReference)" +
                            " for "+tableRef.getClass().getName());
        }
        
        
        return resultColExprList;
    }

    //ckadner
    /**
     * Populates the given <code>withTable</code>'s <code>columnList</code> with
     * <code>ValueExpressionColumn</code>s, with <code>name</code>,
     * <code>dataType</code> and in ordering, so the given
     * <code>tableExpr</code> can be handled like a <code>TableInDatabase</code>.
     * 
     * @param withTable
     * @return List of already or newly exposed <code>ValueExpressionColumn</code>s
     */
    public static List exposeEffectiveResultColumns(WithTableReference withTable) {
        List resultColExprList = new ArrayList();
        
        if (withTable == null || withTable.getWithTableSpecification() == null)
        {
            return resultColExprList;
        }
        
        WithTableSpecification withTableSpec = 
            withTable.getWithTableSpecification();
        
        QueryExpressionBody withTableQuery = 
            withTableSpec.getWithTableQueryExpr();
        
        List withTableQueryResultCols = 
            getEffectiveResultColumns(withTableQuery);
        
        List withTableColNames = withTableSpec.getColumnNameList();
        
        
        
        // if WITH table has not specified column names
        // the effective result columns (with name and type) are determined by
        // the WITH query
        if (withTableColNames == null || withTableColNames.isEmpty())
        {
            // deep copy-by-value! (EMF ownership doesn't allow us copy-by-ref)
            resultColExprList.addAll(copyColumnExprList(withTableQueryResultCols));
        }
        // does WITH table has specified column names?
        // we assume that the number of specified result column names
        // matches the number of effective result columns of the WITH-query
        else if (withTableColNames != null && !withTableColNames.isEmpty() &&
                        withTableColNames.size() <= withTableQueryResultCols.size())
        {
            for (int i = 0; i < withTableColNames.size(); i++)
            {
                ColumnName columnName = (ColumnName) withTableColNames.get(i);
                
                ValueExpressionColumn exposedCol = 
                    StatementHelper.createColumnExpression(columnName.getName());
                
                
                
                ValueExpressionColumn withQueryResultCol =
                    (ValueExpressionColumn) withTableQueryResultCols.get(i);
                
                DataType withQueryResultColDataType = 
                    withQueryResultCol.getDataType();
                
                DataType exposedColDataType = 
                    ValueExpressionHelper.copyDataType(
                                    withQueryResultColDataType);
                
                
	            // we are setting the TableInDatabase that this column was
	            // originally derived from, we should use a reference called
	            // "derivedFromTableInDatabase"
	            exposedCol.setTableInDatabase(withQueryResultCol.getTableInDatabase());

                exposedCol.setDataType(exposedColDataType);
                resultColExprList.add(exposedCol);
            }
        }
        // WITH table has specified column names and the WITH query was not yet
        // resolved - has no exposed columns
        else if (withTableColNames != null && !withTableColNames.isEmpty() &&
                        withTableQueryResultCols.isEmpty())
        {
            for (int i = 0; i < withTableColNames.size(); i++)
            {
                ColumnName columnName = (ColumnName) withTableColNames.get(i);
                
                ValueExpressionColumn exposedCol = 
                    StatementHelper.createColumnExpression(columnName.getName());
                
                resultColExprList.add(exposedCol);
            }
        }
        
        withTable.getColumnList().clear();
        withTable.getColumnList().addAll(resultColExprList);
        
        return resultColExprList;
    }

    
    /**
     * Returns the <code>ValueExpressionColumn</code> with the given
     * <code>columnName</code> from the given <code>tableExpression</code>'s
     * <code>columnList</code> or, if the given <code>tableExpression</code>'s
     * <code>columnList</code> does not contain a
     * <code>ValueExpressionColumn</code> with the given <code>columnName</code>,
     * creates a new <code>ValueExpressionColumn</code> with the given
     * <code>columnName</code> and returns it.
     * 
     * @param columnName 
     * @param tableExpr optional <code>TableExpression</code>, if provided the
     * 		existing <code>ValueExpressionColumn</code> with the given
     * 		<code>columnName</code> will be returned from the
     * 		<code>tableExpr</code>'s <code>columnList</code>
     * 
     * @return a <code>ValueExpressionColumn</code> - already existing or newly
     * 		created
     * 
     */
    public static ValueExpressionColumn getOrCreateColumnExpression(String columnName, TableExpression tableExpr)
    {
        ValueExpressionColumn newCol = null;
        
        if (columnName != null) 
        {
            // first try to find a column (not in the depth of the database, though)
	        if (tableExpr != null && tableExpr.getColumnList() != null)
	        {
	            newCol = getColumnExpressionForName(tableExpr, columnName);
	        }
        }
        
        // if no existing column was found create one and don't attach it to the tableExpr
        if (newCol == null)
        {
            newCol = StatementHelper.createColumnExpression(columnName);
// in question if we want the references from column to table
// or the table have this column added to its exposed columnList
// we don't wanna mess with the exposed columnList though!!! (ordering, names, types!
// will be added to table's exposedColumnList from caller of this method anyways, right?
//	        newCol.setTableExpr(tableExpr);
        }
        
        return newCol;
    }
    
    /**
     * <b>Note: </b> deprecated! use {@link TableExpression#getColumnList()}
     * <br>
     * Returns an distinct List of the given <code>TableExpression</code>'s
     * <code>ValueExpressionColumn</code> s ordered by <code>name</code>.
     * <p>
     * <b>Note: </b> out of two <code>ValueExpressionColumn</code> s with the
     * same name, the first one found in the <code>TableExpression</code>'s
     * <code>columnList</code> will be part of the returned Set, regardles of
     * the references to other objects that are not also refernced by the
     * <code>ValueExpressionColumn</code> in the returned Set.
     * </p>
     * 
     * @param tableRef
     * @return SortedSet of <code>ValueExpressionColumn</code> s without
     *         duplicates regarding their <code>name</code>
     * @deprecated use {@link TableExpression#getColumnList()}it now reflects
     *             the exact order of exposed effective "result" columns of a
     *             TableExpression
     */
    public static SortedSet getDistinctColumnExpressionList(TableExpression tableExpr) 
    {
        TreeSet columnExprSet = null;
        
        if (tableExpr != null && tableExpr.getColumnList().size() > 0) 
        {
            // create anonymous comparator that compares columns names
            Comparator columnExprNameComp = new Comparator() 
            {
                public int compare(Object o1, Object o2) 
                {
                    int columnComp = 0;
                    String colName1 = ((ValueExpressionColumn) o1).getName();
                    String colName2 = ((ValueExpressionColumn) o2).getName();
                    // compare column names regarding delimited identifiers
                    if (colName1 != null && colName1.startsWith(DELIMITED_IDENTIFIER_QUOTE)) 
                    {
                        columnComp = colName1.compareTo(colName2);
                    }
                    else 
                    {
                        columnComp = colName1.compareToIgnoreCase(colName2);
                    }
                    return columnComp;
                }
            };
            columnExprSet = new TreeSet(columnExprNameComp);
            columnExprSet.addAll(tableExpr.getColumnList());
        }
        return columnExprSet;
    }




} // End Class
