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

import org.slim3.gen.Constants;
import org.slim3.gen.util.ClassUtil;
import org.slim3.gen.util.StringUtil;

/**
 * Creates a controller description.
 * 
 * @author taedium
 * @since 1.0.0
 * 
 */
public class ControllerDescFactory {

    /** the base package name of controller */
    protected final String packageName;

    /** the superclass name */
    protected final String superclassName;

    /** the superclass name of testcase */
    protected final String testCaseSuperclassName;

    /** {@code true} if the controller uses a view */
    protected final boolean useView;

    /**
     * Creates a new {@link ControllerDescFactory}.
     * 
     * @param packageName
     *            the base package name of controller
     * @param superclassName
     *            the superclass name
     * @param testCaseSuperclassName
     *            the superclass name of testcase
     * @param useView
     *            {@code true} if the controller uses a view, otherwise {@code
     *            false}
     */
    public ControllerDescFactory(String packageName, String superclassName,
            String testCaseSuperclassName, boolean useView) {
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
        this.packageName = packageName;
        this.superclassName = superclassName;
        this.testCaseSuperclassName = testCaseSuperclassName;
        this.useView = useView;
    }

    /**
     * Creates a controller description.
     * 
     * @param path
     *            the path
     * @return a controller description
     */
    public ControllerDesc createControllerDesc(String path) {
        if (path == null) {
            throw new NullPointerException("The path parameter is null.");
        }
        if (!path.startsWith("/")) {
            throw new IllegalArgumentException(
                "The path does not start with '/'.");
        }
        ControllerDesc controllerDesc = new ControllerDesc();
        controllerDesc.setSuperclassName(superclassName);
        controllerDesc.setTestCaseSuperclassName(testCaseSuperclassName);
        String className = toControllerClassName(path);
        controllerDesc.setPackageName(ClassUtil.getPackageName(className));
        controllerDesc.setSimpleName(ClassUtil.getSimpleName(className));
        controllerDesc.setViewName(toViewName(path));
        controllerDesc.setPath(path);
        controllerDesc.setUseView(useView);
        return controllerDesc;
    }

    /**
     * Converts the path to a name of controller.
     * 
     * @param path
     *            the path
     * @return a name of controller
     */
    protected String toControllerClassName(String path) {
        String className = packageName + path.replace('/', '.');
        if (className.endsWith(".")) {
            className += Constants.INDEX_CONTROLLER;
        } else {
            int pos = className.lastIndexOf('.');
            className =
                className.substring(0, pos + 1)
                    + StringUtil.capitalize(className.substring(pos + 1))
                    + Constants.CONTROLLER_SUFFIX;
        }
        return className;
    }

    /**
     * Converts the path to a neme of view.
     * 
     * @param path
     *            the path
     * @return a name of view
     */
    protected String toViewName(String path) {
        String viewName = path;
        if (path.endsWith("/")) {
            viewName += Constants.INDEX_VIEW;
        } else {
            viewName += Constants.VIEW_SUFFIX;
        }
        return viewName;
    }
}
