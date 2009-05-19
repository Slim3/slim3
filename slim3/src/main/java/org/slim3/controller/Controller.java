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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slim3.util.RuntimeExceptionUtil;

/**
 * A base controller. This controller is created each request.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public abstract class Controller {

    private static final Logger logger =
        Logger.getLogger(Controller.class.getName());

    /**
     * The servlet context.
     */
    protected ServletContext servletContext;

    /**
     * The request.
     */
    protected HttpServletRequest request;

    /**
     * The response.
     */
    protected HttpServletResponse response;

    /**
     * The base path.
     */
    protected String basePath;

    /**
     * Runs the bare controller process.
     * 
     * @return the navigation
     */
    public Navigation runBare() {
        Navigation navigation = null;
        Throwable error = null;
        setUp();
        try {
            navigation = run();
        } catch (Throwable t) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, t.getMessage(), t);
            }
            error = t;
        } finally {
            try {
                tearDown();
            } catch (Throwable t) {
                if (logger.isLoggable(Level.WARNING)) {
                    logger.log(Level.WARNING, t.getMessage(), t);
                }
                if (error == null) {
                    error = t;
                }
            }
        }
        if (error != null) {
            navigation = handleError(error);
        }
        return navigation;
    }

    /**
     * Sets up the this controller. This method is called before "run" method is
     * called.
     */
    protected void setUp() {
    }

    /**
     * Override to run this controller
     * 
     * @return the navigation.
     */
    protected abstract Navigation run();

    /**
     * Tears down this controller. This method is called after "run" method is
     * called.
     */
    protected void tearDown() {
    }

    /**
     * Handles the error.
     * 
     * @param error
     *            the error
     * 
     * @return the navigation.
     */
    protected Navigation handleError(Throwable error) {
        throw RuntimeExceptionUtil.convert(error);
    }

    /**
     * Creates a new {@link Navigation} for "forward".
     * 
     * @param path
     *            the controller-relative path
     * @return a new {@link Navigation}
     */
    protected Navigation forward(String path) {
        return new Navigation(path, false);
    }

    /**
     * Creates a new {@link Navigation} for "redirect".
     * 
     * @param path
     *            the controller-relative path
     * @return a new {@link Navigation}
     */
    protected Navigation redirect(String path) {
        return new Navigation(path, true);
    }
}