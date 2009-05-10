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

import java.util.Enumeration;
import java.util.Iterator;

/**
 * A class to use {@link Iterator} as {@link Enumeration}.
 * 
 * @author higa
 * @param <T>
 *            the type
 * @since 3.0
 * 
 */
public class IteratorEnumeration<T> implements Enumeration<T> {

    /**
     * The original iterator.
     */
    protected Iterator<T> iterator;

    /**
     * Constructor.
     * 
     * @param iterator
     *            the iterator
     * @throws NullPointerException
     *             if the iterator parameter is null
     */
    public IteratorEnumeration(Iterator<T> iterator)
            throws NullPointerException {
        if (iterator == null) {
            throw new NullPointerException("The iterator parameter is null.");
        }
        this.iterator = iterator;
    }

    public boolean hasMoreElements() {
        return iterator.hasNext();
    }

    public T nextElement() {
        return iterator.next();
    }
}