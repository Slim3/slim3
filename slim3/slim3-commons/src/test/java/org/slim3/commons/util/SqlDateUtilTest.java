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

import org.slim3.commons.exception.ParseRuntimeException;

/**
 * @author higa
 * 
 */
public class SqlDateUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testToDateForSqlDate() throws Exception {
        java.util.Date value = new java.util.Date();
        java.sql.Date date = new java.sql.Date(value.getTime());
        assertEquals(date, SqlDateUtil.toDate(date));
    }

    /**
     * @throws Exception
     */
    public void testToDateForDate() throws Exception {
        java.util.Date date = new java.util.Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        assertEquals(cal.getTimeInMillis(), SqlDateUtil.toDate(date).getTime());
    }

    /**
     * @throws Exception
     */
    public void testToDateForCalendar() throws Exception {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.setTimeInMillis(0);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(0);
        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);
        assertEquals(cal2.getTime().getTime(), SqlDateUtil.toDate(cal)
                .getTime());
    }

    /**
     * @throws Exception
     */
    public void testToDateForEmptyString() throws Exception {
        assertNull(SqlDateUtil.toDate(""));
    }

    /**
     * @throws Exception
     */
    public void testToDateForPatternNull() throws Exception {
        System.out.println(SqlDateUtil.toDate("1970-01-01"));
    }

    /**
     * @throws Exception
     */
    public void testToDateWithPattern() throws Exception {
        assertNotNull(SqlDateUtil.toDate("02/01/2008", "MM/dd/yyyy"));
    }

    /**
     * @throws Exception
     */
    public void testToDateWithPatternForException() throws Exception {
        try {
            SqlDateUtil.toDate("xx/17/2008", "yyyyMMdd");
            fail();
        } catch (ParseRuntimeException e) {
            System.out.println(e);
            assertEquals("xx/17/2008", e.getText());
        }
    }
}