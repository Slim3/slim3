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

import org.junit.Test;
import org.slim3.tester.ServletTestCase;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.labs.taskqueue.TaskQueuePb.TaskQueueAddRequest;

/**
 * @author higa
 * 
 */
public class GlobalTransactionServletTest extends ServletTestCase {

    private GlobalTransaction gtx;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        gtx = new GlobalTransaction();
    }

    /**
     * @throws Exception
     */
    @Test
    public void rollForward() throws Exception {
        gtx.put(new Entity("Hoge"));
        gtx.commitAsync();
        tester.request.setServletPath("/slim3/gtx/rollforward/"
            + Datastore.keyToString(gtx.globalTransactionKey)
            + "/1");
        GlobalTransactionServlet servlet = new GlobalTransactionServlet();
        servlet.process(tester.request, tester.response);
        assertThat(Datastore.query("Hoge").count(), is(1));
        assertThat(Datastore.query(GlobalTransaction.KIND).count(), is(0));
        assertThat(Datastore.query(Lock.KIND).count(), is(0));
        assertThat(Datastore.query(Journal.KIND).count(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void rollback() throws Exception {
        gtx.put(new Entity("Hoge"));
        gtx.put(new Entity("Hoge"));
        gtx.rollbackAsync();
        tester.request.setServletPath("/slim3/gtx/rollback/"
            + Datastore.keyToString(gtx.globalTransactionKey));
        GlobalTransactionServlet servlet = new GlobalTransactionServlet();
        servlet.process(tester.request, tester.response);
        assertThat(Datastore.query("Hoge").count(), is(0));
        assertThat(Datastore.query(GlobalTransaction.KIND).count(), is(0));
        assertThat(Datastore.query(Lock.KIND).count(), is(0));
        assertThat(Datastore.query(Journal.KIND).count(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void cleanUp() throws Exception {
        String encodedKey = Datastore.keyToString(gtx.globalTransactionKey);
        gtx.put(new Entity("Hoge"));
        Journal.put(gtx.journalMap.values());
        gtx.commitGlobalTransactionInternally();
        tester.request.setServletPath("/slim3/gtx/cleanup");
        GlobalTransactionServlet servlet = new GlobalTransactionServlet();
        servlet.process(tester.request, tester.response);
        assertThat(tester.tasks.size(), is(1));
        TaskQueueAddRequest task = tester.tasks.get(0);
        assertThat(task.getUrl(), is(GlobalTransaction.ROLLFORWARD_PATH
            + encodedKey
            + "/1"));
    }
}