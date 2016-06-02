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
package com.raspoid.brickpi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/**
 * Implementation of a led present on the brickpi.
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class Led {
     
    /**
     * Contains the gpio output object to control the led state
     */
    private final GpioPinDigitalOutput ledOutputPin;

    /**
     * Constructs a led with its number
     * @param ledNb the led number (1 or 2)
     */
    protected Led(int ledNb) {
        GpioController gpio = GpioFactory.getInstance();
        // provision the corresponding gpio pin as an output pin
        // and make sure it is set to LOW at startup
        if(ledNb == 1) {
            ledOutputPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "led1", PinState.LOW);
        } else {
            ledOutputPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "led2", PinState.LOW);
        }

        // configure the pin shutdown behavior; these settings will be
        // automatically applied to the pin when the application is terminated
        // ensure that the LED is turned OFF when the application is shutdown
        ledOutputPin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
    }

    /**
     * Retrieve pi4j Gpio digital output pin interface corresponding to the LED.
     * @return Gpio digital output pin interface corresponding to the LED.
     */
    public GpioPinDigitalOutput getGpioPinDigitalOutput() {
        return ledOutputPin;
    }
    
    /**
     * Check if the LED is on.
     * @return true if the LED is on, false otherwise.
     */
    public boolean isOn() {
        return ledOutputPin.isHigh();
    }
    
    /**
     * Check if the LED is off.
     * @return true if the LED is off, false otherwise.
     */
    public boolean isOff() {
        return ledOutputPin.isLow();
    }

    /**
     * Turns the LED on.
     */
    public void on() {
        ledOutputPin.high();
    }

    /**
     * Turns the LED off.
     */
    public void off() {
        ledOutputPin.low();
    }

    /**
     * If the LED is on, turns it off.
     * If off, turns on.
     */
    public void toggle() {
        ledOutputPin.toggle();
    }

    /**
     * Turns the LED on for duration seconds and then off.
     * @param duration in milliseconds.
     */
    public void pulse(long duration) {
        ledOutputPin.pulse(duration, true);
    }
}
