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
package org.slim3.gen.desc;

/**
 * Represents an attribute description.
 * 
 * @author taedium
 * @since 3.0
 */
public class AttributeDesc {

    /** the name */
    protected String name;

    /** the className */
    protected String className;

    /** the elementClassName */
    protected String elementClassName;

    /** {@code true} if the {@code className} is a model type. */
    protected boolean modelType;

    /**
     * Returns the name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the className.
     * 
     * @return the className
     */
    public String getClassName() {
        return className;
    }

    /**
     * Sets the className.
     * 
     * @param className
     *            the className to set
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Returns the elementClassName.
     * 
     * @return the elementClassName
     */
    public String getElementClassName() {
        return elementClassName;
    }

    /**
     * Sets the elementClassName.
     * 
     * @param elementClassName
     *            the elementClassName to set
     */
    public void setElementClassName(String elementClassName) {
        this.elementClassName = elementClassName;
    }

    /**
     * Returns {@code true} if the {@code className} is a model type.
     * 
     * @return the modelType
     */
    public boolean isModelType() {
        return modelType;
    }

    /**
     * Sets {@code true} if the {@code className} is a model type.
     * 
     * @param modelType
     *            the modelType to set
     */
    public void setModelType(boolean modelType) {
        this.modelType = modelType;
    }

}
