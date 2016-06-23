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

import com.raspoid.Tools;
import com.raspoid.brickpi.BrickPi;
import com.raspoid.brickpi.Motor;
import com.raspoid.brickpi.nxt.sensor.LightOnSensor;

/**
 * Implementation of a behavior for the POC robot that allows to track a black line on the floor.
 * 
 * <p>Basically, a black line is firstly searched. When it is detected,
 * the robot rotates and moves forwards to follow and stay on top of this black line.</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class TrackingRobot {
    
    LightOnSensor lightOnSensor;
    Motor motorLeft;
    Motor motorRight;
    boolean stop = false;
    
    private TrackingRobot() {
        BrickPi.MA = new Motor();
        motorRight = BrickPi.MA;
        motorRight.setDiameter(5);
        BrickPi.MC = new Motor();
        motorLeft = BrickPi.MC;
        motorLeft.setDiameter(5);
        
        BrickPi.S1 = new LightOnSensor();
        lightOnSensor = (LightOnSensor) BrickPi.S1;
        
        BrickPi.start();
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> stop()));
    }
    
    private boolean blackDetected() {
        return lightOnSensor.getIntensity() > 410;
    }
    
    /**
     * Move forward until black detected line or while black line detected (depending on asLongAsBlack value).
     * BLOCKING.
     */
    private void moveForward(boolean asLongAsBlack) {
        Tools.log("moveForward");
        
        Thread thread = new Thread() {
            
            @Override
            public void run() {
                try {
                    while(true) {
                        if(asLongAsBlack && !blackDetected() || !asLongAsBlack && blackDetected()) {
                            motorLeft.setPower(0);
                            motorRight.setPower(0);
                            this.interrupt();
                            this.join();
                            return;
                        }
                        Tools.sleepMilliseconds(10);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
        thread.start();
        
        motorLeft.setPower(-150);
        motorRight.setPower(-150);
        
        while(thread.isAlive())
            Tools.sleepMilliseconds(10);
    }
    
    /**
     * Rotate, until black line detected.
     * BLOCKING.
     * @param right true to turn to the right, false otherwise.
     * @param maxEncoderDelta maximum encoder delta value before stopping to search a black line.
     * @return true if a black line has been detected. False if no black line detected and maxEncoderDelta traveled.
     */
    private boolean rotate(boolean right, int maxEncoderDelta) {
        Tools.log("Rotate " + (right ? "right" : "left"));
        
        int startEncoderValue = motorLeft.getEncoderValue();
        
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while(true) {
                        if(blackDetected() || Math.abs(motorLeft.getEncoderValue() - startEncoderValue) > maxEncoderDelta) {
                            motorLeft.setPower(0);
                            motorRight.setPower(0);
                            this.interrupt();
                            this.join();
                            return;
                        }
                        Tools.sleepMilliseconds(10);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
        thread.start();
        
        if(right) {
            motorLeft.setPower(-150);
            motorRight.setPower(150);
        } else {
            motorLeft.setPower(150);
            motorRight.setPower(-150);
        }
        
        while(thread.isAlive())
            Tools.sleepMilliseconds(10);
        
        return blackDetected();
    }
    
    /**
     * Start the robot.
     */
    public void start() {
        new Thread(() -> {
            while(true) {
                moveForward(false);
                moveForward(true);
                rotate(true, 2880);
                rotate(false, 5760);
            }
        }).start();
    }
    
    /**
     * Stop the robot.
     */
    public void stop() {
        stop = true;
        BrickPi.stop();
    }
    
    /**
     * Command-line interface.
     * @param args unused here.
     */
    public static void main(String[] args) {
        Tools.sleepMilliseconds(10000);
        TrackingRobot robot = new TrackingRobot();
        robot.start();
        Tools.sleepMilliseconds(100000);
        robot.stop();
    }
}
