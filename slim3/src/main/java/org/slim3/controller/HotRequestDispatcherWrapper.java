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

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * {@link RequestDispatcher} for HOT reloading.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class HotRequestDispatcherWrapper implements RequestDispatcher {

    /**
     * The original dispatcher.
     */
    protected RequestDispatcher originalDispatcher;

    /**
     * Constructor.
     * 
     * @param originalDispatcher
     *            the original dispatcher
     * @throws NullPointerException
     *             if the originalDispatcher parameter is null
     */
    public HotRequestDispatcherWrapper(RequestDispatcher originalDispatcher)
            throws NullPointerException {
        if (originalDispatcher == null) {
            throw new NullPointerException(
                "The originalDispatcher parameter is null.");
        }
        this.originalDispatcher = originalDispatcher;
    }

    /**
     * Returns the original request dispatcher.
     * 
     * @return the original request dispatcher
     */
    public RequestDispatcher getOriginalDispatcher() {
        return originalDispatcher;
    }

    public void forward(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
        originalDispatcher.forward(getOriginalRequest(request), response);

    }

    public void include(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
        originalDispatcher.include(getOriginalRequest(request), response);
    }

    /**
     * Returns the original request.
     * 
     * @param request
     *            the request
     * @return the original request
     */
    protected ServletRequest getOriginalRequest(ServletRequest request) {
        if (request instanceof HotHttpServletRequestWrapper) {
            return ((HotHttpServletRequestWrapper) request)
                .getOriginalRequest();
        }
        return request;
    }
}
