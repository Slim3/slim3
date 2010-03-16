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
public class FloatUtilTest {

    /**
     * @throws Exception
     */
    @Test
    public void toFloatForNull() throws Exception {
        assertThat(FloatUtil.toFloat(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toFloatForEmptyString() throws Exception {
        assertThat(FloatUtil.toFloat(""), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toFloatForFloat() throws Exception {
        Float value = Float.valueOf(1);
        assertThat(FloatUtil.toFloat(value), is(value));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toFloatForNumber() throws Exception {
        Integer i = Integer.valueOf(1);
        assertThat(FloatUtil.toFloat(i).floatValue(), is(1f));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toFloatForString() throws Exception {
        assertThat(FloatUtil.toFloat("1").floatValue(), is(1f));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toFloatForTrue() throws Exception {
        assertThat(FloatUtil.toFloat(Boolean.TRUE).floatValue(), is(1f));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toFloatForFalse() throws Exception {
        assertThat(FloatUtil.toFloat(Boolean.FALSE).floatValue(), is(0f));
    }

    /**
     * @throws Exception
     */
    @Test(expected = NumberFormatException.class)
    public void toFloatForException() throws Exception {
        FloatUtil.toFloat("xx");
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveFloatForNull() throws Exception {
        assertThat(FloatUtil.toPrimitiveFloat(null), is(0f));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveFloatForEmptyString() throws Exception {
        assertThat(FloatUtil.toPrimitiveFloat(""), is(0f));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveFloatForNumber() throws Exception {
        Integer i = Integer.valueOf(1);
        assertThat(FloatUtil.toPrimitiveFloat(i), is(1f));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveFloatForString() throws Exception {
        assertThat(FloatUtil.toPrimitiveFloat("1"), is(1f));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveFloatForTrue() throws Exception {
        assertThat(FloatUtil.toPrimitiveFloat(Boolean.TRUE), is(1f));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveFloatForFalse() throws Exception {
        assertThat(FloatUtil.toPrimitiveFloat(Boolean.FALSE), is(0f));
    }

    /**
     * @throws Exception
     */
    @Test(expected = NumberFormatException.class)
    public void toPrimitiveFloatForIllegalArgument() throws Exception {
        FloatUtil.toPrimitiveFloat("xx");
    }
}