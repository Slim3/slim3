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
 * Represents visitor for {@link DataType}.
 * 
 * @author taedium
 * @param <R>
 *            the return type
 * @param <P>
 *            the parameter type
 * @param <TH>
 *            the throwable type
 * @since 1.0.0
 * 
 */
public interface DataTypeVisitor<R, P, TH extends RuntimeException> {

    /**
     * Visits {@link DataType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitDataType(DataType type, P p) throws TH;

    /**
     * Visits {@link ReferenceType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitReferenceType(ReferenceType type, P p) throws TH;

    /**
     * Visits {@link CoreReferenceType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitCoreReferenceType(CoreReferenceType type, P p) throws TH;

    /**
     * Visits {@link ModelRefType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitModelRefType(ModelRefType type, P p) throws TH;

    /**
     * Visits {@link InverseModelRefType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitInverseModelRefType(InverseModelRefType type, P p) throws TH;

    /**
     * Visits {@link InverseModelListRefType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitInverseModelListRefType(InverseModelListRefType type, P p) throws TH;

    /**
     * Visits {@link OtherReferenceType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitOtherReferenceType(OtherReferenceType type, P p) throws TH;

    /**
     * Visits {@link BooleanType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitBooleanType(BooleanType type, P p) throws TH;

    /**
     * Visits {@link ShortType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitShortType(ShortType type, P p) throws TH;

    /**
     * Visits {@link IntegerType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitIntegerType(IntegerType type, P p) throws TH;

    /**
     * Visits {@link LongType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitLongType(LongType type, P p) throws TH;

    /**
     * Visits {@link FloatType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitFloatType(FloatType type, P p) throws TH;

    /**
     * Visits {@link DoubleType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitDoubleType(DoubleType type, P p) throws TH;

    /**
     * Visits {@link StringType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitStringType(StringType type, P p) throws TH;

    /**
     * Visits {@link DateType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitDateType(DateType type, P p) throws TH;

    /**
     * Visits {@link KeyType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitKeyType(KeyType type, P p) throws TH;

    /**
     * Visits {@link UserType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitUserType(UserType type, P p) throws TH;

    /**
     * Visits {@link CategoryType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitCategoryType(CategoryType type, P p) throws TH;

    /**
     * Visits {@link EmailType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitEmailType(EmailType type, P p) throws TH;

    /**
     * Visits {@link GeoPtType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitGeoPtType(GeoPtType type, P p) throws TH;

    /**
     * Visits {@link IMHandleType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitIMHandleType(IMHandleType type, P p) throws TH;

    /**
     * Visits {@link LinkType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitLinkType(LinkType type, P p) throws TH;

    /**
     * Visits {@link PhoneNumberType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitPhoneNumberType(PhoneNumberType type, P p) throws TH;

    /**
     * Visits {@link PostalAddressType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitPostalAddressType(PostalAddressType type, P p) throws TH;

    /**
     * Visits {@link RatingType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitRatingType(RatingType type, P p) throws TH;

    /**
     * Visits {@link ShortBlobType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitShortBlobType(ShortBlobType type, P p) throws TH;

    /**
     * Visits {@link BlobType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitBlobType(BlobType type, P p) throws TH;

    /**
     * Visits {@link TextType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitTextType(TextType type, P p) throws TH;

    /**
     * Visits {@link BlobKeyType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitBlobKeyType(BlobKeyType type, P p) throws TH;

    /**
     * Visits {@link EnumType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitEnumType(EnumType type, P p) throws TH;

    /**
     * Visits {@link PrimitiveType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitPrimitiveType(PrimitiveType type, P p) throws TH;

    /**
     * Visits {@link PrimitiveCharType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitPrimitiveCharType(PrimitiveCharType type, P p) throws TH;

    /**
     * Visits {@link CorePrimitiveType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitCorePrimitiveType(CorePrimitiveType type, P p) throws TH;

    /**
     * Visits {@link PrimitiveByteType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitPrimitiveByteType(PrimitiveByteType type, P p) throws TH;

    /**
     * Visits {@link PrimitiveShortType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitPrimitiveShortType(PrimitiveShortType type, P p) throws TH;

    /**
     * Visits {@link PrimitiveIntType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitPrimitiveIntType(PrimitiveIntType type, P p) throws TH;

    /**
     * Visits {@link PrimitiveLongType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitPrimitiveLongType(PrimitiveLongType type, P p) throws TH;

    /**
     * Visits {@link PrimitiveFloatType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitPrimitiveFloatType(PrimitiveFloatType type, P p) throws TH;

    /**
     * Visits {@link PrimitiveDoubleType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitPrimitiveDoubleType(PrimitiveDoubleType type, P p) throws TH;

    /**
     * Visits {@link PrimitiveBooleanType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitPrimitiveBooleanType(PrimitiveBooleanType type, P p) throws TH;

    /**
     * Visits {@link ArrayType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitArrayType(ArrayType type, P p) throws TH;

    /**
     * Visits {@link CollectionType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitCollectionType(CollectionType type, P p) throws TH;

    /**
     * Visits {@link ListType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitListType(ListType type, P p) throws TH;

    /**
     * Visits {@link ArrayListType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitArrayListType(ArrayListType type, P p) throws TH;

    /**
     * Visits {@link LinkedListType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitLinkedListType(LinkedListType type, P p) throws TH;

    /**
     * Visits {@link SetType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitSetType(SetType type, P p) throws TH;

    /**
     * Visits {@link HashSetType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitHashSetType(HashSetType type, P p) throws TH;

    /**
     * Visits {@link LinkedHashSetType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitLinkedHashSetType(LinkedHashSetType type, P p) throws TH;

    /**
     * Visits {@link SortedSetType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitSortedSetType(SortedSetType type, P p) throws TH;

    /**
     * Visits {@link TreeSetType}.
     * 
     * @param type
     *            the data type
     * @param p
     *            the parameter
     * @return a result
     * @throws TH
     *             the throwable
     */
    R visitTreeSetType(TreeSetType type, P p) throws TH;

}
