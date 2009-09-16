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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.slim3.gen.ClassConstants;
import org.slim3.gen.processor.Options;
import org.slim3.gen.util.DeclarationUtil;
import org.slim3.gen.util.StringUtil;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.Modifier;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.ArrayType;
import com.sun.mirror.type.DeclaredType;
import com.sun.mirror.type.InterfaceType;
import com.sun.mirror.type.PrimitiveType;
import com.sun.mirror.type.ReferenceType;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.type.VoidType;
import com.sun.mirror.type.WildcardType;
import com.sun.mirror.type.PrimitiveType.Kind;
import com.sun.mirror.util.SimpleTypeVisitor;

/**
 * Creates a model meta description.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class ModelMetaDescFactory {

    /** the environment */
    protected final AnnotationProcessorEnvironment env;

    /** the attribute meta description factory */
    protected final AttributeMetaDescFactory attributeMetaDescFactory;

    /**
     * Creates a new {@link ModelMetaDescFactory}.
     * 
     * @param env
     *            the environment
     * @param attributeMetaDescFactory
     *            the attribute meta description factory
     */
    public ModelMetaDescFactory(AnnotationProcessorEnvironment env,
            AttributeMetaDescFactory attributeMetaDescFactory) {
        if (env == null) {
            throw new NullPointerException("The env parameter is null.");
        }
        if (attributeMetaDescFactory == null) {
            throw new NullPointerException(
                "The attributeMetaDescFactory parameter is null.");
        }
        this.env = env;
        this.attributeMetaDescFactory = attributeMetaDescFactory;
    }

    /**
     * Creates a model meta description.
     * 
     * @param modelDeclaration
     *            the model declaration.
     * @return a model description
     */
    public ModelMetaDesc createModelMetaDesc(ClassDeclaration modelDeclaration) {
        if (modelDeclaration == null) {
            throw new NullPointerException(
                "The classDeclaration parameter is null.");
        }
        String modelClassName = modelDeclaration.getQualifiedName().toString();
        ModelMetaClassName modelMetaClassName =
            createModelMetaClassName(modelClassName);
        ModelMetaDesc modelMetaDesc = new ModelMetaDesc();
        modelMetaDesc.setPackageName(modelMetaClassName.getPackageName());
        modelMetaDesc.setSimpleName(modelMetaClassName.getSimpleName());
        modelMetaDesc.setModelClassName(modelClassName);
        handleAttributes(modelDeclaration, modelMetaDesc);
        return modelMetaDesc;
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
     * Handles attributes.
     * 
     * @param modelDeclaration
     *            the model declaration.
     * @param modelMetaDesc
     *            the model meta description
     */
    protected void handleAttributes(ClassDeclaration modelDeclaration,
            ModelMetaDesc modelMetaDesc) {
        for (FieldDeclaration attributeDeclaration : modelDeclaration
            .getFields()) {
            handleAttribute(attributeDeclaration, modelMetaDesc);
        }
    }

    /**
     * Handles an attribute.
     * 
     * @param attributeDeclaration
     *            an attribute declaration.
     * @param modelMetaDesc
     *            the model meta description.
     */
    protected void handleAttribute(FieldDeclaration attributeDeclaration,
            ModelMetaDesc modelMetaDesc) {
        AttributeMetaDesc attributeMetaDesc =
            attributeMetaDescFactory
                .createAttributeMetaDesc(attributeDeclaration);
        if (attributeMetaDesc != null) {
            modelMetaDesc.addAttributeMetaDesc(attributeMetaDesc);
        }
    }

    protected void handleAttributeMetaDescs(ClassDeclaration modelDeclaration,
            ModelMetaDesc modelMetaDesc) {
        for (ClassDeclaration c = modelDeclaration; c != null
            && !c.getQualifiedName().equals("java.lang.Object"); c =
            c.getSuperclass().getDeclaration()) {
            handleAttributeMetaDescs0(modelDeclaration, modelMetaDesc);
        }
    }

    protected void handleAttributeMetaDescs0(ClassDeclaration modelDeclaration,
            ModelMetaDesc modelMetaDesc) {
        List<MethodDeclaration> methodDeclarations =
            getMethodDeclarations(modelDeclaration);
        for (FieldDeclaration field : getFieldDeclarations(modelDeclaration)) {
            AttributeMetaDesc attributeMetaDesc =
                new AttributeMetaDesc(field.getSimpleName(), field
                    .getDeclaringType()
                    .getQualifiedName());
            if (isPrimaryKeyAnnotated(field)) {
                attributeMetaDesc.setPrimaryKey(true);
            }
            if (isVersionAnnotated(field)) {
                attributeMetaDesc.setVersion(true);
            }
            if (isTextAnnotated(field)) {
                attributeMetaDesc.setText(true);
            }
            if (isBlobAnnotated(field)) {
                attributeMetaDesc.setBlob(true);
            }
            if (isImpermanentAnnotated(field)) {
                attributeMetaDesc.setImpermanent(true);
            }
            attributeMetaDesc.setUnindexed(false);
            if (isCollection(field.getType())) {
                DeclaredType declaredType = toDeclaredType(field.getType());
                if (declaredType != null) {
                    attributeMetaDesc.setCollection(true);
                    attributeMetaDesc
                        .setAttributeElementClassName(getElementNameOfCollection(declaredType));
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
            modelMetaDesc.addAttributeMetaDesc(
                attributeMetaDesc.getName(),
                attributeMetaDesc);
        }
    }

    protected boolean isReadMethod(MethodDeclaration m,
            AttributeMetaDesc attributeMetaDesc) {
        String propertyName = null;
        if (m.getSimpleName().startsWith("get")) {
            propertyName =
                StringUtil.decapitalize(m.getSimpleName().substring(3));
        } else if (m.getSimpleName().startsWith("is")) {
            propertyName =
                StringUtil.decapitalize(m.getSimpleName().substring(2));
        } else {
            return false;
        }
        if (m.getParameters().size() != 0 || isVoid(m.getReturnType())) {
            return false;
        }
        if (!propertyName.equals(attributeMetaDesc.getName())) {
            return false;
        }
        TypeDeclaration propertyClass = toTypeDeclaration(m.getReturnType());
        if (propertyClass == null) {
            return false;
        }
        if (!propertyClass.getQualifiedName().equals(
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
        if (m.getParameters().size() != 1 || isVoid(m.getReturnType())) {
            return false;
        }

        if (!propertyName.equals(attributeMetaDesc.getName())) {
            return false;
        }
        TypeDeclaration propertyClass =
            toTypeDeclaration(m.getParameters().iterator().next().getType());
        if (propertyClass == null) {
            return false;
        }
        if (!propertyClass.getQualifiedName().equals(
            attributeMetaDesc.getAttributeClassName())) {
            return false;
        }
        return true;
    }

    protected void validateAttributeMetaDesc(AttributeMetaDesc attributeMetaDesc) {
        //
    }

    protected List<FieldDeclaration> getFieldDeclarations(
            ClassDeclaration classDeclaration) {
        List<FieldDeclaration> results = new ArrayList<FieldDeclaration>();
        for (ClassDeclaration c = classDeclaration; c != null
            && !c.getQualifiedName().equals(Object.class.getName()); c =
            c.getSuperclass().getDeclaration()) {
            gatherClassDeclararedField(classDeclaration, results);
        }

        // filter hidden fields

        return results;
    }

    protected void gatherClassDeclararedField(
            ClassDeclaration classDeclaration,
            List<FieldDeclaration> fieldDeclarations) {
        for (FieldDeclaration field : classDeclaration.getFields()) {
            Collection<Modifier> modifiers = field.getModifiers();
            if (!modifiers.contains(Modifier.STATIC)) {
                fieldDeclarations.add(field);
            }
        }
    }

    protected List<MethodDeclaration> getMethodDeclarations(
            ClassDeclaration classDeclaration) {
        List<MethodDeclaration> results = new ArrayList<MethodDeclaration>();
        for (ClassDeclaration c = classDeclaration; c != null
            && !c.getQualifiedName().equals(Object.class.getName()); c =
            c.getSuperclass().getDeclaration()) {
            gatherClassMethod(classDeclaration, results);
            for (InterfaceType interfaceType : c.getSuperinterfaces()) {
                InterfaceDeclaration interfaceDeclaration =
                    interfaceType.getDeclaration();
                if (interfaceDeclaration == null) {
                    continue;
                }
                gatherInterfaceMethods(interfaceDeclaration, results);
            }
        }
        return results;
    }

    protected void gatherClassMethod(ClassDeclaration classDeclaration,
            List<MethodDeclaration> methodDeclarations) {
        for (MethodDeclaration method : classDeclaration.getMethods()) {
            Collection<Modifier> modifiers = method.getModifiers();
            if (modifiers.contains(Modifier.PUBLIC)
                && !modifiers.contains(Modifier.STATIC)) {
                methodDeclarations.add(method);
            }
        }
    }

    protected void gatherInterfaceMethods(
            InterfaceDeclaration interfaceDeclaration,
            List<MethodDeclaration> methodDeclarations) {
        for (MethodDeclaration method : interfaceDeclaration.getMethods()) {
            methodDeclarations.add(method);
        }
        for (InterfaceType interfaceType : interfaceDeclaration
            .getSuperinterfaces()) {
            InterfaceDeclaration superInterfaceDeclaration =
                interfaceType.getDeclaration();
            if (superInterfaceDeclaration == null) {
                continue;
            }
            gatherInterfaceMethods(
                superInterfaceDeclaration,
                methodDeclarations);
        }
    }

    protected boolean isVoid(TypeMirror typeMirror) {
        class Visitor extends SimpleTypeVisitor {
            boolean result;

            @Override
            public void visitVoidType(VoidType voidtype) {
                result = true;
            }
        }
        Visitor visitor = new Visitor();
        typeMirror.accept(visitor);
        return visitor.result;
    }

    protected boolean isPrimitiveBoolean(TypeMirror typeMirror) {
        class Visitor extends SimpleTypeVisitor {
            boolean result;

            @Override
            public void visitPrimitiveType(PrimitiveType primitivetype) {
                result = primitivetype.getKind() == Kind.BOOLEAN;
            }
        }
        Visitor visitor = new Visitor();
        typeMirror.accept(visitor);
        return visitor.result;
    }

    protected TypeDeclaration toTypeDeclaration(TypeMirror typeMirror) {
        class Visitor extends SimpleTypeVisitor {
            TypeDeclaration result;

            @Override
            public void visitDeclaredType(DeclaredType declaredtype) {
                result = declaredtype.getDeclaration();
            }
        }
        Visitor visitor = new Visitor();
        typeMirror.accept(visitor);
        return visitor.result;
    }

    protected DeclaredType toDeclaredType(TypeMirror typeMirror) {
        class Visitor extends SimpleTypeVisitor {
            DeclaredType result;

            @Override
            public void visitDeclaredType(DeclaredType declaredtype) {
                result = declaredtype;
            }
        }
        Visitor visitor = new Visitor();
        typeMirror.accept(visitor);
        return visitor.result;
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

    protected boolean isCollection(TypeMirror typeMirror) {
        TypeDeclaration collection =
            env.getTypeDeclaration(Collection.class.getName());
        return env.getTypeUtils().isSubtype(
            env.getTypeUtils().getErasure(typeMirror),
            env.getTypeUtils().getDeclaredType(collection));
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
