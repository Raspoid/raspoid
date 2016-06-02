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

import java.util.BitSet;

import org.junit.Before;
import org.junit.Test;

import com.raspoid.brickpi.Motor;
import com.raspoid.brickpi.uart.message.MessageType;
import com.raspoid.brickpi.uart.message.ValuesMessage;

/**
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class ValuesMessageTest {
    
    private Motor motor1;
    
    private Motor motor2;
    
    @Before
    public void before() {
        motor1 = new Motor();
        motor2 = new Motor();
    }

    @Test
    public void testEmptyValuesMessage() {
        ValuesMessage msg = new ValuesMessage( () -> null, () -> null);
        byte[] payload = msg.getPayload();
        assertThat(payload, equalTo(new byte[] {0,0,0}));
    }
    
    @Test
    public void testOnlyFirstMotor() {
        motor1.setPower(255);
        ValuesMessage msg = new ValuesMessage( () -> motor1, () -> null);
        BitSet payload = BitSet.valueOf(msg.getPayload());
        //false bits for encoder offsets
        assertThat(payload.get(0), equalTo(false));
        assertThat(payload.get(1), equalTo(false));
        
        //enable bit
        assertThat(payload.get(2), equalTo(true));
        
        //dir bit
        assertThat(payload.get(3), equalTo(false));
        
        //true bits for second motor
        for (int i = 4; i < 8; i++) {
            assertThat(payload.get(i), equalTo(true));
        }
        
        //false bits for second motor
        for (int i = 12; i < 10; i++) {
            assertThat(payload.get(i), equalTo(false));
        }
    }
    
    @Test
    public void testOnlySecondMotor() {
        motor2.setPower(-255);
        ValuesMessage msg = new ValuesMessage( () -> null, () -> motor2);
        BitSet payload = BitSet.valueOf(msg.getPayload());
        
        //false bits for encoder offsets
        assertThat(payload.get(0), equalTo(false));
        assertThat(payload.get(1), equalTo(false));
        
        //false bits for first motor
        for (int i = 2; i < 10; i++) {
            assertThat(payload.get(i), equalTo(false));
        }
        
        //enable bit
        assertThat(payload.get(12), equalTo(true));
        
        //dir bit
        assertThat(payload.get(13), equalTo(true));
        
        //false bits for second motor
        for (int i = 14; i < 8; i++) {
            assertThat(payload.get(i), equalTo(true));
        }
    }
    
    @Test
    public void testBothMotors() {
        motor1.setPower(-255);
        motor2.setPower(255);
        ValuesMessage msg = new ValuesMessage( () -> motor1, () -> motor2);
        BitSet payload = BitSet.valueOf(msg.getPayload());
        
        //false bits for encoder offsets
        assertThat(payload.get(0), equalTo(false));
        assertThat(payload.get(1), equalTo(false));
        
        // enable bit
        assertThat(payload.get(2), equalTo(true));

        // dir bit
        assertThat(payload.get(3), equalTo(true));

        // true bits for second motor
        for (int i = 4; i < 8; i++) {
            assertThat(payload.get(i), equalTo(true));
        }
        
        // enable bit
        assertThat(payload.get(12), equalTo(true));

        // dir bit
        assertThat(payload.get(13), equalTo(false));

        // false bits for second motor
        for (int i = 14; i < 8; i++) {
            assertThat(payload.get(i), equalTo(true));
        }
    }
    
    @Test
    public void testPowerEncoding() {
        ValuesMessage msg = new ValuesMessage( () -> motor1, () -> motor2);
        
        motor1.setPower(1);
        BitSet payload = BitSet.valueOf(msg.getPayload());
        
        assertThat(payload.get(2), equalTo(true));
        assertThat(payload.get(3), equalTo(false));
        assertThat(payload.get(4), equalTo(true));
        for (int i = 5; i < 7; i++) {
            assertThat(payload.get(i), equalTo(false));
        }
        
        motor2.setPower(-1);
        payload = BitSet.valueOf(msg.getPayload());
        
        assertThat(payload.get(12), equalTo(true));
        assertThat(payload.get(13), equalTo(true));
        assertThat(payload.get(14), equalTo(true));
        for (int i = 15; i < 7; i++) {
            assertThat(payload.get(i), equalTo(false));
        }
        
        motor1.setPower(47);
        payload = BitSet.valueOf(msg.getPayload());
        
        assertThat(payload.get(2), equalTo(true));
        assertThat(payload.get(3), equalTo(false));
        
        assertThat(payload.get(4), equalTo(true));
        assertThat(payload.get(5), equalTo(true));
        assertThat(payload.get(6), equalTo(true));
        assertThat(payload.get(7), equalTo(true));
        assertThat(payload.get(8), equalTo(false));
        assertThat(payload.get(9), equalTo(true));
        assertThat(payload.get(10), equalTo(false));
        assertThat(payload.get(11), equalTo(false));
        
        motor2.setPower(-238);
        payload = BitSet.valueOf(msg.getPayload());
        
        assertThat(payload.get(12), equalTo(true));
        assertThat(payload.get(13), equalTo(true));
        
        assertThat(payload.get(14), equalTo(false));
        assertThat(payload.get(15), equalTo(true));
        assertThat(payload.get(16), equalTo(true));
        assertThat(payload.get(17), equalTo(true));
        assertThat(payload.get(18), equalTo(false));
        assertThat(payload.get(19), equalTo(true));
        assertThat(payload.get(20), equalTo(true));
        assertThat(payload.get(21), equalTo(true));
    }
    
    @Test
    public void testPayloads() {
        ValuesMessage msg = new ValuesMessage( () -> motor1, () -> motor2);
        
        motor1.setPower(231);
        motor2.setPower(174);
        assertThat(msg.getPayloadBytesCount(), equalTo(3));
        assertThat(msg.getPayload(), equalTo(new byte[] {116, -98, 43}));
        
        motor1.setPower(-31);
        motor2.setPower(34);
        assertThat(msg.getPayloadBytesCount(), equalTo(3));
        assertThat(msg.getPayload(), equalTo(new byte[] {-4, -111, 8}));
        
        motor1.setPower(-58);
        motor2.setPower(-214);
        assertThat(msg.getPayloadBytesCount(), equalTo(3));
        assertThat(msg.getPayload(), equalTo(new byte[] {-84, -77, 53}));
        
        motor1.setPower(34);
        motor2.setPower(-211);
        assertThat(msg.getPayloadBytesCount(), equalTo(3));
        assertThat(msg.getPayload(), equalTo(new byte[] {36, -14, 52}));
        
        motor1.setPower(111);
        motor2.setPower(-56);
        assertThat(msg.getPayloadBytesCount(), equalTo(3));
        assertThat(msg.getPayload(), equalTo(new byte[] {-12, 54, 14}));
    }

    @Test
    public void testGetType() {
        ValuesMessage msg = new ValuesMessage( () -> motor1, () -> motor2);
        assertThat(msg.getType(), equalTo(MessageType.MSG_TYPE_VALUES.toByte()));
    }

}
