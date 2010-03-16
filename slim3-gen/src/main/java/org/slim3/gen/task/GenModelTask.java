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
package org.slim3.gen.task;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.xml.xpath.XPathExpressionException;

import org.slim3.gen.ClassConstants;
import org.slim3.gen.Constants;
import org.slim3.gen.desc.ModelDesc;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.generator.ModelGenerator;
import org.slim3.gen.generator.ModelTestCaseGenerator;
import org.slim3.gen.message.MessageCode;
import org.slim3.gen.message.MessageFormatter;

/**
 * Represents a task to generate a model java file.
 * 
 * @author taedium
 * @since 1.0.0
 */
public class GenModelTask extends AbstractGenJavaFileTask {

    /** the packageName */
    protected String packageName;

    /** the superclass name of testcase */
    protected String testCaseSuperclassName =
        ClassConstants.AppEngineTestCase;

    /** the modelDefinition */
    protected String modelDefinition;

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
     * Sets the superclass name of testcase.
     * 
     * @param testCaseSuperclassName
     *            the superclass name of testcase to set
     */
    public void setTestCaseSuperclassName(String testCaseSuperclassName) {
        this.testCaseSuperclassName = testCaseSuperclassName;
    }

    /**
     * Sets the modelDefinition.
     * 
     * @param modelDefinition
     *            the modelDefinition to set
     */
    public void setModelDefinition(String modelDefinition) {
        this.modelDefinition = modelDefinition;
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
        if (modelDefinition == null) {
            throw new IllegalStateException(
                "The modelDefinition parameter is null.");
        }
        if (modelClassNameProperty == null) {
            throw new IllegalStateException(
                "The modelClassNameProperty parameter is null.");
        }
        if (getProject().getProperty(modelClassNameProperty) != null) {
            throw new IllegalStateException(MessageFormatter.getMessage(
                MessageCode.SLIM3GEN0009,
                modelClassNameProperty));
        }

        ModelDesc modelDesc = createModelDesc();

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
     * Creates a model description.
     * 
     * @return a model description
     * @throws IOException
     * @throws XPathExpressionException
     */
    private ModelDesc createModelDesc() throws IOException,
            XPathExpressionException {
        ModelDef modelDef = parse(modelDefinition);

        ModelDesc modelDesc = new ModelDesc();
        ClassNameBuilder classNameBuilder = new ClassNameBuilder();
        classNameBuilder.append(getModelBasePackageName());
        classNameBuilder.append(modelDef.modelRelativeClassName);
        modelDesc.setPackageName(classNameBuilder.getPackageName());
        modelDesc.setSimpleName(classNameBuilder.getSimpleName());
        if (modelDef.modelRelativeSuperclassName == null) {
            modelDesc.setSuperclassName(ClassConstants.Object);
        } else {
            ClassNameBuilder superclassNameBuilder = new ClassNameBuilder();
            superclassNameBuilder.append(getModelBasePackageName());
            superclassNameBuilder.append(modelDef.modelRelativeSuperclassName);
            modelDesc.setSuperclassName(superclassNameBuilder
                .getQualifiedName());
        }
        modelDesc.setTestCaseSuperclassName(testCaseSuperclassName);
        return modelDesc;
    }

    /**
     * 
     * @param input
     *            the input
     * @return the parsed text.
     */
    protected ModelDef parse(String input) {
        StringTokenizer tokenizer = new StringTokenizer(input, " ");
        int count = tokenizer.countTokens();
        if (count == 1) {
            ModelDef modelDef = new ModelDef();
            modelDef.modelRelativeClassName = tokenizer.nextToken();
            return modelDef;
        }
        if (count == 3) {
            ModelDef parsedText = new ModelDef();
            parsedText.modelRelativeClassName = tokenizer.nextToken();
            String keyword = tokenizer.nextToken();
            if (!"extends".equals(keyword)) {
                throw new RuntimeException(MessageFormatter.getSimpleMessage(
                    MessageCode.SLIM3GEN0012,
                    keyword,
                    input));
            }
            parsedText.modelRelativeSuperclassName = tokenizer.nextToken();
            return parsedText;
        }
        throw new RuntimeException(MessageFormatter.getSimpleMessage(
            MessageCode.SLIM3GEN0013,
            input,
            count));
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
        StringBuilder buf = new StringBuilder();
        buf.append(config.getRootPackageName());
        if (config.isGWTServiceServletDefined()) {
            buf.append(".");
            buf.append(Constants.SHARED_PACKAGE);
        }
        buf.append(".");
        buf.append(Constants.MODEL_PACKAGE);
        return buf.toString();
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

    /**
     * The parsed text.
     * 
     * @author taedium
     * 
     */
    protected static class ModelDef {

        /** the modelRelativeClassName */
        protected String modelRelativeClassName;

        /** the modelRelativeSuperclassName */
        protected String modelRelativeSuperclassName;
    }
}
