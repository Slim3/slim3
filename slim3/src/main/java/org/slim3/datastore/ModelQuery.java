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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.slim3.util.ConversionUtil;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.Query.FilterOperator;

/**
 * A query class for select.
 * 
 * @author higa
 * @param <M>
 *            the model type
 * @since 1.0.0
 * 
 */
public class ModelQuery<M> extends AbstractQuery<ModelQuery<M>> {

    /**
     * The meta data of model.
     */
    protected ModelMeta<M> modelMeta;

    /**
     * The in-memory filter criteria.
     */
    protected List<InMemoryFilterCriterion> inMemoryFilterCriteria =
        new ArrayList<InMemoryFilterCriterion>();

    /**
     * The in-memory sort orders.
     */
    protected List<InMemorySortCriterion> inMemorySortCriteria =
        new ArrayList<InMemorySortCriterion>();

    /**
     * Constructor.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param modelMeta
     *            the meta data of model
     * @throws NullPointerException
     *             if the ds parameter is null or if the modelMeta parameter is
     *             null
     */
    public ModelQuery(AsyncDatastoreService ds, ModelMeta<M> modelMeta)
            throws NullPointerException {
        super(ds);
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        this.modelMeta = modelMeta;
        setUpQuery(modelMeta.getKind());
    }

    /**
     * Constructor.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param modelMeta
     *            the meta data of model
     * @param ancestorKey
     *            the ancestor key
     * @throws NullPointerException
     *             if the ds parameter is null or if the modelMeta parameter is
     *             null or if the ancestorKey parameter is null
     */
    public ModelQuery(AsyncDatastoreService ds, ModelMeta<M> modelMeta,
            Key ancestorKey) throws NullPointerException {
        super(ds);
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        if (ancestorKey == null) {
            throw new NullPointerException("The ancestorKey parameter is null.");
        }
        this.modelMeta = modelMeta;
        setUpQuery(modelMeta.getKind(), ancestorKey);
    }

    /**
     * Constructor.
     * 
     * @param ds
     *            the asynchronous datastore service
     * @param tx
     *            the transaction
     * @param modelMeta
     *            the meta data of model
     * @param ancestorKey
     *            the ancestor key
     * @throws NullPointerException
     *             if the ds parameter is null or if the modelMeta parameter is
     *             null or if the ancestorKey parameter is null
     */
    public ModelQuery(AsyncDatastoreService ds, Transaction tx,
            ModelMeta<M> modelMeta, Key ancestorKey)
            throws NullPointerException {
        super(ds);
        if (modelMeta == null) {
            throw new NullPointerException("The modelMeta parameter is null.");
        }
        if (ancestorKey == null) {
            throw new NullPointerException("The ancestorKey parameter is null.");
        }
        setTx(tx);
        this.modelMeta = modelMeta;
        setUpQuery(modelMeta.getKind(), ancestorKey);
    }

    /**
     * Adds the filter criteria.
     * 
     * @param criteria
     *            the filter criteria
     * @return this instance
     * @throws NullPointerException
     *             if the element of the criteria parameter is null
     */
    public ModelQuery<M> filter(FilterCriterion... criteria)
            throws NullPointerException {
        for (FilterCriterion c : criteria) {
            if (c == null) {
                throw new NullPointerException(
                    "The element of the criteria parameter must not be null.");
            }
            for (Filter f : c.getFilters()) {
                Object value = f.getValue();
                if (modelMeta.isCipherProperty(f.getPropertyName())) {
                    if (value instanceof String) {
                        value = modelMeta.encrypt((String) value);
                    } else if (value instanceof Text) {
                        value = modelMeta.encrypt((Text) value);
                    }
                }
                query.addFilter(f.getPropertyName(), f.getOperator(), value);
            }
        }
        return this;
    }

    /**
     * Adds the in-memory filter criteria.
     * 
     * @param criteria
     *            the in-memory filter criteria
     * @return this instance
     * @throws NullPointerException
     *             if the element of the criteria parameter is null
     */
    public ModelQuery<M> filterInMemory(InMemoryFilterCriterion... criteria)
            throws NullPointerException {
        for (InMemoryFilterCriterion c : criteria) {
            if (c == null) {
                throw new NullPointerException(
                    "The element of the criteria parameter must not be null.");
            }
            inMemoryFilterCriteria.add(c);
        }
        return this;
    }

    /**
     * Adds the sort criteria.
     * 
     * @param criteria
     *            the sort criteria
     * @return this instance
     * @throws NullPointerException
     *             if the element of the criteria parameter is null
     */
    public ModelQuery<M> sort(SortCriterion... criteria)
            throws NullPointerException {
        for (SortCriterion c : criteria) {
            if (c == null) {
                throw new NullPointerException(
                    "The element of the criteria parameter must not be null.");
            }
            Sort s = c.getSort();
            query.addSort(s.getPropertyName(), s.getDirection());
        }
        return this;
    }

