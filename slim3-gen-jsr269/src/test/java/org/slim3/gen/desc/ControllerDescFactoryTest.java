/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author taedium
 * 
 */
public class ControllerDescFactoryTest {

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
    @Test
    public void testCreateControllerDesc() throws Exception {
        ControllerDesc controllerDesc =
            factory.createControllerDesc("/ccc/ddd");
        assertThat(controllerDesc.getSuperclassName(), is("MyController"));
        assertThat(
            controllerDesc.getTestCaseSuperclassName(),
            is("MyControllerTestCase"));
        assertThat(controllerDesc.getPackageName(), is("aaa.bbb.ccc"));
        assertThat(controllerDesc.getSimpleName(), is("DddController"));
        assertThat(controllerDesc.getViewName(), is("/ccc/ddd.jsp"));
        assertThat(controllerDesc.getPath(), is("/ccc/ddd"));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCreateControllerDesc_withoutView() throws Exception {
        factory =
            new ControllerDescFactory(
                "aaa.bbb",
                "MyController",
                "MyControllerTestCase",
                false);
        ControllerDesc controllerDesc =
            factory.createControllerDesc("/ccc/ddd");
        assertThat(controllerDesc.isUseView(), is(false));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCreateControllerDesc_slashOnly() throws Exception {
        ControllerDesc controllerDesc = factory.createControllerDesc("/");
        assertThat(controllerDesc.getSuperclassName(), is("MyController"));
        assertThat(
            controllerDesc.getTestCaseSuperclassName(),
            is("MyControllerTestCase"));
        assertThat(controllerDesc.getPackageName(), is("aaa.bbb"));
        assertThat(controllerDesc.getSimpleName(), is("IndexController"));
        assertThat(controllerDesc.getViewName(), is("/index.jsp"));
        assertThat(controllerDesc.getPath(), is("/"));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCreateControllerDesc_endsWithSlash() throws Exception {
        ControllerDesc controllerDesc =
            factory.createControllerDesc("/ccc/ddd/");
        assertThat(controllerDesc.getSuperclassName(), is("MyController"));
        assertThat(
            controllerDesc.getTestCaseSuperclassName(),
            is("MyControllerTestCase"));
        assertThat(controllerDesc.getPackageName(), is("aaa.bbb.ccc.ddd"));
        assertThat(controllerDesc.getSimpleName(), is("IndexController"));
        assertThat(controllerDesc.getViewName(), is("/ccc/ddd/index.jsp"));
        assertThat(controllerDesc.getPath(), is("/ccc/ddd/"));
    }
}
