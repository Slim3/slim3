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
 * An delegate interface to get the application message.
 * 
 * @author higa
 * @since 1.0.2
 * 
 */
public interface ApplicationMessageDelegate {

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
    String get(String key, Object... args) throws MissingResourceException;

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
    void setBundle(String bundleName, Locale locale)
            throws NullPointerException;

    /**
     * Clears the resource bundle attached to the current thread.
     */
    void clearBundle();
}