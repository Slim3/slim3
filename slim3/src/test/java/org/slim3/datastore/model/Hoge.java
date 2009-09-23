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
package org.slim3.datastore.model;

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

import org.slim3.datastore.Blob;
import org.slim3.datastore.Model;
import org.slim3.datastore.PrimaryKey;
import org.slim3.datastore.Text;
import org.slim3.datastore.Version;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.ShortBlob;

/**
 * @author higa
 * 
 */
@Model
public class Hoge {

    @PrimaryKey
    private Key key;

    private short myPrimitiveShort;

    private Short myShort;

    private int myPrimitiveInt;

    private Integer myInteger;

    private long myPrimitiveLong;

    private Long myLong;

    private float myPrimitiveFloat;

    private Float myFloat;

    private double myPrimitiveDouble;

    private Double myDouble;

    private String myString;

    private boolean myPrimitiveBoolean;

    private Boolean myBoolean;

    private Date myDate;

    @Text
    private String myStringText;

    private com.google.appengine.api.datastore.Text myText;

    private byte[] myBytes;

    @Blob
    private byte[] myBytesBlob;

    private MySerializable mySerializable;

    @Blob
    private MySerializable mySerializableBlob;

    private com.google.appengine.api.datastore.Blob myBlob;

    private ShortBlob myShortBlob;

    private short[] myPrimitiveShortArray;

    private Short[] myShortArray;

    private List<Short> myShortList;

    private ArrayList<Short> myShortArrayList;

    private Set<Short> myShortSet;

    private HashSet<Short> myShortHashSet;

    private SortedSet<Short> myShortSortedSet;

    private TreeSet<Short> myShortTreeSet;

    private LinkedList<Short> myShortLinkedList;

    private LinkedHashSet<Short> myShortLinkedHashSet;

    private Stack<Short> myShortStack;

    private Vector<Short> myShortVector;

    private int[] myPrimitiveIntArray;

    private Integer[] myIntegerArray;

    private List<Integer> myIntegerList;

    private ArrayList<Integer> myIntegerArrayList;

    private Set<Integer> myIntegerSet;

    private HashSet<Integer> myIntegerHashSet;

    private SortedSet<Integer> myIntegerSortedSet;

    private TreeSet<Integer> myIntegerTreeSet;

    private LinkedList<Integer> myIntegerLinkedList;

    private LinkedHashSet<Integer> myIntegerLinkedHashSet;

    private Stack<Integer> myIntegerStack;

    private Vector<Integer> myIntegerVector;

    private long[] myPrimitiveLongArray;

    private Long[] myLongArray;

    private List<Long> myLongList;

    private ArrayList<Long> myLongArrayList;

    private Set<Long> myLongSet;

    private HashSet<Long> myLongHashSet;

    private SortedSet<Long> myLongSortedSet;

    private TreeSet<Long> myLongTreeSet;

    private LinkedList<Long> myLongLinkedList;

    private LinkedHashSet<Long> myLongLinkedHashSet;

    private Stack<Long> myLongStack;

    private Vector<Long> myLongVector;

    @Version
    private Long version;

    /**
     * @return the key
     */
    public Key getKey() {
        return key;
    }

    /**
     * @param key
     *            the key to set
     */
    public void setKey(Key key) {
        this.key = key;
    }

    /**
     * @return the myShort
     */
    public short getMyPrimitiveShort() {
        return myPrimitiveShort;
    }

    /**
     * @param myShort
     *            the myShort to set
     */
    public void setMyPrimitiveShort(short myShort) {
        this.myPrimitiveShort = myShort;
    }

    /**
     * @return the myShortWrapper
     */
    public Short getMyShort() {
        return myShort;
    }

    /**
     * @param myShortWrapper
     *            the myShortWrapper to set
     */
    public void setMyShort(Short myShortWrapper) {
        this.myShort = myShortWrapper;
    }

