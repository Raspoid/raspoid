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
package com.raspoid.examples.robots.poc;

import com.raspoid.brickpi.BrickPi;
import com.raspoid.brickpi.Motor;

/**
 * Basic configuration used to easily develop other behaviours for the Proof of Concept robot
 * presented in the Raspoid.com website.
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class BaseRobot {
    
    boolean stop = false;
    
    Motor motorLeft;
    Motor motorRight;
    
    /**
     * Constructor for a BaseRobot.
     */
    public BaseRobot() {
        BrickPi.MA = new Motor();
        motorRight = BrickPi.MA;
        motorRight.setDiameter(5);
        BrickPi.MC = new Motor();
        motorLeft = BrickPi.MC;
        motorLeft.setDiameter(5);
    }
    
    protected void stop() {
        stop = true;
        BrickPi.stop();
    }
}
