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
     * The stage key.
     */
    public static final String STAGE_KEY = "slim3.stage";

    /**
     * The deployment key.
     */
    public static final String HOT_KEY = "slim3.hot";

    /**
     * production value.
     */
    public static final String PRODUCTION = "production";

    /**
     * development value.
     */
    public static final String DEVELOPMENT = "development";

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
    protected static final Logger logger = Logger.getLogger(Configuration.class
            .getName());

    /**
     * The configuration data.
     */
    protected Map<String, String> config = new HashMap<String, String>(47);

    /**
     * The stage.
     */
    protected String stage = PRODUCTION;

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
     * @throws IllegalArgumentException
     *             if the path is illegal or if IOException is encountered
     */
    public static void initialize(String path) {
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

    /**
     * Constructor
     */
    @SuppressWarnings("unchecked")
    private Configuration(String path) {
        Properties p = new Properties();
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            URL url = loader.getResource(path);
            if (url == null) {
                throw new IllegalArgumentException("The path(" + path
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
        stage = p.getProperty(STAGE_KEY);
        if (stage == null || stage.length() == 0) {
            stage = PRODUCTION;
        }
        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, "Slim3 stage:" + stage);
        }
        p.remove(STAGE_KEY);
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
        String suffix = "_" + stage;
        int suffixLength = suffix.length();
        for (Iterator i = p.keySet().iterator(); i.hasNext();) {
            String key = (String) i.next();
            String value = (String) p.get(key);
            if (key.endsWith(suffix)) {
                key = key.substring(0, key.length() - suffixLength);
                config.put(key, value);
            } else if (!config.containsKey(key)) {
                config.put(key, value);
            }
        }
    }

    /**
     * Returns the stage value.
     * 
     * @return the stage value
     * 
     */
    public String getStage() {
        return stage;
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