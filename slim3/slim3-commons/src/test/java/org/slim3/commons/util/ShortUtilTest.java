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

import org.slim3.commons.util.ShortUtil;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class ShortUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testToShortForNull() throws Exception {
        assertNull(ShortUtil.toShort(null));
    }

    /**
     * @throws Exception
     */
    public void testToShortForEmptyString() throws Exception {
        assertNull(ShortUtil.toShort(""));
    }

    /**
     * @throws Exception
     */
    public void testToShortForShort() throws Exception {
        Short value = Short.valueOf("1");
        assertEquals(value, ShortUtil.toShort(value));
    }

    /**
     * @throws Exception
     */
    public void testToShortForNumber() throws Exception {
        Integer i = Integer.valueOf(1);
        assertEquals((short) 1, ShortUtil.toShort(i).shortValue());
    }

    /**
     * @throws Exception
     */
    public void testToShortForString() throws Exception {
        assertEquals((short) 1, ShortUtil.toShort("1").shortValue());
    }

    /**
     * @throws Exception
     */
    public void testToShortForTrue() throws Exception {
        assertEquals((short) 1, ShortUtil.toShort(Boolean.TRUE).shortValue());
    }

    /**
     * @throws Exception
     */
    public void testToShortForFalse() throws Exception {
        assertEquals((short) 0, ShortUtil.toShort(Boolean.FALSE).shortValue());
    }

    /**
     * @throws Exception
     */
    public void testToShortForException() throws Exception {
        try {
            ShortUtil.toShort("xx");
            fail();
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveShortForNull() throws Exception {
        assertEquals(0, ShortUtil.toPrimitiveShort(null));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveShortForEmptyString() throws Exception {
        assertEquals(0, ShortUtil.toPrimitiveShort(""));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveShortForNumber() throws Exception {
        Integer i = Integer.valueOf(1);
        assertEquals((short) 1, ShortUtil.toPrimitiveShort(i));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveShortForString() throws Exception {
        assertEquals((short) 1, ShortUtil.toPrimitiveShort("1"));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveShortForTrue() throws Exception {
        assertEquals((short) 1, ShortUtil.toPrimitiveShort(Boolean.TRUE));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveShortForFalse() throws Exception {
        assertEquals((short) 0, ShortUtil.toPrimitiveShort(Boolean.FALSE));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveShortForException() throws Exception {
        try {
            ShortUtil.toPrimitiveShort("xx");
            fail();
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
    }
}