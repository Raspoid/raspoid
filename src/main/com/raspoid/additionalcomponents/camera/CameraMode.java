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
 * <b>This enum lists the different camera mode that can be applied to the image.</b>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public enum CameraMode {
    /**
     * Disabled camera mode.
     */
    DISABLED("disabled"),
    
    /**
     * Automatic selection (default).
     */
    MODE_0("0"),
    
    /**
     * Size: 1920x1080 | Aspect ratio: 16:9 | Frame rates: 1-30fps | FOV: Partial | Binning: None.
     */
    MODE_1("1"),
    
    /**
     * Size: 2592x1944 | Aspect ratio: 4:3 | Frame rates: 1-15fps | FOV: Full | Binning: None
     */
    MODE_2("2"),
    
    /**
     * Size: 2592x1944 | Aspect ratio: 4:3 | Frame rates: 0.1666-1fps | FOV: Full | Binning: None
     */
    MODE_3("3"),
    
    /**
     * Size: 1296x972 | Aspect ratio: 4:3 | Frame rates: 1-42fps | FOV: Full | Binning: 2x2
     */
    MODE_4("4"),
    
    /**
     * Size: 1296x730 | Aspect ratio: 16:9 | Frame rates: 1-49fps | FOV: Full | Binning: 2x2
     */
    MODE_5("5"),
    
    /**
     * Size: 640x480 | Aspect ratio: 4:3 | Frame rates: 42.1-60fps | FOV: Full | Binning: 2x2 plus skip
     */
    MODE_6("6"),
    
    /**
     * Size: 640x480 | Aspect ratio: 4:3 | Frame rates: 60.1-90fps | FOV: Full | Binning: 2x2 plus skip
     */
    MODE_7("7");
    
    String mode;
    
    CameraMode(String mode) {
        this.mode = mode;
    }
    
    /**
     * Get the value corresponding to this CameraMode.
     * @return the command line option value corresponding to this camera mode.
     */
    public String getValue() {
        return mode;
    }
}
