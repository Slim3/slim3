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
package org.slim3.commons.bean;

import java.util.Date;

import junit.framework.TestCase;

import org.slim3.commons.exception.CastRuntimeException;

/**
 * @author higa
 * 
 */
public class DateConverterTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testGetAsObjectAndGetAsString() throws Exception {
        DateConverter converter = new DateConverter("yyyy/MM/dd");
        Date result = (Date) converter.getAsObject("2008/01/16");
        System.out.println(result);
        assertEquals("2008/01/16", converter.getAsString(result));
    }

    /**
     * @throws Exception
     */
    public void testGetAsStringForCastException() throws Exception {
        DateConverter converter = new DateConverter("yyyy/MM/dd");
        try {
            converter.getAsString("aaa");
            fail();
        } catch (CastRuntimeException e) {
            System.out.println(e);
            assertEquals(String.class, e.getSrcClass());
            assertEquals(Date.class, e.getDestClass());
        }
    }

    /**
     * @throws Exception
     */
    public void testConstructorForNull() throws Exception {
        try {
            new DateConverter(null);
        } catch (NullPointerException e) {
            System.out.println(e);
        }
    }

    /**
     * @throws Exception
     */
    public void testIsTarget() throws Exception {
        DateConverter converter = new DateConverter("MM/dd/yyyy");
        assertTrue(converter.isTarget(java.util.Date.class));
        assertFalse(converter.isTarget(java.sql.Date.class));
    }
}