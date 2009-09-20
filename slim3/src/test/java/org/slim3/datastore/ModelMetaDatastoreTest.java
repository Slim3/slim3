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
import java.util.Date;

import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;
import org.slim3.datastore.model.MySerializable;
import org.slim3.tester.DatastoreTestCase;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * @author higa
 * 
 */
public class ModelMetaDatastoreTest extends DatastoreTestCase {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     */
    public void testModelToEntityAndEntityToModel() throws Exception {
        Hoge model = new Hoge();
        model.setMyPrimitiveShort((short) 1);
        model.setMyShort((short) 1);
        model.setMyPrimitiveInt(1);
        model.setMyInteger(1);
        model.setMyPrimitiveLong(1);
        model.setMyLong(1L);
        model.setMyPrimitiveFloat(1);
        model.setMyFloat(1f);
        model.setMyPrimitiveDouble(1);
        model.setMyDouble(1d);
        model.setMyString("1");
        model.setMyPrimitiveBoolean(true);
        model.setMyBoolean(true);
        model.setMyDate(new Date(0));
        model.setMyStringText("aaa");
        model.setMyText(new com.google.appengine.api.datastore.Text("aaa"));
        model.setMyBytes(new byte[] { 1 });
        model.setMyBytesBlob(new byte[] { 1 });
        model.setMySerializable(new MySerializable("aaa"));
        model.setMySerializableBlob(new MySerializable("aaa"));
        model.setMyBigDecimal(new BigDecimal("1"));
        model.setMyPrimitiveShortArray(new short[] { 1 });
        model.setMyShortArray(new Short[] { 1 });
        Entity entity = meta.modelToEntity(model);
        Key key = ds.put(entity);
        Entity entity2 = ds.get(key);
        Hoge model2 = meta.entityToModel(entity2);
        assertEquals((short) 1, model2.getMyPrimitiveShort());
        assertEquals(Short.valueOf((short) 1), model2.getMyShort());
        assertEquals(1, model2.getMyPrimitiveInt());
        assertEquals(Integer.valueOf(1), model2.getMyInteger());
        assertEquals(1L, model2.getMyPrimitiveLong());
        assertEquals(Long.valueOf(1), model2.getMyLong());
        assertEquals(1.0f, model2.getMyPrimitiveFloat());
        assertEquals(Float.valueOf(1), model2.getMyFloat());
        assertEquals(1.0d, model2.getMyPrimitiveDouble());
        assertEquals(Double.valueOf(1), model2.getMyDouble());
        assertEquals("1", model2.getMyString());
        assertTrue(model2.isMyPrimitiveBoolean());
        assertTrue(model2.getMyBoolean());
        assertEquals(new Date(0), model2.getMyDate());
        assertEquals("aaa", model2.getMyStringText());
        assertEquals(new Text("aaa"), model2.getMyText());
        byte[] myBytes = model2.getMyBytes();
        assertEquals(1, myBytes.length);
        assertEquals(1, myBytes[0]);
        byte[] myBytesBlob = model2.getMyBytesBlob();
        assertEquals(1, myBytesBlob.length);
        assertEquals(1, myBytesBlob[0]);
        assertEquals(new MySerializable("aaa"), model2.getMySerializable());
        assertEquals(new MySerializable("aaa"), model2.getMySerializableBlob());
        assertEquals(new BigDecimal("1"), model2.getMyBigDecimal());
        short[] myPrimitiveShortArray = model2.getMyPrimitiveShortArray();
        assertEquals(1, myPrimitiveShortArray.length);
        assertEquals((short) 1, myPrimitiveShortArray[0]);
        Short[] myShortArray = model2.getMyShortArray();
        assertEquals(1, myShortArray.length);
        assertEquals(Short.valueOf("1"), myShortArray[0]);
    }
}
