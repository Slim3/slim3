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

import org.slim3.gen.ClassConstants;
import org.slim3.gen.Constants;
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
public class GenControllerTask extends AbstractGenJavaFileTask {

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
    public void doExecute() throws Exception {
        super.doExecute();
        if (controllerPath == null) {
            throw new IllegalStateException(
                "The controllerPath parameter is null.");
        }
        String path =
            controllerPath.startsWith("/") ? controllerPath : "/"
                + controllerPath;
        WebConfig config = createWebConfig();
        String controllerPackageName =
            config.getRootPackageName()
                + "."
                + Constants.CONTROLLER_SUB_PACKAGE;
        ControllerDescFactory factory =
            createControllerDescFactory(controllerPackageName);
        ControllerDesc controllerDesc = factory.createControllerDesc(path);

        JavaFile javaFile = createJavaFile(controllerDesc);
        Generator generator = createControllerGenerator(controllerDesc);
        generateJavaFile(generator, javaFile);

        JavaFile testCaseJavaFile = createTestCaseJavaFile(controllerDesc);
        Generator testCaseGenerator =
            createControllerTestCaseGenerator(controllerDesc);
        generateJavaFile(testCaseGenerator, testCaseJavaFile);
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
     * Creates a {@link Generator} for a test case.
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
