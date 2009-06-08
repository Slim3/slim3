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

import java.io.File;
import java.io.IOException;

import javax.xml.xpath.XPathExpressionException;

import org.slim3.gen.ClassConstants;
import org.slim3.gen.Constants;
import org.slim3.gen.desc.ModelDesc;
import org.slim3.gen.desc.ModelDescFactory;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.generator.ModelGenerator;
import org.slim3.gen.generator.ModelTestCaseGenerator;

/**
 * Represents a task to generate a model java file.
 * 
 * @author taedium
 * @since 3.0
 */
public class GenModelTask extends AbstractTask {

    /** the source directory */
    protected File srcDir;

    /** the test source directory */
    protected File testDir;

    /** the packageName */
    protected String packageName;

    /** the simpleName */
    protected String simpleName;

    /** the superclass name of testcase */
    protected String testCaseSuperclassName = ClassConstants.JDOTestCase;

    /**
     * Sets the srcDir.
     * 
     * @param srcDir
     *            the srcDir to set
     */
    public void setSrcDir(File srcDir) {
        this.srcDir = srcDir;
    }

    /**
     * Sets the testDir.
     * 
     * @param testDir
     *            the testDir to set
     */
    public void setTestDir(File testDir) {
        this.testDir = testDir;
    }

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
     * Sets the simpleName.
     * 
     * @param simpleName
     *            the simpleName to set
     */
    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
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

    @Override
    public void doExecute() throws IOException, XPathExpressionException {
        if (srcDir == null) {
            throw new IllegalStateException("The srcDir parameter is null.");
        }
        if (testDir == null) {
            throw new IllegalStateException("The testDir parameter is null.");
        }
        if (warDir == null) {
            throw new IllegalStateException("The warDir parameter is null.");
        }
        if (simpleName == null) {
            throw new IllegalStateException("The simpleName parameter is null.");
        }
        String modelPackageName = getModelPackageName();
        ModelDescFactory factory = createModelDescFactory(modelPackageName);
        ModelDesc modelDesc = factory.createModelDesc();
        JavaFileCreator javaFileCreator =
            new JavaFileCreator(
                srcDir,
                testDir,
                modelDesc.getPackageName(),
                modelDesc.getSimpleName());
        ClassNameCreator classNameCreator =
            new ClassNameCreator(modelDesc.getPackageName(), modelDesc
                .getSimpleName());
        generateModel(modelDesc, javaFileCreator, classNameCreator);
        generateModelTestCase(modelDesc, javaFileCreator, classNameCreator);
    }

    /**
     * Returns the model package name.
     * 
     * @return the model package name.
     * @throws IOException
     * @throws XPathExpressionException
     */
    protected String getModelPackageName() throws IOException,
            XPathExpressionException {
        if (packageName != null) {
            return packageName;
        }
        AppEngineConfig config = new AppEngineConfig(warDir);
        String controllerPackageName = config.getControllerPackageName();
        int pos = controllerPackageName.lastIndexOf(".");
        String rootPackageName =
            pos > 0 ? controllerPackageName.substring(0, pos) : null;
        if (rootPackageName != null) {
            return controllerPackageName + "." + Constants.MODEL_SUB_PACKAGE;
        }
        return Constants.MODEL_SUB_PACKAGE;
    }

    /**
     * Creates a {@link ModelDescFactory}.
     * 
     * @param packageName
     *            the package name
     * @return a factory of model description.
     */
    protected ModelDescFactory createModelDescFactory(String packageName) {
        return new ModelDescFactory(
            packageName,
            simpleName,
            testCaseSuperclassName);
    }

    /**
     * Generates a model.
     * 
     * @param modelDesc
     *            the model description
     * @param javaFileCreator
     *            the java file creator
     * @param classNameCreator
     *            the class name creator
     * @throws IOException
     */
    protected void generateModel(ModelDesc modelDesc,
            JavaFileCreator javaFileCreator, ClassNameCreator classNameCreator)
            throws IOException {
        Generator generator = createModelGenerator(modelDesc);
        File javaFile = javaFileCreator.createJavaFile();
        String className = classNameCreator.createClassName();
        generate(generator, javaFile, className);
    }

    /**
     * Creates a {@link Generator}.
     * 
     * @param modelDesc
     *            the controller description
     * @return a generator
     */
    protected Generator createModelGenerator(ModelDesc modelDesc) {
        return new ModelGenerator(modelDesc);
    }

    /**
     * Generates a model test case.
     * 
     * @param modelDesc
     *            the model description
     * @param javaFileCreator
     *            the java file creator
     * @param classNameCreator
     *            the class name creator
     * @throws IOException
     */
    protected void generateModelTestCase(ModelDesc modelDesc,
            JavaFileCreator javaFileCreator, ClassNameCreator classNameCreator)
            throws IOException {
        Generator generator = createModelTestCaseGenerator(modelDesc);
        File javaFile = javaFileCreator.createTestCaseJavaFile();
        String className = classNameCreator.createTestCaseClassName();
        generate(generator, javaFile, className);
    }

    /**
     * Creates a {@link Generator}.
     * 
     * @param modelDesc
     *            the model description
     * @return a generator
     */
    protected Generator createModelTestCaseGenerator(ModelDesc modelDesc) {
        return new ModelTestCaseGenerator(modelDesc);
    }

}
