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
package org.slim3.controller.router;

import javax.servlet.http.HttpServletRequest;

/**
 * A router of request.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public interface Router {

    /**
     * Routes the path.
     * 
     * @param request
     *            the request
     * @param path
     *            the path
     * @return a routed path
     * @throws NullPointerException
     *             if the request parameter is null or if the path parameter is
     *             null
     */
    String route(HttpServletRequest request, String path)
            throws NullPointerException;

    /**
     * Determines if a file specified by the path is static.
     * 
     * @param path
     *            the path
     * @return whether a file specified by the path is static
     * @throws NullPointerException
     *             if the path parameter is null
     */
    boolean isStatic(String path) throws NullPointerException;
}