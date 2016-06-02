/*******************************************************************************
 * Copyright (c) 2016 Julien Louette & Gaël Wittorski
 * 
 * This file is part of Raspoid.
 * 
 * Raspoid is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Raspoid is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Raspoid.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.raspoid.brickpi.nxt;

/**
 * Implements a Ranged value change listener. The listener is triggered only if the
 * new value is exceeding the initial value plus (or minus) the range.
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class RangedValueListener implements ValueListener {

    /**
     * The range to be exceeded for triggering
     */
    private int range;

    /**
     * Contains the underlying ValueListener
     */
    private ValueListener listener;

    /**
     * Contains the initial value of the listener
     */
    private int initialValue;

    /**
     * Creates a RangedValueListener with the specified range and listener
     * @param range the range to be exceeded
     * @param listener the listener to be triggered
     */
    public RangedValueListener(int range, ValueListener listener) {
        this.range = range;
        this.listener = listener;
    }

    /**
     * Get the listener triggered
     * @return the listener
     */
    public ValueListener getListener() {
        return listener;
    }

    /**
     * Get the range
     * @return the range used for this listener.
     */
    public int getRange() {
        return range;
    }

    /**
     * Get the the inital value
     * @return the initial value of this listener.
     */
    public int getInitialValue() {
        return initialValue;
    }
    
    /**
     * Reset the initial value
     * @param initialValue the initial value to be set
     */
    public void setInitialValue(int initialValue) {
        this.initialValue = initialValue;
    }

    /**
     * Get the old inital value, and reset it with a new one
     * @param newInitialValue the new initial value
     * @return the old initial value
     */
    private int getAndSetInitialValue(int newInitialValue) {
        int oldInitialValue = this.initialValue;
        this.initialValue = newInitialValue;
        return oldInitialValue;
    }

    /**
     * Checks whether the range is exceeded by a new value
     * @param newValue the newValue to be checked
     * @return true if the range is exceeded, false otherwise
     */
    private boolean isRangeExceeded(int newValue) {
        return Math.abs(newValue - initialValue) > range;
    }
    
    /**
     * Notify the update only if the new value is exceeding the range of
     * the initial value
     */
    @Override
    public void notifyUpdate(ValueChangeEvent evt) {
        if (isRangeExceeded(evt.getNewValue())) {
            listener.notifyUpdate(new ValueChangeEvent(getAndSetInitialValue(evt.getNewValue()), evt.getNewValue()));
        }
    }
}
