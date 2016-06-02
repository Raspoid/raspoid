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
 * <b>This enum is used to restrict access to the correct UART pins.</b>
 * 
 * <p>UART is the abreviation for Universal Synchronous & Asynchronous Receiver Transmitter.</p>
 * 
 * <p>In this framework, UART is mainly used for the communications with the BrickPi.</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public enum UARTPin {
    /**
     * TXD. Transmit pin.
     * @see Pin#PHYSICAL_08
     */
    TXD(Pin.PHYSICAL_08),
    /**
     * RXD. Receive pin.
     * @see Pin#PHYSICAL_10
     */
    RXD(Pin.PHYSICAL_10);
    
    Pin pin;
    
    UARTPin(Pin pin) {
        this.pin = pin;
    }
    
    /**
     * Get the Raspoid pin abstraction for this UARTPin.
     * @return the Raspoid Pin object corresponding to this UARTPin.
     * @see Pin
     */
    public Pin getPin() {
        return pin;
    }
}
