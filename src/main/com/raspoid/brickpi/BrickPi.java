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
/*
 *  Copyright ErgoTech Systems, Inc 2014
 *
 * This file is made available online through a Creative Commons Attribution-ShareAlike 3.0  license.
 * (http://creativecommons.org/licenses/by-sa/3.0/)
 *
 *  This is a library of functions for the RPi to communicate with the BrickPi.
 */
package com.raspoid.brickpi;

import com.raspoid.brickpi.nxt.sensor.RawSensor;

/**
 * BrickPi implementation providing an interface to use motors and sensors
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class BrickPi {

    /**
     * Number of Atmel chips (AtMega328).
     */
    public static final int NB_ATMEGA_TARGETS = 2;
    
    /**
     * Number of sensors for each AtMega328.
     */
    public static final int NB_SENSORS_BY_ATMEGA = 2;
    
    /**
     * Number of motors for each AtMega328.
     */
    public static final int NB_MOTORS_BY_ATMEGA = 2;
    
    /**
     * Use when a motor is connected on port A to control it
     */
    public static Motor MA; // NOSONAR
    
    /**
     * Use when a motor is connected on port B to control it
     */
    public static Motor MB; // NOSONAR
    
    /**
     * Use when a motor is connected on port C to control it
     */
    public static Motor MC; // NOSONAR
    
    /**
     * Use when a motor is connected on port D to control it
     */
    public static Motor MD; // NOSONAR
    
    /**
     * Use when a sensor is connected on port 1 to control it.
     * It is and should be a RawSensor when not used.
     */
    public static Sensor S1 = new RawSensor(); // NOSONAR
    
    /**
     * Use when a sensor is connected on port 2 to control it.
     * It is and should be a RawSensor when not used.
     */
    public static Sensor S2 = new RawSensor(); // NOSONAR
    
    /**
     * Use when a sensor is connected on port 3 to control it.
     * It is and should be a RawSensor when not used.
     */
    public static Sensor S3 = new RawSensor(); // NOSONAR
    
    /**
     * Use when a sensor is connected on port 4 to control it.
     * It is and should be a RawSensor when not used.
     */
    public static Sensor S4 = new RawSensor(); // NOSONAR

    /**
     * Contains the connector to communicate with the BrickPi card
     */
    private static BrickPiConnector brickPiConnector = new BrickPiConnector();

    /**
     * Use when the LED 1 need to be controlled 
     */
    public static final Led LED1 = new Led(1);
    
    /**
     * Use when the LED 2 need to be controlled
     */
    public static final Led LED2 = new Led(2);

    /**
     * Constructor is private
     */
    private BrickPi() {/* only use static methods */}
    
    /**
     * Once sensors and motors are configured, this method is used to run the BrickPi.
     * This launches the polling threads that update motors encoders and fetches fresh sensor values.
     */
    public static void start() {
        brickPiConnector.start();
    }
    
    /**
     * Once the program is over, this method should be called to release the
     * resources properly
     */
    public static void stop() {
        brickPiConnector.stop();
    }
    
    /**
     * Convenient getter for all the motors
     * @return an array with the motors references in order MA, MB, MC, MD
     */
    public static Motor[] getMotors() {
        return new Motor[] {MA, MB, MC, MD};
    }
    
    /**
     * Convenient getter for all the sensors
     * @return an array with the sensors references in order S1,S2,S3,S4
     */
    public static Sensor[] getSensors() {
        return new Sensor[] {S1, S2, S3, S4};
    }
    
    /**
     * Convenient method to configure motors and sensors in an atomic fashion.
     * This should only be used to set some values for motors or sensors and it should never
     * block. The settings are committed in the caller's thread.
     * @param transaction the transaction code to execute
     */
    public static synchronized void commitSettings(Runnable transaction) {
        transaction.run();
        brickPiConnector.flush();
    }
}
