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
package org.slim3.struts.form;

import javax.servlet.ServletContext;

import org.apache.commons.beanutils.DynaClass;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResourcesFactory;
import org.apache.struts.util.PropertyMessageResources;
import org.slim3.commons.bean.BeanUtil;
import org.slim3.commons.bean.PropertyDesc;
import org.slim3.commons.config.Configuration;
import org.slim3.commons.unit.CleanableTestCase;
import org.slim3.struts.config.S3ExecuteConfig;
import org.slim3.struts.config.S3ModuleConfig;
import org.slim3.struts.form.action.HogeAction;
import org.slim3.struts.unit.MockHttpServletRequest;
import org.slim3.struts.unit.MockServletContext;
import org.slim3.struts.util.S3ExecuteConfigUtil;
import org.slim3.struts.util.S3PropertyMessageResourcesFactory;
import org.slim3.struts.validator.S3ValidatorPlugIn;
import org.slim3.struts.web.WebLocator;

/**
 * @author higa
 * 
 */
public class ActionFormWrapperTest extends CleanableTestCase {

    private static final String CONFIG_PATH = "org/slim3/struts/form/slim3_configuration.properties";

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request = new MockHttpServletRequest(
            servletContext, "/hoge.do");

    private S3ModuleConfig moduleConfig = new S3ModuleConfig("");

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Configuration.initialize(CONFIG_PATH);
        WebLocator.setServletContext(servletContext);
        WebLocator.setRequest(request);
        servletContext.setAttribute(Globals.MODULE_KEY, moduleConfig);
        MessageResourcesFactory factory = new S3PropertyMessageResourcesFactory();
        PropertyMessageResources resources = new PropertyMessageResources(
                factory, "application");
        servletContext.setAttribute(Globals.MESSAGES_KEY, resources);
        S3ValidatorPlugIn plugIn = new S3ValidatorPlugIn();
        plugIn.setPathnames("validator-rules.xml");
        plugIn.init(new MyActionServlet(servletContext), moduleConfig);

    }

    @Override
    protected void tearDown() throws Exception {
        WebLocator.setServletContext(null);
        WebLocator.setRequest(null);
        super.tearDown();
    }

    /**
     * @throws Exception
     */
    public void testReset() throws Exception {
        HogeAction action = new HogeAction();
        DynaClass dynaClass = new ActionFormWrapperClass("hoge");
        ActionFormWrapper actionForm = new ActionFormWrapper(dynaClass, action);
        S3ExecuteConfig executeConfig = new S3ExecuteConfig(HogeAction.class
                .getMethod("index"));
        executeConfig.setResetMethod(HogeAction.class.getMethod("reset"));
        S3ExecuteConfigUtil.setExecuteConfig(executeConfig);
        actionForm.reset(null, request);
        assertTrue(action.reseted);
    }

    /**
     * @throws Exception
     */
    public void testValidate() throws Exception {
        ActionMapping actionMapping = (ActionMapping) moduleConfig
                .findActionConfig("/hoge");
        HogeAction action = new HogeAction();
        DynaClass dynaClass = new ActionFormWrapperClass("hoge");
        ActionFormWrapper actionForm = new ActionFormWrapper(dynaClass, action);
        S3ExecuteConfig executeConfig = new S3ExecuteConfig(HogeAction.class
                .getMethod("index"));
        S3ExecuteConfigUtil.setExecuteConfig(executeConfig);
        ActionErrors errors = actionForm.validate(actionMapping, request);
        assertFalse(errors.isEmpty());
    }

    /**
     * @throws Exception
     */
    public void testGet() throws Exception {
        ActionFormWrapperClass dynaClass = new ActionFormWrapperClass("hoge");
        PropertyDesc pd = BeanUtil.getBeanDesc(HogeAction.class)
                .getPropertyDesc("aaa");
        dynaClass.addDynaProperty(new S3DynaProperty(pd));
        HogeAction action = new HogeAction();
        action.aaa = "111";
        ActionFormWrapper actionForm = new ActionFormWrapper(dynaClass, action);
        assertEquals("111", actionForm.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testGetForNoProperty() throws Exception {
        ActionFormWrapperClass dynaClass = new ActionFormWrapperClass("hoge");
        HogeAction action = new HogeAction();
        ActionFormWrapper actionForm = new ActionFormWrapper(dynaClass, action);
        try {
            actionForm.get("xxx");
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static class MyActionServlet extends ActionServlet {
        private static final long serialVersionUID = 1L;

        private ServletContext servletContext;

        /**
         * @param servletContext
         */
        public MyActionServlet(ServletContext servletContext) {
            super();
            this.servletContext = servletContext;
        }

        @Override
        public ServletContext getServletContext() {
            return servletContext;
        }
    }
}