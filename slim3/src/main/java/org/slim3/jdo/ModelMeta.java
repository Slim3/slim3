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
package org.slim3.jdo;

import org.slim3.util.BeanDesc;
import org.slim3.util.BeanUtil;

/**
 * A meta data of model.
 * 
 * @author higa
 * @param <T>
 *            the model type
 * @since 3.0
 * 
 */
public class ModelMeta<T> {

    /**
     * The model class.
     */
    protected Class<T> modelClass;

    /**
     * The bean descriptor.
     */
    protected BeanDesc beanDesc;

    /**
     * The attribute name in case of embedded attribute.
     */
    protected String attributeName;

    /**
     * Constructor.
     * 
     * @param modelClass
     *            the model class
     */
    public ModelMeta(Class<T> modelClass) {
        this(modelClass, null);
    }

    /**
     * Constructor.
     * 
     * @param modelClass
     *            the model class
     * @param attributeName
     *            the attribute name in case of embedded attribute
     * @throws NullPointerException
     *             if the modelClass parameter is null
     */
    public ModelMeta(Class<T> modelClass, String attributeName)
            throws NullPointerException {
        if (modelClass == null) {
            throw new NullPointerException("The modelClass parameter is null.");
        }
        this.modelClass = modelClass;
        this.attributeName = attributeName;
    }

    /**
     * Returns the model class.
     * 
     * @return the model class
     */
    public Class<T> getModelClass() {
        return modelClass;
    }

    /**
     * Returns the bean descriptor.
     * 
     * @return the bean descriptor
     */
    protected BeanDesc getBeanDesc() {
        if (beanDesc != null) {
            return beanDesc;
        }
        beanDesc = BeanUtil.getBeanDesc(modelClass);
        return beanDesc;
    }
}