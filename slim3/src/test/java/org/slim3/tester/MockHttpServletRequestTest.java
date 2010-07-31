/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Enumeration;

import org.junit.Test;

/**
 * @author higa
 * 
 */
public class MockHttpServletRequestTest {

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request =
        new MockHttpServletRequest(servletContext);

    /**
     * @throws Exception
     */
    @Test
    public void addParameter() throws Exception {
        request.addParameter("aaa", "111");
        String[] values = request.getParameterValues("aaa");
        assertThat(values.length, is(1));
        assertThat(values[0], is("111"));
        request.addParameter("aaa", "222");
        values = request.getParameterValues("aaa");
        assertThat(values.length, is(2));
        assertThat(values[0], is("111"));
        assertThat(values[1], is("222"));
        request.addParameter("aaa", new String[] { "333", "444" });
        values = request.getParameterValues("aaa");
        assertThat(values.length, is(4));
        assertThat(values[0], is("111"));
        assertThat(values[1], is("222"));
        assertThat(values[2], is("333"));
        assertThat(values[3], is("444"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void setParameter() throws Exception {
        request.addParameter("aaa", "111");
        request.setParameter("aaa", new String[] { "222", "333" });
        String[] values = request.getParameterValues("aaa");
        assertThat(values.length, is(2));
        assertThat(values[0], is("222"));
        assertThat(values[1], is("333"));
        request.setParameter("aaa", "444");
        values = request.getParameterValues("aaa");
        assertThat(values.length, is(1));
        assertThat(values[0], is("444"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void attribute() throws Exception {
        Object value = "111";
        String name = "aaa";
        request.setAttribute(name, value);
        assertThat(request.getAttribute(name), is(sameInstance(value)));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void setAttributeForNull() throws Exception {
        Object value = null;
        String name = "aaa";
        request.setAttribute(name, value);
        assertThat(request.getAttribute(name), is(nullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getAttributeNames() throws Exception {
        request.setAttribute("aaa", "");
        request.setAttribute("bbb", "");
        Enumeration<String> e = request.getAttributeNames();
        System.out.println(e.nextElement());
        System.out.println(e.nextElement());
        assertThat(e.hasMoreElements(), is(false));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void header() throws Exception {
        request.setHeader("aaa", "111");
        assertThat(request.getHeader("aaa"), is("111"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void intHeader() throws Exception {
        request.setIntHeader("aaa", 1);
        assertThat(request.getHeader("aaa"), is("1"));
        assertThat(request.getIntHeader("aaa"), is(1));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void dateHeader() throws Exception {
        request.setDateHeader("aaa", 1000);
        System.out.println(request.getHeader("aaa"));
        assertThat(request.getDateHeader("aaa"), is(1000L));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void defaultMethod() throws Exception {
        assertThat(request.getMethod(), is("get"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void setQueryString() throws Exception {
        request.setQueryString("aaa=1&bbb=&ccc");
        assertThat(request.getParameter("aaa"), is("1"));
        assertThat(request.getParameter("bbb"), is(""));
        assertThat(request.getParameter("ccc"), is(""));
    }
}