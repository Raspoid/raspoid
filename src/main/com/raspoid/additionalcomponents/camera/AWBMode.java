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
 * <b>Automatic White Balance (AWB) modes.</b>
 * 
 * <p>Note that not all of these settings may be implemented,
 * depending on camera type.</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public enum AWBMode {
    
    /**
     * Turn off white balance calculation.
     */
    OFF("off"),
    
    /**
     * Automatic mode (default).
     */
    AUTO("auto"),
    
    /**
     * Sunny mode.
     */
    SUN("sun"),
    
    /**
     * Cloudy mode.
     */
    CLOUD("cloud"),
    
    /**
     * Shaded mode.
     */
    SHADE("shade"),
    
    /**
     * Tungsten lighting mode.
     */
    TUNGSTEN("tungsten"),
    
    /**
     * Fluorescent lighting mode.
     */
    FLUORESCENT("fluorescent"),
    
    /**
     * Incandescent lighting mode.
     */
    INCANDESCENT("incandescent"),
    
    /**
     * Flash mode.
     */
    FLASH("flash"),
    
    /**
     * Horizon mode.
     */
    HORIZON("horizon");
    
    String mode;
    
    AWBMode(String mode) {
        this.mode = mode;
    }
    
    /**
     * Get the value corresponding to this AWBMode.
     * @return the command line option value corresponding to this AWB mode.
     */
    public String getValue() {
        return mode;
    }
}
