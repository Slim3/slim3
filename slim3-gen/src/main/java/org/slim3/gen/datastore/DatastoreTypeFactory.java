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
import static org.slim3.gen.ClassConstants.BigDecimal;
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
public class DatastoreTypeFactory {

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

    /** the supported single types */
    protected static final List<String> SINGLE_TYPES = new ArrayList<String>();
    static {
        SINGLE_TYPES.add(String);
        SINGLE_TYPES.add(Boolean);
        SINGLE_TYPES.add(Short);
        SINGLE_TYPES.add(Integer);
        SINGLE_TYPES.add(Long);
        SINGLE_TYPES.add(Float);
        SINGLE_TYPES.add(Double);
        SINGLE_TYPES.add(Date);
        SINGLE_TYPES.add(BigDecimal);
        SINGLE_TYPES.add(User);
        SINGLE_TYPES.add(Key);
        SINGLE_TYPES.add(Category);
        SINGLE_TYPES.add(Email);
        SINGLE_TYPES.add(GeoPt);
        SINGLE_TYPES.add(IMHandle);
        SINGLE_TYPES.add(Link);
        SINGLE_TYPES.add(PhoneNumber);
        SINGLE_TYPES.add(PostalAddress);
        SINGLE_TYPES.add(Rating);
        SINGLE_TYPES.add(ShortBlob);
        SINGLE_TYPES.add(Blob);
        SINGLE_TYPES.add(Text);
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
     * Creates a new {@link DatastoreTypeFactory}.
     * 
     * @param env
     *            the environment
     */
    public DatastoreTypeFactory(AnnotationProcessorEnvironment env) {
        this.env = env;
    }

    /**
     * Creates a new {@link DatastoreType}.
     * 
     * @param declaration
     *            the declaration
     * @param typeMirror
     *            the typemirror
     * @return a new {@link DatastoreType}
     */
    public DatastoreType createDatastoreType(Declaration declaration,
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

        /** the typemirror */
        protected final TypeMirror typeMirror;

        /** the datastore type */
        protected final DatastoreType datastoreType;

        /**
         * Creates a new {@link Builder}
         * 
         * @param declaration
         *            the declaration
         * @param typeMirror
         *            the typemirror
         */
        public Builder(Declaration declaration, TypeMirror typeMirror) {
            this.typeMirror = typeMirror;
            this.datastoreType =
                new DatastoreType(env, declaration, typeMirror);
        }

        /**
         * Builds a {@link DatastoreType}.
         * 
         * @return a datastore type
         */
        public DatastoreType build() {
            typeMirror.accept(this);
            return datastoreType;
        }

        @Override
        public void visitArrayType(ArrayType arrayType) {
            datastoreType.setArray(true);
            arrayType.getComponentType().accept(new SimpleTypeVisitor() {

                @Override
                public void visitDeclaredType(DeclaredType declaredtype) {
                    TypeDeclaration typeDeclaration =
                        toTypeDeclaration(declaredtype);
                    String name = typeDeclaration.getQualifiedName();
                    String typeName = name + "[]";
                    datastoreType.setTypeName(typeName);
                    datastoreType.setImplicationTypeName(typeName);
                    datastoreType.setWrapperTypeName(typeName);
                    datastoreType.setElementTypeName(name);
                    if (SINGLE_TYPES.contains(name)) {
                        // do nothing
                    } else if (isSerializable(declaredtype)) {
                        datastoreType.setSerializable(true);
                        datastoreType.setUnindex(true);
                    } else {
                        throw new ValidationException(
                            MessageCode.SILM3GEN1005,
                            env,
                            datastoreType.getDeclaration(),
                            name);
                    }
                }

                @Override
                public void visitPrimitiveType(PrimitiveType primitiveType) {
                    Kind kind = primitiveType.getKind();
                    String name = kind.name().toLowerCase();
                    String typeName = name + "[]";
                    datastoreType.setTypeName(typeName);
                    datastoreType.setImplicationTypeName(typeName);
                    datastoreType.setWrapperTypeName(typeName);
                    datastoreType.setElementTypeName(name);
                    if (PRIMITIVE_TYPES.containsKey(kind)) {
                        // do nothing
                    } else {
                        datastoreType.setSerializable(true);
                        datastoreType.setUnindex(true);
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
            String name = typeDeclaration.getQualifiedName();
            datastoreType.setTypeName(name);
            datastoreType.setImplicationTypeName(name);
            datastoreType.setWrapperTypeName(name);
            if (SINGLE_TYPES.contains(name)) {
                if (Blob.equals(name) || Text.equals(name)) {
                    datastoreType.setUnindex(true);
                }
            } else if (COLLECTION_TYPES.containsKey(name)) {
                datastoreType
                    .setImplicationTypeName(COLLECTION_TYPES.get(name));
                datastoreType.setCollection(true);
                Collection<TypeMirror> typeArgs =
                    declaredType.getActualTypeArguments();
                if (typeArgs.isEmpty()) {
                    throw new ValidationException(
                        MessageCode.SILM3GEN1004,
                        env,
                        datastoreType.getDeclaration(),
                        declaredType);
                }
                TypeMirror elementType = typeArgs.iterator().next();
                DeclaredType elementDeclaredType =
                    TypeUtil.toDeclaredType(typeArgs.iterator().next());
                if (elementDeclaredType == null) {
                    throw new ValidationException(
                        MessageCode.SILM3GEN1016,
                        env,
                        datastoreType.getDeclaration(),
                        elementType);
                }
                TypeDeclaration elementDeclaration =
                    toTypeDeclaration(elementDeclaredType);
                String elementName = elementDeclaration.getQualifiedName();
                datastoreType.setElementTypeName(elementName);
                if (SINGLE_TYPES.contains(elementName)) {
                    // do nothing.
                } else if (isSerializable(elementType)) {
                    datastoreType.setSerializable(true);
                    datastoreType.setUnindex(true);
                } else {
                    throw new ValidationException(
                        MessageCode.SILM3GEN1002,
                        env,
                        datastoreType.getDeclaration(),
                        elementName);
                }
            } else if (isSerializable(declaredType)) {
                datastoreType.setSerializable(true);
                datastoreType.setUnindex(true);
            } else {
                throw new ValidationException(
                    MessageCode.SILM3GEN1002,
                    env,
                    datastoreType.getDeclaration(),
                    name);
            }
        }

        @Override
        public void visitPrimitiveType(PrimitiveType primitiveType) {
            Kind kind = primitiveType.getKind();
            if (PRIMITIVE_TYPES.containsKey(kind)) {
                String name = (kind.name().toLowerCase());
                datastoreType.setTypeName(name);
                datastoreType.setImplicationTypeName(name);
                datastoreType.setWrapperTypeName(PRIMITIVE_TYPES.get(kind));
                datastoreType.setPrimitive(true);
            } else {
                throw new ValidationException(
                    MessageCode.SILM3GEN1002,
                    env,
                    datastoreType.getDeclaration(),
                    kind.name().toLowerCase());
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
            throw new UnknownDeclarationException(env, datastoreType
                .getDeclaration(), declaredType);
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
    }

}
