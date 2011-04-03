/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.junit.After;
import org.junit.Test;

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityTranslator;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.apphosting.api.ApiProxy;
import com.google.apphosting.api.ApiProxy.ApiConfig;
import com.google.apphosting.api.ApiProxy.Delegate;
import com.google.apphosting.api.ApiProxy.Environment;
import com.google.apphosting.api.DatastorePb.PutRequest;

/**
 * @author higa
 * 
 */
public class AppEngineTesterTearDownTest {

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
    public void datastorePut() throws Exception {
        tester.setUp();
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        ds.put(new Entity("Hoge"));
        tester.tearDown();
        ApiProxy.setDelegate(AppEngineTester.apiProxyLocalImpl);
        ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment());
        assertThat(tester.count("Hoge"), is(0));
    }

    /**
     * @throws Exception
     * 
     */
    @SuppressWarnings("unchecked")
    @Test
    public void datastorePutAsync() throws Exception {
        tester.setUp();
        Environment env = ApiProxy.getCurrentEnvironment();
        Delegate<Environment> delegate = ApiProxy.getDelegate();
        PutRequest reqPb = new PutRequest();
        reqPb.addEntity(EntityTranslator.convertToPb(new Entity("Hoge")));
        Future<byte[]> future =
            delegate.makeAsyncCall(
                env,
                AppEngineTester.DATASTORE_SERVICE,
                AppEngineTester.PUT_METHOD,
                reqPb.toByteArray(),
                new ApiConfig());
        future.get();
        assertThat(tester.count("Hoge"), is(1));
        tester.tearDown();
        ApiProxy.setDelegate(AppEngineTester.apiProxyLocalImpl);
        ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment());
        assertThat(tester.count("Hoge"), is(0));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void memcachePut() throws Exception {
        tester.setUp();
        MemcacheService ms = MemcacheServiceFactory.getMemcacheService();
        ms.put("aaa", 1);
        tester.tearDown();
        ApiProxy.setDelegate(AppEngineTester.apiProxyLocalImpl);
        ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment());
        assertThat(ms.contains("aaa"), is(false));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void memcacheIncrement() throws Exception {
        tester.setUp();
        MemcacheService ms = MemcacheServiceFactory.getMemcacheService();
        ms.increment("aaa", 1, 1L);
        tester.tearDown();
        ApiProxy.setDelegate(AppEngineTester.apiProxyLocalImpl);
        ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment());
        assertThat(ms.contains("aaa"), is(false));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void memcacheIncrementAll() throws Exception {
        tester.setUp();
        MemcacheService ms = MemcacheServiceFactory.getMemcacheService();
        Map<Object, Long> offsets = new HashMap<Object, Long>();
        offsets.put("aaa", 1L);
        ms.incrementAll(offsets, 1L);
        tester.tearDown();
        ApiProxy.setDelegate(AppEngineTester.apiProxyLocalImpl);
        ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment());
        assertThat(ms.contains("aaa"), is(false));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void namespace() throws Exception {
        tester.setUp();
        NamespaceManager.set("aaa");
        tester.tearDown();
        tester.setUp();
        assertThat(NamespaceManager.get(), is(nullValue()));
    }
}