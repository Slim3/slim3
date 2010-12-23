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
package com.google.appengine.api.datastore;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * @author galdolber
 *
 */
public class KeyFactoryGWTTest extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "com.google.appengine.api.datastore.Datastore";
    }

    long id = 3;
    String name = "3";
    String kind = "Product";
    
    /**
     * Test create key with name.
     */
    public void testCreateKeyWithName() {
        Key key = KeyFactory.createKey(kind, name);
        assertEquals(name, key.getName());
        assertEquals(kind, key.getKind());
        assertEquals(0L, key.getId());
    }
    
    /**
     * Test create key with id.
     */
    public void testCreateKeyWithId() {
        Key key = KeyFactory.createKey(kind, id);
        assertNull(key.getName());
        assertEquals(kind, key.getKind());
        assertEquals(id, key.getId());
    }
    
    /**
     * Test create key with parent and id.
     */
    public void testCreateKeyWithParentAndId() {
        Key parent = KeyFactory.createKey(kind, name);
        Key key = KeyFactory.createKey(parent, kind, id);
        assertNull(key.getName());
        assertEquals(kind, key.getKind());
        assertEquals(id, key.getId());
        assertEquals(parent, key.getParent());
    }
    
    /**
     * Test create key with parent and name.
     */
    public void testCreateKeyWithParentAndName() {
        Key parent = KeyFactory.createKey(kind, name);
        Key key = KeyFactory.createKey(parent, kind, name);
        assertEquals(name, key.getName());
        assertEquals(kind, key.getKind());
        assertEquals(0L, key.getId());
        assertEquals(parent, key.getParent());
    }
    
    /**
     * Test create key string with id.
     */
    public void testCreateKeyStringWithId() {
        try {
            KeyFactory.createKeyString(kind, id);
        } catch (IllegalStateException e) {
            return;
        }
        fail();
    }
    
    /**
     * Test create key string with name.
     */
    public void testCreateKeyStringWithName() {
        try {
            KeyFactory.createKeyString(kind, name);
        } catch (IllegalStateException e) {
            return;
        }
        fail();
    }
    
    /**
     * Test create key string with parent and id.
     */
    public void testCreateKeyStringWithParentAndId() {
        try {
            KeyFactory.createKeyString(KeyFactory.createKey(kind, name), kind, id);
        } catch (IllegalStateException e) {
            return;
        }
        fail();
    }
    
    /**
     * Test create key string with parent and name.
     */
    public void testCreateKeyStringWithParentAndName() {
        try {
            KeyFactory.createKeyString(KeyFactory.createKey(kind, name), kind, name);
        } catch (IllegalStateException e) {
            return;
        }
        fail();
    }
}
