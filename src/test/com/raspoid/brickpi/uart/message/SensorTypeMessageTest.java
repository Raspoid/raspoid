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

import com.raspoid.brickpi.nxt.sensor.LightOffSensor;
import com.raspoid.brickpi.nxt.sensor.LightOnSensor;
import com.raspoid.brickpi.nxt.sensor.SensorType;
import com.raspoid.brickpi.nxt.sensor.TouchSensor;
import com.raspoid.brickpi.nxt.sensor.UltraSonicSensor;

/**
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class SensorTypeMessageTest {
    
    @Test
    public void testEmptySensorTypeMessage() {
        SensorTypeMessage msg = new SensorTypeMessage(null, null);
        assertThat(msg.getPayload(), equalTo(new byte[]{0,0}));
    }

    @Test
    public void testOnlyFirstSensor() {
        SensorTypeMessage msg = new SensorTypeMessage(new TouchSensor(), null);
        assertThat(msg.getPayloadBytesCount(), equalTo(2));
        assertThat(msg.getPayload(), equalTo(new byte[]{SensorType.TYPE_SENSOR_TOUCH.toByte(),0}));
    }
    
    @Test
    public void testOnlySecondSensor() {
        SensorTypeMessage msg = new SensorTypeMessage(null, new LightOnSensor());
        assertThat(msg.getPayloadBytesCount(), equalTo(2));
        assertThat(msg.getPayload(), equalTo(new byte[]{0, SensorType.TYPE_SENSOR_LIGHT_ON.toByte()}));
    }
    
    @Test
    public void testBothSensors() {
        SensorTypeMessage msg = new SensorTypeMessage(new LightOffSensor(), new TouchSensor());
        assertThat(msg.getPayloadBytesCount(), equalTo(2));
        assertThat(msg.getPayload(), equalTo(new byte[]{0, 32}));
        
        msg = new SensorTypeMessage(new TouchSensor(), new LightOffSensor());
        assertThat(msg.getPayloadBytesCount(), equalTo(2));
        assertThat(msg.getPayload(), equalTo(new byte[]{32, 0}));
    }
    
    @Test
    public void testUltrasonicSensor() {
        //One ultrasonic
        SensorTypeMessage msg = new SensorTypeMessage(null, new UltraSonicSensor());
        assertThat(msg.getPayloadBytesCount(), equalTo(7));
        assertThat(msg.getPayload(), equalTo(new byte[]{0, SensorType.TYPE_SENSOR_I2C.toByte(), 10, 8, 28, 33, 4}));
    }
    
    @Test
    public void testI2cEncoding() {
        SensorTypeMessage msg = new SensorTypeMessage(new UltraSonicSensor(), new UltraSonicSensor());
        assertThat(msg.getPayloadBytesCount(), equalTo(11));
        assertThat(msg.getPayload(), equalTo(new byte[]{41, SensorType.TYPE_SENSOR_I2C.toByte(), 10, 8, 28, 33, -92, -128, -64, 17, 66}));
    }
    
    @Test
    public void testGetType() {
        SensorTypeMessage msg = new SensorTypeMessage(null, null);
        assertThat(msg.getType(), equalTo(MessageType.MSG_TYPE_SENSOR_TYPE.toByte()));
    }

}
