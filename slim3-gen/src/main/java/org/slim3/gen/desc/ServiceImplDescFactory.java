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
package org.slim3.gen.desc;

import org.slim3.gen.Constants;
import org.slim3.gen.util.ClassUtil;

/**
 * Creates a service implementation description factory.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class ServiceImplDescFactory {

    /** the service package name */
    protected final String packageName;

    /** the test case superclass name */
    protected final String testCaseSuperclassName;

    /** the service class qualified name */
    protected final String serviceClassName;

    /**
     * Creates a new {@link ServiceImplDescFactory}.
     * 
     * @param packageName
     *            the service package name
     * @param testCaseSuperclassName
     *            the test case superclass name
     * @param serviceClassName
     *            the service class qualified name
     */
    public ServiceImplDescFactory(String packageName,
            String testCaseSuperclassName, String serviceClassName) {
        if (packageName == null) {
            throw new NullPointerException("The packageName parameter is null.");
        }
        if (testCaseSuperclassName == null) {
            throw new NullPointerException(
                "The testCaseSuperclassName parameter is null.");
        }
        if (serviceClassName == null) {
            throw new NullPointerException(
                "The serviceClassName parameter is null.");
        }
        this.packageName = packageName;
        this.testCaseSuperclassName = testCaseSuperclassName;
        this.serviceClassName = serviceClassName;
    }

    /**
     * Creates a service implementation description.
     * 
     * @return a service implementation description.
     */
    public ServiceImplDesc createServiceImplDesc() {
        ServiceImplDesc serviceImplDesc = new ServiceImplDesc();
        serviceImplDesc.setPackageName(packageName);
        serviceImplDesc.setSimpleName(ClassUtil.getSimpleName(serviceClassName)
            + Constants.IMPL_SUFFIX);
        serviceImplDesc.setTestCaseSuperclassName(testCaseSuperclassName);
        serviceImplDesc.setServiceClassName(serviceClassName);
        return serviceImplDesc;
    }
}
