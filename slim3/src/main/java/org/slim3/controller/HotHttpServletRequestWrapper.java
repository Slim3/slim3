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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/**
 * {@link HttpServletRequestWrapper} for Slim3.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class HotHttpServletRequestWrapper extends HttpServletRequestWrapper {

    /**
     * The original request.
     */
    protected HttpServletRequest originalRequest;

    /**
     * The http session.
     */
    protected HttpSession session;

    /**
     * Constructor.
     * 
     * @param request
     *            the original request
     */
    public HotHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        originalRequest = request;
    }

    @Override
    public HttpSession getSession() {
        return getSession(true);
    }

    @Override
    public HttpSession getSession(boolean create) {
        if (session != null) {
            return session;
        }
        HttpSession originalSession = originalRequest.getSession(create);
        if (originalSession == null) {
            return null;
        }
        session = new HotHttpSessionWrapper(originalSession, this);
        return session;
    }

    /**
     * Returns the original request.
     * 
     * @return the original request
     */
    public HttpServletRequest getOriginalRequest() {
        return originalRequest;
    }

    /**
     * Invalidates the session.
     */
    protected void invalidateSession() {
        session = null;
    }
}