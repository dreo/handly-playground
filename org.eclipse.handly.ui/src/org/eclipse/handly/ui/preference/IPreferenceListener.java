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
 * Listener for preference changes.
 * <p>
 * This interface may be implemented by clients.
 * </p>
 */
public interface IPreferenceListener
{
    /**
     * Notification that a preference has changed.
     * <p>
     * <b>Note</b> A listener will be called in the same thread that it is
     * invoked in. Any thread-dependent listeners (such as those that update
     * an SWT widget) will need to update in the correct thread. In the case
     * of an SWT update you can update using Display#syncExec(Runnable) or
     * Display#asyncExec(Runnable).
     * </p>
     *
     * @param event describes which preference changed and how 
     *  (never <code>null</code>)
     */
    void preferenceChanged(PreferenceChangeEvent event);
}
