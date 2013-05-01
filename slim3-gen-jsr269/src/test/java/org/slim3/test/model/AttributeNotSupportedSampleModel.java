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

import java.util.Collection;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

/**
 * @author vvakame
 */
@Model
public class AttributeNotSupportedSampleModel {
    @Attribute(primaryKey = true)
    Key primaryKey;

    byte bytePrimitive;
    Byte byteWrapper;
    char charPrimitive;
    Character charWrapper;

    String[] strArray;
    Collection<String> strCollection;

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
     * @return the bytePrimitive
     */
    public byte getBytePrimitive() {
        return bytePrimitive;
    }

    /**
     * @param bytePrimitive
     *            the bytePrimitive to set
     */
    public void setBytePrimitive(byte bytePrimitive) {
        this.bytePrimitive = bytePrimitive;
    }

    /**
     * @return the byteWrapper
     */
    public Byte getByteWrapper() {
        return byteWrapper;
    }

    /**
     * @param byteWrapper
     *            the byteWrapper to set
     */
    public void setByteWrapper(Byte byteWrapper) {
        this.byteWrapper = byteWrapper;
    }

    /**
     * @return the charPrimitive
     */
    public char getCharPrimitive() {
        return charPrimitive;
    }

    /**
     * @param charPrimitive
     *            the charPrimitive to set
     */
    public void setCharPrimitive(char charPrimitive) {
        this.charPrimitive = charPrimitive;
    }

    /**
     * @return the charWrapper
     */
    public Character getCharWrapper() {
        return charWrapper;
    }

    /**
     * @param charWrapper
     *            the charWrapper to set
     */
    public void setCharWrapper(Character charWrapper) {
        this.charWrapper = charWrapper;
    }

    /**
     * @return the strArray
     */
    public String[] getStrArray() {
        return strArray;
    }

    /**
     * @param strArray
     *            the strArray to set
     */
    public void setStrArray(String[] strArray) {
        this.strArray = strArray;
    }

    /**
     * @return the strCollection
     */
    public Collection<String> getStrCollection() {
        return strCollection;
    }

    /**
     * @param strCollection
     *            the strCollection to set
     */
    public void setStrCollection(Collection<String> strCollection) {
        this.strCollection = strCollection;
    }
}
