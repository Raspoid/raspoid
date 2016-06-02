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
import com.raspoid.examples.additionalcomponents.servomotor.TowerProMG90SExample;

/**
 * Implementation of a servo motor TowerPro MG90S.
 * 
 * <p>Datasheet: <a href="http://raspoid.com/download/datasheet/TowerProMG90S">TowerPro MG90S</a></p>
 * 
 * <p>Example of use: {@link TowerProMG90SExample}</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class TowerProMG90S extends ServoMotor {
    
    /**
     * Constructor for a servo motor TowerPro MG90S using a specific PWM pin.
     * @param pin the PWM pin to use to manage the servo.
     */
    public TowerProMG90S(PWMPin pin) {
        // 0.7ms -> 0°. 2.8ms -> 180°.
        super(pin, 0, 0.7, 180, 2.8, 0.1, 2.5);
    }
    
    /**
     * Constructor for a servo motor TowerPro MG90S using a PCA9685 to generate PWM signals.
     * @param pca9685 the PCA9685 to use to manage the servo.
     * @param channel the channel on the PCA9685 to use to manage the servo.
     */
    public TowerProMG90S(PCA9685 pca9685, PCA9685Channel channel) {
        super(pca9685, channel, 0, 0.7, 180, 2.8, 0.1, 2.5);
    }
}
