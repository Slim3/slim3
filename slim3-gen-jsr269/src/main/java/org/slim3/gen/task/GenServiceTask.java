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
import org.slim3.gen.desc.ServiceDesc;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.generator.ServiceGenerator;
import org.slim3.gen.generator.ServiceTestCaseGenerator;

/**
 * Represents a task to generate a service java file.
 * 
 * @author taedium
 * @since 1.0.0
 */
public class GenServiceTask extends AbstractGenJavaFileTask {

    /** the packageName */
    protected String packageName;

    /** the superclass name */
    protected String superclassName = ClassConstants.Object;

    /** the superclass name of testcase */
    protected String testCaseSuperclassName =
        ClassConstants.AppEngineTestCase;

    /** the serviceDefinition */
    protected String serviceDefinition;

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
     * Sets the superclass name.
     * 
     * @param superclassName
     *            the superclass name to set
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
     * Sets the serviceDefinition.
     * 
     * @param serviceDefinition
     *            the serviceDefinition to set
     */
    public void setServiceDefinition(String serviceDefinition) {
        this.serviceDefinition = serviceDefinition;
    }

    @Override
    public void doExecute() throws Exception {
        super.doExecute();
        if (serviceDefinition == null) {
            throw new IllegalStateException(
                "The serviceDefinition parameter is null.");
        }

        ServiceDesc serviceDesc = createServiceDesc();

        JavaFile javaFile = createJavaFile(serviceDesc);
        Generator generator = createServiceGenerator(serviceDesc);
        generateJavaFile(generator, javaFile);

        JavaFile testCaseJavaFile = createTestCaseJavaFile(serviceDesc);
        Generator testCaseGenerator =
            createServiceTestCaseGenerator(serviceDesc);
        generateJavaFile(testCaseGenerator, testCaseJavaFile);
    }

    /**
     * Creates a service description.
     * 
     * @return a GWT service implementation description
     * @throws IOException
     * @throws XPathExpressionException
     */
    private ServiceDesc createServiceDesc() throws IOException,
            XPathExpressionException {
        ClassNameBuilder nameBuilder = new ClassNameBuilder();
        nameBuilder.append(getServiceBasePackageName());
        nameBuilder.append(serviceDefinition);

        ServiceDesc serviceDesc = new ServiceDesc();
        serviceDesc.setPackageName(nameBuilder.getPackageName());
        serviceDesc.setSimpleName(nameBuilder.getSimpleName());
        serviceDesc.setSuperclassName(superclassName);
        serviceDesc.setTestCaseSuperclassName(testCaseSuperclassName);
        return serviceDesc;
    }

    /**
     * Returns the service base package name.
     * 
     * @return the service implementation base package name.
     * @throws IOException
     * @throws XPathExpressionException
     */
    protected String getServiceBasePackageName() throws IOException,
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
        buf.append(Constants.SERVICE_PACKAGE);
        return buf.toString();
    }

    /**
     * Creates a {@link Generator}.
     * 
     * @param serviceDesc
     *            the service description
     * @return a generator
     */
    protected Generator createServiceGenerator(ServiceDesc serviceDesc) {
        return new ServiceGenerator(serviceDesc);
    }

    /**
     * Creates a {@link Generator} for a test case.
     * 
     * @param serviceDesc
     *            the service description
     * @return a generator
     */
    protected Generator createServiceTestCaseGenerator(ServiceDesc serviceDesc) {
        return new ServiceTestCaseGenerator(serviceDesc);
    }
}
