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

import org.slim3.datastore.CollectionAttributeMeta;
import org.slim3.datastore.CoreAttributeMeta;
import org.slim3.datastore.ModelMeta;
import org.slim3.datastore.StringAttributeMeta;
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
    public CoreAttributeMeta<Hoge, String> myStringText =
        new CoreAttributeMeta<Hoge, String>(this, "myStringText", String.class);

    /**
     * 
     */
    public CoreAttributeMeta<Hoge, Text> myText =
        new CoreAttributeMeta<Hoge, Text>(this, "myText", Text.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, short[], Short> myPrimitiveShortArray =
        new CollectionAttributeMeta<Hoge, short[], Short>(
            this,
            "myPrimitiveShortArray",
            short[].class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, Short[], Short> myShortArray =
        new CollectionAttributeMeta<Hoge, Short[], Short>(
            this,
            "myShortArray",
            Short[].class);

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
    public CollectionAttributeMeta<Hoge, ArrayList<Short>, Short> myShortArrayList =
        new CollectionAttributeMeta<Hoge, ArrayList<Short>, Short>(
            this,
            "myShortArrayList",
            ArrayList.class);

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
    public CollectionAttributeMeta<Hoge, HashSet<Short>, Short> myShortHashSet =
        new CollectionAttributeMeta<Hoge, HashSet<Short>, Short>(
            this,
            "myShortHashSet",
            HashSet.class);

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
    public CollectionAttributeMeta<Hoge, TreeSet<Short>, Short> myShortTreeSet =
        new CollectionAttributeMeta<Hoge, TreeSet<Short>, Short>(
            this,
            "myShortTreeSet",
            TreeSet.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, LinkedList<Short>, Short> myShortLinkedList =
        new CollectionAttributeMeta<Hoge, LinkedList<Short>, Short>(
            this,
            "myShortLinkedList",
            LinkedList.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, LinkedHashSet<Short>, Short> myShortLinkedHashSet =
        new CollectionAttributeMeta<Hoge, LinkedHashSet<Short>, Short>(
            this,
            "myShortLinkedHashSet",
            LinkedHashSet.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, Stack<Short>, Short> myShortStack =
        new CollectionAttributeMeta<Hoge, Stack<Short>, Short>(
            this,
            "myShortStack",
            Stack.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, Vector<Short>, Short> myShortVector =
        new CollectionAttributeMeta<Hoge, Vector<Short>, Short>(
            this,
            "myShortVector",
            Vector.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, int[], Integer> myPrimitiveIntArray =
        new CollectionAttributeMeta<Hoge, int[], Integer>(
            this,
            "myPrimitiveIntArray",
            int[].class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, Integer[], Integer> myIntegerArray =
        new CollectionAttributeMeta<Hoge, Integer[], Integer>(
            this,
            "myIntegerArray",
            Integer[].class);

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
    public CollectionAttributeMeta<Hoge, ArrayList<Integer>, Integer> myIntegerArrayList =
        new CollectionAttributeMeta<Hoge, ArrayList<Integer>, Integer>(
            this,
            "myIntegerArrayList",
            ArrayList.class);

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
    public CollectionAttributeMeta<Hoge, HashSet<Integer>, Integer> myIntegerHashSet =
        new CollectionAttributeMeta<Hoge, HashSet<Integer>, Integer>(
            this,
            "myIntegerHashSet",
            HashSet.class);

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
    public CollectionAttributeMeta<Hoge, TreeSet<Integer>, Integer> myIntegerTreeSet =
        new CollectionAttributeMeta<Hoge, TreeSet<Integer>, Integer>(
            this,
            "myIntegerTreeSet",
            TreeSet.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, LinkedList<Integer>, Integer> myIntegerLinkedList =
        new CollectionAttributeMeta<Hoge, LinkedList<Integer>, Integer>(
            this,
            "myIntegerLinkedList",
            LinkedList.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, LinkedHashSet<Integer>, Integer> myIntegerLinkedHashSet =
        new CollectionAttributeMeta<Hoge, LinkedHashSet<Integer>, Integer>(
            this,
            "myIntegerLinkedHashSet",
            LinkedHashSet.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, Stack<Integer>, Integer> myIntegerStack =
        new CollectionAttributeMeta<Hoge, Stack<Integer>, Integer>(
            this,
            "myIntegerStack",
            Stack.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, Vector<Integer>, Integer> myIntegerVector =
        new CollectionAttributeMeta<Hoge, Vector<Integer>, Integer>(
            this,
            "myIntegerVector",
            Vector.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, long[], Long> myPrimitiveLongArray =
        new CollectionAttributeMeta<Hoge, long[], Long>(
            this,
            "myPrimitiveLongArray",
            long[].class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, Long[], Long> myLongArray =
        new CollectionAttributeMeta<Hoge, Long[], Long>(
            this,
            "myLongArray",
            Long[].class);

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
    public CollectionAttributeMeta<Hoge, ArrayList<Long>, Long> myLongArrayList =
        new CollectionAttributeMeta<Hoge, ArrayList<Long>, Long>(
            this,
            "myLongArrayList",
            ArrayList.class);

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
    public CollectionAttributeMeta<Hoge, HashSet<Long>, Long> myLongHashSet =
        new CollectionAttributeMeta<Hoge, HashSet<Long>, Long>(
            this,
            "myLongHashSet",
            HashSet.class);

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
    public CollectionAttributeMeta<Hoge, TreeSet<Long>, Long> myLongTreeSet =
        new CollectionAttributeMeta<Hoge, TreeSet<Long>, Long>(
            this,
            "myLongTreeSet",
            TreeSet.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, LinkedList<Long>, Long> myLongLinkedList =
        new CollectionAttributeMeta<Hoge, LinkedList<Long>, Long>(
            this,
            "myLongLinkedList",
            LinkedList.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, LinkedHashSet<Long>, Long> myLongLinkedHashSet =
        new CollectionAttributeMeta<Hoge, LinkedHashSet<Long>, Long>(
            this,
            "myLongLinkedHashSet",
            LinkedHashSet.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, Stack<Long>, Long> myLongStack =
        new CollectionAttributeMeta<Hoge, Stack<Long>, Long>(
            this,
            "myLongStack",
            Stack.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, Vector<Long>, Long> myLongVector =
        new CollectionAttributeMeta<Hoge, Vector<Long>, Long>(
            this,
            "myLongVector",
            Vector.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, float[], Float> myPrimitiveFloatArray =
        new CollectionAttributeMeta<Hoge, float[], Float>(
            this,
            "myPrimitiveFloatArray",
            float[].class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, Float[], Float> myFloatArray =
        new CollectionAttributeMeta<Hoge, Float[], Float>(
            this,
            "myFloatArray",
            Float[].class);

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
    public CollectionAttributeMeta<Hoge, ArrayList<Float>, Float> myFloatArrayList =
        new CollectionAttributeMeta<Hoge, ArrayList<Float>, Float>(
            this,
            "myFloatArrayList",
            ArrayList.class);

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
    public CollectionAttributeMeta<Hoge, HashSet<Float>, Float> myFloatHashSet =
        new CollectionAttributeMeta<Hoge, HashSet<Float>, Float>(
            this,
            "myFloatHashSet",
            HashSet.class);

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
    public CollectionAttributeMeta<Hoge, TreeSet<Float>, Float> myFloatTreeSet =
        new CollectionAttributeMeta<Hoge, TreeSet<Float>, Float>(
            this,
            "myFloatTreeSet",
            TreeSet.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, LinkedList<Float>, Float> myFloatLinkedList =
        new CollectionAttributeMeta<Hoge, LinkedList<Float>, Float>(
            this,
            "myFloatLinkedList",
            LinkedList.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, LinkedHashSet<Float>, Float> myFloatLinkedHashSet =
        new CollectionAttributeMeta<Hoge, LinkedHashSet<Float>, Float>(
            this,
            "myFloatLinkedHashSet",
            LinkedHashSet.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, Stack<Float>, Float> myFloatStack =
        new CollectionAttributeMeta<Hoge, Stack<Float>, Float>(
            this,
            "myFloatStack",
            Stack.class);

    /**
     * 
     */
    public CollectionAttributeMeta<Hoge, Vector<Float>, Float> myFloatVector =
        new CollectionAttributeMeta<Hoge, Vector<Float>, Float>(
            this,
            "myFloatVector",
            Vector.class);

    /**
     * 
     */
    public CoreAttributeMeta<Hoge, Long> version =
        new CoreAttributeMeta<Hoge, Long>(this, "version", Long.class);

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

        model
            .setMyPrimitiveFloatArray(doubleListToPrimitiveFloatArray((List<Double>) entity
                .getProperty("myPrimitiveFloatArray")));
        model.setMyFloatArray(doubleListToFloatArray((List<Double>) entity
            .getProperty("myFloatArray")));
        model.setMyFloatList(doubleListToFloatList((List<Double>) entity
            .getProperty("myFloatList")));
        model.setMyFloatArrayList(doubleListToFloatList((List<Double>) entity
            .getProperty("myFloatArrayList")));
        model.setMyFloatSet(doubleListToFloatSet((List<Double>) entity
            .getProperty("myFloatSet")));
        model.setMyFloatHashSet(doubleListToFloatSet((List<Double>) entity
            .getProperty("myFloatHashSet")));
        model
            .setMyFloatSortedSet(doubleListToFloatSortedSet((List<Double>) entity
                .getProperty("myFloatSortedSet")));
        model
            .setMyFloatTreeSet(doubleListToFloatSortedSet((List<Double>) entity
                .getProperty("myFloatTreeSet")));
        model
            .setMyFloatLinkedList(doubleListToFloatLinkedList((List<Double>) entity
                .getProperty("myFloatLinkedList")));
        model
            .setMyFloatLinkedHashSet(doubleListToFloatLinkedHashSet((List<Double>) entity
                .getProperty("myFloatLinkedHashSet")));
        model.setMyFloatStack(doubleListToFloatStack((List<Double>) entity
            .getProperty("myFloatStack")));
        model.setMyFloatVector(doubleListToFloatVector((List<Double>) entity
            .getProperty("myFloatVector")));
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
        e.setUnindexedProperty("myStringText", stringToText(m
            .getMyStringText()));
        e.setUnindexedProperty("myText", m.getMyText());
        e
            .setUnindexedProperty("myBytes", bytesToShortBlob(m.getMyBytes()));
        e.setUnindexedProperty("myBytesBlob", bytesToBlob(m
            .getMyBytesBlob()));
        e.setUnindexedProperty("mySerializable", serializableToShortBlob(m
            .getMySerializable()));
        e.setUnindexedProperty("mySerializableBlob", serializableToBlob(m
            .getMySerializableBlob()));

        e.setProperty(
            "myPrimitiveShortArray",
            primitiveShortArrayToLongList(m.getMyPrimitiveShortArray()));
        e.setProperty("myShortArray", shortArrayToLongList(m
            .getMyShortArray()));
        e.setProperty("myShortList", m.getMyShortList());
        e.setProperty("myShortArrayList", m.getMyShortArrayList());
        e.setProperty("myShortSet", m.getMyShortSet());
        e.setProperty("myShortHashSet", m.getMyShortHashSet());
        e.setProperty("myShortSortedSet", m.getMyShortSortedSet());
        e.setProperty("myShortTreeSet", m.getMyShortTreeSet());
        e.setProperty("myShortLinkedList", m.getMyShortLinkedList());
        e.setProperty("myShortLinkedHashSet", m.getMyShortLinkedHashSet());
        e.setProperty("myShortStack", m.getMyShortStack());
        e.setProperty("myShortVector", m.getMyShortVector());

        e.setProperty("myPrimitiveIntArray", primitiveIntArrayToLongList(m
            .getMyPrimitiveIntArray()));
        e.setProperty("myIntegerArray", integerArrayToLongList(m
            .getMyIntegerArray()));
        e.setProperty("myIntegerList", m.getMyIntegerList());
        e.setProperty("myIntegerArrayList", m.getMyIntegerArrayList());
        e.setProperty("myIntegerSet", m.getMyIntegerSet());
        e.setProperty("myIntegerHashSet", m.getMyIntegerHashSet());
        e.setProperty("myIntegerSortedSet", m.getMyIntegerSortedSet());
        e.setProperty("myIntegerTreeSet", m.getMyIntegerTreeSet());
        e.setProperty("myIntegerLinkedList", m.getMyIntegerLinkedList());
        e.setProperty("myIntegerLinkedHashSet", m
            .getMyIntegerLinkedHashSet());
        e.setProperty("myIntegerStack", m.getMyIntegerStack());
        e.setProperty("myIntegerVector", m.getMyIntegerVector());

        e.setProperty(
            "myPrimitiveLongArray",
            primitiveLongArrayToLongList(m.getMyPrimitiveLongArray()));
        e.setProperty("myLongArray", longArrayToLongList(m
            .getMyLongArray()));
        e.setProperty("myLongList", m.getMyLongList());
        e.setProperty("myLongArrayList", m.getMyLongArrayList());
        e.setProperty("myLongSet", m.getMyLongSet());
        e.setProperty("myLongHashSet", m.getMyLongHashSet());
        e.setProperty("myLongSortedSet", m.getMyLongSortedSet());
        e.setProperty("myLongTreeSet", m.getMyLongTreeSet());
        e.setProperty("myLongLinkedList", m.getMyLongLinkedList());
        e.setProperty("myLongLinkedHashSet", m.getMyLongLinkedHashSet());
        e.setProperty("myLongStack", m.getMyLongStack());
        e.setProperty("myLongVector", m.getMyLongVector());

        e.setProperty(
            "myPrimitiveFloatArray",
            primitiveFloatArrayToDoubleList(m.getMyPrimitiveFloatArray()));
        e.setProperty("myFloatArray", floatArrayToDoubleList(m
            .getMyFloatArray()));
        e.setProperty("myFloatList", m.getMyFloatList());
        e.setProperty("myFloatArrayList", m.getMyFloatArrayList());
        e.setProperty("myFloatSet", m.getMyFloatSet());
        e.setProperty("myFloatHashSet", m.getMyFloatHashSet());
        e.setProperty("myFloatSortedSet", m.getMyFloatSortedSet());
        e.setProperty("myFloatTreeSet", m.getMyFloatTreeSet());
        e.setProperty("myFloatLinkedList", m.getMyFloatLinkedList());
        e.setProperty("myFloatLinkedHashSet", m.getMyFloatLinkedHashSet());
        e.setProperty("myFloatStack", m.getMyFloatStack());
        e.setProperty("myFloatVector", m.getMyFloatVector());
        return e;
    }
}