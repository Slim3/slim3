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
import static org.slim3.gen.ClassConstants.Collection;
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
import static org.slim3.gen.ClassConstants.LinkedHashSet;
import static org.slim3.gen.ClassConstants.LinkedList;
import static org.slim3.gen.ClassConstants.List;
import static org.slim3.gen.ClassConstants.Long;
import static org.slim3.gen.ClassConstants.PhoneNumber;
import static org.slim3.gen.ClassConstants.PostalAddress;
import static org.slim3.gen.ClassConstants.Rating;
import static org.slim3.gen.ClassConstants.Set;
import static org.slim3.gen.ClassConstants.Short;
import static org.slim3.gen.ClassConstants.ShortBlob;
import static org.slim3.gen.ClassConstants.SortedSet;
import static org.slim3.gen.ClassConstants.Stack;
import static org.slim3.gen.ClassConstants.String;
import static org.slim3.gen.ClassConstants.Text;
import static org.slim3.gen.ClassConstants.TreeSet;
import static org.slim3.gen.ClassConstants.User;
import static org.slim3.gen.ClassConstants.Vector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
 * Represents a datastore type factory.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class DataTypeFactory {

    /** the supported primitive types */
    protected static final Map<Kind, String> PRIMITIVE_TYPES =
        new HashMap<Kind, String>();
    static {
        PRIMITIVE_TYPES.put(Kind.BOOLEAN, Boolean);
        PRIMITIVE_TYPES.put(Kind.SHORT, Short);
        PRIMITIVE_TYPES.put(Kind.INT, Integer);
        PRIMITIVE_TYPES.put(Kind.LONG, Long);
        PRIMITIVE_TYPES.put(Kind.FLOAT, Float);
        PRIMITIVE_TYPES.put(Kind.DOUBLE, Double);
    }

    /** the supported value types */
    protected static final List<String> VALUE_TYPES = new ArrayList<String>();
    static {
        VALUE_TYPES.add(String);
        VALUE_TYPES.add(Boolean);
        VALUE_TYPES.add(Short);
        VALUE_TYPES.add(Integer);
        VALUE_TYPES.add(Long);
        VALUE_TYPES.add(Float);
        VALUE_TYPES.add(Double);
        VALUE_TYPES.add(Date);
        VALUE_TYPES.add(User);
        VALUE_TYPES.add(Key);
        VALUE_TYPES.add(Category);
        VALUE_TYPES.add(Email);
        VALUE_TYPES.add(GeoPt);
        VALUE_TYPES.add(IMHandle);
        VALUE_TYPES.add(Link);
        VALUE_TYPES.add(PhoneNumber);
        VALUE_TYPES.add(PostalAddress);
        VALUE_TYPES.add(Rating);
        VALUE_TYPES.add(ShortBlob);
        VALUE_TYPES.add(Blob);
        VALUE_TYPES.add(Text);
    }

    /** the supported collection types */
    protected static final Map<String, String> COLLECTION_TYPES =
        new HashMap<String, String>();
    static {
        COLLECTION_TYPES.put(Collection, ArrayList);
        COLLECTION_TYPES.put(List, ArrayList);
        COLLECTION_TYPES.put(ArrayList, ArrayList);
        COLLECTION_TYPES.put(LinkedList, LinkedList);
        COLLECTION_TYPES.put(Vector, Vector);
        COLLECTION_TYPES.put(Stack, Stack);
        COLLECTION_TYPES.put(Set, HashSet);
        COLLECTION_TYPES.put(HashSet, HashSet);
        COLLECTION_TYPES.put(LinkedHashSet, LinkedHashSet);
        COLLECTION_TYPES.put(SortedSet, TreeSet);
        COLLECTION_TYPES.put(TreeSet, TreeSet);
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
     * The datastore type builder.
     * 
     * @author taedium
     * @since 3.0
     * 
     */
    protected class Builder extends SimpleTypeVisitor {

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
                public void visitDeclaredType(DeclaredType declaredtype) {
                    TypeDeclaration typeDeclaration =
                        toTypeDeclaration(declaredtype);
                    String className =
                        typeDeclaration.getQualifiedName() + "[]";
                    dataType =
                        new org.slim3.gen.datastore.ArrayType(
                            className,
                            declaredtype.toString(),
                            createDataType(typeDeclaration, declaredtype));
                    if (VALUE_TYPES.contains(className)) {
                        // do nothing
                    } else if (isSerializable(declaredtype)) {
                        dataType.setSerializable(true);
                        dataType.setUnindex(true);
                    } else {
                        throw new ValidationException(
                            MessageCode.SILM3GEN1005,
                            env,
                            declaration,
                            className);
                    }
                }

                @Override
                public void visitPrimitiveType(PrimitiveType primitiveType) {
                    Kind kind = primitiveType.getKind();
                    String componentClassName = kind.name().toLowerCase();
                    String className = componentClassName + "[]";
                    dataType =
                        new org.slim3.gen.datastore.ArrayType(
                            className,
                            PRIMITIVE_TYPES.get(kind),
                            createDataType(null, primitiveType));
                    if (kind == Kind.CHAR) {
                        dataType.setSerializable(true);
                        dataType.setUnindex(true);
                    }
                }

                @Override
                public void visitTypeMirror(TypeMirror typemirror) {
                    throw new AssertionError("unreachable");
                }
            });
        }

        @Override
        public void visitDeclaredType(DeclaredType declaredType) {
            TypeDeclaration typeDeclaration = toTypeDeclaration(declaredType);
            String className = typeDeclaration.getQualifiedName();
            if (VALUE_TYPES.contains(className)) {
                dataType = createCoreType(className);
                return;
            }
            if (COLLECTION_TYPES.containsKey(className)) {
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
                DataType elementDataType =
                    createDataType(
                        elementDeclaredType.getDeclaration(),
                        elementType);

                TypeDeclaration elementDeclaration =
                    toTypeDeclaration(elementDeclaredType);
                String elementClassName = elementDeclaration.getQualifiedName();
                dataType =
                    new CollectionType(
                        className,
                        declaredType.toString(),
                        elementDataType);
                if (VALUE_TYPES.contains(elementClassName)) {
                    // do nothing.
                } else if (isSerializable(elementType)) {
                    dataType.setSerializable(true);
                    dataType.setUnindex(true);
                } else {
                    throw new ValidationException(
                        MessageCode.SILM3GEN1002,
                        env,
                        declaration,
                        elementClassName);
                }
                return;
            }
            if (isSerializable(declaredType)) {
                dataType = new AnyType(className, declaredType.toString());
                dataType.setSerializable(true);
                dataType.setUnindex(true);
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
            String className = kind.name().toLowerCase();
            if (PRIMITIVE_TYPES.containsKey(kind)) {
                dataType = createPrimitiveType(className);
            } else {
                throw new ValidationException(
                    MessageCode.SILM3GEN1002,
                    env,
                    declaration,
                    className);
            }
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

        protected CoreType createCoreType(String className) {
            env.getMessager().printNotice(
                "Builder.createCoreType : " + className);
            if (String.equals(className)) {
                return new StringType();
            }
            if (Key.equals(className)) {
                return new KeyType();
            }
            if (Blob.equals(className)) {
                return new BlobType();
            }
            if (Text.equals(className)) {
                return new TextType();
            }
            throw new AssertionError("not yet implemented. : " + className);
        }

        protected org.slim3.gen.datastore.PrimitiveType createPrimitiveType(
                String className) {

            return null;
        }

        protected DataType createDataType(Declaration declaration,
                TypeMirror typeMirror) {
            Builder builder = new Builder(declaration, typeMirror);
            return builder.build();
        }
    }

}
