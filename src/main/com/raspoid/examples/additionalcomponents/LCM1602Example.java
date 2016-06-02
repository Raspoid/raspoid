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
package com.raspoid.examples.additionalcomponents;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.raspoid.Tools;
import com.raspoid.additionalcomponents.LCM1602;

/**
 * Example of use of an LCM1602 (LCD display).
 * 
 * <p>With this example, time is simply displayed on the screen.</p>
 * 
 * @see LCM1602
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class LCM1602Example {
    
    /**
     * Private constructore to hide the implicit public one.
     */
    private LCM1602Example() {
    }

    /**
     * Command-line interface.
     * @param args unused here.
     */
    public static void main(String[] args) {
        LCM1602 display = new LCM1602();
        display.setDisplay(true, false, false);

        // To execute on exit (clean screen)
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                display.disableDisplay()));

        display.writeText(0, 0, "Raspoid  Welcome");
        
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        while (true) {
            display.writeText(4, 1, formatter.format(new Date()));
            Tools.sleepMilliseconds(250);
        }
    }
}
