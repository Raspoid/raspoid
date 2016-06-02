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
package com.raspoid.network.pushbullet;

import java.util.List;

/**
 * <b>Abstraction of a list of Pushes from the Pushbullet API.</b>
 * 
 * <p>Pushbullet API: <a href="https://docs.pushbullet.com/">https://docs.pushbullet.com</a></p>
 * 
 * <p><b>! ATTENTION ! Classical Java naming conventions can't be respected here.
 * The name of variables must respect the deserialized Pushbullet json fields.</b></p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 * 
 * @see Push
 */
public class ListOfPushes {
    
    private List<Push> pushes;
    
    /**
     * Get the list of pushes.
     * @return the list of pushes.
     */
    public List<Push> getPushes() {
        return pushes;
    }
}
