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

import com.google.appengine.api.datastore.Key;

/**
 * @author higa
 * 
 */
public class AbstAttributeMetaTest extends TestCase {

    /**
     * @throws Exception
     * 
     */
    public void testConstructor() throws Exception {
        HogeMeta meta = new HogeMeta();
        assertEquals(meta, meta.key.modelMeta);
        assertEquals("__key__", meta.key.name);
        assertEquals(Key.class, meta.key.attributeClass);
    }

    /**
     * @throws Exception
     * 
     */
    public void testIsEmpty() throws Exception {
        HogeMeta meta = new HogeMeta();
        assertTrue(meta.myString.isEmpty(null));
        assertTrue(meta.myString.isEmpty(""));
        assertFalse(meta.myString.isEmpty("aa"));
    }
}