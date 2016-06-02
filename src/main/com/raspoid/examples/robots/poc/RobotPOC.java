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

import java.util.List;
import java.util.Locale;

import com.raspoid.GPIOPin;
import com.raspoid.PWMPin;
import com.raspoid.Tools;
import com.raspoid.additionalcomponents.BarometerBMP180;
import com.raspoid.additionalcomponents.LCM1602;
import com.raspoid.additionalcomponents.LED;
import com.raspoid.additionalcomponents.PCA9685;
import com.raspoid.additionalcomponents.PCA9685.PCA9685Channel;
import com.raspoid.additionalcomponents.adc.PCF8591;
import com.raspoid.additionalcomponents.adc.PCF8591InputChannel;
import com.raspoid.additionalcomponents.ir.IRProtocolSunfounderMediaRemote;
import com.raspoid.additionalcomponents.ir.IRReceiverOS1838B;
import com.raspoid.additionalcomponents.ir.IRSignal;
import com.raspoid.additionalcomponents.notes.BaseNote;
import com.raspoid.additionalcomponents.servomotor.TowerProMG90S;
import com.raspoid.additionalcomponents.PassiveBuzzer;
import com.raspoid.additionalcomponents.Thermistor;
import com.raspoid.additionalcomponents.ThermistorNTCLE203E3103SB0;
import com.raspoid.network.MessageLikeSocketServer;
import com.raspoid.network.NetworkUtilities;
import com.raspoid.network.Router;
import com.raspoid.network.SocketServer;

