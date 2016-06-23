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
package com.raspoid.examples.robots.poc;

/**
 * <b>Abstraction used to easily enable/disable each of the components
 * composing the proff of concept robot ({@link RobotPOC})</b>
 * 
 * @see RobotPOC
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class POCConfig {
    
    private boolean pcf8591Nb1Enabled = true;
    private boolean lcdDisplayEnabled = true;
    private boolean cameraSupportEnabled = true;
    private boolean cameraStreamEnabled = true;
    private boolean irReceiverEnabled = true;
    private boolean thermistorEnabled = true;
    private boolean barometerEnabled = true;
    private boolean passiveBuzzerEnabled = true;
    private boolean nxtEnabled = true;
    private boolean photoresistorEnabled = true;
    private boolean soundSensorEnabled = true;
    
    /**
     * Default constructor to enable each component of the POC Robot.
     */
    public POCConfig() {
        // each component is enabled.
    }
    
    /* -----------------------------------------------
     *                    PCF8591Nb1
     * ---------------------------------------------*/
    
    /**
     * Enable de the first PCF8591.
     */
    public void enablePCF8591Nb1() {
        this.pcf8591Nb1Enabled = true;
    }
    
    /**
     * Disable the first PCF8591.
     */
    public void disablePCF8591Nb1() {
        this.pcf8591Nb1Enabled = false;
    }
    
    /**
     * Checks if the first PCF8591 is enabled.
     * @return true if the first PCF8591 is enabled. False otherwise.
     */
    public boolean pcf8591Nb1Enabled() {
        return this.pcf8591Nb1Enabled;
    }
        
    /* -----------------------------------------------
     *                  LCD display
     * ---------------------------------------------*/
    
    /**
     * Enable the LCD display.
     */
    public void enableLcdDisplay() {
        this.lcdDisplayEnabled = true;
    }
    
    /**
     * Disable the LCD display.
     */
    public void disableLcdDisplay() {
        this.lcdDisplayEnabled = false;
    }
    
    /**
     * Checks if the LCD display is enabled.
     * @return true if the LCD display is enabled. False otherwise.
     */
    public boolean lcdDisplayEnabled() {
        return this.lcdDisplayEnabled;
    }
    
    /* -----------------------------------------------
     *                  Camera support
     * ---------------------------------------------*/
    
    /**
     * Enable the camera support.
     */
    public void enableCameraSupport() {
        this.cameraSupportEnabled = true;
    }
    
    /**
     * Disable the camera support.
     */
    public void disableCameraSupport() {
        this.cameraSupportEnabled = false;
    }
    
    /**
     * Checks if the camera support is enabled.
     * @return true if the camera support is enabled. False otherwise.
     */
    public boolean cameraSupportEnabled() {
        return this.cameraSupportEnabled;
    }
    
    /* -----------------------------------------------
     *                  Camera stream
     * ---------------------------------------------*/
    
    /**
     * Enable the camera stream.
     */
    public void enableCameraStream() {
        this.cameraStreamEnabled = true;
    }
    
    /**
     * Disable the camera stream.
     */
    public void disableCameraStream() {
        this.cameraStreamEnabled = false;
    }
    
    /**
     * Checks if the camera stream is enabled.
     * @return true if the camera stream is enabled. False otherwise.
     */
    public boolean cameraStreamEnabled() {
        return this.cameraStreamEnabled;
    }

    
    /* -----------------------------------------------
     *                   IR Receiver
     * ----------------------------------------------*/
    
    /**
     * Enable the infrared receiver.
     */
    public void enableIRReceiver() {
        this.irReceiverEnabled = true;
    }
    
    /**
     * Disable the infrared receiver.
     */
    public void disableIRReceiver() {
        this.irReceiverEnabled = false;
    }
    
    /**
     * Checks if the infrared receiver is enabled.
     * @return true if the infrared receiver is enabled.
     */
    public boolean irReceiverEnabled() {
        return irReceiverEnabled;
    }
    
    /* -----------------------------------------------
     *                  Thermistor
     * ---------------------------------------------*/
    
    /**
     * Enable the thermistor.
     */
    public void enableThermistor() {
        this.thermistorEnabled = true;
    }
    
    /**
     * Disable the thermistor.
     */
    public void disableThermistor() {
        this.thermistorEnabled = false;
    }
    
    /**
     * Checks if the thermistor is enabled.
     * @return true if the thermistor is enabled. False otherwise.
     */
    public boolean thermistorEnabled() {
        return thermistorEnabled;
    }
    
    /* -----------------------------------------------
     *                  Barometer
     * ---------------------------------------------*/
    
    /**
     * Enable the barometer.
     */
    public void enableBarometer() {
        this.barometerEnabled = true;
    }
    
    /**
     * Disable the barometer.
     */
    public void disableBarometer() {
        this.barometerEnabled = false;
    }
    
    /**
     * Checks if the barometer is enabled.
     * @return true if the barometer is enabled. False otherwise.
     */
    public boolean baromaterEnabled() {
        return barometerEnabled;
    }
    
    /* -----------------------------------------------
     *                 Passive buzzer
     * ---------------------------------------------*/
    
    /**
     * Enable the passive buzzer.
     */
    public void enablePassiveBuzzer() {
        this.passiveBuzzerEnabled = true;
    }
    
    /**
     * Disable the passive buzzer.
     */
    public void disablePassiveBuzzer() {
        this.passiveBuzzerEnabled = false;
    }
    
    /**
     * Checks if the passive buzzer is enabled.
     * @return true if the passive buzzer is enabled. False otherwise.
     */
    public boolean passiveBuzzerEnabled() {
        return passiveBuzzerEnabled;
    }
    
    /* -----------------------------------------------
     *                       NXT
     * ---------------------------------------------*/
    
    /**
     * Enable NXT components.
     */
    public void enableNXT() {
        this.nxtEnabled = true;
    }
    
    /**
     * Disable the NXT components.
     */
    public void disableNXT() {
        this.nxtEnabled = false;
    }
    
    /**
     * Checks if the NXT components are enabled.
     * @return true if the NXT components are enabled. False otherwise.
     */
    public boolean NXTEnabled() {
        return nxtEnabled;
    }
    
    /* -----------------------------------------------
     *                 Photoresistor
     * ---------------------------------------------*/
    
    /**
     * Enable the photoresistor.
     */
    public void enablePhotoresistor() {
        this.photoresistorEnabled = true;
    }
    
    /**
     * Disable the photoresistor.
     */
    public void disablePhotoresistor() {
        this.photoresistorEnabled = false;
    }
    
    /**
     * Checks if the photoresistor is enabled.
     * @return true if the photoresistor is enabled. False otherwise.
     */
    public boolean photoresistorEnabled() {
        return photoresistorEnabled;
    }
    
    /* -----------------------------------------------
     *                 Sound Sensor
     * ---------------------------------------------*/
    
    /**
     * Enable the sound sensor.
     */
    public void enableSoundSensor() {
        this.soundSensorEnabled = true;
    }
    
    /**
     * Disable the sound sensor.
     */
    public void disableSoundSensor() {
        this.soundSensorEnabled = false;
    }
    
    /**
     * Checks if the sound sensor is enabled.
     * @return true if the sound sensor is enabled. False otherwise.
     */
    public boolean soundSensorEnabled() {
        return soundSensorEnabled;
    }

}
