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

import org.apache.struts.util.MessageResources;
import org.apache.struts.util.MessageResourcesFactory;
import org.slim3.commons.cleaner.Cleanable;
import org.slim3.commons.cleaner.Cleaner;
import org.slim3.commons.message.MessageResourceBundle;
import org.slim3.commons.message.MessageResourceBundleFactory;

/**
 * {@link MessageResources} for Slim3.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class S3PropertyMessageResources extends MessageResources {

    private static final long serialVersionUID = 1L;

    /**
     * Whether this object was initialized.
     */
    protected volatile boolean initialized = false;

    /**
     * Constructor.
     * 
     * @param factory
     *            the message resources factory
     * @param config
     *            the path of configuration file
     */
    public S3PropertyMessageResources(MessageResourcesFactory factory,
            String config) {
        super(factory, config);
        initialize();
    }

    @Override
    public String getMessage(Locale locale, String key) {
        if (!initialized) {
            initialize();
        }
        if (locale == null) {
            locale = defaultLocale;
        }
        MessageResourceBundle bundle = MessageResourceBundleFactory.getBundle(
                locale, config);
        return bundle.get(key);
    }

    /**
     * Initializes this object.
     */
    protected void initialize() {
        Cleaner.add(new Cleanable() {
            public void clean() {
                formats.clear();
                initialized = false;
            }
        });
        initialized = true;
    }
}