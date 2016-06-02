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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.raspoid.Tools;
import com.raspoid.examples.additionalcomponents.camera.CameraPiExample;
import com.raspoid.exceptions.RaspoidException;

/**
 * <b>This class is a wrapper for raspistill and raspivid, the command line tools
 * for capturing still photographs and videos with the camera module from Raspberry Pi.</b>
 * 
 * <p>Through this wrapper, you can easily
 *  <ul>
 *      <li>launch a preview window on your HDMI or PCB display</li>
 *      <li>take pictures</li>
 *      <li>take videos</li>
 *  </ul>
 * ... all of this with a panel of usefull parameters.</p>
 * 
 * <p>We provide you with a set of predefined constructors to easily use all the available options.</p>
 * 
 * <h3>Setting up camera module</h3>
 * <p>Before using this, you need to enable camera support on your Raspberry Pi.
 * This can be done by executing the following instructions on the command line.</p>
 * <p>First, download and install the latest kernel, GPU firmware and applications.
 * You will need an internet connection for this to work correctly.</p>
 *      <pre>sudo apt-get update sudo apt-get upgrade</pre>
 * <p>Now you need to enable camera support using the raspi-config.<p>
 *      <pre>sudo raspi-config</pre>
 * <p>Use the cursor keys to move to the camera option and select "enable".</p>
 * <p>On exiting raspi-config it will ask to reboot. The enable option will ensure that 
 * on reboot the correct GPU firmware will be running (with the camera driver and tuning),
 * and the GPU memory split is sufficient to allow the camera to acquire enough memory
 * to run correctly.</p>
 * 
 * <h3>Notes</h3>
 * <ul>
 *  <li>The camera adds about 200-250mA to the power requirements of your Raspberry Pi.
 * </uL>
 * 
 * <h3>Infos</h3>
 * <ul>
 *  <li>https://www.raspberrypi.org/documentation/raspbian/applications/camera.md</li>
 *  <li>https://github.com/raspberrypi/userland/tree/master/host_applications/linux/apps/raspicam</li>
 * </ul>
 * 
 * <p>Example of use: {@link CameraPiExample}</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class CameraPi {
    
    /* ----------------------------------------------------------
     *                          UTILS
     * ----------------------------------------------------------*/
    
    /**
     * Application ran successfully.
     */
    private static final int EX_OK = 0;
    
    /**
     * Bad command line parameter.
     */
    private static final int EX_USAGE = 64;
    
    /**
     * Software or camera error.
     */
    private static final int EX_SOFTWARE = 70;
    
    /**
     * Application terminated by ctrl-C.
     */
    private static final int APPLICATION_TERMINATED_BY_CTRL_C = 130;
    
    /**
     * Private constructor to hide the implicit public one.
     */
    private CameraPi() {
    }
    
    /**
     * Executes the specified string command in a separate process.
     * @param command the string command to execute.
     * @return an exit value. EX_OK if command ran successfully,
     * EX_USAGE if bad command line parameter,
     * EX_SOFTWARE if software or camera error,
     * APPLICATION_TERMINATED_BY_CTRL_C if application terminated by ctrl-C
     * or other for other type of error.
     */
    public static int executeCommand(String command) {
        try {
            Tools.debug("[CameraPi] executed command: " + command, Tools.Color.ANSI_BLUE);
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            int exitValue = process.exitValue();
            switch(exitValue) {
            case EX_OK:
                Tools.debug("[CameraPi] Command ran successfully", Tools.Color.ANSI_BLUE);
                break;
            case EX_USAGE:
                Tools.debug("[CameraPi] Bad command line parameter", Tools.Color.ANSI_BLUE);
                break;
            case EX_SOFTWARE:
                Tools.debug("[CameraPi] Software or camera error", Tools.Color.ANSI_BLUE);
                break;
            case APPLICATION_TERMINATED_BY_CTRL_C:
                Tools.debug("[CameraPi] Application terminated by ctrl-C", Tools.Color.ANSI_BLUE);
                break;
            default:
                Tools.debug("[CameraPi] Error when executing command. Exit value=" + exitValue, Tools.Color.ANSI_BLUE);
                break;
            }
            return exitValue;
        } catch (IOException | InterruptedException e) {
            throw new RaspoidException("Error when executing command: " + command, e);
        }
    }
    
    /* ----------------------------------------------------------
     *                      PREVIEW WINDOW
     * ----------------------------------------------------------*/
    
    /**
     * Enable a fullscreen preview for a duration of millis milliseconds.
     * <p>The preview can only be done with an HDMI display (directly connected to the Pi),
     * or a display connected through the PCB display port (we couldn't test this).
     * A vnc connection is not sufficient.</p>
     * @param millis the duration of the preview, in milliseconds.
     */
    public static void preview(int millis) {
        PreviewConfig previewConfig = new PreviewConfig();
        previewConfig.enablePreviewFullscreen();
        preview(previewConfig, millis);
    }
    
    /**
     * Enable a preview with specific config, for a default duration of 5000 milliseconds.
     * @param previewConfig the preview configuration to apply on the image.
     */
    public static void preview(PreviewConfig previewConfig) {
        preview(previewConfig, PictureConfig.DEFAULT_TIMEOUT);
    }
    
    /**
     * Enable a preview with specific preview configuration,
     * and for a duration of millis milliseconds.
     * @param previewConfig the preview configuration to apply on the image.
     * @param millis the duration of the preview, in milliseconds.
     */
    public static void preview(PreviewConfig previewConfig, int millis) {
        if(millis < PictureConfig.MIN_TIMEOUT)
            millis = PictureConfig.MIN_TIMEOUT;
        String command = PictureConfig.BASE_COMMAND;
        command += " -t " + millis;
        command += previewConfig.getPreviewSettings();
        executeCommand(command);
    }
    
    /* ----------------------------------------------------------
     *                          PICTURES
     * ----------------------------------------------------------*/
    
    /**
     * Takes a picture as soon as possible, with default parameter values.
     * <p>No preview.</p>
     * @return a new Picture object, corresponding to the newly taken picture.
     */
    public static Picture takePicture() {
        return takePicture(new PictureConfig());
    }
    
    /**
     * Takes a new picture with specified width, height and quality.
     * Other parameters are defaults.
     * <p>No preview.</p>
     * @param width the width of the new picture, in the [{@link PictureConfig#MIN_WIDTH}, {@link PictureConfig#MAX_WIDTH}] range.
     * @param height the height of the new picture, in the [{@link PictureConfig#MIN_HEIGHT}, {@link PictureConfig#MAX_HEIGHT}] range.
     * @param quality the quality of the new picture, in the [{@link PictureConfig#MIN_QUALITY}, {@link PictureConfig#MAX_QUALITY}] range.
     * @return a new Picture object, corresponding to the newly taken picture.
     */
    public static Picture takePicture(int width, int height, int quality) {
        return takePicture(new PictureConfig(width, height, quality));
    }
    
    /**
     * Takes a new picture with a custom PictureConfig.
     * <p>No preview.</p>
     * @param config the PictureConfig to apply to take the picture.
     * @return a new Picture object, corresponding to the newly taken picture.
     */
    public static Picture takePicture(PictureConfig config) {
        PreviewConfig previewConfig = new PreviewConfig();
        previewConfig.disablePreview();
        return takePicture(config, previewConfig);
    }
    
    /**
     * Takes a new picture with a custom PictureConfig,
     * and a PreviewConfig to enable a preview in the same time.
     * @param pictureConfig the PictureConfig to apply to take the picture.
     * @param previewConfig the PreviewConfig to apply to take the picture.
     * @return a new Picture object, corresponding to the newly taken picture.
     */
    public static Picture takePicture(PictureConfig pictureConfig, PreviewConfig previewConfig) {
        String command = pictureConfig.getCommand();
        command += previewConfig.getPreviewSettings();
        executeCommand(command);
        String filePath = System.getProperty("user.dir") + "/" + pictureConfig.getOutputFilenameWithExtension();
        return new Picture(pictureConfig, filePath);
    }
    
    /* ----------------------------------------------------------
     *                          VIDEOS
     * ----------------------------------------------------------*/
    
    /**
     * Takes a new video of millis milliseconds.
     * <p>No preview.</p>
     * @param millis the duration of the video, in milliseconds.
     * @return a new Video object, corresponding to the newly taken video.
     */
    public static Video takeVideo(int millis) {
        return takeVideo(new VideoConfig(millis));
    }
    
    /**
     * Takes a new video for duration of millis milliseconds, with specified width and height.
     * Other parameters are defaults.
     * <p>No preview.</p>
     * @param millis the duration of the video, in milliseconds.
     * @param width the width of the new video.
     * @param height rge height of the new video.
     * @return a new Video object, corresponding to the newly taken video.
     */
    public static Video takeVideo(int millis, int width, int height) {
        return takeVideo(new VideoConfig(millis, width, height));
    }
    
    /**
     * Takes a new video with the specified VideoConfig.
     * <p>No preview.</p>
     * @param config the VideoConfig to apply to take the video.
     * @return a new Video object, corresponding to the newly taken video.
     */
    public static Video takeVideo(VideoConfig config) {
        PreviewConfig previewConfig = new PreviewConfig();
        previewConfig.disablePreview();
        return takeVideo(config, previewConfig);
    }
    
    /**
     * Takes a new video with the specific VideoConfig and a PreviewConfig to enable a preview in the same time.
     * @param videoConfig the VideoConfig to apply to take the video.
     * @param previewConfig the PreviewConfig to apply to take the video.
     * @return a new Video object, corresponding to the newly taken video.
     */
    public static Video takeVideo(VideoConfig videoConfig, PreviewConfig previewConfig) {
        String command = videoConfig.getCommand();
        command += previewConfig.getPreviewSettings();
        executeCommand(command);
        String filePath = System.getProperty("user.dir") + "/" + videoConfig.getOutputFilenameWithExtension();
        return new Video(videoConfig, filePath);
    }
    
    /* ----------------------------------------------------------
     *                        STREAMING
     * ----------------------------------------------------------*/
    
    /**
     * Creates a new video stream from the Camera Pi, on a specific IP address:port,
     * using the GStreamer tool (v0.10).
     * 
     * <p>There is no need to stop this GStreamer server. It will shut down when the java process will end.</p>
     * 
     * @param ipAddress the IP address linked to the video stream.
     * @param port the port linked to the video stream.
     * @param width the width of the video stream. Higher is better, but requires larger network load.
     * @param height the height of the video stream. Higher is better, but requires larger network load.
     * @param vflip true to flip the image vertically. False otherwise.
     * @param hflip true to flip the image horizontally. False otherwise.
     * @param bitrate the bitrate used for the video stream.
     * @param preview true to enable a fullscreen preview.
     * @param verbose true to print gstreamer through Tools.log system.
     */
    public static void startGStreamerServer(String ipAddress, int port, int width, int height, 
            boolean vflip, boolean hflip, int bitrate, boolean preview, boolean verbose) {
        VideoConfig videoConfig = new VideoConfig(0, width, height);
        videoConfig.setOutputFilename("-");
        videoConfig.setFramerate(25);
        videoConfig.setVerticalFlip(vflip);
        videoConfig.setHorizontalFlip(hflip);
        videoConfig.setBitrate(bitrate);
        String command = videoConfig.getCommand();
        if(preview)
            command += " -f";
        
        command += " | gst-launch -v fdsrc ! h264parse ! rtph264pay config-interval=1 pt=96 ! gdppay ! tcpserversink host=" + ipAddress + " port=" + port;
        
        // execute command. Because of piped commands, we need to write a script
        // and execute the script instead of separate commands.
        // Pipe is part of the shell, so we can use the following to do the trick.
        try {
            String[] cmd = {"/bin/sh", "-c", command};
            Process process = Runtime.getRuntime().exec(cmd);
            Tools.log("GStreamer server launching.", Tools.Color.ANSI_GREEN);
            
            if(verbose) {
                new Thread(()->readInputStreamLines(process)).start();
            }
            
            Tools.sleepMilliseconds(2000);
            Tools.log("GStreamer server launched. You can read it on client side with the following:"
                    + "\n\tgst-launch-1.0 -v tcpclientsrc host=" + ipAddress + " port=" + port + 
                    "  ! gdpdepay ! rtph264depay ! avdec_h264 ! videoconvert ! autovideosink sync=false", Tools.Color.ANSI_GREEN);

            process.waitFor();
            int exitValue = process.exitValue();
            Tools.debug("GStreamer server stopped. (Exit value=" + exitValue + ")");
        } catch(IOException | InterruptedException e) {
            throw new RaspoidException("Error when executing command: " + command, e);
        }
    }
    
    /**
     * Method created to respect Java metrics (Sonar lint issue asking to create a new method for this).
     * @param process the process used to read input stream from.
     */
    private static void readInputStreamLines(Process process) {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String line; 
            while ((line = input.readLine()) != null)
               Tools.log(line);
        } catch (IOException e) {
            Tools.log("Error when printing verbose lines from gstreamer. (Exception=" + e + ")");
        }
    }
}
