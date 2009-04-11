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

import javax.servlet.http.HttpServletRequest;

import org.slim3.commons.config.Configuration;
import org.slim3.commons.util.StringUtil;
import org.slim3.struts.S3StrutsGlobals;
import org.slim3.struts.web.WebLocator;

/**
 * A utility for {@link HttpServletRequest}.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public final class RequestUtil {

    /**
     * Returns the path.
     * 
     * @return the path
     */
    public static String getPath() {
        return getPath(WebLocator.getRequest());
    }

    /**
     * Returns the path.
     * 
     * @param request
     *            the request
     * @return the path
     * @throws NullPointerException
     *             if the request parameter is null
     */
    public static String getPath(HttpServletRequest request)
            throws NullPointerException {
        if (request == null) {
            throw new NullPointerException("The request parameter is null.");
        }
        String path = request.getPathInfo();
        if (StringUtil.isEmpty(path)) {
            path = request.getServletPath();
        }
        if (path == null) {
            return null;
        }
        String viewPrefix = Configuration.getInstance().getValue(
                S3StrutsGlobals.VIEW_PREFIX_KEY);
        if (viewPrefix == null) {
            return path;
        }
        if (path.startsWith(viewPrefix)) {
            path = path.substring(viewPrefix.length());
        }
        return path;
    }

    private RequestUtil() {
    }
}
