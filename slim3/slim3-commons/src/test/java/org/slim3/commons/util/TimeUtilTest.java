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
import java.util.Calendar;

import junit.framework.TestCase;

import org.slim3.commons.exception.ParseRuntimeException;

/**
 * @author higa
 * 
 */
public class TimeUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testToTimeForTime() throws Exception {
        java.sql.Time time = new java.sql.Time(0);
        assertEquals(time, TimeUtil.toTime(time));
    }

    /**
     * @throws Exception
     */
    public void testToTimeForDate() throws Exception {
        java.util.Date date = new java.util.Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.YEAR, 1970);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        Time time = TimeUtil.toTime(date);
        System.out.println(time);
        System.out.println(new java.util.Date(time.getTime()));
        assertEquals(cal.getTimeInMillis(), time.getTime());
    }

    /**
     * @throws Exception
     */
    public void testToTimeForEmptyString() throws Exception {
        assertNull(TimeUtil.toTime(""));
    }

    /**
     * @throws Exception
     */
    public void testToTimeWithPattern() throws Exception {
        assertNotNull(TimeUtil.toTime("123456", "HHmmss"));
    }

    /**
     * @throws Exception
     */
    public void testToTimeWithPatternForException() throws Exception {
        try {
            TimeUtil.toTime("xx:34:56", "HHmmss");
            fail();
        } catch (ParseRuntimeException e) {
            System.out.println(e);
            assertEquals("xx:34:56", e.getText());
        }
    }

    /**
     * @throws Exception
     */
    public void testToTimeForDefaultPattern() throws Exception {
        Time time = TimeUtil.toTime("12:34:56");
        System.out.println(time);
        assertNotNull(time);
    }

    /**
     * @throws Exception
     */
    public void testToStringWithPatten() throws Exception {
        String text = TimeUtil.toString(new Time(0), "mm:ss");
        System.out.println(text);
        assertEquals(5, text.length());
    }

    /**
     * @throws Exception
     */
    public void testToStringForDefaultPatten() throws Exception {
        String text = TimeUtil.toString(new Time(0));
        System.out.println(text);
        assertNotNull(text);
    }

    /**
     * @throws Exception
     */
    public void testToStringForDate() throws Exception {
        String text = TimeUtil.toString(new java.util.Date(0));
        System.out.println(text);
        assertEquals(8, text.length());
    }

    /**
     * @throws Exception
     */
    public void testToStringForException() throws Exception {
        try {
            TimeUtil.toString(new Time(0), "xxx");
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
    }
}