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

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class IteratorEnumrationTest extends TestCase {

    /**
     * @throws Exception
     * 
     */
    public void testHasMoreElements() throws Exception {
        List<String> list = new ArrayList<String>();
        IteratorEnumeration<String> e =
            new IteratorEnumeration<String>(list.iterator());
        assertFalse(e.hasMoreElements());
        list.add("aaa");
        e = new IteratorEnumeration<String>(list.iterator());
        assertTrue(e.hasMoreElements());
        e.nextElement();
        assertFalse(e.hasMoreElements());
    }

    /**
     * @throws Exception
     * 
     */
    public void testNextElement() throws Exception {
        List<String> list = new ArrayList<String>();
        IteratorEnumeration<String> e =
            new IteratorEnumeration<String>(list.iterator());
        try {
            e.nextElement();
            fail();
        } catch (NoSuchElementException ignore) {
        }
        list.add("aaa");
        e = new IteratorEnumeration<String>(list.iterator());
        assertTrue(e.hasMoreElements());
        assertEquals("aaa", e.nextElement());
    }
}