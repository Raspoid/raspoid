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

import com.raspoid.additionalcomponents.adc.ADC;
import com.raspoid.additionalcomponents.adc.ADCChannel;
import com.raspoid.examples.additionalcomponents.ThermistorNTCLE203E3103SB0Example;

/**
 * Implementation of a Thermistor using specific values corresponding to the characteristics
 * of an NTCLE203E3103SB0.
 * 
 * <p>Datasheet: <a href="http://raspoid.com/download/datasheet/ThermistorNTC">Thermistor NTCLE203E3103SB0</a></p>
 * 
 * <p>The module can be applied to temperature detection, temperature control sensor, and ambient temperature detection. 
 * Features:
 *  <ul>
 *      <li>1)  An analog temperature sensor module that consists of an NTC thermistor, resistor, capacitor in a circuit.</li> 
 *      <li>2)  The NTC thermistor is very sensitive to ambient temperature and hence widely used to detect the temperature, with a wide range of temperature measurement.</li> 
 *      <li>3)  Working voltage: 3.3V-5V; PCB size: 2.0 x 2.0 cm</li> 
 *      <li>4)  High measurement accuracy with a wide range, good stability, and strong overload capacity.</li>
 *  </ul>
 * </p>
 * 
 * <p>Example of use: {@link ThermistorNTCLE203E3103SB0Example}</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class ThermistorNTCLE203E3103SB0 extends Thermistor {
    
    /**
     * Resistance at 25 degrees Celsius, in Ohm.
     */
    public static final double R0 = 10000.; // Ohm
    
    /**
     * B coefficient of the B-parameter equation, for this thermistor, in K.
     * <p>B25/85 cfr datasheet p.1, "ELECTRICAL DATA AND ORDERING INFORMATION", line "NTCLE203E3103SB0".</p>  
     */
    public static final double B_PARAMETER = 3984.; // K
    
    /**
     * Constructor for a thermistor NTCLE203E3103SB0 using a specific ADC
     * and a specific channel on this ADC to decode analog voltage values around the thermistor.
     * @param adc the ADC to use to decode analog voltage values around the thermistor.
     * @param inputChannel the channel on the ADC used to read voltage values around the thermistor.
     */
    public ThermistorNTCLE203E3103SB0(ADC adc, ADCChannel inputChannel) {
        super(adc, inputChannel, 10000, R0, B_PARAMETER);
    }
}
