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
package org.slim3.datastore.model;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.InverseModelListRef;
import org.slim3.datastore.InverseModelRef;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.ShortBlob;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * @author higa
 * 
 */
@Model
public class Hoge {

    @Attribute(primaryKey = true)
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

    private SortDirection myEnum;

    @Attribute(lob = true)
    private String myStringText;

    private Text myText;

    private byte[] myBytes;

    @Attribute(lob = true)
    private byte[] myBytesBlob;

    @Attribute(lob = true)
    private MySerializable mySerializable;

    @Attribute(lob = true)
    private MySerializable mySerializableBlob;

    private Blob myBlob;

    private ShortBlob myShortBlob;

    private List<Short> myShortList;

    private Set<Short> myShortSet;

    private SortedSet<Short> myShortSortedSet;

    private List<Integer> myIntegerList;

    private Set<Integer> myIntegerSet;

    private SortedSet<Integer> myIntegerSortedSet;

    private List<Long> myLongList;

    private Set<Long> myLongSet;

    private SortedSet<Long> myLongSortedSet;

    private List<Float> myFloatList;

    private Set<Float> myFloatSet;

    private SortedSet<Float> myFloatSortedSet;

    private List<SortDirection> myEnumList;

    private List<String> myStringList;

    @Attribute(cipher = true)
    private String myCipherString;

    @Attribute(cipher = true)
    private Text myCipherText;

    @Attribute(cipher = true, lob = true)
    private String myCipherLobString;
    
    @Attribute(version = true)
    private Long version;

    @Attribute(persistent = false)
    private InverseModelRef<Bbb, Hoge> bbbRef =
        new InverseModelRef<Bbb, Hoge>(Bbb.class, "hogeRef", this);

    @Attribute(persistent = false)
    private InverseModelListRef<Bbb, Hoge> bbbListRef =
        new InverseModelListRef<Bbb, Hoge>(Bbb.class, "hoge2Ref", this);

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
     * @return the myEnum
     */
    public SortDirection getMyEnum() {
        return myEnum;
    }

    /**
     * @param myEnum
     *            the myEnum to set
     */
    public void setMyEnum(SortDirection myEnum) {
        this.myEnum = myEnum;
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
     * @return the myFloatList
     */
    public List<Float> getMyFloatList() {
        return myFloatList;
    }

    /**
     * @param myFloatList
     *            the myFloatList to set
     */
    public void setMyFloatList(List<Float> myFloatList) {
        this.myFloatList = myFloatList;
    }

    /**
     * @return the myFloatSet
     */
    public Set<Float> getMyFloatSet() {
        return myFloatSet;
    }

    /**
     * @param myFloatSet
     *            the myFloatSet to set
     */
    public void setMyFloatSet(Set<Float> myFloatSet) {
        this.myFloatSet = myFloatSet;
    }

    /**
     * @return the myFloatSortedSet
     */
    public SortedSet<Float> getMyFloatSortedSet() {
        return myFloatSortedSet;
    }

    /**
     * @param myFloatSortedSet
     *            the myFloatSortedSet to set
     */
    public void setMyFloatSortedSet(SortedSet<Float> myFloatSortedSet) {
        this.myFloatSortedSet = myFloatSortedSet;
    }

    /**
     * @return the myEnumList
     */
    public List<SortDirection> getMyEnumList() {
        return myEnumList;
    }

    /**
     * @param myEnumList
     *            the myEnumList to set
     */
    public void setMyEnumList(List<SortDirection> myEnumList) {
        this.myEnumList = myEnumList;
    }

    /**
     * @return the myStringList
     */
    public List<String> getMyStringList() {
        return myStringList;
    }

    /**
     * @param myStringList
     *            the myStringList to set
     */
    public void setMyStringList(List<String> myStringList) {
        this.myStringList = myStringList;
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

    /**
     * @return the bbbRef
     */
    public InverseModelRef<Bbb, Hoge> getBbbRef() {
        return bbbRef;
    }

    /**
     * @return the bbbListRef
     */
    public InverseModelListRef<Bbb, Hoge> getBbbListRef() {
        return bbbListRef;
    }

    /**
     * @return the myCipherString
     */
    public String getMyCipherString() {
        return myCipherString;
    }

    /**
     * @param myCipherString
     *            the myCipherString to set
     */
    public void setMyCipherString(String myCipherString) {
        this.myCipherString = myCipherString;
    }

    /**
     * @return the myCipherText
     */
    public Text getMyCipherText() {
        return myCipherText;
    }

    /**
     * @param myCipherText
     *            the myCipherText to set
     */
    public void setMyCipherText(Text myCipherText) {
        this.myCipherText = myCipherText;
    }

    /**
     * @return the myCipherLobString
     */
    public String getMyCipherLobString() {
        return myCipherLobString;
    }

    /**
     * @param myCipherLobString
     *            the myCipherLobString to set
     */
    public void setMyCipherLobString(String myCipherLobString) {
        this.myCipherLobString = myCipherLobString;
    }
}
