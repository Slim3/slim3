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
 * A utility class for {@link Integer}.
 * 
 * @author higa
 * @version 3.0
 */
public final class IntegerUtil {

    private IntegerUtil() {
    }

    /**
     * Converts the object to {@link Integer}.
     * 
     * @param o
     *            the object
     * @return the converted value
     */
    public static Integer toInteger(Object o) {
        if (o == null) {
            return null;
        } else if (o.getClass() == Integer.class) {
            return (Integer) o;
        } else if (o instanceof Number) {
            return Integer.valueOf(((Number) o).intValue());
        } else if (o.getClass() == String.class) {
            String s = (String) o;
            if (StringUtil.isEmpty(s)) {
                return null;
            }
            return Integer.valueOf(s);
        } else if (o.getClass() == Boolean.class) {
            return ((Boolean) o).booleanValue() ? Integer.valueOf(1) : Integer
                .valueOf(0);
        } else if (o instanceof Enum<?>) {
            return ((Enum<?>) o).ordinal();
        } else {
            return Integer.valueOf(o.toString());
        }
    }

    /**
     * Converts the object to primitive integer.
     * 
     * @param o
     *            the object
     * @return the converted value
     */
    public static int toPrimitiveInt(Object o) {
        Integer i = toInteger(o);
        if (i == null) {
            return 0;
        }
        return i.intValue();
    }
}