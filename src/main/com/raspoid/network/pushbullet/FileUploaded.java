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
 * <b>Abstraction for the response wo the upload of a file on the Pushbullet services.</b>
 * 
 * <p>Pushbullet API: <a href="https://docs.pushbullet.com/">https://docs.pushbullet.com</a></p>
 * 
 * <p><b>! ATTENTION ! Classical Java naming conventions can't be respected here.
 * The name of variables must respect the deserialized Pushbullet json fields.</b></p>
 * 
 * @author Julien Louette &amp; Ga&euml;l Wittorski
 * @version 1.0
 */
public class FileUploaded {
    
    private String file_name; // NOSONAR
    private String file_type; // NOSONAR
    private String file_url; // NOSONAR
    private String upload_url; // NOSONAR
    
    /**
     * Get the file name that will be used for the file.
     * @return the file name that will be used for the file.
     */
    public String getFileName() {
        return file_name;
    }
    
    /**
     * Get the file type that will be used for the file.
     * @return the file type that will be used for the file.
     */
    public String getFileType() {
        return file_type;
    }
    
    /**
     * Get the URL where the file will be available after it is uploaded.
     * @return the URL where the file will be available after it is uploaded.
     */
    public String getFileUrl() {
        return file_url;
    }
    
    /**
     * Get the URL to POST the file to. The file must be posted using multipart/form-data.
     * @return the URL to POST the file to.
     */
    public String getUploadUrl() {
        return upload_url;
    }
}
