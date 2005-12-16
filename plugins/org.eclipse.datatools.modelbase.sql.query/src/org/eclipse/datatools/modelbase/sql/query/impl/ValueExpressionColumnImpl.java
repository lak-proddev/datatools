/**
 * <copyright>
 * </copyright>
 *
 * $Id: ValueExpressionColumnImpl.java,v 1.10 2005/10/22 01:35:24 bpayton Exp $
 */
package org.eclipse.datatools.modelbase.sql.query.impl;

import java.util.Collection;

import org.eclipse.datatools.modelbase.sql.query.GroupingExpression;
import org.eclipse.datatools.modelbase.sql.query.OrderByValueExpression;
import org.eclipse.datatools.modelbase.sql.query.PredicateBasic;
import org.eclipse.datatools.modelbase.sql.query.PredicateBetween;
import org.eclipse.datatools.modelbase.sql.query.PredicateInValueList;
import org.eclipse.datatools.modelbase.sql.query.PredicateInValueRowSelect;
import org.eclipse.datatools.modelbase.sql.query.PredicateInValueSelect;
import org.eclipse.datatools.modelbase.sql.query.PredicateIsNull;
import org.eclipse.datatools.modelbase.sql.query.PredicateLike;
import org.eclipse.datatools.modelbase.sql.query.PredicateQuantifiedRowSelect;
import org.eclipse.datatools.modelbase.sql.query.PredicateQuantifiedValueSelect;
import org.eclipse.datatools.modelbase.sql.query.QueryInsertStatement;
import org.eclipse.datatools.modelbase.sql.query.ResultColumn;
import org.eclipse.datatools.modelbase.sql.query.SQLQueryPackage;
import org.eclipse.datatools.modelbase.sql.query.SQLQueryPackage;
import org.eclipse.datatools.modelbase.sql.query.TableExpression;
import org.eclipse.datatools.modelbase.sql.query.TableInDatabase;
import org.eclipse.datatools.modelbase.sql.query.UpdateAssignmentExpression;
import org.eclipse.datatools.modelbase.sql.query.UpdateSourceExprList;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionCaseElse;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionCaseSearchContent;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionCaseSimple;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionCaseSimpleContent;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionCast;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionColumn;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionCombined;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionFunction;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionLabeledDuration;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionNested;
import org.eclipse.datatools.modelbase.sql.query.ValueExpressionUnaryOperator;
import org.eclipse.datatools.modelbase.sql.query.ValuesRow;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.datatools.modelbase.sql.datatypes.DataType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>SQL Value Expression Column</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.datatools.modelbase.sql.query.impl.ValueExpressionColumnImpl#getAssignmentExprTarget <em>Assignment Expr Target</em>}</li>
 *   <li>{@link org.eclipse.datatools.modelbase.sql.query.impl.ValueExpressionColumnImpl#getParentTableExpr <em>Parent Table Expr</em>}</li>
 *   <li>{@link org.eclipse.datatools.modelbase.sql.query.impl.ValueExpressionColumnImpl#getInsertStatement <em>Insert Statement</em>}</li>
 *   <li>{@link org.eclipse.datatools.modelbase.sql.query.impl.ValueExpressionColumnImpl#getTableExpr <em>Table Expr</em>}</li>
 *   <li>{@link org.eclipse.datatools.modelbase.sql.query.impl.ValueExpressionColumnImpl#getTableInDatabase <em>Table In Database</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ValueExpressionColumnImpl extends ValueExpressionAtomicImpl implements ValueExpressionColumn {
	/**
	 * The cached value of the '{@link #getAssignmentExprTarget() <em>Assignment Expr Target</em>}' reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getAssignmentExprTarget()
	 * @generated
	 * @ordered
	 */
    protected EList assignmentExprTarget = null;

	/**
	 * The cached value of the '{@link #getInsertStatement() <em>Insert Statement</em>}' reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getInsertStatement()
	 * @generated
	 * @ordered
	 */
    protected EList insertStatement = null;

	/**
	 * The cached value of the '{@link #getTableExpr() <em>Table Expr</em>}' reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getTableExpr()
	 * @generated
	 * @ordered
	 */
    protected TableExpression tableExpr = null;

	/**
	 * The cached value of the '{@link #getTableInDatabase() <em>Table In Database</em>}' reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getTableInDatabase()
	 * @generated
	 * @ordered
	 */
    protected TableInDatabase tableInDatabase = null;

	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected ValueExpressionColumnImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return SQLQueryPackage.eINSTANCE.getValueExpressionColumn();
	}

	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getAssignmentExprTarget() {
		if (assignmentExprTarget == null) {
			assignmentExprTarget = new EObjectWithInverseResolvingEList.ManyInverse(UpdateAssignmentExpression.class, this, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__ASSIGNMENT_EXPR_TARGET, SQLQueryPackage.UPDATE_ASSIGNMENT_EXPRESSION__TARGET_COLUMN_LIST);
		}
		return assignmentExprTarget;
	}

	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public TableExpression getParentTableExpr() {
		if (eContainerFeatureID != SQLQueryPackage.VALUE_EXPRESSION_COLUMN__PARENT_TABLE_EXPR) return null;
		return (TableExpression)eContainer;
	}

	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setParentTableExpr(TableExpression newParentTableExpr) {
		if (newParentTableExpr != eContainer || (eContainerFeatureID != SQLQueryPackage.VALUE_EXPRESSION_COLUMN__PARENT_TABLE_EXPR && newParentTableExpr != null)) {
			if (EcoreUtil.isAncestor(this, newParentTableExpr))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eContainer != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newParentTableExpr != null)
				msgs = ((InternalEObject)newParentTableExpr).eInverseAdd(this, SQLQueryPackage.TABLE_EXPRESSION__COLUMN_LIST, TableExpression.class, msgs);
			msgs = eBasicSetContainer((InternalEObject)newParentTableExpr, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__PARENT_TABLE_EXPR, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__PARENT_TABLE_EXPR, newParentTableExpr, newParentTableExpr));
	}

	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public TableExpression getTableExpr() {
		if (tableExpr != null && tableExpr.eIsProxy()) {
			TableExpression oldTableExpr = tableExpr;
			tableExpr = (TableExpression)eResolveProxy((InternalEObject)tableExpr);
			if (tableExpr != oldTableExpr) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__TABLE_EXPR, oldTableExpr, tableExpr));
			}
		}
		return tableExpr;
	}

	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public TableExpression basicGetTableExpr() {
		return tableExpr;
	}

	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetTableExpr(TableExpression newTableExpr, NotificationChain msgs) {
		TableExpression oldTableExpr = tableExpr;
		tableExpr = newTableExpr;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__TABLE_EXPR, oldTableExpr, newTableExpr);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTableExpr(TableExpression newTableExpr) {
		if (newTableExpr != tableExpr) {
			NotificationChain msgs = null;
			if (tableExpr != null)
				msgs = ((InternalEObject)tableExpr).eInverseRemove(this, SQLQueryPackage.TABLE_EXPRESSION__VALUE_EXPR_COLUMNS, TableExpression.class, msgs);
			if (newTableExpr != null)
				msgs = ((InternalEObject)newTableExpr).eInverseAdd(this, SQLQueryPackage.TABLE_EXPRESSION__VALUE_EXPR_COLUMNS, TableExpression.class, msgs);
			msgs = basicSetTableExpr(newTableExpr, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__TABLE_EXPR, newTableExpr, newTableExpr));
	}

	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public TableInDatabase getTableInDatabase() {
		if (tableInDatabase != null && tableInDatabase.eIsProxy()) {
			TableInDatabase oldTableInDatabase = tableInDatabase;
			tableInDatabase = (TableInDatabase)eResolveProxy((InternalEObject)tableInDatabase);
			if (tableInDatabase != oldTableInDatabase) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__TABLE_IN_DATABASE, oldTableInDatabase, tableInDatabase));
			}
		}
		return tableInDatabase;
	}

	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public TableInDatabase basicGetTableInDatabase() {
		return tableInDatabase;
	}

	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetTableInDatabase(TableInDatabase newTableInDatabase, NotificationChain msgs) {
		TableInDatabase oldTableInDatabase = tableInDatabase;
		tableInDatabase = newTableInDatabase;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__TABLE_IN_DATABASE, oldTableInDatabase, newTableInDatabase);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTableInDatabase(TableInDatabase newTableInDatabase) {
		if (newTableInDatabase != tableInDatabase) {
			NotificationChain msgs = null;
			if (tableInDatabase != null)
				msgs = ((InternalEObject)tableInDatabase).eInverseRemove(this, SQLQueryPackage.TABLE_IN_DATABASE__DERIVED_COLUMN_LIST, TableInDatabase.class, msgs);
			if (newTableInDatabase != null)
				msgs = ((InternalEObject)newTableInDatabase).eInverseAdd(this, SQLQueryPackage.TABLE_IN_DATABASE__DERIVED_COLUMN_LIST, TableInDatabase.class, msgs);
			msgs = basicSetTableInDatabase(newTableInDatabase, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__TABLE_IN_DATABASE, newTableInDatabase, newTableInDatabase));
	}

	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getInsertStatement() {
		if (insertStatement == null) {
			insertStatement = new EObjectWithInverseResolvingEList.ManyInverse(QueryInsertStatement.class, this, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__INSERT_STATEMENT, SQLQueryPackage.QUERY_INSERT_STATEMENT__TARGET_COLUMN_LIST);
		}
		return insertStatement;
	}

	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__EANNOTATIONS:
					return ((InternalEList)getEAnnotations()).basicAdd(otherEnd, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUES_ROW:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUES_ROW, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__ORDER_BY_VALUE_EXPR:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__ORDER_BY_VALUE_EXPR, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__RESULT_COLUMN:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__RESULT_COLUMN, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BASIC_RIGHT:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BASIC_RIGHT, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BASIC_LEFT:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BASIC_LEFT, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LIKE_PATTERN:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LIKE_PATTERN, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LIKE_MATCHING:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LIKE_MATCHING, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__PREDICATE_NULL:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__PREDICATE_NULL, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_LIST_RIGHT:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_LIST_RIGHT, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_LIST_LEFT:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_LIST_LEFT, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_ROW_SELECT_LEFT:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_ROW_SELECT_LEFT, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_SELECT_LEFT:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_SELECT_LEFT, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__QUANTIFIED_ROW_SELECT_LEFT:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__QUANTIFIED_ROW_SELECT_LEFT, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__QUANTIFIED_VALUE_SELECT_LEFT:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__QUANTIFIED_VALUE_SELECT_LEFT, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BETWEEN_LEFT:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BETWEEN_LEFT, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BETWEEN_RIGHT1:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BETWEEN_RIGHT1, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BETWEEN_RIGHT2:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BETWEEN_RIGHT2, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CAST:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CAST, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_FUNCTION:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_FUNCTION, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_COMBINED_LEFT:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_COMBINED_LEFT, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_COMBINED_RIGHT:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_COMBINED_RIGHT, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__GROUPING_EXPR:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__GROUPING_EXPR, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_ELSE:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_ELSE, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SIMPLE:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SIMPLE, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SIMPLE_CONTENT_WHEN:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SIMPLE_CONTENT_WHEN, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SIMPLE_CONTENT_RESULT:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SIMPLE_CONTENT_RESULT, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SEARCH_CONTENT:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SEARCH_CONTENT, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LIKE_ESCAPE:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LIKE_ESCAPE, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_LABELED_DURATION:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_LABELED_DURATION, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__NEST:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__NEST, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__UPDATE_SOURCE_EXPR_LIST:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__UPDATE_SOURCE_EXPR_LIST, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__ASSIGNMENT_EXPR_TARGET:
					return ((InternalEList)getAssignmentExprTarget()).basicAdd(otherEnd, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__PARENT_TABLE_EXPR:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__PARENT_TABLE_EXPR, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__INSERT_STATEMENT:
					return ((InternalEList)getInsertStatement()).basicAdd(otherEnd, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__TABLE_EXPR:
					if (tableExpr != null)
						msgs = ((InternalEObject)tableExpr).eInverseRemove(this, SQLQueryPackage.TABLE_EXPRESSION__VALUE_EXPR_COLUMNS, TableExpression.class, msgs);
					return basicSetTableExpr((TableExpression)otherEnd, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__TABLE_IN_DATABASE:
					if (tableInDatabase != null)
						msgs = ((InternalEObject)tableInDatabase).eInverseRemove(this, SQLQueryPackage.TABLE_IN_DATABASE__DERIVED_COLUMN_LIST, TableInDatabase.class, msgs);
					return basicSetTableInDatabase((TableInDatabase)otherEnd, msgs);
				default:
					return eDynamicInverseAdd(otherEnd, featureID, baseClass, msgs);
			}
		}
		if (eContainer != null)
			msgs = eBasicRemoveFromContainer(msgs);
		return eBasicSetContainer(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__EANNOTATIONS:
					return ((InternalEList)getEAnnotations()).basicRemove(otherEnd, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__DEPENDENCIES:
					return ((InternalEList)getDependencies()).basicRemove(otherEnd, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__DATA_TYPE:
					return basicSetDataType(null, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUES_ROW:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUES_ROW, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__ORDER_BY_VALUE_EXPR:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__ORDER_BY_VALUE_EXPR, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__RESULT_COLUMN:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__RESULT_COLUMN, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BASIC_RIGHT:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BASIC_RIGHT, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BASIC_LEFT:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BASIC_LEFT, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LIKE_PATTERN:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LIKE_PATTERN, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LIKE_MATCHING:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LIKE_MATCHING, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__PREDICATE_NULL:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__PREDICATE_NULL, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_LIST_RIGHT:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_LIST_RIGHT, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_LIST_LEFT:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_LIST_LEFT, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_ROW_SELECT_LEFT:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_ROW_SELECT_LEFT, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_SELECT_LEFT:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_SELECT_LEFT, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__QUANTIFIED_ROW_SELECT_LEFT:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__QUANTIFIED_ROW_SELECT_LEFT, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__QUANTIFIED_VALUE_SELECT_LEFT:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__QUANTIFIED_VALUE_SELECT_LEFT, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BETWEEN_LEFT:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BETWEEN_LEFT, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BETWEEN_RIGHT1:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BETWEEN_RIGHT1, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BETWEEN_RIGHT2:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BETWEEN_RIGHT2, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CAST:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CAST, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_FUNCTION:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_FUNCTION, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_COMBINED_LEFT:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_COMBINED_LEFT, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_COMBINED_RIGHT:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_COMBINED_RIGHT, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__GROUPING_EXPR:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__GROUPING_EXPR, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_ELSE:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_ELSE, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SIMPLE:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SIMPLE, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SIMPLE_CONTENT_WHEN:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SIMPLE_CONTENT_WHEN, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SIMPLE_CONTENT_RESULT:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SIMPLE_CONTENT_RESULT, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SEARCH_CONTENT:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SEARCH_CONTENT, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LIKE_ESCAPE:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LIKE_ESCAPE, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_LABELED_DURATION:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_LABELED_DURATION, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__NEST:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__NEST, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__UPDATE_SOURCE_EXPR_LIST:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__UPDATE_SOURCE_EXPR_LIST, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__ASSIGNMENT_EXPR_TARGET:
					return ((InternalEList)getAssignmentExprTarget()).basicRemove(otherEnd, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__PARENT_TABLE_EXPR:
					return eBasicSetContainer(null, SQLQueryPackage.VALUE_EXPRESSION_COLUMN__PARENT_TABLE_EXPR, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__INSERT_STATEMENT:
					return ((InternalEList)getInsertStatement()).basicRemove(otherEnd, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__TABLE_EXPR:
					return basicSetTableExpr(null, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__TABLE_IN_DATABASE:
					return basicSetTableInDatabase(null, msgs);
				default:
					return eDynamicInverseRemove(otherEnd, featureID, baseClass, msgs);
			}
		}
		return eBasicSetContainer(null, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eBasicRemoveFromContainer(NotificationChain msgs) {
		if (eContainerFeatureID >= 0) {
			switch (eContainerFeatureID) {
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUES_ROW:
					return eContainer.eInverseRemove(this, SQLQueryPackage.VALUES_ROW__EXPR_LIST, ValuesRow.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__ORDER_BY_VALUE_EXPR:
					return eContainer.eInverseRemove(this, SQLQueryPackage.ORDER_BY_VALUE_EXPRESSION__VALUE_EXPR, OrderByValueExpression.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__RESULT_COLUMN:
					return eContainer.eInverseRemove(this, SQLQueryPackage.RESULT_COLUMN__VALUE_EXPR, ResultColumn.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BASIC_RIGHT:
					return eContainer.eInverseRemove(this, SQLQueryPackage.PREDICATE_BASIC__RIGHT_VALUE_EXPR, PredicateBasic.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BASIC_LEFT:
					return eContainer.eInverseRemove(this, SQLQueryPackage.PREDICATE_BASIC__LEFT_VALUE_EXPR, PredicateBasic.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LIKE_PATTERN:
					return eContainer.eInverseRemove(this, SQLQueryPackage.PREDICATE_LIKE__PATTERN_VALUE_EXPR, PredicateLike.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LIKE_MATCHING:
					return eContainer.eInverseRemove(this, SQLQueryPackage.PREDICATE_LIKE__MATCHING_VALUE_EXPR, PredicateLike.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__PREDICATE_NULL:
					return eContainer.eInverseRemove(this, SQLQueryPackage.PREDICATE_IS_NULL__VALUE_EXPR, PredicateIsNull.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_LIST_RIGHT:
					return eContainer.eInverseRemove(this, SQLQueryPackage.PREDICATE_IN_VALUE_LIST__VALUE_EXPR_LIST, PredicateInValueList.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_LIST_LEFT:
					return eContainer.eInverseRemove(this, SQLQueryPackage.PREDICATE_IN_VALUE_LIST__VALUE_EXPR, PredicateInValueList.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_ROW_SELECT_LEFT:
					return eContainer.eInverseRemove(this, SQLQueryPackage.PREDICATE_IN_VALUE_ROW_SELECT__VALUE_EXPR_LIST, PredicateInValueRowSelect.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_SELECT_LEFT:
					return eContainer.eInverseRemove(this, SQLQueryPackage.PREDICATE_IN_VALUE_SELECT__VALUE_EXPR, PredicateInValueSelect.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__QUANTIFIED_ROW_SELECT_LEFT:
					return eContainer.eInverseRemove(this, SQLQueryPackage.PREDICATE_QUANTIFIED_ROW_SELECT__VALUE_EXPR_LIST, PredicateQuantifiedRowSelect.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__QUANTIFIED_VALUE_SELECT_LEFT:
					return eContainer.eInverseRemove(this, SQLQueryPackage.PREDICATE_QUANTIFIED_VALUE_SELECT__VALUE_EXPR, PredicateQuantifiedValueSelect.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BETWEEN_LEFT:
					return eContainer.eInverseRemove(this, SQLQueryPackage.PREDICATE_BETWEEN__LEFT_VALUE_EXPR, PredicateBetween.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BETWEEN_RIGHT1:
					return eContainer.eInverseRemove(this, SQLQueryPackage.PREDICATE_BETWEEN__RIGHT_VALUE_EXPR1, PredicateBetween.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BETWEEN_RIGHT2:
					return eContainer.eInverseRemove(this, SQLQueryPackage.PREDICATE_BETWEEN__RIGHT_VALUE_EXPR2, PredicateBetween.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CAST:
					return eContainer.eInverseRemove(this, SQLQueryPackage.VALUE_EXPRESSION_CAST__VALUE_EXPR, ValueExpressionCast.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_FUNCTION:
					return eContainer.eInverseRemove(this, SQLQueryPackage.VALUE_EXPRESSION_FUNCTION__PARAMETER_LIST, ValueExpressionFunction.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_COMBINED_LEFT:
					return eContainer.eInverseRemove(this, SQLQueryPackage.VALUE_EXPRESSION_COMBINED__LEFT_VALUE_EXPR, ValueExpressionCombined.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_COMBINED_RIGHT:
					return eContainer.eInverseRemove(this, SQLQueryPackage.VALUE_EXPRESSION_COMBINED__RIGHT_VALUE_EXPR, ValueExpressionCombined.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__GROUPING_EXPR:
					return eContainer.eInverseRemove(this, SQLQueryPackage.GROUPING_EXPRESSION__VALUE_EXPR, GroupingExpression.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_ELSE:
					return eContainer.eInverseRemove(this, SQLQueryPackage.VALUE_EXPRESSION_CASE_ELSE__VALUE_EXPR, ValueExpressionCaseElse.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SIMPLE:
					return eContainer.eInverseRemove(this, SQLQueryPackage.VALUE_EXPRESSION_CASE_SIMPLE__VALUE_EXPR, ValueExpressionCaseSimple.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SIMPLE_CONTENT_WHEN:
					return eContainer.eInverseRemove(this, SQLQueryPackage.VALUE_EXPRESSION_CASE_SIMPLE_CONTENT__WHEN_VALUE_EXPR, ValueExpressionCaseSimpleContent.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SIMPLE_CONTENT_RESULT:
					return eContainer.eInverseRemove(this, SQLQueryPackage.VALUE_EXPRESSION_CASE_SIMPLE_CONTENT__RESULT_VALUE_EXPR, ValueExpressionCaseSimpleContent.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SEARCH_CONTENT:
					return eContainer.eInverseRemove(this, SQLQueryPackage.VALUE_EXPRESSION_CASE_SEARCH_CONTENT__VALUE_EXPR, ValueExpressionCaseSearchContent.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LIKE_ESCAPE:
					return eContainer.eInverseRemove(this, SQLQueryPackage.PREDICATE_LIKE__ESCAPE_VALUE_EXPR, PredicateLike.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_LABELED_DURATION:
					return eContainer.eInverseRemove(this, SQLQueryPackage.VALUE_EXPRESSION_LABELED_DURATION__VALUE_EXPR, ValueExpressionLabeledDuration.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__NEST:
					return eContainer.eInverseRemove(this, SQLQueryPackage.VALUE_EXPRESSION_NESTED__NESTED_VALUE_EXPR, ValueExpressionNested.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__UPDATE_SOURCE_EXPR_LIST:
					return eContainer.eInverseRemove(this, SQLQueryPackage.UPDATE_SOURCE_EXPR_LIST__VALUE_EXPR_LIST, UpdateSourceExprList.class, msgs);
				case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__PARENT_TABLE_EXPR:
					return eContainer.eInverseRemove(this, SQLQueryPackage.TABLE_EXPRESSION__COLUMN_LIST, TableExpression.class, msgs);
				default:
					return eDynamicBasicRemoveFromContainer(msgs);
			}
		}
		return eContainer.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - eContainerFeatureID, null, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__EANNOTATIONS:
				return getEAnnotations();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__NAME:
				return getName();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__DEPENDENCIES:
				return getDependencies();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__DESCRIPTION:
				return getDescription();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LABEL:
				return getLabel();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__UNARY_OPERATOR:
				return getUnaryOperator();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__DATA_TYPE:
				return getDataType();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUES_ROW:
				return getValuesRow();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__ORDER_BY_VALUE_EXPR:
				return getOrderByValueExpr();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__RESULT_COLUMN:
				return getResultColumn();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BASIC_RIGHT:
				return getBasicRight();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BASIC_LEFT:
				return getBasicLeft();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LIKE_PATTERN:
				return getLikePattern();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LIKE_MATCHING:
				return getLikeMatching();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__PREDICATE_NULL:
				return getPredicateNull();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_LIST_RIGHT:
				return getInValueListRight();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_LIST_LEFT:
				return getInValueListLeft();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_ROW_SELECT_LEFT:
				return getInValueRowSelectLeft();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_SELECT_LEFT:
				return getInValueSelectLeft();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__QUANTIFIED_ROW_SELECT_LEFT:
				return getQuantifiedRowSelectLeft();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__QUANTIFIED_VALUE_SELECT_LEFT:
				return getQuantifiedValueSelectLeft();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BETWEEN_LEFT:
				return getBetweenLeft();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BETWEEN_RIGHT1:
				return getBetweenRight1();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BETWEEN_RIGHT2:
				return getBetweenRight2();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CAST:
				return getValueExprCast();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_FUNCTION:
				return getValueExprFunction();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_COMBINED_LEFT:
				return getValueExprCombinedLeft();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_COMBINED_RIGHT:
				return getValueExprCombinedRight();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__GROUPING_EXPR:
				return getGroupingExpr();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_ELSE:
				return getValueExprCaseElse();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SIMPLE:
				return getValueExprCaseSimple();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SIMPLE_CONTENT_WHEN:
				return getValueExprCaseSimpleContentWhen();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SIMPLE_CONTENT_RESULT:
				return getValueExprCaseSimpleContentResult();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SEARCH_CONTENT:
				return getValueExprCaseSearchContent();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LIKE_ESCAPE:
				return getLikeEscape();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_LABELED_DURATION:
				return getValueExprLabeledDuration();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__NEST:
				return getNest();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__UPDATE_SOURCE_EXPR_LIST:
				return getUpdateSourceExprList();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__ASSIGNMENT_EXPR_TARGET:
				return getAssignmentExprTarget();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__PARENT_TABLE_EXPR:
				return getParentTableExpr();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__INSERT_STATEMENT:
				return getInsertStatement();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__TABLE_EXPR:
				if (resolve) return getTableExpr();
				return basicGetTableExpr();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__TABLE_IN_DATABASE:
				if (resolve) return getTableInDatabase();
				return basicGetTableInDatabase();
		}
		return eDynamicGet(eFeature, resolve);
	}

	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void eSet(EStructuralFeature eFeature, Object newValue) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__EANNOTATIONS:
				getEAnnotations().clear();
				getEAnnotations().addAll((Collection)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__NAME:
				setName((String)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__DEPENDENCIES:
				getDependencies().clear();
				getDependencies().addAll((Collection)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LABEL:
				setLabel((String)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__UNARY_OPERATOR:
				setUnaryOperator((ValueExpressionUnaryOperator)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__DATA_TYPE:
				setDataType((DataType)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUES_ROW:
				setValuesRow((ValuesRow)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__ORDER_BY_VALUE_EXPR:
				setOrderByValueExpr((OrderByValueExpression)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__RESULT_COLUMN:
				setResultColumn((ResultColumn)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BASIC_RIGHT:
				setBasicRight((PredicateBasic)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BASIC_LEFT:
				setBasicLeft((PredicateBasic)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LIKE_PATTERN:
				setLikePattern((PredicateLike)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LIKE_MATCHING:
				setLikeMatching((PredicateLike)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__PREDICATE_NULL:
				setPredicateNull((PredicateIsNull)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_LIST_RIGHT:
				setInValueListRight((PredicateInValueList)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_LIST_LEFT:
				setInValueListLeft((PredicateInValueList)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_ROW_SELECT_LEFT:
				setInValueRowSelectLeft((PredicateInValueRowSelect)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_SELECT_LEFT:
				setInValueSelectLeft((PredicateInValueSelect)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__QUANTIFIED_ROW_SELECT_LEFT:
				setQuantifiedRowSelectLeft((PredicateQuantifiedRowSelect)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__QUANTIFIED_VALUE_SELECT_LEFT:
				setQuantifiedValueSelectLeft((PredicateQuantifiedValueSelect)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BETWEEN_LEFT:
				setBetweenLeft((PredicateBetween)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BETWEEN_RIGHT1:
				setBetweenRight1((PredicateBetween)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BETWEEN_RIGHT2:
				setBetweenRight2((PredicateBetween)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CAST:
				setValueExprCast((ValueExpressionCast)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_FUNCTION:
				setValueExprFunction((ValueExpressionFunction)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_COMBINED_LEFT:
				setValueExprCombinedLeft((ValueExpressionCombined)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_COMBINED_RIGHT:
				setValueExprCombinedRight((ValueExpressionCombined)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__GROUPING_EXPR:
				setGroupingExpr((GroupingExpression)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_ELSE:
				setValueExprCaseElse((ValueExpressionCaseElse)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SIMPLE:
				setValueExprCaseSimple((ValueExpressionCaseSimple)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SIMPLE_CONTENT_WHEN:
				setValueExprCaseSimpleContentWhen((ValueExpressionCaseSimpleContent)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SIMPLE_CONTENT_RESULT:
				setValueExprCaseSimpleContentResult((ValueExpressionCaseSimpleContent)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SEARCH_CONTENT:
				setValueExprCaseSearchContent((ValueExpressionCaseSearchContent)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LIKE_ESCAPE:
				setLikeEscape((PredicateLike)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_LABELED_DURATION:
				setValueExprLabeledDuration((ValueExpressionLabeledDuration)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__NEST:
				setNest((ValueExpressionNested)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__UPDATE_SOURCE_EXPR_LIST:
				setUpdateSourceExprList((UpdateSourceExprList)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__ASSIGNMENT_EXPR_TARGET:
				getAssignmentExprTarget().clear();
				getAssignmentExprTarget().addAll((Collection)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__PARENT_TABLE_EXPR:
				setParentTableExpr((TableExpression)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__INSERT_STATEMENT:
				getInsertStatement().clear();
				getInsertStatement().addAll((Collection)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__TABLE_EXPR:
				setTableExpr((TableExpression)newValue);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__TABLE_IN_DATABASE:
				setTableInDatabase((TableInDatabase)newValue);
				return;
		}
		eDynamicSet(eFeature, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void eUnset(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__EANNOTATIONS:
				getEAnnotations().clear();
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__NAME:
				setName(NAME_EDEFAULT);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__DEPENDENCIES:
				getDependencies().clear();
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LABEL:
				setLabel(LABEL_EDEFAULT);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__UNARY_OPERATOR:
				setUnaryOperator(UNARY_OPERATOR_EDEFAULT);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__DATA_TYPE:
				setDataType((DataType)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUES_ROW:
				setValuesRow((ValuesRow)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__ORDER_BY_VALUE_EXPR:
				setOrderByValueExpr((OrderByValueExpression)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__RESULT_COLUMN:
				setResultColumn((ResultColumn)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BASIC_RIGHT:
				setBasicRight((PredicateBasic)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BASIC_LEFT:
				setBasicLeft((PredicateBasic)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LIKE_PATTERN:
				setLikePattern((PredicateLike)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LIKE_MATCHING:
				setLikeMatching((PredicateLike)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__PREDICATE_NULL:
				setPredicateNull((PredicateIsNull)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_LIST_RIGHT:
				setInValueListRight((PredicateInValueList)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_LIST_LEFT:
				setInValueListLeft((PredicateInValueList)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_ROW_SELECT_LEFT:
				setInValueRowSelectLeft((PredicateInValueRowSelect)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_SELECT_LEFT:
				setInValueSelectLeft((PredicateInValueSelect)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__QUANTIFIED_ROW_SELECT_LEFT:
				setQuantifiedRowSelectLeft((PredicateQuantifiedRowSelect)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__QUANTIFIED_VALUE_SELECT_LEFT:
				setQuantifiedValueSelectLeft((PredicateQuantifiedValueSelect)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BETWEEN_LEFT:
				setBetweenLeft((PredicateBetween)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BETWEEN_RIGHT1:
				setBetweenRight1((PredicateBetween)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BETWEEN_RIGHT2:
				setBetweenRight2((PredicateBetween)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CAST:
				setValueExprCast((ValueExpressionCast)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_FUNCTION:
				setValueExprFunction((ValueExpressionFunction)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_COMBINED_LEFT:
				setValueExprCombinedLeft((ValueExpressionCombined)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_COMBINED_RIGHT:
				setValueExprCombinedRight((ValueExpressionCombined)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__GROUPING_EXPR:
				setGroupingExpr((GroupingExpression)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_ELSE:
				setValueExprCaseElse((ValueExpressionCaseElse)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SIMPLE:
				setValueExprCaseSimple((ValueExpressionCaseSimple)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SIMPLE_CONTENT_WHEN:
				setValueExprCaseSimpleContentWhen((ValueExpressionCaseSimpleContent)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SIMPLE_CONTENT_RESULT:
				setValueExprCaseSimpleContentResult((ValueExpressionCaseSimpleContent)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SEARCH_CONTENT:
				setValueExprCaseSearchContent((ValueExpressionCaseSearchContent)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LIKE_ESCAPE:
				setLikeEscape((PredicateLike)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_LABELED_DURATION:
				setValueExprLabeledDuration((ValueExpressionLabeledDuration)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__NEST:
				setNest((ValueExpressionNested)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__UPDATE_SOURCE_EXPR_LIST:
				setUpdateSourceExprList((UpdateSourceExprList)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__ASSIGNMENT_EXPR_TARGET:
				getAssignmentExprTarget().clear();
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__PARENT_TABLE_EXPR:
				setParentTableExpr((TableExpression)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__INSERT_STATEMENT:
				getInsertStatement().clear();
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__TABLE_EXPR:
				setTableExpr((TableExpression)null);
				return;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__TABLE_IN_DATABASE:
				setTableInDatabase((TableInDatabase)null);
				return;
		}
		eDynamicUnset(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean eIsSet(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__EANNOTATIONS:
				return eAnnotations != null && !eAnnotations.isEmpty();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__DEPENDENCIES:
				return dependencies != null && !dependencies.isEmpty();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LABEL:
				return LABEL_EDEFAULT == null ? label != null : !LABEL_EDEFAULT.equals(label);
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__UNARY_OPERATOR:
				return unaryOperator != UNARY_OPERATOR_EDEFAULT;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__DATA_TYPE:
				return dataType != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUES_ROW:
				return getValuesRow() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__ORDER_BY_VALUE_EXPR:
				return getOrderByValueExpr() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__RESULT_COLUMN:
				return getResultColumn() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BASIC_RIGHT:
				return getBasicRight() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BASIC_LEFT:
				return getBasicLeft() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LIKE_PATTERN:
				return getLikePattern() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LIKE_MATCHING:
				return getLikeMatching() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__PREDICATE_NULL:
				return getPredicateNull() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_LIST_RIGHT:
				return getInValueListRight() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_LIST_LEFT:
				return getInValueListLeft() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_ROW_SELECT_LEFT:
				return getInValueRowSelectLeft() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__IN_VALUE_SELECT_LEFT:
				return getInValueSelectLeft() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__QUANTIFIED_ROW_SELECT_LEFT:
				return getQuantifiedRowSelectLeft() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__QUANTIFIED_VALUE_SELECT_LEFT:
				return getQuantifiedValueSelectLeft() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BETWEEN_LEFT:
				return getBetweenLeft() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BETWEEN_RIGHT1:
				return getBetweenRight1() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__BETWEEN_RIGHT2:
				return getBetweenRight2() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CAST:
				return getValueExprCast() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_FUNCTION:
				return getValueExprFunction() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_COMBINED_LEFT:
				return getValueExprCombinedLeft() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_COMBINED_RIGHT:
				return getValueExprCombinedRight() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__GROUPING_EXPR:
				return getGroupingExpr() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_ELSE:
				return getValueExprCaseElse() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SIMPLE:
				return getValueExprCaseSimple() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SIMPLE_CONTENT_WHEN:
				return getValueExprCaseSimpleContentWhen() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SIMPLE_CONTENT_RESULT:
				return getValueExprCaseSimpleContentResult() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_CASE_SEARCH_CONTENT:
				return getValueExprCaseSearchContent() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__LIKE_ESCAPE:
				return getLikeEscape() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__VALUE_EXPR_LABELED_DURATION:
				return getValueExprLabeledDuration() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__NEST:
				return getNest() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__UPDATE_SOURCE_EXPR_LIST:
				return getUpdateSourceExprList() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__ASSIGNMENT_EXPR_TARGET:
				return assignmentExprTarget != null && !assignmentExprTarget.isEmpty();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__PARENT_TABLE_EXPR:
				return getParentTableExpr() != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__INSERT_STATEMENT:
				return insertStatement != null && !insertStatement.isEmpty();
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__TABLE_EXPR:
				return tableExpr != null;
			case SQLQueryPackage.VALUE_EXPRESSION_COLUMN__TABLE_IN_DATABASE:
				return tableInDatabase != null;
		}
		return eDynamicIsSet(eFeature);
	}

} //SQLValueExpressionColumnImpl
