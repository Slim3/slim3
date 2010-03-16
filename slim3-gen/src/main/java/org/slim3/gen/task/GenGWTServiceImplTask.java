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
import org.slim3.gen.desc.GWTServiceImplDesc;
import org.slim3.gen.generator.GWTServiceImplGenerator;
import org.slim3.gen.generator.GWTServiceImplTestCaseGenerator;
import org.slim3.gen.generator.Generator;

/**
 * Represents a task to generate a GWT service implementation java file.
 * 
 * @author taedium
 * @since 1.0.0
 */
public class GenGWTServiceImplTask extends AbstractGenJavaFileTask {

    /** the packageName */
    protected String packageName;

    /** the superclass name */
    protected String superclassName = ClassConstants.Object;

    /** the superclass name of testcase */
    protected String testCaseSuperclassName = ClassConstants.ServletTestCase;

    /** the serviceClassName */
    protected String serviceClassName;

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
     * Sets the serviceClassName.
     * 
     * @param serviceClassName
     *            the serviceClassName to set
     */
    public void setServiceClassName(String serviceClassName) {
        this.serviceClassName = serviceClassName;
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
        if (serviceClassName == null) {
            throw new IllegalStateException(
                "The serviceClassName parameter is null.");
        }

        GWTServiceImplDesc serviceImplDesc = createServiceImplDesc();

        JavaFile javaFile = createJavaFile(serviceImplDesc);
        Generator generator = createServiceImplGenerator(serviceImplDesc);
        generateJavaFile(generator, javaFile);

        JavaFile testCaseJavaFile = createTestCaseJavaFile(serviceImplDesc);
        Generator testCaseGenerator =
            createServiceImplTestCaseGenerator(serviceImplDesc);
        generateJavaFile(testCaseGenerator, testCaseJavaFile);
    }

    /**
     * Creates a GWT service implementation description.
     * 
     * @return a GWT service implementation description
     * @throws IOException
     * @throws XPathExpressionException
     */
    private GWTServiceImplDesc createServiceImplDesc() throws IOException,
            XPathExpressionException {
        ClassNameBuilder nameBuilder = new ClassNameBuilder();
        nameBuilder.append(getServiceImplBasePackageName());
        nameBuilder.append(serviceDefinition);
        nameBuilder.appendSuffix(Constants.IMPL_SUFFIX);

        GWTServiceImplDesc serviceImplDesc = new GWTServiceImplDesc();
        serviceImplDesc.setPackageName(nameBuilder.getPackageName());
        serviceImplDesc.setSimpleName(nameBuilder.getSimpleName());
        serviceImplDesc.setSuperclassName(superclassName);
        serviceImplDesc.setTestCaseSuperclassName(testCaseSuperclassName);
        serviceImplDesc.setServiceClassName(serviceClassName);
        return serviceImplDesc;
    }

    /**
     * Returns the service implementation base package name.
     * 
     * @return the service implementation base package name.
     * @throws IOException
     * @throws XPathExpressionException
     */
    protected String getServiceImplBasePackageName() throws IOException,
            XPathExpressionException {
        if (packageName != null) {
            return packageName;
        }
        WebConfig config = createWebConfig();
        StringBuilder buf = new StringBuilder();
        buf.append(config.getRootPackageName());
        buf.append(".");
        buf.append(Constants.SERVER_PACKAGE);
        buf.append(".");
        buf.append(Constants.SERVICE_PACKAGE);
        return buf.toString();
    }

    /**
     * Creates a {@link Generator}.
     * 
     * @param serviceImplDesc
     *            the service implementation description
     * @return a generator
     */
    protected Generator createServiceImplGenerator(
            GWTServiceImplDesc serviceImplDesc) {
        return new GWTServiceImplGenerator(serviceImplDesc);
    }

    /**
     * Creates a {@link Generator} for a test case.
     * 
     * @param serviceImplDesc
     *            the service implementation description
     * @return a generator
     */
    protected Generator createServiceImplTestCaseGenerator(
            GWTServiceImplDesc serviceImplDesc) {
        return new GWTServiceImplTestCaseGenerator(serviceImplDesc);
    }
}
