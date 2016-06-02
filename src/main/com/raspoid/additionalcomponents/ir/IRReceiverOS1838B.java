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
package com.raspoid.additionalcomponents.ir;

import com.raspoid.GPIOPin;
import com.raspoid.examples.additionalcomponents.ir.IRReceiverOS1838BExample;

/**
 * Implementation of a specific infrared receiver: OS1838B.
 * 
 * <p>Datasheet: <a href="http://raspoid.com/download/datasheet/IRReceiverOS1838B">OS-1838B</a></p>
 * 
 * <p>The OS-1838B is a miniaturized infrared receivers for remote
 * control and other applications requiring improved ambient light
 * rejection.<p>
 *  
 * <p>This module has excellent performance even in disturbed
 * ambient light applications and provides protection against
 * uncontrolled output pulses</p>
 * 
 * <p>Some specifications:
 *  <ul>
 *      <li>Size: square, 6.7mm by 7.6mm by 4.8mm detector area</li>
 *      <li>Output: 0.5V (low) on detection of 38KHz carrier, 4.5V (high) otherwise</li>
 *      <li>Power supply: 3-5V DC 0.35-0.6mA. Low power consumption.</li>
 *      <li>Reception distance: 12-18m.</li>
 *      <li>BPF center frequency: 38 KHz.</li>
 *      <li>Peak wavelength: 940nm.<li>
 *  </ul>
 * </p>
 * 
 * <p>Note: the sensor chip can then be operated at 3.3V or 5V,
 * when used with the Raspberry Pi.</p>
 * 
 * <p>Example of use: {@link IRReceiverOS1838BExample}</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class IRReceiverOS1838B extends IRReceiver {
    
    /**
     * Constructor for an infrared receiver OS-1838B using a specific GPIO pin.
     * @param pin the GPIO pin to use to deal with the infrared receiver.
     */
    public IRReceiverOS1838B(GPIOPin pin) {
        super(pin);
    }
}
