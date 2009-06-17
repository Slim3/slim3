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

import java.math.BigDecimal;
import java.util.Date;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.slim3.controller.ControllerConstants;
import org.slim3.controller.validator.Errors;
import org.slim3.tester.controller.HelloController;
import org.slim3.util.RequestLocator;
import org.slim3.util.ResponseLocator;
import org.slim3.util.ServletContextLocator;
import org.slim3.util.TimeZoneLocator;

/**
 * @author higa
 * 
 */
public class ControllerTestCaseTest extends TestCase {

    private ControllerTestCase testCase = new MyTestCase();

    @Override
    protected void setUp() throws Exception {
        System.setProperty(
            ControllerConstants.CONTROLLER_PACKAGE_KEY,
            getClass().getPackage().getName() + ".controller");
        super.setUp();
        TimeZoneLocator.set(TimeZone.getTimeZone("UTC"));
    }

    @Override
    protected void tearDown() throws Exception {
        System.clearProperty(ControllerConstants.CONTROLLER_PACKAGE_KEY);
        ServletContextLocator.set(null);
        RequestLocator.set(null);
        ResponseLocator.set(null);
        TimeZoneLocator.set(null);
        super.tearDown();
    }

    /**
     * @throws Exception
     * 
     */
    public void testSetUp() throws Exception {
        testCase.setUp();
        assertNotNull(testCase.application);
        assertNotNull(testCase.config);
        assertNotNull(testCase.filterConfig);
        assertNotNull(testCase.frontController);
        assertNotNull(testCase.request);
        assertNotNull(testCase.response);
        assertNotNull(testCase.filterChain);
    }

    /**
     * @throws Exception
     * 
     */
    public void testTearDown() throws Exception {
        testCase.setUp();
        testCase.tearDown();
        assertNull(testCase.application);
        assertNull(testCase.config);
        assertNull(testCase.filterConfig);
        assertNull(testCase.frontController);
        assertNull(testCase.request);
        assertNull(testCase.response);
        assertNull(testCase.filterChain);
        assertNull(RequestLocator.get());
        assertNull(ResponseLocator.get());
    }

    /**
     * @throws Exception
     * 
     */
    public void testParam() throws Exception {
        testCase.setUp();
        testCase.param("aaa", "111");
        assertEquals("111", testCase.param("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testParamValues() throws Exception {
        String[] array = new String[] { "111" };
        testCase.setUp();
        testCase.paramValues("aaa", array);
        assertEquals(array, testCase.paramValues("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testRequestScope() throws Exception {
        testCase.setUp();
        Integer value = 1;
        testCase.requestScope("aaa", value);
        Integer returnValue = testCase.requestScope("aaa");
        assertEquals(value, returnValue);
        assertEquals(value, testCase.request.getAttribute("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testAsByte() throws Exception {
        testCase.setUp();
        testCase.request.setAttribute("aaa", "1");
        assertEquals(new Byte("1"), testCase.asByte("aaa"));
        assertEquals(new Byte("1"), testCase.asByte("aaa", "###"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testAsShort() throws Exception {
        testCase.setUp();
        testCase.request.setAttribute("aaa", "1");
        assertEquals(new Short("1"), testCase.asShort("aaa"));
        assertEquals(new Short("1"), testCase.asShort("aaa", "###"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testAsInteger() throws Exception {
        testCase.setUp();
        testCase.request.setAttribute("aaa", "1");
        assertEquals(new Integer("1"), testCase.asInteger("aaa"));
        assertEquals(new Integer("1"), testCase.asInteger("aaa", "###"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testAsLong() throws Exception {
        testCase.setUp();
        testCase.request.setAttribute("aaa", "1");
        assertEquals(new Long("1"), testCase.asLong("aaa"));
        assertEquals(new Long("1"), testCase.asLong("aaa", "###"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testAsFloat() throws Exception {
        testCase.setUp();
        testCase.request.setAttribute("aaa", "1");
        assertEquals(new Float("1"), testCase.asFloat("aaa"));
        assertEquals(new Float("1"), testCase.asFloat("aaa", "###"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testAsDouble() throws Exception {
        testCase.setUp();
        testCase.request.setAttribute("aaa", "1");
        assertEquals(new Double("1"), testCase.asDouble("aaa"));
        assertEquals(new Double("1"), testCase.asDouble("aaa", "###"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testAsBigDecimal() throws Exception {
        testCase.setUp();
        testCase.request.setAttribute("aaa", "1");
        assertEquals(new BigDecimal("1"), testCase.asBigDecimal("aaa"));
        assertEquals(new BigDecimal("1"), testCase.asBigDecimal("aaa", "###"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testAsDateForDate() throws Exception {
        testCase.setUp();
        testCase.request.setAttribute("aaa", "19700101");
        assertEquals(new Date(0), testCase.asDate("aaa", "yyyyMMdd"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testAsDateForTime() throws Exception {
        testCase.setUp();
        testCase.request.setAttribute("aaa", "000000");
        assertEquals(new Date(0), testCase.asDate("aaa", "hhmmss"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testSessionScope() throws Exception {
        testCase.setUp();
        Integer value = 1;
        testCase.sessionScope("aaa", value);
        Integer returnValue = testCase.sessionScope("aaa");
        assertEquals(value, returnValue);
        assertEquals(value, testCase.request.getSession().getAttribute("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testApplicationScope() throws Exception {
        testCase.setUp();
        Integer value = 1;
        testCase.applicationScope("aaa", value);
        Integer returnValue = testCase.applicationScope("aaa");
        assertEquals(value, returnValue);
        assertEquals(value, testCase.application.getAttribute("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testStartForPath() throws Exception {
        testCase.setUp();
        testCase.start("/");
        assertEquals("/", testCase.request.getServletPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testStartForBadPath() throws Exception {
        testCase.setUp();
        try {
            testCase.start("xxx");
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     * 
     */
    public void testStartForForward() throws Exception {
        testCase.setUp();
        testCase.start("/");
        assertFalse(testCase.isRedirect());
        assertEquals("/index.jsp", testCase.getNextPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testStartForRedirect() throws Exception {
        testCase.setUp();
        testCase.start("/redirect");
        assertTrue(testCase.isRedirect());
        assertEquals("http://www.google.com", testCase.getNextPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testStartForJSP() throws Exception {
        testCase.setUp();
        testCase.start("/index.jsp");
        assertFalse(testCase.isRedirect());
        assertEquals("/index.jsp", testCase.getNextPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetNextPathBeforStarting() throws Exception {
        testCase.setUp();
        try {
            testCase.getNextPath();
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
        testCase.setUp();
        testCase.start("/hello");
        HelloController controller = testCase.getController();
        assertNotNull(controller);
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetControllerBeforStarting() throws Exception {
        testCase.setUp();
        try {
            testCase.getController();
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetErrors() throws Exception {
        testCase.setUp();
        testCase.request.setAttribute(
            ControllerConstants.ERRORS_KEY,
            new Errors());
        Errors errors = testCase.getErrors();
        assertNotNull(errors);
    }

    private static class MyTestCase extends ControllerTestCase {

    }
}