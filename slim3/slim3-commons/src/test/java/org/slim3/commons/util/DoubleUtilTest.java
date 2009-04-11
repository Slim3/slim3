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

import org.slim3.commons.util.DoubleUtil;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class DoubleUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testToDoubleForNull() throws Exception {
        assertNull(DoubleUtil.toDouble(null));
    }

    /**
     * @throws Exception
     */
    public void testToDoubleForEmptyString() throws Exception {
        assertNull(DoubleUtil.toDouble(""));
    }

    /**
     * @throws Exception
     */
    public void testToDoubleForDouble() throws Exception {
        Double value = Double.valueOf(1);
        assertEquals(value, DoubleUtil.toDouble(value));
    }

    /**
     * @throws Exception
     */
    public void testToDoubleForNumber() throws Exception {
        Integer i = Integer.valueOf(1);
        assertEquals(1d, DoubleUtil.toDouble(i).doubleValue());
    }

    /**
     * @throws Exception
     */
    public void testToDoubleForString() throws Exception {
        assertEquals(1d, DoubleUtil.toDouble("1").doubleValue());
    }

    /**
     * @throws Exception
     */
    public void testToDoubleForTrue() throws Exception {
        assertEquals(1d, DoubleUtil.toDouble(Boolean.TRUE).doubleValue());
    }

    /**
     * @throws Exception
     */
    public void testToDoubleForFalse() throws Exception {
        assertEquals(0d, DoubleUtil.toDouble(Boolean.FALSE).doubleValue());
    }

    /**
     * @throws Exception
     */
    public void testToDoubleForException() throws Exception {
        try {
            DoubleUtil.toDouble("xx");
            fail();
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveDoubleForNull() throws Exception {
        assertEquals(0d, DoubleUtil.toPrimitiveDouble(null));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveDoubleForEmptyString() throws Exception {
        assertEquals(0d, DoubleUtil.toPrimitiveDouble(""));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveDoubleForNumber() throws Exception {
        Integer i = Integer.valueOf(1);
        assertEquals(1d, DoubleUtil.toPrimitiveDouble(i));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveDoubleForString() throws Exception {
        assertEquals(1d, DoubleUtil.toPrimitiveDouble("1"));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveDoubleForTrue() throws Exception {
        assertEquals(1d, DoubleUtil.toPrimitiveDouble(Boolean.TRUE));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveDoubleForFalse() throws Exception {
        assertEquals(0d, DoubleUtil.toPrimitiveDouble(Boolean.FALSE));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveDoubleForException() throws Exception {
        try {
            DoubleUtil.toPrimitiveDouble("xx");
            fail();
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
    }
}