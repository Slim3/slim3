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
import org.slim3.gen.desc.DaoDescFactory;
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

    @Override
    public void doExecute() throws Exception {
        super.doExecute();
        if (modelClassName == null) {
            throw new IllegalStateException(
                "The modelClassName parameter is null.");
        }
        String daoPackageName = getDaoPackageName();
        DaoDescFactory factory = createDaoDescFactory(daoPackageName);
        DaoDesc daoDesc = factory.createDaoDesc();

        JavaFile javaFile = createJavaFile(daoDesc);
        Generator generator = createDaoGenerator(daoDesc);
        generateJavaFile(generator, javaFile);

        JavaFile testCaseJavaFile = createTestCaseJavaFile(daoDesc);
        Generator testCaseGenerator = createDaoTestCaseGenerator(daoDesc);
        generateJavaFile(testCaseGenerator, testCaseJavaFile);
    }

    /**
     * Returns the dao package name.
     * 
     * @return the dao package name.
     * @throws IOException
     * @throws XPathExpressionException
     */
    protected String getDaoPackageName() throws IOException,
            XPathExpressionException {
        if (packageName != null) {
            return packageName;
        }
        AppEngineConfig config = createAppEngineConfig();
        return config.getRootPackageName() + "." + Constants.DAO_SUB_PACKAGE;
    }

    /**
     * Creates a {@link DaoDescFactory}.
     * 
     * @param packageName
     *            the package name
     * @return a factory of dao description.
     */
    protected DaoDescFactory createDaoDescFactory(String packageName) {
        return new DaoDescFactory(
            packageName,
            superclassName,
            testCaseSuperclassName,
            modelClassName);
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
     * Creates a {@link Generator}.
     * 
     * @param daoDesc
     *            the dao description
     * @return a generator
     */
    protected Generator createDaoTestCaseGenerator(DaoDesc daoDesc) {
        return new DaoTestCaseGenerator(daoDesc);
    }

}
