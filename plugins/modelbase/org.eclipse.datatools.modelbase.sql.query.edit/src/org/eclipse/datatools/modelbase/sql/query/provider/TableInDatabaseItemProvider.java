/**
 * <copyright>
 * </copyright>
 *
 * $Id: TableInDatabaseItemProvider.java,v 1.1 2007/09/25 23:18:03 bpayton Exp $
 */
package org.eclipse.datatools.modelbase.sql.query.provider;


import java.util.List;

import org.eclipse.datatools.modelbase.sql.query.SQLQueryModelPackage;
import org.eclipse.datatools.modelbase.sql.query.TableInDatabase;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

/**
 * This is the item provider adapter for a {@link org.eclipse.datatools.modelbase.sql.query.TableInDatabase} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class TableInDatabaseItemProvider
  extends TableExpressionItemProvider
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
  public TableInDatabaseItemProvider(AdapterFactory adapterFactory) {
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

            addDatabaseTablePropertyDescriptor(object);
            addDerivedColumnListPropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
     * This adds a property descriptor for the Database Table feature.
     * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
     * @generated
     */
  protected void addDatabaseTablePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_TableInDatabase_databaseTable_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_TableInDatabase_databaseTable_feature", "_UI_TableInDatabase_type"),
                 SQLQueryModelPackage.Literals.TABLE_IN_DATABASE__DATABASE_TABLE,
                 true,
                 false,
                 false,
                 null,
                 null,
                 null));
    }

    /**
     * This adds a property descriptor for the Derived Column List feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected void addDerivedColumnListPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_TableInDatabase_derivedColumnList_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_TableInDatabase_derivedColumnList_feature", "_UI_TableInDatabase_type"),
                 SQLQueryModelPackage.Literals.TABLE_IN_DATABASE__DERIVED_COLUMN_LIST,
                 true,
                 false,
                 false,
                 null,
                 null,
                 null));
    }

    /**
     * This returns the label text for the adapted class.
     * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
     * @generated
     */
  public String getText(Object object) {
        String label = ((TableInDatabase)object).getName();
        return label == null || label.length() == 0 ?
            getString("_UI_TableInDatabase_type") :
            getString("_UI_TableInDatabase_type") + " " + label;
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
        super.notifyChanged(notification);
    }

}
