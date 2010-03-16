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
public class LongUtilTest {

    /**
     * @throws Exception
     */
    @Test
    public void toLongForNull() throws Exception {
        assertThat(LongUtil.toLong(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toLongForEmptyString() throws Exception {
        assertThat(LongUtil.toLong(""), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toLongForLong() throws Exception {
        Long value = Long.valueOf(1);
        assertThat(LongUtil.toLong(value), is(value));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toLongForNumber() throws Exception {
        assertThat(LongUtil.toLong(1).longValue(), is(1L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toLongForString() throws Exception {
        assertThat(LongUtil.toLong("1").longValue(), is(1L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toLongForTrue() throws Exception {
        assertThat(LongUtil.toLong(Boolean.TRUE).longValue(), is(1L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toLongForFalse() throws Exception {
        assertThat(LongUtil.toLong(Boolean.FALSE).longValue(), is(0L));
    }

    /**
     * @throws Exception
     */
    @Test(expected = NumberFormatException.class)
    public void toLongForException() throws Exception {
        LongUtil.toLong("xx");
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveLongForNull() throws Exception {
        assertThat(LongUtil.toPrimitiveLong(null), is(0L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveLongForEmptyString() throws Exception {
        assertThat(LongUtil.toPrimitiveLong(""), is(0L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveLongForNumber() throws Exception {
        assertThat(LongUtil.toPrimitiveLong(1), is(1L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveLongForString() throws Exception {
        assertThat(LongUtil.toPrimitiveLong("1"), is(1L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveLongForTrue() throws Exception {
        assertThat(LongUtil.toPrimitiveLong(Boolean.TRUE), is(1L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveLongForFalse() throws Exception {
        assertThat(LongUtil.toPrimitiveLong(Boolean.FALSE), is(0L));
    }

    /**
     * @throws Exception
     */
    @Test(expected = NumberFormatException.class)
    public void toPrimitiveLongForException() throws Exception {
        LongUtil.toPrimitiveLong("xx");
    }
}