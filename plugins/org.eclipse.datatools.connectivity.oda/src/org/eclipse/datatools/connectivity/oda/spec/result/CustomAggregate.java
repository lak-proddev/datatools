/*
 *************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.datatools.connectivity.oda.spec.result;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.nls.Messages;
import org.eclipse.datatools.connectivity.oda.spec.ExpressionVariable;
import org.eclipse.datatools.connectivity.oda.spec.IValidator;
import org.eclipse.datatools.connectivity.oda.spec.ValidationContext;
import org.eclipse.datatools.connectivity.oda.spec.manifest.AggregateDefinition;
import org.eclipse.datatools.connectivity.oda.spec.manifest.ResultExtensionExplorer;
import org.eclipse.datatools.connectivity.oda.spec.manifest.SupportedDataSetType;

/**
 * Represents an instance of custom aggregate expression contributed by an extension of
 * the org.eclipse.datatools.connectivity.oda.dynamicResultSet extension point.
 * @since 3.2 (DTP 1.7)
 */
public class CustomAggregate extends AggregateExpression implements IExecutableExtension
{
    private static final String QUALIFER_SEPARATOR = "."; //$NON-NLS-1$
    private String m_id;
    private String m_extensionId;
    private Map<String,Object> m_customData;
    private AggregateDefinition m_definition;  // expects 1-n-only-1 associated AggregateDefinition

    /*
     * Constructor for internal use only.
     * Use ExpressionFactory#createCustomAggregate to create a custom aggregate instance.
     */
    public CustomAggregate( String extensionId, String id, ExpressionVariable inputSourceVar )
    {
        super( inputSourceVar );
        m_extensionId = extensionId;
        m_id = id;
    }

    /*
     * Constructor for internal use only.
     * Use ExpressionFactory#createCustomAggregate to create a custom aggregate instance.
     */
    public CustomAggregate( String extensionId, String id )
    {
        this( extensionId, id, null );
    }
    
    /*
     * Constructor for internal use only by org.eclipse.core.runtime.IExecutableExtension#setInitializationData.
     * Use ExpressionFactory#createCustomAggregate to create a custom aggregate instance.
     */
    public CustomAggregate()
    {
        super( null );
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement, java.lang.String, java.lang.Object)
     */
    public void setInitializationData( IConfigurationElement exprElement,
            String propertyName, Object data ) throws CoreException
    {
        m_extensionId = exprElement.getDeclaringExtension().getUniqueIdentifier();
        try
        {
            m_id = AggregateDefinition.getIdAttributeValue( exprElement );
        }
        catch( OdaException ex )
        {
            throw new CoreException( new Status( IStatus.ERROR, exprElement.getContributor().getName(), ex.getLocalizedMessage() ) );
        }
    }

    /**
     * Gets the expression id.  It is unique within the contributing extension.
     * @return the expression id
     */
    public String getId()
    {
        return m_id;
    }
    
    /**
     * Gets the unique id of the dynamicResultSet extension that declares this custom expression type.
     * @return unique id of declaring extension
     */
    public String getDeclaringExtensionId()
    {
        return m_extensionId;
    }
 
