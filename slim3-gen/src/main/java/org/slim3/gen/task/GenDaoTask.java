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
public class GenDaoTask extends AbstractTask {

    /** the source directory */
    protected File srcDir;

    /** the test source directory */
    protected File testDir;

    /** the packageName */
    protected String packageName;

    /** the superclassName */
    protected String superclassName = ClassConstants.GenericDao;

    /** the superclass name of testcase */
    protected String testCaseSuperclassName = ClassConstants.JDOTestCase;

    /** the modelClassName */
    protected String modelClassName;

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
        if (modelClassName == null) {
            throw new IllegalStateException(
                "The modelClassName parameter is null.");
        }
        String daoPackageName = getDaoPackageName();
        DaoDescFactory factory = createDaoDescFactory(daoPackageName);
        DaoDesc daoDesc = factory.createDaoDesc();
        JavaFileCreator javaFileCreator =
            new JavaFileCreator(
                srcDir,
                testDir,
                daoDesc.getPackageName(),
                daoDesc.getSimpleName());
        ClassNameCreator classNameCreator =
            new ClassNameCreator(daoDesc.getPackageName(), daoDesc
                .getSimpleName());
        generateDao(daoDesc, javaFileCreator, classNameCreator);
        generateDaoTestCase(daoDesc, javaFileCreator, classNameCreator);
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
        AppEngineConfig config = new AppEngineConfig(warDir);
        String controllerPackageName = config.getControllerPackageName();
        int pos = controllerPackageName.lastIndexOf(".");
        String rootPackageName =
            pos > 0 ? controllerPackageName.substring(0, pos) : null;
        if (rootPackageName != null) {
            return rootPackageName + "." + Constants.DAO_SUB_PACKAGE;
        }
        return Constants.DAO_SUB_PACKAGE;
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
     * Generates a dao.
     * 
     * @param daoDesc
     *            the dao description
     * @param javaFileCreator
     *            the java file creator
     * @param classNameCreator
     *            the class name creator
     * @throws IOException
     */
    protected void generateDao(DaoDesc daoDesc,
            JavaFileCreator javaFileCreator, ClassNameCreator classNameCreator)
            throws IOException {
        Generator generator = createDaoGenerator(daoDesc);
        File javaFile = javaFileCreator.createJavaFile();
        String className = classNameCreator.createClassName();
        generate(generator, javaFile, className);
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
     * Generates a dao test case.
     * 
     * @param daoDesc
     *            the dao description
     * @param javaFileCreator
     *            the java file creator
     * @param classNameCreator
     *            the class name creator
     * @throws IOException
     */
    protected void generateDaoTestCase(DaoDesc daoDesc,
            JavaFileCreator javaFileCreator, ClassNameCreator classNameCreator)
            throws IOException {
        Generator generator = createDaoTestCaseGenerator(daoDesc);
        File javaFile = javaFileCreator.createTestCaseJavaFile();
        String className = classNameCreator.createTestCaseClassName();
        generate(generator, javaFile, className);
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
