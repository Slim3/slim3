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

import java.math.BigDecimal;
import java.util.Date;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.slim3.tester.MockHttpServletRequest;
import org.slim3.tester.MockHttpServletResponse;
import org.slim3.tester.MockServletContext;
import org.slim3.util.TimeZoneLocator;

/**
 * @author higa
 * 
 */
public class ControllerTest extends TestCase {

    private IndexController controller = new IndexController();

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request =
        new MockHttpServletRequest(servletContext);

    private MockHttpServletResponse response = new MockHttpServletResponse();

    @Override
    protected void setUp() throws Exception {
        controller.application = servletContext;
        controller.request = request;
        controller.response = response;
        TimeZoneLocator.set(TimeZone.getTimeZone("UTC"));
    }

    @Override
    protected void tearDown() throws Exception {
        TimeZoneLocator.set(null);
        super.tearDown();
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

    /**
     * @throws Exception
     * 
     */
    public void testDownload() throws Exception {
        controller.download("aaa.txt", new byte[] { 1 });
        byte[] bytes = response.getOutputAsByteArray();
        assertEquals(1, bytes.length);
        assertEquals(1, bytes[0]);
    }

    /**
     * @throws Exception
     * 
     */
    public void testAsByte() throws Exception {
        request.setAttribute("aaa", "1");
        assertEquals(new Byte("1"), controller.asByte("aaa"));
        assertEquals(new Byte("1"), controller.asByte("aaa", "###"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testAsShort() throws Exception {
        request.setAttribute("aaa", "1");
        assertEquals(new Short("1"), controller.asShort("aaa"));
        assertEquals(new Short("1"), controller.asShort("aaa", "###"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testAsInteger() throws Exception {
        request.setAttribute("aaa", "1");
        assertEquals(new Integer("1"), controller.asInteger("aaa"));
        assertEquals(new Integer("1"), controller.asInteger("aaa", "###"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testAsLong() throws Exception {
        request.setAttribute("aaa", "1");
        assertEquals(new Long("1"), controller.asLong("aaa"));
        assertEquals(new Long("1"), controller.asLong("aaa", "###"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testAsFloat() throws Exception {
        request.setAttribute("aaa", "1");
        assertEquals(new Float("1"), controller.asFloat("aaa"));
        assertEquals(new Float("1"), controller.asFloat("aaa", "###"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testAsDouble() throws Exception {
        request.setAttribute("aaa", "1");
        assertEquals(new Double("1"), controller.asDouble("aaa"));
        assertEquals(new Double("1"), controller.asDouble("aaa", "###"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testAsBigDecimal() throws Exception {
        request.setAttribute("aaa", "1");
        assertEquals(new BigDecimal("1"), controller.asBigDecimal("aaa"));
        assertEquals(new BigDecimal("1"), controller.asBigDecimal("aaa", "###"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testAsDateForDate() throws Exception {
        request.setAttribute("aaa", "19700101");
        assertEquals(new Date(0), controller.asDate("aaa", "yyyyMMdd"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testAsDateForTime() throws Exception {
        request.setAttribute("aaa", "000000");
        assertEquals(new Date(0), controller.asDate("aaa", "hhmmss"));
    }

    private static class IndexController extends Controller {

        @Override
        public Navigation run() {
            return null;
        }
    }
}