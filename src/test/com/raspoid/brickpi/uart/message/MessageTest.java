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

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class MessageTest {
    
    private List<Message> msgs = new ArrayList<>();
    
    @Before
    public void before() {
        msgs.add(new ChangeAddrMessage((byte) 4));
        msgs.add(new EStopMessage());
        msgs.add(new SensorTypeMessage(null, null));
        msgs.add(new ValuesMessage( () -> null, () -> null));
    }

    @Test
    public void testGetTotalBytesCount() {
        assertThat(msgs.get(0).getTotalBytesCount(), equalTo(2));
        assertThat(msgs.get(1).getTotalBytesCount(), equalTo(1));
        assertThat(msgs.get(2).getTotalBytesCount(), equalTo(3));
        assertThat(msgs.get(3).getTotalBytesCount(), equalTo(4));
        
        assertThat(msgs.get(0).getPayloadBytesCount(), equalTo(1));
        assertThat(msgs.get(1).getPayloadBytesCount(), equalTo(0));
        assertThat(msgs.get(2).getPayloadBytesCount(), equalTo(2));
        assertThat(msgs.get(3).getPayloadBytesCount(), equalTo(3));
    }

    @Test
    public void testGetTotalBytesSum() {
        assertThat(msgs.get(0).getTotalBytesSum(), equalTo(5));
        assertThat(msgs.get(1).getTotalBytesSum(), equalTo(4));
        assertThat(msgs.get(2).getTotalBytesSum(), equalTo(2));
        assertThat(msgs.get(3).getTotalBytesSum(), equalTo(3));
    }

}
