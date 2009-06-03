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
package org.slim3.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.slim3.util.ArrayMap;

/**
 * A class to control validation process.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class Validators {

    /**
     * The map of validators.
     */
    protected ArrayMap<String, Validator[]> validatorsMap =
        new ArrayMap<String, Validator[]>();

    /**
     * The request.
     */
    protected HttpServletRequest request;

    /**
     * The error messages.
     */
    protected Errors errors = new Errors();

    /**
     * Constructor.
     * 
     * @param request
     *            the request
     * @throws NullPointerException
     *             if the request parameter is null
     */
    public Validators(HttpServletRequest request) throws NullPointerException {
        if (request == null) {
            throw new NullPointerException("The request parameter is null.");
        }
        this.request = request;
    }

    /**
     * Adds the validators.
     * 
     * @param name
     *            the name
     * @param validators
     *            the validators
     * @return this instance
     * @throws NullPointerException
     *             if the name parameter is null
     */
    public Validators add(String name, Validator... validators)
            throws NullPointerException {
        if (name == null) {
            throw new NullPointerException("The name parameter is null.");
        }
        validatorsMap.put(name, validators);
        return this;
    }

    /**
     * Validates input values.
     * 
     * @return whether input values are valid. Returns true if input values are
     *         valid.
     */
    public boolean validate() {
        for (int i = 0; i < validatorsMap.size(); i++) {
            String name = validatorsMap.getKey(i);
            for (Validator v : validatorsMap.get(i)) {
                String message = v.validate(request, name);
                if (message != null) {
                    errors.put(name, message);
                    break;
                }
            }
        }
        request.setAttribute(ControllerConstants.ERRORS_KEY, errors);
        return errors.isEmpty();
    }

    /**
     * Returns the error messages.
     * 
     * @return the error messages
     */
    public Errors getErrors() {
        return errors;
    }

    /**
     * Returns {@link RequiredValidator}.
     * 
     * @return {@link RequiredValidator}
     */
    public RequiredValidator required() {
        return RequiredValidator.INSTANCE;
    }

    /**
     * Returns {@link ByteTypeValidator}.
     * 
     * @return {@link ByteTypeValidator}
     */
    public ByteTypeValidator byteType() {
        return ByteTypeValidator.INSTANCE;
    }

    /**
     * Returns {@link ShortTypeValidator}.
     * 
     * @return {@link ShortTypeValidator}
     */
    public ShortTypeValidator shortType() {
        return ShortTypeValidator.INSTANCE;
    }

    /**
     * Returns {@link IntegerTypeValidator}.
     * 
     * @return {@link IntegerTypeValidator}
     */
    public IntegerTypeValidator integerType() {
        return IntegerTypeValidator.INSTANCE;
    }

    /**
     * Returns {@link LongTypeValidator}.
     * 
     * @return {@link LongTypeValidator}
     */
    public LongTypeValidator longType() {
        return LongTypeValidator.INSTANCE;
    }

    /**
     * Returns {@link FloatTypeValidator}.
     * 
     * @return {@link FloatTypeValidator}
     */
    public FloatTypeValidator floatType() {
        return FloatTypeValidator.INSTANCE;
    }

    /**
     * Returns {@link DoubleTypeValidator}.
     * 
     * @return {@link DoubleTypeValidator}
     */
    public DoubleTypeValidator doubleType() {
        return DoubleTypeValidator.INSTANCE;
    }

    /**
     * Returns {@link NumberTypeValidator}.
     * 
     * @param pattern
     *            the pattern of {@link DecimalFormat}
     * 
     * @return {@link NumberTypeValidator}
     * @see NumberTypeValidator#NumberTypeValidator(String)
     */
    public NumberTypeValidator numberType(String pattern) {
        return new NumberTypeValidator(pattern);
    }

    /**
     * Returns {@link DateTypeValidator}.
     * 
     * @param pattern
     *            the pattern of {@link SimpleDateFormat}
     * 
     * @return {@link DateTypeValidator}
     * @see DateTypeValidator#DateTypeValidator(String)
     */
    public DateTypeValidator dateType(String pattern) {
        return new DateTypeValidator(pattern);
    }
}