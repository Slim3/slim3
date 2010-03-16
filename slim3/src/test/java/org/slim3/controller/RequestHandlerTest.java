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
package org.slim3.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.MockHttpServletRequest;
import org.slim3.tester.MockServletContext;

/**
 * @author higa
 * 
 */
public class RequestHandlerTest {

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request =
        new MockHttpServletRequest(servletContext);

    /**
     * @throws Exception
     * 
     */
    @Test
    public void handle() throws Exception {
        request.setParameter("aaa", "111");
        request.setParameter("bbbArray", new String[] { "222" });
        RequestHandler handler = new RequestHandler(request);
        handler.handle();
        assertThat((String) request.getAttribute("aaa"), is("111"));
        String[] bbbArray = (String[]) request.getAttribute("bbbArray");
        assertThat(bbbArray, is(not(nullValue())));
        assertThat(bbbArray.length, is(1));
        assertThat(bbbArray[0], is("222"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void normalizeValue() throws Exception {
        RequestHandler handler = new RequestHandler(request);
        assertThat(handler.normalizeValue(null), is(nullValue()));
        assertThat(handler.normalizeValue(""), is(""));
        assertThat(handler.normalizeValue("aaa"), is("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void normalizeValues() throws Exception {
        String[] values = new String[] { "", "aaa", null };
        RequestHandler handler = new RequestHandler(request);
        String[] ret = handler.normalizeValues(values);
        assertThat(ret.length, is(3));
        assertThat(ret[0], is(""));
        assertThat(ret[1], is("aaa"));
        assertThat(ret[2], is(nullValue()));
    }
}