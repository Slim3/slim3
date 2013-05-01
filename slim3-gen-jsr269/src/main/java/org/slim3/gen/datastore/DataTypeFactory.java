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

import static org.slim3.gen.ClassConstants.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleTypeVisitor6;

import org.slim3.gen.message.MessageCode;
import org.slim3.gen.processor.UnknownDeclarationException;
import org.slim3.gen.processor.ValidationException;
import org.slim3.gen.util.TypeUtil;

/**
 * Represents a datastore data type factory.
 * 
 * @author taedium
 * @since 1.0.0
 * 
 */
public class DataTypeFactory {

    /** the supported primitive types */
    protected static final Map<TypeKind, CorePrimitiveType> CORE_PRIMITIVE_TYPES =
        new HashMap<TypeKind, CorePrimitiveType>();
    static {
        CORE_PRIMITIVE_TYPES.put(TypeKind.BOOLEAN, new PrimitiveBooleanType());
        CORE_PRIMITIVE_TYPES.put(TypeKind.SHORT, new PrimitiveShortType());
        CORE_PRIMITIVE_TYPES.put(TypeKind.INT, new PrimitiveIntType());
        CORE_PRIMITIVE_TYPES.put(TypeKind.LONG, new PrimitiveLongType());
        CORE_PRIMITIVE_TYPES.put(TypeKind.FLOAT, new PrimitiveFloatType());
        CORE_PRIMITIVE_TYPES.put(TypeKind.DOUBLE, new PrimitiveDoubleType());
    }

    /** the supported value types */
    protected static final Map<String, CoreReferenceType> CORE_REFERENCE_TYPES =
        new HashMap<String, CoreReferenceType>();
    static {
        CORE_REFERENCE_TYPES.put(String, new StringType());
        CORE_REFERENCE_TYPES.put(Boolean, new BooleanType());
        CORE_REFERENCE_TYPES.put(Short, new ShortType());
        CORE_REFERENCE_TYPES.put(Integer, new IntegerType());
        CORE_REFERENCE_TYPES.put(Long, new LongType());
        CORE_REFERENCE_TYPES.put(Float, new FloatType());
        CORE_REFERENCE_TYPES.put(Double, new DoubleType());
        CORE_REFERENCE_TYPES.put(Date, new DateType());
        CORE_REFERENCE_TYPES.put(User, new UserType());
        CORE_REFERENCE_TYPES.put(Key, new KeyType());
        CORE_REFERENCE_TYPES.put(Category, new CategoryType());
        CORE_REFERENCE_TYPES.put(Email, new EmailType());
        CORE_REFERENCE_TYPES.put(GeoPt, new GeoPtType());
        CORE_REFERENCE_TYPES.put(IMHandle, new IMHandleType());
        CORE_REFERENCE_TYPES.put(Link, new LinkType());
        CORE_REFERENCE_TYPES.put(PhoneNumber, new PhoneNumberType());
        CORE_REFERENCE_TYPES.put(PostalAddress, new PostalAddressType());
        CORE_REFERENCE_TYPES.put(Rating, new RatingType());
        CORE_REFERENCE_TYPES.put(ShortBlob, new ShortBlobType());
        CORE_REFERENCE_TYPES.put(Blob, new BlobType());
        CORE_REFERENCE_TYPES.put(Text, new TextType());
        CORE_REFERENCE_TYPES.put(BlobKey, new BlobKeyType());
    }

    /** the environment */
    final ProcessingEnvironment processingEnv;
    final RoundEnvironment roundEnv;

    /**
     * Creates a new {@link DataTypeFactory}.
     * 
     * @param processingEnv
     *            the processing environment
     * @param roundEnv
     *            the round environment
     */
    public DataTypeFactory(ProcessingEnvironment processingEnv,
            RoundEnvironment roundEnv) {
        this.processingEnv = processingEnv;
        this.roundEnv = roundEnv;
    }

    /**
     * Creates a new {@link DataType}.
     * 
     * @param fieldElement
     *            the declaration
     * @param typeMirror
     *            the typemirror
     * @return a new {@link DataType}
     */
    public DataType createDataType(VariableElement fieldElement,
            TypeMirror typeMirror) {
        if (fieldElement == null) {
            throw new NullPointerException("The declaration parameter is null.");
        }
        if (typeMirror == null) {
            throw new NullPointerException("The typeMirror parameter is null.");
        }
        return createDataTypeInternal(fieldElement, typeMirror);
    }

    /**
     * Creates a new {@link DataType} internally.
     * 
     * @param fieldElement
     *            the declaration
     * @param typeMirror
     *            the typemirror
     * @return a new {@link DataType}
     */
    protected DataType createDataTypeInternal(Element fieldElement,
            TypeMirror typeMirror) {
        Builder builder = new Builder(fieldElement, typeMirror);
        return builder.build();
    }

    /**
     * The datastore data type builder.
     * 
     * @author taedium
     * @since 1.0.0
     * 
     */
    protected class Builder extends SimpleTypeVisitor6<Void, Void> {

