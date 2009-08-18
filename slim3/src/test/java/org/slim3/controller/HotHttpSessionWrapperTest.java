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
import org.slim3.tester.MockServletContext;
import org.slim3.util.Cleaner;

/**
 * @author higa
 * 
 */
public class HotHttpSessionWrapperTest extends TestCase {

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request =
        new MockHttpServletRequest(servletContext);

    private HotHttpServletRequestWrapper requestWrapper =
        new HotHttpServletRequestWrapper(request);

    private HotHttpSessionWrapper sessionWrapper =
        (HotHttpSessionWrapper) requestWrapper.getSession();

    /**
     * @throws Exception
     * 
     */
    public void testCleanAndGetAttribute() throws Exception {
        sessionWrapper.setAttribute("aaa", "111");
        Cleaner.cleanAll();
        assertTrue(request.getSession().getAttribute("aaa") instanceof BytesHolder);
        Object value = sessionWrapper.getAttribute("aaa");
        assertEquals("111", value);
        assertSame(value, sessionWrapper.getAttribute("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testMultipleCleanAndGetAttribute() throws Exception {
        sessionWrapper.setAttribute("aaa", "111");
        Cleaner.cleanAll();
        requestWrapper = new HotHttpServletRequestWrapper(request);
        sessionWrapper = (HotHttpSessionWrapper) requestWrapper.getSession();
        Cleaner.cleanAll();
        assertTrue(request.getSession().getAttribute("aaa") instanceof BytesHolder);
        Object value = sessionWrapper.getAttribute("aaa");
        assertEquals("111", value);
        assertSame(value, sessionWrapper.getAttribute("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testInvalidate() throws Exception {
        sessionWrapper.invalidate();
        assertNull(sessionWrapper.originalSession);
        assertNull(sessionWrapper.requestWrapper);
        assertNull(requestWrapper.sessionWrapper);
        sessionWrapper.invalidate();
    }
}