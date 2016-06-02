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
package com.raspoid.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.raspoid.Tools;
import com.raspoid.exceptions.RaspoidException;

/**
 * <b>This class contains some utility functions,
 * usefull for some network operations.</b>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class NetworkUtilities {
    
    /**
     * The maximum port number value available on a Raspberry Pi.
     */
    public static final int MAX_PORT_NUMBER = 65535;
    
    /**
     * A port number must be a positive integer.
     */
    public static final int MIN_PORT_NUMBER = 0;
    
    /**
     * Private constructor, used to hide the implicit public one.
     */
    private NetworkUtilities() {}
        
    /**
     * Returns a list containing all ip addresses (IPv4 and IPv6) of current host,
     * without any link local or loopback addresses.
     * @return the list of ip addresses for the current host, without link local or loopback addresses.
     */
    public static List<String> getIpAddresses() {
        try {
            ArrayList<String> result = new ArrayList<>();
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            while(e.hasMoreElements()) {
                NetworkInterface n = e.nextElement();
                Enumeration<InetAddress> ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress i = ee.nextElement();
                    String newIP = i.getHostAddress();
                    if(!i.isLinkLocalAddress() && !i.isLoopbackAddress())
                        result.add(newIP);
                }
            }
            return result;
        } catch (SocketException e) {
            throw new RaspoidException("Error when getting ip addresses.", e);
        }
    }
    
    /**
     * Searches a port number available to launch a new server,
     * or -1 if no port is available.
     * @return a port number available to launch a new server, or -1 if no port available.
     */
    public static int getAvailablePort() {
        for(int port = 80; port <= MAX_PORT_NUMBER; port++)
            if(portIsAvailable(port))
                return port;
        for(int port = 0; port < 80; port++)
            if(portIsAvailable(port))
                return port;
        return -1;
    }
    
    /**
     * Checks if a port number is a valid port number.
     * A valid port number is an integer value, in the 
     * MIN_PORT_NUMBER..MAX_PORT_NUMBER interval.
     * 
     * @param portNumber the port number to check.
     * @return true if the port number is a valid port number, false otherwise.
     */
    public static boolean isAValidPortNumber(int portNumber) {
        return portNumber < NetworkUtilities.MAX_PORT_NUMBER &&
                portNumber > NetworkUtilities.MIN_PORT_NUMBER;
    }

    /**
     * Checks if a port is available to launch a new server.
     * 
     * @see NetworkUtilities#isAValidPortNumber(int)
     * 
     * @param port the port number to check.
     * @return true if the port is available, false otherwise.
     */
    private static boolean portIsAvailable(int port) {
        if(!isAValidPortNumber(port))
            return false;
        
        ServerSocket server = null;
        boolean result = false;
        try {
            server = new ServerSocket(port);
            result = true;
            server.close();
         } catch (IOException e) {
             Tools.debug("The port " + port + " is not available to launch a new server. (Related exception: " + e + ")");
             result = false;
         }
        return result;
    }
}
