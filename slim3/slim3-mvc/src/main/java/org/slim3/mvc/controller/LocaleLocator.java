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
import javax.servlet.http.HttpSession;

import org.slim3.mvc.MvcConstants;

/**
 * The locator for {@link Controller}.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public final class LocaleLocator {

    /**
     * Returns the locale.
     * 
     * @return the locale
     */
    public static Locale getLocale() {
        Locale locale = null;
        HttpServletRequest request = RequestLocator.getRequest();
        HttpSession session = request.getSession(false);
        if (session != null) {
            locale = (Locale) session.getAttribute(MvcConstants.LOCALE_KEY);
            if (locale != null) {
                return locale;
            }
        }
        locale = request.getLocale();
        if (locale != null) {
            return locale;
        }
        return Locale.getDefault();
    }

    /**
     * Sets the locale.
     * 
     * @param locale
     *            the locale
     */
    public static void setLocale(Locale locale) {
        RequestLocator.getRequest().getSession().setAttribute(
            MvcConstants.LOCALE_KEY,
            locale);
    }

    private LocaleLocator() {
    }
}