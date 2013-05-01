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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * A map acts like array.
 * 
 * @author higa
 * @param <K>
 *            the key type
 * @param <V>
 *            the value type
 * @since 1.0.0
 */
public class ArrayMap<K, V> extends AbstractMap<K, V> implements Map<K, V>,
        Cloneable, Externalizable {

    private static final long serialVersionUID = 1L;

    /**
     * The default initial capacity.
     */
    protected static final int INITIAL_CAPACITY = 17;

    /**
     * The default load factor.
     */
    protected static final float LOAD_FACTOR = 0.75f;

    /**
     * The threshold to extend the capacity.
     */
    protected transient int threshold;

    /**
     * The map table.
     */
    protected transient Entry<K, V>[] mapTable;

    /**
     * The list table.
     */
    protected transient Entry<K, V>[] listTable;

    /**
     * The number of entries.
     */
    protected transient int size = 0;

    /**
     * The set of entries.
     */
    protected transient Set<Map.Entry<K, V>> entrySet = null;

    /**
     * Constructor.
     */
    public ArrayMap() {
        this(INITIAL_CAPACITY);
    }

    /**
     * Constructor.
     * 
     * @param initialCapacity
     *            the initial capacity
     */
    public ArrayMap(int initialCapacity) {
        if (initialCapacity <= 0) {
            initialCapacity = INITIAL_CAPACITY;
        }
        mapTable = createArray(initialCapacity);
        listTable = createArray(initialCapacity);
        threshold = (int) (initialCapacity * LOAD_FACTOR);
    }

    /**
     * Constructor.
     * 
     * @param map
     *            other map
     */
    public ArrayMap(Map<? extends K, ? extends V> map) {
        this((int) (map.size() / LOAD_FACTOR) + 1);
        putAll(map);
    }

    @Override
    public final int size() {
        return size;
    }

    @Override
    public final boolean isEmpty() {
        return size == 0;
    }

    @Override
    public final boolean containsValue(Object value) {
        return indexOf(value) >= 0;
    }

    /**
     * Returns the index in the list table.
     * 
     * @param value
     *            the value
     * @return the index
     */
    public final int indexOf(Object value) {
        if (value != null) {
            for (int i = 0; i < size; i++) {
                if (value.equals(listTable[i].value)) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (listTable[i].value == null) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public boolean containsKey(Object key) {
        Entry<K, V>[] tbl = mapTable;
        if (key != null) {
            int hashCode = key.hashCode();
            int index = (hashCode & 0x7FFFFFFF) % tbl.length;
            for (Entry<K, V> e = tbl[index]; e != null; e = e.next) {
                if (e.hashCode == hashCode && key.equals(e.key)) {
                    return true;
                }
            }
        } else {
            for (Entry<K, V> e = tbl[0]; e != null; e = e.next) {
                if (e.key == null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        Entry<K, V>[] tbl = mapTable;
        if (key != null) {
            int hashCode = key.hashCode();
            int index = (hashCode & 0x7FFFFFFF) % tbl.length;
            for (Entry<K, V> e = tbl[index]; e != null; e = e.next) {
                if (e.hashCode == hashCode && key.equals(e.key)) {
                    return e.value;
                }
            }
        } else {
            for (Entry<K, V> e = tbl[0]; e != null; e = e.next) {
                if (e.key == null) {
                    return e.value;
                }
            }
        }
        return null;
    }

    /**
     * Returns the value for the index.
     * 
     * @param index
     *            the index
     * @return the value
     */
    public final V get(int index) {
        return getEntry(index).value;
    }

    /**
     * Returns the key for the index.
     * 
     * @param index
     *            the index
     * @return the key
     */
    public final K getKey(int index) {
        return getEntry(index).key;
    }

    /**
     * Returns the entry for the index.
     * 
     * @param index
     *            the index
     * @return the entry
     */
    public final Entry<K, V> getEntry(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException("Index:"
                + index
                + ", Size:"
                + size);
        }
        return listTable[index];
    }

    @Override
    public V put(K key, V value) {
        int hashCode = 0;
        int index = 0;

        if (key != null) {
            hashCode = key.hashCode();
            index = (hashCode & 0x7FFFFFFF) % mapTable.length;
            for (Entry<K, V> e = mapTable[index]; e != null; e = e.next) {
                if ((e.hashCode == hashCode) && key.equals(e.key)) {
                    return swapValue(e, value);
                }
            }
        } else {
            for (Entry<K, V> e = mapTable[0]; e != null; e = e.next) {
                if (e.key == null) {
                    return swapValue(e, value);
                }
            }
        }
        ensureCapacity();
        index = (hashCode & 0x7FFFFFFF) % mapTable.length;
        Entry<K, V> e = new Entry<K, V>(hashCode, key, value, mapTable[index]);
        mapTable[index] = e;
        listTable[size++] = e;
        return null;
    }

    /**
     * Sets the value for the index.
     * 
     * @param index
     *            the index
     * @param value
     *            the value
     */
    public final void set(int index, V value) {
        getEntry(index).setValue(value);
    }

    @Override
    public V remove(Object key) {
        Entry<K, V> e = removeMap(key);
        if (e != null) {
            V value = e.value;
            removeList(indexOf(e));
            e.clear();
            return value;
        }
        return null;
    }

    /**
     * Removes the entry for the index.
     * 
     * @param index
     *            the index
     * @return the removed value
     */
    public final V remove(int index) {
        Entry<K, V> e = removeList(index);
        V value = e.value;
        removeMap(e.key);
        e.value = null;
        return value;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        for (Iterator<? extends Map.Entry<? extends K, ? extends V>> i =
            map.entrySet().iterator(); i.hasNext();) {
            Map.Entry<? extends K, ? extends V> e = i.next();
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public final void clear() {
        for (int i = 0; i < mapTable.length; i++) {
            mapTable[i] = null;
        }
        for (int i = 0; i < listTable.length; i++) {
            listTable[i] = null;
        }
        size = 0;
    }

    /**
     * Converts this object to an array.
     * 
     * @return an array
     */
    public final Object[] toArray() {
        Object[] array = new Object[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = get(i);
        }
        return array;
    }

    /**
     * Converts this object to an array.
     * 
     * @param proto
     *            the prototype
     * @return an array
     */
    public final Object[] toArray(final Object proto[]) {
        Object[] array = proto;
        if (proto.length < size) {
            array =
                (Object[]) Array.newInstance(proto
                    .getClass()
                    .getComponentType(), size);
        }
        for (int i = 0; i < array.length; i++) {
            array[i] = get(i);
        }
        if (array.length > size) {
            array[size] = null;
        }
        return array;
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final boolean equals(Object o) {
        if (!getClass().isInstance(o)) {
            return false;
        }
        ArrayMap<K, V> e = (ArrayMap) o;
        if (size != e.size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!listTable[i].equals(e.listTable[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public final Set<Map.Entry<K, V>> entrySet() {
        if (entrySet == null) {
            entrySet = new AbstractSet<Map.Entry<K, V>>() {
                @Override
                public Iterator<Map.Entry<K, V>> iterator() {
                    return new ArrayMapIterator();
                }

                @Override
                @SuppressWarnings("unchecked")
                public boolean contains(Object o) {
                    if (!(o instanceof Entry)) {
                        return false;
                    }
                    Entry<K, V> entry = (Entry<K, V>) o;
                    int index = (entry.hashCode & 0x7FFFFFFF) % mapTable.length;
                    for (Entry<K, V> e = mapTable[index]; e != null; e = e.next) {
                        if (e.equals(entry)) {
                            return true;
                        }
                    }
                    return false;
                }

                @Override
                @SuppressWarnings({ "unchecked", "rawtypes" })
                public boolean remove(Object o) {
                    if (!(o instanceof Entry)) {
                        return false;
                    }
                    Entry<K, V> entry = (Entry) o;
                    return ArrayMap.this.remove(entry.key) != null;
                }

                @Override
                public int size() {
                    return size;
                }

                @Override
                public void clear() {
                    ArrayMap.this.clear();
                }
            };
        }
        return entrySet;
    }

    public final void writeExternal(final ObjectOutput out) throws IOException {
        out.writeInt(listTable.length);
        out.writeInt(size);
        for (int i = 0; i < size; i++) {
            out.writeObject(listTable[i].key);
            out.writeObject(listTable[i].value);
        }
    }

    @SuppressWarnings("unchecked")
    public final void readExternal(final ObjectInput in) throws IOException,
            ClassNotFoundException {

        int num = in.readInt();
        mapTable = createArray(num);
        listTable = createArray(num);
        threshold = (int) (num * LOAD_FACTOR);
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            K key = (K) in.readObject();
            V value = (V) in.readObject();
            put(key, value);
        }
    }

    @Override
    public Object clone() {
        ArrayMap<K, V> copy = new ArrayMap<K, V>();
        copy.threshold = threshold;
        copy.mapTable = mapTable;
        copy.listTable = listTable;
        copy.size = size;
        return copy;
    }

    private final int indexOf(final Entry<K, V> entry) {
        for (int i = 0; i < size; i++) {
            if (listTable[i] == entry) {
                return i;
            }
        }
        return -1;
    }

    private final Entry<K, V> removeMap(Object key) {
        int hashCode = 0;
        int index = 0;

        if (key != null) {
            hashCode = key.hashCode();
            index = (hashCode & 0x7FFFFFFF) % mapTable.length;
            for (Entry<K, V> e = mapTable[index], prev = null; e != null; prev =
                e, e = e.next) {
                if ((e.hashCode == hashCode) && key.equals(e.key)) {
                    if (prev != null) {
                        prev.next = e.next;
                    } else {
                        mapTable[index] = e.next;
                    }
                    return e;
                }
            }
        } else {
            for (Entry<K, V> e = mapTable[index], prev = null; e != null; prev =
                e, e = e.next) {
                if ((e.hashCode == hashCode) && e.key == null) {
                    if (prev != null) {
                        prev.next = e.next;
                    } else {
                        mapTable[index] = e.next;
                    }
                    return e;
                }
            }
        }
        return null;
    }

    private final Entry<K, V> removeList(int index) {
        Entry<K, V> e = listTable[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(listTable, index + 1, listTable, index, numMoved);
        }
        listTable[--size] = null;
        return e;
    }

    private final void ensureCapacity() {
        if (size >= threshold) {
            Entry<K, V>[] oldTable = listTable;
            int newCapacity = oldTable.length * 2 + 1;
            Entry<K, V>[] newMapTable = createArray(newCapacity);
            Entry<K, V>[] newListTable = createArray(newCapacity);
            threshold = (int) (newCapacity * LOAD_FACTOR);
            System.arraycopy(oldTable, 0, newListTable, 0, size);
            for (int i = 0; i < size; i++) {
                Entry<K, V> old = oldTable[i];
                int index = (old.hashCode & 0x7FFFFFFF) % newCapacity;
                Entry<K, V> e = old;
                old = old.next;
                e.next = newMapTable[index];
                newMapTable[index] = e;
            }
            mapTable = newMapTable;
            listTable = newListTable;
        }
    }

    private final V swapValue(final Entry<K, V> entry, final V value) {
        V old = entry.value;
        entry.value = value;
        return old;
    }

    @SuppressWarnings("unchecked")
    private Entry<K, V>[] createArray(final int length) {
        return new Entry[length];
    }

    private class ArrayMapIterator implements Iterator<Map.Entry<K, V>> {

        private int current = 0;

        private int last = -1;

        public boolean hasNext() {
            return current != size;
        }

        public Map.Entry<K, V> next() {
            try {
                Entry<K, V> n = listTable[current];
                last = current++;
                return n;
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }

        public void remove() {
            if (last == -1) {
                throw new IllegalStateException();
            }
            ArrayMap.this.remove(last);
            if (last < current) {
                current--;
            }
            last = -1;
        }
    }

    private static class Entry<K, V> implements Map.Entry<K, V>, Externalizable {

        private static final long serialVersionUID = 1L;

        transient int hashCode;

        transient K key;

        transient V value;

        transient Entry<K, V> next;

        /**
         * Constructor.
         * 
         * @param hashCode
         *            the hashCode
         * @param key
         *            the key
         * @param value
         *            the value
         * @param next
         *            the next
         */
        public Entry(final int hashCode, final K key, final V value,
                final Entry<K, V> next) {

            this.hashCode = hashCode;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(final V value) {
            V oldValue = value;
            this.value = value;
            return oldValue;
        }

        /**
         * Clears contents.
         */
        public void clear() {
            key = null;
            value = null;
            next = null;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            Entry<?, ?> e = (Entry<?, ?>) o;
            return (key != null ? key.equals(e.key) : e.key == null)
                && (value != null ? value.equals(e.value) : e.value == null);
        }

        @Override
        public int hashCode() {
            return hashCode;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }

        public void writeExternal(final ObjectOutput s) throws IOException {
            s.writeInt(hashCode);
            s.writeObject(key);
            s.writeObject(value);
            s.writeObject(next);
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public void readExternal(final ObjectInput s) throws IOException,
                ClassNotFoundException {

            hashCode = s.readInt();
            key = (K) s.readObject();
            value = (V) s.readObject();
            next = (Entry) s.readObject();
        }
    }
}