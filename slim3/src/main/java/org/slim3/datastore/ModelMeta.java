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
     * Converts the entity to model.
     * 
     * @param entity
     *            the entity
     * @return converted model.
     */
    public abstract T entityToModel(Entity entity);

    /**
     * Converts the long to a primitive short.
     * 
     * @param value
     *            the long
     * @return a primitive short
     */
    protected short toPrimitiveShort(Long value) {
        return value != null ? value.shortValue() : 0;
    }

    /**
     * Converts the long to a short.
     * 
     * @param value
     *            the long
     * @return a short
     */
    protected Short toShort(Long value) {
        return value != null ? value.shortValue() : null;
    }

    /**
     * Converts the long to a primitive int.
     * 
     * @param value
     *            the long
     * @return a primitive int
     */
    protected int toPrimitiveInt(Long value) {
        return value != null ? value.intValue() : 0;
    }

    /**
     * Converts the long to an integer.
     * 
     * @param value
     *            the long
     * @return an integer
     */
    protected Integer toInteger(Long value) {
        return value != null ? value.intValue() : null;
    }

    /**
     * Converts the long to a primitive long.
     * 
     * @param value
     *            the long
     * @return a primitive long
     */
    protected long toPrimitiveLong(Long value) {
        return value != null ? value : 0;
    }

    /**
     * Converts the list of long to an array of primitive short.
     * 
     * @param value
     *            the list of long
     * @return an array of primitive short
     */
    protected short[] toPrimitiveShortArray(List<Long> value) {
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
     * Converts the list of long to a list of short.
     * 
     * @param value
     *            the list of long
     * @return a list of short
     */
    protected List<Short> toShortList(List<Long> value) {
        if (value == null) {
            return null;
        }
        List<Short> ret = new ArrayList<Short>(value.size());
        int size = value.size();
        for (int i = 0; i < size; i++) {
            Long l = value.get(i);
            ret.add(l != null ? l.shortValue() : null);
        }
        return ret;
    }

    protected String toString(Text value) {
        return value != null ? value.getValue() : null;
    }

    protected byte[] toBytes(ShortBlob value) {
        return value != null ? value.getBytes() : null;
    }

    protected byte[] toBytes(Blob value) {
        return value != null ? value.getBytes() : null;
    }

    protected Serializable toSerializable(ShortBlob value) {
        return value != null ? (Serializable) ByteUtil.toObject(value
            .getBytes()) : null;
    }

    protected Serializable toSerializable(Blob value) {
        return value != null ? (Serializable) ByteUtil.toObject(value
            .getBytes()) : null;
    }
}