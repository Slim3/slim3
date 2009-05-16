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

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A utility class for bean.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public final class BeanUtil {

    private static final CopyOptions DEFAULT_OPTIONS = new CopyOptions();

    private static ConcurrentHashMap<String, BeanDesc> beanDescCache =
        new ConcurrentHashMap<String, BeanDesc>(200);

    private static volatile boolean initialized = false;

    static {
        initialize();
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
     * Copies a bean to another one.
     * 
     * @param src
     *            the source
     * @param dest
     *            the destination
     */
    public static void copy(Object src, Object dest) {
        copy(src, dest, DEFAULT_OPTIONS);
    }

    /**
     * Copies a bean to another one.
     * 
     * @param src
     *            the source
     * @param dest
     *            the destination
     * @param options
     *            the copy options
     * @throws NullPointerException
     *             if the src parameter is null or if the dest parameter is null
     *             or if the options parameter is null
     */
    public static void copy(Object src, Object dest, CopyOptions options)
            throws NullPointerException {
        if (src == null) {
            throw new NullPointerException("The src parameter is null.");
        }
        if (dest == null) {
            throw new NullPointerException("The dest parameter is null.");
        }
        if (options == null) {
            throw new NullPointerException("The options parameter is null.");
        }
        BeanDesc srcBeanDesc = getBeanDesc(src.getClass());
        BeanDesc destBeanDesc = getBeanDesc(dest.getClass());
        int size = srcBeanDesc.getPropertyDescSize();
        for (int i = 0; i < size; i++) {
            PropertyDesc srcPropertyDesc = srcBeanDesc.getPropertyDesc(i);
            String propertyName = srcPropertyDesc.getName();
            if (!srcPropertyDesc.isReadable()
                || !options.isTargetProperty(propertyName)) {
                continue;
            }
            PropertyDesc destPropertyDesc =
                destBeanDesc.getPropertyDesc(propertyName);
            if (destPropertyDesc == null) {
                continue;
            }
            if (!destPropertyDesc.isWritable()) {
                continue;
            }
            Object value = srcPropertyDesc.getValue(src);
            if (!options.isTargetValue(value)) {
                continue;
            }
            value =
                options.convertValue(value, propertyName, destPropertyDesc
                    .getPropertyClass());
            destPropertyDesc.setValue(dest, value);
        }
    }

    /**
     * Copies a bean to a map.
     * 
     * @param src
     *            the source
     * @param dest
     *            the destination
     */
    @SuppressWarnings("unchecked")
    public static void copy(Object src, Map dest) {
        copy(src, dest, DEFAULT_OPTIONS);
    }

    /**
     * Copies a bean to a map.
     * 
     * @param src
     *            the source
     * @param dest
     *            the destination
     * @param options
     *            the copy options
     * @throws NullPointerException
     *             if the src parameter is null or if the dest parameter is null
     *             or if the options parameter is null
     */
    @SuppressWarnings("unchecked")
    public static void copy(Object src, Map dest, CopyOptions options)
            throws NullPointerException {
        if (src == null) {
            throw new NullPointerException("The src parameter is null.");
        }
        if (dest == null) {
            throw new NullPointerException("The dest parameter is null.");
        }
        if (options == null) {
            throw new NullPointerException("The options parameter is null.");
        }
        BeanDesc srcBeanDesc = getBeanDesc(src.getClass());
        int size = srcBeanDesc.getPropertyDescSize();
        for (int i = 0; i < size; i++) {
            PropertyDesc srcPropertyDesc = srcBeanDesc.getPropertyDesc(i);
            String propertyName = srcPropertyDesc.getName();
            if (!srcPropertyDesc.isReadable()
                || !options.isTargetProperty(propertyName)) {
                continue;
            }
            Object value = srcPropertyDesc.getValue(src);
            if (!options.isTargetValue(value)) {
                continue;
            }
            value = options.convertValue(value, propertyName, null);
            dest.put(propertyName, value);
        }
    }

    /**
     * Copies a map to a bean.
     * 
     * @param src
     *            the source
     * @param dest
     *            the destination
     */
    @SuppressWarnings("unchecked")
    public static void copy(Map src, Object dest) {
        copy(src, dest, DEFAULT_OPTIONS);
    }

    /**
     * Copies a map to a bean.
     * 
     * @param src
     *            the source
     * @param dest
     *            the destination
     * @param options
     *            the copy options
     * @throws NullPointerException
     *             if the src parameter is null or if the dest parameter is null
     *             or if the options parameter is null
     */
    @SuppressWarnings("unchecked")
    public static void copy(Map src, Object dest, CopyOptions options)
            throws NullPointerException {
        if (src == null) {
            throw new NullPointerException("The src parameter is null.");
        }
        if (dest == null) {
            throw new NullPointerException("The dest parameter is null.");
        }
        if (options == null) {
            throw new NullPointerException("The options parameter is null.");
        }
        BeanDesc destBeanDesc = getBeanDesc(dest.getClass());
        for (Iterator<String> i = src.keySet().iterator(); i.hasNext();) {
            String propertyName = i.next();
            if (!options.isTargetProperty(propertyName)) {
                continue;
            }
            PropertyDesc destPropertyDesc =
                destBeanDesc.getPropertyDesc(propertyName);
            if (destPropertyDesc == null) {
                continue;
            }
            if (!destPropertyDesc.isWritable()) {
                continue;
            }
            Object value = src.get(propertyName);
            if (!options.isTargetValue(value)) {
                continue;
            }
            value =
                options.convertValue(value, propertyName, destPropertyDesc
                    .getPropertyClass());
            destPropertyDesc.setValue(dest, value);
        }
    }

    /**
     * Copies a map to another one.
     * 
     * @param src
     *            the source
     * @param dest
     *            the destination
     */
    @SuppressWarnings("unchecked")
    public static void copy(Map src, Map dest) {
        copy(src, dest, DEFAULT_OPTIONS);
    }

    /**
     * Copies a map to another one.
     * 
     * @param src
     *            the source
     * @param dest
     *            the destination
     * @param options
     *            the copy options
     * @throws NullPointerException
     *             if the src parameter is null or if the dest parameter is null
     *             or if the options parameter is null
     */
    @SuppressWarnings("unchecked")
    public static void copy(Map src, Map dest, CopyOptions options)
            throws NullPointerException {
        if (src == null) {
            throw new NullPointerException("The src parameter is null.");
        }
        if (dest == null) {
            throw new NullPointerException("The dest parameter is null.");
        }
        if (options == null) {
            throw new NullPointerException("The options parameter is null.");
        }
        for (Iterator<String> i = src.keySet().iterator(); i.hasNext();) {
            String propertyName = i.next();
            if (!options.isTargetProperty(propertyName)) {
                continue;
            }
            Object value = src.get(propertyName);
            if (!options.isTargetValue(value)) {
                continue;
            }
            value = options.convertValue(value, propertyName, null);
            dest.put(propertyName, value);
        }
    }

    private BeanUtil() {
    }
}