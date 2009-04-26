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
package org.slim3.mvc.unit;

import java.util.NoSuchElementException;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class EmptyEnumrationTest extends TestCase {

    /**
     * @throws Exception
     * 
     */
    public void testHasMoreElements() throws Exception {
        EmptyEnumeration<String> e = new EmptyEnumeration<String>();
        assertFalse(e.hasMoreElements());
    }

    /**
     * @throws Exception
     * 
     */
    public void testNextElement() throws Exception {
        EmptyEnumeration<String> e = new EmptyEnumeration<String>();
        try {
            e.nextElement();
            fail();
        } catch (NoSuchElementException ex) {
            System.out.println(ex.getMessage());
        }
    }
}