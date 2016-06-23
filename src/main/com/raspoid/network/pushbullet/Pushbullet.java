/*******************************************************************************
 * Copyright (c) 2016 Julien Louette & Gaël Wittorski
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.raspoid.network.pushbullet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.raspoid.Tools;
import com.raspoid.exceptions.RaspoidException;
import com.raspoid.network.Router;

/**
 * <b>This class is an abstraction to easily use some of the Pushbullet services.</b>
 * 
 * <p>We implemented here the main Pushbullet features that can be useful for your project.
 * The utilization is really simple. You can for example easily send some requests to your robots 
 * from any of your Pushbullet devices, receive a response from your robot, or even setup a notification
 * system to be notified when some specific event occurs.</p>
 * 
 * <p>All you need to use this Pushbullet wrapper is a Pushbullet account,
 * and an access token for this account.<br>
 * <i>This access token can easily be retrieved from your Pushbullet settings.</i></p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class Pushbullet {
    
    /**
     * The websocket session with the pushbullet servers.
     */
    private Session session;
    
    /**
     * The private token used to access pushbullet services.
     */
    private String accessToken;
    
    /**
     * The Gson instance used to decode pushbullet json messages.
     */
    private Gson gson = null;
    
    /**
     * Our device iden on pushbullet servers.
     */
    private String deviceIden;
    
    /**
     * The last timestamp of the last received push (for us or not).
     * <p>This timestamp is used to ask only new pushes when asking pushes list to pushbullet.</p>
     */
    private double lastPushReceivedTime = MIN_LAST_PUSH_RECEIVED_TIMESTAMP;
    
    /**
     * Needed in case of no push already received/sent by the user.
     */
    private static final double MIN_LAST_PUSH_RECEIVED_TIMESTAMP = 1.4E+9;
    
    /**
     * Constructor for a new Pushbullet instance with a specific access token, a device name
     * corresponding to the name that your robot will take in your Pushbullet list of devices,
     * and the Raspoid router to use with this Pushbullet instance.
     * 
     * <p>The access token can easily be retrieved from your Pushbullet account settings.</p>
     * 
     * <p>Note that if a device with the specified name already exists, this device will be
     * retrieved. If no devices with this name exists, a new one will be created.</p>
     * 
     * <p>As for other types of servers, the router is used to deal with requests
     * received on this Pushbullet instance.</p>
     * 
     * @param accessToken the access token used to access Pushbullet services.
     * @param deviceName the name corresponding to your robot's Pushbullet device.
     * @param router the router to use to deal with requests received on the deviceName.
     */
    public Pushbullet(String accessToken, String deviceName, Router router) {
        this.accessToken = accessToken;
        
        gson = new Gson();
        
        this.deviceIden = initDevice(deviceName);
        this.lastPushReceivedTime = initLastPushReceivedTime();
        
        // WebSocket
        final ClientEndpointConfig clientEndpointConfig = ClientEndpointConfig.Builder.create().build();
        WebSocketContainer websocketClient = ContainerProvider.getWebSocketContainer();
        try {
            session = websocketClient.connectToServer(new PushbulletClientEndpoint(router),
                    clientEndpointConfig, new URI("wss://stream.pushbullet.com/websocket/" + accessToken));
        } catch (DeploymentException | IOException | URISyntaxException e) {
            throw new RaspoidException("Error when connecting to Pushbullet server.", e);
        }
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Websocket closed by client"));
            } catch (IOException e) { 
                throw new RaspoidException("Error when closing the websocket session with Pushbullet.", e);
            }
        }));
    }
    
    /**
     * Creates a new device if needed and retrieve the corresponding Pushbullet device iden.
     * 
     * <p>This method checks if a device named deviceName already exists in your Pushbullet devices.
     * <ul>
     *  <li>If it already exists, it returns the device id.</li>
     *  <li>If it doesn't exist, a new device with this name is created and the new device id is returned.</li>
     * </ul>
     * </p>
     * @param deviceName the name of the Pushbullet device corresponding to this instance of the Pushbullet wrapper.
     * @return the device id of the Pushbullet device named deviceName.
     */
    private String initDevice(String deviceName) {
        String deviceId;
        List<Device> userDevices = getListOfDevices();
        for(Device userDevice : userDevices) {
            if(userDevice.isActive() && userDevice.getNickname().equals(deviceName)) {
                // the device already exists, we juste need to retrieve the iden
                deviceId = userDevice.getIden();
                Tools.log("[Pushbullet] An active device with this name already exists on pushbullet: (iden)" + deviceId);
                return deviceId;
            }
        }
        
        // the device doesn't exists, we need to create it and retrieve the iden
        Device newDevice = createNewDevice(deviceName);
        deviceId = newDevice.getIden();
        Tools.log("New device created on pushbullet: (name)" + newDevice.getNickname() + " (iden)" + deviceId);
        return deviceIden;
    }
    
    /**
     * Retrieve and updates the timestamp of the last push in Pushbullet pushes of the user.
     */
    private double initLastPushReceivedTime() {
        List<Push> lastPushList = getListOfPushes(MIN_LAST_PUSH_RECEIVED_TIMESTAMP, 1);
        if(lastPushList.isEmpty())
            return MIN_LAST_PUSH_RECEIVED_TIMESTAMP;
        else
            return lastPushList.get(0).getLastModificationTimestamp();
    }
    
    private <T> T deserializePushbulletEntity(String jsonRepresentation, Class<T> targetClass) {
        return gson.fromJson(jsonRepresentation, targetClass);
    }
        
    /**
     * Executes an Http Get request and returns the String representation 
     * of the response from the server.
     * Returns null in case of problem.
     * @param url the url of the Get request to execute.
     * @return the String representation of the response from the server.
     */
    public String sendGetRequest(String url) {
        HttpGet request = new HttpGet(url);
        request.addHeader("Access-Token", accessToken);
        return sendHttpRequest(request);
    }
    
    /**
     * Executes an Http Post request and returns the String representation
     * of the response from the server.
     * Returns null in case of problem.
     * @param url the url of the Post request to execute.
     * @return the String representation of the response from the server.
     */
    public String sendPostRequest(String url) {
        return sendPostRequest(url, null);
    }
    
    /**
     * Executes an Http Post request with specific url parameters
     * and returns the String representation of the response from the server.
     * Returns null in case of problem.
     * @param url the url of the Post request to execute.
     * @param urlParameters the url parameters to add to the request.
     * @return the String representation of the response from the server.
     */
    public String sendPostRequest(String url, List<NameValuePair> urlParameters) {
        HttpPost request = new HttpPost(url);
        request.addHeader("Access-Token", accessToken);
        
        if(urlParameters != null)
            try {
                request.setEntity(new UrlEncodedFormEntity(urlParameters, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RaspoidException("[Pushbullet] Error when setting Http request entity.", e);
            }
        
        return sendHttpRequest(request);
    }
    
    /**
     * Upload a file on the Pushbullet servers.
     * @param url authorized url to post the file.
     * @param filePath the local path of the file to post.
     * @return the response from the HTTP request.
     */
    public int postFile(String url, String filePath) {
        String cmd = "curl -i -X POST " + url + " -F file=@" + filePath;
        Process process;
        try {
            process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
            return process.exitValue();
        } catch (IOException | InterruptedException e) {
            throw new RaspoidException("[Pushbullet] Error when uploading a file.", e);
        }
    }
    
    /**
     * Sends an Http request (Get or Post), and returns the String representation
     * of the response from the server.
     * @return the String representation of the response from the server. 
     */
    private String sendHttpRequest(HttpUriRequest request) {
        HttpClient client = HttpClientBuilder.create().build();
        try {
            Tools.debug("[Pushbullet] Http request executed: " + request);
            HttpResponse response = client.execute(request);
            Tools.debug("[Pushbullet] Http Response Code: " + response.getStatusLine().getStatusCode());
            try(BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8))) {
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null)
                    result.append(line);
                return result.toString();
            }
        } catch (IOException e) {
            throw new RaspoidException("Error when executing Http request.", e);
        }
    }
    
    /**
     * Retrieve the list of pushes received after a specific timestamp,
     * with a specific limit in the number of retrieved pushes.
     * @param modifiedAfter the timestamp from which to retrieve pushes.
     * @param limit the maximum number of pushes to retrieve on the Pushbullet servers. -1 if no limit.
     * @return the list of last limit pushes modified after the modifiedAfter timestamp, retrieved on the Pushbullet services.
     */
    public List<Push> getListOfPushes(double modifiedAfter, int limit) {
        return getListOfPushes(true, modifiedAfter, limit);
    }
    
    /**
     * Retrieve the list of pushes without any limit in time, with a specific limit
     * in the number of retrieved pushes.
     * @param limit the maximum number of pushes to retrieve on the Pushbullet servers. -1 if no limit.
     * @return the list of last limit pushes, retrieved on the Pushbullet services.
     */
    public List<Push> getListOfPushes(int limit) {
        return getListOfPushes(false, -1, limit);
    }
    
    /**
     * Retrieve the list of pushes received after a specific timestamp, if useModifiedAfter is setted to true.
     * This method allows to avoid to use a comparison for the double modifiedAfter parameter (modifiedAfter == -1),
     * which is a bad idea, for rounding reasons of double primitive types in java.
     * @param useModifiedAfter
     * @param modifiedAfter
     * @param limit
     * @return the list of last limit pushes, with a limit is time if useModifiedAfter is set to true.
     * @see #getListOfPushes(int)
     * @see #getListOfPushes(double, int)
     */
    private List<Push> getListOfPushes(boolean useModifiedAfter, double modifiedAfter, int limit) {
        String url = "https://api.pushbullet.com/v2/pushes?active=true";
        if(useModifiedAfter)
            url += "&modified_after=" + modifiedAfter;
        if(limit != -1)
            url += "&limit=" + limit;
        return deserializePushbulletEntity(sendGetRequest(url), ListOfPushes.class).getPushes();
    }

    
    /**
     * Retrieve the list of Pushbullet devices linked to the specified access token account.
     * @return the list of Pusbullet devices linked to the specifiec access token account.
     */
    public List<Device> getListOfDevices() {
        return deserializePushbulletEntity(
                sendGetRequest("https://api.pushbullet.com/v2/devices"), ListOfDevices.class).getDevices();
    }
    
    private Device createNewDevice(String nickname) {
        String manufacturer = "Raspoid";
        String model = "Raspberry Pi";
        
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("nickname", nickname));
        urlParameters.add(new BasicNameValuePair("manufacturer", manufacturer));
        urlParameters.add(new BasicNameValuePair("model", model));
        
        return deserializePushbulletEntity(
                sendPostRequest("https://api.pushbullet.com/v2/devices", urlParameters), Device.class);
    }
    
    /**
     * Sends a new push with a specific body to the Pushbullet server.
     * The push is sent in broadcast mode (to all account devices) to the account linked to the specified access token.
     * @param body the body of the push sent to Pushbullet server.
     * @return a Push entity representing the newly sent push to the Pushbullet server.
     */
    public Push sendNewPush(String body) {
        return sendNewPush(null, body, null);
    }
    
    /**
     * Sends a new push with a specific title and a specific body to the Pushbullet server.
     * The push is sent in broadcast mode (to all account devices) to the account linked to the specified access token.
     * @param title the title of the push sent to the Pusbullet server.
     * @param body the body of the push sent to the Pushbullet server.
     * @return a Push entity representing the newly sent push to the Pushbullet server.
     */
    public Push sendNewPush(String title, String body) {
        return sendNewPush(title, body, null);
    }
    
    /**
     * Sends a new push with a specific title and a specific body to the Pushbullet server.
     * The push is sent to a specific device, or in broadcast mode (to all account devices) if targetDeviceIden is null,
     * to the account linked to the specified access token.
     * @param title the title of the push sent to the Pusbullet server.
     * @param body the body of the push sent to the Pushbullet server.
     * @param targetDeviceIden the user's device targeted by the push. null for broadcast.
     * @return a Push entity representing the newly sent push to the Pushbullet server.
     */
    public Push sendNewPush(String title, String body, String targetDeviceIden) {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("type", "note"));
        if(title != null)
            urlParameters.add(new BasicNameValuePair("title", title));
        if(body != null)
            urlParameters.add(new BasicNameValuePair("body", body));
        if(targetDeviceIden != null)
            urlParameters.add(new BasicNameValuePair("device_iden", targetDeviceIden));
        urlParameters.add(new BasicNameValuePair("source_device_iden", deviceIden));
        String response = sendPostRequest("https://api.pushbullet.com/v2/pushes", urlParameters);
        return deserializePushbulletEntity(response, Push.class);
    }
    
    /**
     * Sends a new file to the Pushbullet servers and sends this file through a push.
     * The push is sent to a specific device, or in broadcast mode if targetDeviceIden is null.
     * @param filePath the path of the file to send.
     * @param fileName the name of the file to send.
     * @param fileType the type of the file to send.
     * @param targetDeviceIden the user's device targeted by the push. null for broadcast.
     * @return a Push entity representing the newly sent push.
     */
    public Push sendNewFile(String filePath, String fileName, String fileType, String targetDeviceIden) {
        // step 1 - request authorization to upload a file
        List<NameValuePair> url1Parameters = new ArrayList<>();
        url1Parameters.add(new BasicNameValuePair("file_name", fileName));
        url1Parameters.add(new BasicNameValuePair("file_type", fileType));
        String response1 = sendPostRequest("https://api.pushbullet.com/v2/upload-request", url1Parameters);
        FileUploaded fileUploaded = deserializePushbulletEntity(response1, FileUploaded.class);
        Tools.log("DEBUG AAAAA: " + fileUploaded.getFileName() + " " + fileUploaded.getUploadUrl());
        
        // step 2 - upload the file
        int result = postFile(fileUploaded.getUploadUrl(), filePath);
        Tools.log("FILE POSTED? exit value: " + result);
        
        // step 3 - new push
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("type", "file"));
        urlParameters.add(new BasicNameValuePair("file_name", fileUploaded.getFileName()));
        urlParameters.add(new BasicNameValuePair("file_url", fileUploaded.getFileUrl()));
        urlParameters.add(new BasicNameValuePair("file_type", fileUploaded.getFileType()));
        if(targetDeviceIden != null)
            urlParameters.add(new BasicNameValuePair("device_iden", targetDeviceIden));
        urlParameters.add(new BasicNameValuePair("source_device_iden", deviceIden));
        Tools.log("URL PARAMETERS: " + urlParameters);
        String response = sendPostRequest("https://api.pushbullet.com/v2/pushes", urlParameters);
        return deserializePushbulletEntity(response, Push.class);
    }
    
    /**
     * The WebSocket client endpoint used to connect to the Pushbullet WebSocket server.
     */
    private class PushbulletClientEndpoint extends Endpoint {
        
        Router router;
        
        PushbulletClientEndpoint(Router router) {
            this.router = router;
        }
        
        @Override
        public void onOpen(Session session, EndpointConfig endpointConfig) {
            session.addMessageHandler(String.class, (String message) -> {
                RealtimeEventStreamMessage streamMessage = decodeRealtimeEventStreamMessage(message);
                if("tickle".equals(streamMessage.getType()) && "push".equals(streamMessage.getSubtype())) {
                    Tools.debug("[Pushbullet] New tickle push received.");
                    // We then need to check pushes and keep the new ones for our device into account
                    List<Push> newPushes = getListOfPushes(lastPushReceivedTime, -1);
                    for(Push newPush : newPushes) {
                        String targetDeviceIden = newPush.getTargetDeviceIden();
                        if(targetDeviceIden != null && targetDeviceIden.equals(deviceIden)) {
                            String newPushBody = newPush.getBody();
                            String response;
                            if(router != null)
                                response = router.getResponse(newPushBody, null);
                            else
                                response = "Sorry, no router is defined. I can't understand your request.";
                            Tools.log("[Pushbullet] New push detected for us: " + newPush.getBody());
                            sendNewPush("Response", response, newPush.getSourceDeviceIden());
                        }
                        if(newPush.getLastModificationTimestamp() > lastPushReceivedTime)
                            lastPushReceivedTime = newPush.getLastModificationTimestamp();
                    }
                }
                Tools.debug("[Pushbullet] Realtime Event Stream message received: " + message);
            });
            Tools.log("[Pushbullet] Websocket session opened with Pushbullet servers.");
        }

        @Override
        public void onClose(Session session, CloseReason closeReason) {
            Tools.log("[Pushbullet] Session closed.");
        }
        
        @Override
        public void onError(Session session, Throwable thr) {
            Tools.log("[Pushbullet] Error: " + thr.getMessage());
        }
        
        private RealtimeEventStreamMessage decodeRealtimeEventStreamMessage(String message) {
            return deserializePushbulletEntity(message, RealtimeEventStreamMessage.class);
        }
    };
}
