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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author higa
 * 
 */
public class ArrayUtilTest {

    /**
     * @throws Exception
     */
    @Test
    public void add() throws Exception {
        String[] array = new String[] { "aaa", "bbb" };
        String[] ret = ArrayUtil.add(array, "ccc");
        assertThat(ret.length, is(3));
        assertThat(ret[0], is("aaa"));
        assertThat(ret[1], is("bbb"));
        assertThat(ret[2], is("ccc"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void addForArrayNull() throws Exception {
        String[] ret = ArrayUtil.add(null, "aaa");
        assertThat(ret.length, is(1));
        assertThat(ret[0], is("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void addForValueNull() throws Exception {
        String[] array = new String[] { "aaa", "bbb" };
        String[] ret = ArrayUtil.add(array, null);
        assertThat(ret.length, is(3));
        assertThat(ret[0], is("aaa"));
        assertThat(ret[1], is("bbb"));
        assertThat(ret[2], is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void addForArrayNullAndValueNull() throws Exception {
        assertThat(ArrayUtil.add(null, null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void addForNestedArray() throws Exception {
        byte[][] array = new byte[][] { new byte[] { 1 } };
        byte[][] ret = ArrayUtil.<byte[]> add(array, new byte[] { 2 });
        assertThat(ret.length, is(2));
        assertThat(ret[0][0], is((byte) 1));
        assertThat(ret[1][0], is((byte) 2));
    }
}
