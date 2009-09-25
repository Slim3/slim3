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

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

/**
 * An implementation class for "startsWith" filter criterion.
 * 
 * @author higa
 * @param <M>
 *            the model type
 * @since 3.0
 * 
 */
public class StartsWithCriterion<M> extends AbstractCriterion<M, String>
        implements FilterCriterion<M> {

    /**
     * The parameter;
     */
    protected String parameter;

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
    public StartsWithCriterion(CoreAttributeMeta<M, String> attributeMeta,
            String parameter) throws NullPointerException {
        super(attributeMeta);
        if (parameter == null) {
            throw new NullPointerException("The parameter parameter is null.");
        }
        this.parameter = parameter;
    }

    public void apply(Query query) {
        query.addFilter(
            attributeMeta.getName(),
            FilterOperator.GREATER_THAN_OR_EQUAL,
            parameter).addFilter(
            attributeMeta.getName(),
            FilterOperator.LESS_THAN,
            parameter + "\ufffd");

    }
}