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
package org.slim3.gen.generator;

import java.util.Iterator;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class ImportedNamesTest extends TestCase {

    public void test() throws Exception {
        ImportedNames names = new ImportedNames("aaa");
        assertEquals("Bbb", names.add("bbb.Bbb"));
        assertEquals("Ccc", names.add("ccc.Ccc"));
        assertEquals("Integer", names.add("java.lang.Integer"));
        assertEquals("Annotation", names.add("java.lang.annotation.Annotation"));
        assertEquals("Aaa", names.add("aaa.Aaa"));
        assertEquals("Bbb", names.add("bbb.Bbb"));
        assertEquals("ddd.Bbb", names.add("ddd.Bbb"));

        Iterator<String> it = names.iterator();
        assertTrue(it.hasNext());
        assertEquals("bbb.Bbb", it.next());
        assertTrue(it.hasNext());
        assertEquals("ccc.Ccc", it.next());
        assertTrue(it.hasNext());
        assertEquals("java.lang.annotation.Annotation", it.next());
        assertFalse(it.hasNext());
    }

    public void testDuplicatedClassNames_samePackage() throws Exception {
        ImportedNames names = new ImportedNames("aaa");
        assertEquals("Aaa", names.add("aaa.Aaa"));
        assertEquals("bbb.Aaa", names.add("bbb.Aaa"));

        Iterator<String> it = names.iterator();
        assertFalse(it.hasNext());
    }

    public void testDuplicatedClassNames_samePackage2() throws Exception {
        ImportedNames names = new ImportedNames("aaa");
        assertEquals("Aaa", names.add("bbb.Aaa"));
        assertEquals("aaa.Aaa", names.add("aaa.Aaa"));

        Iterator<String> it = names.iterator();
        assertTrue(it.hasNext());
        assertEquals("bbb.Aaa", it.next());
        assertFalse(it.hasNext());
    }

    public void testDuplicatedClassNames_javaLangPackage() throws Exception {
        ImportedNames names = new ImportedNames("aaa");
        assertEquals("Integer", names.add("java.lang.Integer"));
        assertEquals("bbb.Integer", names.add("bbb.Integer"));
        assertEquals("Integer", names.add("java.lang.Integer"));

        Iterator<String> it = names.iterator();
        assertFalse(it.hasNext());
    }

    public void testDuplicatedClassNames_javaLangPackage2() throws Exception {
        ImportedNames names = new ImportedNames("aaa");
        assertEquals("Integer", names.add("bbb.Integer"));
        assertEquals("java.lang.Integer", names.add("java.lang.Integer"));
        assertEquals("Integer", names.add("bbb.Integer"));

        Iterator<String> it = names.iterator();
        assertTrue(it.hasNext());
        assertEquals("bbb.Integer", it.next());
        assertFalse(it.hasNext());
    }

    public void testDuplicatedClassNames_defaultPacakge() throws Exception {
        ImportedNames names = new ImportedNames("aaa");
        assertEquals("Bbb", names.add("Bbb"));
        assertEquals("bbb.Bbb", names.add("bbb.Bbb"));
        assertEquals("Bbb", names.add("Bbb"));

        Iterator<String> it = names.iterator();
        assertFalse(it.hasNext());
    }

    public void testDuplicatedClassNames_defaultPacakge2() throws Exception {
        ImportedNames names = new ImportedNames("aaa");
        assertEquals("Bbb", names.add("bbb.Bbb"));
        assertEquals("Bbb", names.add("Bbb"));
        assertEquals("Bbb", names.add("bbb.Bbb"));

        Iterator<String> it = names.iterator();
        assertTrue(it.hasNext());
        assertEquals("bbb.Bbb", it.next());
        assertFalse(it.hasNext());
    }
}
