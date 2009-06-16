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

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class ByteUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testToByteForNull() throws Exception {
        assertNull(ByteUtil.toByte(null));
    }

    /**
     * @throws Exception
     */
    public void testToByteForEmptyString() throws Exception {
        assertNull(ByteUtil.toByte(""));
    }

    /**
     * @throws Exception
     */
    public void testToByteForByte() throws Exception {
        Byte value = Byte.valueOf("1");
        assertEquals(value, ByteUtil.toByte(value));
    }

    /**
     * @throws Exception
     */
    public void testToByteForNumber() throws Exception {
        Integer i = Integer.valueOf(1);
        assertEquals((byte) 1, ByteUtil.toByte(i).byteValue());
    }

    /**
     * @throws Exception
     */
    public void testToByteForString() throws Exception {
        assertEquals((byte) 1, ByteUtil.toByte("1").byteValue());
    }

    /**
     * @throws Exception
     */
    public void testToByteForTrue() throws Exception {
        assertEquals((byte) 1, ByteUtil.toByte(Boolean.TRUE).byteValue());
    }

    /**
     * @throws Exception
     */
    public void testToByteForFalse() throws Exception {
        assertEquals((byte) 0, ByteUtil.toByte(Boolean.FALSE).byteValue());
    }

    /**
     * @throws Exception
     */
    public void testToByteForException() throws Exception {
        try {
            ByteUtil.toByte("xx");
            fail();
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveByteForNull() throws Exception {
        assertEquals(0, ByteUtil.toPrimitiveByte(null));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveByteForEmptyString() throws Exception {
        assertEquals(0, ByteUtil.toPrimitiveByte(""));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveByteForNumber() throws Exception {
        Integer i = Integer.valueOf(1);
        assertEquals((byte) 1, ByteUtil.toPrimitiveByte(i));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveByteForString() throws Exception {
        assertEquals((byte) 1, ByteUtil.toPrimitiveByte("1"));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveByteForTrue() throws Exception {
        assertEquals((byte) 1, ByteUtil.toPrimitiveByte(Boolean.TRUE));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveByteForFalse() throws Exception {
        assertEquals((byte) 0, ByteUtil.toPrimitiveByte(Boolean.FALSE));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveByteForException() throws Exception {
        try {
            ByteUtil.toPrimitiveByte("xx");
            fail();
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
    }

    /**
     * @throws Exception
     */
    public void testToByteArray() throws Exception {
        assertNull(ByteUtil.toByteArray(null));
        assertNotNull(ByteUtil.toByteArray("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testToObject() throws Exception {
        assertNull(ByteUtil.toObject(null));
        assertEquals("aaa", ByteUtil.toObject(ByteUtil.toByteArray("aaa")));
    }

    /**
     * @throws Exception
     */
    public void testSplitForNoMod() throws Exception {
        byte[] bytes = new byte[] { 1, 2, 3, 4 };
        byte[][] ret = ByteUtil.split(bytes, 2);
        assertEquals(2, ret.length);
        assertEquals(2, ret[0].length);
        assertEquals(1, ret[0][0]);
        assertEquals(2, ret[0][1]);
        assertEquals(2, ret[1].length);
        assertEquals(3, ret[1][0]);
        assertEquals(4, ret[1][1]);
    }

    /**
     * @throws Exception
     */
    public void testSplitForMod() throws Exception {
        byte[] bytes = new byte[] { 1, 2, 3, 4, 5 };
        byte[][] ret = ByteUtil.split(bytes, 2);
        assertEquals(3, ret.length);
        assertEquals(2, ret[0].length);
        assertEquals(1, ret[0][0]);
        assertEquals(2, ret[0][1]);
        assertEquals(2, ret[1].length);
        assertEquals(3, ret[1][0]);
        assertEquals(4, ret[1][1]);
        assertEquals(1, ret[2].length);
        assertEquals(5, ret[2][0]);
    }

    /**
     * @throws Exception
     */
    public void testJoin() throws Exception {
        byte[][] bytesArray =
            new byte[][] {
                new byte[] { 1, 2 },
                new byte[] { 3, 4 },
                new byte[] { 5 } };
        byte[] bytes = ByteUtil.join(bytesArray);
        assertEquals(5, bytes.length);
        assertEquals(1, bytes[0]);
        assertEquals(2, bytes[1]);
        assertEquals(3, bytes[2]);
        assertEquals(4, bytes[3]);
        assertEquals(5, bytes[4]);
    }
}