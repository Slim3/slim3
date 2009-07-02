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
package org.slim3.jdo;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class GenericDaoTest extends TestCase {

    private SampleDao dao = new SampleDao();

    /**
     * @throws Exception
     */
    public void testSort() throws Exception {
        List<Sample> list = new ArrayList<Sample>();
        Sample sample = new Sample();
        sample.setId(1L);
        list.add(sample);
        sample = new Sample();
        sample.setId(3L);
        list.add(sample);
        sample = new Sample();
        sample.setId(2L);
        list.add(sample);

        SampleMeta m = new SampleMeta();
        List<Sample> sorted = dao.sort(list, m.id.asc());
        assertEquals(3, sorted.size());
        assertEquals(new Long(1), sorted.get(0).getId());
        assertEquals(new Long(2), sorted.get(1).getId());
        assertEquals(new Long(3), sorted.get(2).getId());
    }

    /**
     * @throws Exception
     */
    public void testSortForMultiProperty() throws Exception {
        List<Sample> list = new ArrayList<Sample>();
        Sample sample = new Sample();
        sample.setId(1L);
        sample.setName("aaa");
        list.add(sample);
        sample = new Sample();
        sample.setId(1L);
        sample.setName("ccc");
        list.add(sample);
        sample = new Sample();
        sample.setId(1L);
        sample.setName("bbb");
        list.add(sample);

        SampleMeta m = new SampleMeta();
        List<Sample> sorted = dao.sort(list, m.id.asc(), m.name.desc());
        assertEquals(3, sorted.size());
        assertEquals("ccc", sorted.get(0).getName());
        assertEquals("bbb", sorted.get(1).getName());
        assertEquals("aaa", sorted.get(2).getName());
    }

    /**
     * @throws Exception
     */
    public void testFilter() throws Exception {
        List<Sample> list = new ArrayList<Sample>();
        Sample sample = new Sample();
        sample.setId(1L);
        list.add(sample);
        sample = new Sample();
        sample.setId(3L);
        list.add(sample);
        sample = new Sample();
        sample.setId(2L);
        list.add(sample);

        SampleMeta m = new SampleMeta();
        List<Sample> filtered = dao.filter(list, m.id.ge(2), m.id.lt(3));
        assertEquals(1, filtered.size());
        assertEquals(new Long(2), filtered.get(0).getId());
    }
}