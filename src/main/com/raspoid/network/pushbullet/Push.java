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
package com.raspoid.network.pushbullet;

import java.util.List;

/**
 * <b>Abstraction of a Push entity from the Pushbullet API.</b>
 * 
 * <p>Pushbullet API: <a href="https://docs.pushbullet.com/">https://docs.pushbullet.com</a></p>
 * 
 * <p><b>! ATTENTION ! Classical Java naming conventions can't be respected here.
 * The name of variables must respect the deserialized Pushbullet json fields.</b></p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class Push {
    private String iden;
    private boolean active;
    private float created;
    private double modified;
    private String type;
    private boolean dismissed;
    private String guid;
    private String direction; // "self", "outgoing", or "incoming"
    private String sender_iden; // sender_iden NOSONAR
    private String sender_email; // sender_email NOSONAR
    private String sender_email_normalized; // sender_email_normalized NOSONAR
    private String sender_name; // sender_name NOSONAR
    private String receiver_iden; // receiver_iden NOSONAR
    private String receiver_email; // receiver_email NOSONAR
    private String receiver_email_normalized; // receiver_email_normalized NOSONAR
    private String target_device_iden; // target_device_iden NOSONAR
    private String source_device_iden; // source_device_iden NOSONAR
    private String client_iden; // client_iden NOSONAR
    private String channel_iden; // channel_iden NOSONAR
    private List<String> awake_app_guids; // awake_app_guids NOSONAR
    private String title; // !
    private String body; // !
    private String url;
    private String file_name; // file_name NOSONAR
    private String file_type; // file_type NOSONAR
    private String file_url; // file_url NOSONAR
    private String image_url; // image_url NOSONAR
    private int image_width; // image_width NOSONAR
    private int image_height; // image_height NOSONAR
    
    /**
     * Returns true if the push is incoming.
     * <p>Note: a push sent by a user to one of its devices is "self".</p>
     * @return true if the push is incoming.
     */
    public boolean isIncoming() {
        return "incoming".equals(this.direction);
    }
    
    /**
     * Returns true if the push is outgoing.
     * @return true if the push is outgoing.
     */
    public boolean isOutgoing() {
        return "outgoing".equals(this.direction);
    }
    
    /**
     * Get the unique identifier for this push.
     * @return the unique identifier for this push.
     */
    public String getIden() {
        return iden;
    }
    
    /**
     * Get the status of the push.
     * @return true if the push is active. False if the push has been deleted.
     */
    public boolean getActive() {
        return active;
    }
    
    /**
     * Get the creation timestamp, in floating point seconds.
     * @return the creation timestamp, in floating point seconds.
     */
    public float getCreated() {
        return created;
    }
    
    /**
     * Get the last modification timestamp, in floating point seconds.
     * @return the last modification timestamp, in floating point seconds.
     */
    public double getModified() {
        return modified;
    }
    
    /**
     * Get the last modification timestamp, in floating point seconds.
     * @return the last modification timestamp, in floating point seconds.
     * @see #getModified()
     */
    public double getLastModificationTimestamp() {
        return modified;
    }
    
    /**
     * Get the type of the push.
     * <p>A push must be one of the following: "note", "file", "link".</p>
     * @return the type of the push, among "note", "file" and "link".
     */
    public String getType() {
        return type;
    }
    
    /**
     * Returns true if the push has been dismissed by any device or if any 
     * device was active when the push was received.
     * @return true if the push has been dismissed by any device or if any 
     *  device was active when the push was received.
     */
    public boolean getDismissed() {
        return dismissed;
    }
    
    /**
     * Get the Guid of the push. The Guid is a unique identifier set by the client,
     * used to identify a push in case you receive it from /v2/everything before 
     * the call to /v2/pushes has completed. This should be a unique value.
     * @return the Guid of the push.
     */
    public String getGuid() {
        return guid;
    }
    
    /**
     * Get the direction the push was sent in, can be "self", "outgoing", or "incoming".
     * @return the direction of the push, among "self", "outgoing", or "incoming".
     */
    public String getDirection() {
        return direction;
    }
    
    /**
     * Get the user iden of the sender of the push.
     * @return the user iden of the sender of the push.
     */
    public String getSenderIden() {
        return sender_iden;
    }
    
    /**
     * Get the email address of the sender of the push.
     * @return the email address of the sender of the push.
     */
    public String getSenderEmail() {
        return sender_email;
    }
    
    /**
     * Get the normalized email address of the sender of the push.
     * @return the normalized email address of the sender of the push.
     */
    public String getSenderEmailNormalized() {
        return sender_email_normalized;
    }
    
    /**
     * Get the name of the sender of the push.
     * @return the name of the sender of the push.
     */
    public String getSenderName() {
        return sender_name;
    }
    
    /**
     * Get the iden of the receiver of the push.
     * @return the iden of the receiver of the push.
     */
    public String getReceiverIden() {
        return receiver_iden;
    }
    
    /**
     * Get the email address of the receiver of the push.
     * @return the email address of the receiver of the push.
     */
    public String getReceiverEmail() {
        return receiver_email;
    }
    
    /**
     * Get the normalized email address of the receiver of the push.
     * @return the normalized email address of the receiver of the push.
     */
    public String getReceiverEmailNormalized() {
        return receiver_email_normalized;
    }
    
    /**
     * Get the device iden of the target device, if sending to a signe device.
     * @return the device iden of the target device, if sending to a signe device.
     */
    public String getTargetDeviceIden() {
        return target_device_iden;
    }
    
    /**
     * Get the device iden of the sending device. Optionally set by the sender
     * when creating a push.
     * @return the device iden of the sending device. Optionally set by the sender
     *  when creating a push.
     */
    public String getSourceDeviceIden() {
        return source_device_iden;
    }
    
    /**
     * If the push was created by a client, get the iden of the client sending the push.
     * @return if the push was created by a client, get the iden of the client sending the push.
     */
    public String getClientIden() {
        return client_iden;
    }
    
    /**
     * If the push was created by a channel, get the iden of that channel.
     * @return if the push was created by a channel, the iden of that channel. 
     */
    public String getChannelIden() {
        return channel_iden;
    }
    
    /**
     * Get the list of guids (client side identifiers, not the guid field on pushes)
     * for awake apps at the time the push was sent.
     * If the length of this list is > 0, dismissed will be set to true and the awake 
     * app(s) must decide what to do with the notification.
     * @return the list of guids for awake apps at the time the push was sent.
     */
    public List<String> getAwakeAppGuids() {
        return awake_app_guids;
    }
    
    /**
     * Get the title of the push.
     * @return the title of the push.
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Get the body of the push.
     * @return the body of the push.
     */
    public String getBody() {
        return body;
    }
    
    /**
     * Get the URL field, used for type="link" pushes.
     * @return the URL field, used for type="link" pushes.
     */
    public String getUrl() {
        return url;
    }
    
    /**
     * Get the file name, used for type="file" pushes.
     * @return the file name, used for type="file" pushes.
     */
    public String getFileName() {
        return file_name;
    }
    
    /**
     * Get the file mime type, used for type="file" pushes.
     * @return the file mime type, used for type="file" pushes.
     */
    public String getFileType() {
        return file_type;
    }
    
    /**
     * Get the file download url, used for type="file" pushes.
     * @return the file download url, used for type="file" pushes.
     */
    public String getFileUrl() {
        return file_url;
    }
    
    /**
     * Get the URL to an image to use for this push, present on 
     * type="file" pushes if file_type matches image/*.
     * @return the URL to an image to use for this push, present on 
     *  type="file" pushes if file_type matches image/*.
     */
    public String getImageUrl() {
        return image_url;
    }
    
    /**
     * Get the width of image in pixels, only present if image_url is set.
     * @return the width of image in pixels, only present if image_url is set.
     */
    public int getImageWidth() {
        return image_width;
    }
    
    /**
     * Get the height of image in pixels, only present if image_url is set.
     * @return the height of image in pixels, only present if image_url is set.
     */
    public int getImageHeight() {
        return image_height;
    }
    
    @Override
    public String toString() {
        return "title: " + title +
                " | body: " + body +
                " | type: " + type +
                " | direction: " + direction +
                " | iden: " + iden +
                " | sender_iden: " + sender_iden + 
                " | sender_name: " + sender_name +
                " | sender_email: " + sender_email +
                " | receiver_iden: " + receiver_iden +
                " | receiver_email: " + receiver_email +
                " | target_device_iden: " + target_device_iden +
                " | source_device_iden: " + source_device_iden +
                " | active: " + active + 
                " | last modified time: " + modified;
    }
}
