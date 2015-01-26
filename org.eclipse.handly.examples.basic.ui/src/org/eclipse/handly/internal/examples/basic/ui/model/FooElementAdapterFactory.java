/*******************************************************************************
 * Copyright (c) 2014, 2015 1C LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Vladimir Piskarev (1C) - initial API and implementation
 *******************************************************************************/
package org.eclipse.handly.internal.examples.basic.ui.model;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.handly.examples.basic.ui.model.IFooElement;
import org.eclipse.handly.examples.basic.ui.model.IFooFile;
import org.eclipse.handly.examples.basic.ui.model.IFooModel;
import org.eclipse.handly.examples.basic.ui.model.IFooProject;
import org.eclipse.ui.IContainmentAdapter;
import org.eclipse.ui.IPersistableElement;

/**
 * Adapter factory for Foo elements.
 */
public class FooElementAdapterFactory
    implements IAdapterFactory
{
    private static Class<?>[] ADAPTER_LIST = new Class<?>[] { IResource.class,
        IPersistableElement.class, IContainmentAdapter.class };

    private static IContainmentAdapter containmentAdapter =
        new FooElementContainmentAdapter();

    @Override
    public Object getAdapter(Object adaptableObject,
        @SuppressWarnings("rawtypes") Class adapterType)
    {
        IFooElement element = (IFooElement)adaptableObject;
        if (adapterType == IResource.class)
            return getResource(element);
        if (adapterType == IPersistableElement.class)
            return new PersistableFooElementFactory(element);
        if (adapterType == IContainmentAdapter.class)
            return containmentAdapter;
        return null;
    }

    @Override
    public Class<?>[] getAdapterList()
    {
        return ADAPTER_LIST;
    }

    private IResource getResource(IFooElement element)
    {
        if (element instanceof IFooModel || element instanceof IFooProject
            || element instanceof IFooFile)
        {
            return element.getResource();
        }
        return null;
    }
}
