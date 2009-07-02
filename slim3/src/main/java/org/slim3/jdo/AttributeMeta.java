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
package org.slim3.jdo;

import java.util.Collection;

import org.slim3.util.ConversionUtil;
import org.slim3.util.PropertyDesc;

/**
 * A meta data of attribute.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class AttributeMeta {

    /**
     * The meta data of model.
     */
    protected ModelMeta<?> modelMeta;

    /**
     * The name.
     */
    protected String name;

    /**
     * The full name.
     */
    protected String fullName;

    /**
     * The attribute class.
     */
    protected Class<?> attributeClass;

    /**
     * The element class.
     */
    protected Class<?> elementClass;

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
     * @param attributeClass
     *            the attribute class
     */
    public AttributeMeta(ModelMeta<?> modelMeta, String name,
            Class<?> attributeClass) {
        this(modelMeta, name, attributeClass, null);
    }

    /**
     * Constructor.
     * 
     * @param modelMeta
     *            the meta data of model
     * @param name
     *            the name
     * @param attributeClass
     *            the attribute class
     * @param elementClass
     *            the element class
     * @throws NullPointerException
     *             if the modelMeta parameter is null or if the name parameter
     *             is null or if the attributeClass parameter is null
     * @throws IllegalArgumentException
     *             if the property is not found
     */
    public AttributeMeta(ModelMeta<?> modelMeta, String name,
            Class<?> attributeClass, Class<?> elementClass)
            throws NullPointerException, IllegalArgumentException {
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        if (name == null) {
            throw new NullPointerException("The name parameter is null.");
        }
        if (attributeClass == null) {
            throw new NullPointerException(
                "The attributeClass parameter is null.");
        }
        this.modelMeta = modelMeta;
        this.name = name;
        this.attributeClass = attributeClass;
        this.elementClass = elementClass;
        if (modelMeta.attributeName == null) {
            fullName = name;
        } else {
            fullName = modelMeta.attributeName + "." + name;
        }
        propertyDesc = modelMeta.getBeanDesc().getPropertyDesc(name);
        if (propertyDesc == null) {
            throw new IllegalArgumentException("The property("
                + name
                + ") of model("
                + modelMeta.getModelClass().getName()
                + ") is not found.");
        }
    }

    /**
     * Returns the "equal" filter criterion.
     * 
     * @param parameter
     * @return the "equal" filter criterion
     */
    public EqCriterion eq(Object parameter) {
        if (isEmpty(parameter)) {
            return null;
        }
        if (!parameter.getClass().isArray()
            && !(parameter instanceof Collection)) {
            parameter = ConversionUtil.convert(parameter, attributeClass);
        }
        return new EqCriterion(this, parameter);
    }

    /**
     * Returns the "less than" filter criterion.
     * 
     * @param parameter
     * @return the "less than" filter criterion
     */
    public LtCriterion lt(Object parameter) {
        if (isEmpty(parameter)) {
            return null;
        }
        parameter = ConversionUtil.convert(parameter, attributeClass);
        return new LtCriterion(this, parameter);
    }

    /**
     * Returns the "less equal" filter criterion.
     * 
     * @param parameter
     * @return the "less equals" filter criterion
     */
    public LeCriterion le(Object parameter) {
        if (isEmpty(parameter)) {
            return null;
        }
        parameter = ConversionUtil.convert(parameter, attributeClass);
        return new LeCriterion(this, parameter);
    }

    /**
     * Returns the "greater than" filter criterion.
     * 
     * @param parameter
     * @return the "greater than" filter criterion
     */
    public GtCriterion gt(Object parameter) {
        if (isEmpty(parameter)) {
            return null;
        }
        parameter = ConversionUtil.convert(parameter, attributeClass);
        return new GtCriterion(this, parameter);
    }

    /**
     * Returns the "greater equal" filter criterion.
     * 
     * @param parameter
     * @return the "greater equal" filter criterion
     */
    public GeCriterion ge(Object parameter) {
        if (isEmpty(parameter)) {
            return null;
        }
        parameter = ConversionUtil.convert(parameter, attributeClass);
        return new GeCriterion(this, parameter);
    }

    /**
     * Returns the "contains" filter criterion.
     * 
     * @param parameter
     * @return the "contains" filter criterion
     */
    public ContainsCriterion contains(Object parameter) {
        if (isEmpty(parameter)) {
            return null;
        }
        if (elementClass != null) {
            parameter = ConversionUtil.convert(parameter, elementClass);
        }
        return new ContainsCriterion(this, parameter);
    }

    /**
     * Returns the "ascending" order criterion.
     * 
     * @return the "ascending" order criterion
     */
    public AscCriterion asc() {
        return new AscCriterion(this);
    }

    /**
     * Returns the "descending" order criterion.
     * 
     * @return the "descending" order criterion
     */
    public DescCriterion desc() {
        return new DescCriterion(this);
    }

    /**
     * Returns the property descriptor.
     * 
     * @return the property descriptor
     */
    public PropertyDesc getPropertyDesc() {
        return propertyDesc;
    }

    /**
     * Determines if the parameter is empty.
     * 
     * @param parameter
     *            the parameter
     * @return whether the parameter is empty
     */
    protected boolean isEmpty(Object parameter) {
        return parameter == null || "".equals(parameter);
    }
}