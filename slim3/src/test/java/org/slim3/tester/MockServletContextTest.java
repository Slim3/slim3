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

import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.RequestDispatcher;

import org.junit.Test;

/**
 * @author higa
 * 
 */
public class MockServletContextTest {

    private MockServletContext servletContext = new MockServletContext();

    /**
     * 
     */
    @Test
    public void resourcePaths() {
        Set<String> paths = new HashSet<String>();
        paths.add("hoge");
        String path = "aaa";
        servletContext.addResourcePaths(path, paths);
        assertThat(servletContext.getResourcePaths(path), is(paths));
        assertThat(servletContext.getResourcePaths("xxx").size(), is(0));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void resource() throws Exception {
        URL url = new URL("http://www.slim3.org/");
        String path = "aaa";
        servletContext.addResource(path, url);
        assertThat(servletContext.getResource(path), is(url));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void realPath() throws Exception {
        String realPath = "real/path";
        String path = "aaa";
        servletContext.addRealPath(path, realPath);
        assertThat(servletContext.getRealPath(path), is(realPath));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void attribute() throws Exception {
        Object value = "111";
        String name = "aaa";
        servletContext.setAttribute(name, value);
        assertThat(servletContext.getAttribute(name), is(value));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void setAttributeForNull() throws Exception {
        Object value = null;
        String name = "aaa";
        servletContext.setAttribute(name, value);
        assertThat(servletContext.getAttribute(name), is(nullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getAttributeNames() throws Exception {
        servletContext.setAttribute("aaa", "");
        servletContext.setAttribute("bbb", "");
        Enumeration<String> e = servletContext.getAttributeNames();
        System.out.println(e.nextElement());
        System.out.println(e.nextElement());
        assertThat(e.hasMoreElements(), is(false));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void initParameter() throws Exception {
        String value = "111";
        String name = "aaa";
        servletContext.setInitParameter(name, value);
        assertThat(servletContext.getInitParameter(name), is(value));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void setInitParameterForNull() throws Exception {
        String value = null;
        String name = "aaa";
        servletContext.setInitParameter(name, value);
        assertThat(servletContext.getInitParameter(name), is(nullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getInitParameterNames() throws Exception {
        servletContext.setInitParameter("aaa", "");
        servletContext.setInitParameter("bbb", "");
        Enumeration<String> e = servletContext.getInitParameterNames();
        System.out.println(e.nextElement());
        System.out.println(e.nextElement());
        assertThat(e.hasMoreElements(), is(false));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getRequestDispatcher() throws Exception {
        RequestDispatcher dispatcher =
            servletContext.getRequestDispatcher("/hello/");
        assertThat(
            servletContext.getLatestRequestDispatcher(),
            is(sameInstance(dispatcher)));
    }
}
