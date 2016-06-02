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
package com.raspoid.brickpi.uart;

import java.util.AbstractMap;
import java.util.Map;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.SerialPortException;
import com.raspoid.Tools;
import com.raspoid.brickpi.Atmel;
import com.raspoid.exceptions.RaspoidSerialException;

/**
 * Transmitter used to send and receive bytes between the pi and brickpi over the serial port.
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class BrickPiSerialTransmitter {

    /**
     * Baud rate used with the serial
     */
    public static final int BAUD_RATE = 500000;
    
    /**
     * Contains the rate in milliseconds at which the serial is monitored
     * to detect a change in its buffer
     */
    public static final int SERIAL_MONITOR_RATE = 5;
    
    /**
     * Contains the inter send message delay the transmitter waits for
     * in milliseconds. This delay is a bit longer than the monitor rate
     */
    public static final int SERIAL_MONITOR_DELAY = SERIAL_MONITOR_RATE + 3;

    /**
     * Serial communication instance
     */
    private final Serial serial;

    /**
     * Timeout after which a retry to communicate is done
     */
    private final int timeout;

    /**
     * Construct the BrickPiSerialTransmitter with blocking queues for messaging
     * @param timeout The timeout in millisecond to wait before retrying to send and receive data
     * @throws RaspoidSerialException in case of trouble when opening serial communications with the brick pi
     */
    public BrickPiSerialTransmitter(int timeout) {
        this.timeout = timeout;
        try {
            serial = SerialFactory.createInstance();
            serial.open(Serial.DEFAULT_COM_PORT, BAUD_RATE);
            serial.setMonitorInterval(SERIAL_MONITOR_RATE);
        } catch (SerialPortException e) {
            throw new RaspoidSerialException("The transmitter was unable to open the serial channel", e);
        }
    }
    
    /**
     * Send a message to the BrickPi then waits for the reponse message
     * @param bytesChunkToWrite the bytes of the message to write
     * @return an entry with the response message and the Atmel it came from
     */
    public synchronized Map.Entry<Atmel, byte[]> sendMessage(Map.Entry<Atmel, byte[]> bytesChunkToSend) {
        byte[] bytesChunkRead = null;
        // Send a packet to the brick pi, with a retry after timeout ms,
        // until a response is received from the brick pi
        while (bytesChunkRead == null) {
            writeToBrickPi(bytesChunkToSend.getValue());
            // Sleep a bit more than the rate at which the serial
            // is regularly checked for new content
            Tools.sleepMilliseconds(SERIAL_MONITOR_DELAY);
            bytesChunkRead = readFromBrickPi();

            // In case a packet is lost, wait for timeout
            // before trying again
            if (bytesChunkRead.length == 0) {
                Tools.sleepMilliseconds(timeout);
            }
        }
        Atmel from = bytesChunkToSend.getKey();
        return new AbstractMap.SimpleEntry<>(from, bytesChunkRead);
    }

    /**
     * Send a packet to the brick pi.
     * This method is synchronized to prevent multiple threads from attempting to use
     * the serial interface simultaneously.
     * @param bytesChunkToWrite
     */
    private void writeToBrickPi(byte[] bytesChunkToWrite) {
        // clear the read buffer before writing...
        serial.flush();
        serial.write(bytesChunkToWrite);
    }

    /**
     * Read a packet from the brick pi.
     * @return a byte chunk corresponding to a packet
     */
    private byte[] readFromBrickPi() {
        // the first byte of the received packet is the checksum.
        if (serial.availableBytes() == 0) {
            return new byte[] {};
        }
        Byte checksum = (byte) serial.read();
        // the second byte is the number of bytes in the packet content.
        if (serial.availableBytes() == 0) {
            return new byte[] {};
        }
        Byte packetSize = (byte) serial.read();
        
        // this a workaround to handle a bug in the firmware that occurs when
        // a packet is received with this weird size, causing the communication to break
        if (packetSize == 104) {
            packetSize = 10;
        }

        // so, we have packetSize bytes left to read.
        // the packet size does not include the two bytes of header.
        if (packetSize < 0) {
            // packetSize is corrupted
            return new byte[] {};
        }
        byte[] receivedData = new byte[packetSize + 2];
        receivedData[0] = checksum;
        receivedData[1] = packetSize;
        for (int i = 0; i < packetSize; i++) {
            if (serial.availableBytes() < 0) {
                return new byte[] {};
            }
            receivedData[i + 2] = (byte) (serial.read() & 0xFF);
        }
        return receivedData;
    }

}
