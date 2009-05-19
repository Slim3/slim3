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

import javax.servlet.ServletException;

import junit.framework.TestCase;

import org.slim3.controller.Controller;
import org.slim3.controller.ControllerConstants;

/**
 * A test case for Slim3 Controller.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public abstract class ControllerTestCase extends TestCase {

    /**
     * The tester for Slim3 Controller.
     */
    protected ControllerTester controllerTester = new ControllerTester();

    /**
     * @throws Exception
     * 
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setUpControllerPackage();
        controllerTester.setUp();
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

    /**
     * @throws Exception
     * 
     */
    @Override
    protected void tearDown() throws Exception {
        controllerTester.tearDown();
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
    protected String getParameter(String name) {
        return controllerTester.getParameter(name);
    }

    /**
     * Sets the request parameter.
     * 
     * @param name
     *            the parameter name
     * @param value
     *            the parameter value
     */
    protected void setParameter(String name, String value) {
        controllerTester.setParameter(name, value);
    }

    /**
     * Returns the request parameter.
     * 
     * @param name
     *            the parameter name
     * @return the parameter value
     */
    protected String[] getParameterValues(String name) {
        return controllerTester.getParameterValues(name);
    }

    /**
     * Sets the request parameter.
     * 
     * @param name
     *            the parameter name
     * @param value
     *            the parameter value
     */
    protected void setParameter(String name, String[] value) {
        controllerTester.setParameter(name, value);
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
    protected <T> T getAttribute(String name) {
        return (T) controllerTester.getAttribute(name);
    }

    /**
     * Sets the request attribute
     * 
     * @param name
     *            the attribute name
     * @param value
     *            the attribute value
     */
    protected void setAttribute(String name, Object value) {
        controllerTester.setAttribute(name, value);
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
    protected <T> T getSessionAttribute(String name) {
        return (T) controllerTester.getSessionAttribute(name);
    }

    /**
     * Sets the session attribute.
     * 
     * @param name
     *            the attribute name
     * @param value
     *            the attribute value
     */
    protected void setSessionAttribute(String name, Object value) {
        controllerTester.setSessionAttribute(name, value);
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
    protected <T> T getServletContextAttribute(String name) {
        return (T) controllerTester.getServletContextAttribute(name);
    }

    /**
     * Sets the servlet context attribute.
     * 
     * @param name
     *            the attribute name
     * @param value
     *            the attribute value
     */
    protected void setServletContextAttribute(String name, Object value) {
        controllerTester.setServletContextAttribute(name, value);
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
        controllerTester.start(path);
    }

    /**
     * Determines if the test result is "redirect".
     * 
     * @return whether the test result is "redirect"
     */
    protected boolean isRedirect() {
        return controllerTester.isRedirect();
    }

    /**
     * Returns the next path.
     * 
     * @return the next path
     */
    protected String getNextPath() {
        return controllerTester.getNextPath();
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
        return (T) controllerTester.getController();
    }
}