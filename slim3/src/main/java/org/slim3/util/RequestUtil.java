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
package org.slim3.util;

import javax.servlet.http.HttpServletRequest;

/**
 * A utility class for {@link HttpServletRequest}.
 * 
 * @author higa
 * @version 3.0
 */
public final class RequestUtil {

    /**
     * Returns the path of the request.
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
            throw new NullPointerException(
                "The request parameter must not be null.");
        }
        return request.getServletPath();
    }

    /**
     * Returns the extension.
     * 
     * @param path
     *            the path
     * @return the extension
     * @throws NullPointerException
     *             if the path parameter is null
     */
    public static String getExtension(String path) throws NullPointerException {
        if (path == null) {
            throw new NullPointerException("The path parameter is null.");
        }
        int dotIndex = path.lastIndexOf('.');
        if (dotIndex < 0) {
            return null;
        }
        int slashIndex = path.lastIndexOf('/');
        if (slashIndex < 0 || dotIndex > slashIndex) {
            return path.substring(dotIndex + 1);
        }
        return null;
    }

    private RequestUtil() {
    }
}