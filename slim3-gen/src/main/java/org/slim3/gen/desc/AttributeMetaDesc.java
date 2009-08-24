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
 * Represents an attribute meta description.
 * 
 * @author taedium
 * @since 3.0
 */
public class AttributeMetaDesc {

    /** the name */
    protected String name;

    /** the attributeClassName */
    protected String attributeClassName;

    /** the attributeElementClassName */
    protected String attributeElementClassName;

    /** {@code true} if this attribute is embedded. */
    protected boolean embedded = false;

    /** the embeddedModelMetaClassName */
    protected String embeddedModelMetaClassName;

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
     * Returns the attributeClassName.
     * 
     * @return the attributeClassName
     */
    public String getAttributeClassName() {
        return attributeClassName;
    }

    /**
     * Sets the attributeClassName.
     * 
     * @param attributeClassName
     *            the attributeClassName to set
     */
    public void setAttributeClassName(String attributeClassName) {
        this.attributeClassName = attributeClassName;
    }

    /**
     * Returns the attributeElementClassName.
     * 
     * @return the attributeElementClassName
     */
    public String getAttributeElementClassName() {
        return attributeElementClassName;
    }

    /**
     * Sets the attributeElementClassName.
     * 
     * @param attributeElementClassName
     *            the attributeElementClassName to set
     */
    public void setAttributeElementClassName(String attributeElementClassName) {
        this.attributeElementClassName = attributeElementClassName;
    }

    /**
     * Returns {@code true} if this attribute is embedded.
     * 
     * @return whether this attribute is embedded
     */
    public boolean isEmbedded() {
        return embedded;
    }

    /**
     * Sets {@code true} if this attribute is embedded.
     * 
     * @param embedded
     *            whether this attribute is embedded
     */
    public void setEmbedded(boolean embedded) {
        this.embedded = embedded;
    }

    /**
     * Returns the embeddedModelMetaClassName.
     * 
     * @return the embeddedModelMetaClassName
     */
    public String getEmbeddedModelMetaClassName() {
        return embeddedModelMetaClassName;
    }

    /**
     * Sets the embeddedModelMetaClassName.
     * 
     * @param embeddedModelMetaClassName
     *            the embeddedModelMetaClassName to set
     */
    public void setEmbeddedModelMetaClassName(String embeddedModelMetaClassName) {
        this.embeddedModelMetaClassName = embeddedModelMetaClassName;
    }

}