    /*
     * (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.spec.result.AggregateExpression#getQualifiedId()
     */
    public String getQualifiedId()
    {
        return m_extensionId + QUALIFER_SEPARATOR + m_id;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.spec.result.AggregateExpression#getAlias()
     */
    @Override
    public String getAlias()
    {
        if( getDefinition() == null )
            return super.getAlias();
        
        StringBuffer alias = new StringBuffer( getDefinition().getDisplayName() );
        alias.append( ALIAS_SEPARATOR );
        alias.append( super.getAlias() );
        return alias.toString();
    }

    /**
     * Indicates whether this expression can be applied to the specified data set type within the data source type.
     * @param odaDataSourceId   id of an ODA data source extension
     * @param odaDataSetId      id of an ODA data set defined within the data source extension
     * @return  true if this can be applied to the specified data set type; false otherwise
     */
    public boolean supportsDataSetType( String odaDataSourceId, String odaDataSetId )
    {
        AggregateDefinition defn = getDefinition();
        if( defn == null )
            return false;
        
        return defn.supportsDataSetType( odaDataSourceId, odaDataSetId );
    }
    
    /**
     * Indicates whether this expression can be applied to the specified data set type.
     * @param dataSetType   an instance of {@link SupportedDataSetType} 
     * @return  true if this can be applied to the specified ODA data set type; false otherwise
     */
    public boolean supportsDataSetType( SupportedDataSetType dataSetType )
    {
        if( dataSetType == null )
            return false;
        return supportsDataSetType( dataSetType.getOdaDataSourceId(), dataSetType.getOdaDataSetId() );
    }
    
    /**
     * Indicates whether this aggregate type is capable of ignoring duplicate values of its input variable. 
     * Default value is false if none is specified in the extension.
     * @return true if this aggregate type is capable of ignoring duplicate values of its input variable;
     *          false otherwise 
     */
    public boolean canIgnoreDuplicateValues()
    {
        AggregateDefinition defn = getDefinition();
        return ( defn == null ) ? false : defn.canIgnoreDuplicateValues();
    }

    /**
     * Indicates whether this aggregate type is capable of ignoring null values of its input variable. 
     * Default value is false if none is specified in the extension.
     * @return true if this aggregate type is capable of ignoring null values of its input variable;
     *          false otherwise 
     */
    public boolean canIgnoreNullValues()
    {
        AggregateDefinition defn = getDefinition();
        return ( defn == null ) ? false : defn.canIgnoreNullValues();
    }

    /**
     * Gets the definition of this expression's capabilities and metadata, as registered by the provider 
     * in its extension of the <i>org.eclipse.datatools.connectivity.oda.dynamicResultSet</i> extension point.
     * @return  definition of this custom aggregate expression
     */
    public AggregateDefinition getDefinition()
    {
        // obtain from extension manifest explorer by extensionId + id
        if( m_definition == null )
        {
            try
            {
                m_definition = ResultExtensionExplorer.getInstance()
                    .getExtensionAggregateDefinition( getDeclaringExtensionId(), getId() );
            }
            catch( OdaException ex )
            {
                // TODO log warning
                ex.printStackTrace();
                return null;
            }
        }
        return m_definition;
    }
    
    /**
     * Gets the value of an extension-defined property of the specified name.
     * An extension contributor may have arbitrary objects associated with an expression. 
     * @param key   the name of property
     * @return      the value of the named property, or null if it has not been set
     */
    public Object getData( String key )
    {
        if( m_customData == null )
            return null;
        return m_customData.get( key );
    }

    /**
     * Sets the value of an extension-defined property of the specified name.
     * An extension contributor may use this to associate arbitrary objects with an expression.
     * @param key   the name of property
     * @param value the new value of the named property
     */
    public void setData( String key, Object value )
    {
        if( m_customData == null )
            m_customData = new HashMap<String,Object>();
        m_customData.put( key, value );
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.spec.result.AggregateExpression#validate(org.eclipse.datatools.connectivity.oda.spec.ValidationContext)
     */
    @Override
    public void validate( ValidationContext context ) throws OdaException
    {
        AggregateDefinition defn = getDefinition();
        if( defn == null )
            throw new OdaException( 
                    Messages.bind( "The custom expression ({0}) is not found in the dynamicResultSet extension registry.",
                                getQualifiedId() ));
        
        validate( context, defn );     
    }

    /**
     * Validates that this expression meets the restriction specified
     * by the expression definition. 
     * @param defn
     * @throws OdaException  if validation fails
     */
    protected void validate( ValidationContext context, AggregateDefinition defn ) throws OdaException
    {
        assert( defn != null );
        
        // TODO - validate type of associated variable
        
        // validates the capabilities
        if( ignoresDuplicateValues() && ! defn.canIgnoreDuplicateValues() )
            throw new OdaException( 
                    Messages.bind( "The custom aggregate type ({0}) cannot ignore duplicate values.", getQualifiedId() ));
 
        if( ignoresNullValues() && ! defn.canIgnoreNullValues() )
            throw new OdaException( 
                    Messages.bind( "The custom aggregate type ({0}) cannot ignore null values.", getQualifiedId() ));

        // up to custom validator class to resolve a variable's data type and validate
        // against one of the expression's restricted data types
        IValidator customValidator = getValidator( context );
        if( customValidator != null )
            customValidator.validate( this, context );
    }
    
    protected IValidator getValidator( ValidationContext context )
    {
        // try use the validator in the context, if available
        if( context != null && context.getValidator() != null )
            return context.getValidator();

        // use validator in the definition, if specified
        try
        {
            if( getDefinition() != null )
                return getDefinition().getValidator();
        }
        catch( OdaException ex )
        {
            // TODO log warning
        }

        return null;
    }

}
