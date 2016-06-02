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

import com.raspoid.brickpi.Atmel;

/**
 * Acknowledgment message received from the brickpi
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class AckMessage implements Message {

    /**
     * Contains the Ack message type
     */
    protected final byte type;

    /**
     * Contains the Atmel chip the message 
     * originated from
     */
    private final Atmel origin;

    /**
     * Creates an AckMessage with its type and origin.
     * @param type the type of the ack message.
     * @param origin the atmel corresponding to the origin of the message.
     */
    public AckMessage(byte type, Atmel origin) {
        this.type = type;
        this.origin = origin;
    }

    /**
     * Get the atmel chip the ack message originated from
     * @return the atmel chip (with its address)
     */
    public Atmel getOrigin() {
        return this.origin;
    }

    @Override
    public byte getType() {
        return this.type;
    }

    @Override
    public byte[] getPayload() {
        return new byte[]{};
    }
}
