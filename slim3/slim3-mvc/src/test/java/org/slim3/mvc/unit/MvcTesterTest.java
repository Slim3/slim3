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

import junit.framework.TestCase;

import org.slim3.mvc.controller.RequestLocator;
import org.slim3.mvc.controller.ResponseLocator;
import org.slim3.mvc.controller.ServletContextLocator;

/**
 * @author higa
 * 
 */
public class MvcTesterTest extends TestCase {

    private MvcTester tester = new MvcTester();

    /**
     * @throws Exception
     * 
     */
    public void testSetUp() throws Exception {
        tester.setUp();
        try {
            assertNotNull(ServletContextLocator.getServletContext());
            assertNotNull(RequestLocator.getRequest());
            assertNotNull(ResponseLocator.getResponse());
            assertNotNull(tester.getServletContext());
            assertNotNull(tester.getRequest());
            assertNotNull(tester.getResponse());
        } finally {
            tester.tearDown();
        }
    }

    /**
     * @throws Exception
     * 
     */
    public void testTearDown() throws Exception {
        tester.setUp();
        tester.tearDown();
        assertNull(ServletContextLocator.getServletContext());
        assertNull(RequestLocator.getRequest());
        assertNull(ResponseLocator.getResponse());
        assertNull(tester.getServletContext());
        assertNull(tester.getRequest());
        assertNull(tester.getResponse());
    }
}