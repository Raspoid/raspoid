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

/**
 * Implementation of the Sound Sensor.
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class SoundSensor extends RawSensor {

    /**
     * Contains the maximum value returned by the sound sensor
     * which corresponds to no sound
     */
    private static final int SOUND_PRESSURE_MAX_VAL = 1000;
    
    /**
     * Tells the sound pressure measure in percent by the sensor.
     * @return the sound pressure measured by the sensor, in the [0;100] range.
     */
    public int getSoundPressure() {
        return getValue();
    }

    @Override
    protected void setValue(int value) {
        // The value returned is the sound pressure in percent with one decimal
        // 1000 = no sound, 0 = loud sound. We invert the value and drop the
        // decimal so that it's more natural
        int volumePercent = (int)((SOUND_PRESSURE_MAX_VAL - value) * 0.1);
        super.setValue(volumePercent);
    }
}
