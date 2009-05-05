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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * This class parses the request parameters.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class RequestParameterParser {

    /**
     * The request.
     */
    protected HttpServletRequest request;

    /**
     * The request parameters.
     */
    protected Map<String, Object> parameters = new HashMap<String, Object>(27);

    /**
     * Constructor.
     * 
     * @param request
     *            the request
     */
    public RequestParameterParser(HttpServletRequest request) {
        if (request == null) {
            throw new NullPointerException("The request parameter is null.");
        }
        this.request = request;
    }

    /**
     * Parses the request parameters
     * 
     * @return the parsed parameters
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> parse() {
        for (Enumeration<String> e = request.getParameterNames(); e
            .hasMoreElements();) {
            String name = e.nextElement();
            parameters.put(name, request.getParameterValues(name));
        }
        return parameters;
    }
}