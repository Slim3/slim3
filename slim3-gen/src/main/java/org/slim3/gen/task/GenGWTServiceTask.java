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

import org.slim3.gen.Constants;
import org.slim3.gen.desc.GWTServiceAsyncDesc;
import org.slim3.gen.desc.GWTServiceAsyncDescFactory;
import org.slim3.gen.desc.GWTServiceDesc;
import org.slim3.gen.desc.GWTServiceDescFactory;
import org.slim3.gen.generator.GWTServiceAsyncGenerator;
import org.slim3.gen.generator.GWTServiceGenerator;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.message.MessageCode;
import org.slim3.gen.message.MessageFormatter;

/**
 * Represents a task to generate a GWT service java file.
 * 
 * @author taedium
 * @since 3.0
 */
public class GenGWTServiceTask extends AbstractGenJavaFileTask {

    /** the packageName */
    protected String packageName;

    /** the serviceRelativeClassName */
    protected String serviceRelativeClassName;

    /** the remoteServiceRelativePath */
    protected String remoteServiceRelativePath = "gwtservice";

    /** the property which represents a service class name */
    protected String serviceClassNameProperty;

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
     * Sets the serviceRelativeClassName.
     * 
     * @param serviceRelativeClassName
     *            the serviceRelativeClassName to set
     */
    public void setServiceRelativeClassName(String serviceRelativeClassName) {
        this.serviceRelativeClassName = serviceRelativeClassName;
    }

    /**
     * Sets the remoteServiceRelativePath.
     * 
     * @param remoteServiceRelativePath
     *            the remoteServiceRelativePath to set
     */
    public void setRemoteServiceRelativePath(String remoteServiceRelativePath) {
        this.remoteServiceRelativePath = remoteServiceRelativePath;
    }

    /**
     * Sets the serviceClassNameProperty.
     * 
     * @param serviceClassNameProperty
     *            the serviceClassNameProperty to set
     */
    public void setServiceClassNameProperty(String serviceClassNameProperty) {
        this.serviceClassNameProperty = serviceClassNameProperty;
    }

    @Override
    public void doExecute() throws Exception {
        super.doExecute();
        if (serviceRelativeClassName == null) {
            throw new IllegalStateException(
                "The serviceRelativeClassName parameter is null.");
        }
        if (serviceClassNameProperty == null) {
            throw new IllegalStateException(
                "The serviceClassNameProperty parameter is null.");
        }
        if (getProject().getProperty(serviceClassNameProperty) != null) {
            throw new IllegalStateException(MessageFormatter.getMessage(
                MessageCode.SILM3GEN0009,
                serviceClassNameProperty));
        }

        ClassNameBuilder nameBuilder = getClassNameBuilder();

        GWTServiceDescFactory serviceDescFactory =
            createServiceDescFactory(nameBuilder.getPackageName(), nameBuilder
                .getSimpleName());
        GWTServiceDesc serviceDesc = serviceDescFactory.createServiceDesc();
        JavaFile serviceJavaFile = createJavaFile(serviceDesc);
        Generator serviceGenerator = createServiceGenerator(serviceDesc);
        generateJavaFile(serviceGenerator, serviceJavaFile);

        GWTServiceAsyncDescFactory serviceAsyncDescFactory =
            createServiceAsyncDescFactory(serviceDesc.getQualifiedName());
        GWTServiceAsyncDesc serviceAsyncDesc =
            serviceAsyncDescFactory.createServiceAsyncDesc();
        JavaFile serviceAsyncJavaFile = createJavaFile(serviceAsyncDesc);
        Generator serviceAsyncGenerator =
            createServiceAsyncGenerator(serviceAsyncDesc);
        generateJavaFile(serviceAsyncGenerator, serviceAsyncJavaFile);

        getProject().setNewProperty(
            serviceClassNameProperty,
            serviceDesc.getQualifiedName());
    }

    /**
     * Creates a {@link ClassNameBuilder}.
     * 
     * @return a {@link ClassNameBuilder}
     * @throws IOException
     * @throws XPathExpressionException
     */
    protected ClassNameBuilder getClassNameBuilder() throws IOException,
            XPathExpressionException {
        ClassNameBuilder nameBuilder = new ClassNameBuilder();
        nameBuilder.append(getServiceBasePackageName());
        nameBuilder.append(serviceRelativeClassName);
        return nameBuilder;
    }

    /**
     * Returns the service package name.
     * 
     * @return the service package name.
     * @throws IOException
     * @throws XPathExpressionException
     */
    protected String getServiceBasePackageName() throws IOException,
            XPathExpressionException {
        if (packageName != null) {
            return packageName;
        }
        WebConfig config = createWebConfig();
        String rootPackageName = config.getRootPackageName();
        int pos = rootPackageName.lastIndexOf(".");
        if (pos > -1) {
            String basePackageName = rootPackageName.substring(0, pos);
            return basePackageName + "." + Constants.SERVICE_SUB_PACKAGE;
        }
        return "";
    }

    /**
     * Creates a {@link GWTServiceDescFactory}.
     * 
     * @param packageName
     *            the package name
     * @param simpleName
     *            the simple name
     * @return a service description factory.
     */
    protected GWTServiceDescFactory createServiceDescFactory(
            String packageName, String simpleName) {
        return new GWTServiceDescFactory(
            packageName,
            simpleName,
            remoteServiceRelativePath);
    }

    /**
     * Creates a {@link GWTServiceAsyncDescFactory}.
     * 
     * @param serviceClassName
     *            the service class name
     * @return a service async description factory.
     */
    protected GWTServiceAsyncDescFactory createServiceAsyncDescFactory(
            String serviceClassName) {
        return new GWTServiceAsyncDescFactory(serviceClassName);
    }

    /**
     * Creates a {@link Generator} for a service.
     * 
     * @param serviceDesc
     *            the service description
     * @return a generator
     */
    protected Generator createServiceGenerator(GWTServiceDesc serviceDesc) {
        return new GWTServiceGenerator(serviceDesc);
    }

    /**
     * Creates a {@link Generator} a service async.
     * 
     * @param serviceAsyncDesc
     *            the service async description
     * @return a generator
     */
    protected Generator createServiceAsyncGenerator(
            GWTServiceAsyncDesc serviceAsyncDesc) {
        return new GWTServiceAsyncGenerator(serviceAsyncDesc);
    }

}
