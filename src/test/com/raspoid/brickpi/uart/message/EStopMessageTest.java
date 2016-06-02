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

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

import org.junit.Test;

import com.raspoid.brickpi.uart.message.EStopMessage;
import com.raspoid.brickpi.uart.message.MessageType;

/**
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class EStopMessageTest {

    @Test
    public void testGetType() {
        EStopMessage msg = new EStopMessage();
        assertThat(msg.getType(), equalTo(MessageType.MSG_TYPE_E_STOP.toByte()));
    }

    @Test
    public void testGetPayload() {
        EStopMessage msg = new EStopMessage();
        assertThat(msg.getPayload(), equalTo(new byte[] {}));
    }

}
