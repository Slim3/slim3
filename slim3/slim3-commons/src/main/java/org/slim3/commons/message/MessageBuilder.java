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
package org.slim3.commons.message;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * A class to build a message.
 * 
 * @author higa
 * @since 3.0
 */
public final class MessageBuilder {

    private static final String MESSAGES = "Messages";

    private MessageBuilder() {
    }

    /**
     * Returns the message.
     * 
     * @param messageCode
     *            the message code
     * @param args
     *            the arguments
     * @return the message
     */
    public static String getMessage(String messageCode, Object... args) {
        return getMessage(null, messageCode, args);
    }

    /**
     * Returns the message.
     * 
     * @param locale
     *            the locale
     * @param messageCode
     *            the message code
     * @param args
     *            the arguments
     * @return the message
     */
    public static String getMessage(Locale locale, String messageCode,
            Object... args) {
        if (messageCode == null) {
            return getMessageByArgs(args);
        }
        if (locale == null) {
            locale = Locale.getDefault();
        }
        try {
            String pattern = getPattern(locale, messageCode);
            if (pattern != null) {
                return MessageFormat.format(pattern, args);
            }
        } catch (Throwable ignore) {
        }
        return getMessageByArgs(args);
    }

    /**
     * Returns the message built by the arguments.
     * 
     * @param args
     *            the arguments
     * @return a message
     */
    protected static String getMessageByArgs(Object... args) {
        if (args == null || args.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder(100);
        for (Object arg : args) {
            builder.append(arg + ", ");
        }
        builder.setLength(builder.length() - 2);
        return builder.toString();
    }

    /**
     * Returns the pattern of {@link MessageFormat}.
     * 
     * @param locale
     *            the locale
     * @param messageCode
     *            the message code
     * @return the pattern of {@link MessageFormat}
     */
    protected static String getPattern(Locale locale, String messageCode) {
        String bundleName = calculateBundleName(messageCode);
        if (bundleName == null) {
            return null;
        }
        MessageResourceBundle bundle = MessageResourceBundleFactory.getBundle(
                locale, bundleName);
        if (bundle == null) {
            return null;
        }
        String key = calculateKey(messageCode);
        return bundle.get(key);
    }

    /**
     * Calculates the bundle name for the message code.
     * 
     * @param messageCode
     *            the message code
     * @return the bundle name
     */
    protected static String calculateBundleName(String messageCode) {
        int index = messageCode.indexOf('-');
        if (index < 0) {
            return null;
        }
        return messageCode.substring(0, index) + MESSAGES;
    }

    /**
     * Calculates the key for the message code.
     * 
     * @param messageCode
     *            the message code
     * @return the key
     */
    protected static String calculateKey(String messageCode) {
        int index = messageCode.indexOf('-');
        if (index < 0) {
            return null;
        }
        return messageCode.substring(index + 1);
    }
}