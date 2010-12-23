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
package org.slim3.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

/**
 * @author higa
 * 
 */
public class SimpleControllerTest extends ControllerTestCase {

    private SimpleController controller = new SimpleController() {

        @Override
        protected Navigation run() throws Exception {
            return null;
        }
    };

    @Override
    public void setUp() throws Exception {
        super.setUp();
        controller.servletContext = tester.servletContext;
        controller.request = tester.request;
        controller.response = tester.response;
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void createRequestHandler() throws Exception {
        assertThat(controller
            .createRequestHandler(tester.request)
            .getClass()
            .getName(), is(SimpleRequestHandler.class.getName()));
    }
}