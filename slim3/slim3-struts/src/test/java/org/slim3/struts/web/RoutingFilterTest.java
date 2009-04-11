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
package org.slim3.struts.web;

import org.apache.struts.Globals;
import org.apache.struts.validator.ValidatorPlugIn;
import org.slim3.commons.config.Configuration;
import org.slim3.commons.unit.CleanableTestCase;
import org.slim3.struts.config.S3ActionMapping;
import org.slim3.struts.config.S3ModuleConfig;
import org.slim3.struts.unit.MockHttpServletRequest;
import org.slim3.struts.unit.MockHttpServletResponse;
import org.slim3.struts.unit.MockServletContext;
import org.slim3.struts.util.S3ExecuteConfigUtil;
import org.slim3.struts.util.S3PropertyMessageResources;
import org.slim3.struts.util.S3PropertyMessageResourcesFactory;
import org.slim3.struts.validator.S3ValidatorResources;

/**
 * @author higa
 * 
 */
public class RoutingFilterTest extends CleanableTestCase {

    private static final String CONFIG_PATH = "org/slim3/struts/web/slim3_configuration.properties";

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request = new MockHttpServletRequest(
            servletContext, "/filter/");

    private MockHttpServletResponse response = new MockHttpServletResponse();

    private S3ModuleConfig moduleConfig = new S3ModuleConfig("");

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Configuration.initialize(CONFIG_PATH);
        WebLocator.setRequest(request);
        WebLocator.setResponse(response);
        WebLocator.setServletContext(servletContext);
        S3ValidatorResources validatorResources = new S3ValidatorResources();
        servletContext.setAttribute(ValidatorPlugIn.VALIDATOR_KEY,
                validatorResources);
        S3PropertyMessageResourcesFactory factory = new S3PropertyMessageResourcesFactory();
        S3PropertyMessageResources resources = new S3PropertyMessageResources(
                factory, "application");
        servletContext.setAttribute(Globals.MESSAGES_KEY, resources);
        servletContext.setAttribute(Globals.MODULE_KEY, moduleConfig);
    }

    @Override
    protected void tearDown() throws Exception {
        WebLocator.setServletContext(null);
        super.tearDown();
    }

    /**
     * @throws Exception
     */
    public void testAssembleActionPathActionPathOnly() throws Exception {
        assertEquals("/filter.do", RoutingFilter.assembleActionPath("/filter/"));
        assertNull(S3ExecuteConfigUtil.getExecuteConfig());
    }

    /**
     * @throws Exception
     */
    public void testAssembleActionPathActionPathOnlyPreviousExecuteConfigExist()
            throws Exception {
        S3ExecuteConfigUtil.setExecuteConfig(((S3ActionMapping) moduleConfig
                .findActionConfig("/filter")).getExecuteConfig("submit"));
        assertEquals("/filter.do?submit=", RoutingFilter
                .assembleActionPath("/filter/"));
        assertNull(S3ExecuteConfigUtil.getExecuteConfig());
    }

    /**
     * @throws Exception
     */
    public void testAssembleActionPathActionPathAndMethod() throws Exception {
        assertEquals("/filter.do", RoutingFilter
                .assembleActionPath("/filter/index"));
        assertNotNull(S3ExecuteConfigUtil.getExecuteConfig());
    }

    /**
     * @throws Exception
     */
    public void testAssembleActionPathIndexAction() throws Exception {
        assertEquals("/index.do", RoutingFilter.assembleActionPath("/"));
        assertNull(S3ExecuteConfigUtil.getExecuteConfig());
    }

    /**
     * @throws Exception
     */
    public void testAssembleActionPathIndexActionAndMethod() throws Exception {
        assertEquals("/index.do", RoutingFilter.assembleActionPath("/index"));
        assertNotNull(S3ExecuteConfigUtil.getExecuteConfig());
    }

    /**
     * @throws Exception
     */
    public void testAssembleActionPathNoActionPath() throws Exception {
        assertEquals("/xxx", RoutingFilter.assembleActionPath("/xxx"));
    }
}