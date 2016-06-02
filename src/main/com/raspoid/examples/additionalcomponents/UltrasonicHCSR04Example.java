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

import com.raspoid.GPIOPin;
import com.raspoid.Tools;
import com.raspoid.additionalcomponents.UltrasonicHCSR04;

/**
 * Example of use of the ultrasonic HCSR04 component.
 * 
 * @see UltrasonicHCSR04
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class UltrasonicHCSR04Example {
    
    /**
     * Private constructor to hide the implicit public one.
     */
    private UltrasonicHCSR04Example() {
    }
    
    /**
     * Command-line interface.
     * @param args unused here.
     */
    public static void main(String[] args) {
        UltrasonicHCSR04 ultrasonicSensor = new UltrasonicHCSR04(GPIOPin.GPIO_29, GPIOPin.GPIO_28);

        while(true) {
            Tools.log("2: " + ultrasonicSensor.getDistance() + " cm");// Good
            Tools.sleepMilliseconds(500);
        }
    }
}
