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
package org.slim3.datastore.meta;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slim3.datastore.AttributeMeta;
import org.slim3.datastore.CollectionAttributeMeta;
import org.slim3.datastore.ModelMeta;
import org.slim3.datastore.model.Hoge;
import org.slim3.datastore.model.MySerializable;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.ShortBlob;
import com.google.appengine.api.datastore.Text;

/**
 * @author higa
 * 
 */
public class HogeMeta extends ModelMeta<Hoge> {

    /**
     * 
     */
    public AttributeMeta<Key> key =
        new AttributeMeta<Key>(this, "key", Key.class);

    /**
     * 
     */
    public AttributeMeta<Short> myPrimitiveShort =
        new AttributeMeta<Short>(this, "myPrimitiveShort", short.class);

    /**
     * 
     */
    public AttributeMeta<Short> myShort =
        new AttributeMeta<Short>(this, "myShort", Short.class);

    /**
     * 
     */
    public AttributeMeta<Integer> myPrimitiveInt =
        new AttributeMeta<Integer>(this, "myPrimitiveInt", int.class);

    /**
     * 
     */
    public AttributeMeta<Integer> myInteger =
        new AttributeMeta<Integer>(this, "myInteger", Integer.class);

    /**
     * 
     */
    public AttributeMeta<Long> myPrimitiveLong =
        new AttributeMeta<Long>(this, "myPrimitiveLong", long.class);

    /**
     * 
     */
    public AttributeMeta<Long> myLong =
        new AttributeMeta<Long>(this, "myLong", Long.class);

    /**
     * 
     */
    public AttributeMeta<Float> myPrimitiveFloat =
        new AttributeMeta<Float>(this, "myPrimitiveFloat", float.class);

    /**
     * 
     */
    public AttributeMeta<Float> myFloat =
        new AttributeMeta<Float>(this, "myFloat", Float.class);

    /**
     * 
     */
    public AttributeMeta<Double> myPrimitiveDouble =
        new AttributeMeta<Double>(this, "myPrimitiveDouble", double.class);

    /**
     * 
     */
    public AttributeMeta<Double> myDouble =
        new AttributeMeta<Double>(this, "myDouble", Double.class);

    /**
     * 
     */
    public AttributeMeta<String> myString =
        new AttributeMeta<String>(this, "myString", String.class);

    /**
     * 
     */
    public AttributeMeta<Boolean> myPrimitiveBoolean =
        new AttributeMeta<Boolean>(this, "myPrimitiveBoolean", boolean.class);

    /**
     * 
     */
    public AttributeMeta<Boolean> myBoolean =
        new AttributeMeta<Boolean>(this, "myBoolean", Boolean.class);

    /**
     * 
     */
    public AttributeMeta<Date> myDate =
        new AttributeMeta<Date>(this, "myDate", Date.class);

    /**
     * 
     */
    public AttributeMeta<String> myStringText =
        new AttributeMeta<String>(this, "myStringText", String.class);

    /**
     * 
     */
    public AttributeMeta<Text> myText =
        new AttributeMeta<Text>(this, "myText", Text.class);

