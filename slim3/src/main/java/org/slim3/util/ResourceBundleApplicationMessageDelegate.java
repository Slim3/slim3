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
package org.slim3.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * A class to get the application message using {@link ResourceBundle}.
 * 
 * @author higa
 * @since 1.0.2
 * 
 */
public class ResourceBundleApplicationMessageDelegate implements
        ApplicationMessageDelegate {

    /**
     * The resource bundles.
     */
    protected ThreadLocal<ResourceBundle> bundles =
        new ThreadLocal<ResourceBundle>();

    public void setBundle(String bundleName, Locale locale)
            throws NullPointerException {
        if (bundleName == null) {
            throw new NullPointerException("The bundleName parameter is null.");
        }
        if (locale == null) {
            throw new NullPointerException("The locale parameter is null.");
        }
        try {
            bundles.set(ResourceBundle.getBundle(bundleName, locale, Thread
                .currentThread()
                .getContextClassLoader()));
        } catch (MissingResourceException ignore) {
            bundles.set(ResourceBundle.getBundle(
                bundleName,
                Locale.ENGLISH,
                Thread.currentThread().getContextClassLoader()));
        }
    }

    public void clearBundle() {
        bundles.set(null);
    }

    public String get(String key, Object... args)
            throws MissingResourceException {
        ResourceBundle bundle = bundles.get();
        if (bundle == null) {
            throw new IllegalStateException(
                "The bundle attached to the current thread is not found.");
        }
        String pattern = bundle.getString(key);
        return MessageFormat.format(pattern, args);
    }
}