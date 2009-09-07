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
import org.slim3.gen.desc.DaoDesc;
import org.slim3.gen.desc.ModelMetaClassName;
import org.slim3.gen.generator.DaoGenerator;
import org.slim3.gen.generator.DaoTestCaseGenerator;
import org.slim3.gen.generator.Generator;

/**
 * Represents a task to generate a dao java file.
 * 
 * @author taedium
 * @since 3.0
 */
public class GenDaoTask extends AbstractGenJavaFileTask {

    /** the packageName */
    protected String packageName;

    /** the superclassName */
    protected String superclassName = ClassConstants.GenericDao;

    /** the superclass name of testcase */
    protected String testCaseSuperclassName = ClassConstants.JDOTestCase;

    /** the modelClassName */
    protected String modelClassName;

    /** the modelRelativeClassName */
    protected String modelRelativeClassName;

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
     * Sets the superclassName.
     * 
     * @param superclassName
     *            the superclassName to set
     */
    public void setSuperclassName(String superclassName) {
        this.superclassName = superclassName;
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
     * Sets the modelClassName.
     * 
     * @param modelClassName
     *            the modelClassName to set
     */
    public void setModelClassName(String modelClassName) {
        this.modelClassName = modelClassName;
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

    @Override
    public void doExecute() throws Exception {
        super.doExecute();
        if (modelClassName == null) {
            throw new IllegalStateException(
                "The modelClassName parameter is null.");
        }
        if (modelRelativeClassName == null) {
            throw new IllegalStateException(
                "The modelRelativeClassName parameter is null.");
        }

        DaoDesc daoDesc = createDaoDesc();

        JavaFile javaFile = createJavaFile(daoDesc);
        Generator generator = createDaoGenerator(daoDesc);
        generateJavaFile(generator, javaFile);

        JavaFile testCaseJavaFile = createTestCaseJavaFile(daoDesc);
        Generator testCaseGenerator = createDaoTestCaseGenerator(daoDesc);
        generateJavaFile(testCaseGenerator, testCaseJavaFile);
    }

    /**
     * Creates a dao description.
     * 
     * @return a dao description
     * @throws IOException
     * @throws XPathExpressionException
     */
    private DaoDesc createDaoDesc() throws IOException,
            XPathExpressionException {
        ClassNameBuilder nameBuilder = new ClassNameBuilder();
        nameBuilder.append(getDaoBasePackageName());
        nameBuilder.append(modelRelativeClassName);
        nameBuilder.appendSuffix(Constants.DAO_SUFFIX);

        DaoDesc daoDesc = new DaoDesc();
        daoDesc.setPackageName(nameBuilder.getPackageName());
        daoDesc.setSimpleName(nameBuilder.getSimpleName());
        daoDesc.setSuperclassName(superclassName);
        daoDesc.setTestCaseSuperclassName(testCaseSuperclassName);
        daoDesc.setModelClassName(modelClassName);
        ModelMetaClassName modelMetaClassName =
            createModelMetaClassName(modelClassName);
        daoDesc.setModelMetaClassName(modelMetaClassName.getQualifiedName());
        return daoDesc;
    }

    /**
     * Returns the dao package name.
     * 
     * @return the dao base package name.
     * @throws IOException
     * @throws XPathExpressionException
     */
    protected String getDaoBasePackageName() throws IOException,
            XPathExpressionException {
        if (packageName != null) {
            return packageName;
        }
        WebConfig config = createWebConfig();
        StringBuilder buf = new StringBuilder();
        buf.append(config.getRootPackageName());
        if (config.isGWTServiceServletDefined()) {
            buf.append(".");
            buf.append(Constants.SERVER_PACKAGE);
        }
        buf.append(".");
        buf.append(Constants.DAO_PACKAGE);
        return buf.toString();
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
            Constants.MODEL_PACKAGE,
            Constants.META_PACKAGE,
            Constants.SHARED_PACKAGE,
            Constants.SERVER_PACKAGE);
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
