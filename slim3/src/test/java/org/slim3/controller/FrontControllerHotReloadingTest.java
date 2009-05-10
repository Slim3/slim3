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
package org.slim3.controller;

import org.slim3.tester.ControllerTestCase;

/**
 * @author higa
 * 
 */
public class FrontControllerHotReloadingTest extends ControllerTestCase {

    private static final String CONTROLLER_PACKAGE =
        FrontControllerHotReloadingTest.class.getPackage().getName()
            + ".controller";

    @Override
    protected void setUp() throws Exception {
        System.setProperty(ControllerConstants.HOT_RELOADING_KEY, "true");
        System.setProperty(
            ControllerConstants.CONTROLLER_PACKAGE_KEY,
            CONTROLLER_PACKAGE);
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        System.clearProperty(ControllerConstants.HOT_RELOADING_KEY);
        System.clearProperty(ControllerConstants.CONTROLLER_PACKAGE_KEY);
        super.tearDown();
    }

    /**
     * @throws Exception
     * 
     */
    public void testInit() throws Exception {
        assertTrue(controllerTester.frontController.hotReloading);
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateController() throws Exception {
        Controller controller =
            controllerTester.frontController.createController("/hello/list");
        assertNotNull(controller);
        assertEquals(HotReloadingClassLoader.class, controller
            .getClass()
            .getClassLoader()
            .getClass());
    }
}