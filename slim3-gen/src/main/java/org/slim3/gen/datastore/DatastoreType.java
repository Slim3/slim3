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

import static org.slim3.gen.ClassConstants.primitve_byte;

import org.slim3.gen.util.DeclarationUtil;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.type.TypeMirror;

/**
 * Represents a datastore type.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class DatastoreType {

    /** the environment */
    protected final AnnotationProcessorEnvironment env;

    /** the declaration */
    protected final Declaration declaration;

    /** the typeMirror */
    protected final TypeMirror typeMirror;

    /** the declaredTypeName */
    protected final String declaredTypeName;

    /** the typeName */
    protected String typeName;

    /** the wrapperTypeName */
    protected String wrapperTypeName;

    /** the implicationTypeName */
    protected String implicationTypeName;

    /** the elementTypeName */
    protected String elementTypeName;

    /** the primitive */
    protected boolean primitive;

    /** the unindex */
    protected boolean unindex;

    /** the collection */
    protected boolean collection;

    /** the serializable */
    protected boolean serializable;

    /** the array */
    protected boolean array;

    /**
     * Creates a new {@link DatastoreType}.
     * 
     * @param env
     *            the environment
     * @param declaration
     *            the declaration
     * @param typeMirror
     *            the typemirror
     */
    public DatastoreType(AnnotationProcessorEnvironment env,
            Declaration declaration, TypeMirror typeMirror) {
        if (env == null) {
            throw new NullPointerException("The env parameter is null.");
        }
        if (declaration == null) {
            throw new NullPointerException("The declaration parameter is null.");
        }
        if (typeMirror == null) {
            throw new NullPointerException("The typeMirror parameter is null.");
        }
        this.env = env;
        this.declaration = declaration;
        this.typeMirror = typeMirror;
        this.declaredTypeName = typeMirror.toString();
    }

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
    public boolean isSerializable() {
        return serializable;
    }

    /**
     * @param serializable
     *            the serializable to set
     */
    public void setSerializable(boolean serializable) {
        this.serializable = serializable;
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
     * @return the elementTypeName
     */
    public String getElementTypeName() {
        return elementTypeName;
    }

    /**
     * @param elementTypeName
     *            the elementTypeName to set
     */
    public void setElementTypeName(String elementTypeName) {
        this.elementTypeName = elementTypeName;
    }

    /**
     * @return the declarationName
     */
    public String getDeclaredTypeName() {
        return declaredTypeName;
    }

    /**
     * @return the implClassName
     */
    public String getImplicationTypeName() {
        return implicationTypeName;
    }

    /**
     * @param implicationTypeName
     *            the implicationTypeName to set
     */
    public void setImplicationTypeName(String implicationTypeName) {
        this.implicationTypeName = implicationTypeName;
    }

    /**
     * @return the wrapperTypeName
     */
    public String getWrapperTypeName() {
        return wrapperTypeName;
    }

    /**
     * @param wrapperTypeName
     *            the wrapperTypeName to set
     */
    public void setWrapperTypeName(String wrapperTypeName) {
        this.wrapperTypeName = wrapperTypeName;
    }

    /**
     * @return the declaration
     */
    public Declaration getDeclaration() {
        return declaration;
    }

    /**
     * @return the typeMirror
     */
    public TypeMirror getTypeMirror() {
        return typeMirror;
    }

    /**
     * Returns {@code true} if this represents byte array.
     * 
     * @return {@code true} if this represents byte array otherwise {@code
     *         false}.
     */
    public boolean isByteArray() {
        return primitve_byte.equals(elementTypeName) && array;
    }

    /**
     * Returns {@code true} if this anntated with {@code annotation}.
     * 
     * @param annotation
     *            the annotation
     * @return {{@code true} if this anntated with {@code annotation} otherwise
     *         {@code false}.
     */
    public boolean isAnnotated(String annotation) {
        return DeclarationUtil
            .getAnnotationMirror(env, declaration, annotation) != null;
    }
}
