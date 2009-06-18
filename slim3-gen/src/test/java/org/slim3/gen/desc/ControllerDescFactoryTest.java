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
package org.slim3.gen.desc;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class ControllerDescFactoryTest extends TestCase {

    private ControllerDescFactory factory =
        new ControllerDescFactory(
            "aaa.bbb",
            "MyController",
            "MyControllerTestCase",
            true);

    /**
     * 
     * @throws Exception
     */
    public void testCreateControllerDesc() throws Exception {
        ControllerDesc controllerDesc =
            factory.createControllerDesc("/ccc/ddd");
        assertEquals("MyController", controllerDesc.getSuperclassName());
        assertEquals("MyControllerTestCase", controllerDesc
            .getTestCaseSuperclassName());
        assertEquals("aaa.bbb.ccc", controllerDesc.getPackageName());
        assertEquals("DddController", controllerDesc.getSimpleName());
        assertEquals("/ccc/ddd.jsp", controllerDesc.getViewName());
        assertEquals("/ccc/ddd", controllerDesc.getPath());
    }

    /**
     * 
     * @throws Exception
     */
    public void testCreateControllerDesc_withoutView() throws Exception {
        factory =
            new ControllerDescFactory(
                "aaa.bbb",
                "MyController",
                "MyControllerTestCase",
                false);
        ControllerDesc controllerDesc =
            factory.createControllerDesc("/ccc/ddd");
        assertFalse(controllerDesc.isUseView());
    }

    /**
     * 
     * @throws Exception
     */
    public void testCreateControllerDesc_slashOnly() throws Exception {
        ControllerDesc controllerDesc = factory.createControllerDesc("/");
        assertEquals("MyController", controllerDesc.getSuperclassName());
        assertEquals("MyControllerTestCase", controllerDesc
            .getTestCaseSuperclassName());
        assertEquals("aaa.bbb", controllerDesc.getPackageName());
        assertEquals("IndexController", controllerDesc.getSimpleName());
        assertEquals("/index.jsp", controllerDesc.getViewName());
        assertEquals("/", controllerDesc.getPath());
    }

    /**
     * 
     * @throws Exception
     */
    public void testCreateControllerDesc_endsWithSlash() throws Exception {
        ControllerDesc controllerDesc =
            factory.createControllerDesc("/ccc/ddd/");
        assertEquals("MyController", controllerDesc.getSuperclassName());
        assertEquals("MyControllerTestCase", controllerDesc
            .getTestCaseSuperclassName());
        assertEquals("aaa.bbb.ccc.ddd", controllerDesc.getPackageName());
        assertEquals("IndexController", controllerDesc.getSimpleName());
        assertEquals("/ccc/ddd/index.jsp", controllerDesc.getViewName());
        assertEquals("/ccc/ddd/", controllerDesc.getPath());
    }
}
