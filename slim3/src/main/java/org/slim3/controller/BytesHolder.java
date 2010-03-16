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
package org.slim3.controller;

import java.io.Serializable;

/**
 * A class to hold the array of bytes.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class BytesHolder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The array of bytes.
     */
    protected byte[] bytes;

    /**
     * Constructor.
     * 
     * @param bytes
     *            the array of bytes
     * @throws NullPointerException
     *             if the bytes parameter is null
     */
    public BytesHolder(byte[] bytes) throws NullPointerException {
        if (bytes == null) {
            throw new NullPointerException("The bytes parameter is null.");
        }
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
