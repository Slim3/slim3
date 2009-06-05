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
package org.slim3.controller.validator;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.slim3.util.ApplicationMessage;

/**
 * A validator for a date value.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class DateTypeValidator extends AbstractValidator {

    /**
     * The pattern of {@link SimpleDateFormat}.
     */
    protected String pattern;

    /**
     * Constructor.
     * 
     * @param pattern
     *            the pattern of {@link SimpleDateFormat}
     * @throws NullPointerException
     *             if the pattern parameter is null
     */
    public DateTypeValidator(String pattern) throws NullPointerException {
        if (pattern == null) {
            throw new NullPointerException("The pattern parameter is null.");
        }
        this.pattern = pattern;
    }

    @Override
    public String validate(HttpServletRequest request, String name) {
        Object value = request.getAttribute(name);
        if (value == null || "".equals(value)) {
            return null;
        }
        try {
            String s = (String) value;
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            format.setLenient(false);
            format.parse(s);
            return null;
        } catch (Throwable ignore) {
            return ApplicationMessage.get(
                "validator.dateType",
                getLabel(name),
                pattern);
        }
    }
}