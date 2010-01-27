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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * @author higa
 * 
 */
public class SpikeTest extends AppEngineTestCase {

    /**
     * @throws Exception
     */
    @Test
    public void spike() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        List<Key> keys = new ArrayList<Key>();
        for (int i = 0; i < 501; i++) {
            keys.add(key);
        }
        Datastore.getAsMap(keys);
    }
}