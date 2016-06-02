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
import com.raspoid.brickpi.nxt.sensor.SoundSensor;

/**
 * Behavior enclosing the sound management
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class SoundBehavior extends SimpleBehavior {
    
    /**
     * Contains the number of time the Sound behavior
     * wants to claim control (= number of claps detected)
     */
    private int claimControl = 0;
    
    /**
     * Lock object for manipulating safely the clap counter
     * in multi-threaded environment
     */
    private Object lock = new Object();
    
    /**
     * Contains the generic code to trigger when
     * a clap has been detected
     */
    Runnable trigger;
    
    /**
     * constructs a Sound Behavior by passing a generic
     * code to be called each time a clap is detected
     * @param trigger
     */
    public SoundBehavior(Runnable trigger) {
        this.trigger = trigger;
        BrickPi.S1 = new SoundSensor();
        //listener to 60% of sound pressure 
        // difference to ensure sound is strong
        BrickPi.S1.onChange(60, evt -> {
            if (evt.getOldValue() > 50) {
                synchronized (lock) {
                    claimControl++;
                }
            }
        });
    }

    /**
     * Claims control when the clap counter
     * is strictly positive
     */
    @Override
    public boolean claimsControl() {
        if (claimControl > 0) {
            synchronized (lock) {
                claimControl--;
            }
            return true;
        }
        return false;
    }

    /**
     * Run the generic code when clap is detected
     */
    @Override
    public void gainControl() {
        trigger.run();
    }

    /**
     * Priority is high (2)
     */
    @Override
    public int getPriority() {
        return 2;
    }
}
