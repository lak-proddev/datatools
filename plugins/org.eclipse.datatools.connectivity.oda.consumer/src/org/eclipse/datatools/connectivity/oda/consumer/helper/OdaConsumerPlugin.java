/*
 *************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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

package org.eclipse.datatools.connectivity.oda.consumer.helper;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.datatools.connectivity.oda.consumer.util.manifest.ExtensionExplorer;
import org.osgi.framework.BundleContext;

/**
 * Bundle activator class.
 */
public class OdaConsumerPlugin extends Plugin
{

    /* (non-Javadoc)
     * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop( BundleContext context ) throws Exception
    {
        // release plugin's singleton instances
        ExtensionExplorer.releaseInstance();
        
        super.stop( context );
    }

}
