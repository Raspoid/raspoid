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

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.raspoid.GPIOComponent;
import com.raspoid.GPIOPin;
import com.raspoid.examples.additionalcomponents.LEDExample;
import com.pi4j.io.gpio.PinPullResistance;

/**
 * Abstraction to easily integrate a LED in your project.
 * 
 * <p>This component only uses a Gpio pin.</p>
 * 
 * <p>Example of use: {@link LEDExample}</p>
 * 
 * <p>Datasheet (example): <a href="http://raspoid.com/download/datasheet/LED">LED</a>,
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class LED extends GPIOComponent {

    protected final GpioPinDigitalOutput digitalOutput;
    
    /**
     * Constructor for a LED using a specific Gpio pin,
     * and a specific name.
     * @param pin the pin used to manage the LED.
     * @param name the name used to print specific pi4j logs.
     */
    public LED(GPIOPin pin, String name) {
        digitalOutput = gpio.provisionDigitalOutputPin(pin.getWiringPiPin(), name, PinState.LOW);
        digitalOutput.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
    }
    
    /**
     * Constructor for a LED using a specific Gpio pin.
     * @param pin the Gpio pin used to manage the LED.
     */
    public LED(GPIOPin pin) {
        this(pin, "led-" + pin.getPin().getName());
    }
    
    /**
     * Turns the LED on.
     */
    public void on() {
        digitalOutput.high();
    }

    /**
     * Turns the LED off.
     */
    public void off() {
        digitalOutput.low();
    }

    /**
     * Toggles the LED state.
     * <p>Turns on if OFF. Turns off if ON.
     */
    public void toggle() {
        digitalOutput.toggle();
    }

    /**
     * Turns the LED on for millis milliseconds and then off.
     * @param millis the duration of the ON period, in milliseconds.
     */
    public void pulse(long millis) {
        digitalOutput.pulse(millis, true);
    }
}
