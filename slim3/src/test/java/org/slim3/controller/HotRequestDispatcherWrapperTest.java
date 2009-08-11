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

import org.slim3.tester.MockHttpServletRequest;
import org.slim3.tester.MockHttpServletResponse;
import org.slim3.tester.MockRequestDispatcher;
import org.slim3.tester.MockServletContext;

/**
 * @author higa
 * 
 */
public class HotRequestDispatcherWrapperTest extends TestCase {

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request =
        new MockHttpServletRequest(servletContext);

    private MockHttpServletResponse response = new MockHttpServletResponse();

    private HotHttpServletRequestWrapper requestWrapper =
        new HotHttpServletRequestWrapper(request);

    private MockRequestDispatcher dispatcher =
        new MockRequestDispatcher("/index.jsp");

    private HotRequestDispatcherWrapper dispatcherWrapper =
        new HotRequestDispatcherWrapper(dispatcher);

    /**
     * @throws Exception
     * 
     */
    public void testGetOriginal() throws Exception {
        assertSame(request, dispatcherWrapper.getOriginalRequest(request));
        assertSame(request, dispatcherWrapper
            .getOriginalRequest(requestWrapper));
    }

    /**
     * @throws Exception
     * 
     */
    public void testForward() throws Exception {
        dispatcherWrapper.forward(request, response);
        assertSame(request, dispatcher.getRequest());
        assertSame(response, dispatcher.getResponse());
    }

    /**
     * @throws Exception
     * 
     */
    public void testForwardForWrapper() throws Exception {
        dispatcherWrapper.forward(requestWrapper, response);
        assertSame(request, dispatcher.getRequest());
        assertSame(response, dispatcher.getResponse());
    }

    /**
     * @throws Exception
     * 
     */
    public void testInclude() throws Exception {
        dispatcherWrapper.include(request, response);
        assertSame(request, dispatcher.getRequest());
        assertSame(response, dispatcher.getResponse());
    }

    /**
     * @throws Exception
     * 
     */
    public void testIncludeForWrapper() throws Exception {
        dispatcherWrapper.include(requestWrapper, response);
        assertSame(request, dispatcher.getRequest());
        assertSame(response, dispatcher.getResponse());
    }
}