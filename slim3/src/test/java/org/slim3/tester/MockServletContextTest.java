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

import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.RequestDispatcher;

import org.slim3.tester.MockServletContext;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class MockServletContextTest extends TestCase {

    private MockServletContext servletContext = new MockServletContext();

    /**
     * 
     */
    public void testResourcePaths() {
        Set<String> paths = new HashSet<String>();
        paths.add("hoge");
        String path = "aaa";
        servletContext.addResourcePaths(path, paths);
        assertSame(paths, servletContext.getResourcePaths(path));
        assertEquals(0, servletContext.getResourcePaths("xxx").size());
    }

    /**
     * @throws Exception
     * 
     */
    public void testResource() throws Exception {
        URL url = new URL("http://www.slim3.org/");
        String path = "aaa";
        servletContext.addResource(path, url);
        assertSame(url, servletContext.getResource(path));
    }

    /**
     * @throws Exception
     * 
     */
    public void testRealPath() throws Exception {
        String realPath = "real/path";
        String path = "aaa";
        servletContext.addRealPath(path, realPath);
        assertSame(realPath, servletContext.getRealPath(path));
    }

    /**
     * @throws Exception
     * 
     */
    public void testAttribute() throws Exception {
        Object value = "111";
        String name = "aaa";
        servletContext.setAttribute(name, value);
        assertSame(value, servletContext.getAttribute(name));
    }

    /**
     * @throws Exception
     * 
     */
    public void testSetAttributeForNull() throws Exception {
        Object value = null;
        String name = "aaa";
        servletContext.setAttribute(name, value);
        assertNull(servletContext.getAttribute(name));
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetAttributeNames() throws Exception {
        servletContext.setAttribute("aaa", "");
        servletContext.setAttribute("bbb", "");
        Enumeration<String> e = servletContext.getAttributeNames();
        System.out.println(e.nextElement());
        System.out.println(e.nextElement());
        assertFalse(e.hasMoreElements());
    }

    /**
     * @throws Exception
     * 
     */
    public void testInitParameter() throws Exception {
        String value = "111";
        String name = "aaa";
        servletContext.setInitParameter(name, value);
        assertSame(value, servletContext.getInitParameter(name));
    }

    /**
     * @throws Exception
     * 
     */
    public void testSetInitParameterForNull() throws Exception {
        String value = null;
        String name = "aaa";
        servletContext.setInitParameter(name, value);
        assertNull(servletContext.getInitParameter(name));
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetInitParameterNames() throws Exception {
        servletContext.setInitParameter("aaa", "");
        servletContext.setInitParameter("bbb", "");
        Enumeration<String> e = servletContext.getInitParameterNames();
        System.out.println(e.nextElement());
        System.out.println(e.nextElement());
        assertFalse(e.hasMoreElements());
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetRequestDispatcher() throws Exception {
        RequestDispatcher dispatcher = servletContext
                .getRequestDispatcher("/hello/");
        assertSame(dispatcher, servletContext.getLatestRequestDispatcher());
    }
}
