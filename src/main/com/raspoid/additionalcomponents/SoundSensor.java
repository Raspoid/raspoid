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
package com.raspoid.additionalcomponents;

import com.raspoid.AnalogComponent;
import com.raspoid.additionalcomponents.adc.ADC;
import com.raspoid.additionalcomponents.adc.ADCChannel;
import com.raspoid.examples.additionalcomponents.SoundSensorExample;

/**
 * Sound sensor is a component that receives sound waves and converts them into electrical
 * signal. It detects the sound intensity in ambient environment like a microphone.
 * 
 * <p>Example Datasheet: <a href="http://raspoid.com/download/datasheet/SoundSensor">SoundSensor</a></p>
 * 
 * <p>Example of use: {@link SoundSensorExample}</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class SoundSensor extends AnalogComponent {

    private ADCChannel soundSensorChannel;
    
    /**
     * Constructor for a sound sensor using a specific ADC instance and a specific
     * channel on this ADC to decode analog input from the sound sensor.
     * @param adc the ADC to use to decode analogic signals from the sound sensor.
     * @param soundSensorChannel the channel on the ADC used to read inputs from the sound sensor.
     */
    public SoundSensor(ADC adc, ADCChannel soundSensorChannel) {
        super(adc);
        this.soundSensorChannel = soundSensorChannel;
    }
    
    /**
     * Get the sound intensity read from the sound sensor.
     * @return the sound intensity read from the sound sensor.
     */
    public int getIntensity() {
        return adc.analogToDigital(soundSensorChannel);
    }
}
