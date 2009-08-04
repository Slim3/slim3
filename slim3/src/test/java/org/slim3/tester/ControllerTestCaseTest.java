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

import org.slim3.controller.ControllerConstants;
import org.slim3.controller.validator.Errors;
import org.slim3.tester.controller.HelloController;
import org.slim3.util.TimeZoneLocator;

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
        TimeZoneLocator.set(TimeZone.getTimeZone("UTC"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testParam() throws Exception {
        param("aaa", "111");
        assertEquals("111", param("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testParamValues() throws Exception {
        String[] array = new String[] { "111" };
        paramValues("aaa", array);
        assertEquals(array, paramValues("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testRequestScope() throws Exception {
        Integer value = 1;
        requestScope("aaa", value);
        Integer returnValue = requestScope("aaa");
        assertEquals(value, returnValue);
        assertEquals(value, request.getAttribute("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testAsByte() throws Exception {
        request.setAttribute("aaa", "1");
        assertEquals(new Byte("1"), asByte("aaa"));
        assertEquals(new Byte("1"), asByte("aaa", "###"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testAsShort() throws Exception {
        request.setAttribute("aaa", "1");
        assertEquals(new Short("1"), asShort("aaa"));
        assertEquals(new Short("1"), asShort("aaa", "###"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testAsInteger() throws Exception {
        request.setAttribute("aaa", "1");
        assertEquals(new Integer("1"), asInteger("aaa"));
        assertEquals(new Integer("1"), asInteger("aaa", "###"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testAsLong() throws Exception {
        request.setAttribute("aaa", "1");
        assertEquals(new Long("1"), asLong("aaa"));
        assertEquals(new Long("1"), asLong("aaa", "###"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testAsFloat() throws Exception {
        request.setAttribute("aaa", "1");
        assertEquals(new Float("1"), asFloat("aaa"));
        assertEquals(new Float("1"), asFloat("aaa", "###"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testAsDouble() throws Exception {
        request.setAttribute("aaa", "1");
        assertEquals(new Double("1"), asDouble("aaa"));
        assertEquals(new Double("1"), asDouble("aaa", "###"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testAsBigDecimal() throws Exception {
        request.setAttribute("aaa", "1");
        assertEquals(new BigDecimal("1"), asBigDecimal("aaa"));
        assertEquals(new BigDecimal("1"), asBigDecimal("aaa", "###"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testAsDateForDate() throws Exception {
        request.setAttribute("aaa", "19700101");
        assertEquals(new Date(0), asDate("aaa", "yyyyMMdd"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testAsDateForTime() throws Exception {
        request.setAttribute("aaa", "000000");
        assertEquals(new Date(0), asDate("aaa", "hhmmss"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testKey() throws Exception {
        request.setAttribute("key", "111");
        assertEquals("111", key());
    }

    /**
     * @throws Exception
     * 
     */
    public void testKeyForKeyNotFound() throws Exception {
        try {
            key();
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     * 
     */
    public void testVersion() throws Exception {
        request.setAttribute("version", "111");
        assertEquals(111, version());
    }

    /**
     * @throws Exception
     * 
     */
    public void testVersionForVersionNotFound() throws Exception {
        try {
            version();
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     * 
     */
    public void testSessionScope() throws Exception {
        Integer value = 1;
        sessionScope("aaa", value);
        Integer returnValue = sessionScope("aaa");
        assertEquals(value, returnValue);
        assertEquals(value, request.getSession().getAttribute("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testApplicationScope() throws Exception {
        Integer value = 1;
        applicationScope("aaa", value);
        Integer returnValue = applicationScope("aaa");
        assertEquals(value, returnValue);
        assertEquals(value, application.getAttribute("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testStartForPath() throws Exception {
        start("/");
        assertEquals("/", request.getServletPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testStartForBadPath() throws Exception {
        try {
            start("xxx");
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
        start("/");
        assertFalse(isRedirect());
        assertEquals("/index.jsp", getDestinationPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testStartForRedirect() throws Exception {
        start("/redirect");
        assertTrue(isRedirect());
        assertEquals("http://www.google.com", getDestinationPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testStartForJSP() throws Exception {
        start("/index.jsp");
        assertFalse(isRedirect());
        assertEquals("/index.jsp", getDestinationPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetDestinationPathBeforStarting() throws Exception {
        try {
            getDestinationPath();
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
        start("/hello");
        HelloController controller = getController();
        assertNotNull(controller);
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetControllerBeforStarting() throws Exception {
        try {
            getController();
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
        request.setAttribute(ControllerConstants.ERRORS_KEY, new Errors());
        Errors errors = getErrors();
        assertNotNull(errors);
    }
}