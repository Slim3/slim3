/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import java.util.List;

/**
 * An implementation class for "in" in-memory filter.
 * 
 * @author higa
 * @since 1.0.1
 * 
 */
public class InMemoryInCriterion extends AbstractCriterion implements
        InMemoryFilterCriterion {

    /**
     * The value;
     */
    protected List<?> value;

    /**
     * Constructor.
     * 
     * @param attributeMeta
     *            the meta data of attribute
     * @param value
     *            the value
     * @throws NullPointerException
     *             if the attributeMeta parameter is null or if the value
     *             parameter is null
     * @throws IllegalArgumentException
     *             if the IN(value) parameter is empty
     */
    public InMemoryInCriterion(AbstractAttributeMeta<?, ?> attributeMeta,
            Iterable<?> value) throws NullPointerException {
        super(attributeMeta);
        if (value == null) {
            throw new NullPointerException("The IN parameter must not be null.");
        }
        this.value = convertValueForDatastore(value);
        if (this.value.isEmpty()) {
            throw new IllegalArgumentException(
                "The IN parameter must not be empty.");
        }
    }

    public boolean accept(Object model) {
        Object v = convertValueForDatastore(attributeMeta.getValue(model));
        for (Object o : value) {
            if (compareValue(v, o) == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return attributeMeta.getName() + " in(" + value + ")";
    }
}