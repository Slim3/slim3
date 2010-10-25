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

import com.google.appengine.api.datastore.Text;

/**
 * An implementation class for "startsWith" in-memory filter.
 * 
 * @author higa
 * @since 1.0.1
 * 
 */
public class InMemoryStartsWithCriterion extends AbstractCriterion implements
        InMemoryFilterCriterion {

    /**
     * The value;
     */
    protected String value;

    /**
     * The high value.
     */
    protected String highValue = "\ufffd";

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
    public InMemoryStartsWithCriterion(
            AbstractAttributeMeta<?, ?> attributeMeta, String value)
            throws NullPointerException {
        super(attributeMeta);
        Object o = convertValueForDatastore(value);
        if (o instanceof String) {
            this.value = (String) o;
        } else {
            this.value = value;
        }
        if (this.value != null) {
            highValue = this.value + highValue;
        }
    }

    public boolean accept(Object model) {
        Object v = attributeMeta.getValue(model);
        if (v instanceof Iterable<?>) {
            for (Object o : (Iterable<?>) v) {
                if (compareValue(o, value) >= 0
                    && compareValue(o, highValue) < 0) {
                    return true;
                }
            }
            return false;
        }
        if (v instanceof Text) {
            v = ((Text) v).getValue();
        }
        return compareValue(v, value) >= 0 && compareValue(v, highValue) < 0;
    }

    @Override
    public String toString() {
        return attributeMeta.getName()
            + " >= "
            + value
            + " && "
            + attributeMeta.getName()
            + " < "
            + highValue;
    }
}