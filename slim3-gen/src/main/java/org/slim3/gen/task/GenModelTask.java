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
package org.slim3.gen.task;

import java.io.IOException;

import javax.xml.xpath.XPathExpressionException;

import org.slim3.gen.ClassConstants;
import org.slim3.gen.Constants;
import org.slim3.gen.desc.ModelDesc;
import org.slim3.gen.desc.ModelDescFactory;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.generator.ModelGenerator;
import org.slim3.gen.generator.ModelTestCaseGenerator;
import org.slim3.gen.message.MessageCode;
import org.slim3.gen.message.MessageFormatter;

/**
 * Represents a task to generate a model java file.
 * 
 * @author taedium
 * @since 3.0
 */
public class GenModelTask extends AbstractGenJavaFileTask {

    /** the packageName */
    protected String packageName;

    /** the modelRelativeClassName */
    protected String modelRelativeClassName;

    /** the superclass name of testcase */
    protected String testCaseSuperclassName = ClassConstants.JDOTestCase;

    /** the property which represents a model class name */
    protected String modelClassNameProperty;

    /**
     * Sets the packageName.
     * 
     * @param packageName
     *            the packageName to set
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * Sets the modelRelativeClassName.
     * 
     * @param modelRelativeClassName
     *            the modelRelativeClassName to set
     */
    public void setModelRelativeClassName(String modelRelativeClassName) {
        this.modelRelativeClassName = modelRelativeClassName;
    }

    /**
     * Sets the testCaseSuperclassName.
     * 
     * @param testCaseSuperclassName
     *            the testCaseSuperclassName to set
     */
    public void setTestCaseSuperclassName(String testCaseSuperclassName) {
        this.testCaseSuperclassName = testCaseSuperclassName;
    }

    /**
     * Sets the modelClassNameProperty.
     * 
     * @param modelClassNameProperty
     *            the modelClassNameProperty to set
     */
    public void setModelClassNameProperty(String modelClassNameProperty) {
        this.modelClassNameProperty = modelClassNameProperty;
    }

    @Override
    public void doExecute() throws Exception {
        super.doExecute();
        if (modelRelativeClassName == null) {
            throw new IllegalStateException(
                "The modelRelativeClassName parameter is null.");
        }
        if (modelClassNameProperty == null) {
            throw new IllegalStateException(
                "The modelClassNameProperty parameter is null.");
        }
        if (getProject().getProperty(modelClassNameProperty) != null) {
            throw new IllegalStateException(MessageFormatter.getMessage(
                MessageCode.SILM3GEN0009,
                modelClassNameProperty));
        }

        ClassNameBuilder nameBuilder = getClassNameBuilder();

        ModelDescFactory factory =
            createModelDescFactory(nameBuilder.getPackageName(), nameBuilder
                .getSimpleName());
        ModelDesc modelDesc = factory.createModelDesc();

        JavaFile javaFile = createJavaFile(modelDesc);
        Generator generator = createModelGenerator(modelDesc);
        generateJavaFile(generator, javaFile);

        JavaFile testCaseJavaFile = createTestCaseJavaFile(modelDesc);
        Generator testCaseGenerator = createModelTestCaseGenerator(modelDesc);
        generateJavaFile(testCaseGenerator, testCaseJavaFile);

        getProject().setNewProperty(
            modelClassNameProperty,
            modelDesc.getQualifiedName());
    }

    /**
     * Creates a {@link ClassNameBuilder}.
     * 
     * @return a {@link ClassNameBuilder}
     * @throws IOException
     * @throws XPathExpressionException
     */
    protected ClassNameBuilder getClassNameBuilder() throws IOException,
            XPathExpressionException {
        ClassNameBuilder nameBuilder = new ClassNameBuilder();
        nameBuilder.append(getModelBasePackageName());
        nameBuilder.append(modelRelativeClassName);
        return nameBuilder;
    }

    /**
     * Returns the model base package name.
     * 
     * @return the model base package name.
     * @throws IOException
     * @throws XPathExpressionException
     */
    protected String getModelBasePackageName() throws IOException,
            XPathExpressionException {
        if (packageName != null) {
            return packageName;
        }
        WebConfig config = createWebConfig();
        return config.getRootPackageName() + "." + Constants.MODEL_SUB_PACKAGE;
    }

    /**
     * Creates a {@link ModelDescFactory}.
     * 
     * @param packageName
     *            the package name
     * @param simpleName
     *            the simple name
     * @return a factory of model description.
     */
    protected ModelDescFactory createModelDescFactory(String packageName,
            String simpleName) {
        return new ModelDescFactory(
            packageName,
            simpleName,
            testCaseSuperclassName);
    }

    /**
     * Creates a {@link Generator}.
     * 
     * @param modelDesc
     *            the model description
     * @return a generator
     */
    protected Generator createModelGenerator(ModelDesc modelDesc) {
        return new ModelGenerator(modelDesc);
    }

    /**
     * Creates a {@link Generator} for a test case.
     * 
     * @param modelDesc
     *            the model description
     * @return a generator
     */
    protected Generator createModelTestCaseGenerator(ModelDesc modelDesc) {
        return new ModelTestCaseGenerator(modelDesc);
    }

}
