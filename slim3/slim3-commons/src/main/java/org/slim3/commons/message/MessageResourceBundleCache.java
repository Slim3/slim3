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
package org.slim3.commons.message;

import java.io.File;

/**
 * A class to cache {@link MessageResourceBundle}.
 * 
 * @author higa
 * @since 3.0
 */
public class MessageResourceBundleCache {

    /**
     * The message bundle.
     */
    protected MessageResourceBundle bundle;

    /**
     * The file for the message bundle.
     */
    protected File file;

    /**
     * The last modified time.
     */
    protected long lastModified;

    /**
     * Constructor.
     * 
     * @param bundle
     *            the message bundle
     * @param file
     *            the file for the message bundle
     */
    public MessageResourceBundleCache(MessageResourceBundle bundle, File file) {
        this.bundle = bundle;
        this.file = file;
        if (file != null) {
            lastModified = file.lastModified();
        }
    }

    /**
     * Determines if the bundle is modified.
     * 
     * @return whether the bundle is modified
     */
    public boolean isModified() {
        if (file == null) {
            return false;
        }
        return file.lastModified() > lastModified;
    }

    /**
     * Returns the message bundle.
     * 
     * @return the message bundle
     */
    public MessageResourceBundle getBundle() {
        return bundle;
    }

    /**
     * Sets the message bundle.
     * 
     * @param bundle
     *            the message bundle
     */
    public void setBundle(MessageResourceBundle bundle) {
        this.bundle = bundle;
    }
}