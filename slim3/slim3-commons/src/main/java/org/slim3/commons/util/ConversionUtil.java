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
package org.slim3.commons.util;

import java.util.Date;

import org.slim3.commons.exception.ClassCanNotAssignedRuntimeException;

/**
 * A utility class for conversion.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public final class ConversionUtil {

    /**
     * Converts the value into the destination class.
     * 
     * @param value
     *            the value
     * @param destinationClass
     *            the destination class
     * @return a converted value
     */
    public static Object convert(Object value, Class<?> destinationClass) {
        if (destinationClass.isPrimitive()) {
            return convertToPrimitiveWrapper(value, destinationClass);
        } else if (value == null) {
            return null;
        } else if (destinationClass.isInstance(value)) {
            return value;
        } else if (Number.class.isAssignableFrom(destinationClass)) {
            return convertToNumber(value, destinationClass);
        } else if (java.util.Date.class.isAssignableFrom(destinationClass)) {
            return convertToDate(value, destinationClass);
        } else if (destinationClass == Boolean.class) {
            return BooleanUtil.toBoolean(value);
        } else if (destinationClass.isEnum()) {
            return convertToEnum(value, destinationClass);
        } else if (destinationClass == String.class) {
            return value.toString();
        } else {
            throw new ClassCanNotAssignedRuntimeException(
                value.getClass(),
                destinationClass);
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

    private static Object convertToNumber(Object value,
            Class<?> destinationClass) {
        if (destinationClass == Integer.class) {
            return IntegerUtil.toInteger(value);
        } else if (destinationClass == Long.class) {
            return LongUtil.toLong(value);
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

    private static Object convertToDate(Object value, Class<?> destinationClass) {
        if (destinationClass == Date.class) {
            return DateUtil.toDate(value);
        }
        throw new IllegalArgumentException("Unsupported date class: "
            + destinationClass.getName());
    }

    private static Object convertToEnum(Object value, Class<?> destinationClass) {
        int ordinal = IntegerUtil.toPrimitiveInt(value);
        return destinationClass.getEnumConstants()[ordinal];
    }

    private ConversionUtil() {
    }
}