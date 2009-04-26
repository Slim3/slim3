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
            assertNotNull(tester.servletContext);
            assertNotNull(tester.servletConfig);
            assertNotNull(tester.filterConfig);
            assertNotNull(tester.frontController);
            assertNotNull(tester.request);
            assertNotNull(tester.response);
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
        assertNull(tester.servletContext);
        assertNull(tester.servletConfig);
        assertNull(tester.filterConfig);
        assertNull(tester.frontController);
        assertNull(tester.request);
        assertNull(tester.response);
    }
}