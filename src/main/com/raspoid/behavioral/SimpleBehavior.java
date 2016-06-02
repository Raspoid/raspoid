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
package com.raspoid.behavioral;

/**
 * Abstract implementation of a simple behavior where
 * flow control uses a boolean value.
 *
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public abstract class SimpleBehavior implements Behavior {

    /**
     * Informs the behavior whether it should yield control.
     * This value should be checked regularly in case it is set by
     * the system, in that case it should yield as fast as possible
     */
    protected boolean shouldYield = false;
    
    @Override
    public void reset() {
        shouldYield = false;
    }

    @Override
    public void yieldControl() {
        shouldYield = true;
    }
}
