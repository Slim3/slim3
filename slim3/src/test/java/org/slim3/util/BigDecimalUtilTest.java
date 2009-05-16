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

import java.math.BigDecimal;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class BigDecimalUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testToBigDecimalForNull() throws Exception {
        assertNull(BigDecimalUtil.toBigDecimal(null));
    }

    /**
     * @throws Exception
     */
    public void testToBigDecimalForBigDecimal() throws Exception {
        BigDecimal value = new BigDecimal(1);
        assertSame(value, BigDecimalUtil.toBigDecimal(value));
    }

    /**
     * @throws Exception
     */
    public void testToBigDecimalForString() throws Exception {
        BigDecimal value = new BigDecimal("1");
        assertEquals(value, BigDecimalUtil.toBigDecimal("1"));
    }

    /**
     * @throws Exception
     */
    public void testToBigDecimalForEmptyString() throws Exception {
        assertNull(BigDecimalUtil.toBigDecimal(""));
    }
}