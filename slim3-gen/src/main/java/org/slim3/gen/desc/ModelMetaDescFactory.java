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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.slim3.gen.AnnotationConstants;
import org.slim3.gen.datastore.PrimitiveBooleanType;
import org.slim3.gen.message.MessageCode;
import org.slim3.gen.processor.AptException;
import org.slim3.gen.processor.Options;
import org.slim3.gen.processor.UnknownDeclarationException;
import org.slim3.gen.processor.ValidationException;
import org.slim3.gen.util.AnnotationMirrorUtil;
import org.slim3.gen.util.DeclarationUtil;
import org.slim3.gen.util.StringUtil;
import org.slim3.gen.util.TypeUtil;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.Modifier;
import com.sun.mirror.type.ClassType;
import com.sun.mirror.type.InterfaceType;
import com.sun.mirror.type.TypeMirror;

/**
 * Creates a model meta description.
 * 
 * @author taedium
 * @author oyama
 * @since 1.0.0
 * 
 */
@SuppressWarnings("deprecation")
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
     * @param classDeclaration
     *            the model declaration.
     * @return a model description
     */
    public ModelMetaDesc createModelMetaDesc(ClassDeclaration classDeclaration) {
        if (classDeclaration == null) {
            throw new NullPointerException(
                "The classDeclaration parameter is null.");
        }
        validateTopLevel(classDeclaration);
        validatePublicModifier(classDeclaration);
        validateNonGenericType(classDeclaration);
        validateDefaultConstructor(classDeclaration);

        AnnotationMirror model =
            DeclarationUtil.getAnnotationMirror(
                env,
                classDeclaration,
                AnnotationConstants.Model);
        if (model == null) {
            throw new IllegalStateException(AnnotationConstants.Model
                + " not found.");
        }

        String modelClassName = classDeclaration.getQualifiedName().toString();
        ModelMetaClassName modelMetaClassName =
            createModelMetaClassName(modelClassName);

        String kind = null;
        List<String> classHierarchyList = new ArrayList<String>();
        PolyModelDesc polyModelDesc = createPolyModelDesc(classDeclaration);
        if (polyModelDesc == null) {
            kind = getKind(model, modelMetaClassName.getKind());
        } else {
            kind = polyModelDesc.getKind();
            classHierarchyList = polyModelDesc.getClassHierarchyList();
            validateKind(classDeclaration);
        }

        String schemaVersionName =
            AnnotationMirrorUtil.getElementValueWithDefault(
                model,
                AnnotationConstants.schemaVersionName);
        validateSchemaVersionName(classDeclaration, schemaVersionName);

        Integer schemaVersion =
            AnnotationMirrorUtil.getElementValueWithDefault(
                model,
                AnnotationConstants.schemaVersion);

        String classHierarchyListName =
            AnnotationMirrorUtil.getElementValueWithDefault(
                model,
                AnnotationConstants.classHierarchyListName);
        validateClassHierarchyListName(classDeclaration, classHierarchyListName);

        ModelMetaDesc modelMetaDesc =
            new ModelMetaDesc(
                modelMetaClassName.getPackageName(),
                modelMetaClassName.getSimpleName(),
                classDeclaration.getModifiers().contains(Modifier.ABSTRACT),
                modelClassName,
                kind,
                schemaVersionName,
                schemaVersion.intValue(),
                classHierarchyListName,
                classHierarchyList);
        handleModelListener(modelMetaDesc, classDeclaration, model);
        handleAttributes(classDeclaration, modelMetaDesc);
        return modelMetaDesc;
    }

    /**
     * Creates the poly model description.
     * 
     * @param classDeclaration
     *            the model declaration.
     * @return the poly model description.
     */
    protected PolyModelDesc createPolyModelDesc(
            ClassDeclaration classDeclaration) {
        String kind = null;
        LinkedList<String> classHierarchyList = new LinkedList<String>();
        for (ClassDeclaration c = classDeclaration; c != null
            && !c.getQualifiedName().equals(Object.class.getName()); c =
            c.getSuperclass().getDeclaration()) {
            AnnotationMirror anno =
                DeclarationUtil.getAnnotationMirror(
                    env,
                    c,
                    AnnotationConstants.Model);
            if (anno != null) {
                ModelMetaClassName modelMetaClassName =
                    createModelMetaClassName(c.getQualifiedName().toString());
                kind = getKind(anno, modelMetaClassName.getKind());
                classHierarchyList.addFirst(c.getQualifiedName());
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
    protected String getKind(AnnotationMirror anno, String defaultKind) {
        String value =
            AnnotationMirrorUtil
                .getElementValue(anno, AnnotationConstants.kind);
        if (value != null && value.length() > 0) {
            return value;
        }
        return defaultKind;
    }

    /**
     * Validates that nested level is top level.
     * 
     * @param classDeclaration
     *            the class declaration
     */
    protected void validateTopLevel(ClassDeclaration classDeclaration) {
        if (classDeclaration.getDeclaringType() != null) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1019,
                env,
                classDeclaration.getPosition());
        }
    }

    /**
     * Validates that modifier is public.
     * 
     * @param classDeclaration
     *            the class declaration
     */
    protected void validatePublicModifier(ClassDeclaration classDeclaration) {
        if (!classDeclaration.getModifiers().contains(Modifier.PUBLIC)) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1017,
                env,
                classDeclaration.getPosition());
        }
    }

    /**
     * Validates that the class is not generic type.
     * 
     * @param classDeclaration
     */
    protected void validateNonGenericType(ClassDeclaration classDeclaration) {
        if (!classDeclaration.getFormalTypeParameters().isEmpty()) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1020,
                env,
                classDeclaration.getPosition());
        }
    }

    /**
     * Validates that the default constructor is existent.
     * 
     * @param classDeclaration
     *            the class declaration
     */
    protected void validateDefaultConstructor(ClassDeclaration classDeclaration) {
        if (!DeclarationUtil.hasPublicDefaultConstructor(classDeclaration)) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1018,
                env,
                classDeclaration.getPosition());
        }
    }

    /**
     * Validates that the kind is unspecified.
     * 
     * @param classDeclaration
     *            the class declaration
     */
    protected void validateKind(ClassDeclaration classDeclaration) {
        AnnotationMirror anno =
            DeclarationUtil.getAnnotationMirror(
                env,
                classDeclaration,
                AnnotationConstants.Model);
        if (anno == null) {
            throw new IllegalStateException(AnnotationConstants.Model
                + " not found.");
        }
        String value =
            AnnotationMirrorUtil
                .getElementValue(anno, AnnotationConstants.kind);
        if (value != null && value.length() > 0) {
            throw new ValidationException(MessageCode.SLIM3GEN1022, env, anno
                .getPosition());
        }
    }

    /**
     * Validates that the schemaVersionName is not empty.
     * 
     * @param classDeclaration
     * @param schemaVersionName
     */
    protected void validateSchemaVersionName(ClassDeclaration classDeclaration,
            String schemaVersionName) {
        if (StringUtil.isEmpty(schemaVersionName)) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1023,
                env,
                classDeclaration.getPosition());
        }
    }

    /**
     * Validates that the classHierarchyListName is not empty.
     * 
     * @param classDeclaration
     * @param classHierarchyListName
     */
    protected void validateClassHierarchyListName(
            ClassDeclaration classDeclaration, String classHierarchyListName) {
        if (StringUtil.isEmpty(classHierarchyListName)) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1049,
                env,
                classDeclaration.getPosition());
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
        return new ModelMetaClassName(modelClassName, Options
            .getModelPackage(env), Options.getMetaPackage(env), Options
            .getSharedPackage(env), Options.getServerPackage(env));
    }

    /**
     * Handles attributes.
     * 
     * @param classDeclaration
     *            the model declaration.
     * @param modelMetaDesc
     *            the model meta description
     */
    protected void handleAttributes(ClassDeclaration classDeclaration,
            ModelMetaDesc modelMetaDesc) {
        List<MethodDeclaration> methodDeclarations =
            getMethodDeclarations(classDeclaration);
        Set<String> propertyNames = createPropertyNames(modelMetaDesc);
        Set<String> booleanAttributeNames = new HashSet<String>();
        for (FieldDeclaration fieldDeclaration : getFieldDeclarations(classDeclaration)) {
            AttributeMetaDesc attributeMetaDesc =
                createAttributeMetaDesc(
                    classDeclaration,
                    fieldDeclaration,
                    methodDeclarations);
            if (attributeMetaDesc == null) {
                modelMetaDesc.setError(true);
                continue;
            }
            if (attributeMetaDesc.isPersistent()) {
                validatePrimaryKeyUniqueness(
                    attributeMetaDesc,
                    classDeclaration,
                    modelMetaDesc);
                validateVersionUniqueness(
                    attributeMetaDesc,
                    classDeclaration,
                    modelMetaDesc);
                validatePropertyNameUniqueness(
                    propertyNames,
                    attributeMetaDesc,
                    classDeclaration,
                    fieldDeclaration);
                validateBooleanAttributeNameUniqueness(
                    booleanAttributeNames,
                    attributeMetaDesc,
                    classDeclaration,
                    fieldDeclaration);
            }
            modelMetaDesc.addAttributeMetaDesc(attributeMetaDesc);
        }
        if (!modelMetaDesc.isError()
            && modelMetaDesc.getKeyAttributeMetaDesc() == null) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1015,
                env,
                classDeclaration.getPosition());
        }
        modelMetaDesc.createJsonAttributeMetaDescList();
    }
    
    /**
     * Handles the model listener.
     * 
     * @param modelMetaDesc
     *            the model meta description
     * @param classDeclaration
     *            the model class declaration
     * @param model
     *            the annotation mirror for Model
     */
    protected void handleModelListener(ModelMetaDesc modelMetaDesc,
            ClassDeclaration classDeclaration, AnnotationMirror model) {
        Object listener =
            AnnotationMirrorUtil.getElementValue(
                model,
                AnnotationConstants.listener);
        if (listener == null) {
            return;
        }
        if (listener instanceof InterfaceType) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1052,
                env,
                classDeclaration.getPosition());
        }
        ClassType listenerClassType =
            TypeUtil.toClassType((TypeMirror) listener);
        if (listenerClassType == null) {
            return;
        }
        ClassDeclaration listenerClassDeclaration =
            listenerClassType.getDeclaration();
        if (listenerClassDeclaration == null) {
            throw new UnknownDeclarationException(
                env,
                listenerClassDeclaration,
                listenerClassType);
        }
        if (!DeclarationUtil
            .hasPublicDefaultConstructor(listenerClassDeclaration)) {
                throw new ValidationException(
                    MessageCode.SLIM3GEN1050,
                    env,
                    classDeclaration.getPosition(),
                    listenerClassDeclaration.getQualifiedName());
        }
        modelMetaDesc
            .setModelListenerClassName(listenerClassDeclaration
                .getQualifiedName());
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
     * @param classDeclaration
     * @param modelMetaDesc
     */
    protected void validatePrimaryKeyUniqueness(
            AttributeMetaDesc attributeMetaDesc,
            ClassDeclaration classDeclaration, ModelMetaDesc modelMetaDesc) {
        if (attributeMetaDesc.isPrimaryKey()
            && modelMetaDesc.getKeyAttributeMetaDesc() != null) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1013,
                env,
                classDeclaration.getPosition());
        }
    }

    /**
     * Validates version uniqueness.
     * 
     * @param attributeMetaDesc
     * @param classDeclaration
     * @param modelMetaDesc
     */
    protected void validateVersionUniqueness(
            AttributeMetaDesc attributeMetaDesc,
            ClassDeclaration classDeclaration, ModelMetaDesc modelMetaDesc) {
        if (attributeMetaDesc.isVersion()
            && modelMetaDesc.getVersionAttributeMetaDesc() != null) {
            throw new ValidationException(
                MessageCode.SLIM3GEN1014,
                env,
                classDeclaration.getPosition());
        }
    }

    /**
     * Validates property name uniqueness.
     * 
     * @param propertyNames
     * @param attributeMetaDesc
     * @param classDeclaration
     * @param fieldDeclaration
     */
    protected void validatePropertyNameUniqueness(Set<String> propertyNames,
            AttributeMetaDesc attributeMetaDesc,
            ClassDeclaration classDeclaration, FieldDeclaration fieldDeclaration) {
        String propertyName = attributeMetaDesc.getName();
        if (propertyNames.contains(propertyName)) {
            if (classDeclaration.equals(fieldDeclaration.getDeclaringType())) {
                throw new ValidationException(
                    MessageCode.SLIM3GEN1047,
                    env,
                    fieldDeclaration.getPosition(),
                    propertyName);
            }
            throw new ValidationException(
                MessageCode.SLIM3GEN1048,
                env,
                classDeclaration.getPosition(),
                propertyName,
                fieldDeclaration.getSimpleName(),
                fieldDeclaration.getDeclaringType().getQualifiedName());
        }
        propertyNames.add(propertyName);
    }

    /**
     * Validates boolean attribute name uniqueness.
     * 
     * @param booleanAttributeNames
     * @param attributeMetaDesc
     * @param classDeclaration
     * @param fieldDeclaration
     */
    protected void validateBooleanAttributeNameUniqueness(
            Set<String> booleanAttributeNames,
            AttributeMetaDesc attributeMetaDesc,
            ClassDeclaration classDeclaration, FieldDeclaration fieldDeclaration) {
        if (attributeMetaDesc.getDataType() instanceof PrimitiveBooleanType) {
            String attributeName = attributeMetaDesc.getAttributeName();
            if (booleanAttributeNames.contains(attributeName)) {
                if (classDeclaration
                    .equals(fieldDeclaration.getDeclaringType())) {
                    throw new ValidationException(
                        MessageCode.SLIM3GEN1043,
                        env,
                        fieldDeclaration.getPosition(),
                        fieldDeclaration.getSimpleName());
                }
                throw new ValidationException(
                    MessageCode.SLIM3GEN1044,
                    env,
                    classDeclaration.getPosition(),
                    fieldDeclaration.getSimpleName(),
                    fieldDeclaration.getDeclaringType().getQualifiedName());
            }
            booleanAttributeNames.add(attributeName);
        }
    }

    /**
     * Creates a attribute meta description.
     * 
     * @param classDeclaration
     *            the model declaration
     * @param fieldDeclaration
     * @param methodDeclarations
     * @return a attribute meta description or {@code null} if error occured.
     */
    protected AttributeMetaDesc createAttributeMetaDesc(
            ClassDeclaration classDeclaration,
            FieldDeclaration fieldDeclaration,
            List<MethodDeclaration> methodDeclarations) {
        try {
            return attributeMetaDescFactory.createAttributeMetaDesc(
                classDeclaration,
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
     * @param classDeclaration
     *            the class declaration
     * @return field declarations
     */
    protected List<FieldDeclaration> getFieldDeclarations(
            ClassDeclaration classDeclaration) {
        List<FieldDeclaration> results = new LinkedList<FieldDeclaration>();
        for (ClassDeclaration c = classDeclaration; c != null
            && !c.getQualifiedName().equals(Object.class.getName()); c =
            c.getSuperclass().getDeclaration()) {
            for (FieldDeclaration field : c.getFields()) {
                Collection<Modifier> modifiers = field.getModifiers();
                if (!modifiers.contains(Modifier.STATIC)) {
                    results.add(field);
                }
            }
        }

        List<FieldDeclaration> hiderFieldDeclarations =
            new LinkedList<FieldDeclaration>();
        for (Iterator<FieldDeclaration> it = results.iterator(); it.hasNext();) {
            FieldDeclaration hidden = it.next();
            for (FieldDeclaration hider : hiderFieldDeclarations) {
                if (env.getDeclarationUtils().hides(hider, hidden)) {
                    it.remove();
                }
            }
        }
        return results;
    }

    /**
     * Returns method declarations.
     * 
     * @param classDeclaration
     *            the class declaration
     * @return method declarations
     */
    protected List<MethodDeclaration> getMethodDeclarations(
            ClassDeclaration classDeclaration) {
        List<MethodDeclaration> results = new LinkedList<MethodDeclaration>();
        for (ClassDeclaration c = classDeclaration; c != null
            && !c.getQualifiedName().equals(Object.class.getName()); c =
            c.getSuperclass().getDeclaration()) {
            gatherClassMethodDeclarations(c, results);
            for (InterfaceType superinterfaceType : c.getSuperinterfaces()) {
                InterfaceDeclaration superinterfaceDeclaration =
                    superinterfaceType.getDeclaration();
                if (superinterfaceDeclaration == null) {
                    throw new UnknownDeclarationException(
                        env,
                        classDeclaration,
                        superinterfaceType);
                }
                gatherInterfaceMethodDeclarations(
                    superinterfaceDeclaration,
                    results);
            }
        }

        List<MethodDeclaration> overriderMethodDeclarations =
            new LinkedList<MethodDeclaration>();
        for (Iterator<MethodDeclaration> it = results.iterator(); it.hasNext();) {
            MethodDeclaration overriden = it.next();
            for (MethodDeclaration overrider : overriderMethodDeclarations) {
                if (env.getDeclarationUtils().overrides(overrider, overriden)) {
                    it.remove();
                }
            }
        }
        return results;
    }

    /**
     * Gather class method declarations.
     * 
     * @param classDeclaration
     *            the class declaration
     * @param methodDeclarations
     *            the list of method declarations
     */
    protected void gatherClassMethodDeclarations(
            ClassDeclaration classDeclaration,
            List<MethodDeclaration> methodDeclarations) {
        for (MethodDeclaration method : classDeclaration.getMethods()) {
            Collection<Modifier> modifiers = method.getModifiers();
            if (modifiers.contains(Modifier.PUBLIC)
                && !modifiers.contains(Modifier.STATIC)) {
                methodDeclarations.add(method);
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
            InterfaceDeclaration interfaceDeclaration,
            List<MethodDeclaration> methodDeclarations) {
        for (MethodDeclaration method : interfaceDeclaration.getMethods()) {
            methodDeclarations.add(method);
        }
        for (InterfaceType superinterfaceType : interfaceDeclaration
            .getSuperinterfaces()) {
            InterfaceDeclaration superInterfaceDeclaration =
                superinterfaceType.getDeclaration();
            if (superInterfaceDeclaration == null) {
                throw new UnknownDeclarationException(
                    env,
                    interfaceDeclaration,
                    superinterfaceType);
            }
            gatherInterfaceMethodDeclarations(
                superInterfaceDeclaration,
                methodDeclarations);
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
