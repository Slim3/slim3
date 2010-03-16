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
package org.slim3.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;

/**
 * A utility class for {@link Byte}.
 * 
 * @author higa
 * @version 3.0
 */
public final class ByteUtil {

    private static final int BYTE_ARRAY_SIZE = 8 * 1024;

    /**
     * Converts the object to {@link Byte}.
     * 
     * @param o
     *            the object
     * @return the converted value
     */
    public static Byte toByte(Object o) {
        if (o == null) {
            return null;
        } else if (o.getClass() == Byte.class) {
            return (Byte) o;
        } else if (o instanceof Number) {
            return Byte.valueOf(((Number) o).byteValue());
        } else if (o.getClass() == String.class) {
            String s = (String) o;
            if (StringUtil.isEmpty(s)) {
                return null;
            }
            return Byte.valueOf(s);
        } else if (o.getClass() == Boolean.class) {
            return ((Boolean) o).booleanValue() ? Byte.valueOf((byte) 1) : Byte
                .valueOf((byte) 0);
        } else {
            return Byte.valueOf(o.toString());
        }
    }

    /**
     * Converts the object to primitive byte.
     * 
     * @param o
     *            the object
     * @return the converted value
     */
    public static byte toPrimitiveByte(Object o) {
        Byte b = toByte(o);
        if (b == null) {
            return 0;
        }
        return b.byteValue();
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
            ByteArrayOutputStream baos =
                new ByteArrayOutputStream(BYTE_ARRAY_SIZE);
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
     * @param <T>
     *            the return type
     * @param bytes
     *            array of bytes
     * @return object
     */
    @SuppressWarnings("unchecked")
    public static <T> T toObject(byte[] bytes) {
        return (T) toObject(bytes, Thread
            .currentThread()
            .getContextClassLoader());
    }

    /**
     * Converts array of bytes to object.
     * 
     * @param <T>
     *            the return type
     * @param bytes
     *            array of bytes
     * @param classLoader
     *            the class loader
     * @return object
     * @throws NullPointerException
     *             if the classLoader parameter is null
     */
    @SuppressWarnings("unchecked")
    public static <T> T toObject(byte[] bytes, final ClassLoader classLoader)
            throws NullPointerException {
        if (bytes == null) {
            return null;
        }
        if (classLoader == null) {
            throw new NullPointerException("The classLoader parameter is null.");
        }
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais) {
                @Override
                protected Class<?> resolveClass(ObjectStreamClass desc)
                        throws IOException, ClassNotFoundException {
                    String name = desc.getName();
                    try {
                        return Class.forName(name, false, classLoader);
                    } catch (ClassNotFoundException ex) {
                        return super.resolveClass(desc);
                    }
                }
            };
            Object o = null;
            try {
                o = ois.readObject();
            } finally {
                ois.close();
            }
            return (T) o;
        } catch (IOException e) {
            throw new WrapRuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new WrapRuntimeException(e);
        }
    }

    /**
     * Splits the array of bytes.
     * 
     * @param bytes
     *            the array of bytes
     * @param size
     *            the piece size
     * @return the split result.
     * @throws NullPointerException
     *             if the bytes parameter is null
     * @throws IllegalArgumentException
     *             if the size parameter is less than 1
     */
    public static byte[][] split(byte[] bytes, int size)
            throws NullPointerException, IllegalArgumentException {
        if (bytes == null) {
            throw new NullPointerException("The bytes parameter is null.");
        }
        if (size <= 0) {
            throw new IllegalArgumentException(
                "The size parameter must be more than 0.");
        }
        int num = bytes.length / size;
        int mod = bytes.length % size;
        byte[][] ret = mod > 0 ? new byte[num + 1][0] : new byte[num][0];
        for (int i = 0; i < num; i++) {
            ret[i] = new byte[size];
            System.arraycopy(bytes, i * size, ret[i], 0, size);
        }
        if (mod > 0) {
            ret[num] = new byte[mod];
            System.arraycopy(bytes, num * size, ret[num], 0, mod);
        }
        return ret;
    }

    /**
     * Joins the array of array of bytes.
     * 
     * @param bytesArray
     *            the array of array of bytes
     * @return the joined array of bytes
     */
    public static byte[] join(byte[][] bytesArray) {
        if (bytesArray == null) {
            throw new NullPointerException("The bytesArray parameter is null.");
        }
        int length = 0;
        for (byte[] bytes : bytesArray) {
            length += bytes.length;
        }
        byte[] ret = new byte[length];
        int pos = 0;
        for (byte[] bytes : bytesArray) {
            System.arraycopy(bytes, 0, ret, pos, bytes.length);
            pos += bytes.length;
        }
        return ret;
    }

    private ByteUtil() {
    }
}
