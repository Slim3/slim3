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

import org.slim3.commons.config.Configuration;
import org.slim3.mvc.unit.MvcTestCase;

/**
 * @author higa
 * 
 */
public class FrontControllerHotReloadingTest extends MvcTestCase {

    /**
     * 
     */
    protected static final String PACKAGE = "org/slim3/mvc/controller/";

    /**
     * 
     */
    protected static final String CONFIG_PATH = PACKAGE
            + "slim3_configuration_hot.properties";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Configuration.initialize(CONFIG_PATH);
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateController() throws Exception {
        Controller controller = mvcTester.frontController
                .createController("/hello/list");
        assertNotNull(controller);
        assertEquals(HotReloadingClassLoader.class, controller.getClass()
                .getClassLoader().getClass());
    }
}