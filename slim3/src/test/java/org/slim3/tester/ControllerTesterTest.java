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
package org.slim3.tester;

import junit.framework.TestCase;

import org.slim3.controller.ControllerConstants;
import org.slim3.controller.RequestLocator;
import org.slim3.controller.ResponseLocator;
import org.slim3.tester.ControllerTester;
import org.slim3.tester.controller.HelloController;

/**
 * @author higa
 * 
 */
public class ControllerTesterTest extends TestCase {

    private ControllerTester tester = new ControllerTester();

    @Override
    protected void setUp() throws Exception {
        System.setProperty(
            ControllerConstants.CONTROLLER_PACKAGE_KEY,
            getClass().getPackage().getName() + ".controller");
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        System.clearProperty(ControllerConstants.CONTROLLER_PACKAGE_KEY);
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
        assertNull(RequestLocator.getRequest());
        assertNull(ResponseLocator.getResponse());
    }

    /**
     * @throws Exception
     * 
     */
    public void testSetParameter() throws Exception {
        tester.setUp();
        tester.setParameter("aaa", "111");
        assertEquals("111", tester.getParameter("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testSetParameterForArray() throws Exception {
        String[] array = new String[] { "111" };
        tester.setUp();
        tester.setParameter("aaa", array);
        assertEquals(array, tester.getParameterValues("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testSetAttribute() throws Exception {
        tester.setUp();
        Integer value = 1;
        tester.setAttribute("aaa", value);
        Integer returnValue = tester.getAttribute("aaa");
        assertEquals(value, returnValue);
        assertEquals(value, tester.request.getAttribute("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testRemoveAttribute() throws Exception {
        tester.setUp();
        Integer value = 1;
        tester.setAttribute("aaa", value);
        Integer returnValue = tester.removeAttribute("aaa");
        assertEquals(value, returnValue);
        assertNull(tester.getAttribute("aaa"));
        assertNull(tester.request.getAttribute("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testSetSessionAttribute() throws Exception {
        tester.setUp();
        Integer value = 1;
        tester.setSessionAttribute("aaa", value);
        Integer returnValue = tester.getSessionAttribute("aaa");
        assertEquals(value, returnValue);
        assertEquals(value, tester.request.getSession().getAttribute("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testRemoveSessionAttribute() throws Exception {
        tester.setUp();
        Integer value = 1;
        tester.setSessionAttribute("aaa", value);
        Integer returnValue = tester.removeSessionAttribute("aaa");
        assertEquals(value, returnValue);
        assertNull(tester.getSessionAttribute("aaa"));
        assertNull(tester.request.getSession().getAttribute("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testSetServletContextAttribute() throws Exception {
        tester.setUp();
        Integer value = 1;
        tester.setServletContextAttribute("aaa", value);
        Integer returnValue = tester.getServletContextAttribute("aaa");
        assertEquals(value, returnValue);
        assertEquals(value, tester.servletContext.getAttribute("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testRemoveServletContextAttribute() throws Exception {
        tester.setUp();
        Integer value = 1;
        tester.setServletContextAttribute("aaa", value);
        Integer returnValue = tester.removeServletContextAttribute("aaa");
        assertEquals(value, returnValue);
        assertNull(tester.getServletContextAttribute("aaa"));
        assertNull(tester.servletContext.getAttribute("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testStartForPath() throws Exception {
        tester.setUp();
        tester.start("/");
        assertEquals("/", tester.request.getPathInfo());
    }

    /**
     * @throws Exception
     * 
     */
    public void testStartForBadPath() throws Exception {
        tester.setUp();
        try {
            tester.start("xxx");
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     * 
     */
    public void testStartBeforSetUp() throws Exception {
        try {
            tester.start("/");
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     * 
     */
    public void testStartForForward() throws Exception {
        tester.setUp();
        tester.start("/");
        assertFalse(tester.isRedirect());
        assertEquals("/index.jsp", tester.getNextPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testStartForRedirect() throws Exception {
        tester.setUp();
        tester.start("/redirect");
        assertTrue(tester.isRedirect());
        assertEquals("http://www.google.com", tester.getNextPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testStartForJSP() throws Exception {
        tester.setUp();
        tester.start("/index.jsp");
        assertFalse(tester.isRedirect());
        assertEquals("/index.jsp", tester.getNextPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetNextPathBeforStarting() throws Exception {
        tester.setUp();
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
    public void testGetController() throws Exception {
        tester.setUp();
        tester.start("/hello");
        HelloController controller = tester.getController();
        assertNotNull(controller);
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetControllerBeforStarting() throws Exception {
        tester.setUp();
        try {
            tester.getController();
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}