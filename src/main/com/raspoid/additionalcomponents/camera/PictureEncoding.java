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
 * <b>Available encodings to use for new picture output files.</b>
 * 
 * <p>Valid options are jpg, bmp, gif and png. Note that unaccelerated image types (gif, png, bmp)
 * will take much longer to save than JPG which is hardware accelerated.</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public enum PictureEncoding {
    
    /**
     * jpg.
     */
    JPG("jpg"),
    
    /**
     * bmp.
     */
    BMP("bmp"),
    
    /**
     * gif.
     */
    GIF("gif"),
    
    /**
     * png.
     */
    PNG("png");
    
    String encoding;
    
    PictureEncoding(String encoding) {
        this.encoding = encoding;
    }
    
    /**
     * Get the value corresponding to this PictureEncoding.
     * @return the command line option value corresponding to this picture encoding.
     */
    public String getValue() {
        return encoding;
    }
}
