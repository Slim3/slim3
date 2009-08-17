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
import org.slim3.gen.desc.ServiceImplDesc;
import org.slim3.gen.desc.ServiceImplDescFactory;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.generator.ServiceImplGenerator;
import org.slim3.gen.generator.ServiceImplTestCaseGenerator;

/**
 * Represents a task to generate a service implementation java file.
 * 
 * @author taedium
 * @since 3.0
 */
public class GenServiceImplTask extends AbstractGenJavaFileTask {

    /** the packageName */
    protected String packageName;

    /** the superclass name of testcase */
    protected String testCaseSuperclassName = ClassConstants.TestCase;

    /** the serviceClassName */
    protected String serviceClassName;

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
     * Sets the serviceClassName.
     * 
     * @param serviceClassName
     *            the serviceClassName to set
     */
    public void setServiceClassName(String serviceClassName) {
        this.serviceClassName = serviceClassName;
    }

    @Override
    public void doExecute() throws Exception {
        super.doExecute();
        if (serviceClassName == null) {
            throw new IllegalStateException(
                "The serviceClassName parameter is null.");
        }
        String serviceImplPackageName = getServiceImplPackageName();
        ServiceImplDescFactory factory =
            createServiceImplDescFactory(serviceImplPackageName);
        ServiceImplDesc serviceImplDesc = factory.createServiceImplDesc();

        JavaFile javaFile = createJavaFile(serviceImplDesc);
        Generator generator = createServiceImplGenerator(serviceImplDesc);
        generateJavaFile(generator, javaFile);

        JavaFile testCaseJavaFile = createTestCaseJavaFile(serviceImplDesc);
        Generator testCaseGenerator =
            createServiceImplTestCaseGenerator(serviceImplDesc);
        generateJavaFile(testCaseGenerator, testCaseJavaFile);
    }

    /**
     * Returns the service implementation package name.
     * 
     * @return the service implementation package name.
     * @throws IOException
     * @throws XPathExpressionException
     */
    protected String getServiceImplPackageName() throws IOException,
            XPathExpressionException {
        if (packageName != null) {
            return packageName;
        }
        WebConfig config = createWebConfig();
        return config.getRootPackageName();
    }

    /**
     * Creates a {@link ServiceImplDescFactory}.
     * 
     * @param packageName
     *            the package name
     * @return a service implementation description factory.
     */
    protected ServiceImplDescFactory createServiceImplDescFactory(
            String packageName) {
        return new ServiceImplDescFactory(
            packageName,
            testCaseSuperclassName,
            serviceClassName);
    }

    /**
     * Creates a {@link Generator}.
     * 
     * @param serviceImplDesc
     *            the service implementation description
     * @return a generator
     */
    protected Generator createServiceImplGenerator(
            ServiceImplDesc serviceImplDesc) {
        return new ServiceImplGenerator(serviceImplDesc);
    }

    /**
     * Creates a {@link Generator} for a test case.
     * 
     * @param serviceImplDesc
     *            the service implementation description
     * @return a generator
     */
    protected Generator createServiceImplTestCaseGenerator(
            ServiceImplDesc serviceImplDesc) {
        return new ServiceImplTestCaseGenerator(serviceImplDesc);
    }
}
