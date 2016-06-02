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
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.raspoid.GPIOComponent;
import com.raspoid.GPIOPin;
import com.raspoid.Tools;
import com.raspoid.examples.additionalcomponents.ActiveBuzzerExample;

/**
 * The active buzzer has built-in oscillating source, so it will beep
 * as long as it is wired up, but it can only beep with fixed frequency.
 * 
 * <p>You just need to apply 3V to 5V to this buzzer module.
 * You can then simply connect the buzzer to a classical Gpio pin of the Raspberry Pi.
 * If the pin is high: no sound. If the pin is low: sound.</p>
 * 
 * <p>Example Datasheet: <a href="http://raspoid.com/download/datasheet/ActiveBuzzer">Active Buzzer</a></p>
 * 
 * <p>Example of use: {@link ActiveBuzzerExample}</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class ActiveBuzzer extends GPIOComponent {
    
    private final GpioPinDigitalOutput buzzer;
    private boolean inverse = true;
    
    /**
     * Constructor for an active buzzer, using a specific GPIOPin.
     * The "inverse" input arg is used to illustrate the fact that some active buzzer modules
     * beeps when the Gpio pin is low. Some others when the pin is high. 
     * @param pin the Gpio pin used to access the active buzzer.
     * @param inverse true if the buzzer beeps when the pin is low.
     * False if the buzzer beeps when the pin is high.
     */
    public ActiveBuzzer(GPIOPin pin, boolean inverse) {
        buzzer = gpio.provisionDigitalOutputPin(pin.getWiringPiPin(), PinState.HIGH);
        buzzer.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        this.inverse = inverse;
    }
    
    /**
     * Enable the buzzer.
     */
    public void on() {
        if(inverse)
            buzzer.low();
        else
            buzzer.high();
    }
    
    /**
     * Disable the buzzer.
     */
    public void off() {
        if(inverse)
            buzzer.high();
        else
            buzzer.low();
    }
    
    /**
     * Beep for a duration of millis milliseconds on and the millis milliseconds off.
     * @param millis the duration of the on/off period.
     */
    public void beep(int millis) {
        on();
        Tools.sleepMilliseconds(millis);
        off();
        Tools.sleepMilliseconds(millis);
    }
}
