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

/**
 * An abstract class for filter criterion.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public abstract class AbstractOrderCriterion implements OrderCriterion {

    /**
     * The meta data of attribute.
     */
    protected AttributeMeta attributeMeta;

    /**
     * The property name.
     */
    protected String propertyName;

    /**
     * Constructor.
     * 
     * @param attributeMeta
     *            the meta data of attribute
     * @param propertyName
     *            the property name
     * @throws NullPointerException
     *             if the propertyName parameter is null
     */
    public AbstractOrderCriterion(AttributeMeta attributeMeta,
            String propertyName) throws NullPointerException {
        if (attributeMeta == null) {
            throw new NullPointerException(
                "The attributeMeta parameter is null.");
        }
        if (propertyName == null) {
            throw new NullPointerException(
                "The propertyName parameter is null.");
        }
        this.attributeMeta = attributeMeta;
        this.propertyName = propertyName;
    }

    /**
     * Returns the property name.
     * 
     * @return the property name
     */
    public String getPropertyName() {
        return propertyName;
    }
}