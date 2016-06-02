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
package com.raspoid.additionalcomponents.servomotor;

import com.raspoid.PWMPin;
import com.raspoid.additionalcomponents.PCA9685;
import com.raspoid.additionalcomponents.PCA9685.PCA9685Channel;
import com.raspoid.examples.additionalcomponents.servomotor.MicroServo9gA0090Example;

/**
 * Implementation of a servo motor "MicroServo 9g A0090".
 * 
 * <p>Example of use: {@link MicroServo9gA0090Example}</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class MicroServo9gA0090 extends ServoMotor {

    /**
     * Constructor for a MicroServo 9g A0090, using a specific PCA9685
     * and a specific channel on this PCA9685.
     * @param pca9685 the PCA9685 to use to manage the servo.
     * @param channel the channel on the PCA9685 to use to manage the servo.
     */
    public MicroServo9gA0090(PCA9685 pca9685, PCA9685Channel channel) {
        super(pca9685, channel, 0, .688, 140, 2.05, 0.1, 2.5);
    }
    
    /**
     * Constructor for a MicroServo 9g A0090, using a specific PWM pin
     * from the Raspberry Pi.
     * @param pin the PWM to use to manage the servo.
     */
    public MicroServo9gA0090(PWMPin pin) {
        super(pin, 0, .688, 140, 2.05, 0.1, 2.5);
    }
}
