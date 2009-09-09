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

import java.util.Iterator;
import java.util.LinkedList;

import org.slim3.gen.ClassConstants;
import org.slim3.gen.message.MessageCode;
import org.slim3.gen.message.MessageFormatter;
import org.slim3.gen.processor.Logger;
import org.slim3.gen.processor.Options;
import org.slim3.gen.util.DeclarationUtil;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.Modifier;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.ArrayType;
import com.sun.mirror.type.DeclaredType;
import com.sun.mirror.type.PrimitiveType;
import com.sun.mirror.type.ReferenceType;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.type.WildcardType;
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
            FieldDeclaration attributeDeclaration) {
        if (attributeDeclaration == null) {
            throw new NullPointerException(
                "The attributeDeclaration parameter is null.");
        }
        if (isNestedType(attributeDeclaration)) {
            return null;
        }
        if (isNotPersistent(attributeDeclaration)
            || isStatic(attributeDeclaration)) {
            return null;
        }
        if (!isPersistent(attributeDeclaration)) {
            if (isTransient(attributeDeclaration)) {
                return null;
            }
            Logger.warning(env, attributeDeclaration, MessageFormatter
                .getSimpleMessage(MessageCode.SILM3GEN0010));
        }
        Iterator<String> classNames =
            new ClassNameCollector(attributeDeclaration.getType())
                .collect()
                .iterator();
        AttributeMetaDesc attributeMetaDesc = new AttributeMetaDesc();
        attributeMetaDesc.setName(attributeDeclaration
            .getSimpleName()
            .toString());
        if (isEmbedded(attributeDeclaration)) {
            attributeMetaDesc.setEmbedded(true);
            if (classNames.hasNext()) {
                String modelClassName = classNames.next();
                ModelMetaClassName modelMetaClassName =
                    createModelMetaClassName(modelClassName);
                attributeMetaDesc
                    .setEmbeddedModelMetaClassName(modelMetaClassName
                        .getQualifiedName());
            }
        } else {
            if (classNames.hasNext()) {
                String className = classNames.next();
                attributeMetaDesc.setAttributeClassName(className);
            }
            if (classNames.hasNext()) {
                String elementClassName = classNames.next();
                attributeMetaDesc
                    .setAttributeElementClassName(elementClassName);
            }
        }
        return attributeMetaDesc;
    }

    /**
     * Returns {@code true} if the attribute type is a nested type.
     * 
     * @param attributeDeclaration
     *            the declaration of an attribute
     * @return {@code true} if the attribute type is a nested type
     */
    protected boolean isNestedType(FieldDeclaration attributeDeclaration) {
        class GetTypeMirrorVisitor extends SimpleTypeVisitor {

            TypeMirror result;

            GetTypeMirrorVisitor(TypeMirror typeMirror) {
                result = typeMirror;
            }

            @Override
            public void visitArrayType(ArrayType type) {
                GetTypeMirrorVisitor visitor = new GetTypeMirrorVisitor(result);
                type.getComponentType().accept(visitor);
                this.result = visitor.result;
            }
        }
        GetTypeMirrorVisitor getTypeMirrorVisitor =
            new GetTypeMirrorVisitor(attributeDeclaration.getType());
        attributeDeclaration.getType().accept(getTypeMirrorVisitor);
        TypeMirror typeMirror = getTypeMirrorVisitor.result;

        class GetDeclaredType extends SimpleTypeVisitor {
            DeclaredType result;

            @Override
            public void visitDeclaredType(DeclaredType type) {
                result = type;
            }
        }
        GetDeclaredType getDeclaredTypeVisitor = new GetDeclaredType();
        typeMirror.accept(getDeclaredTypeVisitor);
        DeclaredType declaredType = getDeclaredTypeVisitor.result;
        if (declaredType == null) {
            return false;
        }
        TypeDeclaration typeDeclaration = declaredType.getDeclaration();
        if (typeDeclaration == null) {
            return false;
        }
        return typeDeclaration.getDeclaringType() != null;
    }

    /**
     * Returns {@code true} if the attribute is persistent.
     * 
     * @param attributeDeclaration
     *            the declaration of an attribute
     * @return {@code true} if the attribute is persistent.
     */
    protected boolean isPersistent(FieldDeclaration attributeDeclaration) {
        return DeclarationUtil.getAnnotationMirror(
            attributeDeclaration,
            ClassConstants.Persistent) != null;
    }

    /**
     * Returns {@code true} if the attribute is not persistent.
     * 
     * @param attributeDeclaration
     *            the declaration of an attribute
     * @return {@code true} if the attribute is persistent.
     */
    protected boolean isNotPersistent(FieldDeclaration attributeDeclaration) {
        return DeclarationUtil.getAnnotationMirror(
            attributeDeclaration,
            ClassConstants.NotPersistent) != null;
    }

    /**
     * Returns {@code true} if the attribute is a static variable.
     * 
     * @param attributeDeclaration
     *            the declaration of an attribute
     * @return {@code true} if the attribute is a static variable.
     */
    protected boolean isStatic(FieldDeclaration attributeDeclaration) {
        return attributeDeclaration.getModifiers().contains(Modifier.STATIC);
    }

    /**
     * Returns {@code true} if the attribute is transient.
     * 
     * @param attributeDeclaration
     *            the declaration of an attribute
     * @return {@code true} if the attribute is transient.
     */
    protected boolean isTransient(FieldDeclaration attributeDeclaration) {
        return attributeDeclaration.getModifiers().contains(Modifier.TRANSIENT);
    }

    /**
     * Returns {@code true} if the attribute is embedded.
     * 
     * @param attributeDeclaration
     *            the declaration of an attribute
     * @return {@code true} if the attribute is embedded.
     */
    protected boolean isEmbedded(FieldDeclaration attributeDeclaration) {
        return DeclarationUtil.getAnnotationMirror(
            attributeDeclaration,
            ClassConstants.Embedded) != null;
    }

    /**
     * Creates a model meta class name.
     * 
     * @param modelClassName
     *            a model class name
     * @return a model meta class name
     */
    protected ModelMetaClassName createModelMetaClassName(String modelClassName) {
        return new ModelMetaClassName(modelClassName, Options
            .getModelPackage(env), Options.getMetaPackage(env), Options
            .getSharedPackage(env), Options.getServerPackage(env));
    }

    /**
     * Collects the collection of class name.
     * 
     * @author taedium
     * @since 3.0
     * 
     */
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
