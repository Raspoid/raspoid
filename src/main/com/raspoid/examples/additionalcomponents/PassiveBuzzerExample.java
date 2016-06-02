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
package com.raspoid.examples.additionalcomponents;

import com.raspoid.additionalcomponents.PCA9685;
import com.raspoid.additionalcomponents.PassiveBuzzer;
import com.raspoid.additionalcomponents.PCA9685.PCA9685Channel;
import com.raspoid.additionalcomponents.notes.BaseNote;

/**
 * Example of use of a Passive Buzzer.
 * 
 * @see PassiveBuzzer
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class PassiveBuzzerExample {
    
    /**
     * Private constructor to hide the implicit public one.
     */
    private PassiveBuzzerExample() {
    }
    
    /**
     * Command-line interface.
     * @param args unused here.
     */
    public static void main(String[] args) {
        // Comparison - PassiveBuzzer using 
        //      1. a Raspberry Pi PWM pin
        //      2. a PCA9685 (max 1000Hz !)
        // Note: we can observed that frequencies for our PassiveBuzzer are OK 
        // for octaves in the 3..7 range.
        
        int timePerNote = 500; // ms
        PassiveBuzzer buzzer;
        
        // Comment/Uncomment the corresponding lines for comparison.
        // 1. Using a Raspberry Pi PWM pin
        //buzzer = new PassiveBuzzer(PWMPin.PWM1); // NOSONAR
        // 2. Using a PCA9685
        buzzer = new PassiveBuzzer(new PCA9685(), PCA9685Channel.CHANNEL_00); // NOSONAR
        
        for(int i=0; i <= 7; i++) {
            buzzer.playNote(BaseNote.DO_0, i, timePerNote);
            buzzer.playNote(BaseNote.RE_0, i, timePerNote);
            buzzer.playNote(BaseNote.MI_0, i, timePerNote);
            buzzer.playNote(BaseNote.FA_0, i, timePerNote);
            buzzer.playNote(BaseNote.SOL_0, i, timePerNote);
            buzzer.playNote(BaseNote.LA_0, i, timePerNote);
            buzzer.playNote(BaseNote.SI_0, i, timePerNote);
        }
    }
}
