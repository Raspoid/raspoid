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
package com.raspoid.additionalcomponents.adc;

import java.io.IOException;

import com.raspoid.I2CComponent;
import com.raspoid.examples.additionalcomponents.adc.PCF8591Example;
import com.raspoid.exceptions.RaspoidException;

/**
 * Implementation of the ADC (Analog to Digital Converter) PCF8591.
 * 
 * <p>Some specifications:
 *  <ul>
 *      <li>Four analog inputs.</li>
 *      <li>One analog output.</li>
 *      <li>A serial I2C-bus interface.</li>
 *      <li>Three address pins A0, A1 and A2 (up to eight devices connected to the i2c bus without additional hardware).</li>
 *  </ul>
 * </p>
 * 
 * <p>Address, control and data to and from the device are transfered serially via the two-line bidirectional I2C-bus.</p>
 * 
 * <p><b>! NOTE !</b> known bug when reading digital values from PCF8591 if analog output disabled ! (independent of this framework)</p>
 * 
 * <p>Datasheet: <a href="http://raspoid.com/download/datasheet/PCF8591">PCF8591</a></p>
 * 
 * <p>Each PCF8591 device in an I2C-bus system is activated by sending a valid address to the device.
 * The address (8 bits) consists of a fixed part (4 bits), a programmable part (3 bits) and a read/write-bit (1 bit) which sets the direction of the following data transfer.
 *  <ul>
 *      <li>Programmable part: set according to the address pins A0, A1 and A2.</li>
 *      <li>The address: always the first byte sent after the start condition in the I2C-bus protocol.</li>
 *  </ul>
 * </p>
 * 
 * <p>Example of use: {@link PCF8591Example}</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class PCF8591 extends I2CComponent implements ADC {

    /**
     * Default i2c PCF8591 address.
     */
    public static final int DEFAULT_PCF8591_ADDRESS = 0x48;
    
    /**
     * Analog output active if bit at position 2 in control byte is 1.
     */
    public static final byte ANALOGUE_OUTPUT_ENABLE_FLAG = 0x40; // 0100 0000
    
    /**
     * There exists 4 different modes to deal with the four analog inputs.
     * <p><b>[datasheet - p.6]</b></p>
     */
    public enum AnalogueInputProgrammingMode {
        /**
         * Analogue input programming mode:
         * four single ended inputs.
         */
        FOUR_SINGLE_ENDED_INPUTS(0x00), // 0000 0000
        
        /**
         * Analogue input programming mode:
         * three differential inputs.
         */
        THREE_DIFFERENTIAL_INPUTS(0x01), // 0000 0001
        
        /**
         * Analogue input programming mode:
         * single ended and differential mixed.
         */
        SINGLE_ENDED_AND_DIFFERENTIAL_MIXED(0x02), // 0000 0010
        
        /**
         * Analogue input programming mode:
         * two differential inputs.
         */
        TWO_DIFFERENTIAL_INPUTS(0x03); // 0000 0011
        
        byte code;
        
        AnalogueInputProgrammingMode(int code) {
            this.code = (byte) code;
        }
        
        byte toByte() {
            return code;
        }
    }
    
    /**
     *  If the auto-increment flag is set to 1, the channel number 
     *  is incremented automatically after each A/D conversion.
     */
    public static final byte AUTO_INCREMENT_FLAG = 0x04; // 0000 0100
    
    /**
     * A/D channel 0.
     * <p>Note: the output of each channel depends on the mode selected for the analogue input progamming.</p>
     * @see #CHANNEL_1
     * @see #CHANNEL_2
     * @see #CHANNEL_3
     */
    public static final PCF8591InputChannel CHANNEL_0 = PCF8591InputChannel.CHANNEL_0; // 0000 0000
    
    /**
     * A/D channel 1.
     * <p>Note: the output of each channel depends on the mode selected for the analogue input progamming.</p>
     * @see #CHANNEL_0
     * @see #CHANNEL_2
     * @see #CHANNEL_3
     */
    public static final PCF8591InputChannel CHANNEL_1 = PCF8591InputChannel.CHANNEL_1; // 0000 0001
    
    /**
     * A/D channel 2.
     * <p>Note: the output of each channel depends on the mode selected for the analogue input progamming.</p>
     * @see #CHANNEL_0
     * @see #CHANNEL_1
     * @see #CHANNEL_3
     */
    public static final PCF8591InputChannel CHANNEL_2 = PCF8591InputChannel.CHANNEL_2; // 0000 0010
    
    /**
     * A/D channel 3.
     * <p>Note: the output of each channel depends on the mode selected for the analogue input progamming.</p>
     * @see #CHANNEL_0
     * @see #CHANNEL_1
     * @see #CHANNEL_2
     */
    public static final PCF8591InputChannel CHANNEL_3 = PCF8591InputChannel.CHANNEL_3; // 0000 0011
    
    /**
     * Mode selected to deal with analog inputs (AIN0-4).
     */
    private AnalogueInputProgrammingMode selectedMode;
    
    private boolean enableAnalogOutput = false;
    private boolean enableAutoIncrement = false;
    
    /**
     * Default constructor for a PCF8591 using the default i2c address of the component,
     * the four single ended analogue input programming mode, enabling the analog output and disabling the auto increment flag.
     */
    public PCF8591() {
        // /!\ We need to set enableAnalogOutput to true cfr bug described in the header.
        this(DEFAULT_PCF8591_ADDRESS, AnalogueInputProgrammingMode.FOUR_SINGLE_ENDED_INPUTS, true, false);
    }
    
    /**
     * Constructor for a PCF8591 using a specific i2c address, a specific analog input programming mode,
     * a specific boolean to enable/disable the analogue output of the PCF8591 and a specific value for the auto increment flag.
     * @param i2cAddress the i2c address of the PCF8591. 
     * @param mode the selected analog input programming mode.
     * @param enableAnalogOutput true to enable the analog output of the PCF8591 (/!\ pay attention to bug described in class header ({@link PCF8591})). False to disable.
     * @param enableAutoIncrement true to enable the auto increment flag. False to disable.
     */
    public PCF8591(int i2cAddress, AnalogueInputProgrammingMode mode, boolean enableAnalogOutput, boolean enableAutoIncrement) {
        super(i2cAddress);
        this.selectedMode = mode;
        this.enableAnalogOutput = enableAnalogOutput;
        this.enableAutoIncrement = enableAutoIncrement;
    }
    
    @Override
    public int analogToDigital(ADCChannel inputChannel) {
        return analogToDigital((PCF8591InputChannel) inputChannel);
    }
        
    /**
     * Read a digital value from the PCF8591 through i2c.
     * 
     * <p>Note:
     *  <ul>
     *      <li>readBuffer[0] contains the previous digital value.</li>
     *      <li>readBuffer[1] contains the new digital value.</li>
     *  </ul>
     * </p>
     * <p><b>[datasheet]</b> The first byte transmitted in a read cycle
     * contains the conversion result code of the previous read cycle. After a POR condition, the first byte read is 80h.</p>
     * @param inputChannel the input channel on the PCF8591 from which to read the data.
     * @return the int representation of the newly read value from the specified analog input channel. 
     */
    public int analogToDigital(PCF8591InputChannel inputChannel) {
        int channel = inputChannel.getValue();
        switch(selectedMode) {
        case FOUR_SINGLE_ENDED_INPUTS:
            if(channel < 0 || channel > 3)
                throw new IllegalArgumentException("In the FOUR_SINGLE_ENDED_INPUTS mode, the analog input number must be in [0, 3]");
            break;
        case THREE_DIFFERENTIAL_INPUTS:
            if(channel < 0 || channel > 2)
                throw new IllegalArgumentException("In the THREE_DIFFERENTIAL_INPUTS mode, the analog input number must be in [0, 2]");
            break;
        case SINGLE_ENDED_AND_DIFFERENTIAL_MIXED:
            if(channel < 0 || channel > 2)
                throw new IllegalArgumentException("In the SINGLE_ENDED_AND_DIFFERENTIAL_MIXED mode, the analog input number must be in [0, 2]");
            break;
        case TWO_DIFFERENTIAL_INPUTS:
            if(channel < 0 || channel > 1)
                throw new IllegalArgumentException("In the TWO_DIFFERENTIAL_INPUTS mode, the analog input number must be in [0, 1]");
            break;
        default:
            throw new IllegalArgumentException("Mode not supported");
        }
        
        // Control byte
        byte controlByte = 0;
        if(enableAnalogOutput)
            controlByte += ANALOGUE_OUTPUT_ENABLE_FLAG;
        controlByte += selectedMode.toByte();
        if(enableAutoIncrement)
            controlByte += AUTO_INCREMENT_FLAG;
        controlByte += (byte) channel;
        
        //Tools.log(Tools.getBinaryString(new byte[] {controlByte}), Tools.Color.ANSI_RED); // DEBUG
        
        byte[] readBuffer = new byte[2];
        try {
            device.write(controlByte);
            device.read(readBuffer, 0, 2);
            //Tools.log(String.valueOf(readBuffer[1]), Tools.Color.ANSI_BLUE); // DEBUG
        } catch (IOException e) {
            throw new RaspoidException(e);
        }
        
        return readBuffer[1] & 0xFF;
    }
    
    /**
     * Send a digital value to the PCF8591 through i2c.
     * <p>This value will be converted in an analog signal by the PCF8591,
     * and this signal will output on the AOUT pin.</p>
     * 
     * <p>Note: the led on the sunfounder PCF8591 custom board will shine
     * accordingly to the analog output voltage (led won't light up below 125).</p>
     * 
     * <p><b>[datasheet - p.7]</b>
     * The third byte sent to a PCF8591 device is stored in the
     * DAC data register and is converted to the corresponding
     * analog voltage using the on-chip D/A converter.</p>
     * 
     * <p><b>[datasheet - p.5-7]</b> protocol: for D/A conversion, after the addressing byte,
     * send the control byte and then the data byte.
     * Note: we use a buffer to send the two consecutive bytes ! (otherwise: bug)</p>
     * 
     * @param value the data value to send on the analog output, in the 0..255 range.
     */
    public void digitalToAnalog(int value) {
        if(value < 0 || value > 255)
            throw new IllegalArgumentException("Value must be in the 0..255 range");
        
        byte controlByte = ANALOGUE_OUTPUT_ENABLE_FLAG;
        byte dataByte = (byte) value;
        byte[] buffer = {controlByte, dataByte};
        
        try {
            device.write(buffer, 0, 2);
        } catch (IOException e) {
            throw new RaspoidException(e);
        }
    }
    
    @Override
    public String toString() {
        String result = "";
        
        String c0 = " CHANNEL_0: ";
        String c1 = " CHANNEL_1: ";
        String c2 = " CHANNEL_2: ";
        String c3 = " CHANNEL_3: ";
        
        switch(selectedMode) {
        case FOUR_SINGLE_ENDED_INPUTS:
            result += "[FOUR_SINGLE_ENDED_INPUTS]" + 
                    c0 + analogToDigital(CHANNEL_0) +
                    c1 + analogToDigital(CHANNEL_1) +
                    c2 + analogToDigital(CHANNEL_2) +
                    c3 + analogToDigital(CHANNEL_3);
            break;
        case THREE_DIFFERENTIAL_INPUTS:
            result += "[THREE_DIFFERENTIAL_INPUTS]" + 
                    c0 + analogToDigital(CHANNEL_0) +
                    c1 + analogToDigital(CHANNEL_1) +
                    c2 + analogToDigital(CHANNEL_2);
            break;
        case SINGLE_ENDED_AND_DIFFERENTIAL_MIXED:
            result += "[SINGLE_ENDED_AND_DIFFERENTIAL_MIXED]" + 
                    c0 + analogToDigital(CHANNEL_0) +
                    c1 + analogToDigital(CHANNEL_1) +
                    c2 + analogToDigital(CHANNEL_2);
            break;
        case TWO_DIFFERENTIAL_INPUTS:
            result += "[TWO_DIFFERENTIAL_INPUTS]" + 
                    c0 + analogToDigital(CHANNEL_0) +
                    c1 + analogToDigital(CHANNEL_1);
            break;
        default:
            throw new IllegalArgumentException("Mode not supported");
        }
        
        return result;
    }
}
