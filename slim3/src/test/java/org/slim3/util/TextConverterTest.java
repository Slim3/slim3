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
package org.slim3.util;

import junit.framework.TestCase;

import com.google.appengine.api.datastore.Text;

/**
 * @author higa
 * 
 */
public class TextConverterTest extends TestCase {

    private TextConverter converter = new TextConverter();

    /**
     * @throws Exception
     */
    public void testGetAsObject() throws Exception {
        assertEquals(new Text("aaa"), converter.getAsObject("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testGetAsString() throws Exception {
        assertEquals("aaa", converter.getAsString(new Text("aaa")));
    }

    /**
     * @throws Exception
     */
    public void testIsTarget() throws Exception {
        assertTrue(converter.isTarget(Text.class));
        assertFalse(converter.isTarget(String.class));
    }
}