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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import org.slim3.util.BeanDesc;
import org.slim3.util.BeanUtil;
import org.slim3.util.ByteUtil;
import org.slim3.util.LongUtil;
import org.slim3.util.PropertyDesc;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.ShortBlob;
import com.google.appengine.api.datastore.Text;

/**
 * A meta data of model.
 * 
 * @author higa
 * @param <M>
 *            the model type
 * @since 3.0
 * 
 */
public abstract class ModelMeta<M> {

    /**
     * The model class.
     */
    protected Class<M> modelClass;

    /**
     * The bean descriptor.
     */
    protected BeanDesc beanDesc;

    /**
     * Constructor.
     * 
     * @param modelClass
     *            the model class
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public ModelMeta(Class<M> modelClass) throws NullPointerException {
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
    public Class<M> getModelClass() {
        return modelClass;
    }

    /**
     * Converts the entity to a model.
     * 
     * @param entity
     *            the entity
     * @return a model
     */
    public abstract M entityToModel(Entity entity);

    /**
     * Converts the model to an entity.
     * 
     * @param model
     *            the model
     * @return an entity
     */
    public abstract Entity modelToEntity(Object model);

    /**
     * Returns version property value of the model.
     * 
     * @param model
     *            the model
     * @return a version property value of the model
     */
    public long getVersion(Object model) {
        BeanDesc beanDesc = getBeanDesc();
        PropertyDesc pd = beanDesc.getPropertyDesc("version");
        return LongUtil.toPrimitiveLong(pd.getValue(model));
    }

    /**
     * Increments the version property value.
     * 
     * @param model
     *            the model
     */
    public void incrementVersion(Object model) {
        BeanDesc beanDesc = getBeanDesc();
        PropertyDesc pd = beanDesc.getPropertyDesc("version");
        long version = LongUtil.toPrimitiveLong(pd.getValue(model));
        pd.setValue(model, version + 1);
    }

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
     * Converts the {@link Enum} to a string representation.
     * 
     * @param value
     *            the {@link Enum}
     * @return a string representation
     */
    protected String enumToString(Enum<?> value) {
        return value != null ? value.name() : null;
    }

