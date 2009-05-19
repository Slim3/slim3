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
package org.slim3.controller;

import org.slim3.controller.controller.HogeController;
import org.slim3.controller.controller.IndexController;
import org.slim3.controller.controller.hello.ListController;
import org.slim3.tester.ControllerTestCase;

/**
 * @author higa
 * 
 */
public class FrontControllerTest extends ControllerTestCase {

    private static final String CONTROLLER_PACKAGE =
        FrontControllerTest.class.getPackage().getName() + ".controller";

    @Override
    protected void setUp() throws Exception {
        System.setProperty(
            ControllerConstants.CONTROLLER_PACKAGE_KEY,
            CONTROLLER_PACKAGE);
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        System.clearProperty(ControllerConstants.CONTROLLER_PACKAGE_KEY);
        super.tearDown();
    }

    /**
     * @throws Exception
     * 
     */
    public void testInit() throws Exception {
        controllerTester.frontController.init(controllerTester.filterConfig);
        assertEquals(
            ControllerConstants.DEFAULT_REQUEST_CHARSET,
            controllerTester.frontController.charset);
        assertNotNull(ServletContextLocator.getServletContext());
        assertFalse(controllerTester.frontController.hotReloading);
        assertEquals(
            CONTROLLER_PACKAGE,
            controllerTester.frontController.controllerPackageName);
    }

    /**
     * @throws Exception
     * 
     */
    public void testToControllerClassName() throws Exception {
        assertEquals(
            ListController.class.getName(),
            controllerTester.frontController
                .toControllerClassName("/hello/list"));
        assertEquals(
            HogeController.class.getName(),
            controllerTester.frontController.toControllerClassName("/hoge"));
        assertEquals(
            IndexController.class.getName(),
            controllerTester.frontController.toControllerClassName("/"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateController() throws Exception {
        Controller controller =
            controllerTester.frontController.createController("/hello/list");
        assertNotNull(controller);
        assertEquals(ListController.class, controller.getClass());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateControllerForRoot() throws Exception {
        Controller controller =
            controllerTester.frontController.createController("/hoge");
        assertNotNull(controller);
        assertEquals(HogeController.class, controller.getClass());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateControllerForRootIndexController() throws Exception {
        Controller controller =
            controllerTester.frontController.createController("/");
        assertNotNull(controller);
        assertEquals(IndexController.class, controller.getClass());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateControllerForIndexController() throws Exception {
        Controller controller =
            controllerTester.frontController.createController("/hello/");
        assertNotNull(controller);
        assertEquals(
            org.slim3.controller.controller.hello.IndexController.class,
            controller.getClass());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateControllerForBadController() throws Exception {
        try {
            controllerTester.frontController.createController("/bad");
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateControllerForClassNotFound() throws Exception {
        assertNull(controllerTester.frontController.createController("/xxx"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetController() throws Exception {
        setParameter("aaa", "111");
        Controller controller =
            controllerTester.frontController.getController(
                controllerTester.request,
                controllerTester.response,
                "/");
        assertNotNull(controller.servletContext);
        assertNotNull(controller.request);
        assertNotNull(controller.response);
        assertEquals("/", controller.basePath);
        assertSame(controller, controllerTester.request
            .getAttribute(ControllerConstants.CONTROLLER_KEY));
        assertEquals("111", getAttribute("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetControllerForJsp() throws Exception {
        assertNull(controllerTester.frontController.getController(
            controllerTester.request,
            controllerTester.response,
            "/index.jsp"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testDoRedirect() throws Exception {
        String path = "http://www.google.com";
        Controller controller =
            controllerTester.frontController.createController("/");
        controllerTester.frontController.doRedirect(
            controllerTester.request,
            controllerTester.response,
            controller,
            path);
        assertEquals(path, controllerTester.response.getRedirectPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testDoRedirectForContextRelativePath() throws Exception {
        controllerTester.servletContext.setContextPath("/slim3-tutorial");
        Controller controller =
            controllerTester.frontController.createController("/");
        controllerTester.frontController.doRedirect(
            controllerTester.request,
            controllerTester.response,
            controller,
            "/hello/list");
        assertEquals("/slim3-tutorial/hello/list", controllerTester.response
            .getRedirectPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testDoForward() throws Exception {
        Controller controller =
            controllerTester.frontController.getController(
                controllerTester.request,
                controllerTester.response,
                "/");
        controllerTester.frontController.doForward(
            controllerTester.request,
            controllerTester.response,
            controller,
            "index.jsp");
        assertEquals("/index.jsp", controllerTester.servletContext
            .getLatestRequestDispatcher()
            .getPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testDoForwardForApplicationRelativePath() throws Exception {
        Controller controller =
            controllerTester.frontController.getController(
                controllerTester.request,
                controllerTester.response,
                "/hoge");
        controllerTester.frontController.doForward(
            controllerTester.request,
            controllerTester.response,
            controller,
            "index.jsp");
        assertEquals("/index.jsp", controllerTester.servletContext
            .getLatestRequestDispatcher()
            .getPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testHandleNavigationForNoNavigation() throws Exception {
        Controller controller =
            controllerTester.frontController.createController("/");
        controllerTester.frontController.handleNavigation(
            controllerTester.request,
            controllerTester.response,
            controller,
            null);
        assertNull(controllerTester.response.getRedirectPath());
        assertNull(controllerTester.servletContext.getLatestRequestDispatcher());
    }

    /**
     * @throws Exception
     * 
     */
    public void testHandleNavigationForRedirect() throws Exception {
        String path = "http://www.google.com";
        Controller controller =
            controllerTester.frontController.createController("/");
        Navigation navigation = controller.redirect(path);
        controllerTester.frontController.handleNavigation(
            controllerTester.request,
            controllerTester.response,
            controller,
            navigation);
        assertEquals(path, controllerTester.response.getRedirectPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testHandleNavigationForForward() throws Exception {
        Controller controller =
            controllerTester.frontController.getController(
                controllerTester.request,
                controllerTester.response,
                "/");
        Navigation navigation = controller.forward("index.jsp");
        controllerTester.frontController.handleNavigation(
            controllerTester.request,
            controllerTester.response,
            controller,
            navigation);
        assertEquals("/index.jsp", controllerTester.servletContext
            .getLatestRequestDispatcher()
            .getPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testProcessController() throws Exception {
        Controller controller =
            controllerTester.frontController.getController(
                controllerTester.request,
                controllerTester.response,
                "/");
        controllerTester.frontController.processController(
            controllerTester.request,
            controllerTester.response,
            controller);
        assertNotNull(controller.servletContext);
        assertNotNull(controller.request);
        assertNotNull(controller.response);
        assertEquals("/index.jsp", controllerTester.servletContext
            .getLatestRequestDispatcher()
            .getPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testDoFilter() throws Exception {
        controllerTester.request.setServletPath("/");
        controllerTester.frontController.doFilter(
            controllerTester.request,
            controllerTester.response,
            controllerTester.filterChain);
        assertEquals("UTF-8", controllerTester.request.getCharacterEncoding());
        assertEquals("/index.jsp", controllerTester.servletContext
            .getLatestRequestDispatcher()
            .getPath());
    }
}