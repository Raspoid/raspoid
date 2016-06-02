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
package com.raspoid.brickpi.nxt.sensor;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration listing the Sensor type available and their byte value
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public enum SensorType {
    /**
     * Type corresponding to touch sensor.
     */
    TYPE_SENSOR_TOUCH((byte)32),
    
    /**
     * Type corresponding to ultrasonic sensor.
     */
    TYPE_SENSOR_ULTRASONIC_CONT((byte)33),
    
    /**
     * Type corresponding to color full sensor.
     */
    TYPE_SENSOR_COLOR_FULL((byte)36),
    
    /**
     * Type corresponding to light on sensor.
     */
    TYPE_SENSOR_LIGHT_ON((byte)9),
    
    /**
     * Type corresponding to RCX light sensor.
     */
    TYPE_SENSOR_RCX_LIGHT((byte)35),
    
    /**
     * Type corresponding to color red sensor.
     */
    TYPE_SENSOR_COLOR_RED((byte)37),
    
    /**
     * Type corresponding to color green sensor.
     */
    TYPE_SENSOR_COLOR_GREEN((byte)38),
    
    /**
     * Type corresponding to color blue sensor.
     */
    TYPE_SENSOR_COLOR_BLUE((byte)39),
    
    /* TYPE_SENSOR_RAW and TYPE_SENSOR_LIGHT_OFF have the same value
     * TYPE_SENSOR_RAW is used as default for the mapping */
    
    /**
     * Type corresponding to color none sensor.
     */
    TYPE_SENSOR_COLOR_NONE((byte)40),
    
    /**
     * Type corresponding to I2C sensor.
     */
    TYPE_SENSOR_I2C((byte)41),
    
    /**
     * Type corresponding to raw sensor.
     */
    TYPE_SENSOR_RAW((byte)0),
    
    /**
     * Type corresponding to light off sensor.
     */
    TYPE_SENSOR_LIGHT_OFF((byte)0);

    /**
     * Contains a map with all the sensor type accessible by their byte value,
     * this allows to retrieve a sensor type object by providing it's byte value
     */
    private static Map<Byte, SensorType> mapping = new HashMap<>();

    /**
     * Contains the sensor type byte value
     */
    private final byte value;

    /**
     * Static constructor initializing the mapping Map
     */
    static {
        for (SensorType sensorType : SensorType.values()) {
            mapping.put(sensorType.value, sensorType);
        }
        //Force the mapping to use TYPE_SENSOR_RAW for value 0
        mapping.put(TYPE_SENSOR_RAW.value, TYPE_SENSOR_RAW);
    }

    /**
     * Creates a sensor type by giving the byte value
     * @param value
     */
    private SensorType(byte value) {
        this.value = value;
    }

    /**
     * Get the byte value of the sensor type
     * @return the byte representation of the sensor type.
     */
    public byte toByte() {
        return value;
    }

    /**
     * Get a SensorType object by giving its byte value
     * @param type the byte value type
     * @return a corresponding SensorType object
     */
    public static SensorType valueOf(byte type) {
        if (!mapping.containsKey(type)) {
            throw new IllegalArgumentException("Sensor type is unknown");
        }
        return mapping.get(type);
    }
}
