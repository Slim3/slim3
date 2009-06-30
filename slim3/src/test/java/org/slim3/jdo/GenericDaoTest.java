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
        dao.sort(list, m.id.asc());
        assertEquals(new Long(1), list.get(0).getId());
        assertEquals(new Long(2), list.get(1).getId());
        assertEquals(new Long(3), list.get(2).getId());
    }
}