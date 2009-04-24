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
package org.slim3.mvc.controller;

import java.io.Serializable;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.slim3.commons.util.StringUtil;

/**
 * The meta data of the execute method.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class ExecuteMethodMeta implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String[] EMPTY_ROLE = new String[0];

    /**
     * The method of the action class.
     */
    protected Method method;

    /**
     * Whether this execute method is validated.
     */
    protected boolean validate = true;

    /**
     * The path to transit when a validation error is encountered.
     **/
    protected String input;

    /**
     * The roles.
     */
    protected String[] roles = EMPTY_ROLE;

    /**
     * Whether the action form is removed from session or request.
     */
    protected boolean removeActionForm = false;

    /**
     * The reset method.
     */
    protected Method resetMethod;

    /**
     * Constructor.
     * 
     * @param method
     *            the method
     * @throws NullPointerException
     *             if the method parameter is null
     */
    public ExecuteMethodMeta(Method method) throws NullPointerException {
        if (method == null) {
            throw new NullPointerException("The method parameter is null.");
        }
        this.method = method;
    }

    /**
     * Returns the method of the action class.
     * 
     * @return the method
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Determines if this execute method is validated.
     * 
     * @return the validate flag
     */
    public boolean isValidate() {
        return validate;
    }

    /**
     * Sets the validate flag.
     * 
     * @param validate
     *            the validate flag
     */
    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    /**
     * Returns the path to transit when a validation error is encountered.
     * 
     * @return the path to transit
     */
    public String getInput() {
        return input;
    }

    /**
     * Sets the path to transit when a validation error is encountered.
     * 
     * @param input
     *            the path to transit
     * @throws NullPointerException
     *             if the input parameter is null
     */
    public void setInput(String input) throws NullPointerException {
        if (input == null) {
            throw new NullPointerException("The input parameer is null.");
        }
        this.input = input;
    }

    /**
     * Returns the roles.
     * 
     * @return the roles
     */
    public String[] getRoles() {
        return roles;
    }

    /**
     * Set the roles.
     * 
     * @param roles
     *            the roles
     * @throws NullPointerException
     *             if the roles parameter is null
     */
    public void setRoles(String[] roles) throws NullPointerException {
        if (roles == null) {
            throw new NullPointerException("The roles parameter is null.");
        }
        this.roles = roles;
    }

    /**
     * Determines if the action form is removed from session or request.
     * 
     * @return the removeActionForm flag
     */
    public boolean isRemoveActionForm() {
        return removeActionForm;
    }

    /**
     * Sets the removeActionForm flag.
     * 
     * @param removeActionForm
     *            the removeActionForm flag
     */
    public void setRemoveActionForm(boolean removeActionForm) {
        this.removeActionForm = removeActionForm;
    }

    /**
     * Returns the reset method.
     * 
     * @return the reset method
     */
    public Method getResetMethod() {
        return resetMethod;
    }

    /**
     * Sets the reset method.
     * 
     * @param resetMethod
     *            the reset method
     */
    public void setResetMethod(Method resetMethod) {
        this.resetMethod = resetMethod;
    }

    /**
     * Determines if this execute method is target.
     * 
     * @param request
     *            the request
     * @return whether this execute method is target
     */
    public boolean isTarget(HttpServletRequest request) {
        return !StringUtil.isEmpty(request.getParameter(method.getName()))
                || !StringUtil.isEmpty(request.getParameter(method.getName()
                        + ".x"))
                || !StringUtil.isEmpty(request.getParameter(method.getName()
                        + ".y"));
    }
}