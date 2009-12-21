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

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortPredicate;

/**
 * An inverse reference for models.
 * 
 * @author higa
 * @param <M>
 *            the model type
 * @since 3.0
 * 
 */
public class InverseModelListRef<M> extends AbstractModelRef<M> {

    private static final long serialVersionUID = 1L;

    /**
     * The mapped property name.
     */
    protected String mappedPropertyName;

    /**
     * The owner that has this {@link InverseModelListRef}.
     */
    protected Object owner;

    /**
     * The default {@link SortPredicate}s.
     */
    protected SortPredicate[] defaultSortPredicates;

    /**
     * The model.
     */
    protected List<M> modelList;

    /**
     * Constructor.
     */
    protected InverseModelListRef() {
    }

    /**
     * Constructor.
     * 
     * @param <O>
     *            the owner type
     * @param mappedAttributeMeta
     *            the mapped {@link AbstractAttributeMeta}
     * @param owner
     *            the owner that has this {@link InverseModelListRef}the sort
     *            criteria
     * @param criteria
     *            the sort criteria
     * @throws NullPointerException
     *             if the attributeMeta parameter is null or if the owner
     *             parameter is null or if the element of criteria is null
     */
    public <O> InverseModelListRef(
            ModelRefAttributeMeta<M, ModelRef<O>, O> mappedAttributeMeta,
            O owner, SortCriterion... criteria) throws NullPointerException {
        if (mappedAttributeMeta == null) {
            throw new NullPointerException(
                "The mappedAttributeMeta must not be null.");
        }
        if (owner == null) {
            throw new NullPointerException("The owner must not be null.");
        }
        mappedPropertyName = mappedAttributeMeta.getName();
        setModelMeta(mappedAttributeMeta.modelMeta);
        this.owner = owner;
        defaultSortPredicates = new SortPredicate[criteria.length];
        for (int i = 0; i < criteria.length; i++) {
            SortCriterion c = criteria[i];
            if (c == null) {
                throw new NullPointerException(
                    "The element of criteria must not be null.");
            }
            defaultSortPredicates[i] = c.getSortPredicate();
        }
    }

    /**
     * Returns the models.
     * 
     * @return the models
     */
    public List<M> getModelList() {
        if (modelList != null) {
            return modelList;
        }
        return query().getModelList();
    }

    /**
     * Returns {@link ModelListQuery}.
     * 
     * @return {@link ModelListQuery}
     */
    public ModelListQuery query() {
        return new ModelListQuery();
    }

    /**
     * Clears the state of this {@link InverseModelListRef}.
     */
    public void clear() {
        modelList = null;
    }

    /**
     * An internal query for {@link InverseModelListRef}.
     * 
     */
    public class ModelListQuery {

        /**
         * The {@link ModelQuery}.
         */
        protected ModelQuery<M> query;

        /**
         * Whether {@link SortCriterion}s are set. If this flag is false,
         * defaultSortPredicates is used.
         */
        protected boolean sortsSet = false;

        /**
         * Constructor.
         */
        protected ModelListQuery() {
            query = Datastore.query(getModelMeta());
        }

        /**
         * Adds the filter criteria.
         * 
         * @param criteria
         *            the filter criteria
         * @return this instance
         */
        public ModelListQuery filter(FilterCriterion... criteria) {
            query.filter(criteria);
            return this;
        }

        /**
         * Adds the in-memory filter criteria.
         * 
         * @param criteria
         *            the in-memory filter criteria
         * @return this instance
         */
        public ModelListQuery filterInMemory(
                InMemoryFilterCriterion... criteria) {
            query.filterInMemory(criteria);
            return this;
        }

        /**
         * Adds the sort criteria.
         * 
         * @param criteria
         *            the sort criteria
         * @return this instance
         */
        public ModelListQuery sort(SortCriterion... criteria) {
            sortsSet = true;
            query.sort(criteria);
            return this;
        }

        /**
         * Adds the in-memory sort criteria.
         * 
         * @param criteria
         *            the in-memory sort criteria
         * @return this instance
         */
        public ModelListQuery sortInMemory(SortCriterion... criteria) {
            query.sortInMemory(criteria);
            return this;
        }

        /**
         * Returns models.
         * 
         * @return models
         */
        public List<M> getModelList() {
            ModelMeta<?> ownerModelMeta =
                Datastore.getModelMeta(owner.getClass());
            Key key = ownerModelMeta.getKey(owner);
            if (key == null) {
                modelList = new ArrayList<M>();
            } else {
                query.filter(mappedPropertyName, FilterOperator.EQUAL, key);
                if (!sortsSet) {
                    query.sort(defaultSortPredicates);
                }
                modelList = query.asList();
            }
            return modelList;
        }
    }
}