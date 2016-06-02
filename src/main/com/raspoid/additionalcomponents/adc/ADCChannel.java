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

import com.raspoid.AnalogComponent;

/**
 * Interface representing an input channel on an ADC.
 * 
 * <p>This interface has been created to allow to easily add a new ADC in the framework,
 * and use this ADC with each {@link AnalogComponent}.</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
@FunctionalInterface
public interface ADCChannel {
    
    /**
     * Get the address of the register corresponding to the input channel on the ADC.
     * @return get the address of the register corresponding to the input channel on the ADC.
     */
    public byte getValue();
}
