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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.slim3.struts.util.ActionUtil;

/**
 * {@link DynaClass} of {@link ActionFormWrapper}.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class ActionFormWrapperClass implements DynaClass {

    /**
     * The name
     */
    protected String name;

    /**
     * The map for dynamic properties.
     */
    protected Map<String, DynaProperty> propertyMap = new HashMap<String, DynaProperty>();

    /**
     * Constructor.
     * 
     * @param name
     *            the name
     * 
     * @throws NullPointerException
     *             if the name parameter is null
     */
    public ActionFormWrapperClass(String name) throws NullPointerException {
        if (name == null) {
            throw new NullPointerException("The name parameter is null.");
        }
        this.name = name;
    }

    public DynaProperty[] getDynaProperties() {
        return propertyMap.values().toArray(
                new DynaProperty[propertyMap.size()]);
    }

    public DynaProperty getDynaProperty(String name) {
        if (name == null) {
            throw new NullPointerException("The name parameter is null.");
        }
        return propertyMap.get(name);
    }

    /**
     * Adds the dynamic property.
     * 
     * @param property
     *            the dynamic property
     */
    public void addDynaProperty(DynaProperty property) {
        propertyMap.put(property.getName(), property);
    }

    public String getName() {
        return name;
    }

    public DynaBean newInstance() throws IllegalAccessException,
            InstantiationException {
        return new ActionFormWrapper(this, ActionUtil.getAction());
    }
}