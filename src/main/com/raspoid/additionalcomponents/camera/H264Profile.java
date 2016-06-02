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
package com.raspoid.additionalcomponents.camera;

/**
 * <b>H264 profile to use for encoding.</b>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public enum H264Profile {
    /**
     * Baseline.
     */
    BASELINE("baseline"),
    
    /**
     * Main.
     */
    MAIN("main"),
    
    /**
     * High.
     */
    HIGH("high");
    
    String profile;
    
    H264Profile(String profile) {
        this.profile = profile;
    }
    
    /**
     * Get the value corresponding to this H264Profile.
     * @return the command line option value corresponding to this H264 profile.
     */
    public String getValue() {
        return profile;
    }
}
