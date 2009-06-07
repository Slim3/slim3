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
public class ControllerTest extends TestCase {

    private IndexController controller = new IndexController();

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request =
        new MockHttpServletRequest(servletContext);

    @Override
    protected void setUp() throws Exception {
        controller.application = servletContext;
        controller.request = request;
    }

    /**
     * @throws Exception
     * 
     */
    public void testForward() throws Exception {
        Navigation nav = controller.forward("index.jsp");
        assertEquals("index.jsp", nav.getPath());
        assertFalse(nav.isRedirect());
    }

    /**
     * @throws Exception
     * 
     */
    public void testForwardForOtherController() throws Exception {
        Navigation nav = controller.forward("/hello/index");
        assertEquals("/hello/index", nav.getPath());
        assertFalse(nav.isRedirect());
    }

    /**
     * @throws Exception
     * 
     */
    public void testRedirect() throws Exception {
        Navigation nav = controller.redirect("index");
        assertEquals("index", nav.getPath());
        assertTrue(nav.isRedirect());
    }

    /**
     * @throws Exception
     * 
     */
    public void testParam() throws Exception {
        request.setParameter("aaa", "111");
        assertEquals("111", controller.param("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testParamValues() throws Exception {
        String[] array = new String[] { "111" };
        request.setParameter("aaa", array);
        assertEquals(array, controller.paramValues("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testRequestScope() throws Exception {
        Integer value = 1;
        controller.requestScope("aaa", value);
        Integer returnValue = controller.requestScope("aaa");
        assertEquals(value, returnValue);
        assertEquals(value, request.getAttribute("aaa"));
        returnValue = controller.removeRequestScope("aaa");
        assertEquals(value, returnValue);
        assertNull(request.getAttribute("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testSessionScope() throws Exception {
        Integer value = 1;
        controller.sessionScope("aaa", value);
        Integer returnValue = controller.sessionScope("aaa");
        assertEquals(value, returnValue);
        assertEquals(value, request.getSession().getAttribute("aaa"));
        returnValue = controller.removeSessionScope("aaa");
        assertEquals(value, returnValue);
        assertNull(request.getSession().getAttribute("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testApplicationScope() throws Exception {
        Integer value = 1;
        controller.applicationScope("aaa", value);
        Integer returnValue = controller.applicationScope("aaa");
        assertEquals(value, returnValue);
        assertEquals(value, servletContext.getAttribute("aaa"));
        returnValue = controller.removeApplicationScope("aaa");
        assertEquals(value, returnValue);
        assertNull(servletContext.getAttribute("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testIsDevelopment() throws Exception {
        assertFalse(controller.isDevelopment());
        servletContext.setServerInfo("Development");
        assertTrue(controller.isDevelopment());
    }

    private static class IndexController extends Controller {

        @Override
        public Navigation run() {
            return null;
        }
    }
}