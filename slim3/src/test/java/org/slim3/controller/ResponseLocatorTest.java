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
package org.slim3.controller;

import junit.framework.TestCase;

import org.slim3.controller.ResponseLocator;
import org.slim3.tester.MockHttpServletResponse;

/**
 * @author higa
 * 
 */
public class ResponseLocatorTest extends TestCase {

    @Override
    protected void tearDown() throws Exception {
        ResponseLocator.setResponse(null);
    }

    /**
     * @throws Exception
     * 
     */
    public void test() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        ResponseLocator.setResponse(response);
        assertSame(response, ResponseLocator.getResponse());
    }
}