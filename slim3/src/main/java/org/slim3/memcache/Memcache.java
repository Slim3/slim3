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
package org.slim3.memcache;

import java.util.Map;
import java.util.Set;

import org.slim3.util.ClassUtil;

import com.google.appengine.api.memcache.ConsistentErrorHandler;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.InvalidValueException;
import com.google.appengine.api.memcache.Stats;
import com.google.appengine.api.memcache.MemcacheService.SetPolicy;
import com.google.apphosting.api.ApiProxy.CapabilityDisabledException;

/**
 * A class to access memcache service.
 * 
 * @author higa
 * @since 1.0.0
 * 
 */
public final class Memcache {

    private static Class<? extends MemcacheDelegate> delegateClass =
        MemcacheDelegate.class;

    /**
     * Empties the cache of all values. Statistics are not affected. Note that
     * clearAll() does not respect namespaces - this flushes the cache for every
     * namespace.
     * 
     * @throws CapabilityDisabledException
     *             if App Engine is read only
     */
    public static void cleanAll() throws CapabilityDisabledException {
        delegate().cleanAll();
    }

    /**
     * Tests whether a given value is in cache, even if its value is null.
     * 
     * @param key
     *            the key
     * @return whether a given value is in cache
     * @throws IllegalArgumentException
     *             if the key cannot be serialized
     */
    public static boolean contains(Object key) throws IllegalArgumentException {
        return delegate().contains(key);
    }

    /**
     * Removes key from the cache.
     * 
     * @param key
     *            the key
     * @return true if an entry existed to delete
     * @throws IllegalArgumentException
     *             if the key cannot be serialized
     * @throws CapabilityDisabledException
     *             if App Engine is read only
     */
    public static boolean delete(Object key) throws IllegalArgumentException,
            CapabilityDisabledException {
        return delegate().delete(key);
    }

    /**
     * Removes the given key from the cache, and prevents it from being added
     * under the MemcacheService.SetPolicy.ADD_ONLY_IF_NOT_PRESENT policy for
     * millisNoReAdd milliseconds thereafter. Calls to a put(java.lang.Object,
     * java.lang.Object, com.google.appengine.api.memcache.Expiration,
     * com.google.appengine.api.memcache.MemcacheService.SetPolicy) method using
     * MemcacheService.SetPolicy.SET_ALWAYS are not blocked, however.
     * 
     * @param key
     *            the key
     * @param millisNoReAdd
     *            the time during which calls to put using ADD_IF_NOT_PRESENT
     *            should be denied
     * @return true if an entry existed to delete
     * @throws IllegalArgumentException
     *             if the key cannot be serialized
     * @throws CapabilityDisabledException
     *             if App Engine is read only
     */
    public static boolean delete(Object key, long millisNoReAdd)
            throws IllegalArgumentException, CapabilityDisabledException {
        return delegate().delete(key, millisNoReAdd);
    }

    /**
     * Removes keys from the cache.
     * 
     * @param keys
     *            the keys for entries to delete.
     * @return the Set of keys deleted. Any keys in keys but not in the returned
     *         set were not found in the cache.
     * @throws NullPointerException
     *             the keys parameter is null
     * @throws IllegalArgumentException
     *             if the key cannot be serialized
     * @throws CapabilityDisabledException
     *             if App Engine is read only
     * 
     */
    public static Set<Object> deleteAll(Iterable<?> keys)
            throws NullPointerException, IllegalArgumentException,
            CapabilityDisabledException {
        return delegate().deleteAll(keys);
    }

    /**
     * Removes keys from the cache.
     * 
     * @param keys
     *            the keys for entries to delete.
     * @param millisNoReAdd
     *            the time during which calls to put using ADD_IF_NOT_PRESENT
     *            should be denied
     * @return the Set of keys deleted. Any keys in keys but not in the returned
     *         set were not found in the cache.
     * @throws NullPointerException
     *             the keys parameter is null
     * @throws IllegalArgumentException
     *             if the key cannot be serialized
     * @throws CapabilityDisabledException
     *             if App Engine is read only
     * 
     */
    public static Set<Object> deleteAll(Iterable<?> keys, long millisNoReAdd)
            throws NullPointerException, IllegalArgumentException,
            CapabilityDisabledException {
        return delegate().deleteAll(keys, millisNoReAdd);
    }

