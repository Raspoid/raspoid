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

/**
 * <b>Abstraction of a RealtimeEventStreamMessage entity from the Pushbullet API.</b>
 * 
 * <p>Pushbullet API: <a href="https://docs.pushbullet.com/">https://docs.pushbullet.com</a></p>
 * 
 * <p><b>! ATTENTION ! Classical Java naming conventions can't be respected here.
 * The name of variables must respect the deserialized Pushbullet json fields.</b></p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class RealtimeEventStreamMessage {
    
    private String type;
    private String subtype;
    
    /**
     * Get the type of the realtime event stream message.
     * @return the type of the realtime event stream message.
     */
    public String getType() {
        return type;
    }
    
    /**
     * Get the subtype of the realtime event stream message.
     * @return the subtype of the realtime event stream message.
     */
    public String getSubtype() {
        return subtype;
    }
}
