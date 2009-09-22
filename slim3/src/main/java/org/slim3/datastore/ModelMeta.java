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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

import org.slim3.util.ByteUtil;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.ShortBlob;
import com.google.appengine.api.datastore.Text;

/**
 * A meta data of model.
 * 
 * @author higa
 * @param <T>
 *            the model type
 * @since 3.0
 * 
 */
public abstract class ModelMeta<T> {

    /**
     * The model class.
     */
    protected Class<T> modelClass;

    /**
     * Constructor.
     * 
     * @param modelClass
     *            the model class
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public ModelMeta(Class<T> modelClass) throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        this.modelClass = modelClass;
    }

    /**
     * Returns the model class.
     * 
     * @return the model class
     */
    public Class<T> getModelClass() {
        return modelClass;
    }

    /**
     * Converts the entity to a model.
     * 
     * @param entity
     *            the entity
     * @return a model
     */
    public abstract T entityToModel(Entity entity);

    /**
     * Converts the model to an entity.
     * 
     * @param model
     *            the model
     * @return an entity
     */
    public abstract Entity modelToEntity(T model);

    /**
     * Converts the long to a primitive short.
     * 
     * @param value
     *            the long
     * @return a primitive short
     */
    protected short longToPrimitiveShort(Long value) {
        return value != null ? value.shortValue() : 0;
    }

    /**
     * Converts the long to a short.
     * 
     * @param value
     *            the long
     * @return a short
     */
    protected Short longToShort(Long value) {
        return value != null ? value.shortValue() : null;
    }

    /**
     * Converts the long to a primitive int.
     * 
     * @param value
     *            the long
     * @return a primitive int
     */
    protected int longToPrimitiveInt(Long value) {
        return value != null ? value.intValue() : 0;
    }

    /**
     * Converts the long to an integer.
     * 
     * @param value
     *            the long
     * @return an integer
     */
    protected Integer longToInteger(Long value) {
        return value != null ? value.intValue() : null;
    }

    /**
     * Converts the long to a primitive long.
     * 
     * @param value
     *            the long
     * @return a primitive long
     */
    protected long longToPrimitiveLong(Long value) {
        return value != null ? value : 0;
    }

    /**
     * Converts the double to a primitive float.
     * 
     * @param value
     *            the double
     * @return a primitive float
     */
    protected float doubleToPrimitiveFloat(Double value) {
        return value != null ? value.floatValue() : 0;
    }

    /**
     * Converts the double to a float.
     * 
     * @param value
     *            the double
     * @return a float
     */
    protected Float doubleToFloat(Double value) {
        return value != null ? value.floatValue() : null;
    }

    /**
     * Converts the double to a primitive double.
     * 
     * @param value
     *            the double
     * @return a primitive double
     */
    protected double doubleToPrimitiveDouble(Double value) {
        return value != null ? value : 0;
    }

    /**
     * Converts the boolean to a primitive boolean.
     * 
     * @param value
     *            the boolean
     * @return a primitive boolean
     */
    protected boolean booleanToPrimitiveBoolean(Boolean value) {
        return value != null ? value : false;
    }

    /**
     * Converts the text to a string
     * 
     * @param value
     *            the text
     * @return a string
     */
    protected String textToString(Text value) {
        return value != null ? value.getValue() : null;
    }

    /**
     * Converts the string to a text
     * 
     * @param value
     *            the string
     * @return a text
     */
    protected Text stringToText(String value) {
        return value != null ? new Text(value) : null;
    }

    /**
     * Converts the short blob to an array of bytes.
     * 
     * @param value
     *            the short blob
     * @return an array of bytes
     */
    protected byte[] shortBlobToBytes(ShortBlob value) {
        return value != null ? value.getBytes() : null;
    }

    /**
     * Converts the array of bytes to a short blob.
     * 
     * @param value
     *            the array of bytes
     * @return a short blob
     */
    protected ShortBlob bytesToShortBlob(byte[] value) {
        return value != null ? new ShortBlob(value) : null;
    }

