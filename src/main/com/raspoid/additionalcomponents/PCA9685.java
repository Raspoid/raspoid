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

import com.raspoid.I2CComponent;
import com.raspoid.Tools;

/**
 * A PCA9685 can be used to generated PWM signals. It's composed of 16 independent channels
 * (16 output PWM signals), and is directly controlled through i2c.
 * 
 * <p>Datasheet: <a href="http://raspoid.com/download/datasheet/PCA9685">PCA9685</a></p>
 * 
 * <p>Each channel has 4 registers of 8 bits:
 *  <ul>
 *      <li>ON_L</li>
 *      <li>ON_H</li>
 *      <li>OFF_L</li>
 *      <li>OFF_H</li>
 *  </ul>
 * </p>
 * 
 * <p>The implementation of this component is mainly based on:
 *  <ul>
 *      <li>the datasheet</li>
 *      <li>the python driver from Adafruit</li>
 *      <a href="https://github.com/adafruit/Adafruit-Raspberry-Pi-Python-Code/blob/master/Adafruit_PWM_Servo_Driver/Adafruit_PWM_Servo_Driver.py">Adafruit Adafruit_PWM_Servo_Driver.py</a>
 *  </ul>
 * </p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class PCA9685 extends I2CComponent {
    
    /**
     * Defautl i2c address for the PCA9685 component.
     */
    public static final int DEFAULT_I2C_ADDRESS = 0x40;
    
    /*
     * [datasheet - p.10-13] Registers addresses.
     */
    
    /**
     * <b>[datasheet - p.10]</b> Register address.
     * <p>Mode register 1.</p>
     */
    public static final int MODE1 = 0x00;
    
    /**
     * <b>[datasheet - p.10]</b> Register address.
     * <p>Mode register 2.</p>
     */
    public static final int MODE2 = 0x01;
    
    /**
     * <b>[datasheet - p.10]</b> Register address.
     * <p>i2c-bus subaddress 1.</p>
     */
    public static final int SUBADR1 = 0x02;
    
    /**
     * <b>[datasheet - p.10]</b> Register address.
     * <p>i2c-bus subaddress 2.</p>
     */
    public static final int SUBADR2 = 0x03;
    
    /**
     * <b>[datasheet - p.10]</b> Register address.
     * <p>i2c-bus subaddress 3.</p>
     */
    public static final int SUBADR3 = 0x04;
    
    /**
     * <b>[datasheet - p.10]</b> Register address.
     * <p>LED All Call i2c-bus address.</p>
     */
    public static final int ALLCALLADR = 0x05;
    
    /**
     * <b>[datasheet - p.10]</b> Register address.
     * <p>LED0 output and brightness control byte 0.</p>
     * <p>Equivalent for other LEDs can be accessed with: (for LED number LED_NB)
     * <pre>LED0_ON_L + LED_NB * REGISTERS_PER_LED</pre></p>
     */
    public static final int LED0_ON_L = 0x06;
    
    /**
     * <b>[datasheet - p.10]</b> Register address.
     * <p>LED0 output and brightness control byte 0.</p>
     * <p>Equivalent for other LEDs can be accessed with: (for LED number LED_NB)
     * <pre>LED0_ON_H + LED_NB * REGISTERS_PER_LED</pre></p>
     */
    public static final int LED0_ON_H = 0x07;
    
    /**
     * <b>[datasheet - p.10]</b> Register address.
     * <p>LED0 output and brightness control byte 0.</p>
     * <p>Equivalent for other LEDs can be accessed with: (for LED number LED_NB)
     * <pre>LED0_OFF_L + LED_NB * REGISTERS_PER_LED</pre></p>
     */
    public static final int LED0_OFF_L = 0x08;
    
    /**
     * <b>[datasheet - p.10]</b> Register address.
     * <p>LED0 output and brightness control byte 0.</p>
     * <p>Equivalent for other LEDs can be accessed with: (for LED number LED_NB)
     * <pre>LED0_OFF_H + LED_NB * REGISTERS_PER_LED</pre></p>
     */
    public static final int LED0_OFF_H = 0x09;
    
    /**
     * <b>[datasheet - p.10]</b> Register address.
     * <p>Number of registers used for each of the 16 LEDs.</p>
     */
    public static final int REGISTERS_PER_LED = 4;
    
    /**
     * <b>[datasheet - p.13]</b> Register address.
     * <p>Load all the LEDn_ON registers, byte 0.</p>
     */
    public static final int ALL_LED_ON_L = 0xFA;
    
    /**
     * <b>[datasheet - p.13]</b> Register address.
     * <p>Load all the LEDn_ON registers, byte 1.</p>
     */
    public static final int ALL_LED_ON_H = 0xFB;
    
    /**
     * <b>[datasheet - p.13]</b> Register address.
     * <p>Load all the LEDn_OFF registers, byte 0.</p>
     */
    public static final int ALL_LED_OFF_L = 0xFC;
    
    /**
     * <b>[datasheet - p.13]</b> Register address.
     * <p>Load all the LEDn_OFF registers, byte 1.</p>
     */
    public static final int ALL_LED_OFF_H = 0xFD;
    
    /**
     * <b>[datasheet - p.13]</b> Register address.
     * <p>Prescaled for PWM output frequency.</p>
     * <p>The PRE_SCALE register defines the frequency at which the outputs modulate. The
     * prescale value is determined with the formula shown in Equation 1, p.25 in datasheet.
     * <pre>prescale_value = round(osc_clock/(4096*update_rate) -1</pre></p>
     */
    public static final int PRE_SCALE = 0xFE;
    
    /*
     * [datasheet - p.14] Mode register 1, MODE1 (enable commands)
     */
    
    /**
     * <b>[datasheet - p.14]</b> Mode register 1, MODE1.
     * <p>Restart enabled.</p>
     */
    public static final int RESTART = 0x80;
    
    /**
     * <b>[datasheet - p.14]</b> Mode register 1, MODE1.
     * <p>Use EXTCLK pin clock.</p>
     */
    public static final int EXTCLK = 0x40;
    
    /**
     * <b>[datasheet - p.14]</b> Mode register 1, MODE1.
     * <p>Register Auto-Increment enabled.</p>
     */
    public static final int AI = 0x20;
    
    /**
     * <b>[datasheet - p.14]</b> Mode register 1, MODE1.
     * <p>Low power mode. Oscillator off.</p>
     */
    public static final int SLEEP = 0x10;
    
    /**
     * <b>[datasheet - p.14]</b> Mode register 1, MODE1.
     * <p>PCA9685 responds to I2C-bus subaddress 1.</p>
     */
    public static final int SUB1 = 0x08;
    
    /**
     * <b>[datasheet - p.14]</b> Mode register 1, MODE1.
     * <p>PCA9685 responds to I2C-bus subaddress 2.</p>
     */
    public static final int SUB2 = 0x04;
    
    /**
     * <b>[datasheet - p.14]</b> Mode register 1, MODE1.
     * <p>PCA9685 responds to I2C-bus subaddress 3.</p>
     */
    public static final int SUB3 = 0x02;
    
    /**
     * <b>[datasheet - p.14]</b> Mode register 1, MODE1.
     * <p>PCA9685 responds to LED All Call I2C-bus address.</p>
     */
    public static final int ALLCALL = 0x01;
    
    /*
     * [datasheet - p.16] Mode register 2, MODE2 (enable commands)
     */
    
    /**
     * <b>[datasheet - p.16]</b> Mode register 2, MODE2.
     * <p>Output logic state inverted. Value to use when no external driver used.
     * Applicable when OE = 0.</p>
     */
    public static final int INVRT = 0x10;
    
    /**
     * <b>[datasheet - p.16]</b> Mode register 2, MODE2.
     * <p>Outputs change on ACK.</p>
     */
    public static final int OCH = 0x08;
    
    /**
     * <b>[datasheet - p.16]</b> Mode register 2, MODE2.
     * <p>The 16 LEDn outputs are configured with a totem pole structure.</p>
     */
    public static final int OUTDRV = 0x04;
    
    /**
     * <b>[datasheet - p.16]</b> Mode register 2, MODE2.
     * <p>When OE = 1 (output drivers not enabled):
     *  <ul>
     *      <li>LEDn = 1 when OUTDRV = 1</li>
     *      <li>LEDn = high-impedance when OUTDRV = 0 (same as OUTNE[1:0] = 10)</li>
     *  </ul>
     * </p>
     */
    public static final int OUTNE = 0x01;
    
    /**
     * Maximum frequency for the PWM signals generated by the PCA9685, in Hz.
     */
    public static final int MAX_FREQUENCY = 1000;
    
    /**
     * Minimum frequency for the PWM signals generated by the PCA9685, in Hz.
     */
    public static final int MIN_FREQUENCY = 40;
    
    /**
     * Default constructor for a PCA9685 using the default i2c address of the component.
     */
    public PCA9685() {
        this(DEFAULT_I2C_ADDRESS);
    }
    
    /**
     * Constructor for a PCA9685 using a specific i2c address.
     * @param i2cAddress the i2c address of the PCA9685.
     */
    public PCA9685(int i2cAddress) {
        super(i2cAddress);
        setAllPWM(0, 0);
        writeUnsignedValueToRegister(MODE2, OUTDRV);
        writeUnsignedValueToRegister(MODE1, ALLCALL);
        Tools.sleepMilliseconds(5);
        
        int mode1 = readUnsignedRegisterValue(MODE1);
        mode1 = mode1 & ~SLEEP;
        writeUnsignedValueToRegister(MODE1, mode1);
        Tools.sleepMilliseconds(5);
    }
    
    /**
     * Sets the PWM frequency, which determines how many full pulses per second are generated by the module.
     * @param frequency the frequency in Hz, in the {@link #MAX_FREQUENCY}..{@link #MIN_FREQUENCY} range.
     */
    public void setPWMFreq(int frequency) {
        if(frequency > MAX_FREQUENCY)
            Tools.debug("[PCA9685] Max frequency: " + MAX_FREQUENCY + "Hz. " + frequency + " requested.", Tools.Color.ANSI_RED);
        if(frequency < MIN_FREQUENCY)
            Tools.debug("[PCA9685] Min frequency: " + MIN_FREQUENCY + "Hz. " + frequency + " requested.", Tools.Color.ANSI_RED);
        frequency = Math.max(frequency, MIN_FREQUENCY);
        frequency = Math.min(frequency, MAX_FREQUENCY);
        
        // pre_scale calculation: defines the frequency at which the outputs modulate.
        // The prescale value is determined with the formula shown in Equation 1, p.25 in datasheet.
        double preScale = 25000000.; // 25 MHz
        preScale /= 4096.; // 12-bit
        preScale /= (double)frequency;
        preScale -= 1.;
        preScale = Math.floor(preScale + .5);
        int oldMode = readUnsignedRegisterValue(MODE1);
        int newMode = (oldMode & 0x7F) | 0x10; // sleep
        writeUnsignedValueToRegister(MODE1, newMode); // go to sleep
        writeUnsignedValueToRegister(PRE_SCALE, (int)Math.floor(preScale)); // set pwm frequency
        writeUnsignedValueToRegister(MODE1, oldMode); // go back to previous mode
        Tools.sleepMilliseconds(5);
        writeUnsignedValueToRegister(MODE1, oldMode | 0x80);
    }
    
    /**
     * Sets the start (on) and end (off) of the high segment of the PWM pulse, on a specific channel.
     * You then specify WHEN the PWM signal will turn on, and WHEN it will turn off for each pulse.
     * (! You don't set the PWM frequency itself !).
     * @param channel the channel that should be updated with the new values.
     * @param on the tick, between 0 and 4096, when the signal should transition from low to high.
     * @param off the tick, between on and 4096, when the signal should transition from high to low.
     * @see #setAllPWM(int, int)
     */
    public void setPWM(PCA9685Channel channel, int on, int off) {
        on = Math.max(on, 0);
        on = Math.min(on, 4096);
        off = Math.max(off, on);
        off = Math.min(off, 4096);
        
        writeUnsignedValueToRegister(LED0_ON_L + REGISTERS_PER_LED * channel.getValue(), on & 0xFF); // 0xFF to take the 8 lowest bits
        writeUnsignedValueToRegister(LED0_ON_H + REGISTERS_PER_LED * channel.getValue(), on >> 8); // >> 8 to take the 8 highest bits
        writeUnsignedValueToRegister(LED0_OFF_L + REGISTERS_PER_LED * channel.getValue(), off & 0xFF); // 0xFF to take the 8 lowest bits
        writeUnsignedValueToRegister(LED0_OFF_H + REGISTERS_PER_LED * channel.getValue(), off >> 8); // >> 8 to take the 8 highest bits
    }
    
    /**
     * Sets the start and end of the high segment of the PWM pulse, for all the PWM channels of the PCA9685.
     * @param on the tick, between 0 and 4096, when the signal should transition from low to high.
     * @param off the tick, between on and 4096, when the signal should transition from high to low.
     * @see #setPWM(PCA9685Channel, int, int)
     */
    public void setAllPWM(int on, int off) {
        writeUnsignedValueToRegister(ALL_LED_ON_L, on & 0xFF); // 0xFF to take the 8 lowest bits
        writeUnsignedValueToRegister(ALL_LED_ON_H, on >> 8); // >> 8 to take the 8 highest bits
        writeUnsignedValueToRegister(ALL_LED_OFF_L, off & 0xFF); // 0xFF to take the 8 lowest bits
        writeUnsignedValueToRegister(ALL_LED_OFF_H, off >> 8); // >> 8 to take the 8 highest bits
    }
    
    /**
     * 16 channels are available on the PCA9685 module,
     * numbered from 0 to 15.
     */
    public enum PCA9685Channel {
        /**
         * Channel 0.
         */
        CHANNEL_00(0x00),
        
        /**
         * Channel 1.
         */
        CHANNEL_01(0x01),
        
        /**
         * Channel 2.
         */
        CHANNEL_02(0x02),
        
        /**
         * Channel 3.
         */
        CHANNEL_03(0x03),
        
        /**
         * Channel 4.
         */
        CHANNEL_04(0x04),
        
        /**
         * Channel 5.
         */
        CHANNEL_05(0x05),
        
        /**
         * Channel 6.
         */
        CHANNEL_06(0x06),
        
        /**
         * Channel 7.
         */
        CHANNEL_07(0x07),
        
        /**
         * Channel 8.
         */
        CHANNEL_08(0x08),
        
        /**
         * Channel 9.
         */
        CHANNEL_09(0x09),
        
        /**
         * Channel 10.
         */
        CHANNEL_10(0x0A),
        
        /**
         * Channel 11.
         */
        CHANNEL_11(0x0B),
        
        /**
         * Channel 12.
         */
        CHANNEL_12(0x0C),
        
        /**
         * Channel 13.
         */
        CHANNEL_13(0x0D),
        
        /**
         * Channel 14.
         */
        CHANNEL_14(0x0E),
        
        /**
         * Channel 15.
         */
        CHANNEL_15(0x0F);
        
        int channel;
        
        private PCA9685Channel(int channel) {
            this.channel = channel;
        }
        
        /**
         * Get the value corresponding to the channel number, in the 0..15 range.
         * @return the channel number of the PCA9685 channel, il the 0..15 range.
         */
        public int getValue() {
            return channel;
        }
    }
}