        /** the declaration */
        protected final Element declaration;

        /** the typemirror */
        protected final TypeMirror typeMirror;

        /** the datastore type */
        protected AbstractDataType dataType;

        /**
         * Creates a new {@link Builder}
         * 
         * @param fieldElement
         *            the declaration
         * @param typeMirror
         *            the typemirror
         */
        public Builder(Element fieldElement, TypeMirror typeMirror) {
            this.declaration = fieldElement;
            this.typeMirror = typeMirror;
        }

        /**
         * Builds a {@link DataType}.
         * 
         * @return a datastore type
         */
        public DataType build() {
            typeMirror.accept(this, null);
            return dataType;
        }

        @Override
        public Void visitArray(final ArrayType arrayType, Void p) {
            final TypeMirror componentType = arrayType.getComponentType();
            componentType.accept(new SimpleTypeVisitor6<Void, Void>() {

                @Override
                public Void visitPrimitive(PrimitiveType primitiveType, Void p) {
                    TypeKind kind = primitiveType.getKind();
                    if (kind == TypeKind.BYTE) {
                        dataType =
                            new org.slim3.gen.datastore.ArrayType(
                                primitive_byte + "[]",
                                arrayType.toString(),
                                new PrimitiveByteType());
                        return null;
                    }
                    return super.visitPrimitive(primitiveType, p);
                }

                @Override
                protected Void defaultAction(TypeMirror typemirror, Void p) {
                    dataType =
                        new org.slim3.gen.datastore.ArrayType(
                            typeMirror.toString(),
                            arrayType.toString(),
                            new OtherReferenceType(
                                typeMirror.toString(),
                                typeMirror.toString()));
                    return null;
                }
            },
                null);
            return null;
        }

        @Override
        public Void visitDeclared(DeclaredType declaredType, Void p) {
            TypeElement typeDeclaration = toTypeDeclaration(declaredType);
            String className = typeDeclaration.getQualifiedName().toString();
            dataType = getCoreReferenceType(className, declaredType);
            if (dataType != null) {
                return null;
            }
            dataType = getModelRefType(className, declaredType);
            if (dataType != null) {
                return null;
            }
            dataType = getInverseModelRefType(className, declaredType);
            if (dataType != null) {
                return null;
            }
            dataType = getInverseModelListRefType(className, declaredType);
            if (dataType != null) {
                return null;
            }
            dataType = getCollectionType(className, declaredType);
            if (dataType != null) {
                return null;
            }
            dataType =
                new OtherReferenceType(className, declaredType.toString());
            return null;
        }

        @Override
        public Void visitPrimitive(PrimitiveType primitiveType, Void p) {
            TypeKind kind = primitiveType.getKind();
            dataType = getCorePrimitiveType(kind);
            if (dataType != null) {
                return null;
            }
            throw new ValidationException(
                MessageCode.SLIM3GEN1002,
                declaration,
                kind.name().toLowerCase());
        }

        @Override
        protected Void defaultAction(TypeMirror typeMirror, Void p) {
            throw new AssertionError("unreachable");
        }

        /**
         * Converts {@link DeclaredType} to {@link TypeElement}.
         * 
         * @param declaredType
         *            the declared type
         * @return a type declaration
         */
        protected TypeElement toTypeDeclaration(DeclaredType declaredType) {
            TypeElement typeElement = (TypeElement) declaredType.asElement();
            if (typeElement != null) {
                return typeElement;
            }
            throw new UnknownDeclarationException(
                declaration,
                declaredType.asElement());
        }

        /**
         * Returns a core reference type.
         * 
         * @param className
         *            the class name
         * @param declaredType
         *            the declared type
         * @return a core reference type
         */
        protected CoreReferenceType getCoreReferenceType(
                final String className, DeclaredType declaredType) {
            if (CORE_REFERENCE_TYPES.containsKey(className)) {
                return CORE_REFERENCE_TYPES.get(className);
            }
            if (declaredType.asElement().getKind() == ElementKind.ENUM) {
                return new org.slim3.gen.datastore.EnumType(className);
            }
            return null;
        }

        /**
         * Returns a model ref type.
         * 
         * @param className
         *            the class name
         * @param declaredType
         *            the declared type
         * @return a model ref type
         */
        protected ModelRefType getModelRefType(String className,
                DeclaredType declaredType) {
            DeclaredType referenceModelType =
                getReferenceModelType(className, declaredType, ModelRef);
            if (referenceModelType == null) {
                return null;
            }
            TypeElement referenceModelDeclaration =
                toTypeDeclaration(referenceModelType);
            return new ModelRefType(
                className,
                declaredType.toString(),
                referenceModelDeclaration.getQualifiedName().toString(),
                referenceModelType.toString());
        }

