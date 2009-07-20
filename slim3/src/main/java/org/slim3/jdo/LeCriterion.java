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

/**
 * An implementation class for "less equal" filter criterion.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class LeCriterion extends AbstractCriterion implements FilterCriterion {

    /**
     * The parameter;
     */
    protected Object parameter;

    /**
     * Constructor.
     * 
     * @param attributeMeta
     *            the meta data of attribute
     * @param parameter
     *            the parameter
     * @throws NullPointerException
     *             if the parameter parameter is null
     */
    public LeCriterion(AttributeMeta attributeMeta, Object parameter)
            throws NullPointerException {
        super(attributeMeta);
        if (parameter == null) {
            throw new NullPointerException("The parameter parameter is null.");
        }
        this.parameter = parameter;
    }

    @Override
    public String getQueryString(String parameterName) {
        return attributeMeta.fullName + " <= " + parameterName;
    }

    @Override
    public Object[] getParameters() {
        return new Object[] { parameter };
    }

    @Override
    public boolean accept(Object model) {
        Object value = attributeMeta.getValue(model);
        if (value == null) {
            return false;
        }
        return compareValue(value, parameter) <= 0;
    }
}