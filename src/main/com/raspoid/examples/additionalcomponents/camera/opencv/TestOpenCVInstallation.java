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
package com.raspoid.examples.additionalcomponents.camera.opencv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import com.raspoid.Tools;

/**
 * This class is simply used to test the OpenCV installation.
 * 
 * It tests the availability to access the camera,
 * and then takes a picture with the OpenCV tool.
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class TestOpenCVInstallation {
    
    /**
     * Private constructor to hide the implicit public one.
     */
    private TestOpenCVInstallation() {
    }
    
    /**
     * Command-line interface.
     * 
     * <p>This example tests the OpenCV installation
     * and takes a picture with this OpenCV tool.</p>
     * 
     * @param args unused here.
     */
    public static void main(String[] args) {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Tools.log(Core.NATIVE_LIBRARY_NAME);
        VideoCapture camera = new VideoCapture(0);
        Tools.sleepMilliseconds(1000);
        
        if(!camera.isOpened())
            Tools.log("Video capturing hasn't been correctly initialized.");
        else
            Tools.log("The camera has been correctly initialized.");
        
        Mat frame = new Mat();
        camera.read(frame);
        Highgui.imwrite("capture.jpg", frame);
    }
}