    /**
     * @return the myInt
     */
    public int getMyPrimitiveInt() {
        return myPrimitiveInt;
    }

    /**
     * @param myInt
     *            the myInt to set
     */
    public void setMyPrimitiveInt(int myInt) {
        this.myPrimitiveInt = myInt;
    }

    /**
     * @return the myIntWrapper
     */
    public Integer getMyInteger() {
        return myInteger;
    }

    /**
     * @param myIntWrapper
     *            the myIntWrapper to set
     */
    public void setMyInteger(Integer myIntWrapper) {
        this.myInteger = myIntWrapper;
    }

    /**
     * @return the myLong
     */
    public long getMyPrimitiveLong() {
        return myPrimitiveLong;
    }

    /**
     * @param myLong
     *            the myLong to set
     */
    public void setMyPrimitiveLong(long myLong) {
        this.myPrimitiveLong = myLong;
    }

    /**
     * @return the myLongWrapper
     */
    public Long getMyLong() {
        return myLong;
    }

    /**
     * @param myLongWrapper
     *            the myLongWrapper to set
     */
    public void setMyLong(Long myLongWrapper) {
        this.myLong = myLongWrapper;
    }

    /**
     * @return the myFloat
     */
    public float getMyPrimitiveFloat() {
        return myPrimitiveFloat;
    }

    /**
     * @param myFloat
     *            the myFloat to set
     */
    public void setMyPrimitiveFloat(float myFloat) {
        this.myPrimitiveFloat = myFloat;
    }

    /**
     * @return the myFloatWrapper
     */
    public Float getMyFloat() {
        return myFloat;
    }

    /**
     * @param myFloatWrapper
     *            the myFloatWrapper to set
     */
    public void setMyFloat(Float myFloatWrapper) {
        this.myFloat = myFloatWrapper;
    }

    /**
     * @return the myDouble
     */
    public double getMyPrimitiveDouble() {
        return myPrimitiveDouble;
    }

    /**
     * @param myDouble
     *            the myDouble to set
     */
    public void setMyPrimitiveDouble(double myDouble) {
        this.myPrimitiveDouble = myDouble;
    }

    /**
     * @return the myDoubleWrapper
     */
    public Double getMyDouble() {
        return myDouble;
    }

    /**
     * @param myDoubleWrapper
     *            the myDoubleWrapper to set
     */
    public void setMyDouble(Double myDoubleWrapper) {
        this.myDouble = myDoubleWrapper;
    }

    /**
     * @return the myString
     */
    public String getMyString() {
        return myString;
    }

    /**
     * @param myString
     *            the myString to set
     */
    public void setMyString(String myString) {
        this.myString = myString;
    }

    /**
     * @return the myBoolean
     */
    public boolean isMyPrimitiveBoolean() {
        return myPrimitiveBoolean;
    }

    /**
     * @param myBoolean
     *            the myBoolean to set
     */
    public void setMyPrimitiveBoolean(boolean myBoolean) {
        this.myPrimitiveBoolean = myBoolean;
    }

    /**
     * @return the myBooleanWrapper
     */
    public Boolean getMyBoolean() {
        return myBoolean;
    }

    /**
     * @param myBooleanWrapper
     *            the myBooleanWrapper to set
     */
    public void setMyBoolean(Boolean myBooleanWrapper) {
        this.myBoolean = myBooleanWrapper;
    }

    /**
     * @return the myDate
     */
    public Date getMyDate() {
        return myDate;
    }

    /**
     * @param myDate
     *            the myDate to set
     */
    public void setMyDate(Date myDate) {
        this.myDate = myDate;
    }

    /**
     * @return the myText
     */
    public String getMyStringText() {
        return myStringText;
    }

    /**
     * @param myText
     *            the myText to set
     */
    public void setMyStringText(String myText) {
        this.myStringText = myText;
    }

    /**
     * @return the myText
     */
    public com.google.appengine.api.datastore.Text getMyText() {
        return myText;
    }

