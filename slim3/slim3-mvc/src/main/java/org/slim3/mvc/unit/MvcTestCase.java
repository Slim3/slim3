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
package org.slim3.mvc.unit;

import org.slim3.commons.unit.CleanableTestCase;

/**
 * A test case for Slim3 MVC.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public abstract class MvcTestCase extends CleanableTestCase {

    /**
     * The tester for Slim3 MVC.
     */
    protected MvcTester mvcTester = new MvcTester();

    /**
     * @throws Exception
     * 
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mvcTester.setUp();
    }

    /**
     * @throws Exception
     * 
     */
    @Override
    protected void tearDown() throws Exception {
        mvcTester.tearDown();
        super.tearDown();
    }
}