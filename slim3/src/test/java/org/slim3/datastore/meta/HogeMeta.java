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

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.slim3.datastore.CollectionAttributeMeta;
import org.slim3.datastore.CoreAttributeMeta;
import org.slim3.datastore.ModelMeta;
import org.slim3.datastore.StringAttributeMeta;
import org.slim3.datastore.StringCollectionAttributeMeta;
import org.slim3.datastore.model.Hoge;
import org.slim3.datastore.model.MySerializable;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.ShortBlob;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * @author higa
 * 
 */
public class HogeMeta extends ModelMeta<Hoge> {

    /**
     * 
     */
    public CoreAttributeMeta<Hoge, Key> key =
        new CoreAttributeMeta<Hoge, Key>(this, "__key__", Key.class);

    /**
     * 
     */
    public CoreAttributeMeta<Hoge, Short> myPrimitiveShort =
        new CoreAttributeMeta<Hoge, Short>(
            this,
            "myPrimitiveShort",
            Short.class);

    /**
     * 
     */
    public CoreAttributeMeta<Hoge, Short> myShort =
        new CoreAttributeMeta<Hoge, Short>(this, "myShort", Short.class);

    /**
     * 
     */
    public CoreAttributeMeta<Hoge, Integer> myPrimitiveInt =
        new CoreAttributeMeta<Hoge, Integer>(this, "myPrimitiveInt", int.class);

    /**
     * 
     */
    public CoreAttributeMeta<Hoge, Integer> myInteger =
        new CoreAttributeMeta<Hoge, Integer>(this, "myInteger", Integer.class);

    /**
     * 
     */
    public CoreAttributeMeta<Hoge, Long> myPrimitiveLong =
        new CoreAttributeMeta<Hoge, Long>(this, "myPrimitiveLong", long.class);

    /**
     * 
     */
    public CoreAttributeMeta<Hoge, Long> myLong =
        new CoreAttributeMeta<Hoge, Long>(this, "myLong", Long.class);

    /**
     * 
     */
    public CoreAttributeMeta<Hoge, Float> myPrimitiveFloat =
        new CoreAttributeMeta<Hoge, Float>(
            this,
            "myPrimitiveFloat",
            float.class);

    /**
     * 
     */
    public CoreAttributeMeta<Hoge, Float> myFloat =
        new CoreAttributeMeta<Hoge, Float>(this, "myFloat", Float.class);

    /**
     * 
     */
    public CoreAttributeMeta<Hoge, Double> myPrimitiveDouble =
        new CoreAttributeMeta<Hoge, Double>(
            this,
            "myPrimitiveDouble",
            double.class);

    /**
     * 
     */
    public CoreAttributeMeta<Hoge, Double> myDouble =
        new CoreAttributeMeta<Hoge, Double>(this, "myDouble", Double.class);

    /**
     * 
     */
    public StringAttributeMeta<Hoge> myString =
        new StringAttributeMeta<Hoge>(this, "myString");

    /**
     * 
     */
    public CoreAttributeMeta<Hoge, Boolean> myPrimitiveBoolean =
        new CoreAttributeMeta<Hoge, Boolean>(
            this,
            "myPrimitiveBoolean",
            boolean.class);

    /**
     * 
     */
    public CoreAttributeMeta<Hoge, Boolean> myBoolean =
        new CoreAttributeMeta<Hoge, Boolean>(this, "myBoolean", Boolean.class);

    /**
     * 
     */
    public CoreAttributeMeta<Hoge, Date> myDate =
        new CoreAttributeMeta<Hoge, Date>(this, "myDate", Date.class);

