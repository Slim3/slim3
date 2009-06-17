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
import org.slim3.gen.desc.ControllerDesc;
import org.slim3.gen.desc.ControllerDescFactory;
import org.slim3.gen.generator.ControllerGenerator;
import org.slim3.gen.generator.ControllerTestCaseGenerator;
import org.slim3.gen.generator.Generator;

/**
 * Represents a task to generate a controller java file.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class GenControllerTask extends AbstractTask {

    /** the source directory */
    protected File srcDir;

    /** the test source directory */
    protected File testDir;

    /** the controller path */
    protected String controllerPath;

    /** the superclass name */
    protected String superclassName = ClassConstants.JDOController;

    /** the superclass name of testcase */
    protected String testCaseSuperclassName =
        ClassConstants.JDOControllerTestCase;

    /** {@code true} if the controller uses a view */
    protected boolean useView;

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
     * Sets the controllerPath.
     * 
     * @param controllerPath
     *            the controllerPath to set
     */
    public void setControllerPath(String controllerPath) {
        this.controllerPath = controllerPath;
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
     * Sets the useView.
     * 
     * @param useView
     *            the useView to set
     */
    public void setUseView(boolean useView) {
        this.useView = useView;
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
        if (controllerPath == null) {
            throw new IllegalStateException(
                "The controllerPath parameter is null.");
        }
        String path =
            controllerPath.startsWith("/") ? controllerPath : "/"
                + controllerPath;
        AppEngineConfig config = new AppEngineConfig(warDir);
        String controllerPackageName = config.getControllerPackageName();
        ControllerDescFactory factory =
            createControllerDescFactory(controllerPackageName);
        ControllerDesc controllerDesc = factory.createControllerDesc(path);
        JavaFileCreator javaFileCreator =
            new JavaFileCreator(srcDir, testDir, controllerDesc
                .getPackageName(), controllerDesc.getSimpleName());
        ClassNameCreator classNameCreator =
            new ClassNameCreator(
                controllerDesc.getPackageName(),
                controllerDesc.getSimpleName());
        generateController(controllerDesc, javaFileCreator, classNameCreator);
        generateControllerTestCase(
            controllerDesc,
            javaFileCreator,
            classNameCreator);
    }

    /**
     * Creates a {@link ControllerDescFactory}.
     * 
     * @param controllerPackageName
     *            the base package name of controllers.
     * @return a factory
     */
    protected ControllerDescFactory createControllerDescFactory(
            String controllerPackageName) {
        return new ControllerDescFactory(
            controllerPackageName,
            superclassName,
            testCaseSuperclassName,
            useView);
    }

    /**
     * Generates a controller.
     * 
     * @param controllerDesc
     *            the controller description
     * @param javaFileCreator
     *            the java file creator
     * @param classNameCreator
     *            the class name creator
     * @throws IOException
     */
    protected void generateController(ControllerDesc controllerDesc,
            JavaFileCreator javaFileCreator, ClassNameCreator classNameCreator)
            throws IOException {
        Generator generator = createControllerGenerator(controllerDesc);
        File javaFile = javaFileCreator.createJavaFile();
        String className = classNameCreator.createClassName();
        generate(generator, javaFile, className);
    }

    /**
     * Creates a {@link Generator}.
     * 
     * @param controllerDesc
     *            the controller description
     * @return a generator
     */
    protected Generator createControllerGenerator(ControllerDesc controllerDesc) {
        return new ControllerGenerator(controllerDesc);
    }

    /**
     * Generates a controller test case.
     * 
     * @param controllerDesc
     *            the controller description
     * @param javaFileCreator
     *            the java file creator
     * @param classNameCreator
     *            the class name creator
     * @throws IOException
     */
    protected void generateControllerTestCase(ControllerDesc controllerDesc,
            JavaFileCreator javaFileCreator, ClassNameCreator classNameCreator)
            throws IOException {
        Generator generator = createControllerTestCaseGenerator(controllerDesc);
        File javaFile = javaFileCreator.createTestCaseJavaFile();
        String className = classNameCreator.createTestCaseClassName();
        generate(generator, javaFile, className);
    }

    /**
     * Creates a {@link Generator}.
     * 
     * @param controllerDesc
     *            the controller description
     * @return a generator
     */
    protected Generator createControllerTestCaseGenerator(
            ControllerDesc controllerDesc) {
        return new ControllerTestCaseGenerator(controllerDesc);
    }

}
