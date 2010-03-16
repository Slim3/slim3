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
public class MockHttpSessionTest {

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpSession session = new MockHttpSession(servletContext);

    /**
     * @throws Exception
     * 
     */
    @Test
    public void attribute() throws Exception {
        Object value = "111";
        String name = "aaa";
        session.setAttribute(name, value);
        assertThat(session.getAttribute(name), is(value));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void setAttributeForNull() throws Exception {
        Object value = null;
        String name = "aaa";
        session.setAttribute(name, value);
        assertThat(session.getAttribute(name), is(nullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getAttributeNames() throws Exception {
        session.setAttribute("aaa", "");
        session.setAttribute("bbb", "");
        Enumeration<String> e = session.getAttributeNames();
        System.out.println(e.nextElement());
        System.out.println(e.nextElement());
        assertThat(e.hasMoreElements(), is(false));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void invalidate() throws Exception {
        assertTrue(session.isValid());
        session.invalidate();
        assertThat(session.isValid(), is(false));
    }
}