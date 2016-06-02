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
package com.raspoid.examples.additionalcomponents;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.raspoid.GPIOPin;
import com.raspoid.Tools;
import com.raspoid.additionalcomponents.RotaryEncoder;

/**
 * Example of use of a Rotary Encoder.
 * 
 * @see RotaryEncoder
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class RotaryEncoderExample {
    
    /**
     * Private constructor to hide the implicit public one.
     */
    private RotaryEncoderExample() {
    }
    
    /**
     * Command-line interface.
     * @param args unused here.
     */
    public static void main(String[] args) {
        RotaryEncoder rotaryEncoder = new RotaryEncoder(GPIOPin.GPIO_27, GPIOPin.GPIO_28, GPIOPin.GPIO_29);
        
        rotaryEncoder.getGpioPinDigitalInput().addListener((GpioPinListenerDigital)
                (GpioPinDigitalStateChangeEvent event) -> Tools.log(rotaryEncoder.isPressed() ? "rotary pressed" : "rotary released"));
        
        int previousCounterValue = rotaryEncoder.getCounterValue();
        while(true) {
            rotaryEncoder.getEncoderTurn();
            if(previousCounterValue != rotaryEncoder.getCounterValue()) {
                Tools.log(rotaryEncoder.getCounterValue());
                previousCounterValue = rotaryEncoder.getCounterValue();
            }
        }
    }
}
