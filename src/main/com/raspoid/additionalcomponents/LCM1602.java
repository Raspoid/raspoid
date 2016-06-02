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

import java.io.IOException;

import com.raspoid.I2CComponent;
import com.raspoid.Tools;
import com.raspoid.examples.additionalcomponents.LCM1602Example;
import com.raspoid.exceptions.RaspoidException;

/**
 * An LCM is mainly an LCD1602 display that can be used with the i2c protocol.
 * 
 * <p>Datasheet: <a href="http://raspoid.com/download/datasheet/LCM_MODULE">LCM_MODULE (part 1)</a>,
 *  <a href="http://raspoid.com/download/datasheet/LCM_MODULE_2">LCM_MODULE (part 2)</a></p>
 * 
 * <p>To use this display, we use the 4-Bit interface (cfr. <b>[datasheet - p.13]</b>).</p>
 * 
 * <p>Example of use: {@link LCM1602Example}</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class LCM1602 extends I2CComponent {
    
    /**
     * Default i2c LCM1602 address.
     */
    public static final int DEFAULT_LCM1602_ADDRESS = 0x27;
    
    /**
     * Number of lines on the LCD.
     */
    public static final int NB_LINES = 2;
    
    /**
     * Number of columns on the LCD.
     */
    public static final int NB_COL = 16;
    
    /**
     * Maximum index used for the lines on the LCD.
     */
    public static final int MAX_LINE_NB = NB_LINES - 1;
    
    /**
     * Minimum index used for the lines on the LCD.
     */
    public static final int MIN_LINE_NB = 0;
    
    /**
     * Maximum index used for columns on the LCD.
     */
    public static final int MAX_COL_NB = NB_COL - 1;
    
    /**
     * Minimum index used for columns on the LCD.
     */
    public static final int MIN_COL_NB = 0;
    
    /**
     * Instruction codes headers, used to construct packets 
     * in correct format for the LCM.
     * @see #RS1_RW0_EN1
     * @see #MAKE_EN0
     */
    private static final int RS0_RW0_EN1 = 0x04; // RS = 0, RW = 0, EN = 1
    
    /**
     * Instruction codes headers, used to construct packets 
     * in correct format for the LCM.
     * @see #RS0_RW0_EN1
     * @see #MAKE_EN0
     */
    private static final int RS1_RW0_EN1 = 0x05; // RS = 1, RW = 0, EN = 1
    
    /**
     * Instruction codes headers, used to construct packets 
     * in correct format for the LCM.
     * @see #RS0_RW0_EN1
     * @see #RS1_RW0_EN1
     */
    private static final int MAKE_EN0 = 0xFB;
    
    /**
     * [Command] Clear display <b>[datasheet - p.11]</b>.
     */
    private static final int CLEAR_DISPLAY_COMMAND = 0x01; // 0000 0001

    /**
     * [Command] Return home <b>[datasheet - p.11]</b>.
     */
    private static final int RETURN_HOME_COMMAND = 0x02; // 0000 0010
    
    /**
     * [Command] Set entry mode <b>[datasheet - p.11]</b>.
     */
    private static final int SET_ENTRY_MODE_COMMAND = 0x04;
    
    /**
     * [Command {@link #SET_ENTRY_MODE_COMMAND}]
     * <p>Cursor moving from left to right.</p>
     */
    private static final int CURSOR_MOVING_FROM_LEFT_TO_RIGHT = 0x02;
    
    /**
     * [Command {@link #SET_ENTRY_MODE_COMMAND}]
     * <p>Enable entire display shift.</p>
     */
    private static final int ENABLE_ENTIRE_DISPLAY_SHIFT = 0x01;
        
    /**
     * [Command] Set display <b>[datasheet - p.11]</b>.
     */
    private static final int SET_DISPLAY_COMMAND = 0x08; // 0000 1000
    
    /**
     * [Command {@link #SET_DISPLAY_COMMAND}]
     * <p>Enable display.</p>
     */
    private static final int ENABLE_DISPLAY = 0x04; // 0000 0100
    
    /**
     * [Command {@link #SET_DISPLAY_COMMAND}]
     * <p>Enable cursor.</p>
     */
    private static final int ENABLE_CURSOR = 0x02; // 0000 0010
    
    /**
     * [Command {@link #SET_DISPLAY_COMMAND}]
     * <p>Enable blinking cursor.</p>
     */
    private static final int ENABLE_BLINKING_CURSOR = 0x01; // 0000 0001
    
    /**
     * [Command] Set function (datasheet p.11).
     */
    private static final int SET_FUNCTION_COMMAND = 0x20; // 0010 0000
    
    /**
     * [Command {@link #SET_FUNCTION_COMMAND}]
     * <p>Enable 8 bits interface.</p>
     */
    private static final int ENABLE_8BITS_INTERFACE = 0x10; // 0001 0000
    
    /**
     * [Command {@link #SET_FUNCTION_COMMAND}]
     * <p>Enable 2 lines mode.</p>
     */
    private static final int ENABLE_2LINES_MODE = 0x08; // 0000 1000
    
    /**
     * [Command {@link #SET_FUNCTION_COMMAND}]
     * <p>Enable 5x10 dots font.</p>
     */
    private static final int ENABLE_5X10_DOTS_FONT = 0x04; // 0000 0100
    
    /**
     * Default constructor for an LCM1602 using the default LCM i2c address.
     */
    public LCM1602() {
        this(DEFAULT_LCM1602_ADDRESS);
    }
    
    /**
     * Constructor for an LCM1602 using a specific i2cAddress.
     * @param i2cAddress the i2c address of the LCM1602.
     */
    public LCM1602(int i2cAddress) {
        super(i2cAddress);
        resetFunction(); // init phase [datasheet - p.13]
    }
    
    /* ===============================================
     *                    Commands
     * =============================================*/
    
    /**
     * Clears the content of the display and move the cursor to the (0,0) position.
     */
    public void clearDisplay() {
        sendCommand(CLEAR_DISPLAY_COMMAND);
        Tools.sleepMilliseconds(3);
    }
    
    /**
     * The cursor is shifted to its original position (0, 0) if shifted,
     * and the content of the display is not changed.
     */
    public void returnHome() {
        sendCommand(RETURN_HOME_COMMAND);
        Tools.sleepMilliseconds(3);
    }
    
    /**
     * Assigns cursor moving direction and enable the shift of entire display.
     * @param cursorMovingFromLeftToRight true for a cursor moving from left to right.
     *        False for a cursor moving from right to left.
     * @param enableEntireDisplayShift true to enable the entire display shift. False to disable.
     */
    public void setEntryMode(boolean cursorMovingFromLeftToRight, boolean enableEntireDisplayShift) {
        int command = SET_ENTRY_MODE_COMMAND;
        if(cursorMovingFromLeftToRight)
            command += CURSOR_MOVING_FROM_LEFT_TO_RIGHT;
        if(enableEntireDisplayShift)
            command += ENABLE_ENTIRE_DISPLAY_SHIFT;
        sendCommand(command);
    }
        
    /**
     * Sets the display according to the specified parameters.
     * <p>Note: the cursor position can't be changed.</p>
     * @param enableDisplay the display is enabled/disabled (on/off).
     * @param enableCursor a cursor is displayed at the current cursor position "_".
     * @param enableBlinkingCursor a plein cursor is displayed and blinking at the current cursor position.
     */
    public void setDisplay(boolean enableDisplay, boolean enableCursor, boolean enableBlinkingCursor) {
        int command = SET_DISPLAY_COMMAND;
        if(enableDisplay)
            command += ENABLE_DISPLAY;
        if(enableCursor)
            command += ENABLE_CURSOR;
        if(enableBlinkingCursor)
            command += ENABLE_BLINKING_CURSOR;
        sendCommand(command);
    }
    
    /**
     * Disable the display.
     * <p>The content of the display is deleted and the cursor is moved to (0,0).</p>
     */
    public void disableDisplay() {
        clearDisplay();
        setDisplay(false, false, false);
    }
    
    /**
     * Sets the interface data length (8bit/4bit),
     * the number of display lines (2-line/1-line) and 
     * the display font type (5x10 dots/5x8 dots).
     * @param heightBitsInterface 8bit if true, 4bit if false.
     * @param twoLines 2-line if true, 1-line if false.
     * @param fiveTenDots 5x10 if true, 5x8 if false.
     */
    public void setFunction(boolean heightBitsInterface, boolean twoLines, boolean fiveTenDots) {
        int command = SET_FUNCTION_COMMAND;
        if(heightBitsInterface)
            command += ENABLE_8BITS_INTERFACE;
        if(twoLines)
            command += ENABLE_2LINES_MODE;
        if(fiveTenDots)
            command += ENABLE_5X10_DOTS_FONT;
        sendCommand(command);
    }
    
    /**
     * Moves the cursor to the given position.
     * @param line the new line of the cursor, in the {@link #MIN_LINE_NB}..{@link #MAX_LINE_NB} interval.
     * @param col the new column of the cursor, in the {@link #MIN_COL_NB}..{@link #MAX_COL_NB} interval.
     */
    public void moveCursor(int line, int col) {
        if(col < MIN_COL_NB)
            col = MIN_COL_NB;
        if(col > MAX_COL_NB)
            col = MAX_COL_NB;
        if(line < MIN_LINE_NB)
            line = MIN_LINE_NB;
        if(line > MAX_LINE_NB)
            line = MAX_LINE_NB;
        
        int command = 0x80 + 0x40 * line + col;
        sendCommand(command);
    }
    
    /**
     * Writes a text on the display, starting at the current cursor position.
     * @param text the text to print on the display.
     */
    public void writeText(String text) {
        this.writeCharArray(text.toCharArray());
    }
    
    /**
     * Sets the current cursor position to (line, col),
     * and writes a text starting at this position.
     * @param line the new line for the cursor.
     * @param col the new column for the cursor.
     * @param text the text to print on the display.
     */
    public void writeText(int line, int col, String text) {
        moveCursor(line, col);
        writeText(text);
    }
    
    /**
     * Writes a text from left to right. The text is right align if text.length < {@link #NB_COL}.
     * <p>Pay attention to the length of the text. The screen is only 2x16 chars.
     * If the text is too long, some chars may not be displayed.
     * It is for you to manage.</p>
     * @param line the line to print the text.
     * @param text the text to print on the display.
     */
    public void writeTextRightAlign(int line, String text) {
        int textLength = text.length();
        if(textLength > NB_COL)
            moveCursor(line, 0);
        else
            moveCursor(line, NB_COL - textLength);
        writeText(text);
    }
    
    /**
     * Writes an array of char at the current cursor position.
     * The cursor is then switched to initial_position + data.length().
     * @param data the data to write on the display.
     */
    private void writeCharArray(char[] data) {
        int tmp = data.length;
        for (int i = 0; i < tmp; i++)
            sendData(data[i]);
    }
    
    /* ===============================================
     *                      Utils
     * =============================================*/
    
    /**
     * Writes data on the I2C bus, respecting the requested format (cfr <b>[datasheet - p.11]</b>).
     * @param data
     */
    private void writeDataToI2C(int data) {
        try {
            data |= 0x08;
            byte sent = (byte)(data);
            device.write(sent);
        } catch (IOException e) {
            throw new RaspoidException(e);
        }
    }
    
    /**
     * Send a command to the LCM using I2C protocol. This command is sent using
     * the 4-bit data-length interface.
     * @param command
     */
    private void sendCommand(int command) {
        int buf;
        // Send bit7-4 firstly
        buf = command & 0xF0;
        buf |= RS0_RW0_EN1;
        writeDataToI2C(buf);
        Tools.sleepMilliseconds(2);
        buf &= MAKE_EN0;
        writeDataToI2C(buf);

        // Send bit3-0 secondly
        buf = (command & 0x0F) << 4;
        buf |= RS0_RW0_EN1;
        writeDataToI2C(buf);
        Tools.sleepMilliseconds(2);
        buf &= MAKE_EN0;
        writeDataToI2C(buf);
    }
    
    /**
     * Print a char on lcd at current cursor position.
     * @param data
     */
    private void sendData(int data) {
        int buf;
        // Send bit7-4 firstly
        buf = data & 0xF0;
        buf |= RS1_RW0_EN1;
        writeDataToI2C(buf);
        Tools.sleepMilliseconds(2);
        buf &= MAKE_EN0;
        writeDataToI2C(buf);

        // Send bit3-0 secondly
        buf = (data & 0x0F) << 4;
        buf |= RS1_RW0_EN1;
        writeDataToI2C(buf);
        Tools.sleepMilliseconds(2);
        buf &= MAKE_EN0;
        writeDataToI2C(buf);
    }
    
    /**
     * <b>[datasheet - p.13]</b>
     * Implementation of the initial procedure, for the 4-bit interface.
     * Only used once, when the cursor is powered on.
     */
    private void resetFunction() {
        Tools.sleepMilliseconds(40);
        sendCommand(0x33); // 0011 and then 0011
        sendCommand(0x32); // 0011 and then 0010
        setFunction(false, true, false);
        setDisplay(false, false, false);
        setEntryMode(true, false);
    }
}
