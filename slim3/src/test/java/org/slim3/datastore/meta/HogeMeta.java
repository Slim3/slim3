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

import org.slim3.datastore.AttributeMeta;
import org.slim3.datastore.ModelMeta;
import org.slim3.datastore.model.Hoge;
import org.slim3.datastore.model.MySerializable;
import org.slim3.util.ByteUtil;

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
    public AttributeMeta<Short> myShort =
        new AttributeMeta<Short>(this, "myShort", short.class);

    /**
     * 
     */
    public AttributeMeta<Short> myShortWrapper =
        new AttributeMeta<Short>(this, "myShortWrapper", Short.class);

    /**
     * 
     */
    public AttributeMeta<Integer> myInt =
        new AttributeMeta<Integer>(this, "myInt", int.class);

    /**
     * 
     */
    public AttributeMeta<Integer> myIntWrapper =
        new AttributeMeta<Integer>(this, "myIntWrapper", Integer.class);

    /**
     * 
     */
    public AttributeMeta<Long> myLong =
        new AttributeMeta<Long>(this, "myLong", long.class);

    /**
     * 
     */
    public AttributeMeta<Long> myLongWrapper =
        new AttributeMeta<Long>(this, "myLongWrapper", Long.class);

    /**
     * 
     */
    public AttributeMeta<Float> myFloat =
        new AttributeMeta<Float>(this, "myFloat", float.class);

    /**
     * 
     */
    public AttributeMeta<Float> myFloatWrapper =
        new AttributeMeta<Float>(this, "myFloatWrapper", Float.class);

    /**
     * 
     */
    public AttributeMeta<Double> myDouble =
        new AttributeMeta<Double>(this, "myDouble", double.class);

    /**
     * 
     */
    public AttributeMeta<Double> myDoubleWrapper =
        new AttributeMeta<Double>(this, "myDoubleWrapper", Double.class);

    /**
     * 
     */
    public AttributeMeta<String> myString =
        new AttributeMeta<String>(this, "myString", String.class);

    /**
     * 
     */
    public AttributeMeta<Boolean> myBoolean =
        new AttributeMeta<Boolean>(this, "myBoolean", boolean.class);

    /**
     * 
     */
    public AttributeMeta<Boolean> myBooleanWrapper =
        new AttributeMeta<Boolean>(this, "myBooleanWrapper", Boolean.class);

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
    public AttributeMeta<byte[]> myBytes =
        new AttributeMeta<byte[]>(this, "myBytes", byte[].class);

    /**
     * 
     */
    public AttributeMeta<byte[]> myBytesBlob =
        new AttributeMeta<byte[]>(this, "myBytesBlob", byte[].class);

    /**
     * 
     */
    public AttributeMeta<MySerializable> mySerializable =
        new AttributeMeta<MySerializable>(
            this,
            "mySerializable",
            MySerializable.class);

    /**
     * 
     */
    public AttributeMeta<MySerializable> mySerializableBlob =
        new AttributeMeta<MySerializable>(
            this,
            "mySerializableBlob",
            MySerializable.class);

    /**
     * 
     */
    public AttributeMeta<Blob> myBlob =
        new AttributeMeta<Blob>(this, "myBlob", Blob.class);

    /**
     * 
     */
    public AttributeMeta<ShortBlob> myShortBlob =
        new AttributeMeta<ShortBlob>(this, "myShortBlob", ShortBlob.class);

    /**
     * 
     */
    public AttributeMeta<BigDecimal> myBigDecimal =
        new AttributeMeta<BigDecimal>(this, "myBigDecimal", BigDecimal.class);

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

    @Override
    public Hoge entityToModel(Entity entity) {
        Hoge model = new Hoge();
        model.setKey(entity.getKey());
        Long myShort = (Long) entity.getProperty("myShort");
        if (myShort != null) {
            model.setMyShort(myShort.shortValue());
        }
        Long myShortWrapper = (Long) entity.getProperty("myShortWrapper");
        if (myShortWrapper != null) {
            model.setMyShortWrapper(myShortWrapper.shortValue());
        } else {
            model.setMyShortWrapper(null);
        }
        Long myInt = (Long) entity.getProperty("myInt");
        if (myInt != null) {
            model.setMyInt(myInt.intValue());
        }
        Long myIntWrapper = (Long) entity.getProperty("myIntWrapper");
        if (myIntWrapper != null) {
            model.setMyIntWrapper(myIntWrapper.intValue());
        } else {
            model.setMyIntWrapper(null);
        }
        Long myLong = (Long) entity.getProperty("myLong");
        if (myLong != null) {
            model.setMyLong(myLong);
        }
        Long myLongWrapper = (Long) entity.getProperty("myLongWrapper");
        model.setMyLongWrapper(myLongWrapper);
        Double myFloat = (Double) entity.getProperty("myFloat");
        if (myFloat != null) {
            model.setMyFloat(myFloat.floatValue());
        }
        Double myFloatWrapper = (Double) entity.getProperty("myFloatWrapper");
        if (myFloatWrapper != null) {
            model.setMyFloatWrapper(myFloatWrapper.floatValue());
        } else {
            model.setMyFloatWrapper(null);
        }
        Double myDouble = (Double) entity.getProperty("myDouble");
        if (myDouble != null) {
            model.setMyDouble(myDouble);
        }
        Double myDoubleWrapper = (Double) entity.getProperty("myDoubleWrapper");
        if (myDoubleWrapper != null) {
            model.setMyDoubleWrapper(myDoubleWrapper);
        } else {
            model.setMyDoubleWrapper(null);
        }
        String myString = (String) entity.getProperty("myString");
        model.setMyString(myString);
        Boolean myBoolean = (Boolean) entity.getProperty("myBoolean");
        if (myBoolean != null) {
            model.setMyBoolean(myBoolean);
        }
        Boolean myBooleanWrapper =
            (Boolean) entity.getProperty("myBooleanWrapper");
        model.setMyBooleanWrapper(myBooleanWrapper);
        Date myDate = (Date) entity.getProperty("myDate");
        model.setMyDate(myDate);
        Text myStringText = (Text) entity.getProperty("myStringText");
        if (myStringText != null) {
            model.setMyStringText(myStringText.getValue());
        } else {
            model.setMyStringText(null);
        }
        Text myText = (Text) entity.getProperty("myText");
        model.setMyText(myText);
        ShortBlob myBytes = (ShortBlob) entity.getProperty("myBytes");
        if (myBytes != null) {
            model.setMyBytes(myBytes.getBytes());
        } else {
            model.setMyBytes(null);
        }
        Blob myBytesBlob = (Blob) entity.getProperty("myBytesBlob");
        if (myBytesBlob != null) {
            model.setMyBytesBlob(myBytesBlob.getBytes());
        } else {
            model.setMyBytesBlob(null);
        }
        ShortBlob mySerializable =
            (ShortBlob) entity.getProperty("mySerializable");
        if (mySerializable != null) {
            model.setMySerializable((MySerializable) ByteUtil
                .toObject(mySerializable.getBytes()));
        } else {
            model.setMySerializable(null);
        }
        Blob mySerializableBlob =
            (Blob) entity.getProperty("mySerializableBlob");
        if (mySerializableBlob != null) {
            model.setMySerializableBlob((MySerializable) ByteUtil
                .toObject(mySerializableBlob.getBytes()));
        } else {
            model.setMySerializableBlob(null);
        }
        Blob myBlob = (Blob) entity.getProperty("myBlob");
        if (myBlob != null) {
            model.setMyBlob(myBlob);
        } else {
            model.setMyBlob(null);
        }
        ShortBlob myShortBlob = (ShortBlob) entity.getProperty("myShortBlob");
        if (myShortBlob != null) {
            model.setMyShortBlob(myShortBlob);
        } else {
            model.setMyShortBlob(null);
        }
        String myBigDecimal = (String) entity.getProperty("myBigDecimal");
        if (myBigDecimal != null) {
            model.setMyBigDecimal(new BigDecimal(myBigDecimal));
        } else {
            model.setMyBigDecimal(null);
        }
        return model;
    }
}