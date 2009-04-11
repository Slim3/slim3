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

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slim3.commons.exception.ParseRuntimeException;

/**
 * A utility class for {@link Time}.
 * 
 * @author higa
 * @version 3.0
 */
public final class TimeUtil {

    /**
     * The pattern for ISO-8601.
     */
    public static final String ISO_PATTERN = "HH:mm:ss";

    private TimeUtil() {
    }

    /**
     * Converts the object to {@link Time}.
     * 
     * @param o
     *            the object
     * @return the converted value
     */
    public static Time toTime(Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof Time) {
            return (Time) o;
        } else if (o instanceof java.util.Date) {
            Calendar cal = Calendar.getInstance();
            cal.setTime((java.util.Date) o);
            return toTime(cal);
        } else if (o instanceof Calendar) {
            Calendar cal = (Calendar) o;
            return toTime(cal);
        } else {
            return toTime(o.toString());
        }
    }

    /**
     * Converts the calendar object into {@link java.sql.Time}.
     * 
     * @param cal
     *            the calendar object
     * @return the converted value
     */
    public static Time toTime(Calendar cal) {
        if (cal == null) {
            return null;
        }
        cal = CalendarUtil.toLocal(cal);
        cal.set(Calendar.YEAR, 1970);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        return new Time(cal.getTimeInMillis());
    }

    /**
     * Converts the text to {@link Time}.
     * 
     * @param text
     *            the text
     * @return the converted value
     */
    public static Time toTime(String text) {
        return toTime(text, null);
    }

    /**
     * Converts the text to {@link Time}.
     * 
     * @param text
     *            the text
     * @param pattern
     *            the pattern for {@link SimpleDateFormat}
     * @return the converted value
     */
    public static Time toTime(String text, String pattern) {
        if (StringUtil.isEmpty(text)) {
            return null;
        }
        if (pattern == null) {
            pattern = ISO_PATTERN;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return new Time(df.parse(text).getTime());
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
        return toString(toTime(value), pattern);
    }

    /**
     * Converts the {@link Time} value to text.
     * 
     * @param value
     *            the {@link Time} value
     * @return the converted value
     */
    public static String toString(java.sql.Time value) {
        return toString(value, null);
    }

    /**
     * Converts the {@link Time} value to text.
     * 
     * @param value
     *            the {@link Time} value
     * @param pattern
     *            the pattern for {@link SimpleDateFormat}
     * @return the converted value
     */
    public static String toString(java.sql.Time value, String pattern) {
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