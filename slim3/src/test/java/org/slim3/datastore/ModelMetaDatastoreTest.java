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

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;
import org.slim3.datastore.model.MySerializable;
import org.slim3.tester.DatastoreTestCase;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * @author higa
 * 
 */
public class ModelMetaDatastoreTest extends DatastoreTestCase {

    private HogeMeta meta = new HogeMeta();

    private Hoge model = new Hoge();

    /**
     * @throws Exception
     */
    public void testPrimitiveShort() throws Exception {
        model.setMyPrimitiveShort((short) 1);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertEquals((short) 1, model2.getMyPrimitiveShort());
    }

    /**
     * @throws Exception
     */
    public void testShort() throws Exception {
        model.setMyShort((short) 1);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertEquals(Short.valueOf("1"), model2.getMyShort());
    }

    /**
     * @throws Exception
     */
    public void testPrimitiveInt() throws Exception {
        model.setMyPrimitiveInt(1);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertEquals(1, model2.getMyPrimitiveInt());
    }

    /**
     * @throws Exception
     */
    public void testInteger() throws Exception {
        model.setMyInteger(1);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertEquals(Integer.valueOf(1), model2.getMyInteger());
    }

    /**
     * @throws Exception
     */
    public void testPrimitiveLong() throws Exception {
        model.setMyPrimitiveLong(1);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertEquals(1L, model2.getMyPrimitiveLong());
    }

    /**
     * @throws Exception
     */
    public void testLong() throws Exception {
        model.setMyLong(1L);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertEquals(Long.valueOf(1), model2.getMyLong());
    }

    /**
     * @throws Exception
     */
    public void testPrimitiveFloat() throws Exception {
        model.setMyPrimitiveFloat(1);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertEquals(1.0f, model2.getMyPrimitiveFloat());
    }

    /**
     * @throws Exception
     */
    public void testFloat() throws Exception {
        model.setMyFloat(1f);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertEquals(Float.valueOf(1), model2.getMyFloat());
    }

    /**
     * @throws Exception
     */
    public void testPrimitiveDouble() throws Exception {
        model.setMyPrimitiveDouble(1);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertEquals(1.0d, model2.getMyPrimitiveDouble());
    }

    /**
     * @throws Exception
     */
    public void testDouble() throws Exception {
        model.setMyDouble(1d);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertEquals(Double.valueOf(1), model2.getMyDouble());
    }

    /**
     * @throws Exception
     */
    public void testPrimitiveBoolean() throws Exception {
        model.setMyPrimitiveBoolean(true);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertTrue(model2.isMyPrimitiveBoolean());
    }

    /**
     * @throws Exception
     */
    public void testBoolean() throws Exception {
        model.setMyBoolean(true);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertTrue(model2.getMyBoolean());
    }

    /**
     * @throws Exception
     */
    public void testString() throws Exception {
        model.setMyString("1");
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertEquals("1", model2.getMyString());
    }

    /**
     * @throws Exception
     */
    public void testDate() throws Exception {
        model.setMyDate(new Date(0));
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertEquals(new Date(0), model2.getMyDate());
    }

    /**
     * @throws Exception
     */
    public void testEnum() throws Exception {
        model.setMyEnum(SortDirection.ASCENDING);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertEquals(SortDirection.ASCENDING, model2.getMyEnum());
    }

