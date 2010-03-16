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
package org.slim3.tester;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

/**
 * @author higa
 * 
 */
public class MockHttpServletResponseTest {

    private MockHttpServletResponse response = new MockHttpServletResponse();

    /**
     * @throws Exception
     */
    @Test
    public void writer() throws Exception {
        Writer writer = response.getWriter();
        String data = "abc";
        writer.write(data);
        response.flushBuffer();
        assertThat(response.getOutputAsString(), is(data));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void header() throws Exception {
        response.setHeader("aaa", "111");
        assertThat(response.getHeader("aaa"), is("111"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void intHeader() throws Exception {
        response.setIntHeader("aaa", 1);
        assertThat(response.getHeader("aaa"), is("1"));
        assertThat(response.getIntHeader("aaa"), is(1));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void dateHeader() throws Exception {
        response.setDateHeader("aaa", 1000);
        System.out.println(response.getHeader("aaa"));
        assertThat(response.getDateHeader("aaa"), is(1000L));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getStatus() throws Exception {
        assertThat(response.getStatus(), is(HttpServletResponse.SC_OK));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void setRedirect() throws Exception {
        response.sendRedirect("/abc");
        assertThat(response.redirectPath, is("/abc"));
        assertThat(
            response.getStatus(),
            is(HttpServletResponse.SC_MOVED_TEMPORARILY));
    }
}