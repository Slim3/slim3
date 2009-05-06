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

import java.util.Locale;

import junit.framework.TestCase;

import org.slim3.mvc.unit.MockHttpServletRequest;
import org.slim3.mvc.unit.MockServletContext;

/**
 * @author higa
 * 
 */
public class LocaleLocatorTest extends TestCase {

    @Override
    protected void tearDown() throws Exception {
        RequestLocator.setRequest(null);
    }

    /**
     * @throws Exception
     * 
     */
    public void test() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest request =
            new MockHttpServletRequest(servletContext);
        RequestLocator.setRequest(request);
        assertEquals(Locale.getDefault(), LocaleLocator.getLocale());
        LocaleLocator.setLocale(Locale.US);
        assertEquals(Locale.US, LocaleLocator.getLocale());
    }
}