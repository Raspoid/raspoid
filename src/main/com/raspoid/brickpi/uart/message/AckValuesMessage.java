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

import static com.raspoid.brickpi.BrickPi.NB_MOTORS_BY_ATMEGA;
import static com.raspoid.brickpi.BrickPi.NB_SENSORS_BY_ATMEGA;

import java.util.BitSet;

import com.raspoid.brickpi.Atmel;
import com.raspoid.brickpi.BrickPi;
import com.raspoid.brickpi.Sensor;
import com.raspoid.brickpi.nxt.sensor.SensorType;

/**
 * Acknowledgment message received from the brickpi containing
 * fresh values retrieved from the motors and sensors.
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class AckValuesMessage extends AckMessage {

    /**
     * Number of bits needed to encode the value of an encoder
     */
    private static final int NB_ENC_LENGTH_BITS = 5;

    /**
     * Number of bits needed to encode the ultrasonic sensor value
     */
    private static final int NB_ULTRASONIC_BITS = 8;

    /**
     * Number of bits needed to encode a raw sensor value
     */
    private static final int NB_RAW_OTHER_BITS = 10;

    /**
     * Number of bits needed to encode the touch sensor value
     */
    private static final int NB_TOUCH_BITS = 1;

    /**
     * Contains the length in number of bits on which the encoder
     * value is encoded for each motor 
     */
    private int[] motorEncoderLength = {0, 0};
    
    /**
     * Contains the value of the encoder for each motor
     */
    private int[] motorEncoderValue = {0, 0};

    /**
     * Contains the sensor type for each sensor
     */
    private SensorType[] sensorType = {null, null};
    
    /**
     * Contains the sensor values for each sensor
     */
    private Integer[] sensorValue = {null, null};

    /**
     * Contains the complete payload of the message
     */
    private byte[] payload;
    
    /**
     * Creates a new AckValuesMessage giving it a payload and 
     * the Atmel chip from which it originates
     * @param payloadBytes the payload in bytes
     * @param origin the Atmel from which it originates
     */
    public AckValuesMessage(byte[] payloadBytes, Atmel origin) {
        super(MessageType.MSG_TYPE_VALUES.toByte(), origin);
        
        // Keep the payload as it is needed by the parent class
        // but decoding is done by a bitset
        payload = payloadBytes;
        BitSet msgBits = BitSet.valueOf(payloadBytes);
        
        // Decode the encoders length needed to decode the encoders values
        // the length are encoded on a fixed number of bits: NB_ENC_LENGTH_BITS
        motorEncoderLength[0] = decodeBitsToInt(0, msgBits, NB_ENC_LENGTH_BITS);
        motorEncoderLength[1] = decodeBitsToInt(NB_ENC_LENGTH_BITS, msgBits, NB_ENC_LENGTH_BITS);
        
        // Decode the encoders values, the values is encoded on variable number
        // of bits decoded previously
        motorEncoderValue[0] = decodeBitsToInt(2 * NB_ENC_LENGTH_BITS, msgBits, motorEncoderLength[0]);
        motorEncoderValue[1] = decodeBitsToInt(2 * NB_ENC_LENGTH_BITS + motorEncoderLength[0], msgBits,
                motorEncoderLength[1]);
        
        // The type of the sensors is not included in the message itself and
        // need to be retrieved from the BrickPi configuration
        Sensor[] sensors = BrickPi.getSensors();
        sensorType[0] = sensors[origin.getIndex()].getType();
        sensorType[1] = sensors[origin.getIndex() + 1].getType();
        
        // Extract the value (a subset of the bits) for each sensor 
        // into sensorValue based on the sensor type provided
        int baseOffset = (NB_MOTORS_BY_ATMEGA * NB_ENC_LENGTH_BITS) 
                + motorEncoderLength[0] + motorEncoderLength[1];
        baseOffset = decodeSensorValue(baseOffset, msgBits, 0);
        decodeSensorValue(baseOffset, msgBits, 1);
    }
    
    /**
     * Get the value of the specified sensor
     * @param sensorNum sensor number to get the value from
     * @return the sensor value
     */
    public int getSensorValue(int sensorNum) {
        if (sensorNum < 0 || sensorNum >= NB_SENSORS_BY_ATMEGA) {
            throw new IllegalArgumentException("Bad device number, it should be between 0 and " + NB_SENSORS_BY_ATMEGA);
        }
        return sensorValue[sensorNum];
    }

    /**
     * Get the encoder value of the specified motor
     * @param motorNum motor number to get the value from
     * @return the motor encoder value
     */
    public int getMotorEncoderValue(int motorNum) {
        return motorEncoderValue[motorNum];
    }

    /**
     * Given a baseOffset in the specified BitSet and a sensor number, extract the sensor value
     * into sensorValue[deviceNum].
     * @param baseOffset the base offset to start decoding in the bitset
     * @param msgBits the bitset in which to decode the bits
     * @param sensorNum sensor number decoded
     * @return the new baseOffset
     */
    private int decodeSensorValue(int baseOffset, BitSet msgBits, int sensorNum) {
        int tmpBaseOffset = baseOffset;
        switch (sensorType[sensorNum]) {
        case TYPE_SENSOR_TOUCH:
            // One bit used 1 for pressed, 0 for not pressed
            sensorValue[sensorNum] = msgBits.get(tmpBaseOffset) ? 1 : 0;
            tmpBaseOffset += NB_TOUCH_BITS;
            break;
        case TYPE_SENSOR_ULTRASONIC_CONT:
            // Distance value in centimeters scaled by two on 8 bits
            sensorValue[sensorNum] = decodeBitsToInt(tmpBaseOffset, msgBits, NB_ULTRASONIC_BITS) / 2;
            tmpBaseOffset += NB_ULTRASONIC_BITS;
            break;
        case TYPE_SENSOR_RAW:
        case TYPE_SENSOR_LIGHT_ON:
        case TYPE_SENSOR_LIGHT_OFF:
        case TYPE_SENSOR_RCX_LIGHT:
        case TYPE_SENSOR_COLOR_RED:
        case TYPE_SENSOR_COLOR_GREEN:
        case TYPE_SENSOR_COLOR_BLUE:
        case TYPE_SENSOR_COLOR_NONE:
            // Raw value or analog sensor is 10 bits value
            sensorValue[sensorNum] = decodeBitsToInt(tmpBaseOffset, msgBits , NB_RAW_OTHER_BITS);
            tmpBaseOffset += NB_RAW_OTHER_BITS;
            break;
        default:
            throw new IllegalArgumentException("Sensor type is not supported");
        }
        return tmpBaseOffset;
    }

    /**
     * Decode the last nbBits bits of an integer in Least byte order from a bitset 
     * starting at a base offset and return the extracted value
     * @param baseOffset the base offset to start decoding in the bitset
     * @param msgBits the bitset in which to decode the bits
     * @param nbBits the number of bits starting from the least bit to encode
     * @return the value extracted as an int
     */
    private int decodeBitsToInt(int baseOffset, BitSet msgBits, int nbBits) {
        if (nbBits < 0 || nbBits > 32) {
            throw new IllegalArgumentException("Number of bits to extract must be between 0 and 32");
        }
        int intValue = 0;
        // Decoding the bits in LSB order
        for (int i = baseOffset + nbBits - 1; i >= baseOffset; i--) {
            intValue <<= 1;
            intValue = intValue | (msgBits.get(i)?1:0);
        }
        return intValue;
    }

    @Override
    public byte[] getPayload() {
        return payload;
    }
}
