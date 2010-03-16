/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author higa
 * 
 */
public class NumberUtilTest {

    /**
     * @throws Exception
     */
    @Test
    public void toNumber() throws Exception {
        assertThat((Long) NumberUtil.toNumber("100", "###"), is(100L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toNumberForEmptyString() throws Exception {
        assertThat(NumberUtil.toNumber("", "###"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = NullPointerException.class)
    public void toNumberForPattenNull() throws Exception {
        NumberUtil.toNumber("100", null);
    }

    /**
     * @throws Exception
     */
    @Test(expected = WrapRuntimeException.class)
    public void toNumberForBadText() throws Exception {
        NumberUtil.toNumber("abc", "###");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToString() throws Exception {
        assertThat(NumberUtil.toString(new Integer(1000), "#,###"), is("1,000"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toStringWhenValueIsNull() throws Exception {
        assertThat(NumberUtil.toString(null, "#,###"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = NullPointerException.class)
    public void toStringWhenPattenIsNull() throws Exception {
        NumberUtil.toString(new Integer(1000), null);
    }
}