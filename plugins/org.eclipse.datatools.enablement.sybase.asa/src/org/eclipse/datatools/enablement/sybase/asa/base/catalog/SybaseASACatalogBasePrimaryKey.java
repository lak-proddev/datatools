package org.eclipse.datatools.enablement.sybase.asa.base.catalog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.eclipse.datatools.connectivity.sqm.core.rte.ICatalogObject;
import org.eclipse.datatools.connectivity.sqm.core.rte.RefreshManager;
import org.eclipse.datatools.enablement.sybase.asa.JDBCASAPlugin;
import org.eclipse.datatools.enablement.sybase.asa.catalog.SQLScriptsProvider;
import org.eclipse.datatools.enablement.sybase.asa.catalog.SybaseASACatalogUtils;
import org.eclipse.datatools.enablement.sybase.asa.models.sybaseasabasesqlmodel.SybaseASABaseDatabase;
import org.eclipse.datatools.enablement.sybase.asa.models.sybaseasabasesqlmodel.SybaseasabasesqlmodelPackage;
import org.eclipse.datatools.enablement.sybase.asa.models.sybaseasabasesqlmodel.impl.SybaseASABasePrimaryKeyImpl;
import org.eclipse.datatools.modelbase.sql.schema.Database;
import org.eclipse.datatools.modelbase.sql.tables.Table;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature;

public class SybaseASACatalogBasePrimaryKey extends SybaseASABasePrimaryKeyImpl
		implements ICatalogObject {

	private static final long serialVersionUID = -7797014533423629745L;
	protected Boolean pkInfoLoaded = Boolean.FALSE;
	
	public Database getCatalogDatabase() {
		return this.getBaseTable().getSchema().getDatabase();
	}

	public Connection getConnection() {
		return ((ICatalogObject)getCatalogDatabase()).getConnection();
	}

	public void refresh() {
		synchronized (pkInfoLoaded) {
			if(pkInfoLoaded.booleanValue())
			{
				pkInfoLoaded = Boolean.FALSE;
			}
		}
		RefreshManager.getInstance().referesh(this);
	}

	public boolean eIsSet(EStructuralFeature eFeature) {
		switch(eDerivedStructuralFeatureID(eFeature))
		{
		case SybaseasabasesqlmodelPackage.SYBASE_ASA_BASE_UNIQUE_CONSTRAINT__MEMBERS:
			getMembers();
			break;
		case SybaseasabasesqlmodelPackage.SYBASE_ASA_BASE_UNIQUE_CONSTRAINT__CLUSTERED:
			isClustered();
			break;
		}
		return super.eIsSet(eFeature);
	}
	
	public EList getMembers() {
		synchronized (pkInfoLoaded) {
			if(!pkInfoLoaded.booleanValue())
			{
				loadPKInfo();
				pkInfoLoaded = Boolean.TRUE;
			}
		}
		return super.getMembers();
	}

	public boolean isClustered() {
		synchronized (pkInfoLoaded) {
			if(!pkInfoLoaded.booleanValue())
			{
				loadPKInfo();
				pkInfoLoaded = Boolean.TRUE;
			}
		}
		return super.isClustered();
	}
	
	protected void loadPKInfo() {
		boolean deliver = this.eDeliver();
		this.eSetDeliver(false);
		
		SybaseASABaseDatabase db = (SybaseASABaseDatabase)this.getCatalogDatabase();
		
		Connection conn = this.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			Table table  = this.getBaseTable();
			String schemaName = table.getSchema().getName();
			String tableName = table.getName();
			
			stmt = conn.prepareStatement(SQLScriptsProvider.getQueryPrimaryKeyInfo(db));
			stmt.setString(1, schemaName);
			stmt.setString(2, tableName);
			stmt.setString(3, this.getName());
			rs = stmt.executeQuery();
			while(rs.next())
			{
                String colListStr = rs.getString(2);
                boolean isClustered = rs.getString(3).equals("Y"); //$NON-NLS-1$
                
                List columnList = SybaseASACatalogUtils.getSpecifiedColumns(colListStr, table.getColumns());
                super.getMembers().clear();
                super.getMembers().addAll(columnList);
                super.setClustered(isClustered);
			}
		}
		catch (SQLException e) {
			JDBCASAPlugin.getDefault().log(e);
		}
		finally
		{
			SybaseASACatalogUtils.cleanupJDBCResouce(rs, stmt);
		}
		this.eSetDeliver(deliver);
	}
}