    /**
     * @param myText
     *            the myText to set
     */
    public void setMyText(com.google.appengine.api.datastore.Text myText) {
        this.myText = myText;
    }

    /**
     * @return the myBytes
     */
    public byte[] getMyBytes() {
        return myBytes;
    }

    /**
     * @param myBytes
     *            the myBytes to set
     */
    public void setMyBytes(byte[] myBytes) {
        this.myBytes = myBytes;
    }

    /**
     * @return the myBytesBlob
     */
    public byte[] getMyBytesBlob() {
        return myBytesBlob;
    }

    /**
     * @param myBytesBlob
     *            the myBytesBlob to set
     */
    public void setMyBytesBlob(byte[] myBytesBlob) {
        this.myBytesBlob = myBytesBlob;
    }

    /**
     * @return the mySerializable
     */
    public MySerializable getMySerializable() {
        return mySerializable;
    }

    /**
     * @param mySerializable
     *            the mySerializable to set
     */
    public void setMySerializable(MySerializable mySerializable) {
        this.mySerializable = mySerializable;
    }

    /**
     * @return the mySerializableBlob
     */
    public MySerializable getMySerializableBlob() {
        return mySerializableBlob;
    }

    /**
     * @param mySerializableBlob
     *            the mySerializableBlob to set
     */
    public void setMySerializableBlob(MySerializable mySerializableBlob) {
        this.mySerializableBlob = mySerializableBlob;
    }

    /**
     * @return the myBlob
     */
    public com.google.appengine.api.datastore.Blob getMyBlob() {
        return myBlob;
    }

    /**
     * @param myBlob
     *            the myBlob to set
     */
    public void setMyBlob(com.google.appengine.api.datastore.Blob myBlob) {
        this.myBlob = myBlob;
    }

    /**
     * @return the myShortBlob
     */
    public ShortBlob getMyShortBlob() {
        return myShortBlob;
    }

    /**
     * @param myShortBlob
     *            the myShortBlob to set
     */
    public void setMyShortBlob(ShortBlob myShortBlob) {
        this.myShortBlob = myShortBlob;
    }

    /**
     * @return the myShortArray
     */
    public short[] getMyPrimitiveShortArray() {
        return myPrimitiveShortArray;
    }

    /**
     * @param myShortArray
     *            the myShortArray to set
     */
    public void setMyPrimitiveShortArray(short[] myShortArray) {
        this.myPrimitiveShortArray = myShortArray;
    }

    /**
     * @return the myShortArray
     */
    public Short[] getMyShortArray() {
        return myShortArray;
    }

    /**
     * @param myShortArray
     *            the myShortArray to set
     */
    public void setMyShortArray(Short[] myShortArray) {
        this.myShortArray = myShortArray;
    }

    /**
     * @return the myShortList
     */
    public List<Short> getMyShortList() {
        return myShortList;
    }

    /**
     * @param myShortList
     *            the myShortList to set
     */
    public void setMyShortList(List<Short> myShortList) {
        this.myShortList = myShortList;
    }

    /**
     * @return the myShortArrayList
     */
    public ArrayList<Short> getMyShortArrayList() {
        return myShortArrayList;
    }

    /**
     * @param myShortArrayList
     *            the myShortArrayList to set
     */
    public void setMyShortArrayList(ArrayList<Short> myShortArrayList) {
        this.myShortArrayList = myShortArrayList;
    }

    /**
     * @return the myShortSet
     */
    public Set<Short> getMyShortSet() {
        return myShortSet;
    }

    /**
     * @param myShortSet
     *            the myShortSet to set
     */
    public void setMyShortSet(Set<Short> myShortSet) {
        this.myShortSet = myShortSet;
    }

    /**
     * @return the myShortHashSet
     */
    public HashSet<Short> getMyShortHashSet() {
        return myShortHashSet;
    }

