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

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/**
 * {@link HttpServletRequestWrapper} for HOT reloading.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class HotHttpServletRequestWrapper extends HttpServletRequestWrapper {

    /**
     * The original request.
     */
    protected HttpServletRequest originalRequest;

    /**
     * The session wrapper.
     */
    protected HotHttpSessionWrapper sessionWrapper;

    /**
     * Constructor.
     * 
     * @param request
     *            the original request
     */
    public HotHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        originalRequest = request;
        HttpSession originalSession = request.getSession(false);
        if (originalSession != null) {
            sessionWrapper = new HotHttpSessionWrapper(originalSession, this);
        }
    }

    @Override
    public HttpSession getSession() {
        return getSession(true);
    }

    @Override
    public HttpSession getSession(boolean create) {
        if (sessionWrapper != null) {
            return sessionWrapper;
        }
        HttpSession originalSession = originalRequest.getSession(create);
        if (originalSession == null) {
            return null;
        }
        sessionWrapper = new HotHttpSessionWrapper(originalSession, this);
        return sessionWrapper;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return new HotRequestDispatcherWrapper(originalRequest
            .getRequestDispatcher(path));
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
        sessionWrapper = null;
    }
}