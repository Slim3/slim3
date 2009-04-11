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

import java.util.Calendar;
import java.util.TimeZone;

/**
 * A utility class for {@link Calendar}.
 * 
 * @author higa
 * 
 */
public final class CalendarUtil {

    private CalendarUtil() {
    }

    /**
     * Converts the object to {@link Calendar}.
     * 
     * @param o
     *            the object
     * @return the converted value
     */
    public static Calendar toCalendar(Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof Calendar) {
            return toLocal((Calendar) o);
        } else if (o instanceof java.util.Date) {
            Calendar cal = Calendar.getInstance();
            cal.setTime((java.util.Date) o);
            return cal;
        } else {
            java.util.Date date = DateUtil.toDate(o.toString());
            if (date != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                return cal;
            }
            return null;
        }
    }

    /**
     * Converts the calendar to local time.
     * 
     * @param calendar
     *            the calendar
     * @return local time
     */
    public static Calendar toLocal(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        if (calendar.getTimeZone().equals(TimeZone.getDefault())) {
            return calendar;
        }
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTimeInMillis(calendar.getTimeInMillis());
        return localCalendar;
    }
}