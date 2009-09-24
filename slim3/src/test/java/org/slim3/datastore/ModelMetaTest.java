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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.Vector;

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
    public void testLongListToPrimitiveShortArray() throws Exception {
        List<Long> longList = Arrays.asList(1L);
        short[] ret = meta.longListToPrimitiveShortArray(longList);
        assertEquals(1, ret.length);
        assertEquals(1, ret[0]);
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
    public void testCopyLongListToShortCollection() throws Exception {
        List<Long> value = Arrays.asList(1L);
        Collection<Short> collection = new ArrayList<Short>();
        meta.copyLongListToShortCollection(value, collection);
        assertEquals(1, collection.size());
        assertEquals(Short.valueOf((short) 1), collection.iterator().next());
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

    /**
     * @throws Exception
     */
    public void testLongListToShortSet() throws Exception {
        List<Long> value = Arrays.asList(1L);
        Set<Short> ret = meta.longListToShortSet(value);
        assertEquals(1, ret.size());
        assertEquals(Short.valueOf((short) 1), ret.iterator().next());
        assertNull(meta.longListToShortSet(null));
    }

    /**
     * @throws Exception
     */
    public void testLongListToShortSortedSet() throws Exception {
        List<Long> value = Arrays.asList(1L);
        SortedSet<Short> ret = meta.longListToShortSortedSet(value);
        assertEquals(1, ret.size());
        assertEquals(Short.valueOf((short) 1), ret.iterator().next());
        assertNull(meta.longListToShortSortedSet(null));
    }

    /**
     * @throws Exception
     */
    public void testLongListToShortLinkedList() throws Exception {
        List<Long> value = Arrays.asList(1L);
        LinkedList<Short> ret = meta.longListToShortLinkedList(value);
        assertEquals(1, ret.size());
        assertEquals(Short.valueOf((short) 1), ret.iterator().next());
        assertNull(meta.longListToShortLinkedList(null));
    }

    /**
     * @throws Exception
     */
    public void testLongListToShortLinkedHashSet() throws Exception {
        List<Long> value = Arrays.asList(1L);
        LinkedHashSet<Short> ret = meta.longListToShortLinkedHashSet(value);
        assertEquals(1, ret.size());
        assertEquals(Short.valueOf((short) 1), ret.iterator().next());
        assertNull(meta.longListToShortLinkedHashSet(null));
    }

    /**
     * @throws Exception
     */
    public void testLongListToShortStack() throws Exception {
        List<Long> value = Arrays.asList(1L);
        Stack<Short> ret = meta.longListToShortStack(value);
        assertEquals(1, ret.size());
        assertEquals(Short.valueOf((short) 1), ret.iterator().next());
        assertNull(meta.longListToShortStack(null));
    }

    /**
     * @throws Exception
     */
    public void testLongListToShortVector() throws Exception {
        List<Long> value = Arrays.asList(1L);
        Vector<Short> ret = meta.longListToShortVector(value);
        assertEquals(1, ret.size());
        assertEquals(Short.valueOf((short) 1), ret.iterator().next());
        assertNull(meta.longListToShortVector(null));
    }

    /**
     * @throws Exception
     */
    public void testLongListToPrimitiveIntArray() throws Exception {
        List<Long> longList = Arrays.asList(1L);
        int[] ret = meta.longListToPrimitiveIntArray(longList);
        assertEquals(1, ret.length);
        assertEquals(1, ret[0]);
        assertNull(meta.longListToPrimitiveIntArray(null));
    }

    /**
     * @throws Exception
     */
    public void testPrimitiveIntArrayToLongList() throws Exception {
        int[] value = new int[] { 1 };
        List<Long> ret = meta.primitiveIntArrayToLongList(value);
        assertEquals(1, ret.size());
        assertEquals(Long.valueOf(1), ret.get(0));
        assertNull(meta.primitiveIntArrayToLongList(null));
    }

    /**
     * @throws Exception
     */
    public void testLongListToIntegerArray() throws Exception {
        List<Long> value = Arrays.asList(1L);
        Integer[] ret = meta.longListToIntegerArray(value);
        assertEquals(1, ret.length);
        assertEquals(Integer.valueOf(1), ret[0]);
        assertNull(meta.longListToIntegerArray(null));
    }

    /**
     * @throws Exception
     */
    public void testIntegerArrayToLongList() throws Exception {
        Integer[] value = new Integer[] { 1 };
        List<Long> ret = meta.integerArrayToLongList(value);
        assertEquals(1, ret.size());
        assertEquals(Long.valueOf(1), ret.get(0));
        assertNull(meta.integerArrayToLongList(null));
    }

    /**
     * @throws Exception
     */
    public void testCopyLongListToIntegerCollection() throws Exception {
        List<Long> value = Arrays.asList(1L);
        Collection<Integer> collection = new ArrayList<Integer>();
        meta.copyLongListToIntegerCollection(value, collection);
        assertEquals(1, collection.size());
        assertEquals(Integer.valueOf(1), collection.iterator().next());
    }

    /**
     * @throws Exception
     */
    public void testLongListToIntegerList() throws Exception {
        List<Long> value = Arrays.asList(1L);
        List<Integer> ret = meta.longListToIntegerList(value);
        assertEquals(1, ret.size());
        assertEquals(Integer.valueOf(1), ret.get(0));
        assertNull(meta.longListToIntegerList(null));
    }

    /**
     * @throws Exception
     */
    public void testLongListToIntegerSet() throws Exception {
        List<Long> value = Arrays.asList(1L);
        Set<Integer> ret = meta.longListToIntegerSet(value);
        assertEquals(1, ret.size());
        assertEquals(Integer.valueOf(1), ret.iterator().next());
        assertNull(meta.longListToIntegerSet(null));
    }

    /**
     * @throws Exception
     */
    public void testLongListToIntegerSortedSet() throws Exception {
        List<Long> value = Arrays.asList(1L);
        SortedSet<Integer> ret = meta.longListToIntegerSortedSet(value);
        assertEquals(1, ret.size());
        assertEquals(Integer.valueOf(1), ret.iterator().next());
        assertNull(meta.longListToIntegerSortedSet(null));
    }

    /**
     * @throws Exception
     */
    public void testLongListToIntegerLinkedList() throws Exception {
        List<Long> value = Arrays.asList(1L);
        LinkedList<Integer> ret = meta.longListToIntegerLinkedList(value);
        assertEquals(1, ret.size());
        assertEquals(Integer.valueOf(1), ret.iterator().next());
        assertNull(meta.longListToIntegerLinkedList(null));
    }

    /**
     * @throws Exception
     */
    public void testLongListToIntegerLinkedHashSet() throws Exception {
        List<Long> value = Arrays.asList(1L);
        LinkedHashSet<Integer> ret = meta.longListToIntegerLinkedHashSet(value);
        assertEquals(1, ret.size());
        assertEquals(Integer.valueOf(1), ret.iterator().next());
        assertNull(meta.longListToIntegerLinkedHashSet(null));
    }

    /**
     * @throws Exception
     */
    public void testLongListToIntegerStack() throws Exception {
        List<Long> value = Arrays.asList(1L);
        Stack<Integer> ret = meta.longListToIntegerStack(value);
        assertEquals(1, ret.size());
        assertEquals(Integer.valueOf(1), ret.iterator().next());
        assertNull(meta.longListToIntegerStack(null));
    }

    /**
     * @throws Exception
     */
    public void testLongListToIntegerVector() throws Exception {
        List<Long> value = Arrays.asList(1L);
        Vector<Integer> ret = meta.longListToIntegerVector(value);
        assertEquals(1, ret.size());
        assertEquals(Integer.valueOf(1), ret.iterator().next());
        assertNull(meta.longListToIntegerVector(null));
    }

    /**
     * @throws Exception
     */
    public void testLongListToPrimitiveLongArray() throws Exception {
        List<Long> longList = Arrays.asList(1L);
        long[] ret = meta.longListToPrimitiveLongArray(longList);
        assertEquals(1, ret.length);
        assertEquals(1, ret[0]);
        assertNull(meta.longListToPrimitiveLongArray(null));
    }

    /**
     * @throws Exception
     */
    public void testPrimitiveLongArrayToLongList() throws Exception {
        long[] value = new long[] { 1 };
        List<Long> ret = meta.primitiveLongArrayToLongList(value);
        assertEquals(1, ret.size());
        assertEquals(Long.valueOf(1), ret.get(0));
        assertNull(meta.primitiveLongArrayToLongList(null));
    }

    /**
     * @throws Exception
     */
    public void testLongListToLongArray() throws Exception {
        List<Long> value = Arrays.asList(1L);
        Long[] ret = meta.longListToLongArray(value);
        assertEquals(1, ret.length);
        assertEquals(Long.valueOf(1), ret[0]);
        assertNull(meta.longListToLongArray(null));
    }

    /**
     * @throws Exception
     */
    public void testLongArrayToLongList() throws Exception {
        Long[] value = new Long[] { 1L };
        List<Long> ret = meta.longArrayToLongList(value);
        assertEquals(1, ret.size());
        assertEquals(Long.valueOf(1), ret.get(0));
        assertNull(meta.longArrayToLongList(null));
    }

    /**
     * @throws Exception
     */
    public void testLongListToLongSet() throws Exception {
        List<Long> value = Arrays.asList(1L);
        Set<Long> ret = meta.longListToLongSet(value);
        assertEquals(1, ret.size());
        assertEquals(Long.valueOf(1), ret.iterator().next());
        assertNull(meta.longListToLongSet(null));
    }

    /**
     * @throws Exception
     */
    public void testLongListToLongSortedSet() throws Exception {
        List<Long> value = Arrays.asList(1L);
        SortedSet<Long> ret = meta.longListToLongSortedSet(value);
        assertEquals(1, ret.size());
        assertEquals(Long.valueOf(1), ret.iterator().next());
        assertNull(meta.longListToLongSortedSet(null));
    }

    /**
     * @throws Exception
     */
    public void testLongListToLongLinkedList() throws Exception {
        List<Long> value = Arrays.asList(1L);
        LinkedList<Long> ret = meta.longListToLongLinkedList(value);
        assertEquals(1, ret.size());
        assertEquals(Long.valueOf(1), ret.iterator().next());
        assertNull(meta.longListToLongLinkedList(null));
    }

    /**
     * @throws Exception
     */
    public void testLongListToLongLinkedHashSet() throws Exception {
        List<Long> value = Arrays.asList(1L);
        LinkedHashSet<Long> ret = meta.longListToLongLinkedHashSet(value);
        assertEquals(1, ret.size());
        assertEquals(Long.valueOf(1), ret.iterator().next());
        assertNull(meta.longListToLongLinkedHashSet(null));
    }

    /**
     * @throws Exception
     */
    public void testLongListToLongStack() throws Exception {
        List<Long> value = Arrays.asList(1L);
        Stack<Long> ret = meta.longListToLongStack(value);
        assertEquals(1, ret.size());
        assertEquals(Long.valueOf(1), ret.iterator().next());
        assertNull(meta.longListToLongStack(null));
    }

    /**
     * @throws Exception
     */
    public void testLongListToLongVector() throws Exception {
        List<Long> value = Arrays.asList(1L);
        Vector<Long> ret = meta.longListToLongVector(value);
        assertEquals(1, ret.size());
        assertEquals(Long.valueOf(1), ret.iterator().next());
        assertNull(meta.longListToLongVector(null));
    }

    /**
     * @throws Exception
     */
    public void testDoubleListToPrimitiveFloatArray() throws Exception {
        List<Double> list = Arrays.asList(1d);
        float[] ret = meta.doubleListToPrimitiveFloatArray(list);
        assertEquals(1, ret.length);
        assertEquals(1f, ret[0]);
        assertNull(meta.doubleListToPrimitiveFloatArray(null));
    }

    /**
     * @throws Exception
     */
    public void testPrimitiveFloatArrayToDoubleList() throws Exception {
        float[] value = new float[] { 1 };
        List<Double> ret = meta.primitiveFloatArrayToDoubleList(value);
        assertEquals(1, ret.size());
        assertEquals(Double.valueOf(1), ret.get(0));
        assertNull(meta.primitiveFloatArrayToDoubleList(null));
    }

    /**
     * @throws Exception
     */
    public void testDoubleListToFloatArray() throws Exception {
        List<Double> value = Arrays.asList(1d);
        Float[] ret = meta.doubleListToFloatArray(value);
        assertEquals(1, ret.length);
        assertEquals(Float.valueOf(1), ret[0]);
        assertNull(meta.doubleListToFloatArray(null));
    }

    /**
     * @throws Exception
     */
    public void testFloatArrayToDoubleList() throws Exception {
        Float[] value = new Float[] { 1f };
        List<Double> ret = meta.floatArrayToDoubleList(value);
        assertEquals(1, ret.size());
        assertEquals(Double.valueOf(1), ret.get(0));
        assertNull(meta.floatArrayToDoubleList(null));
    }

    /**
     * @throws Exception
     */
    public void testCopyDoubleListToFloatCollection() throws Exception {
        List<Double> value = Arrays.asList(1d);
        Collection<Float> collection = new ArrayList<Float>();
        meta.copyDoubleListToFloatCollection(value, collection);
        assertEquals(1, collection.size());
        assertEquals(Float.valueOf(1), collection.iterator().next());
    }

    /**
     * @throws Exception
     */
    public void testDoubleListToFloatList() throws Exception {
        List<Double> value = Arrays.asList(1d);
        List<Float> ret = meta.doubleListToFloatList(value);
        assertEquals(1, ret.size());
        assertEquals(Float.valueOf(1), ret.get(0));
        assertNull(meta.doubleListToFloatList(null));
    }

    /**
     * @throws Exception
     */
    public void testDoubleListToFloatSet() throws Exception {
        List<Double> value = Arrays.asList(1d);
        Set<Float> ret = meta.doubleListToFloatSet(value);
        assertEquals(1, ret.size());
        assertEquals(Float.valueOf(1), ret.iterator().next());
        assertNull(meta.doubleListToFloatSet(null));
    }

    /**
     * @throws Exception
     */
    public void testDoubleListToFloatSortedSet() throws Exception {
        List<Double> value = Arrays.asList(1d);
        SortedSet<Float> ret = meta.doubleListToFloatSortedSet(value);
        assertEquals(1, ret.size());
        assertEquals(Float.valueOf(1), ret.iterator().next());
        assertNull(meta.doubleListToFloatSortedSet(null));
    }

    /**
     * @throws Exception
     */
    public void testDoubleListToFloatLinkedList() throws Exception {
        List<Double> value = Arrays.asList(1d);
        LinkedList<Float> ret = meta.doubleListToFloatLinkedList(value);
        assertEquals(1, ret.size());
        assertEquals(Float.valueOf(1), ret.iterator().next());
        assertNull(meta.doubleListToFloatLinkedList(null));
    }

    /**
     * @throws Exception
     */
    public void testDoubleListToFloatLinkedHashSet() throws Exception {
        List<Double> value = Arrays.asList(1d);
        LinkedHashSet<Float> ret = meta.doubleListToFloatLinkedHashSet(value);
        assertEquals(1, ret.size());
        assertEquals(Float.valueOf(1), ret.iterator().next());
        assertNull(meta.doubleListToFloatLinkedHashSet(null));
    }

    /**
     * @throws Exception
     */
    public void testDoubleListToFLoatStack() throws Exception {
        List<Double> value = Arrays.asList(1d);
        Stack<Float> ret = meta.doubleListToFloatStack(value);
        assertEquals(1, ret.size());
        assertEquals(Float.valueOf(1), ret.iterator().next());
        assertNull(meta.doubleListToFloatStack(null));
    }

    /**
     * @throws Exception
     */
    public void testDoubleListToFloatVector() throws Exception {
        List<Double> value = Arrays.asList(1d);
        Vector<Float> ret = meta.doubleListToFloatVector(value);
        assertEquals(1, ret.size());
        assertEquals(Float.valueOf(1), ret.iterator().next());
        assertNull(meta.doubleListToFloatVector(null));
    }
}