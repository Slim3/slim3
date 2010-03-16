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
package org.slim3.util;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Set;

/**
 * A set acts like array.
 * 
 * @author higa
 * @param <E>
 *            the element type
 * @since 1.0.0
 */
public class ArraySet<E> extends AbstractSet<E> implements Set<E>, Cloneable,
        java.io.Serializable {

    static final long serialVersionUID = -5024744406713321676L;

    private transient ArrayMap<E, Object> map;

    // Dummy value to associate with an Object in the backing Map
    private static final Object PRESENT = new Object();

    /**
     * Constructor.
     */
    public ArraySet() {
        map = new ArrayMap<E, Object>();
    }

    /**
     * Constructor.
     * 
     * @param c
     *            the collection whose elements are to be placed into this set.
     * @throws NullPointerException
     *             if the specified collection is null.
     */
    public ArraySet(Collection<? extends E> c) throws NullPointerException {
        if (c == null) {
            throw new NullPointerException("The c parameter must not be null.");
        }
        map =
            new ArrayMap<E, Object>(Math.max((int) (c.size() / .75f) + 1, 16));
        addAll(c);
    }

    /**
     * Constructor.
     * 
     * @param initialCapacity
     *            the initial capacity of the hash table.
     * @throws IllegalArgumentException
     *             if the initial capacity is less than zero.
     */
    public ArraySet(int initialCapacity) {
        map = new ArrayMap<E, Object>(initialCapacity);
    }

    /**
     * Returns an element specified by the index.
     * 
     * @param index
     *            the index
     * @return an element
     */
    public E get(int index) {
        return map.getKey(index);
    }

    /**
     * Returns an iterator over the elements in this set. The elements are
     * returned in no particular order.
     * 
     * @return an Iterator over the elements in this set.
     * @see ConcurrentModificationException
     */
    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    /**
     * Returns the number of elements in this set (its cardinality).
     * 
     * @return the number of elements in this set (its cardinality).
     */
    @Override
    public int size() {
        return map.size();
    }

    /**
     * Returns <tt>true</tt> if this set contains no elements.
     * 
     * @return <tt>true</tt> if this set contains no elements.
     */
    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Returns <tt>true</tt> if this set contains the specified element.
     * 
     * @param o
     *            element whose presence in this set is to be tested.
     * @return <tt>true</tt> if this set contains the specified element.
     */
    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    /**
     * Adds the specified element to this set if it is not already present.
     * 
     * @param o
     *            element to be added to this set.
     * @return <tt>true</tt> if the set did not already contain the specified
     *         element.
     */
    @Override
    public boolean add(E o) {
        return map.put(o, PRESENT) == null;
    }

    /**
     * Removes the specified element from this set if it is present.
     * 
     * @param o
     *            object to be removed from this set, if present.
     * @return <tt>true</tt> if the set contained the specified element.
     */
    @Override
    public boolean remove(Object o) {
        return map.remove(o) == PRESENT;
    }

    /**
     * Removes all of the elements from this set.
     */
    @Override
    public void clear() {
        map.clear();
    }

    /**
     * Returns a shallow copy of this <tt>HashSet</tt> instance: the elements
     * themselves are not cloned.
     * 
     * @return a shallow copy of this set.
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object clone() {
        try {
            ArraySet<E> newSet = (ArraySet<E>) super.clone();
            newSet.map = (ArrayMap<E, Object>) map.clone();
            return newSet;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    /**
     * Save the state of this <tt>HashSet</tt> instance to a stream (that is,
     * serialize this set).
     * 
     * @serialData The capacity of the backing <tt>HashMap</tt> instance (int),
     *             and its load factor (float) are emitted, followed by the size
     *             of the set (the number of elements it contains) (int),
     *             followed by all of its elements (each an Object) in no
     *             particular order.
     */
    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        s.defaultWriteObject();
        s.writeInt(map.size());
        for (Iterator<E> i = map.keySet().iterator(); i.hasNext();) {
            s.writeObject(i.next());
        }
    }

    /**
     * Reconstitute the <tt>HashSet</tt> instance from a stream (that is,
     * deserialize it).
     */
    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        // Read in any hidden serialization magic
        s.defaultReadObject();
        int size = s.readInt();
        map = new ArrayMap<E, Object>(size);
        for (int i = 0; i < size; i++) {
            E e = (E) s.readObject();
            map.put(e, PRESENT);
        }
    }
}