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

import org.apache.commons.beanutils.DynaClass;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.FormBeanConfig;

/**
 * {@link FormBeanConfig} for Slim3.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class S3FormBeanConfig extends FormBeanConfig {

    private static final long serialVersionUID = 1L;

    /**
     * The dynamic class.
     */
    protected DynaClass dynaClass;

    @Override
    public ActionForm createActionForm(ActionServlet servlet)
            throws IllegalAccessException, InstantiationException {

        ActionForm actionForm = (ActionForm) dynaClass.newInstance();
        actionForm.setServlet(servlet);
        return actionForm;
    }

    /**
     * Returns the dynamic class.
     * 
     * @return the dynamic class
     */
    public DynaClass getDynaClass() {
        return dynaClass;
    }

    /**
     * Sets the dynamic class
     * 
     * @param dynaClass
     *            the dynamic class
     */
    public void setDynaClass(DynaClass dynaClass) {
        this.dynaClass = dynaClass;
    }
}