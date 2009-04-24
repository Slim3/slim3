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

import java.util.Enumeration;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class MockHttpServletRequestTest extends TestCase {

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request = new MockHttpServletRequest(
            servletContext);

    /**
     * @throws Exception
     */
    public void testAddParameter() throws Exception {
        request.addParameter("aaa", "111");
        String[] values = request.getParameterValues("aaa");
        assertEquals(1, values.length);
        assertEquals("111", values[0]);
        request.addParameter("aaa", "222");
        values = request.getParameterValues("aaa");
        assertEquals(2, values.length);
        assertEquals("111", values[0]);
        assertEquals("222", values[1]);
        request.addParameter("aaa", new String[] { "333", "444" });
        values = request.getParameterValues("aaa");
        assertEquals(4, values.length);
        assertEquals("111", values[0]);
        assertEquals("222", values[1]);
        assertEquals("333", values[2]);
        assertEquals("444", values[3]);
    }

    /**
     * @throws Exception
     */
    public void testSetParameter() throws Exception {
        request.addParameter("aaa", "111");
        request.setParameter("aaa", new String[] { "222", "333" });
        String[] values = request.getParameterValues("aaa");
        assertEquals(2, values.length);
        assertEquals("222", values[0]);
        assertEquals("333", values[1]);
        request.setParameter("aaa", "444");
        values = request.getParameterValues("aaa");
        assertEquals(1, values.length);
        assertEquals("444", values[0]);
    }

    /**
     * @throws Exception
     * 
     */
    public void testAttribute() throws Exception {
        Object value = "111";
        String name = "aaa";
        request.setAttribute(name, value);
        assertSame(value, request.getAttribute(name));
    }

    /**
     * @throws Exception
     * 
     */
    public void testSetAttributeForNull() throws Exception {
        Object value = null;
        String name = "aaa";
        request.setAttribute(name, value);
        assertNull(request.getAttribute(name));
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetAttributeNames() throws Exception {
        request.setAttribute("aaa", "");
        request.setAttribute("bbb", "");
        Enumeration<String> e = request.getAttributeNames();
        System.out.println(e.nextElement());
        System.out.println(e.nextElement());
        assertFalse(e.hasMoreElements());
    }

    /**
     * @throws Exception
     * 
     */
    public void testHeader() throws Exception {
        request.setHeader("aaa", "111");
        assertEquals("111", request.getHeader("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testIntHeader() throws Exception {
        request.setIntHeader("aaa", 1);
        assertEquals("1", request.getHeader("aaa"));
        assertEquals(1, request.getIntHeader("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testDateHeader() throws Exception {
        request.setDateHeader("aaa", 1000);
        System.out.println(request.getHeader("aaa"));
        assertEquals(1000, request.getDateHeader("aaa"));
    }
}