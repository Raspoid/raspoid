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
package com.raspoid.additionalcomponents;

import static java.lang.Math.PI;
import static java.lang.Math.atan2;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.io.IOException;

import com.raspoid.I2CComponent;
import com.raspoid.exceptions.RaspoidException;

/**
 * This implements access to a 3-axis ADXL345 accelerometer.
 * 
 * <p>Datasheet: <a href="http://raspoid.com/download/datasheet/ADXL345">ADXL345</a></p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class AccelerometerADXL345 extends I2CComponent {
    
    /**
     * Default i2c ADXL345 address.
     */
    public static final int DEFAULT_ADXL345_ADDRESS = 0x53;
    
    /**
     * Scale that should be used when the device is in full resolution mode.
     * <p>Corresponds to 3.9mg/LSB</p>
     * <p>This scale can change when the temperature of the device varies.
     * Please see the datasheet
     * for more information</p>
     * <p>[datasheet - p.1]</p>
     */
    private double scale = 0.0039;
    
    /**
     * Constructor for a new ADXL345, using the default i2c ADXL345 address.
     */
    public AccelerometerADXL345() {
        this(DEFAULT_ADXL345_ADDRESS);
    }
    
    /**
     * Constructor for a new ADXL345, using a specific i2c address.
     * @param i2cAddress the i2c address of the ADXL345.
     */
    public AccelerometerADXL345(int i2cAddress) {
        super(i2cAddress);
        // Power control
        writeUnsignedValueToRegister(0x2D, 0x8);
        // Data format
        writeUnsignedValueToRegister(0x31, 0xB);
    }
    
    /**
     * Get the scale that should be used when the device is in full resolution mode.
     * @return the scale that should be used when the device is in full resolution mode.
     */
    public double getScale() {
        return scale;
    }

    /**
     * Sets the scale that should be used when the device is in full resolution mode.
     * @param scale the new scale to use when the device is in full resolution mode.
     */
    public void setScale(double scale) {
        this.scale = scale;
    }
    
    /**
     * Get the current acceleration, read from the ADXL345.
     * @return the current acceleration from the ADXL345, in the [x, y, z] format.
     */
    public double[] getGAcceleration() {
        byte[] rawData = new byte[6];
        int readSize = 0;
        try {
            readSize = device.read(0x32, rawData, 0, 6);
            if (readSize != 6)
                throw new RaspoidException("The accelerometer data were not retrieved properly.");
        } catch (IOException e) {
            throw new RaspoidException("There was an error while reading the accelerometer data.", e);
        }

        short x = (short) (((rawData[1] & 0xff) << 8) | (rawData[0] & 0xff));
        short y = (short) (((rawData[3] & 0xff) << 8) | (rawData[2] & 0xff));
        short z = (short) (((rawData[5] & 0xff) << 8) | (rawData[4] & 0xff));
        
        return new double[] {x * scale,y * scale,z * scale};
    }
    
    /**
     * Get the pitch angle.
     * @return the pitch angle.
     */
    public double getPitchAngle() {
        double[] acceleration = getGAcceleration();
        return (atan2(acceleration[0], sqrt(pow(acceleration[1], 2) + pow(acceleration[3], 2))) * 180.0) / PI;
    }   
}
