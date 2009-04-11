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
package org.slim3.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.apache.struts.action.ActionForward;
import org.slim3.struts.annotation.Execute;
import org.slim3.struts.config.S3ExecuteConfig;
import org.slim3.struts.unit.MockHttpServletRequest;
import org.slim3.struts.unit.MockServletContext;
import org.slim3.struts.util.S3ExecuteConfigUtil;
import org.slim3.struts.web.WebLocator;

/**
 * @author higa
 * 
 */
public class ActionWrapperTest extends TestCase {

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request = new MockHttpServletRequest(
            servletContext, "/hoge.do");

    @Override
    protected void setUp() throws Exception {
        WebLocator.setRequest(request);
    }

    @Override
    protected void tearDown() throws Exception {
        WebLocator.setRequest(null);
    }

    /**
     * @throws Exception
     */
    public void testExecute() throws Exception {
        HogeAction action = new HogeAction();
        S3ExecuteConfig executeConfig = new S3ExecuteConfig(HogeAction.class
                .getMethod("index"));
        S3ExecuteConfigUtil.setExecuteConfig(executeConfig);
        ActionWrapper wrapper = new ActionWrapper(action);
        ActionForward forward = wrapper.execute(null, null,
                (HttpServletRequest) null, (HttpServletResponse) null);
        assertNotNull(forward);
        assertEquals("index.jsp", forward.getPath());
    }

    /**
     *
     */
    public static class HogeAction {
        /**
         * @return the result
         */
        @Execute(validate = false)
        public ActionForward index() {
            return new ActionForward("index.jsp");
        }
    }
}