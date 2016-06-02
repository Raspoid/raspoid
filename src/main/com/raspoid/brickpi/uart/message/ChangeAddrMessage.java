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
 * Message that can be send to change the address of an
 * atmel chip on the brickpi. For the operation to succeed,
 * the touch sensor need to be connected to the first port of the
 * atmel chip and pressed while sending this message with the new address
 * to be assigned. This allows to stack multiple brickpi on the raspberry pi.
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class ChangeAddrMessage implements Message {

    /**
     * Contains the new address to be assigned to the amtel chip
     */
    private final byte newAddress;

    /**
     * Creates a new change address message
     * @param newAddress the new address to be assigned
     */
    public ChangeAddrMessage(byte newAddress) {
        this.newAddress = newAddress;
    }

    @Override
    public byte getType() {
        return MessageType.MSG_TYPE_CHANGE_ADDR.toByte();
    }

    @Override
    public byte[] getPayload() {
        return new byte[]{newAddress};
    }
}
