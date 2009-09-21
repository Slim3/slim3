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
package org.slim3.datastore;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;
import org.slim3.datastore.model.MySerializable;
import org.slim3.util.ByteUtil;

import com.google.appengine.api.datastore.ShortBlob;
import com.google.appengine.api.datastore.Text;

/**
 * @author higa
 * 
 */
public class ModelMetaTest extends TestCase {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     */
    public void testConstructor() throws Exception {
        assertEquals(Hoge.class, meta.getModelClass());
    }

    /**
     * @throws Exception
     */
    public void testLongToPrimitiveShort() throws Exception {
        assertEquals((short) 1, meta.longToPrimitiveShort(1L));
        assertEquals((short) 0, meta.longToPrimitiveShort(null));
    }

    /**
     * @throws Exception
     */
    public void testLongToShort() throws Exception {
        assertEquals(Short.valueOf((short) 1), meta.longToShort(1L));
        assertNull(meta.longToShort(null));
    }

    /**
     * @throws Exception
     */
    public void testLongToPrimitiveInt() throws Exception {
        assertEquals(1, meta.longToPrimitiveInt(1L));
        assertEquals(0, meta.longToPrimitiveInt(null));
    }

    /**
     * @throws Exception
     */
    public void testLongToInteger() throws Exception {
        assertEquals(Integer.valueOf(1), meta.longToInteger(1L));
        assertNull(meta.longToInteger(null));
    }

    /**
     * @throws Exception
     */
    public void testLongToPrimitiveLong() throws Exception {
        assertEquals(1L, meta.longToPrimitiveLong(1L));
        assertEquals(0L, meta.longToPrimitiveLong(null));
    }

    /**
     * @throws Exception
     */
    public void testDoubleToPrimitiveFloat() throws Exception {
        assertEquals(1f, meta.doubleToPrimitiveFloat(1d));
        assertEquals(0f, meta.doubleToPrimitiveFloat(null));
    }

    /**
     * @throws Exception
     */
    public void testDoubleToFloat() throws Exception {
        assertEquals(Float.valueOf(1), meta.doubleToFloat(1d));
        assertNull(meta.doubleToFloat(null));
    }

    /**
     * @throws Exception
     */
    public void testDoubleToPrimitiveDouble() throws Exception {
        assertEquals(1d, meta.doubleToPrimitiveDouble(1d));
        assertEquals(0d, meta.doubleToPrimitiveDouble(null));
    }

    /**
     * @throws Exception
     */
    public void testBooleanToPrimitiveBoolean() throws Exception {
        assertEquals(true, meta.booleanToPrimitiveBoolean(true));
        assertEquals(false, meta.booleanToPrimitiveBoolean(null));
    }

    /**
     * @throws Exception
     */
    public void testTextToString() throws Exception {
        assertEquals("aaa", meta.textToString(new Text("aaa")));
        assertNull(meta.textToString(null));
    }

    /**
     * @throws Exception
     */
    public void testStringToText() throws Exception {
        assertEquals(new Text("aaa"), meta.stringToText("aaa"));
        assertNull(meta.stringToText(null));
    }

    /**
     * @throws Exception
     */
    public void testShortBlobToBytes() throws Exception {
        byte[] bytes = new byte[] { 1 };
        byte[] bytes2 = meta.shortBlobToBytes(new ShortBlob(bytes));
        assertEquals(1, bytes2.length);
        assertEquals(1, bytes2[0]);
        assertNull(meta.shortBlobToBytes(null));
    }

    /**
     * @throws Exception
     */
    public void testBytesToShortBlob() throws Exception {
        byte[] bytes = new byte[] { 1 };
        byte[] bytes2 = meta.bytesToShortBlob(bytes).getBytes();
        assertEquals(1, bytes2.length);
        assertEquals(1, bytes2[0]);
        assertNull(meta.bytesToShortBlob(null));
    }

    /**
     * @throws Exception
     */
    public void testBlobToBytes() throws Exception {
        byte[] bytes = new byte[] { 1 };
        byte[] bytes2 =
            meta
                .blobToBytes(new com.google.appengine.api.datastore.Blob(bytes));
        assertEquals(1, bytes2.length);
        assertEquals(1, bytes2[0]);
        assertNull(meta.blobToBytes(null));
    }

