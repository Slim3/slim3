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
package org.slim3.struts.validator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.util.ValidatorUtils;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.FieldChecks;
import org.apache.struts.validator.Resources;

/**
 * This class contains the added validations.
 * 
 * @author Satoshi Kimura
 * @author higa
 * @since 3.0
 * @see S3GenericValidator
 */
public class S3FieldChecks extends FieldChecks {

    private static final long serialVersionUID = 1L;

    /**
     * Validates if the byte length is less than or equal to the maximum length.
     * 
     * @param bean
     *            JavaBeans
     * @param validatorAction
     *            the validator action
     * @param field
     *            the field
     * @param errors
     *            the errors
     * @param validator
     *            the validator
     * @param request
     *            the request
     * @return the validated result
     */
    public static boolean validateMaxByteLength(Object bean,
            ValidatorAction validatorAction, Field field,
            ActionMessages errors, Validator validator,
            HttpServletRequest request) {
        String value = getValueAsString(bean, field);
        if (!GenericValidator.isBlankOrNull(value)) {
            try {
                int max = Integer.parseInt(field.getVarValue("maxbytelength"));
                String charset = field.getVarValue("charset");
                if (!S3GenericValidator.maxByteLength(value, max, charset)) {
                    errors.add(field.getKey(), Resources.getActionMessage(
                            validator, request, validatorAction, field));
                    return false;
                }
            } catch (Exception e) {
                errors.add(field.getKey(), Resources.getActionMessage(
                        validator, request, validatorAction, field));
                return false;
            }
        }
        return true;
    }

    /**
     * Validates if the byte length is greater than or equal to the minimum
     * length.
     * 
     * @param bean
     *            JavaBeans
     * @param validatorAction
     *            the validator action
     * @param field
     *            the field
     * @param errors
     *            the errors
     * @param validator
     *            the validator
     * @param request
     *            the request
     * @return the validated result
     */
    public static boolean validateMinByteLength(Object bean,
            ValidatorAction validatorAction, Field field,
            ActionMessages errors, Validator validator,
            HttpServletRequest request) {
        String value = getValueAsString(bean, field);
        if (!GenericValidator.isBlankOrNull(value)) {
            try {
                int min = Integer.parseInt(field.getVarValue("minbytelength"));
                String charset = field.getVarValue("charset");
                if (!S3GenericValidator.minByteLength(value, min, charset)) {
                    errors.add(field.getKey(), Resources.getActionMessage(
                            validator, request, validatorAction, field));
                    return false;
                }
            } catch (Exception e) {
                errors.add(field.getKey(), Resources.getActionMessage(
                        validator, request, validatorAction, field));
                return false;
            }
        }
        return true;
    }

    /**
     * Validates if a fields value is within a range.
     * 
     * @param bean
     *            JavaBeans
     * @param validatorAction
     *            the validator action
     * @param field
     *            the field
     * @param errors
     *            the errors
     * @param validator
     *            the validator
     * @param request
     *            the request
     * @return the validated result
     */
    public static boolean validateLongRange(Object bean,
            ValidatorAction validatorAction, Field field,
            ActionMessages errors, Validator validator,
            HttpServletRequest request) {
        String value = getValueAsString(bean, field);
        if (!GenericValidator.isBlankOrNull(value)) {
            try {
                long longValue = Long.parseLong(value);
                long min = Long.parseLong(field.getVarValue("min"));
                long max = Long.parseLong(field.getVarValue("max"));
                if (!GenericValidator.isInRange(longValue, min, max)) {
                    errors.add(field.getKey(), Resources.getActionMessage(
                            validator, request, validatorAction, field));
                    return false;
                }
            } catch (Exception e) {
                errors.add(field.getKey(), Resources.getActionMessage(
                        validator, request, validatorAction, field));
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the value as string.
     * 
     * @param bean
     *            JavaBeans
     * @param field
     *            the field
     * @return the result
     */
    protected static String getValueAsString(Object bean, Field field) {
        if (isString(bean)) {
            return (String) bean;
        }
        return ValidatorUtils.getValueAsString(bean, field.getProperty());
    }
}
