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

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.raspoid.GPIOComponent;
import com.raspoid.GPIOPin;
import com.raspoid.examples.additionalcomponents.JoystickExample;

/**
 * Implementation of a simple button.
 * 
 * <p>This component only uses one Gpio pin.</p>
 * 
 * <p>As an example, you can use this kind of component:
 * <a href="https://www.sparkfun.com/products/9190">Momentary Pushbutton Switch</a></p>
 * 
 * <p>Example of use: {@link JoystickExample}</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class Button extends GPIOComponent {
    
    protected final GpioPinDigitalInput digitalInput;
    
    private boolean highIsPressed = true;
    
    /**
     * Constructor for a new button, using a specific Gpio pin.
     * @param pin the pin used to deal with the button.
     */
    public Button(GPIOPin pin) {
        this(pin, true);
    }
    
    /**
     * Constructor for a new button, using a specific Gpio pin,
     * and specifying the signal corresponding to the button pressed state: high or low.
     * @param pin the Gpio pin used to deal with the button.
     * @param highIsPressed true if a high signal corresponds to button pressed. false otherwise.
     */
    public Button(GPIOPin pin, boolean highIsPressed) {
        this.highIsPressed = highIsPressed;
        digitalInput = gpio.provisionDigitalInputPin(pin.getWiringPiPin(), 
                PinPullResistance.PULL_DOWN);
    }
    
    /**
     * Checks if the button is currently pressed.
     * @return true if the button is currently pressed. False otherwise.
     */
    public boolean isPressed() {
        if(highIsPressed)
            return digitalInput.isHigh();
        else
            return digitalInput.isLow();
    }
    
    /**
     * Get the pi4j GpioPinDigitalInput corresponding to this button.
     * <p>This can be usefull to add custom listeners to easily react when the button is pressed/released.</p>
     * @return the pi4j GpioPinDigitalInput corresponding to this button.
     */
    public GpioPinDigitalInput getGpioPinDigitalInput() {
        return digitalInput;
    }
}