    /**
     * 
     */
    public CoreAttributeMeta<Hoge, SortDirection> myEnum =
        new CoreAttributeMeta<Hoge, SortDirection>(
            this,
            "myEnum",
            SortDirection.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, List<Short>, Short> myShortList =
        new CollectionAttributeMeta<Hoge, List<Short>, Short>(
            this,
            "myShortList",
            List.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, Set<Short>, Short> myShortSet =
        new CollectionAttributeMeta<Hoge, Set<Short>, Short>(
            this,
            "myShortSet",
            Set.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, SortedSet<Short>, Short> myShortSortedSet =
        new CollectionAttributeMeta<Hoge, SortedSet<Short>, Short>(
            this,
            "myShortSortedSet",
            SortedSet.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, List<Integer>, Integer> myIntegerList =
        new CollectionAttributeMeta<Hoge, List<Integer>, Integer>(
            this,
            "myIntegerList",
            List.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, Set<Integer>, Integer> myIntegerSet =
        new CollectionAttributeMeta<Hoge, Set<Integer>, Integer>(
            this,
            "myIntegerSet",
            Set.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, SortedSet<Integer>, Integer> myIntegerSortedSet =
        new CollectionAttributeMeta<Hoge, SortedSet<Integer>, Integer>(
            this,
            "myIntegerSortedSet",
            SortedSet.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, List<Long>, Long> myLongList =
        new CollectionAttributeMeta<Hoge, List<Long>, Long>(
            this,
            "myLongList",
            List.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, Set<Long>, Long> myLongSet =
        new CollectionAttributeMeta<Hoge, Set<Long>, Long>(
            this,
            "myLongSet",
            Set.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, SortedSet<Long>, Long> myLongSortedSet =
        new CollectionAttributeMeta<Hoge, SortedSet<Long>, Long>(
            this,
            "myLongSortedSet",
            SortedSet.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, List<Float>, Float> myFloatList =
        new CollectionAttributeMeta<Hoge, List<Float>, Float>(
            this,
            "myFloatList",
            List.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, Set<Float>, Float> myFloatSet =
        new CollectionAttributeMeta<Hoge, Set<Float>, Float>(
            this,
            "myFloatSet",
            Set.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, SortedSet<Float>, Float> myFloatSortedSet =
        new CollectionAttributeMeta<Hoge, SortedSet<Float>, Float>(
            this,
            "myFloatSortedSet",
            SortedSet.class);

    /**
     * 
     */
    public CoreAttributeMeta<Hoge, Long> version =
        new CoreAttributeMeta<Hoge, Long>(this, "version", Long.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, List<SortDirection>, SortDirection> myEnumList =
        new CollectionAttributeMeta<Hoge, List<SortDirection>, SortDirection>(
            this,
            "myEnumList",
            List.class);

    /**
     * 
     */
    public StringCollectionAttributeMeta<Hoge, List<String>> myStringList =
        new StringCollectionAttributeMeta<Hoge, List<String>>(
            this,
            "myStringList",
            List.class);

    /**
     * 
     */
    public HogeMeta() {
        super("Hoge", Hoge.class);
    }

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
        model.setMyEnum(stringToEnum(SortDirection.class, (String) entity
            .getProperty("myEnum")));

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

        model.setMyShortList(longListToShortList(entity
            .getProperty("myShortList")));
        model
            .setMyShortSet(longListToShortSet(entity.getProperty("myShortSet")));
        model.setMyShortSortedSet(longListToShortSortedSet(entity
            .getProperty("myShortSortedSet")));

        model.setMyIntegerList(longListToIntegerList(entity
            .getProperty("myIntegerList")));
        model.setMyIntegerSet(longListToIntegerSet(entity
            .getProperty("myIntegerSet")));
        model.setMyIntegerSortedSet(longListToIntegerSortedSet(entity
            .getProperty("myIntegerSortedSet")));

        model
            .setMyLongList(toList(Long.class, entity.getProperty("myLongList")));
        model.setMyLongSet(toSet(Long.class, entity.getProperty("myLongSet")));
        model.setMyLongSortedSet(toSortedSet(Long.class, entity
            .getProperty("myLongSortedSet")));

        model.setMyFloatList(doubleListToFloatList(entity
            .getProperty("myFloatList")));
        model.setMyFloatSet(doubleListToFloatSet(entity
            .getProperty("myFloatSet")));
        model.setMyFloatSortedSet(doubleListToFloatSortedSet(entity
            .getProperty("myFloatSortedSet")));
        model.setMyEnumList(stringListToEnumList(SortDirection.class, entity
            .getProperty("myEnumList")));
        model.setMyStringList(toList(String.class, entity
            .getProperty("myStringList")));
        model.setVersion((Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public Entity modelToEntity(Object model) {
        Hoge m = (Hoge) model;
        Entity e = null;
        if (m.getKey() != null) {
            e = new Entity(m.getKey());
        } else {
            e = new Entity("Hoge");
        }
        e.setProperty("myPrimitiveShort", m.getMyPrimitiveShort());
        e.setProperty("myShort", m.getMyShort());
        e.setProperty("myPrimitiveInt", m.getMyPrimitiveInt());
        e.setProperty("myInteger", m.getMyInteger());
        e.setProperty("myPrimitiveLong", m.getMyPrimitiveLong());
        e.setProperty("myLong", m.getMyLong());
        e.setProperty("myPrimitiveFloat", m.getMyPrimitiveFloat());
        e.setProperty("myFloat", m.getMyFloat());
        e.setProperty("myPrimitiveDouble", m.getMyPrimitiveDouble());
        e.setProperty("myDouble", m.getMyDouble());
        e.setProperty("myString", m.getMyString());
        e.setProperty("myPrimitiveBoolean", m.isMyPrimitiveBoolean());
        e.setProperty("myBoolean", m.getMyBoolean());
        e.setProperty("myDate", m.getMyDate());
        e.setProperty("myEnum", enumToString(m.getMyEnum()));

        e.setUnindexedProperty(
            "myStringText",
            stringToText(m.getMyStringText()));
        e.setUnindexedProperty("myText", m.getMyText());
        e.setUnindexedProperty("myBytes", bytesToShortBlob(m.getMyBytes()));
        e.setUnindexedProperty("myBytesBlob", bytesToBlob(m.getMyBytesBlob()));
        e.setUnindexedProperty("mySerializable", serializableToShortBlob(m
            .getMySerializable()));
        e.setUnindexedProperty("mySerializableBlob", serializableToBlob(m
            .getMySerializableBlob()));

        e.setProperty("myShortList", m.getMyShortList());
        e.setProperty("myShortSet", m.getMyShortSet());
        e.setProperty("myShortSortedSet", m.getMyShortSortedSet());

        e.setProperty("myIntegerList", m.getMyIntegerList());
        e.setProperty("myIntegerSet", m.getMyIntegerSet());
        e.setProperty("myIntegerSortedSet", m.getMyIntegerSortedSet());

        e.setProperty("myLongList", m.getMyLongList());
        e.setProperty("myLongSet", m.getMyLongSet());
        e.setProperty("myLongSortedSet", m.getMyLongSortedSet());

        e.setProperty("myFloatList", m.getMyFloatList());
        e.setProperty("myFloatSet", m.getMyFloatSet());
        e.setProperty("myFloatSortedSet", m.getMyFloatSortedSet());

        e.setProperty("myEnumList", enumListToStringList(m.getMyEnumList()));
        e.setProperty("myStringList", m.getMyStringList());

        e.setProperty("version", m.getVersion());
        return e;
    }

    @Override
    protected Key getKey(Object model) {
        org.slim3.datastore.model.Hoge m =
            (org.slim3.datastore.model.Hoge) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, Key key) {
        Hoge m = (Hoge) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        Hoge m = (Hoge) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void incrementVersion(Object model) {
        Hoge m = (Hoge) model;
        long version = m.getVersion() != null ? m.getVersion().longValue() : 0L;
        m.setVersion(version + 1);

    }
}