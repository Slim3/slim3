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
import org.slim3.util.ByteUtil;
import org.slim3.util.Cleaner;

/**
 * @author higa
 * 
 */
public class HotHttpSessionWrapperTest {

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request =
        new MockHttpServletRequest(servletContext);

    private HotHttpServletRequestWrapper requestWrapper =
        new HotHttpServletRequestWrapper(request);

    /**
     * @throws Exception
     * 
     */
    @Test
    public void cleanAll() throws Exception {
        HotHttpSessionWrapper sessionWrapper =
            (HotHttpSessionWrapper) requestWrapper.getSession();
        sessionWrapper.setAttribute("aaa", "111");
        sessionWrapper.setAttribute("__aaa", "111");
        Cleaner.cleanAll();
        assertThat(
            request.getSession().getAttribute("aaa"),
            is(BytesHolder.class));
        assertThat(request.getSession().getAttribute("__aaa"), is(String.class));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void constructorAndGetAttribute() throws Exception {
        request.getSession().setAttribute(
            "aaa",
            new BytesHolder(ByteUtil.toByteArray("111")));
        HotHttpSessionWrapper sessionWrapper =
            (HotHttpSessionWrapper) requestWrapper.getSession();
        assertThat((String) request.getSession().getAttribute("aaa"), is("111"));
        assertThat((String) sessionWrapper.getAttribute("aaa"), is("111"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void invalidate() throws Exception {
        HotHttpSessionWrapper sessionWrapper =
            (HotHttpSessionWrapper) requestWrapper.getSession();
        sessionWrapper.invalidate();
        assertThat(sessionWrapper.originalSession, is(nullValue()));
        assertThat(sessionWrapper.requestWrapper, is(nullValue()));
        assertThat(requestWrapper.sessionWrapper, is(nullValue()));
        sessionWrapper.invalidate();
    }
}