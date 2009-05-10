/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.slim3.gen.generator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.ElementScanner6;
import javax.lang.model.util.SimpleElementVisitor6;
import javax.lang.model.util.SimpleTypeVisitor6;

import org.slim3.gen.ClassConstants;
import org.slim3.gen.Options;
import org.slim3.gen.util.ElementUtil;
import org.slim3.gen.util.Logger;

/**
 * Collects attribute descriptions of a JDO model.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class AttributeDescCollector extends
        ElementScanner6<List<AttributeDesc>, Void> {

    /** the document URL */
    protected static final String docURL = "http://code.google.com/intl/en/appengine/docs/java/datastore/dataclasses.html";

    /** the list of unsupported package names */
    protected static final List<String> unsupportedPackageNameList = new ArrayList<String>();
    static {
        unsupportedPackageNameList.add("java.math");
        unsupportedPackageNameList.add("java.sql");
    }

    /** the list of unsupported class names */
    protected static final List<String> unsupportedClassNameList = new ArrayList<String>();
    static {
        unsupportedClassNameList.add(Calendar.class.getName());
        unsupportedClassNameList.add(Character.class.getName());
    }

    /** the processing environment */
    protected final ProcessingEnvironment processingEnv;

    /** the collection of imported names */
    protected final ImportedNames importedNames;

    /** the list of attribute description */
    protected final List<AttributeDesc> attributeDescList;

    /**
     * Creates a new {@link AttributeDescCollector}.
     * 
     * @param defaultValue
     *            the default value.
     * @param importedNames
     *            the collection of imported names
     * @param processingEnv
     *            the processing environment
     */
    public AttributeDescCollector(List<AttributeDesc> defaultValue,
            ImportedNames importedNames, ProcessingEnvironment processingEnv) {
        super(defaultValue);
        this.processingEnv = processingEnv;
        this.importedNames = importedNames;
        this.attributeDescList = defaultValue;
    }

    @Override
    public List<AttributeDesc> visitVariable(VariableElement attribute, Void p) {
        AnnotationMirror persistent = ElementUtil.getAnnotationMirror(
                attribute, ClassConstants.Persistent);
        if (persistent == null) {
            return attributeDescList;
        }

        boolean modelType = isModelType(attribute);
        boolean serialized = false;
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : persistent
                .getElementValues().entrySet()) {

            ExecutableElement element = entry.getKey();
            AnnotationValue annotationValue = entry.getValue();
            if (element.getSimpleName().contentEquals(
                    ClassConstants.Persistent$serialized)) {
                serialized = Boolean.valueOf(annotationValue.toString());
            } else if (element.getSimpleName().contentEquals(
                    ClassConstants.Persistent$mappedBy)) {
                if (Options.isValidationEnabled(processingEnv)) {
                    validateMappedBy(modelType, attribute, persistent,
                            annotationValue);
                }
            }
        }

        String name = attribute.getSimpleName().toString();
        String typeName = new AttributeTypeNameBuilder(attribute, serialized)
                .build();
        AttributeDesc attributeDesc = new AttributeDesc(name, typeName,
                modelType);
        attributeDescList.add(attributeDesc);

        return attributeDescList;
    }

    /**
     * Returns {@code true} if the attribute type is a model type.
     * 
     * @param attribute
     *            the element of an attribute
     * @return {@code true} if the attribute type is a model type.
     */
    protected boolean isModelType(Element attribute) {
        Element e = processingEnv.getTypeUtils().asElement(attribute.asType());
        if (e != null
                && ElementUtil.getAnnotationMirror(e,
                        ClassConstants.PersistenceCapable) != null) {
            return true;
        }
        return false;
    }

    /**
     * Validates mappdeBy element of {@code javax.jdo.annotations.Persistent}.
     * 
     * @param modelType
     *            {@code true} if the attribute type is a model type.
     * @param attribute
     *            the element of an attribute
     * @param persistent
     *            the {@code javax.jdo.annotations.Persistent} annotation
     * @param mappedBy
     *            the value of the mappdeBy element
     */
    protected void validateMappedBy(boolean modelType, Element attribute,
            AnnotationMirror persistent, final AnnotationValue mappedBy) {

        if (!modelType) {
            Logger
                    .error(
                            processingEnv,
                            attribute,
                            "[slim3-gen] The mappedBy element is unavailable for a non Entity property(%s).",
                            attribute);
            return;
        }

        Element model = processingEnv.getTypeUtils().asElement(
                attribute.asType());
        boolean mapped = model.accept(
                new ElementScanner6<Boolean, Void>(false) {

                    @Override
                    public Boolean visitType(TypeElement model, Void p) {
                        for (Element e : model.getEnclosedElements()) {
                            if (e.getKind() == ElementKind.FIELD) {
                                if (scan(e, p)) {
                                    return true;
                                }
                            }
                        }
                        return false;
                    }

                    @Override
                    public Boolean visitVariable(VariableElement attribute,
                            Void p) {
                        if (ElementUtil.getAnnotationMirror(attribute,
                                ClassConstants.Persistent) != null) {
                            return attribute.getSimpleName().contentEquals(
                                    mappedBy.toString());
                        }
                        return false;
                    }
                }, null);

        if (!mapped) {
            Logger
                    .error(
                            processingEnv,
                            attribute,
                            persistent,
                            mappedBy,
                            "[slim3-gen] The mappedBy property(%1$s) is not found in a Entity class(%2$s). The property(%1$s) must be annotated with a Persistent annotation.",
                            mappedBy, model);
        }
    }

    /**
     * Builds a string of a type name.
     * 
     * @author taedium
     * @since 3.0
     * 
     */
    protected class AttributeTypeNameBuilder extends
            SimpleTypeVisitor6<Void, StringBuilder> {

        /** the element which a message notified */
        protected Element notifiedElement;

        protected TypeMirror typeMirror;

        protected boolean serialized;

        public AttributeTypeNameBuilder(Element notifiedElement,
                boolean serialized) {
            this.notifiedElement = notifiedElement;
            this.typeMirror = notifiedElement.asType();
            this.serialized = serialized;
        }

        public String build() {
            StringBuilder buf = new StringBuilder();
            typeMirror.accept(this, buf);
            return buf.toString();
        }

        @Override
        public Void visitArray(ArrayType t, StringBuilder p) {
            t.getComponentType().accept(this, p);
            p.append("[]");
            return null;
        }

        @Override
        public Void visitPrimitive(PrimitiveType t, StringBuilder p) {
            processingEnv.getTypeUtils().boxedClass(t).asType().accept(this, p);
            return null;
        }

        @Override
        public Void visitWildcard(WildcardType t, StringBuilder p) {
            p.append("?");
            TypeMirror extendedBound = t.getExtendsBound();
            if (extendedBound != null) {
                p.append(" extends ");
                extendedBound.accept(this, p);
            }
            TypeMirror superBound = t.getSuperBound();
            if (superBound != null) {
                p.append(" super ");
                superBound.accept(this, p);
            }
            return null;
        }

        @Override
        public Void visitError(ErrorType t, StringBuilder p) {
            p.append("Object");
            return null;
        }

        @Override
        public Void visitDeclared(DeclaredType t, StringBuilder p) {
            appendName(t, p);
            List<? extends TypeMirror> typeArgs = t.getTypeArguments();
            if (typeArgs.size() > 0) {
                p.append("<");
                for (TypeMirror arg : typeArgs) {
                    arg.accept(this, p);
                    p.append(", ");
                }
                p.setLength(p.length() - 2);
                p.append(">");
            }
            return null;
        }

        /**
         * Appends a type name to {@code p}.
         * 
         * @param t
         *            the type.
         * @param p
         *            the object to which the type name is appended.
         */
        protected void appendName(DeclaredType t, StringBuilder p) {
            ElementVisitor<Void, StringBuilder> visitor = new SimpleElementVisitor6<Void, StringBuilder>() {
                @Override
                public Void visitType(TypeElement e, StringBuilder p) {
                    if (!serialized) {
                        if (Options.isValidationEnabled(processingEnv)) {
                            validateTypeName(e);
                        }
                    }
                    String qualifiedName = e.getQualifiedName().toString();
                    p.append(importedNames.add(qualifiedName));
                    return null;
                }

                protected void validateTypeName(TypeElement e) {
                    String packageName = processingEnv.getElementUtils()
                            .getPackageOf(e).getQualifiedName().toString();
                    String qualifiedName = e.getQualifiedName().toString();
                    if (unsupportedPackageNameList.contains(packageName)) {
                        Logger
                                .error(
                                        processingEnv,
                                        notifiedElement,
                                        "[slim3-gen] Package(%s) is not supported on Google App Engine. See %s",
                                        packageName, docURL);
                    }
                    if (unsupportedClassNameList.contains(qualifiedName)) {
                        Logger
                                .error(
                                        processingEnv,
                                        notifiedElement,
                                        "slim3-gen] Class(%s) is not supported on Google App Engine. See %s",
                                        qualifiedName, docURL);
                    }
                }
            };
            t.asElement().accept(visitor, p);
        }
    }
}
