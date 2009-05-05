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
package org.slim3.mvc.controller;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.slim3.mvc.unit.MockHttpServletRequest;
import org.slim3.mvc.unit.MockServletContext;

/**
 * @author higa
 * 
 */
public class ControllerTest extends TestCase {

    private IndexController controller = new IndexController();

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request =
        new MockHttpServletRequest(servletContext);

    private Map<String, Object> parameters = new HashMap<String, Object>();

    @Override
    protected void setUp() throws Exception {
        controller.setServletContext(servletContext);
        controller.setRequest(request);
        controller.setParameters(parameters);
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
    public void testForwardForDefaultPath() throws Exception {
        Navigation nav = controller.forward();
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
    public void testSetPath() throws Exception {
        controller.setPath("/hello/list");
        assertEquals("/hello/", controller.getApplicationPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCalculateDefaultPath() throws Exception {
        assertEquals("index.jsp", controller.calculateDefaultPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCalculateDefaultPathForBadControllerClass()
            throws Exception {
        try {
            new BadControl().calculateDefaultPath();
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testToBoolean() throws Exception {
        assertEquals(Boolean.TRUE, controller.toBoolean("true"));
    }

    /**
     * @throws Exception
     */
    public void testToByte() throws Exception {
        assertEquals(Byte.valueOf("1"), controller.toByte("1"));
    }

    /**
     * @throws Exception
     */
    public void testToShort() throws Exception {
        assertEquals(Short.valueOf("1"), controller.toShort("1"));
    }

    /**
     * @throws Exception
     */
    public void testToInteger() throws Exception {
        assertEquals(Integer.valueOf(1), controller.toInteger("1"));
    }

    /**
     * @throws Exception
     */
    public void testToLong() throws Exception {
        assertEquals(Long.valueOf(1), controller.toLong("1"));
    }

    /**
     * @throws Exception
     */
    public void testFloat() throws Exception {
        assertEquals(Float.valueOf(1), controller.toFloat("1"));
    }

    /**
     * @throws Exception
     */
    public void testDouble() throws Exception {
        assertEquals(Double.valueOf(1), controller.toDouble("1"));
    }

    /**
     * @throws Exception
     */
    public void testDate() throws Exception {
        assertEquals(new java.util.Date(1), controller
            .toDate(new java.sql.Date(1)));
    }

    /**
     * @throws Exception
     */
    public void testToDateAndClearTimePart() throws Exception {
        java.util.Date date = new java.util.Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        assertEquals(cal.getTime(), controller.toDateAndClearTimePart(date));
    }

    /**
     * @throws Exception
     */
    public void testToDateAndClearDatePart() throws Exception {
        java.util.Date date = new java.util.Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.YEAR, 1970);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        assertEquals(cal.getTime(), controller.toDateAndClearDatePart(date));
    }

    /**
     * @throws Exception
     */
    public void testDateForString() throws Exception {
        assertEquals(java.util.Date.class, controller.toDate(
            "01/01/1970 00:00:00",
            "MM/dd/yyyy").getClass());
    }

    /**
     * @throws Exception
     */
    public void testGetAttribute() throws Exception {
        request.setAttribute("aaa", 1);
        Integer aaa = controller.getAttribute("aaa");
        assertEquals(new Integer(1), aaa);
    }

    /**
     * @throws Exception
     */
    public void testSetAttribute() throws Exception {
        controller.setAttribute("aaa", 1);
        Integer aaa = controller.getAttribute("aaa");
        assertEquals(new Integer(1), aaa);
    }

    /**
     * @throws Exception
     */
    public void testRemoveAttribute() throws Exception {
        controller.setAttribute("aaa", 1);
        Integer aaa = controller.removeAttribute("aaa");
        assertEquals(new Integer(1), aaa);
        assertNull(controller.getAttribute("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testGetSessionAttribute() throws Exception {
        request.getSession().setAttribute("aaa", 1);
        Integer aaa = controller.getSessionAttribute("aaa");
        assertEquals(new Integer(1), aaa);
    }

    /**
     * @throws Exception
     */
    public void testSetSessionAttribute() throws Exception {
        controller.setSessionAttribute("aaa", 1);
        Integer aaa = controller.getSessionAttribute("aaa");
        assertEquals(new Integer(1), aaa);
    }

    /**
     * @throws Exception
     */
    public void testRemoveSessionAttribute() throws Exception {
        controller.setSessionAttribute("aaa", 1);
        Integer aaa = controller.removeSessionAttribute("aaa");
        assertEquals(new Integer(1), aaa);
        assertNull(controller.getSessionAttribute("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testGetServletContextAttribute() throws Exception {
        servletContext.setAttribute("aaa", 1);
        Integer aaa = controller.getServletContextAttribute("aaa");
        assertEquals(new Integer(1), aaa);
    }

    /**
     * @throws Exception
     */
    public void testSetServletContextAttribute() throws Exception {
        controller.setServletContextAttribute("aaa", 1);
        Integer aaa = controller.getServletContextAttribute("aaa");
        assertEquals(new Integer(1), aaa);
    }

    /**
     * @throws Exception
     */
    public void testRemoveServletContextAttribute() throws Exception {
        controller.setServletContextAttribute("aaa", 1);
        Integer aaa = controller.removeServletContextAttribute("aaa");
        assertEquals(new Integer(1), aaa);
        assertNull(controller.getServletContextAttribute("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testGetParameter() throws Exception {
        parameters.put("aaa", new String[] { "111" });
        assertEquals("111", controller.getParameter("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testGetStringArrayParameter() throws Exception {
        Object value = new String[] { "111" };
        parameters.put("aaa", value);
        assertEquals(value, controller.getStringArrayParameter("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testGetStringArrayParameterForByteArray() throws Exception {
        Object value = new byte[] { (byte) 1 };
        parameters.put("aaa", value);
        try {
            controller.getStringArrayParameter("aaa");
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testGetByteArrayParameter() throws Exception {
        Object value = new byte[] { (byte) 1 };
        parameters.put("aaa", value);
        assertEquals(value, controller.getByteArrayParameter("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testGetByteArrayParameterForStringArray() throws Exception {
        Object value = new String[] { "111" };
        parameters.put("aaa", value);
        try {
            controller.getByteArrayParameter("aaa");
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private static class IndexController extends Controller {

        @Override
        public Navigation execute() {
            return null;
        }
    }

    private static class BadControl extends Controller {

        @Override
        public Navigation execute() {
            return null;
        }
    }
}