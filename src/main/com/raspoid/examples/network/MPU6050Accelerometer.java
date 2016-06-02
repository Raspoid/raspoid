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
package com.raspoid.examples.network;

import com.raspoid.Tools;
import com.raspoid.additionalcomponents.MPU6050;
import com.raspoid.network.Router;
import com.raspoid.network.SocketServer;

/**
 * Example of a socket server to remotely retrieve informations from
 * an MPU6050 accelerometer/gyroscope.
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class MPU6050Accelerometer {
    
    /**
     * Constructor to hide the implicit public one.
     */
    private MPU6050Accelerometer() {
    }
    
    /**
     * Command-line interface.
     * @param args unused here.
     */
    public static void main(String[] args) {
        Router router = new Router();
        SocketServer server = new SocketServer(router);
        server.start();
        
        Tools.log(server.getIpAddresses().get(0));
        
        MPU6050 mpu6050 = new MPU6050();
        mpu6050.startUpdatingThread();
        
        router.addRoute("accel", () -> {
            double[] accelAngles = mpu6050.getAccelAngles();
            return accelAngles[0] + " " + accelAngles[1] + " " + accelAngles[2];
        });
        
        router.addRoute("accel_accelerations", () -> {
            double[] accelAccelerations = mpu6050.getAccelAccelerations();
            return accelAccelerations[0] + " " + accelAccelerations[1] + " " + accelAccelerations[2];
        });
        
        router.addRoute("gyro", () -> {
            double[] gyroAngles = mpu6050.getGyroAngles();
            return gyroAngles[0] + " " + gyroAngles[1] + " " + gyroAngles[2];
        });
        
        router.addRoute("gyro_angular_speeds", () -> {
            double[] gyroAngularSpeeds = mpu6050.getGyroAngularSpeeds();
            return gyroAngularSpeeds[0] + " " + gyroAngularSpeeds[1] + " " + gyroAngularSpeeds[2];
        });
        
        router.addRoute("gyro_angular_speeds_offsets", () -> {
            double[] gyroAngularSpeedsOffsets = mpu6050.getGyroAngularSpeedsOffsets();
            return gyroAngularSpeedsOffsets[0] + " " + gyroAngularSpeedsOffsets[1] + " " + gyroAngularSpeedsOffsets[2];
        });
        
        router.addRoute("filtered", () -> {
            double[] filteredAngles = mpu6050.getFilteredAngles();
            return filteredAngles[0] + " " + filteredAngles[1] + " " + filteredAngles[2];
        });
    }
}
