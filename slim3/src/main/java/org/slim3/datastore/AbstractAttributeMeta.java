/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import org.slim3.util.PropertyDesc;

/**
 * An abstract meta data of attribute.
 * 
 * @author higa
 * @param <M>
 *            the model type
 * @param <A>
 *            the attribute type
 * @since 1.0.1
 * 
 */
public abstract class AbstractAttributeMeta<M, A> implements CharSequence {

    /**
     * The meta data of model.
     */
    protected ModelMeta<M> modelMeta;

    /**
     * The name.
     */
    protected String name;

    /**
     * The attribute name.
     */
    protected String attributeName;

    /**
     * The attribute class.
     */
    protected Class<? super A> attributeClass;

    /**
     * The property descriptor.
     */
    protected PropertyDesc propertyDesc;

    /**
     * Constructor.
     * 
     * @param modelMeta
     *            the meta data of model
     * @param name
     *            the name
     * @param attributeName
     *            the attribute name
     * @param attributeClass
     *            the attribute class
     * 
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the name parameter
     *             is null or if the attributeName parameter is null or if the
     *             attributeClass parameter is null
     */
    public AbstractAttributeMeta(ModelMeta<M> modelMeta, String name,
            String attributeName, Class<? super A> attributeClass) {
        if (modelMeta == null) {
            throw new NullPointerException(
                "The modelMeta parameter must not be null.");
        }
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        if (attributeName == null) {
            throw new NullPointerException(
                "The attributeName parameter must not be null.");
        }
        if (attributeClass == null) {
            throw new NullPointerException(
                "The attributeClass parameter must not be null.");
        }
        this.modelMeta = modelMeta;
        this.name = name;
        this.attributeName = attributeName;
        this.attributeClass = attributeClass;
    }

    /**
     * Returns the name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Use {@link #getAttributeName()} instead of this method.
     * 
     * @return the field name
     */
    @Deprecated
    public String getFieldName() {
        return getAttributeName();
    }

    /**
     * Returns the attribute class
     * 
     * @return the attribute class
     */
    public Class<? super A> getAttributeClass() {
        return attributeClass;
    }

    /**
     * Returns the attribute name.
     * 
     * @return the attribute name
     */
    public String getAttributeName() {
        return attributeName;
    }

    public char charAt(int index) {
        return attributeName.charAt(index);
    }

    public int length() {
        return attributeName.length();
    }

    public CharSequence subSequence(int start, int end) {
        return attributeName.subSequence(start, end);
    }

    @Override
    public String toString() {
        return attributeName;
    }

    /**
     * Returns the property value.
     * 
     * @param model
     *            the model
     * @return the property value
     * @throws IllegalArgumentException
     *             if the property is not found
     */
    protected Object getValue(Object model) throws IllegalArgumentException {
        if (propertyDesc == null) {
            propertyDesc =
                modelMeta.getBeanDesc().getPropertyDesc(attributeName);
        }
        if (propertyDesc == null) {
            throw new IllegalArgumentException("The property("
                + name
                + ") of model("
                + modelMeta.getModelClass().getName()
                + ") is not found.");
        }
        return propertyDesc.getValue(model);
    }
}