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
import org.slim3.gen.desc.ServiceDesc;
import org.slim3.gen.desc.ServiceDescFactory;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.generator.ServiceGenerator;
import org.slim3.gen.message.MessageCode;
import org.slim3.gen.message.MessageFormatter;

/**
 * Represents a task to generate a service java file.
 * 
 * @author taedium
 * @since 3.0
 */
public class GenServiceTask extends AbstractGenJavaFileTask {

    /** the packageName */
    protected String packageName;

    /** the simpleName */
    protected String simpleName;

    /** the remoteServiceRelativePath */
    protected String remoteServiceRelativePath = "s3gwt";

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
     * Sets the simpleName.
     * 
     * @param simpleName
     *            the simpleName to set
     */
    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
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
        if (simpleName == null) {
            throw new IllegalStateException("The simpleName parameter is null.");
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
        String servicePackageName = getServicePackageName();
        ServiceDescFactory factory =
            createServiceDescFactory(servicePackageName);
        ServiceDesc serviceDesc = factory.createServiceDesc();

        JavaFile javaFile = createJavaFile(serviceDesc);
        Generator generator = createServiceGenerator(serviceDesc);
        generateJavaFile(generator, javaFile);

        getProject().setNewProperty(
            serviceClassNameProperty,
            serviceDesc.getQualifiedName());
    }

    /**
     * Returns the service package name.
     * 
     * @return the service package name.
     * @throws IOException
     * @throws XPathExpressionException
     */
    protected String getServicePackageName() throws IOException,
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
     * Creates a {@link ServiceDescFactory}.
     * 
     * @param packageName
     *            the package name
     * @return a service description factory.
     */
    protected ServiceDescFactory createServiceDescFactory(String packageName) {
        return new ServiceDescFactory(
            packageName,
            simpleName,
            remoteServiceRelativePath);
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

}
