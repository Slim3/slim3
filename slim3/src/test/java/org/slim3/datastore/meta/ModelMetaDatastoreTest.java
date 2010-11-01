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
package org.slim3.datastore.meta;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;
import org.slim3.datastore.model.Bbb;
import org.slim3.datastore.model.Hoge;
import org.slim3.datastore.model.MySerializable;
import org.slim3.tester.AppEngineTestCase;
import org.slim3.util.CipherFactory;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * @author higa
 * 
 */
public class ModelMetaDatastoreTest extends AppEngineTestCase {

    private HogeMeta meta = new HogeMeta();

    private Hoge model = new Hoge();

    private BbbMeta bbbMeta = new BbbMeta();

    private Bbb bbb = new Bbb();

    private DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        CipherFactory.getFactory().setGlobalKey("xxxxxxxxxxxxxxxx");
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        CipherFactory.getFactory().clearGlobalKey();
    }

    /**
     * @throws Exception
     */
    @Test
    public void primitiveShort() throws Exception {
        model.setMyPrimitiveShort((short) 1);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyPrimitiveShort(), is((short) 1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void shortWrapper() throws Exception {
        model.setMyShort((short) 1);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyShort(), is((short) 1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void primitiveInt() throws Exception {
        model.setMyPrimitiveInt(1);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyPrimitiveInt(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void integer() throws Exception {
        model.setMyInteger(1);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyInteger(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void primitiveLong() throws Exception {
        model.setMyPrimitiveLong(1);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyPrimitiveLong(), is(1L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void longWrapper() throws Exception {
        model.setMyLong(1L);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyLong(), is(1L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void primitiveFloat() throws Exception {
        model.setMyPrimitiveFloat(1);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyPrimitiveFloat(), is(1f));
    }

    /**
     * @throws Exception
     */
    @Test
    public void floatWrapper() throws Exception {
        model.setMyFloat(1f);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyFloat(), is(1f));
    }

    /**
     * @throws Exception
     */
    @Test
    public void primitiveDouble() throws Exception {
        model.setMyPrimitiveDouble(1);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyPrimitiveDouble(), is(1d));
    }

    /**
     * @throws Exception
     */
    @Test
    public void doubleWrapper() throws Exception {
        model.setMyDouble(1d);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyDouble(), is(1d));
    }

    /**
     * @throws Exception
     */
    @Test
    public void primitiveBoolean() throws Exception {
        model.setMyPrimitiveBoolean(true);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.isMyPrimitiveBoolean(), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void booleanWrapper() throws Exception {
        model.setMyBoolean(true);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyBoolean(), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void string() throws Exception {
        model.setMyString("1");
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyString(), is("1"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void date() throws Exception {
        model.setMyDate(new Date(0));
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyDate(), is(new Date(0)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void enumValue() throws Exception {
        model.setMyEnum(SortDirection.ASCENDING);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyEnum(), is(SortDirection.ASCENDING));
    }

    /**
     * @throws Exception
     */
    @Test
    public void key() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        model.setKey(key);
        Entity entity = meta.modelToEntity(model);
        ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getKey(), is(key));
    }

    /**
     * @throws Exception
     */
    @Test
    public void stringText() throws Exception {
        model.setMyStringText("aaa");
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyStringText(), is("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void text() throws Exception {
        model.setMyText(new Text("aaa"));
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyText(), is(new Text("aaa")));
    }

    /**
     * @throws Exception
     */
    @Test
    public void bytes() throws Exception {
        byte[] value = new byte[] { 1 };
        model.setMyBytes(value);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyBytes(), is(value));
    }

    /**
     * @throws Exception
     */
    @Test
    public void bytesBlob() throws Exception {
        byte[] value = new byte[] { 1 };
        model.setMyBytesBlob(value);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyBytesBlob(), is(value));
    }

    /**
     * @throws Exception
     */
    @Test
    public void serializable() throws Exception {
        model.setMySerializable(new MySerializable("aaa"));
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMySerializable(), is(new MySerializable("aaa")));
    }

    /**
     * @throws Exception
     */
    @Test
    public void serializableBlob() throws Exception {
        MySerializable value = new MySerializable("aaa");
        model.setMySerializableBlob(value);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMySerializableBlob(), is(value));
    }

    /**
     * @throws Exception
     */
    @Test
    public void shortList() throws Exception {
        List<Short> value = Arrays.asList((short) 1);
        model.setMyShortList(value);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyShortList(), is(value));
    }

    /**
     * @throws Exception
     */
    @Test
    public void shortSet() throws Exception {
        Set<Short> value = new HashSet<Short>(Arrays.asList((short) 1));
        model.setMyShortSet(value);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyShortSet(), is(value));
    }

    /**
     * @throws Exception
     */
    @Test
    public void shortSortedSet() throws Exception {
        SortedSet<Short> value = new TreeSet<Short>(Arrays.asList((short) 1));
        model.setMyShortSortedSet(value);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyShortSortedSet(), is(value));
    }

    /**
     * @throws Exception
     */
    @Test
    public void integerList() throws Exception {
        List<Integer> value = Arrays.asList(1);
        model.setMyIntegerList(value);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyIntegerList(), is(value));
    }

    /**
     * @throws Exception
     */
    @Test
    public void integerSet() throws Exception {
        Set<Integer> value = new HashSet<Integer>(Arrays.asList(1));
        model.setMyIntegerSet(value);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyIntegerSet(), is(value));
    }

    /**
     * @throws Exception
     */
    @Test
    public void integerSortedSet() throws Exception {
        SortedSet<Integer> value = new TreeSet<Integer>(Arrays.asList(1));
        model.setMyIntegerSortedSet(value);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyIntegerSortedSet(), is(value));
    }

    /**
     * @throws Exception
     */
    @Test
    public void longList() throws Exception {
        List<Long> value = Arrays.asList(1L);
        model.setMyLongList(value);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyLongList(), is(value));
    }

    /**
     * @throws Exception
     */
    @Test
    public void longSet() throws Exception {
        Set<Long> value = new HashSet<Long>(Arrays.asList(1L));
        model.setMyLongSet(value);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyLongSet(), is(value));
    }

    /**
     * @throws Exception
     */
    @Test
    public void longSortedSet() throws Exception {
        SortedSet<Long> value = new TreeSet<Long>(Arrays.asList(1L));
        model.setMyLongSortedSet(value);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyLongSortedSet(), is(value));
    }

    /**
     * @throws Exception
     */
    @Test
    public void floatList() throws Exception {
        List<Float> value = Arrays.asList(1f);
        model.setMyFloatList(value);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyFloatList(), is(value));
    }

    /**
     * @throws Exception
     */
    @Test
    public void floatSet() throws Exception {
        Set<Float> value = new HashSet<Float>(Arrays.asList(1f));
        model.setMyFloatSet(value);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyFloatSet(), is(value));
    }

    /**
     * @throws Exception
     */
    @Test
    public void floatSortedSet() throws Exception {
        SortedSet<Float> value = new TreeSet<Float>(Arrays.asList(1f));
        model.setMyFloatSortedSet(value);
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertThat(model2.getMyFloatSortedSet(), is(value));
    }

    /**
     * @throws Exception
     */
    @Test
    public void modelRef() throws Exception {
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        bbb.getHogeRef().setModel(model2);
        Entity bbbEntity = bbbMeta.modelToEntity(bbb);
        Key bbbKey = ds.put(bbbEntity);
        Entity bbbEntity2 = ds.get(bbbKey);
        Bbb bbb2 = bbbMeta.entityToModel(bbbEntity2);
        assertThat(bbb2.getHogeRef().getKey(), is(key));
    }
}
