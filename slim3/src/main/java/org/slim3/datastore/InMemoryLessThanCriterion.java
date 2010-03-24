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

/**
 * An implementation class for "less than" in-memory filter.
 * 
 * @author higa
 * @since 1.0.1
 * 
 */
public class InMemoryLessThanCriterion extends AbstractCriterion implements
        InMemoryFilterCriterion {

    /**
     * The value;
     */
    protected Object value;

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
    public InMemoryLessThanCriterion(AbstractAttributeMeta<?, ?> attributeMeta,
            Object value) throws NullPointerException {
        super(attributeMeta);
        this.value = convertValueForDatastore(value);
    }

    public boolean accept(Object model) {
        Object v = convertValueForDatastore(attributeMeta.getValue(model));
        if (v instanceof Iterable<?>) {
            for (Object o : (Iterable<?>) v) {
                if (compareValue(o, value) < 0) {
                    return true;
                }
            }
            return false;
        }
        return compareValue(v, value) < 0;
    }

    @Override
    public String toString() {
        return attributeMeta.getName() + " < " + value;
    }
}