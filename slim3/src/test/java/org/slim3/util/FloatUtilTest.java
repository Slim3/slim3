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

import org.slim3.util.FloatUtil;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class FloatUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testToFloatForNull() throws Exception {
        assertNull(FloatUtil.toFloat(null));
    }

    /**
     * @throws Exception
     */
    public void testToFloatForEmptyString() throws Exception {
        assertNull(FloatUtil.toFloat(""));
    }

    /**
     * @throws Exception
     */
    public void testToFloatForFloat() throws Exception {
        Float value = Float.valueOf(1);
        assertEquals(value, FloatUtil.toFloat(value));
    }

    /**
     * @throws Exception
     */
    public void testToFloatForNumber() throws Exception {
        Integer i = Integer.valueOf(1);
        assertEquals(1f, FloatUtil.toFloat(i).floatValue());
    }

    /**
     * @throws Exception
     */
    public void testToFloatForString() throws Exception {
        assertEquals(1f, FloatUtil.toFloat("1").floatValue());
    }

    /**
     * @throws Exception
     */
    public void testToFloatForTrue() throws Exception {
        assertEquals(1f, FloatUtil.toFloat(Boolean.TRUE).floatValue());
    }

    /**
     * @throws Exception
     */
    public void testToFloatForFalse() throws Exception {
        assertEquals(0f, FloatUtil.toFloat(Boolean.FALSE).floatValue());
    }

    /**
     * @throws Exception
     */
    public void testToFloatForException() throws Exception {
        try {
            FloatUtil.toFloat("xx");
            fail();
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveFloatForNull() throws Exception {
        assertEquals(0f, FloatUtil.toPrimitiveFloat(null));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveFloatForEmptyString() throws Exception {
        assertEquals(0f, FloatUtil.toPrimitiveFloat(""));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveFloatForNumber() throws Exception {
        Integer i = Integer.valueOf(1);
        assertEquals(1f, FloatUtil.toPrimitiveFloat(i));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveFloatForString() throws Exception {
        assertEquals(1f, FloatUtil.toPrimitiveFloat("1"));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveFloatForTrue() throws Exception {
        assertEquals(1f, FloatUtil.toPrimitiveFloat(Boolean.TRUE));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveFloatForFalse() throws Exception {
        assertEquals(0f, FloatUtil.toPrimitiveFloat(Boolean.FALSE));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveFloatForException() throws Exception {
        try {
            FloatUtil.toPrimitiveFloat("xx");
            fail();
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
    }
}