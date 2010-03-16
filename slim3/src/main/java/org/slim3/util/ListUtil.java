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
 * A utility for {@link List}.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public final class ListUtil {

    /**
     * Splits the list.
     * 
     * @param <T>
     *            the type of list element
     * @param list
     *            the list
     * @param size
     *            the piece size
     * @return the split lists.
     * @throws NullPointerException
     *             if the list parameter is null
     * @throws IllegalArgumentException
     *             if the size parameter is less than 1
     */
    public static <T> List<List<T>> split(List<T> list, int size)
            throws NullPointerException, IllegalArgumentException {
        if (list == null) {
            throw new NullPointerException("The list parameter is null.");
        }
        if (size <= 0) {
            throw new IllegalArgumentException(
                "The size parameter must be more than 0.");
        }
        int num = list.size() / size;
        int mod = list.size() % size;
        List<List<T>> ret = new ArrayList<List<T>>(mod > 0 ? num + 1 : num);
        for (int i = 0; i < num; i++) {
            ret.add(list.subList(i * size, (i + 1) * size));
        }
        if (mod > 0) {
            ret.add(list.subList(num * size, list.size()));
        }
        return ret;
    }

    private ListUtil() {
    }
}