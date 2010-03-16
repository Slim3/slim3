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
 * This exception is thrown while parsing multipart request.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class FileUploadException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     */
    public FileUploadException() {
    }

    /**
     * Constructor.
     * 
     * @param msg
     *            the error message.
     */
    public FileUploadException(String msg) {
        super(msg);
    }

    /**
     * Constructor.
     * 
     * @param msg
     *            The exceptions detail message.
     * @param cause
     *            The exceptions cause.
     */
    public FileUploadException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
