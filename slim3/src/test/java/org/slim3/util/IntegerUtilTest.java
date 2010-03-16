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
public class IntegerUtilTest {

    /**
     * @throws Exception
     */
    @Test
    public void toIntegerForNull() throws Exception {
        assertThat(IntegerUtil.toInteger(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toIntegerForEmptyString() throws Exception {
        assertThat(IntegerUtil.toInteger(""), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toIntegerForInteger() throws Exception {
        Integer value = Integer.valueOf(1);
        assertThat(IntegerUtil.toInteger(value), is(value));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toIntegerForNumber() throws Exception {
        Integer i = Integer.valueOf(1);
        assertThat(IntegerUtil.toInteger(i).intValue(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toIntegerForString() throws Exception {
        assertThat(IntegerUtil.toInteger("1").intValue(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toIntegerForTrue() throws Exception {
        assertThat(IntegerUtil.toInteger(Boolean.TRUE).intValue(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toIntegerForFalse() throws Exception {
        assertThat(IntegerUtil.toInteger(Boolean.FALSE).intValue(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toIntegerForEnum() throws Exception {
        assertThat(IntegerUtil.toInteger(MyEnum.TWO).intValue(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test(expected = NumberFormatException.class)
    public void toIntegerForException() throws Exception {
        IntegerUtil.toInteger("xx");
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveIntForNull() throws Exception {
        assertThat(IntegerUtil.toPrimitiveInt(null), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveIntForEmptyString() throws Exception {
        assertThat(IntegerUtil.toPrimitiveInt(""), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveIntForNumber() throws Exception {
        Integer i = Integer.valueOf(1);
        assertThat(IntegerUtil.toPrimitiveInt(i), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveIntForString() throws Exception {
        assertThat(IntegerUtil.toPrimitiveInt("1"), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveIntForTrue() throws Exception {
        assertThat(IntegerUtil.toPrimitiveInt(Boolean.TRUE), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveIntForFalse() throws Exception {
        assertThat(IntegerUtil.toPrimitiveInt(Boolean.FALSE), is(0));
    }

    /**
     * @throws Exception
     */
    @Test(expected = NumberFormatException.class)
    public void toPrimitiveIntForException() throws Exception {
        IntegerUtil.toPrimitiveInt("xx");
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