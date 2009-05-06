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
public class BooleanUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testToBooleanForNull() throws Exception {
        assertNull(BooleanUtil.toBoolean(null));
    }

    /**
     * @throws Exception
     */
    public void testToBooleanForEmptyString() throws Exception {
        assertFalse(BooleanUtil.toBoolean(""));
    }

    /**
     * @throws Exception
     */
    public void testToBooleanForBoolean() throws Exception {
        assertSame(Boolean.TRUE, BooleanUtil.toBoolean(Boolean.TRUE));
    }

    /**
     * @throws Exception
     */
    public void testToBooleanForString() throws Exception {
        assertEquals(Boolean.TRUE, BooleanUtil.toBoolean("1"));
        assertEquals(Boolean.FALSE, BooleanUtil.toBoolean("0"));
        assertEquals(Boolean.TRUE, BooleanUtil.toBoolean("true"));
        assertEquals(Boolean.FALSE, BooleanUtil.toBoolean("false"));
    }

    /**
     * @throws Exception
     */
    public void testToBooleanForObject() throws Exception {
        assertEquals(Boolean.TRUE, BooleanUtil.toBoolean(new Object()));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveBooleanForNull() throws Exception {
        assertFalse(BooleanUtil.toPrimitiveBoolean(null));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveBooleanForEmptyString() throws Exception {
        assertFalse(BooleanUtil.toPrimitiveBoolean(""));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveBooleanForBoolean() throws Exception {
        assertTrue(BooleanUtil.toPrimitiveBoolean(Boolean.TRUE));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveInt() throws Exception {
        assertEquals(1, BooleanUtil.toPrimitiveInt(true));
        assertEquals(0, BooleanUtil.toPrimitiveInt(false));
    }
}