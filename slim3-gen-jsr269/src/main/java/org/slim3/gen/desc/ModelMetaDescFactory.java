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
package org.slim3.gen.desc;

import static javax.lang.model.util.ElementFilter.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.slim3.datastore.Model;
import org.slim3.gen.AnnotationConstants;
import org.slim3.gen.datastore.PrimitiveBooleanType;
import org.slim3.gen.message.MessageCode;
import org.slim3.gen.processor.AptException;
import org.slim3.gen.processor.Options;
import org.slim3.gen.processor.UnknownDeclarationException;
import org.slim3.gen.processor.ValidationException;
import org.slim3.gen.util.DeclarationUtil;
import org.slim3.gen.util.StringUtil;

/**
 * Creates a model meta description.
 * 
 * @author taedium
 * @author oyama
 * @since 1.0.0
 * 
 */
public class ModelMetaDescFactory {

    /** the processing environment */
    protected final ProcessingEnvironment processingEnv;
    /** the round environment */
    protected final RoundEnvironment roundEnv;
    /** the attribute meta description factory */
    protected final AttributeMetaDescFactory attributeMetaDescFactory;

    /**
     * Creates a new {@link ModelMetaDescFactory}.
     * 
     * @param processingEnv
     *            the processing environment
     * @param roundEnv
     *            the round environment
     * @param attributeMetaDescFactory
     *            the attribute meta description factory
     */
    public ModelMetaDescFactory(ProcessingEnvironment processingEnv,
            RoundEnvironment roundEnv,
            AttributeMetaDescFactory attributeMetaDescFactory) {
        if (processingEnv == null) {
            throw new NullPointerException(
                "The processingEnv parameter is null.");
        }
        if (roundEnv == null) {
            throw new NullPointerException("The roundEnv parameter is null.");
        }
        if (attributeMetaDescFactory == null) {
            throw new NullPointerException(
                "The attributeMetaDescFactory parameter is null.");
        }
        this.processingEnv = processingEnv;
        this.roundEnv = roundEnv;
        this.attributeMetaDescFactory = attributeMetaDescFactory;
    }

    /**
     * Creates a model meta description.
     * 
     * @param classElement
     *            the model declaration.
     * @return a model description
     */
    public ModelMetaDesc createModelMetaDesc(TypeElement classElement) {
        validateTopLevel(classElement);
        validatePublicModifier(classElement);
        validateNonGenericType(classElement);
        validateDefaultConstructor(classElement);

        Model model = classElement.getAnnotation(Model.class);
        classElement.getAnnotationMirrors();
        if (model == null) {
            throw new IllegalStateException(AnnotationConstants.Model
                + " not found.");
        }

        String modelClassName = classElement.getQualifiedName().toString();
        ModelMetaClassName modelMetaClassName =
            createModelMetaClassName(modelClassName);

        String kind = null;
        List<String> classHierarchyList = new ArrayList<String>();
        PolyModelDesc polyModelDesc = createPolyModelDesc(classElement);
        if (polyModelDesc == null) {
            kind = getKind(model, modelMetaClassName.getKind());
        } else {
            kind = polyModelDesc.getKind();
            classHierarchyList = polyModelDesc.getClassHierarchyList();
            validateKind(classElement);
        }

        String schemaVersionName = model.schemaVersionName();
        validateSchemaVersionName(classElement, schemaVersionName);

        int schemaVersion = model.schemaVersion();

        String classHierarchyListName = model.classHierarchyListName();
        validateClassHierarchyListName(classElement, classHierarchyListName);

        ModelMetaDesc modelMetaDesc =
            new ModelMetaDesc(
                modelMetaClassName.getPackageName(),
                modelMetaClassName.getSimpleName(),
                classElement.getModifiers().contains(Modifier.ABSTRACT),
                modelClassName,
                kind,
                schemaVersionName,
                schemaVersion,
                classHierarchyListName,
                classHierarchyList);
        handleModelListener(modelMetaDesc, classElement, model);
        handleAttributes(classElement, modelMetaDesc);
        return modelMetaDesc;
    }

