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
package com.raspoid.brickpi;

import java.util.ArrayList;
import java.util.List;

import com.raspoid.brickpi.nxt.ValueListener;
import com.raspoid.brickpi.nxt.sensor.SensorType;
import com.raspoid.brickpi.nxt.RangedValueListener;
import com.raspoid.brickpi.nxt.ValueChangeEvent;

/**
 * Abstract class implementing a Sensor
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public abstract class Sensor {

    /**
     * Contains the listeners without range argument needed.
     */
    protected List<ValueListener> listeners = new ArrayList<>();

    /**
     * Contains the listeners with range argument needed.
     * Distinction needed due to java 8 utilization of lambda expressions.
     * Each interface can contain at most one method.
     */
    protected List<RangedValueListener> listenersWithRange = new ArrayList<>();

    /**
     * Contains the current value of the sensor.
     */
    protected int value;
    
    /**
     * Creates a new sensor without attaching it to the BrickPi
     */
    protected Sensor() {}
    
    /**
     * Get the sensor type
     * @return the sensor type
     */
    public abstract SensorType getType();

    /**
     * Get the value of the sensor
     * @return the sensor value
     */
    public int getValue() {
        return value;
    }

    /**
     * Set the value of the sensor, listeners will be triggered when updating this value
     * to a different value than the previous one
     * @param value the new sensor value to be set
     */
    protected void setValue(int value) {
        int oldValue = this.value;
        this.value = value;
        if (oldValue != value) {
            for (ValueListener listener : listeners) {
                listener.notifyUpdate(new ValueChangeEvent(oldValue, value));
            }
            for (RangedValueListener listener : listenersWithRange) {
                listener.notifyUpdate(new ValueChangeEvent(oldValue, value));
            }
        }
    }
    
    /**
     * Registers a listener on the value of the sensor, 
     * the listener will be triggered each time it changes.
     * @param listener the listener to be triggered
     */
    public void onChange(ValueListener listener) {
        this.addListener(listener);
    }
    
    /**
     * Register a new listener with a range. The listener will only be triggered if the difference
     * between the new value and the old exceeds the range
     * @param range the range to be set
     * @param listener the listener that will be triggered
     */
    public void onChange(int range, ValueListener listener) {
        this.addListenerWithRange(new RangedValueListener(range, listener));
    }

    /**
     * Add a value listener to the sensor
     * @param listener the listener to be triggered when the value changes
     */
    protected void addListener(ValueListener listener) {
        if(!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Add a value listener with range to the sensor
     * @param listener the ranged value listener to be triggered 
     * when the value is out of the range
     */
    protected void addListenerWithRange(RangedValueListener listener) {
        if(!listenersWithRange.contains(listener)) {
            listenersWithRange.add(listener);
        }
    }
}
