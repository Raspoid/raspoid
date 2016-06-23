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
package com.raspoid.examples.brickpi;

import com.raspoid.Tools;
import com.raspoid.brickpi.BrickPi;
import com.raspoid.brickpi.Motor;

import static com.raspoid.brickpi.BrickPi.MA;

/**
 * Example of use of a NXT motor with utility methods and PID
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class MotorUtilExample {
    
    /**
     * Private constructor to hide the implicit public one.
     */
    private MotorUtilExample() {
    }
    
    /**
     * Command-line interface.
     * @param args unused here.
     */
    public static void main(String[] args) {
        Tools.log("---- Motors Utils Example ----");
        BrickPi.MA = new Motor();
        
        BrickPi.start();
        MA.setDiameter(3);
        MA.setPidParams(1.5, 1, 1);
        MA.rotate(3, 100);
        Tools.log(MA.getEncoderValue());
        MA.rotate(6, -255);
        Tools.log(MA.getEncoderValue());
        MA.move(9.4247, 150);
        Tools.log(MA.getEncoderValue());
        BrickPi.stop();
    }
}
