/*
 * Copyright 2004-2009 the original author or authors.
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
package org.slim3.jdo;

import java.io.Serializable;

/**
 * The blob class to store large array of bytes.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class S3Blob implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The value.
     */
    protected byte[] bytes;

    /**
     * Constructor.
     */
    public S3Blob() {
    }

    /**
     * Constructor.
     * 
     * @param bytes
     *            the array of bytes
     */
    public S3Blob(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * Returns the array of bytes.
     * 
     * @return the array of bytes
     */
    public byte[] getBytes() {
        return bytes;
    }
}