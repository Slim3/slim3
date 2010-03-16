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
package org.slim3.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;
import org.slim3.tester.MockServletContext;
import org.slim3.util.ServletContextLocator;

/**
 * @author higa
 * 
 */
public class HotReloadingFilterTest {

    private HotReloadingFilter filter = new HotReloadingFilter();

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        ServletContextLocator.set(null);
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void hotReloading() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        System.setProperty(
            "com.google.appengine.runtime.environment",
            "Development");
        try {
            filter.servletContext = servletContext;
            filter.initHotReloading();
            assertThat(filter.hotReloading, is(true));
            assertThat(
                ServletContextLocator.get(),
                is(HotServletContextWrapper.class));
        } finally {
            System.clearProperty("com.google.appengine.runtime.environment");
        }

    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void rootPackage() throws Exception {
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
    @Test(expected = IllegalStateException.class)
    public void rootPackageWhenRootPackageNameIsNotFound() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        filter.servletContext = servletContext;
        filter.initRootPackageName();
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void coolPackage() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        filter.servletContext = servletContext;
        servletContext.setInitParameter(
            ControllerConstants.COOL_PACKAGE_KEY,
            "aaa");
        filter.initCoolPackageName();
        assertThat(filter.coolPackageName, is("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void coolPackageWhenCoolPackageNameIsNotFound() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        filter.servletContext = servletContext;
        filter.initCoolPackageName();
        assertThat(filter.coolPackageName, is("cool"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void createHotReloadingRuntimeException() throws Exception {
        filter.rootPackageName = "tutorial";
        filter.coolPackageName = "cool";
        HotReloadingRuntimeException e =
            filter.createHotReloadingRuntimeException(new Exception("cause"));
        System.out.println(e.getMessage());
    }
}