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
package org.slim3.commons.exception;

/**
 * This exception is thrown when an exception is encountered while converting.
 * 
 * @author higa
 * @since 3.0
 */
public class ConverterRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    private String propertyName;

    private Object value;

    /**
     * Constructor.
     * 
     * @param propertyName
     *            the property name
     * @param value
     *            the value
     * @param cause
     *            the cause
     */
    public ConverterRuntimeException(String propertyName, Object value,
            Throwable cause) {
        super(cause, "S3Commons-E0014", propertyName, value,
                convertCauseMessage(cause));
        this.propertyName = propertyName;
        this.value = value;
    }

    /**
     * Returns the property name.
     * 
     * @return the property name
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Returns the value.
     * 
     * @return the value
     */
    public Object getValue() {
        return value;
    }
}