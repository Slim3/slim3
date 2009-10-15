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
package org.slim3.datastore;

import junit.framework.TestCase;

import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * @author higa
 * 
 */
public class AbstAttributeMetaTest extends TestCase {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     * 
     */
    public void testConstructor() throws Exception {
        assertEquals(meta, meta.key.modelMeta);
        assertEquals("__key__", meta.key.name);
        assertEquals(Key.class, meta.key.attributeClass);
    }

    /**
     * @throws Exception
     * 
     */
    public void testAsc() throws Exception {
        assertEquals(AscCriterion.class, meta.myString.asc.getClass());
    }

    /**
     * @throws Exception
     * 
     */
    public void testDesc() throws Exception {
        assertEquals(DescCriterion.class, meta.myString.desc.getClass());
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetValue() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyString("aaa");
        assertEquals("aaa", meta.myString.getValue(hoge));
    }

    /**
     * @throws Exception
     * 
     */
    public void testConvertValueForDatastore() throws Exception {
        assertEquals("ASCENDING", meta.myEnum
            .convertValueForDatastore(SortDirection.ASCENDING));
        assertEquals("ASCENDING", meta.myString
            .convertValueForDatastore("ASCENDING"));
        assertNull(meta.myString.convertValueForDatastore(null));
    }
}