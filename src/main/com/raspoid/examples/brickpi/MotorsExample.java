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

/**
 * Example of use of an NXT motor.
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class MotorsExample {
    
    /**
     * Private constructor to hide the implicit public one.
     */
    private MotorsExample() {
    }

    /**
     * Command-line interface.
     * @param args unused here.
     */
    public static void main(String[] args) {
        Tools.log("---- Motors Example ----");
        BrickPi.MA = new Motor();
        int range = 500;
        BrickPi.MA.onChange(range, evt -> Tools.log("Encoder value updated (with a delta >= " + range + "): " + evt.getNewValue()));
        BrickPi.MA.onRotate( evt -> Tools.log("Rotate"));
        BrickPi.start();
        Tools.log("Encoders " + BrickPi.MA.getEncoderValue());
        BrickPi.MA.setPower(100);
        Tools.sleepMilliseconds(10000);
        
        BrickPi.MA.stop();
        BrickPi.MA.resetEncoderValue();
        
        Tools.log("Encoders " + BrickPi.MA.getEncoderValue());
        BrickPi.MA.setPower(100);
        Tools.sleepMilliseconds(10000);
        Tools.log("stop");
        BrickPi.stop();
    }
}

