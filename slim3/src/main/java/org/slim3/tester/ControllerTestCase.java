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
package org.slim3.tester;

import org.junit.After;
import org.junit.Before;

/**
 * A test case for Slim3 Controller.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public abstract class ControllerTestCase {

    /**
     * The tester for Slim3 Controller.
     */
    protected ControllerTester tester =
        new ControllerTester(getClass());

    /**
     * Sets up this test.
     * 
     * @throws Exception
     *             if an exception occurred
     */
    @Before
    public void setUp() throws Exception {
        tester.setUp();
    }

    /**
     * Tears down this test
     * 
     * @throws Exception
     *             if an exception occurred
     */
    @After
    public void tearDown() throws Exception {
        tester.tearDown();
    }
}