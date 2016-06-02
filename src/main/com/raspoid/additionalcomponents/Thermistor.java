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

import com.raspoid.AnalogComponent;
import com.raspoid.additionalcomponents.adc.ADC;
import com.raspoid.additionalcomponents.adc.ADCChannel;

/**
 * An NTC (negative temperature coefficient) thermistor changes its effective resistance
 * with regard to temperature. To measure a temperature, we then need to measure the value
 * of this resistance.
 * 
 * <p>To do this, we use a an ADC (Anolog to Digital Converter) to measure the 
 * voltage value around the thermistor. We then calculate the value of the thermistor 
 * resistance, using the voltage divider in place with a resistor in serie with the thermistor.</p>
 * 
 * <p>We can then convert the resistance value with the Steinhart-Hart equation.
 *  <pre>1/T = a + b ln(R) + c ln(R)^3</pre>
 * As we don't know all the requested parameters (A, B and C), we will use the B-parameter equation,
 * which is essentially the Steinhart-Hart equation with
 *  <pre>
 *  a = (1/T0) - (1/B) ln(R0)
 *  b = 1/B
 *  c = 0
 *  </pre>
 * </p>
 * 
 * <p><b>Note:</b> with our observations, we can observe an error of about 1.4% in temperature measurements
 * (using the Kelvin scale). A temperature of 24°C should correspond to a real temperature of 20°C.
 * Different factors can be at the origin of this problem.
 * It's important to keep this in mind, and use a thermistor for appropriate use.
 * As an example, a thermistor can be used as a trigger to track a temperature exceeding a fixed threshold:
 * alert when the tap water is too hot. It is less appropriate to precisely define the room temperature.</p>
 * 
 * <p>Formulas from: <a href="https://en.wikipedia.org/wiki/Thermistor">https://en.wikipedia.org/wiki/Thermistor</a></p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class Thermistor extends AnalogComponent {
    
    /**
     * T0 coefficient from the B-parameter equalition, in K.
     * <p>298.15K = 25°C.</p>
     */
    public static final double T0 = 298.15; // K
    
    /**
     * Temperature in °C is temperature in K - K_TO_C.
     */
    public static final double K_TO_C = 273.15; // K
    
    /**
     * The power voltage used for this module, in V.
     * <p>Note: this value cancels itself in the calculations.
     * We keep it for a better understanding in calculations.</p>
     */
    private static final double POWER_VOLTAGE = 3.3; // V
    
    private ADCChannel thermistorChannel;
    
    /**
     * Value of the resistance used in serie with the thermistor, in Ohm.
     */
    private double serieResistor; // Ohm
    
    /**
     * Resistance at 25 degrees Celsius, in Ohm.
     */
    private double r0; // Ohm
    
    /**
     * B coefficient of the B-parameter equation, for this thermistor, in K.
     */
    private double b; // K
    
    /**
     * Constructor for a thermistor using a specific ADC instance, a specific channel on this ADC,
     * and specific parameters regarding the thermistor characteristics.
     * @param adc the ADC to use to decode analog voltage values around the thermistor.
     * @param thermistorChannel the channel used on the ADC to read voltage values around the thermistor.
     * @param serieResistor the value of the resistance placed in serie with the thermistor.
     * @param r0 the resistance of the thermistor, at 25 degrees Celsius (cfr thermistor datasheet).
     * @param b the B-parameter equation coefficient (cfr thermistor datasheet).
     */
    public Thermistor(ADC adc, ADCChannel thermistorChannel, double serieResistor, double r0, double b) {
        super(adc);
        this.thermistorChannel = thermistorChannel;
        this.serieResistor = serieResistor;
        this.r0 = r0;
        this.b = b;
    }
    
    /**
     * Return the measured temperature, in °C.
     * 
     * <p>Keep in mind the note presented in this class header: {@link Thermistor}</p>
     * 
     * @return the measured temperature, in °C.
     */
    public double getTemperature() {
        // raw value from ADC (between 0 and 255).
        double rawValue = (double) adc.analogToDigital(thermistorChannel);
        
        // convert the raw value to a voltage.
        // 0 = 0V, 255 = Max voltage. So
        double vt = POWER_VOLTAGE * rawValue / 255.; // voltage around thermistor
        
        // thermistor value is then (voltage divider)
        double rt = serieResistor * vt / (POWER_VOLTAGE - vt);
        
        // B-equation to convert rt in temperature (https://en.wikipedia.org/wiki/Thermistor)
        double c1 = (1./b) * Math.log(rt / r0);
        double c2 = 1./T0;
        
        double t = 1./(c1 + c2); // K
        // we then convert the result in degrees Celsius.
        return t - K_TO_C;
    }
}
