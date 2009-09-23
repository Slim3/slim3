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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

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
    public CollectionAttributeMeta<Short> myPrimitiveShortArray =
        new CollectionAttributeMeta<Short>(
            this,
            "myPrimitiveShortArray",
            short[].class);

    /**
     * 
     */
    public CollectionAttributeMeta<Short> myShortArray =
        new CollectionAttributeMeta<Short>(this, "myShortArray", Short[].class);

    /**
     * 
     */
    public CollectionAttributeMeta<Short> myShortList =
        new CollectionAttributeMeta<Short>(this, "myShortList", List.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Short> myShortArrayList =
        new CollectionAttributeMeta<Short>(
            this,
            "myShortArrayList",
            ArrayList.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Short> myShortSet =
        new CollectionAttributeMeta<Short>(this, "myShortSet", Set.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Short> myShortHashSet =
        new CollectionAttributeMeta<Short>(
            this,
            "myShortHashSet",
            HashSet.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Short> myShortSortedSet =
        new CollectionAttributeMeta<Short>(
            this,
            "myShortSortedSet",
            SortedSet.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Short> myShortTreeSet =
        new CollectionAttributeMeta<Short>(
            this,
            "myShortTreeSet",
            TreeSet.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Short> myShortLinkedList =
        new CollectionAttributeMeta<Short>(
            this,
            "myShortLinkedList",
            LinkedList.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Short> myShortLinkedHashSet =
        new CollectionAttributeMeta<Short>(
            this,
            "myShortLinkedHashSet",
            LinkedHashSet.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Short> myShortStack =
        new CollectionAttributeMeta<Short>(this, "myShortStack", Stack.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Short> myShortVector =
        new CollectionAttributeMeta<Short>(this, "myShortVector", Vector.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Integer> myPrimitiveIntArray =
        new CollectionAttributeMeta<Integer>(
            this,
            "myPrimitiveIntArray",
            int[].class);

    /**
     * 
     */
    public CollectionAttributeMeta<Integer> myIntegerArray =
        new CollectionAttributeMeta<Integer>(
            this,
            "myIntegerArray",
            Integer[].class);

    /**
     * 
     */
    public CollectionAttributeMeta<Integer> myIntegerList =
        new CollectionAttributeMeta<Integer>(this, "myIntegerList", List.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Integer> myIntegerArrayList =
        new CollectionAttributeMeta<Integer>(
            this,
            "myIntegerArrayList",
            ArrayList.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Integer> myIntegerSet =
        new CollectionAttributeMeta<Integer>(this, "myIntegerSet", Set.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Integer> myIntegerHashSet =
        new CollectionAttributeMeta<Integer>(
            this,
            "myIntegerHashSet",
            HashSet.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Integer> myIntegerSortedSet =
        new CollectionAttributeMeta<Integer>(
            this,
            "myIntegerSortedSet",
            SortedSet.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Integer> myIntegerTreeSet =
        new CollectionAttributeMeta<Integer>(
            this,
            "myIntegerTreeSet",
            TreeSet.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Integer> myIntegerLinkedList =
        new CollectionAttributeMeta<Integer>(
            this,
            "myIntegerLinkedList",
            LinkedList.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Integer> myIntegerLinkedHashSet =
        new CollectionAttributeMeta<Integer>(
            this,
            "myIntegerLinkedHashSet",
            LinkedHashSet.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Integer> myIntegerStack =
        new CollectionAttributeMeta<Integer>(
            this,
            "myIntegerStack",
            Stack.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Integer> myIntegerVector =
        new CollectionAttributeMeta<Integer>(
            this,
            "myIntegerVector",
            Vector.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Long> myPrimitiveLongArray =
        new CollectionAttributeMeta<Long>(
            this,
            "myPrimitiveLongArray",
            long[].class);

    /**
     * 
     */
    public CollectionAttributeMeta<Long> myLongArray =
        new CollectionAttributeMeta<Long>(this, "myLongArray", Long[].class);

    /**
     * 
     */
    public CollectionAttributeMeta<Long> myLongList =
        new CollectionAttributeMeta<Long>(this, "myLongList", List.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Long> myLongArrayList =
        new CollectionAttributeMeta<Long>(
            this,
            "myLongArrayList",
            ArrayList.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Long> myLongSet =
        new CollectionAttributeMeta<Long>(this, "myLongSet", Set.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Long> myLongHashSet =
        new CollectionAttributeMeta<Long>(this, "myLongHashSet", HashSet.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Long> myLongSortedSet =
        new CollectionAttributeMeta<Long>(
            this,
            "myLongSortedSet",
            SortedSet.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Long> myLongTreeSet =
        new CollectionAttributeMeta<Long>(this, "myLongTreeSet", TreeSet.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Long> myLongLinkedList =
        new CollectionAttributeMeta<Long>(
            this,
            "myLongLinkedList",
            LinkedList.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Long> myLongLinkedHashSet =
        new CollectionAttributeMeta<Long>(
            this,
            "myLongLinkedHashSet",
            LinkedHashSet.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Long> myLongStack =
        new CollectionAttributeMeta<Long>(this, "myLongStack", Stack.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Long> myLongVector =
        new CollectionAttributeMeta<Long>(this, "myLongVector", Vector.class);

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
        model
            .setMyPrimitiveShortArray(longListToPrimitiveShortArray((List<Long>) entity
                .getProperty("myPrimitiveShortArray")));
        model.setMyShortArray(longListToShortArray((List<Long>) entity
            .getProperty("myShortArray")));
        model.setMyShortList(longListToShortList((List<Long>) entity
            .getProperty("myShortList")));
        model.setMyShortArrayList(longListToShortList((List<Long>) entity
            .getProperty("myShortArrayList")));
        model.setMyShortSet(longListToShortSet((List<Long>) entity
            .getProperty("myShortSet")));
        model.setMyShortHashSet(longListToShortSet((List<Long>) entity
            .getProperty("myShortHashSet")));
        model.setMyShortSortedSet(longListToShortSortedSet((List<Long>) entity
            .getProperty("myShortSortedSet")));
        model.setMyShortTreeSet(longListToShortSortedSet((List<Long>) entity
            .getProperty("myShortTreeSet")));
        model
            .setMyShortLinkedList(longListToShortLinkedList((List<Long>) entity
                .getProperty("myShortLinkedList")));
        model
            .setMyShortLinkedHashSet(longListToShortLinkedHashSet((List<Long>) entity
                .getProperty("myShortLinkedHashSet")));
        model.setMyShortStack(longListToShortStack((List<Long>) entity
            .getProperty("myShortStack")));
        model.setMyShortVector(longListToShortVector((List<Long>) entity
            .getProperty("myShortVector")));
        model
            .setMyPrimitiveIntArray(longListToPrimitiveIntArray((List<Long>) entity
                .getProperty("myPrimitiveIntArray")));
        model.setMyIntegerArray(longListToIntegerArray((List<Long>) entity
            .getProperty("myIntegerArray")));
        model.setMyIntegerList(longListToIntegerList((List<Long>) entity
            .getProperty("myIntegerList")));
        model.setMyIntegerArrayList(longListToIntegerList((List<Long>) entity
            .getProperty("myIntegerArrayList")));
        model.setMyIntegerSet(longListToIntegerSet((List<Long>) entity
            .getProperty("myIntegerSet")));
        model.setMyIntegerHashSet(longListToIntegerSet((List<Long>) entity
            .getProperty("myIntegerHashSet")));
        model
            .setMyIntegerSortedSet(longListToIntegerSortedSet((List<Long>) entity
                .getProperty("myIntegerSortedSet")));
        model
            .setMyIntegerTreeSet(longListToIntegerSortedSet((List<Long>) entity
                .getProperty("myIntegerTreeSet")));
        model
            .setMyIntegerLinkedList(longListToIntegerLinkedList((List<Long>) entity
                .getProperty("myIntegerLinkedList")));
        model
            .setMyIntegerLinkedHashSet(longListToIntegerLinkedHashSet((List<Long>) entity
                .getProperty("myIntegerLinkedHashSet")));
        model.setMyIntegerStack(longListToIntegerStack((List<Long>) entity
            .getProperty("myIntegerStack")));
        model.setMyIntegerVector(longListToIntegerVector((List<Long>) entity
            .getProperty("myIntegerVector")));

        model
            .setMyPrimitiveLongArray(longListToPrimitiveLongArray((List<Long>) entity
                .getProperty("myPrimitiveLongArray")));
        model.setMyLongArray(longListToLongArray((List<Long>) entity
            .getProperty("myLongArray")));
        model.setMyLongList((List<Long>) entity.getProperty("myLongList"));
        model.setMyLongArrayList((ArrayList<Long>) entity
            .getProperty("myLongArrayList"));
        model.setMyLongSet(longListToLongSet((List<Long>) entity
            .getProperty("myLongSet")));
        model.setMyLongHashSet(longListToLongSet((List<Long>) entity
            .getProperty("myLongHashSet")));
        model.setMyLongSortedSet(longListToLongSortedSet((List<Long>) entity
            .getProperty("myLongSortedSet")));
        model.setMyLongTreeSet(longListToLongSortedSet((List<Long>) entity
            .getProperty("myLongTreeSet")));
        model.setMyLongLinkedList(longListToLongLinkedList((List<Long>) entity
            .getProperty("myLongLinkedList")));
        model
            .setMyLongLinkedHashSet(longListToLongLinkedHashSet((List<Long>) entity
                .getProperty("myLongLinkedHashSet")));
        model.setMyLongStack(longListToLongStack((List<Long>) entity
            .getProperty("myLongStack")));
        model.setMyLongVector(longListToLongVector((List<Long>) entity
            .getProperty("myLongVector")));
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

        entity.setProperty(
            "myPrimitiveShortArray",
            primitiveShortArrayToLongList(model.getMyPrimitiveShortArray()));
        entity.setProperty("myShortArray", shortArrayToLongList(model
            .getMyShortArray()));
        entity.setProperty("myShortList", model.getMyShortList());
        entity.setProperty("myShortArrayList", model.getMyShortArrayList());
        entity.setProperty("myShortSet", model.getMyShortSet());
        entity.setProperty("myShortHashSet", model.getMyShortHashSet());
        entity.setProperty("myShortSortedSet", model.getMyShortSortedSet());
        entity.setProperty("myShortTreeSet", model.getMyShortTreeSet());
        entity.setProperty("myShortLinkedList", model.getMyShortLinkedList());
        entity.setProperty("myShortLinkedHashSet", model
            .getMyShortLinkedHashSet());
        entity.setProperty("myShortStack", model.getMyShortStack());
        entity.setProperty("myShortVector", model.getMyShortVector());

        entity.setProperty(
            "myPrimitiveIntArray",
            primitiveIntArrayToLongList(model.getMyPrimitiveIntArray()));
        entity.setProperty("myIntegerArray", integerArrayToLongList(model
            .getMyIntegerArray()));
        entity.setProperty("myIntegerList", model.getMyIntegerList());
        entity.setProperty("myIntegerArrayList", model.getMyIntegerArrayList());
        entity.setProperty("myIntegerSet", model.getMyIntegerSet());
        entity.setProperty("myIntegerHashSet", model.getMyIntegerHashSet());
        entity.setProperty("myIntegerSortedSet", model.getMyIntegerSortedSet());
        entity.setProperty("myIntegerTreeSet", model.getMyIntegerTreeSet());
        entity.setProperty("myIntegerLinkedList", model
            .getMyIntegerLinkedList());
        entity.setProperty("myIntegerLinkedHashSet", model
            .getMyIntegerLinkedHashSet());
        entity.setProperty("myIntegerStack", model.getMyIntegerStack());
        entity.setProperty("myIntegerVector", model.getMyIntegerVector());

        entity.setProperty(
            "myPrimitiveLongArray",
            primitiveLongArrayToLongList(model.getMyPrimitiveLongArray()));
        entity.setProperty("myLongArray", longArrayToLongList(model
            .getMyLongArray()));
        entity.setProperty("myLongList", model.getMyLongList());
        entity.setProperty("myLongArrayList", model.getMyLongArrayList());
        entity.setProperty("myLongSet", model.getMyLongSet());
        entity.setProperty("myLongHashSet", model.getMyLongHashSet());
        entity.setProperty("myLongSortedSet", model.getMyLongSortedSet());
        entity.setProperty("myLongTreeSet", model.getMyLongTreeSet());
        entity.setProperty("myLongLinkedList", model.getMyLongLinkedList());
        entity.setProperty("myLongLinkedHashSet", model
            .getMyLongLinkedHashSet());
        entity.setProperty("myLongStack", model.getMyLongStack());
        entity.setProperty("myLongVector", model.getMyLongVector());
        return entity;
    }
}