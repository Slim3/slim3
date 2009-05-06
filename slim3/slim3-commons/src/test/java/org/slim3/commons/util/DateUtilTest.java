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

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.slim3.commons.exception.ParseRuntimeException;

/**
 * @author higa
 * 
 */
public class DateUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testToDateForNull() throws Exception {
        assertNull(DateUtil.toDate(null));
    }

    /**
     * @throws Exception
     */
    public void testToDateForNullUsingPattern() throws Exception {
        assertNull(DateUtil.toDate(null, "MM/dd/yyyy"));
    }

    /**
     * @throws Exception
     */
    public void testToDateForPatternNull() throws Exception {
        System.out.println(DateUtil.toDate("1970-01-01"));
    }

    /**
     * @throws Exception
     */
    public void testToDateForDate() throws Exception {
        Date date = new Date();
        assertEquals(date, DateUtil.toDate(date));
    }

    /**
     * @throws Exception
     */
    public void testToDateForCalendar() throws Exception {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertEquals(date, DateUtil.toDate(cal));
    }

    /**
     * @throws Exception
     */
    public void testToDateForString() throws Exception {
        assertNotNull(DateUtil.toDate("01/17/2008", "MM/dd/yyyy"));
    }

    /**
     * @throws Exception
     */
    public void testToDateForTimeString() throws Exception {
        System.out.println(DateUtil.toDate(
            "12:34:56",
            DateUtil.ISO_TIME_PATTERN));
        assertNotNull(DateUtil.toDate("12:34:56", DateUtil.ISO_TIME_PATTERN));
    }

    /**
     * @throws Exception
     */
    public void testToDateForException() throws Exception {
        try {
            DateUtil.toDate("xx/17/2008", "MM/dd/yyyy");
            fail();
        } catch (ParseRuntimeException e) {
            System.out.println(e);
            assertEquals("xx/17/2008", e.getText());
        }
    }

    /**
     * @throws Exception
     */
    public void testToDateForEmptyString() throws Exception {
        assertNull(DateUtil.toDate(""));
    }

    /**
     * @throws Exception
     */
    public void testToDateWithPattern() throws Exception {
        assertNotNull(DateUtil.toDate("02/01/2008", "MM/dd/yyyy"));
    }

    /**
     * @throws Exception
     */
    public void testToDateWithPatternForException() throws Exception {
        try {
            DateUtil.toDate("xx/17/2008", "yyyyMMdd");
            fail();
        } catch (ParseRuntimeException e) {
            System.out.println(e);
            assertEquals("xx/17/2008", e.getText());
        }
    }

    /**
     * @throws Exception
     */
    public void testToCalendar() throws Exception {
        assertNotNull(DateUtil.toCalendar(new Date()));
    }

    /**
     * @throws Exception
     */
    public void testClearTimePartForCalendar() throws Exception {
        java.util.Date date = new java.util.Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        assertEquals(cal, DateUtil.clearTimePart(cal2));
    }

    /**
     * @throws Exception
     */
    public void testClearTimePartForDate() throws Exception {
        java.util.Date date = new java.util.Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        assertEquals(cal.getTime(), DateUtil.clearTimePart(date));
    }

    /**
     * @throws Exception
     */
    public void testToDateAndClearTimePart() throws Exception {
        java.util.Date date = new java.util.Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        assertEquals(cal.getTime(), DateUtil.toDateAndClearTimePart(date));
    }

    /**
     * @throws Exception
     */
    public void testClearDatePartForCalendar() throws Exception {
        java.util.Date date = new java.util.Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date);
        cal.set(Calendar.YEAR, 1970);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        assertEquals(cal, DateUtil.clearDatePart(cal2));
    }

    /**
     * @throws Exception
     */
    public void testClearDatePartForDate() throws Exception {
        java.util.Date date = new java.util.Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.YEAR, 1970);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        assertEquals(cal.getTime(), DateUtil.clearDatePart(date));
    }

    /**
     * @throws Exception
     */
    public void testToDateAndClearDatePart() throws Exception {
        java.util.Date date = new java.util.Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.YEAR, 1970);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        assertEquals(cal.getTime(), DateUtil.toDateAndClearDatePart(date));
    }

    /**
     * @throws Exception
     */
    public void testToString() throws Exception {
        assertEquals("01/01/1970", DateUtil.toString(new Date(0), "MM/dd/yyyy"));
    }

    /**
     * @throws Exception
     */
    public void testToStringForException() throws Exception {
        try {
            DateUtil.toString(new Date(0), "xxx");
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
    }
}