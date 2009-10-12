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

import org.slim3.gen.datastore.DataType;

/**
 * Represents an attribute meta description.
 * 
 * @author taedium
 * @since 3.0
 */
public class AttributeMetaDesc {

    /** the name */
    protected final String name;

    /** the datastore data type */
    protected final DataType dataType;

    /** the property name */
    protected final String propertyName;

    /** the primaryKey */
    protected boolean primaryKey;

    /** the lob */
    protected boolean lob;

    /** the version */
    protected boolean version;

    /** the impermanent */
    protected boolean impermanent;

    /** the unindexed */
    protected boolean unindexed;

    /** the readMethodName */
    protected String readMethodName;

    /** the writeMethodName */
    protected String writeMethodName;

    /**
     * Creates a new {@code AttributeMetaDesc}.
     * 
     * @param name
     *            the name
     * @param dataType
     *            the data Type
     * @param propertyName
     *            the property name
     */
    public AttributeMetaDesc(String name, DataType dataType, String propertyName) {
        if (name == null) {
            throw new NullPointerException("The name parameter is null.");
        }
        if (dataType == null) {
            throw new NullPointerException("The dataType parameter is null.");
        }
        if (propertyName == null) {
            throw new NullPointerException(
                "The propertyName parameter is null.");
        }
        this.name = name;
        this.dataType = dataType;
        this.propertyName = propertyName;
    }

    /**
     * @return the dataType
     */
    public DataType getDataType() {
        return dataType;
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
     * @return the lob
     */
    public boolean isLob() {
        return lob;
    }

    /**
     * @param lob
     *            the lob
     */
    public void setLob(boolean lob) {
        this.lob = lob;
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

    /**
     * Returns the property name
     * 
     * @return the property name
     */
    public String getPropertyName() {
        return propertyName;
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

}
