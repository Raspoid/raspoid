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
package com.raspoid;

import com.pi4j.wiringpi.Gpio;
import com.raspoid.additionalcomponents.PCA9685;
import com.raspoid.additionalcomponents.PCA9685.PCA9685Channel;

/**
 * <b>This class is used as an abstraction for each component using a PWM signal.</b>
 * 
 * <p>To generate PWM signal, you can choose to use either:
 * <ul>
 *  <li>a PWM pin from the Raspberry Pi (only two PWM pins allowing real PWM signals on recent Raspberry Pi).</li>
 *  <li>a PCA9685 (16 output channels per module, but a smaller range of available PWM frequencies).</li>
 * </p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class PWMComponent implements Component {
    
    /**
     * Id used to differentiate the mode used to generated the PWM signal.<br>
     * Here: using a Raspberry Pi PWM pin.
     */
    private static final int RPI_PWM_PIN_MODE = 0;
    
    /**
     * Id used to differentiate the mode used to generated the PWM signal.<br>
     * Here: using a PCA9685.
     */
    private static final int PCA9685_MODE = 1;
    
    /**
     * Mode selected to generate the PWM signal.
     */
    private final int selectedMode;
    
    /**
     * The generator used to define the range of values that can be used to define a signal.
     * <p>E.g. the tick when the signal turn off for each pulse must be between 0 and rangeGenerator.
     * The range generator when using a PCA9685 should preferably be 4096.</p>
     */
    private final int rangeGenerator;
    
    /*
     * Specific to RPI_PWM_PIN_MODE
     */
    
    /**
     * Default Raspberry Pi PWM clock frequency: 19.2MHz.
     */
    public static final int DEFAULT_RPI_PWM_CLOCK_FREQUENCY = 19200000;
    
    /**
     * The Raspberry Pi PWM pin used to generate the PWM signals.
     */
    private PWMPin pin;
    
    /**
     * The WiringPi pin number corresponding to the PWMPin pin.
     * <p>This is used for performance reasons with components requiring microseconds delays.</p>
     * @see #pin
     */
    protected int pinNumber;
    
    /*
     * Specific to PCA9685_MODE
     */
    
    /**
     * With the PCA9685, a pulse is composed of 4096 ticks (12bits value).
     * The recommended value for the range generator is then 4096.
     * You can use another value, but we will round your range generator to the 0..4096 range.
     */
    public static final int PCA9685_PULSE_TICKS = 4096;
    
    /**
     * The PCA9685 used to generate the PWM signals.
     */
    private PCA9685 pca9685;
    
    /**
     * The output channel, on the PCA9685, used to generate the PWM signals.
     */
    private PCA9685Channel channel;
    
    private boolean stop = false;
        
    /**
     * Constructor for a PWM component, using a PCA9685 module to generate PWM signals.
     * <p><b>Advantage:</b> 16 channels on each PCA9685.</p>
     * <p><b>Disadvantage:</b> target frequency must be an integer values, only between 40 and 1000Hz.</p>
     * @param pca9685 the pca9685 module used to generate PWM signals.
     * @param channel the target channel on the pca9685 to generate the PWM signals.
     * @param rangeGenerator the value used to define the range of values that can be used 
     *  to define a signal. The tick when the signal turn off for each pulse must then be between 0 and rangeGenerator.
     *  <b>! Attention !</b> when using the PCA9685, the maximum rangeGenerator is 4096 (maximum 12-bits value). Must be >= 1.
     */
    public PWMComponent(PCA9685 pca9685, PCA9685Channel channel, int rangeGenerator) {
        this.selectedMode = PCA9685_MODE;
        this.pca9685 = pca9685;
        this.channel = channel;
        this.rangeGenerator = Math.max(rangeGenerator, 1); // 12-bit value (cfr datasheet)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {stop = true;setPWM(0);}));
    }
    
    /**
     * Constructor for a PWM component, using a RPi hardware PWM pin to generate PWM signals.
     * <p><b>Advantage:</b> wide range of PWM frequencies, with continuous values.</p>
     * <p><b>Disadvantage:</b> only 2 hardware PWM pins on recent RPi.</p>
     * @param pin the PWMPin used to generate PWM signal.
     * @param rangeGenerator the value used to define the range of values that can be used 
     *  to define a signal. The tick when the signal turn off for each pulse must then be between 0 and rangeGenerator.
     *  Must be >= 1.
     * @see PWMPin
     */
    public PWMComponent(PWMPin pin, int rangeGenerator) {
        this.selectedMode = RPI_PWM_PIN_MODE;
        this.pin = pin;
        this.pinNumber = pin.getPin().getWiringPiNb();
        this.rangeGenerator = Math.max(rangeGenerator, 1);
        
        // Gpio setup
        Gpio.wiringPiSetup(); // we need to initialize wiringPi
        Gpio.pinMode(pin.getPin().getWiringPiNb(), Gpio.PWM_OUTPUT);
        Gpio.pwmSetMode(Gpio.PWM_MODE_MS);
        Gpio.pwmSetRange(rangeGenerator);
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {stop = true;setPWM(0);}));
    }
    
    /**
     * Sets the PWM frequency, which determines how many full pulses per second are generated.
     * <p>Depending on the selected mode (Raspberry Pi PWM pin or PCA9685), the maximum available frequency 
     * is different.</p>
     * @param frequency the new PWM frequency.
     */
    public void setPWMFreq(double frequency) {
        Tools.debug("New PWM frequency: " + (int)frequency, Tools.Color.ANSI_RED);
        if(selectedMode == PCA9685_MODE) {
            pca9685.setPWMFreq((int)frequency);
        } else if(selectedMode == RPI_PWM_PIN_MODE) {
            // The clock divisor is the divisor to reach the target frequency.
            // This value is stored in the corresponding PWM register, and used
            // by the Raspberry Pi hardware to generate the PWM signal.
            // Note: to get the current clockFrequencyPWM to multiply with this divisor,
            // you can use SystemInfo.getClockFrequencyPWM().
            int pwmClockDivisor = (int) (DEFAULT_RPI_PWM_CLOCK_FREQUENCY / (frequency * rangeGenerator));
            Gpio.pwmSetClock(pwmClockDivisor);
        }
    }
    
    /**
     * Sets for each full pulse of the PWM signal, the tick where the signal turns off (low).
     * 
     * <p>The rangeGenerator is used to define the range of values that the off tick can take.
     * As an example, a rangeGenerator of 20000 and an off value of 5000 means that
     * each PWM full pulse is composed of 25% high signal, followed by 75% of low signal.</p>
     * 
     * <p><b>! Attention !</b> when using the PCA9685, the maximum rangeGenerator is 4096 (maximum 12-bits value).</p>
     * 
     * <p>If you use a higher value for the off than for the rangeGenerator, we will set
     * the off value to the rangeGenerator value.</p>
     * 
     * <p>Note that an off value of zero will stop the PWM signal.</p>
     * @param off the tick where the signal turns off (low). 0 to stop the PWM signal.
     * @see PWMComponent#setPWM(int, long)
     */
    public void setPWM(int off) {
        off = Math.max(off, 0);
        off = Math.min(off, rangeGenerator);
        
        if(stop)
            off = 0;
        
        if(selectedMode == PCA9685_MODE) {
            // with the PCA9685, a pulse is composed of 4096 ticks (12bits).
            // the range of values for the on, off ticks, must then be adapted to this 0..4096 range.
            off = (int)(((double)off / (double)rangeGenerator) * PCA9685_PULSE_TICKS);
            
            pca9685.setPWM(channel, 0, off);
        } else if(selectedMode == RPI_PWM_PIN_MODE) {
            Gpio.pwmWrite(pin.getPin().getWiringPiNb(), off);
        }
    }
    
    /**
     * Uses the {@link PWMComponent#setPWM(int)} method to set the PWM signal with the off parameter,
     * for a duration of millis milliseconds, and then stops the signal.
     * @param off the tick where the signal turns off (low).
     * @param millis the duration of the PWM signal, in milliseconds.
     * @see PWMComponent#setPWM(int)
     */
    public void setPWM(int off, long millis) {
        setPWM(off);
        Tools.sleepMilliseconds(millis);
        setPWM(0);
    }
    
    @Override
    public String getType() {
        return "PWMComponent";
    }
}
