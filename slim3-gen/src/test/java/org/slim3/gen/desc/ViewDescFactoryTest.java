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
public class ViewDescFactoryTest extends TestCase {

    /**
     * 
     * @throws Exception
     */
    public void testCreateViewDesc_slashOnly() throws Exception {
        ViewDescFactory factory = new ViewDescFactory();
        ViewDesc viewDesc = factory.createViewDesc("/");
        assertEquals("", viewDesc.getDirName());
        assertEquals("index.jsp", viewDesc.getFileName());
        assertEquals("", viewDesc.getRelativePath());
        assertEquals("Index", viewDesc.getTitle());
    }

    /**
     * 
     * @throws Exception
     */
    public void testCreateViewDesc_oneDepth() throws Exception {
        ViewDescFactory factory = new ViewDescFactory();
        ViewDesc viewDesc = factory.createViewDesc("/aaa");
        assertEquals("", viewDesc.getDirName());
        assertEquals("aaa.jsp", viewDesc.getFileName());
        assertEquals("aaa", viewDesc.getRelativePath());
        assertEquals("Aaa", viewDesc.getTitle());
    }

    /**
     * 
     * @throws Exception
     */
    public void testCreateViewDesc_oneDepth_endsWithSlash() throws Exception {
        ViewDescFactory factory = new ViewDescFactory();
        ViewDesc viewDesc = factory.createViewDesc("/aaa/");
        assertEquals("/aaa", viewDesc.getDirName());
        assertEquals("index.jsp", viewDesc.getFileName());
        assertEquals("", viewDesc.getRelativePath());
        assertEquals("aaa Index", viewDesc.getTitle());
    }

    /**
     * 
     * @throws Exception
     */
    public void testCreateViewDesc_twoDepth() throws Exception {
        ViewDescFactory factory = new ViewDescFactory();
        ViewDesc viewDesc = factory.createViewDesc("/aaa/bbb");
        assertEquals("/aaa", viewDesc.getDirName());
        assertEquals("bbb.jsp", viewDesc.getFileName());
        assertEquals("bbb", viewDesc.getRelativePath());
        assertEquals("aaa Bbb", viewDesc.getTitle());
    }

    /**
     * 
     * @throws Exception
     */
    public void testCreateViewDesc_twoDepth_endsWithSlash() throws Exception {
        ViewDescFactory factory = new ViewDescFactory();
        ViewDesc viewDesc = factory.createViewDesc("/aaa/bbb/");
        assertEquals("/aaa/bbb", viewDesc.getDirName());
        assertEquals("index.jsp", viewDesc.getFileName());
        assertEquals("", viewDesc.getRelativePath());
        assertEquals("aaa bbb Index", viewDesc.getTitle());
    }
}
