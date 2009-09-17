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
import com.sun.mirror.type.ClassType;
import com.sun.mirror.type.DeclaredType;
import com.sun.mirror.type.InterfaceType;
import com.sun.mirror.type.PrimitiveType;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.type.PrimitiveType.Kind;
import com.sun.mirror.util.SimpleTypeVisitor;

/**
 * @author taedium
 * 
 */
public class DatastoreTypeFactory {

    protected static final List<Kind> PRIMITIVE_TYPES = new ArrayList<Kind>();
    static {
        PRIMITIVE_TYPES.add(Kind.BOOLEAN);
        PRIMITIVE_TYPES.add(Kind.INT);
        PRIMITIVE_TYPES.add(Kind.SHORT);
    }

    protected static final List<String> REFERENCE_TYPES =
        new ArrayList<String>();
    static {
        REFERENCE_TYPES.add(String);
        REFERENCE_TYPES.add(Boolean);
        REFERENCE_TYPES.add(Short);
        REFERENCE_TYPES.add(Integer);
        REFERENCE_TYPES.add(Long);
        REFERENCE_TYPES.add(Float);
        REFERENCE_TYPES.add(Double);
        REFERENCE_TYPES.add(Date);
        REFERENCE_TYPES.add(BigDecimal);
        REFERENCE_TYPES.add(User);
        REFERENCE_TYPES.add(Key);
        REFERENCE_TYPES.add(Category);
        REFERENCE_TYPES.add(Email);
        REFERENCE_TYPES.add(GeoPt);
        REFERENCE_TYPES.add(IMHandle);
        REFERENCE_TYPES.add(Link);
        REFERENCE_TYPES.add(PhoneNumber);
        REFERENCE_TYPES.add(PostalAddress);
        REFERENCE_TYPES.add(Rating);
        REFERENCE_TYPES.add(ShortBlob);
        REFERENCE_TYPES.add(Blob);
        REFERENCE_TYPES.add(Text);
    }

    protected static final Map<String, String> COLLECTION_INTERFACE_TYPES =
        new HashMap<String, String>();
    static {
        COLLECTION_INTERFACE_TYPES.put(Collection, ArrayList);
        COLLECTION_INTERFACE_TYPES.put(List, ArrayList);
        COLLECTION_INTERFACE_TYPES.put(Set, HashSet);
        COLLECTION_INTERFACE_TYPES.put(SortedSet, TreeSet);
    }

    protected static final List<String> COLLECTION_IMPL_TYPES =
        new ArrayList<String>();
    static {
        COLLECTION_IMPL_TYPES.add(ArrayList);
        COLLECTION_IMPL_TYPES.add(LinkedList);
        COLLECTION_IMPL_TYPES.add(Vector);
        COLLECTION_IMPL_TYPES.add(Stack);
        COLLECTION_IMPL_TYPES.add(HashSet);
        COLLECTION_IMPL_TYPES.add(LinkedHashSet);
        COLLECTION_IMPL_TYPES.add(TreeSet);
    }

    protected final AnnotationProcessorEnvironment env;

    public DatastoreTypeFactory(AnnotationProcessorEnvironment env) {
        this.env = env;
    }

    public DatastoreType createDatastoreType(Declaration declaration,
            TypeMirror typeMirror) {
        if (typeMirror == null) {
            throw new NullPointerException("The parameter typeMirror is null.");
        }
        DatastoreType datastoreType =
            new DatastoreType(env, declaration, typeMirror);
        datastoreType.setDeclaredTypeName(typeMirror.toString());
        TypeAnalizer visitor = new TypeAnalizer(datastoreType);
        typeMirror.accept(visitor);
        return datastoreType;
    }

    protected class TypeAnalizer extends SimpleTypeVisitor {

        protected DatastoreType datastoreType;

        public TypeAnalizer(DatastoreType datastoreType) {
            this.datastoreType = datastoreType;
        }

