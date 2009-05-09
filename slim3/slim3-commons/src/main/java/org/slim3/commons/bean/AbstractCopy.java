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
package org.slim3.commons.bean;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slim3.commons.util.DateUtil;

/**
 * An abstract class to copy an object to an another object.
 * 
 * @author higa
 * @since 3.0
 * @param <S>
 *            the sub type.
 * 
 */
public abstract class AbstractCopy<S extends AbstractCopy<S>> {

    /**
     * The empty strings.
     */
    protected static final String[] EMPTY_STRINGS = new String[0];

    /**
     * The default converter for {@link java.util.Date}.
     */
    protected static final Converter DEFAULT_DATE_CONVERTER =
        new DateConverter(DateUtil.ISO_DATE_PATTERN);

    /**
     * The included property names.
     */
    protected String[] includedPropertyNames = EMPTY_STRINGS;

    /**
     * The excluded property names.
     */
    protected String[] excludedPropertyNames = EMPTY_STRINGS;

    /**
     * Whether null is copied.
     */
    protected boolean copyNull = false;

    /**
     * Whether emptyString is copied.
     */
    protected boolean copyEmptyString = false;

    /**
     * The converters that are bound to the specific property.
     */
    protected Map<String, Converter> converterMap =
        new HashMap<String, Converter>();

    /**
     * The converters that are not bound to any properties.
     */
    protected List<Converter> converters = new ArrayList<Converter>();

    /**
     * Specifies the included property names.
     * 
     * @param propertyNames
     *            the included property names
     * @return this instance
     */
    @SuppressWarnings("unchecked")
    public S include(String... propertyNames) {
        includedPropertyNames = propertyNames;
        return (S) this;
    }

    /**
     * Specifies the excluded property names.
     * 
     * @param propertyNames
     *            the excluded property names
     * @return this instance
     */
    @SuppressWarnings("unchecked")
    public S exclude(String... propertyNames) {
        excludedPropertyNames = propertyNames;
        return (S) this;
    }

    /**
     * Specifies whether null is copied.
     * 
     * @return this instance
     */
    @SuppressWarnings("unchecked")
    public S copyNull() {
        this.copyNull = true;
        return (S) this;
    }

    /**
     * Specifies whether empty string is copied.
     * 
     * @return this instance
     */
    @SuppressWarnings("unchecked")
    public S copyEmptyString() {
        this.copyEmptyString = true;
        return (S) this;
    }

    /**
     * Specifies the converter.
     * 
     * @param converter
     *            the converter
     * @param propertyNames
     *            the property names
     * @return this instance
     */
    @SuppressWarnings("unchecked")
    public S converter(Converter converter, String... propertyNames) {
        if (propertyNames.length == 0) {
            converters.add(converter);
        } else {
            for (String name : propertyNames) {
                converterMap.put(name, converter);
            }
        }
        return (S) this;
    }

    /**
     * Specifies the converter for {@link Date}.
     * 
     * @param pattern
     *            the pattern for {@link SimpleDateFormat}
     * @param propertyNames
     *            the property names
     * @return this instance
     */
    public S dateConverter(String pattern, String... propertyNames) {
        return converter(new DateConverter(pattern), propertyNames);
    }

    /**
     * Specifies the number converter.
     * 
     * @param pattern
     *            the pattern for {@link DecimalFormat}
     * @param propertyNames
     *            the property names
     * @return this instance
     */
    public S numberConverter(String pattern, String... propertyNames) {
        return converter(new NumberConverter(pattern), propertyNames);
    }

