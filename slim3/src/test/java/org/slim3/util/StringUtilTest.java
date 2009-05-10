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

import org.slim3.util.StringUtil;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class StringUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testIsEmptyForNull() throws Exception {
        assertTrue(StringUtil.isEmpty(null));
    }

    /**
     * @throws Exception
     */
    public void testIsEmptyForEmptyString() throws Exception {
        assertTrue(StringUtil.isEmpty(""));
    }

    /**
     * @throws Exception
     */
    public void testIsEmptyForNotEmptyString() throws Exception {
        assertFalse(StringUtil.isEmpty("a"));
    }

    /**
     * @throws Exception
     */
    public void testDecapitalize() throws Exception {
        assertEquals("aaa", StringUtil.decapitalize("Aaa"));
    }

    /**
     * @throws Exception
     */
    public void testDecapitalizeForOneCharacter() throws Exception {
        assertEquals("a", StringUtil.decapitalize("A"));
    }

    /**
     * @throws Exception
     */
    public void testDecapitalizeForEmpty() throws Exception {
        assertEquals("", StringUtil.decapitalize(""));
    }

    /**
     * @throws Exception
     */
    public void testDecapitalizeForNoDecapitalize() throws Exception {
        assertEquals("URL", StringUtil.decapitalize("URL"));
    }

    /**
     * @throws Exception
     */
    public void testCapitalize() throws Exception {
        assertEquals("Aaa", StringUtil.capitalize("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCapitalizeForChapitalizedString() throws Exception {
        assertEquals("Aaa", StringUtil.capitalize("Aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCapitalizeForEmpty() throws Exception {
        assertEquals("", StringUtil.capitalize(""));
    }

    /**
     * @throws Exception
     */
    public void testSplit() throws Exception {
        String[] array = StringUtil.split("aaa\nbbb", "\n");
        assertEquals(2, array.length);
        assertEquals("aaa", array[0]);
        assertEquals("bbb", array[1]);
    }

    /**
     * 
     */
    public void testSplitIncludingBlankDelimiter() {
        String[] array = StringUtil.split("aaa, bbb", ", ");
        assertEquals(2, array.length);
        assertEquals("aaa", array[0]);
        assertEquals("bbb", array[1]);
    }

    /**
     * 
     */
    public void testToString() {
        assertEquals("2", StringUtil.toString(2));
        assertNull(StringUtil.toString(null));
    }
}