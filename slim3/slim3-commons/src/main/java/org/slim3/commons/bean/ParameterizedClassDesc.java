/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.slim3.commons.bean;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import org.slim3.commons.util.GenericsUtil;

/**
 * 
 * This class describes a parameterized class.
 * 
 * @author koichik
 * @author higa
 * @since 3.0
 */
public final class ParameterizedClassDesc {

    private Class<?> rawClass;

    private ParameterizedClassDesc[] arguments;

    /**
     * Creates a parameterized class descriptor.
     * 
     * @param type
     *            the type
     * @return a parameterized class descriptor
     */
    public static ParameterizedClassDesc create(Type type) {
        Class<?> rawClass = GenericsUtil.getRawClass(type);
        if (rawClass == null) {
            return null;
        }
        ParameterizedClassDesc[] arguments = null;
        Type[] typeArguments = GenericsUtil.getTypeArguments(type);
        if (typeArguments != null) {
            arguments = new ParameterizedClassDesc[typeArguments.length];
            for (int i = 0; i < arguments.length; ++i) {
                arguments[i] = create(typeArguments[i]);
            }
        }
        return new ParameterizedClassDesc(rawClass, arguments);
    }

    /**
     * Constructor.
     * 
     * @param rawClass
     *            the raw class
     * @param arguments
     *            the {@link ParameterizedClassDesc} objects
     */
    ParameterizedClassDesc(Class<?> rawClass, ParameterizedClassDesc[] arguments) {
        this.rawClass = rawClass;
        this.arguments = arguments;
    }

    /**
     * Determines if the property is parameterized. Returns true if the property
     * is parameterized.
     * 
     * @return whether the property is parameterized
     */
    public boolean isParameterized() {
        return arguments != null;
    }

    /**
     * Returns the raw class.
     * 
     * @return the raw class
     */
    public Class<?> getRawClass() {
        return rawClass;
    }

    /**
     * Returns the {@link ParameterizedClassDesc} objects.
     * 
     * @return the {@link ParameterizedClassDesc} objects
     */
    public ParameterizedClassDesc[] getArguments() {
        return arguments;
    }

    /**
     * Returns the element class of collection.
     * 
     * @return the element class of collection
     */
    public Class<?> getElementClassOfCollection() {
        if (!Collection.class.isAssignableFrom(rawClass) || !isParameterized()) {
            return null;
        }
        return arguments[0].getRawClass();
    }

    /**
     * Returns the key class of map.
     * 
     * @return the key class of map
     */
    public Class<?> getKeyClassOfMap() {
        if (!Map.class.isAssignableFrom(rawClass) || !isParameterized()) {
            return null;
        }
        return arguments[0].getRawClass();
    }

    /**
     * Returns the value class of map.
     * 
     * @return the value class of map
     */
    public Class<?> getValueClassOfMap() {
        if (!Map.class.isAssignableFrom(rawClass) || !isParameterized()) {
            return null;
        }
        return arguments[1].getRawClass();
    }
}