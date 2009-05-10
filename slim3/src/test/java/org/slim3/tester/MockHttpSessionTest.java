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

import java.util.Enumeration;

import org.slim3.tester.MockHttpSession;
import org.slim3.tester.MockServletContext;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class MockHttpSessionTest extends TestCase {

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpSession session = new MockHttpSession(servletContext);

    /**
     * @throws Exception
     * 
     */
    public void testAttribute() throws Exception {
        Object value = "111";
        String name = "aaa";
        session.setAttribute(name, value);
        assertSame(value, session.getAttribute(name));
    }

    /**
     * @throws Exception
     * 
     */
    public void testSetAttributeForNull() throws Exception {
        Object value = null;
        String name = "aaa";
        session.setAttribute(name, value);
        assertNull(session.getAttribute(name));
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetAttributeNames() throws Exception {
        session.setAttribute("aaa", "");
        session.setAttribute("bbb", "");
        Enumeration<String> e = session.getAttributeNames();
        System.out.println(e.nextElement());
        System.out.println(e.nextElement());
        assertFalse(e.hasMoreElements());
    }

    /**
     * @throws Exception
     * 
     */
    public void testInvalidate() throws Exception {
        assertTrue(session.isValid());
        session.invalidate();
        assertFalse(session.isValid());
    }
}