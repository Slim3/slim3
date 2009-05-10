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

import org.slim3.controller.Controller;
import org.slim3.controller.ControllerConstants;
import org.slim3.controller.FrontController;
import org.slim3.controller.RequestLocator;
import org.slim3.controller.ResponseLocator;

/**
 * A tester for Slim3 Controller.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class ControllerTester {

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
     * Whether "setUp" method was invoked.
     */
    protected boolean setUpInvoked = false;

    /**
     * Whether "start" method was invoked.
     */
    protected boolean startInvoked = false;

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
        RequestLocator.setRequest(request);
        ResponseLocator.setResponse(response);
        filterChain = new MockFilterChain();
        setUpInvoked = true;
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
        RequestLocator.setRequest(null);
        ResponseLocator.setResponse(null);
        filterChain = null;
    }

    /**
     * Returns the request parameter.
     * 
     * @param name
     *            the parameter name
     * @return the parameter value
     */
    public String getParameter(String name) {
        assertSetUpWasInvoked();
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
    public void setParameter(String name, String value) {
        assertSetUpWasInvoked();
        request.setParameter(name, value);
    }

    /**
     * Returns the request parameter.
     * 
     * @param name
     *            the parameter name
     * @return the parameter value
     */
    public String[] getParameterValues(String name) {
        assertSetUpWasInvoked();
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
    public void setParameter(String name, String[] value) {
        assertSetUpWasInvoked();
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
    public <T> T getAttribute(String name) {
        assertSetUpWasInvoked();
        return (T) request.getAttribute(name);
    }

    /**
     * Sets the request attribute
     * 
     * @param name
     *            the attribute name
     * @param value
     *            the attribute value
     */
    public void setAttribute(String name, Object value) {
        assertSetUpWasInvoked();
        request.setAttribute(name, value);
    }

    /**
     * Removes the request attribute.
     * 
     * @param <T>
     *            the return type
     * @param name
     *            the attribute name
     * @return the removed value
     */
    @SuppressWarnings("unchecked")
    public <T> T removeAttribute(String name) {
        assertSetUpWasInvoked();
        Object value = request.getAttribute(name);
        request.removeAttribute(name);
        return (T) value;
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
    public <T> T getSessionAttribute(String name) {
        assertSetUpWasInvoked();
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
    public void setSessionAttribute(String name, Object value) {
        assertSetUpWasInvoked();
        request.getSession().setAttribute(name, value);
    }

    /**
     * Removes the session attribute.
     * 
     * @param <T>
     *            the return type
     * @param name
     *            the attribute name
     * @return the removed value
     */
    @SuppressWarnings("unchecked")
    public <T> T removeSessionAttribute(String name) {
        assertSetUpWasInvoked();
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(name);
        session.removeAttribute(name);
        return (T) value;
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
    public <T> T getServletContextAttribute(String name) {
        assertSetUpWasInvoked();
        return (T) servletContext.getAttribute(name);
    }

    /**
     * Sets the servlet context attribute.
     * 
     * @param name
     *            the attribute name
     * @param value
     *            the attribute value
     */
    public void setServletContextAttribute(String name, Object value) {
        assertSetUpWasInvoked();
        servletContext.setAttribute(name, value);
    }

    /**
     * Returns the servlet context attribute.
     * 
     * @param <T>
     *            the return type
     * @param name
     *            the attribute name
     * @return the removed value
     */
    @SuppressWarnings("unchecked")
    public <T> T removeServletContextAttribute(String name) {
        assertSetUpWasInvoked();
        Object value = servletContext.getAttribute(name);
        servletContext.removeAttribute(name);
        return (T) value;
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
    public void start(String path) throws NullPointerException,
            IllegalArgumentException, IOException, ServletException {
        if (path == null) {
            throw new NullPointerException("The path parameter is null.");
        }
        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("The path("
                + path
                + ") must start with \"/\".");
        }
        assertSetUpWasInvoked();
        request.setServletPath(path);
        frontController.doFilter(request, response, filterChain);
        startInvoked = true;
    }

    /**
     * Determines if the test result is "redirect".
     * 
     * @return whether the test result is "redirect"
     */
    public boolean isRedirect() {
        assertStartWasInvoked();
        return response.getRedirectPath() != null;
    }

    /**
     * Returns the next path.
     * 
     * @return the next path
     */
    public String getNextPath() {
        assertStartWasInvoked();
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
     * Returns the controller.
     * 
     * @param <T>
     *            the controller type
     * @return the controller
     */
    @SuppressWarnings("unchecked")
    public <T extends Controller> T getController() {
        assertStartWasInvoked();
        return (T) request.getAttribute(ControllerConstants.CONTROLLER_KEY);
    }

    /**
     * Asserts that "setUp" method was invoked.
     */
    protected void assertSetUpWasInvoked() {
        if (!setUpInvoked) {
            throw new IllegalStateException(
                "Invoke ControllerTester#setUp() before using this tester.");
        }
    }

    /**
     * Asserts that "start" method was invoked.
     */
    protected void assertStartWasInvoked() {
        if (!startInvoked) {
            throw new IllegalStateException(
                "Invoke ControllerTester#start() before getting the test results.");
        }
    }
}