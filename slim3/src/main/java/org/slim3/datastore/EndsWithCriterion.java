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
package org.slim3.datastore;

/**
 * An implementation class for "endsWith" filter.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class EndsWithCriterion extends AbstractCriterion implements
        InMemoryFilterCriterion {

    /**
     * The value;
     */
    protected String value;

    /**
     * Constructor.
     * 
     * @param attributeMeta
     *            the meta data of attribute
     * @param value
     *            the value
     * @throws NullPointerException
     *             if the attributeMeta parameter is null
     */
    public EndsWithCriterion(AbstractAttributeMeta<?, ?> attributeMeta,
            String value) throws NullPointerException {
        super(attributeMeta);
        this.value = value;
    }

    public boolean accept(Object model) {
        Object v = attributeMeta.getValue(model);
        if (v instanceof Iterable<?>) {
            for (Object o : (Iterable<?>) v) {
                if (acceptInternal(o)) {
                    return true;
                }
            }
            return false;
        }
        return acceptInternal(v);
    }

    /**
     * Determines if the model is accepted internally.
     * 
     * @param propertyValue
     *            the property value
     * @return whether the model is accepted
     */
    protected boolean acceptInternal(Object propertyValue) {
        if (propertyValue == null && value == null) {
            return true;
        }
        if (propertyValue != null
            && value != null
            && ((String) propertyValue).endsWith(value)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return attributeMeta.getName() + ".endsWith(" + value + ")";
    }
}