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
package org.slim3.util;

import org.slim3.exception.WrapRuntimeException;

/**
 * A utility class for {@link RuntimeException}.
 * 
 * @author higa
 * @version 3.0
 */
public final class RuntimeExceptionUtil {

    /**
     * Converts the exception to {@link RuntimeException}.
     * 
     * @param exception
     *            the exception
     * @return {@link RuntimeException}
     * @throws NullPointerException
     *             if the exception parameter is null
     */
    public static RuntimeException convert(Throwable exception)
            throws NullPointerException {
        if (exception == null) {
            throw new NullPointerException("The exception parameter is null.");
        }
        if (exception instanceof RuntimeException) {
            return (RuntimeException) exception;
        }
        return new WrapRuntimeException(exception);
    }

    /**
     * Wraps and throws {@link RuntimeException}.
     * 
     * @param throwable
     *            the exception
     * @throws WrapRuntimeException
     *             if the exception except {@link RuntimeException} has occurred
     */
    public static void wrapAndThrow(Throwable throwable)
            throws WrapRuntimeException {
        throw convert(throwable);
    }

    private RuntimeExceptionUtil() {
    }
}