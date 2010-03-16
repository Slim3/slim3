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
 * A utility class for {@link Double}.
 * 
 * @author higa
 * @version 3.0
 */
public final class DoubleUtil {

    private DoubleUtil() {
    }

    /**
     * Converts the object to {@link Double}.
     * 
     * @param o
     *            the object
     * @return the converted value
     */
    public static Double toDouble(Object o) {
        if (o == null) {
            return null;
        } else if (o.getClass() == Double.class) {
            return (Double) o;
        } else if (o instanceof Number) {
            return Double.valueOf(((Number) o).doubleValue());
        } else if (o.getClass() == String.class) {
            String s = (String) o;
            if (StringUtil.isEmpty(s)) {
                return null;
            }
            return Double.valueOf(s);
        } else if (o.getClass() == Boolean.class) {
            return ((Boolean) o).booleanValue() ? Double.valueOf(1) : Double
                .valueOf(0);
        } else {
            return Double.valueOf(o.toString());
        }
    }

    /**
     * Converts the object to primitive double.
     * 
     * @param o
     *            the object
     * @return the converted value
     */
    public static double toPrimitiveDouble(Object o) {
        Double d = toDouble(o);
        if (d == null) {
            return 0;
        }
        return d.doubleValue();
    }
}
