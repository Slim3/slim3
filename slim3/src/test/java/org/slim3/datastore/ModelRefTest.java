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
import org.slim3.datastore.model.AaaRef;
import org.slim3.datastore.model.Bbb;
import org.slim3.datastore.model.Hoge;
import org.slim3.datastore.model.HogeRef;
import org.slim3.tester.LocalServiceTestCase;

/**
 * @author higa
 * 
 */
public class ModelRefTest extends LocalServiceTestCase {

    private HogeRef ref = new HogeRef();

    /**
     * @throws Exception
     */
    @Test
    public void setModel() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setKey(Datastore.allocateId(Hoge.class));
        ref.setModel(hoge);
        assertThat(ref.getKey(), is(hoge.getKey()));
        assertThat(ref.model, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void setModelForSubModel() throws Exception {
        AaaRef aaaRef = new AaaRef();
        Bbb bbb = new Bbb();
        bbb.setKey(Datastore.allocateId(Bbb.class));
        aaaRef.setModel(bbb);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModel() throws Exception {
        assertThat(ref.getModel(), is(nullValue()));
        Hoge hoge = new Hoge();
        Datastore.put(hoge);
        ref.setKey(hoge.getKey());
        Hoge hoge2 = ref.getModel();
        assertThat(hoge2, is(notNullValue()));
        assertThat(hoge2, is(sameInstance(ref.getModel())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void refresh() throws Exception {
        assertThat(ref.refresh(), is(nullValue()));
        Hoge hoge = new Hoge();
        Datastore.put(hoge);
        ref.setModel(hoge);
        assertThat(ref.refresh(), is(not(sameInstance(hoge))));
    }

    /**
     * @throws Exception
     */
    @Test
    public void setKey() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setKey(Datastore.allocateId(Hoge.class));
        ref.setModel(hoge);
        ref.setKey(hoge.getKey());
        assertThat(ref.getKey(), is(hoge.getKey()));
        assertThat(ref.model, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void validate() throws Exception {
        ref.validate(Datastore.allocateId("Hoge"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void validateForSubModel() throws Exception {
        AaaRef aaaRef = new AaaRef();
        aaaRef.validate(Datastore.allocateId(Bbb.class));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void validateForIllegalKey() throws Exception {
        ref.validate(Datastore.allocateId("Foo"));
    }
}