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

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

/**
 * <b>This class is used as an abstraction for each component using classical Gpio pins from the Raspberry Pi.</b>
 * 
 * <p>A classical pin means a pin that is not reserved for I2C, UART or PWM components.</p>
 * 
 * <p>You can easily access the corresponding pins using the {@link GPIOPin} enum created for this.</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class GPIOComponent implements Component {
    
    /**
     * The default instance of GpioController.
     * <p>The {@link GpioFactory#getInstance()} is not a thread safe singleton pattern implementation.
     * Implementation does not provide any synchronization or mechanisms to prevent instantiation of two instances.
     * We then use a static variable to share this GpioController instance with each {@link GPIOComponent}.</p>
     */
    protected static final GpioController gpio = GpioFactory.getInstance();
    
    /**
     * Protected constructor to hide the implicit public one.
     */
    protected GPIOComponent() {
    }
    
    @Override
    public String getType() {
        return "GPIOComponent";
    }
}
