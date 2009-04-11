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
package org.slim3.struts.util;

import junit.framework.TestCase;

import org.slim3.commons.config.Configuration;
import org.slim3.struts.unit.MockHttpServletRequest;
import org.slim3.struts.unit.MockServletContext;
import org.slim3.struts.web.WebLocator;

/**
 * @author higa
 * 
 */
public class RequestUtilTest extends TestCase {

    private static final String CONFIG_PATH = "org/slim3/struts/util/slim3_configuration.properties";

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request = new MockHttpServletRequest(
            servletContext, "/hello");

    @Override
    protected void setUp() throws Exception {
        Configuration.initialize(CONFIG_PATH);
        WebLocator.setRequest(request);
    }

    @Override
    protected void tearDown() throws Exception {
        WebLocator.setRequest(null);
        Configuration.initialize();
    }

    /**
     * @throws Exception
     */
    public void testGetPath() throws Exception {
        request.setPathInfo("/WEB-INF/view/hoge.jsp");
        assertEquals("/hoge.jsp", RequestUtil.getPath(request));
    }
}