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
package org.slim3.util;

/**
 * A utility class for {@link Byte}.
 * 
 * @author higa
 * @version 3.0
 */
public final class ByteUtil {

    private ByteUtil() {
    }

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
}
