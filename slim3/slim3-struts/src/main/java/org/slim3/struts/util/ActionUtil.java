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
 * A utility for an action.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public final class ActionUtil {

    private static final String KEY = ActionUtil.class.getName();

    private ActionUtil() {
    }

    /**
     * Returns the action from request.
     * 
     * @return the action
     */
    public static Object getAction() {
        return WebLocator.getRequest().getAttribute(KEY);
    }

    /**
     * Sets the action to request.
     * 
     * @param action
     *            the action
     */
    public static void setAction(Object action) {
        WebLocator.getRequest().setAttribute(KEY, action);
    }

    /**
     * Returns the action path.
     * 
     * @return the action path
     */
    public static String getActionPath() {
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