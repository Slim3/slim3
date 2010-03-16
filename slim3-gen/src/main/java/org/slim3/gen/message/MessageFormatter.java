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
package org.slim3.gen.message;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Formats the message.
 * 
 * @author taedium
 * @since 1.0.0
 */
public final class MessageFormatter {

    /** the resource bundle */
    protected static ResourceBundle bundle =
        ResourceBundle.getBundle(MessageResource.class.getName());

    /**
     * Returns the formatted message.
     * 
     * @param messageCode
     *            the message code.
     * @param args
     *            the arguments.
     * @return a formatted message.
     */
    public static String getMessage(MessageCode messageCode, Object... args) {
        String pattern = bundle.getString(messageCode.name());
        return MessageFormat.format(
            "[" + messageCode.name() + "] " + pattern,
            args);
    }

    /**
     * Returns the simple formatted message.
     * 
     * @param messageCode
     *            the message code.
     * @param args
     *            the arguments.
     * @return a formatted message.
     */
    public static String getSimpleMessage(MessageCode messageCode,
            Object... args) {
        String pattern = bundle.getString(messageCode.name());
        return MessageFormat.format(pattern, args);
    }
}
