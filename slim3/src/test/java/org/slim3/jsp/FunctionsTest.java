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
package org.slim3.jsp;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slim3.controller.ControllerConstants;
import org.slim3.tester.MockHttpServletRequest;
import org.slim3.tester.MockHttpServletResponse;
import org.slim3.tester.MockServletContext;
import org.slim3.util.RequestLocator;
import org.slim3.util.ResponseLocator;

/**
 * @author higa
 * 
 */
public class FunctionsTest {

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request =
        new MockHttpServletRequest(servletContext);

    private MockHttpServletResponse response = new MockHttpServletResponse();

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        RequestLocator.set(request);
        ResponseLocator.set(response);
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        RequestLocator.set(null);
        ResponseLocator.set(null);
    }

    /**
     * @throws Exception
     */
    @Test
    public void hForString() throws Exception {
        assertThat(Functions.h("<a>"), is("&lt;a&gt;"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void hForDate() throws Exception {
        String s = Functions.h(new Date());
        System.out.println(s);
        assertThat(s, is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void hForNumber() throws Exception {
        String s = Functions.h(1.123);
        System.out.println(s);
        assertThat(s, is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void urlForExternal() throws Exception {
        String input = "http://www.google.com";
        assertThat(Functions.url(input), is(input));
    }

    /**
     * @throws Exception
     */
    @Test
    public void urlForNull() throws Exception {
        servletContext.setContextPath("/aaa");
        request.setServletPath("/bbb/hoge");
        assertThat(Functions.url(null), is("/aaa/bbb/"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void urlForNullAndNoContextPath() throws Exception {
        request.setServletPath("/bbb/hoge");
        assertThat(Functions.url(null), is("/bbb/"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void urlForControllerRelativePathWithServletPath() throws Exception {
        request.setServletPath("/bbb/hoge");
        assertThat(Functions.url("foo"), is("/bbb/foo"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void urlForControllerRelativePathWithBasePath() throws Exception {
        request.setAttribute(ControllerConstants.BASE_PATH_KEY, "/bbb/");
        assertThat(Functions.url("foo"), is("/bbb/foo"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void urlForOtherController() throws Exception {
        request.setServletPath("/bbb/hoge");
        assertThat(Functions.url("/hello/sayHello"), is("/hello/sayHello"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void brForCRLF() throws Exception {
        assertThat(Functions.br("\r\n"), is("<br />"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void brForCR() throws Exception {
        assertThat(Functions.br("\r"), is("<br />"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void brForLF() throws Exception {
        assertThat(Functions.br("\n"), is("<br />"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void brForNull() throws Exception {
        assertThat(Functions.br(null), is(""));
    }

    /**
     * @throws Exception
     */
    @Test
    public void nbspForBlank() throws Exception {
        assertThat(Functions.nbsp(" "), is("&nbsp;"));
        assertThat(Functions.nbsp("  "), is("&nbsp;&nbsp;"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void nbspForNull() throws Exception {
        assertThat(Functions.nbsp(null), is(""));
    }

    /**
     * @throws Exception
     */
    @Test
    public void nbspForEmptyString() throws Exception {
        assertThat(Functions.nbsp(""), is(""));
    }

    /**
     * @throws Exception
     */
    @Test
    public void locale() throws Exception {
        assertThat(Functions.locale(), is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void timeZone() throws Exception {
        assertThat(Functions.timeZone(), is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void errorClass() throws Exception {
        Map<String, String> errors = new HashMap<String, String>();
        errors.put("aaa", "Aaa is required.");
        request.setAttribute(ControllerConstants.ERRORS_KEY, errors);
        assertThat(Functions.errorClass("aaa", "error"), is("error"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void errorClassForNoError() throws Exception {
        assertThat(Functions.errorClass("aaa", "error"), is(""));
    }

    /**
     * @throws Exception
     */
    @Test
    public void errors() throws Exception {
        Map<String, String> errors = new HashMap<String, String>();
        errors.put("aaa", "Aaa is required.");
        request.setAttribute(ControllerConstants.ERRORS_KEY, errors);
        Iterator<String> iterator = Functions.errors();
        assertThat(iterator, is(not(nullValue())));
        assertThat(iterator.next(), is("Aaa is required."));
    }

    /**
     * @throws Exception
     */
    @Test
    public void errorsForNoErrors() throws Exception {
        assertThat(Functions.errors(), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void text() throws Exception {
        request.setAttribute("aaa", "111");
        assertThat(Functions.text("aaa"), is("name=\"aaa\" value=\"111\""));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void textForBadName() throws Exception {
        Functions.text("aaaArray");
    }

    /**
     * @throws Exception
     */
    @Test
    public void hidden() throws Exception {
        request.setAttribute("aaa", "111");
        assertThat(Functions.hidden("aaa"), is("name=\"aaa\" value=\"111\""));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void hiddenForBadName() throws Exception {
        Functions.hidden("aaaArray");
    }

    /**
     * @throws Exception
     */
    @Test
    public void checkbox() throws Exception {
        request.setAttribute("aaa", "111");
        request.setAttribute("ccc", "false");
        assertThat(
            Functions.checkbox("aaa"),
            is("name=\"aaa\" checked=\"checked\""));
        assertThat(Functions.checkbox("bbb"), is("name=\"bbb\""));
        assertThat(Functions.checkbox("ccc"), is("name=\"ccc\""));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void checkboxForBadName() throws Exception {
        Functions.checkbox("aaaArray");
    }

    /**
     * @throws Exception
     */
    @Test
    public void multibox() throws Exception {
        String[] aaaArray = new String[] { "111" };
        request.setAttribute("aaaArray", aaaArray);
        assertThat(
            Functions.multibox("aaaArray", "111"),
            is("name=\"aaaArray\" value=\"111\" checked=\"checked\""));
        assertThat(
            Functions.multibox("aaaArray", "222"),
            is("name=\"aaaArray\" value=\"222\""));
    }

    /**
     * @throws Exception
     */
    @Test
    public void multiboxForNull() throws Exception {
        assertThat(
            Functions.multibox("aaaArray", "111"),
            is("name=\"aaaArray\" value=\"111\""));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void multiboxForBadName() throws Exception {
        String[] aaaArray = new String[] { "111" };
        request.setAttribute("aaa", aaaArray);
        Functions.multibox("aaa", "111");
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void multiboxForNotArray() throws Exception {
        String aaaArray = "111";
        request.setAttribute("aaaArray", aaaArray);
        Functions.multibox("aaaArray", "111");
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void multiboxForNotStringArray() throws Exception {
        int[] aaaArray = new int[] { 1 };
        request.setAttribute("aaaArray", aaaArray);
        Functions.multibox("aaaArray", "111");
    }

    /**
     * @throws Exception
     */
    @Test
    public void radio() throws Exception {
        request.setAttribute("aaa", "111");
        assertThat(
            Functions.radio("aaa", "111"),
            is("name=\"aaa\" value=\"111\" checked=\"checked\""));
        assertThat(
            Functions.radio("aaa", "222"),
            is("name=\"aaa\" value=\"222\""));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void radioForBadName() throws Exception {
        Functions.radio("aaaArray", "111");
    }

    /**
     * @throws Exception
     */
    @Test
    public void select() throws Exception {
        request.setAttribute("aaa", new Integer(111));
        assertThat(
            Functions.select("aaa", "111"),
            is("value=\"111\" selected=\"selected\""));
        assertThat(Functions.select("aaa", "222"), is("value=\"222\""));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectForBadName() throws Exception {
        Functions.select("aaaArray", "111");
    }

    /**
     * @throws Exception
     */
    @Test
    public void multiselect() throws Exception {
        String[] aaaArray = new String[] { "111" };
        request.setAttribute("aaaArray", aaaArray);
        assertThat(
            Functions.multiselect("aaaArray", "111"),
            is("value=\"111\" selected=\"selected\""));
        assertThat(
            Functions.multiselect("aaaArray", "222"),
            is("value=\"222\""));
    }

    /**
     * @throws Exception
     */
    @Test
    public void multiselectForNull() throws Exception {
        assertThat(
            Functions.multiselect("aaaArray", "111"),
            is("value=\"111\""));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void multiselectForBadName() throws Exception {
        String[] aaaArray = new String[] { "111" };
        request.setAttribute("aaa", aaaArray);
        Functions.multiselect("aaa", "111");
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void multiselectForNotArray() throws Exception {
        String aaaArray = "111";
        request.setAttribute("aaaArray", aaaArray);
        Functions.multiselect("aaaArray", "111");
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void multiselectForNotStringArray() throws Exception {
        int[] aaaArray = new int[] { 1 };
        request.setAttribute("aaaArray", aaaArray);
        Functions.multiselect("aaaArray", "111");
    }

    /**
     * @throws Exception
     */
    @Test
    public void request() throws Exception {
        assertThat(Functions.request(), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void requestForCurrentRequestIsNull() throws Exception {
        RequestLocator.set(null);
        assertThat(Functions.request(), is(notNullValue()));
    }
}