    /**
     * Converts the blob to an array of bytes.
     * 
     * @param value
     *            the blob
     * @return an array of bytes
     */
    protected byte[] blobToBytes(Blob value) {
        return value != null ? value.getBytes() : null;
    }

    /**
     * Converts the array of bytes to a blob.
     * 
     * @param value
     *            the array of bytes
     * @return a blob
     */
    protected Blob bytesToBlob(byte[] value) {
        return value != null ? new Blob(value) : null;
    }

    /**
     * Converts the short blob to a serializable object.
     * 
     * @param value
     *            the short blob
     * @return a serializable object
     */
    protected Serializable shortBlobToSerializable(ShortBlob value) {
        return value != null ? (Serializable) ByteUtil.toObject(value
            .getBytes()) : null;
    }

    /**
     * Converts the serializable object to a short blob.
     * 
     * @param value
     *            the serializable object
     * @return a short blob
     */
    protected ShortBlob serializableToShortBlob(Serializable value) {
        return value != null
            ? new ShortBlob(ByteUtil.toByteArray(value))
            : null;
    }

    /**
     * Converts the blob to a serializable object.
     * 
     * @param value
     *            the blob
     * @return a serializable object
     */
    protected Serializable blobToSerializable(Blob value) {
        return value != null ? (Serializable) ByteUtil.toObject(value
            .getBytes()) : null;
    }

    /**
     * Converts the serializable object to a blob.
     * 
     * @param value
     *            the serializable object
     * @return a blob
     */
    protected Blob serializableToBlob(Serializable value) {
        return value != null ? new Blob(ByteUtil.toByteArray(value)) : null;
    }

    /**
     * Converts the list of long to an array of primitive short.
     * 
     * @param value
     *            the list of long
     * @return an array of primitive short
     */
    protected short[] longListToPrimitiveShortArray(List<Long> value) {
        if (value == null) {
            return null;
        }
        short[] ret = new short[value.size()];
        int size = value.size();
        for (int i = 0; i < size; i++) {
            Long l = value.get(i);
            ret[i] = l != null ? l.shortValue() : 0;
        }
        return ret;
    }

    /**
     * Converts the array of primitive short to a list of long.
     * 
     * @param value
     *            the array of primitive short
     * @return a list of long
     */
    protected List<Long> primitiveShortArrayToLongList(short[] value) {
        if (value == null) {
            return null;
        }
        List<Long> ret = new ArrayList<Long>(value.length);
        int size = value.length;
        for (int i = 0; i < size; i++) {
            ret.add(Long.valueOf(value[i]));
        }
        return ret;
    }

    /**
     * Converts the list of long to an array of short.
     * 
     * @param value
     *            the list of long
     * @return an array of short
     */
    protected Short[] longListToShortArray(List<Long> value) {
        if (value == null) {
            return null;
        }
        Short[] ret = new Short[value.size()];
        int size = value.size();
        for (int i = 0; i < size; i++) {
            Long l = value.get(i);
            ret[i] = l != null ? l.shortValue() : 0;
        }
        return ret;
    }

    /**
     * Converts the array of short to a list of long.
     * 
     * @param value
     *            the array of short
     * @return a list of long
     */
    protected List<Long> shortArrayToLongList(Short[] value) {
        if (value == null) {
            return null;
        }
        List<Long> ret = new ArrayList<Long>(value.length);
        int size = value.length;
        for (int i = 0; i < size; i++) {
            ret.add(value[i].longValue());
        }
        return ret;
    }

    /**
     * Copies the list of long to the collection of short.
     * 
     * @param value
     *            the list of long
     * @param collection
     *            the collection of short
     */
    protected void copyLongListToShortCollection(List<Long> value,
            Collection<Short> collection) {
        int size = value.size();
        for (int i = 0; i < size; i++) {
            Long l = value.get(i);
            collection.add(l != null ? l.shortValue() : null);
        }
    }

