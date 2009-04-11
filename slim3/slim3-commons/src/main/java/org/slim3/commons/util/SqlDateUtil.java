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

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slim3.commons.exception.ParseRuntimeException;

/**
 * A utility class for {@link Date}.
 * 
 * @author higa
 * @version 3.0
 */
public final class SqlDateUtil {

    /**
     * The pattern for ISO-8601.
     */
    public static final String ISO_PATTERN = "yyyy-MM-dd";

    private SqlDateUtil() {
    }

    /**
     * Converts the object into {@link Date}.
     * 
     * @param o
     *            the object
     * @return the converted value
     */
    public static Date toDate(Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof java.sql.Date) {
            return (java.sql.Date) o;
        } else if (o instanceof java.util.Date) {
            Calendar cal = Calendar.getInstance();
            cal.setTime((java.util.Date) o);
            return toDate(cal);
        } else if (o instanceof Calendar) {
            Calendar cal = (Calendar) o;
            return toDate(cal);
        } else {
            return toDate(o.toString());
        }
    }

    /**
     * Converts the calendar object into {@link java.sql.Date}.
     * 
     * @param cal
     *            the calendar object
     * @return the converted value
     */
    public static Date toDate(Calendar cal) {
        if (cal == null) {
            return null;
        }
        cal = CalendarUtil.toLocal(cal);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Date(cal.getTimeInMillis());
    }

    /**
     * Converts the text into {@link Date}.
     * 
     * @param text
     *            the text
     * @return the converted value
     * @throws ParseRuntimeException
     *             if {@link ParseException} is encountered
     */
    public static java.sql.Date toDate(String text)
            throws ParseRuntimeException {
        return toDate(text, (String) null);
    }

    /**
     * Converts the text into {@link Date}.
     * 
     * @param text
     *            the text
     * @param pattern
     *            the pattern for {@link SimpleDateFormat}
     * @return the converted value
     * @throws ParseRuntimeException
     *             if {@link ParseException} is encountered
     */
    public static java.sql.Date toDate(String text, String pattern)
            throws ParseRuntimeException {
        if (StringUtil.isEmpty(text)) {
            return null;
        }
        if (pattern == null) {
            pattern = ISO_PATTERN;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return new java.sql.Date(df.parse(text).getTime());
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
        return toString(toDate(value), pattern);
    }

    /**
     * Converts the {@link Date} value to text.
     * 
     * @param value
     *            the {@link Date} value
     * @return the converted value
     */
    public static String toString(java.sql.Date value) {
        return toString(value, null);
    }

    /**
     * Converts the {@link Date} value to text.
     * 
     * @param value
     *            the {@link Date} value
     * @param pattern
     *            the pattern for {@link SimpleDateFormat}
     * @return the converted value
     */
    public static String toString(java.sql.Date value, String pattern) {
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