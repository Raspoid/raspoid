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

import java.util.BitSet;
import java.util.function.Supplier;

import com.raspoid.brickpi.Motor;

/**
 * Values message sent from the Rpi to the brickPi to get
 * some fresh values from it.
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class ValuesMessage implements Message {

    /**
     * Contains the number of bits to encode the motor speed
     */
    private static final int NB_MOTOR_BITS = 10;

    /**
     * Contains the number of reserved bits in the message.
     * This can be used to set some custom encoder offset
     * but it's not currently supported by the firmware
     */
    private static final int NB_RESERVED_BITS = 2;

    /**
     * Contains an access to the motor 1
     */
    private Supplier<Motor> motor1;

    /**
     * Contains an access to the motor 2
     */
    private Supplier<Motor> motor2;
    
    /**
     * Constructs a ValueMessage for two motors retrieved through suppliers
     * @param motor1 the motor1 supplier
     * @param motor2 the motor2 supplier
     */
    public ValuesMessage(Supplier<Motor> motor1, Supplier<Motor> motor2) {
        this.motor1 = motor1;
        this.motor2 = motor2;
    }

    @Override
    public byte getType() {
        return MessageType.MSG_TYPE_VALUES.toByte();
    }

    @Override
    public byte[] getPayload() {
        //The two first bits are reserved for encoder offset
        //These are not currently used by the firmware and are set to 0
        BitSet msgBits = new BitSet(NB_RESERVED_BITS + 2 * NB_MOTOR_BITS);
        if (motor1.get() != null) {
            encodeMotor(NB_RESERVED_BITS , msgBits, motor1.get());
        }
        if (motor2.get() != null) {
            encodeMotor(NB_RESERVED_BITS + NB_MOTOR_BITS, msgBits, motor2.get());
        }
        //2 + 2 * 10 = 22 bits needed it fits in 3 bytes
        //trailing zeroes are discarded with bitset so we need
        //this hack in case there are only zeroes in the bitset
        byte[] payload = new byte[3];
        byte[] bits = msgBits.toByteArray();
        System.arraycopy(bits, 0, payload, 0, bits.length);
        return payload;
    }

    /**
     * Encode the bits needed for a motor into the message bitset at the specified offset
     * @param baseOffset the baseOffset to start encoding the motor into the bitSet
     * @param msgBits the bitset of the message
     * @param motor the motor to be encoded
     */
    private void encodeMotor(int baseOffset, BitSet msgBits, Motor motor) {
        //Enable the motor
        msgBits.set(baseOffset);
        //Set direction bit
        msgBits.set(baseOffset + 1, motor.getPower() < 0);
        //Encode the speed in LSB
        int motorSpeed = Math.abs(motor.getPower());
        for (int i = 0; i < Byte.SIZE; i++) {
            msgBits.set(baseOffset + i + 2, ((motorSpeed >> i) & 0x1) == 1);
        }
    }
}
