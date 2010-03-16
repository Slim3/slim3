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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A utility class for {@link Date}.
 * 
 * @author higa
 * @version 3.0
 */
public final class DateUtil {

    /**
     * The date pattern for ISO-8601.
     */
    public static final String ISO_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * The time pattern for ISO-8601.
     */
    public static final String ISO_TIME_PATTERN = "HH:mm:ss";

    /**
     * The date time pattern for ISO-8601.
     */
    public static final String ISO_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * Converts the object to {@link Date}.
     * 
     * @param o
     *            the object
     * @return the converted value
     */
    public static Date toDate(Object o) {
        if (o == null) {
            return null;
        } else if (o.getClass() == Date.class) {
            return (Date) o;
        } else if (o instanceof Date) {
            return new Date(((Date) o).getTime());
        } else if (o instanceof Calendar) {
            return new Date(((Calendar) o).getTime().getTime());
        } else {
            return toDate(o.toString(), ISO_DATE_PATTERN);
        }
    }

    /**
     * Converts the object to the date part of the date.
     * 
     * @param o
     *            the object
     * @return the date part of the date
     */
    public static Date toDateAndClearTimePart(Object o) {
        return clearTimePart(toDate(o));
    }

    /**
     * Converts the object to the time part of the date.
     * 
     * @param o
     *            the object
     * @return the time part of the date
     */
    public static Date toDateAndClearDatePart(Object o) {
        return clearDatePart(toDate(o));
    }

    /**
     * Converts the text to {@link Date}.
     * 
     * @param text
     *            the text
     * @param pattern
     *            the pattern for {@link SimpleDateFormat}
     * @return the converted value
     * @throws NullPointerException
     *             if the pattern parameter is null
     * @throws WrapRuntimeException
     *             if an error occurred while parsing the text
     */
    public static Date toDate(String text, String pattern)
            throws NullPointerException, WrapRuntimeException {
        if (pattern == null) {
            throw new NullPointerException("The pattern parameter is null.");
        }
        if (StringUtil.isEmpty(text)) {
            return null;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            df.setTimeZone(TimeZoneLocator.get());
            return df.parse(text);
        } catch (ParseException cause) {
            throw new WrapRuntimeException(
                "An error occurred while parsing the text("
                    + text
                    + "). Error message: "
                    + cause.getMessage(),
                cause);
        }
    }

    /**
     * Converts {@link Date} to {@link Calendar}.
     * 
     * @param date
     *            the date
     * @return {@link Calendar}
     */
    public static Calendar toCalendar(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal =
            Calendar.getInstance(TimeZoneLocator.get(), LocaleLocator.get());
        cal.setTime(date);
        return cal;
    }

    /**
     * Clears the time part of the date.
     * 
     * @param date
     *            the date
     * @return the date part of the date
     */
    public static Date clearTimePart(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = toCalendar(date);
        cal = clearTimePart(cal);
        return cal.getTime();
    }

    /**
     * Clears the time part of the calendar.
     * 
     * @param cal
     *            the calendar
     * @return the date part of the calendar
     */
    public static Calendar clearTimePart(Calendar cal) {
        if (cal == null) {
            return null;
        }
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    /**
     * Clears the date part of the date.
     * 
     * @param date
     *            the date
     * @return the time part of the date
     */
    public static Date clearDatePart(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = toCalendar(date);
        cal = clearDatePart(cal);
        return cal.getTime();
    }

    /**
     * Clears the date part of the calendar.
     * 
     * @param cal
     *            the calendar
     * @return the time part of the calendar
     */
    public static Calendar clearDatePart(Calendar cal) {
        if (cal == null) {
            return null;
        }
        cal.set(Calendar.YEAR, 1970);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        return cal;
    }

    /**
     * Converts the object value to text.
     * 
     * @param value
     *            the object value
     * @return the converted value
     */
    public static String toString(Object value) {
        return toString(toDate(value));
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
     * 
     * @return the converted value
     */
    public static String toString(Date value) {
        return toString(value, ISO_DATE_PATTERN);
    }

    /**
     * Converts the {@link Date} value to text.
     * 
     * @param value
     *            the {@link Date} value
     * @param pattern
     *            the pattern for {@link SimpleDateFormat}
     * @return the converted value
     * @throws NullPointerException
     *             if the pattern parameter is null
     */
    public static String toString(Date value, String pattern)
            throws NullPointerException {
        if (pattern == null) {
            throw new NullPointerException("The pattern parameter is null.");
        }
        if (value == null) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        df.setTimeZone(TimeZoneLocator.get());
        return df.format(value);
    }

    private DateUtil() {
    }
}