    /**
     * Creates the poly model description.
     * 
     * @param classElement
     *            the model declaration.
     * @return the poly model description.
     */
    protected PolyModelDesc createPolyModelDesc(TypeElement classElement) {
        String kind = null;
        LinkedList<String> classHierarchyList = new LinkedList<String>();
        for (TypeElement c = classElement; c != null
            && !c.getQualifiedName().equals(Object.class.getName()); c =
            (TypeElement) processingEnv.getTypeUtils().asElement(
                c.getSuperclass())) {
            Model anno = c.getAnnotation(Model.class);
            if (anno != null) {
                ModelMetaClassName modelMetaClassName =
                    createModelMetaClassName(c.getQualifiedName().toString());
                kind = getKind(anno, modelMetaClassName.getKind());
                classHierarchyList.addFirst(c.getQualifiedName().toString());
            }
        }
        if (classHierarchyList.size() <= 1) {
            return null;
        }
        classHierarchyList.removeFirst();
        return new PolyModelDesc(kind, classHierarchyList);
    }

    /**
     * Returns the kind.
     * 
     * @param anno
     *            the model annotation mirror.
     * @param defaultKind
     *            the default kind.
     * @return the kind
     */
    protected String getKind(Model anno, String defaultKind) {
        String value = anno.kind();
        if (value != null && value.length() > 0) {
            return value;
        }
        return defaultKind;
    }

