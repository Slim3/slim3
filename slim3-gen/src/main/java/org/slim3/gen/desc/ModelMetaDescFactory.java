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

import org.slim3.gen.message.MessageCode;
import org.slim3.gen.processor.AptException;
import org.slim3.gen.processor.Options;
import org.slim3.gen.processor.ValidationException;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.ClassDeclaration;
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
        List<MethodDeclaration> methodDeclarations =
            getMethodDeclarations(modelDeclaration);
        for (FieldDeclaration fieldDeclaration : getFieldDeclarations(modelDeclaration)) {
            AttributeMetaDesc attributeMetaDesc =
                createAttributeMetaDesc(fieldDeclaration, methodDeclarations);
            if (attributeMetaDesc == null) {
                continue;
            }
            if (attributeMetaDesc.isPrimaryKey()
                && modelMetaDesc.getKeyAttributeMetaDesc() != null) {
                throw new ValidationException(
                    MessageCode.SILM3GEN1013,
                    env,
                    modelDeclaration);
            }
            if (attributeMetaDesc.isVersion()
                && modelMetaDesc.getVersionAttributeMetaDesc() != null) {
                throw new ValidationException(
                    MessageCode.SILM3GEN1014,
                    env,
                    modelDeclaration);
            }
            modelMetaDesc.addAttributeMetaDesc(attributeMetaDesc);
        }
        if (modelMetaDesc.getKeyAttributeMetaDesc() == null) {
            throw new ValidationException(
                MessageCode.SILM3GEN1015,
                env,
                modelDeclaration);
        }
    }

    protected AttributeMetaDesc createAttributeMetaDesc(
            FieldDeclaration fieldDeclaration,
            List<MethodDeclaration> methodDeclarations) {
        try {
            return attributeMetaDescFactory.createAttributeMetaDesc(
                fieldDeclaration,
                methodDeclarations);
        } catch (AptException e) {
            e.printError();
        }
        return null;
    }

    protected List<FieldDeclaration> getFieldDeclarations(
            ClassDeclaration classDeclaration) {
        List<FieldDeclaration> results = new LinkedList<FieldDeclaration>();
        for (ClassDeclaration c = classDeclaration; c != null
            && !c.getQualifiedName().equals(Object.class.getName()); c =
            c.getSuperclass().getDeclaration()) {
            for (FieldDeclaration field : classDeclaration.getFields()) {
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

    protected List<MethodDeclaration> getMethodDeclarations(
            ClassDeclaration classDeclaration) {
        List<MethodDeclaration> results = new LinkedList<MethodDeclaration>();
        for (ClassDeclaration c = classDeclaration; c != null
            && !c.getQualifiedName().equals(Object.class.getName()); c =
            c.getSuperclass().getDeclaration()) {
            gatherClassMethods(classDeclaration, results);
            for (InterfaceType interfaceType : c.getSuperinterfaces()) {
                InterfaceDeclaration interfaceDeclaration =
                    interfaceType.getDeclaration();
                if (interfaceDeclaration == null) {
                    continue;
                }
                gatherInterfaceMethods(interfaceDeclaration, results);
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

    protected void gatherClassMethods(ClassDeclaration classDeclaration,
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

}
