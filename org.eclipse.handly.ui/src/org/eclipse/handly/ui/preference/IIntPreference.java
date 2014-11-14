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
package org.eclipse.handly.ui.preference;

/**
 * Represents an integer-valued preference.
 * 
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 */
public interface IIntPreference
    extends IPreference
{
    /**
     * Returns the current value of this preference.
     * 
     * @return the current value of this preference
     */
    int getValue();

    /**
     * Sets the current value of this preference.
     * <p>
     * A preference change event is reported if the current value
     * of the preference actually changes from its previous value.
     * </p>
     *
     * @param value the new current value of this preference
     */
    void setValue(int value);
}
