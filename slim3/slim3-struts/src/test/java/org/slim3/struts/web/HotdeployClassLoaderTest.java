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
package org.slim3.struts.web;

import org.slim3.commons.config.Configuration;
import org.slim3.commons.unit.CleanableTestCase;
import org.slim3.struts.unit.MockHttpServletRequest;
import org.slim3.struts.unit.MockServletContext;

/**
 * @author higa
 * 
 */
public class HotdeployClassLoaderTest extends CleanableTestCase {

    /**
     * 
     */
    protected static final String PACKAGE = "org/slim3/struts/web/";

    /**
     * 
     */
    protected static final String CONFIG_PATH = PACKAGE
            + "slim3_configuration.properties";

    /**
     * 
     */
    protected static final String ACTION_CLASS_NAME = "org.slim3.struts.web.action.IndexAction";

    /**
     * 
     */
    protected ClassLoader originalClassLoader;

    /**
     * 
     */
    protected HotdeployClassLoader hotClassLoader;

    /**
     * 
     */
    protected MockServletContext servletContext;

    /**
     * 
     */
    protected MockHttpServletRequest request;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Configuration.initialize(CONFIG_PATH);
        originalClassLoader = Thread.currentThread().getContextClassLoader();
        hotClassLoader = new HotdeployClassLoader(originalClassLoader);
        servletContext = new MockServletContext();
        request = new MockHttpServletRequest(servletContext, "/index.jsp");
        WebLocator.setRequest(request);
        HotdeployClassLoader.setCurrentInstance(hotClassLoader);
    }

    @Override
    protected void tearDown() throws Exception {
        HotdeployClassLoader.removeCurrentInstance();
        WebLocator.setRequest(null);
        super.tearDown();
    }

    /**
     * @throws Exception
     * 
     */
    public void testLoadClass() throws Exception {
        Class<?> clazz = hotClassLoader.loadClass(ACTION_CLASS_NAME);
        assertNotNull(clazz);
        assertSame(hotClassLoader, clazz.getClassLoader());
        assertSame(clazz, hotClassLoader.loadClass(ACTION_CLASS_NAME));
        assertNotNull(clazz.getPackage());
    }

    /**
     * @throws Exception
     */
    public void testGetCurrentInstance() throws Exception {
        assertNotNull(HotdeployClassLoader.getCurrentInstance());
    }

    /**
     * @throws Exception
     */
    public void testRemoveCurrentInstance() throws Exception {
        HotdeployClassLoader.removeCurrentInstance();
        assertNull(HotdeployClassLoader.getCurrentInstance());
    }

    /**
     * @throws Exception
     */
    public void testIsTarget() throws Exception {
        assertTrue(hotClassLoader.isTarget(ACTION_CLASS_NAME));
        assertFalse(hotClassLoader.isTarget(String.class.getName()));
    }
}
