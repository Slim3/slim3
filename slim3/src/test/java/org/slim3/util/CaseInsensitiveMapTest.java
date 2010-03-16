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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author higa
 * 
 */
public class CaseInsensitiveMapTest {

    private CaseInsensitiveMap<String> map;

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        map = new CaseInsensitiveMap<String>();
        map.put("one", "1");
        map.put("two", "2");
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        map = null;
    }

    /**
     * @throws Exception
     */
    @Test
    public void containsKey() throws Exception {
        assertThat(map.containsKey("ONE"), is(true));
        assertThat(map.containsKey("one"), is(true));
        assertThat(map.containsKey("onex"), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void get() throws Exception {
        assertThat(map.get("ONE"), is("1"));
        assertThat(map.get("One"), is("1"));
        assertThat(map.get("hoge"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void put() throws Exception {
        assertThat(map.put("One", "11"), is("1"));
        assertThat(map.get("one"), is("11"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void remove() throws Exception {
        assertThat(map.remove("ONE"), is("1"));
        assertThat(map.size(), is(1));
        assertThat(map.remove("dummy"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putAll() throws Exception {
        CaseInsensitiveMap<String> m = new CaseInsensitiveMap<String>();
        m.put("three", "3");
        m.put("four", "4");
        map.putAll(m);
        assertThat(map.get("THREE"), is("3"));
        assertThat(map.get("FOUR"), is("4"));
        assertThat(map.size(), is(4));
    }
}