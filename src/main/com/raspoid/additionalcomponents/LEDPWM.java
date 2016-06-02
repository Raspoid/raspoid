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
package com.raspoid.additionalcomponents;

import com.raspoid.PWMComponent;
import com.raspoid.additionalcomponents.PCA9685.PCA9685Channel;
import com.raspoid.examples.additionalcomponents.LEDPWMExample;

/**
 * Implementation of a LED using a PWM signal to control the light intensity.
 * 
 * <p>Example of use: {@link LEDPWMExample}</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class LEDPWM extends PWMComponent {
    
    /**
     * Constructor for a LED controlled by a PWM signal, using a specific PCA9685
     * and a specific channel on this PCA9685 to send the PWM signal.
     * @param pca9685 the PCA9685 to use to send PWM signals.
     * @param channel the channel on the PCA9685 to use to send PWM signals.
     */
    public LEDPWM(PCA9685 pca9685, PCA9685Channel channel) {
        super(pca9685, channel, 4096);
    }
    
    /**
     * Sets the intensity of the light of the LED.
     * @param percent the new intensity of light to apply to the LED, in the 0..100 range.
     */
    public void setIntensity(int percent) {
        percent = Math.max(1, percent);
        percent = Math.min(percent, 99);
        setPWM((int)((4096./100.) * percent));
    }
}
