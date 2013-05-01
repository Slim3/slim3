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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.memcache.ConsistentErrorHandler;
import com.google.appengine.api.memcache.ConsistentLogAndContinueErrorHandler;
import com.google.appengine.api.memcache.MemcacheService.SetPolicy;

/**
 * @author higa
 * 
 */
public class MemcacheDelegateTest extends AppEngineTestCase {

    /**
     * @throws Exception
     */
    @Test
    public void clearAll() throws Exception {
        MemcacheDelegate cache = new MemcacheDelegate();
        cache.ms.put("aaa", 1);
        cache.cleanAll();
        assertThat(cache.ms.contains("aaa"), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void contains() throws Exception {
        MemcacheDelegate cache = new MemcacheDelegate();
        assertThat(cache.contains("aaa"), is(false));
        cache.ms.put("aaa", 1);
        assertThat(cache.contains("aaa"), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void delete() throws Exception {
        MemcacheDelegate cache = new MemcacheDelegate();
        assertThat(cache.delete("aaa"), is(false));
        cache.ms.put("aaa", 1);
        assertThat(cache.delete("aaa"), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteUsingMillisNoReAdd() throws Exception {
        MemcacheDelegate cache = new MemcacheDelegate();
        assertThat(cache.delete("aaa", 1000), is(false));
        cache.ms.put("aaa", 1);
        assertThat(cache.delete("aaa", 1000), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteAll() throws Exception {
        MemcacheDelegate cache = new MemcacheDelegate();
        Iterable<?> keys = Arrays.asList("aaa");
        Set<Object> ret = cache.deleteAll(keys);
        assertThat(ret, is(notNullValue()));
        assertThat(ret.size(), is(0));
        cache.ms.put("aaa", "111");
        ret = cache.deleteAll(keys);
        assertThat(ret.size(), is(1));
        assertThat((String) ret.iterator().next(), is("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteAllUsingMillisNoReAdd() throws Exception {
        MemcacheDelegate cache = new MemcacheDelegate();
        Iterable<?> keys = Arrays.asList("aaa");
        Set<Object> ret = cache.deleteAll(keys, 1000);
        assertThat(ret, is(notNullValue()));
        assertThat(ret.size(), is(0));
        cache.ms.put("aaa", "111");
        ret = cache.deleteAll(keys, 1000);
        assertThat(ret.size(), is(1));
        assertThat((String) ret.iterator().next(), is("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getInternal() throws Exception {
        MemcacheDelegate cache = new MemcacheDelegate();
        NamespaceManager.set("hoge");
        cache.put("aaa", "1");
        assertThat((String) cache.get("aaa"), is("1"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void get() throws Exception {
        MemcacheDelegate cache = new MemcacheDelegate();
        assertThat(cache.get(null), is(nullValue()));
        assertThat(cache.get("aaa"), is(nullValue()));
        cache.ms.put("aaa", 1);
        assertThat((Integer) cache.get("aaa"), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getAllInternal() throws Exception {
        MemcacheDelegate cache = new MemcacheDelegate();
        NamespaceManager.set("hoge");
        cache.put("aaa", "1");
        Map<?, ?> map = cache.getAll(Arrays.asList("aaa"));
        assertThat(map.size(), is(1));
        assertThat((String) cache.get("aaa"), is("1"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getAll() throws Exception {
        MemcacheDelegate cache = new MemcacheDelegate();
        assertThat(cache.getAll(Arrays.asList("aaa")).isEmpty(), is(true));
        cache.ms.put("aaa", 1);
        Map<?, ?> map = cache.getAll(Arrays.asList("aaa"));
        assertThat(map.size(), is(1));
        assertThat((Integer) map.get("aaa"), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getNamespace() throws Exception {
        MemcacheDelegate cache = new MemcacheDelegate();
        assertThat(cache.getNamespace(), is(""));
        NamespaceManager.set("hoge");
        assertThat(cache.getNamespace(), is("hoge"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void increment() throws Exception {
        MemcacheDelegate cache = new MemcacheDelegate();
        cache.ms.put("aaa", 1);
        assertThat(cache.increment("aaa", 2), is(3L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void incrementUsingInitialValue() throws Exception {
        MemcacheDelegate cache = new MemcacheDelegate();
        assertThat(cache.increment("aaa", 2, 1), is(3L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void incrementAll() throws Exception {
        MemcacheDelegate cache = new MemcacheDelegate();
        cache.ms.put("aaa", 1);
        cache.ms.put("bbb", "bbb");
        Map<Object, Long> map =
            cache.incrementAll(Arrays.asList("aaa", "bbb", "ccc"), 2);
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
        MemcacheDelegate cache = new MemcacheDelegate();
        cache.ms.put("bbb", "bbb");
        Map<Object, Long> map =
            cache.incrementAll(Arrays.asList("aaa", "bbb"), 2, 1);
        assertThat(map.size(), is(2));
        assertThat(map.get("aaa"), is(3L));
        assertThat(map.get("bbb"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void incrementAllUsingOffsets() throws Exception {
        MemcacheDelegate cache = new MemcacheDelegate();
        cache.ms.put("bbb", 2);
        Map<Object, Long> offsets = new HashMap<Object, Long>();
        offsets.put("aaa", 1L);
        offsets.put("bbb", 2L);
        Map<Object, Long> map = cache.incrementAll(offsets);
        assertThat(map.size(), is(2));
        assertThat(map.get("aaa"), is(nullValue()));
        assertThat(map.get("bbb"), is(4L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void incrementAllUsingOffsetsAndInitialValue() throws Exception {
        MemcacheDelegate cache = new MemcacheDelegate();
        cache.ms.put("bbb", 2);
        Map<Object, Long> offsets = new HashMap<Object, Long>();
        offsets.put("aaa", 1L);
        offsets.put("bbb", 2L);
        Map<Object, Long> map = cache.incrementAll(offsets, 1L);
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
        MemcacheDelegate cache = new MemcacheDelegate();
        cache.put("aaa", 1);
        assertThat((Integer) cache.ms.get("aaa"), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putUsingExpires() throws Exception {
        MemcacheDelegate cache = new MemcacheDelegate();
        cache.put("aaa", 1, null);
        assertThat((Integer) cache.ms.get("aaa"), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putUsingExpiresAndPolicy() throws Exception {
        MemcacheDelegate cache = new MemcacheDelegate();
        cache.put("aaa", 1, null, SetPolicy.SET_ALWAYS);
        assertThat((Integer) cache.ms.get("aaa"), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putAll() throws Exception {
        MemcacheDelegate cache = new MemcacheDelegate();
        Map<Object, Object> values = new HashMap<Object, Object>();
        values.put("aaa", 1L);
        cache.putAll(values);
        assertThat((Long) cache.ms.get("aaa"), is(1L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putAllUsingExpires() throws Exception {
        MemcacheDelegate cache = new MemcacheDelegate();
        Map<Object, Object> values = new HashMap<Object, Object>();
        values.put("aaa", 1L);
        cache.putAll(values, null);
        assertThat((Long) cache.ms.get("aaa"), is(1L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putAllUsingExpiresAndPolicy() throws Exception {
        MemcacheDelegate cache = new MemcacheDelegate();
        Map<Object, Object> values = new HashMap<Object, Object>();
        values.put("aaa", 1L);
        Set<Object> set = cache.putAll(values, null, SetPolicy.SET_ALWAYS);
        assertThat((Long) cache.ms.get("aaa"), is(1L));
        assertThat(set.size(), is(1));
        assertThat((String) set.iterator().next(), is("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getErrorHandler() throws Exception {
        MemcacheDelegate cache = new MemcacheDelegate();
        assertThat(cache.errorHandler(), is(cache.ms.getErrorHandler()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void setErrorHandler() throws Exception {
        MemcacheDelegate cache = new MemcacheDelegate();
        ConsistentErrorHandler errorHandler =
            new ConsistentLogAndContinueErrorHandler(Level.WARNING);
        assertThat(cache.errorHandler(errorHandler), is(cache));
        assertThat(cache.errorHandler(), is(errorHandler));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getStatistics() throws Exception {
        MemcacheDelegate cache = new MemcacheDelegate();
        assertThat(cache.statistics(), is(notNullValue()));
        cache.ms.put("aaa", 1);
        cache.ms.get("aaa");
        cache.ms.get("bbb");
    }

    /**
     * @throws Exception
     */
    @Test
    public void toCollection() throws Exception {
        MemcacheDelegate cache = new MemcacheDelegate();
        Iterable<?> ite = Arrays.asList("aaa");
        Collection<?> col = cache.toCollection(ite);
        assertThat(col, is(notNullValue()));
        assertThat(col.size(), is(1));
    }
}