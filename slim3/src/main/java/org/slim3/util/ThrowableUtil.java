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


/**
 * A utility class for {@link RuntimeException}.
 * 
 * @author higa
 * @version 3.0
 */
public final class ThrowableUtil {

    /**
     * Wraps and throws the exception.
     * 
     * @param throwable
     *            the exception
     * @throws NullPointerException
     *             if the throwable parameter is null
     * @throws RuntimeException
     *             if the exception is {@link RuntimeException}
     * @throws Error
     *             if the exception is {@link Error}
     * @throws WrapRuntimeException
     *             if the exception except {@link RuntimeException} and
     *             {@link Error} occurred
     */
    public static void wrapAndThrow(Throwable throwable)
            throws NullPointerException, RuntimeException, Error,
            WrapRuntimeException {
        if (throwable == null) {
            throw new NullPointerException("The throwable parameter is null.");
        }
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        }
        if (throwable instanceof Error) {
            throw (Error) throwable;
        }
        throw new WrapRuntimeException(throwable);
    }

    private ThrowableUtil() {
    }
}