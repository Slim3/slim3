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

import org.slim3.controller.ControllerConstants;
import org.slim3.tester.ControllerTestCase;
import org.slim3.tester.controller.HelloController;

/**
 * @author higa
 * 
 */
public class ControllerTestCaseTest extends ControllerTestCase {

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
    public void testSetParameter() throws Exception {
        setParameter("aaa", "111");
        assertEquals("111", getParameter("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testSetParameterForArray() throws Exception {
        String[] array = new String[] { "111" };
        setParameter("aaa", array);
        assertEquals(array, getParameterValues("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testSetAttribute() throws Exception {
        Integer value = 1;
        setAttribute("aaa", value);
        Integer returnValue = getAttribute("aaa");
        assertEquals(value, returnValue);
        assertEquals(value, controllerTester.request.getAttribute("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testRemoveAttribute() throws Exception {
        Integer value = 1;
        setAttribute("aaa", value);
        Integer returnValue = removeAttribute("aaa");
        assertEquals(value, returnValue);
        assertNull(getAttribute("aaa"));
        assertNull(controllerTester.request.getAttribute("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testSetSessionAttribute() throws Exception {
        Integer value = 1;
        setSessionAttribute("aaa", value);
        Integer returnValue = getSessionAttribute("aaa");
        assertEquals(value, returnValue);
        assertEquals(value, controllerTester.request.getSession().getAttribute(
            "aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testRemoveSessionAttribute() throws Exception {
        Integer value = 1;
        setSessionAttribute("aaa", value);
        Integer returnValue = removeSessionAttribute("aaa");
        assertEquals(value, returnValue);
        assertNull(getSessionAttribute("aaa"));
        assertNull(controllerTester.request.getSession().getAttribute("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testSetServletContextAttribute() throws Exception {
        Integer value = 1;
        setServletContextAttribute("aaa", value);
        Integer returnValue = getServletContextAttribute("aaa");
        assertEquals(value, returnValue);
        assertEquals(value, controllerTester.servletContext.getAttribute("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testRemoveServletContextAttribute() throws Exception {
        Integer value = 1;
        setServletContextAttribute("aaa", value);
        Integer returnValue = removeServletContextAttribute("aaa");
        assertEquals(value, returnValue);
        assertNull(getServletContextAttribute("aaa"));
        assertNull(controllerTester.servletContext.getAttribute("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testStartForForward() throws Exception {
        start("/");
        assertFalse(isRedirect());
        assertEquals("/index.jsp", getNextPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testStartForRedirect() throws Exception {
        start("/redirect");
        assertTrue(isRedirect());
        assertEquals("http://www.google.com", getNextPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testStartForJSP() throws Exception {
        start("/index.jsp");
        assertFalse(isRedirect());
        assertEquals("/index.jsp", getNextPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetController() throws Exception {
        start("/hello");
        HelloController controller = getController();
        assertNotNull(controller);
    }
}