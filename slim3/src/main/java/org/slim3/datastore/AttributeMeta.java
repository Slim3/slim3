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

import org.slim3.util.PropertyDesc;

/**
 * An abstract meta data of attribute.
 * 
 * @author higa
 * @param <M>
 *            the model type
 * @param <A>
 *            the attribute type
 * @since 3.0
 * 
 */
public abstract class AttributeMeta<M, A> {

    /**
     * The "ascending" sort criterion
     */
    public final SortCriterion asc;

    /**
     * The "descending" sort criterion
     */
    public final SortCriterion desc;

    /**
     * The meta data of model.
     */
    protected ModelMeta<M> modelMeta;

    /**
     * The name.
     */
    protected String name;

    /**
     * The field name.
     */
    protected String fieldName;

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
     * @param fieldName
     *            the field name
     * @param attributeClass
     *            the attribute class
     * 
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the name parameter
     *             is null or if the attributeClass parameter is null or if the
     *             fieldName parameter is null
     */
    public AttributeMeta(ModelMeta<M> modelMeta, String name,
            String fieldName, Class<? super A> attributeClass) {
        if (modelMeta == null) {
            throw new NullPointerException(
                "The modelMeta parameter must not be null.");
        }
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        if (fieldName == null) {
            throw new NullPointerException(
                "The fieldName parameter must not be null.");
        }
        if (attributeClass == null) {
            throw new NullPointerException(
                "The attributeClass parameter must not be null.");
        }
        this.modelMeta = modelMeta;
        this.name = name;
        this.fieldName = fieldName;
        this.attributeClass = attributeClass;
        asc = new AscCriterion(this);
        desc = new DescCriterion(this);
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
     * Returns the attribute class
     * 
     * @return the attribute class
     */
    public Class<? super A> getAttributeClass() {
        return attributeClass;
    }

    /**
     * Returns the field name.
     * 
     * @return the field name
     */
    public String getFieldName() {
        return fieldName;
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
            propertyDesc = modelMeta.getBeanDesc().getPropertyDesc(fieldName);
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