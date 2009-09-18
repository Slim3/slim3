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
    protected final String name;

    /** the attributeClassName */
    protected String typeName;

    /** the declaredTypeName */
    protected String declaredTypeName;

    /** the implicationTypeName */
    protected String implicationTypeName;

    /** the wrapperTypeName */
    protected String wrapperTypeName;

    /** the elementTypeName */
    protected String elementTypeName;

    /** the primaryKey */
    protected boolean primaryKey;

    /** the shortBlob */
    protected boolean shortBlob;

    /** the blob */
    protected boolean blob;

    /** the text */
    protected boolean text;

    /** the version */
    protected boolean version;

    /** the impermanent */
    protected boolean impermanent;

    /** the unindexed */
    protected boolean unindexed;

    /** the collection */
    protected boolean collection;

    /** the array */
    protected boolean array;

    /** the primitive */
    protected boolean primitive;

    /** the interfase */
    protected boolean interfase;

    /** the serialized */
    protected boolean serialized;

    /** the readMethodName */
    protected String readMethodName;

    /** the writeMethodName */
    protected String writeMethodName;

    /**
     * Creates a new {@code AttributeMetaDesc}.
     * 
     * @param name
     *            the name
     */
    public AttributeMetaDesc(String name) {
        this.name = name;
    }

    /**
     * @return the typeName
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * @param typeName
     *            the typeName to set
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * @return the declaredTypeName
     */
    public String getDeclaredTypeName() {
        return declaredTypeName;
    }

    /**
     * @param declaredTypeName
     *            the declaredTypeName to set
     */
    public void setDeclaredTypeName(String declaredTypeName) {
        this.declaredTypeName = declaredTypeName;
    }

    /**
     * @return the implicationTypeName
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
     * @return the shortBlob
     */
    public boolean isShortBlob() {
        return shortBlob;
    }

    /**
     * @param shortBlob
     *            the shortBlob to set
     */
    public void setShortBlob(boolean shortBlob) {
        this.shortBlob = shortBlob;
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
     * @return the interfase
     */
    public boolean isInterfase() {
        return interfase;
    }

    /**
     * @param interfase
     *            the interfase to set
     */
    public void setInterfase(boolean interfase) {
        this.interfase = interfase;
    }

    /**
     * @return the serialized
     */
    public boolean isSerialized() {
        return serialized;
    }

    /**
     * @param serialized
     *            the serialized to set
     */
    public void setSerialized(boolean serialized) {
        this.serialized = serialized;
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "AttributeMetaDesc [array="
            + array
            + ", blob="
            + blob
            + ", collection="
            + collection
            + ", declaredTypeName="
            + declaredTypeName
            + ", elementTypeName="
            + elementTypeName
            + ", impermanent="
            + impermanent
            + ", implicationTypeName="
            + implicationTypeName
            + ", interfase="
            + interfase
            + ", name="
            + name
            + ", primaryKey="
            + primaryKey
            + ", primitive="
            + primitive
            + ", readMethodName="
            + readMethodName
            + ", serialized="
            + serialized
            + ", shortBlob="
            + shortBlob
            + ", text="
            + text
            + ", typeName="
            + typeName
            + ", unindexed="
            + unindexed
            + ", version="
            + version
            + ", wrapperTypeName="
            + wrapperTypeName
            + ", writeMethodName="
            + writeMethodName
            + "]";
    }

}
