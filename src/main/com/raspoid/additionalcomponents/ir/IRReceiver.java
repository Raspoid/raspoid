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
import com.raspoid.GPIOComponent;
import com.raspoid.GPIOPin;

/**
 * IR detectors are little microchips with a photocell that are tuned
 * to listen to infrared light.
 * 
 * <p>They are almost always used for remote control detection:
 *  <ul>
 *      <li>AV instruments such as Audio,TV,VCR,CD,MD,DVD,etc</li>
 *      <li>Home appliances such as Air-conditioner,Fan,etc.</li>
 *      <li>CATV set top boxes</li>
 *      <li>Multi-media Equipment</li>
 *  </ul>
 * </p>
 * 
 * <p>Inside the remote control is a matching IR LED, which emits IR pulses 
 * to tell the device to turn on, off, etc.</p>
 * 
 * <p>IR detectors have a demodulator inside that looks for modulated IR at 38KHz.
 * Just shining an IR LED wont be detected, it has to be PWM blinking at 38KHz.</p>
 * 
 * <p>The PWM is a "carrier" pulsing. By PWM'ing it, we let the LED cool off half the time (reason 1).
 * Another reason to use a PWM carrier is that the TV will only listen to certain frequencies of PWM.
 * So a Sony remote at 37KHz wont be able to work with a JVC DVD player that only wants say 50KHz (reason 2).
 * Finally, the most important reason is that by pulsing a carrier wave, you reduce the afects of ambient lighting (reason 3).</p>
 * 
 * <p>How to decode the signal when we don't have a $1000 oscilloscope ?
 * The IR decoder such as the 1838B does us one favor, it 'filters out' the 38KHz signal
 * so that we only get the big chunks of signal in the milliscond range.
 * This is much easier for a microcontroller like the Raspberry Pi to handle.
 * Thats what we do here.</p>
 * 
 * <p>Main source of informations:
 * <a href="https://learn.adafruit.com/ir-sensor/ir-remote-signals">https://learn.adafruit.com/ir-sensor/ir-remote-signals</a>.</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public abstract class IRReceiver extends GPIOComponent {

    /**
     * The maximum pulse we'll listen for. 65ms is a long time.
     */
    public static final int MAX_PULSE = 65000; // μs
    
    /**
     * What the timing resolution should be.
     * Larger is better as it's more 'precise'.
     * But if too large, you won't get accurate timing.
     */
    public static final int RESOLUTION = 20; // μs
    
    private int pinNumber;
    
    /**
     * Constructor for a new infrared receiver using a specific GPIO pin.
     * @param pin the GPIO pin to use to deal with the infrared receiver.
     */
    public IRReceiver(GPIOPin pin) {
        Gpio.wiringPiSetup();
        pinNumber = pin.getPin().getWiringPiNb();
        Gpio.pinMode(pinNumber, Gpio.INPUT);
    }
    
    /**
     * Polls until an infrared signal is detected and then return this signal.
     * <p><b>! Attention !</b> Polling means ~100% of CPU for 1 core.</p>
     * @return the IRSignal corresponding to the newly detected infrared signal.
     */
    public IRSignal detectSignal() {
        // We will store up to 100 pulse pairs (this is -a lot-).
        // Pair is high and low pulse (2 int per pulse).
        int[] signal = new int[200];
        // temporary storage timing
        int highPulse, lowPulse;
        
        boolean completeSignalDetected = false;
        int currentPulse = 0;
        // setted to true as soon as a new signal start to be detected
        boolean newSignalDetected = false;
        
        while(!completeSignalDetected) {
            highPulse = 0;
            lowPulse = 0;
            
            // HIGH / OFF
            while(Gpio.digitalRead(pinNumber) == 1 && !completeSignalDetected) {
                // pin is still HIGH
                
                // count off another few microseconds
                highPulse++;
                Gpio.delayMicroseconds(RESOLUTION);
                
                // If the pulse is too long, we timed out:
                // either nothing was received or the code is finished,
                if((highPulse * RESOLUTION >= MAX_PULSE) && newSignalDetected) {
                    completeSignalDetected = true;
                }
            }
            if(!completeSignalDetected) {
                // we didn't time out so lets stash the reading
                signal[currentPulse * 2] = highPulse * RESOLUTION;
            }
            
            // LOW / ON
            // same as above for low pulse
            while(Gpio.digitalRead(pinNumber) == 0 && !completeSignalDetected) {
                // pin is still LOW
                
                if(!newSignalDetected)
                    newSignalDetected = true;
                
                // count off another few microseconds
                lowPulse++;
                Gpio.delayMicroseconds(RESOLUTION);
                
                // If the pulse is too long, we timed out:
                // either nothing was received or the code is finished,
                // so print what we've grabbed so far and then reset
                if(lowPulse * RESOLUTION >= MAX_PULSE) {
                    completeSignalDetected = true;
                }
            }
            if(!completeSignalDetected) {
                signal[currentPulse * 2 + 1] = lowPulse * RESOLUTION;
            }
            
            // we read one high-low pulse successfully, continue !
            currentPulse++;
        }
        
        return new IRSignal(signal);
    }
    
    /**
     * Returns the IRSignal from IRProtocol corresponding to the input signal,
     * or null if the input signal has no correspondance in the given protocol.
     * @param protocol the protocol to analyze.
     * @param signal the signal to search in the protocol.
     * @return the IRSignal from the protocol corresponding to the input signal,
     * or null if no correspondance for the signal in the protocol.
     */
    public IRSignal decodeIRSignal(IRProtocol protocol, IRSignal signal) {
        if(protocol == null)
            throw new IllegalArgumentException("A protocol must be defined to decode an IRSignal.");
        if(signal == null)
            throw new IllegalArgumentException("The signal can't be null");
        
        int tolerance = 20; // %
        
        for(IRSignal protocolSignal : protocol.getSignals()) {
            if(protocolSignal.matches(signal, tolerance)) {
                return protocolSignal;
            }
        }
        
        return null;
    }
}
