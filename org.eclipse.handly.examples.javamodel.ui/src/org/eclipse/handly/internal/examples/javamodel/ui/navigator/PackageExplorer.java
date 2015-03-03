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
package org.eclipse.handly.internal.examples.javamodel.ui.navigator;

import org.eclipse.handly.examples.javamodel.JavaModelCore;
import org.eclipse.handly.model.IElementChangeEvent;
import org.eclipse.handly.model.IElementChangeListener;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.navigator.CommonNavigator;

/**
 * Package explorer view.
 */
public class PackageExplorer
    extends CommonNavigator
    implements IElementChangeListener
{

    /**
     * Foo Navigator view id.
     */
    public static final String ID =
        "org.eclipse.handly.examples.javamodel.ui.views.PackageExplorer"; //$NON-NLS-1$

    @Override
    public void init(IViewSite site) throws PartInitException
    {
        super.init(site);
        JavaModelCore.getJavaModel().addElementChangeListener(this);
    }

    @Override
    public void dispose()
    {
        JavaModelCore.getJavaModel().removeElementChangeListener(this);
        super.dispose();
    }

    @Override
    public void elementChanged(IElementChangeEvent event)
    {
        // NOTE: don't hold on the event or its delta.
        // The delta is only valid during the dynamic scope of the notification.
        // In particular, don't pass it to another thread (e.g. via asyncExec). 
        final Control control = getCommonViewer().getControl();
        control.getDisplay().asyncExec(new Runnable()
        {
            public void run()
            {
                if (!control.isDisposed())
                {
                    refresh(); // full refresh should suffice for our example (but not for production code)
                }
            }
        });
    }

    @Override
    protected Object getInitialInput()
    {
        return JavaModelCore.getJavaModel();
    }

    private void refresh()
    {
        Control control = getCommonViewer().getControl();
        control.setRedraw(false);
        BusyIndicator.showWhile(control.getDisplay(), new Runnable()
        {
            public void run()
            {
                TreePath[] treePaths = getCommonViewer().getExpandedTreePaths();
                getCommonViewer().refresh();
                getCommonViewer().setExpandedTreePaths(treePaths);
            }
        });
        control.setRedraw(true);
    }

}
