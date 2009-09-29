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
package org.slim3.gen.datastore;

/**
 * Represents abstract data type.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public abstract class AbstractDataType implements DataType {

    /** the className */
    protected final String className;

    /** the typeName */
    protected final String typeName;

    /** the serializable */
    protected boolean serialized;

    /**
     * 
     * @param className
     * @param typeName
     */
    public AbstractDataType(String className, String typeName) {
        this.className = className;
        this.typeName = typeName;
    }

    /**
     * @return the name
     */
    public String getClassName() {
        return className;
    }

    /**
     * @return the serializable
     */
    public boolean isSerialized() {
        return serialized;
    }

    /**
     * @param serializable
     *            the serializable to set
     */
    public void setSerialized(boolean serializable) {
        this.serialized = serializable;
    }

    /**
     * @return the className
     */
    public String getTypeName() {
        return typeName;
    }

}
