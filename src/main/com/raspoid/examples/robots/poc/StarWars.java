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
package com.raspoid.examples.robots.poc;

import com.raspoid.Tools;
import com.raspoid.additionalcomponents.PassiveBuzzer;

/**
 * Play The Imperial March - Star Wars, on a passive buzzer.
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class StarWars {
    
    // Notes
    private static double F3 =  174.61, Ab3 = 207.65, LA3 = 220.00, Bb3 = 233.08, B3 =  246.94, 
            C4 =  261.63, Db4 = 277.18, D4 =  293.66, Eb4 = 311.13, E4 =  329.63, F4 =  349.23, 
            Gb4 = 369.99, G4 =  392.00, Ab4 = 415.30, LA4 = 440.00;
    
    // Durations
    private static double BPM = 120;
    private static double Q = 60000/BPM; // quarter 1/4 
    private static double H = 2*Q; // half 2/4
    private static double E = Q/2; // eighth 1/8
    private static double S = Q/4; // sixteenth 1/16
    
    /**
     * Private constructor to hide the implicit public one.
     */
    private StarWars() {
    }
    
    private static void tone(PassiveBuzzer buzzer, double note, double millis) {
        buzzer.playTone(note, (int) millis);
    }
    
    private static void delay(double millis) {
        Tools.sleepMilliseconds((long) millis/1000);
    }
    
    /**
     * Play the intro of The Imperial March - Star Wars.
     * @param buzzer the instance of the buzzer playing the tone.
     */
    public static void playShort(PassiveBuzzer buzzer) {
        tone(buzzer,LA3,Q);
        delay(1+Q);
        tone(buzzer,LA3,Q);
        delay(1+Q);
        tone(buzzer,LA3,Q);
        delay(1+Q);
        tone(buzzer,F3,E+S);
        delay(1+E+S);
        tone(buzzer,C4,S);
        delay(1+S);

        tone(buzzer,LA3,Q);
        delay(1+Q);
        tone(buzzer,F3,E+S);
        delay(1+E+S);
        tone(buzzer,C4,S);
        delay(1+S);
        tone(buzzer,LA3,H);
        delay(1+H);

        tone(buzzer,E4,Q); 
        delay(1+Q); 
        tone(buzzer,E4,Q);
        delay(1+Q);
        tone(buzzer,E4,Q);
        delay(1+Q);
        tone(buzzer,F4,E+S);
        delay(1+E+S);
        tone(buzzer,C4,S);
        delay(1+S);

        tone(buzzer,Ab3,Q);
        delay(1+Q);
        tone(buzzer,F3,E+S);
        delay(1+E+S);
        tone(buzzer,C4,S);
        delay(1+S);
        tone(buzzer,LA3,H);
        delay(1+H);
    }
    
    /**
     * Play a long part of The Imperial March - Star Wars.
     * @param buzzer the instance of the buzzer playing the tone.
     */
    public static void playLong(PassiveBuzzer buzzer) {
        playShort(buzzer);

        tone(buzzer,LA4,Q);
        delay(1+Q);
        tone(buzzer,LA3,E+S);
        delay(1+E+S);
        tone(buzzer,LA3,S);
        delay(1+S);
        tone(buzzer,LA4,Q);
        delay(1+Q);
        tone(buzzer,Ab4,E+S);
        delay(1+E+S);
        tone(buzzer,G4,S);
        delay(1+S);

        tone(buzzer,Gb4,S);
        delay(1+S);
        tone(buzzer,E4,S);
        delay(1+S);
        tone(buzzer,F4,E);
        delay(1+E);
        delay(1+E);
        tone(buzzer,Bb3,E);
        delay(1+E);
        tone(buzzer,Eb4,Q);
        delay(1+Q);
        tone(buzzer,D4,E+S);
        delay(1+E+S);
        tone(buzzer,Db4,S);
        delay(1+S);

        tone(buzzer,C4,S);
        delay(1+S);
        tone(buzzer,B3,S);
        delay(1+S);
        tone(buzzer,C4,E);
        delay(1+E);
        delay(1+E);
        tone(buzzer,F3,E);
        delay(1+E);
        tone(buzzer,Ab3,Q);
        delay(1+Q);
        tone(buzzer,F3,E+S);
        delay(1+E+S);
        tone(buzzer,LA3,S);
        delay(1+S);

        tone(buzzer,C4,Q);
        delay(1+Q);
        tone(buzzer,LA3,E+S);
        delay(1+E+S);
        tone(buzzer,C4,S);
        delay(1+S);
        tone(buzzer,E4,H);
        delay(1+H);

        tone(buzzer,LA4,Q);
        delay(1+Q);
        tone(buzzer,LA3,E+S);
        delay(1+E+S);
        tone(buzzer,LA3,S);
        delay(1+S);
        tone(buzzer,LA4,Q);
        delay(1+Q);
        tone(buzzer,Ab4,E+S);
        delay(1+E+S);
        tone(buzzer,G4,S);
        delay(1+S);

        tone(buzzer,Gb4,S);
        delay(1+S);
        tone(buzzer,E4,S);
        delay(1+S);
        tone(buzzer,F4,E);
        delay(1+E);
        delay(1+E);
        tone(buzzer,Bb3,E);
        delay(1+E);
        tone(buzzer,Eb4,Q);
        delay(1+Q);
        tone(buzzer,D4,E+S);
        delay(1+E+S);
        tone(buzzer,Db4,S);
        delay(1+S);

        tone(buzzer,C4,S);
        delay(1+S);
        tone(buzzer,B3,S);
        delay(1+S);
        tone(buzzer,C4,E);
        delay(1+E);
        delay(1+E);
        tone(buzzer,F3,E);
        delay(1+E);
        tone(buzzer,Ab3,Q);
        delay(1+Q);
        tone(buzzer,F3,E+S);
        delay(1+E+S);
        tone(buzzer,C4,S);
        delay(1+S);

        tone(buzzer,LA3,Q);
        delay(1+Q);
        tone(buzzer,F3,E+S);
        delay(1+E+S);
        tone(buzzer,C4,S);
        delay(1+S);
        tone(buzzer,LA3,H);
        delay(1+H);

        delay(2*H);
    }
}
