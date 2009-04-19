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
package org.slim3.gae.jdo;

/**
 * An implementation class for "greater than" filter criterion.
 * 
 * @author higa
 * @param <T>
 *            the parameter type
 * @since 3.0
 * 
 */
public class GtCriterion<T> extends AbstractFilterCriterion<T> {

    /**
     * Constructor.
     * 
     * @param propertyName
     *            the property name
     * @param parameterName
     *            the parameter name
     * @param parameter
     *            the parameter
     */
    public GtCriterion(String propertyName, String parameterName, T parameter) {
        super(propertyName, parameterName, parameter);
    }

    @Override
    public String getQueryString() {
        return propertyName + " > " + parameterName;
    }

}
