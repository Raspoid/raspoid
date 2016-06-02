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
import com.raspoid.examples.additionalcomponents.TouchSwitchExample;

/**
 * A touch sensor operates with the conductivity of human body.
 * When you touch the metal on the base electrode of the transistor,
 * the level of pin SIG will turn over.
 * 
 * <p>So, at each touch, the signal will go from low to high or from high to low.</p>
 * 
 * <p>Example of use: {@link TouchSwitchExample}</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class TouchSwitch extends Button {
    
    /**
     * Constructor for a touch switch using a specific Gpio pin from the Raspberry Pi.
     * @param pin the pin to use to manage the touch switch.
     */
    public TouchSwitch(GPIOPin pin) {
        super(pin);
    }
}
