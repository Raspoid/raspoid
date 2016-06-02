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

import com.raspoid.GPIOPin;
import com.raspoid.examples.additionalcomponents.AutoFlashLEDExample;

/**
 * On the Synfounder 7-Color Auto-flash LED, the LED can automatically flash
 * built-in colors after power on.
 * 
 * <p>It can be used to make quite fascinating light effects.</p>
 * 
 * <p>7-Color Auto-flash LED Module: <a href="http://www.sunfounder.com/7-color-auto-flash-led-module.html">sunfounder.com/7-color-auto-flash-led-module.html</a></p>
 * 
 * <p>Example of use: {@link AutoFlashLEDExample}</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class AutoFlashLED extends LED {
    
    /**
     * Constructor for an Auto-Flash LED, using a specific Gpio pin.
     * @param pin the Gpio pin used to access the Auto-Flash LED.
     */
    public AutoFlashLED(GPIOPin pin) {
        super(pin);
    }
}
