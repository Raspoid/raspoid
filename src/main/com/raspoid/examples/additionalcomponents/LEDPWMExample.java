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
package com.raspoid.examples.additionalcomponents;

import com.raspoid.Tools;
import com.raspoid.additionalcomponents.LEDPWM;
import com.raspoid.additionalcomponents.PCA9685;
import com.raspoid.additionalcomponents.PCA9685.PCA9685Channel;

/**
 * Example of use of a LED with PWM.
 * 
 * @see LEDPWM
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class LEDPWMExample {
    
    /**
     * Private constructor to hide the implicit public one.
     */
    private LEDPWMExample() {
    }
    
    /**
     * Command-line interface.
     * @param args unused here.
     */
    public static void main(String[] args) {
        LEDPWM led = new LEDPWM(new PCA9685(), PCA9685Channel.CHANNEL_00);
        
        while(true) {
            for(int i=0; i<100; i++) {
                led.setIntensity(i);
                Tools.sleepMilliseconds(5);
            }
            
            for(int i = 100; i > 0; i--) {
                led.setIntensity(i);
                Tools.sleepMilliseconds(5);
            }
        }
    }
}
