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

import java.util.Arrays;

import org.junit.Test;
import org.slim3.tester.LocalServiceTestCase;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query.FilterOperator;

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
        Entity entity = new Entity("Hoge");
        entity.setProperty("list", Arrays.asList("aaa", "abb"));
        Datastore.put(entity);
        System.out.println(Datastore.query("Hoge").filter(
            "list",
            FilterOperator.GREATER_THAN_OR_EQUAL,
            "a").asList());
    }
}