    /**
     * @param myShortHashSet
     *            the myShortHashSet to set
     */
    public void setMyShortHashSet(HashSet<Short> myShortHashSet) {
        this.myShortHashSet = myShortHashSet;
    }

    /**
     * @return the myShortSortedSet
     */
    public SortedSet<Short> getMyShortSortedSet() {
        return myShortSortedSet;
    }

    /**
     * @param myShortSortedSet
     *            the myShortSortedSet to set
     */
    public void setMyShortSortedSet(SortedSet<Short> myShortSortedSet) {
        this.myShortSortedSet = myShortSortedSet;
    }

    /**
     * @return the myShortTreeSet
     */
    public TreeSet<Short> getMyShortTreeSet() {
        return myShortTreeSet;
    }

    /**
     * @param myShortTreeSet
     *            the myShortTreeSet to set
     */
    public void setMyShortTreeSet(TreeSet<Short> myShortTreeSet) {
        this.myShortTreeSet = myShortTreeSet;
    }

    /**
     * @return the myShortLinkedList
     */
    public LinkedList<Short> getMyShortLinkedList() {
        return myShortLinkedList;
    }

    /**
     * @param myShortLinkedList
     *            the myShortLinkedList to set
     */
    public void setMyShortLinkedList(LinkedList<Short> myShortLinkedList) {
        this.myShortLinkedList = myShortLinkedList;
    }

    /**
     * @return the myShortLinkedHashSet
     */
    public LinkedHashSet<Short> getMyShortLinkedHashSet() {
        return myShortLinkedHashSet;
    }

    /**
     * @param myShortLinkedHashSet
     *            the myShortLinkedHashSet to set
     */
    public void setMyShortLinkedHashSet(
            LinkedHashSet<Short> myShortLinkedHashSet) {
        this.myShortLinkedHashSet = myShortLinkedHashSet;
    }

    /**
     * @return the myShortStack
     */
    public Stack<Short> getMyShortStack() {
        return myShortStack;
    }

    /**
     * @param myShortStack
     *            the myShortStack to set
     */
    public void setMyShortStack(Stack<Short> myShortStack) {
        this.myShortStack = myShortStack;
    }

    /**
     * @return the myShortVector
     */
    public Vector<Short> getMyShortVector() {
        return myShortVector;
    }

    /**
     * @param myShortVector
     *            the myShortVector to set
     */
    public void setMyShortVector(Vector<Short> myShortVector) {
        this.myShortVector = myShortVector;
    }

    /**
     * @return the myPrimitiveIntArray
     */
    public int[] getMyPrimitiveIntArray() {
        return myPrimitiveIntArray;
    }

    /**
     * @param myPrimitiveIntArray
     *            the myPrimitiveIntArray to set
     */
    public void setMyPrimitiveIntArray(int[] myPrimitiveIntArray) {
        this.myPrimitiveIntArray = myPrimitiveIntArray;
    }

    /**
     * @return the myIntegerArray
     */
    public Integer[] getMyIntegerArray() {
        return myIntegerArray;
    }

    /**
     * @param myIntegerArray
     *            the myIntegerArray to set
     */
    public void setMyIntegerArray(Integer[] myIntegerArray) {
        this.myIntegerArray = myIntegerArray;
    }

    /**
     * @return the myIntegerList
     */
    public List<Integer> getMyIntegerList() {
        return myIntegerList;
    }

    /**
     * @param myIntegerList
     *            the myIntegerList to set
     */
    public void setMyIntegerList(List<Integer> myIntegerList) {
        this.myIntegerList = myIntegerList;
    }

    /**
     * @return the myIntegerArrayList
     */
    public ArrayList<Integer> getMyIntegerArrayList() {
        return myIntegerArrayList;
    }

    /**
     * @param myIntegerArrayList
     *            the myIntegerArrayList to set
     */
    public void setMyIntegerArrayList(ArrayList<Integer> myIntegerArrayList) {
        this.myIntegerArrayList = myIntegerArrayList;
    }

