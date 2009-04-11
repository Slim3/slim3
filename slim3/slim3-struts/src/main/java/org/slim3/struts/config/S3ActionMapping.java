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
package org.slim3.struts.config;

import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.slim3.commons.bean.BeanDesc;
import org.slim3.commons.bean.BeanUtil;
import org.slim3.commons.config.Configuration;
import org.slim3.commons.util.ArrayMap;
import org.slim3.struts.S3StrutsGlobals;
import org.slim3.struts.util.ActionUtil;
import org.slim3.struts.util.S3ExecuteConfigUtil;

/**
 * {@link ActionMapping} for Slim3.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class S3ActionMapping extends ActionMapping {

    private static final long serialVersionUID = 1L;

    /**
     * The action name.
     */
    protected String actionName;

    /**
     * The action class.
     */
    protected Class<?> actionClass;

    /**
     * The bean descriptor of the action.
     */
    protected BeanDesc actionBeanDesc;

    /**
     * The map for {@link S3ExecuteConfig}.
     */
    protected ArrayMap<String, S3ExecuteConfig> executeConfigs = new ArrayMap<String, S3ExecuteConfig>();

    /**
     * The form field.
     */
    protected Field formField;

    /**
     * Constructor.
     */
    public S3ActionMapping() {
        scope = "request";
    }

    @Override
    public String getInput() {
        S3ExecuteConfig executeConfig = S3ExecuteConfigUtil.getExecuteConfig();
        if (executeConfig != null) {
            String input2 = executeConfig.getInput();
            if (!input2.startsWith("/")) {
                input2 = ActionUtil.getActionPath() + input2;
            }
            if (input2.indexOf('.') > 0) {
                input2 = Configuration.getInstance().getValue(
                        S3StrutsGlobals.VIEW_PREFIX_KEY)
                        + input2;
            }
            return input2;
        }
        return super.getInput();
    }

    @Override
    public boolean getValidate() {
        S3ExecuteConfig executeConfig = S3ExecuteConfigUtil.getExecuteConfig();
        if (executeConfig != null) {
            return executeConfig.isValidate();
        }
        return super.getValidate();
    }

    /**
     * Returns the action name.
     * 
     * @return the action name
     */
    public String getActionName() {
        return actionName;
    }

    /**
     * Sets the action name.
     * 
     * @param actionName
     *            the action name
     */
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    /**
     * Returns the action class.
     * 
     * @return the action class
     */
    public Class<?> getActionClass() {
        return actionClass;
    }

    /**
     * Returns the bean descriptor of the action.
     * 
     * @return the bean descriptor of the action
     */
    public BeanDesc getActionBeanDesc() {
        return actionBeanDesc;
    }

    /**
     * Sets the action class.
     * 
     * @param actionClass
     *            the action class
     * @throws NullPointerException
     *             if the actionClass parameter is null
     */
    public void setActionClass(Class<?> actionClass)
            throws NullPointerException {
        if (actionClass == null) {
            throw new NullPointerException("The actionClass parameter is null.");
        }
        this.actionClass = actionClass;
        actionBeanDesc = BeanUtil.getBeanDesc(actionClass);
    }

    /**
     * Returns the number of execute configurations.
     * 
     * @return the number of execute configurations
     */
    public int getExecuteConfigSize() {
        return executeConfigs.size();
    }

    /**
     * Find execute configuration.
     * 
     * @param request
     *            the request
     * @return execute configuration
     */
    public S3ExecuteConfig findExecuteConfig(HttpServletRequest request) {
        for (int i = 0; i < executeConfigs.size(); i++) {
            S3ExecuteConfig executeConfig = executeConfigs.get(i);
            if (executeConfig.isTarget(request)) {
                return executeConfig;
            }
        }
        return getExecuteConfig("index");
    }

    /**
     * Returns execute configuration.
     * 
     * @param name
     *            the name
     * @return execute configuration
     */
    public S3ExecuteConfig getExecuteConfig(String name) {
        return executeConfigs.get(name);
    }

    /**
     * Adds execute configuration.
     * 
     * @param executeConfig
     *            the execute configuration
     */
    public void addExecuteConfig(S3ExecuteConfig executeConfig) {
        executeConfigs.put(executeConfig.getMethod().getName(), executeConfig);
    }

    /**
     * Returns the execute method names.
     * 
     * @return the execute method names
     */
    public String[] getExecuteMethodNames() {
        return executeConfigs.keySet().toArray(
                new String[executeConfigs.size()]);
    }
}