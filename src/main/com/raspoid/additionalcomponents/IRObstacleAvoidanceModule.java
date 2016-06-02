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

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.raspoid.GPIOComponent;
import com.raspoid.GPIOPin;
import com.raspoid.examples.additionalcomponents.IRObstacleAvoidanceModuleExample;

/**
 * An IR obstacle avoidance module uses infrared reflection principle
 * to detect obstacles.
 * 
 * <p>When there is no object ahead, infrared-receiver
 * cannot receive signals; when there is an object ahead, it will block
 * and reflect infrared light, then infrared-receiver can receive signals.</p>
 * 
 * <p>Notes:
 *  <ul>
 *      <li>With the sunfounder IR obstacle avoidance module, the detection distance 
 *      of the infrared sensor is adjustable. You may adjust it with the potentiometer:
 *      between 0 and ~7cm.</li>
 *      <li>If the obstacle is black, it will not reflect the IR signals.</li>
 *  </ul>
 * </p>
 * 
 * <p>Datasheet: <a href="http://raspoid.com/download/datasheet/DifferentialComparator">Differential Comparator</a></p>
 * 
 * <p>Example of use: {@link IRObstacleAvoidanceModuleExample}</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class IRObstacleAvoidanceModule extends GPIOComponent {
    
    protected final GpioPinDigitalInput digitalInput;

    /**
     * Constructor for a new infrared obstacle avoidance module using a specific Gpio pin.
     * @param pin the Gpio pin to use to deal with the module.
     */
    public IRObstacleAvoidanceModule(GPIOPin pin) {
        digitalInput = gpio.provisionDigitalInputPin(pin.getWiringPiPin());
    }
    
    /**
     * Returns true if an obstacle is currently detected by the module.
     * @return true if an obstacle is currently detected by the module.
     */
    public boolean obstacleDetected() {
        return digitalInput.isLow();
    }
    
    /**
     * Get the pi4j GpioPinDigitalInput corresponding to this IR obstacle avoidance module.
     * <p>This can be usefull to add custom listeners to easily react when the module detects an obstacle.</p>
     * @return the pi4j GpioPinDigitalInput corresponding to this IR obstacle avoidance module.
     */
    public GpioPinDigitalInput getGpioPinDigitalInput() {
        return digitalInput;
    }
}
