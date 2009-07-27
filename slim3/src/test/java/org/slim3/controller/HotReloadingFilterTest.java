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

import org.slim3.tester.MockServletContext;

/**
 * @author higa
 * 
 */
public class HotReloadingFilterTest extends TestCase {

    private HotReloadingFilter filter = new HotReloadingFilter();

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
    public void testStaticPackages() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        filter.servletContext = servletContext;
        servletContext.setInitParameter(
            ControllerConstants.STATIC_PACKAGES_KEY,
            "model, aaa");
        filter.initStaticPackageNames();
        assertEquals(2, filter.staticPackageNames.length);
        assertEquals("model", filter.staticPackageNames[0]);
        assertEquals("aaa", filter.staticPackageNames[1]);
    }

    /**
     * @throws Exception
     * 
     */
    public void testStaticPackagesForModelIsNotFound() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        filter.servletContext = servletContext;
        servletContext.setInitParameter(
            ControllerConstants.STATIC_PACKAGES_KEY,
            "aaa");
        try {
            filter.initStaticPackageNames();
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}