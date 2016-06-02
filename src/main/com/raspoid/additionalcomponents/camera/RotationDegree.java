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
 * <b>Available rotations applicable to the pictures/videos.</b>
 * 
 * <p>Due to hardware constraints only 0, 90, 180 and 270 degree 
 * rotations are supported.</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public enum RotationDegree {
    
    /**
     * 0°.
     */
    ANGLE_0("0"),
    
    /**
     * 90°.
     */
    ANGLE_90("90"),
    
    /**
     * 180°.
     */
    ANGLE_180("180"),
    
    /**
     * 270°.
     */
    ANGLE_270("270");
    
    String degree;
    
    RotationDegree(String degree) {
        this.degree = degree;
    }
    
    /**
     * Get the value corresponding to this RotationDegree.
     * @return the command line option value corresponding to this RotationDegree.
     */
    public String getValue() {
        return degree;
    }
}
