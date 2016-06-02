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
 * <b>Exposure modes.</b>
 * 
 * <p>Note that not all of these settings may be implemented,
 * depending on camera tuning.</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public enum ExposureMode {
    
    /**
     * Use automatic exposure mode.
     */
    AUTO("auto"),
    
    /**
     * Select setting for night shooting.
     */
    NIGHT("night"),
    
    /**
     * nightpreview.
     */
    NIGHT_PREVIEW("nightpreview"),
    
    /**
     * Select setting for back lit subject.
     */
    BACKLIGHT("backlight"),
    
    /**
     * spotlight.
     */
    SPOTLIGHT("spotlight"),
    
    /**
     * Select setting for sports (fast shutter etc).
     */
    SPORT("sports"),
    
    /**
     * Select setting optimised for snowy scenery.
     */
    SNOW("snow"),
    
    /**
     * Select setting optimised for beach.
     */
    BEACH("beach"),
    
    /**
     * Select setting for long exposures.
     */
    VERYLONG("verylong"),
    
    /**
     * Constrain fps to a fixed value.
     */
    FIXEDFPS("fixedfps"),
    
    /**
     * Antishake mode.
     */
    ANTISHAKE("antishake"),
    
    /**
     * Select settings.
     */
    FIREWORKS("fireworks");
    
    String mode;
    
    ExposureMode(String mode) {
        this.mode = mode;
    }
    
    /**
     * Get the value corresponding to this ExposureMode.
     * @return the command line option value corresponding to this exposure mode.
     */
    public String getValue() {
        return mode;
    }
}