    /**
     * @throws Exception
     */
    public void testKey() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        model.setKey(key);
        Entity entity = meta.modelToEntity(model);
        ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertEquals(key, model2.getKey());
    }

    /**
     * @throws Exception
     */
    public void testStringText() throws Exception {
        model.setMyStringText("aaa");
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertEquals("aaa", model2.getMyStringText());
    }

    /**
     * @throws Exception
     */
    public void testText() throws Exception {
        model.setMyText(new Text("aaa"));
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertEquals(new Text("aaa"), model2.getMyText());
    }

    /**
     * @throws Exception
     */
    public void testBytes() throws Exception {
        model.setMyBytes(new byte[] { 1 });
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        byte[] myBytes2 = model2.getMyBytes();
        assertEquals(1, myBytes2.length);
        assertEquals(1, myBytes2[0]);
    }

    /**
     * @throws Exception
     */
    public void testBytesBlob() throws Exception {
        model.setMyBytesBlob(new byte[] { 1 });
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        byte[] myBytesBlob2 = model2.getMyBytesBlob();
        assertEquals(1, myBytesBlob2.length);
        assertEquals(1, myBytesBlob2[0]);
    }

    /**
     * @throws Exception
     */
    public void testSerializable() throws Exception {
        model.setMySerializable(new MySerializable("aaa"));
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertEquals(new MySerializable("aaa"), model2.getMySerializable());
    }

    /**
     * @throws Exception
     */
    public void testSerializableBlob() throws Exception {
        model.setMySerializableBlob(new MySerializable("aaa"));
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertEquals(new MySerializable("aaa"), model2.getMySerializableBlob());
    }

    /**
     * @throws Exception
     */
    public void testShortList() throws Exception {
        model.setMyShortList(Arrays.asList((short) 1));
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        List<Short> myShortList2 = model2.getMyShortList();
        assertEquals(1, myShortList2.size());
        assertEquals(Short.valueOf("1"), myShortList2.get(0));
    }

    /**
     * @throws Exception
     */
    public void testShortSet() throws Exception {
        model.setMyShortSet(new HashSet<Short>(Arrays.asList((short) 1)));
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        Set<Short> myShortSet2 = model2.getMyShortSet();
        assertEquals(1, myShortSet2.size());
        assertEquals(Short.valueOf("1"), myShortSet2.iterator().next());
    }

    /**
     * @throws Exception
     */
    public void testShortSortedSet() throws Exception {
        model.setMyShortSortedSet(new TreeSet<Short>(Arrays.asList((short) 1)));
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        SortedSet<Short> myShortSortedSet2 = model2.getMyShortSortedSet();
        assertEquals(1, myShortSortedSet2.size());
        assertEquals(Short.valueOf("1"), myShortSortedSet2.iterator().next());
    }

    /**
     * @throws Exception
     */
    public void testIntegerList() throws Exception {
        model.setMyIntegerList(Arrays.asList(1));
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        List<Integer> myIntegerList2 = model2.getMyIntegerList();
        assertEquals(1, myIntegerList2.size());
        assertEquals(Integer.valueOf("1"), myIntegerList2.get(0));
    }

    /**
     * @throws Exception
     */
    public void testIntegerSet() throws Exception {
        model.setMyIntegerSet(new HashSet<Integer>(Arrays.asList(1)));
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        Set<Integer> myIntegerSet2 = model2.getMyIntegerSet();
        assertEquals(1, myIntegerSet2.size());
        assertEquals(Integer.valueOf("1"), myIntegerSet2.iterator().next());
    }

    /**
     * @throws Exception
     */
    public void testIntegerSortedSet() throws Exception {
        model.setMyIntegerSortedSet(new TreeSet<Integer>(Arrays.asList(1)));
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        SortedSet<Integer> myIntegerSortedSet2 = model2.getMyIntegerSortedSet();
        assertEquals(1, myIntegerSortedSet2.size());
        assertEquals(Integer.valueOf("1"), myIntegerSortedSet2
            .iterator()
            .next());
    }

    /**
     * @throws Exception
     */
    public void testLongList() throws Exception {
        model.setMyLongList(Arrays.asList(1L));
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        List<Long> myLongList2 = model2.getMyLongList();
        assertEquals(1, myLongList2.size());
        assertEquals(Long.valueOf("1"), myLongList2.get(0));
    }

    /**
     * @throws Exception
     */
    public void testLongSet() throws Exception {
        model.setMyLongSet(new HashSet<Long>(Arrays.asList(1L)));
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        Set<Long> myLongSet2 = model2.getMyLongSet();
        assertEquals(1, myLongSet2.size());
        assertEquals(Long.valueOf("1"), myLongSet2.iterator().next());
    }

    /**
     * @throws Exception
     */
    public void testLongSortedSet() throws Exception {
        model.setMyLongSortedSet(new TreeSet<Long>(Arrays.asList(1L)));
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        SortedSet<Long> myLongSortedSet2 = model2.getMyLongSortedSet();
        assertEquals(1, myLongSortedSet2.size());
        assertEquals(Long.valueOf("1"), myLongSortedSet2.iterator().next());
    }

    /**
     * @throws Exception
     */
    public void testFloatList() throws Exception {
        model.setMyFloatList(Arrays.asList(1f));
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        List<Float> myFloatList2 = model2.getMyFloatList();
        assertEquals(1, myFloatList2.size());
        assertEquals(Float.valueOf("1"), myFloatList2.get(0));
    }

    /**
     * @throws Exception
     */
    public void testFloatSet() throws Exception {
        model.setMyFloatSet(new HashSet<Float>(Arrays.asList(1f)));
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        Set<Float> myFloatSet2 = model2.getMyFloatSet();
        assertEquals(1, myFloatSet2.size());
        assertEquals(Float.valueOf("1"), myFloatSet2.iterator().next());
    }

    /**
     * @throws Exception
     */
    public void testFloatSortedSet() throws Exception {
        model.setMyFloatSortedSet(new TreeSet<Float>(Arrays.asList(1f)));
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        SortedSet<Float> myFloatSortedSet2 = model2.getMyFloatSortedSet();
        assertEquals(1, myFloatSortedSet2.size());
        assertEquals(Float.valueOf("1"), myFloatSortedSet2.iterator().next());
    }
}
