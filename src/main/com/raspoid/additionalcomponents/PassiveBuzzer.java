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

import com.raspoid.PWMComponent;
import com.raspoid.PWMPin;
import com.raspoid.Tools;
import com.raspoid.additionalcomponents.PCA9685.PCA9685Channel;
import com.raspoid.additionalcomponents.notes.BaseNote;
import com.raspoid.additionalcomponents.notes.Note;
import com.raspoid.examples.additionalcomponents.PassiveBuzzerExample;

/**
 * A passive buzzer will not beep if DC signals are used.
 * Instead, you need to use square waves to drive it.
 * 
 * <p>This component then uses a PWM signal to control the output
 * frequency from the buzzer.</p>
 * 
 * <p>Example Datasheet: <a href="http://raspoid.com/download/datasheet/PassiveBuzzer">Passive Buzzer</a></p>
 * 
 * <p>Example of use: {@link PassiveBuzzerExample}</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class PassiveBuzzer extends PWMComponent {
    
    /**
     * Range register in the PWM generator.
     * The values sent to the generator must be in the [0; PWM_RANGE_GENERATOR] range.
     */
    public static final int PWM_RANGE_GENERATOR = 100; // %
    
    /**
     * Constructor for a passive buzzer using a specific PWM pin from the Raspberry Pi
     * to create PWM signals.
     * @param pin the PWM pin to use to generate the PWM signals.
     */
    public PassiveBuzzer(PWMPin pin) {
        super(pin, PWM_RANGE_GENERATOR);
    }
    
    /**
     * Constructor for a passive buzzer using a specific PCA9685 to create PWM signals.
     * @param pca9685 the PCA9685 to use to generate PWM signals.
     * @param channel the channel on the PCA9685 used to generate the PWM signals.
     */
    public PassiveBuzzer(PCA9685 pca9685, PCA9685Channel channel) {
        super(pca9685, channel, PWM_RANGE_GENERATOR);
    }
    
    /**
     * Plays a tone for a duration of millis milliseconds.
     * @param frequency the frequency of the tone.
     * @param millis the duration of the tone.
     */
    public void playTone(double frequency, int millis) {
        setFrequency(frequency);
        pulse(millis);
    }
    
    /**
     * Plays a note, specified from a {@link BaseNote} and an octave,
     * for a duration of millis milliseconds.
     * @param baseNote the base note to play.
     * @param octave the octave from the base note to play.
     * @param millis the duration of the note to play.
     */
    public void playNote(BaseNote baseNote, int octave, int millis) {
        playTone(Note.getNoteFrequency(baseNote, octave), millis);
        Tools.debug("Note played: " + baseNote + " (octave)" + octave, Tools.Color.ANSI_YELLOW);
    }
    
    /**
     * Sets the frequency played by the buzzer.
     * @see #pulse(int)
     * @param frequency the new frequency played by the buzzer.
     */
    public void setFrequency(double frequency) {
        setPWMFreq(frequency);
    }
    
    /**
     * Enables the buzzer to play the tone at the currently setted frequency.
     * @param millis the duration of the tone.
     */
    public void pulse(int millis) {
        setPWM(50, millis);
    }
    
    /**
     * Stops the tone played by the buzzer.
     */
    public void stop() {
        setPWM(0);
    }
}
