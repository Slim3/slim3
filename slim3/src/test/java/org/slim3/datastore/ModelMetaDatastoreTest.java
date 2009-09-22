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
        assertEquals((short) 1, myPrimitiveIntArray2[0]);
        Integer[] myIntegerArray2 = model2.getMyIntegerArray();
        assertEquals(1, myIntegerArray2.length);
        assertEquals(Integer.valueOf("1"), myIntegerArray2[0]);
    }
}
