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

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.raspoid.GPIOComponent;
import com.raspoid.GPIOPin;
import com.raspoid.examples.additionalcomponents.TrackingSensorExample;

/**
 * The infrared tracking sensor uses a TCRT5000 sensor.
 * The blue LED of the TCRT5000 is the emission tube and after electrified
 * it emits infrared light invisible to human eye. The black part
 * of the sensor is for receiving; the resistance of the resistor
 * inside changes with the infrared light received.
 * 
 * <p>Datasheet: <a href="http://raspoid.com/download/datasheet/tcrt5000">tcrt5000</a></p>
 * 
 * <p>Note: working distance: 12mm from the support (5mm from the leds).</p>
 * 
 * <p>Example of use: {@link TrackingSensorExample}</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class TrackingSensor extends GPIOComponent {
    
    protected final GpioPinDigitalInput digitalInput;
    
    /**
     * Constructor for a tracking sensor using a specific Gpio pin from the Raspberry Pi.
     * @param pin the pin to use to manage the tracking sensor.
     */
    public TrackingSensor(GPIOPin pin) {
        digitalInput = gpio.provisionDigitalInputPin(pin.getWiringPiPin());
    }
    
    /**
     * Returns true if white color is detected.
     * @return true if white color is detected.
     */
    public boolean whiteDetected() {
        return digitalInput.isLow();
    }
    
    /**
     * Get the pi4j GpioPinDigitalInput used for this tracking sensor.
     * <p>This can be usefull to add custom listeners to easily react when the 
     * tracking state changes.</p>
     * @return the pi4j GpioPinDigitalInput corresponding to this tracking sensor.
     */
    public GpioPinDigitalInput getGpioPinDigitalInput() {
        return digitalInput;
    }
}
