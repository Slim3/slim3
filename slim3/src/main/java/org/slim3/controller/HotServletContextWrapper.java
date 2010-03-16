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

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * {@link ServletContext} for HOT reloading.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class HotServletContextWrapper implements ServletContext {

    /**
     * The original servlet context.
     */
    protected ServletContext originalContext;

    /**
     * Constructor.
     * 
     * @param originalContext
     *            the original servlet context
     * @throws NullPointerException
     */
    public HotServletContextWrapper(ServletContext originalContext)
            throws NullPointerException {
        if (originalContext == null) {
            throw new NullPointerException(
                "The originalContext parameter is null.");
        }
        this.originalContext = originalContext;
    }

    /**
     * Returns the original servlet context.
     * 
     * @return the original servlet context
     */
    public ServletContext getOriginalContext() {
        return originalContext;
    }

    public Object getAttribute(String name) {
        return originalContext.getAttribute(name);
    }

    public Enumeration<?> getAttributeNames() {
        return originalContext.getAttributeNames();
    }

    public ServletContext getContext(String name) {
        return originalContext.getContext(name);
    }

    public String getContextPath() {
        return originalContext.getContextPath();
    }

    public String getInitParameter(String name) {
        return originalContext.getInitParameter(name);
    }

    public Enumeration<?> getInitParameterNames() {
        return originalContext.getInitParameterNames();
    }

    public int getMajorVersion() {
        return originalContext.getMajorVersion();
    }

    public String getMimeType(String name) {
        return originalContext.getMimeType(name);
    }

    public int getMinorVersion() {
        return originalContext.getMinorVersion();
    }

    public RequestDispatcher getNamedDispatcher(String name) {
        return new HotRequestDispatcherWrapper(originalContext
            .getNamedDispatcher(name));
    }

    public String getRealPath(String path) {
        return originalContext.getRealPath(path);
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        return new HotRequestDispatcherWrapper(originalContext
            .getRequestDispatcher(path));
    }

    public URL getResource(String path) throws MalformedURLException {
        return originalContext.getResource(path);
    }

    public InputStream getResourceAsStream(String path) {
        return originalContext.getResourceAsStream(path);
    }

    public Set<?> getResourcePaths(String name) {
        return originalContext.getResourcePaths(name);
    }

    public String getServerInfo() {
        return originalContext.getServerInfo();
    }

    @SuppressWarnings("deprecation")
    public Servlet getServlet(String name) throws ServletException {
        return originalContext.getServlet(name);
    }

    public String getServletContextName() {
        return originalContext.getServletContextName();
    }

    @SuppressWarnings("deprecation")
    public Enumeration<?> getServletNames() {
        return originalContext.getServletNames();
    }

    @SuppressWarnings("deprecation")
    public Enumeration<?> getServlets() {
        return originalContext.getServlets();
    }

    public void log(String message) {
        originalContext.log(message);
    }

    @SuppressWarnings("deprecation")
    public void log(Exception cause, String message) {
        originalContext.log(cause, message);
    }

    public void log(String message, Throwable cause) {
        originalContext.log(message, cause);
    }

    public void removeAttribute(String name) {
        originalContext.removeAttribute(name);
    }

    public void setAttribute(String name, Object value) {
        originalContext.setAttribute(name, value);
    }
}