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

import static org.slim3.gen.ClassConstants.ArrayList;
import static org.slim3.gen.ClassConstants.Blob;
import static org.slim3.gen.ClassConstants.Boolean;
import static org.slim3.gen.ClassConstants.Category;
import static org.slim3.gen.ClassConstants.Date;
import static org.slim3.gen.ClassConstants.Double;
import static org.slim3.gen.ClassConstants.Email;
import static org.slim3.gen.ClassConstants.Float;
import static org.slim3.gen.ClassConstants.GeoPt;
import static org.slim3.gen.ClassConstants.HashSet;
import static org.slim3.gen.ClassConstants.IMHandle;
import static org.slim3.gen.ClassConstants.Integer;
import static org.slim3.gen.ClassConstants.Key;
import static org.slim3.gen.ClassConstants.Link;
import static org.slim3.gen.ClassConstants.List;
import static org.slim3.gen.ClassConstants.Long;
import static org.slim3.gen.ClassConstants.PhoneNumber;
import static org.slim3.gen.ClassConstants.PostalAddress;
import static org.slim3.gen.ClassConstants.Rating;
import static org.slim3.gen.ClassConstants.Set;
import static org.slim3.gen.ClassConstants.Short;
import static org.slim3.gen.ClassConstants.ShortBlob;
import static org.slim3.gen.ClassConstants.SortedSet;
import static org.slim3.gen.ClassConstants.String;
import static org.slim3.gen.ClassConstants.Text;
import static org.slim3.gen.ClassConstants.TreeSet;
import static org.slim3.gen.ClassConstants.User;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slim3.gen.message.MessageCode;
import org.slim3.gen.processor.UnknownDeclarationException;
import org.slim3.gen.processor.ValidationException;
import org.slim3.gen.util.TypeUtil;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.ArrayType;
import com.sun.mirror.type.DeclaredType;
import com.sun.mirror.type.PrimitiveType;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.type.PrimitiveType.Kind;
import com.sun.mirror.util.SimpleTypeVisitor;

