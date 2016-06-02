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
 * <b>This class is used to easily wrap the specific part of the
 * raspistill/raspivid commands regarding the preview configuration.</b>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class PreviewConfig extends CameraControlOptions {
    
    /**
     * Maximum opacity (fully opaque).
     */
    public static final int MAX_OPACITY = 255;
    
    /**
     * Minimum opacity (invisible).
     */
    public static final int MIN_OPACITY = 0;
        
    /**
     * True if preview enabled.
     */
    private boolean preview = false;
    
    /**
     * True if fullscreen preview enabled.
     */
    private boolean fullscreenPreview = false;
    
    /**
     * Preview window settings, x position.
     */
    private int x;
    
    /**
     * Preview window settings, y position.
     */
    private int y;
    
    /**
     * Preview window settings, width.
     */
    private int w;
    
    /**
     * Preview window settings, height.
     */
    private int h;
    
    /**
     * Opacity of the preview window. -1 if disabled.
     */
    private int opacity = -1;
    
    /**
     * Enable the preview window and define the size and location on the screen that the preview 
     * window will be placed.
     * <p>Note this will be superimposed over the top of any other 
     * windows/graphics.</p>
     * @param x the x position of the window on the screen, in pixels.
     * @param y the y position of the window on the screen, in pixels.
     * @param w the width of the window, in pixels.
     * @param h the height of the window, in pixels.
     */
    public void enablePreview(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        preview = true;
    }
    
    /**
     * Enable fullscreen preview mode. Forces the preview window to use the whole screen.
     * <p>Note that the aspect ratio of the incoming image will be retained, so there may be bars on some edges.</p>
     */
    public void enablePreviewFullscreen() {
        preview = true;
        fullscreenPreview = true;
    }
    
    /**
     * Disables the preview window completely.
     * <p>Note that even though the preview is disabled, the camera will still be producing frames, so will be using power.</p>
     */
    public void disablePreview() {
        preview = false;
    }
    
    /**
     * Sets the opacity of the preview windows.
     * <p>0 = invisible, 255 = fully opaque. -1 if disabled.</p>
     * @param opacity the new opacity to apply for future previews.
     */
    public void setOpacity(int opacity) {
        if(opacity != -1) {
            if(opacity > MAX_OPACITY)
                opacity = MAX_OPACITY;
            else if(opacity < MIN_OPACITY)
                opacity = MIN_OPACITY;
        }
        this.opacity = opacity;
    }
    
    /**
     * Get the preview settings part to use with the complete command line.
     * @return the preview settings part to use with the complete raspistill/raspivid command lines.
     */
    public String getPreviewSettings() {
        String command = "";
        
        if(preview) {
            if(fullscreenPreview)
                command += " -f";
            else
                command += " -p " + x + "," + y + "," + w + "," + h;
            
            if(opacity != -1)
                command += " -op " + opacity;
        } else {
            command += " -n"; // Do not display a preview window
        }
        
        command += this.getOptions();
        
        return command;
    }
}
