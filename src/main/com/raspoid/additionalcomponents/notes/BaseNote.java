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
package com.raspoid.additionalcomponents.notes;

/**
 * Frequencies of to base notes "do re mi fa sol la si", for the octave 0.
 * 
 * <p>To get frequency for octave x, simply use the following formula:
 * <pre>
 *  baseNote.getFrequency() * Math.pow(2, octave)
 * </pre>
 * </p>
 *  
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public enum BaseNote {
    /**
     * Do (octave 0).
     */
    DO_0(32.7, "DO"),
    
    /**
     * Re (octave 0).
     */
    RE_0(36.71, "RE"), 
    
    /**
     * Mi (octave 0).
     */
    MI_0(41.20, "MI"), 
    
    /**
     * Fa (octave 0).
     */
    FA_0(43.65, "FA"),
    
    /**
     * Sol (octave 0).
     */
    SOL_0(49., "SOL"), 
    
    /**
     * La (octave 0).
     */
    LA_0(55., "LA"), 
    
    /**
     * Si (octave 0).
     */
    SI_0(61.74, "SI");
    
    double frequency;
    String name;
    
    BaseNote(double frequency, String name) {
        this.frequency = frequency;
        this.name = name;
    }
    
    /**
     * Get the frequency of this base note (octave 0), in Hz.
     * @return the frequency of the base note (octave 0), in Hz.
     */
    public double getFrequency() {
        return frequency;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
