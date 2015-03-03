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
package org.eclipse.handly.internal.examples.javamodel.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.handly.examples.javamodel.ICompilationUnit;
import org.eclipse.handly.examples.javamodel.IJavaElement;
import org.eclipse.handly.examples.javamodel.IJavaProject;
import org.eclipse.handly.examples.javamodel.IPackageFragment;
import org.eclipse.handly.examples.javamodel.IPackageFragmentRoot;
import org.eclipse.handly.examples.javamodel.JavaModelCore;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Content provider for Java model. 
 */
public class JavaContentProvider
    implements ITreeContentProvider
{
    protected static final Object[] NO_CHILDREN = new Object[0];

    @Override
    public Object[] getElements(Object inputElement)
    {
        return getChildren(inputElement);
    }

    @Override
    public Object[] getChildren(Object parentElement)
    {
        if (parentElement instanceof IJavaProject)
        {
            try
            {
                Object[] children = ((IJavaElement)parentElement).getChildren();
                Object[] nonFooResources =
                    ((IJavaProject)parentElement).getNonJavaResources();
                return concat(children, nonFooResources);
            }
            catch (CoreException e)
            {
            }
        }
        if (parentElement instanceof IPackageFragmentRoot)
        {
            try
            {
                // keep only non-empty fragments
                List<IPackageFragment> children =
                    new ArrayList<IPackageFragment>(
                        Arrays.asList(((IPackageFragmentRoot)parentElement).getPackageFragments()));
                Iterator<IPackageFragment> it = children.iterator();
                while (it.hasNext())
                {
                    if (it.next().getCompilationUnits().length == 0)
                        it.remove();
                }
                Object[] nonJavaResources =
                    ((IPackageFragmentRoot)parentElement).getNonJavaResources();
                return concat(children.toArray(), nonJavaResources);
            }
            catch (CoreException e)
            {
            }
        }
        if (parentElement instanceof IPackageFragment)
        {
            try
            {
                Object[] children =
                    ((IPackageFragment)parentElement).getCompilationUnits();
                Object[] nonJavaResources =
                    ((IPackageFragment)parentElement).getNonJavaResources();
                return concat(children, nonJavaResources);
            }
            catch (CoreException e)
            {
            }
        }
        if (parentElement instanceof ICompilationUnit)
        {
            try
            {
                return ((ICompilationUnit)parentElement).getTypes();
            }
            catch (CoreException e)
            {
            }
        }
        if (parentElement instanceof IJavaElement)
        {
            try
            {
                return ((IJavaElement)parentElement).getChildren();
            }
            catch (CoreException e)
            {
            }
        }
        if (parentElement instanceof IFolder)
        {
            try
            {
                return ((IFolder)parentElement).members();
            }
            catch (CoreException e)
            {
            }
        }
        return NO_CHILDREN;
    }

    @Override
    public Object getParent(Object element)
    {
        if (element instanceof IJavaElement)
            return ((IJavaElement)element).getParent();
        if (element instanceof IResource)
        {
            IContainer parent = ((IResource)element).getParent();
            if (parent instanceof IFolder)
                return parent;
            if (parent instanceof IProject)
                return JavaModelCore.create(parent); // FooProject
        }
        return null;
    }

    @Override
    public boolean hasChildren(Object element)
    {
        return getChildren(element).length > 0;
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
    }

    @Override
    public void dispose()
    {
    }

    private static Object[] concat(Object[] a, Object[] b)
    {
        Object[] c = new Object[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

}
