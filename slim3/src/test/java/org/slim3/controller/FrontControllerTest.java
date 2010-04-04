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

import org.junit.Test;
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
    public void setUp() throws Exception {
        tester.servletContext.setInitParameter(
            ControllerConstants.ROOT_PACKAGE_KEY,
            ROOT_PACKAGE);
        super.setUp();
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void init() throws Exception {
        assertThat(
            tester.frontController.charset,
            is(ControllerConstants.DEFAULT_REQUEST_CHARSET));
        assertThat(
            tester.frontController.bundleName,
            is(ControllerConstants.DEFAULT_LOCALIZATION_CONTEXT));
        assertThat(ServletContextLocator.get(), is(not(nullValue())));
        assertThat(tester.frontController.rootPackageName, is(ROOT_PACKAGE));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void toControllerClassName() throws Exception {
        assertThat(
            tester.frontController.toControllerClassName("/hello/list"),
            is(ListController.class.getName()));
        assertThat(
            tester.frontController.toControllerClassName("/hoge"),
            is(HogeController.class.getName()));
        assertThat(
            tester.frontController.toControllerClassName("/"),
            is(IndexController.class.getName()));
        assertThat(
            tester.frontController.toControllerClassName("/_ah/admin"),
            is(nullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getControllerPackageName() throws Exception {
        assertThat(
            tester.frontController.getControllerPackageName(),
            is("controller"));
        tester.servletContext.setAttribute(
            "slim3.controllerPackage",
            "server.controller");
        assertThat(
            tester.frontController.getControllerPackageName(),
            is("server.controller"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void createController() throws Exception {
        Controller controller =
            tester.frontController.createController("/hello/list");
        assertThat(controller, is(not(nullValue())));
        assertThat(controller, is(ListController.class));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void createControllerForRootController() throws Exception {
        Controller controller =
            tester.frontController.createController("/hoge");
        assertThat(controller, is(not(nullValue())));
        assertThat(controller, is(HogeController.class));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void createControllerForRootIndexController() throws Exception {
        Controller controller = tester.frontController.createController("/");
        assertThat(controller, is(not(nullValue())));
        assertThat(controller, is(IndexController.class));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void createControllerForIndexController() throws Exception {
        Controller controller =
            tester.frontController.createController("/hello/");
        assertThat(controller, is(not(nullValue())));
        assertThat(
            controller,
            is(org.slim3.controller.controller.hello.IndexController.class));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void createControllerForControllerThatDoesNotExtendController()
            throws Exception {
        assertThat(
            tester.frontController.createController("/bad"),
            is(nullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void createControllerForAbstractController() throws Exception {
        assertThat(
            tester.frontController.createController("/abstract"),
            is(nullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void createControllerForClassNotFound() throws Exception {
        assertThat(
            tester.frontController.createController("/xxx"),
            is(nullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void createControllerForAppEnginePath() throws Exception {
        assertThat(
            tester.frontController.createController("/_ah/admin"),
            is(nullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getController() throws Exception {
        Controller controller =
            tester.frontController.getController(
                tester.request,
                tester.response,
                "/");
        assertThat(controller.servletContext, is(not(nullValue())));
        assertThat(controller.request, is(not(nullValue())));
        assertThat(controller.response, is(not(nullValue())));
        assertThat(controller.basePath, is("/"));
        assertThat(controller, is(sameInstance(tester
            .requestScope(ControllerConstants.CONTROLLER_KEY))));
        assertThat(controller.basePath, is(tester
            .requestScope(ControllerConstants.BASE_PATH_KEY)));
        Errors errors = controller.errors;
        assertThat(errors, is(not(nullValue())));
        assertThat(errors, is(sameInstance(tester
            .requestScope(ControllerConstants.ERRORS_KEY))));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getControllerForErrors() throws Exception {
        Errors errors = new Errors();
        tester.requestScope(ControllerConstants.ERRORS_KEY, errors);
        Controller controller =
            tester.frontController.getController(
                tester.request,
                tester.response,
                "/");
        assertThat(errors, is(sameInstance(controller.errors)));
        assertThat(errors, is(sameInstance(tester
            .requestScope(ControllerConstants.ERRORS_KEY))));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getControllerForJsp() throws Exception {
        assertThat(tester.frontController.getController(
            tester.request,
            tester.response,
            "/index.jsp"), is(nullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void doRedirect() throws Exception {
        String path = "http://www.google.com";
        Controller controller = tester.frontController.createController("/");
        tester.frontController.doRedirect(
            tester.request,
            tester.response,
            controller,
            path);
        assertThat(tester.response.getRedirectPath(), is(path));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void doRedirectForContextRelativePath() throws Exception {
        tester.servletContext.setContextPath("/slim3-tutorial");
        Controller controller = tester.frontController.createController("/");
        tester.frontController.doRedirect(
            tester.request,
            tester.response,
            controller,
            "/hello/list");
        assertThat(
            tester.response.getRedirectPath(),
            is("/slim3-tutorial/hello/list"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void doForward() throws Exception {
        Controller controller =
            tester.frontController.getController(
                tester.request,
                tester.response,
                "/");
        tester.frontController.doForward(
            tester.request,
            tester.response,
            controller,
            "index.jsp");
        assertThat(
            tester.servletContext.getLatestRequestDispatcher().getPath(),
            is("/index.jsp"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void doForwardForApplicationRelativePath() throws Exception {
        Controller controller =
            tester.frontController.getController(
                tester.request,
                tester.response,
                "/hoge");
        tester.frontController.doForward(
            tester.request,
            tester.response,
            controller,
            "index.jsp");
        assertThat(
            tester.servletContext.getLatestRequestDispatcher().getPath(),
            is("/index.jsp"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void doForwardForRouter() throws Exception {
        Controller controller =
            tester.frontController.getController(
                tester.request,
                tester.response,
                "/");
        tester.frontController.doForward(
            tester.request,
            tester.response,
            controller,
            "/_ah/mail/hoge");
        assertThat(
            tester.servletContext.getLatestRequestDispatcher().getPath(),
            is("/mail?address=hoge"));
        assertThat(
            tester.servletContext.getLatestRequestDispatcher().getPath(),
            is("/mail?address=hoge"));
        assertThat(
            tester.request.getAttribute(ControllerConstants.ROUTED_KEY),
            is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void handleNavigationForNoNavigation() throws Exception {
        Controller controller = tester.frontController.createController("/");
        tester.frontController.handleNavigation(
            tester.request,
            tester.response,
            controller,
            null);
        assertThat(tester.response.getRedirectPath(), is(nullValue()));
        assertThat(
            tester.servletContext.getLatestRequestDispatcher(),
            is(nullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void handleNavigationForRedirect() throws Exception {
        String path = "http://www.google.com";
        Controller controller = tester.frontController.createController("/");
        Navigation navigation = controller.redirect(path);
        tester.frontController.handleNavigation(
            tester.request,
            tester.response,
            controller,
            navigation);
        assertThat(tester.response.getRedirectPath(), is(path));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void handleNavigationForForward() throws Exception {
        Controller controller =
            tester.frontController.getController(
                tester.request,
                tester.response,
                "/");
        Navigation navigation = controller.forward("index.jsp");
        tester.frontController.handleNavigation(
            tester.request,
            tester.response,
            controller,
            navigation);
        assertThat(
            tester.servletContext.getLatestRequestDispatcher().getPath(),
            is("/index.jsp"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void processController() throws Exception {
        tester.param("aaa", "111");
        Controller controller =
            tester.frontController.getController(
                tester.request,
                tester.response,
                "/");
        tester.frontController.processController(
            tester.request,
            tester.response,
            controller);
        assertThat(controller.servletContext, is(not(nullValue())));
        assertThat(controller.request, is(not(nullValue())));
        assertThat(controller.response, is(not(nullValue())));
        assertThat(
            tester.servletContext.getLatestRequestDispatcher().getPath(),
            is("/index.jsp"));
        assertThat(tester.asString("aaa"), is("111"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void doFilter() throws Exception {
        tester.request.setServletPath("/");
        tester.frontController.doFilter(
            tester.request,
            tester.response,
            tester.filterChain);
        assertThat(tester.request.getCharacterEncoding(), is("UTF-8"));
        assertThat(
            tester.servletContext.getLatestRequestDispatcher().getPath(),
            is("/index.jsp"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void doFilterForRouter() throws Exception {
        tester.request.setServletPath("/_ah/mail/hoge");
        tester.frontController.doFilter(
            tester.request,
            tester.response,
            tester.filterChain);
        assertThat(
            tester.servletContext.getLatestRequestDispatcher().getPath(),
            is("/mail?address=hoge"));
        assertThat(
            tester.request.getAttribute(ControllerConstants.ROUTED_KEY),
            is(notNullValue()));
    }
}