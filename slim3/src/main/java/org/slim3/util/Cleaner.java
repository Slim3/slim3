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

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A utility class to clean cached resources.
 * 
 * @author higa
 * @since 1.0.0
 */
public final class Cleaner {

    static final LinkedList<Cleanable> cleanables = new LinkedList<Cleanable>();

    private static final Logger logger =
        Logger.getLogger(Cleaner.class.getName());

    private static Method clearCacheMethod;

    private static Map<?, ?> cacheList;

    static {
        try {
            clearCacheMethod =
                ResourceBundle.class.getMethod("clearCache", ClassLoader.class);
        } catch (Throwable ignore) {
            try {
                Field field =
                    ResourceBundle.class.getDeclaredField("cacheList");
                field.setAccessible(true);
                cacheList = (Map<?, ?>) field.get(null);
            } catch (Throwable ignore2) {
            }
        }
    }

    /**
     * Adds the cleanable resource.
     * 
     * @param cleanable
     *            the cleanable resource
     */
    public static void add(Cleanable cleanable) {
        cleanables.add(cleanable);
    }

    /**
     * Removes the cleanable resource.
     * 
     * @param cleanable
     *            the cleanable resource
     */
    public static void remove(Cleanable cleanable) {
        cleanables.remove(cleanable);
    }

    /**
     * Cleans all resources.
     */
    public static void cleanAll() {
        while (!cleanables.isEmpty()) {
            Cleanable cleanable = cleanables.removeLast();
            try {
                cleanable.clean();
            } catch (Throwable t) {
                if (logger.isLoggable(Level.WARNING)) {
                    logger.log(Level.WARNING, t.getMessage(), t);
                }
            }
        }
        try {
            Introspector.flushCaches();
        } catch (Throwable t) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, t.getMessage(), t);
            }
        }
        try {
            if (clearCacheMethod != null) {
                clearCacheMethod.invoke(null, Thread
                    .currentThread()
                    .getContextClassLoader());
            } else if (cacheList != null) {
                cacheList.clear();
            }
        } catch (Throwable t) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, t.getMessage(), t);
            }
        }
    }
}