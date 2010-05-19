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
import org.slim3.controller.router.Routing.PlaceHolderFragment;
import org.slim3.controller.router.Routing.StringFragment;
import org.slim3.tester.MockHttpServletRequest;
import org.slim3.tester.MockServletContext;

/**
 * @author higa
 * 
 */
public class RoutingTest {

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request =
        new MockHttpServletRequest(servletContext);

    /**
     * @throws Exception
     */
    @Test
    public void setFrom() throws Exception {
        Routing routing =
            new Routing("/abc/{xxx}/{yyy}", "/abc?xxx=${xxx}&yyy=${yyy}");
        assertThat(routing.from, is("/abc/{xxx}/{yyy}"));
        assertThat(routing.fromPattern.pattern(), is("^/abc/([^/]+)/([^/]+)$"));
        assertThat(routing.placeHolderList.size(), is(2));
        assertThat(routing.placeHolderList.get(0), is("xxx"));
        assertThat(routing.placeHolderList.get(1), is("yyy"));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void setFromWhenStartCurlyBracketIsMissing() throws Exception {
        new Routing("/abc/{xxx}/yyy}", "/abc?xxx=${xxx}&yyy=${yyy}");
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void setFromWhenEndCurlyBracketIsMissing() throws Exception {
        new Routing("/abc/{xxx/{yyy}", "/abc?xxx=${xxx}&yyy=${yyy}");
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void setFromWhenLastEndCurlyBracketIsMissing() throws Exception {
        new Routing("/abc/{xxx}/{yyy", "/abc?xxx=${xxx}&yyy=${yyy}");
    }

    /**
     * @throws Exception
     */
    @Test
    public void setTo() throws Exception {
        Routing routing =
            new Routing("/abc/{xxx}/{yyy}", "/abc?xxx={xxx}&yyy={yyy}");
        assertThat(routing.to, is("/abc?xxx={xxx}&yyy={yyy}"));
        assertThat(routing.toFragmentList.size(), is(4));
        assertThat(
            ((StringFragment) routing.toFragmentList.get(0)).value,
            is("/abc?xxx="));
        assertThat(
            ((PlaceHolderFragment) routing.toFragmentList.get(1)).name,
            is("xxx"));
        assertThat(
            ((StringFragment) routing.toFragmentList.get(2)).value,
            is("&yyy="));
        assertThat(
            ((PlaceHolderFragment) routing.toFragmentList.get(3)).name,
            is("yyy"));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void setToWhenNameIsNotFound() throws Exception {
        new Routing("/abc/{xxx}/{yyy}", "/abc?zzz={zzz}");
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void setToWhenStartCurlyBracketIsMissing() throws Exception {
        new Routing("/abc/{xxx}/{yyy}", "/abc?xxx=xxx}&yyy={yyy}");
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void setToWhenEndCurlyBracketIsMissing() throws Exception {
        new Routing("/abc/{xxx}/{yyy}", "/abc?xxx={xxx&yyy={yyy}");
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void setToWhenLastEndCurlyBracketIsMissing() throws Exception {
        new Routing("/abc/{xxx}/{yyy}", "/abc?xxx={xxx}&yyy={yyy");
    }

    /**
     * @throws Exception
     */
    @Test
    public void route() throws Exception {
        Routing routing =
            new Routing("/abc/{xxx}/{yyy}", "/abc?xxx={xxx}&yyy={yyy}");
        assertThat(
            routing.route(request, "/abc/111/222"),
            is("/abc?xxx=111&yyy=222"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void routeForMail() throws Exception {
        Routing routing =
            new Routing(
                "/_ah/mail/{address}",
                "/mail/receive?address={address}");
        assertThat(
            routing.route(request, "/_ah/mail/admin@appspotmail.com"),
            is("/mail/receive?address=admin%40appspotmail.com"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void routeForNoMatchPath() throws Exception {
        Routing routing =
            new Routing("/abc/{xxx}/{yyy}", "/abc?xxx={xxx}&yyy={yyy}");
        assertThat(routing.route(request, "/abc/111/"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void routeForNoPlaceHolder() throws Exception {
        Routing routing = new Routing("/abc/", "/xyz/");
        assertThat(routing.route(request, "/abc/"), is("/xyz/"));
        assertThat(routing.route(request, "/abc/xxx"), is(nullValue()));
    }
}