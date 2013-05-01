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

import java.io.Serializable;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.slim3.util.ByteUtil;
import org.slim3.util.Cleanable;
import org.slim3.util.Cleaner;

/**
 * {@link HttpSession} for HOT reloading.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class HotHttpSessionWrapper implements HttpSession, Cleanable {

    /**
     * The original session.
     */
    protected HttpSession originalSession;

    /**
     * The request wrapper
     */
    protected HotHttpServletRequestWrapper requestWrapper;

    /**
     * Constructor.
     * 
     * @param originalSession
     *            the original session
     * @param requestWrapper
     *            the request wrapper
     * @throws NullPointerException
     *             if the originalSession parameter is null or if the
     *             requestWrapper parameter is null
     */
    @SuppressWarnings("unchecked")
    public HotHttpSessionWrapper(HttpSession originalSession,
            HotHttpServletRequestWrapper requestWrapper)
            throws NullPointerException {
        if (originalSession == null) {
            throw new NullPointerException(
                "The originalSession parameter is null.");
        }
        if (requestWrapper == null) {
            throw new NullPointerException(
                "The requestWrapper parameter is null.");
        }
        this.originalSession = originalSession;
        this.requestWrapper = requestWrapper;
        for (Enumeration<String> e = originalSession.getAttributeNames(); e
            .hasMoreElements();) {
            String name = e.nextElement();
            Object value = originalSession.getAttribute(name);
            if (value instanceof BytesHolder) {
                byte[] bytes = ((BytesHolder) value).getBytes();
                value = ByteUtil.toObject(bytes);
                originalSession.setAttribute(name, value);
            }
        }
        Cleaner.add(this);
    }

    public Object getAttribute(String name) {
        return originalSession.getAttribute(name);
    }

    @SuppressWarnings("unchecked")
    public Enumeration<String> getAttributeNames() {
        return originalSession.getAttributeNames();
    }

    public long getCreationTime() {
        return originalSession.getCreationTime();
    }

    public String getId() {
        return originalSession.getId();
    }

    public long getLastAccessedTime() {
        return originalSession.getLastAccessedTime();
    }

    public int getMaxInactiveInterval() {
        return originalSession.getMaxInactiveInterval();
    }

    public ServletContext getServletContext() {
        return originalSession.getServletContext();
    }

    @SuppressWarnings("deprecation")
    public javax.servlet.http.HttpSessionContext getSessionContext() {
        return originalSession.getSessionContext();
    }

    public Object getValue(String name) {
        return getAttribute(name);
    }

    @SuppressWarnings("deprecation")
    public String[] getValueNames() {
        return originalSession.getValueNames();
    }

    public void invalidate() {
        if (originalSession == null) {
            return;
        }
        originalSession.invalidate();
        originalSession = null;
        requestWrapper.invalidateSession();
        requestWrapper = null;
        Cleaner.remove(this);
    }

    public boolean isNew() {
        return originalSession.isNew();
    }

    public void putValue(String name, Object value) {
        setAttribute(name, value);
    }

    public void removeAttribute(String name) {
        originalSession.removeAttribute(name);
    }

    public void removeValue(String name) {
        removeAttribute(name);
    }

    public void setAttribute(String name, Object value) {
        if (value != null && !(value instanceof Serializable)) {
            throw new IllegalArgumentException("The class("
                + value.getClass().getName()
                + ") is not serializable, so you can not set the attribute("
                + name
                + ").");
        }
        originalSession.setAttribute(name, value);
    }

    public void setMaxInactiveInterval(int interval) {
        originalSession.setMaxInactiveInterval(interval);
    }

    @SuppressWarnings("unchecked")
    public void clean() {
        if (originalSession == null) {
            return;
        }
        for (Enumeration<String> e = originalSession.getAttributeNames(); e
            .hasMoreElements();) {
            String name = e.nextElement();
            if (name.startsWith("__")) {
                continue;
            }
            Object value = originalSession.getAttribute(name);
            if (value == null || value instanceof BytesHolder) {
                continue;
            }
            originalSession.setAttribute(name, new BytesHolder(ByteUtil
                .toByteArray(value)));
        }
    }
}