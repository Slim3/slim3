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

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Text;

/**
 * An abstract class for criterion.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public abstract class AbstractCriterion {

    /**
     * The meta data of attribute.
     */
    protected AbstractAttributeMeta<?, ?> attributeMeta;

    /**
     * Constructor.
     * 
     * @param attributeMeta
     *            the meta data of attribute
     * @throws NullPointerException
     *             if the attributeMeta parameter is null
     */
    public AbstractCriterion(AbstractAttributeMeta<?, ?> attributeMeta)
            throws NullPointerException {
        if (attributeMeta == null) {
            throw new NullPointerException(
                "The attributeMeta parameter must not be null.");
        }
        this.attributeMeta = attributeMeta;
    }

    /**
     * Compares its two arguments for order. Returns a negative integer, zero,
     * or a positive integer as the first argument is less than, equal to, or
     * greater than the second.
     * 
     * @param v1
     *            the attribute value
     * @param v2
     *            the compared value
     * @return the compared result
     * @throws IllegalStateException
     *             if the attribute value is not comparable
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected int compareValue(Object v1, Object v2) {
        if (v1 == null && v2 == null) {
            return 0;
        }
        if (v1 == null) {
            return -1;
        }
        if (v2 == null) {
            return 1;
        }
        if (!(v1 instanceof Comparable)) {
            throw new IllegalStateException("The property("
                + attributeMeta.name
                + ") of model("
                + attributeMeta.modelMeta.getModelClass().getName()
                + ") is not comparable.");
        }
        return ((Comparable) v1).compareTo(v2);
    }

    /**
     * Converts the value for datastore.
     * 
     * @param value
     *            the value
     * @return a converted value for datastore
     */
    protected Object convertValueForDatastore(Object value) {
        if (value instanceof Enum<?>) {
            return ((Enum<?>) value).name();
        }
        if (value instanceof Iterable<?>) {
            return convertValueForDatastore((Iterable<?>) value);
        }
        if (value instanceof ModelRef<?>) {
            return ((ModelRef<?>) value).getKey();
        }
        if (value instanceof Text) {
            return ((Text) value).getValue();
        }
        return value;
    }

    /**
     * Converts the value for datastore.
     * 
     * @param value
     *            the value
     * @return a converted value for datastore
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected List<?> convertValueForDatastore(Iterable<?> value) {
        if (value == null) {
            return null;
        }
        List list = new ArrayList();
        for (Object o : value) {
            list.add(convertValueForDatastore(o));
        }
        return list;
    }
}