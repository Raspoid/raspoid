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

import java.io.File;
import java.io.IOException;

import com.raspoid.Tools;
import com.raspoid.exceptions.RaspoidException;

/**
 * <b>Abstraction for a video taken by the camera pi using the Raspoid
 * raspivid wrapper.</b>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class Video {
    
    private VideoConfig config;
    private String filePath;
    
    /**
     * Constructor for a new Video, using a specific VideoConfig and
     * the file path of the video file on the system.
     * @param config the VideoConfig used to take this video.
     * @param filePath the complete file path of the video on the system.
     */
    public Video(VideoConfig config, String filePath) {
        this.config = config;
        this.filePath = filePath;
    }
    
    /**
     * Get the VideoConfig used to take this video.
     * @return the VideoConfig used to take this Video.
     */
    public VideoConfig getConfig() {
        return config;
    }
    
    /**
     * Get the complete file path of the video file on the system.
     * @return the complete file path of the video file on the system.
     */
    public String getFilePath() {
        return filePath;
    }
    
    /**
     * Converts the video from the h264 format to the mp4 format and creates a new file
     * with this converted video.
     * <p><b>! Attention !</b> the libav-tools needs to be installed on the Raspberry Pi 
     * (<code>sudo apt-get install -y libav-tools</code>)</p>
     * @return the file path of the newly created mp4 file.
     */
    public String convertToMP4() {
        try {
            Process process = Runtime.getRuntime().exec("avconv -r " + config.getFramerate() + 
                    " -i " + config.getOutputFilenameWithExtension() + " -vcodec copy " + config.getOutputFilenameWithoutExtension() + ".mp4");
            process.waitFor();
            int exitValue = process.exitValue();
            if(exitValue != 0)
                Tools.log("Exit value when converting video to mp4 != 0. (Exit value=" + exitValue + ")");
        } catch(IOException | InterruptedException e) {
            throw new RaspoidException("Error when converting video to mp4 format.", e);
        }
        return System.getProperty("user.dir") + "/" + config.getOutputFilenameWithoutExtension() + ".mp4";
    }
    
    /**
     * Get the abstract representation of the video file.
     * @return the java.io.File instance corresponding to the video file.
     */
    public File getFile() {
        return new File(filePath);
    }
    
    @Override
    public String toString() {
        return "(Video) Config: " + config.getCommand() +
                " | file path: " + filePath;
    }
}
