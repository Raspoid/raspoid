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

import java.util.HashMap;
import java.util.Map;

/**
 * Enumerates all the message types used to communicate with the brickpi
 * along with their byte value.
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public enum MessageType {
    
    /**
     * Change Atmel address.
     */
    MSG_TYPE_CHANGE_ADDR((byte)1),
    
    /**
     * Sensor type.
     */
    MSG_TYPE_SENSOR_TYPE((byte)2),
    
    /**
     * Values.
     */
    MSG_TYPE_VALUES((byte)3),
    
    /**
     * Emergency stop.
     */
    MSG_TYPE_E_STOP((byte)4),
    
    /**
     * Timeout settings.
     */
    MSG_TYPE_TIMEOUT_SETTINGS((byte)5);

    /**
     * Contains the mapping between bytes values and MessageType values
     */
    private static Map<Byte, MessageType> mapping = new HashMap<>();

    /**
     * Contains the byte value of the message type
     */
    private final byte value;

    /**
     * Constructs the byte to MessageType mapping
     */
    static {
        for (MessageType msgType : MessageType.values()) {
            mapping.put(msgType.value, msgType);
        }
    }

    /**
     * Creates a MessageType with its byte value
     * @param value
     */
    private MessageType(byte value) {
        this.value = value;
    }

    /**
     * Converts the MessageType to its byte value
     * @return the byte type value of the MessageType
     */
    public byte toByte() {
        return value;
    }
    
    /**
     * Get the the MessageType corresponding to its byte value
     * @param type the byte type value
     * @return the corresponding MessageType value
     */
    public static MessageType valueOf(byte type) {
        if (!mapping.containsKey(type)) {
            throw new IllegalArgumentException("Message type is unknown");
        }
        return mapping.get(type);
    }

}
