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
package com.raspoid.examples.network.pushbullet;

import java.util.List;

import com.raspoid.Tools;
import com.raspoid.network.Router;
import com.raspoid.network.pushbullet.Device;
import com.raspoid.network.pushbullet.Push;
import com.raspoid.network.pushbullet.Pushbullet;

/**
 * Example of use of Pushbullet.
 * 
 * @see Pushbullet
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class PushbulletExample {
    
    /**
     * Private constructor to hide the implicit public one.
     */
    private PushbulletExample() {
    }
    
    /**
     * Pushbullet features examples.
     * @param args input args. args[0] = your_access_token, args[1] = the name of the device related to your robot.
     */
    public static void main(String [] args){
        if(args.length != 2)
            throw new IllegalArgumentException("Error with input args. Utilization: <your_access_token> <device_name>");
        
        Pushbullet test = new Pushbullet(args[0], args[1], new Router());
        
        test.sendNewPush("Hello !", "Hello world !");
        
        List<Push> pushes = test.getListOfPushes(-1);
        List<Device> devices = test.getListOfDevices();
        
        Tools.log("--------Pushes--------");
        for(Push push : pushes)
            Tools.log("Push: " + push);
        
        Tools.log("--------Devices--------");
        for(Device device: devices)
            Tools.log("Device: " + device);
        
        Tools.log("--------Wait for a response--------");
        
        while(true)
            Tools.sleepMilliseconds(1000);
    }
}
