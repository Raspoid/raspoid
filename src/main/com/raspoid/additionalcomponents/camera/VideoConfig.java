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
 * <b>This class is used to easily wrap the complete raspivid command line tool.</b>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class VideoConfig extends CameraControlOptions {
    
    /**
     * Path to the raspivid executable.
     */
    public static final String BASE_COMMAND = "/usr/bin/raspivid";
    
    /**
     * Default image width, in pixels.
     */
    public static final int DEFAULT_WIDTH = 1920;
    
    /**
     * Maximum image width, in pixels.
     */
    public static final int MAX_WIDTH = 1920;
    
    /**
     * Minimum image width, in pixels.
     */
    public static final int MIN_WIDTH = 64;
    
    /**
     * Default image height, in pixels.
     */
    public static final int DEFAULT_HEIGHT = 1080;
    
    /**
     * Maximum image height, in pixels.
     */
    public static final int MAX_HEIGHT = 1080;
    
    /**
     * Minimum image height, in pixels.
     */
    public static final int MIN_HEIGHT = 64;
    
    /**
     * Default bitrate value, in bits/s.
     * <p>For h264 1080p30, a high quality bitrate would be 15Mbits/s.</p>
     */
    public static final int DEFAULT_BITRATE = 15000000;
    
    /**
     * Minimum bitrate value, in bits/s.
     */
    public static final int MIN_BITRATE = 1000000;
    
    /**
     * Maximum bitrate value, in bits/s.
     */
    public static final int MAX_BITRATE = 25000000;
    
    /**
     * Default timeout value, in milliseconds.
     */
    public static final int DEFAULT_TIMEOUT = 1;
    
    /**
     * Minimum timeout value, in milliseconds.
     */
    public static final int MIN_TIMEOUT = 0;
    
    /**
     * Default frame rate value, in fps.
     */
    public static final int DEFAULT_FRAMERATE = 30;
    
    /**
     * At the present, the maximum frame rate allowed is 30fps.
     */
    public static final int MAX_FRAMERATE = 30;
    
    /**
     * At the present, the minimum frame rate allowed is 2fps.
     */
    public static final int MIN_FRAMERATE = 2;
    
    /**
     * Minimum segment value, in milliseconds.
     */
    public static final int MIN_SEGMENT = 0;
    
    /**
     * Minimum wrap value.
     */
    public static final int MIN_WRAP = 1;
    
    /**
     * Default segment number.
     */
    public static final int DEFAULT_SEGMENT_NUMBER = 1;
    
    /**
     * Minimum segment number.
     */
    public static final int MIN_SEGMENT_NUMBER = 1;
    
    /**
     * Width of resulting video.
     */
    private int width = DEFAULT_WIDTH;
    
    /**
     * Height of resulting video.
     */
    private int height = DEFAULT_HEIGHT;
    
    /**
     * Bitrate, in bits/second (so 10Mbits/s would be 10000000).
     */
    private int bitrate = DEFAULT_BITRATE;
    private String outputFilename = null;
    private boolean verbose = false;
    private int timeout = DEFAULT_TIMEOUT;
    private int framerate = DEFAULT_FRAMERATE;
    private H264Profile h264Profile = null;
    private boolean insertPpsSpsHeaders = false;
    private boolean useTimed = false;
    private int timedOn;
    private int timedOff;
    private boolean keypressMode = false;
    private InitialState initialState = null;
    
    /**
     * Segmentation: segments size in milliseconds.
     * <p>-1 if disabled.</p>
     */
    private int segment = -1;
    
    /**
     * Segmentation: the maximum value for segments numbers.
     * <p>-1 if no maximum.</p>
     */
    private int wrap = -1;
    
    /**
     * Segmentation: when outputting segments, this is the initial segment number,
     * giving the ability to resume previous recording from a given segment.
     * <p>The default value is 1.</p>
     */
    private int startSegmentNumber = DEFAULT_SEGMENT_NUMBER;
    
    private boolean turnOnVideoStabilisation = false;
    
    /**
     * Constructor for a new VideoConfig using only default values for parameters.
     */
    public VideoConfig() {
        super();
    }
    
    /**
     * Constructor for a new VideoConfig using specific value for
     * the duration of the video. Other parameters values are default.
     * @param millis the duration in milliseconds to apply for future videos.
     */
    public VideoConfig(int millis) {
        this();
        setTimeout(millis);
        setInitialState(InitialState.RECORD);
    }
    
    /**
     * Constructor for a new VideConfig using specific values for
     * the duration of the video, the width and the height of the images.
     * @param millis the duration in milliseconds to apply for future videos.
     * @param width the width to use for the new video, in pixels.
     * @param height the height to use for the new video, in pixels.
     */
    public VideoConfig(int millis, int width, int height) {
        this(millis);
        setWidth(width);
        setHeight(height);
    }
    
    /**
     * Set the image width.
     * <p>This should be between 64 and 1920.</p>
     * @param width the new width to apply for future videos.
     */
    public void setWidth(int width) {
        if(width > MAX_WIDTH)
            width = MAX_WIDTH;
        else if(width < MIN_WIDTH)
            width = MIN_WIDTH;
        this.width = width;
    }
    
    /**
     * Set the image height.
     * <p>This should be between 64 and 1080.</p>
     * @param height the new height to apply for future videos.
     */
    public void setHeight(int height) {
        if(height > MAX_HEIGHT)
            height = MAX_HEIGHT;
        else if(height < MIN_HEIGHT)
            height = MIN_HEIGHT;
        this.height = height;
    }
    
    /**
     * Set the bitrate of resulting video.
     * <p>Use bits per second, so 10Mbits/s would be -b 10000000.</p> 
     * <p>For H264, 1080p30 a high quality bitrate would be 15Mbits/s or more. 
     * Maximum bitrate is 25Mbits/s (-b 25000000), but much over 17Mbits/s 
     * will not show noticeable improvement at 1080p30.</p>
     * @param bitrate the new bitrate to apply for future videos.
     */
    public void setBitrate(int bitrate) {
        if(bitrate > MAX_BITRATE)
            bitrate = MAX_BITRATE;
        else if(bitrate < MIN_BITRATE)
            bitrate = MIN_BITRATE;
        this.bitrate = bitrate;
    }
    
    /**
     * Specify the output filename.
     * <p>If not specified (filename = null), default name is given.
     * If the filename is '-', then all output is sent to stdout.</p>
     * <p>Do not enter the extension here !</p>
     * @param filename the output file name, without the extension.
     */
    public void setOutputFilename(String filename) {
        this.outputFilename = filename;
    }
    
    /**
     * Return the output filename, with the ".h264" extension (only if the output filename isn't "-",
     * which corresponds to the use of the standard output (no file created on the system)).
     * If the filename is '-', then '-' is returned.
     * @return  the output filename.
     */
    public String getOutputFilenameWithExtension() {
        String result = getOutputFilenameWithoutExtension();
        if(!"-".equals(result))
            result += ".h264";
        return result;
    }
    
    /**
     * Return the output filename, with the extension.
     * Will return "-" if the standard output is used (no file created on the system).
     * @return the output filename, or "-" if no file created on the system.
     */
    public String getOutputFilenameWithoutExtension() {
        String result;
        if(outputFilename == null)
            result = "video";
        else
            result = outputFilename;
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
     * Sets the duration (in ms) for future videos. 
     * <p>The program will run for this duration, taking the capture if requested (if an output is specified).
     * If not specified, this timeout is set to 1 millisecond.</p>
     * <p>Setting to 0 means the application will run continuously until stopped with Ctrl-C.</p>
     * @param timeout the new timeout to apply for future videos.
     */
    public void setTimeout(int timeout) {
        if(timeout < MIN_TIMEOUT)
            timeout = MIN_TIMEOUT;
        this.timeout = timeout;
    }
    
    /**
     * Returns the frame rate used for the video.
     * @return the frame rate used for the video, in fps.
     */
    public int getFramerate() {
        return framerate;
    }
    
    /**
     * Specify the frames per second to record.
     * <p>At present, the minimum frame rate allowed is 2fps, the maximum is 30fps.
     * This is likely to change in the future.</p>
     * @param framerate the new framerate to apply for future videos, in fps.
     */
    public void setFramerate(int framerate) {
        if(framerate > MAX_FRAMERATE)
            framerate = MAX_FRAMERATE;
        else if(framerate < MIN_FRAMERATE)
            framerate = MIN_FRAMERATE;
        this.framerate = framerate;
    }
    
   /**
    * Specify the h264 profile to use for encoding.
    * @param profile the new profile to apply for future videos. null for default.
    */
    public void setH264Profile(H264Profile profile) {
        this.h264Profile = profile;
    }
    
    /**
     * Forces the stream to include PPS and SPS headers on every I-frame.
     * <p>Needed for certain streaming cases. e.g. Apple HLS. These headers
     * are small, so do not greatly increase file size.</p>
     */
    public void enablePpsSpsHeaders() {
        insertPpsSpsHeaders = true;
    }
    
    /**
     * This option allows the video capture to be paused and restarted 
     * at particular time intervals. Two values are required, the On time 
     * and the Off time.
     * 
     * <p>On time is the amount of time the video is captured,
     * off time is the amount it is paused. The total time of the recording 
     * is defined by the timeout option. Note the recording may take slightly 
     * over the timeout setting depending on the On and Off times.</p>
     * 
     * <p>For example:</p>
     *      <pre>raspivid -o test.h264 -t 25000 -timed 2500,5000</pre>
     * <p>will record for a period of 25 seconds. The recording will be over 
     * a timeframe consisting of 2500ms (2.5s) segments with 5000ms (5s) gaps, 
     * repeating over the 20s. So the entire recording will actually be only 
     * 10s long, since 4 segments of 2.5s = 10s separated by 5s gaps.</p>
     * 
     * <pre>2.5 record – 5 pause - 2.5 record – 5 pause - 2.5 record – 5 pause – 2.5 record</pre>
     * 
     * <p>gives a total recording period of 25s, but only 10s of actual recorded footage.</p>
     * @param on the new on time to apply for future videos, in milliseconds.
     * @param off the new off time to apply for future videos, in milliseconds.
     */
    public void setTimed(int on, int off) {
        useTimed = true;
        timedOn = on;
        timedOff = off;
    }
    
    /**
     * Toggle between record and pause on ENTER key pressed.
     * <p>On each press of the ENTER key the recording will be paused or restarted.</p>
     * <p>Pressing X then ENTER will stop recording and close the application.</p>
     * <p>Note that the timeout value will be used to signal end of recording,
     * but is only checked after each ENTER keypress, so if the system is waiting
     * for a keypress, even if the timeout has expired, it will still wait for 
     * the keypress before exiting.</p>
     * @param enable true to enable the keypress mode. False to disable.
     */
    public void enableKeypressMode(boolean enable) {
        this.keypressMode = enable;
    }
    
    /**
     * Define initial state on startup.
     * <p>Define whether the camera will start paused or will immediately start recording.
     * Options are 'record' or 'pause'.</p>
     * <p>Note that if you are using a simple timeout,
     * and initial is set to 'pause', no output will be recorded.</p>
     * @param initialState the new initial state to apply for future videos. null to use default.
     */
    public void setInitialState(InitialState initialState) {
        this.initialState = initialState;
    }
    
    /**
     * Segment the stream in to multiple files.
     * <p>Rather than creating a single file, the file is split up in to segments of 
     * approximately the numer of milliseconds specified. In order to provide different 
     * filenames, you should add %04d or similar at the point in the filename where 
     * you want a segment count number to appear. e.g:</p>
     *      <pre>--segment 3000 -o video%04d.h264</pre>
     * <p>will produce video clips of approximately 3000ms (3s) long, named video0001.h264,
     * video0002.h264 etc. The clips should be seamless (no frame drops between clips),
     * but the accuracy of each clip length will depend on the intraframe period, 
     * as the segments will always start on an I-frame. They will therefore always be 
     * equal or longer to the specified period.</p>
     * @param millis the new segments duration to apply for future videos. -1 to disable.
     */
    public void segment(int millis) {
        if(millis != 1 && millis < MIN_SEGMENT)
            millis = MIN_SEGMENT;
        this.segment = millis;
    }
    
    /**
     * When outputting segments, this is the maximum the segment number can reach before 
     * it is reset to 1, giving the ability to keep recording segments, but overwriting 
     * the oldest one.
     * <p>So if set to four, in the segment example above, the files produced 
     * will be video0001.h264, video0002.h264, video0003.h264, video0004.h264.
     * Once video0004.h264 is recorded, the count will reset to 1, and the video0001.h264 
     * will be overwritten.</p>
     * @param wrap the new maximum value to apply for future segments numbers. -1 if no maximum segment number.
     */
    public void wrap(int wrap) {
        if(wrap != -1 && wrap < MIN_WRAP)
            wrap = MIN_WRAP;
        this.wrap = wrap;
    }
    
    /**
     * When outputting segments, this is the initial segment number, giving the ability
     * to resume previous recording from a given segment. The default value is 1.
     * @param startSequenceNumber the new start sequence number to apply for future videos.
     */
    public void setStartSequenceNumber(int startSequenceNumber) {
        if(startSequenceNumber < MIN_SEGMENT_NUMBER)
            startSequenceNumber = MIN_SEGMENT_NUMBER;
        this.startSegmentNumber = startSequenceNumber;
    }
    
    /**
     * Turn on/off video stabilisation.
     * @param active true to turn on. False to turn off.
     */
    public void turnOnVideoStabilisation(boolean active) {
        turnOnVideoStabilisation = active;
    }
    
    /**
     * Get the complee command to execute to take a new video with the requested parameters.
     * @return the complete command to execute to take the video with the specified values for the parameters.
     */
    public String getCommand() {
        String command = BASE_COMMAND;
        
        command += " -w " + width;
        command += " -h " + height;
        command += " -b " + bitrate;
        
        command += " -o " + getOutputFilenameWithExtension();
        if(verbose)
            command += " -v";
        command += " -t " + timeout;
        command += " -fps " + framerate;
        if(h264Profile != null)
            command += " -pf " + h264Profile.getValue();
        if(insertPpsSpsHeaders)
            command += " -ih";
        if(useTimed)
            command += " -timed " + timedOn + "," + timedOff;
        if(keypressMode)
            command += " -k";
        if(initialState != null)
            command += " -i " + initialState.getValue();
        if(segment != -1) {
            command += " -sg " + segment;
            command += " -sn " + startSegmentNumber;
        }
        if(wrap != -1)
            command += " wr " + wrap;
        if(turnOnVideoStabilisation)
            command += " -vs";
        
        command += this.getOptions();
        
        return command;
    }
}
