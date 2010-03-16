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

/**
 * A utility for AppEngine.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public final class AppEngineUtil {

    private static final String RUNTIME = "com.google.appengine.runtime";

    /**
     * The environment key.
     */
    public static final String ENVIRONMENT_KEY = RUNTIME + ".environment";

    /**
     * The version value.
     */
    public static final String VERSION_KEY = RUNTIME + ".version";

    /**
     * The production value.
     */
    public static final String PRODUCTION = "Production";

    /**
     * The development value.
     */
    public static final String DEVELOPMENT = "Development";

    /**
     * Determines if this application is running on App Engine Server.
     * 
     * @return whether this application is running on App Engine Server
     */
    public static boolean isServer() {
        return System.getProperty(ENVIRONMENT_KEY) != null;
    }

    /**
     * Determines if this application is running on development server.
     * 
     * @return whether this application is running on development server
     */
    public static boolean isDevelopment() {
        return DEVELOPMENT.equals(System.getProperty(ENVIRONMENT_KEY));
    }

    /**
     * Determines if this application is running on production server.
     * 
     * @return whether this application is running on production server
     */
    public static boolean isProduction() {
        return PRODUCTION.equals(System.getProperty(ENVIRONMENT_KEY));
    }

    /**
     * Returns the version
     * 
     * @return the version
     */
    public static String getVersion() {
        return System.getProperty(VERSION_KEY);
    }

    private AppEngineUtil() {
    }
}