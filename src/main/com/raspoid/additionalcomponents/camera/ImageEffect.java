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
 * <b>Image effects that can be applied to the Camera Pi images.</pi>
 * 
 * <p>Note that not all of these settings may be available in all
 * circumstances.</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public enum ImageEffect {
    
    /**
     * NO effect (default).
     */
    NONE("none"),
    
    /**
     * Negate the image.
     */
    NEGATIVE("negative"),
    
    /**
     * Solarise the image.
     */
    SOLARISE("solarise"),
    
    /**
     * Posterise the image.
     */
    POSTERISE("posterise"),
    
    /**
     * Whiteboard effect.
     */
    WHITEBOARD("whiteboard"),
    
    /**
     * Blackboard effect.
     */
    BLACKBOARD("blackboard"),
    
    /**
     * Sketch style effect.
     */
    SKETCH("sketch"),
    
    /**
     * Denoise the image.
     */
    DENOISE("denoise"),
    
    /**
     * Emboss the image.
     */
    EMBOSS("emboss"),
    
    /**
     * Apply an oil paint style effect.
     */
    OILPAINT("oilpaint"),
    
    /**
     * Hatch sketch style.
     */
    HATCH("hatch"),
    
    /**
     * gpen
     */
    GPEN("gpen"),
    
    /**
     * A pastel style effect.
     */
    PASTEL("pastel"),
    
    /**
     * A watercolour style effect.
     */
    WATERCOLOUR("watercolour"),
    
    /**
     * Film grain style effect.
     */
    FILM("film"),
    
    /**
     * Blur the image.
     */
    BLUR("blur"),
    
    /**
     * Colour saturate the image.
     */
    SATURATION("saturation");
    
    String effect;
    
    ImageEffect(String effect) {
        this.effect = effect;
    }
    
    /**
     * Get the value corresponding to this ImageEffect.
     * @return the command line option value corresponding to this image effect.
     */
    public String getValue() {
        return effect;
    }
}
