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
package org.slim3.controller;

import junit.framework.TestCase;

import org.slim3.exception.HotReloadingRuntimeException;
import org.slim3.tester.MockServletContext;
import org.slim3.util.ServletContextLocator;

/**
 * @author higa
 * 
 */
public class HotReloadingFilterTest extends TestCase {

    private HotReloadingFilter filter = new HotReloadingFilter();

    @Override
    protected void tearDown() throws Exception {
        ServletContextLocator.set(null);
        super.tearDown();
    }

    /**
     * @throws Exception
     * 
     */
    public void testHotReloading() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        servletContext.setServerInfo("Development");
        filter.servletContext = servletContext;
        filter.initHotReloading();
        assertTrue(filter.hotReloading);
        assertTrue(ServletContextLocator.get() instanceof HotServletContextWrapper);
    }

    /**
     * @throws Exception
     * 
     */
    public void testRootPackage() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        filter.servletContext = servletContext;
        servletContext.setInitParameter(
            ControllerConstants.ROOT_PACKAGE_KEY,
            "aaa");
        filter.initRootPackageName();
        assertEquals("aaa", filter.rootPackageName);
    }

    /**
     * @throws Exception
     * 
     */
    public void testRootPackageNotFound() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        filter.servletContext = servletContext;
        try {
            filter.initRootPackageName();
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     * 
     */
    public void testCoolPackage() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        filter.servletContext = servletContext;
        servletContext.setInitParameter(
            ControllerConstants.COOL_PACKAGE_KEY,
            "aaa");
        filter.initCoolPackageName();
        assertEquals("aaa", filter.coolPackageName);
    }

    /**
     * @throws Exception
     * 
     */
    public void testCoolPackageForNotFound() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        filter.servletContext = servletContext;
        filter.initCoolPackageName();
        assertEquals("cool", filter.coolPackageName);
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateHotReloadingRuntimeException() throws Exception {
        filter.rootPackageName = "tutorial";
        filter.coolPackageName = "cool";
        HotReloadingRuntimeException e =
            filter.createHotReloadingRuntimeException(new Exception("cause"));
        System.out.println(e.getMessage());
    }
}