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
package org.slim3.tester;

import com.google.appengine.api.datastore.Entity;

/**
 * @author higa
 * 
 */
public class DatastoreTestCaseTest extends DatastoreTestCase {

    /**
     * @throws Exception
     * 
     */
    public void testCount() throws Exception {
        assertEquals(0, count("Hoge"));
        ds.put(new Entity("Hoge"));
        assertEquals(1, count("Hoge"));
    }
}