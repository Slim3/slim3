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

import org.slim3.commons.unit.CleanableTestCase;
import org.slim3.mvc.MvcConstants;
import org.slim3.mvc.unit.controller.HelloController;

/**
 * @author higa
 * 
 */
public class MvcTesterTest extends CleanableTestCase {

    private MvcTester tester = new MvcTester();

    @Override
    protected void setUp() throws Exception {
        System.setProperty(
            MvcConstants.CONTROLLER_PACKAGE_KEY,
            "org.slim3.mvc.unit.controller");
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        System.clearProperty(MvcConstants.CONTROLLER_PACKAGE_KEY);
        super.tearDown();
    }

    /**
     * @throws Exception
     * 
     */
    public void testSetUp() throws Exception {
        tester.setUp();
        assertNotNull(tester.servletContext);
        assertNotNull(tester.servletConfig);
        assertNotNull(tester.filterConfig);
        assertNotNull(tester.frontController);
        assertNotNull(tester.request);
        assertNotNull(tester.response);
        assertNotNull(tester.filterChain);
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
        assertNull(tester.filterChain);
    }

    /**
     * @throws Exception
     * 
     */
    public void testSetPath() throws Exception {
        tester.setUp();
        tester.setPath("index.jsp");
        assertEquals("index.jsp", tester.request.getPathInfo());
    }

    /**
     * @throws Exception
     * 
     */
    public void testSetPathBeforSetUp() throws Exception {
        try {
            tester.setPath("index.jsp");
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     * 
     */
    public void testSetParameter() throws Exception {
        tester.setUp();
        tester.setParameter("aaa", "111");
        assertEquals("111", tester.request.getParameter("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testSetParameterForArray() throws Exception {
        String[] array = new String[] { "111" };
        tester.setUp();
        tester.setParameter("aaa", array);
        assertEquals(array, tester.request.getParameterValues("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testTestForForward() throws Exception {
        tester.setUp();
        tester.setPath("/");
        tester.test();
        assertFalse(tester.isRedirect());
        assertEquals("/index.jsp", tester.getNextPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testTestForRedirect() throws Exception {
        tester.setUp();
        tester.setPath("/redirect");
        tester.test();
        assertTrue(tester.isRedirect());
        assertEquals("http://www.google.com", tester.getNextPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testTestForJSP() throws Exception {
        tester.setUp();
        tester.setPath("/index.jsp");
        tester.test();
        assertFalse(tester.isRedirect());
        assertEquals("/index.jsp", tester.getNextPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetNextPathBeforTest() throws Exception {
        try {
            tester.getNextPath();
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetAttributge() throws Exception {
        tester.setUp();
        tester.setPath("/index.jsp");
        tester.test();
        tester.request.setAttribute("aaa", 1);
        Integer aaa = tester.getAttribute("aaa");
        assertEquals(new Integer(1), aaa);
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetSessionAttributge() throws Exception {
        tester.setUp();
        tester.setPath("/index.jsp");
        tester.test();
        tester.request.getSession().setAttribute("aaa", 1);
        Integer aaa = tester.getSessionAttribute("aaa");
        assertEquals(new Integer(1), aaa);
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetServletContextAttributge() throws Exception {
        tester.setUp();
        tester.setPath("/index.jsp");
        tester.test();
        tester.servletContext.setAttribute("aaa", 1);
        Integer aaa = tester.getServletContextAttribute("aaa");
        assertEquals(new Integer(1), aaa);
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetController() throws Exception {
        tester.setUp();
        tester.setPath("/hello");
        tester.test();
        HelloController controller = tester.getController();
        assertNotNull(controller);
    }
}