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
package org.eclipse.handly.internal.examples.basic.ui.workingset;

import org.eclipse.handly.examples.basic.ui.model.FooModelCore;
import org.eclipse.handly.examples.basic.ui.model.IFooFile;
import org.eclipse.handly.internal.examples.basic.ui.FooContentProvider;
import org.eclipse.handly.internal.examples.basic.ui.FooLabelProvider;
import org.eclipse.handly.ui.workingset.AbstractWorkingSetPage;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * The Foo working set page allows the user to create and edit a Foo working set.
 */
public class FooWorkingSetPage
    extends AbstractWorkingSetPage
{
    /**
     * Default constructor.
     */
    public FooWorkingSetPage()
    {
        super("fooWorkingSetPage", "Foo Working Set", null); //$NON-NLS-1$
    }

    @Override
    protected String getPageId()
    {
        return "org.eclipse.handly.examples.basic.ui.FooWorkingSetPage"; //$NON-NLS-1$
    }

    @Override
    protected void configureTree(TreeViewer tree)
    {
        tree.setContentProvider(new FooContentProvider()
        {
            @Override
            public Object[] getChildren(Object parentElement)
            {
                if (parentElement instanceof IFooFile)
                    return NO_CHILDREN;
                return super.getChildren(parentElement);
            }
        });
        tree.setLabelProvider(new FooLabelProvider());
        tree.setInput(FooModelCore.getFooModel());
    }

    @Override
    protected void configureTable(TableViewer table)
    {
        table.setLabelProvider(new FooLabelProvider());
    }
}
