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
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

/**
 * @author higa
 * 
 */
public class GtxUowTest extends AppEngineTestCase {

    /**
     * @throws Exception
     * 
     */
    @Test
    @SuppressWarnings("deprecation")
    public void run() throws Exception {
        Key[] keys = Uow.run(new GtxUow() {

            @Override
            protected Object run() {
                Key[] keys = new Key[2];
                keys[0] = gtx.put(new Entity("Hoge"));
                keys[1] = gtx.put(new Entity("Hoge"));
                return keys;
            }
        });
        assertThat(Datastore.getOrNull(keys[0]), is(notNullValue()));
        assertThat(Datastore.getOrNull(keys[1]), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    @SuppressWarnings("deprecation")
    public void runForException() throws Exception {
        final Entity entity = new Entity("Hoge");
        final Entity entity2 = new Entity("Hoge");
        try {
            Uow.run(new GtxUow() {

                @Override
                protected Object run() {
                    gtx.put(entity);
                    gtx.put(entity2);
                    throw new RuntimeException();
                }
            });
        } catch (RuntimeException ignore) {
        }
        assertThat(Datastore.getOrNull(entity.getKey()), is(nullValue()));
        assertThat(Datastore.getOrNull(entity2.getKey()), is(nullValue()));
    }
}