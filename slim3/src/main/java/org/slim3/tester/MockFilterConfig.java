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

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

/**
 * A mock implementation for {@link FilterConfig}.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class MockFilterConfig implements FilterConfig {

    /**
     * The servlet context.
     */
    protected ServletContext servletContext;

    /**
     * The filter name.
     */
    protected String filterName;

    /**
     * The map for initial parameters.
     */
    protected Map<String, String> initParameterMap =
        new HashMap<String, String>();

    /**
     * Constructor.
     * 
     * @param servletContext
     *            the servlet context
     * @throws NullPointerException
     *             if the servletContext parameter is null
     */
    public MockFilterConfig(ServletContext servletContext)
            throws NullPointerException {
        if (servletContext == null) {
            throw new NullPointerException(
                "The servletContext parameter is null.");
        }
        this.servletContext = servletContext;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public String getFilterName() {
        return filterName;
    }

    /**
     * Sets the filter name.
     * 
     * @param filterName
     *            the filter name
     */
    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public String getInitParameter(String name) {
        return initParameterMap.get(name);
    }

    /**
     * Sets the initial parameter
     * 
     * @param name
     *            the initial parameter name
     * @param value
     *            the value
     */
    public void setInitParameter(String name, final String value) {
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

    public Enumeration<String> getInitParameterNames() {
        return Collections.enumeration(initParameterMap.keySet());
    }
}