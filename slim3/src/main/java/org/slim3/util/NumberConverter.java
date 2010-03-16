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

import java.text.DecimalFormat;

/**
 * A converter for number.
 * 
 * @author higa
 * @since 1.0.0
 */
public class NumberConverter implements Converter<Number> {

    /**
     * The pattern for {@link DecimalFormat}.
     */
    protected String pattern;

    /**
     * Constructor.
     * 
     * @param pattern
     *            the pattern for {@link DecimalFormat}
     * @throws NullPointerException
     *             if the pattern parameter is null
     */
    public NumberConverter(String pattern) throws NullPointerException {
        if (StringUtil.isEmpty(pattern)) {
            throw new NullPointerException("The pattern parameter is null.");
        }
        this.pattern = pattern;
    }

    public Number getAsObject(String value) {
        return NumberUtil.toNumber(value, pattern);
    }

    public String getAsString(Object value) {
        if (value == null) {
            return null;
        }
        if (!(value instanceof Number)) {
            throw new IllegalArgumentException("The class("
                + value.getClass().getName()
                + ") can not be assigned to number.");
        }
        return NumberUtil.toString((Number) value, pattern);
    }

    public boolean isTarget(Class<?> clazz) {
        return Number.class.isAssignableFrom(clazz);
    }
}