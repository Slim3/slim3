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
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.slim3.controller.ControllerConstants;
import org.slim3.tester.MockHttpServletRequest;
import org.slim3.tester.MockHttpServletResponse;
import org.slim3.tester.MockServletContext;
import org.slim3.util.RequestLocator;
import org.slim3.util.ResponseLocator;

import com.google.appengine.api.datastore.Text;

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
    public void testHForText() throws Exception {
        assertEquals("&nbsp;", Functions.h(new Text(" ")));
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

    /**
     * @throws Exception
     */
    public void testLocale() throws Exception {
        assertNotNull(Functions.locale());
    }

    /**
     * @throws Exception
     */
    public void testTimeZone() throws Exception {
        assertNotNull(Functions.timeZone());
    }

    /**
     * @throws Exception
     */
    public void testErrorStyle() throws Exception {
        Map<String, String> errors = new HashMap<String, String>();
        errors.put("aaa", "Aaa is required.");
        request.setAttribute(ControllerConstants.ERRORS_KEY, errors);
        assertEquals("error", Functions.errorClass("aaa", "error"));
    }

    /**
     * @throws Exception
     */
    public void testErrorStyleForNoError() throws Exception {
        assertEquals("", Functions.errorClass("aaa", "error"));
    }

    /**
     * @throws Exception
     */
    public void testText() throws Exception {
        request.setAttribute("aaa", "111");
        assertEquals("name = \"aaa\" value = \"111\"", Functions.text("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testTextForBadName() throws Exception {
        try {
            Functions.text("aaaArray");
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testHidden() throws Exception {
        request.setAttribute("aaa", "111");
        assertEquals("name = \"aaa\" value = \"111\"", Functions.hidden("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testHiddenForBadName() throws Exception {
        try {
            Functions.hidden("aaaArray");
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testCheckbox() throws Exception {
        request.setAttribute("aaa", "111");
        assertEquals("name = \"aaa\" checked = \"checked\"", Functions
            .checkbox("aaa"));
        assertEquals("name = \"bbb\"", Functions.checkbox("bbb"));
    }

    /**
     * @throws Exception
     */
    public void testCheckboxForBadName() throws Exception {
        try {
            Functions.checkbox("aaaArray");
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testMultibox() throws Exception {
        String[] aaaArray = new String[] { "111" };
        request.setAttribute("aaaArray", aaaArray);
        assertEquals(
            "name = \"aaaArray\" value = \"111\" checked = \"checked\"",
            Functions.multibox("aaaArray", "111"));
        assertEquals("name = \"aaaArray\" value = \"222\"", Functions.multibox(
            "aaaArray",
            "222"));
    }

    /**
     * @throws Exception
     */
    public void testMultiboxForNull() throws Exception {
        assertEquals("name = \"aaaArray\" value = \"111\"", Functions.multibox(
            "aaaArray",
            "111"));
    }

    /**
     * @throws Exception
     */
    public void testMultiboxForBadName() throws Exception {
        String[] aaaArray = new String[] { "111" };
        request.setAttribute("aaa", aaaArray);
        try {
            Functions.multibox("aaa", "111");
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testMultiboxForNotArray() throws Exception {
        String aaaArray = "111";
        request.setAttribute("aaaArray", aaaArray);
        try {
            Functions.multibox("aaaArray", "111");
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testMultiboxForNotStringArray() throws Exception {
        int[] aaaArray = new int[] { 1 };
        request.setAttribute("aaaArray", aaaArray);
        try {
            Functions.multibox("aaaArray", "111");
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testRadio() throws Exception {
        request.setAttribute("aaa", "111");
        assertEquals(
            "name = \"aaa\" value = \"111\" checked = \"checked\"",
            Functions.radio("aaa", "111"));
        assertEquals("name = \"aaa\" value = \"222\"", Functions.radio(
            "aaa",
            "222"));
    }

    /**
     * @throws Exception
     */
    public void testRadioForBadName() throws Exception {
        try {
            Functions.radio("aaaArray", "111");
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testSelect() throws Exception {
        request.setAttribute("aaa", new Integer(111));
        assertEquals("value = \"111\" selected = \"selected\"", Functions
            .select("aaa", "111"));
        assertEquals("value = \"222\"", Functions.select("aaa", "222"));
    }

    /**
     * @throws Exception
     */
    public void testSelectForBadName() throws Exception {
        try {
            Functions.select("aaaArray", "111");
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testMultiselect() throws Exception {
        String[] aaaArray = new String[] { "111" };
        request.setAttribute("aaaArray", aaaArray);
        assertEquals("value = \"111\" selected = \"selected\"", Functions
            .multiselect("aaaArray", "111"));
        assertEquals("value = \"222\"", Functions
            .multiselect("aaaArray", "222"));
    }

    /**
     * @throws Exception
     */
    public void testMultiselectForNull() throws Exception {
        assertEquals("value = \"111\"", Functions
            .multiselect("aaaArray", "111"));
    }

    /**
     * @throws Exception
     */
    public void testMultiselectForBadName() throws Exception {
        String[] aaaArray = new String[] { "111" };
        request.setAttribute("aaa", aaaArray);
        try {
            Functions.multiselect("aaa", "111");
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testMultiselectForNotArray() throws Exception {
        String aaaArray = "111";
        request.setAttribute("aaaArray", aaaArray);
        try {
            Functions.multiselect("aaaArray", "111");
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testMultiselectForNotStringArray() throws Exception {
        int[] aaaArray = new int[] { 1 };
        request.setAttribute("aaaArray", aaaArray);
        try {
            Functions.multiselect("aaaArray", "111");
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}