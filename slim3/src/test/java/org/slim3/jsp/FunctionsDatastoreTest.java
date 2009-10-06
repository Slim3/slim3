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
package org.slim3.jsp;

import org.slim3.tester.DatastoreTestCase;

import com.google.appengine.api.datastore.KeyFactory;

/**
 * @author higa
 * 
 */
public class FunctionsDatastoreTest extends DatastoreTestCase {

    /**
     * @throws Exception
     */
    public void testKey() throws Exception {
        assertEquals("", Functions.key(null));
        String s = Functions.key(KeyFactory.createKey("Hoge", 1));
        System.out.println(s);
        assertNotNull(s);
    }
}