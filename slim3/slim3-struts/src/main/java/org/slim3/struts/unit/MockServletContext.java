/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.slim3.struts.unit;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.slim3.commons.exception.WrapRuntimeException;
import org.slim3.commons.util.IteratorEnumeration;

/**
 * A mock implementation for {@link ServletContext}.
 * 
 * @author higa
 * @since 3.0
 */
public class MockServletContext implements ServletContext, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Major Version
     */
    public static final int MAJOR_VERSION = 2;

    /**
     * Minor Version
     */
    public static final int MINOR_VERSION = 4;

    /**
     * Server Info
     */
    public static final String SERVER_INFO = "slim/3.0";

    /**
     * The servlet context name.
     */
    protected String servletContextName = "";

    /**
     * The map for the initial parameters.
     */
    protected Map<String, String> initParameterMap = new HashMap<String, String>();

    /**
     * The map for the attributes.
     */
    protected Map<String, Object> attributeTable = new HashMap<String, Object>();

    /**
     * The map for resource paths.
     */
    protected Map<String, Set<String>> resourcePathsMap = new HashMap<String, Set<String>>();

    /**
     * The map for resource.
     */
    protected Map<String, URL> resourceMap = new HashMap<String, URL>();

    /**
     * The map for initial parameters.
     */
    protected Map<String, String> realPathMap = new HashMap<String, String>();

    /**
     * Constructor.
     */
    public MockServletContext() {
    }

    public ServletContext getContext(String path) {
        throw new UnsupportedOperationException();
    }

    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    public int getMinorVersion() {
        return MINOR_VERSION;
    }

    public String getMimeType(String file) {
        throw new UnsupportedOperationException();
    }

    public Set<String> getResourcePaths(String path) {
        Set<String> resourcePaths = resourcePathsMap.get(path);
        if (resourcePaths != null) {
            return resourcePaths;
        }
        return new HashSet<String>();
    }

    /**
     * Adds the resource paths.
     * 
     * @param path
     *            the path
     * @param resourcePaths
     *            the resource paths
     */
    public void addResourcePaths(String path, Set<String> resourcePaths) {
        resourcePathsMap.put(path, resourcePaths);
    }

    public URL getResource(String path) {
        return resourceMap.get(path);
    }

    /**
     * Adds the URL resource.
     * 
     * @param path
     *            the path
     * @param url
     *            the URL resource
     */
    public void addResource(String path, URL url) {
        resourceMap.put(path, url);
    }

    public InputStream getResourceAsStream(String path) {
        try {
            URL url = getResource(path);
            if (url == null) {
                return null;
            }
            return url.openStream();
        } catch (Exception e) {
            throw new WrapRuntimeException(e);
        }
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        return new MockRequestDispatcher(path);
    }

    public RequestDispatcher getNamedDispatcher(String name) {
        throw new UnsupportedOperationException();
    }

    public Servlet getServlet(String name) throws ServletException {
        throw new UnsupportedOperationException();
    }

    public Enumeration<Servlet> getServlets() {
        throw new UnsupportedOperationException();
    }

    public Enumeration<String> getServletNames() {
        throw new UnsupportedOperationException();
    }

    public void log(String message) {
        System.out.println(message);
    }

    public void log(Exception ex, String message) {
        System.out.println(message);
        ex.printStackTrace();
    }

    public void log(String message, Throwable t) {
        System.out.println(message);
        t.printStackTrace();
    }

    public String getRealPath(String path) {
        return realPathMap.get(path);
    }

    /**
     * Adds the real path.
     * 
     * @param path
     *            the context relative path
     * @param realPath
     *            the real path
     */
    public void addRealPath(String path, String realPath) {
        realPathMap.put(path, realPath);
    }

    /**
     * @see javax.servlet.ServletContext#getServerInfo()
     */
    public String getServerInfo() {
        return SERVER_INFO;
    }

    public String getInitParameter(String name) {
        return initParameterMap.get(name);
    }

    public Enumeration<String> getInitParameterNames() {
        return new IteratorEnumeration<String>(initParameterMap.keySet()
                .iterator());
    }

    /**
     * Sets the initial parameter.
     * 
     * @param name
     *            the parameter name
     * @param value
     *            the value
     */
    public void setInitParameter(String name, String value) {
        initParameterMap.put(name, value);
    }

    /**
     * Removes the initial parameter.
     * 
     * @param name
     *            the parameter name
     */
    public void removeInitParameter(String name) {
        initParameterMap.remove(name);
    }

    /**
     * @see javax.servlet.ServletContext#getAttribute(java.lang.String)
     */
    public Object getAttribute(String name) {
        return attributeTable.get(name);
    }

    public Enumeration<String> getAttributeNames() {
        return new IteratorEnumeration<String>(attributeTable.keySet()
                .iterator());
    }

    public void setAttribute(String name, Object value) {
        attributeTable.put(name, value);
    }

    /**
     * @see javax.servlet.ServletContext#removeAttribute(java.lang.String)
     */
    public void removeAttribute(String name) {
        attributeTable.remove(name);
    }

    /**
     * @see javax.servlet.ServletContext#getServletContextName()
     */
    public String getServletContextName() {
        return servletContextName;
    }

    /**
     * Sets the servlet context name.
     * 
     * @param servletContextName
     *            the servlet context name
     */
    public void setServletContextName(String servletContextName) {
        this.servletContextName = servletContextName;
    }
}