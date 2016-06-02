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
 * <b>This class is used to set general parameters regarding the camera options.</b>
 * 
 * <p>Those options are general in the sense that they can be used for a simple 
 * preview as well as for a new picture or a new video.</p>
 * 
 * <p>The default, min and max values of those options are defined according
 * to informations given in official documentation 
 * (<a href="https://www.raspberrypi.org/documentation/raspbian/applications/camera.md">RaspberryPi/Camera</a>)
 * 
 * @see PreviewConfig
 * @see PictureConfig
 * @see VideoConfig
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class CameraControlOptions {
    
    /**
     * Default sharpness value.
     */
    public static final int DEFAULT_SHARPNESS = 0;
    
    /**
     * Maximum sharpness value.
     */
    public static final int MAX_SHARPNESS = 100;
    
    /**
     * Minimum sharpness value.
     */
    public static final int MIN_SHARPNESS = -100;
    
    /**
     * Deault contrast value.
     */
    public static final int DEFAULT_CONTRAST = 0;
    
    /**
     * Maximum contrast value.
     */
    public static final int MAX_CONTRAST = 100;
    
    /**
     * Minimum contrast value.
     */
    public static final int MIN_CONTRAST = -100;
    
    /**
     * Default brightness value.
     */
    public static final int DEFAULT_BRIGHTNESS = 50;
    
    /**
     * Maximum brightness value.
     */
    public static final int MAX_BRIGHTNESS = 100;
    
    /**
     * Minimum brightness value.
     */
    public static final int MIN_BRIGHTNESS = 0;
    
    /**
     * Default saturation value.
     */
    public static final int DEFAULT_SATURATION = 0;
    
    /**
     * Maximum saturation value.
     */
    public static final int MAX_SATURATION = 100;
    
    /**
     * Minimum saturation value.
     */
    public static final int MIN_SATURATION = -100;

    /**
     * Maximum ISO value.
     */
    public static final int MAX_ISO = 800;
    
    /**
     * Minimum ISO value.
     */
    public static final int MIN_ISO = 100;
    
    /**
     * Default EV compensation value.
     */
    public static final int DEFAULT_EV_COMPENSATION = 0;
    
    /**
     * Maximum EV compensation value.
     */
    public static final int MAX_EV_COMPENSATION = 10;
    
    /**
     * Minimum EV compensation value.
     */
    public static final int MIN_EV_COMPENSATION = -10;
    
    /**
     * Sharpness value.
     */
    private int sharpness = DEFAULT_SHARPNESS;
    
    /**
     * Contrast value.
     */
    private int contrast = DEFAULT_CONTRAST;
    
    /**
     * Brightness value.
     */
    private int brightness = DEFAULT_BRIGHTNESS;
    
    /**
     * Saturation value;
     */
    private int saturation = DEFAULT_SATURATION;
    
    /**
     * ISO value. -1 if disabled.
     */
    private int iso = -1;
    
    /**
     * EV compensation value of the image.
     */
    private int evCompensation = DEFAULT_EV_COMPENSATION;
    
    /**
     * Exposure mode.
     */
    private ExposureMode exposureMode = ExposureMode.AUTO;
    
    /**
     * Automatic White Balance (AWB) mode.
     */
    private AWBMode awbMode = AWBMode.AUTO;
    
    /**
     * Image effect.
     */
    private ImageEffect imageEffect = ImageEffect.NONE;
    
    /**
     * Rotation of the image in viewfinder and resulting image.
     */
    private RotationDegree rotationDegree = RotationDegree.ANGLE_0;
    
    /**
     * Horizontal flip.
     */
    private boolean horizontalFlip = false;
    
    /**
     * Vertical flip.
     */
    private boolean verticalFlip = false;
    
    /**
     * Camera sensor mode.
     */
    private CameraMode cameraMode = CameraMode.DISABLED;
    
    /**
     * Text and/or metadata added to the picture.
     */
    private String annotationTxt = null;
    
    /**
     * Add annotation time to the picture. 
     */
    private boolean annotationTime = false;
    
    /**
     * Add annotation date to the picture.
     */
    private boolean annotationDate = false;
    
    /**
     * Set the sharpness of the image. Sharpness value between -100 and 100.
     * Default is 0.
     * @param sharpness the new sharpness value, in the -100..100 interval.
     */
    public void setSharpness(int sharpness) {
        if(sharpness > MAX_SHARPNESS)
            sharpness = MAX_SHARPNESS;
        else if(sharpness < MIN_SHARPNESS)
            sharpness = MIN_SHARPNESS;
        this.sharpness = sharpness;
    }
    
    /**
     * Set the contrast of the image. Contrast value between -100 and 100.
     * Default is 0.
     * @param contrast the new contrast value, in the -100..100 interval.
     */
    public void setContrast(int contrast) {
        if(contrast > MAX_CONTRAST)
            contrast = MAX_CONTRAST;
        else if(contrast < MIN_CONTRAST)
            contrast = MIN_CONTRAST;
        this.contrast = contrast;
    }
    
    /**
     * Set the brightness of the image. 0 is black, 100 is white.
     * Default is 50.
     * @param brightness the new brightness value, in the 0..100 interval.
     */
    public void setBrightness(int brightness) {
        if(brightness > MAX_BRIGHTNESS)
            brightness = MAX_BRIGHTNESS;
        else if(brightness < MIN_BRIGHTNESS)
            brightness = MIN_BRIGHTNESS;
        this.brightness = brightness;
    }
    
    /**
     * Set the colour saturation of the image. Saturation value between -100 and 100.
     * 0 is the default.
     * @param saturation the new saturation value, in the -100..100 interval.
     */
    public void setSaturation(int saturation) {
        if(saturation > MAX_SATURATION)
            saturation = MAX_SATURATION;
        else if(saturation < MIN_SATURATION)
            saturation = MIN_SATURATION;
        this.saturation = saturation;
    }
    
    /**
     * Sets the ISO to be used for captures. Range is 100 to 800.
     * @param iso the new ISO value, in the 100..800 interval.
     */
    public void setISO(int iso) {
        if(iso > MAX_ISO)
            iso = MAX_ISO;
        else if(iso < MIN_ISO)
            iso = MIN_ISO;
        this.iso = iso;
    }
    
    /**
     * Set the EV compensation of the image. Range is -10 to +10.
     * Default is 10.
     * @param evCompensation the new EV compensation value, in the -10..10 interval.
     */
    public void setEVCompensation(int evCompensation) {
        if(evCompensation > MAX_EV_COMPENSATION)
            evCompensation = MAX_EV_COMPENSATION;
        else if(evCompensation < MIN_EV_COMPENSATION)
            evCompensation = MIN_EV_COMPENSATION;
        this.evCompensation = evCompensation;
    }
    
    /**
     * Set the exposure mode.
     * @param exposureMode the new exposure mode.
     */
    public void setExposureMode(ExposureMode exposureMode) {
        this.exposureMode = exposureMode;
    }
    
    /**
     * Set the Automatic White Balance (AWB) mode.
     * @param awbMode the new automatic white balance mode.
     */
    public void setAWBMode(AWBMode awbMode) {
        this.awbMode = awbMode;
    }
    
    /**
     * Set an effect to be applied to the image.
     * @param imageEffect the new effected to be applied to the image.
     */
    public void setImageEffect(ImageEffect imageEffect) {
        this.imageEffect = imageEffect;
    }
    
    /**
     * Sets the rotation of the image in viewfinder and resulting image.
     * This can take any value from 0 upwards, but due to hardware constraints
     * only 0, 90, 180 and 270 degree rotations are supported (cfr RotationDegree).
     * @param rotationDegree the new rotation to be applied to the image.
     */
    public void setRotationDegree(RotationDegree rotationDegree) {
        this.rotationDegree = rotationDegree;
    }
    
    /**
     * Flips the preview and saved image horizontally.
     * @param active true to flip the image horizontally. False otherwise.
     */
    public void setHorizontalFlip(boolean active) {
        this.horizontalFlip = active;
    }
    
    /**
     * Flips the preview and saved image vertically.
     * @param active true to flip the image vertically. False otherwise.
     */
    public void setVerticalFlip(boolean active) {
        this.verticalFlip = active;
    }
    
    /**
     * Sets a specified sensor mode, disabling the automatic selection.
     * @param cameraMode the new camera mode to be applied on the image.
     */
    public void setCameraMode(CameraMode cameraMode) {
        this.cameraMode = cameraMode;
    }
    
    /**
     * Add some text and/or metadata to the picture.
     * @param annotationTime true to add time on the picture. False otherwise.
     * @param annotationDate true to add date on the picture. False otherwise.
     * @param annotationTxt the text to add on the picture. null if unused
     */
    public void annotate(boolean annotationTime, boolean annotationDate, String annotationTxt) {
        this.annotationTime = annotationTime;
        this.annotationDate = annotationDate;
        this.annotationTxt = annotationTxt;
    }
    
    /**
     * Get the options to add to command line requests, regarding
     * camera control options.
     * @return the options to add to raspistill and raspivid command line requests.
     */
    protected String getOptions() {
        String command = "";
        
        command += " -sh " + sharpness;
        command += " -co " + contrast;
        command += " -br " + brightness;
        command += " -sa " + saturation;
        if(iso != -1)
            command += " -ISO " + iso;
        command += " -ev " + evCompensation;
        command += " -ex " + exposureMode.getValue();
        command += " -awb " + awbMode.getValue();
        command += " -ifx " + imageEffect.getValue();
        command += " -rot " + rotationDegree.getValue();
        if(horizontalFlip)
            command += " -hf";
        if(verticalFlip)
            command += " -vf";
        if(cameraMode != CameraMode.DISABLED)
            command += " -md " + cameraMode.getValue();
        if(annotationTime && annotationDate)
            command += " -a 12";
        else if(annotationTime)
            command += " -a 4";
        else if(annotationDate)
            command += " -a 8";
        if(annotationTxt != null)
            command += " -a \"" + annotationTxt + "\"";
        
        return command;
    }
}
