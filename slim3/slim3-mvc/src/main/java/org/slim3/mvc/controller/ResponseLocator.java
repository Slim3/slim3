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
package org.slim3.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The locator for {@link HttpServletRequest}.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public final class ResponseLocator {

    private static ThreadLocal<HttpServletResponse> responseThreadLocal = new ThreadLocal<HttpServletResponse>();

    /**
     * Returns the current response.
     * 
     * @return the current response
     */
    public static HttpServletResponse getResponse() {
        return responseThreadLocal.get();
    }

    /**
     * Sets the current response.
     * 
     * @param response
     *            the current response
     */
    public static void setResponse(HttpServletResponse response) {
        responseThreadLocal.set(response);
    }

    private ResponseLocator() {
    }
}