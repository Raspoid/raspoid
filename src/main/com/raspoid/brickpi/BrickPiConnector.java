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
package com.raspoid.brickpi;

import static com.raspoid.brickpi.Atmel.CHIP1;
import static com.raspoid.brickpi.Atmel.CHIP2;
import static com.raspoid.brickpi.BrickPi.NB_MOTORS_BY_ATMEGA;
import static com.raspoid.brickpi.BrickPi.NB_SENSORS_BY_ATMEGA;

import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.raspoid.brickpi.uart.BrickPiSerialTransmitter;
import com.raspoid.brickpi.uart.PacketFormatter;
import com.raspoid.brickpi.uart.message.AckMessage;
import com.raspoid.brickpi.uart.message.AckValuesMessage;
import com.raspoid.brickpi.uart.message.Message;
import com.raspoid.brickpi.uart.message.SensorTypeMessage;
import com.raspoid.brickpi.uart.message.TimeoutSettingsMessage;
import com.raspoid.brickpi.uart.message.ValuesMessage;

/**
 * BrickPi connector implementing the message exchanges between the
 * raspberry pi and the BrickPi. It is implementing the init phase,
 * the regular polling of new values and the update of the local BrickPi state. 
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class BrickPiConnector {
    
    /**
     * How frequently send valuesMessages to get updated values from the brick pi.
     * This is in milliseconds.
     */
    public static final int DEFAULT_DELAY = 20;

    /**
     * Contains the timeout after which we assume the response was lost
     */
    public static final int RECV_TIMEOUT = 25;

    /**
     * Timeout to shutdown motors.
     * When the brick pi doesn't receive information request
     * within the last MOTOR_TIMEOUT ms, the motors are stopped.
     */
    public static final int MOTOR_TIMEOUT = 100;
    
    /**
     * Single thread pool used for sending messages over the serial link
     */
    private ScheduledExecutorService serialExecutor = Executors.newSingleThreadScheduledExecutor();
    
    /**
     * Single thread pool used for updating the local state of the BrickPi
     */
    private ExecutorService notifyExecutor = Executors.newSingleThreadExecutor();
    
    /**
     * Serial transmitter used for exchanging messages with the BrickPi
     */
    private BrickPiSerialTransmitter transmitter = new BrickPiSerialTransmitter(RECV_TIMEOUT);
    
    /**
     * Values message destined for the 1st Atmel
     */
    private ValuesMessage valuesMessage1;
    
    /**
     * Values message destined for the 2nd Atmel
     */
    private ValuesMessage valuesMessage2;

    /**
     * Starts the execution of the BrickPi
     */
    public void start() {
        
        // Create the values message with the motors configuration
        // Each time the motors are needed, we retrieve them through a supplier
        // from the BrickPi class. This is because we need to ensure there is no
        // lock currently set because we could be in the middle of a transaction
        valuesMessage1 = new ValuesMessage(() -> BrickPi.MA, () -> BrickPi.MB);
        valuesMessage2 = new ValuesMessage(() -> BrickPi.MC, () -> BrickPi.MD);

        // Set the timeout to shutdown the motors
        TimeoutSettingsMessage timeoutMsg = new TimeoutSettingsMessage(MOTOR_TIMEOUT);
        sendMessage(CHIP1, timeoutMsg);
        sendMessage(CHIP2, timeoutMsg);

        // Configure the sensors
        // We send one packet to each AtMega to configure the corresponding sensors.
        Sensor[] sensors = BrickPi.getSensors();
        for(int atmelIndex = 0; atmelIndex < BrickPi.NB_ATMEGA_TARGETS; atmelIndex++) {
            sendMessage(Atmel.valueOf((byte) (atmelIndex + 1)),
                    new SensorTypeMessage(sensors[atmelIndex * BrickPi.NB_ATMEGA_TARGETS],
                            sensors[atmelIndex * BrickPi.NB_ATMEGA_TARGETS + 1]));
        }
        
        // Regularly send the values message to the BrickPi
        // This will update the BrickPi motor speed if it was changed locally
        // It will refresh the local values for motor encoders and sensors values
        serialExecutor.scheduleAtFixedRate(() -> {
            sendThenNotify(CHIP1, valuesMessage1);
            sendThenNotify(CHIP2, valuesMessage2);
        } , 0, DEFAULT_DELAY, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Stops the execution of the BrickPi
     */
    public void stop() {
        // Flush so that the last values set are transmitted
        flush();
        serialExecutor.shutdown();
        notifyExecutor.shutdown();
    }
    
    /**
     * Send a message over the serial transmitter, then notify using the notify executor
     * @param chip the chip on which to send the message
     * @param msg the message to be send
     */
    private void sendThenNotify(Atmel chip, Message msg) {
        notifyExecutor.submit(() -> 
            updateBrickPi(transmitter.sendMessage(new AbstractMap.SimpleEntry<>(chip, PacketFormatter.encode(chip, msg))))
        );
    }

    /**
     * Send a message to the BrickPi using the serial executor to send the message
     * and the notify executor to update the BrickPi state accordingly
     * @param chip the chip on which to send the message
     * @param msg the message to be send
     */
    private void sendMessage(Atmel chip, Message msg) {
        serialExecutor.submit(() -> sendThenNotify(chip, msg));
    }
    
    /**
     * Immediately send the values message to update the BrickPi
     * with the values of the local state
     */
    public void flush() {
        sendMessage(CHIP1, valuesMessage1);
        sendMessage(CHIP2, valuesMessage2);
    }
    
    /**
     * Update the local state of the BrickPi with the values received from the BrickPi
     * @param byteChunksRead the byte chunk read along with the Atmel it was received from
     */
    private void updateBrickPi(Map.Entry<Atmel, byte[]> byteChunksRead) {
        AckMessage ackMsg = PacketFormatter.decode(byteChunksRead);
        if (ackMsg instanceof AckValuesMessage) {
            AckValuesMessage ackValMsg = (AckValuesMessage) ackMsg;
            int chipIndex = ackMsg.getOrigin().getIndex();

            /**
             * Update the motors encoders 1 Atmel chip, that is 2 motors.
             */
            for (int deviceNum = 0; deviceNum < NB_MOTORS_BY_ATMEGA; deviceNum++) {
                int deviceIndex = chipIndex + deviceNum;

                // Update the motor encoder
                Motor currentMotor = BrickPi.getMotors()[deviceIndex];
                if (currentMotor != null) {
                    currentMotor.setEncoderValue(ackValMsg.getMotorEncoderValue(deviceNum));
                }
            }

            /**
             * Update the sensors values of 1 Atmel chip, that is 2 sensors.
             */
            for (int deviceNum = 0; deviceNum < NB_SENSORS_BY_ATMEGA; deviceNum++) {
                int deviceIndex = chipIndex + deviceNum;

                // Update the sensor value
                BrickPi.getSensors()[deviceIndex].setValue(ackValMsg.getSensorValue(deviceNum));
            }
        }
    }
}
