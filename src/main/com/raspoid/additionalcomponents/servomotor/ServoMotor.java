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
package com.raspoid.additionalcomponents.servomotor;

import com.raspoid.PWMComponent;
import com.raspoid.PWMPin;
import com.raspoid.additionalcomponents.PCA9685;
import com.raspoid.additionalcomponents.PCA9685.PCA9685Channel;

/**
 * This abstract class represents the implementation of a servo motor.
 * 
 * <p>All you need to do to implement a new type of servo is to extend this class 
 * with the parameters corresponding to your servo.</p>
 * 
 * <p>You can use the ServoMotorCalibration class to easily determine 
 * the values of the different parameters.</p>
 * 
 * <p>To rotate the servo, we use the Pulse Width Modulation technique.
 * Pulse Width Modulation (or PWM) is a technique for controlling power.
 * We use it here to control the length of the pulse going to the servo and hence the position of the rotor.</p>
 * 
 * <p>There is only twos pins on the recent Pi that are capable of producing pulses in this way.
 * But we also implemented the driver to easily use a PCA9685, capable of producing up to 16 independent PWM signals.</p>
 * 
 * <p>It is strongly recommended that the power to the servo is provided by an external battery
 * as powering the servo from the Pi itself is likely to cause it to crash as the servo draws too much current as it starts to move.
 * Servos require ~4.8-6V DC power to the motor, but the signal level (pulse output)
 * can be 3.3V, which is how its OK to just connect the signal line directly to the GPIO output of the Pi.</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public abstract class ServoMotor extends PWMComponent {
    
    /**
     * Each PWM period must be 20ms long.
     */
    public static final int TARGET_FREQUENCY = 50; // Hz
    
    /**
     * Range register in the PWM generator.
     * <p>The values sent to the generator must be in the [0; PWM_RANGE_GENERATOR] range.</p>
     */
    public static final int PWM_RANGE_GENERATOR = 20000; // μs (20ms)
    
    /**
     * The minimum time required to reach a new position.
     */
    private static final int MIN_TIME_TO_REACH_POSITION = 10; // ms
    
    /**
     * The number of μs of pulse length for a rotation of one degree.
     */
    private double pulseLengthUsPerDegreeOfRotation; // μs/°
    
    /**
     * The time needed for a rotation of one degree.
     */
    private double timeForARotationOfOneDegree; // ms
    
    /**
     * Pulse length corresponding to the minAngle position of the rotor.
     */
    private int minPulseLength; // μs
    
    /**
     * Pulse length corresponding to the maxAngle position of the rotor.
     */
    private int maxPulseLength; // μs
    
    /**
     * Pulse length corresponding to the current position of the rotor.
     */
    private int currentPulseLength; // μs
    
    /**
     * The minimum angle available for the rotor of this servo motor.
     */
    private double minAngle; // °
    
    /**
     * The maximum angle available for the rotor of this servo motor.
     */
    private double maxAngle; // °
    
    /**
     * Value used to multiply the computed time needed to reach a position.
     * <p>Sometimes, allows to reach a position with a better accuracy,
     * but implies a bigger jitter between position changes.</p>
     */
    private double multiplicatorForTimeToReachPosition;
    
    /**
     * Creates a new instance of a servo motor, with the corresponding parameters,
     * using a PCA9685 module to generate PWM signals.
     * 
     * <p>Note: the ServoMotorCalibration class can be used to easily determine the usefull values.</p>
     * 
     * <p>Example:
     *  <ul>
     *      <li>0° corresponds to a pulse of 0.7ms length.</li>
     *      <li>180° corresponds to a puls of 2.8ms length.</li>
     *  </ul>
     * </p>
     * 
     * @param pca9685 the pca9685 module used to generate PWM signals.
     * @param channel the channel, on the pca9685, used to generate PWM signals.
     * @param minAngle the minimum angle available for the rotor of this servo motor.
     * @param minPulseLength the pulse length in ms corresponding to the minAngle position of the rotor.
     * @param maxAngle the maximum angle available for the rotor of this servo motor.
     * @param maxPulseLength the pulse length in ms corresponding to the maxAngle position of the rotor.
     * @param timeToRotate60Degrees the time needed in seconds to execute a rotation of 60° [cfr datasheets].
     * @param multiplicatorForTimeToReachPosition the value used to multiply the computed time needed to reach a position.
     */
    public ServoMotor(PCA9685 pca9685, PCA9685Channel channel, double minAngle, double minPulseLength, double maxAngle, double maxPulseLength, double timeToRotate60Degrees, double multiplicatorForTimeToReachPosition) {
        super(pca9685, channel, PWM_RANGE_GENERATOR);
        init(minAngle, minPulseLength, maxAngle, maxPulseLength, timeToRotate60Degrees, multiplicatorForTimeToReachPosition);
    }
    
    /**
     * Creates a new instance of a servo motor, with the corresponding parameters,
     * using a Raspberry Pi PWM pin to generate PWM signals.
     * 
     * <p>Note: the ServoMotorCalibration class can be used to easily determine the usefull values.</p>
     * 
     * <p>Example:
     *  <ul>
     *      <li>0° corresponds to a pulse of 0.7ms length.</li>
     *      <li>180° corresponds to a puls of 2.8ms length.</li>
     *  </ul>
     * </p>
     * 
     * @param pin the PWMPin where the servo is attached.
     * @param minAngle the minimum angle available for the rotor of this servo motor.
     * @param minPulseLength the pulse length in ms corresponding to the minAngle position of the rotor.
     * @param maxAngle the maximum angle available for the rotor of this servo motor.
     * @param maxPulseLength the pulse length in ms corresponding to the maxAngle position of the rotor.
     * @param timeToRotate60Degrees the time needed in seconds to execute a rotation of 60° [cfr datasheets].
     * @param multiplicatorForTimeToReachPosition the value used to multiply the computed time needed to reach a position.
     */
    public ServoMotor(PWMPin pin, double minAngle, double minPulseLength, double maxAngle, double maxPulseLength, double timeToRotate60Degrees, double multiplicatorForTimeToReachPosition) {
        super(pin, PWM_RANGE_GENERATOR);
        init(minAngle, minPulseLength, maxAngle, maxPulseLength, timeToRotate60Degrees, multiplicatorForTimeToReachPosition);
    }
    
    /**
     * Initialization phase.
     * 
     * @param minAngle the minimum angle available for the rotor of this servo motor.
     * @param minPulseLength the pulse length in ms corresponding to the minAngle position of the rotor.
     * @param maxAngle the maximum angle available for the rotor of this servo motor.
     * @param maxPulseLength the pulse length in ms corresponding to the maxAngle position of the rotor.
     * @param timeToRotate60Degrees the time needed in seconds to execute a rotation of 60° [cfr datasheets].
     * @param multiplicatorForTimeToReachPosition the value used to multiply the computed time needed to reach a position.
     */
    private void init(double minAngle, double minPulseLength, double maxAngle, double maxPulseLength, double timeToRotate60Degrees, double multiplicatorForTimeToReachPosition) {
        // Check input parameters
        if(minAngle < 0)
            throw new IllegalArgumentException("minAngle needs to be positive.");
        if(minPulseLength < 0 || minPulseLength > PWM_RANGE_GENERATOR/1000)
            throw new IllegalArgumentException("minPulseLength needs to be in the [0; 2] ms interval.");
        if(maxAngle <= minAngle || maxAngle > 360)
            throw new IllegalArgumentException("maxAngle needs to be strictly greater than minAngle.");
        if(maxPulseLength <= minPulseLength || maxPulseLength > PWM_RANGE_GENERATOR/1000)
            throw new IllegalArgumentException("maxPulseLength needs to be strictly greater than minPulseLength and needs to be in the [0; 2] ms interval.");
        if(timeToRotate60Degrees < 0)
            throw new IllegalArgumentException("timeToRotate60Degrees represents a duration in seconds and then needs to be strictly positive.");
        if(multiplicatorForTimeToReachPosition < 0)
            throw new IllegalArgumentException("multiplicatorForTimeToReachPosition can't be negative.");

        
        // parameters calculations
        this.minPulseLength = (int)(1000 * minPulseLength);
        this.maxPulseLength = (int)(1000 * maxPulseLength);
        this.minAngle = minAngle;
        this.maxAngle = maxAngle;
        this.multiplicatorForTimeToReachPosition = multiplicatorForTimeToReachPosition;
        
        pulseLengthUsPerDegreeOfRotation = (this.maxPulseLength - this.minPulseLength) / (maxAngle - minAngle);
        timeForARotationOfOneDegree = (timeToRotate60Degrees * 1000) / 60;
        
        setPWMFreq(TARGET_FREQUENCY);
        
        // tell the servo motor to go to the zero position.
        // this will allow us to keep track of the position of the rotor.
        int timeForInitialization = timeToReachPosition(this.maxPulseLength, this.minPulseLength);
        setPulseLength(this.minPulseLength, timeForInitialization);
    }
    
    /**
     * Stop sending orders to the servo motor.
     * <p>It tells the motor to turn itself off and wait for more instructions.</p>
     */
    public void stopMotor() {
        setPWM(0);
    }
    
    /**
     * Sets the position of the rotor to the angle position, in degree.
     * @param angle the target position of the rotor, in °.
     */
    public void setAngle(double angle) {
        if(angle < minAngle)
            angle = minAngle;
        else if(angle > maxAngle)
            angle = maxAngle;
        int targetPulseLength = minPulseLength + (int)(pulseLengthUsPerDegreeOfRotation * angle);
        setPulseLength(targetPulseLength);
    }
    
    /**
     * Turns the rotor to the minAngle position.
     */
    public void resetPosition() {
        setPulseLength(minPulseLength);
    }
        
    /**
     * Writes the value to the PWM register for the given pin.
     * <p>Rotates the servo to the new position, and then stops the signal.</p>
     * @param value the value for the PWM signal corresponding to the new position to apply on the servo.
     */
    public void setPulseLength(int value) {
        if(value > 0)
            setPulseLength(value, timeToReachPosition(value));
        else
            stopMotor();
    }
    
    /**
     * Sends a PWM signal during delayToReachPosition ms.
     * <p>This allows you to send a PWM signal for a custom duration.</p>
     * @param value the value for the PWM signal corresponding to the new position to apply on the servo.
     * @param delayToReachPosition the estimated delay to reach the new position, with regard to it current position.
     */
    public void setPulseLength(int value, int delayToReachPosition) {
        if(value < minPulseLength)
            value = minPulseLength;
        else if(value > maxPulseLength)
            value = maxPulseLength;
        currentPulseLength = value;
        setPWM(value, delayToReachPosition);
    }
    
    /**
     * Returns the estimated time required for the motor to reach the target position,
     * with regard to its current position.
     * @param targetPulseLength the target position of the rotor.
     * @return the time required for the motor to reach the target position.
     */
    private int timeToReachPosition(int targetPulseLength) {
        return timeToReachPosition(currentPulseLength, targetPulseLength);
    }
    
    /**
     * Returns the time needed for the motor to reach the target position,
     * with regard to the startPulseLength position.
     * @param startPulseLength the start position of the rotor.
     * @param targetPulseLength the target position of the rotor.
     * @return the time required for the motor to reach the target position.
     */
    private int timeToReachPosition(int startPulseLength, int targetPulseLength) {
        double waitingTime = multiplicatorForTimeToReachPosition * Math.abs(((targetPulseLength - startPulseLength)/pulseLengthUsPerDegreeOfRotation)*timeForARotationOfOneDegree);
        if(waitingTime < MIN_TIME_TO_REACH_POSITION)
            waitingTime = MIN_TIME_TO_REACH_POSITION;
        return (int)waitingTime;
    }
    
    /**
     * Computes the current position of the rotor, in °.
     * @return the angle between minAngle and maxAngle corresponding to the current position of the rotor.
     */
    public double getPositionAngle(){
        return minAngle + (currentPulseLength - minPulseLength) / pulseLengthUsPerDegreeOfRotation;
    }
}
