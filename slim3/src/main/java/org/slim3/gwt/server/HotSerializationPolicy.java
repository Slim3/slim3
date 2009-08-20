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
package org.slim3.gwt.server;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.SerializationPolicy;

/**
 * The serialization policy for HOT reloading.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class HotSerializationPolicy extends SerializationPolicy {

    /**
     * Constructor.
     */
    public HotSerializationPolicy() {
    }

    @Override
    public boolean shouldDeserializeFields(Class<?> clazz) {
        return isSerializable(clazz);
    }

    @Override
    public boolean shouldSerializeFields(Class<?> clazz) {
        return isSerializable(clazz);
    }

    @Override
    public void validateDeserialize(Class<?> clazz)
            throws SerializationException {
        if (!isSerializable(clazz)) {
            throw new SerializationException("Type '"
                + clazz.getName()
                + "' is not assignable to '"
                + Serializable.class.getName()
                + "'.");
        }
    }

    @Override
    public void validateSerialize(Class<?> clazz) throws SerializationException {
        if (!isSerializable(clazz)) {
            throw new SerializationException("Type '"
                + clazz.getName()
                + "' is not assignable to '"
                + Serializable.class.getName()
                + "'.");
        }
    }

    /**
     * Determines if the class is able to be serialized.
     * 
     * @param clazz
     *            the class
     * @return whether the class is able to be serialized
     */
    protected boolean isSerializable(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return true;
        }
        if (clazz.isArray()) {
            return isSerializable(clazz.getComponentType());
        }
        return Serializable.class.isAssignableFrom(clazz);
    }
}