    /**
     * @return the myIntegerSet
     */
    public Set<Integer> getMyIntegerSet() {
        return myIntegerSet;
    }

    /**
     * @param myIntegerSet
     *            the myIntegerSet to set
     */
    public void setMyIntegerSet(Set<Integer> myIntegerSet) {
        this.myIntegerSet = myIntegerSet;
    }

    /**
     * @return the myIntegerHashSet
     */
    public HashSet<Integer> getMyIntegerHashSet() {
        return myIntegerHashSet;
    }

    /**
     * @param myIntegerHashSet
     *            the myIntegerHashSet to set
     */
    public void setMyIntegerHashSet(HashSet<Integer> myIntegerHashSet) {
        this.myIntegerHashSet = myIntegerHashSet;
    }

    /**
     * @return the myIntegerSortedSet
     */
    public SortedSet<Integer> getMyIntegerSortedSet() {
        return myIntegerSortedSet;
    }

    /**
     * @param myIntegerSortedSet
     *            the myIntegerSortedSet to set
     */
    public void setMyIntegerSortedSet(SortedSet<Integer> myIntegerSortedSet) {
        this.myIntegerSortedSet = myIntegerSortedSet;
    }

    /**
     * @return the myIntegerTreeSet
     */
    public TreeSet<Integer> getMyIntegerTreeSet() {
        return myIntegerTreeSet;
    }

    /**
     * @param myIntegerTreeSet
     *            the myIntegerTreeSet to set
     */
    public void setMyIntegerTreeSet(TreeSet<Integer> myIntegerTreeSet) {
        this.myIntegerTreeSet = myIntegerTreeSet;
    }

    /**
     * @return the myIntegerLinkedList
     */
    public LinkedList<Integer> getMyIntegerLinkedList() {
        return myIntegerLinkedList;
    }

    /**
     * @param myIntegerLinkedList
     *            the myIntegerLinkedList to set
     */
    public void setMyIntegerLinkedList(LinkedList<Integer> myIntegerLinkedList) {
        this.myIntegerLinkedList = myIntegerLinkedList;
    }

    /**
     * @return the myIntegerLinkedHashSet
     */
    public LinkedHashSet<Integer> getMyIntegerLinkedHashSet() {
        return myIntegerLinkedHashSet;
    }

    /**
     * @param myIntegerLinkedHashSet
     *            the myIntegerLinkedHashSet to set
     */
    public void setMyIntegerLinkedHashSet(
            LinkedHashSet<Integer> myIntegerLinkedHashSet) {
        this.myIntegerLinkedHashSet = myIntegerLinkedHashSet;
    }

    /**
     * @return the myIntegerStack
     */
    public Stack<Integer> getMyIntegerStack() {
        return myIntegerStack;
    }

    /**
     * @param myIntegerStack
     *            the myIntegerStack to set
     */
    public void setMyIntegerStack(Stack<Integer> myIntegerStack) {
        this.myIntegerStack = myIntegerStack;
    }

    /**
     * @return the myIntegerVector
     */
    public Vector<Integer> getMyIntegerVector() {
        return myIntegerVector;
    }

    /**
     * @param myIntegerVector
     *            the myIntegerVector to set
     */
    public void setMyIntegerVector(Vector<Integer> myIntegerVector) {
        this.myIntegerVector = myIntegerVector;
    }

    /**
     * @return the myPrimitiveLongArray
     */
    public long[] getMyPrimitiveLongArray() {
        return myPrimitiveLongArray;
    }

    /**
     * @param myPrimitiveLongArray
     *            the myPrimitiveLongArray to set
     */
    public void setMyPrimitiveLongArray(long[] myPrimitiveLongArray) {
        this.myPrimitiveLongArray = myPrimitiveLongArray;
    }

    /**
     * @return the myLongArray
     */
    public Long[] getMyLongArray() {
        return myLongArray;
    }

