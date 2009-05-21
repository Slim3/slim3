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
package org.slim3.gen.processor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
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
import javax.lang.model.util.TypeKindVisitor6;

import org.slim3.gen.ClassConstants;
import org.slim3.gen.desc.AttributeDesc;
import org.slim3.gen.desc.ModelDesc;
import org.slim3.gen.util.ClassUtil;
import org.slim3.gen.util.ElementUtil;

/**
 * Scans a JDO model.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class JDOModelScanner extends ElementScanner6<Void, ModelDesc> {

    /** the Google App Engine document URL */
    protected static final String documentURL = "http://code.google.com/intl/en/appengine/docs/java/datastore/dataclasses.html";

    /** the list of unsupported package names */
    protected static final List<String> unsupportedPackageNameList = new ArrayList<String>();
    static {
        unsupportedPackageNameList.add("java.sql");
    }

    /** the list of unsupported class names */
    protected static final List<String> unsupportedClassNameList = new ArrayList<String>();
    static {
        unsupportedClassNameList.add(Calendar.class.getName());
        unsupportedClassNameList.add(Character.class.getName());
        unsupportedClassNameList.add(BigInteger.class.getName());
    }

    /** the processing environment */
    protected final ProcessingEnvironment processingEnv;

    /**
     * Creates a new {@link JDOModelScanner}.
     * 
     * @param processingEnv
     *            the processing environment
     */
    public JDOModelScanner(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    @Override
    public Void visitVariable(VariableElement attribute, ModelDesc p) {
        AnnotationMirror persistent = ElementUtil.getAnnotationMirror(
                attribute, ClassConstants.Persistent);
        if (persistent == null) {
            return null;
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
        AttributeDesc attributeDesc = new AttributeDesc();
        attributeDesc.setName(attribute.getSimpleName().toString());
        attributeDesc.setModelType(modelType);
        Iterator<String> classNames = new ClassNameCollector(attribute.asType())
                .collect().iterator();
        if (classNames.hasNext()) {
            String className = classNames.next();
            validateClassName(className, serialized, attribute);
            attributeDesc.setClassName(className);
        }
        if (classNames.hasNext()) {
            String elementClassName = classNames.next();
            validateClassName(elementClassName, serialized, attribute);
            attributeDesc.setElementClassName(elementClassName);
        }
        p.addAttributeDesc(attributeDesc);
        return null;
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
                            "[slim3-gen] The mappedBy property(%1$s) is not found in an Entity class(%2$s). The property(%1$s) must be annotated with a Persistent annotation.",
                            mappedBy, model);
        }
    }

    /**
     * Validates the className.
     * 
     * @param className
     *            the class name
     * @param serialized
     *            {@code true} if the attribute element is marked as serialized
     * @param attribute
     *            the attribute element
     */
    protected void validateClassName(String className, boolean serialized,
            Element attribute) {
        String packageName = ClassUtil.getPackageName(className);
        if (unsupportedPackageNameList.contains(packageName)) {
            Logger
                    .error(
                            processingEnv,
                            attribute,
                            "[slim3-gen] Package(%s) is not supported on Google App Engine. See %s",
                            packageName, documentURL);
        }
        if (unsupportedClassNameList.contains(className)) {
            Logger
                    .error(
                            processingEnv,
                            attribute,
                            "[slim3-gen] Class(%s) is not supported on Google App Engine. See %s",
                            className, documentURL);
        }
    }

    /**
     * Collects the collection of class name.
     * 
     * @author taedium
     * @since 3.0
     * 
     */
    protected class ClassNameCollector extends
            SimpleTypeVisitor6<Void, LinkedList<String>> {

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

        public List<String> collect() {
            LinkedList<String> names = new LinkedList<String>();
            typeMirror.accept(this, names);
            return names;
        }

        @Override
        public Void visitArray(ArrayType t, LinkedList<String> p) {
            LinkedList<String> names = new LinkedList<String>();
            t.getComponentType().accept(this, names);
            p.add(names.getFirst() + "[]");
            p.add(names.getFirst());
            return null;
        }

        @Override
        public Void visitPrimitive(PrimitiveType t, LinkedList<String> p) {
            t.accept(new TypeKindVisitor6<Void, LinkedList<String>>() {

                @Override
                public Void visitPrimitiveAsBoolean(PrimitiveType t,
                        LinkedList<String> p) {
                    p.add(boolean.class.getSimpleName());
                    return null;
                }

                @Override
                public Void visitPrimitiveAsByte(PrimitiveType t,
                        LinkedList<String> p) {
                    p.add(byte.class.getSimpleName());
                    return null;
                }

                @Override
                public Void visitPrimitiveAsChar(PrimitiveType t,
                        LinkedList<String> p) {
                    p.add(char.class.getSimpleName());
                    return null;
                }

                @Override
                public Void visitPrimitiveAsDouble(PrimitiveType t,
                        LinkedList<String> p) {
                    p.add(double.class.getSimpleName());
                    return null;
                }

                @Override
                public Void visitPrimitiveAsFloat(PrimitiveType t,
                        LinkedList<String> p) {
                    p.add(float.class.getSimpleName());
                    return null;
                }

                @Override
                public Void visitPrimitiveAsInt(PrimitiveType t,
                        LinkedList<String> p) {
                    p.add(int.class.getSimpleName());
                    return null;
                }

                @Override
                public Void visitPrimitiveAsLong(PrimitiveType t,
                        LinkedList<String> p) {
                    p.add(long.class.getSimpleName());
                    return null;
                }

                @Override
                public Void visitPrimitiveAsShort(PrimitiveType t,
                        LinkedList<String> p) {
                    p.add(short.class.getSimpleName());
                    return null;
                }
            }, p);
            return null;
        }

        @Override
        public Void visitWildcard(WildcardType t, LinkedList<String> p) {
            TypeMirror extendedBound = t.getExtendsBound();
            if (extendedBound != null) {
                LinkedList<String> names = new LinkedList<String>();
                extendedBound.accept(this, names);
                p.add(names.getFirst());
            }
            TypeMirror superBound = t.getSuperBound();
            if (superBound != null) {
                LinkedList<String> names = new LinkedList<String>();
                superBound.accept(this, names);
                p.add(names.getFirst());
            }
            return null;
        }

        @Override
        public Void visitError(ErrorType t, LinkedList<String> p) {
            p.add("Object");
            return null;
        }

        @Override
        public Void visitDeclared(DeclaredType t, LinkedList<String> p) {
            t.asElement().accept(
                    new SimpleElementVisitor6<Void, LinkedList<String>>() {
                        @Override
                        public Void visitType(TypeElement e,
                                LinkedList<String> p) {
                            p.add(e.getQualifiedName().toString());
                            return null;
                        }
                    }, p);
            for (TypeMirror arg : t.getTypeArguments()) {
                LinkedList<String> names = new LinkedList<String>();
                arg.accept(this, names);
                p.add(names.getFirst());
            }
            return null;
        }
    }
}
