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

/**
 * @author higa
 * 
 */
public class RequestHandlerTest extends TestCase {

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request =
        new MockHttpServletRequest(servletContext);

    /**
     * @throws Exception
     * 
     */
    public void testHandle() throws Exception {
        request.setParameter("aaa", "111");
        request.setParameter("bbbArray", new String[] { "222" });
        RequestHandler handler = new RequestHandler(request);
        handler.handle();
        assertEquals("111", request.getAttribute("aaa"));
        String[] bbbArray = (String[]) request.getAttribute("bbbArray");
        assertNotNull(bbbArray);
        assertEquals(1, bbbArray.length);
        assertEquals("222", bbbArray[0]);
    }
}