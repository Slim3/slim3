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
package org.slim3.gen.generator;

/**
 * Represents an attribute description.
 * 
 * @author taedium
 * @since 3.0
 */
public class AttributeDesc {

    /** the name of attribute */
    protected final String name;

    /** the name of attribute type */
    protected final String typeName;

    /** {@code true} if the attribute type is a model type. */
    protected final boolean modelType;

    /**
     * Creates a new {@link AttributeDesc}.
     * 
     * @param name
     *            the name of attribute.
     * @param typeName
     *            the name of attribute type.
     * @param modelType
     *            {@code true} if the attribute type is a model type.
     */
    public AttributeDesc(String name, String typeName, boolean modelType) {
        this.name = name;
        this.typeName = typeName;
        this.modelType = modelType;
    }

    /**
     * Returns the name of attribute.
     * 
     * @return the name of attribute
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the name of attribute type.
     * 
     * @return the name of attribute type
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Returns {@code true} if the attribute type is a model type.
     * 
     * @return {@code true} if the attribute type is a model type
     */
    public boolean isModelType() {
        return modelType;
    }

}
