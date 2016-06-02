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

/**
 * Message contained in a packet.
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public interface Message {

    /**
     * Get the type value of the message
     * @return the type value
     */
    public byte getType();

    /**
     * Get the message payload excluding the type
     * @return the message payload
     */
    public byte[] getPayload();

    /**
     * Get the total count of message payload bytes plus type byte
     * @return the total bytes count
     */
    public default int getTotalBytesCount() {
        //length is the message length + 1 for the type byte
        return getPayload().length + 1;
    }

    /**
     * Get the count of message payload bytes
     * @return the payload bytes count
     */
    public default int getPayloadBytesCount() {
        return getPayload().length;
    }

    /**
     * Get the summation of the bytes values of the payload
     * @return the bytes sum of the payload
     */
    public default int getTotalBytesSum() {
        int checksum = getType();
        for (byte toAdd : getPayload()) {
            checksum += toAdd;
        }
        return checksum;
    }
}
