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

import java.io.Serializable;

/**
 * There are three main methods of pin numbering on Raspberry Pi:
 * <ul>
 *  <li>numbering based on the physical location of the pin</li>
 *  <li>numbering appointed by C language GPIO library wiringPi</li>
 *  <li>numbering appointed by BCM2835 SOC</li>
 * </ul>
 * 
 * <p>All those numberings can easily be used with this Raspoid Pin abstraction.<p>
 *  
 * Sources:
 * <ul>
 *  <li><a href="http://pinout.xyz">http://pinout.xyz</a></li>
 *  <li><a href="http://pi4j.com/pins/model-2b-rev1.html">http://pi4j.com/pins/model-2b-rev1.html</a></li>
 * </ul>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class Pin implements Serializable {
    
    /**
     * The serialization key used to serialize these Pin instances.
     */
    private static final long serialVersionUID = 42918029371924L;

    /**
     * 3v3 Power.
     * <p>The 3v3, 3.3 volt, supply pin on the Pi has a max available
     * current of about 50 mA. Enough to power a couple of LEDs or
     * a microprocessor, but not much more. You should generally use 
     * the 5v supply, coupled with a 3v3 regulator for 3.3v projects.</p>
     */
    public static final Pin PHYSICAL_01 = new Pin("3v3 Power", 1, -1, -1);
    
    /**
     * 5v Power.
     * <p>The 5v power pins are connected directly to the Pi's power input
     * and will capably provide the full current of your mains adaptor,
     * less that used by the Pi itself.</p>
     */
    public static final Pin PHYSICAL_02 = new Pin("5v Power", 2, -1, -1);
    
    /**
     * I2C Data (SDA).
     * <p>SDA (i2c Data) is one of the i2c pins on the Pi. SDA includes 
     * a fixed, 1.8 kohms pull-up to 3.3v, which means this pin is not
     * suitable for use as a general purpose IO where no pullup resistor
     * is desired.</p>
     */
    public static final Pin PHYSICAL_03 = new Pin("I2C Data (SDA)", 3, 2, 8);
    
    /**
     * @see Pin#PHYSICAL_03
     */
    public static final Pin BCM_02 = PHYSICAL_03;
    
    /**
     * @see Pin#PHYSICAL_03
     */
    public static final Pin WIRING_PI_08 = PHYSICAL_03;
    
    /**
     * 5v Power.
     * <p>The 5v power pins are connected directly to the Pi's power input
     * and will capably provide the full current of your mains adaptor,
     * less that used by the Pi itself.</p>
     */
    public static final Pin PHYSICAL_04 = new Pin("5v Power", 4, -1, -1);
    
    /**
     * I2C Clock.
     * <p>SCL (i2c Clock) is one of the i2c pins on the Pi. SCL includes 
     * a fixed, 1.8 kohms pull-up to 3.3v, which means this pin is not
     * suitable for use as a general purpose IO where no pullup resistor
     * is desired.</p>
     */
    public static final Pin PHYSICAL_05 = new Pin("I2C Clock (SCL)", 5, 3, 9);
    
    /**
     * @see Pin#PHYSICAL_05
     */
    public static final Pin BCM_03 = PHYSICAL_05;
    
    /**
     * @see Pin#PHYSICAL_05
     */
    public static final Pin WIRING_PI_09 = PHYSICAL_05;
    
    private static final String GROUND_PIN_NAME = "Ground";
    
    /**
     * Ground.
     * <p>The Ground pins on the Raspberry Pi are all electrically connected,
     * so it doesn't matter which one you use if you're wiring up a voltage
     * supply.</p>
     * Uses 8 GPIO pins.
     */
    public static final Pin PHYSICAL_06 = new Pin(GROUND_PIN_NAME, 6, -1, -1);
    
    /**
     * @see Pin#PHYSICAL_06
     */
    public static final Pin PHYSICAL_09 = new Pin(GROUND_PIN_NAME, 9, -1, -1);
    
    /**
     * @see Pin#PHYSICAL_06
     */
    public static final Pin PHYSICAL_14 = new Pin(GROUND_PIN_NAME, 14, -1, -1);
    
    /**
     * @see Pin#PHYSICAL_06
     */
    public static final Pin PHYSICAL_20 = new Pin(GROUND_PIN_NAME, 20, -1, -1);
    
    /**
     * @see Pin#PHYSICAL_06
     */
    public static final Pin PHYSICAL_25 = new Pin(GROUND_PIN_NAME, 25, -1, -1);
    
    /**
     * @see Pin#PHYSICAL_06
     */
    public static final Pin PHYSICAL_30 = new Pin(GROUND_PIN_NAME, 30, -1, -1);
    
    /**
     * @see Pin#PHYSICAL_06
     */
    public static final Pin PHYSICAL_34 = new Pin(GROUND_PIN_NAME, 34, -1, -1);
    
    /**
     * @see Pin#PHYSICAL_06
     */
    public static final Pin PHYSICAL_39 = new Pin(GROUND_PIN_NAME, 39, -1, -1);
    
    /**
     * GPIO7.
     */
    public static final Pin PHYSICAL_07 = new Pin("GPIO7", 7, 4, 7);
    
    /**
     * @see Pin#PHYSICAL_07
     */
    public static final Pin BCM_04 = PHYSICAL_07;
    
    /**
     * @see Pin#PHYSICAL_07
     */
    public static final Pin WIRING_PI_07 = PHYSICAL_07;
    
    /**
     * UART Transmit.
     * <p>This pin doubles up as the UART transmit pin, TXD. It's also 
     * commonly known as "Serial" and, by default, will output a Console 
     * from your Pi that, with a suitable Serial cable, you can use to 
     * control your Pi via the command-line.</p>
     * <p>The UART pins are useful for setting up a "headless" Pi
     * (a Pi without a screen) and getting it connected to a network.</p>
     */
    public static final Pin PHYSICAL_08 = new Pin("UART Transmit", 8, 14, 15);
    
    /**
     * @see Pin#PHYSICAL_08
     */
    public static final Pin BCM_14 = PHYSICAL_08;
    
    /**
     * @see Pin#PHYSICAL_08
     */
    public static final Pin WIRING_PI_15 = PHYSICAL_08;
    
    /**
     * UART Receive.
     * <p>This pin doubles up as the UART recieve pin, RXD. It's also 
     * commonly known as "Serial" and, by default, will output a Console
     * from your Pi that, with a suitable Serial cable, you can use to 
     * control your Pi via the command-line.</p>
     * <p>The UART pins are useful for setting up a "headless" Pi 
     * (a Pi without a screen) and getting it connected to a network.</p>
     */
    public static final Pin PHYSICAL_10 = new Pin("UART Receive", 10, 15, 16);
    
    /**
     * @see Pin#PHYSICAL_10
     */
    public static final Pin BCM_15 = PHYSICAL_10;
    
    /**
     * @see Pin#PHYSICAL_10
     */
    public static final Pin WIRING_PI_16 = PHYSICAL_10;
    
    /**
     * GPIO0.
     */
    public static final Pin PHYSICAL_11 = new Pin("GPIO0", 11, 17, 0);
    
    /**
     * @see Pin#PHYSICAL_11
     */
    public static final Pin BCM_17 = PHYSICAL_11;
    
    /**
     * @see Pin#PHYSICAL_11
     */
    public static final Pin WIRING_PI_00 = PHYSICAL_11;
    
    /**
     * PWM0.
     * <p>The PWM0 output of BCM 18 is particularly useful, in combination 
     * with some fast, direct memory access trickery, for driving tricky
     * devices with very specific timings.</p>
     */
    public static final Pin PHYSICAL_12 = new Pin("PWM0", 12, 18, 1);
    
    /**
     * @see Pin#PHYSICAL_12
     */
    public static final Pin BCM_18 = PHYSICAL_12;
    
    /**
     * @see Pin#PHYSICAL_12
     */
    public static final Pin WIRING_PI_01 = PHYSICAL_12;
    
    /**
     * GPIO2.
     */
    public static final Pin PHYSICAL_13 = new Pin("GPIO2", 13, 27, 2);
    
    /**
     * @see Pin#PHYSICAL_13
     */
    public static final Pin BCM_27 = PHYSICAL_13;
    
    /**
     * @see Pin#PHYSICAL_13
     */
    public static final Pin WIRING_PI_02 = PHYSICAL_13;
    
    /**
     * GPIO3.
     */
    public static final Pin PHYSICAL_15 = new Pin("GPIO3", 15, 22, 3);
    
    /**
     * @see Pin#PHYSICAL_15
     */
    public static final Pin BCM_22 = PHYSICAL_15;
    
    /**
     * @see Pin#PHYSICAL_15
     */
    public static final Pin WIRING_PI_03 = PHYSICAL_15;
    
    /**
     * GPIO4.
     */
    public static final Pin PHYSICAL_16 = new Pin("GPIO4", 16, 23, 4);
    
    /**
     * @see Pin#PHYSICAL_16
     */
    public static final Pin BCM_23 = PHYSICAL_16;
    
    /**
     * @see Pin#PHYSICAL_16
     */
    public static final Pin WIRING_PI_04 = PHYSICAL_16;
    
    /**
     * 3v3 Power.
     * <p>The 3v3, 3.3 volt, supply pin on the Pi has a max available
     * current of about 50 mA. Enough to power a couple of LEDs or
     * a microprocessor, but not much more. You should generally use 
     * the 5v supply, coupled with a 3v3 regulator for 3.3v projects.</p>
     */
    public static final Pin PHYSICAL_17 = new Pin("3v3 Power", 17, -1, -1);
    
    /**
     * GPIO5.
     */
    public static final Pin PHYSICAL_18 = new Pin("GPIO5", 18, 24, 5);
    
    /**
     * @see Pin#PHYSICAL_18
     */
    public static final Pin BCM_24 = PHYSICAL_18;
    
    /**
     * @see Pin#PHYSICAL_18
     */
    public static final Pin WIRING_PI_05 = PHYSICAL_18;
    
    /**
     * GPIO12.
     */
    public static final Pin PHYSICAL_19 = new Pin("GPIO12", 19, 10, 12);
    
    /**
     * @see Pin#PHYSICAL_19
     */
    public static final Pin BCM_10 = PHYSICAL_19;
    
    /**
     * @see Pin#PHYSICAL_19
     */
    public static final Pin WIRING_PI_12 = PHYSICAL_19;
    
    /**
     * GPIO13.
     */
    public static final Pin PHYSICAL_21 = new Pin("GPIO13", 21, 9, 13);
    
    /**
     * @see Pin#PHYSICAL_21
     */
    public static final Pin BCM_09 = PHYSICAL_21;
    
    /**
     * @see Pin#PHYSICAL_21
     */
    public static final Pin WIRING_PI_13 = PHYSICAL_21;
    
    /**
     * GPIO6.
     */
    public static final Pin PHYSICAL_22 = new Pin("GPIO6", 22, 25, 6);
    
    /**
     * @see Pin#PHYSICAL_22
     */
    public static final Pin BCM_25 = PHYSICAL_22;
    
    /**
     * @see Pin#PHYSICAL_22
     */
    public static final Pin WIRING_PI_06 = PHYSICAL_22;
    
    /**
     * GPIO14.
     */
    public static final Pin PHYSICAL_23 = new Pin("GPIO14", 23, 11, 14);
    
    /**
     * @see Pin#PHYSICAL_23
     */
    public static final Pin BCM_11 = PHYSICAL_23;
    
    /**
     * @see Pin#PHYSICAL_23
     */
    public static final Pin WIRING_PI_14 = PHYSICAL_23;
    
    /**
     * GPIO10.
     */
    public static final Pin PHYSICAL_24 = new Pin("GPIO10", 24, 8, 10);
    
    /**
     * @see Pin#PHYSICAL_24
     */
    public static final Pin BCM_08 = PHYSICAL_24;
    
    /**
     * @see Pin#PHYSICAL_24
     */
    public static final Pin WIRING_PI_10 = PHYSICAL_24;
    
    /**
     * GPIO11.
     */
    public static final Pin PHYSICAL_26 = new Pin("GPIO11", 26, 7, 11);
    
    /**
     * @see Pin#PHYSICAL_26
     */
    public static final Pin BCM_07 = PHYSICAL_26;
    
    /**
     * @see Pin#PHYSICAL_26
     */
    public static final Pin WIRING_PI_11 = PHYSICAL_26;
    
    /**
     * GPIO30.
     */
    public static final Pin PHYSICAL_27 = new Pin("GPIO30", 27, 0, 30);
    
    /**
     * @see Pin#PHYSICAL_27
     */
    public static final Pin BCM_00 = PHYSICAL_27;
    
    /**
     * @see Pin#PHYSICAL_27
     */
    public static final Pin WIRING_PI_30 = PHYSICAL_27;
    
    /**
     * GPIO31.
     */
    public static final Pin PHYSICAL_28 = new Pin("GPIO31", 28, 1, 31);
    
    /**
     * @see Pin#PHYSICAL_28
     */
    public static final Pin BCM_01 = PHYSICAL_28;
    
    /**
     * @see Pin#PHYSICAL_28
     */
    public static final Pin WIRING_PI_31 = PHYSICAL_28;
    
    /**
     * GPIO21.
     */
    public static final Pin PHYSICAL_29 = new Pin("GPIO21", 29, 5, 21);
    
    /**
     * @see Pin#PHYSICAL_29
     */
    public static final Pin BCM_05 = PHYSICAL_29;
    
    /**
     * @see Pin#PHYSICAL_29
     */
    public static final Pin WIRING_PI_21 = PHYSICAL_29;
    
    /**
     * GPIO22.
     */
    public static final Pin PHYSICAL_31 = new Pin("GPIO22", 31, 6, 22);
    
    /**
     * @see Pin#PHYSICAL_31
     */
    public static final Pin BCM_06 = PHYSICAL_31;
    
    /**
     * @see Pin#PHYSICAL_31
     */
    public static final Pin WIRING_PI_22 = PHYSICAL_31;
    
    /**
     * GPIO26.
     */
    public static final Pin PHYSICAL_32 = new Pin("GPIO32", 32, 12, 26);
    
    /**
     * @see Pin#PHYSICAL_32
     */
    public static final Pin BCM_12 = PHYSICAL_32;
    
    /**
     * @see Pin#PHYSICAL_32
     */
    public static final Pin WIRING_PI_26 = PHYSICAL_32;
    
    /**
     * PWM1.
     */
    public static final Pin PHYSICAL_33 = new Pin("PWM1", 33, 13, 23);
    
    /**
     * @see Pin#PHYSICAL_33
     */
    public static final Pin BCM_13 = PHYSICAL_33;
    
    /**
     * @see Pin#PHYSICAL_33
     */
    public static final Pin WIRING_PI_23 = PHYSICAL_33;
    
    /**
     * GPIO24.
     */
    public static final Pin PHYSICAL_35 = new Pin("GPIO24", 35, 19, 24);
    
    /**
     * @see Pin#PHYSICAL_35
     */
    public static final Pin BCM_19 = PHYSICAL_35;
    
    /**
     * @see Pin#PHYSICAL_35
     */
    public static final Pin WIRING_PI_24 = PHYSICAL_35;
    
    /**
     * GPIO27.
     */
    public static final Pin PHYSICAL_36 = new Pin("GPIO27", 36, 16, 27);
    
    /**
     * @see Pin#PHYSICAL_36
     */
    public static final Pin BCM_16 = PHYSICAL_36;
    
    /**
     * @see Pin#PHYSICAL_36
     */
    public static final Pin WIRING_PI_27 = PHYSICAL_36;
    
    /**
     * GPIO25.
     */
    public static final Pin PHYSICAL_37 = new Pin("GPIO25", 37, 26, 25);
    
    /**
     * @see Pin#PHYSICAL_37
     */
    public static final Pin BCM_26 = PHYSICAL_37;
    
    /**
     * @see Pin#PHYSICAL_37
     */
    public static final Pin WIRING_PI_25 = PHYSICAL_37;
    
    /**
     * GPIO28.
     */
    public static final Pin PHYSICAL_38 = new Pin("GPIO28", 38, 20, 28);
    
    /**
     * @see Pin#PHYSICAL_38
     */
    public static final Pin BCM_20 = PHYSICAL_38;
    
    /**
     * @see Pin#PHYSICAL_38
     */
    public static final Pin WIRING_PI_28 = PHYSICAL_38;
    
    /**
     * GPIO29.
     */
    public static final Pin PHYSICAL_40 = new Pin("GPIO29", 40, 21, 29);
    
    /**
     * @see Pin#PHYSICAL_40
     */
    public static final Pin BCM_21 = PHYSICAL_40;
    
    /**
     * @see Pin#PHYSICAL_40
     */
    public static final Pin WIRING_PI_29 = PHYSICAL_40;
    
    /**
     * Custom name of the pin.
     */
    private final String name;
    
    /**
     * Physical number of the pin.
     */
    private final int physical;
    
    /**
     * BCM number of the pin if it makes sense. -1 otherwise.
     */
    private final int bcm;
    
    /**
     * WiringPi number of the pin if it makes sense. -1 otherwise.
     */
    private final int wiringPi;
    
    /**
     * Private constructor for a new Pin.
     * @param name a custom name for the pin.
     * @param physical the physical number of the pin.
     * @param bcm the bcm number of the pin. -1 if doesn't make sense.
     * @param wiringPi the WiringPi number of the pin. -1 if doesn't make sense.
     */
    private Pin(String name, int physical, int bcm, int wiringPi) {
        this.name = name;
        this.physical = physical;
        this.bcm = bcm;
        this.wiringPi = wiringPi;
    }
    
    /**
     * Get the name of this pin.
     * @return the custom name of the Pin.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the physical number corresponding to this pin.
     * @return the physical number of the Pin.
     */
    public int getPhysicalNb() {
        return physical;
    }
    
    /**
     * Returns the WiringPi number corresponding to this pin.
     * -1 if not adapted.
     * @return the WiringPi number of the Pin. -1 if doesn't make sense.
     */
    public int getWiringPiNb() {
        return wiringPi;
    }
    
    /**
     * Returns the BCM number corresponding to this pin.
     * -1 if not adapted.
     * @return the BCM number of the Pin. -1 if doesn't make sense.
     */
    public int getBCMNb() {
        return bcm;
    }
    
    @Override
    public String toString() {
        return "Name: " + getName() +
                " | (physical) " + getPhysicalNb() +
                " | (wiringPi) " + (getWiringPiNb() == -1 ? "inappropriate" : getWiringPiNb()) +
                " | (BCM) " + (getBCMNb() == -1 ? "inappropriate" : getBCMNb());
    }
}
