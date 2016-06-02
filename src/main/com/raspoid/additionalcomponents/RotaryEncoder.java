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

import com.pi4j.wiringpi.Gpio;
import com.raspoid.GPIOPin;
import com.raspoid.examples.additionalcomponents.RotaryEncoderExample;

/**
 * A rotary encoder is an electro-mechanical device that converts
 * the motion of a shaft or axle to digital code.
 * 
 * <p>A rotary encoders is usually placed at the side which is perpendicular to the shaft. 
 * They act as sensors for detecting angle, speed, length, position, and acceleration in automation field.</p>
 * 
 * <p>Example datasheet: <a href="http://raspoid.com/download/datasheet/rotaryEncoder">Rotary Encoder</a></p>
 * 
 * <p>Example of use: {@link RotaryEncoderExample}</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class RotaryEncoder extends Button {
    
    private int dtPinNumber;
    private int clkPinNumber;
    private int flag = 0;
    private int currentDt = 0;
    private int globalCounter = 0;
    
    /**
     * Constructor for a new rotary encoder using specific GPIO pin for sw, dt and clk pins.
     * @param swPin GPIO pin corresponding to the push button of the encoder.
     * @param dtPin GPIO pin corresponding to the data signal from the encoder.
     * @param clkPin GPIO pin corresponding to the clock from the encoder.
     */
    public RotaryEncoder(GPIOPin swPin, GPIOPin dtPin, GPIOPin clkPin) {
        super(swPin, false);
        this.dtPinNumber = dtPin.getPin().getWiringPiNb();
        this.clkPinNumber = clkPin.getPin().getWiringPiNb();
        
        Gpio.pinMode(dtPinNumber, Gpio.INPUT);
        Gpio.pinMode(clkPinNumber, Gpio.INPUT);
    }
    
    /**
     * Updates the value of the counter used for this rotary encoder.
     * <p>The counter is used to represent the position of the shaft. If you turn to the right,
     * the counter is incremeted by one for each tick. If you turn to the left, the counter is 
     * then decremented by one for each tick.</p>
     * @see #getCounterValue()
     */
    public void getEncoderTurn() {
        int lastDt = Gpio.digitalRead(dtPinNumber);
        
        while(Gpio.digitalRead(clkPinNumber) != Gpio.HIGH) {
            currentDt = Gpio.digitalRead(dtPinNumber);
            flag = 1;
        }
        
        if(flag == 1) {
            flag = 0;
            if(lastDt == Gpio.LOW && currentDt == Gpio.HIGH)
                globalCounter++;
            if(lastDt == Gpio.HIGH && currentDt == Gpio.LOW)
                globalCounter--;
        }
    }
    
    /**
     * Get the current value of the counter corresponding to the position of the shaft.
     * @return the value of the counter corresponding to the position of the shaft.
     */
    public int getCounterValue() {
        return globalCounter;
    }
}
