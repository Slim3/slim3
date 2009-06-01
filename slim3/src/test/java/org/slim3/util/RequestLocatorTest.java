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
package org.slim3.util;

import junit.framework.TestCase;

import org.slim3.tester.MockHttpServletRequest;
import org.slim3.tester.MockServletContext;
import org.slim3.util.RequestLocator;

/**
 * @author higa
 * 
 */
public class RequestLocatorTest extends TestCase {

    @Override
    protected void tearDown() throws Exception {
        RequestLocator.set(null);
    }

    /**
     * @throws Exception
     * 
     */
    public void test() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest request =
            new MockHttpServletRequest(servletContext);
        RequestLocator.set(request);
        assertSame(request, RequestLocator.get());
    }
}