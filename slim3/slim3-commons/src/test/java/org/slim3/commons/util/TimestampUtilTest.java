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
import java.util.Calendar;

import junit.framework.TestCase;

import org.slim3.commons.exception.ParseRuntimeException;

/**
 * @author higa
 * 
 */
public class TimestampUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testToTimestampForTimestamp() throws Exception {
        java.util.Date value = new java.util.Date();
        java.sql.Timestamp timestamp = new java.sql.Timestamp(value.getTime());
        assertEquals(timestamp, TimestampUtil.toTimestamp(timestamp));
    }

    /**
     * @throws Exception
     */
    public void testToTimestampForDate() throws Exception {
        java.util.Date value = new java.util.Date();
        assertEquals(value.getTime(), TimestampUtil.toTimestamp(value)
                .getTime());
    }

    /**
     * @throws Exception
     */
    public void testToTimestampForCalendar() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        assertEquals(0, TimestampUtil.toTimestamp(cal).getTime());
    }

    /**
     * @throws Exception
     */
    public void testToTimestampForEmptyString() throws Exception {
        assertNull(TimestampUtil.toTimestamp(""));
    }

    /**
     * @throws Exception
     */
    public void testToTimestampForPattern() throws Exception {
        assertNotNull(TimestampUtil.toTimestamp("20080201123456",
                "yyyyMMddHHmmss"));
    }

    /**
     * @throws Exception
     */
    public void testToTimestampForDefaultPattern() throws Exception {
        assertNotNull(TimestampUtil.toTimestamp("2008-02-20T12:34:56"));
    }

    /**
     * @throws Exception
     */
    public void testToTimestampWithPatternForException() throws Exception {
        try {
            TimestampUtil.toTimestamp("xx/17/2008", "yyyyMMdd");
            fail();
        } catch (ParseRuntimeException e) {
            System.out.println(e);
            assertEquals("xx/17/2008", e.getText());
        }
    }

    /**
     * @throws Exception
     */
    public void testToStringForDefaultPatten() throws Exception {
        String text = TimestampUtil.toString(new Timestamp(0));
        System.out.println(text);
        assertNotNull(text);
    }

    /**
     * @throws Exception
     */
    public void testToStringForPattern() throws Exception {
        String text = TimestampUtil.toString(new Timestamp(0), "MM/dd/yyyy");
        System.out.println(text);
        assertNotNull(text);
        assertEquals(10, text.length());
    }

    /**
     * @throws Exception
     */
    public void testToStringForDate() throws Exception {
        String text = TimestampUtil.toString(new java.util.Date(0));
        System.out.println(text);
        assertNotNull(text);
    }

    /**
     * @throws Exception
     */
    public void testToStringForException() throws Exception {
        try {
            TimestampUtil.toString(new Timestamp(0), "xxx");
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
    }
}