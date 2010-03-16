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
 * A utility class to access caller information.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public final class CallerUtil {

    private static MySecurityManager sm = new MySecurityManager();

    /**
     * Returns the current execution stack.
     * 
     * @return the current execution stack
     */
    public static Class<?>[] getCallerStack() {
        return sm.getClassContext();
    }

    /**
     * Returns the caller class.
     * 
     * @return the caller class
     */
    public static Class<?> getCaller() {
        return sm.getClassContext()[3];
    }

    /**
     * Returns the caller class loader.
     * 
     * @return the caller class loader
     */
    public static ClassLoader getCallerClassLoader() {
        return sm.getClassContext()[3].getClassLoader();
    }

    private CallerUtil() {
    }

    private static class MySecurityManager extends SecurityManager {

        @Override
        protected Class<?>[] getClassContext() {
            return super.getClassContext();
        }

    }
}
