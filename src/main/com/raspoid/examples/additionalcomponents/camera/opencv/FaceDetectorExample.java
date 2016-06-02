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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.highgui.Highgui;

import com.raspoid.Tools;
import com.raspoid.additionalcomponents.camera.CameraPi;
import com.raspoid.additionalcomponents.camera.PictureConfig;
import com.raspoid.additionalcomponents.camera.opencv.FaceDetector;

/**
 * Example of use of the face detector algorithm using OpenCV.
 * 
 * @see FaceDetector
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class FaceDetectorExample {
    
    /**
     * Private constructor to hide the implicit public one.
     */
    private FaceDetectorExample() {
    }
    
    /**
     * Command-line interface.
     * @param args unused here.
     */
    public static void main(String[] args) {
        // Load the OpenCV native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        // Take a picture with the camera pi
        String pictureId = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
        PictureConfig pictureConfig = new PictureConfig("capture_" + pictureId, 640, 480);
        pictureConfig.setVerticalFlip(true);
        String filePath = CameraPi.takePicture(pictureConfig).getFilePath();
        
        // Look for faces
        Mat image = Highgui.imread(filePath);
        Rect[] faces = FaceDetector.detectFaces(image);
        Tools.log(String.format("%s faces detected", faces.length));
        Tools.log("Input file path: " + filePath);
        
        // Create a new picture, with detected faces
        FaceDetector.surroundFaces(image, faces, "output_" + pictureId + ".jpg");
        Tools.log("Output file: " + "output_" + pictureId + ".jpg");
    }
}