    /**
     * Determines if the property is target.
     * 
     * @param name
     *            the property name
     * @return whether the property is target
     */
    protected boolean isTargetProperty(String name) {
        if (includedPropertyNames.length > 0) {
            for (String s : includedPropertyNames) {
                if (s.equals(name)) {
                    for (String s2 : excludedPropertyNames) {
                        if (s2.equals(name)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }
        if (excludedPropertyNames.length > 0) {
            for (String s : excludedPropertyNames) {
                if (s.equals(name)) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    /**
     * Determines if the value is target.
     * 
     * @param value
     *            the value
     * @return whether the value is target
     */
    protected boolean isTargetValue(Object value) {
        if (value == null) {
            return copyNull;
        }
        if ("".equals(value)) {
            return copyEmptyString;
        }
        return true;
    }

    /**
     * Copies a bean to an another bean.
     * 
     * @param src
     *            the source bean
     * @param dest
     *            the destination bean
     */
    protected void copyBeanToBean(Object src, Object dest) {
        BeanDesc srcBeanDesc = BeanUtil.getBeanDesc(src.getClass());
        BeanDesc destBeanDesc = BeanUtil.getBeanDesc(dest.getClass());
        int size = srcBeanDesc.getPropertyDescSize();
        for (int i = 0; i < size; i++) {
            PropertyDesc srcPropertyDesc = srcBeanDesc.getPropertyDesc(i);
            String propertyName = srcPropertyDesc.getName();
            if (!srcPropertyDesc.isReadable()
                || !isTargetProperty(propertyName)) {
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
            if (!isTargetValue(value)) {
                continue;
            }
            value =
                convertValue(value, propertyName, destPropertyDesc
                    .getPropertyClass());
            destPropertyDesc.setValue(dest, value);
        }
    }

    /**
     * Copies a bean to an another map.
     * 
     * @param src
     *            the source bean
     * @param dest
     *            the destination map
     */
    protected void copyBeanToMap(Object src, Map<String, Object> dest) {
        BeanDesc beanDesc = BeanUtil.getBeanDesc(src.getClass());
        int size = beanDesc.getPropertyDescSize();
        for (int i = 0; i < size; i++) {
            PropertyDesc propertyDesc = beanDesc.getPropertyDesc(i);
            String propertyName = propertyDesc.getName();
            if (!propertyDesc.isReadable() || !isTargetProperty(propertyName)) {
                continue;
            }
            Object value = propertyDesc.getValue(src);
            if (!isTargetValue(value)) {
                continue;
            }
            value = convertValue(value, propertyName, null);
            dest.put(propertyName, value);
        }
    }

    /**
     * Copies a map to an another bean.
     * 
     * @param src
     *            the source map
     * @param dest
     *            the destination bean
     */
    protected void copyMapToBean(Map<String, Object> src, Object dest) {
        BeanDesc beanDesc = BeanUtil.getBeanDesc(dest.getClass());
        for (Iterator<String> i = src.keySet().iterator(); i.hasNext();) {
            String propertyName = i.next();
            if (!isTargetProperty(propertyName)) {
                continue;
            }
            PropertyDesc propertyDesc = beanDesc.getPropertyDesc(propertyName);
            if (propertyDesc == null) {
                continue;
            }
            if (!propertyDesc.isWritable()) {
                continue;
            }
            Object value = src.get(propertyName);
            if (!isTargetValue(value)) {
                continue;
            }
            value =
                convertValue(value, propertyName, propertyDesc
                    .getPropertyClass());
            propertyDesc.setValue(dest, value);
        }
    }

    /**
     * Copies a map to an another map.
     * 
     * @param src
     *            the source map
     * @param dest
     *            the destination map
     */
    protected void copyMapToMap(Map<String, Object> src,
            Map<String, Object> dest) {
        for (Iterator<String> i = src.keySet().iterator(); i.hasNext();) {
            String propertyName = i.next();
            if (!isTargetProperty(propertyName)) {
                continue;
            }
            Object value = src.get(propertyName);
            if (!isTargetValue(value)) {
                continue;
            }
            value = convertValue(value, propertyName, null);
            dest.put(propertyName, value);
        }
    }

    /**
     * Converts the value.
     * 
     * @param value
     *            the value
     * @param destPropertyName
     *            the destination property name
     * @param destPropertyClass
     *            the destination property class
     * @return the converted value
     * @throws IllegalArgumentException
     *             if an exception occurred while converting
     * 
     */
    protected Object convertValue(Object value, String destPropertyName,
            Class<?> destPropertyClass) throws IllegalArgumentException {
        if (value == null
            || value.getClass() == String.class
            && destPropertyClass == String.class
            || value.getClass() != String.class
            && (destPropertyClass != String.class && destPropertyClass != null)) {
            return value;
        }
        Converter converter = converterMap.get(destPropertyName);
        if (converter == null) {
            Class<?> targetClass =
                value.getClass() != String.class
                    ? value.getClass()
                    : destPropertyClass;
            if (targetClass == null) {
                return value;
            }
            for (Class<?> clazz = targetClass; clazz != null
                && clazz != Object.class; clazz = clazz.getSuperclass()) {
                converter = findConverter(clazz);
                if (converter != null) {
                    break;
                }
            }
            if (converter == null) {
                converter = findDefaultConverter(targetClass);
            }
            if (converter == null) {
                return value;
            }
        }
        try {
            if (value.getClass() == String.class) {
                return converter.getAsObject((String) value);
            }
            return converter.getAsString(value);
        } catch (Throwable cause) {
            throw new IllegalArgumentException("The value("
                + value
                + ") of the class("
                + value.getClass().getName()
                + ") can not be converted to the class("
                + destPropertyClass.getName()
                + "). Error message: "
                + cause.getMessage(), cause);
        }
    }

    /**
     * Finds the converter for the class.
     * 
     * @param clazz
     *            the class.
     * @return converter
     */
    protected Converter findConverter(Class<?> clazz) {
        for (Converter c : converters) {
            if (c.isTarget(clazz)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Finds the default converter.
     * 
     * @param clazz
     *            the converter.
     * @return the default converter.
     */
    protected Converter findDefaultConverter(Class<?> clazz) {
        if (java.util.Date.class.isAssignableFrom(clazz)) {
            return DEFAULT_DATE_CONVERTER;
        }
        return null;
    }
}