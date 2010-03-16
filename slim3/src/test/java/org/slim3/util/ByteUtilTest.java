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
public class ByteUtilTest {

    /**
     * @throws Exception
     */
    @Test
    public void toByteForNull() throws Exception {
        assertThat(ByteUtil.toByte(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toByteForEmptyString() throws Exception {
        assertThat(ByteUtil.toByte(""), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toByteForByte() throws Exception {
        Byte value = Byte.valueOf("1");
        assertThat(ByteUtil.toByte(value), is(value));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toByteForNumber() throws Exception {
        Integer i = Integer.valueOf(1);
        assertThat(ByteUtil.toByte(i).byteValue(), is((byte) 1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toByteForString() throws Exception {
        assertThat(ByteUtil.toByte("1").byteValue(), is((byte) 1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toByteForTrue() throws Exception {
        assertThat(ByteUtil.toByte(Boolean.TRUE).byteValue(), is((byte) 1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toByteForFalse() throws Exception {
        assertThat(ByteUtil.toByte(Boolean.FALSE).byteValue(), is((byte) 0));
    }

    /**
     * @throws Exception
     */
    @Test(expected = NumberFormatException.class)
    public void toByteForException() throws Exception {
        ByteUtil.toByte("xx");
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveByteForNull() throws Exception {
        assertThat(ByteUtil.toPrimitiveByte(null), is((byte) 0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveByteForEmptyString() throws Exception {
        assertThat(ByteUtil.toPrimitiveByte(""), is((byte) 0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveByteForNumber() throws Exception {
        Integer i = Integer.valueOf(1);
        assertThat(ByteUtil.toPrimitiveByte(i), is((byte) 1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveByteForString() throws Exception {
        assertThat(ByteUtil.toPrimitiveByte("1"), is((byte) 1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveByteForTrue() throws Exception {
        assertThat(ByteUtil.toPrimitiveByte(Boolean.TRUE), is((byte) 1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toPrimitiveByteForFalse() throws Exception {
        assertThat(ByteUtil.toPrimitiveByte(Boolean.FALSE), is((byte) 0));
    }

    /**
     * @throws Exception
     */
    @Test(expected = NumberFormatException.class)
    public void toPrimitiveByteForIllegalArgument() throws Exception {
        ByteUtil.toPrimitiveByte("xx");
    }

    /**
     * @throws Exception
     */
    @Test(expected = WrapRuntimeException.class)
    public void toByteArray() throws Exception {
        assertThat(ByteUtil.toByteArray(null), is(nullValue()));
        assertThat(ByteUtil.toByteArray("aaa"), is(notNullValue()));
        ByteUtil.toByteArray(new NoSerializable());
    }

    /**
     * @throws Exception
     */
    @Test
    public void toObject() throws Exception {
        assertThat(ByteUtil.toObject(null), is(nullValue()));
        assertThat(
            (String) ByteUtil.toObject(ByteUtil.toByteArray("aaa")),
            is("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void splitForNoMod() throws Exception {
        byte[] bytes = new byte[] { 1, 2, 3, 4 };
        byte[][] ret = ByteUtil.split(bytes, 2);
        assertThat(ret.length, is(2));
        assertThat(ret[0].length, is(2));
        assertThat(ret[0][0], is((byte) 1));
        assertThat(ret[0][1], is((byte) 2));
        assertThat(ret[1].length, is(2));
        assertThat(ret[1][0], is((byte) 3));
        assertThat(ret[1][1], is((byte) 4));
    }

    /**
     * @throws Exception
     */
    @Test
    public void splitForMod() throws Exception {
        byte[] bytes = new byte[] { 1, 2, 3, 4, 5 };
        byte[][] ret = ByteUtil.split(bytes, 2);
        assertThat(ret.length, is(3));
        assertThat(ret[0].length, is(2));
        assertThat(ret[0][0], is((byte) 1));
        assertThat(ret[0][1], is((byte) 2));
        assertThat(ret[1].length, is(2));
        assertThat(ret[1][0], is((byte) 3));
        assertThat(ret[1][1], is((byte) 4));
        assertThat(ret[2].length, is(1));
        assertThat(ret[2][0], is((byte) 5));
    }

    /**
     * @throws Exception
     */
    @Test
    public void join() throws Exception {
        byte[][] bytesArray =
            new byte[][] {
                new byte[] { 1, 2 },
                new byte[] { 3, 4 },
                new byte[] { 5 } };
        byte[] bytes = ByteUtil.join(bytesArray);
        assertThat(bytes.length, is(5));
        assertThat(bytes[0], is((byte) 1));
        assertThat(bytes[1], is((byte) 2));
        assertThat(bytes[2], is((byte) 3));
        assertThat(bytes[3], is((byte) 4));
        assertThat(bytes[4], is((byte) 5));
    }

    private static class NoSerializable {
    }
}