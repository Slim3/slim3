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
package org.slim3.struts.form;

import java.lang.reflect.Method;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorException;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.Resources;
import org.slim3.commons.util.MethodUtil;
import org.slim3.struts.config.S3ExecuteConfig;
import org.slim3.struts.util.S3ExecuteConfigUtil;
import org.slim3.struts.web.WebLocator;

/**
 * A wrapper class of {@link ActionForm}.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class ActionFormWrapper extends ActionForm implements DynaBean {

    private static final long serialVersionUID = 1L;

    /**
     * The dynamic class.
     */
    protected DynaClass dynaClass;

    /**
     * The controller.
     */
    protected Object controller;

    /**
     * Constructor.
     * 
     * @param dynaClass
     *            the dynamic class
     * @param controller
     *            the controller
     * @throws NullPointerException
     *             if the dynaClass parameter is null or if the controller
     *             parameter is null
     * 
     */
    public ActionFormWrapper(DynaClass dynaClass, Object controller)
            throws NullPointerException {
        if (dynaClass == null) {
            throw new NullPointerException("The dynaClass parameter is null.");
        }
        if (controller == null) {
            throw new NullPointerException("The controller parameter is null.");
        }
        this.dynaClass = dynaClass;
        this.controller = controller;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        S3ExecuteConfig executeConfig = S3ExecuteConfigUtil.getExecuteConfig();
        if (executeConfig != null) {
            Method m = executeConfig.getResetMethod();
            if (m != null) {
                MethodUtil.invoke(m, controller);
            }
        }
    }

    @Override
    public ActionErrors validate(ActionMapping actionMapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        S3ExecuteConfig executeConfig = S3ExecuteConfigUtil.getExecuteConfig();
        if (executeConfig != null) {
            ServletContext application = WebLocator.getServletContext();
            String validationKey = actionMapping.getName() + "_"
                    + executeConfig.getMethod().getName();
            Validator validator = Resources.initValidator(validationKey, this,
                    application, request, errors, 0);
            try {
                validator.validate();
            } catch (ValidatorException e) {
                throw new RuntimeException(e);
            }
        }
        return errors;
    }

    public DynaClass getDynaClass() {
        return dynaClass;
    }

    public Object get(String name) {
        S3DynaProperty property = (S3DynaProperty) dynaClass
                .getDynaProperty(name);
        if (property == null) {
            throw new IllegalArgumentException("The property(" + name
                    + ") is not found in the class("
                    + controller.getClass().getName() + ").");
        }
        return property.getPropertyDesc().getValue(controller);
    }

    public void set(String name, Object value) {
        throw new UnsupportedOperationException("set");
    }

    public boolean contains(String name, String key) {
        throw new UnsupportedOperationException("contains");
    }

    public Object get(String name, int index) {
        throw new UnsupportedOperationException("get");
    }

    public Object get(String name, String key) {
        throw new UnsupportedOperationException("get");
    }

    public void remove(String name, String key) {
        throw new UnsupportedOperationException("remove");

    }

    public void set(String name, int index, Object value) {
        throw new UnsupportedOperationException("set");
    }

    public void set(String name, String key, Object value) {
        throw new UnsupportedOperationException("set");

    }
}