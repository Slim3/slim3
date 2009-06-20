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
 * Creates a dao description.
 * 
 * @author taedium
 * @since 3.0
 */
public class DaoDescFactory {

    /** the base package name of dao */
    protected final String packageName;

    /** the superclass name of dao */
    protected final String superclassName;

    /** the superclass name of testcase */
    protected final String testCaseSuperclassName;

    /** the class name of model */
    protected String modelClassName;

    /**
     * Creates a new {@link DaoDescFactory}.
     * 
     * @param packageName
     *            the base package name of DAO
     * @param superclassName
     *            the superclass name of DAO
     * @param testCaseSuperclassName
     *            the superclass name of testcase
     * @param modelClassName
     *            the class name of model
     */
    public DaoDescFactory(String packageName, String superclassName,
            String testCaseSuperclassName, String modelClassName) {
        if (packageName == null) {
            throw new NullPointerException("The packageName parameter is null.");
        }
        if (superclassName == null) {
            throw new NullPointerException(
                "The superclassName parameter is null.");
        }
        if (testCaseSuperclassName == null) {
            throw new NullPointerException(
                "The testCaseSuperclassName parameter is null.");
        }
        if (modelClassName == null) {
            throw new NullPointerException(
                "The modelClassName parameter is null.");
        }
        this.packageName = packageName;
        this.superclassName = superclassName;
        this.testCaseSuperclassName = testCaseSuperclassName;
        this.modelClassName = modelClassName;
    }

    /**
     * Creates the dao description.
     * 
     * @return a dao description.
     */
    public DaoDesc createDaoDesc() {
        DaoDesc daoDesc = new DaoDesc();
        daoDesc.setPackageName(packageName);
        daoDesc.setSimpleName(getSimpleName());
        daoDesc.setSuperclassName(superclassName);
        daoDesc.setTestCaseSuperclassName(testCaseSuperclassName);
        daoDesc.setModelClassName(modelClassName);
        return daoDesc;
    }

    /**
     * Returns the dao simple name.
     * 
     * @return a dao simple name
     */
    protected String getSimpleName() {
        String modelSimpleName = ClassUtil.getSimpleName(modelClassName);
        return modelSimpleName + Constants.DAO_SUFFIX;
    }

}