    /**
     * 
     */
    public AttributeMeta<BigDecimal> myBigDecimal =
        new AttributeMeta<BigDecimal>(this, "myBigDecimal", BigDecimal.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Short> myShortArray =
        new CollectionAttributeMeta<Short>(this, "myShortArray", short[].class);

    /**
     * 
     */
    public AttributeMeta<Long> version =
        new AttributeMeta<Long>(this, "version", Long.class);

    /**
     * 
     */
    public HogeMeta() {
        super(Hoge.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Hoge entityToModel(Entity entity) {
        Hoge model = new Hoge();
        model.setKey(entity.getKey());
        model.setMyPrimitiveShort(longToPrimitiveShort((Long) entity
            .getProperty("myPrimitiveShort")));
        model.setMyShort(longToShort((Long) entity.getProperty("myShort")));
        model.setMyPrimitiveInt(longToPrimitiveInt((Long) entity
            .getProperty("myPrimitiveInt")));
        model
            .setMyInteger(longToInteger((Long) entity.getProperty("myInteger")));
        model.setMyPrimitiveLong(longToPrimitiveLong((Long) entity
            .getProperty("myPrimitiveLong")));
        model.setMyLong((Long) entity.getProperty("myLong"));
        model.setMyPrimitiveFloat(doubleToPrimitiveFloat((Double) entity
            .getProperty("myPrimitiveFloat")));
        model.setMyFloat(doubleToFloat((Double) entity.getProperty("myFloat")));
        model.setMyPrimitiveDouble(doubleToPrimitiveDouble((Double) entity
            .getProperty("myPrimitiveDouble")));
        model.setMyDouble((Double) entity.getProperty("myDouble"));
        model.setMyString((String) entity.getProperty("myString"));
        model.setMyPrimitiveBoolean(booleanToPrimitiveBoolean((Boolean) entity
            .getProperty("myPrimitiveBoolean")));
        model.setMyBoolean((Boolean) entity.getProperty("myBoolean"));
        model.setMyDate((Date) entity.getProperty("myDate"));
        model.setMyStringText(textToString((Text) entity
            .getProperty("myStringText")));
        model.setMyText((Text) entity.getProperty("myText"));
        model.setMyBytes(shortBlobToBytes((ShortBlob) entity
            .getProperty("myBytes")));
        model.setMyBytesBlob(blobToBytes((Blob) entity
            .getProperty("myBytesBlob")));
        model
            .setMySerializable((MySerializable) shortBlobToSerializable((ShortBlob) entity
                .getProperty("mySerializable")));
        model
            .setMySerializableBlob((MySerializable) blobToSerializable((Blob) entity
                .getProperty("mySerializableBlob")));
        model.setMyBlob((Blob) entity.getProperty("myBlob"));
        model.setMyShortBlob((ShortBlob) entity.getProperty("myShortBlob"));
        model.setMyBigDecimal(stringToBigDecimal((String) entity
            .getProperty("myBigDecimal")));
        List<Long> myShortArray =
            (List<Long>) entity.getProperty("myShortArray");
        model.setMyShortArray(toPrimitiveShortArray(myShortArray));
        return model;
    }

    @Override
    public Entity modelToEntity(Hoge model) {
        Entity entity = null;
        if (model.getKey() != null) {
            entity = new Entity(model.getKey());
        } else {
            entity = new Entity("Hoge");
        }
        entity.setProperty("myPrimitiveShort", model.getMyPrimitiveShort());
        entity.setProperty("myShort", model.getMyShort());
        entity.setProperty("myPrimitiveInt", model.getMyPrimitiveInt());
        entity.setProperty("myInteger", model.getMyInteger());
        entity.setProperty("myPrimitiveLong", model.getMyPrimitiveLong());
        entity.setProperty("myLong", model.getMyLong());
        entity.setProperty("myPrimitiveFloat", model.getMyPrimitiveFloat());
        entity.setProperty("myFloat", model.getMyFloat());
        entity.setProperty("myPrimitiveDouble", model.getMyPrimitiveDouble());
        entity.setProperty("myDouble", model.getMyDouble());
        entity.setProperty("myString", model.getMyString());
        entity.setProperty("myPrimitiveBoolean", model.isMyPrimitiveBoolean());
        entity.setProperty("myBoolean", model.getMyBoolean());
        entity.setProperty("myDate", model.getMyDate());
        entity.setUnindexedProperty("myStringText", stringToText(model
            .getMyStringText()));
        entity.setUnindexedProperty("myText", model.getMyText());
        entity.setUnindexedProperty("myBytes", bytesToShortBlob(model
            .getMyBytes()));
        entity.setUnindexedProperty("myBytesBlob", bytesToBlob(model
            .getMyBytesBlob()));
        entity.setUnindexedProperty(
            "mySerializable",
            serializableToShortBlob(model.getMySerializable()));
        entity.setUnindexedProperty(
            "mySerializableBlob",
            serializableToBlob(model.getMySerializableBlob()));
        entity.setProperty("myBigDecimal", bigDecimalToString(model
            .getMyBigDecimal()));
        return entity;
    }
}