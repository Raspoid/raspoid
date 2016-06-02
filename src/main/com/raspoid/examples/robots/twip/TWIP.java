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
package com.raspoid.examples.robots.twip;

import java.io.FileWriter;
import java.io.IOException;

import org.pid4j.pid.DefaultPid;
import org.pid4j.pid.Pid;

import com.raspoid.Tools;
import com.raspoid.additionalcomponents.MPU6050;
import com.raspoid.brickpi.BrickPi;
import com.raspoid.brickpi.Motor;

/**
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class TWIP {
    
    /**
     * Command-line interface.
     * @param args unused here.
     * @throws IOException TODO TO REMOVE
     */
    public static void main(String[] args) throws IOException {
        MPU6050 mpu6050 = new MPU6050();
        Double.parseDouble(args[0]);
        
        final Double kp = Double.parseDouble(args[0]), ki = Double.parseDouble(args[1]), kd = Double.parseDouble(args[2]);
        
        Pid myPid = new DefaultPid();
        double adjustment = 0.;
        myPid.setKp(kp);
        myPid.setKi(ki);
        myPid.setKd(kd);
        myPid.setOutputLimits(-255 + adjustment, 255. - adjustment);
        myPid.setSetPoint(0.);
        
        
        BrickPi.MA = new Motor();
        BrickPi.MB = new Motor();
        BrickPi.start();
        System.out.println("3");
        Tools.sleepMilliseconds(500);
        System.out.println("2");
        Tools.sleepMilliseconds(500);
        System.out.println("1");
        Tools.sleepMilliseconds(500);
        System.out.println("Go!");
        
        long startTime = System.currentTimeMillis();
        
        FileWriter fw = new FileWriter("/home/pi/pid/" + kp + "-" + ki + "-" + kd + ".txt");
        
        while(loop) {
            
            double pitchAngle = mpu6050.getFilteredAngles()[1];
            
            if (pitchAngle > 180) {
                pitchAngle = pitchAngle - 360;
            }
            
            double output = myPid.compute(pitchAngle);
            int adjustedOutput =(int) output;
            //mpu6050.updateValues(); // TODO
            BrickPi.commitSettings(()-> {
                BrickPi.MA.setPower(-(int)adjustedOutput);
                BrickPi.MB.setPower((int)adjustedOutput);
            });
            
            long now = System.currentTimeMillis() - startTime;
            
//          data1.add(now, - output, pitchAngle, - adjustedOutput);
            
            fw.write((System.currentTimeMillis() - startTime) + "\t" + String.format ("%.4f", pitchAngle) + "\t" + adjustedOutput + "\t" + String.format ("%.4f", output) + "\n");
            System.out.println((System.currentTimeMillis() - startTime) + "\t" + String.format ("%.4f", pitchAngle) + "\t" + adjustedOutput);

            //          System.out.println(String.format ("%.4f", pitchAngle));
            
            Tools.sleepMilliseconds(5);
            if (now >= 10000) {
                BrickPi.MA.setPower(0);
                BrickPi.MB.setPower(0);
//              LiveChart chart = new LiveChart();
//              chart.save("" + kp + "-" + ki + "-" + kd);
                loop = false;
            }
        }
        BrickPi.MA.setPower(0);
        BrickPi.MB.setPower(0);
        BrickPi.stop();
        fw.close();
        System.exit(0);
    }
    
//    static DataTable data1 = new DataTable(Long.class, Double.class, Double.class, Integer.class);
    
    static boolean loop = true;
    
//    public static class LiveChart {
//      
//      XYPlot plot;
//      
//      
//        public LiveChart() {
////            setDefaultCloseOperation(EXIT_ON_CLOSE);
////            setSize(1800, 1100);
////            setLocationRelativeTo(null);
//            System.out.println("adding data series");
//            
//            DataSeries s1 = new DataSeries(data1, 0, 1);
//          DataSeries s2 = new DataSeries(data1, 0, 2);
//          DataSeries s3 = new DataSeries(data1, 0, 3);
//            
//            plot = new XYPlot(s1);
//          plot.add(s2);
//          plot.add(s3);
//          
//          plot.setInsets(new Insets2D.Double(20, 50, 50, 20));
//          
//          System.out.println("adding to pane");
////            getContentPane().add(new InteractivePanel(plot));
//            
//          System.out.println("rendering");
//          LineRenderer lineRenderer = new DefaultLineRenderer2D();
//          
//          plot.setLineRenderers(s1, lineRenderer);
//            plot.getPointRenderers(s1).get(0).setColor(Color.GREEN);
//            plot.getLineRenderers(s1).get(0).setColor(Color.GREEN);
//
//            plot.setLineRenderers(s2, lineRenderer);
//            plot.getPointRenderers(s2).get(0).setColor(Color.RED);
//            plot.getLineRenderers(s2).get(0).setColor(Color.RED);
//            
//            plot.setLineRenderers(s3, lineRenderer);
//            plot.getPointRenderers(s3).get(0).setColor(Color.BLUE);
//            plot.getLineRenderers(s3).get(0).setColor(Color.BLUE);
//            
//            plot.getAxisRenderer(XYPlot.AXIS_X).setTickSpacing(1000.0);
//            plot.getAxisRenderer(XYPlot.AXIS_Y).setTickSpacing(20.0);
//            plot.getAxisRenderer(XYPlot.AXIS_Y).setShapeDirectionSwapped(true);
//            
//            System.out.println("ready to show");
//            
//        }
//        
//        public void save(String filename) {
//          try {
//              DrawableWriter writer = DrawableWriterFactory.getInstance().get("image/png");
//              writer.write(plot, new FileOutputStream(new File("/home/pi/pid/" + filename + ".png")), 1800, 1100);
//          } catch (IOException e) {
//              e.printStackTrace();
//          }
//      }
//    }

    }
