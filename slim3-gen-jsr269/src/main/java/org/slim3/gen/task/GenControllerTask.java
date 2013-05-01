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
import org.slim3.gen.desc.ControllerDesc;
import org.slim3.gen.desc.ControllerDescFactory;
import org.slim3.gen.generator.ControllerGenerator;
import org.slim3.gen.generator.ControllerTestCaseGenerator;
import org.slim3.gen.generator.Generator;

/**
 * Represents a task to generate a controller java file.
 * 
 * @author taedium
 * @since 1.0.0
 * 
 */
public class GenControllerTask extends AbstractGenJavaFileTask {

    /** the controller path */
    protected String controllerPath;

    /** the superclass name */
    protected String superclassName = ClassConstants.Controller;

    /** the superclass name of testcase */
    protected String testCaseSuperclassName = ClassConstants.ControllerTestCase;

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
     * Sets the superclass name of testcase.
     * 
     * @param testCaseSuperclassName
     *            the superclass name of testcase to set
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
        ControllerDesc controllerDesc = createControllerDesc();

        JavaFile javaFile = createJavaFile(controllerDesc);
        Generator generator = createControllerGenerator(controllerDesc);
        generateJavaFile(generator, javaFile);

        JavaFile testCaseJavaFile = createTestCaseJavaFile(controllerDesc);
        Generator testCaseGenerator =
            createControllerTestCaseGenerator(controllerDesc);
        generateJavaFile(testCaseGenerator, testCaseJavaFile);
    }

    /**
     * Creates a controller description.
     * 
     * @return a controller description
     * @throws IOException
     * @throws XPathExpressionException
     */
    private ControllerDesc createControllerDesc() throws IOException,
            XPathExpressionException {
        String path =
            controllerPath.startsWith("/") ? controllerPath : "/"
                + controllerPath;
        String controllerBasePackageName = getControllerBasePackageName();
        ControllerDescFactory factory =
            createControllerDescFactory(controllerBasePackageName);
        ControllerDesc controllerDesc = factory.createControllerDesc(path);
        return controllerDesc;
    }

    /**
     * Creates a controller base package name.
     * 
     * @return a controller base package name
     * @throws IOException
     * @throws XPathExpressionException
     */
    private String getControllerBasePackageName() throws IOException,
            XPathExpressionException {
        StringBuilder buf = new StringBuilder();
        WebConfig config = createWebConfig();
        buf.append(config.getRootPackageName());
        if (config.isGWTServiceServletDefined()) {
            buf.append(".");
            buf.append(Constants.SERVER_PACKAGE);
        }
        buf.append(".");
        buf.append(Constants.CONTROLLER_PACKAGE);
        return buf.toString();
    }

    /**
     * Creates a {@link ControllerDescFactory}.
     * 
     * @param controllerBasePackageName
     *            the base package name of controllers.
     * @return a factory
     */
    protected ControllerDescFactory createControllerDescFactory(
            String controllerBasePackageName) {
        return new ControllerDescFactory(
            controllerBasePackageName,
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
