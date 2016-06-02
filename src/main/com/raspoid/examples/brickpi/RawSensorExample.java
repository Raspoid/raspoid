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
import com.raspoid.brickpi.nxt.ValueChangeEvent;

/**
 * Example of use of an NXT raw sensor.
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class RawSensorExample {
    
    /**
     * Private constructor to hide the implicit public one.
     */
    private RawSensorExample() {
    }
    
    /**
     * Command-line interface.
     * @param args unused here.
     */
    public static void main(String[] args) {
        Tools.log("---- Raw Sensor Example ----");
        
        int range = 1;
        BrickPi.S1.onChange(range, (ValueChangeEvent evt) 
                -> Tools.log("Raw sensor value updated (with a delta > " + range 
                        + "): old value=" + evt.getOldValue() + " new value=" + evt.getNewValue()));
        
        BrickPi.start();
        Tools.sleepMilliseconds(10000);
        BrickPi.stop();
    }
}
