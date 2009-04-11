/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.slim3.struts.unit;

import java.util.Enumeration;

import org.slim3.struts.unit.MockServletConfig;
import org.slim3.struts.unit.MockServletContext;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class MockServletConfigTest extends TestCase {

    private MockServletContext servletContext = new MockServletContext();

    private MockServletConfig servletConfig = new MockServletConfig(
            servletContext);

    /**
     * @throws Exception
     * 
     */
    public void testInitParameter() throws Exception {
        String value = "111";
        String name = "aaa";
        servletConfig.setInitParameter(name, value);
        assertSame(value, servletConfig.getInitParameter(name));
    }

    /**
     * @throws Exception
     * 
     */
    public void testSetInitParameterForNull() throws Exception {
        String value = null;
        String name = "aaa";
        servletConfig.setInitParameter(name, value);
        assertNull(servletConfig.getInitParameter(name));
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetInitParameterNames() throws Exception {
        servletConfig.setInitParameter("aaa", "");
        servletConfig.setInitParameter("bbb", "");
        Enumeration<String> e = servletConfig.getInitParameterNames();
        System.out.println(e.nextElement());
        System.out.println(e.nextElement());
        assertFalse(e.hasMoreElements());
    }
}
