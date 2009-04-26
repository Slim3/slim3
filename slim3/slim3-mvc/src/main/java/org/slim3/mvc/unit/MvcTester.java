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

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    }
}