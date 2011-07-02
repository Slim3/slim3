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

import javax.xml.xpath.XPathExpressionException;

import org.slim3.gen.ClassConstants;
import org.slim3.gen.Constants;
import org.slim3.gen.desc.DaoDesc;
import org.slim3.gen.generator.DaoGenerator;
import org.slim3.gen.generator.DaoTestCaseGenerator;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.message.MessageCode;
import org.slim3.gen.message.MessageFormatter;
import org.slim3.gen.task.GenModelTask.ModelDef;

/**
 * Represents a task to generate a dao java file.
 *
 * @author sue445
 * @since 1.0.12
 */
public class GenDaoTask extends AbstractGenJavaFileTask {

    /** the packageName */
    protected String packageName;

    /** the superclass name of testcase */
    protected String testCaseSuperclassName =
        ClassConstants.AppEngineTestCase;

    /** the modelDefinition */
    protected String modelDefinition;

    /** the property which represents a dao class name */
    protected String daoClassNameProperty;

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
     * Sets the daoClassNameProperty.
     *
     * @param daoClassNameProperty
     *            the modelClassNameProperty to set
     */
    public void setDaoClassNameProperty(String daoClassNameProperty) {
        this.daoClassNameProperty = daoClassNameProperty;
    }

    @Override
    public void doExecute() throws Exception {
        super.doExecute();
        if (modelDefinition == null) {
            throw new IllegalStateException(
                "The modelDefinition parameter is null.");
        }
        if (daoClassNameProperty == null) {
            throw new IllegalStateException(
                "The daoClassNameProperty parameter is null.");
        }
        if (getProject().getProperty(daoClassNameProperty) != null) {
            throw new IllegalStateException(MessageFormatter.getMessage(
                MessageCode.SLIM3GEN0009,
                daoClassNameProperty));
        }

        DaoDesc daoDesc = createDaoDesc();

        JavaFile javaFile = createJavaFile(daoDesc);
        Generator generator = createDaoGenerator(daoDesc);
        generateJavaFile(generator, javaFile);

        JavaFile testCaseJavaFile = createTestCaseJavaFile(daoDesc);
        Generator testCaseGenerator = createDaoTestCaseGenerator(daoDesc);
        generateJavaFile(testCaseGenerator, testCaseJavaFile);

        getProject().setNewProperty(
            daoClassNameProperty,
            daoDesc.getQualifiedName());
    }

    /**
     * Creates a dao description.
     *
     * @return a dao description
     * @throws IOException
     * @throws XPathExpressionException
     */
    protected DaoDesc createDaoDesc() throws IOException,
            XPathExpressionException {
        ModelDef modelDef = parse(modelDefinition);
        String basePackageName = getBasePackageName();
        String modelPackageName = basePackageName + "." + Constants.MODEL_PACKAGE;
        String daoPackageName = basePackageName + "." + Constants.DAO_PACKAGE;
        String daoSimpleClassName = modelDef.modelRelativeClassName + Constants.DAO_SUFFIX;

        DaoDesc daoDesc = new DaoDesc();
        ClassNameBuilder classNameBuilder = new ClassNameBuilder();
        classNameBuilder.append(daoPackageName);
        classNameBuilder.append(daoSimpleClassName);
        daoDesc.setPackageName(classNameBuilder.getPackageName());
        daoDesc.setSimpleName(classNameBuilder.getSimpleName());
        daoDesc.setModelClassName(modelPackageName + "." + modelDef.modelRelativeClassName);
        daoDesc.setTestCaseSuperclassName(testCaseSuperclassName);

        return daoDesc;
    }

    /**
     *
     * @param input
     *            the input
     * @return the parsed text.
     */
    protected ModelDef parse(String input) {
        // transfer to GenModelTask
        GenModelTask task = new GenModelTask();
        return task.parse(input);
    }

    /**
     * Returns the model base package name.
     *
     * @return the model base package name.
     * @throws IOException
     * @throws XPathExpressionException
     */
    protected String getBasePackageName() throws IOException,
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
        return buf.toString();
    }

    /**
     * Creates a {@link Generator}.
     *
     * @param daoDesc
     *            the dao description
     * @return a generator
     */
    protected Generator createDaoGenerator(DaoDesc daoDesc) {
        return new DaoGenerator(daoDesc);
    }

    /**
     * Creates a {@link Generator} for a test case.
     *
     * @param daoDesc
     *            the dao description
     * @return a generator
     */
    protected Generator createDaoTestCaseGenerator(DaoDesc daoDesc) {
        return new DaoTestCaseGenerator(daoDesc);
    }
}
