/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
        assertEquals("Aaa", names.add("aaa.Aaa"));
        assertEquals("Bbb", names.add("bbb.Bbb"));
        assertEquals("ddd.Bbb", names.add("ddd.Bbb"));

        Iterator<String> it = names.iterator();
        assertTrue(it.hasNext());
        assertEquals("bbb.Bbb", it.next());
        assertTrue(it.hasNext());
        assertEquals("ccc.Ccc", it.next());
        assertFalse(it.hasNext());
    }
}
