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

/**
 * This class represents where to go.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class Navigation {

    /**
     * The application-relative path.
     */
    protected String path;

    /**
     * Whether this navigation is "redirect".
     */
    protected boolean redirect = false;

    /**
     * Constructor.
     * 
     * @param path
     *            the application-relative path
     * @param redirect
     *            whether this navigation is "redirect"
     * @throws NullPointerException
     *             if the path parameter is null
     */
    public Navigation(String path, boolean redirect)
            throws NullPointerException {
        if (path == null) {
            throw new NullPointerException("The path parameter is null.");
        }
        this.path = path;
        this.redirect = redirect;
    }

    /**
     * Returns the application-relative path.
     * 
     * @return the application-relative path
     */
    public String getPath() {
        return path;
    }

    /**
     * Determines if this navigation is "redirect".
     * 
     * @return whether this navigation is "redirect"
     */
    public boolean isRedirect() {
        return redirect;
    }
}