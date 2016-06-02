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

import com.raspoid.brickpi.uart.message.Message;

/**
 * Packet structure used to encapsulate/decapsulate 
 * a message to/from the its byte representation.
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class Packet {

    /**
     * Destination address for the message, or 0 for BROADCAST. This should not
     * be used if the packet was sent to the raspberry
     */
    private final byte destAddress;

    /**
     * Tells if the message is to send from the raspberry to the brickpi
     */
    private final boolean rpiToBrick;

    /**
     * Contains the message wrapped into the packet
     */
    private final Message message;

    /**
     * Constructor for BrickPi to RPi
     * @param message the message received from brick pi
     */
    public Packet(Message message) {
        this.message = message;
        this.destAddress = 0;
        this.rpiToBrick = false;
    }

    /**
     * Constructor for RPi to BrickPi
     * @param destAddress the destination address on the brick pi
     * @param message the message to send
     */
    public Packet(byte destAddress, Message message) {
        this.message = message;
        this.destAddress = destAddress;
        this.rpiToBrick = true;
    }

    /**
     * Get the destination address
     * @return the address byte value
     */
    public byte getDestAddress() {
        return destAddress;
    }

    /**
     * Get the message of the packet
     * @return the message in the packet
     */
    public Message getMessage() {
        return message;
    }

    /**
     * Get the number of bytes of the packet
     * @return the number of bytes of the packet.
     */
    public byte getBytesCount() {
        return (byte) (message.getTotalBytesCount() & 0xFF);
    }

    /**
     * Get the computed checksum of the packet
     * @return the packet checksum
     */
    public byte getChecksum() {
        return (byte) (destAddress + getBytesCount() + getMessage().getTotalBytesSum());
    }

    /**
     * Get the byte representation of the packet
     * @return the packet transformed into bytes
     */
    public byte[] toBytes() {
        /**
         * Size of the byte array is the message total size
         * plus two bytes for the checksum and byte counter
         * plus one byte if the packet is sent from the rpi to the brick
         */
        byte[] bytePacket = new byte[getMessage().getTotalBytesCount() + 2 + (rpiToBrick?1:0)];
        //When sent from rpi to the brick there is an address prefix
        int currentPos = 0;
        if (rpiToBrick) {
            bytePacket[currentPos] = destAddress;
            currentPos++;
        }
        //Put the checksum ,payload length and type
        bytePacket[currentPos] = getChecksum();
        currentPos++;
        bytePacket[currentPos] = (byte)(message.getTotalBytesCount()& 0xFF);
        currentPos++;
        bytePacket[currentPos] = message.getType();
        currentPos++;
        //Put the payload in the end
        System.arraycopy(getMessage().getPayload(), 0, bytePacket, currentPos, getMessage().getPayloadBytesCount());
        return bytePacket;
    }

}
