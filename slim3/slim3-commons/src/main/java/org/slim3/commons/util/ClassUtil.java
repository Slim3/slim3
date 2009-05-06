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
package org.slim3.commons.util;

import org.slim3.commons.exception.ClassNotFoundRuntimeException;
import org.slim3.commons.exception.IllegalAccessRuntimeException;
import org.slim3.commons.exception.InstantiationRuntimeException;

/**
 * A utility class for {@link Class}.
 * 
 * @author higa
 * @since 3.0
 */
public final class ClassUtil {

    private ClassUtil() {
    }

    /**
     * Returns a class instance.
     * 
     * @param className
     *            the class name
     * @param loader
     *            the class loader
     * @return a class instance
     * @throws NullPointerException
     *             if the className parameter is null or if the loader parameter
     *             is null
     * @throws ClassNotFoundRuntimeException
     *             if {@link ClassNotFoundException} is encountered
     */
    public static Class<?> forName(String className, ClassLoader loader)
            throws NullPointerException, ClassNotFoundRuntimeException {
        if (className == null) {
            throw new NullPointerException("The className parameter is null.");
        }
        if (loader == null) {
            throw new NullPointerException("The loader parameter is null.");
        }
        try {
            return Class.forName(className, true, loader);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundRuntimeException(className, e);
        }
    }

    /**
     * Creates a new instance.
     * 
     * @param <T>
     *            the target type
     * @param clazz
     *            the class
     * @return a new instance
     * @throws InstantiationRuntimeException
     *             if {@link InstantiationException} is encountered.
     * @throws IllegalAccessRuntimeException
     *             if {@link IllegalAccessException} is encountered.
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<?> clazz) {
        try {
            return (T) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new InstantiationRuntimeException(clazz, e);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessRuntimeException(clazz, e);
        }
    }

    /**
     * Creates a new instance.
     * 
     * @param <T>
     *            the target type
     * @param className
     *            the class name
     * @param loader
     *            the class loader
     * @return a new instance
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(String className, ClassLoader loader) {
        Class<?> clazz = forName(className, loader);
        return (T) newInstance(clazz);
    }
}