    /**
     * Returns a previously-stored value, or null if unset. Since null might be
     * the set value in some cases, so we also have contains(Object) which
     * returns boolean.
     * 
     * @param <T>
     *            the return type
     * 
     * @param key
     *            the key
     * @return a previously-stored value
     * @throws IllegalArgumentException
     *             if the key cannot be serialized
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(Object key) throws IllegalArgumentException {
        return (T) delegate().get(key);
    }

    /**
     * Performs a get of multiple keys at once. This is more efficient than
     * multiple separate calls to get(Object), and allows a single call to both
     * test for contains(Object) and also fetch the value, because the return
     * will not include mappings for keys not found.
     * 
     * @param keys
     *            the keys.
     * @return a mapping from keys to values of any entries found. If a
     *         requested key is not found in the cache, the key will not be in
     *         the returned Map.
     * @throws NullPointerException
     *             if the keys parameter is null
     * @throws IllegalArgumentException
     *             if the key cannot be serialized
     */
    public static Map<Object, Object> getAll(Iterable<?> keys)
            throws NullPointerException, IllegalArgumentException {
        return delegate().getAll(keys);
    }

    /**
     * Atomically fetches, increments, and stores a given integral value.
     * "Integral" types are Byte, Short, Integer, Long, and in some cases String
     * (if the string is parseable as a number, for example via
     * Long.parseLong(String). The entry must already exist, and have a
     * non-negative value.
     * 
     * Incrementing by positive amounts will reach signed 64-bit max (2^63 - 1)
     * and then wrap-around to signed 64-bit min (-2^63), continuing increments
     * from that point.
     * 
     * To facilitate use as an atomic countdown, incrementing by a negative
     * value (i.e. decrementing) will not go below zero: incrementing 2 by -5
     * will return 0, not -3. However, due to the way numbers are stored,
     * decrementing -3 by -5 will result in -8; so the zero-floor rule only
     * applies to decrementing numbers that were positive.
     * 
     * Note: The actual representation of all numbers in Memcache is a string.
     * This means if you initially stored a number as a string (e.g., "10") and
     * then increment it, everything will work properly, including wrapping
     * beyond signed 64-bit int max. However, if you get the key past the point
     * of wrapping, you will receive an unsigned integer value, not a signed
     * integer value.
     * 
     * @param key
     *            the key
     * @param delta
     *            the size of the increment, positive or negative.
     * @return the post-increment value, as a long. However, a get(Object) of
     *         the key will still have the original type (Byte, Short, etc.). If
     *         there is no entry for key, returns null.
     * @throws IllegalArgumentException
     *             if the key cannot be serialized
     * @throws InvalidValueException
     *             if the object incremented is not of a integral type
     * @throws CapabilityDisabledException
     *             if App Engine is read only
     */
    public static Long increment(Object key, long delta)
            throws IllegalArgumentException, InvalidValueException,
            CapabilityDisabledException {
        return delegate().increment(key, delta);
    }

    /**
     * Like normal increment, but allows for an optional initial value for the
     * key to take on if not already present in the cache.
     * 
     * @param key
     *            the key
     * @param delta
     *            the size of the increment, positive or negative.
     * @param initialValue
     *            the value to insert into the cache if the key is not present
     * @return the post-increment value, as a long. However, a get(Object) of
     *         the key will still have the original type (Byte, Short, etc.). If
     *         there is no entry for key, returns null.
     * @throws IllegalArgumentException
     *             if the key cannot be serialized
     * @throws InvalidValueException
     *             if the object incremented is not of a integral type
     * @throws CapabilityDisabledException
     *             if App Engine is read only
     */
    public static Long increment(Object key, long delta, long initialValue)
            throws IllegalArgumentException, InvalidValueException,
            CapabilityDisabledException {
        return delegate().increment(key, delta, initialValue);
    }

    /**
     * Like normal increment, but increments a batch of separate keys in
     * parallel by the same delta.
     * 
     * @param keys
     *            the keys
     * @param delta
     *            the size of the increment, positive or negative.
     * @return mapping keys to their new values; values will be null if they
     *         could not be incremented or were not present in the cache
     * @throws IllegalArgumentException
     *             if the key cannot be serialized
     * @throws CapabilityDisabledException
     *             if App Engine is read only
     */
    public static Map<Object, Long> incrementAll(Iterable<?> keys, long delta)
            throws IllegalArgumentException, CapabilityDisabledException {
        return delegate().incrementAll(keys, delta);
    }

