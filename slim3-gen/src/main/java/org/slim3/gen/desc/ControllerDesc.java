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
 * Represents a controller description.
 * 
 * @author taedium
 * @since 3.0
 * 
 */
public class ControllerDesc {

    /** the superclass name */
    protected String superclassName;

    /** the superclass name of testcase */
    protected String testCaseSuperclassName;

    /** the package name of controller */
    protected String packageName;

    /** the simple name of controller */
    protected String simpleName;

    /** the name of view */
    protected String viewName;

    /** the simple name of view */
    protected String simpleViewName;

    /** the path of controller */
    protected String path;

    /**
     * Returns the superclassName.
     * 
     * @return the superclassName
     */
    public String getSuperclassName() {
        return superclassName;
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
     * Returns the testCaseSuperclassName.
     * 
     * @return the testCaseSuperclassName
     */
    public String getTestCaseSuperclassName() {
        return testCaseSuperclassName;
    }

    /**
     * Sets the testCaseSuperclassName.
     * 
     * @param testCaseSuperclassName
     *            the testCaseSuperclassName to set
     */
    public void setTestCaseSuperclassName(String testCaseSuperclassName) {
        this.testCaseSuperclassName = testCaseSuperclassName;
    }

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

    /**
     * Returns the viewName.
     * 
     * @return the viewName
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * Sets the viewName.
     * 
     * @param viewName
     *            the viewName to set
     */
    public void setViewName(String viewName) {
        this.viewName = viewName;
        if (viewName != null) {
            simpleViewName = viewName.substring(viewName.lastIndexOf('/') + 1);
        }
    }

    /**
     * Returns the simple name of view.
     * 
     * @return the simple name of view
     */
    public String getSimpleViewName() {
        return simpleViewName;
    }

    /**
     * Returns the path.
     * 
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the path.
     * 
     * @param path
     *            the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

}
