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
 * An implementation class for "ascending" order criterion.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class AscCriterion extends AbstractOrderCriterion {

    /**
     * Constructor.
     * 
     * @param attributeMeta
     *            the meta data of attribute
     * @param propertyName
     *            the property name
     */
    public AscCriterion(AttributeMeta attributeMeta, String propertyName) {
        super(attributeMeta, propertyName);
    }

    @Override
    public String getQueryString() {
        return propertyName + " asc";
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compare(Object o1, Object o2) {
        if (attributeMeta.modelMeta.attributeName != null) {
            throw new IllegalStateException(
                "Sort operation is not supported for embedded model("
                    + attributeMeta.modelMeta.getModelClass().getName()
                    + ").");
        }
        Object v1 = attributeMeta.getPropertyDesc().getValue(o1);
        Object v2 = attributeMeta.getPropertyDesc().getValue(o2);
        if (v1 == null && v2 == null) {
            return 0;
        }
        if (v1 == null) {
            return 1;
        }
        if (v2 == null) {
            return -1;
        }
        if (!(v1 instanceof Comparable)) {
            throw new IllegalStateException("The property("
                + propertyName
                + ") of model("
                + attributeMeta.modelMeta.getModelClass().getName()
                + ") is not comparable.");
        }
        return Comparable.class.cast(v1).compareTo(v2);
    }

}