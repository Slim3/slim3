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

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
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

import org.slim3.controller.router.Router;
import org.slim3.controller.router.RouterFactory;
import org.slim3.controller.validator.Errors;
import org.slim3.util.AppEngineUtil;
import org.slim3.util.ApplicationMessage;
import org.slim3.util.CipherFactory;
import org.slim3.util.ClassUtil;
import org.slim3.util.LocaleLocator;
import org.slim3.util.LocaleUtil;
import org.slim3.util.RequestLocator;
import org.slim3.util.RequestUtil;
import org.slim3.util.ResponseLocator;
import org.slim3.util.ServletContextLocator;
import org.slim3.util.StringUtil;
import org.slim3.util.ThrowableUtil;
import org.slim3.util.TimeZoneLocator;
import org.slim3.util.WrapRuntimeException;

import com.google.apphosting.api.ApiProxy;

/**
 * The front controller of Slim3.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class FrontController implements Filter {

    /**
     * The logger.
     */
    protected static final Logger logger =
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
     * Whether the servlet context is set to {@link ServletContextLocator}.
     */
    protected boolean servletContextSet = false;

    /**
     * The root package name.
     */
    protected String rootPackageName;

    /**
     * UUID of this application.
     */
    protected final String uuid = UUID.randomUUID().toString();

    /**
     * Constructor.
     */
    public FrontController() {
    }

    public void init(FilterConfig config) throws ServletException {
        checkDuplicateClasses();
        initServletContext(config);
        initCharset();
        initBundleName();
        initDefaultLocale();
        initDefaultTimeZone();
        initRootPackageName();
        if (AppEngineUtil.isProduction() && logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, "Initialized FrontController(UUID:"
                + uuid
                + ")");
        }
    }

    /**
     * Checks if multiple front controllers are registered in the classpath. If
     * so, {@link IllegalStateException} is thrown.
     * 
     * @throws IllegalStateException
     *             if multiple front controllers are registered in the classpath
     */
    protected void checkDuplicateClasses() throws IllegalStateException {
        if (AppEngineUtil.isDevelopment()) {
            try {
                Enumeration<URL> resources =
                    Thread
                        .currentThread()
                        .getContextClassLoader()
                        .getResources(
                            getClass().getName().replace('.', '/') + ".class");
                int count = 0;
                while (resources.hasMoreElements()) {
                    resources.nextElement();
                    count++;
                }
                if (count > 1) {
                    throw new IllegalStateException(
                        "slim3-xxx.jar files are duplicate in the classpath.");
                }
            } catch (IOException e) {
                throw new WrapRuntimeException(e);
            }
            try {
                Enumeration<URL> resources =
                    Thread
                        .currentThread()
                        .getContextClassLoader()
                        .getResources(
                            ApiProxy.class.getName().replace('.', '/')
                                + ".class");
                int count = 0;
                while (resources.hasMoreElements()) {
                    resources.nextElement();
                    count++;
                }
                if (count > 1) {
                    throw new IllegalStateException(
                        "appengine-api-1.0-sdk-xxx.jar files are duplicate in the classpath.");
                }
            } catch (IOException e) {
                throw new WrapRuntimeException(e);
            }
        }
    }

    /**
     * Initializes the servlet context.
     * 
     * @param config
     *            the filter configuration.
     */
    protected void initServletContext(FilterConfig config) {
        servletContext = config.getServletContext();
        if (ServletContextLocator.get() == null) {
            ServletContextLocator.set(servletContext);
            servletContextSet = true;
        } else {
            servletContext = ServletContextLocator.get();
        }
        servletContext.setAttribute(ControllerConstants.UUID_KEY, uuid);
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
     * Initializes the bundle name.
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
     * Initializes the root package name.
     */
    protected void initRootPackageName() {
        rootPackageName =
            servletContext
                .getInitParameter(ControllerConstants.ROOT_PACKAGE_KEY);
        if (StringUtil.isEmpty(rootPackageName)) {
            throw new IllegalStateException("The context-param("
                + ControllerConstants.ROOT_PACKAGE_KEY
                + ") is not found in web.xml.");
        }
    }

    public void destroy() {
        if (servletContextSet) {
            ServletContextLocator.set(null);
        }
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
        String path = RequestUtil.getPath(request);
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(charset);
        }
        Router router = RouterFactory.getRouter();
        if (request.getAttribute(ControllerConstants.ROUTED_KEY) == Boolean.TRUE) {
            request.removeAttribute(ControllerConstants.ROUTED_KEY);
            doFilter(request, response, chain, path);
        } else {
            if (!router.isStatic(path)) {
                String routingPath = router.route(request, path);
                if (routingPath != null) {
                    request.setAttribute(ControllerConstants.ROUTED_KEY, true);
                    doForward(request, response, routingPath);
                } else {
                    doFilter(request, response, chain, path);
                }
            } else {
                chain.doFilter(request, response);
            }
        }
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
     * @param path
     *            the path
     * @throws IOException
     *             if {@link IOException} is encountered
     * @throws ServletException
     *             if {@link ServletException} is encountered
     */
    protected void doFilter(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain, String path)
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
        CipherFactory.getFactory().clearLimitedKey();
        try {
            Controller controller = getController(request, response, path);
            if (controller != null) {
                processController(request, response, controller);
            } else {
                if (request instanceof HotHttpServletRequestWrapper) {
                    request =
                        ((HotHttpServletRequestWrapper) request)
                            .getOriginalRequest();
                }
                chain.doFilter(request, response);
            }
        } finally {
            ApplicationMessage.clearBundle();
            TimeZoneLocator.set(previousTimeZone);
            LocaleLocator.set(previousLocale);
            ResponseLocator.set(previousResponse);
            RequestLocator.set(previousRequest);
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
        controller.servletContext = servletContext;
        controller.request = request;
        controller.response = response;
        int pos = path.lastIndexOf('/');
        controller.basePath = path.substring(0, pos + 1);
        request.setAttribute(
            ControllerConstants.BASE_PATH_KEY,
            controller.basePath);
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
        if (className == null) {
            return null;
        }
        Class<?> clazz = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            clazz = Class.forName(className, true, loader);
        } catch (Throwable t) {
            return null;
        }
        if (!Controller.class.isAssignableFrom(clazz)) {
            if (AppEngineUtil.isDevelopment()) {
                System.out.println("The class("
                    + className
                    + ") does not extend \""
                    + Controller.class.getName()
                    + "\".");
            }
            return null;
        }
        if (Modifier.isAbstract(clazz.getModifiers())) {
            return null;
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
        if (path.startsWith("/_ah/")) {
            return null;
        }
        String className =
            rootPackageName
                + "."
                + getControllerPackageName()
                + path.replace('/', '.');
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
     * Returns the controller package name.
     * 
     * @return the controller package name
     */
    protected String getControllerPackageName() {
        String packageName =
            (String) servletContext
                .getAttribute(ControllerConstants.CONTROLLER_PACKAGE_KEY);
        if (packageName == null) {
            packageName = ControllerConstants.DEFAULT_CONTROLLER_PACKAGE;
        }
        return packageName;
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
        RequestHandler requestHandler =
            controller.createRequestHandler(request);
        requestHandler.handle();
        try {
            Navigation navigation = controller.runBare();
            handleNavigation(request, response, controller, navigation);
        } catch (Throwable t) {
            if (t instanceof IOException) {
                throw (IOException) t;
            }
            if (t instanceof ServletException) {
                throw (ServletException) t;
            }
            throw ThrowableUtil.wrap(t);
        }
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
        doRedirect(request, response, path);
    }

    /**
     * Do a redirect to the path.
     * 
     * @param request
     *            the request
     * @param response
     *            the response
     * @param path
     *            the path
     * @throws IOException
     *             if {@link IOException} has occurred
     * @throws ServletException
     *             if {@link ServletException} has occurred
     */
    protected void doRedirect(HttpServletRequest request,
            HttpServletResponse response, String path) throws IOException,
            ServletException {
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
        Router router = RouterFactory.getRouter();
        if (!router.isStatic(path)) {
            String routedPath = router.route(request, path);
            if (routedPath != null) {
                request.setAttribute(ControllerConstants.ROUTED_KEY, true);
                doForward(request, response, routedPath);
                return;
            }
        }
        doForward(request, response, path);
    }

    /**
     * Do a forward to the path.
     * 
     * @param request
     *            the request
     * @param response
     *            the response
     * @param path
     *            the path
     * @throws IOException
     *             if {@link IOException} has occurred
     * @throws ServletException
     *             if {@link ServletException} has occurred
     */
    protected void doForward(HttpServletRequest request,
            HttpServletResponse response, String path) throws IOException,
            ServletException {
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