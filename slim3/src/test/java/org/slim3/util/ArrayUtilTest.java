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

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class ArrayUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testAdd() throws Exception {
        String[] array = new String[] { "aaa", "bbb" };
        String[] ret = ArrayUtil.add(array, "ccc");
        assertEquals(3, ret.length);
        assertEquals("aaa", ret[0]);
        assertEquals("bbb", ret[1]);
        assertEquals("ccc", ret[2]);
    }

    /**
     * @throws Exception
     */
    public void testAddForArrayNull() throws Exception {
        String[] ret = ArrayUtil.add(null, "aaa");
        assertEquals(1, ret.length);
        assertEquals("aaa", ret[0]);
    }

    /**
     * @throws Exception
     */
    public void testAddForValueNull() throws Exception {
        String[] array = new String[] { "aaa", "bbb" };
        String[] ret = ArrayUtil.add(array, null);
        assertEquals(3, ret.length);
        assertEquals("aaa", ret[0]);
        assertEquals("bbb", ret[1]);
        assertNull(ret[2]);
    }

    /**
     * @throws Exception
     */
    public void testAddForArrayNullAndValueNull() throws Exception {
        assertNull(ArrayUtil.add(null, null));
    }

    /**
     * @throws Exception
     */
    public void testAddForNestedArray() throws Exception {
        byte[][] array = new byte[][] { new byte[] { 1 } };
        byte[][] ret = ArrayUtil.add(array, new byte[] { 2 });
        assertEquals(2, ret.length);
        assertEquals(1, ret[0][0]);
        assertEquals(2, ret[1][0]);
    }
}
