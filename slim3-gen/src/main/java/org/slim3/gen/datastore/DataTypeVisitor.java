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
public interface DataTypeVisitor<R, P, TH extends Throwable> {

    R visitDataType(DataType type, P p) throws TH;

    R visitReferenceType(ReferenceType type, P p) throws TH;

    R visitCoreReferenceType(CoreReferenceType type, P p) throws TH;

    R visitBooleanType(BooleanType type, P p) throws TH;

    R visitByteType(ByteType type, P p) throws TH;

    R visitShortType(ShortType type, P p) throws TH;

    R visitIntegerType(IntegerType type, P p) throws TH;

    R visitLongType(LongType type, P p) throws TH;

    R visitFloatType(FloatType type, P p) throws TH;

    R visitDoubleType(DoubleType type, P p) throws TH;

    R visitStringType(StringType type, P p) throws TH;

    R visitDateType(DateType type, P p) throws TH;

    R visitKeyType(KeyType type, P p) throws TH;

    R visitUserType(UserType type, P p) throws TH;

    R visitCategoryType(CategoryType type, P p) throws TH;

    R visitEmailType(EmailType type, P p) throws TH;

    R visitGeoPtType(GeoPtType type, P p) throws TH;

    R visitIMHandleType(IMHandleType type, P p) throws TH;

    R visitLinkType(LinkType type, P p) throws TH;

    R visitPhoneNumberType(PhoneNumberType type, P p) throws TH;

    R visitPostalAddressType(PostalAddressType type, P p) throws TH;

    R visitRatingType(RatingType type, P p) throws TH;

    R visitShortBlobType(ShortBlobType type, P p) throws TH;

    R visitBlobType(BlobType type, P p) throws TH;

    R visitTextType(TextType type, P p) throws TH;

    R visitPrimitiveType(PrimitiveType type, P p) throws TH;

    R visitPrimitiveCharType(PrimitiveCharType type, P p) throws TH;

    R visitCorePrimitiveType(CorePrimitiveType type, P p) throws TH;

    R visitPrimitiveByteType(PrimitiveByteType type, P p) throws TH;

    R visitPrimitiveShortType(PrimitiveShortType type, P p) throws TH;

    R visitPrimitiveIntType(PrimitiveIntType type, P p) throws TH;

    R visitPrimitiveLongType(PrimitiveLongType type, P p) throws TH;

    R visitPrimitiveFloatType(PrimitiveFloatType type, P p) throws TH;

    R visitPrimitiveDoubleType(PrimitiveDoubleType type, P p) throws TH;

    R visitPrimitiveBooleanType(PrimitiveBooleanType type, P p) throws TH;

    R visitArrayType(ArrayType type, P p) throws TH;

    R visitCollectionType(CollectionType type, P p) throws TH;

    R visitArrayListType(ArrayListType type, P p) throws TH;

    R visitLinkedListType(LinkedListType type, P p) throws TH;

    R visitStackType(StackType type, P p) throws TH;

    R visitVectorType(VectorType type, P p) throws TH;

    R visitHashSetType(HashSetType type, P p) throws TH;

    R visitLinkedHashSetType(LinkedHashSetType type, P p) throws TH;

    R visitTreeSetType(TreeSetType type, P p) throws TH;

}
