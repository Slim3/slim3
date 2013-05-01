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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.memcache.ConsistentErrorHandler;
import com.google.appengine.api.memcache.ConsistentLogAndContinueErrorHandler;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheService.SetPolicy;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

/**
 * @author higa
 * 
 */
public class MemcacheTest extends AppEngineTestCase {

    private MemcacheService ms;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        ms = MemcacheServiceFactory.getMemcacheService();
    }

    @Override
    public void tearDown() throws Exception {
        Memcache.delegateClass(MemcacheDelegate.class);
        super.tearDown();
    }

    /**
     * @throws Exception
     */
    @Test
    public void clearAll() throws Exception {
        ms.put("aaa", 1);
        Memcache.cleanAll();
        assertThat(ms.contains("aaa"), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void contains() throws Exception {
        assertThat(Memcache.contains("aaa"), is(false));
        ms.put("aaa", 1);
        assertThat(Memcache.contains("aaa"), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void delete() throws Exception {
        assertThat(Memcache.delete("aaa"), is(false));
        ms.put("aaa", 1);
        assertThat(Memcache.delete("aaa"), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteUsingMillisNoReAdd() throws Exception {
        assertThat(Memcache.delete("aaa", 1000), is(false));
        ms.put("aaa", 1);
        assertThat(Memcache.delete("aaa", 1000), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteAll() throws Exception {
        Iterable<?> keys = Arrays.asList("aaa");
        Set<Object> ret = Memcache.deleteAll(keys);
        assertThat(ret, is(notNullValue()));
        assertThat(ret.size(), is(0));
        ms.put("aaa", "111");
        ret = Memcache.deleteAll(keys);
        assertThat(ret.size(), is(1));
        assertThat((String) ret.iterator().next(), is("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteAllUsingMillisNoReAdd() throws Exception {
        Iterable<?> keys = Arrays.asList("aaa");
        Set<Object> ret = Memcache.deleteAll(keys, 1000);
        assertThat(ret, is(notNullValue()));
        assertThat(ret.size(), is(0));
        ms.put("aaa", "111");
        ret = Memcache.deleteAll(keys, 1000);
        assertThat(ret.size(), is(1));
        assertThat((String) ret.iterator().next(), is("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void get() throws Exception {
        assertThat(Memcache.get(null), is(nullValue()));
        assertThat(Memcache.get("aaa"), is(nullValue()));
        ms.put("aaa", 1);
        assertThat((Integer) Memcache.get("aaa"), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getAll() throws Exception {
        assertThat(Memcache.getAll(Arrays.asList("aaa")).isEmpty(), is(true));
        ms.put("aaa", 1);
        Map<?, ?> map = Memcache.getAll(Arrays.asList("aaa"));
        assertThat(map.size(), is(1));
        assertThat((Integer) map.get("aaa"), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void increment() throws Exception {
        ms.put("aaa", 1);
        assertThat(Memcache.increment("aaa", 2), is(3L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void incrementUsingInitialValue() throws Exception {
        assertThat(Memcache.increment("aaa", 2, 1), is(3L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void incrementAll() throws Exception {
        ms.put("aaa", 1);
        ms.put("bbb", "bbb");
        Map<Object, Long> map =
            Memcache.incrementAll(Arrays.asList("aaa", "bbb", "ccc"), 2);
        assertThat(map.size(), is(3));
        assertThat(map.get("aaa"), is(3L));
        assertThat(map.get("bbb"), is(nullValue()));
        assertThat(map.get("ccc"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void incrementAllUsingInitialValue() throws Exception {
        ms.put("bbb", "bbb");
        Map<Object, Long> map =
            Memcache.incrementAll(Arrays.asList("aaa", "bbb"), 2, 1);
        assertThat(map.size(), is(2));
        assertThat(map.get("aaa"), is(3L));
        assertThat(map.get("bbb"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void incrementAllUsingOffsets() throws Exception {
        ms.put("bbb", 2);
        Map<Object, Long> offsets = new HashMap<Object, Long>();
        offsets.put("aaa", 1L);
        offsets.put("bbb", 2L);
        Map<Object, Long> map = Memcache.incrementAll(offsets);
        assertThat(map.size(), is(2));
        assertThat(map.get("aaa"), is(nullValue()));
        assertThat(map.get("bbb"), is(4L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void incrementAllUsingOffsetsAndInitialValue() throws Exception {
        ms.put("bbb", 2);
        Map<Object, Long> offsets = new HashMap<Object, Long>();
        offsets.put("aaa", 1L);
        offsets.put("bbb", 2L);
        Map<Object, Long> map = Memcache.incrementAll(offsets, 1L);
        assertThat(map.size(), is(2));
        // the bug of local memcache service
        // assertThat(map.get("aaa"), is(2L));
        assertThat(map.get("bbb"), is(4L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void put() throws Exception {
        Memcache.put("aaa", 1);
        assertThat((Integer) ms.get("aaa"), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putUsingExpires() throws Exception {
        Memcache.put("aaa", 1, null);
        assertThat((Integer) ms.get("aaa"), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putUsingExpiresAndPolicy() throws Exception {
        Memcache.put("aaa", 1, null, SetPolicy.SET_ALWAYS);
        assertThat((Integer) ms.get("aaa"), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putAll() throws Exception {
        Map<Object, Object> values = new HashMap<Object, Object>();
        values.put("aaa", 1L);
        Memcache.putAll(values);
        assertThat((Long) ms.get("aaa"), is(1L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putAllUsingExpires() throws Exception {
        Map<Object, Object> values = new HashMap<Object, Object>();
        values.put("aaa", 1L);
        Memcache.putAll(values, null);
        assertThat((Long) ms.get("aaa"), is(1L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putAllUsingExpiresAndPolicy() throws Exception {
        Map<Object, Object> values = new HashMap<Object, Object>();
        values.put("aaa", 1L);
        Set<Object> set = Memcache.putAll(values, null, SetPolicy.SET_ALWAYS);
        assertThat((Long) ms.get("aaa"), is(1L));
        assertThat(set.size(), is(1));
        assertThat((String) set.iterator().next(), is("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void errorHandler() throws Exception {
        ConsistentErrorHandler errorHandler =
            new ConsistentLogAndContinueErrorHandler(Level.WARNING);
        MemcacheDelegate cache = Memcache.errorHandler(errorHandler);
        assertThat(cache, is(notNullValue()));
        assertThat(cache.errorHandler(), is(errorHandler));
    }

    /**
     * @throws Exception
     */
    @Test
    public void statistics() throws Exception {
        assertThat(Memcache.statistics(), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void delegate() throws Exception {
        assertThat(Memcache.delegate(), is(MemcacheDelegate.class));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getDelegateClass() throws Exception {
        assertThat(Memcache.delegateClass(), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void setDelegateClass() throws Exception {
        assertThat(Memcache.delegateClass(MyDelegate.class), is(notNullValue()));
        assertThat(Memcache.delegateClass().getName(), is(MyDelegate.class
            .getName()));
    }

    private static class MyDelegate extends MemcacheDelegate {

    }
}