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
public class DoubleUtilTest {

    /**
     * @throws Exception
     */
    @Test
    public void toDoubleForNull() throws Exception {
        assertThat(DoubleUtil.toDouble(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toDoubleForEmptyString() throws Exception {
        assertThat(DoubleUtil.toDouble(""), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toDoubleForDouble() throws Exception {
        Double value = Double.valueOf(1);
        assertThat(DoubleUtil.toDouble(value), is(value));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toDoubleForNumber() throws Exception {
        Integer i = Integer.valueOf(1);
        assertThat(DoubleUtil.toDouble(i).doubleValue(), is(1d));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toDoubleForString() throws Exception {
        assertThat(DoubleUtil.toDouble("1").doubleValue(), is(1d));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toDoubleForTrue() throws Exception {
        assertThat(DoubleUtil.toDouble(Boolean.TRUE).doubleValue(), is(1d));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toDoubleForFalse() throws Exception {
        assertThat(DoubleUtil.toDouble(Boolean.FALSE).doubleValue(), is(0d));
    }

    /**
     * @throws Exception
     */
    @Test(expected = NumberFormatException.class)
    public void toDoubleForException() throws Exception {
        DoubleUtil.toDouble("xx");
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveDoubleForNull() throws Exception {
        assertThat(DoubleUtil.toPrimitiveDouble(null), is(0d));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveDoubleForEmptyString() throws Exception {
        assertThat(DoubleUtil.toPrimitiveDouble(""), is(0d));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveDoubleForNumber() throws Exception {
        assertThat(DoubleUtil.toPrimitiveDouble(1), is(1d));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveDoubleForString() throws Exception {
        assertThat(DoubleUtil.toPrimitiveDouble("1"), is(1d));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveDoubleForTrue() throws Exception {
        assertThat(DoubleUtil.toPrimitiveDouble(Boolean.TRUE), is(1d));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveDoubleForFalse() throws Exception {
        assertThat(DoubleUtil.toPrimitiveDouble(Boolean.FALSE), is(0d));
    }

    /**
     * @throws Exception
     */
    @Test(expected = NumberFormatException.class)
    public void toPrimitiveDoubleForException() throws Exception {
        DoubleUtil.toPrimitiveDouble("xx");
    }
}