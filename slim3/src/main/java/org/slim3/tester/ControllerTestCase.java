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
package org.slim3.tester;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import junit.framework.TestCase;

import org.slim3.controller.Controller;
import org.slim3.controller.ControllerConstants;
import org.slim3.controller.FrontController;
import org.slim3.controller.RequestLocator;
import org.slim3.controller.ResponseLocator;

/**
 * A test case for Slim3 Controller.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public abstract class ControllerTestCase extends TestCase {

    /**
     * The mock for {@link ServletContext}.
     */
    protected MockServletContext application;

    /**
     * The mock for {@link ServletConfig}.
     */
    protected MockServletConfig config;

    /**
     * The mock for {@link FilterConfig}.
     */
    protected MockFilterConfig filterConfig;

    /**
     * The front controller.
     */
    protected FrontController frontController;

    /**
     * The mock for {@link HttpServletRequest}.
     */
    protected MockHttpServletRequest request;

    /**
     * The mock for {@link HttpServletResponse}.
     */
    protected MockHttpServletResponse response;

    /**
     * A mock for {@link FilterChain}.
     */
    protected MockFilterChain filterChain;

    /**
     * Whether "start" method was called.
     */
    protected boolean startCalled = false;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setUpControllerPackage();
        application = new MockServletContext();
        config = new MockServletConfig(application);
        filterConfig = new MockFilterConfig(application);
        frontController = new FrontController();
        frontController.init(filterConfig);
        request = new MockHttpServletRequest(application);
        response = new MockHttpServletResponse();
        RequestLocator.setRequest(request);
        ResponseLocator.setResponse(response);
        filterChain = new MockFilterChain();
    }

    /**
     * Sets up the controller package automatically.
     */
    protected void setUpControllerPackage() {
        if (System.getProperty(ControllerConstants.CONTROLLER_PACKAGE_KEY) != null) {
            return;
        }
        String className = getClass().getName();
        int pos = className.indexOf(".controller.");
        if (pos < 0) {
            return;
        }
        String packageName = className.substring(0, pos + 11);
        System.setProperty(
            ControllerConstants.CONTROLLER_PACKAGE_KEY,
            packageName);
    }

    @Override
    protected void tearDown() throws Exception {
        application = null;
        config = null;
        filterConfig = null;
        frontController.destroy();
        frontController = null;
        request = null;
        response = null;
        RequestLocator.setRequest(null);
        ResponseLocator.setResponse(null);
        filterChain = null;
        tearDownControllerPackage();
        super.tearDown();
    }

    /**
     * Tears down the controller package.
     */
    protected void tearDownControllerPackage() {
        System.clearProperty(ControllerConstants.CONTROLLER_PACKAGE_KEY);
    }

    /**
     * Returns the request parameter.
     * 
     * @param name
     *            the parameter name
     * @return the parameter value
     */
    protected String param(String name) {
        return request.getParameter(name);
    }

    /**
     * Sets the request parameter.
     * 
     * @param name
     *            the parameter name
     * @param value
     *            the parameter value
     */
    protected void param(String name, String value) {
        request.setParameter(name, value);
    }

    /**
     * Returns the request parameter.
     * 
     * @param name
     *            the parameter name
     * @return the parameter value
     */
    protected String[] paramValues(String name) {
        return request.getParameterValues(name);
    }

    /**
     * Sets the request parameter.
     * 
     * @param name
     *            the parameter name
     * @param value
     *            the parameter value
     */
    protected void paramValues(String name, String[] value) {
        request.setParameter(name, value);
    }

    /**
     * Returns the request attribute.
     * 
     * @param <T>
     *            the return type
     * @param name
     *            the attribute name
     * @return the request attribute
     */
    @SuppressWarnings("unchecked")
    protected <T> T requestScope(String name) {
        return (T) request.getAttribute(name);
    }

    /**
     * Sets the request attribute.
     * 
     * @param name
     *            the attribute name
     * @param value
     *            the attribute value
     */
    protected void requestScope(String name, Object value) {
        request.setAttribute(name, value);
    }

    /**
     * Returns the session attribute.
     * 
     * @param <T>
     *            the return type
     * @param name
     *            the attribute name
     * @return the attribute value
     */
    @SuppressWarnings("unchecked")
    protected <T> T sessionScope(String name) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (T) session.getAttribute(name);
    }

    /**
     * Sets the session attribute.
     * 
     * @param name
     *            the attribute name
     * @param value
     *            the attribute value
     */
    protected void sessionScope(String name, Object value) {
        request.getSession().setAttribute(name, value);
    }

    /**
     * Returns the servlet context attribute.
     * 
     * @param <T>
     *            the return type
     * @param name
     *            the attribute name
     * @return the attribute value
     */
    @SuppressWarnings("unchecked")
    protected <T> T applicationScope(String name) {
        return (T) application.getAttribute(name);
    }

    /**
     * Sets the servlet context attribute.
     * 
     * @param name
     *            the attribute name
     * @param value
     *            the attribute value
     */
    protected void applicationScope(String name, Object value) {
        application.setAttribute(name, value);
    }

    /**
     * Starts the request process.
     * 
     * @param path
     *            the request path
     * @throws NullPointerException
     *             if the path parameter is null
     * @throws IllegalArgumentException
     *             if the path does not start with "/"
     * @throws IOException
     *             if {@link IOException} occurred
     * @throws ServletException
     *             if {@link ServletException} occurred
     */
    protected void start(String path) throws NullPointerException,
            IllegalArgumentException, IOException, ServletException {
        if (path == null) {
            throw new NullPointerException("The path parameter is null.");
        }
        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("The path("
                + path
                + ") must start with \"/\".");
        }
        request.setServletPath(path);
        frontController.doFilter(request, response, filterChain);
        startCalled = true;
    }

    /**
     * Determines if the test result is "redirect".
     * 
     * @return whether the test result is "redirect"
     */
    protected boolean isRedirect() {
        return response.getRedirectPath() != null;
    }

    /**
     * Returns the next path.
     * 
     * @return the next path
     */
    protected String getNextPath() {
        assertStartWasCalled();
        MockRequestDispatcher dispatcher =
            application.getLatestRequestDispatcher();
        if (dispatcher != null) {
            return dispatcher.getPath();
        }
        if (response.getRedirectPath() != null) {
            return response.getRedirectPath();
        }
        return filterChain.getPath();
    }

    /**
     * Returns the controller.
     * 
     * @param <T>
     *            the controller type
     * @return the controller
     */
    @SuppressWarnings("unchecked")
    protected <T extends Controller> T getController() {
        assertStartWasCalled();
        return (T) request.getAttribute(ControllerConstants.CONTROLLER_KEY);
    }

    /**
     * Asserts that "start" method was called.
     */
    protected void assertStartWasCalled() {
        if (!startCalled) {
            throw new IllegalStateException(
                "Call ControllerTester#start() before getting the test results.");
        }
    }
}