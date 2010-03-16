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

import java.lang.reflect.Array;

/**
 * A utility for {@link Array}.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public final class ArrayUtil {

    /**
     * Adds the object to the array.
     * 
     * @param <T>
     *            the type
     * @param array
     *            the array
     * @param obj
     *            the object
     * @return the added array
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] add(T[] array, T obj) {
        if (array == null && obj == null) {
            return null;
        }
        Class<?> clazz =
            obj != null ? obj.getClass() : array.getClass().getComponentType();
        int length = array != null ? array.length : 0;
        T[] newArray = (T[]) Array.newInstance(clazz, length + 1);
        if (array != null) {
            System.arraycopy(array, 0, newArray, 0, length);
        }
        newArray[length] = obj;
        return newArray;
    }

    private ArrayUtil() {
    }
}