    /**
     * Converts the list of long to a list of short.
     * 
     * @param value
     *            the list of long
     * @return a list of short
     */
    protected ArrayList<Short> longListToShortList(List<Long> value) {
        if (value == null) {
            return null;
        }
        ArrayList<Short> collection = new ArrayList<Short>(value.size());
        copyLongListToShortCollection(value, collection);
        return collection;
    }

    /**
     * Converts the list of long to a set of short.
     * 
     * @param value
     *            the list of long
     * @return a set of short
     */
    protected HashSet<Short> longListToShortSet(List<Long> value) {
        if (value == null) {
            return null;
        }
        HashSet<Short> collection = new HashSet<Short>();
        copyLongListToShortCollection(value, collection);
        return collection;
    }

    /**
     * Converts the list of long to a sorted set of short.
     * 
     * @param value
     *            the list of long
     * @return a sorted set of short
     */
    protected TreeSet<Short> longListToShortSortedSet(List<Long> value) {
        if (value == null) {
            return null;
        }
        TreeSet<Short> collection = new TreeSet<Short>();
        copyLongListToShortCollection(value, collection);
        return collection;
    }

    /**
     * Converts the list of long to a linked list of short.
     * 
     * @param value
     *            the list of long
     * @return a linked list of short
     */
    protected LinkedList<Short> longListToShortLinkedList(List<Long> value) {
        if (value == null) {
            return null;
        }
        LinkedList<Short> collection = new LinkedList<Short>();
        copyLongListToShortCollection(value, collection);
        return collection;
    }

    /**
     * Converts the list of long to a linked hash set of short.
     * 
     * @param value
     *            the list of long
     * @return a linked hash set of short
     */
    protected LinkedHashSet<Short> longListToShortLinkedHashSet(List<Long> value) {
        if (value == null) {
            return null;
        }
        LinkedHashSet<Short> collection = new LinkedHashSet<Short>();
        copyLongListToShortCollection(value, collection);
        return collection;
    }

    /**
     * Converts the list of long to a stack of short.
     * 
     * @param value
     *            the list of long
     * @return a stack of short
     */
    protected Stack<Short> longListToShortStack(List<Long> value) {
        if (value == null) {
            return null;
        }
        Stack<Short> collection = new Stack<Short>();
        copyLongListToShortCollection(value, collection);
        return collection;
    }

    /**
     * Converts the list of long to a vector of short.
     * 
     * @param value
     *            the list of long
     * @return a vector of short
     */
    protected Vector<Short> longListToShortVector(List<Long> value) {
        if (value == null) {
            return null;
        }
        Vector<Short> collection = new Vector<Short>(value.size());
        copyLongListToShortCollection(value, collection);
        return collection;
    }

    /**
     * Converts the list of long to an array of primitive int.
     * 
     * @param value
     *            the list of long
     * @return an array of primitive int
     */
    protected int[] longListToPrimitiveIntArray(List<Long> value) {
        if (value == null) {
            return null;
        }
        int[] ret = new int[value.size()];
        int size = value.size();
        for (int i = 0; i < size; i++) {
            Long l = value.get(i);
            ret[i] = l != null ? l.intValue() : 0;
        }
        return ret;
    }

    /**
     * Converts the array of primitive int to a list of long.
     * 
     * @param value
     *            the array of primitive int
     * @return a list of long
     */
    protected List<Long> primitiveIntArrayToLongList(int[] value) {
        if (value == null) {
            return null;
        }
        List<Long> ret = new ArrayList<Long>(value.length);
        int size = value.length;
        for (int i = 0; i < size; i++) {
            ret.add(Long.valueOf(value[i]));
        }
        return ret;
    }

    /**
     * Converts the list of long to an array of integer.
     * 
     * @param value
     *            the list of long
     * @return an array of integer
     */
    protected Integer[] longListToIntegerArray(List<Long> value) {
        if (value == null) {
            return null;
        }
        Integer[] ret = new Integer[value.size()];
        int size = value.size();
        for (int i = 0; i < size; i++) {
            Long l = value.get(i);
            ret[i] = l != null ? l.intValue() : 0;
        }
        return ret;
    }

