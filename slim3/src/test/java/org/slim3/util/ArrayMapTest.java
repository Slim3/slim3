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
package org.slim3.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slim3.util.ArrayMap;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class ArrayMapTest extends TestCase {

    private ArrayMap<String, String> map;

    @Override
    protected void setUp() throws Exception {
        map = new ArrayMap<String, String>();
        map.put(null, null);
        map.put("1", "test");
        map.put("2", "test2");
    }

    @Override
    protected void tearDown() throws Exception {
        map = null;
    }

    /**
     * 
     * @throws Exception
     */
    public void testSize() throws Exception {
        assertEquals(3, map.size());
        map.put("3", "test3");
        assertEquals(4, map.size());
    }

    /**
     * 
     * 
     * @throws Exception
     */
    public void testIsEmpty() throws Exception {
        assertTrue(!map.isEmpty());
        map.clear();
        assertTrue(map.isEmpty());
    }

    /**
     * 
     * @throws Exception
     */
    public void testContainsValue() throws Exception {
        assertTrue(map.containsValue("test2"));
        assertTrue(!map.containsValue("test3"));
    }

    /**
     * 
     * @throws Exception
     */
    public void testContainsKey() throws Exception {
        assertTrue(map.containsKey("2"));
        assertTrue(!map.containsKey("3"));
        map.put("3", null);
        assertTrue(map.containsKey("3"));
    }

    /**
     * 
     * @throws Exception
     */
    public void testIndexOf() throws Exception {
        assertEquals(1, map.indexOf("test"));
        assertEquals(0, map.indexOf(null));
        assertEquals(-1, map.indexOf("test3"));
    }

    /**
     * 
     * @throws Exception
     */
    public void testGet() throws Exception {
        assertEquals("test", map.get("1"));
        assertNull(map.get(null));
        assertNull(map.get("test3"));
        assertNull(map.get(0));
    }

    /**
     * 
     * @throws Exception
     */
    public void testPut() throws Exception {
        assertEquals("test", map.put("1", "test3"));
        assertEquals("test3", map.get("1"));
        assertEquals("test3", map.get(1));
        map.put(null, "test4");
        map.put(null, "test5");
    }

    /**
     * 
     * @throws Exception
     */
    public void testRemove() throws Exception {
        assertEquals("test", map.remove("1"));
        assertEquals(2, map.size());
        assertEquals(null, map.remove("dummy"));
        assertEquals(null, map.remove(0));
    }

    /**
     * 
     * @throws Exception
     */
    public void testRemove2() throws Exception {
        Map<String, String> m = new ArrayMap<String, String>();
        m.put("1", "d");
        m.remove("1");
        assertFalse(m.containsKey("1"));
        m.put("1", "d");
        m.remove("1");
        assertFalse(m.containsKey("1"));
    }

    /**
     * 
     * @throws Exception
     */
    public void testRemove3() throws Exception {
        Map<MyKey, String> m = new ArrayMap<MyKey, String>();
        m.put(new MyKey("1"), "d");
        m.put(new MyKey("2"), "d");
        m.remove(new MyKey("1"));
        assertFalse(m.containsKey(new MyKey("1")));
    }

    /**
     * 
     * @throws Exception
     */
    public void testRemove4() throws Exception {
        ArrayMap<String, String> m = new ArrayMap<String, String>();
        m.put("1", "d");
        m.put("2", "d");
        System.out.println("remove before:" + m);
        m.remove("2");
        System.out.println("remove after:" + m);
        assertFalse(m.containsKey("2"));
        assertTrue(m.containsKey("1"));
        assertEquals("d", m.get("1"));
        assertNull(null, m.get("2"));
        assertEquals("d", m.get(0));
    }

    /**
     * 
     * @throws Exception
     */
    public void testPutAll() throws Exception {
        Map<String, String> m = new HashMap<String, String>();
        m.put("3", "test3");
        m.put("4", "test4");
        map.putAll(m);
        assertEquals("test3", map.get("3"));
        assertEquals("test4", map.get("4"));
        assertEquals(5, map.size());
    }

    /**
     * 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void testEqaulas() throws Exception {
        Map<String, String> copy = (ArrayMap<String, String>) map.clone();
        assertTrue(map.equals(copy));
        assertFalse(map.equals(null));
        map.put("3", "test3");
        assertFalse(map.equals(copy));
    }

    /**
     * 
     * @throws Exception
     */
    public void testToString() throws Exception {
        assertNotNull(map.toString());
    }

    /**
     * 
     * @throws Exception
     */
    public void testClear() throws Exception {
        map.clear();
        assertEquals(0, map.size());
    }

    /**
     * 
     * @throws Exception
     */
    public void testEntrySet() throws Exception {
        Iterator<Map.Entry<String, String>> i = map.entrySet().iterator();
        assertNull(i.next().getKey());
        assertEquals("1", i.next().getKey());
        assertEquals("2", i.next().getKey());
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