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

import org.slim3.gen.Constants;
import org.slim3.gen.desc.GWTServiceAsyncDesc;
import org.slim3.gen.desc.GWTServiceDesc;
import org.slim3.gen.generator.GWTServiceAsyncGenerator;
import org.slim3.gen.generator.GWTServiceGenerator;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.message.MessageCode;
import org.slim3.gen.message.MessageFormatter;

/**
 * Represents a task to generate a GWT service java file.
 * 
 * @author taedium
 * @since 1.0.0
 */
public class GenGWTServiceTask extends AbstractGenJavaFileTask {

    /** the packageName */
    protected String packageName;

    /** the serviceDefinition */
    protected String serviceDefinition;

    /** the remoteServiceRelativePath */
    protected String remoteServiceRelativePath = "service.s3gwt";

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
     * Sets the serviceDefinition.
     * 
     * @param serviceDefinition
     *            the serviceDefinition to set
     */
    public void setServiceDefinition(String serviceDefinition) {
        this.serviceDefinition = serviceDefinition;
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
        if (serviceDefinition == null) {
            throw new IllegalStateException(
                "The serviceDefinition parameter is null.");
        }
        if (serviceClassNameProperty == null) {
            throw new IllegalStateException(
                "The serviceClassNameProperty parameter is null.");
        }
        if (getProject().getProperty(serviceClassNameProperty) != null) {
            throw new IllegalStateException(MessageFormatter.getMessage(
                MessageCode.SLIM3GEN0009,
                serviceClassNameProperty));
        }

        GWTServiceDesc serviceDesc = createServiceDesc();
        JavaFile serviceJavaFile = createJavaFile(serviceDesc);
        Generator serviceGenerator = createServiceGenerator(serviceDesc);
        generateJavaFile(serviceGenerator, serviceJavaFile);

        GWTServiceAsyncDesc serviceAsyncDesc =
            createServiceAsyncDesc(serviceDesc);
        JavaFile serviceAsyncJavaFile = createJavaFile(serviceAsyncDesc);
        Generator serviceAsyncGenerator =
            createServiceAsyncGenerator(serviceAsyncDesc);
        generateJavaFile(serviceAsyncGenerator, serviceAsyncJavaFile);

        getProject().setNewProperty(
            serviceClassNameProperty,
            serviceDesc.getQualifiedName());
    }

    /**
     * Creates a service description.
     * 
     * @return a service description
     * @throws IOException
     * @throws XPathExpressionException
     */
    private GWTServiceDesc createServiceDesc() throws IOException,
            XPathExpressionException {
        ClassNameBuilder nameBuilder = new ClassNameBuilder();
        nameBuilder.append(getServiceBasePackageName());
        nameBuilder.append(serviceDefinition);

        GWTServiceDesc serviceDesc = new GWTServiceDesc();
        serviceDesc.setPackageName(nameBuilder.getPackageName());
        serviceDesc.setSimpleName(nameBuilder.getSimpleName());
        serviceDesc.setRemoteServiceRelativePath(remoteServiceRelativePath);
        return serviceDesc;
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
        StringBuilder buf = new StringBuilder();
        buf.append(config.getRootPackageName());
        buf.append(".");
        buf.append(Constants.CLIENT_PACKAGE);
        buf.append(".");
        buf.append(Constants.SERVICE_PACKAGE);
        return buf.toString();
    }

    /**
     * Creates a service async description.
     * 
     * @param serviceDesc
     *            the service description
     * @return a service async description
     */
    private GWTServiceAsyncDesc createServiceAsyncDesc(
            GWTServiceDesc serviceDesc) {
        GWTServiceAsyncDesc serviceAsyncDesc = new GWTServiceAsyncDesc();
        serviceAsyncDesc.setPackageName(serviceDesc.getPackageName());
        serviceAsyncDesc.setSimpleName(serviceDesc.getSimpleName()
            + Constants.ASYNC_SUFFIX);
        return serviceAsyncDesc;
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
