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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.pid4j.pid.DefaultPid;
import org.pid4j.pid.Pid;

import com.raspoid.Tools;
import com.raspoid.brickpi.nxt.RangedValueListener;
import com.raspoid.brickpi.nxt.ValueChangeEvent;
import com.raspoid.brickpi.nxt.ValueListener;
import com.raspoid.exceptions.RaspoidException;
import com.raspoid.exceptions.RaspoidInterruptedException;

/**
 * Class allowing to control a motor with the brickpi
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class Motor {
    
    /**
     * Contains the min power that can be set
     */
    public static final int MIN_POWER = -255;

    /**
     * Contains the max power that can be set
     */
    public static final int MAX_POWER = 255;
    
    /**
     * Contains the encoder value for turning one lap
     */
    public static final int ENC_LAP_VALUE = 1440;
    
    /**
     * Contains the diameter when the motor is used with a wheel.
     */
    private double diameter = 2;
    
    /**
     * Contains the proportional gain of the PID 
     */
    private double kp = 1.;
    
    /**
     * Contains the integral gain of the PID
     */
    private double ki = 1.;
    
    /**
     * Contains the derivative gain of the PID
     */
    private double kd = 1.;

    /**
     * Current power of the motor
     */
    private int power = 0;

    /**
     * Encoder value corresponding to real value in NXT motors (without offset calculation)
     */
    private int realEncoderValue = 0;
    
    /**
     * Sign to apply to the encoder value. Used to detect when the motor rotates counterclockwise and goes below 0.
     */
    private int encoderSign = 1;
    
    /**
     * Latch used to detect when the first sign of the encoder value is setted.
     */
    private boolean initSignRetrieved = false;
    
    /**
     * Listeners with range argument needed.
     */
    protected List<RangedValueListener> listenersWithRange = new ArrayList<>();
    
    /**
     * Latch for waiting for the encoders to be initialized
     */
    private final CountDownLatch encoderInitLatch = new CountDownLatch(1);
        
    /**
     * Get the diameter (cm) configured for a wheel attached on the motor.
     * @return the diameter configured in centimeters
     */
    public double getDiameter() {
        return diameter;
    }

    /**
     * Configure the diameter (cm) for a wheel attached on the motor 
     * @param diameter the diameter size in centimeters
     */
    public void setDiameter(double diameter) {
        if (diameter <= 0) {
            throw new RaspoidException("The diameter should be strictly positive");
        }
        this.diameter = diameter;
    }

    /**
     * Returns the proportional gain used for this motor's PID
     * @return the proportional gain configured
     */
    public double getKp() {
        return kp;
    }

    /**
     * Returns the integral gain used for this motor's PID
     * @return the integral gain configured
     */
    public double getKi() {
        return ki;
    }

    /**
     * Returns the derivative gain used for this motor's PID
     * @return the derivative gain configured
     */
    public double getKd() {
        return kd;
    }
    
    /**
     * Sets the PID gains to be used with this motor. You should tune your
     * values to balance precision, rapidity and precision depending on your needs.
     * The diameter of the wheel, initial power and weight of your robot will affect
     * the PID and the gains should be tune accordingly. 
     * @param kp the proportional gain to use with pid
     * @param ki the integral gain to use with pid
     * @param kd the derivative gain to use with pid
     */
    public void setPidParams(double kp, double ki, double kd) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
    }

    /**
     * Returns the value of the power applied to the motor. 
     * @return the value of the power applied to the motor, in the [-255;255] range.
     */
    public int getPower() {
        return power;
    }

    /**
     * Sets the power of the motor, with a value in the [-255; 255] range.
     * Negative values are used for counterclockwise.
     * @param power the new power of the motor, in the [-255;255] range.
     */
    public void setPower(int power) {
        if (power > MAX_POWER || power < MIN_POWER) {
            throw new IllegalArgumentException(
                    "The power range that can be set is [" + MIN_POWER + ";" + MAX_POWER + "]");
        }
        this.power = power;
    }
    
    /**
     * Stops the motor.
     */
    public void stop() {
        setPower(0);
    }
    
    /**
     * Sets the value of the encoder of the motor.
     * This is used to update the realEncoderValue of the motor
     * when an update is received from the brick pi.
     * @param realEncoderValue the new value for the encoder.
     */
    public void setEncoderValue(int realEncoderValue) {
        //avoid polling get encoder otherwise it would block because of the latch
        int oldEncoderValue = encodersInitialized() ? Math.abs(getEncoderValue()) : 0;
        this.realEncoderValue = realEncoderValue;
        
        if(encodersInitialized()) {
            // Update encoder sign
            // (/!\ do not change the sign when power = 0, or when no change in the encoder value !)
            if((oldEncoderValue - realEncoderValue) < 0) {
                // ASC encoder value
                if(power > 0)
                    encoderSign = 1;
                else if(power < 0)
                    encoderSign = -1;
            } else if((oldEncoderValue - realEncoderValue) > 0) {
                // DESC encoder value
                if(power > 0)
                    encoderSign = -1;
                else if(power < 0)
                    encoderSign = 1;
            }
            
            for(RangedValueListener listener : listenersWithRange) {
                listener.notifyUpdate(new ValueChangeEvent(oldEncoderValue, Math.abs(getEncoderValue())));
            }
        } else {
            // We need to initialize the initial encoderValue
            // so the first event is launched when nbRotations are really executed
            // and not for the first encoderValue update
            // Needed if listener added before first encoderValue initialization
                        
            encoderInitLatch.countDown();
            
            for(RangedValueListener listener : listenersWithRange) {
                listener.setInitialValue(realEncoderValue);
            }
        }
    }

    /**
     * Returns true if the encoder value has been initialized.
     * This is used to detect the first encoder value received from the real NXT motors.
     * @return true if the encoder value has been initialized. False otherwhise.
     */
    protected boolean encodersInitialized() {
        return encoderInitLatch.getCount() == 0;
    }

    /**
     * Returns a calculated value for the encoder of the motor.
     * This value is based on the realEncoderValue (real value from the NXT motor)
     * and the encoder value offset.
     * @return the real encoder value - the encoder value offset.
     */
    public int getEncoderValue() {
        try {
            encoderInitLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RaspoidInterruptedException("Motor initialization was interrupted unexpectedly", e);
        }
        return realEncoderValue * encoderSign;
    }
 
    /**
     * Adds a listener that will be triggered as soon as the encoder value
     * has changed for more than range value, wrt. the last triggered event. 
     * @param rangeValue the value range before an event is triggered.
     * @param listener the new listener added to the list of listeners of the motor.
     */
    public void onChange(int rangeValue, ValueListener listener) {
        RangedValueListener listenerWithRange = new RangedValueListener(rangeValue, listener);
        // We need to initialize the initial encoderValue
        // so the first event is launched when value range is really exceeded
        // and not for the first encoderValue update
        listenerWithRange.setInitialValue(realEncoderValue);
        
        this.addListenerWithRange(listenerWithRange);
    }
    
    /**
     * Adds a listener that will be triggered each time a full rotation has been done.
     * @param listener the listener that will be triggered on a completed rotation
     */
    public void onRotate(ValueListener listener) {
        onChange(ENC_LAP_VALUE, listener);
    }
    
    /**
     * Rotate the motor for a specified number of rotations using a PID eval loop.
     * <p>This method is blocking.</p>
     * @param nbRotations the number of rotations to perform.
     * @param initPower the initial power applied which is also the upper power bound used by the PID.
     */
    public void rotate(double nbRotations, int initPower) {
        if(nbRotations <= 0)
            throw new RaspoidException("The number of rotations to perform must be strictly positive");
        
        if(!initSignRetrieved) {
            // Note: we can't determine the sign of the first encoder value.
            // So we need to make the motor move to know the first encoder value.
            setPower(50);
            Tools.sleepMilliseconds(100);
            setPower(0);
            initSignRetrieved = true;
        }
        
        // Handle the blocking call
        CountDownLatch endLatch = new CountDownLatch(1);
        Pid pid = new DefaultPid();
        pid.setKpid(kp, ki, kd);
        
        // Configure the pid output range to be the power applied to the motor
        if(initPower > 0) {
            pid.setOutputLimits(0., (double)initPower);
        } else {
            pid.setOutputLimits((double)initPower, 0.);
        }
        
        //Set the wanted encoder value to be reach
        double encoderGoal = nbRotations * Motor.ENC_LAP_VALUE;
        encoderGoal = initPower > 0 ? getEncoderValue() + encoderGoal : getEncoderValue() - encoderGoal;
        pid.setSetPoint(encoderGoal);
        
        //Listener checking if goal is reached
        ValueListener encoderListener = evt -> {
            double output = pid.compute((double)getEncoderValue());
            setPower((int)output);
            if ((initPower >= 0 && pid.getSetPoint() <= getEncoderValue())
                    || (initPower < 0 && pid.getSetPoint() >= getEncoderValue())) {
                endLatch.countDown(); // release the method when done
                setPower(0);
            }
        };
        
        //Initiate the move
        onChange(10, encoderListener);
        double output = pid.compute((double)getEncoderValue());
        setPower((int)output);
        
        // Wait completion
        try {
            endLatch.await();
            removeListener(encoderListener);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RaspoidInterruptedException("Motor rotation interrupted unexpectedly", e);
        }
    }
    
    /**
     * Rotate the motor for a specified number of rotations using a PID eval loop.
     * <p>This method is non-blocking.</p>
     * @see #rotate(double, int)
     * @param nbRotations the number of rotations to perform.
     * @param initPower the initial power applied which is also the upper power bound used by the PID.
     * @return the Thread used to execute the action.
     */
    public Thread rotateNonBlocking(double nbRotations, int initPower) {
        Thread thread = new Thread() {
            
            @Override
            public void run() {
                rotate(nbRotations, initPower);
                try {
                    this.interrupt();
                    this.join();
                    return;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
        thread.start();
        return thread;
    }
    
    /**
     * Rotates the motor to travel a specific distance (cm) using a PID eval loop.<br>
     * The diameter of the wheel attached to the motor should be properly set before calling this method.
     * <p>This method is blocking.</p>
     * @param distance the distance to travel, in centimeters.
     * @param initPower the initial power applied, which is also the upper power bound used by the PID.
     */
    public void move(double distance, int initPower) {
        double perimeter = Math.PI * diameter;
        rotate(distance / perimeter, initPower);
    }
    
    /**
     * Rotates the motor to travel a specific distance (cm) using a PID eval loop.<br>
     * The diameter of the wheel attached to the motor should be properly set before calling this method.
     * <p>This method is non-blocking.</p>
     * @param distance the distance to travel, in centimeters.
     * @param initPower the initial power applied, which is also the upper power bound used by the PID.
     * @return the Thread used to execute the action.
     */
    public Thread moveNonBlocking(double distance, int initPower) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                move(distance, initPower);
                try {
                    this.interrupt();
                    this.join();
                    return;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
        thread.start();
        return thread;
    }
    
    /**
     * This utility method is used as a barrier: each Thread given in parameter must be finished, before the method stops.<br>
     * This allows to have a blocking method combining non-blocking actions.
     * <p>As an example, it is possible to rotate 2 motors at the same time,
     * and wait that the rotations are finished on both motors to consider the complete action as finished.</p>
     * <p>This method is blocking.</p>
     * @param threads the Threads to consider for the barrier.
     */
    public static void waitAllFinished(Thread[] threads) {
        boolean stillRunning = true;
        while(stillRunning) {
            Tools.sleepMilliseconds(10);
            stillRunning = false;
            for (Thread thread : threads) {
                if(thread.isAlive())
                    stillRunning = true;
            }
        }
    }
    
    /**
     * Removes the specified RangedValueListener if present in the listeners
     * @param listener the RangedValueListener to be removed
     */
    public void removeListener(RangedValueListener listener) {
        if(listenersWithRange.contains(listener)) {
            listenersWithRange.remove(listener);
        }
    }
    
    /**
     * Removes the specified ValueListener if present in the listeners
     * @param listener the ValueListener to be removed
     */
    public void removeListener(ValueListener listener) {
        if(listenersWithRange.contains(listener)) {
            listenersWithRange.remove(listener);
        }
    }
    
    protected void addListenerWithRange(RangedValueListener listener) {
        if(!listenersWithRange.contains(listener)) {
            listenersWithRange.add(listener);
        }
    }
    
    @Override
    public String toString() {
        return "[Motor] wheel diameter: " + diameter + "cm.";
    }
}
