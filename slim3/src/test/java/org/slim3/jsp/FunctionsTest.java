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
package org.slim3.jsp;

import java.util.Date;

import junit.framework.TestCase;

import org.slim3.tester.MockHttpServletRequest;
import org.slim3.tester.MockHttpServletResponse;
import org.slim3.tester.MockServletContext;
import org.slim3.util.RequestLocator;
import org.slim3.util.ResponseLocator;

/**
 * @author higa
 * 
 */
public class FunctionsTest extends TestCase {

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request =
        new MockHttpServletRequest(servletContext);

    private MockHttpServletResponse response = new MockHttpServletResponse();

    @Override
    protected void setUp() throws Exception {
        RequestLocator.set(request);
        ResponseLocator.set(response);
    }

    @Override
    protected void tearDown() throws Exception {
        RequestLocator.set(null);
        ResponseLocator.set(null);
    }

    /**
     * @throws Exception
     */
    public void testHForString() throws Exception {
        assertEquals("&lt;a&gt;", Functions.h("<a>"));
    }

    /**
     * @throws Exception
     */
    public void testHForCharArray() throws Exception {
        assertEquals("[1]", Functions.h(new char[] { '1' }));
    }

    /**
     * @throws Exception
     */
    public void testHForByteArray() throws Exception {
        assertEquals("[1]", Functions.h(new byte[] { 1 }));
    }

    /**
     * @throws Exception
     */
    public void testHForShortArray() throws Exception {
        assertEquals("[1]", Functions.h(new short[] { 1 }));
    }

    /**
     * @throws Exception
     */
    public void testHForIntArray() throws Exception {
        assertEquals("[1]", Functions.h(new int[] { 1 }));
    }

    /**
     * @throws Exception
     */
    public void testHForFloatArray() throws Exception {
        assertEquals("[1.0]", Functions.h(new float[] { 1 }));
    }

    /**
     * @throws Exception
     */
    public void testHForDoubleArray() throws Exception {
        assertEquals("[1.0]", Functions.h(new double[] { 1 }));
    }

    /**
     * @throws Exception
     */
    public void testHForBooleanArray() throws Exception {
        assertEquals("[true]", Functions.h(new boolean[] { true }));
    }

    /**
     * @throws Exception
     */
    public void testHForStringArray() throws Exception {
        assertEquals("[1]", Functions.h(new String[] { "1" }));
    }

    /**
     * @throws Exception
     */
    public void testHForObjectArray() throws Exception {
        assertEquals("[1]", Functions.h(new Integer[] { Integer.valueOf(1) }));
    }

    /**
     * @throws Exception
     */
    public void testHForNbsp() throws Exception {
        assertEquals("&nbsp;", Functions.h(" "));
    }

    /**
     * @throws Exception
     */
    public void testHForDate() throws Exception {
        String s = Functions.h(new Date());
        System.out.println(s);
        assertNotNull(s);
    }

    /**
     * @throws Exception
     */
    public void testHForNumber() throws Exception {
        String s = Functions.h(1.123);
        System.out.println(s);
        assertNotNull(s);
    }

    /**
     * @throws Exception
     */
    public void testUrlForExternal() throws Exception {
        String input = "http://www.google.com";
        assertEquals(input, Functions.url(input));
    }

    /**
     * @throws Exception
     */
    public void testUrlForNull() throws Exception {
        servletContext.setContextPath("/aaa");
        request.setServletPath("/bbb/hoge");
        assertEquals("/aaa/bbb/", Functions.url(null));
    }

    /**
     * @throws Exception
     */
    public void testUrlForNullAndNoContextPath() throws Exception {
        request.setServletPath("/bbb/hoge");
        assertEquals("/bbb/", Functions.url(null));
    }

    /**
     * @throws Exception
     */
    public void testUrlForControllerRelativePath() throws Exception {
        request.setServletPath("/bbb/hoge");
        assertEquals("/bbb/foo", Functions.url("foo"));
    }

    /**
     * @throws Exception
     */
    public void testUrlForOtherController() throws Exception {
        request.setServletPath("/bbb/hoge");
        assertEquals("/hello/sayHello", Functions.url("/hello/sayHello"));
    }

    /**
     * @throws Exception
     */
    public void testBrForCRLF() throws Exception {
        assertEquals("<br />", Functions.br("\r\n"));
    }

    /**
     * @throws Exception
     */
    public void testBrForCR() throws Exception {
        assertEquals("<br />", Functions.br("\r"));
    }

    /**
     * @throws Exception
     */
    public void testBrForLF() throws Exception {
        assertEquals("<br />", Functions.br("\n"));
    }

    /**
     * @throws Exception
     */
    public void testBrForNull() throws Exception {
        assertEquals("", Functions.br(null));
    }
}