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

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * @author higa
 * 
 */
public class ServletTesterTest {

    private ServletTester tester = new ServletTester();

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        tester.setUp();
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        tester.tearDown();
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void param() throws Exception {
        tester.param("aaa", "111");
        assertThat(tester.request.getParameter("aaa"), is("111"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void paramValues() throws Exception {
        String[] array = new String[] { "111" };
        tester.paramValues("aaa", array);
        assertThat(tester.request.getParameterValues("aaa"), is(array));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void requestScope() throws Exception {
        Integer value = 1;
        tester.requestScope("aaa", value);
        Integer returnValue = tester.requestScope("aaa");
        assertThat(returnValue, is(value));
        assertThat((Integer) tester.request.getAttribute("aaa"), is(value));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void asShort() throws Exception {
        tester.request.setAttribute("aaa", "1");
        assertThat(tester.asShort("aaa"), is(new Short("1")));
        assertThat(tester.asShort("aaa", "###"), is(new Short("1")));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void asInteger() throws Exception {
        tester.request.setAttribute("aaa", "1");
        assertThat(tester.asInteger("aaa"), is(new Integer("1")));
        assertThat(tester.asInteger("aaa", "###"), is(new Integer("1")));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void asLong() throws Exception {
        tester.request.setAttribute("aaa", "1");
        assertThat(tester.asLong("aaa"), is(new Long("1")));
        assertThat(tester.asLong("aaa", "###"), is(new Long("1")));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void asFloat() throws Exception {
        tester.request.setAttribute("aaa", "1");
        assertThat(tester.asFloat("aaa"), is(new Float("1")));
        assertThat(tester.asFloat("aaa", "###"), is(new Float("1")));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void asDouble() throws Exception {
        tester.request.setAttribute("aaa", "1");
        assertThat(tester.asDouble("aaa"), is(new Double("1")));
        assertThat(tester.asDouble("aaa", "###"), is(new Double("1")));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void asDateForDatePattern() throws Exception {
        tester.request.setAttribute("aaa", "01011970");
        assertThat(tester.asDate("aaa", "MMddyyyy"), is(new Date(0)));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void asDateForTimePattern() throws Exception {
        tester.request.setAttribute("aaa", "000000");
        assertThat(tester.asDate("aaa", "hhmmss"), is(new Date(0)));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void asKey() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        tester.request.setAttribute("key", KeyFactory.keyToString(key));
        assertThat(tester.asKey("key"), is(key));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void sessionScope() throws Exception {
        Integer value = 1;
        tester.sessionScope("aaa", value);
        Integer returnValue = tester.sessionScope("aaa");
        assertThat(returnValue, is(value));
        assertThat(
            (Integer) tester.request.getSession().getAttribute("aaa"),
            is(value));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void applicationScope() throws Exception {
        Integer value = 1;
        tester.applicationScope("aaa", value);
        Integer returnValue = tester.applicationScope("aaa");
        assertThat(returnValue, is(value));
        assertThat(
            (Integer) tester.servletContext.getAttribute("aaa"),
            is(value));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void isRedirectWhenRedirect() throws Exception {
        assertThat(tester.isRedirect(), is(false));
        tester.response.sendRedirect("/");
        assertThat(tester.isRedirect(), is(true));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void isRedirectWhenForward() throws Exception {
        assertThat(tester.isRedirect(), is(false));
        tester.servletContext.getRequestDispatcher("/").forward(
            tester.request,
            tester.response);
        assertThat(tester.isRedirect(), is(false));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getDestinationPathWhenRedirect() throws Exception {
        assertThat(tester.getDestinationPath(), is(nullValue()));
        tester.response.sendRedirect("/");
        assertThat(tester.getDestinationPath(), is("/"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getDestinationPathForward() throws Exception {
        assertThat(tester.getDestinationPath(), is(nullValue()));
        tester.servletContext.getRequestDispatcher("/").forward(
            tester.request,
            tester.response);
        assertThat(tester.getDestinationPath(), is("/"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void addBlobKey() throws Exception {
        String name = "aaa";
        String value = "hoge";
        BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
        tester.addBlobKey(name, value);
        assertThat(
            bs.getUploadedBlobs(tester.request).get(name),
            is(new BlobKey(value)));
        tester.servletContext.getRequestDispatcher("/").forward(
            tester.request,
            tester.response);
        assertThat(tester.getDestinationPath(), is("/"));
    }
}