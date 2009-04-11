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

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.slim3.commons.exception.ParseRuntimeException;

/**
 * A utility class for {@link Timestamp}.
 * 
 * @author higa
 * @version 3.0
 */
public final class TimestampUtil {

    /**
     * The pattern for ISO-8601.
     */
    public static final String ISO_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    private TimestampUtil() {
    }

    /**
     * Converts the object to {@link Timestamp}.
     * 
     * @param o
     *            the object
     * @return the converted value
     */
    public static Timestamp toTimestamp(Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof Timestamp) {
            return (Timestamp) o;
        } else if (o instanceof java.util.Date) {
            return new Timestamp(((java.util.Date) o).getTime());
        } else if (o instanceof java.util.Calendar) {
            return new Timestamp(((java.util.Calendar) o).getTime().getTime());
        } else {
            return toTimestamp(o.toString());
        }
    }

    /**
     * Converts the text to {@link Timestamp}.
     * 
     * @param text
     *            the text
     * @return the converted value
     * @throws ParseRuntimeException
     *             if {@link ParseException} is encountered
     */
    public static Timestamp toTimestamp(String text)
            throws ParseRuntimeException {
        return toTimestamp(text, null);
    }

    /**
     * Converts the text to {@link Timestamp}.
     * 
     * @param text
     *            the text
     * @param pattern
     *            the pattern for {@link SimpleDateFormat}
     * @return the converted value
     * @throws ParseRuntimeException
     *             if {@link ParseException} is encountered
     */
    public static Timestamp toTimestamp(String text, String pattern)
            throws ParseRuntimeException {
        if (StringUtil.isEmpty(text)) {
            return null;
        }
        if (pattern == null) {
            pattern = ISO_PATTERN;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return new Timestamp(df.parse(text).getTime());
        } catch (ParseException e) {
            throw new ParseRuntimeException(text, e);
        }
    }

    /**
     * Converts the object value to text.
     * 
     * @param value
     *            the object value
     * @return the converted value
     */
    public static String toString(Object value) {
        return toString(value, null);
    }

    /**
     * Converts the object value to text.
     * 
     * @param value
     *            the object value
     * @param pattern
     *            the pattern for {@link SimpleDateFormat}
     * @return the converted value
     */
    public static String toString(Object value, String pattern) {
        return toString(toTimestamp(value), pattern);
    }

    /**
     * Converts the {@link Timestamp} value to text.
     * 
     * @param value
     *            the {@link Timestamp} value
     * @return the converted value
     */
    public static String toString(java.sql.Timestamp value) {
        return toString(value, null);
    }

    /**
     * Converts the {@link Timestamp} value to text.
     * 
     * @param value
     *            the {@link Timestamp} value
     * @param pattern
     *            the pattern for {@link SimpleDateFormat}
     * @return the converted value
     */
    public static String toString(java.sql.Timestamp value, String pattern) {
        if (value == null) {
            return null;
        }
        if (pattern == null) {
            pattern = ISO_PATTERN;
        }
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(value);
    }
}