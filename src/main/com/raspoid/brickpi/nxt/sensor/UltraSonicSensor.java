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
package com.raspoid.brickpi.nxt.sensor;

import static com.raspoid.brickpi.nxt.sensor.SensorType.TYPE_SENSOR_ULTRASONIC_CONT;

import com.raspoid.brickpi.Sensor;
import com.raspoid.brickpi.nxt.ValueListener;
import com.raspoid.brickpi.nxt.RangedValueListener;

/**
 * Implementation of the Mindstorm Ultrasonic Sensor.
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class UltraSonicSensor extends Sensor {

    @Override
    public SensorType getType() {
        return TYPE_SENSOR_ULTRASONIC_CONT;
    }

    /**
     * Tells the last distance measured in centimeter by the sensor
     * @return the distance measured by the sensor, in the [0;255] range.
     */
    public int getDistance() {
        return super.value;
    }
    
    /**
     * Register a new listener with a range. The listener will only be triggered if the difference
     * between the new value and the old exceeds the range
     * @param range the range to be set
     * @param listener the listener to be triggered
     */
    @Override
    public void onChange(int range, ValueListener listener) {
        this.addListenerWithRange(new RangedValueListener(range, listener));
    }
}
