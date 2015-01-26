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
package org.eclipse.handly.internal.examples.basic.ui.navigator;

import org.eclipse.handly.examples.basic.ui.model.FooModelCore;
import org.eclipse.handly.internal.examples.basic.ui.Activator;
import org.eclipse.handly.model.IElementChangeEvent;
import org.eclipse.handly.model.IElementChangeListener;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.util.Util;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ResourceWorkingSetFilter;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;

/**
 * Foo Navigator view.
 * <p>
 * Note that elements inside a Foo file are automagically reconciled 
 * with the Foo editor's contents, thanks to Handly/Xtext integration.
 * </p>
 */
public class FooNavigator
    extends CommonNavigator
    implements IElementChangeListener
{
    /**
     * Foo Navigator view id.
     */
    public static final String ID =
        "org.eclipse.handly.examples.basic.ui.views.fooNavigator"; //$NON-NLS-1$

    private static final String STORE_SECTION = "FooNavigator"; //$NON-NLS-1$
    private static final String STORE_WORKING_SET =
        "ResourceWorkingSetFilter.STORE_WORKING_SET"; //$NON-NLS-1$

    private IDialogSettings settings;
    private IWorkingSet workingSet;
    private boolean emptyWorkingSet;
    private ResourceWorkingSetFilter workingSetFilter =
        new ResourceWorkingSetFilter();
    private IPropertyChangeListener workingSetListener =
        new IPropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent event)
            {
                String property = event.getProperty();
                Object newValue = event.getNewValue();
                Object oldValue = event.getOldValue();

                if (IWorkingSetManager.CHANGE_WORKING_SET_REMOVE.equals(property)
                    && oldValue == workingSet)
                {
                    setWorkingSet(null);
                }
                else if (IWorkingSetManager.CHANGE_WORKING_SET_NAME_CHANGE.equals(property)
                    && newValue == workingSet)
                {
                    updateTitle();
                }
                else if (IWorkingSetManager.CHANGE_WORKING_SET_CONTENT_CHANGE.equals(property)
                    && newValue == workingSet)
                {
                    if (workingSet.isAggregateWorkingSet()
                        && workingSet.isEmpty())
                    {
                        // act as if the working set has been made null
                        if (!emptyWorkingSet)
                        {
                            emptyWorkingSet = true;
                            workingSetFilter.setWorkingSet(null);
                        }
                    }
                    else
                    {
                        // we've gone from empty to non-empty on our set.
                        // Restore it.
                        if (emptyWorkingSet)
                        {
                            emptyWorkingSet = false;
                            workingSetFilter.setWorkingSet(workingSet);
                        }
                    }
                    refresh();
                }
            }
        };

    /**
     * Constructs a new Foo Navigator view.
     */
    public FooNavigator()
    {
        IDialogSettings viewsSettings =
            Activator.getInstance().getDialogSettings();
        settings = viewsSettings.getSection(STORE_SECTION);
        if (settings == null)
            settings = viewsSettings.addNewSection(STORE_SECTION);
    }

    /**
     * Sets the working set for this view, or <code>null</code> to clear it.
     * 
     * @param workingSet the working set, or <code>null</code> to clear it
     */
    public void setWorkingSet(IWorkingSet workingSet)
    {
        boolean refreshNeeded = internalSetWorkingSet(workingSet);

        workingSetFilter.setWorkingSet(emptyWorkingSet ? null : workingSet);

        if (workingSet != null)
            settings.put(STORE_WORKING_SET, workingSet.getName());
        else
            settings.put(STORE_WORKING_SET, ""); //$NON-NLS-1$

        updateTitle();
        if (refreshNeeded)
        {
            refresh();
        }
    }

    /**
     * Returns the active working set, or <code>null<code> if none.
     *
     * @return the active working set, or <code>null<code> if none
     */
    public IWorkingSet getWorkingSet()
    {
        return workingSetFilter.getWorkingSet();
    }

    @Override
    public CommonViewer createCommonViewer(Composite aParent)
    {
        CommonViewer viewer = super.createCommonViewer(aParent);
        initWorkingSetFilter();
        viewer.addFilter(workingSetFilter);
        FooModelCore.getFooModel().addElementChangeListener(this);
        IWorkingSetManager workingSetManager =
            PlatformUI.getWorkbench().getWorkingSetManager();
        workingSetManager.addPropertyChangeListener(workingSetListener);
        return viewer;
    }

    @Override
    public void dispose()
    {
        IWorkingSetManager workingSetManager =
            PlatformUI.getWorkbench().getWorkingSetManager();
        workingSetManager.removePropertyChangeListener(workingSetListener);
        FooModelCore.getFooModel().removeElementChangeListener(this);
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
        return FooModelCore.getFooModel();
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

    private boolean internalSetWorkingSet(IWorkingSet workingSet)
    {
        boolean refreshNeeded = !Util.equals(this.workingSet, workingSet);
        this.workingSet = workingSet;
        emptyWorkingSet =
            workingSet != null && workingSet.isAggregateWorkingSet()
                && workingSet.isEmpty();
        return refreshNeeded;
    }

    private void initWorkingSetFilter()
    {
        String workingSetName = settings.get(STORE_WORKING_SET);

        IWorkingSet workingSet = null;

        if (workingSetName != null && !workingSetName.isEmpty())
        {
            IWorkingSetManager workingSetManager =
                PlatformUI.getWorkbench().getWorkingSetManager();
            workingSet = workingSetManager.getWorkingSet(workingSetName);
        }
        else if (PlatformUI.getPreferenceStore().getBoolean(
            IWorkbenchPreferenceConstants.USE_WINDOW_WORKING_SET_BY_DEFAULT))
        {
            // use the window set by default if the global preference is set
            workingSet = getSite().getPage().getAggregateWorkingSet();
        }

        if (workingSet != null)
        {
            // Only initialize filter. Don't set working set into viewer.
            // Working set is set via WorkingSetFilterActionGroup
            // during action creation.
            workingSetFilter.setWorkingSet(workingSet);
            internalSetWorkingSet(workingSet);
        }
    }
}
