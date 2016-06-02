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
package com.raspoid.brickpi.uart.message;

import com.raspoid.brickpi.Sensor;
import com.raspoid.brickpi.nxt.sensor.SensorType;

import static com.raspoid.brickpi.nxt.sensor.SensorType.*;

import java.util.BitSet;

/**
 * Encapsulation of the sensor type message.
 * This message is used to declare the sensors used to the brickpi,
 * it is used in the initialization phase before using the values.
 * One type message is necessary for each atmel chip managing 2 sensors
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class SensorTypeMessage implements Message {

    /**
     * Number of bits needed to encode
     */
    private static final int NB_LEGO_US_I2C_DATA_REG_BITS = 8;

    /**
     * Number of bits needed to encode US_I2C_READ
     */
    private static final int NB_US_I2C_READ_BITS = 4;

    /**
     * Number of bits needed to encode US_I2C_WRITE
     */
    private static final int NB_US_I2C_WRITE_BITS = 4;

    /**
     * Number of bits needed to encode US_I2C_SETTINGS
     */
    private static final int NB_US_I2C_SETTINGS_BITS = 2;

    /**
     * Number of bits needed to encode LEGO_US_I2C_ADDR
     */
    private static final int NB_LEGO_US_I2C_ADDR_BITS = 7;

    /**
     * Number of bits needed to encode US_I2C_DEVICE
     */
    private static final int NB_US_I2C_DEVICE_BITS = 3;

    /**
     * Number of bits needed to encode US_I2C_SPEED
     */
    private static final int NB_US_I2C_SPEED_BITS = 8;

    /**
     * Data register for the ultrasonic sensor
     */
    private static final int LEGO_US_I2C_DATA_REG = 0x42;

    /**
     * Number of bytes read by the ultrasonic sensor
     */
    private static final int NB_US_I2C_READ_BYTES = 1;

    /**
     * Number of bytes written by the ultrasonic sensor
     */
    private static final int NB_US_I2C_WRITE_BYTES = 1;

    /**
     * Sensor settings equivalent to (BIT_I2C_SAME | BIT_I2C_MID) in
     * official firmware
     */
    private static final int US_I2C_SETTINGS = 3;

    /**
     * Sensor address for the ultrasonic sensor
     */
    private static final int LEGO_US_I2C_ADDR = 1;

    /**
     * I2C sensor bus for the ultrasonic sensor
     */
    private static final int US_I2C_DEVICE = 0;

    /**
     * I2C speed for the ultrasonic sensor. This value can be tweaked
     * if you have some problems.
     */
    private static final int US_I2C_SPEED = 10;

    /**
     * Number of bytes needed for the types in the payload
     */
    private static final int NB_TYPE_BYTES = 2;

    /**
     * Number of bits needed for an ultrasonic sensor in the payload
     */
    private static final int NB_ULTRASONIC_PAYLOAD_BITS = 36;

    /**
     * Number of bits needed for the types in the payload
     */
    private static final int NB_TYPE_BITS = NB_TYPE_BYTES * Byte.SIZE;

    /**
     * Holds the type of sensor 1
     */
    private SensorType sensorType1;

    /**
     * Holds the type of sensor 2
     */
    private SensorType sensorType2;

    /**
     * Initialize the sensor type message with the two sensors to declare
     * @param sensor1 the first sensor type to declare
     * @param sensor2 the second sensor type to declare
     */
    public SensorTypeMessage(Sensor sensor1, Sensor sensor2) {
        //TYPE_SENSOR_RAW needs to be defined when no sensor is actually used
        this.sensorType1 = (sensor1 != null) ? sensor1.getType() : TYPE_SENSOR_RAW;
        this.sensorType2 = (sensor2 != null) ? sensor2.getType() : TYPE_SENSOR_RAW;
    }

    @Override
    public byte getType() {
        return MessageType.MSG_TYPE_SENSOR_TYPE.toByte();
    }

    @Override
    public byte[] getPayload() {
        //We always need at least the sensors type
        int bitsInPayload = NB_TYPE_BITS;
        // When using the ultrasonic sensor, we need to use a hack
        // leveraging the I2C capabilities of the brickpi. We build the
        // payload of the ultrasonic sensor with a bitset that will be appended
        // after the sensor types
        BitSet ultrasonicBits = new BitSet();
        int baseOffset = 0;
        // Effective byte type in the payload is different for the
        // ultrasonic sensor than the byte type from declaration
        byte[] effectiveTypes = {sensorType1.toByte(), sensorType2.toByte()};
        //Encode the ultrasonic hack payload if declared as such
        for (int i = 0; i < effectiveTypes.length; i++) {
            if (effectiveTypes[i] == TYPE_SENSOR_ULTRASONIC_CONT.toByte()) {
                bitsInPayload += NB_ULTRASONIC_PAYLOAD_BITS;
                effectiveTypes[i] = TYPE_SENSOR_I2C.toByte();
                baseOffset = encodeI2cUltrasound(baseOffset, ultrasonicBits);
            }
        }
        //Create the payload buffer with the right size
        int bytesInPayload = bitsInPayload / Byte.SIZE;
        if (bitsInPayload % Byte.SIZE > 0) {
            bytesInPayload++;
        }
        byte[] payload = new byte[bytesInPayload];
        //Fill the first 2 bytes with effective sensor type
        System.arraycopy(effectiveTypes, 0, payload, 0, effectiveTypes.length);
        //Get the payload part of the ultrasonic sensors
        // the trailing zeroes of a bitset are not converted into
        // the byte array, we could miss a byte in the end but this
        // effect is avoided by first creating the payload with the right
        // which is zeroed by default
        byte[] ultrasonicPayload = ultrasonicBits.toByteArray();
        System.arraycopy(ultrasonicPayload, 0, payload, NB_TYPE_BYTES, ultrasonicPayload.length);
        return payload;
    }

    /**
     * Encodes an ultrasonic sensor into a bistset starting at a base offset
     * and returning the next base offset
     * @param baseOffset the base offset to start encoding
     * @param msgBits the bitset in which to encode the ultrasonic payload
     * @return the new baseOffset value after the encoded payload
     */
    private int encodeI2cUltrasound(int baseOffset, BitSet msgBits) {
        int tmpBaseOffset = baseOffset;
        tmpBaseOffset = encodeBits(tmpBaseOffset, msgBits, US_I2C_SPEED, NB_US_I2C_SPEED_BITS);
        tmpBaseOffset = encodeBits(tmpBaseOffset, msgBits, US_I2C_DEVICE, NB_US_I2C_DEVICE_BITS);
        tmpBaseOffset = encodeBits(tmpBaseOffset, msgBits, LEGO_US_I2C_ADDR, NB_LEGO_US_I2C_ADDR_BITS);
        tmpBaseOffset = encodeBits(tmpBaseOffset, msgBits, US_I2C_SETTINGS, NB_US_I2C_SETTINGS_BITS);
        tmpBaseOffset = encodeBits(tmpBaseOffset, msgBits, NB_US_I2C_WRITE_BYTES, NB_US_I2C_WRITE_BITS);
        tmpBaseOffset = encodeBits(tmpBaseOffset, msgBits, NB_US_I2C_READ_BYTES, NB_US_I2C_READ_BITS);
        tmpBaseOffset = encodeBits(tmpBaseOffset, msgBits, LEGO_US_I2C_DATA_REG, NB_LEGO_US_I2C_DATA_REG_BITS);
        return tmpBaseOffset;
    }

    /**
     * Encode the last nbBits bits of an integer in Least byte order into a bitset starting at a base offset
     * and return the new base offset
     * @param baseOffset the base offset to start encoding in the bitset
     * @param msgBits the bitset in which to encode the bits
     * @param toEncode the int from which bits are extracted
     * @param nbBits the number of bits starting from the least bit to encode
     * @return the new baseOffset
     */
    private int encodeBits(int baseOffset, BitSet msgBits, int toEncode, int nbBits) {
        //Encode in LSB
        for (int i = 0; i < nbBits; i++) {
            msgBits.set(baseOffset + i, ((toEncode >> i) & 0x1) == 1);
        }
        return baseOffset + nbBits;
    }

}
