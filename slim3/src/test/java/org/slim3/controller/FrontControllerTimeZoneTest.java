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

import java.util.TimeZone;

import junit.framework.TestCase;

import org.slim3.tester.MockHttpServletRequest;
import org.slim3.tester.MockServletContext;

/**
 * @author higa
 * 
 */
public class FrontControllerTimeZoneTest extends TestCase {

    /**
     * @throws Exception
     * 
     */
    public void testInitDefaultTimeZone() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        FrontController frontController = new FrontController();
        servletContext.setInitParameter(
            ControllerConstants.TIME_ZONE_KEY,
            "PST");
        frontController.servletContext = servletContext;
        frontController.initDefaultTimeZone();
        assertEquals(
            TimeZone.getTimeZone("PST"),
            frontController.defaultTimeZone);
    }

    /**
     * @throws Exception
     * 
     */
    public void testProcessTimeZoneForSession() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest request =
            new MockHttpServletRequest(servletContext);
        request.getSession().setAttribute(
            ControllerConstants.TIME_ZONE_KEY,
            "PST");
        FrontController frontController = new FrontController();
        frontController.defaultTimeZone = TimeZone.getTimeZone("JST");
        assertEquals(TimeZone.getTimeZone("PST"), frontController
            .processTimeZone(request));
    }

    /**
     * @throws Exception
     * 
     */
    public void testProcessTimeZoneForDefaultLocale() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest request =
            new MockHttpServletRequest(servletContext);
        FrontController frontController = new FrontController();
        frontController.defaultTimeZone = TimeZone.getTimeZone("PST");
        assertEquals(TimeZone.getTimeZone("PST"), frontController
            .processTimeZone(request));
    }

    /**
     * @throws Exception
     * 
     */
    public void testProcessTimeZoneForNoSetting() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest request =
            new MockHttpServletRequest(servletContext);
        FrontController frontController = new FrontController();
        assertEquals(TimeZone.getDefault(), frontController
            .processTimeZone(request));
    }
}