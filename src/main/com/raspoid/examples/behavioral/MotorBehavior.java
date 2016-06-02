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
package com.raspoid.examples.behavioral;

import com.raspoid.behavioral.SimpleBehavior;
import com.raspoid.brickpi.BrickPi;
import com.raspoid.brickpi.Motor;

/**
 * Behavior enclosing the motor management
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class MotorBehavior extends SimpleBehavior {
    
    /**
     * Power applied to the motor
     */
    private int power = 150;
    
    /**
     * Constructs a motor behavior
     */
    public MotorBehavior() {
        BrickPi.MA = new Motor();
    }   

    /**
     * Gets the power applied to the motor
     * @return the current power applied to the motor
     */
    public int getPower() {
        return power;
    }

    /**
     * Sets the power to be applied by the motor
     * @param power the power to apply to the motor
     */
    public void setPower(int power) {
        this.power = power;
    }

    /**
     * The motor behavior always claim control
     */
    @Override
    public boolean claimsControl() {
        return true;
    }

    /**
     * Effectively apply the power to the motor 
     */
    @Override
    public void gainControl() {
        while (!shouldYield) {
            BrickPi.MA.setPower(power);
        }
    }

    /**
     * Priority is low (1)
     */
    @Override
    public int getPriority() {
        return 1;
    }
}
