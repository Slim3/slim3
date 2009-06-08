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

/**
 * Creates a model description.
 * 
 * @author taedium
 * @since 3.0
 */
public class ModelDescFactory {

    /** the base package name of model */
    protected final String packageName;

    /** the simple name of model */
    protected final String simpleName;

    /** the superclass name of testcase */
    protected final String testCaseSuperclassName;

    /**
     * Creates a new {@link ModelDescFactory}.
     * 
     * @param packageName
     *            the base package name of model
     * @param simpleName
     *            the simple name of model
     * @param testCaseSuperclassName
     *            the superclass name of testcase
     */
    public ModelDescFactory(String packageName, String simpleName,
            String testCaseSuperclassName) {
        if (packageName == null) {
            throw new NullPointerException("The packageName parameter is null.");
        }
        if (simpleName == null) {
            throw new NullPointerException("The simpleName parameter is null.");
        }
        if (testCaseSuperclassName == null) {
            throw new NullPointerException(
                "The testCaseSuperclassName parameter is null.");
        }
        this.packageName = packageName;
        this.simpleName = simpleName;
        this.testCaseSuperclassName = testCaseSuperclassName;
    }

    /**
     * Creates the model description.
     * 
     * @return a model description.
     */
    public ModelDesc createModelDesc() {
        ModelDesc modelDesc = new ModelDesc();
        modelDesc.setPackageName(packageName);
        modelDesc.setSimpleName(simpleName);
        modelDesc.setTestCaseSuperclassName(testCaseSuperclassName);
        return modelDesc;
    }

}
