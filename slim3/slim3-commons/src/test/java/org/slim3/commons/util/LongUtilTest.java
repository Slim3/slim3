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
package org.slim3.commons.util;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class LongUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testToLongForNull() throws Exception {
        assertNull(LongUtil.toLong(null));
    }

    /**
     * @throws Exception
     */
    public void testToLongForEmptyString() throws Exception {
        assertNull(LongUtil.toLong(""));
    }

    /**
     * @throws Exception
     */
    public void testToLongForLong() throws Exception {
        Long value = Long.valueOf(1);
        assertEquals(value, LongUtil.toLong(value));
    }

    /**
     * @throws Exception
     */
    public void testToLongForNumber() throws Exception {
        Integer i = Integer.valueOf(1);
        assertEquals(1, LongUtil.toLong(i).longValue());
    }

    /**
     * @throws Exception
     */
    public void testToLongForString() throws Exception {
        assertEquals(1, LongUtil.toLong("1").longValue());
    }

    /**
     * @throws Exception
     */
    public void testToLongForTrue() throws Exception {
        assertEquals(1, LongUtil.toLong(Boolean.TRUE).longValue());
    }

    /**
     * @throws Exception
     */
    public void testToLongForFalse() throws Exception {
        assertEquals(0, LongUtil.toLong(Boolean.FALSE).longValue());
    }

    /**
     * @throws Exception
     */
    public void testToLongForException() throws Exception {
        try {
            LongUtil.toLong("xx");
            fail();
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveLongForNull() throws Exception {
        assertEquals(0, LongUtil.toPrimitiveLong(null));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveLongForEmptyString() throws Exception {
        assertEquals(0, LongUtil.toPrimitiveLong(""));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveLongForNumber() throws Exception {
        Integer i = Integer.valueOf(1);
        assertEquals(1, LongUtil.toPrimitiveLong(i));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveLongForString() throws Exception {
        assertEquals(1, LongUtil.toPrimitiveLong("1"));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveLongForTrue() throws Exception {
        assertEquals(1, LongUtil.toPrimitiveLong(Boolean.TRUE));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveLongForFalse() throws Exception {
        assertEquals(0, LongUtil.toPrimitiveLong(Boolean.FALSE));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveLongForException() throws Exception {
        try {
            LongUtil.toPrimitiveLong("xx");
            fail();
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
    }
}