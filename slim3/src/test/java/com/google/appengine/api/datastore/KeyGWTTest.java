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
public class KeyGWTTest extends GWTTestCase {

    private final String kind = "Kind";
    private final String name = "name";
    private final long id = 3L;

    @Override
    public String getModuleName() {
        return "com.google.appengine.api.datastore.Datastore";
    }

    /**
     * Test key from kind.
     */
    public void testKeyFromKind() {
        Key key = new Key(kind);
        assertEquals(kind, key.getKind());
        assertNull(key.getName());
        assertNull(key.getParent());
        assertEquals(0L, key.getId());
    }
    
    /**
     * Test key from kind and parent.
     */
    public void testKeyFromKindAndParent() {
        Key parent = new Key(kind);
        Key key = new Key(kind, parent);
        assertEquals(kind, key.getKind());
        assertNull(key.getName());
        assertEquals(parent, key.getParent());
        assertEquals(0L, key.getId());
    }
    
    /**
     * Test key from kind and name.
     */
    public void testKeyFromKindAndName() {
        Key key = new Key(kind, name);
        assertEquals(kind, key.getKind());
        assertEquals(name, key.getName());
        assertNull(key.getParent());
        assertEquals(0L, key.getId());
    }
    
    /**
     * Test key from kind, parent and id.
     */
    public void testKeyFromKindParentAndId() {
        Key parent = new Key(kind);
        Key key = new Key(kind,parent, id);
        assertEquals(kind, key.getKind());
        assertNull(key.getName());
        assertEquals(id, key.getId());
        assertEquals(parent, key.getParent());
    }
    
    /**
     * Test key from kind, parent and name.
     */
    public void testKeyFromKindParentAndName() {
        Key parent = new Key(kind);
        Key key = new Key(kind, parent, name);
        assertEquals(kind, key.getKind());
        assertEquals(name, key.getName());
        assertEquals(parent, key.getParent());
        assertEquals(0L, key.getId());
    }
    
    /**
     * Test hashCode.
     */
    public void testKeyHashCode() {
        Key key1;
        Key key2;
        
        Key parent = new Key(kind);
        
        key1 = new Key(kind);
        key2 = new Key(kind);
        assertEquals(key1.hashCode(), key2.hashCode());
        
        key1 = new Key(kind, parent);
        key2 = new Key(kind, parent);
        assertEquals(key1.hashCode(), key2.hashCode());
        
        key1 = new Key(kind, parent, id);
        key2 = new Key(kind, parent, id);
        assertEquals(key1.hashCode(), key2.hashCode());
        
        key1 = new Key(kind, parent, name);
        key2 = new Key(kind, parent, name);
        assertEquals(key1.hashCode(), key2.hashCode());
    }
    
    /**
     * Test equals.
     */
    public void testKeyEquals() {
        Key key1;
        Key key2;
        
        Key parent = new Key(kind);
        
        key1 = new Key(kind);
        key2 = new Key(kind);
        assertEquals(key1, key2);
        
        key1 = new Key(kind, parent);
        key2 = new Key(kind, parent);
        assertEquals(key1, key2);
        
        key1 = new Key(kind, parent, id);
        key2 = new Key(kind, parent, id);
        assertEquals(key1, key2);
        
        key1 = new Key(kind, parent, name);
        key2 = new Key(kind, parent, name);
        assertEquals(key1, key2);
    }
}
