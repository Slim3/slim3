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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slim3.exception.HotReloadingRuntimeException;
import org.slim3.util.Cleaner;
import org.slim3.util.StringUtil;

/**
 * A filter for HOT reloading.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class HotReloadingFilter implements Filter {

    /**
     * The logger.
     */
    private static final Logger logger =
        Logger.getLogger(HotReloadingFilter.class.getName());

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
        String hotReloadingStr =
            System.getProperty(ControllerConstants.HOT_RELOADING_KEY);
        if (!StringUtil.isEmpty(hotReloadingStr)) {
            hotReloading = "true".equalsIgnoreCase(hotReloadingStr);
        } else {
            boolean runningOnDevserver =
                servletContext.getServerInfo().indexOf("Development") >= 0;
            if (runningOnDevserver) {
                hotReloading = true;
            } else {
                hotReloading = false;
            }
        }
        if (hotReloading) {
            System.setSecurityManager(null);
        }
        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, "Slim3 HOT reloading:" + hotReloading);
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
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        if (hotReloading) {
            ClassLoader previousLoader =
                Thread.currentThread().getContextClassLoader();
            if (!(previousLoader instanceof HotReloadingClassLoader)) {
                synchronized (previousLoader) {
                    Thread.currentThread().setContextClassLoader(
                        new HotReloadingClassLoader(
                            previousLoader,
                            rootPackageName,
                            coolPackageName));
                    try {
                        chain.doFilter(request, response);
                    } catch (LinkageError e) {
                        String msg = e.getMessage();
                        if (msg.indexOf("loader constraint violation") >= 0) {
                            throw new HotReloadingRuntimeException(
                                "A class that is not HOT reloaded can not access a HOT reloaded class, "
                                    + msg,
                                e);
                        }
                        throw e;
                    } catch (ClassCastException e) {
                        String msg = e.getMessage();
                        String[] msgs = StringUtil.split(msg, " ");
                        if (msgs.length > 2
                            && msgs[0].equals(msgs[msgs.length - 1])) {
                            throw new HotReloadingRuntimeException(msg
                                + " because of different class loader.", e);
                        }
                        throw e;
                    } finally {
                        Thread.currentThread().setContextClassLoader(
                            previousLoader);
                        Cleaner.cleanAll();
                    }
                }
            } else {
                chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}