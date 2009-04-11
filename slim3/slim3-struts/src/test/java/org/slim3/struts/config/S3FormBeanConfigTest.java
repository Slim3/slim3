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

import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.validator.ValidatorPlugIn;
import org.slim3.commons.config.Configuration;
import org.slim3.commons.unit.CleanableTestCase;
import org.slim3.struts.form.ActionFormWrapper;
import org.slim3.struts.unit.MockHttpServletRequest;
import org.slim3.struts.unit.MockServletContext;
import org.slim3.struts.util.ActionUtil;
import org.slim3.struts.util.S3PropertyMessageResources;
import org.slim3.struts.util.S3PropertyMessageResourcesFactory;
import org.slim3.struts.validator.S3ValidatorResources;
import org.slim3.struts.web.WebLocator;

/**
 * @author higa
 * 
 */
public class S3FormBeanConfigTest extends CleanableTestCase {

    private static final String CONFIG_PATH = "org/slim3/struts/config/slim3_configuration.properties";

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request = new MockHttpServletRequest(
            servletContext, "/hoge.do");

    private S3ModuleConfig moduleConfig = new S3ModuleConfig("/");

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Configuration.initialize(CONFIG_PATH);
        WebLocator.setServletContext(servletContext);
        WebLocator.setRequest(request);
        S3ValidatorResources validatorResources = new S3ValidatorResources();
        servletContext.setAttribute(ValidatorPlugIn.VALIDATOR_KEY,
                validatorResources);
        S3PropertyMessageResourcesFactory factory = new S3PropertyMessageResourcesFactory();
        S3PropertyMessageResources resources = new S3PropertyMessageResources(
                factory, "application");
        servletContext.setAttribute(Globals.MESSAGES_KEY, resources);
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
    public void testCreateActionForm() throws Exception {
        ActionUtil.setAction(new Object());
        String name = "hoge2ActionForm";
        moduleConfig.findActionConfig("/hoge2");
        S3FormBeanConfig formBeanConfig = (S3FormBeanConfig) moduleConfig
                .findFormBeanConfig(name);
        ActionForm actionForm = formBeanConfig
                .createActionForm(new ActionServlet());
        assertNotNull(actionForm);
        assertEquals(ActionFormWrapper.class, actionForm.getClass());
    }
}