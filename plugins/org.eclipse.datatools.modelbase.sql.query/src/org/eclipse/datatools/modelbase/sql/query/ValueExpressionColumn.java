/**
 * <copyright>
 * </copyright>
 *
 * $Id: ValueExpressionColumn.java,v 1.8 2005/10/22 01:35:21 bpayton Exp $
 */
package org.eclipse.datatools.modelbase.sql.query;


import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>SQL Value Expression Column</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This is a VEC
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.datatools.modelbase.sql.query.ValueExpressionColumn#getAssignmentExprTarget <em>Assignment Expr Target</em>}</li>
 *   <li>{@link org.eclipse.datatools.modelbase.sql.query.ValueExpressionColumn#getParentTableExpr <em>Parent Table Expr</em>}</li>
 *   <li>{@link org.eclipse.datatools.modelbase.sql.query.ValueExpressionColumn#getInsertStatement <em>Insert Statement</em>}</li>
 *   <li>{@link org.eclipse.datatools.modelbase.sql.query.ValueExpressionColumn#getTableExpr <em>Table Expr</em>}</li>
 *   <li>{@link org.eclipse.datatools.modelbase.sql.query.ValueExpressionColumn#getTableInDatabase <em>Table In Database</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.datatools.modelbase.sql.query.SQLQueryPackage#getValueExpressionColumn()
 * @model
 * @generated
 */
public interface ValueExpressionColumn extends ValueExpressionAtomic{
	/**
	 * Returns the value of the '<em><b>Assignment Expr Target</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.datatools.modelbase.sql.query.UpdateAssignmentExpression}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.datatools.modelbase.sql.query.UpdateAssignmentExpression#getTargetColumnList <em>Target Column List</em>}'.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Assignment Expr Target</em>' container reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Assignment Expr Target</em>' reference list.
	 * @see org.eclipse.datatools.modelbase.sql.query.SQLQueryPackage#getValueExpressionColumn_AssignmentExprTarget()
	 * @see org.eclipse.datatools.modelbase.sql.query.UpdateAssignmentExpression#getTargetColumnList
	 * @model type="org.eclipse.datatools.modelbase.sql.query.UpdateAssignmentExpression" opposite="targetColumnList"
	 * @generated
	 */
    EList getAssignmentExprTarget();

	/**
	 * Returns the value of the '<em><b>Parent Table Expr</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.datatools.modelbase.sql.query.TableExpression#getColumnList <em>Column List</em>}'.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Parent Table Expr</em>' container reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent Table Expr</em>' container reference.
	 * @see #setParentTableExpr(TableExpression)
	 * @see org.eclipse.datatools.modelbase.sql.query.SQLQueryPackage#getValueExpressionColumn_ParentTableExpr()
	 * @see org.eclipse.datatools.modelbase.sql.query.TableExpression#getColumnList
	 * @model opposite="columnList" unsettable="true" required="true"
	 * @generated
	 */
    TableExpression getParentTableExpr();

	/**
	 * Sets the value of the '{@link org.eclipse.datatools.modelbase.sql.query.ValueExpressionColumn#getParentTableExpr <em>Parent Table Expr</em>}' container reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent Table Expr</em>' container reference.
	 * @see #getParentTableExpr()
	 * @generated
	 */
    void setParentTableExpr(TableExpression value);

	/**
	 * Returns the value of the '<em><b>Table Expr</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.datatools.modelbase.sql.query.TableExpression#getValueExprColumns <em>Value Expr Columns</em>}'.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Table Expr</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Table Expr</em>' reference.
	 * @see #setTableExpr(TableExpression)
	 * @see org.eclipse.datatools.modelbase.sql.query.SQLQueryPackage#getValueExpressionColumn_TableExpr()
	 * @see org.eclipse.datatools.modelbase.sql.query.TableExpression#getValueExprColumns
	 * @model opposite="valueExprColumns" required="true"
	 * @generated
	 */
    TableExpression getTableExpr();

	/**
	 * Sets the value of the '{@link org.eclipse.datatools.modelbase.sql.query.ValueExpressionColumn#getTableExpr <em>Table Expr</em>}' reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Table Expr</em>' reference.
	 * @see #getTableExpr()
	 * @generated
	 */
    void setTableExpr(TableExpression value);

	/**
	 * Returns the value of the '<em><b>Table In Database</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.datatools.modelbase.sql.query.TableInDatabase#getDerivedColumnList <em>Derived Column List</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Gets the <code>TableInDatabase</code> from which this column is ulitimately derived, if any.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Table In Database</em>' reference.
	 * @see #setTableInDatabase(TableInDatabase)
	 * @see org.eclipse.datatools.modelbase.sql.query.SQLQueryPackage#getValueExpressionColumn_TableInDatabase()
	 * @see org.eclipse.datatools.modelbase.sql.query.TableInDatabase#getDerivedColumnList
	 * @model opposite="derivedColumnList"
	 * @generated
	 */
    TableInDatabase getTableInDatabase();

	/**
	 * Sets the value of the '{@link org.eclipse.datatools.modelbase.sql.query.ValueExpressionColumn#getTableInDatabase <em>Table In Database</em>}' reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Table In Database</em>' reference.
	 * @see #getTableInDatabase()
	 * @generated
	 */
    void setTableInDatabase(TableInDatabase value);

	/**
	 * Returns the value of the '<em><b>Insert Statement</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.datatools.modelbase.sql.query.QueryInsertStatement}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.datatools.modelbase.sql.query.QueryInsertStatement#getTargetColumnList <em>Target Column List</em>}'.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Insert Statement</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Insert Statement</em>' reference list.
	 * @see org.eclipse.datatools.modelbase.sql.query.SQLQueryPackage#getValueExpressionColumn_InsertStatement()
	 * @see org.eclipse.datatools.modelbase.sql.query.QueryInsertStatement#getTargetColumnList
	 * @model type="org.eclipse.datatools.modelbase.sql.query.QueryInsertStatement" opposite="targetColumnList"
	 * @generated
	 */
    EList getInsertStatement();

} // SQLValueExpressionColumn
