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

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.slim3.controller.Controller;
import org.slim3.controller.ControllerConstants;
import org.slim3.controller.FrontController;
import org.slim3.controller.router.Router;
import org.slim3.controller.router.RouterFactory;
import org.slim3.controller.validator.Errors;

/**
 * A tester for Slim3 Controller.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class ControllerTester extends ServletTester {

    /**
     * The mock for {@link FilterConfig}.
     */
    public MockFilterConfig filterConfig = new MockFilterConfig(servletContext);

    /**
     * The front controller.
     */
    public FrontController frontController = new FrontController();

    /**
     * A mock for {@link FilterChain}.
     */
    public MockFilterChain filterChain = new MockFilterChain();

    /**
     * The test class.
     */
    protected Class<?> testClass;

    /**
     * Constructor.
     * 
     * @param testClass
     *            the test class
     * @throws NullPointerException
     *             if the testClass parameter is null
     */
    public ControllerTester(Class<?> testClass) throws NullPointerException {
        if (testClass == null) {
            throw new NullPointerException(
                "The testClass parameter must not be null.");
        }
        this.testClass = testClass;
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        setUpServletContext();
        frontController.init(filterConfig);
    }

    @Override
    public void tearDown() throws Exception {
        filterConfig = null;
        frontController.destroy();
        frontController = null;
        filterChain = null;
        super.tearDown();
    }

    /**
     * Starts the request process.
     * 
     * @param path
     *            the request path
     * @throws NullPointerException
     *             if the path parameter is null
     * @throws IllegalArgumentException
     *             if the path does not start with "/"
     * @throws IOException
     *             if {@link IOException} occurred
     * @throws ServletException
     *             if {@link ServletException} occurred
     */
    public void start(String path) throws NullPointerException,
            IllegalArgumentException, IOException, ServletException {
        if (path == null) {
            throw new NullPointerException("The path parameter is null.");
        }
        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("The path("
                + path
                + ") must start with \"/\".");
        }
        request
            .setAttribute(ControllerConstants.FORWARD_SERVLET_PATH_KEY, path);
        Router router = RouterFactory.getRouter();
        String routingPath = router.route(request, path);
        if (routingPath != null) {
            int index = routingPath.lastIndexOf('?');
            if (index < 0) {
                path = routingPath;
            } else {
                path = routingPath.substring(0, index);
                request.setQueryString(routingPath.substring(index + 1));
            }
        }
        request.setServletPath(path);
        frontController.doFilter(request, response, filterChain);
    }

    /**
     * Sets up the servlet context.
     */
    protected void setUpServletContext() {
        String rootPackageName =
            servletContext
                .getInitParameter(ControllerConstants.ROOT_PACKAGE_KEY);
        if (rootPackageName != null) {
            if (rootPackageName
                .endsWith(ControllerConstants.SERVER_CONTROLLER_PACKAGE)) {
                servletContext.setAttribute(
                    ControllerConstants.CONTROLLER_PACKAGE_KEY,
                    ControllerConstants.SERVER_CONTROLLER_PACKAGE);
            }
            return;
        }
        String className = testClass.getName();
        int pos = className.lastIndexOf(".server.controller.");
        if (pos < 0) {
            pos = className.lastIndexOf(".controller.");
            if (pos < 0) {
                pos = className.lastIndexOf('.');
            }
        } else {
            servletContext.setAttribute(
                ControllerConstants.CONTROLLER_PACKAGE_KEY,
                "server.controller");
        }
        rootPackageName = className.substring(0, pos);
        servletContext.setInitParameter(
            ControllerConstants.ROOT_PACKAGE_KEY,
            rootPackageName);
    }

    /**
     * Returns the destination path.
     * 
     * @return the destination path
     */
    @Override
    public String getDestinationPath() {
        String path = super.getDestinationPath();
        if (path != null) {
            return path;
        }
        return filterChain.getPath();
    }

    /**
     * Returns the controller.
     * 
     * @param <T>
     *            the controller type
     * @return the controller
     */
    @SuppressWarnings("unchecked")
    public <T extends Controller> T getController() {
        return (T) request.getAttribute(ControllerConstants.CONTROLLER_KEY);
    }

    /**
     * Returns the error messages.
     * 
     * @return the error messages
     */
    public Errors getErrors() {
        return requestScope(ControllerConstants.ERRORS_KEY);
    }
}