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
package com.raspoid;

/**
 * <b>This enum is used to restrict access to the correct PWM pins.</b>
 * 
 * <p>Only 2 hardware PWM pins are available on recent Raspberry Pi.</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public enum PWMPin {
    /**
     * PWM0.
     * @see Pin#PHYSICAL_13
     */
    PWM0(Pin.PHYSICAL_12),
    /**
     * PWM1.
     * @see Pin#PHYSICAL_33
     */
    PWM1(Pin.PHYSICAL_33);
    
    Pin pin;
    
    PWMPin(Pin pin) {
        this.pin = pin;
    }
    
    /**
     * Get the Raspoid pin abstraction for this PWMPin.
     * @return the Raspoid Pin object corresponding to this PWMPin.
     * @see Pin
     */
    public Pin getPin() {
        return pin;
    }
}