    /**
     * Like normal increment, but increments a batch of separate keys in
     * parallel by the same delta and potentially sets a starting value.
     * 
     * @param keys
     *            the keys
     * @param delta
     *            the size of the increment, positive or negative.
     * @param initialValue
     *            the value to insert into the cache if the key is not present
     * @return mapping keys to their new values; values will be null if they
     *         could not be incremented for whatever reason
     * @throws IllegalArgumentException
     *             if the key cannot be serialized
     * @throws CapabilityDisabledException
     *             if App Engine is read only
     */
    public static Map<Object, Long> incrementAll(Iterable<?> keys, long delta,
            long initialValue) throws IllegalArgumentException,
            CapabilityDisabledException {
        return delegate().incrementAll(keys, delta, initialValue);
    }

    /**
     * Like normal increment, but accepts a mapping of separate controllable
     * offsets for each key individually.
     * 
     * @param offsets
     *            the offsets
     * 
     * @return mapping keys to their new values; values will be null if they
     *         could not be incremented for whatever reason
     * @throws IllegalArgumentException
     *             if the key cannot be serialized
     * @throws CapabilityDisabledException
     *             if App Engine is read only
     */
    public static Map<Object, Long> incrementAll(Map<Object, Long> offsets)
            throws IllegalArgumentException, CapabilityDisabledException {
        return delegate().incrementAll(offsets);
    }

    /**
     * Like normal increment, but accepts a mapping of separate controllable
     * offsets for each key individually. Callers may also pass an initial value
     * for the keys to take on if they are not already present in the cache.
     * 
     * @param offsets
     *            the offsets
     * @param initialValue
     *            the value to insert into the cache if the key is not present
     * @return mapping keys to their new values; values will be null if they
     *         could not be incremented for whatever reason
     * @throws IllegalArgumentException
     *             if the key cannot be serialized
     * @throws CapabilityDisabledException
     *             if App Engine is read only
     */
    public static Map<Object, Long> incrementAll(Map<Object, Long> offsets,
            long initialValue) throws IllegalArgumentException,
            CapabilityDisabledException {
        return delegate().incrementAll(offsets, initialValue);
    }

    /**
     * A convenience shortcut, equivalent to put(key, value, null,
     * SetPolicy.SET_ALWAYS).
     * 
     * @param key
     *            the key
     * @param value
     *            the value
     * @throws IllegalArgumentException
     *             if the key cannot be serialized
     * @throws CapabilityDisabledException
     *             if App Engine is read only
     */
    public static void put(Object key, Object value)
            throws IllegalArgumentException, CapabilityDisabledException {
        delegate().put(key, value);
    }

    /**
     * A convenience shortcut, equivalent to put(key, value, expires,
     * SetPolicy.SET_ALWAYS).
     * 
     * @param key
     *            the key
     * @param value
     *            the value
     * @param expires
     *            expiration time for the new values, or null for no time-based
     *            expiration
     * @throws IllegalArgumentException
     *             if the key cannot be serialized
     * @throws CapabilityDisabledException
     *             if App Engine is read only
     */
    public static void put(Object key, Object value, Expiration expires)
            throws IllegalArgumentException, CapabilityDisabledException {
        delegate().put(key, value, expires);
    }

    /**
     * Store a new value into the cache, using key, but subject to the policy
     * regarding existing entries.
     * 
     * @param key
     *            the key
     * @param value
     *            the value
     * @param expires
     *            expiration time for the new values, or null for no time-based
     *            expiration
     * @param policy
     *            Requests particular handling regarding pre-existing entries
     *            under the same key. This parameter must not be null.
     * @return true if a new entry was created, false if not because of the
     *         policy.
     * @throws NullPointerException
     *             if the policy parameter is null
     * @throws IllegalArgumentException
     *             if the key cannot be serialized
     * @throws CapabilityDisabledException
     *             if App Engine is read only
     */
    public static boolean put(Object key, Object value, Expiration expires,
            SetPolicy policy) throws NullPointerException,
            IllegalArgumentException, CapabilityDisabledException {
        return delegate().put(key, value, expires, policy);
    }

