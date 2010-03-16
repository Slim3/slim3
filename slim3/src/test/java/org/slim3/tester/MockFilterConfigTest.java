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
public class MockFilterConfigTest {

    private MockServletContext servletContext = new MockServletContext();

    private MockFilterConfig filterConfig =
        new MockFilterConfig(servletContext);

    /**
     * @throws Exception
     * 
     */
    @Test
    public void initParameter() throws Exception {
        String value = "111";
        String name = "aaa";
        filterConfig.setInitParameter(name, value);
        assertThat(filterConfig.getInitParameter(name), is(sameInstance(value)));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void setInitParameterForNull() throws Exception {
        String value = null;
        String name = "aaa";
        filterConfig.setInitParameter(name, value);
        assertThat(filterConfig.getInitParameter(name), is(nullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getInitParameterNames() throws Exception {
        filterConfig.setInitParameter("aaa", "");
        filterConfig.setInitParameter("bbb", "");
        Enumeration<String> e = filterConfig.getInitParameterNames();
        System.out.println(e.nextElement());
        System.out.println(e.nextElement());
        assertThat(e.hasMoreElements(), is(false));
    }
}