/**
 * <b>This aim of this robot is to illustrate a maximum of the features offered by the Raspoid framework.</b>
 * 
 * <p>This robot is composed of:
 *  <ul>
 *      <li>LEGO Mindstorms NXT,</li>
 *      <li>XX additional sensors/actuators,</li>
 *      <li>a socket server to send HTTP request from your browser,</li>
 *      <li>a pushbullet integration to send requests from your smartphone,</li>
 *      <li>a message-like socket server to use the joystick remote part of the robot</li>
 *      <li>...</li>
 *  </ul>
 * </p>
 * 
 * <p>Don't hesitate to check our online post regarding this robot.
 * We made some videos and pictures to illustrate the result: <a href="http://raspoid.com">Raspoid.com</a></p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class RobotPOC {
    
    /* ===============================================
     *                     CONSTANTS
     * =============================================*/

    /**
     * Camera support + joystick.
     */
    private static final double CAMERA_SUPPORT_MIN_VERTICAL_ANGLE = 0.; // °
    private static final double CAMERA_SUPPORT_MAX_VERTICAL_ANGLE = 95.; // °
    private static final double CAMERA_SUPPORT_VERTICAL_STRAIGHT_AHEAD_ANGLE = 58.; // °
    private static final int CAMERA_SUPPORT_JOYSTICK_VERTICAL_NEUTRAL_VALUE = 129;
    private static final int CAMERA_SUPPORT_JOYSTICK_VERTICAL_MIN_VALUE_RANGE = 7;
    private static final double CAMERA_SUPPORT_VERTICAL_JOYSTICK_UNIT_TO_DEGREE = CAMERA_SUPPORT_VERTICAL_STRAIGHT_AHEAD_ANGLE / (CAMERA_SUPPORT_JOYSTICK_VERTICAL_NEUTRAL_VALUE - CAMERA_SUPPORT_JOYSTICK_VERTICAL_MIN_VALUE_RANGE);
    private static final int CAMERA_SUPPORT_JOYSTICK_VERTICAL_MAX_VALUE_RANGE = (int)(CAMERA_SUPPORT_MAX_VERTICAL_ANGLE / CAMERA_SUPPORT_VERTICAL_JOYSTICK_UNIT_TO_DEGREE);
    
    // Inverted mode
    private static final double CAMERA_SUPPORT_MIN_HORIZONTAL_ANGLE = 0.; // °
    private static final double CAMERA_SUPPORT_MAX_HORIZONTAL_ANGLE = 180.; // °
    private static final double CAMERA_SUPPORT_HORIZONTAL_STRAIGHT_AHEAD_ANGLE = 90.; // °
    private static final int CAMERA_SUPPORT_JOYSTICK_HORIZONTAL_NEUTRAL_VALUE = 129;
    private static final int CAMERA_SUPPORT_JOYSTICK_HORIZONTAL_MIN_VALUE_RANGE = 7;
    private static final double CAMERA_SUPPORT_HORIZONTAL_JOYSTICK_UNIT_TO_DEGREE = CAMERA_SUPPORT_HORIZONTAL_STRAIGHT_AHEAD_ANGLE / (CAMERA_SUPPORT_JOYSTICK_HORIZONTAL_NEUTRAL_VALUE - CAMERA_SUPPORT_JOYSTICK_HORIZONTAL_MIN_VALUE_RANGE);
    private static final int CAMERA_SUPPORT_JOYSTICK_HORIZONTAL_MAX_VALUE_RANGE = (int)(CAMERA_SUPPORT_MAX_HORIZONTAL_ANGLE / CAMERA_SUPPORT_HORIZONTAL_JOYSTICK_UNIT_TO_DEGREE);


    
    /* ===============================================
     *                  COMPONENTS
     * =============================================*/
    
    /**
     * [PCF8591Nb1] analog to digital converter 1
     */
    private PCF8591 pcf8591Nb1 = null;
    
    /**
     * [LCD1602] lcd display
     */
    private LCM1602 lcdDisplay = null;
    
    /**
     * [Camera support] servo 1 - horizontal rotations
     */
    private TowerProMG90S servo1 = null;
    
    /**
     * [Camera support] servo 2 - vertical rotations
     */
    private TowerProMG90S servo2 = null;
    
    /**
     * [IRReceiver_1838B] ir receiver.
     */
    private IRReceiverOS1838B irReceiver = null;
    
    /**
     * [Thermistor] thermistor.
     */
    private Thermistor thermistor = null;
    
    /**
     * [BMP180] barometer.
     */
    private BarometerBMP180 barometer = null;
    
    /**
     * [PassiveBuzzer] buzzer.
     */
    private PassiveBuzzer buzzer = null;
    
    /* ===============================================
     *                  VARIABLES
     * =============================================*/
    private List<String> ipAddresses;
    
    private double thermistorTemperature;
    
    private DisplayScreen currentDisplayScreen;
    
    /**
     * Enable the refresh of the screen.
     */
    private boolean refreshScreen = true;
    
    /**
     * Sleep delay between successive screen refreshes.
     */
    private int sleepBetweenScreenRefreshes = 0;
    
    private double barometerTemperature;
    private double barometerPressure;
    private double barometerAltitude;
    
    /**
     * true if running. false if in pause mode (or if stopped).
     * If stopped != true, can be restarted.
     * @see #stopped
     * @see #start()
     */
    private boolean running = false;
    
    /**
     * true when the program is stopped.
     * The robot instance can't be restarted.
     * @see #running
     * @see #stop() 
     */
    private boolean stopped = false;
    
    /* ===============================================
     *                  CONSTRUCTOR
     * =============================================*/
    
    /**
     * Constructor for our proof of concept robot, with a specific config.
     * @param config the config to apply for this instance of the robot.
     */
    public RobotPOC(POCConfig config) {
        
        if(config.pcf8591Nb1Enabled()) {
            pcf8591Nb1 = new PCF8591();
            Tools.debug("PCF8591_1 enabled.", Tools.Color.ANSI_RED);
        }
        
        if(config.lcdDisplayEnabled()) {
            lcdDisplay = new LCM1602();
            lcdDisplay.setDisplay(true, false, false);
            switchToDisplayScreen(DisplayScreen.IP_ADDRESS);
            Tools.debug("LCD display enabled.", Tools.Color.ANSI_RED);
            
            // Thread used to update the content of the display, when needed
            new Thread(() -> {
                while(!stopped) {
                    if(running && refreshScreen) {
                        updateDisplayScreen();
                        Tools.sleepMilliseconds(sleepBetweenScreenRefreshes);
                    } else {
                        Tools.sleepMilliseconds(100);
                    }
                }
            }).start();
        }
        
        if(config.cameraSupportEnabled()) {
            PCA9685 pca9685 = new PCA9685();
//            servo1 = new TowerProMG90S(PWMPin.PWM0); // TODO
            servo1 = new TowerProMG90S(pca9685, PCA9685Channel.CHANNEL_01);
//            servo2 = new TowerProMG90S(PWMPin.PWM1);
            servo2 = new TowerProMG90S(pca9685, PCA9685Channel.CHANNEL_02);
            rotateCameraSupportHorizontally(90);
            rotateCameraSupportVertically(58);
            Tools.debug("Camera support enabled", Tools.Color.ANSI_RED);
        }
        
        if(config.irReceiverEnabled()) {
            irReceiver = new IRReceiverOS1838B(GPIOPin.GPIO_00);
            
            new Thread(() -> {
                while(!stopped) {
                    if(running) {
                        IRSignal newSignal = irReceiver.detectSignal();
                        
                        IRSignal signalDecoded = irReceiver.decodeIRSignal(new IRProtocolSunfounderMediaRemote(), newSignal);
                        if(signalDecoded != null) {
                            Tools.debug("New signal received and decoded: " + signalDecoded.getName(), Tools.Color.ANSI_BLUE);
                            if(signalDecoded.equals(IRProtocolSunfounderMediaRemote.button0))
                                switchToDisplayScreen(DisplayScreen.IP_ADDRESS);
                            else if(signalDecoded.equals(IRProtocolSunfounderMediaRemote.button1))
                                switchToDisplayScreen(DisplayScreen.TEMPERATURE);
                            else if(signalDecoded.equals(IRProtocolSunfounderMediaRemote.button2))
                                switchToDisplayScreen(DisplayScreen.BAROMETER_TEMPERATURE);
                            else if(signalDecoded.equals(IRProtocolSunfounderMediaRemote.button3))
                                switchToDisplayScreen(DisplayScreen.BAROMETER_PRESSURE);
                            else if(signalDecoded.equals(IRProtocolSunfounderMediaRemote.button4))
                                switchToDisplayScreen(DisplayScreen.BAROMETER_ALTITUDE);
                            else
                                switchToDisplayScreen(DisplayScreen.UNKNOWN);
                        } else {
                            Tools.debug("New signal received but not decoded: " + newSignal, Tools.Color.ANSI_BLUE);
                        }
                    } else {
                        Tools.sleepMilliseconds(500);
                    }
                }
            }).start();
            
            Tools.debug("IR receiver enabled.", Tools.Color.ANSI_RED);
        }
        
        if(config.thermistorEnabled()) {
            thermistor = new ThermistorNTCLE203E3103SB0(pcf8591Nb1, PCF8591InputChannel.CHANNEL_0);
            Tools.debug("Thermistor enabled.", Tools.Color.ANSI_RED);
            
            new Thread(() -> {
                    while(!stopped) {
                        if(running) {
                            thermistorTemperature = thermistor.getTemperature();
                            Tools.debug("Temperature: " + thermistorTemperature);
                        }
                        Tools.sleepMilliseconds(200);
                    }
                }).start();
        }
        
        if(config.baromaterEnabled()) {
            barometer = new BarometerBMP180();
            Tools.debug("Barometer enabled.", Tools.Color.ANSI_RED);
            
            new Thread(() -> {
                    int sampling = 5;
                    while(!stopped) {
                        if(running && (currentDisplayScreen == DisplayScreen.BAROMETER_TEMPERATURE
                                || currentDisplayScreen == DisplayScreen.BAROMETER_PRESSURE
                                || currentDisplayScreen == DisplayScreen.BAROMETER_ALTITUDE)) {
                            double[] temperatures = new double[sampling];
                            int[] pressures = new int[sampling];
                            double[] altitudes = new double[sampling];
                            
                            for(int i = 0; i < sampling; i++) {
                                temperatures[i] = barometer.calculateTrueTemperature();
                                pressures[i] = barometer.calculateTruePressure();
                                altitudes[i] = barometer.calculateAbsoluteAltitude();
                                Tools.sleepMilliseconds(100);
                            }
                            
                            double temperature = 0;
                            double pressure = 0;
                            double altitude = 0;
                            for(int i = 0; i < sampling; i++) {
                                temperature += temperatures[i];
                                pressure += pressures[i];
                                altitude += altitudes[i];
                            }
                            
                            barometerTemperature = temperature / sampling;
                            barometerPressure = pressure / sampling;
                            barometerAltitude = altitude / sampling;
                            
                            Tools.debug("Barometer data: (temperature)" + barometerTemperature + 
                                    " (pressure)" + barometerPressure + " (altitude)" + barometerAltitude);
                        } else {
                            Tools.sleepMilliseconds(100);
                        }
                    }
                }).start();
        }
        
        if(config.passiveBuzzerEnabled()) {
            buzzer = new PassiveBuzzer(PWMPin.PWM1);
            int wholeNote = 1000000;
            int halfNote = wholeNote / 2;
            int quarterNote = wholeNote / 4;
            int eightNote = wholeNote / 8;
            
            buzzer.playNote(BaseNote.DO_0, 4, wholeNote);
            buzzer.playNote(BaseNote.RE_0, 4, halfNote);
            buzzer.playNote(BaseNote.MI_0, 4, quarterNote);
            buzzer.playNote(BaseNote.FA_0, 4, eightNote);
        }
        
        // Joystick WebSocket server
        Router joystickRouter = new Router();
        joystickRouter.addRouteWithParams("joystick_camera", 2, inputArgs -> {
            this.newJoystickPosition(Integer.valueOf(inputArgs[0]), Integer.valueOf(inputArgs[1]));
            return "New joystick position received.";
        });
        joystickRouter.addRouteWithParams("joystick_robot_motors", 2, inputArgs -> {
            // TODO move robot accordingly
//            this.newJoystickPosition(Integer.valueOf(inputArgs[0]), Integer.valueOf(inputArgs[1]));
            return "";
        });
        // joystick_router will work better on a message like server, to be sure no requests are not correctly received.
        new MessageLikeSocketServer(5, 81, joystickRouter).start();
        new SocketServer(5, 80, joystickRouter).start();
        
        // Green Led light up
        LED greenLed = new LED(GPIOPin.GPIO_26);
        new Thread(() -> {
            while(!stopped) {
                if(running)
                    greenLed.toggle();
                Tools.sleepMilliseconds(500);
            }
        }).start();
        
        // To execute on exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // clear screen
            if(refreshScreen) {
                refreshScreen = false;
                Tools.sleepMilliseconds(sleepBetweenScreenRefreshes);
            }
            lcdDisplay.disableDisplay();
        }));
    }
    
    /* ===============================================
     *                    METHODS
     * =============================================*/
    
    /**
     * Starts the robot.
     */
    public void start() {
        running = true;
    }
    
    /**
     * Sets the robot to the pause mode.
     */
    public void pause() {
        running = false;
    }
    
    /**
     * Stops the robot. It can't then be restarted.
     */
    public void stop() {
        running = false;
        stopped = true;
    }
    
    /**
     * !ATTENTION! pay attention to how you build your support !
     * The position of the rotor is important as your support couldn't rotate
     * in all directions.
     * In our case, we calibrate it with the following:
     *  0 points to the right (min value)
     *  90 points straight ahead
     *  180 points to the left (max value)
     * @param angle the new angle to apply.
     */
    public void rotateCameraSupportHorizontally(double angle) {
        double minAngle = 0;
        double maxAngle = 180;
        if(angle > maxAngle)
            angle = maxAngle;
        else if(angle < minAngle)
            angle = minAngle;
        servo1.setAngle(angle);
    }
    
    /**
     * !ATTENTION! pay attention to how you build your support !
     * The position of the rotor is important as your support couldn't rotate
     * in all directions.
     * In our case, we calibrate it with the following:
     *  0 points to the sky (min value)
     *  58 points straight ahead
     *  95 points down (max value)
     * @param angle the new angle to apply.
     */
    public void rotateCameraSupportVertically(double angle) {
        double minAngle = 0;
        double maxAngle = 95;
        if(angle > maxAngle)
            angle = maxAngle;
        else if(angle < minAngle)
            angle = minAngle;
        servo2.setAngle(angle);
    }
    
    /**
     * Called when the position of the remote joystick has changed.
     * @param x the new x position received.
     * @param y the new y position received.
     */
    public void newJoystickPosition(int x, int y) {
        rotateCameraSupportHorizontally(joystickValueToHorizontalAngle(y));
        rotateCameraSupportVertically(joystickValueToVerticalAngle(x));
        Tools.log("New joystick position: " + x + " " + y + " | vertical angle: " + joystickValueToVerticalAngle(x) + " | horizontal angle: " + joystickValueToHorizontalAngle(y));
    }
        
    /**
     * Returns the angle value in degree corresponding to the value received from the joystick.
     * @param value
     * @return
     */
    private double joystickValueToVerticalAngle(int value) {
        if(value < CAMERA_SUPPORT_JOYSTICK_VERTICAL_MIN_VALUE_RANGE)
            return CAMERA_SUPPORT_MIN_VERTICAL_ANGLE;
        else if(value > CAMERA_SUPPORT_JOYSTICK_VERTICAL_MAX_VALUE_RANGE)
            return CAMERA_SUPPORT_MAX_VERTICAL_ANGLE;
        else
            return (value - CAMERA_SUPPORT_JOYSTICK_VERTICAL_MIN_VALUE_RANGE) * CAMERA_SUPPORT_VERTICAL_JOYSTICK_UNIT_TO_DEGREE;
    }
        
    /**
     * Returns the angle value in degree corresponding to the value received from the joystick.
     * @param value
     * @return
     */
    private double joystickValueToHorizontalAngle(int value) {
        if(value < CAMERA_SUPPORT_JOYSTICK_HORIZONTAL_MIN_VALUE_RANGE)
            return CAMERA_SUPPORT_MIN_HORIZONTAL_ANGLE;
        else if(value > CAMERA_SUPPORT_JOYSTICK_HORIZONTAL_MAX_VALUE_RANGE)
            return CAMERA_SUPPORT_MAX_HORIZONTAL_ANGLE;
        else
            return (value - CAMERA_SUPPORT_JOYSTICK_HORIZONTAL_MIN_VALUE_RANGE) * CAMERA_SUPPORT_HORIZONTAL_JOYSTICK_UNIT_TO_DEGREE;
    }
    
    
    /**
     * This method updates the required content of the display, when needed.
     * Regarding the targetScreen, only usefull sections of the screen are updated.
     * @param targetScreen
     * @param refreshRate
     */
    private void updateDisplayScreen() {
        String message = "Disabled";
        switch(currentDisplayScreen) {
        case IP_ADDRESS:
            ipAddresses = NetworkUtilities.getIpAddresses();
            if(ipAddresses != null && !ipAddresses.isEmpty())
                lcdDisplay.writeTextRightAlign(1, ipAddresses.get(0)); // TODO create a new method "update line content" qui assure qu'on recouvre bien les char qui ne sont plus utilisés sur le display, par de " ". Simplement en gardant une trace du current printed line content.
            else
                lcdDisplay.writeTextRightAlign(1, "none available");
            break;
        case TEMPERATURE:
            if(thermistor != null)
                message = String.format(Locale.US,"%.2f", thermistorTemperature) + (char)223 + "C";
            lcdDisplay.writeTextRightAlign(1, " " + message);
            break;
        case BAROMETER_TEMPERATURE:
            if(barometer != null)
                message = String.format(Locale.US,"%.2f", barometerTemperature) + (char)223 + "C";
            updateLcdLine(1, message, false);
            break;
        case BAROMETER_PRESSURE:
            if(barometer != null)
                message = String.format(Locale.US,"%.2f", barometerPressure) + " Pa";
            updateLcdLine(1, message, false);
            break;
        case BAROMETER_ALTITUDE:
            if(barometer != null)
                message = String.format(Locale.US,"%.2f", barometerAltitude) + " m";
            updateLcdLine(1, message, false);
            break;
        default:
            break;
        }
    }
    
    private void updateLcdLine(int lineNb, String text, boolean leftAlign) {
        if(text.length() > LCM1602.NB_COL) {
            text = text.substring(0, LCM1602.NB_COL);
        } else {
            String comp = "";
            for(int i = 0; i < LCM1602.NB_COL - text.length(); i++)
                comp += " ";
            if(leftAlign)
                text += comp;
            else
                text = comp + text;
        }
        
        lcdDisplay.writeText(lineNb, 0, text);
    }
    
    private void switchToDisplayScreen(DisplayScreen targetScreen) {
        refreshScreen = false; // ensure no update will be made during the switch of screen
        lcdDisplay.clearDisplay();
        this.currentDisplayScreen = targetScreen;
        
        switch(currentDisplayScreen) {
        case IP_ADDRESS:
            lcdDisplay.writeText(0, 0, "IP address:");
            updateDisplayScreen();
            sleepBetweenScreenRefreshes = 5000;
            refreshScreen = true;
            break;
        case TEMPERATURE:
            lcdDisplay.writeText(0, 0, "Temperature:");
            updateDisplayScreen();
            sleepBetweenScreenRefreshes = 200;
            refreshScreen = true;
            break;
        case BAROMETER_TEMPERATURE:
            lcdDisplay.writeText(0, 0, "Barometer T" + (char)223 + ":");
            updateDisplayScreen();
            sleepBetweenScreenRefreshes = 500;
            refreshScreen = true;
            break;
        case BAROMETER_PRESSURE:
            lcdDisplay.writeText(0, 0, "Barometer P:");
            updateDisplayScreen();
            sleepBetweenScreenRefreshes = 500;
            refreshScreen = true;
            break;
        case BAROMETER_ALTITUDE:
            lcdDisplay.writeText(0, 0, "Barometer Alt.:");
            updateDisplayScreen();
            sleepBetweenScreenRefreshes = 500;
            refreshScreen = true;
            break;
        case UNKNOWN:
            lcdDisplay.writeText(0, 0, "Unknown signal");
            lcdDisplay.writeText(1, 0, "received");
            refreshScreen = false;
            break;
        default:
            lcdDisplay.writeText(0, 0, "ERROR 875");
            refreshScreen = false;
            break;
        }
    }
    
    /* ===============================================
     *                      MAIN
     * =============================================*/
    
    /**
     * Command-line interface.
     * @param args unused here.
     */
    public static void main(String[] args) {
        Tools.enableDebugs();
        Tools.enableLogs();
        
        POCConfig config = new POCConfig();
        config.enablePCF8591Nb1();
        config.enableLcdDisplay();
        config.enableCameraSupport();
        config.disableIRReceiver(); // ! Attention ! requires polling
        config.enableThermistor();
        config.enableBarometer();
        config.disablePassiveBuzzer();
        
        RobotPOC robot = new RobotPOC(config);
        robot.start();
        
        while(true)
            Tools.sleepMilliseconds(1000);
    }
    
    /**
     * Different screens available.
     */
    public enum DisplayScreen {
        /**
         * Displays the main ip address.
         */
        IP_ADDRESS("ip_address"),
        
        /**
         * Displays the temperature, given by the thermistor.
         */
        TEMPERATURE("temperature"),
        
        /**
         * Displays the temperature, given by the barometer.
         */
        BAROMETER_TEMPERATURE("barometer_temperature"),
        
        /**
         * Displays the pressure, given by the barometer.
         */
        BAROMETER_PRESSURE("barometer_pressure"),
        
        /**
         * Displays the altitude, calculated from the barometer.
         */
        BAROMETER_ALTITUDE("barometer_altitude"),
        
        /**
         * Unknown screen. Selected in case of error.
         */
        UNKNOWN("unknown");
        
        String name;
        DisplayScreen(String name) {
            this.name = name;
        }
    }

}
