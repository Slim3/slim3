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

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * A mock implementation for {@link RequestDispatcher}.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class MockRequestDispatcher implements RequestDispatcher {

    /**
     * The path.
     */
    protected String path;

    /**
     * The request.
     */
    protected ServletRequest request;

    /**
     * The response
     */
    protected ServletResponse response;

    /**
     * Constructor.
     * 
     * @param path
     *            the path
     */
    public MockRequestDispatcher(String path) {
        this.path = path;
    }

    /**
     * Returns the path.
     * 
     * @return the path
     */
    public String getPath() {
        return path;
    }

    public void forward(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
        this.request = request;
        this.response = response;
    }

    public void include(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
        this.request = request;
        this.response = response;
    }

    /**
     * Returns the request.
     * 
     * @return the request
     */
    public ServletRequest getRequest() {
        return request;
    }

    /**
     * Returns the response.
     * 
     * @return the response
     */
    public ServletResponse getResponse() {
        return response;
    }
}