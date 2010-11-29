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

import java.util.Locale;
import java.util.MissingResourceException;

/**
 * A class to get the application message.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public final class ApplicationMessage {

    /**
     * The key of ApplicationMessageDelegate.
     */
    public static final String DELEGATE_KEY =
        "slim3.applicationMessageDelegate";

    /**
     * The delegate.
     */
    protected static ApplicationMessageDelegate delegate;

    static {
        setDelegateClass(ResourceBundleApplicationMessageDelegate.class);
    }

    /**
     * Initializes this class.
     */
    protected static void initialize() {
        String className =
            System.getProperty(
                DELEGATE_KEY,
                ResourceBundleApplicationMessageDelegate.class.getName());
        Class<? extends ApplicationMessageDelegate> clazz =
            ClassUtil.forName(className);
        setDelegateClass(clazz);
    }

    /**
     * Sets the delegate class.
     * 
     * @param clazz
     *            the delegate class
     */
    public static void setDelegateClass(
            Class<? extends ApplicationMessageDelegate> clazz) {
        delegate = ClassUtil.newInstance(clazz);
    }

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
        delegate.setBundle(bundleName, locale);
    }

    /**
     * Clears the resource bundle attached to the current thread.
     */
    public static void clearBundle() {
        delegate.clearBundle();
    }

    /**
     * Returns the message.
     * 
     * @param key
     *            the key
     * @param args
     *            the arguments
     * @return the message
     * @throws MissingResourceException
     *             if the message is missing
     */
    public static String get(String key, Object... args)
            throws MissingResourceException {
        return delegate.get(key, args);
    }

    /**
     * Returns the delegate.
     * 
     * @param <T>
     *            the delegate type
     * @return the delegate
     */
    @SuppressWarnings("unchecked")
    public static <T extends ApplicationMessageDelegate> T getDelegate() {
        return (T) delegate;
    }

    private ApplicationMessage() {
    }
}