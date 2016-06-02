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
package com.raspoid.additionalcomponents.ir;

import java.util.ArrayList;
import java.util.List;

import com.raspoid.examples.additionalcomponents.ir.IRSignalDetector;

/**
 * An infrared protocol is a collection a infrared signals.
 * 
 * <p>As an example, we created a new IR protocol for the
 * sunfounder media remote (a simple infrared media remote).
 * Each button of the remote has a corresponding signal.
 * We then detected the signal corresponding to each button
 * (using the {@link IRSignalDetector})
 * and saved this in a new IR protocol: {@link IRProtocolSunfounderMediaRemote}.</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public abstract class IRProtocol {
    
    private List<IRSignal> signals = new ArrayList<>();
    
    /**
     * Constructor for a new empty infrared protocol.
     */
    public IRProtocol() {
    }
    
    /**
     * Add a new infrared signal to the collection of infrared signals
     * composing the protocol.
     * @param signal the new IRSignal to add to the protocol.
     */
    public void addSignal(IRSignal signal) {
        if(!signals.contains(signal))
            signals.add(signal);
    }
    
    /**
     * Get the collection of signals composing the infrared protocol.
     * @return the List of IRSignals composing the infrared protocol.
     */
    public List<IRSignal> getSignals() {
        return signals;
    }
}
