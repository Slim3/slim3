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
package org.slim3.gen.desc;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.slim3.gen.ClassConstants;
import org.slim3.gen.util.DeclarationUtil;
import org.slim3.gen.util.StringUtil;
import org.slim3.gen.util.TypeUtil;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.ArrayType;
import com.sun.mirror.type.DeclaredType;
import com.sun.mirror.type.PrimitiveType;
import com.sun.mirror.type.ReferenceType;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.type.WildcardType;
import com.sun.mirror.type.PrimitiveType.Kind;
import com.sun.mirror.util.SimpleTypeVisitor;

/**
 * Represents an attribute meta description factory.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class AttributeMetaDescFactory {

    /** the environment */
    protected final AnnotationProcessorEnvironment env;

    /**
     * Creates a new {@link AttributeMetaDescFactory}.
     * 
     * @param env
     *            the processing environment
     */
    public AttributeMetaDescFactory(AnnotationProcessorEnvironment env) {
        if (env == null) {
            throw new NullPointerException("The env parameter is null.");
        }
        this.env = env;
    }

    /**
     * Creates a new {@link AttributeMetaDesc}
     * 
     * @param attributeDeclaration
     *            the attribute declaration
     * @return an attribute meta description
     */
    public AttributeMetaDesc createAttributeMetaDesc(
            FieldDeclaration fieldDeclaration,
            List<MethodDeclaration> methodDeclarations) {
        if (fieldDeclaration == null) {
            throw new NullPointerException(
                "The fieldDeclaration parameter is null.");
        }
        if (methodDeclarations == null) {
            throw new NullPointerException(
                "The methodDeclarations parameter is null.");
        }

        AttributeMetaDesc attributeMetaDesc =
            new AttributeMetaDesc(
                fieldDeclaration.getSimpleName(),
                fieldDeclaration.getDeclaringType().getQualifiedName());
        if (isPrimaryKeyAnnotated(fieldDeclaration)) {
            attributeMetaDesc.setPrimaryKey(true);
        }
        if (isVersionAnnotated(fieldDeclaration)) {
            attributeMetaDesc.setVersion(true);
        }
        if (isTextAnnotated(fieldDeclaration)) {
            attributeMetaDesc.setText(true);
        }
        if (isBlobAnnotated(fieldDeclaration)) {
            attributeMetaDesc.setBlob(true);
        }
        if (isImpermanentAnnotated(fieldDeclaration)) {
            attributeMetaDesc.setImpermanent(true);
        } else {
            DeclaredType declaredType =
                TypeUtil.toDeclaredType(fieldDeclaration.getType());
            if (declaredType == null) {
                // throw
            }
            if (isSupportedCoreType(declaredType)) {

            } else if (isSupportedCollectionType(declaredType)) {

            } else if (isSerializable(declaredType)) {

                attributeMetaDesc.setUnindexed(false);
            }
        }
        for (MethodDeclaration m : methodDeclarations) {
            if (isReadMethod(m, attributeMetaDesc)) {
                attributeMetaDesc.setReadMethodName(m.getSimpleName());
                if (attributeMetaDesc.getWriteMethodName() != null) {
                    break;
                }
            } else if (isWriteMethod(m, attributeMetaDesc)) {
                attributeMetaDesc.setWriteMethodName(m.getSimpleName());
                if (attributeMetaDesc.getReadMethodName() != null) {
                    break;
                }
            }
        }
        validateAttributeMetaDesc(attributeMetaDesc);
        return attributeMetaDesc;
    }

    protected boolean isReadMethod(MethodDeclaration m,
            AttributeMetaDesc attributeMetaDesc) {
        String propertyName = null;
        if (m.getSimpleName().startsWith("get")) {
            propertyName =
                StringUtil.decapitalize(m.getSimpleName().substring(3));
        } else if (m.getSimpleName().startsWith("is")) {
            if (!TypeUtil.isPrimitive(m.getReturnType(), Kind.BOOLEAN)) {
                return false;
            }
            propertyName =
                StringUtil.decapitalize(m.getSimpleName().substring(2));
        } else {
            return false;
        }
        if (!propertyName.equals(attributeMetaDesc.getName())
            || TypeUtil.isVoid(m.getReturnType())
            || m.getParameters().size() != 0) {
            return false;
        }
        TypeDeclaration propertyClass =
            TypeUtil.toTypeDeclaration(m.getReturnType());
        if (propertyClass == null
            || !propertyClass.getQualifiedName().equals(
                attributeMetaDesc.getAttributeClassName())) {
            return false;
        }
        return true;
    }

    protected boolean isWriteMethod(MethodDeclaration m,
            AttributeMetaDesc attributeMetaDesc) {
        if (!m.getSimpleName().startsWith("set")) {
            return false;
        }
        String propertyName =
            StringUtil.decapitalize(m.getSimpleName().substring(3));
        if (!propertyName.equals(attributeMetaDesc.getName())
            || m.getParameters().size() != 1
            || TypeUtil.isVoid(m.getReturnType())) {
            return false;
        }
        TypeDeclaration propertyClass =
            TypeUtil.toTypeDeclaration(m
                .getParameters()
                .iterator()
                .next()
                .getType());
        if (propertyClass == null
            || !propertyClass.getQualifiedName().equals(
                attributeMetaDesc.getAttributeClassName())) {
            return false;
        }
        return true;
    }

    protected void validateAttributeMetaDesc(AttributeMetaDesc attributeMetaDesc) {
        //
    }

    protected boolean isPrimaryKeyAnnotated(FieldDeclaration fieldDeclaration) {
        return DeclarationUtil.getAnnotationMirror(
            fieldDeclaration,
            ClassConstants.PrimaryKey) != null;
    }

    protected boolean isVersionAnnotated(FieldDeclaration fieldDeclaration) {
        return DeclarationUtil.getAnnotationMirror(
            fieldDeclaration,
            ClassConstants.Version) != null;
    }

    protected boolean isImpermanentAnnotated(FieldDeclaration fieldDeclaration) {
        return DeclarationUtil.getAnnotationMirror(
            fieldDeclaration,
            ClassConstants.Impermanent) != null;
    }

    protected boolean isTextAnnotated(FieldDeclaration fieldDeclaration) {
        return DeclarationUtil.getAnnotationMirror(
            fieldDeclaration,
            ClassConstants.Text) != null;
    }

    protected boolean isBlobAnnotated(FieldDeclaration fieldDeclaration) {
        return DeclarationUtil.getAnnotationMirror(
            fieldDeclaration,
            ClassConstants.Blob) != null;
    }

    protected boolean isCollection(DeclaredType declaredType) {
        return TypeUtil.isSubtype(env, declaredType, Collection.class);
    }

    protected boolean isSupportedCoreType(DeclaredType declaredType) {

        return false;
    }

    protected boolean isSupportedCollectionType(DeclaredType declaredType) {

        return false;
    }

    protected boolean isSerializable(DeclaredType declaredType) {

        return false;
    }

    protected String getElementNameOfCollection(DeclaredType declaredType) {
        TypeMirror element =
            declaredType.getActualTypeArguments().iterator().next();
        List<String> names = new ClassNameCollector(element).collect();
        if (names.isEmpty()) {
            return null;
        }
        return names.get(0);
    }

    protected static class ClassNameCollector extends SimpleTypeVisitor {

        LinkedList<String> names = new LinkedList<String>();

        /** the target typeMirror */
        protected final TypeMirror typeMirror;

        /**
         * Creates a new {@link ClassNameCollector}
         * 
         * @param typeMirror
         *            the target typeMirror
         */
        public ClassNameCollector(TypeMirror typeMirror) {
            this.typeMirror = typeMirror;
        }

        /**
         * Collects the collection of class name.
         * 
         * @return the collection of class name
         */
        public LinkedList<String> collect() {
            typeMirror.accept(this);
            return names;
        }

        @Override
        public void visitArrayType(ArrayType type) {
            ClassNameCollector collector2 = new ClassNameCollector(type);
            LinkedList<String> names = collector2.collect();
            type.getComponentType().accept(collector2);
            this.names.add(names.getFirst() + "[]");
            this.names.add(names.getFirst());
        }

        @Override
        public void visitDeclaredType(DeclaredType type) {
            names.add(type.getDeclaration().getQualifiedName());
            for (TypeMirror arg : type.getActualTypeArguments()) {
                ClassNameCollector collector2 = new ClassNameCollector(arg);
                LinkedList<String> names = collector2.collect();
                this.names.add(names.getFirst());
            }
        }

        @Override
        public void visitPrimitiveType(PrimitiveType type) {
            switch (type.getKind()) {
            case BOOLEAN: {
                names.add(boolean.class.getName());
                break;
            }
            case BYTE: {
                names.add(byte.class.getName());
                break;
            }
            case CHAR: {
                names.add(char.class.getName());
                break;
            }
            case DOUBLE: {
                names.add(double.class.getName());
                break;
            }
            case FLOAT: {
                names.add(float.class.getName());
                break;
            }
            case INT: {
                names.add(int.class.getName());
                break;
            }
            case LONG: {
                names.add(long.class.getName());
                break;
            }
            case SHORT: {
                names.add(short.class.getName());
                break;
            }
            default: {
                throw new IllegalArgumentException(type.getKind().name());
            }
            }
        }

        @Override
        public void visitWildcardType(WildcardType type) {
            for (ReferenceType referenceType : type.getUpperBounds()) {
                ClassNameCollector collector2 =
                    new ClassNameCollector(referenceType);
                LinkedList<String> names = collector2.collect();
                this.names.add(names.getFirst());
            }
            for (ReferenceType referenceType : type.getLowerBounds()) {
                ClassNameCollector collector2 =
                    new ClassNameCollector(referenceType);
                LinkedList<String> names = collector2.collect();
                this.names.add(names.getFirst());
            }
        }
    }
}
