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
package org.slim3.commons.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.slim3.commons.cleaner.Cleanable;
import org.slim3.commons.cleaner.Cleaner;

/**
 * A class to access the configuration information.
 * 
 * @author higa
 * @since 3.0
 */
public final class Configuration {

    /**
     * The deployment key.
     */
    public static final String HOT_KEY = "slim3.hot";

    /**
     * The default path.
     */
    public static final String DEFAULT_PATH = "slim3_configuration.properties";

    /**
     * The instance.
     */
    protected static Configuration instance;

    /**
     * Whether this class was initialized.
     */
    protected static volatile boolean initialized = false;

    /**
     * The logger.
     */
    protected static final Logger logger =
        Logger.getLogger(Configuration.class.getName());

    /**
     * The configuration data.
     */
    protected Map<String, String> config = new HashMap<String, String>(47);

    /**
     * Whether the current deployment mode is hot.
     */
    protected boolean hot = false;

    static {
        initialize();
    }

    /**
     * Initializes the configuration information.
     */
    public static void initialize() {
        initialize(DEFAULT_PATH);
    }

    /**
     * Initializes the configuration information.
     * 
     * @param path
     *            the path for the configuration file
     * @throws NullPointerException
     *             if the path parameter is null
     */
    public static void initialize(String path) throws NullPointerException {
        if (path == null) {
            throw new NullPointerException("The path parameter is null.");
        }
        instance = new Configuration(path);
        Cleaner.add(new Cleanable() {
            public void clean() {
                instance = null;
                initialized = false;
            }
        });
        initialized = true;
    }

    /**
     * Returns the instance.
     * 
     * @return the instance
     */
    public static Configuration getInstance() {
        if (!initialized) {
            initialize();
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    private Configuration(String path) throws IllegalArgumentException {
        Properties p = new Properties();
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            URL url = loader.getResource(path);
            if (url == null) {
                throw new IllegalArgumentException("The path("
                    + path
                    + ") is not found.");
            }
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            InputStream in = connection.getInputStream();
            try {
                p.load(in);
            } finally {
                in.close();
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(path, e);
        }
        String h = System.getProperty(HOT_KEY);
        if (h == null) {
            h = p.getProperty(HOT_KEY);
        }
        if (h != null) {
            hot = "true".equalsIgnoreCase(h);
        } else {
            hot = false;
        }
        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, "Slim3 hot:" + hot);
        }
        p.remove(HOT_KEY);
        for (Iterator i = p.keySet().iterator(); i.hasNext();) {
            String key = (String) i.next();
            String value = System.getProperty(key);
            if (value == null) {
                value = (String) p.get(key);
            }
            if (config.put(key, value) != null) {
                throw new IllegalStateException("The key("
                    + key
                    + ") of configuration("
                    + path
                    + ") is duplicated.");
            }
        }
    }

    /**
     * Determines if the current deployment mode is hot.
     * 
     * @return whether the current deployment mode is hot
     */
    public boolean isHot() {
        return hot;
    }

    /**
     * Returns the string value for the key.
     * 
     * @param key
     *            the key
     * @return the string value.
     * @throws NullPointerException
     *             if the key parameter is null.
     */
    public String getValue(String key) {
        if (key == null) {
            throw new NullPointerException("The key parameter is null.");
        }
        return config.get(key);
    }
}