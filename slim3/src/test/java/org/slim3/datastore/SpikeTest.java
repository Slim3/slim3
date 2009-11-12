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

import java.util.List;

import org.junit.Test;
import org.slim3.tester.LocalServiceTestCase;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

/**
 * @author higa
 * 
 */
public class SpikeTest extends LocalServiceTestCase {

    /**
     * @throws Exception
     */
    @Test
    public void spike() throws Exception {
        Key key = Datastore.put(new Entity("Hoge"));
        Key key2 =
            Datastore.put(new Entity(Datastore.createKey(key, "Hoge2", 1)));
        Transaction tx = Datastore.beginTransaction();
        List<Entity> list = Datastore.get(tx, key, key2);
        Transaction tx2 = Datastore.beginTransaction();
        Datastore.put(tx2, list);
        tx2.commit();
        // Datastore.put(tx, list);
        // tx.commit();
    }
}