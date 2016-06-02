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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import com.raspoid.Tools;
import com.raspoid.examples.network.MessageLikeSocketServerExample;
import com.raspoid.exceptions.RaspoidException;

/**
 * <b>This server is used to provide (if needed) a message-like support on top of a socket server.</b>
 * 
 * <p>In TCP, you can't rely on separate "packets" being received separately.
 * Sending 4 chunks of 10 bytes may be received as 1 chunk of 40 bytes, 
 * 2 chunks of 20 bytes, or one chunk of 39 and one chunk of 1 byte.
 * TCP guarantees in order delivery, but not any particular 'packetization'
 * of your data.</p>
 * 
 * <p>Our protocol is pretty simple.
 * Each packet must have the following format:</p>
 * 
 * <pre>
 * -------------------------------------------
 * | payload_length (4 bytes) | payload      |
 * -------------------------------------------
 * </pre>
 * 
 * <p>You can then easily decode received TCP packets and decode corresponding messages.</p>
 * 
 * <p>This server must be linked to a {@link Router}.</p>
 * 
 * <p>Example of use: {@link MessageLikeSocketServerExample}</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class MessageLikeSocketServer extends SocketServer {
        
    /**
     * Constructor for a message-like socket server using the specified Router
     * and with the following parameter values:
     * <ul>
     *  <li>Maximum number of connections: 5</li>
     *  <li>Port number: Network.getAvailablePort()</li>
     * </ul>
     * @see NetworkUtilities#getAvailablePort()
     * 
     * @param router the Raspoid router to use with this server.
     */
    public MessageLikeSocketServer(Router router) {
        this(5, NetworkUtilities.getAvailablePort(), router);
    }
    
    /**
     * Constructor for a message-like socket server using the specified parameters.
     * 
     * @param maxNbOfConnections the maximum number of parallel connections allowed on this server.
     * @param port the port to use with this server.
     * @param router the Raspoid router to use with this server.
     */
    public MessageLikeSocketServer(int maxNbOfConnections, int port, Router router) {
        super(maxNbOfConnections, port, router);
    }
    
    @Override
    protected void newThreadToDealWithClient(Socket client) {
        new Thread(new MessageLikeSocketConnectionWithClient(router, client)).start();
    }
    
    @Override
    protected void printServerLaunchedMessage() {
        Tools.log("A Raspoid Message Like Server is launched on port " + port +
                ".\nYou can access it with one of the following ip addresses:\n" + 
                "\t127.0.0.1 (localhost)", Tools.Color.ANSI_GREEN); // NOSONAR
        for(String ipAddress : serverIpAddresses)
            Tools.log("\t" + ipAddress, Tools.Color.ANSI_GREEN);
    }
}

/**
 * This class represents a connection with a client,
 * connected to the message like socket server.
 */
class MessageLikeSocketConnectionWithClient implements Runnable {
    private Router router;
    private Socket client;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    
    /**
     * Constructor for a new connection with a client.
     * 
     * @param router the Raspoid router used by the server, for this client.
     * @param client the Socket of the client connected to the server.
     */
    public MessageLikeSocketConnectionWithClient(Router router, Socket client) {
        this.router = router;
        this.client = client;
        
        try {
            inputStream = new DataInputStream(client.getInputStream());
            outputStream = new DataOutputStream(client.getOutputStream());
        } catch (IOException e) {
            throw new RaspoidException("An I/O error occured when creating the input "
                    + "stream. The socket is closed, the socket is not connected, "
                    + "or the socket input has been shutdown using shutdownInput()", e);
        }
    }

    @Override
    public void run() {
        // while the connection is up, we deal with requests
        while(!client.isClosed()) {
            // we wait a request from the client
            String request = waitForRequest();
            if(request == null) {
                closeSocketWithClient();
                break;
            }
            
            String[] splittedRequest = request.split("/");
            
            String route = splittedRequest[0];
            String[] params = null;
            if(splittedRequest.length > 1) {
                int nbOfParams = splittedRequest.length - 1;
                params = new String[nbOfParams];
                for(int i = 0; i < nbOfParams; i++)
                    params[i] = splittedRequest[i + 1];
            }
            
            // we ask for a response from the router
            boolean validRequest = router.routeIsValid(route, params);
            String response = router.getResponse(route, params);
            
            // we send back the response to client
            try {
                if(!validRequest) {
                    Tools.log("The following received request was not understood: " + request);
                } else {
                    byte[] message;
                    message = response.getBytes("UTF-8");
                    outputStream.writeInt(message.length);
                    outputStream.write(message);
                    outputStream.flush();
                }
            } catch (IOException e) {
                throw new RaspoidException("Error when writing on output stream", e);
            }
        }
    }
    
    /**
     * Close the connection with the client.
     */
    private void closeSocketWithClient() {
        try {
            inputStream = null;
            outputStream = null;
            client.close();
        } catch (IOException e) {
            throw new RaspoidException("An I/O error occurs when closing the client socket.", e);
        }
    }
    
    /**
     * Wait for a new request from the client, and parse this request.
     * @return the String request from the client or null if the end of the stream has been reached.
     */
    private String waitForRequest() {
        try {
            int length = inputStream.readInt();
            if(length>0) {
                byte[] message = new byte[length];
                inputStream.readFully(message, 0, length); // read the message
                return new String(message, 0, length, StandardCharsets.UTF_8);
            }
            return null;
        } catch (IOException e) {
            throw new RaspoidException("There is a problem when reading bytes from inputStream.", e);
        }
    }
}
