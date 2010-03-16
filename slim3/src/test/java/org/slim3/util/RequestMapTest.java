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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slim3.tester.MockHttpServletRequest;
import org.slim3.tester.MockServletContext;

/**
 * @author higa
 * 
 */
public class RequestMapTest {

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request =
        new MockHttpServletRequest(servletContext);

    private RequestMap map = new RequestMap(request);

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        request.setAttribute("aaa", "111");
        request.setAttribute("bbb", "222");
    }

    /**
     * @throws Exception
     */
    @After
    public void clear() throws Exception {
        map.clear();
    }

    /**
     * @throws Exception
     */
    @Test
    public void containsKey() throws Exception {
        assertThat(map.containsKey("aaa"), is(true));
        assertThat(map.containsKey("bbb"), is(true));
        assertThat(map.containsKey("ccc"), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void containsValue() throws Exception {
        assertThat(map.containsValue("111"), is(true));
        assertThat(map.containsValue("222"), is(true));
        assertThat(map.containsValue("333"), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void entrySet() throws Exception {
        Set<Entry<String, Object>> entrySet = map.entrySet();
        assertThat(entrySet, is(notNullValue()));
        assertThat(entrySet.size(), is(2));
        Iterator<Entry<String, Object>> iterator = entrySet.iterator();
        Entry<String, Object> entry = iterator.next();
        assertThat(entry.getKey(), is("aaa"));
        assertThat((String) entry.getValue(), is("111"));
        entry = iterator.next();
        assertThat(entry.getKey(), is("bbb"));
        assertThat((String) entry.getValue(), is("222"));
        assertThat(iterator.hasNext(), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void get() throws Exception {
        assertThat((String) map.get("aaa"), is("111"));
        assertThat(map.get("ccc"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void isEmpty() throws Exception {
        assertThat(map.isEmpty(), is(false));
        map.clear();
        assertThat(map.isEmpty(), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void keySet() throws Exception {
        Set<String> keySet = map.keySet();
        assertThat(keySet, is(notNullValue()));
        assertThat(keySet.size(), is(2));
        Iterator<String> iterator = keySet.iterator();
        assertThat(iterator.next(), is("aaa"));
        assertThat(iterator.next(), is("bbb"));
        assertThat(iterator.hasNext(), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void put() throws Exception {
        assertThat((String) map.put("aaa", "333"), is("111"));
        assertThat((String) request.getAttribute("aaa"), is("333"));
        assertThat(map.put("ccc", "333"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putAll() throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("ccc", "333");
        m.put("ddd", "444");
        map.putAll(m);
        assertThat(map.size(), is(4));
        assertThat((String) request.getAttribute("ccc"), is("333"));
        assertThat((String) request.getAttribute("ddd"), is("444"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void remove() throws Exception {
        assertThat((String) map.remove("aaa"), is("111"));
        assertThat(request.getAttribute("aaa"), is(nullValue()));
        assertThat(map.size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void size() throws Exception {
        assertThat(map.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void values() throws Exception {
        Collection<Object> values = map.values();
        assertThat(values, is(notNullValue()));
        assertThat(values.size(), is(2));
        Iterator<Object> iterator = values.iterator();
        assertThat((String) iterator.next(), is("111"));
        assertThat((String) iterator.next(), is("222"));
        assertThat(iterator.hasNext(), is(false));
    }
}