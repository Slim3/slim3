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
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slim3.controller.validator.Errors;
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
    protected ServletContext application;

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
     * The error messages.
     */
    protected Errors errors;

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

    /**
     * Returns the request parameter.
     * 
     * @param name
     *            the parameter name
     * @return the parameter value
     */
    protected String param(String name) {
        return request.getParameter(name);
    }

    /**
     * Returns the request parameter.
     * 
     * @param name
     *            the parameter name
     * @return the parameter value
     */
    protected String[] paramValues(String name) {
        return request.getParameterValues(name);
    }

    /**
     * Returns the request attribute.
     * 
     * @param <T>
     *            the return type
     * @param name
     *            the attribute name
     * @return the request attribute
     */
    @SuppressWarnings("unchecked")
    protected <T> T requestScope(String name) {
        return (T) request.getAttribute(name);
    }

    /**
     * Sets the request attribute.
     * 
     * @param name
     *            the attribute name
     * @param value
     *            the attribute value
     */
    protected void requestScope(String name, Object value) {
        request.setAttribute(name, value);
    }

    /**
     * Removes the request attribute.
     * 
     * @param <T>
     *            the return type
     * @param name
     *            the attribute name
     * @return the removed value
     */
    @SuppressWarnings("unchecked")
    protected <T> T removeRequestScope(String name) {
        T value = (T) request.getAttribute(name);
        request.removeAttribute(name);
        return value;
    }

    /**
     * Returns the session attribute.
     * 
     * @param <T>
     *            the return type
     * @param name
     *            the attribute name
     * @return the attribute value
     */
    @SuppressWarnings("unchecked")
    protected <T> T sessionScope(String name) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (T) session.getAttribute(name);
    }

    /**
     * Sets the session attribute.
     * 
     * @param name
     *            the attribute name
     * @param value
     *            the attribute value
     */
    protected void sessionScope(String name, Object value) {
        request.getSession().setAttribute(name, value);
    }

    /**
     * Removes the session attribute.
     * 
     * @param <T>
     *            the return type
     * @param name
     *            the attribute name
     * @return the removed value
     */
    @SuppressWarnings("unchecked")
    protected <T> T removeSessionScope(String name) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        T value = (T) session.getAttribute(name);
        session.removeAttribute(name);
        return value;
    }

    /**
     * Returns the servlet context attribute.
     * 
     * @param <T>
     *            the return type
     * @param name
     *            the attribute name
     * @return the attribute value
     */
    @SuppressWarnings("unchecked")
    protected <T> T applicationScope(String name) {
        return (T) application.getAttribute(name);
    }

    /**
     * Sets the servlet context attribute.
     * 
     * @param name
     *            the attribute name
     * @param value
     *            the attribute value
     */
    protected void applicationScope(String name, Object value) {
        application.setAttribute(name, value);
    }

    /**
     * Removes the servlet context attribute.
     * 
     * @param <T>
     *            the return type
     * @param name
     *            the attribute name
     * @return the removed value
     */
    @SuppressWarnings("unchecked")
    protected <T> T removeApplicationScope(String name) {
        T value = (T) application.getAttribute(name);
        application.removeAttribute(name);
        return value;
    }

    /**
     * Determines if this application is running on the development server.
     * 
     * @return whether this application is running on the development server
     */
    protected boolean isDevelopment() {
        return application.getServerInfo().indexOf("Development") >= 0;
    }

    /**
     * Downloads the data.
     * 
     * @param fileName
     *            the file name
     * @param data
     *            the data
     */
    protected void download(String fileName, byte[] data) {
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment; filename=\""
                + fileName
                + "\"");
            OutputStream out = response.getOutputStream();
            try {
                out.write(data);
            } finally {
                out.close();
            }
        } catch (IOException e) {
            RuntimeExceptionUtil.wrapAndThrow(e);
        }
    }
}