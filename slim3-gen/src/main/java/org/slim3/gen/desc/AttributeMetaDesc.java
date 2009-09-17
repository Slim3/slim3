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

    protected String declaredTypeName;

    protected String implicationTypeName;

    protected String elementTypeName;

    protected boolean primaryKey;

    protected boolean shortBlob;

    protected boolean blob;

    protected boolean text;

    protected boolean version;

    protected boolean impermanent;

    protected boolean unindexed;

    protected boolean collection;

    protected boolean array;

    protected boolean primitive;

    protected boolean interfase;

    protected boolean serialized;

    protected String readMethodName;

    protected String writeMethodName;

    @Deprecated
    /** {@code true} if this attribute is embedded. */
    protected boolean embedded = false;

    @Deprecated
    /** the embeddedModelMetaClassName */
    protected String embeddedModelMetaClassName;

    public AttributeMetaDesc(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDeclaredTypeName() {
        return declaredTypeName;
    }

    public void setDeclaredTypeName(String declaredTypeName) {
        this.declaredTypeName = declaredTypeName;
    }

    public String getImplicationTypeName() {
        return implicationTypeName;
    }

    public void setImplicationTypeName(String implicationTypeName) {
        this.implicationTypeName = implicationTypeName;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isShortBlob() {
        return shortBlob;
    }

    public void setShortBlob(boolean shortBlob) {
        this.shortBlob = shortBlob;
    }

    public boolean isBlob() {
        return blob;
    }

    public void setBlob(boolean blob) {
        this.blob = blob;
    }

    public boolean isText() {
        return text;
    }

    public void setText(boolean text) {
        this.text = text;
    }

    public boolean isVersion() {
        return version;
    }

    public void setVersion(boolean version) {
        this.version = version;
    }

    public boolean isImpermanent() {
        return impermanent;
    }

    public void setImpermanent(boolean impermanent) {
        this.impermanent = impermanent;
    }

    public boolean isUnindexed() {
        return unindexed;
    }

    public void setUnindexed(boolean unindexed) {
        this.unindexed = unindexed;
    }

    public boolean isCollection() {
        return collection;
    }

    public void setCollection(boolean collection) {
        this.collection = collection;
    }

    public boolean isPrimitive() {
        return primitive;
    }

    public void setPrimitive(boolean primitive) {
        this.primitive = primitive;
    }

    public boolean isInterfase() {
        return interfase;
    }

    public void setInterfase(boolean interfase) {
        this.interfase = interfase;
    }

    public boolean isSerialized() {
        return serialized;
    }

    public void setSerialized(boolean serialized) {
        this.serialized = serialized;
    }

    public String getReadMethodName() {
        return readMethodName;
    }

    public void setReadMethodName(String readMethodName) {
        this.readMethodName = readMethodName;
    }

    public String getWriteMethodName() {
        return writeMethodName;
    }

    public void setWriteMethodName(String writeMethodName) {
        this.writeMethodName = writeMethodName;
    }

    public String getElementTypeName() {
        return elementTypeName;
    }

    public void setElementTypeName(String elementTypeName) {
        this.elementTypeName = elementTypeName;
    }

    public boolean isArray() {
        return array;
    }

    public void setArray(boolean array) {
        this.array = array;
    }

    public boolean isEmbedded() {
        return embedded;
    }

    public void setEmbedded(boolean embedded) {
        this.embedded = embedded;
    }

    public String getEmbeddedModelMetaClassName() {
        return embeddedModelMetaClassName;
    }

    public void setEmbeddedModelMetaClassName(String embeddedModelMetaClassName) {
        this.embeddedModelMetaClassName = embeddedModelMetaClassName;
    }

}
