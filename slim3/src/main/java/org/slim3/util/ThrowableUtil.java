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
 * A utility class for {@link RuntimeException}.
 * 
 * @author higa
 * @version 3.0
 */
public final class ThrowableUtil {

    /**
     * Wraps the exception.
     * 
     * @param throwable
     *            the exception
     * @return a wrapped exception
     * @throws NullPointerException
     *             if the throwable parameter is null
     * @throws Error
     *             if the exception is {@link Error}
     */
    public static RuntimeException wrap(Throwable throwable)
            throws NullPointerException, Error {
        if (throwable == null) {
            throw new NullPointerException("The throwable parameter is null.");
        }
        if (throwable instanceof Error) {
            throw (Error) throwable;
        }
        if (throwable instanceof RuntimeException) {
            return (RuntimeException) throwable;
        }
        return new WrapRuntimeException(throwable);
    }

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
        throw wrap(throwable);
    }

    private ThrowableUtil() {
    }
}