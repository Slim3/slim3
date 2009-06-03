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
package org.slim3.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * A class to get the application message.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public final class ApplicationMessage {

    private static ThreadLocal<ResourceBundle> bundles =
        new ThreadLocal<ResourceBundle>();

    /**
     * Sets the resource bundle to the current thread.
     * 
     * @param bundleName
     *            the resource bundle name
     * @param locale
     *            the locale
     * @throws NullPointerException
     *             if the bundleName parameter is null or if the locale
     *             parameter is null
     */
    public static void setBundle(String bundleName, Locale locale)
            throws NullPointerException {
        if (bundleName == null) {
            throw new NullPointerException("The bundleName parameter is null.");
        }
        if (locale == null) {
            throw new NullPointerException("The locale parameter is null.");
        }
        bundles.set(ResourceBundle.getBundle(bundleName, locale, Thread
            .currentThread()
            .getContextClassLoader()));
    }

    /**
     * Clears the resource bundle attached to the current thread.
     */
    public static void clearBundle() {
        bundles.set(null);
    }

    /**
     * Returns the message.
     * 
     * @param key
     *            the key
     * @param args
     *            the arguments
     * @return the message
     */
    public static String get(String key, Object... args) {
        ResourceBundle bundle = bundles.get();
        if (bundle == null) {
            throw new IllegalStateException(
                "The bundle attached to the current thread is not found.");
        }
        try {
            String pattern = bundle.getString(key);
            return MessageFormat.format(pattern, args);
        } catch (MissingResourceException ignore) {
            return null;
        }
    }

    private ApplicationMessage() {
    }
}