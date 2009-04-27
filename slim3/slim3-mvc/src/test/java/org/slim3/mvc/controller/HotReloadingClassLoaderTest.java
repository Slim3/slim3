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

import org.slim3.commons.config.Configuration;
import org.slim3.commons.unit.CleanableTestCase;

/**
 * @author higa
 * 
 */
public class HotReloadingClassLoaderTest extends CleanableTestCase {

    /**
     * 
     */
    protected static final String PACKAGE = "org/slim3/mvc/controller/";

    /**
     * 
     */
    protected static final String CONFIG_PATH = PACKAGE
            + "slim3_configuration.properties";

    /**
     * 
     */
    protected static final String CONTROLLER_CLASS_NAME = "org.slim3.mvc.controller.controller.HogeController";

    /**
     * 
     */
    protected ClassLoader originalClassLoader;

    /**
     * 
     */
    protected HotReloadingClassLoader hotClassLoader;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Configuration.initialize(CONFIG_PATH);
        originalClassLoader = Thread.currentThread().getContextClassLoader();
        hotClassLoader = new HotReloadingClassLoader(originalClassLoader);
    }

    @Override
    protected void tearDown() throws Exception {
        originalClassLoader = null;
        hotClassLoader = null;
        super.tearDown();
    }

    /**
     * @throws Exception
     * 
     */
    public void testLoadClass() throws Exception {
        Class<?> clazz = hotClassLoader.loadClass(CONTROLLER_CLASS_NAME);
        assertNotNull(clazz);
        assertSame(hotClassLoader, clazz.getClassLoader());
        assertSame(clazz, hotClassLoader.loadClass(CONTROLLER_CLASS_NAME));
        assertNotNull(clazz.getPackage());
    }

    /**
     * @throws Exception
     */
    public void testIsTarget() throws Exception {
        assertTrue(hotClassLoader.isTarget(CONTROLLER_CLASS_NAME));
        assertFalse(hotClassLoader.isTarget(String.class.getName()));
    }
}
