/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.slim3.gae.jdo;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.slim3.gae.unit.DatastoreTestCase;

import slim3.it.model.Sample;

/**
 * @author higa
 * 
 */
public class SpikeTest extends DatastoreTestCase {

    /**
     * @throws Exception
     */
    public void test() throws Exception {
        PersistenceManager pm = PM.get();
        Sample s = new Sample();
        pm.makePersistent(s);
        Query query = pm.newQuery(Sample.class);
        query.setOrdering("key");
        query.execute();
    }
}