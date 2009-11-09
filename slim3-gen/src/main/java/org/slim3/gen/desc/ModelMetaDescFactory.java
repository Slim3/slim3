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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slim3.gen.AnnotationConstants;
import org.slim3.gen.message.MessageCode;
import org.slim3.gen.processor.AptException;
import org.slim3.gen.processor.Options;
import org.slim3.gen.processor.UnknownDeclarationException;
import org.slim3.gen.processor.ValidationException;
import org.slim3.gen.util.AnnotationMirrorUtil;
import org.slim3.gen.util.DeclarationUtil;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.ConstructorDeclaration;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.Modifier;
import com.sun.mirror.type.InterfaceType;

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

        AnnotationMirror anno =
            DeclarationUtil.getAnnotationMirror(
                env,
                classDeclaration,
                AnnotationConstants.Model);
        if (anno == null) {
            throw new IllegalStateException(AnnotationConstants.Model
                + " not found.");
        }

        String modelClassName = classDeclaration.getQualifiedName().toString();
        ModelMetaClassName modelMetaClassName =
            createModelMetaClassName(modelClassName);

        String kind = null;
        String simpleClassNamePath = null;
        PolyModelDesc polyModelDesc = createPolyModelDesc(classDeclaration);
        if (polyModelDesc == null) {
            kind = getKind(anno, modelMetaClassName.getKind());
            simpleClassNamePath = null;
        } else {
            kind = polyModelDesc.getKind();
            simpleClassNamePath = polyModelDesc.getSimpleClassNamePath();
            validateKind(classDeclaration);
        }

        ModelMetaDesc modelMetaDesc =
            new ModelMetaDesc(
                modelMetaClassName.getPackageName(),
                modelMetaClassName.getSimpleName(),
                modelClassName,
                kind,
                simpleClassNamePath);
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
        LinkedList<String> simpleNames = new LinkedList<String>();
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
                simpleNames.addFirst(c.getSimpleName());
            }
        }
        if (simpleNames.size() <= 1) {
            return null;
        }
        simpleNames.removeFirst();
        StringBuilder buf = new StringBuilder();
        for (String simpleName : simpleNames) {
            buf.append(simpleName);
            buf.append("/");
        }
        return new PolyModelDesc(kind, buf.toString());
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
                MessageCode.SILM3GEN1019,
                env,
                classDeclaration);
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
                MessageCode.SILM3GEN1017,
                env,
                classDeclaration);
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
                MessageCode.SILM3GEN1020,
                env,
                classDeclaration);
        }
    }

    /**
     * Validates that the default constructor is existent.
     * 
     * @param classDeclaration
     *            the class declaration
     */
    protected void validateDefaultConstructor(ClassDeclaration classDeclaration) {
        for (ConstructorDeclaration constructor : classDeclaration
            .getConstructors()) {
            if (constructor.getModifiers().contains(Modifier.PUBLIC)) {
                if (constructor.getParameters().isEmpty()) {
                    return;
                }
            }
        }
        throw new ValidationException(
            MessageCode.SILM3GEN1018,
            env,
            classDeclaration);
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
            throw new ValidationException(
                MessageCode.SILM3GEN1022,
                env,
                classDeclaration);
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
        for (FieldDeclaration fieldDeclaration : getFieldDeclarations(classDeclaration)) {
            AttributeMetaDesc attributeMetaDesc =
                createAttributeMetaDesc(fieldDeclaration, methodDeclarations);
            if (attributeMetaDesc == null) {
                modelMetaDesc.setError(true);
                continue;
            }
            if (!attributeMetaDesc.isPersistent()) {
                continue;
            }
            if (attributeMetaDesc.isPrimaryKey()
                && modelMetaDesc.getKeyAttributeMetaDesc() != null) {
                throw new ValidationException(
                    MessageCode.SILM3GEN1013,
                    env,
                    classDeclaration);
            }
            if (attributeMetaDesc.isVersion()
                && modelMetaDesc.getVersionAttributeMetaDesc() != null) {
                throw new ValidationException(
                    MessageCode.SILM3GEN1014,
                    env,
                    classDeclaration);
            }
            modelMetaDesc.addAttributeMetaDesc(attributeMetaDesc);
        }
        if (!modelMetaDesc.isError()
            && modelMetaDesc.getKeyAttributeMetaDesc() == null) {
            throw new ValidationException(
                MessageCode.SILM3GEN1015,
                env,
                classDeclaration);
        }
    }

    /**
     * Creates a attribute meta description.
     * 
     * @param fieldDeclaration
     * @param methodDeclarations
     * @return a attribute meta description or {@code null} if error occured.
     */
    protected AttributeMetaDesc createAttributeMetaDesc(
            FieldDeclaration fieldDeclaration,
            List<MethodDeclaration> methodDeclarations) {
        try {
            return attributeMetaDescFactory.createAttributeMetaDesc(
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

        /** the path of simple class name */
        protected final String simpleClassNamePath;

        /**
         * @param kind
         *            the kind
         * @param simpleClassNamePath
         *            the path of simple class name
         */
        public PolyModelDesc(String kind, String simpleClassNamePath) {
            if (kind == null) {
                throw new NullPointerException("The kind parameter is null.");
            }
            if (simpleClassNamePath == null) {
                throw new NullPointerException(
                    "The simpleClassNamePath parameter is null.");
            }
            this.kind = kind;
            this.simpleClassNamePath = simpleClassNamePath;
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
         * Returns the path of simple class name.
         * 
         * @return the path of simple class name
         */
        public String getSimpleClassNamePath() {
            return simpleClassNamePath;
        }

    }
}
