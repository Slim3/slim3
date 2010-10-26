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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slim3.controller.ControllerConstants;
import org.slim3.util.ArrayMap;
import org.slim3.util.RequestMap;

/**
 * A class to control validation process. The error messages of validation are
 * stored in application[_locale].properties.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public class Validators {

    /**
     * The registered validators.
     */
    protected ArrayMap<String, Validator[]> validatorsMap =
        new ArrayMap<String, Validator[]>();

    /**
     * The parameters.
     */
    protected Map<String, Object> parameters;

    /**
     * The error messages.
     */
    protected Errors errors;

    /**
     * Constructor.
     * 
     * @param request
     *            the request
     */
    public Validators(HttpServletRequest request) {
        this(new RequestMap(request));
    }

    /**
     * Constructor.
     * 
     * @param parameters
     *            the parameters
     * @throws NullPointerException
     *             if the parameters parameter is null
     * @throws IllegalStateException
     *             if the errors is not found in parameters
     */
    public Validators(Map<String, Object> parameters)
            throws NullPointerException, IllegalStateException {
        if (parameters == null) {
            throw new NullPointerException(
                "The parameters parameter must not be null.");
        }
        this.parameters = parameters;
        errors = (Errors) parameters.get(ControllerConstants.ERRORS_KEY);
        if (errors == null) {
            throw new IllegalStateException(
                "The errors is not found in parameters.");
        }
    }

    /**
     * Adds the validators.
     * 
     * @param name
     *            the parameter name
     * @param validators
     *            the validators
     * @return this instance
     * @throws NullPointerException
     *             if the name parameter is null
     */
    public Validators add(CharSequence name, Validator... validators)
            throws NullPointerException {
        if (name == null) {
            throw new NullPointerException(
                "The name parameter must not be null.");
        }
        validatorsMap.put(name.toString(), validators);
        return this;
    }

    /**
     * Validates input values.
     * 
     * @return whether input values are valid. Returns true if input values are
     *         valid.
     */
    public boolean validate() {
        boolean valid = true;
        for (int i = 0; i < validatorsMap.size(); i++) {
            String name = validatorsMap.getKey(i);
            for (Validator v : validatorsMap.get(i)) {
                String message = v.validate(parameters, name);
                if (message != null) {
                    valid = false;
                    errors.put(name, message);
                    break;
                }
            }
        }
        return valid;
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
     * Returns {@link RequiredValidator}. The key of error message is
     * "validator.required".
     * 
     * @return {@link RequiredValidator}
     */
    public RequiredValidator required() {
        return RequiredValidator.INSTANCE;
    }

    /**
     * Returns {@link RequiredValidator}.
     * 
     * @param message
     *            the error message
     * @return {@link RequiredValidator}
     */
    public RequiredValidator required(String message) {
        return new RequiredValidator(message);
    }

    /**
     * Returns {@link ByteTypeValidator}. The key of error message is
     * "validator.byteType".
     * 
     * @return {@link ByteTypeValidator}
     */
    public ByteTypeValidator byteType() {
        return ByteTypeValidator.INSTANCE;
    }

    /**
     * Returns {@link ByteTypeValidator}.
     * 
     * @param message
     *            the error message
     * @return {@link ByteTypeValidator}
     */
    public ByteTypeValidator byteType(String message) {
        return new ByteTypeValidator(message);
    }

    /**
     * Returns {@link ShortTypeValidator}. The key of error message is
     * "validator.shortType".
     * 
     * @return {@link ShortTypeValidator}
     */
    public ShortTypeValidator shortType() {
        return ShortTypeValidator.INSTANCE;
    }

    /**
     * Returns {@link ShortTypeValidator}.
     * 
     * @param message
     *            the error message
     * @return {@link ShortTypeValidator}
     */
    public ShortTypeValidator shortType(String message) {
        return new ShortTypeValidator(message);
    }

    /**
     * Returns {@link IntegerTypeValidator}. The key of error message is
     * "validator.integerType".
     * 
     * @return {@link IntegerTypeValidator}
     */
    public IntegerTypeValidator integerType() {
        return IntegerTypeValidator.INSTANCE;
    }

    /**
     * Returns {@link IntegerTypeValidator}.
     * 
     * @param message
     *            the error message
     * @return {@link IntegerTypeValidator}
     */
    public IntegerTypeValidator integerType(String message) {
        return new IntegerTypeValidator(message);
    }

    /**
     * Returns {@link LongTypeValidator}. The key of error message is
     * "validator.longType".
     * 
     * @return {@link LongTypeValidator}
     */
    public LongTypeValidator longType() {
        return LongTypeValidator.INSTANCE;
    }

    /**
     * Returns {@link LongTypeValidator}.
     * 
     * @param message
     *            the error message
     * @return {@link LongTypeValidator}
     */
    public LongTypeValidator longType(String message) {
        return new LongTypeValidator(message);
    }

    /**
     * Returns {@link FloatTypeValidator}. The key of error message is
     * "validator.floatType".
     * 
     * @return {@link FloatTypeValidator}
     */
    public FloatTypeValidator floatType() {
        return FloatTypeValidator.INSTANCE;
    }

    /**
     * Returns {@link FloatTypeValidator}.
     * 
     * @param message
     *            the error message
     * @return {@link FloatTypeValidator}
     */
    public FloatTypeValidator floatType(String message) {
        return new FloatTypeValidator(message);
    }

    /**
     * Returns {@link DoubleTypeValidator}. The key of error message is
     * "validator.doubleType".
     * 
     * @return {@link DoubleTypeValidator}
     */
    public DoubleTypeValidator doubleType() {
        return DoubleTypeValidator.INSTANCE;
    }

    /**
     * Returns {@link DoubleTypeValidator}.
     * 
     * @param message
     *            the error message
     * @return {@link DoubleTypeValidator}
     */
    public DoubleTypeValidator doubleType(String message) {
        return new DoubleTypeValidator(message);
    }

    /**
     * Returns {@link NumberTypeValidator}. The key of error message is
     * "validator.numberType".
     * 
     * @param pattern
     *            the pattern of {@link DecimalFormat}
     * 
     * @return {@link NumberTypeValidator}
     */
    public NumberTypeValidator numberType(String pattern) {
        return new NumberTypeValidator(pattern);
    }

    /**
     * Returns {@link NumberTypeValidator}.
     * 
     * @param pattern
     *            the pattern of {@link DecimalFormat}
     * @param message
     *            the error message
     * 
     * @return {@link NumberTypeValidator}
     */
    public NumberTypeValidator numberType(String pattern, String message) {
        return new NumberTypeValidator(pattern, message);
    }

    /**
     * Returns {@link DateTypeValidator}. The key of error message is
     * "validator.dateType".
     * 
     * @param pattern
     *            the pattern of {@link SimpleDateFormat}
     * 
     * @return {@link DateTypeValidator}
     */
    public DateTypeValidator dateType(String pattern) {
        return new DateTypeValidator(pattern);
    }

    /**
     * Returns {@link DateTypeValidator}.
     * 
     * @param pattern
     *            the pattern of {@link SimpleDateFormat}
     * @param message
     *            the error message
     * @return {@link DateTypeValidator}
     */
    public DateTypeValidator dateType(String pattern, String message) {
        return new DateTypeValidator(pattern, message);
    }

    /**
     * Returns {@link MinlengthValidator}. The key of error message is
     * "validator.minlength".
     * 
     * @param minlength
     *            the minimum length
     * 
     * @return {@link MinlengthValidator}
     */
    public MinlengthValidator minlength(int minlength) {
        return new MinlengthValidator(minlength);
    }

    /**
     * Returns {@link MinlengthValidator}.
     * 
     * @param minlength
     *            the minimum length
     * @param message
     *            the error message
     * @return {@link MinlengthValidator}
     */
    public MinlengthValidator minlength(int minlength, String message) {
        return new MinlengthValidator(minlength, message);
    }

    /**
     * Returns {@link MaxlengthValidator}. The key of error message is
     * "validator.maxlength".
     * 
     * @param maxlength
     *            the maximum length
     * 
     * @return {@link MaxlengthValidator}
     */
    public MaxlengthValidator maxlength(int maxlength) {
        return new MaxlengthValidator(maxlength);
    }

    /**
     * Returns {@link MaxlengthValidator}.
     * 
     * @param maxlength
     *            the maximum length
     * @param message
     *            the error message
     * @return {@link MaxlengthValidator}
     */
    public MaxlengthValidator maxlength(int maxlength, String message) {
        return new MaxlengthValidator(maxlength, message);
    }

    /**
     * Returns {@link LongRangeValidator}. The key of error message is
     * "validator.range".
     * 
     * @param minimum
     *            the minimum value
     * @param maximum
     *            the maximum value
     * @return {@link LongRangeValidator}
     */
    public LongRangeValidator longRange(long minimum, long maximum) {
        return new LongRangeValidator(minimum, maximum);
    }

    /**
     * Returns {@link LongRangeValidator}.
     * 
     * @param minimum
     *            the minimum value
     * @param maximum
     *            the maximum value
     * @param message
     *            the error message
     * @return {@link LongRangeValidator}
     */
    public LongRangeValidator longRange(long minimum, long maximum,
            String message) {
        return new LongRangeValidator(minimum, maximum, message);
    }

    /**
     * Returns {@link DoubleRangeValidator}. The key of error message is
     * "validator.range".
     * 
     * @param minimum
     *            the minimum value
     * @param maximum
     *            the maximum value
     * @return {@link DoubleRangeValidator}
     */
    public DoubleRangeValidator doubleRange(double minimum, double maximum) {
        return new DoubleRangeValidator(minimum, maximum);
    }

    /**
     * Returns {@link DoubleRangeValidator}.
     * 
     * @param minimum
     *            the minimum value
     * @param maximum
     *            the maximum value
     * @param message
     *            the error message
     * @return {@link DoubleRangeValidator}
     */
    public DoubleRangeValidator doubleRange(double minimum, double maximum,
            String message) {
        return new DoubleRangeValidator(minimum, maximum, message);
    }

    /**
     * Returns {@link RegexpValidator}. The key of error message is
     * "validator.regexp".
     * 
     * @param pattern
     *            the pattern for regular expression
     * @return {@link RegexpValidator}
     * @throws NullPointerException
     *             if the pattern parameter is null
     */
    public RegexpValidator regexp(String pattern) throws NullPointerException {
        return new RegexpValidator(pattern);
    }

    /**
     * Returns {@link RegexpValidator}.
     * 
     * @param pattern
     *            the pattern for regular expression
     * @param message
     *            the error message
     * @return {@link RegexpValidator}
     * @throws NullPointerException
     *             if the pattern parameter is null
     */
    public RegexpValidator regexp(String pattern, String message)
            throws NullPointerException {
        return new RegexpValidator(pattern, message);
    }
}