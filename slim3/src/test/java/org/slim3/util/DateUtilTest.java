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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author higa
 * 
 */
public class DateUtilTest {

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        TimeZoneLocator.set(TimeZone.getTimeZone("UTC"));
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        TimeZoneLocator.set(null);
    }

    /**
     * @throws Exception
     */
    @Test
    public void toDateForNull() throws Exception {
        assertThat(DateUtil.toDate(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toDateForNullUsingPattern() throws Exception {
        assertThat(DateUtil.toDate(null, "MM/dd/yyyy"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toDateForPatternNull() throws Exception {
        assertThat(DateUtil.toDate("1970-01-01"), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toDateForDate() throws Exception {
        Date date = new Date();
        assertThat(DateUtil.toDate(date), is(date));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toDateForCalendar() throws Exception {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertThat(DateUtil.toDate(cal), is(date));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toDateForString() throws Exception {
        assertThat(
            DateUtil.toDate("01/17/2008", "MM/dd/yyyy"),
            is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toDateForTimeString() throws Exception {
        assertThat(
            DateUtil.toDate("12:34:56", DateUtil.ISO_TIME_PATTERN),
            is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = WrapRuntimeException.class)
    public void toDateForBadText() throws Exception {
        DateUtil.toDate("xx/17/2008", "MM/dd/yyyy");
    }

    /**
     * @throws Exception
     */
    @Test
    public void toDateForEmptyString() throws Exception {
        assertThat(DateUtil.toDate(""), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toDateWithPattern() throws Exception {
        assertThat(DateUtil.toDate("01/01/1970", "MM/dd/yyyy"), is(new Date(0)));
    }

    /**
     * @throws Exception
     */
    @Test(expected = WrapRuntimeException.class)
    public void toDateWithPatternForBadText() throws Exception {
        DateUtil.toDate("xx/17/2008", "yyyyMMdd");
    }

    /**
     * @throws Exception
     */
    @Test
    public void toCalendar() throws Exception {
        assertThat(DateUtil.toCalendar(new Date()), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void clearTimePartForCalendar() throws Exception {
        java.util.Date date = new java.util.Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        assertThat(DateUtil.clearTimePart(cal2), is(cal));
    }

    /**
     * @throws Exception
     */
    @Test
    public void clearTimePartForDate() throws Exception {
        java.util.Date date = new java.util.Date();
        Calendar cal = Calendar.getInstance(TimeZoneLocator.get());
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        assertThat(DateUtil.clearTimePart(date), is(cal.getTime()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toDateAndClearTimePart() throws Exception {
        java.util.Date date = new java.util.Date();
        Calendar cal = Calendar.getInstance(TimeZoneLocator.get());
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        assertThat(DateUtil.toDateAndClearTimePart(date), is(cal.getTime()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void clearDatePartForCalendar() throws Exception {
        java.util.Date date = new java.util.Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date);
        cal.set(Calendar.YEAR, 1970);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        assertThat(DateUtil.clearDatePart(cal2), is(cal));
    }

    /**
     * @throws Exception
     */
    @Test
    public void clearDatePartForDate() throws Exception {
        java.util.Date date = new java.util.Date();
        Calendar cal = Calendar.getInstance(TimeZoneLocator.get());
        cal.setTime(date);
        cal.set(Calendar.YEAR, 1970);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        assertThat(DateUtil.clearDatePart(date), is(cal.getTime()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toDateAndClearDatePart() throws Exception {
        java.util.Date date = new java.util.Date();
        Calendar cal = Calendar.getInstance(TimeZoneLocator.get());
        cal.setTime(date);
        cal.set(Calendar.YEAR, 1970);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        assertThat(DateUtil.toDateAndClearDatePart(date), is(cal.getTime()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToString() throws Exception {
        assertThat(
            DateUtil.toString(new Date(0), "MM/dd/yyyy"),
            is("01/01/1970"));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void toStringForIllegalArgument() throws Exception {
        DateUtil.toString(new Date(0), "xxx");
    }
}