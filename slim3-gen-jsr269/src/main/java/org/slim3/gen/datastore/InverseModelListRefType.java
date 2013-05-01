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
 * Represents {@code InverseModelListRef} type.
 * 
 * @author taedium
 * 
 */
public class InverseModelListRefType extends InverseModelRefType {

    /**
     * Creates a new {@code InverseModelRef}.
     * 
     * @param className
     *            the class name
     * @param typeName
     *            the type name
     * @param referenceModelClassName
     *            the reference model class name
     * @param referenceModelTypeName
     *            the reference model type name
     */
    public InverseModelListRefType(String className, String typeName,
            String referenceModelClassName, String referenceModelTypeName) {
        super(
            className,
            typeName,
            referenceModelClassName,
            referenceModelTypeName);
    }

    @Override
    public <R, P, TH extends RuntimeException> R accept(
            DataTypeVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitInverseModelListRefType(this, p);
    }
}
