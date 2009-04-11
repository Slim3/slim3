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
package org.slim3.struts.util;

import junit.framework.TestCase;

import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.MessageResourcesFactory;
import org.apache.struts.util.PropertyMessageResources;
import org.slim3.struts.unit.MockServletContext;
import org.slim3.struts.web.WebLocator;

/**
 * @author higa
 * 
 */
public class MessageResourcesUtilTest extends TestCase {

    private MockServletContext servletContext = new MockServletContext();

    @Override
    protected void setUp() throws Exception {
        WebLocator.setServletContext(servletContext);
    }

    @Override
    protected void tearDown() throws Exception {
        WebLocator.setServletContext(null);
    }

    /**
     * @throws Exception
     */
    public void testGetMessageResources() throws Exception {
        MessageResourcesFactory factory = new MyMessageResourcesFactory();
        PropertyMessageResources resources = new PropertyMessageResources(
                factory, "application");
        servletContext.setAttribute(Globals.MESSAGES_KEY, resources);
        assertNotNull(MessageResourcesUtil.getMessageResources());
    }

    /**
     * @throws Exception
     */
    public void testGetMessage() throws Exception {
        MessageResourcesFactory factory = new MyMessageResourcesFactory();
        PropertyMessageResources resources = new PropertyMessageResources(
                factory, "application");
        servletContext.setAttribute(Globals.MESSAGES_KEY, resources);
        assertEquals("message", MessageResourcesUtil.getMessage("hoge"));
    }

    private static class MyMessageResourcesFactory extends MessageResourcesFactory {

        private static final long serialVersionUID = 1L;

        @Override
        public MessageResources createResources(String config) {
            return null;
        }

    }
}