    /**
     * A convenience shortcut, equivalent to putAll(values, null,
     * SetPolicy.SET_ALWAYS).
     * 
     * @param values
     *            key/value mappings for new entries to add to the cache
     * @throws NullPointerException
     *             if the values parameter is null
     * @throws IllegalArgumentException
     *             if the key cannot be serialized
     * @throws CapabilityDisabledException
     *             if App Engine is read only
     */
    public static void putAll(Map<Object, Object> values)
            throws NullPointerException, IllegalArgumentException,
            CapabilityDisabledException {
        delegate().putAll(values);
    }

    /**
     * A convenience shortcut, equivalent to putAll(values, expires,
     * SetPolicy.SET_ALWAYS).
     * 
     * @param values
     *            key/value mappings for new entries to add to the cache
     * @param expires
     *            expiration time for the new values, or null for no time-based
     *            expiration
     * @throws NullPointerException
     *             if the values parameter is null
     * @throws IllegalArgumentException
     *             if the key cannot be serialized
     * @throws CapabilityDisabledException
     *             if App Engine is read only
     */
    public static void putAll(Map<Object, Object> values, Expiration expires)
            throws NullPointerException, IllegalArgumentException,
            CapabilityDisabledException {
        delegate().putAll(values, expires);
    }

    /**
     * A batch-processing variant of put(java.lang.Object, java.lang.Object,
     * com.google.appengine.api.memcache.Expiration,
     * com.google.appengine.api.memcache.MemcacheService.SetPolicy). This is
     * more efficiently implemented by the service than multiple calls.
     * 
     * @param values
     *            key/value mappings for new entries to add to the cache
     * @param expires
     *            expiration time for the new values, or null for no time-based
     *            expiration
     * @param policy
     *            what to do if the entry is or is not already present
     * @return the set of keys for which entries were created. Keys in values
     *         may not be in the returned set because of the policy regarding
     *         pre-existing entries.
     * @throws NullPointerException
     *             if the values parameter is null or if the policy parameter is
     *             null
     * @throws IllegalArgumentException
     *             if the key cannot be serialized
     * @throws CapabilityDisabledException
     *             if App Engine is read only
     */
    public static Set<Object> putAll(Map<Object, Object> values,
            Expiration expires, SetPolicy policy) throws NullPointerException,
            IllegalArgumentException, CapabilityDisabledException {
        return delegate().putAll(values, expires, policy);
    }

    /**
     * Sets the current error handler.
     * 
     * @param errorHandler
     *            the error handler
     * @return {@link MemcacheDelegate}
     * @throws NullPointerException
     *             if the errorHandler parameter is null
     */
    public static MemcacheDelegate errorHandler(ConsistentErrorHandler errorHandler)
            throws NullPointerException {
        return delegate().errorHandler(errorHandler);
    }

    /**
     * Returns some statistics about the cache and its usage. Note that
     * statistics() does not respect namespaces - this will return stats for
     * every namespace. The response will never be null.
     * 
     * @return some statistics
     */
    public static Stats statistics() {
        return delegate().statistics();
    }

    /**
     * Returns the delegate class.
     * 
     * @return the delegate class
     */
    public static Class<? extends MemcacheDelegate> delegateClass() {
        return delegateClass;
    }

    /**
     * Sets the delegate class and returns the old one.
     * 
     * @param clazz
     *            the delegate class
     * @return the old delegate class
     * @throws NullPointerException
     *             if the clazz parameter is null
     */
    public static Class<? extends MemcacheDelegate> delegateClass(
            Class<? extends MemcacheDelegate> clazz)
            throws NullPointerException {
        if (clazz == null) {
            throw new NullPointerException(
                "The clazz parameter must not be null.");
        }
        Class<? extends MemcacheDelegate> old = delegateClass;
        delegateClass = clazz;
        return old;
    }

    /**
     * Creates a {@link MemcacheDelegate}.
     * 
     * @return a {@link MemcacheDelegate}
     */
    protected static MemcacheDelegate delegate() {
        return ClassUtil.newInstance(delegateClass);
    }

    private Memcache() {
    }
}