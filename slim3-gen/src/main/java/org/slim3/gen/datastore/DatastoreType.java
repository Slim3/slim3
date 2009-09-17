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
package org.slim3.gen.datastore;

import org.slim3.gen.util.DeclarationUtil;

import com.sun.mirror.type.DeclaredType;

/**
 * @author taedium
 * 
 */
public class DatastoreType {

    protected boolean primitive;

    protected boolean unindex;

    protected boolean collection;

    protected boolean serialized;

    protected boolean array;

    protected String typeName;

    protected String elementTypeName;

    protected String implTypeName;

    protected String declaredTypeName;

    protected DeclaredType declaredType;

    /**
     * @return the primitive
     */
    public boolean isPrimitive() {
        return primitive;
    }

    /**
     * @param primitive
     *            the primitive to set
     */
    public void setPrimitive(boolean primitive) {
        this.primitive = primitive;
    }

    /**
     * @return the unindex
     */
    public boolean isUnindex() {
        return unindex;
    }

    /**
     * @param unindex
     *            the unindex to set
     */
    public void setUnindex(boolean unindex) {
        this.unindex = unindex;
    }

    /**
     * @return the collection
     */
    public boolean isCollection() {
        return collection;
    }

    /**
     * @param collection
     *            the collection to set
     */
    public void setCollection(boolean collection) {
        this.collection = collection;
    }

    /**
     * @return the serializable
     */
    public boolean isSerialized() {
        return serialized;
    }

    /**
     * @param serializable
     *            the serializable to set
     */
    public void setSerialized(boolean serializable) {
        this.serialized = serializable;
    }

    /**
     * @return the array
     */
    public boolean isArray() {
        return array;
    }

    /**
     * @param array
     *            the array to set
     */
    public void setArray(boolean array) {
        this.array = array;
    }

    /**
     * @return the className
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * @param className
     *            the className to set
     */
    public void setTypeName(String className) {
        this.typeName = className;
    }

    /**
     * @return the componentClassName
     */
    public String getElementTypeName() {
        return elementTypeName;
    }

    /**
     * @param componentClassName
     *            the componentClassName to set
     */
    public void setElementTypeName(String componentClassName) {
        this.elementTypeName = componentClassName;
    }

    /**
     * @return the declarationName
     */
    public String getDeclaredTypeName() {
        return declaredTypeName;
    }

    /**
     * @param declarationName
     *            the declarationName to set
     */
    public void setDeclaredTypeName(String declarationName) {
        this.declaredTypeName = declarationName;
    }

    /**
     * @return the implClassName
     */
    public String getImplTypeName() {
        return implTypeName;
    }

    /**
     * @param implClassName
     *            the implClassName to set
     */
    public void setImplTypeName(String implClassName) {
        this.implTypeName = implClassName;
    }

    public boolean isAnnotated(String annotation) {
        return DeclarationUtil.getAnnotationMirror(declaredType
            .getDeclaration(), annotation) != null;
    }
}
