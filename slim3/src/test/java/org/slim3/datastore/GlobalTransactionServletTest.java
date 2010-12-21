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
package org.slim3.datastore;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.ServletTestCase;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

/**
 * @author higa
 * 
 */
public class GlobalTransactionServletTest extends ServletTestCase {

    private AsyncDatastoreService ds =
        DatastoreServiceFactory.getAsyncDatastoreService();

    private GlobalTransaction gtx;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        gtx = new GlobalTransaction(ds);
        gtx.begin();
    }

    /**
     * @throws Exception
     */
    @Test
    public void rollForward() throws Exception {
        gtx.put(new Entity("Hoge"));
        gtx.put(new Entity("Hoge"));
        gtx.putJournals();
        gtx.commitGlobalTransactionInternally();
        String encodedKey = Datastore.keyToString(gtx.globalTransactionKey);
        tester.request.setServletPath(GlobalTransactionServlet.SERVLET_PATH);
        tester.request.setParameter(
            GlobalTransactionServlet.COMMAND_NAME,
            GlobalTransactionServlet.ROLLFORWARD_COMMAND);
        tester.request.setParameter(
            GlobalTransactionServlet.KEY_NAME,
            encodedKey);
        GlobalTransactionServlet servlet = new GlobalTransactionServlet();
        servlet.process(tester.request, tester.response);
        assertThat(tester.count("Hoge"), is(1));
        assertThat(tester.count(GlobalTransaction.KIND), is(0));
        assertThat(tester.count(Lock.KIND), is(0));
        assertThat(tester.count(Journal.KIND), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void rollback() throws Exception {
        gtx.put(new Entity("Hoge"));
        gtx.put(new Entity("Hoge"));
        gtx.rollbackAsync();
        String encodedKey = Datastore.keyToString(gtx.globalTransactionKey);
        tester.request.setServletPath(GlobalTransactionServlet.SERVLET_PATH);
        tester.request.setParameter(
            GlobalTransactionServlet.COMMAND_NAME,
            GlobalTransactionServlet.ROLLBACK_COMMAND);
        tester.request.setParameter(
            GlobalTransactionServlet.KEY_NAME,
            encodedKey);
        GlobalTransactionServlet servlet = new GlobalTransactionServlet();
        servlet.process(tester.request, tester.response);
        assertThat(tester.count("Hoge"), is(0));
        assertThat(tester.count(GlobalTransaction.KIND), is(0));
        assertThat(tester.count(Lock.KIND), is(0));
        assertThat(tester.count(Journal.KIND), is(0));
    }
}