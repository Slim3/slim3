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

import org.slim3.mvc.unit.MvcTestCase;

/**
 * @author higa
 * 
 */
public class AppMessageBuilderTest extends MvcTestCase {

    /**
     * @throws Exception
     * 
     */
    public void testGetInstance() throws Exception {
        assertNotNull(AppMessageBuilder.getInstance());
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetBundle() throws Exception {
        assertNotNull(AppMessageBuilder.getInstance().getBundle());
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetMessage() throws Exception {
        assertEquals("hoge is required.", AppMessageBuilder.getInstance()
                .getMessage("errors.required", "hoge"));
    }
}