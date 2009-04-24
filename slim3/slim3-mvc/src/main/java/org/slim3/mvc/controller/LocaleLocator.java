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

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slim3.mvc.MvcConstants;

/**
 * The locator for {@link Locale}.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public final class LocaleLocator {

    /**
     * Returns the locale attached to the current thread.
     * 
     * @return the locale attached to the current thread
     * @throws IllegalStateException
     *             if the request attached to the current thread is not found
     */
    public static Locale getLocale() throws IllegalStateException {
        HttpServletRequest request = RequestLocator.getRequest();
        if (request == null) {
            throw new IllegalStateException(
                    "The request attached to the current thread is not found.");
        }
        Locale locale = (Locale) request.getAttribute(MvcConstants.LOCALE_KEY);
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return locale;
    }

    /**
     * Sets the locale attached to the current thread.
     * 
     * @param locale
     *            the locale attached to the current thread
     * @throws IllegalStateException
     *             if the request attached to the current thread is not found
     */
    public static void setLocale(Locale locale) throws IllegalStateException {
        HttpServletRequest request = RequestLocator.getRequest();
        if (request == null) {
            throw new IllegalStateException(
                    "The request attached to the current thread is not found.");
        }
        if (locale == null) {
            locale = Locale.getDefault();
        }
        request.setAttribute(MvcConstants.LOCALE_KEY, locale);
    }

    private LocaleLocator() {
    }
}