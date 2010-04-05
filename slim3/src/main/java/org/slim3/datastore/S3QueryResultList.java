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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultList;

/**
 * @author higa
 * @param <T>
 *            the element type
 * @since 1.0.1
 */
public class S3QueryResultList<T> implements QueryResultList<T> {

    /**
     * The delegate.
     */
    protected List<T> delegate;

    /**
     * The cursor.
     */
    protected Cursor cursor;

    /**
     * The array of filters.
     */
    protected Filter[] filters;

    /**
     * The array of sorts.
     */
    protected Sort[] sorts;

    /**
     * Whether a next entry exists.
     */
    protected boolean hasNext;

    /**
     * Constructor.
     * 
     * @param delegate
     *            the delegate
     * @param cursor
     *            the cursor
     * @param filters
     *            the array of filters
     * @param sorts
     *            the array of sorts
     * @param hasNext
     *            whether a next element exists
     * @throws NullPointerException
     *             if the delegate parameter is null or if the cursor parameter
     *             is null or if the filters parameter is null or if the sorts
     *             parameter is null
     */
    public S3QueryResultList(List<T> delegate, Cursor cursor, Filter[] filters,
            Sort[] sorts, boolean hasNext) throws NullPointerException {
        if (delegate == null) {
            throw new NullPointerException(
                "The delegate parameter must not be null.");
        }
        if (cursor == null) {
            throw new NullPointerException(
                "The cursor parameter must not be null.");
        }
        if (filters == null) {
            throw new NullPointerException(
                "The filters parameter must not be null.");
        }
        if (sorts == null) {
            throw new NullPointerException(
                "The sorts parameter must not be null.");
        }
        this.delegate = delegate;
        this.cursor = cursor;
        this.filters = filters;
        this.sorts = sorts;
        this.hasNext = hasNext;
    }

    public Cursor getCursor() {
        return cursor;
    }

    /**
     * Returns the array of filters.
     * 
     * @return the array of filters
     */
    public Filter[] getFilters() {
        return filters;
    }

    /**
     * Returns the array of sorts
     * 
     * @return the array of sorts
     */
    public Sort[] getSorts() {
        return sorts;
    }

    /**
     * Determines if a next entry exists
     * 
     * @return whether a next entry exists
     */
    public boolean hasNext() {
        return hasNext;
    }

    public void add(int index, T element) {
        delegate.add(index, element);
    }

    public boolean add(T o) {
        return delegate.add(o);
    }

    public boolean addAll(Collection<? extends T> c) {
        return delegate.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        return delegate.addAll(index, c);
    }

    public void clear() {
        delegate.clear();
    }

    public boolean contains(Object o) {
        return delegate.contains(o);
    }

    public boolean containsAll(Collection<?> c) {
        return delegate.containsAll(c);
    }

    public T get(int index) {
        return delegate.get(index);
    }

    public int indexOf(Object o) {
        return delegate.indexOf(o);
    }

    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    public Iterator<T> iterator() {
        return delegate.iterator();
    }

    public int lastIndexOf(Object o) {
        return delegate.lastIndexOf(o);
    }

    public ListIterator<T> listIterator() {
        return delegate.listIterator();
    }

    public ListIterator<T> listIterator(int index) {
        return delegate.listIterator(index);
    }

    public T remove(int index) {
        return delegate.remove(index);
    }

    public boolean remove(Object o) {
        return delegate.remove(o);
    }

    public boolean removeAll(Collection<?> c) {
        return delegate.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return delegate.retainAll(c);
    }

    public T set(int index, T element) {
        return delegate.set(index, element);
    }

    public int size() {
        return delegate.size();
    }

    public List<T> subList(int fromIndex, int toIndex) {
        return delegate.subList(fromIndex, toIndex);
    }

    public Object[] toArray() {
        return delegate.toArray();
    }

    public <A> A[] toArray(A[] a) {
        return delegate.toArray(a);
    }
}