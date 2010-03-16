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

/**
 * A utility class for {@link Boolean}.
 * 
 * @author higa
 * 
 */
public final class BooleanUtil {

    private BooleanUtil() {
    }

    /**
     * Converts the object to {@link Boolean}.
     * 
     * @param o
     *            the object
     * @return the converted value
     */
    public static Boolean toBoolean(Object o) {
        if (o == null) {
            return null;
        } else if (o.getClass() == Boolean.class) {
            return (Boolean) o;
        } else if (o instanceof Number) {
            int num = ((Number) o).intValue();
            return Boolean.valueOf(num != 0);
        } else if (o.getClass() == String.class) {
            String s = (String) o;
            if (s.length() == 0) {
                return Boolean.FALSE;
            } else if ("false".equalsIgnoreCase(s)) {
                return Boolean.FALSE;
            } else if ("0".equalsIgnoreCase(s)) {
                return Boolean.FALSE;
            } else if ("on".equalsIgnoreCase(s)) {
                return Boolean.TRUE;
            } else {
                return Boolean.TRUE;
            }
        } else {
            return Boolean.TRUE;
        }
    }

    /**
     * Converts the object into primitive boolean.
     * 
     * @param o
     *            the object
     * @return the converted value
     */
    public static boolean toPrimitiveBoolean(Object o) {
        Boolean b = toBoolean(o);
        if (b != null) {
            return b.booleanValue();
        }
        return false;
    }

    /**
     * Converts the object into primitive int.
     * 
     * @param o
     *            the object
     * @return the converted value
     */
    public static int toPrimitiveInt(Object o) {
        return toPrimitiveBoolean(o) ? 1 : 0;
    }
}