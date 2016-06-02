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
package com.raspoid.examples.network;

import com.raspoid.PWMPin;
import com.raspoid.additionalcomponents.PassiveBuzzer;
import com.raspoid.additionalcomponents.Thermistor;
import com.raspoid.additionalcomponents.ThermistorNTCLE203E3103SB0;
import com.raspoid.additionalcomponents.adc.PCF8591;
import com.raspoid.additionalcomponents.adc.PCF8591InputChannel;
import com.raspoid.network.MessageLikeSocketServer;
import com.raspoid.network.pushbullet.Pushbullet;
import com.raspoid.network.Router;
import com.raspoid.network.SocketServer;

/**
 * This class is used to illustrate the utilization of the different servers
 * taking place in the Raspoid framework.
 * 
 * @see SocketServer
 * @see MessageLikeSocketServer
 * @see Pushbullet
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class GeneralExample {
    
    /**
     * Private constructor to hide the implicit public one.
     */
    private GeneralExample() {
    }
    
    /**
     * Command-line interface.
     * @param args unused here.
     */
    public static void main(String[] args) {
        // A simple router...
        Router router = new Router();
        
        // 2 kinds of routes:
        // without parameters
        Thermistor thermistor = new ThermistorNTCLE203E3103SB0(new PCF8591(), PCF8591InputChannel.CHANNEL_0);
        router.addRoute("temperature", () -> thermistor.getTemperature() + "°C");
        
        // with parameters
        PassiveBuzzer buzzer = new PassiveBuzzer(PWMPin.PWM0);
        router.addRouteWithParams("play", 2, inputArgs -> {
            buzzer.playTone(Double.valueOf(inputArgs[0]),  Integer.valueOf(inputArgs[1]));
            return "Tone played.";
        });
        
        // 3 kinds of servers to send requests to and to receive responses from
        SocketServer socketServer = new SocketServer(5, 80, router);
        socketServer.start();
        MessageLikeSocketServer messageLikeSocketServer = new MessageLikeSocketServer(router);
        messageLikeSocketServer.start();
        Pushbullet pushbullet = new Pushbullet("YOUR_PUSHBULLET_ACCESS_TOKEN", "Raspoid - Example", router);
        pushbullet.sendNewPush("Hello world ! =)");
        
        // Then, just either:
        //  - open a web browser to send HTTP requests to the socket server
        //  - develop an app to easily send and receive data through socket/websocket and then plot graphs, create statistics, etc.
        //  - use your pushbullet device to send commands / receive pushes
        //  - extend the router as we do for the demo with Pandorabot (http://www.pandorabots.com/)
        //  - ...
        
        // It's that simple !
    }
}
