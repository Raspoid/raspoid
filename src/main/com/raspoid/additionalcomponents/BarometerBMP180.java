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
import com.raspoid.examples.additionalcomponents.BarometerBMP180Example;

/**
 * The BMP180 barometer is a digital barometric pressure sensor,
 * with a very high performance, which enables applications in advanced 
 * mobile devices, such as smart phones, tablets and sports devices.
 * 
 * <p>Use a barometer to measure air pressure and temperature.</p>
 * 
 * <p>Some specifications:
 *  <ul>
 *      <li>Pressure range: 300 ... 1100hPa</li>
 *      <li>Temperature measurement included</li>
 *      <li>I2C interface</li>
 *      <li>Fully calibrated</li>
 *  </ul>
 * </p>
 * 
 * <p>Datasheet: <a href="http://raspoid.com/download/datasheet/BMP180">BMP180</a></p>
 * 
 * <p><b>[datasheet - p.9]</b>
 * The pressure and temperature data has to be compensated
 * by the callibration data of the E2PROM of the BMP180.<br>
 * The BMP180 consists of a piezo-resistive sensor, an analog to digital
 * converter and a control unit with E2PROM and a serial I2C interface.
 * The BMP180 delivers the uncompensated value of pressure and temperature.
 * The E2PROM has stored 176 bit of individual callibration data.
 * This is used to compensate offset, temperature dependance and other
 * parameters of the sensor.</p>
 * 
 * <ul>
 *  <li>UP = pressure data (16 to 19 bit)</li>
 *  <li>UT = temperature data (16 bit)</li>
 * </ul>
 * 
 * <p>Example of use: {@link BarometerBMP180Example}</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class BarometerBMP180 extends I2CComponent {
    
    /**
     * Default i2c BMP180 address.
     */
    public static final int BMP180_ADDRESS = 0x77;
    
    /**
     * Control register address.
     */
    public static final int CONTROL_REG_ADDR = 0xF4;
    
    /**
     * Value sent to the control register to ask a new temperature
     * value in [OUT_MSB_ADDR, OUT_LSB_ADDR] registers.
     */
    public static final int CONTROL_TEMPERATURE_REQUEST = 0x2E;
    
    /**
     * Value sent to the control register to ask a new pressure 
     * value in [OUT_MSB_ADDR, OUT_LSB_ADDR, OUT_XLSB_ADDR] registers.
     */
    public static final int CONTROL_PRESSURE_REQUEST = 0x34;
    
    /**
     * First register address containing calculated values:
     * respectively temperature or pressure, depending on previous value sent to control register.
     * @see #OUT_LSB_ADDR
     * @see #OUT_XLSB_ADDR
     */
    public static final int OUT_MSB_ADDR = 0xF6;
    
    /**
     * Second register address containing calculated values:
     * respectively temperature or pressure, depending on previous value sent to control register.
     * @see #OUT_MSB_ADDR
     * @see #OUT_XLSB_ADDR
     */
    public static final int OUT_LSB_ADDR = 0xF7;
    
    /**
     * Third register address containing calculated values:
     * respectively temperature or pressure, depending on previous value sent to control register.
     * @see #OUT_MSB_ADDR
     * @see #OUT_LSB_ADDR
     */
    public static final int OUT_XLSB_ADDR = 0xF8;
    
    /**
     * The pressure at sea level, in hPa.
     */
    public static final double PRESSURE_AT_SEA_LEVEL = 1013.25;
    
    /**
     * BMP180 register MSB address of calibration coefficient AC1.
     * (1 address every 16bits (2 bytes) given that each word of data is 16bit long.)
     */
    public static final int BMP180_MSB_REG_ADDR_AC1 = 0xAA;
    
    /**
     * BMP180 register MSB address of calibration coefficient AC2.
     * (1 address every 16bits (2 bytes) given that each word of data is 16bit long.)
     */
    public static final int BMP180_MSB_REG_ADDR_AC2 = 0xAC;
    
    /**
     * BMP180 register MSB address of calibration coefficient AC3.
     * (1 address every 16bits (2 bytes) given that each word of data is 16bit long.)
     */
    public static final int BMP180_MSB_REG_ADDR_AC3 = 0xAE;
    
    /**
     * BMP180 register MSB address of calibration coefficient AC4.
     * (1 address every 16bits (2 bytes) given that each word of data is 16bit long.)
     */
    public static final int BMP180_MSB_REG_ADDR_AC4 = 0xB0;
    
    /**
     * BMP180 register MSB address of calibration coefficient AC5.
     * (1 address every 16bits (2 bytes) given that each word of data is 16bit long.)
     */
    public static final int BMP180_MSB_REG_ADDR_AC5 = 0xB2;
    
    /**
     * BMP180 register MSB address of calibration coefficient AC6.
     * (1 address every 16bits (2 bytes) given that each word of data is 16bit long.)
     */
    public static final int BMP180_MSB_REG_ADDR_AC6 = 0xB4;
    
    /**
     * BMP180 register MSB address of calibration coefficient B1.
     * (1 address every 16bits (2 bytes) given that each word of data is 16bit long.)
     */
    public static final int BMP180_MSB_REG_ADDR_B1 = 0xB6;
    
    /**
     * BMP180 register MSB address of calibration coefficient B2.
     * (1 address every 16bits (2 bytes) given that each word of data is 16bit long.)
     */
    public static final int BMP180_MSB_REG_ADDR_B2 = 0xB8;
    
    /**
     * BMP180 register MSB address of calibration coefficient MB.
     * (1 address every 16bits (2 bytes) given that each word of data is 16bit long.)
     */
    public static final int BMP180_MSB_REG_ADDR_MB = 0xBA;
    
    /**
     * BMP180 register MSB address of calibration coefficient MC.
     * (1 address every 16bits (2 bytes) given that each word of data is 16bit long.)
     */
    public static final int BMP180_MSB_REG_ADDR_MC = 0xBC;
    
    /**
     * BMP180 register MSB address of calibration coefficient MD.
     * (1 address every 16bits (2 bytes) given that each word of data is 16bit long.)
     */
    public static final int BMP180_MSB_REG_ADDR_MD = 0xBE;
        
    /**
     * Value (16bits) of the calibration coefficient AC1 (retrieved at the initalization phase).
     * @see #BMP180_MSB_REG_ADDR_AC1
     */
    private int ac1;
    
    /**
     * Value (16bits) of the calibration coefficient AC2 (retrieved at the initalization phase).
     * @see #BMP180_MSB_REG_ADDR_AC2
     */
    private int ac2;
    
    /**
     * Value (16bits) of the calibration coefficient AC3 (retrieved at the initalization phase).
     * @see #BMP180_MSB_REG_ADDR_AC3
     */
    private int ac3;
    
    /**
     * Value (16bits) of the calibration coefficient AC4 (retrieved at the initalization phase).
     * @see #BMP180_MSB_REG_ADDR_AC4
     */
    private int ac4;
    
    /**
     * Value (16bits) of the calibration coefficient AC5 (retrieved at the initalization phase).
     * @see #BMP180_MSB_REG_ADDR_AC5
     */
    private int ac5;
    
    /**
     * Value (16bits) of the calibration coefficient AC6 (retrieved at the initalization phase).
     * @see #BMP180_MSB_REG_ADDR_AC6
     */
    private int ac6;
    
    /**
     * Value (16bits) of the calibration coefficient B1 (retrieved at the initalization phase).
     * @see #BMP180_MSB_REG_ADDR_B1
     */
    private int b1;
    
    /**
     * Value (16bits) of the calibration coefficient B2 (retrieved at the initalization phase).
     * @see #BMP180_MSB_REG_ADDR_B2
     */
    private int b2;
    
    /**
     * Value (16bits) of the calibration coefficient MB (retrieved at the initalization phase).
     * @see #BMP180_MSB_REG_ADDR_MB
     */
    private int mb;
    
    /**
     * Value (16bits) of the calibration coefficient MC (retrieved at the initalization phase).
     * @see #BMP180_MSB_REG_ADDR_MC
     */
    private int mc;
    
    /**
     * Value (16bits) of the calibration coefficient MD (retrieved at the initalization phase).
     * @see #BMP180_MSB_REG_ADDR_MD
     */
    private int md;
    
    private HardwarePressureAccuracyMode selectedHardwarePressureAccuracyMode;
    
    /**
     * Coefficient calculated with calculateTrueTemperature().
     * <p>Attention: Needed to calculate true pressure !
     * So, we need to calculate true temperature before calculate true pressure !</p>
     */
    private int b5;
    private long lastB5UpdateTime = 0;
    private int b5UpdateTimeout; // ms
    
    /**
     * There exists 4 different hardware accuracy modes to deal with the BMP180 pressure measures.
     * 
     * <p>The mode is selected by driver software via the oss variable.</p>
     * 
     * <b>[datasheet - p.12]</b>
     */
    public enum HardwarePressureAccuracyMode {
        
        /**
         * Ultra low power mode.
         */
        ULTRA_LOW_POWER(0),
        
        /**
         * Standard mode.
         */
        STANDARD(1),
        
        /**
         * High resolution mode.
         */
        HIGH_RESOLUTION(2),
        
        /**
         * Ultra high resolution mode.
         */
        ULTRA_HIGH_RESOLUTION(3);
        
        /**
         * oversampling setting.
         */
        int oss;
        
        HardwarePressureAccuracyMode(int oss) {
            this.oss = oss;
        }
    }
    
    /**
     * Default constructor for a new barometer BMP180 using the default i2c address of the component,
     * the ultra high resolution mode and a minimum timeout before refreshing the temperature value of 1 second.
     * 
     * <p>The timeout for the temperature values here is used to don't refresh the temperature value 
     * (read on the BMP180) too frequently, as recommended by the <b>[datasheet - p.11]</b>.</p>
     */
    public BarometerBMP180() {
        this(BMP180_ADDRESS, HardwarePressureAccuracyMode.ULTRA_HIGH_RESOLUTION, 1000);
    }
    
    /**
     * Constructor for a new barometer BMP180 using a specific i2c address,
     * a specific pressure accuracy mode, and a specific timeout to refresh the temperature value.
     * 
     * <p>The timeout for the temperature values here is used to don't refresh the temperature value 
     * (read on the BMP180) too frequently, as recommended by the <b>[datasheet - p.11]</b>.</p>
     * 
     * @param i2cAddress the i2c address of the BMP180 component.
     * @param selectedHardwarePressureAccuracyMode the pressure accuracy mode.
     * @param temperatureTimeout the minimum timeout before refreshing the temperature value, in milliseconds.
     */
    public BarometerBMP180(int i2cAddress, HardwarePressureAccuracyMode selectedHardwarePressureAccuracyMode, int temperatureTimeout) {
        super(i2cAddress);
        this.selectedHardwarePressureAccuracyMode = selectedHardwarePressureAccuracyMode;
        this.b5UpdateTimeout = temperatureTimeout;
        
        // [datasheet - p.11]
        // For calculating temperature in °C and pressure in hPa, the callibration
        // data has to be used. These constants can be read out from the 
        // BMP180 E2PROM via the I2C interface at software initialization.
        readCalibrationCoefficients();
    }
    
    /**
     * Cfr <b>[datasheet - p.15]</b>.
     */
    private void readCalibrationCoefficients() {
        ac1 = readTwoSignedRegsiters(BMP180_MSB_REG_ADDR_AC1);
        ac2 = readTwoSignedRegsiters(BMP180_MSB_REG_ADDR_AC2);
        ac3 = readTwoSignedRegsiters(BMP180_MSB_REG_ADDR_AC3);
        ac4 = readTwoUnsignedRegisters(BMP180_MSB_REG_ADDR_AC4);
        ac5 = readTwoUnsignedRegisters(BMP180_MSB_REG_ADDR_AC5);
        ac6 = readTwoUnsignedRegisters(BMP180_MSB_REG_ADDR_AC6);
        b1 = readTwoSignedRegsiters(BMP180_MSB_REG_ADDR_B1);
        b2 = readTwoSignedRegsiters(BMP180_MSB_REG_ADDR_B2);
        mb = readTwoSignedRegsiters(BMP180_MSB_REG_ADDR_MB);
        mc = readTwoSignedRegsiters(BMP180_MSB_REG_ADDR_MC);
        md = readTwoSignedRegsiters(BMP180_MSB_REG_ADDR_MD);
        
        Tools.debug("AC1 " + ac1 + " AC2 " + ac2 + " AC3 " + ac3 +
                " AC4 " + ac4 + " AC5 " + ac5 + " AC6 " + ac6 + 
                " B1 " + b1 + " B2 " + b2 + " MB " + mb + " MC " + mc + 
                " MD " + md, Tools.Color.ANSI_YELLOW);
    }
    
    /**
     * Read the uncompensated temperature value from the BMP180.
     * <p><b>[datasheet - p.15]</b></p>
     * @return the uncompensated temperature value from the BMP180.
     */
    public int readUncompensatedTemperature() {
        // [datasheet - p.11]
        // The microcontroller sends a start sequence to start a pressure
        // or temperature measurement. After converting time, the value
        // (UP or UT, respectively) can be read via the I2C interface.
        writeUnsignedValueToRegister(CONTROL_REG_ADDR, CONTROL_TEMPERATURE_REQUEST);
        Tools.sleepMilliseconds(5);
        return readTwoSignedRegsiters(OUT_MSB_ADDR);
    }
    
    /**
     * Read the uncompensated pressure value from the BMP180.
     * <p><b>[datasheet - p.15]</b></p>
     * @return the uncompensated pressure value from the BMP180.
     */
    public int readUncompensatedPressure() {
        // [datasheet - p.11]
        // The microcontroller sends a start sequence to start a pressure
        // or temperature measurement. After converting time, the value
        // (UP or UT, respectively) can be read via the I2C interface.
        int oss = selectedHardwarePressureAccuracyMode.oss;
        writeUnsignedValueToRegister(CONTROL_REG_ADDR, CONTROL_PRESSURE_REQUEST + (oss << 6));
        
        // wait [datasheet - p.12]
        switch(selectedHardwarePressureAccuracyMode) {
        case ULTRA_LOW_POWER:
            Tools.sleepMilliseconds(5); // 4.5ms
            break;
        case STANDARD:
            Tools.sleepMilliseconds(8); // 7.5ms
            break;
        case HIGH_RESOLUTION:
            Tools.sleepMilliseconds(14); // 13.5ms
            break;
        case ULTRA_HIGH_RESOLUTION:
            Tools.sleepMilliseconds(26); // 25.5ms
            break;
        default:
            Tools.log("Unknown hardware pressure accuracy mode selected.", Tools.Color.ANSI_RED);
            break;
        }
        
        int msb = readUnsignedRegisterValue(OUT_MSB_ADDR);
        int lsb = readUnsignedRegisterValue(OUT_LSB_ADDR);
        int xlsb = readUnsignedRegisterValue(OUT_XLSB_ADDR);
        return ((msb << 16) + (lsb << 8) + xlsb) >> (8 - oss);
    }
    
    /**
     * Implements the algorithm pressented in <b>[datasheet - p.15]</b>.
     * and returns the compensated temperature in degree celsius.
     * 
     * <p><b>ATTENTION:</b> the calculation of a new temperature value
     * is limited by the b5UpdateTimeout.
     * This is due to the fact that this value is also used with the
     * calculateTruePressure(), and we don't need to update this value 
     * too frequenty (as recommended in the datasheet - p.11).</p>
     * @return the calculated compensated temperature in degree celsius.
     */
    public double calculateTrueTemperature() {
        long currentTime = System.currentTimeMillis();
        if((currentTime - lastB5UpdateTime) > b5UpdateTimeout) {
            int ut = readUncompensatedTemperature();
            int x1 = ((ut - ac6) * ac5) >> 15; // ">> 15" = "/ 2^15"
            int x2 = (mc << 11) / (x1 + md); // "<< 11" = "* 2^11"
            b5 = x1 + x2;
            lastB5UpdateTime = currentTime;
        }
        return ((b5 + 8) >> 4) / 10.;
    }
    
    /**
     * Implements the algorithm presented in <b>[datasheet - p.15]</b> and
     * returns the compensated pressure in Pa.
     * @return the calculated compensated pressure in Pa.
     */
    public int calculateTruePressure() {
        calculateTrueTemperature(); // will only be executed if B5UpdateTimeout exceeded
        int b6 = b5 - 4000;
        long x1 = (b2 * ((b6 * b6) >> 12)) >> 11;
        long x2 = (ac2 * b6) >> 11;
        long x3 = x1 + x2;
        int oss = selectedHardwarePressureAccuracyMode.oss;
        long b3 = (((ac1 * 4 + x3) << oss) + 2) >> 2;
        x1 = (ac3 * b6) >> 13;
        x2 = (b1 * ((b6 * b6) >> 12)) >> 16;
        x3 = ((x1 + x2) + 2) >> 2;
        long b4 = (ac4 * (x3 + 32768)) >> 15;
        long up = readUncompensatedPressure();
        long b7 = (up - b3) * (50000 >> oss);
        long pressure;
        if(b7 < 0x80000000L)
            pressure = (b7 * 2) / b4;
        else
            pressure = (b7/b4) * 2;
        x1 = (pressure >> 8) * (pressure >> 8);
        x1 = (x1 * 3038) >> 16;
        x2 = (-7357 * pressure) >> 16;
        return (int) (pressure + ((x1 + x2 + 3791) >> 4));
    }
    
    /**
     * With the measured pressure p and the pressure at sea level p0,
     * the altitude in meters can be calculated with the international
     * barometric formula:
     * <pre>
     * altitude = 44330 * (1-(p/p0)^1/5.255)
     * </pre>
     * 
     * <b>[datasheet - p.16]</b>
     * 
     * <p>Note: we recalculate here the calculated true pressure
     * without any sampling rate problem. Indeed, the pressure sampling rate
     * can increase up to 128 samples per second (in standard mode) for dynamic measurements.</p>
     * @return the altitude, in m, with regard to the sea level.
     */
    public double calculateAbsoluteAltitude() {
        int pressure = calculateTruePressure();
        return 44330. * (1. - Math.pow((pressure / 100.) / PRESSURE_AT_SEA_LEVEL, 0.19029));
    }
}
