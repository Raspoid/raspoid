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

/**
 * An infrared signal is a collection of pulses.
 * 
 * <p>Each pulses of the signal are registered as OFF, ON contiguous periods.</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class IRSignal {
    
    /**
     * The array of int representing the signal composition.
     * <p>Each pulses of the signal are registered as off, on contiguous values.
     * Only the 2 * nbPulses first elements of this array are composing the signal.</p>
     */
    private final int[] signal;
    
    /**
     * The number of pulses composing the signal.
     * A pulse is a OFF, ON pair.
     */
    private final int nbPulses;
    
    private String name = ""; 
    
    /**
     * Constructor for a new signal, using the specific int[] signal data input
     * corresponding to the raw composition of the signal.
     * @param signal the int[] data input corresponding to the raw composition of the signal (OFF/ON periods).
     */
    public IRSignal(int[] signal) {
        this("no_name", signal);
    }
    
    /**
     * Constructor for a new signal, using the specific int[] signal data input
     * corresponding to the raw composition of the signal, and using a specific name.
     * @param name the name to link to this signal.
     * @param signal the int[] data input corresponding to the raw composition of the signal (OFF/ON periods).
     */
    public IRSignal(String name, int[] signal) {
        this.name = name;
        this.signal = signal.clone();
        
        // The first OFF pulse can be ignored (its juste the time
        // from when the program is launched on the RPi to the first
        // IR signal received).
        this.signal[0] = 0;
        
        int pulsesNb = 0;
        for(int i = 0; i < signal.length; i += 2) {
            if(!(signal[i] == 0 && signal[i + 1] == 0))
                pulsesNb++;
        }
        this.nbPulses = pulsesNb;
    }
    
    /**
     * Get the number of pulses composing the signal.
     * @return the number of pulses composing the signal.
     */
    public int getNbPulses() {
        return nbPulses;
    }
    
    /**
     * Get the complete array of OFF/ON periods.
     * <p>Only the 2 * nbPulses first elements of this array are composing the signal.</p>
     * @return the complete array of OFF/ON periods.
     */
    public int[] getPulses() {
        return signal.clone();
    }
    
    /**
     * Get the name corresponding to this signal.
     * "no_name" String if undefined.
     * @return the name of this signal. "no_name" String if undefined.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns true if the specific input signal corresponds to this signal,
     * with a fuzziness of tolerance %.
     * @param signal the signal to compare with the current instance.
     * @param tolerance the fuzziness to use for the comparison, in %.
     * @return true if the input signal corresponds to this signal instance. False otherwise.
     */
    public boolean matches(IRSignal signal, int tolerance) {
        
        if(signal.getNbPulses() != nbPulses)
            return false;
        
        // we then compare pulse by pulse
        int[] pulsesToCompare = signal.getPulses();
        
        for(int i = 0; i < 2 * nbPulses; i++) {
            int currentPulseToCompare = pulsesToCompare[i]; 
            int fuzziness = currentPulseToCompare * tolerance / 100;
            int difference = Math.abs(this.signal[i] - currentPulseToCompare);
            if(difference > fuzziness)
                return false;
        }
        
        // everything matches !
        return true;
    }
    
    @Override
    public String toString() {
        String result = name + " - ";
        
        result += "Pulses: \nOFF              ON";
        for(int i = 0; i < nbPulses; i++) {
            String on = signal[i * 2] + " μs";
            result += "\n" + on;
            int onLength = on.length();
            for(int j = onLength; j < 17; j++)
                result += " ";
            result += signal[i * 2 + 1] + " μs";
        }
        
        return result;
    }
    
    /**
     * This method is mainly a utils methods.
     * <p>It returns the String representation of the composition of the signal,
     * in the Java int[] format: "int irSignal[] = {...};"</p>
     * @return the String representation of the composition of the signal,
     * in the Java int[] format: "int irSignal[] = {...};".
     */
    public String toJavaIntArrayRepresentation() {
        String result = "int irSignal[] = {";
        
        for(int i = 0; i < nbPulses; i++) {
            result += signal[i * 2] + ", ";
            result += Integer.toString(signal[i * 2 + 1]);
            if(i != nbPulses - 1)
                result += ", ";
        }
        
        result += "};";
        
        return result;
    }
}
