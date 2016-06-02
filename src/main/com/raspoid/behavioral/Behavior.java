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
 * Behavior services exposed.
 *
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public interface Behavior {
    
    /**
     * Tells whether the behavior claims control of the system
     * @return true if it claims control, false otherwise
     */
    boolean claimsControl();
    
    /**
     * Entry point of the behavior when it gains control of the system.
     * This is where the behavior does the its stuff.
     */
    void gainControl();
    
    /**
     * Enjoin the behavior to yield control back to the system as fast as possible.
     * A call to this method should cause the behavior to quickly return 
     * from the gainControl method
     */
    void yieldControl();
    
    /**
     * Inform the system about the behavior's priority
     * @return the behavior's priority
     */
    int getPriority();
    
    /**
     * Reset the state of the behavior before it gains control. The behavior
     * can thus do some cleanup from older execution, or simply initialize
     * what is needed for gaining control of the system
     */
    void reset();
}
