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

import java.util.ConcurrentModificationException;

import org.junit.Test;

/**
 * @author higa
 * 
 */
public class UowTest {

    private boolean run = false;

    private int retries = 0;

    /**
     * @throws Exception
     * 
     */
    @Test
    public void run() throws Exception {
        String ret = Uow.run(new AbstractUow() {

            @Override
            protected void rollback() {
            }

            @Override
            protected Object run() {
                run = true;
                return "hoge";
            }

            @Override
            protected void commit() {
            }

            @Override
            protected void beginTransaction() {
            }
        });
        assertThat(run, is(true));
        assertThat(ret, is("hoge"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void runForRetries() throws Exception {
        Uow.run(new AbstractUow() {

            @Override
            protected void rollback() {
            }

            @Override
            protected Object run() {
                run = true;
                if (retries < Uow.DEFAULT_MAX_RETRIES) {
                    retries++;
                    throw new ConcurrentModificationException();
                }
                return null;
            }

            @Override
            protected void commit() {
            }

            @Override
            protected void beginTransaction() {
            }
        });
        assertThat(run, is(true));
        assertThat(retries, is(Uow.DEFAULT_MAX_RETRIES));
    }

    /**
     * @throws Exception
     * 
     */
    @Test(expected = ConcurrentModificationException.class)
    public void runForOverMaxRetries() throws Exception {
        Uow.run(new AbstractUow() {

            @Override
            protected void rollback() {
            }

            @Override
            protected Object run() {
                throw new ConcurrentModificationException();
            }

            @Override
            protected void commit() {
            }

            @Override
            protected void beginTransaction() {
            }
        });
    }
}