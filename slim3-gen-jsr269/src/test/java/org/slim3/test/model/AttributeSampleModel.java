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
package org.slim3.test.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Category;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.PhoneNumber;
import com.google.appengine.api.datastore.PostalAddress;
import com.google.appengine.api.datastore.Rating;
import com.google.appengine.api.datastore.ShortBlob;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;

/**
 * @author vvakame
 */
@Model
public class AttributeSampleModel {
    @Attribute(primaryKey = true)
    Key primaryKey;

    boolean booleanPrimitive;
    Boolean booleanWrapper;
    short shortPrimitive;
    Short shortWrapper;
    int intPrimitive;
    Integer intWrapper;
    long longPrimitive;
    Long longWrapper;
    float floatPrimitive;
    Float floatWrapper;
    double doublePrimitive;
    Double doubleWrapper;

    EnumSample enumSample;

    String str;
    List<String> strList;
    ArrayList<String> strArrayList;
    LinkedList<String> strLinkedList;
    Set<String> strSet;
    HashSet<String> strHashSet;
    LinkedHashSet<String> strLinkedHashSet;
    SortedSet<String> strSortedSet;
    TreeSet<String> strTreeSet;

    Key key;
    Text text;
    User user;
    ShortBlob shortBlob;
    Blob blob;
    BlobKey blobKey;
    Email email;
    Category category;
    GeoPt geoPt;
    Link link;
    PhoneNumber phoneNumber;
    PostalAddress postalAddress;
    Rating rating;

    /** enum for sample */
    public static enum EnumSample {
        /** A */
        A,
        /** B */
        B,
    }

    /**
     * @return the primaryKey
     */
    public Key getPrimaryKey() {
        return primaryKey;
    }

