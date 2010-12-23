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

import java.util.Date;

import org.junit.Test;
import org.slim3.controller.upload.FileUpload;
import org.slim3.tester.ControllerTestCase;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * @author higa
 * 
 */
public class ControllerTest extends ControllerTestCase {

    private IndexController controller = new IndexController();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        controller.servletContext = tester.servletContext;
        controller.request = tester.request;
        controller.response = tester.response;
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void forward() throws Exception {
        Navigation nav = controller.forward("index.jsp");
        assertThat(nav.getPath(), is("index.jsp"));
        assertThat(nav.isRedirect(), is(false));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void forwardForOtherController() throws Exception {
        Navigation nav = controller.forward("/hello/index");
        assertThat(nav.getPath(), is("/hello/index"));
        assertThat(nav.isRedirect(), is(false));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void redirect() throws Exception {
        Navigation nav = controller.redirect("index");
        assertThat(nav.getPath(), is("index"));
        assertThat(nav.isRedirect(), is(true));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void param() throws Exception {
        tester.request.setParameter("aaa", "111");
        assertThat(controller.param("aaa"), is("111"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void paramValues() throws Exception {
        String[] array = new String[] { "111" };
        tester.request.setParameter("aaa", array);
        assertThat(controller.paramValues("aaa"), is(array));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void requestScope() throws Exception {
        Integer value = 1;
        controller.requestScope("aaa", value);
        Integer returnValue = controller.requestScope("aaa");
        assertThat(returnValue, is(value));
        assertThat(tester.asInteger("aaa"), is(value));
        returnValue = controller.removeRequestScope("aaa");
        assertThat(returnValue, is(value));
        assertThat(tester.request.getAttribute("aaa"), is(nullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void sessionScope() throws Exception {
        Integer value = 1;
        controller.sessionScope("aaa", value);
        Integer returnValue = controller.sessionScope("aaa");
        assertThat(returnValue, is(value));
        assertThat(
            (Integer) tester.request.getSession().getAttribute("aaa"),
            is(value));
        returnValue = controller.removeSessionScope("aaa");
        assertThat(returnValue, is(value));
        assertThat(
            tester.request.getSession().getAttribute("aaa"),
            is(nullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void applicationScope() throws Exception {
        Integer value = 1;
        controller.applicationScope("aaa", value);
        Integer returnValue = controller.applicationScope("aaa");
        assertThat(returnValue, is(value));
        assertThat(
            (Integer) tester.servletContext.getAttribute("aaa"),
            is(value));
        returnValue = controller.removeApplicationScope("aaa");
        assertThat(returnValue, is(value));
        assertThat(tester.servletContext.getAttribute("aaa"), is(nullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void isDevelopment() throws Exception {
        assertThat(controller.isDevelopment(), is(false));
        System.setProperty(
            "com.google.appengine.runtime.environment",
            "Development");
        try {
            assertThat(controller.isDevelopment(), is(true));
        } finally {
            System.clearProperty("com.google.appengine.runtime.environment");
        }
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void download() throws Exception {
        controller.download("aaa.txt", new byte[] { 1 });
        byte[] bytes = tester.response.getOutputAsByteArray();
        assertThat(bytes.length, is(1));
        assertThat(bytes[0], is((byte) 1));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void asString() throws Exception {
        tester.request.setAttribute("aaa", "1");
        assertThat(controller.asString("aaa"), is("1"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void asShort() throws Exception {
        tester.request.setAttribute("aaa", "1");
        assertThat(controller.asShort("aaa"), is((short) 1));
        assertThat(controller.asShort("aaa", "###"), is((short) 1));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void asInteger() throws Exception {
        tester.request.setAttribute("aaa", "1");
        assertThat(controller.asInteger("aaa"), is(1));
        assertThat(controller.asInteger("aaa", "###"), is(1));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void asLong() throws Exception {
        tester.request.setAttribute("aaa", "1");
        assertThat(controller.asLong("aaa"), is((long) 1));
        assertThat(controller.asLong("aaa", "###"), is((long) 1));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void asFloat() throws Exception {
        tester.request.setAttribute("aaa", "1");
        assertThat(controller.asFloat("aaa"), is(1f));
        assertThat(controller.asFloat("aaa", "###"), is(1f));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void asDouble() throws Exception {
        tester.request.setAttribute("aaa", "1");
        assertThat(controller.asDouble("aaa"), is(1d));
        assertThat(controller.asDouble("aaa", "###"), is(1d));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void asDateForDatePattern() throws Exception {
        tester.request.setAttribute("aaa", "01011970");
        assertThat(controller.asDate("aaa", "MMddyyyy"), is(new Date(0)));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void asDateForTimePattern() throws Exception {
        tester.request.setAttribute("aaa", "000000");
        assertThat(controller.asDate("aaa", "hhmmss"), is(new Date(0)));
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
    public void asKeyForKeyIsNull() throws Exception {
        assertThat(controller.asKey("key"), is(nullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void isGet() throws Exception {
        tester.request.setMethod("get");
        assertThat(controller.isGet(), is(true));
        tester.request.setMethod("post");
        assertThat(controller.isGet(), is(false));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void isPost() throws Exception {
        tester.request.setMethod("post");
        assertThat(controller.isPost(), is(true));
        tester.request.setMethod("get");
        assertThat(controller.isPost(), is(false));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void isPut() throws Exception {
        tester.request.setMethod("put");
        assertThat(controller.isPut(), is(true));
        tester.request.setMethod("get");
        assertThat(controller.isPut(), is(false));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void isDelete() throws Exception {
        tester.request.setMethod("delete");
        assertThat(controller.isDelete(), is(true));
        tester.request.setMethod("get");
        assertThat(controller.isDelete(), is(false));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void encodeFileName() throws Exception {
        tester.request.setHeader("User-Agent", "MSIE");
        assertThat(controller.encodeFileName("abc"), is("filename=abc"));
        tester.request.setHeader("User-Agent", "Chrome");
        assertThat(controller.encodeFileName("abc"), is("filename=abc"));
        tester.request.setHeader("User-Agent", "Firefox/3.6");
        assertThat(controller.encodeFileName("abc"), is("filename*=utf8'abc"));
        tester.request.setHeader("User-Agent", "Safari");
        assertThat(controller.encodeFileName("abc"), is("filename=\"abc\""));
        tester.request.setHeader("User-Agent", "Opera");
        assertThat(controller.encodeFileName("abc"), is("filename=\"abc\""));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getForwardServletPath() throws Exception {
        tester.request.setAttribute(
            ControllerConstants.FORWARD_SERVLET_PATH_KEY,
            "/abc");
        assertThat(controller.getForwardServletPath(), is("/abc"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void createRequestHandler() throws Exception {
        assertThat(controller
            .createRequestHandler(tester.request)
            .getClass()
            .getName(), is(RequestHandler.class.getName()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void createRequestHandlerForMultipartRequest() throws Exception {
        tester.request.setContentType(FileUpload.MULTIPART);
        assertThat(controller
            .createRequestHandler(tester.request)
            .getClass()
            .getName(), is(MultipartRequestHandler.class.getName()));
    }

    private static class IndexController extends Controller {

        @Override
        public Navigation run() {
            return null;
        }
    }
}