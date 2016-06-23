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

import com.raspoid.GPIOPin;
import com.raspoid.Tools;
import com.raspoid.additionalcomponents.UltrasonicHCSR04;
import com.raspoid.brickpi.BrickPi;
import com.raspoid.brickpi.nxt.sensor.UltraSonicSensor;

/**
 * Implementation of a behavior for the POC robot that allows to alleviate obstacles.
 * 
 * <p>Basically, when an obstacle is detected in the front or in the back,
 * the robot rotates and moves forwards, until no new obstacle is detected.</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class ObstacleDetectorRobot extends BaseRobot {
    
    UltraSonicSensor nxtUltrasonic;
    UltrasonicHCSR04 additionalUltrasonic;
    
    private ObstacleDetectorRobot() {
        super();
        
        BrickPi.S2 = new UltraSonicSensor();
        nxtUltrasonic = (UltraSonicSensor) BrickPi.S2;
        BrickPi.start();
        
        additionalUltrasonic = new UltrasonicHCSR04(GPIOPin.GPIO_25, GPIOPin.GPIO_24);
    }
    
    private boolean hcsr04ObstacleDetected() {
        if(additionalUltrasonic.getDistance() < 7.)
            return true;
        return false;
    }
    
    private boolean nxtObstacleDetected() {
        if(nxtUltrasonic.getDistance() < 20)
            return true;
        return false;
    }
    
    /**
     * Moves forward/backward until obstacle detected at < 20 cm.
     * <ul>
     *  <li>if forward == true: uses the HCSR04 to detect obstacles,</li>
     *  <li>if forward == false (= backward): uses the NXT ultrasonic sensor to detect obstacles.</li>
     * </ul>
     * BLOCKING.
     */
    private void moveUntilObstacleDetected(boolean forward) {
        Tools.log("move " + (forward ? "forward" : "backward"));
        
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while(true) {
                        if(forward && hcsr04ObstacleDetected() || !forward && nxtObstacleDetected()) {
                            motorLeft.setPower(0);
                            motorRight.setPower(0);
                            this.interrupt();
                            this.join();
                            return;
                        }
                        Tools.sleepMilliseconds(100);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
        thread.start();
        
        if(forward) {
            motorLeft.setPower(-150);
            motorRight.setPower(-200);
        } else {
            motorLeft.setPower(150);
            motorRight.setPower(200);
        }
        
        while(thread.isAlive())
            Tools.sleepMilliseconds(10);        
    }
    
    /**
     * Rotate right/left until the detected distance in front increases.
     * BLOCKING.
     */
    private void rotate(boolean right, int maxEncoderDelta) {
        Tools.log("Rotate " + (right ? "right" : "left"));
        
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    int startEncoderValue = motorLeft.getEncoderValue();
                    while(true) {
                        if(Math.abs(Math.abs(motorLeft.getEncoderValue()) - Math.abs(startEncoderValue)) > maxEncoderDelta) {
                            motorLeft.setPower(0);
                            motorRight.setPower(0);
                            this.interrupt();
                            this.join();
                            return;
                        }
                        Tools.sleepMilliseconds(100);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
        thread.start();
        
        if(right) {
            motorLeft.setPower(-200);
            motorRight.setPower(-50);
        } else {
            motorLeft.setPower(-50);
            motorRight.setPower(-200);
        }
        
        while(thread.isAlive())
            Tools.sleepMilliseconds(10);
    }
    
    /**
     * Start the robot.
     */
    public void start() {
        new Thread(() -> {
            while(true) {
                moveUntilObstacleDetected(true);
                moveUntilObstacleDetected(false);
                rotate(true, 72000);
            }
        }).start();
    }
    
    /**
     * Command-line interface.
     * @param args unused here.
     */
    public static void main(String[] args) {
        ObstacleDetectorRobot robot = new ObstacleDetectorRobot();
        robot.start();
        Tools.sleepMilliseconds(100000);
        robot.stop();
    }
}