    /**
     * Converts the array of integer to a list of long.
     * 
     * @param value
     *            the array of integer
     * @return a list of long
     */
    protected List<Long> integerArrayToLongList(Integer[] value) {
        if (value == null) {
            return null;
        }
        List<Long> ret = new ArrayList<Long>(value.length);
        int size = value.length;
        for (int i = 0; i < size; i++) {
            ret.add(value[i].longValue());
        }
        return ret;
    }

    /**
     * Copies the list of long to the collection of integer.
     * 
     * @param value
     *            the list of long
     * @param collection
     *            the collection of integer
     */
    protected void copyLongListToIntegerCollection(List<Long> value,
            Collection<Integer> collection) {
        int size = value.size();
        for (int i = 0; i < size; i++) {
            Long l = value.get(i);
            collection.add(l != null ? l.intValue() : null);
        }
    }

    /**
     * Converts the list of long to a list of integer.
     * 
     * @param value
     *            the list of long
     * @return a list of integer
     */
    protected ArrayList<Integer> longListToIntegerList(List<Long> value) {
        if (value == null) {
            return null;
        }
        ArrayList<Integer> collection = new ArrayList<Integer>(value.size());
        copyLongListToIntegerCollection(value, collection);
        return collection;
    }

    /**
     * Converts the list of long to a set of integer.
     * 
     * @param value
     *            the list of long
     * @return a set of integer
     */
    protected HashSet<Integer> longListToIntegerSet(List<Long> value) {
        if (value == null) {
            return null;
        }
        HashSet<Integer> collection = new HashSet<Integer>();
        copyLongListToIntegerCollection(value, collection);
        return collection;
    }

    /**
     * Converts the list of long to a sorted set of integer.
     * 
     * @param value
     *            the list of long
     * @return a sorted set of integer
     */
    protected TreeSet<Integer> longListToIntegerSortedSet(List<Long> value) {
        if (value == null) {
            return null;
        }
        TreeSet<Integer> collection = new TreeSet<Integer>();
        copyLongListToIntegerCollection(value, collection);
        return collection;
    }

    /**
     * Converts the list of long to a linked list of integer.
     * 
     * @param value
     *            the list of long
     * @return a linked list of integer
     */
    protected LinkedList<Integer> longListToIntegerLinkedList(List<Long> value) {
        if (value == null) {
            return null;
        }
        LinkedList<Integer> collection = new LinkedList<Integer>();
        copyLongListToIntegerCollection(value, collection);
        return collection;
    }

    /**
     * Converts the list of long to a linked hash set of integer.
     * 
     * @param value
     *            the list of long
     * @return a linked hash set of integer
     */
    protected LinkedHashSet<Integer> longListToIntegerLinkedHashSet(
            List<Long> value) {
        if (value == null) {
            return null;
        }
        LinkedHashSet<Integer> collection = new LinkedHashSet<Integer>();
        copyLongListToIntegerCollection(value, collection);
        return collection;
    }

    /**
     * Converts the list of long to a stack of integer.
     * 
     * @param value
     *            the list of long
     * @return a stack of integer
     */
    protected Stack<Integer> longListToIntegerStack(List<Long> value) {
        if (value == null) {
            return null;
        }
        Stack<Integer> collection = new Stack<Integer>();
        copyLongListToIntegerCollection(value, collection);
        return collection;
    }

    /**
     * Converts the list of long to a vector of integer.
     * 
     * @param value
     *            the list of long
     * @return a vector of integer
     */
    protected Vector<Integer> longListToIntegerVector(List<Long> value) {
        if (value == null) {
            return null;
        }
        Vector<Integer> collection = new Vector<Integer>(value.size());
        copyLongListToIntegerCollection(value, collection);
        return collection;
    }
}