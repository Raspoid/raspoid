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
package com.raspoid.examples.additionalcomponents.adc;

import com.raspoid.Tools;
import com.raspoid.additionalcomponents.adc.PCF8591;

/**
 * Example of use of a PCF8591.
 * 
 * @see PCF8591
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class PCF8591Example {
    
    /**
     * Private constructor to hide the implicit public one.
     */
    private PCF8591Example() {
    }
    
    /**
     * Command-line interface.
     * @param args unused here.
     */
    public static void main(String[] args) {
        PCF8591 pcf8591 = new PCF8591();
        
        // D/A
        for(int i=0; i<255; i++) {
            pcf8591.digitalToAnalog(i);
            Tools.sleepMilliseconds(10);
        }
        
        // A/D
        while(true) {
            Tools.log(pcf8591);
            Tools.sleepMilliseconds(250);
        }
    }
}
