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
package org.slim3.struts.config;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.slim3.struts.unit.MockHttpServletRequest;
import org.slim3.struts.unit.MockServletContext;

/**
 * @author higa
 * 
 */
public class S3ExecuteConfigTest extends TestCase {

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request = new MockHttpServletRequest(
            servletContext, "/hoge.do");

    /**
     * @throws Exception
     */
    public void testIsTarget() throws Exception {
        Method method = getClass().getMethod("testIsTarget");
        S3ExecuteConfig executeConfig = new S3ExecuteConfig(method);
        assertFalse(executeConfig.isTarget(request));
        request.setParameter(method.getName(), "aaa");
        assertTrue(executeConfig.isTarget(request));
        request.removeParameter(method.getName());
        request.setParameter(method.getName() + ".x", "aaa");
        assertTrue(executeConfig.isTarget(request));
        request.removeParameter(method.getName());
        request.setParameter(method.getName() + ".y", "aaa");
        assertTrue(executeConfig.isTarget(request));
    }
}