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

import com.raspoid.Tools;
import com.raspoid.behavioral.SimpleBehavior;

/**
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class Behavior1 extends SimpleBehavior {

    boolean stopped = false;
    
    @Override
    public boolean claimsControl() {
        //System.out.println(".cc");
        return true;
    }

    @Override
    public void gainControl() {
        if (!shouldYield) {
            System.out.println(".");
            Tools.sleepMilliseconds(250);
        }
    }

    @Override
    public int getPriority() {
        return 1;
    }
    
    
    
}
