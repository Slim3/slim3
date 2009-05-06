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
package org.slim3.commons.util;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class CaseInsensitiveMapTest extends TestCase {

    private CaseInsensitiveMap<String> map;

    @Override
    protected void setUp() throws Exception {
        map = new CaseInsensitiveMap<String>();
        map.put("one", "1");
        map.put("two", "2");
    }

    @Override
    protected void tearDown() throws Exception {
        map = null;
    }

    /**
     * @throws Exception
     */
    public void testContainsKey() throws Exception {
        assertTrue("1", map.containsKey("ONE"));
        assertTrue("2", map.containsKey("one"));
        assertTrue("3", !map.containsKey("onex"));
    }

    /**
     * @throws Exception
     */
    public void testGet() throws Exception {
        assertEquals("1", "1", map.get("ONE"));
        assertEquals("2", "1", map.get("One"));
        assertEquals("3", null, map.get("hoge"));
    }

    /**
     * @throws Exception
     */
    public void testPut() throws Exception {
        assertEquals("1", "1", map.put("One", "11"));
        assertEquals("2", "11", map.get("one"));
    }

    /**
     * @throws Exception
     */
    public void testRemove() throws Exception {
        assertEquals("1", "1", map.remove("ONE"));
        assertEquals("2", 1, map.size());
        assertEquals("3", null, map.remove("dummy"));
    }

    /**
     * @throws Exception
     */
    public void testPutAll() throws Exception {
        CaseInsensitiveMap<String> m = new CaseInsensitiveMap<String>();
        m.put("three", "3");
        m.put("four", "4");
        map.putAll(m);
        assertEquals("1", "3", map.get("THREE"));
        assertEquals("2", "4", map.get("FOUR"));
        assertEquals("3", 4, map.size());
    }
}