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
package org.slim3.struts.taglib;

import junit.framework.TestCase;

import org.slim3.struts.unit.MockHttpServletRequest;
import org.slim3.struts.unit.MockHttpServletResponse;
import org.slim3.struts.unit.MockServletContext;
import org.slim3.struts.web.WebLocator;

/**
 * @author higa
 * 
 */
public class S3FunctionsTest extends TestCase {

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request = new MockHttpServletRequest(
            servletContext, "/hoge.do");

    private MockHttpServletResponse response = new MockHttpServletResponse();

    @Override
    protected void setUp() throws Exception {
        WebLocator.setRequest(request);
        WebLocator.setResponse(response);
    }

    @Override
    protected void tearDown() throws Exception {
        WebLocator.setRequest(null);
        WebLocator.setResponse(null);
    }

    /**
     * @throws Exception
     */
    public void testHForCharArray() throws Exception {
        assertEquals("[1]", S3Functions.h(new char[] { '1' }));
    }

    /**
     * @throws Exception
     */
    public void testHForByteArray() throws Exception {
        assertEquals("[1]", S3Functions.h(new byte[] { 1 }));
    }

    /**
     * @throws Exception
     */
    public void testHForShortArray() throws Exception {
        assertEquals("[1]", S3Functions.h(new short[] { 1 }));
    }

    /**
     * @throws Exception
     */
    public void testHForIntArray() throws Exception {
        assertEquals("[1]", S3Functions.h(new int[] { 1 }));
    }

    /**
     * @throws Exception
     */
    public void testHForFloatArray() throws Exception {
        assertEquals("[1.0]", S3Functions.h(new float[] { 1 }));
    }

    /**
     * @throws Exception
     */
    public void testHForDoubleArray() throws Exception {
        assertEquals("[1.0]", S3Functions.h(new double[] { 1 }));
    }

    /**
     * @throws Exception
     */
    public void testHForBooleanArray() throws Exception {
        assertEquals("[true]", S3Functions.h(new boolean[] { true }));
    }

    /**
     * @throws Exception
     */
    public void testHForStringArray() throws Exception {
        assertEquals("[1]", S3Functions.h(new String[] { "1" }));
    }

    /**
     * @throws Exception
     */
    public void testHForObjectArray() throws Exception {
        assertEquals("[1]", S3Functions.h(new Integer[] { Integer.valueOf(1) }));
    }

    /**
     * @throws Exception
     */
    public void testDate() throws Exception {
        assertNotNull(S3Functions.date("20080131", "yyyyMMdd"));
    }

    /**
     * @throws Exception
     */
    public void testDateValueIsNull() throws Exception {
        assertNull(S3Functions.date(null, "yyyyMMdd"));
    }

    /**
     * @throws Exception
     */
    public void testDatePatternIsNull() throws Exception {
        try {
            S3Functions.date("20080131", null);
            fail();
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testNumber() throws Exception {
        assertEquals("1000", S3Functions.number("1000", "####").toString());
    }

    /**
     * @throws Exception
     */
    public void testNumberValueIsNull() throws Exception {
        assertNull(S3Functions.number(null, "####"));
    }

    /**
     * @throws Exception
     */
    public void testNumberPatternIsNull() throws Exception {
        try {
            S3Functions.number("1000", null);
            fail();
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testBrForCRLF() throws Exception {
        assertEquals("<br />", S3Functions.br("\r\n"));
    }

    /**
     * @throws Exception
     */
    public void testBrForCR() throws Exception {
        assertEquals("<br />", S3Functions.br("\r"));
    }

    /**
     * @throws Exception
     */
    public void testBrForLF() throws Exception {
        assertEquals("<br />", S3Functions.br("\n"));
    }

    /**
     * @throws Exception
     */
    public void testBrForNull() throws Exception {
        assertEquals("", S3Functions.br(null));
    }

    /**
     * @throws Exception
     */
    public void testNbsp() throws Exception {
        assertEquals("&nbsp;&nbsp;", S3Functions.nbsp("  "));
    }

    /**
     * @throws Exception
     */
    public void testNbspForNull() throws Exception {
        assertEquals("", S3Functions.nbsp(null));
    }

    /**
     * @throws Exception
     */
    public void testUrlForNull() throws Exception {
        servletContext.setServletContextName("context");
        request.setPathInfo("/add/index.jsp");
        assertEquals("/context/add/", S3Functions.url(null));
    }

    /**
     * @throws Exception
     */
    public void testUrlForNullAndContextNameNull() throws Exception {
        request.setPathInfo("/add/index.jsp");
        assertEquals("/add/", S3Functions.url(null));
    }

    /**
     * @throws Exception
     */
    public void testUrlForActionRelativePath() throws Exception {
        assertEquals("/hoge/index.jsp", S3Functions.url("index.jsp"));
    }

    /**
     * @throws Exception
     */
    public void testUrlForAction() throws Exception {
        assertEquals("/add/submit", S3Functions.url("/add/submit"));
    }
}