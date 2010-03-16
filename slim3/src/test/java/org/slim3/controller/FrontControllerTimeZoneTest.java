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

import java.util.TimeZone;

import org.junit.Test;
import org.slim3.tester.MockHttpServletRequest;
import org.slim3.tester.MockServletContext;

/**
 * @author higa
 * 
 */
public class FrontControllerTimeZoneTest {

    /**
     * @throws Exception
     * 
     */
    @Test
    public void initDefaultTimeZone() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        FrontController frontController = new FrontController();
        servletContext.setInitParameter(
            ControllerConstants.TIME_ZONE_KEY,
            "PST");
        frontController.servletContext = servletContext;
        frontController.initDefaultTimeZone();
        assertThat(frontController.defaultTimeZone, is(TimeZone
            .getTimeZone("PST")));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void processTimeZoneForSession() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest request =
            new MockHttpServletRequest(servletContext);
        request.getSession().setAttribute(
            ControllerConstants.TIME_ZONE_KEY,
            "PST");
        FrontController frontController = new FrontController();
        frontController.defaultTimeZone = TimeZone.getTimeZone("JST");
        assertThat(frontController.processTimeZone(request), is(TimeZone
            .getTimeZone("PST")));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void processTimeZoneForDefaultLocale() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest request =
            new MockHttpServletRequest(servletContext);
        FrontController frontController = new FrontController();
        frontController.defaultTimeZone = TimeZone.getTimeZone("PST");
        assertThat(frontController.processTimeZone(request), is(TimeZone
            .getTimeZone("PST")));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void processTimeZoneForNoSetting() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest request =
            new MockHttpServletRequest(servletContext);
        FrontController frontController = new FrontController();
        assertThat(frontController.processTimeZone(request), is(TimeZone
            .getDefault()));
    }
}