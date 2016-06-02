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
package com.raspoid.brickpi.uart;

import static com.raspoid.brickpi.uart.message.MessageType.MSG_TYPE_VALUES;

import java.util.Map.Entry;

import com.raspoid.brickpi.Atmel;
import com.raspoid.brickpi.uart.message.AckMessage;
import com.raspoid.brickpi.uart.message.AckValuesMessage;
import com.raspoid.brickpi.uart.message.Message;
import com.raspoid.exceptions.RaspoidPacketFormatException;

/**
 * Packet formatter utility class allowing to encode messages into bytes
 * and decode bytes to messages.
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class PacketFormatter {
    
    /**
     * Constructor is private, this is a utility class
     */
    private PacketFormatter() {}

    /**
     * Encodes a message into bytes provided with the Atmel chip and the message
     * @param chip the atmel chip
     * @param message the message to be encoded
     * @return a byte array with the encoded message
     */
    public static byte[] encode(Atmel chip, Message message) {
        return new Packet(chip.getAddress(), message).toBytes();
    }

    /**
     * Decodes a pair value containing an Atmel chip and a byte array into an AckMessage
     * @param recvPacket the received pair value with the atmel chip and byte array
     * @return an AckMessage that was decoded
     */
    public static AckMessage decode(Entry<Atmel, byte[]> recvPacket) {
        byte[] bytesPacket = recvPacket.getValue();
        // Check if at least the entire header was received
        if (bytesPacket.length < 2) {
            throw new RaspoidPacketFormatException("The header was not received properly");
        }
        byte receivedChecksum = bytesPacket[0];
        byte byteCount = bytesPacket[1];

        // Check the message length
        if (byteCount != bytesPacket.length - 2) {
            throw new RaspoidPacketFormatException("The length of the received message does not match the declared length");
        }

        AckMessage message;
        byte byteMsgType = bytesPacket[2];

        // If payload is empty, it is a ack message
        if (byteCount == 1) {
            message = new AckMessage(byteMsgType, recvPacket.getKey());
        } // Otherwhise it can only be a values message
        else if (byteMsgType == MSG_TYPE_VALUES.toByte()) {
            byte[] messagePayload = new byte[byteCount - 1];
            System.arraycopy(bytesPacket, 3, messagePayload, 0, byteCount - 1);
            message = new AckValuesMessage(messagePayload, recvPacket.getKey());
        } else {
            throw new RaspoidPacketFormatException("The packet type is erroneous (expected Ack packet or Values packet)");
        }

        //Create the packet to validate the checksum
        Packet packet = new Packet(message);
        if (receivedChecksum != packet.getChecksum()) {
            throw new RaspoidPacketFormatException("The packet checksum is erroneous (expected: " + packet.getChecksum() + ", received: " + receivedChecksum + ")");
        }

        return message;
    }
}
