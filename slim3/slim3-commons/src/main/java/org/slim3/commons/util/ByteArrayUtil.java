/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.slim3.commons.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.SQLException;

import org.slim3.commons.exception.WrapRuntimeException;

/**
 * A utility class for array of bytes.
 * 
 * @author higa
 * @version 3.0
 */
public final class ByteArrayUtil {

    private static final byte[] EMPTY_BYTES = new byte[0];

    private static final int BYTE_ARRAY_SIZE = 8 * 1024;

    private ByteArrayUtil() {
    }

    /**
     * Converts the object to string.
     * 
     * @param o
     *            the object
     * @return the converted value
     */
    public static String toString(Object o) {
        if (o == null) {
            return "null";
        }
        if (o instanceof byte[]) {
            byte[] ba = (byte[]) o;
            return "'(byteLength=" + ba.length + ")'";
        }
        return o.toString();
    }

    /**
     * Converts {@link Blob} to array of bytes.
     * 
     * @param blob
     *            the Blob
     * @return array of bytes
     * @throws SQLException
     *             if SQLException occurred
     */
    public static byte[] toByteArray(Blob blob) throws SQLException {
        if (blob == null) {
            return null;
        }
        final long length = blob.length();
        if (length == 0) {
            return EMPTY_BYTES;
        }
        if (length > Integer.MAX_VALUE) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return blob.getBytes(1L, (int) length);
    }

    /**
     * Converts object to array of bytes.
     * 
     * @param o
     *            object
     * @return array of bytes
     */
    public static byte[] toByteArray(Object o) {
        if (o == null) {
            return null;
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(
                    BYTE_ARRAY_SIZE);
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            try {
                oos.writeObject(o);
            } finally {
                oos.close();
            }
            return baos.toByteArray();
        } catch (IOException e) {
            throw new WrapRuntimeException(e);
        }
    }

    /**
     * Converts array of bytes to object.
     * 
     * @param bytes
     *            array of bytes
     * @return object
     */
    public static Object toObject(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object o = null;
            try {
                o = ois.readObject();
            } finally {
                ois.close();
            }
            return o;
        } catch (IOException e) {
            throw new WrapRuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new WrapRuntimeException(e);
        }
    }
}