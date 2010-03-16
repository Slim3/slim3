/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.slim3.controller.upload;

/**
 * A class to access to a file item that was received within a
 * <code>multipart/form-data</code> POST request.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class FileItem {

    /**
     * The file name.
     */
    protected String fileName;

    /**
     * The short file name.
     */
    protected String shortFileName;

    /**
     * The content type.
     */
    protected String contentType;

    /**
     * The data
     */
    protected byte[] data;

    /**
     * Constructor.
     * 
     * @param fileName
     *            the file name
     * @param contentType
     *            the content type
     * @param data
     *            the data
     */
    public FileItem(String fileName, String contentType, byte[] data) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.data = data;
        if (fileName != null) {
            shortFileName = fileName;
            int index = fileName.lastIndexOf("\\");
            if (index < 0) {
                index = fileName.lastIndexOf("/");
            }
            if (index >= 0) {
                shortFileName = fileName.substring(index + 1);
            }
        }
    }

    /**
     * Returns the file name.
     * 
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Returns the short file name.
     * 
     * @return the short file name
     */
    public String getShortFileName() {
        return shortFileName;
    }

    /**
     * Returns the content type.
     * 
     * @return the content type
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Returns the data.
     * 
     * @return the data
     */
    public byte[] getData() {
        return data;
    }
}