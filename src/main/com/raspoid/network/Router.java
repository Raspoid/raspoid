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

import java.util.HashMap;
import java.util.Map;

import com.raspoid.exceptions.RaspoidException;
import com.raspoid.network.pushbullet.Pushbullet;

/**
 * <b>A router is used to map a route with an action to execute on your project.</b>
 * 
 * <p>The main advantage is that each router can easily be used with each of our servers:
 *  <ul>
 *      <li>{@link SocketServer},</li>
 *      <li>{@link MessageLikeSocketServer},</li>
 *      <li>{@link Pushbullet},</li>
 *      <li>...</li>
 * </ul></p>
 * 
 * <p>All you need to do is create a Router, add some routes to this Router, and then
 * link your Router to each intended server.</p>
 * 
 * <p>There exists 2 types of routes:
 *  <ul>
 *      <li>routes without parameters</li>
 *      <li>routes with parameters</li>
 *  </ul>
 * </p>
 * 
 * <p>For each route, you can then easily define the response to send back to the client.</p>
 * 
 * <p>Note: a default <code>"hello"</code> route is added to each router.
 * You can then easily test your installation by sending an "hello" request to your server(s).</p>
 * 
 * @see com.raspoid.examples.network.GeneralExample
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class Router {
    
    private static final Map<String, Integer> RESERVED_COMMANDS = new HashMap<>();
    
    private Map<String, Response> routesWithoutParams = new HashMap<>();
    private Map<String, Pair<Integer, ResponseWithParams>> routesWithParams = new HashMap<>();
    
    /**
     * Constructor for a basic router.
     * <p>A default "hello" route is added to each router.
     * You can then easily test your installation by sending an "hello" request to your server(s).</p>
     */
    public Router() {
        RESERVED_COMMANDS.put(cleanStrRoute("GET"), 0);
        addRoute("hello", () -> "Hello world !");
    }
    
    /**
     * Check if the command is available.
     * <p>An available command is a command that is not in the RESERVED_COMMANDS collection
     * and which is not already used by a user defined command (each command must be unique).</p>
     * @param command the command to check.
     * @return true if the command is available. False otherwise.
     */
    private boolean commandIsAvailable(String command) {
        command = cleanStrRoute(command);
        if(RESERVED_COMMANDS.containsKey(command) ||
                routesWithoutParams.containsKey(command) ||
                routesWithParams.containsKey(command))
            return false;
        return true;
    }
    
    /**
     * Adds a new route WITHOUT parameters to the router.
     * @param route the command corresponding to this route.
     * @param response the response to execute when the request will be triggered.
     */
    public void addRoute(String route, Response response) {
        route = cleanStrRoute(route);
        if(commandIsAvailable(route))
            routesWithoutParams.put(route, response);
        else
            throw new RaspoidException("The command '" + route + "' can not be used.");
    }
    
    /**
     * Adds a new route WITH parameters to the router.
     * @param route the command corresponding to this route.
     * @param nbParams the number of parameters that form the request.
     * @param response the response to execute when the request will be triggered.
     */
    public void addRouteWithParams(String route, int nbParams, ResponseWithParams response) {
        route = cleanStrRoute(route);
        if(commandIsAvailable(route))
            routesWithParams.put(route, new Pair<Integer, ResponseWithParams>(nbParams, response));
        else
            throw new RaspoidException("The command '" + route + "' is already used or is a reserved command.");
    }
    
    /**
     * Checks if the route is valid and contains a correspondance in the router.
     * @param route the route to analyze.
     * @param params the input parameters of the route to analyze. null if no parameters.
     * @return true if the route is valid. False otherwise.
     */
    public boolean routeIsValid(String route, String[] params) {
        route = cleanStrRoute(route);
        if(params == null || params.length == 0) {
            // route without params
            if(routesWithoutParams.containsKey(route))
                return true;
        } else {
            // route with params needed
            if(routesWithParams.containsKey(route)) {
                // we then check the number of received params
                int expectedNbOfParams = routesWithParams.get(route).getLeft();
                int receivedNbOfParams = params.length;
                if(expectedNbOfParams == receivedNbOfParams)
                    return true;
            }
        }
        
        return false;
    }
    
    /**
     * Checks the validity of the request, and then evaluate the response to send 
     * back to the client.
     * <p>This method always returns a comprehensive response String, even if the request is invalid.</p>
     * @param route the route to analyze.
     * @param params the input parameters to use with the route. null if route without parameters.
     * @return the String representation of the response to send back to the client.
     */
    public String getResponse(String route, String[] params) {
        route = cleanStrRoute(route);
        String response;
        
        if(params == null || params.length == 0) {
            // route without params
            // we need to check the existance of the route
            if(routesWithoutParams.containsKey(route))
                response = routesWithoutParams.get(route).getResponse();
            else
                response = "Sorry, but we can't understand your request.";
        } else {
            // route with params needed
            // we need to check the existance of the route
            if(routesWithParams.containsKey(route)) {
                // we then check the number of received parameters
                int expectedNbOfParams = routesWithParams.get(route).getLeft();
                int receivedNbOfParams = params.length;
                if(expectedNbOfParams != receivedNbOfParams) {
                    response = "The number of received paramaters is incorrect for the " + route + 
                            " route: " + expectedNbOfParams + " expected, " + receivedNbOfParams + " received.";
                } else {
                    // the request is valid
                    response = routesWithParams.get(route).getRight().getResponse(params);
                }
            } else
                response = "Sorry, we can't understand your request.";
        }
        
        return response;
    }
    
    /**
     * Get the list of requests without parameters contained in this router.
     * @return the list of requests without parameters contained in this router.
     */
    public Map<String, Response> getRoutesWithoutParams() {
        return routesWithoutParams;
    }
    
    /**
     * Get the list of requests with parameters contained in this router.
     * @return the list of requests with parameters contained in this router.
     */
    public Map<String, Pair<Integer, ResponseWithParams>> getRoutesWithParams() {
        return routesWithParams;
    }
    
    /**
     * Java 8 interface used to represent a response to a request without parameters.
     * <p><i>[Specific to Java 8: only one method per interface to allow the use of lambda expressions]</i></p>
     */
    @FunctionalInterface
    public interface Response {
        
        /**
         * Returns the String representation of a Response to a request without parameters.
         * <p><i>[Specific to Java 8: only one request per interface to allow the use of lambda expressions]</i></p>
         * @return the String representation of a Response.
         */
        String getResponse();
    }
    
    /**
     * Interface used to represent a response to a request with parameters.
     * <p><i>[Specific to Java 8: only one method per interface to allow the use of lambda expressions]</i></p>
     */
    @FunctionalInterface
    public interface ResponseWithParams {
        
        /**
         * Returns the String representation of a response to a request with parameters. 
         * @param args the parameters values.
         * @return the String representation of a response to a request with parameters.
         */
        String getResponse(String[] args);
    }
    
    /**
     * Returns a new String corresponding to the clean version of the input one.
     * <p>This method:
     *  <ul>
     *      <li>removes all non-letter characters,</li>
     *      <li>folds to lowercase,</li>
     *      <li>removes all whitespaces and non visible characters such as tab, \n</li>
     *  </ul>
     * </p>
     * @param input the input String to clean.
     * @return a new String corresponding to the clean version of the input one.
     */
    private String cleanStrRoute(String input) {
        return input.replaceAll("[^a-zA-Z ]", "").toLowerCase().trim().replaceAll("\\s+","");
    }
}

/**
 * This class is an abstraction to a pair of values. 
 * @param <L> the type of the first element of the pair.
 * @param <R> the type of the second element of the pair.
 */
class Pair<L,R> {
    
    private final L left;
    private final R right;
    
    /**
     * Constructor for a pair of values.
     * @param left the first element of the pair.
     * @param right the second element of the pair.
     */
    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }
    
    /**
     * Get the first element of the pair.
     * @return the first element of the pair.
     */
    public L getLeft() { return left; }
    
    /**
     * Get the second element of the pair.
     * @return the second element of the pair.
     */
    public R getRight() { return right; }

    @Override
    public int hashCode() { return left.hashCode() ^ right.hashCode(); }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Pair))
          return false;
      Pair<L, R> pairo = (Pair<L, R>) o;
      return this.left.equals(pairo.getLeft()) &&
             this.right.equals(pairo.getRight());
    }
}
