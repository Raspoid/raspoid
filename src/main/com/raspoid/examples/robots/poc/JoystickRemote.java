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

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.raspoid.GPIOPin;
import com.raspoid.Tools;
import com.raspoid.additionalcomponents.Button;
import com.raspoid.additionalcomponents.Joystick;
import com.raspoid.additionalcomponents.LED;
import com.raspoid.additionalcomponents.adc.PCF8591;
import com.raspoid.additionalcomponents.adc.PCF8591InputChannel;
import com.raspoid.exceptions.RaspoidException;

/**
 * A JoystickRemote is used to remotely control our proof of concept robot ({@link RobotPOC}).
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class JoystickRemote {
    
    /**
     * Mode to control the position of the camera support.
     */
    private static final int CAMERA_SUPPORT_MODE = 0;
    
    /**
     * Mode to control the direction of the robot.
     */
    private static final int ROBOT_MOTORS_MODE = 1;
    
    /**
     * Array containing the different modes of the joystick.
     */
    private static final int[] modes = {CAMERA_SUPPORT_MODE, ROBOT_MOTORS_MODE};
    
    /**
     * The position (index) of the current mode in the modes array.
     * @see #modes
     */
    private int currentModePos = 0;
    
    /**
     * Current selected mode.
     */
    private int currentMode = modes[0];
    
    /**
     * [Joystick] joystick instance.
     * Uses inputs 0, 1 and 2 from ADC8591.
     */
    private Joystick joystick = null;
    
    private Button switchModeButton = null;
    
    private LED led = null;
    
    private DataOutputStream outputStream = null;
    
    private Socket socket = null;
    
    private boolean running = false;
    
    /**
     * Constructor for a new Joystick remote, used to control our RobotPOC.
     * We use a websocket to share the current position of the joystick.
     * @param ipAddress the IP address of the RobotPOC MessageLikeSocketServer.
     * @param port the port of the RobotPOC MessageLikeSocketServer.
     */
    public JoystickRemote(String ipAddress, String port) {
        PCF8591 pcf8591 = new PCF8591();
        joystick = new Joystick(pcf8591, PCF8591InputChannel.CHANNEL_1, PCF8591InputChannel.CHANNEL_0, PCF8591InputChannel.CHANNEL_2);
        
        led = new LED(GPIOPin.GPIO_00);
        led.pulse(1000);
        
        switchModeButton = new Button(GPIOPin.GPIO_04);
        switchModeButton.getGpioPinDigitalInput().addListener((GpioPinListenerDigital)
            (GpioPinDigitalStateChangeEvent event) -> {
                if(switchModeButton.isPressed())
                    switchMode();
            });
        
        connectToRobotPOC(ipAddress, port);
        
        // To execute on exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RaspoidException("Error when closing socket.", e);
                }
            }
        }));
    }
    
    /**
     * Starts updating and sending values to the RobotPOC.
     */
    public void start() {
        running = true;
    }
    
    /**
     * Pauses updating and sending values to the RobotPOC.
     */
    public void pause() {
        running = false;
    }
    
    private void connectToRobotPOC(String ipAddress, String port) {
        try {
            Tools.log("Asking connection with the server.");
            socket = new Socket(ipAddress, Integer.parseInt(port));
            Tools.log("Connection established.");
            
            outputStream = new DataOutputStream(socket.getOutputStream());
            
            new Thread(() -> {
                boolean neutralValueSent = false;
                while(true) {
                    if(running) {
                        neutralValueSent = updateAndSendNewJoystickValue(neutralValueSent);
                    }
                    Tools.sleepMilliseconds(100);
                }
            }).start();
        } catch (NumberFormatException | IOException e) {
            throw new RaspoidException("Error when opening socket with server.", e);
        }
    }
    
    private boolean updateAndSendNewJoystickValue(boolean neutralValueSent) {
        boolean sendNewValue;
        int[] result = joystick.getXYPosition();
        int x = result[0];
        int y = result[1];
        if(x == -1 && y == -1) {
            // button pressed, no message sent
            neutralValueSent = false;
            sendNewValue = false;
        } else if(x <= 133 && x >= 129 && y <= 133 && y >= 129) {
            // neutral position
            if(!neutralValueSent) {
                sendNewValue = true;
                neutralValueSent = true;
            } else {
                sendNewValue = false;
            }
        } else {
            neutralValueSent = false;
            sendNewValue = true;
        }
        if(sendNewValue)
            sendNewValue(result[0], result[1]);
        return neutralValueSent;
    }
    
    private void sendNewValue(int x, int y) {
        String request = "error";
        if(currentMode == CAMERA_SUPPORT_MODE)
            request = "joystick_camera";
        else if(currentMode == ROBOT_MOTORS_MODE)
            request = "joystick_robot_motors";
        try {
            request = request + "/" + x + "/" + y;
            byte[] message = request.getBytes("UTF-8");
            outputStream.writeInt(message.length);
            outputStream.write(message);
            outputStream.flush();
        } catch (IOException e) {
            throw new RaspoidException("Problem when sending new Joystick update: (request) " + request, e);
        }
    }
    
    /**
     * Selects the next mode in modes array.
     * @return
     */
    private int switchMode() {
        if(currentModePos == (modes.length - 1))
            currentModePos = 0;
        else
            currentModePos++;
        currentMode = modes[currentModePos];
        
        Tools.log("New mode selected: " + currentMode);
        
        return currentMode;
    }
    
    /**
     * Command-line interface.
     * <p>Usage:
     * <pre>
     * JoystickRemote ip_address port_nb
     * </pre>
     * </p>
     * @param args input args: <ip_address> <port_nb>
     */
    public static void main(String[] args) {
        if(args.length != 2)
            throw new RaspoidException("Usage: JoystickRemote <ip_address> <port>");
        
        new JoystickRemote(args[0], args[1]).start();
        
        while(true) {
            Tools.sleepMilliseconds(100);
        }
    }
}
