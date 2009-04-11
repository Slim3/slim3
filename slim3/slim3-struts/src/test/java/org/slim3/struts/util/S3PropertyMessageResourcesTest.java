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

import java.util.Locale;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class S3PropertyMessageResourcesTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testGetMessage() throws Exception {
        S3PropertyMessageResourcesFactory factory = new S3PropertyMessageResourcesFactory();
        S3PropertyMessageResources resources = new S3PropertyMessageResources(
                factory, "application");
        assertEquals("message", resources.getMessage(Locale.ENGLISH, "hoge"));
        assertEquals("message", resources.getMessage("hoge"));
        assertNull(resources.getMessage((Locale) null, "xxx"));
    }
}