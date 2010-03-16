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
package org.slim3.util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.MockHttpServletRequest;
import org.slim3.tester.MockServletContext;

/**
 * @author higa
 * 
 */
public class RequestUtilTest {

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request =
        new MockHttpServletRequest(servletContext);

    /**
     * @throws Exception
     */
    @Test
    public void getPath() throws Exception {
        request.setServletPath("/aaa");
        assertThat(RequestUtil.getPath(request), is("/aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getExtension() throws Exception {
        assertThat(RequestUtil.getExtension("aaa.html"), is("html"));
        assertThat(RequestUtil.getExtension("/aaa.html"), is("html"));
        assertThat(RequestUtil.getExtension("/aaa"), is(nullValue()));
        assertThat(RequestUtil.getExtension("/aaa.bbb/ccc"), is(nullValue()));
    }
}
