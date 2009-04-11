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

import junit.framework.TestCase;

import org.slim3.commons.exception.ParseRuntimeException;
import org.slim3.commons.util.NumberUtil;

/**
 * @author higa
 * 
 */
public class NumberUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testToNumber() throws Exception {
        assertEquals(Long.valueOf(100), NumberUtil.toNumber("100", "###"));
    }

    /**
     * @throws Exception
     */
    public void testToNumberForTextEmpty() throws Exception {
        assertNull(NumberUtil.toNumber("", "###"));
    }

    /**
     * @throws Exception
     */
    public void testToNumberForPattenNull() throws Exception {
        try {
            NumberUtil.toNumber("100", null);
            fail();
        } catch (NullPointerException t) {
            System.out.println(t);
        }
    }

    /**
     * @throws Exception
     */
    public void testToNumberForParseException() throws Exception {
        try {
            NumberUtil.toNumber("abc", "###");
            fail();
        } catch (ParseRuntimeException e) {
            System.out.println(e);
            assertEquals("abc", e.getText());
        }
    }

    /**
     * @throws Exception
     */
    public void testToString() throws Exception {
        assertEquals("1,000", NumberUtil.toString(new Integer(1000), "#,###"));
    }

    /**
     * @throws Exception
     */
    public void testToStringForValueNull() throws Exception {
        assertNull(NumberUtil.toString(null, "#,###"));
    }

    /**
     * @throws Exception
     */
    public void testToStringForPattenNull() throws Exception {
        try {
            NumberUtil.toString(new Integer(1000), null);
            fail();
        } catch (NullPointerException t) {
            System.out.println(t);
        }
    }
}