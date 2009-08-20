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

/**
 * @author higa
 * 
 */
public class RequestUtilTest extends TestCase {

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request =
        new MockHttpServletRequest(servletContext);

    /**
     * @throws Exception
     */
    public void testGetPath() throws Exception {
        request.setServletPath("/aaa");
        assertEquals("/aaa", RequestUtil.getPath(request));
    }

    /**
     * @throws Exception
     */
    public void testGetExtension() throws Exception {
        assertEquals("html", RequestUtil.getExtension("aaa.html"));
        assertEquals("html", RequestUtil.getExtension("/aaa.html"));
        assertNull(RequestUtil.getExtension("/aaa"));
        assertNull(RequestUtil.getExtension("/aaa.bbb/ccc"));
    }
}
