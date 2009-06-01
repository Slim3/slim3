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

import java.util.Locale;

import junit.framework.TestCase;

import org.slim3.tester.MockHttpServletRequest;
import org.slim3.tester.MockServletContext;

/**
 * @author higa
 * 
 */
public class FrontControllerLocaleTest extends TestCase {

    /**
     * @throws Exception
     * 
     */
    public void testInitDefaultLocale() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        FrontController frontController = new FrontController();
        servletContext.setInitParameter(ControllerConstants.LOCALE_KEY, "de");
        frontController.servletContext = servletContext;
        frontController.initDefaultLocale();
        assertEquals(Locale.GERMAN, frontController.defaultLocale);
    }

    /**
     * @throws Exception
     * 
     */
    public void testProcessLocaleForSession() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest request =
            new MockHttpServletRequest(servletContext);
        request.getSession().setAttribute(
            ControllerConstants.LOCALE_KEY,
            Locale.GERMAN);
        FrontController frontController = new FrontController();
        frontController.defaultLocale = Locale.FRENCH;
        assertEquals(Locale.GERMAN, frontController.processLocale(request));
    }

    /**
     * @throws Exception
     * 
     */
    public void testProcessLocaleForDefaultLocale() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest request =
            new MockHttpServletRequest(servletContext);
        request.addLocale(Locale.GERMAN);
        FrontController frontController = new FrontController();
        frontController.defaultLocale = Locale.FRENCH;
        assertEquals(Locale.FRENCH, frontController.processLocale(request));
    }

    /**
     * @throws Exception
     * 
     */
    public void testProcessLocaleForRequest() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest request =
            new MockHttpServletRequest(servletContext);
        request.addLocale(Locale.GERMAN);
        FrontController frontController = new FrontController();
        assertEquals(Locale.GERMAN, frontController.processLocale(request));
    }

    /**
     * @throws Exception
     * 
     */
    public void testProcessLocaleForNoSetting() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest request =
            new MockHttpServletRequest(servletContext);
        FrontController frontController = new FrontController();
        assertEquals(Locale.getDefault(), frontController
            .processLocale(request));
    }
}