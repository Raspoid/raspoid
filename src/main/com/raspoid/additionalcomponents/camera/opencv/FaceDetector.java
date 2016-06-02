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
package com.raspoid.additionalcomponents.camera.opencv;

import java.util.Date;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;

import com.raspoid.Tools;
import com.raspoid.examples.additionalcomponents.camera.opencv.FaceDetectorExample;
import com.raspoid.exceptions.RaspoidException;

/**
 * This class is used to illustrate the utilisation of OpenCV to detect faces on a picture.
 * 
 * <p>Example of use: {@link FaceDetectorExample}</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class FaceDetector {
    
    private static String haarcascadeFrontalfaceAltXmlFilePath = "/home/pi/opencv/data/haarcascades/haarcascade_frontalface_alt.xml";
    
    /**
     * Private constructor to hide the implicit public one.
     */
    private FaceDetector() {
    }
    
    /**
     * Launches a new search to detect faces on a specific image, and returns the number 
     * of faces detected on a specific image.
     * 
     * <p><b>! Attention !</b> Up to 68 seconds on a Raspberry Pi 2 for a picture of 2592x1944.<br>
     * 4 seconds for a picture of 640x480.</p>
     * 
     * @param image the org.opencv.core.Mat object corresponding to the image to analyze.
     * @return the number of faces detected on the image.
     */
    public static int detectFacesNb(Mat image) {
        return FaceDetector.detectFaces(image).length;
    }
    
    /**
     * Launches a new search to detect faces on a specific image and creates a new image 
     * with rectangles surrounding the detected faces.
     * 
     * <p><b>! Attention !</b> Up to 68 seconds on a Raspberry Pi 2 for a picture of 2592x1944.<br>
     * 4 seconds for a picture of 640x480.</p>
     * 
     * @param image the org.opencv.core.Mat object corresponding to the image to analyze.
     * @param outputFilename the name given to the newly created image with detected faces.
     * @return true in case of success when creating the output file. False in case of failure.
     */
    public static boolean detectAndSurroundFaces(Mat image, String outputFilename) {
        return surroundFaces(image, FaceDetector.detectFaces(image), outputFilename);
    }
    
    /**
     * Creates a new output image from the input image with faces surrounded with green boxes.
     * @param image the input image previously analyzed.
     * @param faces array of coordinates corresponding to the previously detected faces.
     * @param outputFilename the output file name.
     * @return true in cas of success when creating the output file. False in case of failure.
     */
    public static boolean surroundFaces(Mat image, Rect[] faces, String outputFilename) {
        if(outputFilename == null || outputFilename.isEmpty())
            throw new RaspoidException("The output filename can't be empty.");
        
        for(Rect face : faces)
            Core.rectangle(image,
                    new Point(face.x, face.y),
                    new Point(face.x + face.width, face.y + face.height),
                    new Scalar(0, 255, 0));
        
        return Highgui.imwrite(outputFilename, image);
    }
    
    /**
     * Executes the OpenCV "haarcascade_frontalface_alt" cascade classifier on a specific image
     * and returns an array of org.opencv.core.Rect. Each Rect corresponds to a face detected on the image.
     * 
     * <p><b>! Attention !</b> Up to 68 seconds on a Raspberry Pi 2 for a picture of 2592x1944.<br>
     * 4 seconds for a picture of 640x480.</p>
     * 
     * @param image the org.opencv.core.Mat object corresponding to the image to analyze.
     * @return an array of org.opencv.core.Rect. Each Rect corresponds to a face on the image.
     */
    public static Rect[] detectFaces(Mat image) {
        CascadeClassifier faceDetector = new CascadeClassifier(haarcascadeFrontalfaceAltXmlFilePath);
        
        MatOfRect faceDetections = new MatOfRect();
        Tools.debug("Detect multiscale: START (timestamp " + new Date() + ")", Tools.Color.ANSI_YELLOW);
        faceDetector.detectMultiScale(image, faceDetections);
        Tools.debug("Detect multiscale: STOP  (timestamp " + new Date() + ")", Tools.Color.ANSI_YELLOW);
        
        return faceDetections.toArray();
    }
    
    /**
     * Sets the file path of the OpenCV haarcascade_frontalface_alt.xml file 
     * used to detect faces on pictures with {@link #detectFaces(Mat)}, {@link #detectFacesNb(Mat)} and {@link #detectAndSurroundFaces(Mat, String)}.
     * <p>Default file path is "/home/pi/opencv/data/haarcascades/haarcascade_frontalface_alt.xml".</p>
     * @param filePath the new file path of the OpenCV haarcascade_frontalface_alt.xml file to use to detect faces.
     */
    public static void setHaarcascadeFrontalfaceAltXmlFilePath(String filePath) {
        haarcascadeFrontalfaceAltXmlFilePath = filePath;
    }
}
