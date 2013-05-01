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

import java.math.BigDecimal;
import java.util.Date;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * A utility class for conversion.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public final class ConversionUtil {

    /**
     * Converts the value into the destination class.
     * 
     * @param <T>
     *            the type
     * @param value
     *            the value
     * @param destinationClass
     *            the destination class
     * @return a converted value
     */
    @SuppressWarnings("unchecked")
    public static <T> T convert(Object value, Class<T> destinationClass) {
        if (destinationClass.isPrimitive()) {
            return (T) convertToPrimitiveWrapper(value, destinationClass);
        } else if (value == null) {
            return null;
        } else if (destinationClass.isInstance(value)) {
            return (T) value;
        } else if (Number.class.isAssignableFrom(destinationClass)) {
            return (T) convertToNumber(value, destinationClass);
        } else if (java.util.Date.class.isAssignableFrom(destinationClass)) {
            return (T) convertToDate(value, destinationClass);
        } else if (destinationClass == Boolean.class) {
            return (T) BooleanUtil.toBoolean(value);
        } else if (destinationClass.isEnum()) {
            return (T) convertToEnum(value, destinationClass);
        } else if (destinationClass == String.class) {
            return (T) value.toString();
        } else if (destinationClass == Key.class) {
            return (T) convertToKey(value);
        } else {
            throw new IllegalArgumentException("The class("
                + value.getClass().getName()
                + ") can not be converted to the class("
                + destinationClass.getName()
                + ").");
        }
    }

    private static Object convertToPrimitiveWrapper(Object value,
            Class<?> destinationClass) {
        if (destinationClass == int.class) {
            Integer i = IntegerUtil.toInteger(value);
            if (i != null) {
                return i;
            }
            return Integer.valueOf(0);
        } else if (destinationClass == boolean.class) {
            Boolean b = BooleanUtil.toBoolean(value);
            if (b != null) {
                return b;
            }
            return Boolean.FALSE;
        } else if (destinationClass == long.class) {
            Long l = LongUtil.toLong(value);
            if (l != null) {
                return l;
            }
            return Long.valueOf(0);
        } else if (destinationClass == double.class) {
            Double d = DoubleUtil.toDouble(value);
            if (d != null) {
                return d;
            }
            return Double.valueOf(0);
        } else if (destinationClass == short.class) {
            Short s = ShortUtil.toShort(value);
            if (s != null) {
                return s;
            }
            return Short.valueOf((short) 0);
        } else if (destinationClass == float.class) {
            Float f = FloatUtil.toFloat(value);
            if (f != null) {
                return f;
            }
            return Float.valueOf(0);
        } else if (destinationClass == byte.class) {
            Byte b = ByteUtil.toByte(value);
            if (b != null) {
                return b;
            }
            return Byte.valueOf((byte) 0);
        }
        throw new IllegalArgumentException("Unsupported primitive class: "
            + destinationClass.getName());
    }

    private static Number convertToNumber(Object value,
            Class<?> destinationClass) {
        if (destinationClass == Integer.class) {
            return IntegerUtil.toInteger(value);
        } else if (destinationClass == Long.class) {
            return LongUtil.toLong(value);
        } else if (destinationClass == BigDecimal.class) {
            return BigDecimalUtil.toBigDecimal(value);
        } else if (destinationClass == Double.class) {
            return DoubleUtil.toDouble(value);
        } else if (destinationClass == Short.class) {
            return ShortUtil.toShort(value);
        } else if (destinationClass == Byte.class) {
            return ByteUtil.toByte(value);
        } else if (destinationClass == Float.class) {
            return FloatUtil.toFloat(value);
        }
        throw new IllegalArgumentException("Unsupported number class: "
            + destinationClass.getName());
    }

    private static Date convertToDate(Object value, Class<?> destinationClass) {
        if (destinationClass == Date.class) {
            return DateUtil.toDate(value);
        }
        throw new IllegalArgumentException("Unsupported date class: "
            + destinationClass.getName());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Enum<?> convertToEnum(Object value, Class destinationClass) {
        if (value.getClass() == String.class) {
            return Enum.valueOf(destinationClass, (String) value);
        }
        if (value instanceof Number) {
            int ordinal = IntegerUtil.toPrimitiveInt(value);
            return (Enum<?>) destinationClass.getEnumConstants()[ordinal];
        }
        throw new IllegalArgumentException("The class("
            + value.getClass().getName()
            + ") can not be converted to enum("
            + destinationClass.getName()
            + ").");
    }

    private static Key convertToKey(Object value) {
        if (value instanceof Key) {
            return (Key) value;
        }
        if (value.getClass() == String.class) {
            return KeyFactory.stringToKey((String) value);
        }
        throw new IllegalArgumentException("The class("
            + value.getClass().getName()
            + ") can not be converted to a key.");
    }

    private ConversionUtil() {
    }
}