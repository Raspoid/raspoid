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
package com.raspoid.additionalcomponents;

import com.pi4j.wiringpi.Gpio;
import com.raspoid.GPIOComponent;
import com.raspoid.GPIOPin;
import com.raspoid.Tools;
import com.raspoid.examples.additionalcomponents.UltrasonicHCSR04Example;
import com.raspoid.examples.additionalcomponents.UltrasonicSensorsComparison;
import com.raspoid.exceptions.RaspoidException;

/**
 * Ultrasonic ranging module HCSR04 provides 2cm - 400cm non-contact
 * measurement function. The ranging accuracy can reach to 3mm. The modules
 * includes ultrasonic transmitters, receiver and control circuit.
 * 
 * <p>The basic principle of work:
 *  <ul>
 *      <li>(1) Using IO trigger for at least 10us high level signal,</li>
 *      <li>(2) The Module automatically sends eight 40 kHz and detect whether there is a
 *      pulse signal back.</li>
 *      <li>(3) If the signal back, through high level, time of high output IO duration is
 *      the time from sending ultrasonic to returning.</li>
 *  </ul>
 * </p>
 * 
 * <p>distance = (high level time X velocity of sound (340M/S) / 2</p> 
 * 
 * <p>Datasheet: <a href="http://raspoid.com/download/datasheet/HCSR04">HCSR04</a></p>
 * 
 * <p><i>Note: for accuracy reasons, we need to directly use WiringPi functions
 * instead of pi4j to control the pins, probably because pi4j is too slow to react (higher level).</i></p>
 * 
 * <p>Examples of use: {@link UltrasonicHCSR04Example} and {@link UltrasonicSensorsComparison}</p>
 * 
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class UltrasonicHCSR04 extends GPIOComponent {
    
    private int trig;
    private int echo;
    private final boolean singlePin;
    
    /**
     * Constructor for an ultrasonic sensor HCSR04 using a specific Gpio trigger pin 
     * and a specific Gpio echo pin.
     * @param trigPin the pin to use to trigger signals.
     * @param echoPin the pin to use to detect echo signals.
     */
    public UltrasonicHCSR04(GPIOPin trigPin, GPIOPin echoPin) {
        singlePin = false;
        trig = trigPin.getPin().getWiringPiNb();
        echo = echoPin.getPin().getWiringPiNb();
        if(trig == echo)
            throw new RaspoidException("The selected Gpio pins for the ultrasonic sensor can't be the same.");
        
        Gpio.pinMode(trig, Gpio.OUTPUT);
        Gpio.pinMode(echo, Gpio.INPUT);
    }
    
    /**
     * Constructor for a variant of ultrasonic sensor HCSR04 using a signle Gpio pin for trigger and echo signals.
     * @param trigPin the Gpio pin used for trigger and echo signals.
     */
    public UltrasonicHCSR04(GPIOPin trigPin) {
        singlePin = true;
        trig = trigPin.getPin().getWiringPiNb();
    }
    
    /**
     * Get the calculated distance from the ultrasound sensor.
     * <p>By measuring the width of the echo pulse and dividing by 58 to get distance in cm [cfr datasheet].</p>
     * @return the measured distance, in cm.
     */
    public double getDistance() {
        if(singlePin) {
            return getDistanceOnePin();
        } else {
            return getDistanceTwoPins();
        }
    }
    
    private double getDistanceTwoPins() {
        Gpio.digitalWrite(trig, Gpio.LOW);
        Tools.sleepMilliseconds(2);
        
        Gpio.digitalWrite(trig, Gpio.HIGH);
        Tools.sleepMilliseconds(10);
        Gpio.digitalWrite(trig, Gpio.LOW);

        while(!(Gpio.digitalRead(echo) == 1));
        long start = System.nanoTime();

        while(!(Gpio.digitalRead(echo) == 0));
        long end = System.nanoTime();
        
        return (end - start) / 1000. / 58.;
    }
    
    private double getDistanceOnePin() {
        Gpio.pinMode(trig, Gpio.OUTPUT);
        Gpio.digitalWrite(trig, Gpio.LOW);
        Tools.sleepMilliseconds(2);
        
        Gpio.digitalWrite(trig, Gpio.HIGH);
        Tools.sleepMilliseconds(10);
        Gpio.digitalWrite(trig, Gpio.LOW);
        
        Gpio.pinMode(trig, Gpio.INPUT);
        while(!(Gpio.digitalRead(trig) == 1));
        long start = System.nanoTime();

        while(!(Gpio.digitalRead(trig) == 0));
        long end = System.nanoTime();
        
        return (end - start) / 1000. / 58.;
    }
}
