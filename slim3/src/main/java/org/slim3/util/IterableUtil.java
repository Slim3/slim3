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

import java.util.ArrayList;
import java.util.List;

/**
 * A utility for {@link Iterable}.
 * 
 * @author higa
 * @since 1.0.1
 * 
 */
public final class IterableUtil {

    /**
     * Splits the {@link Iterable}.
     * 
     * @param <T>
     *            the type of {@link Iterable} element
     * @param list
     *            the {@link Iterable}
     * @param size
     *            the piece size
     * @return the split {@link Iterable}s.
     * @throws NullPointerException
     *             if the list parameter is null
     * @throws IllegalArgumentException
     *             if the size parameter is less than 1
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> List<Iterable<T>> split(Iterable<T> list, int size)
            throws NullPointerException, IllegalArgumentException {
        if (list == null) {
            throw new NullPointerException("The list parameter is null.");
        }
        if (size <= 0) {
            throw new IllegalArgumentException(
                "The size parameter must be more than 0.");
        }
        if (list instanceof List<?>) {
            return ListUtil.split((List) list, size);
        }
        List<Iterable<T>> ret = new ArrayList<Iterable<T>>();
        List<T> l = new ArrayList<T>();
        for (T e : list) {
            if (l.size() == size) {
                ret.add(l);
                l = new ArrayList<T>();
            }
            l.add(e);
        }
        if (l.size() > 0) {
            ret.add(l);
        }
        return ret;
    }

    private IterableUtil() {
    }
}