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

import java.text.MessageFormat;

import org.slim3.commons.config.Configuration;
import org.slim3.commons.message.MessageResourceBundle;
import org.slim3.commons.message.MessageResourceBundleFactory;
import org.slim3.commons.util.ClassUtil;
import org.slim3.commons.util.StringUtil;
import org.slim3.mvc.MvcConstants;

/**
 * This class builds the application message.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class AppMessageBuilder {

    /**
     * The default bundle name.
     */
    protected static final String DEFAULT_BUNDLE_NAME = "application";

    /**
     * The instance.
     */
    protected static AppMessageBuilder instance;

    static {
        initialize();
    }

    /**
     * Initializes this class.
     */
    protected static void initialize() {
        String className = Configuration.getInstance().getValue(
                MvcConstants.APP_MESSAGE_BUILDER_KEY);
        if (className == null) {
            className = AppMessageBuilder.class.getName();
        }
        instance = ClassUtil.newInstance(className, Thread.currentThread()
                .getContextClassLoader());
    }

    /**
     * Returns the instance.
     * 
     * @return the instance
     */
    public static AppMessageBuilder getInstance() {
        return instance;
    }

    /**
     * Constructor.
     */
    public AppMessageBuilder() {
    }

    /**
     * Returns the message.
     * 
     * @param key
     *            the key
     * @param arguments
     *            the arguments
     * @return the message
     * @throws NullPointerException
     *             if the key parameter is null
     */
    public String getMessage(String key, Object... arguments)
            throws NullPointerException {
        if (key == null) {
            throw new NullPointerException("The key parameter is null.");
        }
        MessageResourceBundle bundle = getBundle();
        String pattern = bundle.get(key);
        if (StringUtil.isEmpty(pattern)) {
            return null;
        }
        return MessageFormat.format(pattern, arguments);
    }

    /**
     * Returns the message resource bundle.
     * 
     * @return the message resource bundle
     */
    protected MessageResourceBundle getBundle() {
        String bundleName = Configuration.getInstance().getValue(
                MvcConstants.APP_MESSAGE_BUNDLE_NAME_KEY);
        if (bundleName == null) {
            bundleName = DEFAULT_BUNDLE_NAME;
        }
        return MessageResourceBundleFactory.getBundle(
                LocaleLocator.getLocale(), bundleName);

    }
}