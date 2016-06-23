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
package com.raspoid.examples.robots.twip;

import com.raspoid.Tools;
import com.raspoid.additionalcomponents.MPU6050;
import com.raspoid.brickpi.BrickPi;
import com.raspoid.brickpi.Motor;

/**
 * Attempt to implement a pendulum robot (a robot balancing on two wheels).
 * 
 * Many attempts, but does not work.
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class Pendulum {
    
    Motor motorLeft;
    Motor motorRight;
    
    MPU6050 mpu6050;
    
    double equilibriumAngle = 176.; // °
    
    private Pendulum() {
        mpu6050 = new MPU6050(0x68,MPU6050.DEFAULT_DLPF_CFG,MPU6050.DEFAULT_SMPLRT_DIV);
        mpu6050.startUpdatingThread();
        
        BrickPi.MA = new Motor();
        motorRight = BrickPi.MA;
        BrickPi.MB = new Motor();
        motorLeft = BrickPi.MB;
        BrickPi.start();
    }
    
    private void setPowers(int power) {
        motorLeft.setPower(power);
        motorRight.setPower(power);
    }
    
    private void equilibrium() {
        new Thread(() -> {
            double yAngle;
            double delta;
            while(true) {
                yAngle = mpu6050.getFilteredAngles()[1];
                delta = equilibriumAngle - yAngle;
                Tools.log(delta);
                if(delta > 15)
                    setPowers(-250);
                else if(delta  > 7)
                    setPowers(-200);
                else if(delta > 0)
                    setPowers(-100);
                else if(delta < -15)
                    setPowers(250);
                else if(delta < -7)
                    setPowers(200);
                else if(delta < 0)
                    setPowers(100);
                else 
                    setPowers(0);
            }
        }).start();
    }
    
    /**
     * Command-line interface.
     * @param args unused here.
     */
    public static void main(String[] args) {
        Pendulum pendulum = new Pendulum();
        Tools.sleepMilliseconds(5000);
        pendulum.equilibrium();
    }
}
