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

import com.raspoid.GPIOPin;
import com.raspoid.Tools;
import com.raspoid.additionalcomponents.UltrasonicHCSR04;
import com.raspoid.brickpi.BrickPi;
import com.raspoid.brickpi.nxt.sensor.UltraSonicSensor;

/**
 * Comparison between an Ultrasonic sensor HCSR04 and an Ultrasonic sensor NXT.
 * 
 * @see UltrasonicHCSR04
 * @see UltraSonicSensor
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class UltrasonicSensorsComparison {
    
    /**
     * Private constructor to hide the implicit public one.
     */
    private UltrasonicSensorsComparison() {
    }
    
    /**
     * Command-line interface.
     * @param args unused here.
     */
    public static void main(String[] args) {
        UltrasonicHCSR04 hcsr04 = new UltrasonicHCSR04(GPIOPin.GPIO_25, GPIOPin.GPIO_24);
        BrickPi.S1 = new UltraSonicSensor();
        BrickPi.start();
        
        Tools.log("Taking 10 raw values (to avoid eventual noise) [5 seconds].");
        for(int i = 0; i < 10; i++) {
            hcsr04.getDistance();
            ((UltraSonicSensor)BrickPi.S1).getDistance();
            Tools.sleepMilliseconds(500);
        }
        
        int numberOfMeasures = 10;
        double[] hcsr04Measures = new double[numberOfMeasures];
        int[] nxtMeasures = new int[numberOfMeasures];
        
        // Taking measures
        Tools.log("Taking " + numberOfMeasures + " measures.");
        for(int i = 0; i < numberOfMeasures; i++) {
            Tools.log("Measure " + i + ".");
            hcsr04Measures[i] = hcsr04.getDistance(); // m
            nxtMeasures[i] = ((UltraSonicSensor)BrickPi.S1).getDistance(); // cm
            Tools.log("hcsr04: " + hcsr04Measures[i]);
            Tools.sleepMilliseconds(1000);
        }
        
        // First mean values
        Tools.log("First mean values:");
        double firstMeanHCSR04Measures = 0.;
        double firstMeanNXTMeasures = 0.;
        for(int i = 0; i < numberOfMeasures; i++) {
            firstMeanHCSR04Measures += hcsr04Measures[i];
            firstMeanNXTMeasures += nxtMeasures[i];
        }
        firstMeanHCSR04Measures /= numberOfMeasures;
        firstMeanNXTMeasures /= numberOfMeasures;
        Tools.log("HCSR04:\t" + firstMeanHCSR04Measures + "m");
        Tools.log("NXT:\t" + firstMeanNXTMeasures + "cm");
        
        // Cleaning measures
        // we remove all data where value is out of the [mean-tolerance..mean+tolerance] range.
        Tools.log("Cleaning values.");
        double toleranceInPercent = 20; // %
        double toleranceHCSR04 = (firstMeanHCSR04Measures / 100.) * toleranceInPercent;
        double toleranceNXT = (firstMeanNXTMeasures / 100.) * toleranceInPercent;
        for(int i = 0; i < numberOfMeasures; i++) {
            if(Math.abs(hcsr04Measures[i] - firstMeanHCSR04Measures) > toleranceHCSR04) {
                hcsr04Measures[i] = -1;
                Tools.log("[HCSR04] One value cleaned.", Tools.Color.ANSI_GREEN);
            }
            if(Math.abs(nxtMeasures[i] - firstMeanNXTMeasures) > toleranceNXT) {
                nxtMeasures[i] = -1;
                Tools.log("[NXT] One value cleaned.", Tools.Color.ANSI_GREEN);
            }
        }
        
        // Final mean values
        double secondMeanHCSR04Measures = 0.;
        int hcsr04NbMeasures = 0;
        double secondMeanNXTMeasures = 0.;
        int nxtNbMeasures = 0;
        for(int i = 0; i < numberOfMeasures; i++) {
            if(hcsr04Measures[i] > 0) {
                secondMeanHCSR04Measures += hcsr04Measures[i];
                hcsr04NbMeasures++;
            }
            if(nxtMeasures[i] > 0) {
                secondMeanNXTMeasures += nxtMeasures[i];
                nxtNbMeasures++;
            }
        }
        secondMeanHCSR04Measures /= hcsr04NbMeasures;
        secondMeanNXTMeasures /= nxtNbMeasures;
        
        Tools.log("Results:", Tools.Color.ANSI_RED);
        Tools.log("HCSR04:\t" + secondMeanHCSR04Measures + "m", Tools.Color.ANSI_RED);
        Tools.log("NXT:\t" + secondMeanNXTMeasures + "cm", Tools.Color.ANSI_RED);
        
        BrickPi.stop();
    }
}
