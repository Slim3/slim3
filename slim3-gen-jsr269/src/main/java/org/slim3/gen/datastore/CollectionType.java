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

import java.util.Collection;

/**
 * Represents {@link Collection} type.
 * 
 * @author taedium
 * @since 1.0.0
 * 
 */
public abstract class CollectionType extends AbstractDataType {

    /** the element data type */
    protected final DataType elementType;

    /**
     * Creates a new {@link CollectionType}.
     * 
     * @param className
     *            the class name
     * @param typeName
     *            the type name
     * @param elementType
     *            the element data type
     */
    public CollectionType(String className, String typeName,
            DataType elementType) {
        super(className, typeName);
        if (elementType == null) {
            throw new NullPointerException("The elementType parameter is null.");
        }
        this.elementType = elementType;
    }

    /**
     * @return the elementType
     */
    public DataType getElementType() {
        return elementType;
    }

    public <R, P, TH extends RuntimeException> R accept(
            DataTypeVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitCollectionType(this, p);
    }

}
