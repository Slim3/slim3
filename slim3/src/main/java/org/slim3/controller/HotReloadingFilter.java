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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slim3.controller.router.Router;
import org.slim3.controller.router.RouterFactory;
import org.slim3.util.AppEngineUtil;
import org.slim3.util.Cleaner;
import org.slim3.util.RequestLocator;
import org.slim3.util.RequestUtil;
import org.slim3.util.ResponseLocator;
import org.slim3.util.ServletContextLocator;
import org.slim3.util.StringUtil;

/**
 * A filter for HOT reloading.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class HotReloadingFilter implements Filter {

    /**
     * The logger.
     */
    // private static final Logger logger =
    // Logger.getLogger(HotReloadingFilter.class.getName());

    /**
     * The servlet context.
     */
    protected ServletContext servletContext;

    /**
     * Whether this filter supports hot reloading.
     */
    protected boolean hotReloading = false;

    /**
     * The root package name.
     */
    protected String rootPackageName;

    /**
     * The cool package name.
     */
    protected String coolPackageName;

    /**
     * Constructor.
     */
    public HotReloadingFilter() {
    }

    public void init(FilterConfig config) throws ServletException {
        initServletContext(config);
        initHotReloading();
        initRootPackageName();
        initCoolPackageName();
    }

    /**
     * Initializes the servlet context.
     * 
     * @param config
     *            the filter configuration.
     */
    protected void initServletContext(FilterConfig config) {
        servletContext = config.getServletContext();
    }

    /**
     * Initializes the HOT reloading setting.
     */
    protected void initHotReloading() {
        if (AppEngineUtil.isDevelopment()) {
            if ("false".equalsIgnoreCase(System
                .getProperty(ControllerConstants.HOT_RELOADING_KEY))) {
                hotReloading = false;
            } else {
                hotReloading = true;
            }
        } else {
            hotReloading = false;
        }
        if (hotReloading) {
            System.setSecurityManager(null);
            ServletContextLocator.set(new HotServletContextWrapper(
                servletContext));
        }
        if (AppEngineUtil.isDevelopment()) {
            System.out.println("Slim3 HOT reloading:" + hotReloading);
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

    /**
     * Initializes the cool package name.
     */
    protected void initCoolPackageName() {
        coolPackageName =
            servletContext
                .getInitParameter(ControllerConstants.COOL_PACKAGE_KEY);
        if (StringUtil.isEmpty(coolPackageName)) {
            coolPackageName = ControllerConstants.DEFAULT_COOL_PACKAGE;
        }
    }

    public void destroy() {
        Cleaner.cleanAll();
        if (hotReloading) {
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
        if (hotReloading) {
            String path = RequestUtil.getPath(request);
            Router router = RouterFactory.getRouter();
            if (!router.isStatic(path)) {
                ClassLoader previousLoader =
                    Thread.currentThread().getContextClassLoader();
                if (!(previousLoader instanceof HotReloadingClassLoader)) {
                    doHotReloading(request, response, chain, previousLoader);
                    return;
                }
            }
        }
        chain.doFilter(request, response);
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
     * @param previousLoader
     *            the previous class loader
     * @throws IOException
     *             if {@link IOException} is encountered
     * @throws ServletException
     *             if {@link ServletException} is encountered
     */
    protected synchronized void doHotReloading(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain,
            ClassLoader previousLoader) throws IOException, ServletException {
        Thread.currentThread().setContextClassLoader(
            new HotReloadingClassLoader(
                previousLoader,
                rootPackageName,
                coolPackageName));
        request = new HotHttpServletRequestWrapper(request);
        RequestLocator.set(request);
        ResponseLocator.set(response);
        try {
            chain.doFilter(request, response);
        } catch (LinkageError e) {
            String msg = e.getMessage();
            if (msg != null && msg.indexOf("loader constraint violation") >= 0) {
                throw createHotReloadingRuntimeException(e);
            }
            throw e;
        } catch (ClassCastException e) {
            throw createHotReloadingRuntimeException(e);
        } finally {
            Cleaner.cleanAll();
            Thread.currentThread().setContextClassLoader(previousLoader);
            RequestLocator.set(null);
            ResponseLocator.set(null);

        }
    }

    /**
     * Creates {@link HotReloadingRuntimeException}.
     * 
     * @param cause
     *            the cause
     * @return {@link HotReloadingRuntimeException}
     */
    protected HotReloadingRuntimeException createHotReloadingRuntimeException(
            Throwable cause) {
        return new HotReloadingRuntimeException(
            "If you use MemcacheService or JCache, use org.slim3.memcache.Memcache instead of it.\n"
                + "Or if a COOL class wants to access a HOT reloaded class, use CoolBridge.\n"
                + "COOL classes means classes located on \""
                + rootPackageName
                + "."
                + coolPackageName
                + "\" package or classes which Servlet Container manages like Servlets.\n"
                + "HOT reloaded classes means classes located on \""
                + rootPackageName
                + "\" package except \""
                + rootPackageName
                + "."
                + coolPackageName
                + "\" package.\n"
                + "See http://sites.google.com/site/slim3appengine/hot-reloading",
            cause);
    }
}