        @Override
        public void visitArrayType(ArrayType arraytype) {
            datastoreType.setArray(true);
            arraytype.getComponentType().accept(new SimpleTypeVisitor() {

                @Override
                public void visitDeclaredType(DeclaredType declaredtype) {
                    TypeDeclaration typeDeclaration =
                        toTypeDeclaration(declaredtype);
                    String name = typeDeclaration.getQualifiedName();
                    datastoreType.setTypeName("[L" + name);
                    datastoreType.setElementTypeName(name);
                    if (REFERENCE_TYPES.contains(name)) {
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
                public void visitPrimitiveType(PrimitiveType primitivetype) {
                    Kind kind = primitivetype.getKind();
                    datastoreType
                        .setTypeName("[" + kind.name().substring(0, 1));
                    datastoreType.setElementTypeName(kind.name().toLowerCase());
                    if (PRIMITIVE_TYPES.contains(kind)) {
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
        public void visitClassType(ClassType classtype) {
            TypeDeclaration typeDeclaration = toTypeDeclaration(classtype);
            String name = typeDeclaration.getQualifiedName();
            datastoreType.setTypeName(name);
            if (REFERENCE_TYPES.contains(name)) {
                if (Blob.equals(name) || Text.equals(name)) {
                    datastoreType.setUnindex(true);
                }
            } else if (COLLECTION_IMPL_TYPES.contains(name)) {
                datastoreType.setImplicationTypeName(name);
                datastoreType.setCollection(true);
                Collection<TypeMirror> typeArgs =
                    classtype.getActualTypeArguments();
                if (typeArgs.isEmpty()) {
                    throw new ValidationException(
                        MessageCode.SILM3GEN1004,
                        env,
                        datastoreType.getDeclaration());
                }
                DeclaredType elementType =
                    TypeUtil.toDeclaredType(typeArgs.iterator().next());
                TypeDeclaration elementDeclaration =
                    toTypeDeclaration(elementType);
                String elementName = elementDeclaration.getQualifiedName();
                if (REFERENCE_TYPES.contains(elementName)) {
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
            } else if (isSerializable(classtype)) {
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
        public void visitInterfaceType(InterfaceType interfacetype) {
            TypeDeclaration typeDeclaration = toTypeDeclaration(interfacetype);
            String name = typeDeclaration.getQualifiedName();
            datastoreType.setTypeName(name);
            if (COLLECTION_INTERFACE_TYPES.containsKey(name)) {
                datastoreType.setImplicationTypeName(COLLECTION_INTERFACE_TYPES
                    .get(name));
                datastoreType.setCollection(true);
                Collection<TypeMirror> typeArgs =
                    interfacetype.getActualTypeArguments();
                if (typeArgs.isEmpty()) {
                    throw new ValidationException(
                        MessageCode.SILM3GEN1004,
                        env,
                        datastoreType.getDeclaration());
                }
                DeclaredType elementType =
                    TypeUtil.toDeclaredType(typeArgs.iterator().next());
                TypeDeclaration elementDeclaration =
                    toTypeDeclaration(elementType);
                String elementName = elementDeclaration.getQualifiedName();
                if (REFERENCE_TYPES.contains(elementName)) {
                    // do nothing
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
            } else {
                throw new ValidationException(
                    MessageCode.SILM3GEN1003,
                    env,
                    datastoreType.getDeclaration(),
                    name);
            }
        }

        @Override
        public void visitPrimitiveType(PrimitiveType primitiveType) {
            Kind kind = primitiveType.getKind();
            if (PRIMITIVE_TYPES.contains(primitiveType.getKind())) {
                datastoreType.setTypeName(kind.name().toLowerCase());
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
        public void visitTypeMirror(TypeMirror typemirror) {
            throw new AssertionError("unreachable");
        }

        protected TypeDeclaration toTypeDeclaration(DeclaredType declaredType) {
            TypeDeclaration typeDeclaration = declaredType.getDeclaration();
            if (typeDeclaration != null) {
                return typeDeclaration;
            }
            throw new UnknownDeclarationException(env, datastoreType
                .getDeclaration(), declaredType);
        }

        protected boolean isSerializable(TypeMirror typeMirror) {
            return TypeUtil.isSubtype(env, typeMirror, Serializable.class);
        }
    }

}
