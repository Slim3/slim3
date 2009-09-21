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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
     * Converts the string to a big decimal.
     * 
     * @param value
     *            the string
     * @return a big decimal
     */
    protected BigDecimal stringToBigDecimal(String value) {
        return value != null ? new BigDecimal(value) : null;
    }

    /**
     * Converts the big decimal to a string.
     * 
     * @param value
     *            the big decimal
     * @return a string
     */
    protected String bigDecimalToString(BigDecimal value) {
        return value != null ? value.toPlainString() : null;
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
     * Converts the list of long to an array list of short.
     * 
     * @param value
     *            the list of long
     * @return a list of short
     */
    protected ArrayList<Short> longListToShortList(List<Long> value) {
        if (value == null) {
            return null;
        }
        ArrayList<Short> ret = new ArrayList<Short>(value.size());
        int size = value.size();
        for (int i = 0; i < size; i++) {
            Long l = value.get(i);
            ret.add(l != null ? l.shortValue() : null);
        }
        return ret;
    }
}