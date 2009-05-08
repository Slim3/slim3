/*
 * Copyright 2004-2009 the original author or authors.
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
package org.slim3.mvc.controller;

import java.util.Locale;

import org.slim3.commons.config.Configuration;
import org.slim3.mvc.MvcConstants;
import org.slim3.mvc.controller.controller.HogeController;
import org.slim3.mvc.controller.controller.IndexController;
import org.slim3.mvc.controller.controller.hello.ListController;
import org.slim3.mvc.unit.MvcTestCase;

/**
 * @author higa
 * 
 */
public class FrontControllerTest extends MvcTestCase {

    /**
     * 
     */
    protected static final String PACKAGE = "org/slim3/mvc/controller/";

    /**
     * 
     */
    protected static final String CONFIG_PATH =
        PACKAGE + "slim3_configuration.properties";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Configuration.initialize(CONFIG_PATH);
    }

    /**
     * @throws Exception
     * 
     */
    public void testInit() throws Exception {
        mvcTester.frontController.init(mvcTester.filterConfig);
        assertEquals(
            MvcConstants.DEFAULT_REQUEST_CHARSET,
            mvcTester.frontController.charset);
        assertNotNull(ServletContextLocator.getServletContext());
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetPath() throws Exception {
        mvcTester.request.setPathInfo("/hello/");
        assertEquals("/hello/", mvcTester.frontController
            .getPath(mvcTester.request));
    }

    /**
     * @throws Exception
     * 
     */
    public void testToControllerClassName() throws Exception {
        assertEquals(ListController.class.getName(), mvcTester.frontController
            .toControllerClassName("/hello/list"));
        assertEquals(HogeController.class.getName(), mvcTester.frontController
            .toControllerClassName("/hoge"));
        assertEquals(IndexController.class.getName(), mvcTester.frontController
            .toControllerClassName("/"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateController() throws Exception {
        Controller controller =
            mvcTester.frontController.createController("/hello/list");
        assertNotNull(controller);
        assertEquals(ListController.class, controller.getClass());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateControllerForRoot() throws Exception {
        Controller controller =
            mvcTester.frontController.createController("/hoge");
        assertNotNull(controller);
        assertEquals(HogeController.class, controller.getClass());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateControllerForRootIndexController() throws Exception {
        Controller controller = mvcTester.frontController.createController("/");
        assertNotNull(controller);
        assertEquals(IndexController.class, controller.getClass());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateControllerForIndexController() throws Exception {
        Controller controller =
            mvcTester.frontController.createController("/hello/");
        assertNotNull(controller);
        assertEquals(
            org.slim3.mvc.controller.controller.hello.IndexController.class,
            controller.getClass());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateControllerForBadController() throws Exception {
        try {
            mvcTester.frontController.createController("/bad");
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetController() throws Exception {
        Controller controller =
            mvcTester.frontController.getController(
                mvcTester.request,
                mvcTester.response,
                "/");
        assertNotNull(controller.getServletContext());
        assertNotNull(controller.getRequest());
        assertNotNull(controller.getResponse());
        assertEquals("/", controller.getPath());
        assertSame(controller, mvcTester.request
            .getAttribute(MvcConstants.CONTROLLER_KEY));
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetControllerForJsp() throws Exception {
        assertNull(mvcTester.frontController.getController(
            mvcTester.request,
            mvcTester.response,
            "/index.jsp"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testExecuteController() throws Exception {
        Controller controller = mvcTester.frontController.createController("/");
        Navigation navigaton =
            mvcTester.frontController.executeController(
                mvcTester.request,
                mvcTester.response,
                controller);
        assertNotNull(navigaton);
        assertFalse(navigaton.isRedirect());
        assertEquals("index.jsp", navigaton.getPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testDoRedirect() throws Exception {
        String path = "http://www.google.com";
        Controller controller = mvcTester.frontController.createController("/");
        mvcTester.frontController.doRedirect(
            mvcTester.request,
            mvcTester.response,
            controller,
            path);
        assertEquals(path, mvcTester.response.getRedirectPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testDoRedirectForContextRelativePath() throws Exception {
        mvcTester.servletContext.setContextPath("/slim3-tutorial");
        Controller controller = mvcTester.frontController.createController("/");
        mvcTester.frontController.doRedirect(
            mvcTester.request,
            mvcTester.response,
            controller,
            "/hello/list");
        assertEquals("/slim3-tutorial/hello/list", mvcTester.response
            .getRedirectPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testDoForward() throws Exception {
        Controller controller =
            mvcTester.frontController.getController(
                mvcTester.request,
                mvcTester.response,
                "/");
        mvcTester.frontController.doForward(
            mvcTester.request,
            mvcTester.response,
            controller,
            "index.jsp");
        assertEquals("/index.jsp", mvcTester.servletContext
            .getLatestRequestDispatcher()
            .getPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testDoForwardForApplicationRelativePath() throws Exception {
        Controller controller =
            mvcTester.frontController.getController(
                mvcTester.request,
                mvcTester.response,
                "/hoge");
        mvcTester.frontController.doForward(
            mvcTester.request,
            mvcTester.response,
            controller,
            "index.jsp");
        assertEquals("/index.jsp", mvcTester.servletContext
            .getLatestRequestDispatcher()
            .getPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testHandleNavigationForNoNavigation() throws Exception {
        Controller controller = mvcTester.frontController.createController("/");
        mvcTester.frontController.handleNavigation(
            mvcTester.request,
            mvcTester.response,
            controller,
            null);
        assertNull(mvcTester.response.getRedirectPath());
        assertNull(mvcTester.servletContext.getLatestRequestDispatcher());
    }

    /**
     * @throws Exception
     * 
     */
    public void testHandleNavigationForRedirect() throws Exception {
        String path = "http://www.google.com";
        Controller controller = mvcTester.frontController.createController("/");
        Navigation navigation = controller.redirect(path);
        mvcTester.frontController.handleNavigation(
            mvcTester.request,
            mvcTester.response,
            controller,
            navigation);
        assertEquals(path, mvcTester.response.getRedirectPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testHandleNavigationForForward() throws Exception {
        Controller controller =
            mvcTester.frontController.getController(
                mvcTester.request,
                mvcTester.response,
                "/");
        Navigation navigation = controller.forward();
        mvcTester.frontController.handleNavigation(
            mvcTester.request,
            mvcTester.response,
            controller,
            navigation);
        assertEquals("/index.jsp", mvcTester.servletContext
            .getLatestRequestDispatcher()
            .getPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testBindParameters() throws Exception {
        mvcTester.setParameter("aaa", "111");
        Controller controller =
            mvcTester.frontController.getController(
                mvcTester.request,
                mvcTester.response,
                "/");
        mvcTester.frontController.bindParameters(
            mvcTester.request,
            mvcTester.response,
            controller);
        assertNotNull(controller.getParameters());
        IndexController indexController = (IndexController) controller;
        assertEquals("111", indexController.getAaa());
    }

    /**
     * @throws Exception
     * 
     */
    public void testProcessController() throws Exception {
        mvcTester.setParameter("aaa", "111");
        Controller controller =
            mvcTester.frontController.getController(
                mvcTester.request,
                mvcTester.response,
                "/");
        mvcTester.frontController.processController(
            mvcTester.request,
            mvcTester.response,
            controller);
        assertNotNull(controller.getServletContext());
        assertNotNull(controller.getRequest());
        assertNotNull(controller.getResponse());
        assertEquals("/index.jsp", mvcTester.servletContext
            .getLatestRequestDispatcher()
            .getPath());
        IndexController indexController = (IndexController) controller;
        assertEquals("111", indexController.getAaa());
    }

    /**
     * @throws Exception
     * 
     */
    public void testDoFilter() throws Exception {
        mvcTester.request.setPathInfo("/");
        mvcTester.frontController.doFilter(
            mvcTester.request,
            mvcTester.response,
            mvcTester.filterChain);
        assertEquals("UTF-8", mvcTester.request.getCharacterEncoding());
        assertEquals("/index.jsp", mvcTester.servletContext
            .getLatestRequestDispatcher()
            .getPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetLocaleFromSession() throws Exception {
        Locale locale = Locale.ENGLISH;
        Controller controller =
            mvcTester.frontController.getController(
                mvcTester.request,
                mvcTester.response,
                "/");
        controller.setLocale(locale);
        assertEquals(locale, controller.getLocale());
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetLocaleFromRequest() throws Exception {
        Locale locale = Locale.ENGLISH;
        mvcTester.request.addLocale(locale);
        Controller controller =
            mvcTester.frontController.getController(
                mvcTester.request,
                mvcTester.response,
                "/");
        assertEquals(locale, controller.getLocale());
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetLocaleForDefault() throws Exception {
        Controller controller =
            mvcTester.frontController.getController(
                mvcTester.request,
                mvcTester.response,
                "/");
        assertEquals(Locale.getDefault(), controller.getLocale());
    }
}