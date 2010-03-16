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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author higa
 * 
 */
public class ArrayMapTest {

    private ArrayMap<String, String> map;

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        map = new ArrayMap<String, String>();
        map.put(null, null);
        map.put("1", "test");
        map.put("2", "test2");
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        map = null;
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void size() throws Exception {
        assertThat(map.size(), is(3));
        map.put("3", "test3");
        assertThat(map.size(), is(4));
    }

    /**
     * 
     * 
     * @throws Exception
     */
    @Test
    public void isEmpty() throws Exception {
        assertThat(map.isEmpty(), is(false));
        map.clear();
        assertThat(map.isEmpty(), is(true));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void containsValue() throws Exception {
        assertThat(map.containsValue("test2"), is(true));
        assertThat(map.containsValue("test3"), is(false));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void containsKey() throws Exception {
        assertThat(map.containsKey("2"), is(true));
        assertThat(map.containsKey("3"), is(false));
        map.put("3", null);
        assertThat(map.containsKey("3"), is(true));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void indexOf() throws Exception {
        assertThat(map.indexOf("test"), is(1));
        assertThat(map.indexOf(null), is(0));
        assertThat(map.indexOf("test3"), is(-1));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void get() throws Exception {
        assertThat(map.get("1"), is("test"));
        assertThat(map.get(null), is(nullValue()));
        assertThat(map.get("test3"), is(nullValue()));
        assertThat(map.get(0), is(nullValue()));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void put() throws Exception {
        assertThat(map.put("1", "test3"), is("test"));
        assertThat(map.get("1"), is("test3"));
        assertThat(map.get(1), is("test3"));
        map.put(null, "test4");
        map.put(null, "test5");
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void remove() throws Exception {
        assertThat(map.remove("1"), is("test"));
        assertThat(map.size(), is(2));
        assertThat(map.remove("dummy"), is(nullValue()));
        assertThat(map.remove(0), is(nullValue()));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void remove2() throws Exception {
        Map<String, String> m = new ArrayMap<String, String>();
        m.put("1", "d");
        m.remove("1");
        assertThat(m.containsKey("1"), is(false));
        m.put("1", "d");
        m.remove("1");
        assertThat(m.containsKey("1"), is(false));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void remove3() throws Exception {
        Map<MyKey, String> m = new ArrayMap<MyKey, String>();
        m.put(new MyKey("1"), "d");
        m.put(new MyKey("2"), "d");
        m.remove(new MyKey("1"));
        assertThat(m.containsKey(new MyKey("1")), is(false));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void remove4() throws Exception {
        ArrayMap<String, String> m = new ArrayMap<String, String>();
        m.put("1", "d");
        m.put("2", "d");
        m.remove("2");
        assertThat(m.containsKey("2"), is(false));
        assertThat(m.containsKey("1"), is(true));
        assertThat(m.get("1"), is("d"));
        assertThat(m.get("2"), is(nullValue()));
        assertThat(m.get(0), is("d"));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void putAll() throws Exception {
        Map<String, String> m = new HashMap<String, String>();
        m.put("3", "test3");
        m.put("4", "test4");
        map.putAll(m);
        assertThat(map.get("3"), is("test3"));
        assertThat(map.get("4"), is("test4"));
        assertThat(map.size(), is(5));
    }

    /**
     * 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void equals() throws Exception {
        Map<String, String> copy = (ArrayMap<String, String>) map.clone();
        assertThat(map.equals(copy), is(true));
        assertThat(map.equals(null), is(false));
        map.put("3", "test3");
        assertThat(map.equals(copy), is(false));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testToString() throws Exception {
        assertThat(map.toString(), is(notNullValue()));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void clear() throws Exception {
        map.clear();
        assertThat(map.size(), is(0));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void entrySet() throws Exception {
        Iterator<Map.Entry<String, String>> i = map.entrySet().iterator();
        assertThat(i.next().getKey(), is(nullValue()));
        assertThat(i.next().getKey(), is("1"));
        assertThat(i.next().getKey(), is("2"));
    }

    private static class MyKey {
        Object _key;

        MyKey(Object key) {
            _key = key;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o == null || !(o instanceof MyKey)) {
                return false;
            }
            return _key.equals(((MyKey) o)._key);
        }
    }
}