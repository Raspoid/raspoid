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

import com.raspoid.Tools;
import com.raspoid.additionalcomponents.PCA9685;
import com.raspoid.additionalcomponents.PCA9685.PCA9685Channel;
import com.raspoid.additionalcomponents.servomotor.MicroServo9gA0090;

/**
 * Example of use of a MicroServo 9gA0090 servomotor.
 * 
 * @see MicroServo9gA0090
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class MicroServo9gA0090Example {
    
    /**
     * Private constructor to hide the implicit public one.
     */
    private MicroServo9gA0090Example() {
    }
    
    /**
     * Command-line interface.
     * @param args unused here.
     */
    public static void main(String[] args) {
        // Comment/Uncomment the corresponding sections to use PCA9685
        // or hardware PWM pins from the Raspberry Pi
        MicroServo9gA0090 servo;
        
        // Using PCA9685
        PCA9685 pca = new PCA9685();
        servo = new MicroServo9gA0090(pca, PCA9685Channel.CHANNEL_15); // NOSONAR
        
        // Using hardware PWM
        //servo = new MicroServo9gA0090(PWMPin.PWM1); // NOSONAR
        
        servo.setAngle(0);
        Tools.sleepMilliseconds(1000);
        servo.setAngle(45);
        Tools.sleepMilliseconds(1000);
        servo.setAngle(90);
        Tools.sleepMilliseconds(1000);
        servo.setAngle(135);
        Tools.sleepMilliseconds(1000);
        
        while(true)
            Tools.sleepMilliseconds(1000);
    }
}
