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
package org.slim3.commons.bean;

import java.sql.Time;
import java.sql.Timestamp;

import junit.framework.TestCase;

import org.slim3.commons.bean.TimestampConverter;
import org.slim3.commons.exception.CastRuntimeException;

/**
 * @author higa
 * 
 */
public class TimestampConverterTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testGetAsObjectAndGetAsString() throws Exception {
        TimestampConverter converter = new TimestampConverter(
                "MM/dd/yyyy HH:mm:ss");
        Timestamp result = (Timestamp) converter
                .getAsObject("01/16/2008 12:34:56");
        System.out.println(result);
        assertEquals("01/16/2008 12:34:56", converter.getAsString(result));
    }

    /**
     * @throws Exception
     */
    public void testGetAsStringForCastException() throws Exception {
        TimestampConverter converter = new TimestampConverter(
                "MM/dd/yyyy HH:mm:ss");
        try {
            converter.getAsString("aaa");
            fail();
        } catch (CastRuntimeException e) {
            System.out.println(e);
            assertEquals(String.class, e.getSrcClass());
            assertEquals(Timestamp.class, e.getDestClass());
        }
    }

    /**
     * @throws Exception
     */
    public void testConstructorForNull() throws Exception {
        try {
            new TimestampConverter(null);
        } catch (NullPointerException e) {
            System.out.println(e);
        }
    }

    /**
     * @throws Exception
     */
    public void testIsTarget() throws Exception {
        TimestampConverter converter = new TimestampConverter(
                "MM/dd/yyyy HH:mm:ss");
        assertTrue(converter.isTarget(Timestamp.class));
        assertFalse(converter.isTarget(Time.class));
    }
}