    /**
     * @param myLongArray
     *            the myLongArray to set
     */
    public void setMyLongArray(Long[] myLongArray) {
        this.myLongArray = myLongArray;
    }

    /**
     * @return the myLongList
     */
    public List<Long> getMyLongList() {
        return myLongList;
    }

    /**
     * @param myLongList
     *            the myLongList to set
     */
    public void setMyLongList(List<Long> myLongList) {
        this.myLongList = myLongList;
    }

    /**
     * @return the myLongArrayList
     */
    public ArrayList<Long> getMyLongArrayList() {
        return myLongArrayList;
    }

    /**
     * @param myLongArrayList
     *            the myLongArrayList to set
     */
    public void setMyLongArrayList(ArrayList<Long> myLongArrayList) {
        this.myLongArrayList = myLongArrayList;
    }

    /**
     * @return the myLongSet
     */
    public Set<Long> getMyLongSet() {
        return myLongSet;
    }

    /**
     * @param myLongSet
     *            the myLongSet to set
     */
    public void setMyLongSet(Set<Long> myLongSet) {
        this.myLongSet = myLongSet;
    }

    /**
     * @return the myLongHashSet
     */
    public HashSet<Long> getMyLongHashSet() {
        return myLongHashSet;
    }

    /**
     * @param myLongHashSet
     *            the myLongHashSet to set
     */
    public void setMyLongHashSet(HashSet<Long> myLongHashSet) {
        this.myLongHashSet = myLongHashSet;
    }

    /**
     * @return the myLongSortedSet
     */
    public SortedSet<Long> getMyLongSortedSet() {
        return myLongSortedSet;
    }

    /**
     * @param myLongSortedSet
     *            the myLongSortedSet to set
     */
    public void setMyLongSortedSet(SortedSet<Long> myLongSortedSet) {
        this.myLongSortedSet = myLongSortedSet;
    }

    /**
     * @return the myLongTreeSet
     */
    public TreeSet<Long> getMyLongTreeSet() {
        return myLongTreeSet;
    }

    /**
     * @param myLongTreeSet
     *            the myLongTreeSet to set
     */
    public void setMyLongTreeSet(TreeSet<Long> myLongTreeSet) {
        this.myLongTreeSet = myLongTreeSet;
    }

    /**
     * @return the myLongLinkedList
     */
    public LinkedList<Long> getMyLongLinkedList() {
        return myLongLinkedList;
    }

    /**
     * @param myLongLinkedList
     *            the myLongLinkedList to set
     */
    public void setMyLongLinkedList(LinkedList<Long> myLongLinkedList) {
        this.myLongLinkedList = myLongLinkedList;
    }

    /**
     * @return the myLongLinkedHashSet
     */
    public LinkedHashSet<Long> getMyLongLinkedHashSet() {
        return myLongLinkedHashSet;
    }

    /**
     * @param myLongLinkedHashSet
     *            the myLongLinkedHashSet to set
     */
    public void setMyLongLinkedHashSet(LinkedHashSet<Long> myLongLinkedHashSet) {
        this.myLongLinkedHashSet = myLongLinkedHashSet;
    }

    /**
     * @return the myLongStack
     */
    public Stack<Long> getMyLongStack() {
        return myLongStack;
    }

    /**
     * @param myLongStack
     *            the myLongStack to set
     */
    public void setMyLongStack(Stack<Long> myLongStack) {
        this.myLongStack = myLongStack;
    }

    /**
     * @return the myLongVector
     */
    public Vector<Long> getMyLongVector() {
        return myLongVector;
    }

    /**
     * @param myLongVector
     *            the myLongVector to set
     */
    public void setMyLongVector(Vector<Long> myLongVector) {
        this.myLongVector = myLongVector;
    }

    /**
     * @return the version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * @param version
     *            the version to set
     */
    public void setVersion(Long version) {
        this.version = version;
    }
}
