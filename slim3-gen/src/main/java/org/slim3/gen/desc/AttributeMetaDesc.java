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

    protected boolean primaryKey;

    protected boolean blob;

    protected boolean text;

    protected boolean version;

    protected boolean impermanent;

    protected boolean unindexed;

    protected boolean collection;

    protected String readMethodName;

    protected String writeMethodName;

    public AttributeMetaDesc() {
        super();
    }

    public AttributeMetaDesc(String name, String attributeClassName) {
        throw new AssertionError("not yet implemented.");
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

    /**
     * @return the readMethodName
     */
    public String getReadMethodName() {
        return readMethodName;
    }

    /**
     * @param readMethodName
     *            the readMethodName to set
     */
    public void setReadMethodName(String readMethodName) {
        this.readMethodName = readMethodName;
    }

    /**
     * @return the writeMethodName
     */
    public String getWriteMethodName() {
        return writeMethodName;
    }

    /**
     * @param writeMethodName
     *            the writeMethodName to set
     */
    public void setWriteMethodName(String writeMethodName) {
        this.writeMethodName = writeMethodName;
    }

    /**
     * @return the primaryKey
     */
    public boolean isPrimaryKey() {
        return primaryKey;
    }

    /**
     * @param primaryKey
     *            the primaryKey to set
     */
    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * @return the blob
     */
    public boolean isBlob() {
        return blob;
    }

    /**
     * @param blob
     *            the blob to set
     */
    public void setBlob(boolean blob) {
        this.blob = blob;
    }

    /**
     * @return the text
     */
    public boolean isText() {
        return text;
    }

    /**
     * @param text
     *            the text to set
     */
    public void setText(boolean text) {
        this.text = text;
    }

    /**
     * @return the version
     */
    public boolean isVersion() {
        return version;
    }

    /**
     * @param version
     *            the version to set
     */
    public void setVersion(boolean version) {
        this.version = version;
    }

    /**
     * @return the impermanent
     */
    public boolean isImpermanent() {
        return impermanent;
    }

    /**
     * @param impermanent
     *            the impermanent to set
     */
    public void setImpermanent(boolean impermanent) {
        this.impermanent = impermanent;
    }

    /**
     * @return the unindexed
     */
    public boolean isUnindexed() {
        return unindexed;
    }

    /**
     * @param unindexed
     *            the unindexed to set
     */
    public void setUnindexed(boolean unindexed) {
        this.unindexed = unindexed;
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

}