/**
 * Represents a datastore data type factory.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class DataTypeFactory {

    /** the supported primitive types */
    protected static final Map<Kind, CorePrimitiveType> CORE_PRIMITIVE_TYPES =
        new HashMap<Kind, CorePrimitiveType>();
    static {
        CORE_PRIMITIVE_TYPES.put(Kind.BOOLEAN, new PrimitiveBooleanType());
        CORE_PRIMITIVE_TYPES.put(Kind.SHORT, new PrimitiveShortType());
        CORE_PRIMITIVE_TYPES.put(Kind.INT, new PrimitiveIntType());
        CORE_PRIMITIVE_TYPES.put(Kind.LONG, new PrimitiveLongType());
        CORE_PRIMITIVE_TYPES.put(Kind.FLOAT, new PrimitiveFloatType());
        CORE_PRIMITIVE_TYPES.put(Kind.DOUBLE, new PrimitiveLongType());
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
    }

    /** the environment */
    protected final AnnotationProcessorEnvironment env;

    /**
     * Creates a new {@link DataTypeFactory}.
     * 
     * @param env
     *            the environment
     */
    public DataTypeFactory(AnnotationProcessorEnvironment env) {
        this.env = env;
    }

    /**
     * Creates a new {@link DataType}.
     * 
     * @param declaration
     *            the declaration
     * @param typeMirror
     *            the typemirror
     * @return a new {@link DataType}
     */
    public DataType createDatastoreType(Declaration declaration,
            TypeMirror typeMirror) {
        if (declaration == null) {
            throw new NullPointerException("The declaration parameter is null.");
        }
        if (typeMirror == null) {
            throw new NullPointerException("The typeMirror parameter is null.");
        }
        Builder builder = new Builder(declaration, typeMirror);
        return builder.build();
    }

    /**
     * The datastore data type builder.
     * 
     * @author taedium
     * @since 3.0
     * 
     */
    protected class Builder extends SimpleTypeVisitor {

        /** the declaration */
        protected final Declaration declaration;

        /** the typemirror */
        protected final TypeMirror typeMirror;

        /** the datastore type */
        protected AbstractDataType dataType;

        /**
         * Creates a new {@link Builder}
         * 
         * @param declaration
         *            the declaration
         * @param typeMirror
         *            the typemirror
         */
        public Builder(Declaration declaration, TypeMirror typeMirror) {
            this.declaration = declaration;
            this.typeMirror = typeMirror;
        }

        /**
         * Builds a {@link DataType}.
         * 
         * @return a datastore type
         */
        public DataType build() {
            typeMirror.accept(this);
            return dataType;
        }

        @Override
        public void visitArrayType(final ArrayType arrayType) {
            final TypeMirror componentType = arrayType.getComponentType();
            componentType.accept(new SimpleTypeVisitor() {

                @Override
                public void visitPrimitiveType(PrimitiveType primitiveType) {
                    Kind kind = primitiveType.getKind();
                    String componentClassName = kind.name().toLowerCase();
                    String className = componentClassName + "[]";
                    if (kind == Kind.BYTE) {
                        dataType =
                            new org.slim3.gen.datastore.ArrayType(
                                className,
                                arrayType.toString(),
                                new PrimitiveByteType());
                        return;
                    }
                    super.visitPrimitiveType(primitiveType);
                }

                @Override
                public void visitTypeMirror(TypeMirror typemirror) {
                    throw new ValidationException(
                        MessageCode.SILM3GEN1005,
                        env,
                        declaration,
                        arrayType);
                }
            });
        }

        @Override
        public void visitDeclaredType(DeclaredType declaredType) {
            TypeDeclaration typeDeclaration = toTypeDeclaration(declaredType);
            String className = typeDeclaration.getQualifiedName();
            dataType = getCoreReferenceType(className);
            if (dataType != null) {
                return;
            }
            dataType = getCollectionType(className, declaredType);
            if (dataType != null) {
                return;
            }
            if (isSerializable(declaredType)) {
                dataType =
                    new ReferenceType(className, declaredType.toString());
                dataType.setSerialized(true);
                return;
            }
            throw new ValidationException(
                MessageCode.SILM3GEN1002,
                env,
                typeDeclaration,
                className);
        }

        @Override
        public void visitPrimitiveType(PrimitiveType primitiveType) {
            Kind kind = primitiveType.getKind();
            dataType = getCorePrimitiveType(kind);
            if (dataType != null) {
                return;
            }
            throw new ValidationException(
                MessageCode.SILM3GEN1002,
                env,
                declaration,
                kind.name().toLowerCase());
        }

        @Override
        public void visitTypeMirror(TypeMirror typeMirror) {
            throw new AssertionError("unreachable");
        }

        /**
         * Converts {@link DeclaredType} to {@link TypeDeclaration}.
         * 
         * @param declaredType
         *            the declared type
         * @return a type declaration
         */
        protected TypeDeclaration toTypeDeclaration(DeclaredType declaredType) {
            TypeDeclaration typeDeclaration = declaredType.getDeclaration();
            if (typeDeclaration != null) {
                return typeDeclaration;
            }
            throw new UnknownDeclarationException(
                env,
                declaration,
                declaredType);
        }

        /**
         * Returns {@code true} if {@code typeMirror} is serializable.
         * 
         * @param typeMirror
         *            the typemirror
         * @return {@code true} if {@code typeMirror} is serializable, otherwise
         *         {@code false}.
         */
        protected boolean isSerializable(TypeMirror typeMirror) {
            return TypeUtil.isSubtype(env, typeMirror, Serializable.class);
        }

        /**
         * Returns a core reference type.
         * 
         * @param className
         *            the class name
         * @return a core reference type
         */
        protected CoreReferenceType getCoreReferenceType(String className) {
            return CORE_REFERENCE_TYPES.get(className);
        }

        /**
         * Returns a core primitive type.
         * 
         * @param kind
         *            the primitive kind
         * @return a core reference type
         */
        protected CorePrimitiveType getCorePrimitiveType(Kind kind) {
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
            if (!TypeUtil.isSubtype(env, declaredType, Collection.class)) {
                return null;
            }
            Collection<TypeMirror> typeArgs =
                declaredType.getActualTypeArguments();
            if (typeArgs.isEmpty()) {
                throw new ValidationException(
                    MessageCode.SILM3GEN1004,
                    env,
                    declaration,
                    declaredType);
            }
            TypeMirror elementType = typeArgs.iterator().next();
            DeclaredType elementDeclaredType =
                TypeUtil.toDeclaredType(typeArgs.iterator().next());
            if (elementDeclaredType == null) {
                throw new ValidationException(
                    MessageCode.SILM3GEN1016,
                    env,
                    declaration,
                    elementType);
            }
            TypeDeclaration elementDeclaration =
                toTypeDeclaration(elementDeclaredType);
            CoreReferenceType elementCoreReferenceType =
                getCoreReferenceType(elementDeclaration.getQualifiedName());
            if (elementCoreReferenceType != null) {
                return createCollectionType(
                    className,
                    declaredType,
                    elementCoreReferenceType);
            }
            DataType elementDataType =
                createDataType(elementDeclaration, elementType);
            CollectionType collectionType =
                createCollectionType(
                    className,
                    declaredType,
                    elementCoreReferenceType);
            if (isSerializable(elementType)) {
                collectionType.setSerialized(true);
                return collectionType;
            }
            throw new ValidationException(
                MessageCode.SILM3GEN1002,
                env,
                declaration,
                elementDataType.getClassName());
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
                return new ArrayListType(className, typeName, elementType);
            }
            if (ArrayList.equals(className)) {
                return new ArrayListType(className, typeName, elementType);
            }
            if (Set.equals(className)) {
                return new HashSetType(className, typeName, elementType);
            }
            if (HashSet.equals(className)) {
                return new HashSetType(className, typeName, elementType);
            }
            if (SortedSet.equals(className)) {
                return new TreeSetType(className, typeName, elementType);
            }
            if (TreeSet.equals(className)) {
                return new TreeSetType(className, typeName, elementType);
            }
            throw new ValidationException(
                MessageCode.SILM3GEN1002,
                env,
                declaration,
                className);
        }

        /**
         * Creates a {@link DataType}.
         * 
         * @param declaration
         *            the declaration
         * @param typeMirror
         *            the typeMirror
         * @return a data type
         */
        protected DataType createDataType(Declaration declaration,
                TypeMirror typeMirror) {
            Builder builder = new Builder(declaration, typeMirror);
            return builder.build();
        }

    }

}
