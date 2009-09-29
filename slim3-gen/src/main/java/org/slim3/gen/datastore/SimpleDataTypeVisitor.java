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

    public R visitReferenceType(ReferenceType type, P p) throws TH {
        return visitDataType(type, p);
    }

    public R visitCoreReferenceType(CoreReferenceType type, P p) throws TH {
        return visitReferenceType(type, p);
    }

    public R visitBooleanType(BooleanType type, P p) throws TH {
        return visitCoreReferenceType(type, p);
    }

    public R visitDoubleType(DoubleType type, P p) throws TH {
        return visitCoreReferenceType(type, p);
    }

    public R visitFloatType(FloatType type, P p) throws TH {
        return visitCoreReferenceType(type, p);
    }

    public R visitIntegerType(IntegerType type, P p) throws TH {
        return visitCoreReferenceType(type, p);
    }

    public R visitLongType(LongType type, P p) throws TH {
        return visitCoreReferenceType(type, p);
    }

    public R visitShortType(ShortType type, P p) throws TH {
        return visitCoreReferenceType(type, p);
    }

    public R visitStringType(StringType type, P p) throws TH {
        return visitCoreReferenceType(type, p);
    }

    public R visitKeyType(KeyType type, P p) throws TH {
        return visitCoreReferenceType(type, p);
    }

    public R visitCategoryType(CategoryType type, P p) throws TH {
        return visitCoreReferenceType(type, p);
    }

    public R visitDateType(DateType type, P p) throws TH {
        return visitCoreReferenceType(type, p);
    }

    public R visitEmailType(EmailType type, P p) throws TH {
        return visitCoreReferenceType(type, p);
    }

    public R visitGeoPtType(GeoPtType type, P p) throws TH {
        return visitCoreReferenceType(type, p);
    }

    public R visitIMHandleType(IMHandleType type, P p) throws TH {
        return visitCoreReferenceType(type, p);
    }

    public R visitLinkType(LinkType type, P p) throws TH {
        return visitCoreReferenceType(type, p);
    }

    public R visitPhoneNumberType(PhoneNumberType type, P p) throws TH {
        return visitCoreReferenceType(type, p);
    }

    public R visitPostalAddressType(PostalAddressType type, P p) throws TH {
        return visitCoreReferenceType(type, p);
    }

    public R visitRatingType(RatingType type, P p) throws TH {
        return visitCoreReferenceType(type, p);
    }

    public R visitShortBlobType(ShortBlobType type, P p) throws TH {
        return visitCoreReferenceType(type, p);
    }

    public R visitUserType(UserType type, P p) throws TH {
        return visitCoreReferenceType(type, p);
    }

    public R visitBlobType(BlobType type, P p) throws TH {
        return visitCoreReferenceType(type, p);
    }

    public R visitTextType(TextType type, P p) throws TH {
        return visitCoreReferenceType(type, p);
    }

    public R visitPrimitiveType(PrimitiveType type, P p) throws TH {
        return visitDataType(type, p);
    }

    public R visitPrimitiveCharType(PrimitiveCharType type, P p) throws TH {
        return visitPrimitiveType(type, p);
    }

    public R visitCorePrimitiveType(CorePrimitiveType type, P p) throws TH {
        return visitPrimitiveType(type, p);
    }

    public R visitPrimitiveByteType(PrimitiveByteType type, P p) throws TH {
        return visitPrimitiveType(type, p);
    }

    public R visitPrimitiveBooleanType(PrimitiveBooleanType type, P p)
            throws TH {
        return visitCorePrimitiveType(type, p);
    }

    public R visitPrimitiveDoubleType(PrimitiveDoubleType type, P p) throws TH {
        return visitCorePrimitiveType(type, p);
    }

    public R visitPrimitiveFloatType(PrimitiveFloatType type, P p) throws TH {
        return visitCorePrimitiveType(type, p);
    }

    public R visitPrimitiveIntType(PrimitiveIntType type, P p) throws TH {
        return visitCorePrimitiveType(type, p);
    }

    public R visitPrimitiveLongType(PrimitiveLongType type, P p) throws TH {
        return visitCorePrimitiveType(type, p);
    }

    public R visitPrimitiveShortType(PrimitiveShortType type, P p) throws TH {
        return visitCorePrimitiveType(type, p);
    }

    public R visitArrayType(ArrayType type, P p) throws TH {
        return visitDataType(type, p);
    }

    public R visitCollectionType(CollectionType type, P p) throws TH {
        return visitDataType(type, p);
    }

    public R visitArrayListType(ArrayListType type, P p) throws TH {
        return visitCollectionType(type, p);
    }

    public R visitHashSetType(HashSetType type, P p) throws TH {
        return visitCollectionType(type, p);
    }

    public R visitLinkedHashSetType(LinkedHashSetType type, P p) throws TH {
        return visitCollectionType(type, p);
    }

    public R visitLinkedListType(LinkedListType type, P p) throws TH {
        return visitCollectionType(type, p);
    }

    public R visitStackType(StackType type, P p) throws TH {
        return visitCollectionType(type, p);
    }

    public R visitTreeSetType(TreeSetType type, P p) throws TH {
        return visitCollectionType(type, p);
    }

    public R visitVectorType(VectorType type, P p) throws TH {
        return visitCollectionType(type, p);
    }

    protected R defaultAction(DataType type, P p) throws TH {
        return defaultValue;
    }

}
