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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import junit.framework.TestCase;

import org.slim3.tester.MockHttpServletRequest;
import org.slim3.tester.MockServletContext;

/**
 * @author higa
 * 
 */
public class RequestMapTest extends TestCase {

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request =
        new MockHttpServletRequest(servletContext);

    private RequestMap map = new RequestMap(request);

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        request.setAttribute("aaa", "111");
        request.setAttribute("bbb", "222");
    }

    /**
     * @throws Exception
     */
    public void testClear() throws Exception {
        map.clear();
        assertEquals(0, map.size());
    }

    /**
     * @throws Exception
     */
    public void testContainsKey() throws Exception {
        assertTrue(map.containsKey("aaa"));
        assertTrue(map.containsKey("bbb"));
        assertFalse(map.containsKey("ccc"));
    }

    /**
     * @throws Exception
     */
    public void testContainsValue() throws Exception {
        assertTrue(map.containsValue("111"));
        assertTrue(map.containsValue("222"));
        assertFalse(map.containsValue("333"));
    }

    /**
     * @throws Exception
     */
    public void testEntrySet() throws Exception {
        Set<Entry<String, Object>> entrySet = map.entrySet();
        assertNotNull(entrySet);
        assertEquals(2, entrySet.size());
        Iterator<Entry<String, Object>> iterator = entrySet.iterator();
        Entry<String, Object> entry = iterator.next();
        assertEquals("aaa", entry.getKey());
        assertEquals("111", entry.getValue());
        entry = iterator.next();
        assertEquals("bbb", entry.getKey());
        assertEquals("222", entry.getValue());
        assertFalse(iterator.hasNext());
    }

    /**
     * @throws Exception
     */
    public void testGet() throws Exception {
        assertEquals("111", map.get("aaa"));
        assertNull(map.get("ccc"));
    }

    /**
     * @throws Exception
     */
    public void testIsEmpty() throws Exception {
        assertFalse(map.isEmpty());
        map.clear();
        assertTrue(map.isEmpty());
    }

    /**
     * @throws Exception
     */
    public void testKeySet() throws Exception {
        Set<String> keySet = map.keySet();
        assertNotNull(keySet);
        assertEquals(2, keySet.size());
        Iterator<String> iterator = keySet.iterator();
        assertEquals("aaa", iterator.next());
        assertEquals("bbb", iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * @throws Exception
     */
    public void testPut() throws Exception {
        assertEquals("111", map.put("aaa", "333"));
        assertEquals("333", request.getAttribute("aaa"));
        assertNull(map.put("ccc", "333"));
    }

    /**
     * @throws Exception
     */
    public void testPutAll() throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("ccc", "333");
        m.put("ddd", "444");
        map.putAll(m);
        assertEquals(4, map.size());
        assertEquals("333", request.getAttribute("ccc"));
        assertEquals("444", request.getAttribute("ddd"));
    }

    /**
     * @throws Exception
     */
    public void testRemove() throws Exception {
        assertEquals("111", map.remove("aaa"));
        assertNull(request.getAttribute("aaa"));
        assertEquals(1, map.size());
    }

    /**
     * @throws Exception
     */
    public void testSize() throws Exception {
        assertEquals(2, map.size());
    }

    /**
     * @throws Exception
     */
    public void testValues() throws Exception {
        Collection<Object> values = map.values();
        assertNotNull(values);
        assertEquals(2, values.size());
        Iterator<Object> iterator = values.iterator();
        assertEquals("111", iterator.next());
        assertEquals("222", iterator.next());
        assertFalse(iterator.hasNext());
    }
}