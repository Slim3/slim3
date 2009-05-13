/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
 * @author taedium
 * 
 */
public class ControllerDescFactory {

    protected final String controllerPackageName;

    public ControllerDescFactory(String controllerPackageName) {
        this.controllerPackageName = controllerPackageName;
    }

    public ControllerDesc createControllerDesc(String path) {
        if (path == null) {
            throw new NullPointerException("The path parameter is null.");
        }
        if (!path.startsWith("/")) {
            throw new IllegalArgumentException(
                    "The path does not start with '/'.");
        }
        ControllerDesc controllerDesc = new ControllerDesc();
        String className = toControllerClassName(path);
        controllerDesc.setPackageName(ClassUtil.getPackageName(className));
        controllerDesc.setSimpleName(ClassUtil.getSimpleName(className));
        controllerDesc.setViewName(toViewName(path));
        controllerDesc.setPath(path);
        return controllerDesc;
    }

    protected String toControllerClassName(String path)
            throws IllegalStateException {
        String className = controllerPackageName + path.replace('/', '.');
        if (className.endsWith(".")) {
            className += Constants.INDEX_CONTROLLER;
        } else {
            int pos = className.lastIndexOf('.');
            className = className.substring(0, pos + 1)
                    + StringUtil.capitalize(className.substring(pos + 1))
                    + Constants.CONTROLLER_SUFFIX;
        }
        return className;
    }

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
