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

import java.io.IOException;
import java.util.Locale;
import java.util.TimeZone;
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
import javax.servlet.http.HttpSession;

import org.slim3.controller.upload.FileUpload;
import org.slim3.controller.upload.MultipartRequestHandler;
import org.slim3.controller.validator.Errors;
import org.slim3.util.ApplicationMessage;
import org.slim3.util.ClassUtil;
import org.slim3.util.Cleaner;
import org.slim3.util.LocaleLocator;
import org.slim3.util.LocaleUtil;
import org.slim3.util.RequestLocator;
import org.slim3.util.ResponseLocator;
import org.slim3.util.ServletContextLocator;
import org.slim3.util.StringUtil;
import org.slim3.util.TimeZoneLocator;

/**
 * The front controller of Slim3.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class FrontController implements Filter {

    /**
     * The logger.
     */
    private static final Logger logger =
        Logger.getLogger(FrontController.class.getName());

    /**
     * The character set.
     */
    protected String charset;

    /**
     * The bundle name.
     */
    protected String bundleName;

    /**
     * The default locale.
     */
    protected Locale defaultLocale;

    /**
     * The default time zone.
     */
    protected TimeZone defaultTimeZone;

    /**
     * The servlet context.
     */
    protected ServletContext servletContext;

    /**
     * Whether this filter supports hot reloading.
     */
    protected boolean hotReloading = false;

    /**
     * The controller package name.
     */
    protected String controllerPackageName;

    /**
     * Constructor.
     */
    public FrontController() {
    }

    public void init(FilterConfig config) throws ServletException {
        initServletContext(config);
        initCharset();
        initBundleName();
        initDefaultLocale();
        initDefaultTimeZone();
        initHotReloading();
        initControllerPackageName();
    }

    /**
     * Initializes the servlet context.
     * 
     * @param config
     *            the filter configuration.
     */
    protected void initServletContext(FilterConfig config) {
        servletContext = config.getServletContext();
        ServletContextLocator.set(servletContext);
    }

    /**
     * Initializes the character set.
     */
    protected void initCharset() {
        charset =
            servletContext
                .getInitParameter(ControllerConstants.REQUEST_CHARSET_KEY);
        if (charset == null) {
            charset = ControllerConstants.DEFAULT_REQUEST_CHARSET;
        }
    }

    /**
     * Initializes the default time zone.
     */
    protected void initBundleName() {
        bundleName =
            servletContext
                .getInitParameter(ControllerConstants.LOCALIZATION_CONTEXT_KEY);
        if (bundleName == null) {
            bundleName = ControllerConstants.DEFAULT_LOCALIZATION_CONTEXT;
        }
    }

    /**
     * Initializes the default locale.
     */
    protected void initDefaultLocale() {
        defaultLocale =
            LocaleUtil.parse(servletContext
                .getInitParameter(ControllerConstants.LOCALE_KEY));
    }

    /**
     * Initializes the default time zone.
     */
    protected void initDefaultTimeZone() {
        String s =
            servletContext.getInitParameter(ControllerConstants.TIME_ZONE_KEY);
        if (s != null) {
            defaultTimeZone = TimeZone.getTimeZone(s);
        }
    }

    /**
     * Initializes the HOT reloading setting.
     */
    protected void initHotReloading() {
        boolean runningOnDevserver =
            servletContext.getServerInfo().indexOf("Development") >= 0;
        if (runningOnDevserver) {
            System.setSecurityManager(null);
            String hotReloadingStr =
                System.getProperty(ControllerConstants.HOT_RELOADING_KEY);
            if (!StringUtil.isEmpty(hotReloadingStr)) {
                hotReloading = "true".equalsIgnoreCase(hotReloadingStr);
            } else {
                hotReloading = true;
            }
        } else {
            hotReloading = false;
        }
        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, "Slim3 hot reloading:" + hotReloading);
        }
    }

    /**
     * Initializes the controller package name.
     */
    protected void initControllerPackageName() {
        controllerPackageName =
            System.getProperty(ControllerConstants.CONTROLLER_PACKAGE_KEY);
        if (StringUtil.isEmpty(controllerPackageName)) {
            throw new IllegalStateException("The system property("
                + ControllerConstants.CONTROLLER_PACKAGE_KEY
                + ") is not found.");
        }
    }

    public void destroy() {
        Cleaner.cleanAll();
        ServletContextLocator.set(null);
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
        HttpServletRequest previousRequest = RequestLocator.get();
        RequestLocator.set(request);
        HttpServletResponse previousResponse = ResponseLocator.get();
        ResponseLocator.set(response);
        Locale previousLocale = LocaleLocator.get();
        LocaleLocator.set(processLocale(request));
        TimeZone previousTimeZone = TimeZoneLocator.get();
        TimeZoneLocator.set(processTimeZone(request));
        ApplicationMessage.setBundle(bundleName, LocaleLocator.get());
        ClassLoader previousLoader =
            Thread.currentThread().getContextClassLoader();
        if (hotReloading
            && !(previousLoader instanceof HotReloadingClassLoader)) {
            Thread.currentThread().setContextClassLoader(
                new HotReloadingClassLoader(
                    previousLoader,
                    controllerPackageName));
        }
        try {
            doFilterInternal(request, response, chain);
        } finally {
            Thread.currentThread().setContextClassLoader(previousLoader);
            ApplicationMessage.clearBundle();
            TimeZoneLocator.set(previousTimeZone);
            LocaleLocator.set(previousLocale);
            ResponseLocator.set(previousResponse);
            RequestLocator.set(previousRequest);
        }
    }

    /**
     * Executes internal filtering process.
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
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(charset);
        }
        String path = request.getServletPath();
        Controller controller = getController(request, response, path);
        if (controller == null) {
            chain.doFilter(request, response);
        } else {
            if (hotReloading) {
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
    }

    /**
     * Processes the current locale.
     * 
     * @param request
     *            the request
     * @return the current locale
     */
    protected Locale processLocale(HttpServletRequest request) {
        Locale locale = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object o = session.getAttribute(ControllerConstants.LOCALE_KEY);
            if (o instanceof String) {
                locale = LocaleUtil.parse((String) o);
            } else if (o instanceof Locale) {
                locale = (Locale) o;
            }
        }
        if (locale == null) {
            locale = defaultLocale;
        }
        if (locale == null) {
            locale = request.getLocale();
            if (locale == null) {
                locale = Locale.getDefault();
            }
        }
        return locale;
    }

    /**
     * Processes the current time zone.
     * 
     * @param request
     *            the request
     * @return the current time zone
     */
    protected TimeZone processTimeZone(HttpServletRequest request) {
        TimeZone timeZone = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object o = session.getAttribute(ControllerConstants.TIME_ZONE_KEY);
            if (o instanceof String) {
                timeZone = TimeZone.getTimeZone((String) o);
            } else if (o instanceof TimeZone) {
                timeZone = (TimeZone) o;
            }
        }
        if (timeZone == null) {
            timeZone = defaultTimeZone;
        }
        if (timeZone == null) {
            timeZone = TimeZone.getDefault();
        }
        return timeZone;
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
        request.setAttribute(ControllerConstants.CONTROLLER_KEY, controller);
        controller.application = servletContext;
        controller.request = request;
        controller.response = response;
        int pos = path.lastIndexOf('/');
        controller.basePath = path.substring(0, pos + 1);
        Errors errors =
            (Errors) request.getAttribute(ControllerConstants.ERRORS_KEY);
        if (errors == null) {
            errors = new Errors();
            request.setAttribute(ControllerConstants.ERRORS_KEY, errors);
        }
        controller.errors = errors;
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
     *             "org.slim3.controller.Controller"
     */
    protected Controller createController(String path)
            throws IllegalStateException {
        String className = toControllerClassName(path);
        Class<?> clazz = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
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
     *             if the system property(slim3.controllerPackage) is not found
     */
    protected String toControllerClassName(String path)
            throws IllegalStateException {
        String className = controllerPackageName + path.replace('/', '.');
        if (className.endsWith(".")) {
            className += ControllerConstants.INDEX_CONTROLLER;
        } else {
            int pos = className.lastIndexOf('.');
            className =
                className.substring(0, pos + 1)
                    + StringUtil.capitalize(className.substring(pos + 1))
                    + ControllerConstants.CONTROLLER_SUFFIX;
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
        RequestHandler requestHandler = createRequestHandler(request);
        requestHandler.handle();
        Navigation navigation = controller.runBare();
        handleNavigation(request, response, controller, navigation);
    }

    /**
     * Creates a new request handlers.
     * 
     * @param request
     *            the request
     * @return a new request handler
     * 
     */
    protected RequestHandler createRequestHandler(HttpServletRequest request) {
        if (FileUpload.isMultipartContent(request)) {
            return new MultipartRequestHandler(request);
        }
        return new RequestHandler(request);
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
            path = controller.basePath + path;
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