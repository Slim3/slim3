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

import org.junit.Test;
import org.slim3.tester.LocalServiceTestCase;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.users.User;

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
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Entity entity = new Entity("Hoge");
        entity.setProperty("user", new User(
            "higayasuo@gmail.com",
            "gmail.com",
            "higayasuo"));
        ds.put(entity);
        System.out.println(Datastore.query("Hoge").filter(
            "user",
            FilterOperator.EQUAL,
            new User("higayasuo@gmail.com", "xxx", "higayasuo")).count());
    }
}