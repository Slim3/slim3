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

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

/**
 * This class handles the request.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class RequestHandler {

    /**
     * The array suffix.
     */
    protected static final String ARRAY_SUFFIX = "Array";

    /**
     * The request.
     */
    protected HttpServletRequest request;

    /**
     * Constructor.
     * 
     * @param request
     *            the request
     */
    public RequestHandler(HttpServletRequest request) {
        if (request == null) {
            throw new NullPointerException("The request parameter is null.");
        }
        this.request = request;
    }

    /**
     * Handles the request.
     * 
     */
    @SuppressWarnings("unchecked")
    public void handle() {
        for (Enumeration<String> e = request.getParameterNames(); e
            .hasMoreElements();) {
            String name = e.nextElement();
            if (name.endsWith(ARRAY_SUFFIX)) {
                request.setAttribute(name, normalizeValues(request
                    .getParameterValues(name)));
            } else {
                request.setAttribute(name, normalizeValue(request
                    .getParameter(name)));
            }
        }
    }

    /**
     * Normalizes the value.
     * 
     * @param value
     *            the value
     * @return the normalized value
     */
    protected String normalizeValue(String value) {
        return value;
    }

    /**
     * Normalizes the values.
     * 
     * @param values
     *            the values
     * @return the normalized values
     */
    protected String[] normalizeValues(String[] values) {
        if (values == null) {
            return new String[0];
        }
        String[] ret = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = normalizeValue(values[i]);
        }
        return ret;
    }
}