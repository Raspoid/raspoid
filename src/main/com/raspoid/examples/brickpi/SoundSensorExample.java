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
import com.raspoid.brickpi.nxt.sensor.SoundSensor;

/**
 * Example of use of the NXT sound sensor.
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class SoundSensorExample {
    
    /**
     * Private constructor to hide the implicit public one.
     */
    private SoundSensorExample() {
    }
    
    /**
     * Command-line interface.
     * @param args unused here.
     */
    public static void main(String[] args) {
        Tools.log("---- Sound Sensor Example ----");
        BrickPi.S1 = new SoundSensor();
        
        int range = 60;
        BrickPi.S1.onChange(evt -> Tools.log("Sound sensor value updated: old value=" + evt.getOldValue() + " new value=" + evt.getNewValue())
        );
        BrickPi.S1.onChange(range, evt -> Tools.log("##Sound sensor value updated (with a delta > " + range 
                        + "): old value=" + evt.getOldValue() + " new value=" + evt.getNewValue())
                );
        
        BrickPi.start();
        Tools.sleepMilliseconds(10000);
        Tools.log("Stop");
        BrickPi.stop();
    }
}
