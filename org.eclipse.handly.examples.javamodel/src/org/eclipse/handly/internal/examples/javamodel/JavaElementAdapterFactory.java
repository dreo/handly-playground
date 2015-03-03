/*******************************************************************************
 * Copyright (c) 2015 Codasip Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ondrej Ilcik (Codasip) - initial API and implementation
 *******************************************************************************/
package org.eclipse.handly.internal.examples.javamodel;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.handly.examples.javamodel.ICompilationUnit;
import org.eclipse.handly.examples.javamodel.IJavaElement;
import org.eclipse.handly.examples.javamodel.IJavaModel;
import org.eclipse.handly.examples.javamodel.IJavaProject;

public class JavaElementAdapterFactory
    implements IAdapterFactory
{

    private static Class<?>[] ADAPTER_LIST = new Class<?>[] { IResource.class };

    @Override
    public Object getAdapter(Object adaptableObject,
        @SuppressWarnings("rawtypes") Class adapterType)
    {
        IJavaElement element = (IJavaElement)adaptableObject;
        if (adapterType == IResource.class)
            return getResource(element);
        return null;
    }

    @Override
    public Class<?>[] getAdapterList()
    {
        return ADAPTER_LIST;
    }

    private IResource getResource(IJavaElement element)
    {
        if (element instanceof IJavaModel || element instanceof IJavaProject
            || element instanceof ICompilationUnit)
        {
            return element.getResource();
        }
        return null;
    }

}
