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
import com.raspoid.additionalcomponents.servomotor.ServoMotor;
import com.raspoid.additionalcomponents.servomotor.TowerProMG90S;

/**
 * Example of use of a TowerPro MG90S servomotor.
 * 
 * @see TowerProMG90S
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class TowerProMG90SExample {
    
    /**
     * Private constructor to hide the implicit public one.
     */
    private TowerProMG90SExample() {
    }
    
    /**
     * Command-line interface.
     * @param args unused here.
     */
    public static void main(String[] args) {
        // Comment/Uncomment the corresponding sections to use PCA9685
        // or hardware PWM pins from the Raspberry Pi
        ServoMotor motor;
        
        // Using a PWM pin
        //motor = new TowerProMG90S(PWMPin.PWM1); // NOSONAR
        
        // Using a PCA9685
        motor = new TowerProMG90S(new PCA9685(), PCA9685Channel.CHANNEL_01); // NOSONAR
        
        motor.setAngle(90);
        Tools.sleepMilliseconds(100000);
        
        double[] angles = {0, 45, 90, 135, 180, 135, 90, 45, 0};
        
        for(int i = 0; i < angles.length; i++) {
            motor.setAngle(angles[i]);
            Tools.log("position angle: " + motor.getPositionAngle());
            Tools.sleepMilliseconds(1500);
        }
    }
}
