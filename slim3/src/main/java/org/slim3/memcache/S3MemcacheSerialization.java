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
package org.slim3.memcache;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

import com.google.appengine.api.memcache.InvalidValueException;
import com.google.appengine.api.memcache.MemcacheSerialization.Flag;

/**
 * A memcache utility class to serialize and deserialize an object.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public final class S3MemcacheSerialization {

    /**
     * Deserializes the array of bytes.
     * 
     * @param value
     *            the value
     * @param flags
     *            the flags
     * @return an object
     * @throws ClassNotFoundException
     *             if {@link ClassNotFoundException} occurred
     * @throws IOException
     *             if {@link IOException} occurred
     */
    public static Object deserialize(byte value[], int flags)
            throws ClassNotFoundException, IOException {
        Flag flagval = Flag.fromInt(flags);
        switch (flagval) {
        case BYTES:
            return value;
        case BOOLEAN:
            if (value.length != 1) {
                throw new InvalidValueException(
                    "Cannot deserialize Boolean: bad length");
            }
            switch (value[0]) {
            case 49:
                return Boolean.TRUE;
            case 48:
                return Boolean.FALSE;
            }
            throw new InvalidValueException(
                "Cannot deserialize Boolean: bad contents");
        case BYTE:
            return Byte.valueOf(new String(value, "US-ASCII"));
        case SHORT:
            return Short.valueOf(new String(value, "US-ASCII"));
        case INTEGER:
            return Integer.valueOf(new String(value, "US-ASCII"));
        case LONG:
            return Long.valueOf(new String(value, "US-ASCII"));
        case UTF8:
            return new String(value, "UTF-8");
        case OBJECT:
            if (value.length == 0) {
                return null;
            }
            ByteArrayInputStream bais = new ByteArrayInputStream(value);
            ObjectInputStream ois = new ObjectInputStream(bais) {
                @Override
                protected Class<?> resolveClass(ObjectStreamClass desc)
                        throws IOException, ClassNotFoundException {
                    String name = desc.getName();
                    try {
                        return Class.forName(name, false, Thread
                            .currentThread()
                            .getContextClassLoader());
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
            return o;
        }
        return null;
    }

    private S3MemcacheSerialization() {
    }
}