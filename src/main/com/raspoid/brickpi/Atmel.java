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
package com.raspoid.brickpi;

import java.util.HashMap;
import java.util.Map;
import static com.raspoid.brickpi.BrickPi.*;

/**
 * Addresses of the brick pi Atmel chips (AtMega328).
 * This implementation handles a unique brickPi board.
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public enum Atmel {
    /**
     * To reach the two Atmel composing the BrickPi.
     */
    ALL((byte)0),
    
    /**
     * Address of the first Atmel.
     */
    CHIP1((byte)1),
    
    /**
     * Address of the second Atmel.
     */
    CHIP2((byte)2);

    /**
     * Contains a map with all the Atmel chip accessible by their byte value,
     * this allows to retrieve an Atmel chip object by providing it's byte value.
     */
    private static Map<Byte, Atmel> mapping = new HashMap<>();

    /**
     * Containt the Atmel address byte value.
     */
    private final byte address;

    /**
     * Created the Atmel and byte value mapping.
     */
    static {
        for (Atmel atmel : Atmel.values()) {
            mapping.put(atmel.address, atmel);
        }
    }
    /**
     * Creates a Atmel with its byte value.
     * @param address the address of the Atmel.
     */
    private Atmel(byte address) {
        this.address = address;
    }

    /**
     * Get the Atmel chip address.
     * @return the address byte value.
     */
    public byte getAddress() {
        return address;
    }
    
    /**
     * Get the starting index for the sensors and motors contained in a chip.
     * @return the starting index of the chip.
     */
    public int getIndex() {
        // The chip index is its (address - 1) * 2 because chip
        // address start at index 1 but motors and sensors indexes
        // start at 0
        return (address - 1) * NB_SENSORS_BY_ATMEGA;
    }

    /**
     * Get an Atmel object by giving its byte address.
     * @param address the byte address of the chip.
     * @return a corresponding Atmel object.
     */
    public static Atmel valueOf(byte address) {
        if (!mapping.containsKey(address)) {
            throw new IllegalArgumentException("Atmel id is unknown");
        }
        return mapping.get(address);
    }
}
