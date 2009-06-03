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
package org.slim3.util;

import java.util.Date;

import junit.framework.TestCase;

/**
 * @author higa
 */
public class CopyOptionsTest extends TestCase {

    private CopyOptions options = new CopyOptions();

    /**
     * @throws Exception
     */
    public void testInclude() throws Exception {
        assertSame(options, options.include("hoge"));
        assertEquals(1, options.includedPropertyNames.length);
        assertEquals("hoge", options.includedPropertyNames[0]);
    }

    /**
     * @throws Exception
     */
    public void testExclude() throws Exception {
        assertSame(options, options.exclude("hoge"));
        assertEquals(1, options.excludedPropertyNames.length);
        assertEquals("hoge", options.excludedPropertyNames[0]);
    }

    /**
     * @throws Exception
     */
    public void testIsTargetProperty() throws Exception {
        assertTrue(options.isTargetProperty("hoge"));
    }

    /**
     * @throws Exception
     */
    public void testIsTargetPropertyForInclude() throws Exception {
        options.include("hoge");
        assertTrue(options.isTargetProperty("hoge"));
        assertFalse(options.isTargetProperty("hoge2"));
    }

    /**
     * @throws Exception
     */
    public void testIsTargetPropertyForExclude() throws Exception {
        options.exclude("hoge");
        assertFalse(options.isTargetProperty("hoge"));
        assertTrue(options.isTargetProperty("hoge2"));
    }

    /**
     * @throws Exception
     */
    public void testIsTargetPropertyForIncludeExclude() throws Exception {
        options.include("hoge", "hoge2").exclude("hoge2", "hoge3");
        assertTrue(options.isTargetProperty("hoge"));
        assertFalse(options.isTargetProperty("hoge2"));
        assertFalse(options.isTargetProperty("hoge3"));
        assertFalse(options.isTargetProperty("hoge4"));
    }

    /**
     * @throws Exception
     */
    public void testIsTargetValue() throws Exception {
        assertTrue(options.isTargetValue("hoge"));
        assertFalse(options.isTargetValue(""));
        assertFalse(options.isTargetValue(null));
    }

    /**
     * @throws Exception
     */
    public void testIsTargetValueForCopyNull() throws Exception {
        options.copyNull();
        assertTrue(options.isTargetValue("hoge"));
        assertFalse(options.isTargetValue(""));
        assertTrue(options.isTargetValue(null));
    }

    /**
     * @throws Exception
     */
    public void testIsTargetValueForCopyEmptyString() throws Exception {
        options.copyEmptyString();
        assertTrue(options.isTargetValue("hoge"));
        assertTrue(options.isTargetValue(""));
        assertFalse(options.isTargetValue(null));
    }

    /**
     * @throws Exception
     */
    public void testFindConvererForDate() throws Exception {
        Converter converter = options.findConverter(java.sql.Date.class);
        assertNotNull(converter);
        assertEquals(DateConverter.class, converter.getClass());
    }

    /**
     * @throws Exception
     */
    public void testFindConvererForNumber() throws Exception {
        Converter converter =
            options.numberConverter("#,###").findConverter(Long.class);
        assertNotNull(converter);
        assertEquals(NumberConverter.class, converter.getClass());
    }

    /**
     * @throws Exception
     */
    public void testConvertObjectForConverterSpecifiedByProperty()
            throws Exception {
        Object value =
            options
                .dateConverter("yyyyMMdd")
                .dateConverter("yyyy", "aaa")
                .convertObject(new Date(0), "aaa", String.class);
        assertEquals("1970", value);
    }

    /**
     * @throws Exception
     */
    public void testConvertObjectForDestClassNullWithConverter()
            throws Exception {
        Object value =
            options.dateConverter("yyyy").convertObject(
                new Date(0),
                "aaa",
                null);
        assertEquals("1970", value);
    }

    /**
     * @throws Exception
     */
    public void testConvertObjectForDestClassStringWithConverter()
            throws Exception {
        Object value =
            options.dateConverter("yyyy").convertObject(
                new Date(0),
                "aaa",
                String.class);
        assertEquals("1970", value);
    }

    /**
     * @throws Exception
     */
    public void testConvertObjectForDestNullNoConverter() throws Exception {
        assertEquals(new Integer(0), options.convertObject(
            new Integer(0),
            "aaa",
            null));
        assertEquals(new Date(0), options.convertObject(
            new Date(0),
            "aaa",
            null));
    }

    /**
     * @throws Exception
     */
    public void testConvertObjectForDestNotString() throws Exception {
        Object value =
            options.dateConverter("yyyy").convertObject(
                new Date(0),
                "aaa",
                Date.class);
        assertEquals(new Date(0), value);
    }

    /**
     * @throws Exception
     */
    public void testConvertStringForConverterSpecifiedByProperty()
            throws Exception {
        Object value =
            options
                .numberConverter("###")
                .numberConverter("#,###", "aaa")
                .convertString("1,111", "aaa", Long.class);
        assertEquals(new Long(1111), value);
    }

    /**
     * @throws Exception
     */
    public void testConvertStringForDestTypeNull() throws Exception {
        Object value =
            options.numberConverter("###").convertString("1,111", "aaa", null);
        assertEquals("1,111", value);
    }

    /**
     * @throws Exception
     */
    public void testConvertStringForDestTypeString() throws Exception {
        Object value =
            options.numberConverter("###").convertString(
                "1,111",
                "aaa",
                String.class);
        assertEquals("1,111", value);
    }

    /**
     * @throws Exception
     */
    public void testConvertStringForConverterSpecifiedByClass()
            throws Exception {
        Object value =
            options.numberConverter("###").convertString(
                "111",
                "aaa",
                Long.class);
        assertEquals(new Long(111), value);
    }

    /**
     * @throws Exception
     */
    public void testConvertValueForNull() throws Exception {
        assertNull(options.convertValue(null, "aaa", Integer.class));
    }

    /**
     * @throws Exception
     */
    public void testConvertValueForBadValue() throws Exception {
        try {
            options.dateConverter("yyyy", "aaa").convertValue(
                new Integer(0),
                "aaa",
                String.class);
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}