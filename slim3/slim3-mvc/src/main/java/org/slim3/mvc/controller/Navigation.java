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

/**
 * This class represents where to go.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class Navigation {

    /**
     * The path.
     */
    protected String path;

    /**
     * Whether this navigation is "redirect".
     */
    protected boolean redirect = false;

    /**
     * Constructor.
     */
    public Navigation() {
        this(null, false);
    }

    /**
     * Constructor.
     * 
     * @param path
     *            the path
     */
    public Navigation(String path) {
        this(path, false);
    }

    /**
     * Constructor.
     * 
     * @param redirect
     *            whether this navigation is "redirect"
     */
    public Navigation(boolean redirect) {
        this(null, redirect);
    }

    /**
     * Constructor.
     * 
     * @param path
     *            the path
     * @param redirect
     *            whether this navigation is "redirect"
     */
    public Navigation(String path, boolean redirect) {
        this.path = path;
        this.redirect = redirect;
    }

    /**
     * Returns the path.
     * 
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the path.
     * 
     * @param path
     *            the path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Determines if this navigation is "redirect".
     * 
     * @return whether this navigation is "redirect"
     */
    public boolean isRedirect() {
        return redirect;
    }

    /**
     * Sets whether this navigation is "redirect"
     * 
     * @param redirect
     *            whether this navigation is "redirect"
     */
    public void setRedirect(boolean redirect) {
        this.redirect = redirect;
    }
}