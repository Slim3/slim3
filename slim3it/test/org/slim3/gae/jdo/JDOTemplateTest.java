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

import javax.jdo.JDOHelper;
import javax.jdo.ObjectState;

import org.slim3.gae.unit.LocalDatastoreTestCase;

import slim3.it.model.Sample;
import slim3.it.model.SampleMeta;

import com.google.appengine.api.datastore.Key;

/**
 * @author higa
 * 
 */
public class JDOTemplateTest extends LocalDatastoreTestCase {

    /**
     * @throws Exception
     */
    public void testExecuteDetach() throws Exception {
        Sample s = new JDOTemplate<Sample>() {
            @Override
            public Sample doExecute() {
                Sample sample = new Sample();
                makePersistent(sample);
                return sample;
            }
        }.execute();
        assertEquals(ObjectState.TRANSIENT, JDOHelper.getObjectState(s));
    }

    /**
     * @throws Exception
     */
    public void testExecuteDetachAndDetach() throws Exception {
        Sample s = new JDOTemplate<Sample>() {
            @Override
            public Sample doExecute() {
                Sample sample = new Sample();
                makePersistent(sample);
                return pm.detachCopy(sample);
            }
        }.execute();
        assertEquals(ObjectState.TRANSIENT, JDOHelper.getObjectState(s));
    }

    /**
     * @throws Exception
     */
    public void testFrom() throws Exception {
        new JDOTemplate<Void>() {
            @Override
            public Void doExecute() {
                assertNotNull(from(new SampleMeta()));
                return null;
            }
        }.execute();
    }

    /**
     * @throws Exception
     */
    public void testGetObjectByKey() throws Exception {
        final Key key = new JDOTemplate<Key>() {
            @Override
            public Key doExecute() {
                Sample sample = new Sample();
                makePersistent(sample);
                return sample.getKey();
            }
        }.execute();
        new JDOTemplate<Void>() {
            @Override
            public Void doExecute() {
                assertNotNull(getObjectByKey(Sample.class, key));
                return null;
            }
        }.execute();
    }
}