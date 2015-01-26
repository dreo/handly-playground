/*******************************************************************************
 * Copyright (c) 2015 1C LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Vladimir Piskarev (1C) - initial API and implementation
 *******************************************************************************/
package org.eclipse.handly.internal.examples.basic.ui.navigator;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.actions.WorkingSetFilterActionGroup;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;

/**
 * Provides {@link WorkingSetFilterActionGroup} for the Foo Navigator.
 */
public class WorkingSetActionProvider
    extends CommonActionProvider
{
    private WorkingSetFilterActionGroup workingSetActionGroup;
    private boolean contributedToViewMenu = false;

    @Override
    public void init(ICommonActionExtensionSite actionSite)
    {
        super.init(actionSite);
        ICommonViewerWorkbenchSite viewSite =
            (ICommonViewerWorkbenchSite)actionSite.getViewSite();
        final FooNavigator navigator = (FooNavigator)viewSite.getPart();
        IPropertyChangeListener workingSetUpdater =
            new IPropertyChangeListener()
            {
                public void propertyChange(PropertyChangeEvent event)
                {
                    String property = event.getProperty();

                    if (WorkingSetFilterActionGroup.CHANGE_WORKING_SET.equals(property))
                    {
                        Object newValue = event.getNewValue();

                        if (newValue instanceof IWorkingSet)
                        {
                            navigator.setWorkingSet((IWorkingSet)newValue);
                        }
                        else if (newValue == null)
                        {
                            navigator.setWorkingSet(null);
                        }
                    }
                }
            };
        workingSetActionGroup =
            new WorkingSetFilterActionGroup(viewSite.getShell(),
                workingSetUpdater);
        workingSetActionGroup.setWorkingSet(navigator.getWorkingSet());
    }

    @Override
    public void fillActionBars(IActionBars actionBars)
    {
        if (!contributedToViewMenu)
        {
            try
            {
                workingSetActionGroup.fillActionBars(actionBars);
            }
            finally
            {
                contributedToViewMenu = true;
            }
        }
    }

    @Override
    public void dispose()
    {
        workingSetActionGroup.dispose();
        super.dispose();
    }
}
