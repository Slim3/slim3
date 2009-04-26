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
package org.slim3.mvc.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slim3.commons.util.StringUtil;
import org.slim3.mvc.MvcConstants;

/**
 * A base controller. This controller is created for each request.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public abstract class Controller {

    /**
     * The servlet context.
     */
    protected ServletContext servletContext;

    /**
     * The request.
     */
    protected HttpServletRequest request;

    /**
     * The response.
     */
    protected HttpServletResponse response;

    /**
     * Returns the servlet context.
     * 
     * @return the servlet context
     */
    public ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * Sets the servlet context.
     * 
     * @param servletContext
     *            the servlet context
     */
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * Returns the request.
     * 
     * @return the request
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * Sets the request.
     * 
     * @param request
     *            the request
     */
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Returns the response.
     * 
     * @return the response
     */
    public HttpServletResponse getResponse() {
        return response;
    }

    /**
     * Sets the response.
     * 
     * @param response
     *            the response
     */
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    /**
     * Executes the action for request.
     * 
     * @return path to go.
     */
    public abstract Navigation execute();

    /**
     * Creates a new {@link Navigation} for "forward".
     * 
     * @param path
     *            the controller-relative path
     * @return a new {@link Navigation}
     */
    protected Navigation forward(String path) {
        return new Navigation(path, false);
    }

    /**
     * Creates a default {@link Navigation} for "forward".
     * 
     * @return a default {@link Navigation} for "forward".
     */
    protected Navigation forward() {
        return forward(calculateDefaultPath());
    }

    /**
     * Creates a new {@link Navigation} for "redirect".
     * 
     * @param path
     *            the controller-relative path
     * @return a new {@link Navigation}
     */
    protected Navigation redirect(String path) {
        return new Navigation(path, true);
    }

    /**
     * Calculates the default path when the path is not specified.
     * 
     * @return the default path
     * @throws IllegalStateException
     *             if this controller class name does not end with "Controller".
     */
    protected String calculateDefaultPath() throws IllegalStateException {
        String path = getClass().getSimpleName();
        if (!path.endsWith(MvcConstants.CONTROLLER_SUFFIX)) {
            throw new IllegalStateException("The controller class("
                    + getClass().getName() + ") does not end with \""
                    + MvcConstants.CONTROLLER_SUFFIX + "\".");
        }
        path = path.substring(0, path.length()
                - MvcConstants.CONTROLLER_SUFFIX.length());
        return StringUtil.decapitalize(path) + MvcConstants.JSP_EXTENSION;
    }
}