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

import java.util.Properties;

/**
 * This class represents the message bundle.
 * 
 * @author higa
 * @since 3.0
 */
public class MessageResourceBundle {

    /**
     * The properties.
     */
    protected Properties properties;

    /**
     * The parent.
     */
    protected MessageResourceBundle parent;

    /**
     * Constructor.
     * 
     * @param properties
     *            the properties
     */
    public MessageResourceBundle(Properties properties) {
        this(properties, null);
    }

    /**
     * Constructor.
     * 
     * @param properties
     *            the properties
     * @param parent
     *            the parent
     */
    public MessageResourceBundle(Properties properties,
            MessageResourceBundle parent) {
        this.properties = properties;
        setParent(parent);
    }

    /**
     * Returns the message. If this object and parent have same key, this object
     * is prior. If the key does not exist in this object and parent, returns
     * null.
     * 
     * @param key
     *            the key
     * @return the message
     */
    public String get(String key) {
        if (key == null) {
            return null;
        }
        if (properties.containsKey(key)) {
            return properties.getProperty(key);
        }
        return (parent != null) ? parent.get(key) : null;
    }

    /**
     * Returns the parent.
     * 
     * @return the parent
     */
    public MessageResourceBundle getParent() {
        return parent;
    }

    /**
     * Sets the parent.
     * 
     * @param parent
     *            the parent
     */
    public void setParent(MessageResourceBundle parent) {
        this.parent = parent;
    }
}