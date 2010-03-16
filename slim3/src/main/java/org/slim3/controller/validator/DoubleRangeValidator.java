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
package org.slim3.controller.validator;

import java.util.Map;

import org.slim3.util.ApplicationMessage;

/**
 * A validator for double range check.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class DoubleRangeValidator extends AbstractValidator {

    /**
     * The minimum.
     */
    protected double minimum;

    /**
     * The maximum.
     */
    protected double maximum;

    /**
     * Constructor.
     * 
     * @param minimum
     *            the minimum value
     * @param maximum
     *            the maximum value
     */
    public DoubleRangeValidator(double minimum, double maximum) {
        this(minimum, maximum, null);
    }

    /**
     * Constructor.
     * 
     * @param minimum
     *            the minimum value
     * @param maximum
     *            the maximum value
     * @param message
     *            the error message
     */
    public DoubleRangeValidator(double minimum, double maximum, String message) {
        super(message);
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public String validate(Map<String, Object> parameters, String name) {
        Object value = parameters.get(name);
        if (value == null || "".equals(value)) {
            return null;
        }
        try {
            String s = (String) value;
            double doubleValue = Double.valueOf(s);
            if (minimum <= doubleValue && doubleValue <= maximum) {
                return null;
            }
        } catch (Throwable ignore) {
        }
        if (message != null) {
            return message;
        }
        return ApplicationMessage.get(
            getMessageKey(),
            getLabel(name),
            minimum,
            maximum);
    }

    @Override
    protected String getMessageKey() {
        return "validator.range";
    }
}