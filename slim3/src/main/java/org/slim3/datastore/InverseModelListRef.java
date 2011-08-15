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

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.FilterOperator;

/**
 * An inverse reference for models.
 * 
 * @author higa
 * @param <M>
 *            the model type
 * @param <O>
 *            the owner type
 * @since 1.0.0
 * 
 */
public class InverseModelListRef<M, O> extends AbstractInverseModelRef<M, O> {

    private static final long serialVersionUID = 1L;

    /**
     * The default sort orders.
     */
    protected Sort[] defaultSorts;

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
     * @param modelClass
     *            the model class
     * @param mappedPropertyName
     *            the mapped property name
     * @param owner
     *            the owner that has this {@link InverseModelRef}
     * @param defaultSorts
     *            the default sort orders
     * @throws NullPointerException
     *             if the modelClass parameter is null or if the
     *             mappedPropertyName parameter is null or if the owner
     *             parameter is null
     */
    public InverseModelListRef(Class<M> modelClass,
            CharSequence mappedPropertyName, O owner, Sort... defaultSorts)
            throws NullPointerException {
        super(modelClass, mappedPropertyName, owner);
        this.defaultSorts = defaultSorts;
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
        public ModelListQuery sortInMemory(InMemorySortCriterion... criteria) {
            query.sortInMemory(criteria);
            return this;
        }

        /**
         * Specifies the offset.
         * 
         * @param offset
         *            the offset
         * @return this instance
         */
        public ModelListQuery offset(int offset) {
            query.offset(offset);
            return this;
        }

        /**
         * Specifies the limit.
         * 
         * @param limit
         *            the limit
         * @return this instance
         */
        public ModelListQuery limit(int limit) {
            query.limit(limit);
            return this;
        }

        /**
         * Specifies the size of prefetch.
         * 
         * @param prefetchSize
         *            the size of prefetch
         * @return this instance
         */
        public ModelListQuery prefetchSize(int prefetchSize) {
            query.prefetchSize(prefetchSize);
            return this;
        }

        /**
         * Specifies the size of chunk.
         * 
         * @param chunkSize
         *            the size of chunk
         * @return this instance
         */
        public ModelListQuery chunkSize(int chunkSize) {
            query.chunkSize(chunkSize);
            return this;
        }

        /**
         * Specifies the encoded start cursor.
         * 
         * @param encodedCursor
         *            the encoded cursor
         * @return this instance
         * @throws NullPointerException
         *             if the encodedCursor parameter is null
         */
        public ModelListQuery encodedStartCursor(String encodedCursor)
                throws NullPointerException {
            if (encodedCursor == null) {
                throw new NullPointerException(
                    "The encodedCursor parameter must not be null.");
            }
            query.encodedStartCursor(encodedCursor);
            return this;
        }

        /**
         * Specifies the encoded end cursor.
         * 
         * @param encodedCursor
         *            the encoded cursor
         * @return this instance
         * @throws NullPointerException
         *             if the encodedCursor parameter is null
         */
        public ModelListQuery encodedEndCursor(String encodedCursor)
                throws NullPointerException {
            if (encodedCursor == null) {
                throw new NullPointerException(
                    "The encodedCursor parameter must not be null.");
            }
            query.encodedEndCursor(encodedCursor);
            return this;
        }

        /**
         * Returns models.
         * 
         * @return models
         */
        public List<M> getModelList() {
            Key key = getOwnerKey();
            if (key == null) {
                modelList = new ArrayList<M>();
            } else {
                query.filter(mappedPropertyName, FilterOperator.EQUAL, key);
                if (!sortsSet) {
                    query.sort(defaultSorts);
                }
                modelList = query.asList();
            }
            return modelList;
        }

        /**
         * Returns a query result list.
         * 
         * @return a query result list
         */
        public S3QueryResultList<M> asQueryResultList() {
            Key key = getOwnerKey();
            if (key == null) {
                return new S3QueryResultList<M>(new ArrayList<M>());
            }
            query.filter(mappedPropertyName, FilterOperator.EQUAL, key);
            if (!sortsSet) {
                query.sort(defaultSorts);
            }
            return query.asQueryResultList();
        }
    }
}