    /**
     * Converts the string to an {@link Enum}.
     * 
     * @param <T>
     *            the enum type
     * @param clazz
     *            the enum class
     * @param value
     *            the String
     * @return an {@link Enum}
     */
    protected <T extends Enum<T>> T stringToEnum(Class<T> clazz, String value) {
        return value != null ? Enum.valueOf(clazz, value) : null;
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
     * @param <T>
     *            the type
     * @param value
     *            the short blob
     * @return a serializable object
     */
    @SuppressWarnings("unchecked")
    protected <T> T shortBlobToSerializable(ShortBlob value) {
        return value != null ? (T) ByteUtil.toObject(value.getBytes()) : null;
    }

    /**
     * Converts the serializable object to a short blob.
     * 
     * @param value
     *            the serializable object
     * @return a short blob
     */
    protected ShortBlob serializableToShortBlob(Object value) {
        return value != null
            ? new ShortBlob(ByteUtil.toByteArray(value))
            : null;
    }

    /**
     * Converts the blob to a serializable object.
     * 
     * @param <T>
     *            the type
     * @param value
     *            the blob
     * @return a serializable object
     */
    @SuppressWarnings("unchecked")
    protected <T> T blobToSerializable(Blob value) {
        return value != null ? (T) ByteUtil.toObject(value.getBytes()) : null;
    }

    /**
     * Converts the serializable object to a blob.
     * 
     * @param value
     *            the serializable object
     * @return a blob
     */
    protected Blob serializableToBlob(Object value) {
        return value != null ? new Blob(ByteUtil.toByteArray(value)) : null;
    }

    /**
     * Converts the list to an array list.
     * 
     * @param <T>
     *            the type
     * @param clazz
     *            the class
     * @param value
     *            the list
     * @return an array list
     */
    @SuppressWarnings("unchecked")
    protected <T> ArrayList<T> toList(Class<T> clazz, Object value) {
        return (ArrayList<T>) value;
    }

    /**
     * Converts the list to a hash set.
     * 
     * @param <T>
     *            the type
     * @param clazz
     *            the class
     * @param value
     *            the list
     * @return a hash set
     */
    @SuppressWarnings("unchecked")
    protected <T> HashSet<T> toSet(Class<T> clazz, Object value) {
        List<T> v = (List<T>) value;
        if (v == null) {
            return null;
        }
        HashSet<T> set = new HashSet<T>();
        set.addAll(v);
        return set;
    }

    /**
     * Converts the list to a sorted set.
     * 
     * @param <T>
     *            the type
     * @param clazz
     *            the class
     * @param value
     *            the list
     * @return a sorted set
     */
    @SuppressWarnings("unchecked")
    protected <T> TreeSet<T> toSortedSet(Class<T> clazz, Object value) {
        List<T> v = (List<T>) value;
        if (v == null) {
            return null;
        }
        TreeSet<T> set = new TreeSet<T>();
        set.addAll(v);
        return set;
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
    @SuppressWarnings("unchecked")
    protected ArrayList<Short> longListToShortList(Object value) {
        List<Long> v = (List<Long>) value;
        if (v == null) {
            return null;
        }
        ArrayList<Short> collection = new ArrayList<Short>(v.size());
        copyLongListToShortCollection(v, collection);
        return collection;
    }

    /**
     * Converts the list of long to a set of short.
     * 
     * @param value
     *            the list of long
     * @return a set of short
     */
    @SuppressWarnings("unchecked")
    protected HashSet<Short> longListToShortSet(Object value) {
        List<Long> v = (List<Long>) value;
        if (v == null) {
            return null;
        }
        HashSet<Short> collection = new HashSet<Short>();
        copyLongListToShortCollection(v, collection);
        return collection;
    }

    /**
     * Converts the list of long to a sorted set of short.
     * 
     * @param value
     *            the list of long
     * @return a sorted set of short
     */
    @SuppressWarnings("unchecked")
    protected TreeSet<Short> longListToShortSortedSet(Object value) {
        List<Long> v = (List<Long>) value;
        if (v == null) {
            return null;
        }
        TreeSet<Short> collection = new TreeSet<Short>();
        copyLongListToShortCollection(v, collection);
        return collection;
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
    @SuppressWarnings("unchecked")
    protected ArrayList<Integer> longListToIntegerList(Object value) {
        List<Long> v = (List<Long>) value;
        if (v == null) {
            return null;
        }
        ArrayList<Integer> collection = new ArrayList<Integer>(v.size());
        copyLongListToIntegerCollection(v, collection);
        return collection;
    }

    /**
     * Converts the list of long to a set of integer.
     * 
     * @param value
     *            the list of long
     * @return a set of integer
     */
    @SuppressWarnings("unchecked")
    protected HashSet<Integer> longListToIntegerSet(Object value) {
        List<Long> v = (List<Long>) value;
        if (v == null) {
            return null;
        }
        HashSet<Integer> collection = new HashSet<Integer>();
        copyLongListToIntegerCollection(v, collection);
        return collection;
    }

    /**
     * Converts the list of long to a sorted set of integer.
     * 
     * @param value
     *            the list of long
     * @return a sorted set of integer
     */
    @SuppressWarnings("unchecked")
    protected TreeSet<Integer> longListToIntegerSortedSet(Object value) {
        List<Long> v = (List<Long>) value;
        if (v == null) {
            return null;
        }
        TreeSet<Integer> collection = new TreeSet<Integer>();
        copyLongListToIntegerCollection(v, collection);
        return collection;
    }

    /**
     * Copies the list of double to the collection of float.
     * 
     * @param value
     *            the list of double
     * @param collection
     *            the collection of float
     */
    protected void copyDoubleListToFloatCollection(List<Double> value,
            Collection<Float> collection) {
        int size = value.size();
        for (int i = 0; i < size; i++) {
            Double d = value.get(i);
            collection.add(d != null ? d.floatValue() : null);
        }
    }

    /**
     * Converts the list of double to a list of float.
     * 
     * @param value
     *            the list of double
     * @return a list of float
     */
    @SuppressWarnings("unchecked")
    protected ArrayList<Float> doubleListToFloatList(Object value) {
        List<Double> v = (List<Double>) value;
        if (v == null) {
            return null;
        }
        ArrayList<Float> collection = new ArrayList<Float>(v.size());
        copyDoubleListToFloatCollection(v, collection);
        return collection;
    }

    /**
     * Converts the list of double to a set of float.
     * 
     * @param value
     *            the list of double
     * @return a set of float
     */
    @SuppressWarnings("unchecked")
    protected HashSet<Float> doubleListToFloatSet(Object value) {
        List<Double> v = (List<Double>) value;
        if (v == null) {
            return null;
        }
        HashSet<Float> collection = new HashSet<Float>();
        copyDoubleListToFloatCollection(v, collection);
        return collection;
    }

    /**
     * Converts the list of double to a sorted set of float.
     * 
     * @param value
     *            the list of double
     * @return a sorted set of float
     */
    @SuppressWarnings("unchecked")
    protected TreeSet<Float> doubleListToFloatSortedSet(Object value) {
        List<Double> v = (List<Double>) value;
        if (v == null) {
            return null;
        }
        TreeSet<Float> collection = new TreeSet<Float>();
        copyDoubleListToFloatCollection(v, collection);
        return collection;
    }

    /**
     * Returns the bean descriptor.
     * 
     * @return the bean descriptor
     */
    protected BeanDesc getBeanDesc() {
        if (beanDesc != null) {
            return beanDesc;
        }
        beanDesc = BeanUtil.getBeanDesc(modelClass);
        return beanDesc;
    }
}