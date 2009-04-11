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
package org.slim3.commons.bean;

import java.util.concurrent.ConcurrentHashMap;

import org.slim3.commons.cleaner.Cleanable;
import org.slim3.commons.cleaner.Cleaner;

/**
 * A utility class for bean.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public final class BeanUtil {

    private static ConcurrentHashMap<String, BeanDesc> beanDescCache = new ConcurrentHashMap<String, BeanDesc>(
            200);

    private static volatile boolean initialized = false;

    static {
        initialize();
    }

    private BeanUtil() {
    }

    private static void initialize() {
        Cleaner.add(new Cleanable() {
            public void clean() {
                beanDescCache.clear();
                initialized = false;
            }
        });
        initialized = true;
    }

    /**
     * Returns the bean descriptor.
     * 
     * @param beanClass
     *            the bean class
     * @return the bean descriptor
     * @throws NullPointerException
     *             if the beanClass parameter is null
     * 
     */
    public static BeanDesc getBeanDesc(Class<?> beanClass)
            throws NullPointerException {
        if (beanClass == null) {
            throw new NullPointerException("The beanClass parameter is null.");
        }
        if (!initialized) {
            initialize();
        }

        BeanDesc beanDesc = beanDescCache.get(beanClass.getName());
        if (beanDesc != null) {
            return beanDesc;
        }
        beanDesc = BeanDesc.create(beanClass);
        beanDescCache.put(beanClass.getName(), beanDesc);
        return beanDesc;
    }

    /**
     * Creates a {@link Copy} instance.
     * 
     * @param src
     *            the source
     * @param dest
     *            the destination
     * @return a {@link Copy} instance
     */
    public static Copy copy(Object src, Object dest) {
        return new Copy(src, dest);
    }

    /**
     * Creates a {@link CreateAndCopy} instance.
     * 
     * @param <T>
     *            the destination type
     * @param destClass
     *            the destination class
     * @param src
     *            the source
     * @return a {@link CreateAndCopy} instance
     */
    public static <T> CreateAndCopy<T> createAndCopy(Class<T> destClass,
            Object src) {
        return new CreateAndCopy<T>(destClass, src);
    }
}