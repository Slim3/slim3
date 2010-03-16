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

import java.util.Locale;

import org.junit.Test;
import org.slim3.tester.MockHttpServletRequest;
import org.slim3.tester.MockServletContext;

/**
 * @author higa
 * 
 */
public class FrontControllerLocaleTest {

    /**
     * @throws Exception
     * 
     */
    @Test
    public void initDefaultLocale() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        FrontController frontController = new FrontController();
        servletContext.setInitParameter(ControllerConstants.LOCALE_KEY, "de");
        frontController.servletContext = servletContext;
        frontController.initDefaultLocale();
        assertThat(frontController.defaultLocale, is(Locale.GERMAN));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void processLocaleForSession() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest request =
            new MockHttpServletRequest(servletContext);
        request.getSession().setAttribute(
            ControllerConstants.LOCALE_KEY,
            Locale.GERMAN);
        FrontController frontController = new FrontController();
        frontController.defaultLocale = Locale.FRENCH;
        assertThat(frontController.processLocale(request), is(Locale.GERMAN));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void processLocaleForDefaultLocale() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest request =
            new MockHttpServletRequest(servletContext);
        request.addLocale(Locale.GERMAN);
        FrontController frontController = new FrontController();
        frontController.defaultLocale = Locale.FRENCH;
        assertThat(frontController.processLocale(request), is(Locale.FRENCH));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void processLocaleForRequest() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest request =
            new MockHttpServletRequest(servletContext);
        request.addLocale(Locale.GERMAN);
        FrontController frontController = new FrontController();
        assertThat(frontController.processLocale(request), is(Locale.GERMAN));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void processLocaleForNoSetting() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest request =
            new MockHttpServletRequest(servletContext);
        FrontController frontController = new FrontController();
        assertThat(frontController.processLocale(request), is(Locale
            .getDefault()));
    }
}