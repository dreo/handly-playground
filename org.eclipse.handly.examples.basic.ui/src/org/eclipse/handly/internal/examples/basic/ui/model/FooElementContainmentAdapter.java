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
package org.eclipse.handly.internal.examples.basic.ui.model;

import org.eclipse.core.resources.IResource;
import org.eclipse.handly.examples.basic.ui.model.FooModelCore;
import org.eclipse.handly.examples.basic.ui.model.IFooElement;
import org.eclipse.handly.model.IHandle;
import org.eclipse.handly.ui.workingset.AbstractContainmentAdapter;

/**
 * Containment adapter for Foo elements.
 */
public class FooElementContainmentAdapter
    extends AbstractContainmentAdapter
{
    @Override
    protected IHandle getElementFor(IResource resource)
    {
        IFooElement element = FooModelCore.create(resource);
        if (element != null && element.exists())
            return element;
        return null;
    }
}
