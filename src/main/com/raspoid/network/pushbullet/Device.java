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

/**
 * <b>Abstraction of a Device entity from the Pushbullet API.</b>
 * 
 * <p>Pushbullet API: <a href="https://docs.pushbullet.com/">https://docs.pushbullet.com</a></p>
 * 
 * <p><b>! ATTENTION ! Classical Java naming conventions can't be respected here.
 * The name of variables must respect the deserialized Pushbullet json fields.</b></p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class Device {
    private String iden;
    private boolean active;
    private double created;
    private double modified;
    private String icon;
    private String nickname;
    private boolean generated_nickname; // NOSONAR generated_nickname
    private String manufacturer;
    private String model;
    private int app_version; // NOSONAR app_version
    private String fingerprint;
    private String key_fingerprint; // NOSONAR key_fingerprint
    private String push_token; // NOSONAR push_token
    private String has_sms; // NOSONAR has_sms
    
    /**
     * Constructor for a new Device entity with a specific nickname, manufacturer and model.
     * @param nickname the nickname of the new device.
     * @param manufacturer the manufacturer of the device.
     * @param model the model of the device.
     */
    public Device(String nickname, String manufacturer, String model) {
        this.nickname = nickname;
        this.manufacturer = manufacturer;
        this.model = model;
    }
    
    /**
     * Get the unique identifier for this device.
     * @return the unique identifier for this device.
     */
    public String getIden() {
        return iden;
    }
    
    /**
     * Get the status of the device.
     * @return true if the device is active. False if the device has been deleted.
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * Get the creation timestamp, in floating point seconds.
     * @return the creation timestamp.
     */
    public double getCreated() {
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
     * Get the icon to use for this device, can be an arbitrary string.
     * Commonly used values are: "desktop", "browser", "website", "laptop", 
     * "tablet", "phone", "watch", "system"
     * @return the icon to use for this device.
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Get the nickname corresponding to this device.
     * The nickname is the name to use when displaying the device.
     * @return the nickname corresponding to this device.
     */
    public String getNickname() {
        return nickname;
    }
    
    /**
     * Get true if the nickname was automatically generated from the manufacturer and model fields (only used for some android phones).
     * @return true if the nickname was automatically generated from the manufacturer and model fields (only used for some android phones).
     */
    public boolean getGeneratedNickname() {
        return generated_nickname;
    }
    
    /**
     * Get the manufacturer of the device.
     * @return the manufacturer of the device.
     */
    public String getManufacturer() {
        return manufacturer;
    }
    
    /**
     * Get the model of the device.
     * @return the model of the device.
     */
    public String getModel() {
        return model;
    }
    
    /**
     * Get the version of the Pushbullet application installed on the device.
     * @return the version of the Pushbullet application installed on the device.
     */
    public int getAppVersion() {
        return app_version;
    }
    
    /**
     * Get the String fingerprint for the device, used by apps to avoid duplicate devices. Value is platform-specific.
     * @return the String fingerprint for the device, used by apps to avoid duplicate devices. Value is platform-specific.
     */
    public String getFingerprint() {
        return fingerprint;
    }
    
    /**
     * Get the fingerprint for the device's end-to-end encryption key, 
     * used to determine which devices the current device (based on its own key fingerprint) 
     * will be able to talk to.
     * @return the fingerprint for the device's end-to-end encryption key.
     */
    public String getKeyFingerprint() {
        return key_fingerprint;
    }
    
    /**
     * Get the platform-specific push token. Normally a prefix followed by an identifier.
     * @return the platform-specific push token. Normally a prefix followed by an identifier.
     */
    public String getPushToken() {
        return push_token;
    }
    
    /**
     * Get true if the devices has SMS capability, currently only true for type="android" devices.
     * @return true if the devices has SMS capability, currently only true for type="android" devices.
     */
    public String getHasSms() {
        return has_sms;
    }
    
    @Override
    public String toString() {
        return "nickname: " + nickname + 
                " | manufacturer: " + manufacturer +
                " | iden: " + iden +
                " | active: " + active;
    }
}
