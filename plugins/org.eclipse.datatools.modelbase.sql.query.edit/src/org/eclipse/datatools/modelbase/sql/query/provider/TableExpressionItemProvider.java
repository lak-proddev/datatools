/**
 * <copyright>
 * </copyright>
 *
 * $Id: TableExpressionItemProvider.java,v 1.1 2007/03/22 17:10:12 bpayton Exp $
 */
package org.eclipse.datatools.modelbase.sql.query.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.datatools.modelbase.sql.query.SQLQueryModelPackage;
import org.eclipse.datatools.modelbase.sql.query.TableExpression;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adpater for a {@link org.eclipse.datatools.modelbase.sql.query.TableExpression} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class TableExpressionItemProvider
  extends TableReferenceItemProvider
  implements
    IEditingDomainItemProvider,
    IStructuredItemContentProvider,
    ITreeItemContentProvider,
    IItemLabelProvider,
    IItemPropertySource {
    /**
     * This constructs an instance from a factory and a notifier.
     * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
     * @generated
     */
  public TableExpressionItemProvider(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    /**
     * This returns the property descriptors for the adapted class.
     * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
     * @generated
     */
  public List getPropertyDescriptors(Object object) {
        if (itemPropertyDescriptors == null) {
            super.getPropertyDescriptors(object);

            addColumnListPropertyDescriptor(object);
            addResultTableAllColumnsPropertyDescriptor(object);
            addValueExprColumnsPropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
     * This adds a property descriptor for the Column List feature.
     * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
     * @generated
     */
  protected void addColumnListPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (new ItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_TableExpression_columnList_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_TableExpression_columnList_feature", "_UI_TableExpression_type"),
                 SQLQueryModelPackage.eINSTANCE.getTableExpression_ColumnList(),
                 true));
    }

    /**
     * This adds a property descriptor for the Result Table All Columns feature.
     * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
     * @generated
     */
  protected void addResultTableAllColumnsPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (new ItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_TableExpression_resultTableAllColumns_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_TableExpression_resultTableAllColumns_feature", "_UI_TableExpression_type"),
                 SQLQueryModelPackage.eINSTANCE.getTableExpression_ResultTableAllColumns(),
                 true));
    }

    /**
     * This adds a property descriptor for the Value Expr Columns feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addValueExprColumnsPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (new ItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_TableExpression_valueExprColumns_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_TableExpression_valueExprColumns_feature", "_UI_TableExpression_type"),
                 SQLQueryModelPackage.eINSTANCE.getTableExpression_ValueExprColumns(),
                 true));
    }

    /**
     * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
     * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
     * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
     * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
     * @generated NOT
     */
  public Collection getChildrenFeatures(Object object) {
        if (childrenFeatures == null) {
            super.getChildrenFeatures(object);
            //childrenFeatures.add(SQLQueryModelPackage.eINSTANCE.getTableExpression_ColumnList());
            //childrenFeatures.add(SQLQueryModelPackage.eINSTANCE.getTableExpression_TableCorrelation());
        }
        return childrenFeatures;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EStructuralFeature getChildFeature(Object object, Object child) {
        // Check the type of the specified child object and return the proper feature to use for
        // adding (see {@link AddCommand}) it as a child.

        return super.getChildFeature(object, child);
    }

    /**
     * This returns the label text for the adapted class.
     * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
     * @generated
     */
  public String getText(Object object) {
        String label = ((TableExpression)object).getName();
        return label == null || label.length() == 0 ?
            getString("_UI_TableExpression_type") :
            getString("_UI_TableExpression_type") + " " + label;
    }

    /**
     * This handles model notifications by calling {@link #updateChildren} to update any cached
     * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
     * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
     * @generated
     */
  public void notifyChanged(Notification notification) {
        updateChildren(notification);

        switch (notification.getFeatureID(TableExpression.class)) {
            case SQLQueryModelPackage.TABLE_EXPRESSION__TABLE_CORRELATION:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
                return;
        }
        super.notifyChanged(notification);
    }

    /**
     * Return the resource locator for this item provider's resources.
     * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
     * @generated
     */
  public ResourceLocator getResourceLocator() {
        return SQLQueryEditPlugin.INSTANCE;
    }

}