    /**
     * @throws Exception
     */
    public void testBytesToBlob() throws Exception {
        byte[] bytes = new byte[] { 1 };
        byte[] bytes2 = meta.bytesToBlob(bytes).getBytes();
        assertEquals(1, bytes2.length);
        assertEquals(1, bytes2[0]);
        assertNull(meta.bytesToBlob(null));
    }

    /**
     * @throws Exception
     */
    public void testShortBlobToSerializable() throws Exception {
        assertEquals("aaa", meta.shortBlobToSerializable(new ShortBlob(ByteUtil
            .toByteArray("aaa"))));
        assertNull(meta.shortBlobToSerializable(null));
    }

    /**
     * @throws Exception
     */
    public void testSerializableToShortBlob() throws Exception {
        MySerializable serializable = new MySerializable("aaa");
        MySerializable serializable2 =
            (MySerializable) ByteUtil.toObject(meta.serializableToShortBlob(
                serializable).getBytes());
        assertEquals("aaa", serializable2.getAaa());
        assertNull(meta.serializableToShortBlob(null));
    }

    /**
     * @throws Exception
     */
    public void testBlobToSerializable() throws Exception {
        assertEquals("aaa", meta
            .blobToSerializable(new com.google.appengine.api.datastore.Blob(
                ByteUtil.toByteArray("aaa"))));
        assertNull(meta.blobToSerializable(null));
    }

    /**
     * @throws Exception
     */
    public void testSerializableToBlob() throws Exception {
        MySerializable serializable = new MySerializable("aaa");
        MySerializable serializable2 =
            (MySerializable) ByteUtil.toObject(meta.serializableToBlob(
                serializable).getBytes());
        assertEquals("aaa", serializable2.getAaa());
        assertNull(meta.serializableToShortBlob(null));
    }

    /**
     * @throws Exception
     */
    public void testStringToBigDecimal() throws Exception {
        assertEquals(new BigDecimal("1"), meta.stringToBigDecimal("1"));
        assertNull(meta.stringToBigDecimal(null));
    }

    /**
     * @throws Exception
     */
    public void testBigDecimalToString() throws Exception {
        assertEquals("1", meta.bigDecimalToString(new BigDecimal("1")));
        assertNull(meta.bigDecimalToString(null));
    }

    /**
     * @throws Exception
     */
    public void testLongListToPrimitiveShortArray() throws Exception {
        List<Long> longList = Arrays.asList(1L);
        short[] shortArray = meta.longListToPrimitiveShortArray(longList);
        assertEquals(1, shortArray.length);
        assertEquals(1, shortArray[0]);
        assertNull(meta.longListToPrimitiveShortArray(null));
    }

    /**
     * @throws Exception
     */
    public void testPrimitiveShortArrayToLongList() throws Exception {
        short[] value = new short[] { 1 };
        List<Long> ret = meta.primitiveShortArrayToLongList(value);
        assertEquals(1, ret.size());
        assertEquals(Long.valueOf(1), ret.get(0));
        assertNull(meta.primitiveShortArrayToLongList(null));
    }

    /**
     * @throws Exception
     */
    public void testLongListToShortArray() throws Exception {
        List<Long> value = Arrays.asList(1L);
        Short[] ret = meta.longListToShortArray(value);
        assertEquals(1, ret.length);
        assertEquals(Short.valueOf((short) 1), ret[0]);
        assertNull(meta.longListToShortArray(null));
    }

    /**
     * @throws Exception
     */
    public void testShortArrayToLongList() throws Exception {
        Short[] value = new Short[] { 1 };
        List<Long> ret = meta.shortArrayToLongList(value);
        assertEquals(1, ret.size());
        assertEquals(Long.valueOf(1), ret.get(0));
        assertNull(meta.shortArrayToLongList(null));
    }

    /**
     * @throws Exception
     */
    public void testLongListToShortList() throws Exception {
        List<Long> value = Arrays.asList(1L);
        List<Short> ret = meta.longListToShortList(value);
        assertEquals(1, ret.size());
        assertEquals(Short.valueOf((short) 1), ret.get(0));
        assertNull(meta.longListToShortList(null));
    }
}
