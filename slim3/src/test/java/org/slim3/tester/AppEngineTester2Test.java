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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.apphosting.api.ApiProxy;

/**
 * @author higa
 * 
 */
public class AppEngineTester2Test {

    private AppEngineTester tester = new AppEngineTester();

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        ApiProxy.setDelegate(null);
        ApiProxy.setEnvironmentForCurrentThread(null);
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void deleteEntities() throws Exception {
        tester.setUp();
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        ds.put(new Entity("Hoge"));
        tester.tearDown();
        ApiProxy.setDelegate(AppEngineTester.apiProxyLocalImpl);
        ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment());
        assertThat(tester.count("Hoge"), is(0));
    }
}