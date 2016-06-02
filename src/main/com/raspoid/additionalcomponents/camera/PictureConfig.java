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

import com.raspoid.Tools;

/**
 * <b>This class is used to easily wrap the complete raspistill command line tool.</b>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class PictureConfig extends CameraControlOptions {
    
    /**
     * Path to the raspistill executable.
     */
    public static final String BASE_COMMAND = "/usr/bin/raspistill";
    
    /**
     * Default image width, in pixels.
     */
    public static final int DEFAULT_WIDTH = 2592;
    
    /**
     * Maximum image width, in pixels.
     */
    public static final int MAX_WIDTH = 5000;
    
    /**
     * Minimum image width, in pixels.
     */
    public static final int MIN_WIDTH = 18;
    
    /**
     * Default image height, in pixels.
     */
    public static final int DEFAULT_HEIGHT = 1944;
    
    /**
     * Maximum image height, in pixels.
     */
    public static final int MAX_HEIGHT = 5000;
    
    /**
     * Minimum image height, in pixels.
     */
    public static final int MIN_HEIGHT = 18;
    
    /**
     * Default image quality, in percent.
     */
    public static final int DEFAULT_QUALITY = 75;
    
    /**
     * Maximum quality, in percent.
     */
    public static final int MAX_QUALITY = 100;
    
    /**
     * Minimum quality, in percent.
     */
    public static final int MIN_QUALITY = 0;
    
    /**
     * Default timeout value, in milliseconds.
     */
    public static final int DEFAULT_TIMEOUT = 1;
    
    /**
     * Minimum timeout value, in milliseconds.
     */
    public static final int MIN_TIMEOUT = 0;
    
    /**
     * Minimum time-lapse, in milliseconds.
     */
    public static final int MIN_TIMELAPSE = 0;
    
    /**
     * Default thumbnail x value, in pixels.
     */
    public static final int DEFAULT_THUMBNAIL_X = 64;
    
    /**
     * Minimum thumbnail x value, in pixels.
     */
    public static final int MIN_THUMBNAIL_X = 0;
    
    /**
     * Default thumbnail y value, in pixels.
     */
    public static final int DEFAULT_THUMBNAIL_Y = 48;
    
    /**
     * Minimum thumbnail y value, in pixels.
     */
    public static final int MIN_THUMBNAIL_Y = 0;
    
    /**
     * Default thumbnail quality value, in percent.
     */
    public static final int DEFAULT_THUMBNAIL_QUALITY = 35;
    
    /**
     * Maximum thumbnail quality value, in percent.
     */
    public static final int MAX_THUMBNAIL_QUALITY = 100;
    
    /**
     * Minimum thumbnail quality value, in percent.
     */
    public static final int MIN_THUMBNAIL_QUALITY = 0;
    
    /**
     * Image width.
     */
    private int width = DEFAULT_WIDTH;
    
    /**
     * Image height.
     */
    private int height = DEFAULT_HEIGHT;
    
    /**
     * jpeg quality <0 to 100>.
     */
    private int quality = DEFAULT_QUALITY;
    
    private boolean addRawBayerData = false;
    private String outputFilename = null;
    private boolean verbose = false;
    private int timeout = DEFAULT_TIMEOUT;
    private int timelapse = -1;
    private int thumbnailX = DEFAULT_THUMBNAIL_X;
    private int thumbnailY = DEFAULT_THUMBNAIL_Y;
    private int thumbnailQuality = DEFAULT_THUMBNAIL_QUALITY;
    private boolean enableThumbnail = true;
    private PictureEncoding encoding = null;
    private boolean keypressMode = false;
    
    /**
     * Constructor for a new PictureConfig using only default values 
     * for parameters.
     */
    public PictureConfig() {
        super();
    }
    
    /**
     * Constructor for a new PictureConfig using specific values for
     * the width and the height. Other parameters values are defaults.
     * @param width the width to use for the new picture, in pixels.
     * @param height the height to use for the new picture, in pixels.
     */
    public PictureConfig(int width, int height) {
        this();
        setWidth(width);
        setHeight(height);
    }
    
    /**
     * Constructor for a new PictureConfig using specific values for
     * the width, the height and the quality. Other parameters values
     * are defaults.
     * @param width the width to use for the new picture, in pixels.
     * @param height the height to use for the new picture, in pixels.
     * @param quality the quality for the new picture, in percent.
     */
    public PictureConfig(int width, int height, int quality) {
        this(width, height);
        setQuality(quality);
    }
    
    /**
     * Constructor for a new PictureConfig using specific values for
     * the output file name, the width and the height.
     * @param outputFilename the output file name, without the extension part.
     * @param width the width to use for the new picture, in pixels.
     * @param height the height to use for the new picture, in pixels.
     */
    public PictureConfig(String outputFilename, int width, int height) {
        this(width, height);
        setOutputFilename(outputFilename);
    }
    
    /**
     * Constructor for a new PictureConfig using specific values for
     * the output file name, the width, the height and the quality.
     * @param outputFilename the output file name, without the extension part.
     * @param width the width to use for the new picture, in pixels.
     * @param height the height to use for the new picture, in pixels.
     * @param quality the quality for the new picture, in percent.
     */
    public PictureConfig(String outputFilename, int width, int height, int quality) {
        this(width, height, quality);
        setOutputFilename(outputFilename);
    }
    
    /**
     * Set the image width.
     * @param width the new width to apply for future pictures.
     */
    public void setWidth(int width) {
        if(width < MIN_WIDTH) {
            width = MIN_WIDTH;
            Tools.log("The minimum picture width is " + MIN_WIDTH + " pixels. Width set to " + MIN_WIDTH + ".");
        } else if(width > MAX_WIDTH) {
            width = MAX_WIDTH;
            Tools.log("The maximum picture width is " + MAX_WIDTH + " pixels. Width set to " + MAX_WIDTH + ".");
        }
        this.width = width;
    }
    
    /**
     * Set the image height.
     * @param height the new height to apply for future pictures.
     */
    public void setHeight(int height) {
        if(height < MIN_HEIGHT) {
            height = MIN_HEIGHT;
            Tools.log("The minimum picture height is " + MIN_HEIGHT + " pixels. Height set to " + MIN_HEIGHT + ".");
        } else if(height > MAX_HEIGHT) {
            height = MAX_HEIGHT;
            Tools.log("The maximum picture height is " + MAX_HEIGHT + " pixels. Height set to " + MAX_HEIGHT + ".");
        }
        this.height = height;
    }
    
    /**
     * Set the jpeg quality. The quality must be between 0 and 100.
     * <p>Quality 100 is almost completely uncompressed. 75 is a good all round value.</p>
     * @param quality the new quality to apply for future pictures.
     */
    public void setQuality(int quality) {
        if(quality > MAX_QUALITY)
            quality = MAX_QUALITY;
        else if(quality < MIN_QUALITY)
            quality = MIN_QUALITY;
        this.quality = quality;
    }
    
    /**
     * Enable/disable the insertion of raw Bayer data into the jpeg metadata.
     * <p>If true, this option inserts the raw Bayer data from the camera into the jpeg metadata.</p>
     * @param enable true to enable the insertion of raw Bayer data into the jpeg metadata. False otherwise.
     */
    public void enableRawBayerData(boolean enable) {
        addRawBayerData = enable;
    }
    
    /**
     * Specify the output filename.
     * <p>If not specified (filename = null), a default name is given.
     * If the filename is '-', then all output is sent to stdout.</p>
     * @param filename the output file name, without the extension.
     */
    public void setOutputFilename(String filename) {
        this.outputFilename = filename;
    }
    
    /**
     * Returns the output filename, with the extension.
     * If the filename is '-', "-" is returned.
     * @return the output filename.
     */
    public String getOutputFilenameWithExtension() {
        String result;
        if(outputFilename == null)
            result = "capture";
        else
            result = outputFilename;
        
        if(!"-".equals(result))
            result += "." + (encoding == null ? "jpg" : encoding.getValue());
        return result;
    }
    
    /**
     * Enable/disable the verbose mode.
     * <p>If true, outputs debugging/information messages during the program run.</p>
     * @param verbose true to output debugging/information messages during the program run.
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
    
    /**
     * Sets the time (in ms) before taking a picture and shuts down.
     * <p>The program will run for a duration of timeout milliseconds,
     * and then take the capture (if output is specified).
     * If not specified, this timeout is set to 1 millisecond.</p>
     * @param timeout the new timeout to apply for future pictures. 
     */
    public void setTimeout(int timeout) {
        if(timeout < MIN_TIMEOUT)
            timeout = MIN_TIMEOUT;
        this.timeout = timeout;
    }
    
    /**
     * Set the time-lapse value.
     * <p>This specific value is the time between shots in milliseconds.
     * Note you should specify %04d at the point in the filename where you want a frame 
     * count number to appear. e.g:</p>
     *      <pre>-t 30000 -tl 2000 -o image%04d.jpg</pre>
     * <p>will produce a capture every 2 seconds, over a total period of 30s, named image0001.jpg,
     * image0002.jpg..image0015.jpg. Note that the %04d indicates a 4 digit number with leading
     * zero's added to pad to the required number of digits. So, for example, %08d would result 
     * in an 8 digit number.</p>
     * 
     * <p>If a time-lapse value of 0 is entered, the application will take pictures as fast as possible.
     * Note there is an minimum enforced pause of 30ms between captures to ensure that exposure calculations can be made.</p>
     * @param timelapse the new timelapse value to apply for future pictures. -1 to disable timelapse mode.
     */
    public void setTimelapse(int timelapse) {
        if(timelapse != -1 && timelapse < MIN_TIMELAPSE)
            timelapse = MIN_TIMELAPSE;
        this.timelapse = timelapse;
    }
    
    /**
     * Disable the thumbnail.
     * <p>If disabled, no thumbnail information will be placed in the file.
     * This reduces the file size slightly.</p>
     */
    public void disableThumbnail() {
        enableThumbnail = false;
    }
    
    /**
     * Allows specification of the thumbnail image inserted in to the JPEG file.
     * <p>If not specified, defaults are a size of 64x48 at quality 35.</p>
     * @param x the new thumbnail x value to apply for future pictures.
     * @param y the new thumbnail y value to apply for future pictures.
     * @param quality the new thumbnail quality to apply for future picture.
     */
    public void setThumbnail(int x, int y, int quality) {
        if(x < MIN_THUMBNAIL_X)
            x = MIN_THUMBNAIL_X;
        if(y < MIN_THUMBNAIL_Y)
            y = MIN_THUMBNAIL_Y;
        if(quality > MAX_THUMBNAIL_QUALITY)
            quality = MAX_THUMBNAIL_QUALITY;
        else if(quality < MIN_THUMBNAIL_QUALITY)
            quality = MIN_THUMBNAIL_QUALITY;
        thumbnailX = x;
        thumbnailY = y;
        thumbnailQuality = quality;
        enableThumbnail = true;
    }
    
    /**
     * Set the encoding to use for output file.
     * <p>Valid options are jpg, bmp, gif and png. Note that unaccelerated image types 
     * (gif, png, bmp) will take much longer to save than JPG which is hardware accelerated.</p>
     * <p>Also note that the filename suffix is completely ignored when deciding the encoding of a file.</p>
     * @param encoding null if disabled.
     */
    public void setEncoding(PictureEncoding encoding) {
        this.encoding = encoding;
    }
    
    /**
     * Enable/disable the keypress mode.
     * <p>The camera is run for the requested time (-t), and a captures can be initiated throughout
     * that by pressing the Enter key. Press X then Enter will exit the application before the 
     * timeout is reached. If the timeout is set to 0, the camera will run indefinitely until 
     * X then Enter is typed. Using the verbose option (-v) will display a prompt asking for 
     * user input, otherwise no prompt is displayed.</p>
     * @param enable true to enable the keypress mode. False to disable.
     */
    public void enableKeypressMode(boolean enable) {
        this.keypressMode = enable;
    }
    
    /**
     * Get the complete command to execute to take a picture with the requested parameters.
     * @return the complete command to execute to take the picture with specified values for the parameters.
     */
    public String getCommand() {
        String command = BASE_COMMAND;
        
        command += " -w " + width;
        command += " -h " + height;
        command += " -q " + quality;
        
        if(addRawBayerData)
            command += " -r";
        command += " -o " + getOutputFilenameWithExtension();
        if(verbose)
            command += " -v";
        command += " -t " + timeout;
        if(timelapse != -1)
            command += " -tl " + timelapse;
        if(enableThumbnail)
            command += " -th " + thumbnailX + ":" + thumbnailY + ":" + thumbnailQuality;
        else
            command += " -th none";
        if(encoding != null)
            command += " -e " + encoding.getValue();
        if(keypressMode)
            command += " -k";
        
        command += this.getOptions();
        
        return command;
    }
}
