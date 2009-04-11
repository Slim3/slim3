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

import java.sql.Timestamp;
import java.util.Date;

import junit.framework.TestCase;

import org.slim3.commons.bean.DateConverter;
import org.slim3.commons.bean.SqlDateConverter;
import org.slim3.commons.exception.CastRuntimeException;

/**
 * @author higa
 * 
 */
public class SqlDateConverterTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testGetAsObjectAndGetAsString() throws Exception {
        SqlDateConverter converter = new SqlDateConverter("MM/dd/yyyy");
        java.sql.Date result = (java.sql.Date) converter
                .getAsObject("01/16/2008");
        System.out.println(result);
        assertEquals("01/16/2008", converter.getAsString(result));
    }

    /**
     * @throws Exception
     */
    public void testGetAsStringForCastException() throws Exception {
        SqlDateConverter converter = new SqlDateConverter("MM/dd/yyyy");
        try {
            converter.getAsString("aaa");
            fail();
        } catch (CastRuntimeException e) {
            System.out.println(e);
            assertEquals(String.class, e.getSrcClass());
            assertEquals(java.sql.Date.class, e.getDestClass());
        }
    }

    /**
     * @throws Exception
     */
    public void testConstructorForNull() throws Exception {
        try {
            new SqlDateConverter(null);
        } catch (NullPointerException e) {
            System.out.println(e);
        }
    }

    /**
     * @throws Exception
     */
    public void testIsTarget() throws Exception {
        DateConverter converter = new DateConverter("yyyy/MM/dd");
        assertTrue(converter.isTarget(Date.class));
        assertFalse(converter.isTarget(Timestamp.class));
    }
}