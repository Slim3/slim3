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
package org.slim3.commons.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.slim3.commons.exception.IllegalAccessRuntimeException;
import org.slim3.commons.exception.InstantiationRuntimeException;

/**
 * A utility class for {@link Constructor}.
 * 
 * @author higa
 * @since 3.0
 */
public final class ConstructorUtil {

    private ConstructorUtil() {
    }

    /**
     * Returns a new instance.
     * 
     * @param constructor
     *            the constructor
     * @param parameters
     *            the parameters
     * @param <T>
     *            the type
     * @return a new instance
     * @throws IllegalAccessRuntimeException
     *             if {@link IllegalAccessException} is encountered
     * @throws InstantiationRuntimeException
     *             if {@link InstantiationException} is encountered
     * 
     */
    public static <T> T newInstance(Constructor<T> constructor,
            Object... parameters) throws IllegalAccessRuntimeException,
            InstantiationRuntimeException {
        try {
            return constructor.newInstance(parameters);
        } catch (InstantiationException e) {
            throw new InstantiationRuntimeException(constructor
                    .getDeclaringClass(), e);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessRuntimeException(constructor
                    .getDeclaringClass(), e);
        } catch (InvocationTargetException e) {
            Throwable t = e.getCause();
            throw RuntimeExceptionUtil.convert(t);
        }
    }
}