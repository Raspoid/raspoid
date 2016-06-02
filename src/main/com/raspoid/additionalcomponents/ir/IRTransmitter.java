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
package com.raspoid.additionalcomponents.ir;

import com.pi4j.wiringpi.Gpio;
import com.raspoid.PWMComponent;
import com.raspoid.PWMPin;
import com.raspoid.examples.additionalcomponents.ir.IRTransmitterExample;

/**
 * <b>This class is an abstraction for an infrared transmitter.
 * An infrared transmitter can simply be an IR LED.</b>
 * 
 * <p>To send IR codes, the IR transmitter LED is quickly pulsed (PWM - pulse width modulated)
 * at a high frequency of (for example) 38KHz and then that PWM is likewise pulsed on and off much slower,
 * at times that are about 1-3 ms long.</p>
 * 
 * <p>Why using a carrier frequency ? <a href="http://mchobby.be/wiki/index.php?title=Senseur_IR_Signal">McHobby</a></p>
 * 
 * <p>Datasheet: <a href="http://raspoid.com/download/datasheet/ir_led">Infrared LED</a></p>
 * 
 * <p>Example of use: {@link IRTransmitterExample}</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class IRTransmitter extends PWMComponent {
    
    /**
     * The carrier frequency of the infrared signal should be 38kHz.
     * <p>Each PWM period is then 26.32μs long.</p>
     */
    public static final int DEFAULT_CARRIER_FREQUENCY = 38000; // Hz
    
    /**
     * Range register in the PWM generator.
     * <p>The values sent to the generator must be in the [0; PWM_RANGE_GENERATOR] range.</p>
     */
    public static final int PWM_RANGE_GENERATOR = 100; // %
    
    /**
     * Constructor for a default infrared transmitter connected to a specific PWM pin,
     * using the default carrier frequency ({@link #DEFAULT_CARRIER_FREQUENCY}).
     * @param pin the PWMPin used to send the PWM signals.
     * @see #IRTransmitter(PWMPin, int)
     */
    public IRTransmitter(PWMPin pin) {
        this(pin, DEFAULT_CARRIER_FREQUENCY);
    }
    
    /**
     * Constructor for an infrared transmitter connected to a specific the PWM pin,
     * and using the specified carrierFrequency.
     * @param pin the PWMPin used to send the PWM signals.
     * @param carrierFrequency the carrier frequency used to send the infrared signals.
     */
    public IRTransmitter(PWMPin pin, int carrierFrequency) {
        super(pin, PWM_RANGE_GENERATOR);
        setPWMFreq(carrierFrequency);
    }
    
    /**
     * Sends an IR pulse for a duration of micros microseconds.
     * <p>Note: this method isn't placed in the parent PWMComponent, because it's deprecated to use a Gpio.delayMicroseconds.
     * Indeed, since the system is not in realtime executions, we can't get any guarantee regarding the microseconds delays.</p>
     * @param micros
     */
    private void pulse(long micros) {
        Gpio.pwmWrite(pinNumber, 50); // 50%
        Gpio.delayMicroseconds(micros);
        Gpio.pwmWrite(pinNumber, 0);
    }
    
    /**
     * Sends an IRSignal through the IRTransmitter connected to the setted PWM pin.
     * @param signal the IRSignal sent through the PWM pin.
     * @see IRSignal
     */
    public void transmitSignal(IRSignal signal) {
        int[] pulses = signal.getPulses();
        
        for(int i = 0; i < signal.getNbPulses(); i++) {
            Gpio.delayMicroseconds(pulses[i * 2]); // off
            pulse(pulses[i * 2 + 1]); // on
        }
    }
}
