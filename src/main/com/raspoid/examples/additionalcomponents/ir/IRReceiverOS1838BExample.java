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
package com.raspoid.examples.additionalcomponents.ir;

import com.raspoid.GPIOPin;
import com.raspoid.Tools;
import com.raspoid.additionalcomponents.ir.IRProtocolSunfounderMediaRemote;
import com.raspoid.additionalcomponents.ir.IRReceiverOS1838B;
import com.raspoid.additionalcomponents.ir.IRSignal;

/**
 * Example of use of an IRReceiver OS1838B.
 * 
 * @see IRReceiverOS1838B
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class IRReceiverOS1838BExample {
    
    /**
     * Private constructor to hide the implicit public one.
     */
    private IRReceiverOS1838BExample() {
    }
    
    /**
     * Command-line interface.
     * @param args unused here.
     */
    public static void main(String[] args) {
        IRReceiverOS1838B irReceiver = new IRReceiverOS1838B(GPIOPin.GPIO_00);
        
        while(true) {
            IRSignal newSignal = irReceiver.detectSignal();
            
            IRSignal signalDecoded = irReceiver.decodeIRSignal(new IRProtocolSunfounderMediaRemote(), newSignal);
            if(signalDecoded != null)
                Tools.log("New signal received and decoded: " + signalDecoded.getName());
            else
                Tools.log("New signal received but NOT decoded: " + newSignal);
        }
    }
}
