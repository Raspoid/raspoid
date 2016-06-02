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

import com.raspoid.behavioral.Behavior;
import com.raspoid.behavioral.SimpleArbitrator;

/**
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class Example1 {
    
    /**
     * Command-line interface.
     * @param args unused here.
     */
    public static void main(String[] args) {
        Behavior behavior1 = new Behavior1();
        Behavior behavior2 = new Behavior2();
        Behavior behavior3 = new Behavior3();
        
        SimpleArbitrator sa = new SimpleArbitrator();
        sa.addBehavior(behavior1);
        sa.addBehavior(behavior2);
        sa.addBehavior(behavior3);
        sa.start();
    }
}