    /**
     * Adds the in-memory sort criteria.
     * 
     * @param criteria
     *            the in-memory sort criteria
     * @return this instance
     * @throws NullPointerException
     *             if the element of the criteria parameter is null
     */
    public ModelQuery<M> sortInMemory(InMemorySortCriterion... criteria)
            throws NullPointerException {
        for (InMemorySortCriterion c : criteria) {
            if (c == null) {
                throw new NullPointerException(
                    "The element of the criteria parameter must not be null.");
            }
            inMemorySortCriteria.add(c);
        }
        return this;
    }

    /**
     * Returns the result as a list.
     * 
     * @return the result as a list
     */
    public List<M> asList() {
        addFilterIfPolyModel();
        List<Entity> entityList = asEntityList();
        List<M> ret = new ArrayList<M>(entityList.size());
        for (Entity e : entityList) {
            ModelMeta<M> mm = DatastoreUtil.getModelMeta(modelMeta, e);
            ret.add(mm.entityToModel(e));
        }
        ret = DatastoreUtil.filterInMemory(ret, inMemoryFilterCriteria);
        return DatastoreUtil.sortInMemory(ret, inMemorySortCriteria);
    }

    /**
     * Returns a query result list.
     * 
     * @return a query result list
     */
    public S3QueryResultList<M> asQueryResultList() {
        if (inMemoryFilterCriteria.size() > 0) {
            throw new IllegalStateException(
                "In case of asQueryResultList(), you cannot specify filterInMemory().");
        }
        if (inMemorySortCriteria.size() > 0) {
            throw new IllegalStateException(
                "In case of asQueryResultList(), you cannot specify sortInMemory().");
        }
        addFilterIfPolyModel();
        List<M> modelList = null;
        boolean hasNext = false;
        Cursor cursor = null;
        if (fetchOptions.getLimit() == null) {
            QueryResultList<Entity> entityList = asQueryResultEntityList();
            modelList = new ArrayList<M>(entityList.size());
            for (Entity e : entityList) {
                ModelMeta<M> mm = DatastoreUtil.getModelMeta(modelMeta, e);
                modelList.add(mm.entityToModel(e));
            }
            cursor = entityList.getCursor();
        } else {
            int limit = fetchOptions.getLimit();
            fetchOptions.limit(limit + 1);
            modelList = new ArrayList<M>();
            QueryResultIterator<Entity> ite = asQueryResultEntityIterator();
            while (true) {
                hasNext = ite.hasNext();
                if (!hasNext || modelList.size() == limit) {
                    cursor = ite.getCursor();
                    break;
                }
                Entity e = ite.next();
                ModelMeta<M> mm = DatastoreUtil.getModelMeta(modelMeta, e);
                modelList.add(mm.entityToModel(e));
            }
        }
        return new S3QueryResultList<M>(
            modelList,
            cursor.toWebSafeString(),
            getEncodedFilters(),
            getEncodedSorts(),
            hasNext);
    }

    /**
     * Returns the single result or null if no entities match.
     * 
     * @return the single result
     * @throws PreparedQuery.TooManyResultsException
     *             if the number of the results are more than 1 entity.
     * 
     */
    public M asSingle() throws PreparedQuery.TooManyResultsException {
        List<M> list = asList();
        if (list.size() == 0) {
            return null;
        }
        if (list.size() > 1) {
            throw new PreparedQuery.TooManyResultsException();
        }
        return list.get(0);
    }

    /**
     * Returns a list of keys.
     * 
     * @return a list of keys
     * @throws IllegalStateException
     *             if in-memory filers are specified
     */
    @Override
    public List<Key> asKeyList() throws IllegalStateException {
        if (inMemoryFilterCriteria.size() > 0) {
            throw new IllegalStateException(
                "In the case of asKeyList(), you cannot specify filterInMemory().");
        }
        addFilterIfPolyModel();
        List<Key> keys = super.asKeyList();
        if (inMemorySortCriteria.size() > 0 && inMemorySortCriteria.size() == 1) {
            InMemorySortCriterion c = inMemorySortCriteria.get(0);
            if (c instanceof AbstractCriterion) {
                if (((AbstractCriterion) c).attributeMeta.name
                    .equals(Entity.KEY_RESERVED_PROPERTY)) {
                    if (c instanceof AscCriterion) {
                        Collections.sort(keys);
                    } else {
                        Collections.sort(keys, KeyDescComparator.INSTANCE);
                    }
                    return keys;
                }
            }
            throw new IllegalStateException(
                "In the case of asKeyList(), you cannot specify sortInMemory() except for primary key.");
        }
        return keys;
    }

