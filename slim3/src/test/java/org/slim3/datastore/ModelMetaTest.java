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

import junit.framework.TestCase;

import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;
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
    public void testPrimitiveShortToLong() throws Exception {
        assertEquals(Long.valueOf(1), meta.primitiveShortToLong((short) 1));
    }

    /**
     * @throws Exception
     */
    public void testToShort() throws Exception {
        assertEquals(Short.valueOf((short) 1), meta.toShort(1L));
        assertNull(meta.toShort(null));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveInt() throws Exception {
        assertEquals(1, meta.toPrimitiveInt(1L));
        assertEquals(0, meta.toPrimitiveInt(null));
    }

    /**
     * @throws Exception
     */
    public void testToInteger() throws Exception {
        assertEquals(Integer.valueOf(1), meta.toInteger(1L));
        assertNull(meta.toInteger(null));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveLong() throws Exception {
        assertEquals(1L, meta.toPrimitiveLong(1L));
        assertEquals(0L, meta.toPrimitiveLong(null));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveFloat() throws Exception {
        assertEquals(1f, meta.toPrimitiveFloat(1d));
        assertEquals(0f, meta.toPrimitiveFloat(null));
    }

    /**
     * @throws Exception
     */
    public void testToFloat() throws Exception {
        assertEquals(Float.valueOf(1), meta.toFloat(1d));
        assertNull(meta.toFloat(null));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveDouble() throws Exception {
        assertEquals(1d, meta.toPrimitiveDouble(1d));
        assertEquals(0d, meta.toPrimitiveDouble(null));
    }

    /**
     * @throws Exception
     */
    public void testToPrimitiveBoolean() throws Exception {
        assertEquals(true, meta.toPrimitiveBoolean(true));
        assertEquals(false, meta.toPrimitiveBoolean(null));
    }

    /**
     * @throws Exception
     */
    public void testToString() throws Exception {
        assertEquals("aaa", meta.toString(new Text("aaa")));
        assertNull(meta.toString(null));
    }

    /**
     * @throws Exception
     */
    public void testShortBlobToBytes() throws Exception {
        byte[] bytes = new byte[] { 1 };
        byte[] bytes2 = meta.toBytes(new ShortBlob(bytes));
        assertEquals(1, bytes2.length);
        assertEquals(1, bytes2[0]);
        assertNull(meta.toBytes((ShortBlob) null));
    }

    /**
     * @throws Exception
     */
    public void testBlobToBytes() throws Exception {
        byte[] bytes = new byte[] { 1 };
        byte[] bytes2 =
            meta.toBytes(new com.google.appengine.api.datastore.Blob(bytes));
        assertEquals(1, bytes2.length);
        assertEquals(1, bytes2[0]);
        assertNull(meta.toBytes((com.google.appengine.api.datastore.Blob) null));
    }

    /**
     * @throws Exception
     */
    public void testShortBlobToSerializable() throws Exception {
        assertEquals("aaa", meta.toSerializable(new ShortBlob(ByteUtil
            .toByteArray("aaa"))));
        assertNull(meta.toSerializable((ShortBlob) null));
    }

    /**
     * @throws Exception
     */
    public void testBlobToSerializable() throws Exception {
        assertEquals("aaa", meta
            .toSerializable(new com.google.appengine.api.datastore.Blob(
                ByteUtil.toByteArray("aaa"))));
        assertNull(meta
            .toSerializable((com.google.appengine.api.datastore.Blob) null));
    }

    /**
     * @throws Exception
     */
    public void testToBigDecimal() throws Exception {
        assertEquals(new BigDecimal("1"), meta.toBigDecimal("1"));
        assertNull(meta.toBigDecimal(null));
    }
}
