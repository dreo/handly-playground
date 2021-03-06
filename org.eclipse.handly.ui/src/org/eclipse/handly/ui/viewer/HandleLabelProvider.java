/*******************************************************************************
 * Copyright (c) 2014 1C LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Vladimir Piskarev (1C) - initial API and implementation
 *******************************************************************************/
package org.eclipse.handly.ui.viewer;

import org.eclipse.handly.model.IHandle;
import org.eclipse.jface.viewers.LabelProvider;

/**
 * A basic label provider for <code>IHandle</code>s. 
 * Uses the name of the element as its text. No images.
 */
public class HandleLabelProvider
    extends LabelProvider
{
    @Override
    public String getText(Object element)
    {
        if (element instanceof IHandle)
            return ((IHandle)element).getName();
        return super.getText(element);
    };
}
