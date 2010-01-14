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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.servlet.ServletException;

import org.junit.Test;
import org.slim3.tester.ServletTestCase;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

/**
 * @author higa
 * 
 */
public class GlobalTransactionServletTest extends ServletTestCase {

    private GlobalTransaction gtx = new GlobalTransaction();

    private GlobalTransactionServlet servlet = new GlobalTransactionServlet();

    /**
     * @throws Exception
     */
    @Test
    public void rollForward() throws Exception {
        Key putKey = Datastore.createKey("Hoge", 1);
        Entity entity = new Entity(putKey);
        gtx.put(entity);
        Key putKey2 = Datastore.createKey("Hoge", 2);
        Entity entity2 = new Entity(putKey2);
        gtx.put(entity2);
        gtx.startTransactionInternally();
        gtx.writeJournals();
        gtx.txEntity.setProperty(
            GlobalTransaction.STATUS_PROPERTY,
            GlobalTransaction.COMMITTED_STATUS);
        Datastore.put(gtx.txEntity);
        servlet.rollForward(Datastore.keyToString(gtx.txEntity.getKey()));
        assertThat(Datastore
            .query(GlobalTransaction.GLOBAL_TRANSACTION_KIND)
            .count(), is(0));
        assertThat(Datastore.query(GlobalTransaction.LOCK_KIND).count(), is(0));
        assertThat(
            Datastore.query(GlobalTransaction.JOURNAL_KIND).count(),
            is(0));
        assertThat(Datastore.query("Hoge").count(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void rollback() throws Exception {
        Key putKey = Datastore.createKey("Hoge", 1);
        Entity entity = new Entity(putKey);
        gtx.put(entity);
        Key putKey2 = Datastore.createKey("Hoge", 2);
        Entity entity2 = new Entity(putKey2);
        gtx.put(entity2);
        gtx.startTransactionInternally();
        gtx.writeJournals();
        servlet.rollback(Datastore.keyToString(gtx.txEntity.getKey()));
        assertThat(Datastore
            .query(GlobalTransaction.GLOBAL_TRANSACTION_KIND)
            .count(), is(0));
        assertThat(Datastore.query(GlobalTransaction.LOCK_KIND).count(), is(0));
        assertThat(
            Datastore.query(GlobalTransaction.JOURNAL_KIND).count(),
            is(0));
    }

    /**
     * @throws Exception
     */
    @Test(expected = ServletException.class)
    public void processForIllegalPath() throws Exception {
        tester.request.setServletPath("/xxx");
        servlet.process(tester.request, tester.response);
    }

    /**
     * @throws Exception
     */
    @Test(expected = ServletException.class)
    public void processForIllegalPath2() throws Exception {
        tester.request.setServletPath("/slim3/xxx");
        servlet.process(tester.request, tester.response);
    }

    /**
     * @throws Exception
     */
    @Test
    public void processForRollForward() throws Exception {
        Key putKey = Datastore.createKey("Hoge", 1);
        Entity entity = new Entity(putKey);
        gtx.put(entity);
        Key putKey2 = Datastore.createKey("Hoge", 2);
        Entity entity2 = new Entity(putKey2);
        gtx.put(entity2);
        gtx.startTransactionInternally();
        gtx.writeJournals();
        gtx.txEntity.setProperty(
            GlobalTransaction.STATUS_PROPERTY,
            GlobalTransaction.COMMITTED_STATUS);
        Datastore.put(gtx.txEntity);
        tester.request.setServletPath("/slim3/gtx/rollforward/"
            + Datastore.keyToString(gtx.txEntity.getKey()));
        servlet.process(tester.request, tester.response);
        assertThat(Datastore
            .query(GlobalTransaction.GLOBAL_TRANSACTION_KIND)
            .count(), is(0));
        assertThat(Datastore.query(GlobalTransaction.LOCK_KIND).count(), is(0));
        assertThat(
            Datastore.query(GlobalTransaction.JOURNAL_KIND).count(),
            is(0));
        assertThat(Datastore.query("Hoge").count(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void processForRollback() throws Exception {
        Key putKey = Datastore.createKey("Hoge", 1);
        Entity entity = new Entity(putKey);
        gtx.put(entity);
        Key putKey2 = Datastore.createKey("Hoge", 2);
        Entity entity2 = new Entity(putKey2);
        gtx.put(entity2);
        gtx.startTransactionInternally();
        gtx.writeJournals();
        tester.request.setServletPath("/slim3/gtx/rollback/"
            + Datastore.keyToString(gtx.txEntity.getKey()));
        servlet.process(tester.request, tester.response);
        assertThat(Datastore
            .query(GlobalTransaction.GLOBAL_TRANSACTION_KIND)
            .count(), is(0));
        assertThat(Datastore.query(GlobalTransaction.LOCK_KIND).count(), is(0));
        assertThat(
            Datastore.query(GlobalTransaction.JOURNAL_KIND).count(),
            is(0));
    }
}