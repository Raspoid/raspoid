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
import com.raspoid.additionalcomponents.ir.IRProtocol;
import com.raspoid.additionalcomponents.ir.IRProtocolSunfounderMediaRemote;
import com.raspoid.additionalcomponents.ir.IRReceiverOS1838B;

/**
 * Example of use of an infrared detector.
 * 
 * <p>This example allows you to easily detect received infrared signals
 * and get the representation of thoses signals under the Java int[] format required 
 * to easily create new IRProtocol for your app.</p>
 * 
 * @see IRProtocol
 * @see IRProtocolSunfounderMediaRemote
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class IRSignalDetector {
    
    /**
     * Private constructor to hide the implicit public one.
     */
    private IRSignalDetector() {
    }
    
    /**
     * Command-line interface.
     * @param args unused here.
     */
    public static void main(String[] args) {
        IRReceiverOS1838B receiver = new IRReceiverOS1838B(GPIOPin.GPIO_00);
        
        while(true)
            Tools.log(receiver.detectSignal().toJavaIntArrayRepresentation());
    }
}
