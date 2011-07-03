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
public class TxUowTest extends AppEngineTestCase {

    /**
     * @throws Exception
     * 
     */
    @Test
    public void run() throws Exception {
        Key ret = Uow.run(new TxUow() {

            @Override
            protected Object run() {
                return Datastore.put(new Entity("Hoge"));
            }
        });
        assertThat(Datastore.getOrNull(ret), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void runForException() throws Exception {
        final Entity entity = new Entity("Hoge");
        try {
            Uow.run(new TxUow() {

                @Override
                protected Object run() {
                    Datastore.put(entity);
                    throw new RuntimeException();
                }
            });
        } catch (RuntimeException ignore) {
        }
        assertThat(Datastore.getOrNull(entity.getKey()), is(nullValue()));
    }
}