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
package org.slim3.struts.web;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The locator for web.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public final class WebLocator {

    private static ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<HttpServletRequest>();

    private static ThreadLocal<HttpServletResponse> responseThreadLocal = new ThreadLocal<HttpServletResponse>();

    private static ServletContext servletContext;

    /**
     * Returns the current request.
     * 
     * @return the current request
     */
    public static HttpServletRequest getRequest() {
        return requestThreadLocal.get();
    }

    /**
     * Sets the current request.
     * 
     * @param request
     *            the current request
     */
    public static void setRequest(HttpServletRequest request) {
        requestThreadLocal.set(request);
    }

    /**
     * Returns the current response.
     * 
     * @return the current response
     */
    public static HttpServletResponse getResponse() {
        return responseThreadLocal.get();
    }

    /**
     * Sets the current response.
     * 
     * @param response
     *            the current response
     */
    public static void setResponse(HttpServletResponse response) {
        responseThreadLocal.set(response);
    }

    /**
     * Returns the servlet context.
     * 
     * @return the servlet context
     */
    public static ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * Sets the servlet context.
     * 
     * @param servletContext
     *            the servlet context
     */
    public static void setServletContext(ServletContext servletContext) {
        WebLocator.servletContext = servletContext;
    }

    private WebLocator() {
    }
}
