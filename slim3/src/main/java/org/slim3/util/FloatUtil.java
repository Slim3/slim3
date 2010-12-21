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
 * A utility class for {@link Float}.
 * 
 * @author higa
 * @version 1.0.0
 */
public final class FloatUtil {

    private FloatUtil() {
    }

    /**
     * Converts the object to {@link Float}.
     * 
     * @param o
     *            the object
     * @return the converted value
     */
    public static Float toFloat(Object o) {
        if (o == null) {
            return null;
        } else if (o.getClass() == Float.class) {
            return (Float) o;
        } else if (o instanceof Number) {
            return Float.valueOf(((Number) o).floatValue());
        } else if (o.getClass() == String.class) {
            String s = (String) o;
            if (StringUtil.isEmpty(s)) {
                return null;
            }
            return Float.valueOf(s);
        } else if (o.getClass() == Boolean.class) {
            return ((Boolean) o).booleanValue() ? Float.valueOf(1) : Float
                .valueOf(0);
        } else {
            return Float.valueOf(o.toString());
        }
    }

    /**
     * Converts the object to primitive float.
     * 
     * @param o
     *            the object
     * @return the converted value
     */
    public static float toPrimitiveFloat(Object o) {
        Float f = toFloat(o);
        if (f == null) {
            return 0;
        }
        return f.floatValue();
    }
}
