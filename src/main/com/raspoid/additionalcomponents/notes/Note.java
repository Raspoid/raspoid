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
 * Class of utils to easily get the frequency corresponding to a specific note of music.
 * 
 * <p>{@link #getNoteFrequency(BaseNote, int)} allows you to easily get the frequency
 * corresponding to a note of music, starting from the base note ({@link BaseNote})
 * and for a specific octave.</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class Note {
    
    /**
     * Private constructor to hide the implicit public one.
     */
    private Note() {
    }
    
    /**
     * Get the frequency corresponding to a note, from the base note and for a specific octave.
     * @param baseNote the BaseNote from which to calculate the output frequency.
     * @param octave the octave to use to determine output frequency.
     * @return the calculated frequency from the base note and for the specific octave.
     */
    public static double getNoteFrequency(BaseNote baseNote, int octave) {
        if(octave <= 0)
            octave = 0;
        return baseNote.getFrequency() * Math.pow(2, octave);
    }
}
