/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
 * Represents primitive type.
 * 
 * @author taedium
 * @since 1.0.0
 * 
 */
public abstract class PrimitiveType extends AbstractDataType {

    /** the primitive wrapper class name */
    protected final String wrapperClassName;

    /**
     * Creates a new {@link PrimitiveType}.
     * 
     * @param className
     *            the class name
     * @param wrapperClassName
     *            the primitive wrapper class name
     */
    public PrimitiveType(String className, String wrapperClassName) {
        super(className, wrapperClassName);
        if (wrapperClassName == null) {
            throw new NullPointerException(
                "The wrapperClassName parameter is null.");
        }
        this.wrapperClassName = wrapperClassName;
    }

    /**
     * @return the wrapperClassName
     */
    public String getWrapperClassName() {
        return wrapperClassName;
    }

    public <R, P, TH extends RuntimeException> R accept(
            DataTypeVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitPrimitiveType(this, p);
    }
}
