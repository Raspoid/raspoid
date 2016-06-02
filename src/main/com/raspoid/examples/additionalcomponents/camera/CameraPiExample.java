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
package com.raspoid.examples.additionalcomponents.camera;

import com.raspoid.Tools;
import com.raspoid.additionalcomponents.camera.CameraPi;
import com.raspoid.additionalcomponents.camera.ExposureMode;
import com.raspoid.additionalcomponents.camera.Picture;
import com.raspoid.additionalcomponents.camera.PictureConfig;
import com.raspoid.additionalcomponents.camera.Video;
import com.raspoid.network.NetworkUtilities;

/**
 * Example of use of the Camera Pi.
 * 
 * @see CameraPi
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class CameraPiExample {
    
    /**
     * Private constructor to hide the implicit public one.
     */
    private CameraPiExample() {
    }
    
    /**
     * CameraPi features examples.
     * @param args input args. Unused here.
     */
    public static void main(String[] args) {
        // PREVIEW
        CameraPi.preview(5000);
        
        // PICTURES
        Picture picture1 = CameraPi.takePicture();
        Tools.log("New picture: " + picture1.getFilePath());
        
        PictureConfig pictureConfig = new PictureConfig("snowy_scenery", 2592, 1944);
        pictureConfig.setExposureMode(ExposureMode.SNOW);
        Picture picture2 = CameraPi.takePicture(pictureConfig);
        Tools.log("New picture: " + picture2.getFilePath());
        
        // VIDEOS
        Video video = CameraPi.takeVideo(5000);
        Tools.log("New video: " + video.getFilePath());
        String convertedVideoFilePath = video.convertToMP4();
        Tools.log("Converted file: " + convertedVideoFilePath);
        
        // STREAMING
        CameraPi.startGStreamerServer(NetworkUtilities.getIpAddresses().get(0), 
                NetworkUtilities.getAvailablePort(), 640, 360, true, true, 2500000, true, false);
    }
}
