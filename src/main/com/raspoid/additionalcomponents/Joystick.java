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

import com.raspoid.AnalogComponent;
import com.raspoid.additionalcomponents.adc.ADC;
import com.raspoid.additionalcomponents.adc.ADCChannel;
import com.raspoid.examples.additionalcomponents.JoystickExample;

/**
 * Implementation of a Joystick PS2 module.
 * 
 * <p>Example of module: <a href="http://www.sunfounder.com/joystick-ps2-module.html">http://www.sunfounder.com/joystick-ps2-module.html</a></p>
 * 
 * <p>To decode the signals received from this joystick module, we need to use an ADC (Analog to Digital Converter).
 * We then decode the x, y and bt signals from analogic to digital.</p>
 * 
 * @see ADC
 * 
 * <p>Example datasheet: <a href="http://raspoid.com/download/datasheet/Joystick">Joystick</a></p>
 * 
 * <p>Example of use: {@link JoystickExample}</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class Joystick extends AnalogComponent {
    
    /**
     * Channel on the ADC corresponding to variations on the X axis.
     */
    private ADCChannel xChannel;
    
    /**
     * Channel on the ADC corresponding to variations on the Y axis.
     */
    private ADCChannel yChannel;
    
    /**
     * Channel on the ADC corresponding to variations on the push button.
     */
    private ADCChannel btChannel;
    
    /**
     * Constructor for a joystick using a specifc ADC instance, and specific channels on this ADC
     * for the x, y and bt variations of the joystick.
     * @param adc the ADC to use to decode analogic signals from the joystick.
     * @param xChannel the channel on the ADC used to read variations on the x axis of the joystick.
     * @param yChannel the channel on the ADC used to read variations on the y axis of the joystick.
     * @param btChannel the channel on the ADC used to read variations on the push button of the joystick.
     */
    public Joystick(ADC adc, ADCChannel xChannel, ADCChannel yChannel, ADCChannel btChannel) {
        super(adc);
        this.xChannel = xChannel;
        this.yChannel = yChannel;
        this.btChannel = btChannel;
    }
    
    /**
     * Returns the main current position of the joystick among (by priority):
     * <pre>
     *  1. press-down
     *  2. up/down
     *  4. left/right
     *  5. home
     * </pre>
     *  
     * <p>Note: "by priority" means <code>"if 'home' and 'press-down', 'press-down' returned"</code>.</p>
     * @return the main current position of the joystick
     */
    public String getPosition() {
        if(adc.analogToDigital(btChannel) == 0)
            return "press-down";
        
        int x = adc.analogToDigital(xChannel);
        if(x < 7)
            return "up";
        else if(x > 248)
            return "down";
        
        int y = adc.analogToDigital(yChannel);
        if(y < 7)
            return "left";
        else if(y > 248)
            return "right";
        
        return "home";
    }
    
    /**
     * Returns the (x, y) position of the joystick, or (-1,-1) if button pressed.
     * <p>x and y values are between 0 and 255.</p>
     * @return the (x,y) position of the joystick (with x and y in the 0..255 range). (-1,-1) if the button is pressed.
     */
    public int[] getXYPosition() {
        if(adc.analogToDigital(btChannel) == 0)
            return new int[]{-1, -1};
        
        return new int[]{adc.analogToDigital(xChannel), adc.analogToDigital(yChannel)};
    }
    
    /**
     * Returns an array containing raw digital outputs respectively for
     * <ul>
     *  <li>variations on the X axis</li>
     *  <li>variations on the Y axis</li>
     *  <li>variations on the push button</li>
     * </ul>
     * @return an array containing raw digital output vaules for [x axis, y axis, push button].
     */
    public int[] getRawData() {
        return new int[] {adc.analogToDigital(xChannel), adc.analogToDigital(yChannel), adc.analogToDigital(btChannel)};
    }
}
