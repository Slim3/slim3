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
package org.slim3.controller.router;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.MockHttpServletRequest;
import org.slim3.tester.MockServletContext;

/**
 * @author higa
 * 
 */
public class RouterImplTest {

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request =
        new MockHttpServletRequest(servletContext);

    private MyRouter router = new MyRouter();

    /**
     * @throws Exception
     * 
     */
    @Test
    public void isStatic() throws Exception {
        assertThat(
            router.isStatic("/_ah/mail/admin@appspotmail.com"),
            is(false));
        assertThat(router.isStatic("/service.s3get"), is(false));
        assertThat(router.isStatic("/global.css"), is(true));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void route() throws Exception {
        assertThat(
            router.route(request, "/_ah/mail/hoge"),
            is("/mail?address=hoge"));
        assertThat(router.route(request, "/blog/edit/1"), is("/blog/edit?id=1"));
        assertThat(router.route(request, "/abc"), is(nullValue()));
        assertThat(router.route(request, "/xxx/"), is("/yyy/"));
        assertThat(router.route(request, "/1/xxx/"), is("/1/yyy/"));
    }

    private static class MyRouter extends RouterImpl {

        /**
         * 
         */
        public MyRouter() {
            addRouting("/_ah/mail/{address}", "/mail?address={address}");
            addRouting("/{app}/edit/{id}", "/{app}/edit?id={id}");
            addRouting("/xxx/", "/yyy/");
            addRouting("/{id}/xxx/", "/{id}/yyy/");
        }
    }
}