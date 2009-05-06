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

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slim3.commons.cleaner.Cleaner;
import org.slim3.commons.config.Configuration;
import org.slim3.commons.util.ClassUtil;
import org.slim3.commons.util.StringUtil;
import org.slim3.mvc.MvcConstants;

/**
 * The front controller of Slim3.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class FrontController implements Filter {

    /**
     * The default encoding.
     */
    protected static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * The default view prefix
     */
    protected static final String DEFAULT_VIEW_PREFIX = "/WEB-INF/view";

    /**
     * The logger.
     */
    protected static final Logger logger =
        Logger.getLogger(FrontController.class.getName());

    /**
     * The encoding.
     */
    protected String encoding;

    /**
     * The servlet context.
     */
    protected ServletContext servletContext;

    /**
     * Constructor.
     */
    public FrontController() {
    }

    public void init(FilterConfig config) throws ServletException {
        encoding =
            Configuration.getInstance().getValue(MvcConstants.ENCODING_KEY);
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
        servletContext = config.getServletContext();
        ServletContextLocator.setServletContext(servletContext);
    }

    public void destroy() {
        Cleaner.cleanAll();
        ServletContextLocator.setServletContext(null);
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        doFilter(
            (HttpServletRequest) request,
            (HttpServletResponse) response,
            chain);
    }

    /**
     * Executes filtering process.
     * 
     * @param request
     *            the request
     * @param response
     *            the response
     * @param chain
     *            the filter chain
     * @throws IOException
     *             if {@link IOException} is encountered
     * @throws ServletException
     *             if {@link ServletException} is encountered
     */
    protected void doFilter(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest previousRequest = RequestLocator.getRequest();
        HttpServletResponse previousResponse = ResponseLocator.getResponse();
        RequestLocator.setRequest(request);
        ResponseLocator.setResponse(response);
        try {
            if (request.getCharacterEncoding() == null) {
                request.setCharacterEncoding(encoding);
            }
            String path = getPath(request);
            Controller controller = getController(request, response, path);
            if (controller == null) {
                chain.doFilter(request, response);
            } else {
                if (Configuration.getInstance().isHot()) {
                    synchronized (this) {
                        try {
                            processController(request, response, controller);
                        } finally {
                            Cleaner.cleanAll();
                        }
                    }
                } else {
                    processController(request, response, controller);
                }
            }
        } finally {
            RequestLocator.setRequest(previousRequest);
            ResponseLocator.setResponse(previousResponse);
        }
    }

    /**
     * Returns the path of the request.
     * 
     * @param request
     *            the request
     * @return the path of the request
     */
    protected String getPath(HttpServletRequest request) {
        String path = request.getPathInfo();
        if (StringUtil.isEmpty(path)) {
            path = request.getServletPath();
        }
        return path;
    }

    /**
     * Returns the controller specified by the path.
     * 
     * @param request
     *            the request
     * @param response
     *            the response
     * @param path
     *            the path
     * @return the controller
     * 
     */
    protected Controller getController(HttpServletRequest request,
            HttpServletResponse response, String path) {
        if (path.indexOf('.') >= 0) {
            return null;
        }
        Controller controller = createController(path);
        if (controller == null) {
            return null;
        }
        controller.setServletContext(servletContext);
        controller.setRequest(request);
        controller.setResponse(response);
        controller.setPath(path);
        ControllerLocator.setController(controller);
        return controller;
    }

    /**
     * Creates a new controller specified by the path.
     * 
     * @param path
     *            the path
     * @return a new controller
     * @throws IllegalStateException
     *             if the controller does not extend
     *             "org.slim3.mvc.controller.Controller"
     */
    protected Controller createController(String path)
            throws IllegalStateException {
        String className = toControllerClassName(path);
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (Configuration.getInstance().isHot()) {
            loader = new HotReloadingClassLoader(loader);
        }
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className, true, loader);
        } catch (Throwable t) {
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, t.getMessage(), t);
            }
            return null;
        }
        if (!Controller.class.isAssignableFrom(clazz)) {
            throw new IllegalStateException("The controller("
                + className
                + ") must extend \""
                + Controller.class.getName()
                + "\".");
        }
        return ClassUtil.newInstance(clazz);
    }

    /**
     * Converts the path to the controller class name.
     * 
     * @param path
     *            the path
     * @return the controller class name
     * @throws IllegalStateException
     *             if the entry(slim3.controllerPackage) of
     *             "slim3_configuration.properties" is not found
     */
    protected String toControllerClassName(String path)
            throws IllegalStateException {
        String packageName =
            Configuration.getInstance().getValue(
                MvcConstants.CONTROLLER_PACKAGE_KEY);
        if (StringUtil.isEmpty(packageName)) {
            throw new IllegalStateException("The entry("
                + MvcConstants.CONTROLLER_PACKAGE_KEY
                + ") of \"slim3_configuration.properties\" is not found.");
        }
        String className = packageName + path.replace('/', '.');
        if (className.endsWith(".")) {
            className += MvcConstants.INDEX_CONTROLLER;
        } else {
            int pos = className.lastIndexOf('.');
            className =
                className.substring(0, pos + 1)
                    + StringUtil.capitalize(className.substring(pos + 1))
                    + MvcConstants.CONTROLLER_SUFFIX;
        }
        return className;
    }

    /**
     * Processes the controller.
     * 
     * @param request
     *            the request
     * @param response
     *            the response
     * @param controller
     *            the controller
     * @throws IOException
     *             if {@link IOException} has occurred
     * @throws ServletException
     *             if {@link ServletException} has occurred
     */
    protected void processController(HttpServletRequest request,
            HttpServletResponse response, Controller controller)
            throws IOException, ServletException {
        bindParameters(request, response, controller);
        Navigation navigation =
            executeController(request, response, controller);
        handleNavigation(request, response, controller, navigation);
    }

    /**
     * Binds the request parameters to the controller.
     * 
     * @param request
     *            the request
     * @param response
     *            the response
     * @param controller
     *            the controller
     */
    protected void bindParameters(HttpServletRequest request,
            HttpServletResponse response, Controller controller) {
        RequestParameterParser parser = new RequestParameterParser(request);
        Map<String, Object> parameters = parser.parse();
        controller.setParameters(parameters);
        RequestParameterBinder binder = new RequestParameterBinder();
        binder.bind(controller, parameters);
    }

    /**
     * Executes the controller.
     * 
     * @param request
     *            the request
     * @param response
     *            the response
     * @param controller
     *            the controller
     * @return navigation
     */
    protected Navigation executeController(HttpServletRequest request,
            HttpServletResponse response, Controller controller) {
        return controller.execute();
    }

    /**
     * Handles the navigation.
     * 
     * @param request
     *            the request
     * @param response
     *            the response
     * @param controller
     *            the controller
     * @param navigation
     *            the navigation
     * @throws IOException
     *             if {@link IOException} has occurred
     * @throws ServletException
     *             if {@link ServletException} has occurred
     */
    protected void handleNavigation(HttpServletRequest request,
            HttpServletResponse response, Controller controller,
            Navigation navigation) throws IOException, ServletException {
        if (navigation == null) {
            return;
        }
        if (navigation.isRedirect()) {
            doRedirect(request, response, controller, navigation.getPath());
        } else {
            doForward(request, response, controller, navigation.getPath());
        }
    }

    /**
     * Do a redirect to the path.
     * 
     * @param request
     *            the request
     * @param response
     *            the response
     * @param controller
     *            the controller
     * @param path
     *            the path
     * @throws IOException
     *             if {@link IOException} has occurred
     * @throws ServletException
     *             if {@link ServletException} has occurred
     */
    protected void doRedirect(HttpServletRequest request,
            HttpServletResponse response, Controller controller, String path)
            throws IOException, ServletException {
        if (path.startsWith("/")) {
            path = request.getContextPath() + path;
        }
        response.sendRedirect(response.encodeRedirectURL(path));
    }

    /**
     * Do a forward to the path.
     * 
     * @param request
     *            the request
     * @param response
     *            the response
     * @param controller
     *            the controller
     * @param path
     *            the path
     * @throws IOException
     *             if {@link IOException} has occurred
     * @throws ServletException
     *             if {@link ServletException} has occurred
     */
    protected void doForward(HttpServletRequest request,
            HttpServletResponse response, Controller controller, String path)
            throws IOException, ServletException {
        if (!path.startsWith("/")) {
            path = controller.getApplicationPath() + path;
        }
        RequestDispatcher rd = servletContext.getRequestDispatcher(path);
        if (rd == null) {
            response.sendError(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "The request dispatcher specified by the path("
                    + path
                    + ") is not found.");
            return;
        }
        rd.forward(request, response);
    }
}