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

import org.slim3.util.ConversionUtil;

/**
 * A meta data for attribute.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class AttributeMeta {

    /**
     * The name.
     */
    protected String name;

    /**
     * The attribute class.
     */
    protected Class<?> attributeClass;

    /**
     * Constructor.
     * 
     * @param name
     *            the name
     * @param attributeClass
     *            the attribute class
     * @throws NullPointerException
     *             if the name parameter is null or if the attributeClass
     *             parameter is null
     */
    public AttributeMeta(String name, Class<?> attributeClass)
            throws NullPointerException {
        if (name == null) {
            throw new NullPointerException("The name parameter is null.");
        }
        if (attributeClass == null) {
            throw new NullPointerException(
                "The attributeClass parameter is null.");
        }
        this.name = name;
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
     * Returns the attribute class.
     * 
     * @return the attribute class
     */
    public Class<?> getAttributeClass() {
        return attributeClass;
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
        parameter = ConversionUtil.convert(parameter, attributeClass);
        return new EqCriterion(name, name + "Param", parameter);
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
        return new LtCriterion(name, name + "LtParam", parameter);
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
        return new LeCriterion(name, name + "LeParam", parameter);
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
        return new GtCriterion(name, name + "GtParam", parameter);
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
        return new GeCriterion(name, name + "GeParam", parameter);
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
        if (attributeClass.isArray()) {
            Class<?> clazz = attributeClass.getComponentType();
            parameter = ConversionUtil.convert(parameter, clazz);
        }
        return new ContainsCriterion(name, name + "Param", parameter);
    }

    /**
     * Returns the "ascending" order criterion.
     * 
     * @return the "ascending" order criterion
     */
    public AscCriterion asc() {
        return new AscCriterion(name);
    }

    /**
     * Returns the "descending" order criterion.
     * 
     * @return the "descending" order criterion
     */
    public DescCriterion desc() {
        return new DescCriterion(name);
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