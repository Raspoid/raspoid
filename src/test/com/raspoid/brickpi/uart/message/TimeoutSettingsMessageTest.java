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
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.raspoid.brickpi.uart.message.MessageType;
import com.raspoid.brickpi.uart.message.TimeoutSettingsMessage;

/**
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class TimeoutSettingsMessageTest {

    @Test
    public void testZeroTimeout() {
        TimeoutSettingsMessage msg = new TimeoutSettingsMessage(0);
        assertThat(msg.getPayloadBytesCount(), equalTo(4));
        assertThat(msg.getPayload(), equalTo(new byte[] {0, 0, 0, 0}));
    }
    
    @Test
    public void testTimoutValues() {
        TimeoutSettingsMessage msg = new TimeoutSettingsMessage(5);
        assertThat(msg.getPayloadBytesCount(), equalTo(4));
        assertThat(msg.getPayload(), equalTo(new byte[] {5, 0, 0, 0}));
        
        msg = new TimeoutSettingsMessage(256);
        assertThat(msg.getPayloadBytesCount(), equalTo(4));
        assertThat(msg.getPayload(), equalTo(new byte[] {0, 1, 0, 0}));
        
        msg = new TimeoutSettingsMessage(65536);
        assertThat(msg.getPayloadBytesCount(), equalTo(4));
        assertThat(msg.getPayload(), equalTo(new byte[] {0, 0, 1, 0}));
        
        msg = new TimeoutSettingsMessage(16777216);
        assertThat(msg.getPayloadBytesCount(), equalTo(4));
        assertThat(msg.getPayload(), equalTo(new byte[] {0, 0, 0, 1}));
        
        msg = new TimeoutSettingsMessage(871487468);
        assertThat(msg.getPayloadBytesCount(), equalTo(4)); 
        assertThat(msg.getPayload(), equalTo(new byte[] {-20, -41, -15, 51}));
        
        msg = new TimeoutSettingsMessage(547);
        assertThat(msg.getPayloadBytesCount(), equalTo(4));
        assertThat(msg.getPayload(), equalTo(new byte[] {35, 2, 0, 0}));
        
        msg = new TimeoutSettingsMessage(1000000);
        assertThat(msg.getPayloadBytesCount(), equalTo(4));
        assertThat(msg.getPayload(), equalTo(new byte[] {64, 66, 15, 0}));
        
        msg = new TimeoutSettingsMessage(46982);
        assertThat(msg.getPayloadBytesCount(), equalTo(4));
        assertThat(msg.getPayload(), equalTo(new byte[] {-122, -73, 0, 0}));
    }
    
    @Test
    public void testGetType() {
        TimeoutSettingsMessage msg = new TimeoutSettingsMessage(0);
        assertThat(msg.getType(), equalTo(MessageType.MSG_TYPE_TIMEOUT_SETTINGS.toByte()));
    }

}
