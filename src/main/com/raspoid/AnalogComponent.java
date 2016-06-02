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

import com.raspoid.additionalcomponents.adc.ADC;

/**
 * <b>This class is used as an abstraction for each analog component.</b>
 * 
 * <p>An analog component needs to be used with an ADC (Analog to Digital Converter) to convert 
 * the analog signals to digital ones that can be decoded by the Raspberry Pi.</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class AnalogComponent implements Component {
    
    /**
     * The ADC (Analogic to Digital converter) to use with this analog component.
     */
    protected ADC adc;
    
    /**
     * Constructor for a new analog component, connected to the Raspberry Pi through an ADC.
     * @param adc the ADC used to concert analog signals to digital ones.
     */
    public AnalogComponent(ADC adc) {
        this.adc = adc;
    }
    
    /**
     * Get the ADC entity used to deal with this analog component.
     * @return the ADC entity used to deal with the analog component.
     */
    public ADC getADC() {
        return adc;
    }
    
    @Override
    public String getType() {
        return "AnalogComponent";
    }
}
