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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.raspoid.Tools;
import com.raspoid.examples.network.SocketServerExample;
import com.raspoid.exceptions.RaspoidException;

/**
 * <b>This server is used to provide a simple socket server.</b>
 * 
 * <p>This server must be linked to a {@link Router}.</p>
 * 
 * <p>Requests can be send as plain text requests or as http get requests.
 * You can then easily use your browser to send requests to this server.</p>
 * 
 * <p>Example of use: {@link SocketServerExample}</p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class SocketServer {
    
    /**
     * The server socket used for this message-like server.
     */
    private ServerSocket server;
    
    /**
     * List of available ip addresses to access the socket server.
     */
    protected List<String> serverIpAddresses;
    
    /**
     * Port used by the socket server.
     */
    protected int port;
    
    /**
     * Guard value, used to mark when the server is running.
     * True while the server is running, false otherwise.
     */
    private boolean running = false;
    private boolean serverClosed = false;
    
    /**
     * The Raspoid Router used by this server.
     * @see Router
     */
    protected Router router;
    
    /**
     * Constructor for a simple socket server using the specified Router
     * and with the following parameter values:
     * <ul>
     *  <li>Maximum number of connections: 5</li>
     *  <li>Port number: Network.getAvailablePort()</li>
     * </ul>
     * @param router the Raspoid router to use with this socket server.
     */
    public SocketServer(Router router) {
        this(5, NetworkUtilities.getAvailablePort(), router);
    }
    
    /**
     * Constructor for a simple socket server using the specified parameters.
     * @param maxNbOfConnections the maximum number of parallel connections allowed on this server.
     * @param port the port to use with this server.
     * @param router the Raspoid router to use with this socket server.
     */
    public SocketServer(int maxNbOfConnections, int port, Router router) {
        if(!NetworkUtilities.isAValidPortNumber(port))
            throw new IllegalArgumentException("The port must be between " + NetworkUtilities.MIN_PORT_NUMBER + " and " + NetworkUtilities.MAX_PORT_NUMBER);
        
        this.port = port;
        this.router = router;
        
        try {
            server = new ServerSocket(port, maxNbOfConnections);
            run();
        } catch (IOException e) {
            throw new RaspoidException("Error when creating Raspoid server.", e);
        }
    }
    
    /**
     * Starts the Raspoid server in a new Thread.
     */
    private void run() {
        new Thread(() -> {
            while(!serverClosed) {
                if(running) {
                    try {
                        // we wait for a connection from a client
                        Socket client = server.accept();
                        
                        // once received, we treat it in a separate thread
                        newThreadToDealWithClient(client);
                    } catch (IOException e) {
                        throw new RaspoidException("An I/O error occurs when waiting for a connection.", e);
                    }
                } else {
                    Tools.sleepMilliseconds(500);
                }
            }
        }).start();
    }
    
    protected void newThreadToDealWithClient(Socket client) {
        new Thread(new SocketConnectionWithClient(router, client)).start();
    }
    
    protected void printServerLaunchedMessage() {
        Tools.log("The Raspoid server in launched on port " + port +
                ".\nYou can access it with one of the following ip addresses:\n" + 
                "\t127.0.0.1 (localhost)", Tools.Color.ANSI_GREEN); // NOSONAR
        for(String ipAddress : serverIpAddresses)
            Tools.log("\t" + ipAddress, Tools.Color.ANSI_GREEN);
        
        Tools.log("You can test the server with the following: http://" + serverIpAddresses.get(0) + ":" + port + "/hello", Tools.Color.ANSI_GREEN);
    }
    
    /**
     * Turns the Raspoid server on.
     * @see #pause()
     */
    public void start() {
        if(server.isClosed())
            Tools.log("The socket server has been closed. You need to create a new one.");
        else {
            running = true;
            serverIpAddresses = NetworkUtilities.getIpAddresses();
            printServerLaunchedMessage();
        }
    }
    
    /**
     * Turns the Raspoid server in a pause mode.
     * <p>All requests are then rejected. You can use the start() method to relaunch the server.</p>
     * @see #start()
     */
    public void pause() {
        running = false;
        Tools.debug("Socket server: PAUSE.");
    }
    
    /**
     * Turns the Raspoid socket server off.
     */
    public void close() {
        running = false;
        serverClosed = true;
        try {
            server.close();
            Tools.debug("Socket server closed.");
        } catch (IOException e) {
            throw new RaspoidException("Error when closing the Raspoid socket server.", e);
        }
    }
    
    /**
     * Get the list of ip addresses linked to this server.
     * @return the list of ip addresses linked to this server.
     */
    public List<String> getIpAddresses() {
        return serverIpAddresses;
    }
    
    /**
     * Get the port used by the server.
     * @return the port used by the server.
     */
    public int getPort() {
        return port;
    }
}

/**
 * This class represents a connection with a client
 * connected to the socket server.
 */
class SocketConnectionWithClient implements Runnable {
    private Router router;
    private Socket client;
    private BufferedInputStream inputStream;
    private PrintWriter outputStream;
    
    /**
     * Constructor for a new connection with a client.
     * @param router the Raspoid router used by the server, for this client.
     * @param client the Socket of the client connected to the server.
     */
    public SocketConnectionWithClient(Router router, Socket client) {
        this.router = router;
        this.client = client;
        
        try {
            inputStream = new BufferedInputStream(client.getInputStream());
            outputStream = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RaspoidException("An I/O error occured when creating the input "
                    + "stream. The socket is closed, the socket is not connected, "
                    + "or the socket input has been shutdown using shutdownInput()", e);
        }
    }

    @Override
    public void run() {
        boolean httpRequest = false;
        
        // while the connection is up, we deal with requests
        while(!client.isClosed()) {
            // we wait a request from the client
            String request = waitForRequest();
            if(request == null) {
                closeSocketWithClient();
                break;
            }
            
            // we check if the request is an HTTP GET request
            String[] splittedRequest = request.split(" ");
            if("GET".equals(splittedRequest[0])) {
                httpRequest = true;
                request = splittedRequest[1].substring(1);
            }
            
            splittedRequest = request.split("/");
            
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
            if(httpRequest) {
                // we need to close the connection after one HTTP request !
                if(!validRequest) {
                    outputStream.write("HTTP/1.1 404\nContent-type:text/plain;charset=utf-8\n\n");
                    if(!"".equals(response))
                        outputStream.write("\n" + response);
                } else {
                    outputStream.write("HTTP/1.1 200\nContent-type:text/plain;charset=utf-8\n\n");
                    outputStream.write(response);
                }
                outputStream.flush();
                closeSocketWithClient();
            } else {
                // this is not a response to an HTTP request, so we can let the connection open
                if(!validRequest) {
                    Tools.log("The following received request was not understood: " + request);
                } else {
                    outputStream.write(response);
                    outputStream.flush();
                }
            }
        }
    }
    
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
            byte[] buffer = new byte[4096];
            // Reads up to buffer.length bytes of data from the input stream 
            // into the buffer. This method blocks until some input is available.
            int bytesRead = inputStream.read(buffer);
            if(bytesRead == -1) {
                // There is no more data because the end of the stream has been reached.
                // connection closed by the client
                return null;
            }
            return new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RaspoidException("There is a problem when reading bytes from inputStream.", e);
        }
    }
}
