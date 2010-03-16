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
import org.slim3.tester.MockHttpServletResponse;
import org.slim3.tester.MockRequestDispatcher;
import org.slim3.tester.MockServletContext;

/**
 * @author higa
 * 
 */
public class HotRequestDispatcherWrapperTest {

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request =
        new MockHttpServletRequest(servletContext);

    private MockHttpServletResponse response = new MockHttpServletResponse();

    private HotHttpServletRequestWrapper requestWrapper =
        new HotHttpServletRequestWrapper(request);

    private MockRequestDispatcher dispatcher =
        new MockRequestDispatcher("/index.jsp");

    private HotRequestDispatcherWrapper dispatcherWrapper =
        new HotRequestDispatcherWrapper(dispatcher);

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getOriginal() throws Exception {
        assertThat((MockHttpServletRequest) dispatcherWrapper
            .getOriginalRequest(request), is(sameInstance(request)));
        assertThat((MockHttpServletRequest) dispatcherWrapper
            .getOriginalRequest(requestWrapper), is(sameInstance(request)));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void forward() throws Exception {
        dispatcherWrapper.forward(request, response);
        assertThat(
            (MockHttpServletRequest) dispatcher.getRequest(),
            is(sameInstance(request)));
        assertThat(
            (MockHttpServletResponse) dispatcher.getResponse(),
            is(sameInstance(response)));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void forwardForRequestWrapper() throws Exception {
        dispatcherWrapper.forward(requestWrapper, response);
        assertThat(
            (MockHttpServletRequest) dispatcher.getRequest(),
            is(sameInstance(request)));
        assertThat(
            (MockHttpServletResponse) dispatcher.getResponse(),
            is(sameInstance(response)));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void include() throws Exception {
        dispatcherWrapper.include(request, response);
        assertThat(
            (MockHttpServletRequest) dispatcher.getRequest(),
            is(sameInstance(request)));
        assertThat(
            (MockHttpServletResponse) dispatcher.getResponse(),
            is(sameInstance(response)));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void includeForRequestWrapper() throws Exception {
        dispatcherWrapper.include(requestWrapper, response);
        assertThat(
            (MockHttpServletRequest) dispatcher.getRequest(),
            is(sameInstance(request)));
        assertThat(
            (MockHttpServletResponse) dispatcher.getResponse(),
            is(sameInstance(response)));
    }
}