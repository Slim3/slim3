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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slim3.mvc.controller.RequestLocator;
import org.slim3.mvc.controller.ServletContextLocator;
import org.slim3.mvc.controller.ResponseLocator;

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
    protected MockServletContext servletContext;

    /**
     * The mock for {@link HttpServletRequest}.
     */
    protected MockHttpServletRequest request;

    /**
     * The mock for {@link HttpServletResponse}.
     */
    protected MockHttpServletResponse response;

    /**
     * Sets up the test environment.
     * 
     * @throws Exception
     *             if an exception has occurred
     */
    public void setUp() throws Exception {
        servletContext = new MockServletContext();
        request = new MockHttpServletRequest(servletContext);
        response = new MockHttpServletResponse();
        ServletContextLocator.setServletContext(servletContext);
        RequestLocator.setRequest(request);
        ResponseLocator.setResponse(response);
    }

    /**
     * Tears down the test environment.
     * 
     * @throws Exception
     *             if an exception has occurred
     */
    public void tearDown() throws Exception {
        servletContext = null;
        request = null;
        response = null;
        ServletContextLocator.setServletContext(null);
        RequestLocator.setRequest(null);
        ResponseLocator.setResponse(null);
    }

    /**
     * Returns the mock for {@link ServletContext}.
     * 
     * @return the mock for {@link ServletContext}
     */
    public MockServletContext getServletContext() {
        return servletContext;
    }

    /**
     * Returns the mock for {@link HttpServletRequest}.
     * 
     * @return the mock for {@link HttpServletRequest}
     */
    public MockHttpServletRequest getRequest() {
        return request;
    }

    /**
     * Returns the mock for {@link HttpServletResponse}.
     * 
     * @return the mock for {@link HttpServletResponse}
     */
    public MockHttpServletResponse getResponse() {
        return response;
    }
}