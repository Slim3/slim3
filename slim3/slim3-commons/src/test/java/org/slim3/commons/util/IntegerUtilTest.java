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
public class IntegerUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testToIntegerForNull() throws Exception {
        assertNull(IntegerUtil.toInteger(null));
    }

    /**
     * @throws Exception
     */
    public void testToIntegerForEmptyString() throws Exception {
        assertNull(IntegerUtil.toInteger(""));
    }

    /**
     * @throws Exception
     */
    public void testToIntegerForInteger() throws Exception {
        Integer value = Integer.valueOf(1);
        assertEquals(value, IntegerUtil.toInteger(value));
    }

    /**
     * @throws Exception
     */
    public void testToIntegerForNumber() throws Exception {
        Integer i = Integer.valueOf(1);
        assertEquals(1, IntegerUtil.toInteger(i).intValue());
    }

    /**
     * @throws Exception
     */
    public void testToIntegerForString() throws Exception {
        assertEquals(1, IntegerUtil.toInteger("1").intValue());
    }

    /**
     * @throws Exception
     */
    public void testToIntegerForTrue() throws Exception {
        assertEquals(1, IntegerUtil.toInteger(Boolean.TRUE).intValue());
    }

    /**
     * @throws Exception
     */
    public void testToIntegerForFalse() throws Exception {
        assertEquals(0, IntegerUtil.toInteger(Boolean.FALSE).intValue());
    }

    /**
     * @throws Exception
     */
    public void testToIntegerForEnum() throws Exception {
        assertEquals(1, IntegerUtil.toInteger(MyEnum.TWO).intValue());
    }

    /**
     * @throws Exception
     */
    public void testToIntegerForException() throws Exception {
        try {
            IntegerUtil.toInteger("xx");
            fail();
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveIntForNull() throws Exception {
        assertEquals(0, IntegerUtil.toPrimitiveInt(null));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveIntForEmptyString() throws Exception {
        assertEquals(0, IntegerUtil.toPrimitiveInt(""));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveIntForNumber() throws Exception {
        Integer i = Integer.valueOf(1);
        assertEquals(1, IntegerUtil.toPrimitiveInt(i));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveIntForString() throws Exception {
        assertEquals(1, IntegerUtil.toPrimitiveInt("1"));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveIntForTrue() throws Exception {
        assertEquals(1, IntegerUtil.toPrimitiveInt(Boolean.TRUE));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveIntForFalse() throws Exception {
        assertEquals(0, IntegerUtil.toPrimitiveInt(Boolean.FALSE));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveIntForException() throws Exception {
        try {
            IntegerUtil.toPrimitiveInt("xx");
            fail();
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
    }

    private static enum MyEnum {
        /**
         * 
         */
        ONE {
            @Override
            public void hoge() {
            }
        },
        TWO {
            @Override
            public void hoge() {
            }
        };

        public abstract void hoge();
    }
}