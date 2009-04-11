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

import java.math.BigInteger;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class BigIntegerUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testToBigIntegerForNull() throws Exception {
        assertNull(BigIntegerUtil.toBigInteger(null));
    }

    /**
     * @throws Exception
     */
    public void testToBigIntegerForBigInteger() throws Exception {
        BigInteger value = new BigInteger("1");
        assertSame(value, BigIntegerUtil.toBigInteger(value));
    }

    /**
     * @throws Exception
     */
    public void testToBigIntegerForString() throws Exception {
        assertEquals(new BigInteger("1"), BigIntegerUtil.toBigInteger("1"));
    }

    /**
     * @throws Exception
     */
    public void testToBigIntegerForEmptyString() throws Exception {
        assertNull(BigIntegerUtil.toBigInteger(""));
    }
}