        /**
         * Returns an inverse model ref type.
         * 
         * @param className
         *            the class name
         * @param declaredType
         *            the declared type
         * @return an inverse model ref type
         */
        protected InverseModelRefType getInverseModelRefType(String className,
                DeclaredType declaredType) {
            DeclaredType referenceModelType =
                getReferenceModelType(className, declaredType, InverseModelRef);
            if (referenceModelType == null) {
                return null;
            }
            TypeElement referenceModelDeclaration =
                toTypeDeclaration(referenceModelType);
            return new InverseModelRefType(
                className,
                declaredType.toString(),
                referenceModelDeclaration.getQualifiedName().toString(),
                referenceModelType.toString());
        }

        /**
         * Returns an inverse model list ref type.
         * 
         * @param className
         *            the class name
         * @param declaredType
         *            the declared type
         * @return an inverse model list ref type
         */
        protected InverseModelListRefType getInverseModelListRefType(
                String className, DeclaredType declaredType) {
            DeclaredType referenceModelType =
                getReferenceModelType(
                    className,
                    declaredType,
                    InverseModelListRef);
            if (referenceModelType == null) {
                return null;
            }
            TypeElement referenceModelDeclaration =
                toTypeDeclaration(referenceModelType);
            return new InverseModelListRefType(
                className,
                declaredType.toString(),
                referenceModelDeclaration.getQualifiedName().toString(),
                referenceModelType.toString());
        }

        /**
         * Returns reference model type.
         * 
         * @param className
         *            the class name
         * @param declaredType
         *            the declared type
         * @param superclassName
         *            the superclass name
         * @return a reference model type
         */
        protected DeclaredType getReferenceModelType(String className,
                DeclaredType declaredType, String superclassName) {
            DeclaredType supertype =
                TypeUtil.getSuperDeclaredType(declaredType, superclassName);
            if (supertype == null || supertype.getTypeArguments().isEmpty()) {
                return null;
            }
            return TypeUtil.toDeclaredType(supertype
                .getTypeArguments()
                .iterator()
                .next());
        }

        /**
         * Returns a core primitive type.
         * 
         * @param kind
         *            the primitive kind
         * @return a core reference type
         */
        protected CorePrimitiveType getCorePrimitiveType(TypeKind kind) {
            return CORE_PRIMITIVE_TYPES.get(kind);
        }

        /**
         * Returns the core primitive type.
         * 
         * @param className
         *            the class name
         * @param declaredType
         *            the declaredType
         * @return the collection type
         */
        protected CollectionType getCollectionType(String className,
                DeclaredType declaredType) {
            if (!processingEnv.getTypeUtils().isSubtype(
                processingEnv.getTypeUtils().erasure(declaredType),
                processingEnv.getTypeUtils().erasure(
                    processingEnv
                        .getElementUtils()
                        .getTypeElement(Collection.class.getCanonicalName())
                        .asType()))) {
                return null;
            }
            Collection<? extends TypeMirror> typeArgs =
                declaredType.getTypeArguments();
            if (typeArgs.isEmpty()) {
                throw new ValidationException(
                    MessageCode.SLIM3GEN1004,
                    declaration,
                    declaredType);
            }
            TypeMirror elementType = typeArgs.iterator().next();
            DeclaredType elementDeclaredType =
                TypeUtil.toDeclaredType(elementType);
            if (elementDeclaredType == null) {
                throw new ValidationException(
                    MessageCode.SLIM3GEN1016,
                    declaration,
                    elementType);
            }
            TypeElement elementDeclaration =
                toTypeDeclaration(elementDeclaredType);
            CoreReferenceType elementCoreReferenceType =
                getCoreReferenceType(elementDeclaration
                    .getQualifiedName()
                    .toString(), elementDeclaredType);
            if (elementCoreReferenceType != null) {
                return createCollectionType(
                    className,
                    declaredType,
                    elementCoreReferenceType);
            }
            DataType elementDataType =
                createDataTypeInternal(elementDeclaration, elementType);
            CollectionType collectionType =
                createCollectionType(className, declaredType, elementDataType);
            return collectionType;
        }

        /**
         * Creates a {@link CollectionType}.
         * 
         * @param className
         *            the class name
         * @param declaredType
         *            the declaredType
         * @param elementType
         *            the element data type
         * @return a collection data type
         */
        protected CollectionType createCollectionType(String className,
                DeclaredType declaredType, DataType elementType) {
            String typeName = declaredType.toString();
            if (List.equals(className)) {
                return new ListType(className, typeName, elementType);
            }
            if (ArrayList.equals(className)) {
                return new ArrayListType(className, typeName, elementType);
            }
            if (LinkedList.equals(className)) {
                return new LinkedListType(className, typeName, elementType);
            }
            if (Set.equals(className)) {
                return new SetType(className, typeName, elementType);
            }
            if (HashSet.equals(className)) {
                return new HashSetType(className, typeName, elementType);
            }
            if (LinkedHashSet.equals(className)) {
                return new LinkedHashSetType(className, typeName, elementType);
            }
            if (SortedSet.equals(className)) {
                return new SortedSetType(className, typeName, elementType);
            }
            if (TreeSet.equals(className)) {
                return new TreeSetType(className, typeName, elementType);
            }
            throw new ValidationException(
                MessageCode.SLIM3GEN1002,
                declaration,
                className);
        }
    }
}
