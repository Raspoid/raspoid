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
package com.raspoid.examples.additionalcomponents.servomotor;

import com.pi4j.wiringpi.Gpio;
import com.raspoid.PWMPin;
import com.raspoid.Tools;
import com.raspoid.additionalcomponents.servomotor.ServoMotor;

/**
 * This class has been implemented to help you to callibrate your servos.
 * 
 * <p>We send PWM signals with pulse length varying from 0.7 to 2.3 ms.</p>
 * 
 * <p>The method will print at the standard output the current pulse length.
 * All you need to do is to observe the pulse length and the position of the rotor, with regard this length.</p>
 */
public class ServoMotorCallibration {
    int minValue = 700; // 0.7ms
    int maxValue = 2300; // 2.3ms
    int pinNumber;
    
    /**
     * Constructor for a new callibrator.
     * @param minValue the minimum pulse length value, in ms.
     * @param maxValue the maximum pulse length value, in ms.
     */
    public ServoMotorCallibration(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        
        Gpio.wiringPiSetup();
        pinNumber = PWMPin.PWM1.getPin().getWiringPiNb();
        Gpio.pinMode(pinNumber, Gpio.PWM_OUTPUT);
        Gpio.pwmSetMode(Gpio.PWM_MODE_MS);
        int pwmRangeGenerator = 20000;// 20ms
        int targetFrequency = 50; // Hz
        int pwmClockDivisor = ServoMotor.DEFAULT_RPI_PWM_CLOCK_FREQUENCY / (targetFrequency * pwmRangeGenerator);
        Gpio.pwmSetClock(pwmClockDivisor);
        Gpio.pwmSetRange(pwmRangeGenerator);
    }
    
    /**
     * Sets the value sent to the PWM pin.
     * @param value the new value, in the previously set min..max range.
     */
    public void setValue(int value) {
        if(value < minValue)
            value = minValue;
        else if(value > maxValue)
            value = maxValue;
        Gpio.pwmWrite(pinNumber, value);
    }
    
    /**
     * Command-line interface.
     * @param args mixValue maxValue increment.
     */
    public static void main(String[] args) {
        if(args.length != 3) {
            Tools.log("The number of input args is incorrect. The utilization must be as follows: minValue maxValue increment");
            return;
        }
        int minValue = Integer.parseInt(args[0]);
        int maxValue = Integer.parseInt(args[1]);
        int increment = Integer.parseInt(args[2]);
        
        ServoMotorCallibration callibration = new ServoMotorCallibration(minValue, maxValue);
        for(int i=callibration.minValue; i <= callibration.maxValue; i+=increment) {
            Tools.log("Pulse length: " + i + " [ms]");
            callibration.setValue(i);
            Tools.sleepMilliseconds(1500);
        }
        callibration.stop();
    }
    
    /**
     * Stop sending orders to the servo motor.
     * <p>It tells the motor to turn itself off and wait for more instructions.</p>
     */
    public void stop() {
        Gpio.pwmWrite(pinNumber, 0);
    }
}
