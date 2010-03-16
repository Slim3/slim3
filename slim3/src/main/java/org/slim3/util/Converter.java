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
 * An interface that converts string and object.
 * 
 * @author higa
 * @param <T>
 *            the type
 * @since 1.0.0
 * 
 */
public interface Converter<T> {

    /**
     * Returns the value as string.
     * 
     * @param value
     *            the value
     * @return the converted value
     */
    String getAsString(Object value);

    /**
     * Returns the value as object.
     * 
     * @param value
     *            the value
     * @return the converted value
     */
    T getAsObject(String value);

    /**
     * Determines if the class is target.
     * 
     * @param clazz
     *            the class
     * @return whether the class is target
     */
    boolean isTarget(Class<?> clazz);
}