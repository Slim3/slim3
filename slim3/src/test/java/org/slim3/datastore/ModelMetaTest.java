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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.junit.Test;
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;
import org.slim3.datastore.model.MySerializable;
import org.slim3.util.BeanDesc;
import org.slim3.util.ByteUtil;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.ShortBlob;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * @author higa
 * 
 */
public class ModelMetaTest {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     */
    @Test
    public void constructor() throws Exception {
        assertThat(meta.getModelClass(), equalTo(Hoge.class));
    }

    /**
     * @throws Exception
     */
    @Test
    public void longToPrimitiveShort() throws Exception {
        assertThat(meta.longToPrimitiveShort(1L), is((short) 1));
        assertThat(meta.longToPrimitiveShort(null), is((short) 0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void longToShort() throws Exception {
        assertThat(meta.longToShort(1L), is((short) 1));
        assertThat(meta.longToShort(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void longToPrimitiveInt() throws Exception {
        assertThat(meta.longToPrimitiveInt(1L), is(1));
        assertThat(meta.longToPrimitiveInt(null), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void longToInteger() throws Exception {
        assertThat(meta.longToInteger(1L), is(1));
        assertThat(meta.longToInteger(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void longToPrimitiveLong() throws Exception {
        assertThat(meta.longToPrimitiveLong(1L), is(1L));
        assertThat(meta.longToPrimitiveLong(null), is(0L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void doubleToPrimitiveFloat() throws Exception {
        assertThat(meta.doubleToPrimitiveFloat(1d), is(1f));
        assertThat(meta.doubleToPrimitiveFloat(null), is(0f));
    }

    /**
     * @throws Exception
     */
    @Test
    public void doubleToFloat() throws Exception {
        assertThat(meta.doubleToFloat(1d), is(1f));
        assertThat(meta.doubleToFloat(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void doubleToPrimitiveDouble() throws Exception {
        assertThat(meta.doubleToPrimitiveDouble(1d), is(1d));
        assertThat(meta.doubleToPrimitiveDouble(null), is(0d));
    }

    /**
     * @throws Exception
     */
    @Test
    public void booleanToPrimitiveBoolean() throws Exception {
        assertThat(meta.booleanToPrimitiveBoolean(true), is(true));
        assertThat(meta.booleanToPrimitiveBoolean(null), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void enumToString() throws Exception {
        assertThat(meta.enumToString(SortDirection.ASCENDING), is("ASCENDING"));
        assertThat(meta.enumToString(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void stringToEnum() throws Exception {
        assertThat(
            meta.stringToEnum(SortDirection.class, "ASCENDING"),
            is(SortDirection.ASCENDING));
        assertThat(
            meta.stringToEnum(SortDirection.class, null),
            is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void textToString() throws Exception {
        assertThat(meta.textToString(new Text("aaa")), is("aaa"));
        assertThat(meta.textToString(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void stringToText() throws Exception {
        assertThat(meta.stringToText("aaa"), is(new Text("aaa")));
        assertThat(meta.stringToText(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void shortBlobToBytes() throws Exception {
        byte[] bytes = new byte[] { 1 };
        assertThat(meta.shortBlobToBytes(new ShortBlob(bytes)), is(bytes));
        assertThat(meta.shortBlobToBytes(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void bytesToShortBlob() throws Exception {
        byte[] bytes = new byte[] { 1 };
        assertThat(meta.bytesToShortBlob(bytes).getBytes(), is(bytes));
        assertThat(meta.bytesToShortBlob(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void blobToBytes() throws Exception {
        byte[] bytes = new byte[] { 1 };
        assertThat(meta.blobToBytes(new Blob(bytes)), is(bytes));
        assertThat(meta.blobToBytes(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void bytesToBlob() throws Exception {
        byte[] bytes = new byte[] { 1 };
        assertThat(meta.bytesToBlob(bytes).getBytes(), is(bytes));
        assertThat(meta.bytesToBlob(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void shortBlobToSerializable() throws Exception {
        assertThat((String) meta.shortBlobToSerializable(new ShortBlob(ByteUtil
            .toByteArray("aaa"))), is("aaa"));
        assertThat(meta.shortBlobToSerializable(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void serializableToShortBlob() throws Exception {
        MySerializable serializable = new MySerializable("aaa");
        MySerializable serializable2 =
            (MySerializable) ByteUtil.toObject(meta.serializableToShortBlob(
                serializable).getBytes());
        assertThat(serializable2.getAaa(), is("aaa"));
        assertThat(meta.serializableToShortBlob(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void blobToSerializable() throws Exception {
        assertThat((String) meta
            .blobToSerializable(new com.google.appengine.api.datastore.Blob(
                ByteUtil.toByteArray("aaa"))), is("aaa"));
        assertThat(meta.blobToSerializable(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void serializableToBlob() throws Exception {
        MySerializable serializable = new MySerializable("aaa");
        MySerializable serializable2 =
            (MySerializable) ByteUtil.toObject(meta.serializableToBlob(
                serializable).getBytes());
        assertThat(serializable2.getAaa(), is("aaa"));
        assertThat(meta.serializableToShortBlob(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toList() throws Exception {
        List<Long> value = new ArrayList<Long>(Arrays.asList(1L));
        assertThat(meta.toList(Long.class, value), is(sameInstance(value)));
        assertThat(meta.toList(Long.class, null).size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toSet() throws Exception {
        List<Long> value = new ArrayList<Long>(Arrays.asList(1L));
        HashSet<Long> ret = meta.toSet(Long.class, value);
        assertThat(ret.size(), is(1));
        assertThat(ret.iterator().next(), is(1L));
        assertThat(meta.toSet(Long.class, null).size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toSortedSet() throws Exception {
        List<Long> value = new ArrayList<Long>(Arrays.asList(1L));
        SortedSet<Long> ret = meta.toSortedSet(Long.class, value);
        assertThat(ret.size(), is(1));
        assertThat(ret.iterator().next(), is(1L));
        assertThat(meta.toSortedSet(Long.class, null).size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyLongListToShortCollection() throws Exception {
        List<Long> value = Arrays.asList(1L);
        Collection<Short> collection = new ArrayList<Short>();
        meta.copyLongListToShortCollection(value, collection);
        assertThat(collection.size(), is(1));
        assertThat(collection.iterator().next(), is((short) 1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void longListToShortList() throws Exception {
        List<Long> value = Arrays.asList(1L);
        assertThat(
            meta.longListToShortList(value),
            is(Arrays.asList((short) 1)));
        assertThat(meta.longListToShortList(null).size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void longListToShortSet() throws Exception {
        List<Long> value = Arrays.asList(1L);
        Set<Short> ret = meta.longListToShortSet(value);
        assertThat(ret.size(), is(1));
        assertThat(ret.iterator().next(), is((short) 1));
        assertThat(meta.longListToShortSet(null).size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void longListToShortSortedSet() throws Exception {
        List<Long> value = Arrays.asList(1L);
        SortedSet<Short> ret = meta.longListToShortSortedSet(value);
        assertThat(ret.size(), is(1));
        assertThat(ret.iterator().next(), is((short) 1));
        assertThat(meta.longListToShortSortedSet(null).size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyLongListToIntegerCollection() throws Exception {
        List<Long> value = Arrays.asList(1L);
        Collection<Integer> collection = new ArrayList<Integer>();
        meta.copyLongListToIntegerCollection(value, collection);
        assertThat(collection.size(), is(1));
        assertThat(collection.iterator().next(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void longListToIntegerList() throws Exception {
        List<Long> value = Arrays.asList(1L);
        List<Integer> ret = meta.longListToIntegerList(value);
        assertThat(ret.size(), is(1));
        assertThat(ret.get(0), is(1));
        assertThat(meta.longListToIntegerList(null).size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void longListToIntegerSet() throws Exception {
        List<Long> value = Arrays.asList(1L);
        Set<Integer> ret = meta.longListToIntegerSet(value);
        assertThat(ret.size(), is(1));
        assertThat(ret.iterator().next(), is(1));
        assertThat(meta.longListToIntegerSet(null).size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void longListToIntegerSortedSet() throws Exception {
        List<Long> value = Arrays.asList(1L);
        SortedSet<Integer> ret = meta.longListToIntegerSortedSet(value);
        assertThat(ret.size(), is(1));
        assertThat(ret.iterator().next(), is(1));
        assertThat(meta.longListToIntegerSortedSet(null).size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void copyDoubleListToFloatCollection() throws Exception {
        List<Double> value = Arrays.asList(1d);
        Collection<Float> collection = new ArrayList<Float>();
        meta.copyDoubleListToFloatCollection(value, collection);
        assertThat(collection.size(), is(1));
        assertThat(collection.iterator().next(), is(1f));
    }

    /**
     * @throws Exception
     */
    @Test
    public void doubleListToFloatList() throws Exception {
        List<Double> value = Arrays.asList(1d);
        List<Float> ret = meta.doubleListToFloatList(value);
        assertThat(ret.size(), is(1));
        assertThat(ret.get(0), is(1f));
        assertThat(meta.doubleListToFloatList(null).size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void doubleListToFloatSet() throws Exception {
        List<Double> value = Arrays.asList(1d);
        Set<Float> ret = meta.doubleListToFloatSet(value);
        assertThat(ret.size(), is(1));
        assertThat(ret.iterator().next(), is(1f));
        assertThat(meta.doubleListToFloatSet(null).size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void doubleListToFloatSortedSet() throws Exception {
        List<Double> value = Arrays.asList(1d);
        SortedSet<Float> ret = meta.doubleListToFloatSortedSet(value);
        assertThat(ret.size(), is(1));
        assertThat(ret.iterator().next(), is(1f));
        assertThat(meta.doubleListToFloatSortedSet(null).size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void enumListToStringList() throws Exception {
        List<SortDirection> value = Arrays.asList(SortDirection.ASCENDING);
        List<String> ret = meta.enumListToStringList(value);
        assertThat(ret.size(), is(1));
        assertThat(ret.iterator().next(), is("ASCENDING"));
        assertThat(meta.enumListToStringList(null).size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void stringListToEnumList() throws Exception {
        List<String> value = Arrays.asList("ASCENDING");
        List<SortDirection> ret =
            meta.stringListToEnumList(SortDirection.class, value);
        assertThat(ret.size(), is(1));
        assertThat(ret.iterator().next(), is(SortDirection.ASCENDING));
        assertThat(
            meta.stringListToEnumList(SortDirection.class, null).size(),
            is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getBeanDesc() throws Exception {
        BeanDesc beanDesc = meta.getBeanDesc();
        assertThat(beanDesc, is(not(nullValue())));
        assertThat(beanDesc, is(sameInstance(meta.getBeanDesc())));
    }
}