    /**
     * Returns the result as an {@link Iterator}.
     * 
     * @return the result as an {@link Iterator}
     * @throws IllegalStateException
     *             if in-memory filers are specified or if in-memory sorts are
     *             specified
     */
    public Iterator<M> asIterator() throws IllegalStateException {
        if (inMemoryFilterCriteria.size() > 0) {
            throw new IllegalStateException(
                "In case of asIterator(), you cannot specify filterInMemory().");
        }
        if (inMemorySortCriteria.size() > 0) {
            throw new IllegalStateException(
                "In case of asIterator(), you cannot specify sortInMemory().");
        }
        addFilterIfPolyModel();
        Iterator<Entity> entityIterator = asEntityIterator();
        return new ModelIterator<M>(entityIterator, modelMeta);
    }

    /**
     * Returns the result as an {@link Iterable}.
     * 
     * @return the result as an {@link Iterable}
     * @throws IllegalStateException
     *             if in-memory filers are specified or if in-memory sorts are
     *             specified
     */
    public Iterable<M> asIterable() throws IllegalStateException {
        if (inMemoryFilterCriteria.size() > 0) {
            throw new IllegalStateException(
                "In case of asIterable(), you cannot specify filterInMemory().");
        }
        if (inMemorySortCriteria.size() > 0) {
            throw new IllegalStateException(
                "In case of asIterable(), you cannot specify sortInMemory().");
        }
        return new ModelIterable<M>(asIterator());
    }

    /**
     * Return a minimum value of the property. The value does not include null.
     * 
     * @param <A>
     *            the attribute type
     * @param attributeMeta
     *            the meta data of attribute
     * @return a minimum value of the property
     * @throws NullPointerException
     *             if the attributeMeta parameter is null
     * @throws IllegalStateException
     *             if in-memory filers are specified or if in-memory sorts are
     *             specified
     */
    @SuppressWarnings("unchecked")
    public <A> A min(CoreAttributeMeta<M, A> attributeMeta)
            throws NullPointerException, IllegalStateException {
        if (attributeMeta == null) {
            throw new NullPointerException(
                "The attributeMeta parameter is null.");
        }
        if (inMemoryFilterCriteria.size() > 0) {
            throw new IllegalStateException(
                "In case of min(), you cannot specify filterInMemory().");
        }
        if (inMemorySortCriteria.size() > 0) {
            throw new IllegalStateException(
                "In case of min(), you cannot specify sortInMemory().");
        }
        addFilterIfPolyModel();
        Object value = super.min(attributeMeta.getName());
        return (A) ConversionUtil.convert(value, attributeMeta
            .getAttributeClass());
    }

    /**
     * Return a maximum value of the property.
     * 
     * @param <A>
     *            the attribute type
     * @param attributeMeta
     *            the meta data of attribute
     * @return a maximum value of the property
     * @throws NullPointerException
     *             if the attributeMeta parameter is null
     * @throws IllegalStateException
     *             if in-memory filters are specified or if in-memory sorts are
     *             specified
     */
    @SuppressWarnings("unchecked")
    public <A> A max(CoreAttributeMeta<M, A> attributeMeta)
            throws NullPointerException, IllegalStateException {
        if (attributeMeta == null) {
            throw new NullPointerException(
                "The attributeMeta parameter is null.");
        }
        if (inMemoryFilterCriteria.size() > 0) {
            throw new IllegalStateException(
                "In case of max(), you cannot specify filterInMemory().");
        }
        if (inMemorySortCriteria.size() > 0) {
            throw new IllegalStateException(
                "In case of max(), you cannot specify sortInMemory().");
        }
        addFilterIfPolyModel();
        Object value = super.max(attributeMeta.getName());
        return (A) ConversionUtil.convert(value, attributeMeta
            .getAttributeClass());
    }

    /**
     * Returns a number of entities.
     * 
     * @return a number of entities
     */
    @Override
    public int count() {
        inMemorySortCriteria.clear();
        addFilterIfPolyModel();
        if (inMemoryFilterCriteria.size() > 0) {
            return asList().size();
        }
        return super.count();
    }

    /**
     * Adds a filter for polymorphic model.
     */
    protected void addFilterIfPolyModel() {
        if (modelMeta.getClassHierarchyList().isEmpty()) {
            return;
        }
        query.addFilter(
            modelMeta.getClassHierarchyListName(),
            FilterOperator.EQUAL,
            modelMeta.getModelClass().getName());
    }
}