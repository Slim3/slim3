/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.slim3.struts.util;

import java.util.Locale;

import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;
import org.slim3.struts.web.WebLocator;

/**
 * A utility for {@link MessageResources}.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public final class MessageResourcesUtil {

    private MessageResourcesUtil() {
    }

    /**
     * Returns the message resources.
     * 
     * @return the message resources
     */
    public static MessageResources getMessageResources() {
        return (MessageResources) WebLocator.getServletContext().getAttribute(
                Globals.MESSAGES_KEY);
    }

    /**
     * Returns the message.
     * 
     * @param locale
     *            the locale
     * @param key
     *            the key
     * @return the message
     */
    public static String getMessage(Locale locale, String key) {
        return getMessageResources().getMessage(locale, key);
    }

    /**
     * Returns the message.
     * 
     * @param key
     *            the key
     * @return the message
     */
    public static String getMessage(String key) {
        return getMessageResources().getMessage(key);
    }
}