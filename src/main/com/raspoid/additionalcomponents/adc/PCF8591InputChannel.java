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

/**
 * Each PCF8591 is composed of 4 input channels, numbered from 0 to 3.
 */
public enum PCF8591InputChannel implements ADCChannel {
    /**
     * Input channel 0.
     */
    CHANNEL_0((byte)0x00),
    
    /**
     * Input channel 1.
     */
    CHANNEL_1((byte)0x01),
    
    /**
     * Input channel 2.
     */
    CHANNEL_2((byte)0x02),
    
    /**
     * Input channel 3.
     */
    CHANNEL_3((byte)0x03);
    
    byte channelNb;
    
    PCF8591InputChannel(byte channelNb) {
        this.channelNb = channelNb;
    }
    
    /**
     * Get the address corresponding to the input channel.
     * @return get the address corresponding to the input channel.
     */
    @Override
    public byte getValue() {
        return channelNb;
    }
}
