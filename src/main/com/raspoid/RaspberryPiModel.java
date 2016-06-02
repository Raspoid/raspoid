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

/**
 * <b>This enum is used to list the different existing models of Raspberry Pi.</p>
 * 
 * <p>Some specific aspects for the utilization of the framework depends on the version of the RPi where the program is running.</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public enum RaspberryPiModel {
    /**
     * Raspberry Pi Model A. (Feb. 2013)
     * <ul>
     *  <li>ARM1176JZF-S (ARMv6) 700 MHz Broadcom 2835</li>
     *  <li>RAM: 256Mo</li>
     *  <li>GPIO with 26 pins</li>
     * </ul>
     */
    A(1),
    
    /**
     * Raspberry Pi Model A+. (Nov. 2014)
     * <ul>
     *  <li>ARM1176JZF-S (ARMv6) 700 MHz Broadcom 2835</li>
     *  <li>RAM: 256Mo</li>
     *  <li>GPIO with 40 pins</li>
     * </ul>
     * @see RaspberryPiModel#A
     */
    APLUS(1),
    
    /**
     * Raspberry Pi Model B Rev1. (Apr. 2012)
     * <ul>
     *  <li>ARM1176JZF-S (ARMv6) 700 MHz Broadcom 2835</li>
     *  <li>RAM: 256Mo</li>
     *  <li>GPIO with 40 pins</li>
     *  <li>Ethernet 10/100 Mbit/s</li>
     * </ul>
     */
    BREV1(0),
    
    /**
     * Raspberry Pi Model B Rev2. (Jun. 2012)
     * <ul>
     *  <li>ARM1176JZF-S (ARMv6) 700 MHz Broadcom 2835</li>
     *  <li>RAM: 256Mo</li>
     *  <li>GPIO with 40 pins</li>
     *  <li>Ethernet 10/100 Mbit/s</li>
     * </ul>
     * @see RaspberryPiModel#BREV1
     */
    BREV2(1),
    
    /**
     * Raspberry Pi Model B512. (Oct. 2012)
     * <ul>
     *  <li>ARM1176JZF-S (ARMv6) 700 MHz Broadcom 2835</li>
     *  <li>RAM: 512Mo</li>
     *  <li>GPIO with 40 pins</li>
     *  <li>Ethernet 10/100 Mbit/s</li>
     * </ul>
     */
    B512(1),
    
    /**
     * Raspberry Pi Model B+. (Jul. 2014)
     * <ul>
     *  <li>ARM1176JZF-S (ARMv6) 700 MHz Broadcom 2835</li>
     *  <li>RAM: 512Mo</li>
     *  <li>GPIO with 40 pins</li>
     *  <li>Ethernet 10/100 Mbit/s</li>
     * </ul>
     */
    BPLUS(1),
    
    /**
     * Raspberry Pi 2 Model B. (Feb. 2015)
     * <ul>
     *  <li>Broadcom BCM2836, (ARMv7) 900 MHz</li>
     *  <li>RAM: 1Go</li>
     *  <li>GPIO with 40 pins</li>
     *  <li>Ethernet 10/100 Mbit/s</li>
     * </ul>
     */
    PI2(1),
    
    /**
     * Raspberry Pi Zero. (Nov. 2015)
     * <ul>
     *  <li>1 GHz ARM1176JZF-S core (ARM11)</li>
     *  <li>RAM: 512Mo</li>
     *  <li>GPIO with 40 pins</li>
     *  <li>Ethernet 10/100 Mbit/s</li>
     * </ul>
     */
    PIZERO(1),
    
    /**
     * Raspberry Pi 3 Model B. (Feb. 2016)
     * <ul>
     *  <li>Broadcom BCM2837, (ARM Cortex-A53) 1.2GHz</li>
     *  <li>RAM: 1Go</li>
     *  <li>GPIO with 40 pins</li>
     *  <li>Ethernet 10/100 Mbit/s</li>
     * </ul>
     */
    PI3(1);
    
    /**
     * The bus number used by this model of Raspberry Pi.
     */
    int i2cBusNumber;
    
    RaspberryPiModel(int i2cBusNumber) {
        this.i2cBusNumber = i2cBusNumber;
    }
    
    /**
     * Get the bus number used by this model of Raspberry Pi.
     * @return the bus number used by this model of Raspberry Pi.
     */
    public int getBusNumber() {
        return i2cBusNumber;
    }
}
