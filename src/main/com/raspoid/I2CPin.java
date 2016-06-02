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
 * <b>This enum is used to restrict access to the correct i2c pins.</b>
 * 
 * <p>The I2C bus was designed by Philips in the early ’80s to allow easy
 * communication between components which reside on the same circuit board.
 * Only two bus lines are required:
 * <ul>
 *  <li>SDA (Serial Data Line): bidirectional data line.
 *  <li>SCL (Serial Clock Line): clock for bidirectional synchronization.
 * </ul>
 * </p>
 * 
 * <p>Those bus lines can easily be accessed through this enumeration.</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public enum I2CPin {
    /**
     * Serial Data Line pin.
     */
    SDA1(Pin.PHYSICAL_03),
    /**
     * Serial Clock Line pin.
     */
    SCL1(Pin.PHYSICAL_05);
    
    Pin pin;
    
    I2CPin(Pin pin) {
        this.pin = pin;
    }
    
    /**
     * Get the Raspoid pin abstraction for this I2CPin.
     * @return the Raspoid Pin object corresponding to this I2CPin.
     * @see Pin
     */
    public Pin getPin() {
        return pin;
    }
}
