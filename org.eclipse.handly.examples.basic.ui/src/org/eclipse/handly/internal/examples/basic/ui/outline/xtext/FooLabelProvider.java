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
package org.eclipse.handly.internal.examples.basic.ui.outline.xtext;

import org.eclipse.handly.examples.basic.foo.Def;
import org.eclipse.handly.examples.basic.foo.Var;
import org.eclipse.handly.internal.examples.basic.ui.Activator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.xtext.ui.label.ILabelProviderImageDescriptorExtension;
import org.eclipse.xtext.util.Strings;

/**
 * Foo label provider.
 */
public class FooLabelProvider
    extends LabelProvider
    implements ILabelProviderImageDescriptorExtension
{
    @Override
    public String getText(Object element)
    {
        if (element instanceof Def)
        {
            Def def = (Def)element;
            if (def.getParams().isEmpty())
                return def.getName() + "()";
            else
                return def.getName() + '('
                    + Strings.concat(", ", def.getParams()) + ')';
        }
        if (element instanceof Var)
            return ((Var)element).getName();
        return null;
    };

    @Override
    public ImageDescriptor getImageDescriptor(Object element)
    {
        if (element instanceof Def)
            return Activator.getImageDescriptor(Activator.IMG_OBJ_DEF);
        if (element instanceof Var)
            return Activator.getImageDescriptor(Activator.IMG_OBJ_VAR);
        return null;
    }
}
