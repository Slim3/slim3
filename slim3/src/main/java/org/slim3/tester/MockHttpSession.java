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

import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

/**
 * A mock implementation for {@link HttpSession}.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class MockHttpSession implements HttpSession, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The creation time.
     */
    protected final long creationTime = System.currentTimeMillis();

    /**
     * The last accessed time.
     */
    protected long lastAccessedTime = creationTime;

    /**
     * The servlet context.
     */
    protected ServletContext servletContext;

    /**
     * The identifier.
     */
    protected String id = "1";

    /**
     * Whether this session is created by the current request.
     */
    protected boolean newFlag = true;

    /**
     * The max inactive internal.
     */
    protected int maxInactiveInterval = -1;

    /**
     * The map for the attributes.
     */
    protected Map<String, Object> attributeMap = new HashMap<String, Object>();

    /**
     * Whether this session is valid.
     */
    protected boolean valid = true;

    /**
     * Constructor.
     * 
     * @param servletContext
     *            the servlet context
     * @throws NullPointerException
     *             if the servletContext parameter is null
     */
    public MockHttpSession(ServletContext servletContext)
            throws NullPointerException {
        if (servletContext == null) {
            throw new NullPointerException(
                "The servletContext parameter is null.");
        }
        this.servletContext = servletContext;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public String getId() {
        return id;
    }

    /**
     * Sets the identifier.
     * 
     * @param id
     *            the identifier
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @see javax.servlet.http.HttpSession#getLastAccessedTime()
     */
    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    /**
     * Reuses this session.
     */
    public void updateLastAccessedTime() {
        lastAccessedTime = System.currentTimeMillis();
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    public void setMaxInactiveInterval(int maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    public Object getAttribute(String name) {
        return attributeMap.get(name);
    }

    public void setAttribute(String name, Object value) {
        attributeMap.put(name, value);
    }

    public void removeAttribute(String name) {
        attributeMap.remove(name);
    }

    @SuppressWarnings("deprecation")
    public javax.servlet.http.HttpSessionContext getSessionContext() {
        return null;
    }

    public Object getValue(String name) {
        return getAttribute(name);
    }

    public Enumeration<String> getAttributeNames() {
        return Collections.enumeration(attributeMap.keySet());
    }

    public String[] getValueNames() {
        return attributeMap.keySet().toArray(new String[attributeMap.size()]);
    }

    public void putValue(String name, Object value) {
        setAttribute(name, value);
    }

    public void removeValue(String name) {
        removeAttribute(name);
    }

    public void invalidate() {
        attributeMap.clear();
        valid = false;
    }

    public boolean isNew() {
        return newFlag;
    }

    /**
     * Sets the new flag.
     * 
     * @param newFlag
     *            the new flag
     */
    public void setNew(boolean newFlag) {
        this.newFlag = newFlag;
    }

    /**
     * Determines if this session is valid.
     * 
     * @return whether this session is valid.
     */
    public boolean isValid() {
        return valid;
    }
}