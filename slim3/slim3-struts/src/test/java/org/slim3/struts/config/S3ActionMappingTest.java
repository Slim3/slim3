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

import junit.framework.TestCase;

import org.slim3.struts.config.action.HogeAction;
import org.slim3.struts.unit.MockHttpServletRequest;
import org.slim3.struts.unit.MockServletContext;
import org.slim3.struts.util.S3ExecuteConfigUtil;
import org.slim3.struts.web.WebLocator;

/**
 * @author higa
 * 
 */
public class S3ActionMappingTest extends TestCase {

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request = new MockHttpServletRequest(
            servletContext, "/hoge.do");

    @Override
    protected void tearDown() throws Exception {
        WebLocator.setRequest(null);
    }

    /**
     * @throws Exception
     */
    public void testGetValidate() throws Exception {
        S3ActionMapping actionMapping = new S3ActionMapping();
        actionMapping.setValidate(true);
        S3ExecuteConfig executeConfig = new S3ExecuteConfig(getClass()
                .getMethod("hashCode"));
        executeConfig.setValidate(false);
        WebLocator.setRequest(request);
        S3ExecuteConfigUtil.setExecuteConfig(executeConfig);
        assertFalse(actionMapping.getValidate());
        S3ExecuteConfigUtil.setExecuteConfig(null);
        assertTrue(actionMapping.getValidate());
    }

    /**
     * @throws Exception
     */
    public void testActionClass() throws Exception {
        S3ActionMapping actionMapping = new S3ActionMapping();
        actionMapping.setActionClass(HogeAction.class);
        assertEquals(HogeAction.class, actionMapping.getActionBeanDesc()
                .getBeanClass());
    }

    /**
     * @throws Exception
     */
    public void testGetExecuteMethodNames() throws Exception {
        S3ActionMapping actionMapping = new S3ActionMapping();
        String methodName = "hashCode";
        S3ExecuteConfig executeConfig = new S3ExecuteConfig(getClass()
                .getMethod(methodName));
        actionMapping.addExecuteConfig(executeConfig);
        String[] names = actionMapping.getExecuteMethodNames();
        assertEquals(1, names.length);
        assertEquals(methodName, names[0]);
    }
}