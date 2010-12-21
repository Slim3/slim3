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

import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * A utility class for {@link Future}.
 * 
 * @author higa
 * @version 1.0.6
 */
public final class FutureUtil {

    /**
     * Gets a value from the {@link Future}.
     * 
     * @param <T>
     *            the value type
     * 
     * @param future
     *            the future
     * @return a value
     * @throws Exception
     *             if an exception occurred
     */
    public static <T> T get(Future<T> future) throws Exception {
        if (future == null) {
            throw new NullPointerException(
                "The future parameter must not be null.");
        }
        try {
            return future.get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else if (cause instanceof Error) {
                throw (Error) cause;
            } else if (cause instanceof Exception) {
                throw (Exception) cause;
            }
            throw new UndeclaredThrowableException(cause);
        }
    }

    /**
     * Gets a value from the {@link Future} without throwing an exception.
     * 
     * @param <T>
     *            the value type
     * 
     * @param future
     *            the future
     * @return a value
     */
    public static <T> T getQuietly(Future<T> future) {
        if (future == null) {
            throw new NullPointerException(
                "The future parameter must not be null.");
        }
        try {
            return future.get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new WrapRuntimeException(cause);
        } catch (InterruptedException e) {
            throw new WrapRuntimeException("Unexpected failure", e);
        }
    }

    private FutureUtil() {
    }
}