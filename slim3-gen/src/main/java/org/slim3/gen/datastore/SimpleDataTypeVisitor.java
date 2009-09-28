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
 * @author taedium
 * 
 */
public class SimpleDataTypeVisitor<R, P, TH extends Throwable> implements
        DataTypeVisitor<R, P, TH> {

    protected final R defaultValue;

    public SimpleDataTypeVisitor() {
        this(null);
    }

    public SimpleDataTypeVisitor(R defaultValue) {
        this.defaultValue = defaultValue;
    }

    public R visitDataType(DataType type, P p) throws TH {
        return defaultAction(type, p);
    }

    public R visitAnyType(AnyType type, P p) throws TH {
        return visitDataType(type, p);
    }

    public R visitArrayType(ArrayType type, P p) throws TH {
        return visitDataType(type, p);
    }

    public R visitCollectionType(CollectionType type, P p) throws TH {
        return visitDataType(type, p);
    }

    public R visitKeyType(KeyType type, P p) throws TH {
        return visitCoreType(type, p);
    }

    public R visitPrimitiveType(PrimitiveType type, P p) throws TH {
        return visitDataType(type, p);
    }

    public R visitCoreType(CoreType type, P p) throws TH {
        return visitDataType(type, p);
    }

    public R visitPrimitiveByteType(PrimitiveByteType type, P p) throws TH {
        return visitPrimitiveType(type, p);
    }

    public R visitStringType(StringType type, P p) throws TH {
        return visitCoreType(type, p);
    }

    protected R defaultAction(DataType type, P p) throws TH {
        return defaultValue;
    }

}
