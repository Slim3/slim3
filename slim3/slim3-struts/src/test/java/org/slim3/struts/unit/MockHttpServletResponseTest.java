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
package org.slim3.struts.unit;

import java.io.Writer;

import org.slim3.struts.unit.MockHttpServletResponse;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class MockHttpServletResponseTest extends TestCase {

    private MockHttpServletResponse response = new MockHttpServletResponse();

    /**
     * @throws Exception
     */
    public void testWriter() throws Exception {
        Writer writer = response.getWriter();
        String data = "abc";
        writer.write(data);
        response.flushBuffer();
        assertEquals(data, response.getOutputAsString());
    }

    /**
     * @throws Exception
     * 
     */
    public void testHeader() throws Exception {
        response.setHeader("aaa", "111");
        assertEquals("111", response.getHeader("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testIntHeader() throws Exception {
        response.setIntHeader("aaa", 1);
        assertEquals("1", response.getHeader("aaa"));
        assertEquals(1, response.getIntHeader("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testDateHeader() throws Exception {
        response.setDateHeader("aaa", 1000);
        System.out.println(response.getHeader("aaa"));
        assertEquals(1000, response.getDateHeader("aaa"));
    }
}