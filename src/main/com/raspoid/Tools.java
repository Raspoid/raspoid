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

import java.util.BitSet;

import com.raspoid.exceptions.RaspoidInterruptedException;

/**
 * <b>This class contains some utility functions used accross the entire framework.</b>
 * 
 * <p>As an example, our logger system can easily be used to print colored log/debug 
 * messages to the standard output:
 * <pre>
 * Tools.log("Hello world", Tools.Color.ANSI_RED);
 * Tools.debug("Hello world", Tools.Color.ANSI_BLUE);
 * </pre>
 * </p>
 * 
 * <p>You can easily disable thoses logs/debugs with the following static methods:
 * <pre>
 * Tools.disableLogs();
 * Tools.disableDebugs();
 * </pre>
 * </p>
 * 
 * <p>Note that you can also easily disable the use of the colors for the logs.
 * Indeed, some standard outputs don't like this kind of ANSI codes:
 * <pre>
 * Tools.disableColors();
 * </pre>
 * </p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class Tools {
    
    /* -----------------------------------------------
     *                    LOGGING
     * ---------------------------------------------*/
    private static boolean displayLogs = true;
    private static boolean displayDebugs = true;
    private static boolean colorsEnabled = true;
    
    /**
     * Private constructor to hide the implicit public one.
     */
    private Tools() {
    }
    
    /**
     * Colors that can be used to print log and debug messages to standard output.
     */
    public enum Color {
        
        /**
         * ANSI reset code - needed to stop the use of a color arround a String.
         */
        ANSI_RESET("\u001B[0m"),
        
        /**
         * ANSI black code.
         */
        ANSI_BLACK("\u001B[30m"),
        
        /**
         * ANSI red code.
         */
        ANSI_RED("\u001B[31m"),
        
        /**
         * ANSI green code.
         */
        ANSI_GREEN("\u001B[32m"),
        
        /**
         * ANSI yellow code.
         */
        ANSI_YELLOW("\u001B[33m"),
        
        /**
         * ANSI blue code.
         */
        ANSI_BLUE("\u001B[34m"),
        
        /**
         * ANSI purple code.
         */
        ANSI_PURPLE("\u001B[35m"),
        
        /**
         * ANSI cyan code.
         */
        ANSI_CYAN("\u001B[36m"),
        
        /**
         * ANSI white code.
         */
        ANSI_WHITE("\u001B[37m");

        private String code = "";
        Color(String code) {
            this.code = code;
        }
        
        @Override
        public String toString() {
            return code;
        }
    }
    
    /**
     * Print a new colored log message to the standard output, if the logs are enabled.
     * @param obj the object to print in the log content (obj.toString()).
     * @param color the color used to print the log.
     * @see #enableLogs()
     * @see #enableColors()
     * @see #disableLogs()
     * @see #disableColors()
     */
    public static void log(Object obj, Color color) {
        log(obj.toString(), color);
    }
    
    /**
     * Print a new colored log message to the standard output, if the logs are enabled.
     * @param message the String message to print in the log content.
     * @param color the color used to print the log.
     * @see #enableLogs()
     * @see #enableColors()
     * @see #disableLogs()
     * @see #disableColors()
     */
    public static void log(String message, Color color) {
        if(displayLogs)
            System.out.println(colorsEnabled ? color + message + Color.ANSI_RESET : message); // NOSONAR
    }
    
    /**
     * Print a new log message to the standard output, if the logs are enabled.
     * @param obj the object to print in the log content (obj.toString()).
     * @see #enableLogs()
     * @see #disableLogs()
     */
    public static void log(Object obj) {
        log(obj.toString());
    }

    /**
     * Print a new log message to the standard output, if the logs are enabled.
     * @param message the String message to print in the log content.
     * @see #enableLogs()
     * @see #disableLogs()
     */
    public static void log(String message) {
        if(displayLogs)
            System.out.println(message); // NOSONAR
    }
    
    /**
     * Print a new colored debug message to the standard output, if the debugs are enabled.
     * @param obj the object to print in the debug content (obj.toString()).
     * @param color the color used to print the debug.
     * @see #enableDebugs()
     * @see #enableColors()
     * @see #disableDebugs()
     * @see #disableColors()
     */
    public static void debug(Object obj, Color color) {
        debug(obj.toString(), color);
    }
    
    /**
     * Print a new colored debug message to the standard output, if the debugs are enabled.
     * @param message the String message to print in the debug content.
     * @param color the color used to print the debug.
     * @see #enableDebugs()
     * @see #enableColors()
     * @see #disableDebugs()
     * @see #disableColors()
     */
    public static void debug(String message, Color color) {
        if(displayDebugs)
            System.out.println(colorsEnabled ? color + message + Color.ANSI_RESET : message); // NOSONAR
    }
    
    /**
     * Print a new debug message to the standard output, if the debugs are enabled.
     * @param obj the object to print in the debug content (obj.toString()).
     * @see #enableDebugs()
     * @see #disableDebugs()
     */
    public static void debug(Object obj) {
        debug(obj.toString());
    }
    
    /**
     * Print a new debug message to the standard output, if the debugs are enabled.
     * @param message the String message to print in the debug content.
     * @see #enableDebugs()
     * @see #disableDebugs()
     */
    public static void debug(String message) {
        if(displayDebugs)
            System.out.println(message); // NOSONAR
    }
    
    /**
     * Enable printing log messages to the standard output.
     */
    public static void enableLogs() {
        displayLogs = true;
    }
    
    /**
     * Disable printing log messages to the standard output.
     */
    public static void disableLogs() {
        displayLogs = false;
    }
    
    /**
     * Enable printing debug messages to the standard output.
     */
    public static void enableDebugs() {
        displayDebugs = true;
    }
    
    /**
     * Disable printing debug messages to the standard output.
     */
    public static void disableDebugs() {
        displayDebugs = false;
    }
    
    /**
     * Enable the use of colors to print log/debug messages.
     */
    public static void enableColors() {
        colorsEnabled = true;
    }
    
    /**
     * Disable the use of colors to print log/debug messages.
     */
    public static void disableColors() {
        colorsEnabled = false;
    }
    
    /* -----------------------------------------------
     *                    OTHERS
     * ---------------------------------------------*/

    /**
     * Returns the String representation of the byte array given in input.
     * @param byteArray the byteArray to analyze.
     * @return the String representation of the byte array given in input.
     */
    public static String getBinaryString(byte[] byteArray) {
        String result = "";
        int byteArrayLength = byteArray.length;
        for(int i = 0; i < byteArrayLength; i++) {
            byte currentByte = byteArray[i];
            result += String.format("%8s", Integer.toBinaryString(currentByte & 0xFF)).replace(' ', '0');
            if(i < byteArrayLength - 1)
                result += " ";
        }
        return result;
    }

    /**
     * Decode an int value from the input byte array.
     * @param bitLength the number of bits to decode.
     * @param incoming the byte array to decode from.
     * @param startingBitLocation the starting bit location in the byte array.
     * @return the decoded value.
     */
    public static int decodeInt(int bitLength, byte[] incoming, int startingBitLocation) {
        int value = 0;
        while (bitLength-- > 0) {
            value <<= 1;
            int location = bitLength + startingBitLocation;
            boolean set = (incoming[location / 8] & (1 << (location % 8))) != 0;
            if (set) {
                value |= 1;
            }
        }
        return value;
    }

    /**
     * Extracts a BitSet from the incoming byte array.
     * <p>If <code>bitLength == -1</code>, extracts from startingBitLocation to the end of the BitSet.</p>
     * @param bitLength the number of bits to extract.
     * @param incoming the byte array from where the BitSet is extracted.
     * @param startingBitLocation the starting location from which the BitSet is extracted.
     * @return the extracted BitSet.
     */
    public static BitSet extractBitSet(int bitLength, byte[] incoming, int startingBitLocation) {
        BitSet incomingBitSet = BitSet.valueOf(incoming);
        BitSet result;
        if(bitLength < 0) {
            result = incomingBitSet.get(startingBitLocation, incomingBitSet.length() - 1);
        } else {
            result = incomingBitSet.get(startingBitLocation, startingBitLocation+bitLength);
        }

        return result;
    }
    
    /**
     * Causes the currently executing thread to sleep (temporarily cease execution) for 
     * the specified number of milliseconds, subject to the precision and accuracy of 
     * system timers and schedulers. The thread does not lose ownership of any monitors.
     * <p><i>Allows to properly catch the InterruptedException.</i></p>
     * @param millis the number of milliseconds to wait.
     */
    public static void sleepMilliseconds(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RaspoidInterruptedException("The thread was interrupted unexpectedly while sleeping", e);
        }
    }
}