    /**
     * @param primaryKey
     *            the primaryKey to set
     */
    public void setPrimaryKey(Key primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * @return the booleanPrimitive
     */
    public boolean isBooleanPrimitive() {
        return booleanPrimitive;
    }

    /**
     * @param booleanPrimitive
     *            the booleanPrimitive to set
     */
    public void setBooleanPrimitive(boolean booleanPrimitive) {
        this.booleanPrimitive = booleanPrimitive;
    }

    /**
     * @return the booleanWrapper
     */
    public Boolean getBooleanWrapper() {
        return booleanWrapper;
    }

    /**
     * @param booleanWrapper
     *            the booleanWrapper to set
     */
    public void setBooleanWrapper(Boolean booleanWrapper) {
        this.booleanWrapper = booleanWrapper;
    }

    /**
     * @return the shortPrimitive
     */
    public short getShortPrimitive() {
        return shortPrimitive;
    }

    /**
     * @param shortPrimitive
     *            the shortPrimitive to set
     */
    public void setShortPrimitive(short shortPrimitive) {
        this.shortPrimitive = shortPrimitive;
    }

    /**
     * @return the shortWrapper
     */
    public Short getShortWrapper() {
        return shortWrapper;
    }

    /**
     * @param shortWrapper
     *            the shortWrapper to set
     */
    public void setShortWrapper(Short shortWrapper) {
        this.shortWrapper = shortWrapper;
    }

    /**
     * @return the intPrimitive
     */
    public int getIntPrimitive() {
        return intPrimitive;
    }

    /**
     * @param intPrimitive
     *            the intPrimitive to set
     */
    public void setIntPrimitive(int intPrimitive) {
        this.intPrimitive = intPrimitive;
    }

    /**
     * @return the intWrapper
     */
    public Integer getIntWrapper() {
        return intWrapper;
    }

    /**
     * @param intWrapper
     *            the intWrapper to set
     */
    public void setIntWrapper(Integer intWrapper) {
        this.intWrapper = intWrapper;
    }

    /**
     * @return the longPrimitive
     */
    public long getLongPrimitive() {
        return longPrimitive;
    }

    /**
     * @param longPrimitive
     *            the longPrimitive to set
     */
    public void setLongPrimitive(long longPrimitive) {
        this.longPrimitive = longPrimitive;
    }

    /**
     * @return the longWrapper
     */
    public Long getLongWrapper() {
        return longWrapper;
    }

    /**
     * @param longWrapper
     *            the longWrapper to set
     */
    public void setLongWrapper(Long longWrapper) {
        this.longWrapper = longWrapper;
    }

    /**
     * @return the floatPrimitive
     */
    public float getFloatPrimitive() {
        return floatPrimitive;
    }

    /**
     * @param floatPrimitive
     *            the floatPrimitive to set
     */
    public void setFloatPrimitive(float floatPrimitive) {
        this.floatPrimitive = floatPrimitive;
    }

    /**
     * @return the floatWrapper
     */
    public Float getFloatWrapper() {
        return floatWrapper;
    }

    /**
     * @param floatWrapper
     *            the floatWrapper to set
     */
    public void setFloatWrapper(Float floatWrapper) {
        this.floatWrapper = floatWrapper;
    }

    /**
     * @return the doublePrimitive
     */
    public double getDoublePrimitive() {
        return doublePrimitive;
    }

    /**
     * @param doublePrimitive
     *            the doublePrimitive to set
     */
    public void setDoublePrimitive(double doublePrimitive) {
        this.doublePrimitive = doublePrimitive;
    }

    /**
     * @return the doubleWrapper
     */
    public Double getDoubleWrapper() {
        return doubleWrapper;
    }

    /**
     * @param doubleWrapper
     *            the doubleWrapper to set
     */
    public void setDoubleWrapper(Double doubleWrapper) {
        this.doubleWrapper = doubleWrapper;
    }

    /**
     * @return the enumSample
     */
    public EnumSample getEnumSample() {
        return enumSample;
    }

    /**
     * @param enumSample
     *            the enumSample to set
     */
    public void setEnumSample(EnumSample enumSample) {
        this.enumSample = enumSample;
    }

    /**
     * @return the str
     */
    public String getStr() {
        return str;
    }

    /**
     * @param str
     *            the str to set
     */
    public void setStr(String str) {
        this.str = str;
    }

    /**
     * @return the strList
     */
    public List<String> getStrList() {
        return strList;
    }

    /**
     * @param strList
     *            the strList to set
     */
    public void setStrList(List<String> strList) {
        this.strList = strList;
    }

    /**
     * @return the strArrayList
     */
    public ArrayList<String> getStrArrayList() {
        return strArrayList;
    }

    /**
     * @param strArrayList
     *            the strArrayList to set
     */
    public void setStrArrayList(ArrayList<String> strArrayList) {
        this.strArrayList = strArrayList;
    }

    /**
     * @return the strLinkedList
     */
    public LinkedList<String> getStrLinkedList() {
        return strLinkedList;
    }

    /**
     * @param strLinkedList
     *            the strLinkedList to set
     */
    public void setStrLinkedList(LinkedList<String> strLinkedList) {
        this.strLinkedList = strLinkedList;
    }

    /**
     * @return the strSet
     */
    public Set<String> getStrSet() {
        return strSet;
    }

    /**
     * @param strSet
     *            the strSet to set
     */
    public void setStrSet(Set<String> strSet) {
        this.strSet = strSet;
    }

    /**
     * @return the strHashSet
     */
    public HashSet<String> getStrHashSet() {
        return strHashSet;
    }

    /**
     * @param strHashSet
     *            the strHashSet to set
     */
    public void setStrHashSet(HashSet<String> strHashSet) {
        this.strHashSet = strHashSet;
    }

    /**
     * @return the strLinkedHashSet
     */
    public LinkedHashSet<String> getStrLinkedHashSet() {
        return strLinkedHashSet;
    }

    /**
     * @param strLinkedHashSet
     *            the strLinkedHashSet to set
     */
    public void setStrLinkedHashSet(LinkedHashSet<String> strLinkedHashSet) {
        this.strLinkedHashSet = strLinkedHashSet;
    }

    /**
     * @return the strSortedSet
     */
    public SortedSet<String> getStrSortedSet() {
        return strSortedSet;
    }

    /**
     * @param strSortedSet
     *            the strSortedSet to set
     */
    public void setStrSortedSet(SortedSet<String> strSortedSet) {
        this.strSortedSet = strSortedSet;
    }

    /**
     * @return the strTreeSet
     */
    public TreeSet<String> getStrTreeSet() {
        return strTreeSet;
    }

    /**
     * @param strTreeSet
     *            the strTreeSet to set
     */
    public void setStrTreeSet(TreeSet<String> strTreeSet) {
        this.strTreeSet = strTreeSet;
    }

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
     * @return the text
     */
    public Text getText() {
        return text;
    }

    /**
     * @param text
     *            the text to set
     */
    public void setText(Text text) {
        this.text = text;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the shortBlob
     */
    public ShortBlob getShortBlob() {
        return shortBlob;
    }

    /**
     * @param shortBlob
     *            the shortBlob to set
     */
    public void setShortBlob(ShortBlob shortBlob) {
        this.shortBlob = shortBlob;
    }

    /**
     * @return the blob
     */
    public Blob getBlob() {
        return blob;
    }

    /**
     * @param blob
     *            the blob to set
     */
    public void setBlob(Blob blob) {
        this.blob = blob;
    }

    /**
     * @return the blobKey
     */
    public BlobKey getBlobKey() {
        return blobKey;
    }

    /**
     * @param blobKey
     *            the blobKey to set
     */
    public void setBlobKey(BlobKey blobKey) {
        this.blobKey = blobKey;
    }

    /**
     * @return the email
     */
    public Email getEmail() {
        return email;
    }

    /**
     * @param email
     *            the email to set
     */
    public void setEmail(Email email) {
        this.email = email;
    }

    /**
     * @return the category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @param category
     *            the category to set
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * @return the geoPt
     */
    public GeoPt getGeoPt() {
        return geoPt;
    }

    /**
     * @param geoPt
     *            the geoPt to set
     */
    public void setGeoPt(GeoPt geoPt) {
        this.geoPt = geoPt;
    }

    /**
     * @return the link
     */
    public Link getLink() {
        return link;
    }

    /**
     * @param link
     *            the link to set
     */
    public void setLink(Link link) {
        this.link = link;
    }

    /**
     * @return the phoneNumber
     */
    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber
     *            the phoneNumber to set
     */
    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the postalAddress
     */
    public PostalAddress getPostalAddress() {
        return postalAddress;
    }

    /**
     * @param postalAddress
     *            the postalAddress to set
     */
    public void setPostalAddress(PostalAddress postalAddress) {
        this.postalAddress = postalAddress;
    }

    /**
     * @return the rating
     */
    public Rating getRating() {
        return rating;
    }

    /**
     * @param rating
     *            the rating to set
     */
    public void setRating(Rating rating) {
        this.rating = rating;
    }
}
