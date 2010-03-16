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
package org.slim3.gen.desc;

import org.slim3.gen.util.ClassUtil;

/**
 * Represents a GWT service implementation description.
 * 
 * @author taedium
 * @since 1.0.0
 * 
 */
public class GWTServiceImplDesc implements ClassDesc {

    /** the package name */
    protected String packageName;

    /** the simple name */
    protected String simpleName;

    /** the superclass name */
    protected String superclassName;

    /** the test case superclass name */
    protected String testCaseSuperclassName;

    /** the service class qualified name */
    protected String serviceClassName;

    /**
     * Returns the packageName.
     * 
     * @return the packageName
     */
    public String getPackageName() {
        return packageName;
    }

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
     * Returns the simpleName.
     * 
     * @return the simpleName
     */
    public String getSimpleName() {
        return simpleName;
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

    public String getQualifiedName() {
        return ClassUtil.getQualifiedName(packageName, simpleName);
    }

    /**
     * Returns the superclass name.
     * 
     * @return the superclass name
     */
    public String getSuperclassName() {
        return superclassName;
    }

    /**
     * Sets the superclass name.
     * 
     * @param superclassName
     *            the superclass name
     */
    public void setSuperclassName(String superclassName) {
        this.superclassName = superclassName;
    }

    /**
     * Returns the test case superclass name.
     * 
     * @return the test case superclass name
     */
    public String getTestCaseSuperclassName() {
        return testCaseSuperclassName;
    }

    /**
     * Sets the test case superclass name.
     * 
     * @param testCaseSuperclassName
     *            the test case superclass name
     */
    public void setTestCaseSuperclassName(String testCaseSuperclassName) {
        this.testCaseSuperclassName = testCaseSuperclassName;
    }

    /**
     * Returns the service class qualified name.
     * 
     * @return the service class qualified name
     */
    public String getServiceClassName() {
        return serviceClassName;
    }

    /**
     * Sets the service class qualified name.
     * 
     * @param serviceClassName
     *            the service class qualified name
     */
    public void setServiceClassName(String serviceClassName) {
        this.serviceClassName = serviceClassName;
    }

}
