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
package org.slim3.controller.upload;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

/**
 * @author higa
 * 
 */
public class FileItemHeadersTest {

    /**
     * @throws Exception
     */
    @Test
    public void all() throws Exception {
        FileItemHeaders headers = new FileItemHeaders();
        headers.addHeader("aaa", "aaa1");
        headers.addHeader("aaa", "aaa2");
        headers.addHeader("bbb", "bbb1");
        assertThat(headers.getHeader("aaa"), is("aaa1"));
        Iterator<String> i = headers.getHeaders("aaa");
        assertThat(i.next(), is("aaa1"));
        assertThat(i.next(), is("aaa2"));
        assertThat(i.hasNext(), is(false));
        Iterator<String> names = headers.getHeaderNames();
        assertThat(names.next(), is("aaa"));
        assertThat(names.next(), is("bbb"));
        assertThat(names.hasNext(), is(false));
    }
}