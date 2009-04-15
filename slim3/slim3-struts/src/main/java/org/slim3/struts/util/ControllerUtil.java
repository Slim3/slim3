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
package org.slim3.struts.util;

import org.slim3.struts.S3StrutsGlobals;
import org.slim3.struts.web.WebLocator;

/**
 * A utility for a controller.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public final class ControllerUtil {

    private static final String KEY = ControllerUtil.class.getName();

    private ControllerUtil() {
    }

    /**
     * Returns the controller from request.
     * 
     * @return the controller
     */
    public static Object getController() {
        return WebLocator.getRequest().getAttribute(KEY);
    }

    /**
     * Sets the controller to request.
     * 
     * @param controller
     *            the controller
     */
    public static void setController(Object controller) {
        WebLocator.getRequest().setAttribute(KEY, controller);
    }

    /**
     * Returns the controller path.
     * 
     * @return the controller path
     */
    public static String getPath() {
        String s = RequestUtil.getPath();
        if (s.indexOf('.') < 0) {
            return s;
        }
        if (s.endsWith(S3StrutsGlobals.ACTION_EXTENSION)) {
            return s.substring(0, s.length()
                    - S3StrutsGlobals.ACTION_EXTENSION.length())
                    + "/";
        }
        return s.substring(0, s.lastIndexOf('/') + 1);
    }
}