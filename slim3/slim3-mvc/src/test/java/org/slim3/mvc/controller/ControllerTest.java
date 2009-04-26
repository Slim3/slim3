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

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class ControllerTest extends TestCase {

    private IndexController controller = new IndexController();

    /**
     * @throws Exception
     * 
     */
    public void testForward() throws Exception {
        Navigation nav = controller.forward("index.jsp");
        assertEquals("index.jsp", nav.getPath());
        assertFalse(nav.isRedirect());
    }

    /**
     * @throws Exception
     * 
     */
    public void testForwardForDefaultPath() throws Exception {
        Navigation nav = controller.forward();
        assertEquals("index.jsp", nav.getPath());
        assertFalse(nav.isRedirect());
    }

    /**
     * @throws Exception
     * 
     */
    public void testRedirect() throws Exception {
        Navigation nav = controller.redirect("index");
        assertEquals("index", nav.getPath());
        assertTrue(nav.isRedirect());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCalculateDefaultPath() throws Exception {
        assertEquals("index.jsp", controller.calculateDefaultPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCalculateDefaultPathForBadControllerClass()
            throws Exception {
        try {
            new BadControl().calculateDefaultPath();
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private static class IndexController extends Controller {

        @Override
        public Navigation execute() {
            return null;
        }
    }

    private static class BadControl extends Controller {

        @Override
        public Navigation execute() {
            return null;
        }
    }
}