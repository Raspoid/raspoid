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

import com.pi4j.io.gpio.RaspiPin;

/**
 * <b>This enum is used to restrict access to classical programmable Gpio pins.</b>
 * 
 * <p>Those GPIO numbers correspond to those used by WiringPi.</p>
 * 
 * <p>The Raspberry Pi with 40-pin expansion header 
 * (cfr <a href="http://pi4j.com/pins/model-2b-rev1.html">J8 Pinout (40-pin Header)</a>) provides
 * access to 26 programmable GPIO pins ([00,16] + [21,29]
 * (GPIO [17,20] are not existing, for historical compatibility
 * reason with Raspberry Pi Model B (Revision 2.0) additional P5 header).</p>
 * 
 * <p>We removed I2C, PWM and UART pins from those GPIO pins
 * to avoid compatibility issues. You can then freely access each programmable 
 * GPIO pin from this enum to control LEDs, buttons, and others.</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public enum GPIOPin {
    /**
     * Gpio Pin 0 (WiringPi numbering).
     * @see Pin#PHYSICAL_11
     */
    GPIO_00(Pin.PHYSICAL_11),
    // PWM0
    //GPIO_01(Pin.PHYSICAL_12),
    /**
     * Gpio Pin 2 (WiringPi numbering).
     * @see Pin#PHYSICAL_13
     */
    GPIO_02(Pin.PHYSICAL_13),
    /**
     * Gpio Pin 3 (WiringPi numbering).
     * @see Pin#PHYSICAL_15
     */
    GPIO_03(Pin.PHYSICAL_15),
    /**
     * Gpio Pin 4 (WiringPi numbering).
     * @see Pin#PHYSICAL_16
     */
    GPIO_04(Pin.PHYSICAL_16),
    /**
     * Gpio Pin 5 (WiringPi numbering).
     * @see Pin#PHYSICAL_18
     */
    GPIO_05(Pin.PHYSICAL_18),
    /**
     * Gpio Pin 6 (WiringPi numbering).
     * @see Pin#PHYSICAL_22
     */
    GPIO_06(Pin.PHYSICAL_22),
    /**
     * Gpio Pin 7 (WiringPi numbering).
     * @see Pin#PHYSICAL_07
     */
    GPIO_07(Pin.PHYSICAL_07),
    // I2C
    //GPIO_08(Pin.PHYSICAL_03), GPIO_09(Pin.PHYSICAL_05),
    /**
     * Gpio Pin 10 (WiringPi numbering).
     * @see Pin#PHYSICAL_24
     */
    GPIO_10(Pin.PHYSICAL_24),
    /**
     * Gpio Pin 11 (WiringPi numbering).
     * @see Pin#PHYSICAL_26
     */
    GPIO_11(Pin.PHYSICAL_26),
    /**
     * Gpio Pin 12 (WiringPi numbering).
     * @see Pin#PHYSICAL_19
     */
    GPIO_12(Pin.PHYSICAL_19),
    /**
     * Gpio Pin 13 (WiringPi numbering).
     * @see Pin#PHYSICAL_21
     */
    GPIO_13(Pin.PHYSICAL_21),
    /**
     * Gpio Pin 14 (WiringPi numbering).
     * @see Pin#PHYSICAL_23
     */
    GPIO_14(Pin.PHYSICAL_23),
    // UART
    //GPIO_15(Pin.PHYSICAL_08), GPIO_16(Pin.PHYSICAL_10),
    /**
     * Gpio Pin 21 (WiringPi numbering).
     * @see Pin#PHYSICAL_29
     */
    GPIO_21(Pin.PHYSICAL_29),
    /**
     * Gpio Pin 22 (WiringPi numbering).
     * @see Pin#PHYSICAL_31
     */
    GPIO_22(Pin.PHYSICAL_31),
    // PWM1
    //GPIO_23(Pin.PHYSICAL_33),
    /**
     * Gpio Pin 24 (WiringPi numbering).
     * @see Pin#PHYSICAL_35
     */
    GPIO_24(Pin.PHYSICAL_35),
    /**
     * Gpio Pin 25 (WiringPi numbering).
     * @see Pin#PHYSICAL_37
     */
    GPIO_25(Pin.PHYSICAL_37),
    /**
     * Gpio Pin 26 (WiringPi numbering).
     * @see Pin#PHYSICAL_32
     */
    GPIO_26(Pin.PHYSICAL_32),
    /**
     * Gpio Pin 27 (WiringPi numbering).
     * @see Pin#PHYSICAL_36
     */
    GPIO_27(Pin.PHYSICAL_36),
    /**
     * Gpio Pin 28 (WiringPi numbering).
     * @see Pin#PHYSICAL_38
     */
    GPIO_28(Pin.PHYSICAL_38),
    /**
     * Gpio Pin 29 (WiringPi numbering).
     * @see Pin#PHYSICAL_40
     */
    GPIO_29(Pin.PHYSICAL_40);
    
    Pin pin;
    
    GPIOPin(Pin pin) {
        this.pin = pin;
    }
    
    /**
     * Get the Raspoid pin abstraction for this GPIOPin.
     * @return the Raspoid Pin ocject corresponding to this GPIOPin.
     * @see Pin
     */
    public Pin getPin() {
        return pin;
    }
    
    /**
     * Get the WiringPi abstraction object corresponding to this Raspoid GPIOPin.
     * @return the corresponding WiringPi pin (pi4j.io.gpio.Pin instance) or null if no corresponding object found.
     * @see com.pi4j.io.gpio.Pin
     */
    public com.pi4j.io.gpio.Pin getWiringPiPin() {
        int wiringPiNb = pin.getWiringPiNb();
        if(wiringPiNb == -1)
            return null;
        return RaspiPin.getPinByName("GPIO " + pin.getWiringPiNb());
    }
}
