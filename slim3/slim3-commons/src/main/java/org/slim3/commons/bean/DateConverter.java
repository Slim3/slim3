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

import java.text.DecimalFormat;
import java.util.Date;

import org.slim3.commons.exception.CastRuntimeException;
import org.slim3.commons.util.DateUtil;
import org.slim3.commons.util.StringUtil;

/**
 * The converter for date.
 * 
 * @author higa
 * @since 3.0
 */
public class DateConverter implements Converter {

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
    public DateConverter(String pattern) throws NullPointerException {
        if (StringUtil.isEmpty(pattern)) {
            throw new NullPointerException("The pattern parameter is null.");
        }
        this.pattern = pattern;
    }

    public Object getAsObject(String value) {
        return DateUtil.toDate(value, pattern);
    }

    public String getAsString(Object value) {
        if (value == null) {
            return null;
        }
        if (!(value instanceof Date)) {
            throw new CastRuntimeException(value.getClass(), Date.class);
        }
        return DateUtil.toString((Date) value, pattern);
    }

    public boolean isTarget(Class<?> clazz) {
        return clazz == Date.class;
    }
}