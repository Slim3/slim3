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
import org.slim3.controller.validator.Errors;
import org.slim3.tester.ControllerTestCase;
import org.slim3.util.ServletContextLocator;

/**
 * @author higa
 * 
 */
public class FrontControllerTest extends ControllerTestCase {

    private static final String ROOT_PACKAGE =
        FrontControllerTest.class.getPackage().getName();

    @Override
    protected void setUpContextParameter() {
        super.setUpContextParameter();
        application.setInitParameter(
            ControllerConstants.ROOT_PACKAGE_KEY,
            ROOT_PACKAGE);
    }

    /**
     * @throws Exception
     * 
     */
    public void testInit() throws Exception {
        assertEquals(
            ControllerConstants.DEFAULT_REQUEST_CHARSET,
            frontController.charset);
        assertEquals(
            ControllerConstants.DEFAULT_LOCALIZATION_CONTEXT,
            frontController.bundleName);
        assertNotNull(ServletContextLocator.get());
        assertFalse(frontController.hotReloading);
        assertEquals(ROOT_PACKAGE, frontController.rootPackageName);
        String[] names = frontController.staticPackageNames;
        assertEquals(1, names.length);
        assertEquals("model", names[0]);
    }

    /**
     * @throws Exception
     * 
     */
    public void testToControllerClassName() throws Exception {
        assertEquals(ListController.class.getName(), frontController
            .toControllerClassName("/hello/list"));
        assertEquals(HogeController.class.getName(), frontController
            .toControllerClassName("/hoge"));
        assertEquals(IndexController.class.getName(), frontController
            .toControllerClassName("/"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateController() throws Exception {
        Controller controller = frontController.createController("/hello/list");
        assertNotNull(controller);
        assertEquals(ListController.class, controller.getClass());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateControllerForRoot() throws Exception {
        Controller controller = frontController.createController("/hoge");
        assertNotNull(controller);
        assertEquals(HogeController.class, controller.getClass());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateControllerForRootIndexController() throws Exception {
        Controller controller = frontController.createController("/");
        assertNotNull(controller);
        assertEquals(IndexController.class, controller.getClass());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateControllerForIndexController() throws Exception {
        Controller controller = frontController.createController("/hello/");
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
            frontController.createController("/bad");
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
        assertNull(frontController.createController("/xxx"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetController() throws Exception {
        Controller controller =
            frontController.getController(request, response, "/");
        assertNotNull(controller.application);
        assertNotNull(controller.request);
        assertNotNull(controller.response);
        assertEquals("/", controller.basePath);
        assertSame(controller, requestScope(ControllerConstants.CONTROLLER_KEY));
        Errors errors = controller.errors;
        assertNotNull(errors);
        assertSame(errors, requestScope(ControllerConstants.ERRORS_KEY));
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetControllerForErrors() throws Exception {
        Errors errors = new Errors();
        requestScope(ControllerConstants.ERRORS_KEY, errors);
        Controller controller =
            frontController.getController(request, response, "/");
        assertSame(errors, controller.errors);
        assertSame(errors, requestScope(ControllerConstants.ERRORS_KEY));
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetControllerForJsp() throws Exception {
        assertNull(frontController.getController(
            request,
            response,
            "/index.jsp"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testDoRedirect() throws Exception {
        String path = "http://www.google.com";
        Controller controller = frontController.createController("/");
        frontController.doRedirect(request, response, controller, path);
        assertEquals(path, response.getRedirectPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testDoRedirectForContextRelativePath() throws Exception {
        application.setContextPath("/slim3-tutorial");
        Controller controller = frontController.createController("/");
        frontController
            .doRedirect(request, response, controller, "/hello/list");
        assertEquals("/slim3-tutorial/hello/list", response.getRedirectPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testDoForward() throws Exception {
        Controller controller =
            frontController.getController(request, response, "/");
        frontController.doForward(request, response, controller, "index.jsp");
        assertEquals("/index.jsp", application
            .getLatestRequestDispatcher()
            .getPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testDoForwardForApplicationRelativePath() throws Exception {
        Controller controller =
            frontController.getController(request, response, "/hoge");
        frontController.doForward(request, response, controller, "index.jsp");
        assertEquals("/index.jsp", application
            .getLatestRequestDispatcher()
            .getPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testHandleNavigationForNoNavigation() throws Exception {
        Controller controller = frontController.createController("/");
        frontController.handleNavigation(request, response, controller, null);
        assertNull(response.getRedirectPath());
        assertNull(application.getLatestRequestDispatcher());
    }

    /**
     * @throws Exception
     * 
     */
    public void testHandleNavigationForRedirect() throws Exception {
        String path = "http://www.google.com";
        Controller controller = frontController.createController("/");
        Navigation navigation = controller.redirect(path);
        frontController.handleNavigation(
            request,
            response,
            controller,
            navigation);
        assertEquals(path, response.getRedirectPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testHandleNavigationForForward() throws Exception {
        Controller controller =
            frontController.getController(request, response, "/");
        Navigation navigation = controller.forward("index.jsp");
        frontController.handleNavigation(
            request,
            response,
            controller,
            navigation);
        assertEquals("/index.jsp", application
            .getLatestRequestDispatcher()
            .getPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testProcessController() throws Exception {
        param("aaa", "111");
        Controller controller =
            frontController.getController(request, response, "/");
        frontController.processController(request, response, controller);
        assertNotNull(controller.application);
        assertNotNull(controller.request);
        assertNotNull(controller.response);
        assertEquals("/index.jsp", application
            .getLatestRequestDispatcher()
            .getPath());
        assertEquals("111", requestScope("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testDoFilter() throws Exception {
        request.setServletPath("/");
        frontController.doFilter(request, response, filterChain);
        assertEquals("UTF-8", request.getCharacterEncoding());
        assertEquals("/index.jsp", application
            .getLatestRequestDispatcher()
            .getPath());
    }
}