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
 * A validator for an integer value.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class IntegerTypeValidator extends AbstractValidator {

    /**
     * The instance.
     */
    public static IntegerTypeValidator INSTANCE = new IntegerTypeValidator();

    /**
     * Constructor.
     */
    public IntegerTypeValidator() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param message
     *            the error message
     */
    public IntegerTypeValidator(String message) {
        super(message);
    }

    public String validate(Map<String, Object> parameters, String name) {
        Object value = parameters.get(name);
        if (value == null || "".equals(value)) {
            return null;
        }
        try {
            String s = (String) value;
            Integer.valueOf(s);
            return null;
        } catch (Throwable ignore) {
            if (message != null) {
                return message;
            }
            return ApplicationMessage.get(getMessageKey(), getLabel(name));
        }
    }

    @Override
    protected String getMessageKey() {
        return "validator.integerType";
    }
}