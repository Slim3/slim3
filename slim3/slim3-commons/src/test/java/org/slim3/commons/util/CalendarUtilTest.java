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

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class CalendarUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testToCalendarForNull() throws Exception {
        assertNull(CalendarUtil.toCalendar(null));
    }

    /**
     * @throws Exception
     */
    public void testToCalendarForCalendar() throws Exception {
        Calendar cal = Calendar.getInstance();
        assertSame(cal, CalendarUtil.toCalendar(cal));
    }

    /**
     * @throws Exception
     */
    public void testToCalendarForCalendarDeferenceTimeZone() throws Exception {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        assertEquals(TimeZone.getDefault(), CalendarUtil.toCalendar(cal)
                .getTimeZone());
    }

    /**
     * @throws Exception
     */
    public void testToCalendarForDate() throws Exception {
        java.util.Date value = new java.util.Date();
        assertEquals(value, CalendarUtil.toCalendar(value).getTime());
    }

    /**
     * @throws Exception
     */
    public void testToCalendarForEmptyString() throws Exception {
        assertNull(CalendarUtil.toCalendar(""));
    }

    /**
     * @throws Exception
     */
    public void testToLocal() throws Exception {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        System.out.println(cal);
        Calendar cal2 = Calendar.getInstance();
        System.out.println(cal2);
        cal2.setTimeInMillis(cal.getTimeInMillis());
        System.out.println(cal2);
        Calendar cal3 = CalendarUtil.toLocal(cal);
        System.out.println(cal3);
        assertEquals(cal2, cal3);
        assertEquals(cal3.getTimeZone(), TimeZone.getDefault());
        cal.setTimeInMillis(0);
        System.out.println(cal);
        cal2.setTimeInMillis(0);
        System.out.println(cal2);
        System.out.println(cal.getTime());
        System.out.println(cal2.getTime());
        TimeZone defaultTimeZone = TimeZone.getDefault();
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
            System.out.println(cal2.getTime());
        } finally {
            TimeZone.setDefault(defaultTimeZone);
        }
    }

    /**
     * @throws Exception
     */
    public void testToLocalForDefaultTimeZone() throws Exception {
        Calendar cal = Calendar.getInstance();
        assertSame(cal, CalendarUtil.toLocal(cal));
    }
}