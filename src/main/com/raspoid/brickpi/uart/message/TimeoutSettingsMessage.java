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
 * Message for setting the default timeout after which the
 * motors will be stopped by the brickpi. When the brickpi
 * does not receive any update values messages during this 
 * timeout, it floats the motors.
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class TimeoutSettingsMessage implements Message {

    /**
     * The default timeout to be set
     */
    private final long timeout;

    /**
     * Creates a new timeout message
     * @param timeout the default timeout to be set
     */
    public TimeoutSettingsMessage(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public byte getType() {
        return MessageType.MSG_TYPE_TIMEOUT_SETTINGS.toByte();
    }

    @Override
    public byte[] getPayload() {
        return new byte[]{(byte)(timeout & 0xFF), (byte)((timeout >> 8) & 0xFF),
        (byte)((timeout >> 16) & 0xFF), (byte)((timeout >> 24) & 0xFF)};
    }

}
