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
package org.slim3.mvc.unit;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slim3.mvc.MvcConstants;
import org.slim3.mvc.controller.Controller;
import org.slim3.mvc.controller.FrontController;

/**
 * A base test case for Slim3 MVC.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class MvcTester {

    /**
     * The mock for {@link ServletContext}.
     */
    public MockServletContext servletContext;

    /**
     * The mock for {@link ServletConfig}.
     */
    public MockServletConfig servletConfig;

    /**
     * The mock for {@link FilterConfig}.
     */
    public MockFilterConfig filterConfig;

    /**
     * The front controller.
     */
    public FrontController frontController;

    /**
     * The mock for {@link HttpServletRequest}.
     */
    public MockHttpServletRequest request;

    /**
     * The mock for {@link HttpServletResponse}.
     */
    public MockHttpServletResponse response;

    /**
     * A mock for {@link FilterChain}.
     */
    public MockFilterChain filterChain;

    /**
     * Whether setUp method was called.
     */
    protected boolean setUpCalled = false;

    /**
     * Whether test method was called.
     */
    protected boolean testCalled = false;

    /**
     * Sets up the test environment.
     * 
     * @throws Exception
     *             if an exception has occurred
     */
    public void setUp() throws Exception {
        servletContext = new MockServletContext();
        servletConfig = new MockServletConfig(servletContext);
        filterConfig = new MockFilterConfig(servletContext);
        frontController = new FrontController();
        frontController.init(filterConfig);
        request = new MockHttpServletRequest(servletContext);
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();
        setUpCalled = true;
    }

    /**
     * Tears down the test environment.
     * 
     * @throws Exception
     *             if an exception has occurred
     */
    public void tearDown() throws Exception {
        servletContext = null;
        servletConfig = null;
        filterConfig = null;
        frontController.destroy();
        frontController = null;
        request = null;
        response = null;
        filterChain = null;
    }

    /**
     * Sets the request path.
     * 
     * @param path
     *            the request path
     */
    public void setPath(String path) {
        assertSetUpWasCalled();
        request.setPathInfo(path);
    }

    /**
     * Sets the request parameter.
     * 
     * @param name
     *            the parameter name
     * @param value
     *            the parameter value
     */
    public void setParameter(String name, String value) {
        assertSetUpWasCalled();
        request.setParameter(name, value);
    }

    /**
     * Sets the request parameter.
     * 
     * @param name
     *            the parameter name
     * @param value
     *            the parameter value
     */
    public void setParameter(String name, String[] value) {
        assertSetUpWasCalled();
        request.setParameter(name, value);
    }

    /**
     * Tests the front controller.
     * 
     * @throws IOException
     *             if {@link IOException} has occurred
     * @throws ServletException
     *             if {@link ServletException} has occurred
     */
    public void test() throws IOException, ServletException {
        assertSetUpWasCalled();
        frontController.doFilter(request, response, filterChain);
        testCalled = true;
    }

    /**
     * Determines if the test result is "redirect".
     * 
     * @return whether the test result is "redirect"
     */
    public boolean isRedirect() {
        assertTestWasCalled();
        return response.getRedirectPath() != null;
    }

    /**
     * Returns the next path.
     * 
     * @return the next path
     */
    public String getNextPath() {
        assertTestWasCalled();
        MockRequestDispatcher dispatcher =
            servletContext.getLatestRequestDispatcher();
        if (dispatcher != null) {
            return dispatcher.getPath();
        }
        if (response.getRedirectPath() != null) {
            return response.getRedirectPath();
        }
        return filterChain.getPath();
    }

    /**
     * Returns the request attribute.
     * 
     * @param <T>
     *            the type
     * @param name
     *            the name
     * @return the request attribute
     */
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String name) {
        assertTestWasCalled();
        return (T) request.getAttribute(name);
    }

    /**
     * Returns the session attribute.
     * 
     * @param <T>
     *            the type
     * @param name
     *            the name
     * @return the session attribute
     */
    @SuppressWarnings("unchecked")
    public <T> T getSessionAttribute(String name) {
        assertTestWasCalled();
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (T) session.getAttribute(name);
    }

    /**
     * Returns the servlet context attribute.
     * 
     * @param <T>
     *            the type
     * @param name
     *            the name
     * @return the servlet context attribute
     */
    @SuppressWarnings("unchecked")
    public <T> T getServletContextAttribute(String name) {
        assertTestWasCalled();
        return (T) servletContext.getAttribute(name);
    }

    /**
     * Returns the controller.
     * 
     * @param <T>
     *            the controller type
     * @return the controller
     */
    @SuppressWarnings("unchecked")
    public <T extends Controller> T getController() {
        assertTestWasCalled();
        return (T) request.getAttribute(MvcConstants.CONTROLLER_KEY);
    }

    /**
     * Asserts that the setUp method was called.
     */
    protected void assertSetUpWasCalled() {
        if (!setUpCalled) {
            throw new IllegalStateException(
                "Call MvcTester#setUp() to use this tester.");
        }
    }

    /**
     * Asserts that the test method was called.
     */
    protected void assertTestWasCalled() {
        if (!testCalled) {
            throw new IllegalStateException(
                "Call MvcTester#test() to get the test results.");
        }
    }
}