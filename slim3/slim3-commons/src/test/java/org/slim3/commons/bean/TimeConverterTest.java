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

import org.slim3.commons.bean.TimeConverter;
import org.slim3.commons.exception.CastRuntimeException;

/**
 * @author higa
 * 
 */
public class TimeConverterTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testGetAsObjectAndGetAsString() throws Exception {
        TimeConverter converter = new TimeConverter("HH:mm:ss");
        Time result = (Time) converter.getAsObject("12:34:56");
        System.out.println(result);
        assertEquals("12:34:56", converter.getAsString(result));
    }

    /**
     * @throws Exception
     */
    public void testGetAsStringForCastException() throws Exception {
        TimeConverter converter = new TimeConverter("HH:mm:ss");
        try {
            converter.getAsString("aaa");
            fail();
        } catch (CastRuntimeException e) {
            System.out.println(e);
            assertEquals(String.class, e.getSrcClass());
            assertEquals(Time.class, e.getDestClass());
        }
    }

    /**
     * @throws Exception
     */
    public void testConstructorForNull() throws Exception {
        try {
            new TimeConverter(null);
        } catch (NullPointerException e) {
            System.out.println(e);
        }
    }

    /**
     * @throws Exception
     */
    public void testIsTarget() throws Exception {
        TimeConverter converter = new TimeConverter("HH:mm:ss");
        assertTrue(converter.isTarget(Time.class));
        assertFalse(converter.isTarget(Timestamp.class));
    }
}