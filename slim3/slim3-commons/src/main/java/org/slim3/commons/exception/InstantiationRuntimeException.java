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
package org.slim3.commons.exception;

/**
 * This exception is thrown when a {@link InstantiationException} is
 * encountered.
 * 
 * @author higa
 * @since 3.0
 */
public class InstantiationRuntimeException extends SRuntimeException {

    static final long serialVersionUID = 1L;

    private Class<?> targetClass;

    /**
     * Constructor.
     * 
     * @param targetClass
     *            the target class
     * @param cause
     *            the cause
     */
    public InstantiationRuntimeException(Class<?> targetClass,
            InstantiationException cause) {
        super(cause, "S3Commons-E0001", targetClass.getName());
        this.targetClass = targetClass;
    }

    /**
     * Returns the target class.
     * 
     * @return the target class
     */
    public Class<?> getTargetClass() {
        return targetClass;
    }
}