    /**
     * Validates that nested level is top level.
     * 
     * @param classElement
     *            the class declaration
     */
    public static void validateTopLevel(TypeElement classElement) {
        if (classElement.getEnclosingElement().getKind() != ElementKind.PACKAGE) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1019,
                classElement);
        }
    }

    /**
     * Validates that modifier is public.
     * 
     * @param classElement
     *            the class declaration
     */
    public static void validatePublicModifier(TypeElement classElement) {
        if (!classElement.getModifiers().contains(Modifier.PUBLIC)) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1017,
                classElement);
        }
    }

    /**
     * Validates that the class is not generic type.
     * 
     * @param classElement
     */
    public static void validateNonGenericType(TypeElement classElement) {
        if (!classElement.getTypeParameters().isEmpty()) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1020,
                classElement);
        }
    }

    /**
     * Validates that the default constructor is existent.
     * 
     * @param classElement
     *            the class declaration
     */
    public static void validateDefaultConstructor(TypeElement classElement) {
        if (!DeclarationUtil.hasPublicDefaultConstructor(classElement)) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1020,
                classElement);
        }
    }

    /**
     * Validates that the kind is unspecified.
     * 
     * @param classElement
     *            the class declaration
     */
    protected void validateKind(TypeElement classElement) {
        Model anno = classElement.getAnnotation(Model.class);
        if (anno == null) {
            throw new IllegalStateException(AnnotationConstants.Model
                + " not found.");
        }
        String value = anno.kind();
        if (value != null && value.length() > 0) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1022,
                classElement);
        }
    }

    /**
     * Validates that the schemaVersionName is not empty.
     * 
     * @param classElement
     * @param schemaVersionName
     */
    protected void validateSchemaVersionName(TypeElement classElement,
            String schemaVersionName) {
        if (StringUtil.isEmpty(schemaVersionName)) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1023,
                classElement);
        }
    }

    /**
     * Validates that the classHierarchyListName is not empty.
     * 
     * @param classElement
     * @param classHierarchyListName
     */
    protected void validateClassHierarchyListName(TypeElement classElement,
            String classHierarchyListName) {
        if (StringUtil.isEmpty(classHierarchyListName)) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1049,
                classElement);
        }
    }

    /**
     * Creates a model meta class name.
     * 
     * @param modelClassName
     *            a model class name
     * @return a model meta class name
     */
    protected ModelMetaClassName createModelMetaClassName(String modelClassName) {
        return new ModelMetaClassName(
            modelClassName,
            Options.getModelPackage(processingEnv),
            Options.getMetaPackage(processingEnv),
            Options.getSharedPackage(processingEnv),
            Options.getServerPackage(processingEnv));
    }

    /**
     * Handles attributes.
     * 
     * @param classElement
     *            the model declaration.
     * @param modelMetaDesc
     *            the model meta description
     */
    protected void handleAttributes(TypeElement classElement,
            ModelMetaDesc modelMetaDesc) {
        List<ExecutableElement> methodDeclarations =
            getMethodDeclarations(classElement);
        Set<String> propertyNames = createPropertyNames(modelMetaDesc);
        Set<String> booleanAttributeNames = new HashSet<String>();
        for (VariableElement fieldDeclaration : getFieldDeclarations(classElement)) {
            AttributeMetaDesc attributeMetaDesc =
                createAttributeMetaDesc(
                    classElement,
                    fieldDeclaration,
                    methodDeclarations);
            if (attributeMetaDesc == null) {
                modelMetaDesc.setError(true);
                continue;
            }
            if (attributeMetaDesc.isPersistent()) {
                validatePrimaryKeyUniqueness(
                    attributeMetaDesc,
                    classElement,
                    modelMetaDesc);
                validateVersionUniqueness(
                    attributeMetaDesc,
                    classElement,
                    modelMetaDesc);
                validatePropertyNameUniqueness(
                    propertyNames,
                    attributeMetaDesc,
                    classElement,
                    fieldDeclaration);
                validateBooleanAttributeNameUniqueness(
                    booleanAttributeNames,
                    attributeMetaDesc,
                    classElement,
                    fieldDeclaration);
            }
            modelMetaDesc.addAttributeMetaDesc(attributeMetaDesc);
        }
        if (!modelMetaDesc.isError()
            && modelMetaDesc.getKeyAttributeMetaDesc() == null) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1015,
                classElement);
        }
        modelMetaDesc.createJsonAttributeMetaDescList();
    }

    /**
     * Handles the model listener.
     * 
     * @param modelMetaDesc
     *            the model meta description
     * @param classElement
     *            the model class declaration
     * @param model
     *            the annotation mirror for Model
     */
    protected void handleModelListener(ModelMetaDesc modelMetaDesc,
            TypeElement classElement, Model model) {
        AnnotationValue listener = null;
        for (AnnotationMirror mirror : classElement.getAnnotationMirrors()) {
            Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues =
                mirror.getElementValues();
            for (ExecutableElement e : elementValues.keySet()) {
                if (AnnotationConstants.listener.equals(e
                    .getSimpleName()
                    .toString())) {
                    listener = elementValues.get(e);
                }
            }
        }
        if (listener == null) {
            return;
        }
        TypeElement listenerEl =
            processingEnv.getElementUtils().getTypeElement(listener.toString());
        if (listenerEl.getKind() == ElementKind.INTERFACE) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1052,
                classElement);
        }
        boolean findConstructor = false;
        for (ExecutableElement constructor : constructorsIn(listenerEl
            .getEnclosedElements())) {
            if (constructor.getParameters().size() == 0) {
                findConstructor = true;
            }
        }
        if (!findConstructor) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1050,
                classElement,
                listenerEl.toString());
        }
        modelMetaDesc.setModelListenerClassName(listenerEl.toString());
    }

    /**
     * Creates property name set which contains reserved property names.
     * 
     * @param modelMetaDesc
     * @return property name set
     */
    protected Set<String> createPropertyNames(ModelMetaDesc modelMetaDesc) {
        Set<String> results = new HashSet<String>();
        results.add(modelMetaDesc.getClassHierarchyListName());
        results.add(modelMetaDesc.getSchemaVersionName());
        return results;
    }

    /**
     * Validates primary key uniqueness.
     * 
     * @param attributeMetaDesc
     * @param classElement
     * @param modelMetaDesc
     */
    protected void validatePrimaryKeyUniqueness(
            AttributeMetaDesc attributeMetaDesc, TypeElement classElement,
            ModelMetaDesc modelMetaDesc) {
        if (attributeMetaDesc.isPrimaryKey()
            && modelMetaDesc.getKeyAttributeMetaDesc() != null) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1013,
                classElement);
        }
    }

    /**
     * Validates version uniqueness.
     * 
     * @param attributeMetaDesc
     * @param classElement
     * @param modelMetaDesc
     */
    protected void validateVersionUniqueness(
            AttributeMetaDesc attributeMetaDesc, TypeElement classElement,
            ModelMetaDesc modelMetaDesc) {
        if (attributeMetaDesc.isVersion()
            && modelMetaDesc.getVersionAttributeMetaDesc() != null) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1014,
                classElement);
        }
    }

    /**
     * Validates property name uniqueness.
     * 
     * @param propertyNames
     * @param attributeMetaDesc
     * @param classElement
     * @param fieldDeclaration
     */
    protected void validatePropertyNameUniqueness(Set<String> propertyNames,
            AttributeMetaDesc attributeMetaDesc, TypeElement classElement,
            Element fieldDeclaration) {
        String propertyName = attributeMetaDesc.getName();
        if (propertyNames.contains(propertyName)) {
            // TODO equalsで比較していいんだっけ…？
            if (classElement.equals(fieldDeclaration.getEnclosingElement())) {
                throw new ValidationException(
                    MessageCode.SLIM3GEN1047,
                    fieldDeclaration,
                    propertyName);
            }
            throw new ValidationException(
                MessageCode.SLIM3GEN1048,
                classElement,
                propertyName,
                fieldDeclaration.getSimpleName(),
                fieldDeclaration.getEnclosingElement().toString());
        }
        propertyNames.add(propertyName);
    }

    /**
     * Validates boolean attribute name uniqueness.
     * 
     * @param booleanAttributeNames
     * @param attributeMetaDesc
     * @param classElement
     * @param fieldDeclaration
     */
    protected void validateBooleanAttributeNameUniqueness(
            Set<String> booleanAttributeNames,
            AttributeMetaDesc attributeMetaDesc, TypeElement classElement,
            Element fieldDeclaration) {
        if (attributeMetaDesc.getDataType() instanceof PrimitiveBooleanType) {
            String attributeName = attributeMetaDesc.getAttributeName();
            if (booleanAttributeNames.contains(attributeName)) {
                if (classElement.equals(fieldDeclaration.getEnclosingElement())) {
                    throw new ValidationException(
                        MessageCode.SLIM3GEN1043,
                        fieldDeclaration,
                        fieldDeclaration.getSimpleName());
                }
                throw new ValidationException(
                    MessageCode.SLIM3GEN1044,
                    classElement,
                    fieldDeclaration.getSimpleName(),
                    fieldDeclaration.getEnclosingElement().toString());
            }
            booleanAttributeNames.add(attributeName);
        }
    }

    /**
     * Creates a attribute meta description.
     * 
     * @param classElement
     *            the model declaration
     * @param fieldDeclaration
     * @param methodDeclarations
     * @return a attribute meta description or {@code null} if error occured.
     */
    protected AttributeMetaDesc createAttributeMetaDesc(
            TypeElement classElement, VariableElement fieldDeclaration,
            List<ExecutableElement> methodDeclarations) {
        try {
            return attributeMetaDescFactory.createAttributeMetaDesc(
                classElement,
                fieldDeclaration,
                methodDeclarations);
        } catch (AptException e) {
            e.sendError();
        }
        return null;
    }

    /**
     * Returns field declarations.
     * 
     * @param classElement
     *            the class declaration
     * @return field declarations
     */
    protected List<VariableElement> getFieldDeclarations(
            TypeElement classElement) {
        List<VariableElement> results = new LinkedList<VariableElement>();
        for (TypeElement c = classElement; c != null
            && !c.getQualifiedName().equals(Object.class.getName()); c =
            (TypeElement) processingEnv.getTypeUtils().asElement(
                c.getSuperclass())) {
            for (VariableElement element : fieldsIn(c.getEnclosedElements())) {
                Collection<Modifier> modifiers = element.getModifiers();
                if (!modifiers.contains(Modifier.STATIC)) {
                    results.add(element);
                }
            }
        }

        List<VariableElement> hiderFieldDeclarations =
            new LinkedList<VariableElement>();
        for (Iterator<VariableElement> it = results.iterator(); it.hasNext();) {
            Element hidden = it.next();
            for (Element hider : hiderFieldDeclarations) {
                if (processingEnv.getElementUtils().hides(hider, hidden)) {
                    it.remove();
                }
            }
        }
        return results;
    }

    /**
     * Returns method declarations.
     * 
     * @param classElement
     *            the class declaration
     * @return method declarations
     */
    protected List<ExecutableElement> getMethodDeclarations(
            TypeElement classElement) {
        List<ExecutableElement> results = new LinkedList<ExecutableElement>();
        for (TypeElement c = classElement; c != null
            && !c.getQualifiedName().toString().equals(Object.class.getName()); c =
            (TypeElement) processingEnv.getTypeUtils().asElement(
                c.getSuperclass())) {
            gatherClassMethodDeclarations(c, results);
            for (TypeMirror superinterfaceType : c.getInterfaces()) {
                TypeElement el =
                    (TypeElement) processingEnv.getTypeUtils().asElement(
                        superinterfaceType);
                TypeMirror superinterfaceDeclaration = el.getSuperclass();
                if (superinterfaceDeclaration == null) {
                    throw new UnknownDeclarationException(classElement, el);
                }
                TypeElement superEl =
                    (TypeElement) processingEnv.getTypeUtils().asElement(
                        superinterfaceDeclaration);
                gatherInterfaceMethodDeclarations(superEl, results);
            }
        }

        List<ExecutableElement> overriderMethodDeclarations =
            new LinkedList<ExecutableElement>();
        for (Iterator<ExecutableElement> it = results.iterator(); it.hasNext();) {
            Element overriden = it.next();
            for (Element overrider : overriderMethodDeclarations) {
                if (processingEnv.getElementUtils().overrides(
                    (ExecutableElement) overrider,
                    (ExecutableElement) overriden,
                    classElement)) {
                    it.remove();
                }
            }
        }
        return results;
    }

    /**
     * Gather class method declarations.
     * 
     * @param classElement
     *            the class declaration
     * @param methodDeclarations
     *            the list of method declarations
     */
    protected void gatherClassMethodDeclarations(TypeElement classElement,
            List<ExecutableElement> methodDeclarations) {
        for (ExecutableElement element : methodsIn(classElement
            .getEnclosedElements())) {
            Collection<Modifier> modifiers = element.getModifiers();
            if (modifiers.contains(Modifier.PUBLIC)
                && !modifiers.contains(Modifier.STATIC)) {
                methodDeclarations.add(element);
            }
        }
    }

    /**
     * Gather interface method declarations.
     * 
     * @param interfaceDeclaration
     *            the interface declaration
     * @param methodDeclarations
     *            the list of method declarations
     */
    protected void gatherInterfaceMethodDeclarations(
            TypeElement interfaceDeclaration,
            List<ExecutableElement> methodDeclarations) {
        for (ExecutableElement method : methodsIn(interfaceDeclaration
            .getEnclosedElements())) {
            methodDeclarations.add(method);
        }
        for (TypeMirror superinterfaceType : interfaceDeclaration
            .getInterfaces()) {

            TypeElement el =
                (TypeElement) processingEnv.getTypeUtils().asElement(
                    superinterfaceType);
            TypeMirror superInterfaceDeclaration = el.getSuperclass();
            if (superInterfaceDeclaration == null) {
                throw new UnknownDeclarationException(interfaceDeclaration, el);
            }
            TypeElement superEl =
                (TypeElement) processingEnv.getTypeUtils().asElement(
                    superInterfaceDeclaration);
            gatherInterfaceMethodDeclarations(superEl, methodDeclarations);
        }
    }

    /**
     * The poly model description.
     * 
     * @author taedium
     * 
     */
    public static class PolyModelDesc {

        /** the kind */
        protected final String kind;

        /** the class hierarchy list */
        protected final List<String> classHierarchyList;

        /**
         * Creates a new {@link PolyModelDesc}.
         * 
         * @param kind
         *            the kind
         * @param classHierarchyList
         *            the class hierarchy list
         */
        public PolyModelDesc(String kind, List<String> classHierarchyList) {
            if (kind == null) {
                throw new NullPointerException("The kind parameter is null.");
            }
            if (classHierarchyList == null) {
                throw new NullPointerException(
                    "The classHierarchyList parameter is null.");
            }
            this.kind = kind;
            this.classHierarchyList = classHierarchyList;
        }

        /**
         * Returns the kind.
         * 
         * @return the kind
         */
        public String getKind() {
            return kind;
        }

        /**
         * Returns the class hierarchy list.
         * 
         * @return the class hierarchy list
         */
        public List<String> getClassHierarchyList() {
            return classHierarchyList;
        }
    }
}
