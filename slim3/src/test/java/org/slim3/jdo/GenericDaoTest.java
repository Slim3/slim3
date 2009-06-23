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

import org.slim3.util.BeanMap;

/**
 * @author higa
 * 
 */
public class GenericDaoTest extends TestCase {

    private GenericDao<Sample> dao =
        new GenericDao<Sample>(new MockPersistenceManager(), Sample.class);

    /**
     * @throws Exception
     */
    public void testCopyModelToMap() throws Exception {
        Sample sample = new Sample();
        sample.setId(Long.valueOf(1));
        sample.setName("aaa");
        BeanMap map = new BeanMap();
        dao.modelToMap.copy(sample, map);
        assertEquals(Long.valueOf(1), map.get("id"));
        assertEquals("aaa", map.get("name"));
    }

    /**
     * @throws Exception
     */
    public void testToMapList() throws Exception {
        Sample sample = new Sample();
        sample.setId(Long.valueOf(1));
        sample.setName("aaa");
        List<Sample> modelList = new ArrayList<Sample>();
        modelList.add(sample);
        List<BeanMap> mapList = dao.toMapList(modelList);
        assertEquals(1, mapList.size());
        BeanMap map = mapList.get(0);
        assertEquals(Long.valueOf(1), map.get("id"));
        assertEquals("aaa", map.get("name"));
    }
}
