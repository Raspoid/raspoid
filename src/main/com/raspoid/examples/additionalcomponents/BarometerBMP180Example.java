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

import com.raspoid.Tools;
import com.raspoid.additionalcomponents.BarometerBMP180;

/**
 * Example of use of a Barometer BMP180.
 * 
 * @see BarometerBMP180
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class BarometerBMP180Example {
    
    /**
     * Private constructor to hide the implicit public one.
     */
    private BarometerBMP180Example() {
    }
    
    /**
     * Command-line interface.
     * @param args unused here.
     */
    public static void main(String[] args) {
        BarometerBMP180 barometer = new BarometerBMP180();
        
        while(true) {
            Tools.log(
                    "Air pressure: " + barometer.readUncompensatedPressure() + " " +
                    "True air pressure: " + barometer.calculateTruePressure() + " Pa\n" +
                    "Temperature: " + barometer.readUncompensatedTemperature() + " " +
                    "True temperature: " + barometer.calculateTrueTemperature() + "°C\n" +
                    "Altitude: " + barometer.calculateAbsoluteAltitude() + "m",
                    Tools.Color.ANSI_GREEN);
            Tools.sleepMilliseconds(1000);
        }
    }
}
