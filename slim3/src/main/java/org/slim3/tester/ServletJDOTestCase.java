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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slim3.util.RequestLocator;
import org.slim3.util.ResponseLocator;
import org.slim3.util.ServletContextLocator;

/**
 * A test case for Servlet and JDO.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class ServletJDOTestCase extends JDOTestCase {

    /**
     * The mock for {@link ServletContext}.
     */
    protected MockServletContext application;

    /**
     * The mock for {@link HttpServletRequest}.
     */
    protected MockHttpServletRequest request;

    /**
     * The mock for {@link HttpServletResponse}.
     */
    protected MockHttpServletResponse response;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        application = new MockServletContext();
        request = new MockHttpServletRequest(application);
        response = new MockHttpServletResponse();
        ServletContextLocator.set(application);
        RequestLocator.set(request);
        ResponseLocator.set(response);
    }

    @Override
    protected void tearDown() throws Exception {
        application = null;
        request = null;
        response = null;
        ServletContextLocator.set(null);
        RequestLocator.set(null);
        ResponseLocator.set(null);
        super.tearDown();
    }
}
