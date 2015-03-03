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

import java.text.MessageFormat;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.handly.examples.javamodel.ICompilationUnit;
import org.eclipse.handly.examples.javamodel.IJavaElement;
import org.eclipse.handly.examples.javamodel.IJavaProject;
import org.eclipse.handly.examples.javamodel.IMember;
import org.eclipse.handly.examples.javamodel.IMethod;
import org.eclipse.handly.examples.javamodel.IPackageFragment;
import org.eclipse.handly.examples.javamodel.IPackageFragmentRoot;
import org.eclipse.handly.examples.javamodel.IType;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.model.IWorkbenchAdapter;

/**
 * Label provider for Java model.
 */
public class JavaLabelProvider
    extends LabelProvider
    implements IStyledLabelProvider
{
    private ResourceManager resourceManager = new LocalResourceManager(
        JFaceResources.getResources());

    @Override
    public StyledString getStyledText(Object element)
    {
        String text = getText(element);
        if (text == null)
            return new StyledString();
        else
            return new StyledString(text);
    }

    @Override
    public String getText(Object element)
    {
        if (element instanceof IMethod)
            return MessageFormat.format("{0}()", //$NON-NLS-1$
                ((IMethod)element).getName());
        if (element instanceof IJavaElement)
            return ((IJavaElement)element).getName();
        if (element instanceof IResource)
            return ((IResource)element).getName();
        return super.getText(element);
    };

    @Override
    public Image getImage(Object element)
    {
        if (element instanceof IPackageFragmentRoot)
            return JavaUI.getSharedImages().getImage(
                ISharedImages.IMG_OBJS_PACKFRAG_ROOT);
        if (element instanceof IPackageFragment)
            return JavaUI.getSharedImages().getImage(
                ISharedImages.IMG_OBJS_PACKAGE);
        if (element instanceof IType)
        {
            IType type = (IType)element;
            try
            {
                if (type.isClass())
                    return JavaUI.getSharedImages().getImage(
                        ISharedImages.IMG_OBJS_CLASS);
                if (type.isInterface())
                    return JavaUI.getSharedImages().getImage(
                        ISharedImages.IMG_OBJS_INTERFACE);
                if (type.isAnnotation())
                    return JavaUI.getSharedImages().getImage(
                        ISharedImages.IMG_OBJS_ANNOTATION);
                if (type.isEnum())
                    return JavaUI.getSharedImages().getImage(
                        ISharedImages.IMG_OBJS_ENUM);
            }
            catch (CoreException e)
            {
            }
        }
        if (element instanceof IMember)
        {
            try
            {
                IMember member = (IMember)element;
                int flags = member.getFlags();
                if ((flags & Flags.AccPublic) == Flags.AccPublic)
                    return JavaUI.getSharedImages().getImage(
                        ISharedImages.IMG_OBJS_PUBLIC);
                if ((flags & Flags.AccProtected) == Flags.AccProtected)
                    return JavaUI.getSharedImages().getImage(
                        ISharedImages.IMG_OBJS_PROTECTED);
                if ((flags & Flags.AccPrivate) == Flags.AccPrivate)
                    return JavaUI.getSharedImages().getImage(
                        ISharedImages.IMG_OBJS_PRIVATE);
                if ((flags & Flags.AccDefault) == Flags.AccDefault)
                    return JavaUI.getSharedImages().getImage(
                        ISharedImages.IMG_OBJS_DEFAULT);
            }
            catch (CoreException e)
            {
            }
        }
        IResource resource = null;
        if (element instanceof IJavaProject
            || element instanceof ICompilationUnit)
            resource = ((IJavaElement)element).getResource();
        if (element instanceof IResource)
            resource = (IResource)element;
        if (resource != null)
        {
            IWorkbenchAdapter adapter =
                (IWorkbenchAdapter)resource.getAdapter(IWorkbenchAdapter.class);
            if (adapter != null)
                return (Image)resourceManager.get(adapter.getImageDescriptor(resource));
        }
        return super.getImage(element);
    }

    @Override
    public void dispose()
    {
        resourceManager.dispose();
        super.dispose();
    }

}
