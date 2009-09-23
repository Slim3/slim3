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
        model.setMyPrimitiveShortArray(new short[] { 1 });
        model.setMyShortArray(new Short[] { 1 });
        model.setMyShortList(Arrays.asList((short) 1));
        model
            .setMyShortArrayList(new ArrayList<Short>(Arrays.asList((short) 1)));
        model.setMyShortSet(new HashSet<Short>(Arrays.asList((short) 1)));
        model.setMyShortHashSet(new HashSet<Short>(Arrays.asList((short) 1)));
        model.setMyShortSortedSet(new TreeSet<Short>(Arrays.asList((short) 1)));
        model.setMyShortTreeSet(new TreeSet<Short>(Arrays.asList((short) 1)));
        model.setMyShortLinkedList(new LinkedList<Short>(Arrays
            .asList((short) 1)));
        model.setMyShortLinkedHashSet(new LinkedHashSet<Short>(Arrays
            .asList((short) 1)));
        Stack<Short> myShortStack = new Stack<Short>();
        myShortStack.add((short) 1);
        model.setMyShortStack(myShortStack);
        model.setMyShortVector(new Vector<Short>(Arrays.asList((short) 1)));

        model.setMyPrimitiveIntArray(new int[] { 1 });
        model.setMyIntegerArray(new Integer[] { 1 });
        model.setMyIntegerList(Arrays.asList(1));
        model.setMyIntegerArrayList(new ArrayList<Integer>(Arrays.asList(1)));
        model.setMyIntegerSet(new HashSet<Integer>(Arrays.asList(1)));
        model.setMyIntegerHashSet(new HashSet<Integer>(Arrays.asList(1)));
        model.setMyIntegerSortedSet(new TreeSet<Integer>(Arrays.asList(1)));
        model.setMyIntegerTreeSet(new TreeSet<Integer>(Arrays.asList(1)));
        model.setMyIntegerLinkedList(new LinkedList<Integer>(Arrays.asList(1)));
        model.setMyIntegerLinkedHashSet(new LinkedHashSet<Integer>(Arrays
            .asList(1)));
        Stack<Integer> myIntegerStack = new Stack<Integer>();
        myIntegerStack.add(1);
        model.setMyIntegerStack(myIntegerStack);
        model.setMyIntegerVector(new Vector<Integer>(Arrays.asList(1)));

        model.setMyPrimitiveLongArray(new long[] { 1 });
        model.setMyLongArray(new Long[] { 1L });
        model.setMyLongList(Arrays.asList(1L));
        model.setMyLongArrayList(new ArrayList<Long>(Arrays.asList(1L)));
        model.setMyLongSet(new HashSet<Long>(Arrays.asList(1L)));
        model.setMyLongHashSet(new HashSet<Long>(Arrays.asList(1L)));
        model.setMyLongSortedSet(new TreeSet<Long>(Arrays.asList(1L)));
        model.setMyLongTreeSet(new TreeSet<Long>(Arrays.asList(1L)));
        model.setMyLongLinkedList(new LinkedList<Long>(Arrays.asList(1L)));
        model
            .setMyLongLinkedHashSet(new LinkedHashSet<Long>(Arrays.asList(1L)));
        Stack<Long> myLongStack = new Stack<Long>();
        myLongStack.add(1L);
        model.setMyLongStack(myLongStack);
        model.setMyLongVector(new Vector<Long>(Arrays.asList(1L)));

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
        byte[] myBytes2 = model2.getMyBytes();
        assertEquals(1, myBytes2.length);
        assertEquals(1, myBytes2[0]);
        byte[] myBytesBlob2 = model2.getMyBytesBlob();
        assertEquals(1, myBytesBlob2.length);
        assertEquals(1, myBytesBlob2[0]);
        assertEquals(new MySerializable("aaa"), model2.getMySerializable());
        assertEquals(new MySerializable("aaa"), model2.getMySerializableBlob());
        short[] myPrimitiveShortArray2 = model2.getMyPrimitiveShortArray();
        assertEquals(1, myPrimitiveShortArray2.length);
        assertEquals((short) 1, myPrimitiveShortArray2[0]);
        Short[] myShortArray2 = model2.getMyShortArray();
        assertEquals(1, myShortArray2.length);
        assertEquals(Short.valueOf("1"), myShortArray2[0]);
        List<Short> myShortList2 = model2.getMyShortList();
        assertEquals(1, myShortList2.size());
        assertEquals(Short.valueOf("1"), myShortList2.get(0));
        ArrayList<Short> myShortArrayList2 = model2.getMyShortArrayList();
        assertEquals(1, myShortArrayList2.size());
        assertEquals(Short.valueOf("1"), myShortArrayList2.get(0));
        Set<Short> myShortSet2 = model2.getMyShortSet();
        assertEquals(1, myShortSet2.size());
        assertEquals(Short.valueOf("1"), myShortSet2.iterator().next());
        HashSet<Short> myShortHashSet2 = model2.getMyShortHashSet();
        assertEquals(1, myShortHashSet2.size());
        assertEquals(Short.valueOf("1"), myShortHashSet2.iterator().next());
        SortedSet<Short> myShortSortedSet2 = model2.getMyShortSortedSet();
        assertEquals(1, myShortSortedSet2.size());
        assertEquals(Short.valueOf("1"), myShortSortedSet2.iterator().next());
        TreeSet<Short> myShortTreeSet2 = model2.getMyShortTreeSet();
        assertEquals(1, myShortTreeSet2.size());
        assertEquals(Short.valueOf("1"), myShortTreeSet2.iterator().next());
        LinkedList<Short> myShortLinkedList2 = model2.getMyShortLinkedList();
        assertEquals(1, myShortLinkedList2.size());
        assertEquals(Short.valueOf("1"), myShortLinkedList2.iterator().next());
        LinkedHashSet<Short> myShortLinkedHashSet2 =
            model2.getMyShortLinkedHashSet();
        assertEquals(1, myShortLinkedHashSet2.size());
        assertEquals(Short.valueOf("1"), myShortLinkedHashSet2
            .iterator()
            .next());
        Stack<Short> myShortStack2 = model2.getMyShortStack();
        assertEquals(1, myShortStack2.size());
        assertEquals(Short.valueOf("1"), myShortStack2.iterator().next());
        Vector<Short> myShortVector2 = model2.getMyShortVector();
        assertEquals(1, myShortVector2.size());
        assertEquals(Short.valueOf("1"), myShortVector2.iterator().next());

        int[] myPrimitiveIntArray2 = model2.getMyPrimitiveIntArray();
        assertEquals(1, myPrimitiveIntArray2.length);
        assertEquals(1, myPrimitiveIntArray2[0]);
        Integer[] myIntegerArray2 = model2.getMyIntegerArray();
        assertEquals(1, myIntegerArray2.length);
        assertEquals(Integer.valueOf("1"), myIntegerArray2[0]);
        List<Integer> myIntegerList2 = model2.getMyIntegerList();
        assertEquals(1, myIntegerList2.size());
        assertEquals(Integer.valueOf("1"), myIntegerList2.get(0));
        ArrayList<Integer> myIntegerArrayList2 = model2.getMyIntegerArrayList();
        assertEquals(1, myIntegerArrayList2.size());
        assertEquals(Integer.valueOf("1"), myIntegerArrayList2.get(0));
        Set<Integer> myIntegerSet2 = model2.getMyIntegerSet();
        assertEquals(1, myIntegerSet2.size());
        assertEquals(Integer.valueOf("1"), myIntegerSet2.iterator().next());
        HashSet<Integer> myIntegerHashSet2 = model2.getMyIntegerHashSet();
        assertEquals(1, myIntegerHashSet2.size());
        assertEquals(Integer.valueOf("1"), myIntegerHashSet2.iterator().next());
        SortedSet<Integer> myIntegerSortedSet2 = model2.getMyIntegerSortedSet();
        assertEquals(1, myIntegerSortedSet2.size());
        assertEquals(Integer.valueOf("1"), myIntegerSortedSet2
            .iterator()
            .next());
        TreeSet<Integer> myIntegerTreeSet2 = model2.getMyIntegerTreeSet();
        assertEquals(1, myIntegerTreeSet2.size());
        assertEquals(Integer.valueOf("1"), myIntegerTreeSet2.iterator().next());
        LinkedList<Integer> myIntegerLinkedList2 =
            model2.getMyIntegerLinkedList();
        assertEquals(1, myIntegerLinkedList2.size());
        assertEquals(Integer.valueOf("1"), myIntegerLinkedList2
            .iterator()
            .next());
        LinkedHashSet<Integer> myIntegerLinkedHashSet2 =
            model2.getMyIntegerLinkedHashSet();
        assertEquals(1, myIntegerLinkedHashSet2.size());
        assertEquals(Integer.valueOf("1"), myIntegerLinkedHashSet2
            .iterator()
            .next());
        Stack<Integer> myIntegerStack2 = model2.getMyIntegerStack();
        assertEquals(1, myIntegerStack2.size());
        assertEquals(Integer.valueOf("1"), myIntegerStack2.iterator().next());
        Vector<Integer> myIntegerVector2 = model2.getMyIntegerVector();
        assertEquals(1, myIntegerVector2.size());
        assertEquals(Integer.valueOf("1"), myIntegerVector2.iterator().next());

        long[] myPrimitiveLongArray2 = model2.getMyPrimitiveLongArray();
        assertEquals(1, myPrimitiveLongArray2.length);
        assertEquals(1L, myPrimitiveLongArray2[0]);
        Long[] myLongArray2 = model2.getMyLongArray();
        assertEquals(1, myLongArray2.length);
        assertEquals(Long.valueOf("1"), myLongArray2[0]);
        List<Long> myLongList2 = model2.getMyLongList();
        assertEquals(1, myLongList2.size());
        assertEquals(Long.valueOf("1"), myLongList2.get(0));
        ArrayList<Long> myLongArrayList2 = model2.getMyLongArrayList();
        assertEquals(1, myLongArrayList2.size());
        assertEquals(Long.valueOf("1"), myLongArrayList2.get(0));
        Set<Long> myLongSet2 = model2.getMyLongSet();
        assertEquals(1, myLongSet2.size());
        assertEquals(Long.valueOf("1"), myLongSet2.iterator().next());
        HashSet<Long> myLongHashSet2 = model2.getMyLongHashSet();
        assertEquals(1, myLongHashSet2.size());
        assertEquals(Long.valueOf("1"), myLongHashSet2.iterator().next());
        SortedSet<Long> myLongSortedSet2 = model2.getMyLongSortedSet();
        assertEquals(1, myLongSortedSet2.size());
        assertEquals(Long.valueOf("1"), myLongSortedSet2.iterator().next());
        TreeSet<Long> myLongTreeSet2 = model2.getMyLongTreeSet();
        assertEquals(1, myLongTreeSet2.size());
        assertEquals(Long.valueOf("1"), myLongTreeSet2.iterator().next());
        LinkedList<Long> myLongLinkedList2 = model2.getMyLongLinkedList();
        assertEquals(1, myLongLinkedList2.size());
        assertEquals(Long.valueOf("1"), myLongLinkedList2.iterator().next());
        LinkedHashSet<Long> myLongLinkedHashSet2 =
            model2.getMyLongLinkedHashSet();
        assertEquals(1, myLongLinkedHashSet2.size());
        assertEquals(Long.valueOf("1"), myLongLinkedHashSet2.iterator().next());
        Stack<Long> myLongStack2 = model2.getMyLongStack();
        assertEquals(1, myLongStack2.size());
        assertEquals(Long.valueOf("1"), myLongStack2.iterator().next());
        Vector<Long> myLongVector2 = model2.getMyLongVector();
        assertEquals(1, myLongVector2.size());
        assertEquals(Long.valueOf("1"), myLongVector2.iterator().next());
    }
}
