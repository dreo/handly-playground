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
import org.eclipse.handly.model.IElementChangeListener;
import org.eclipse.handly.ui.workingset.AbstractWorkingSetUpdater;

/**
 * Foo working set updater.
 */
public class FooWorkingSetUpdater
    extends AbstractWorkingSetUpdater
{
    @Override
    protected void addElementChangeListener(IElementChangeListener listener)
    {
        FooModelCore.getFooModel().addElementChangeListener(listener);
    }

    @Override
    protected void removeElementChangeListener(IElementChangeListener listener)
    {
        FooModelCore.getFooModel().removeElementChangeListener(listener);
    }
}
