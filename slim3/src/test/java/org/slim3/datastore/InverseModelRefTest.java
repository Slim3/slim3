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
import org.slim3.datastore.model.Bbb;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.AppEngineTestCase;
import org.slim3.util.CipherFactory;

/**
 * @author higa
 * 
 */
public class InverseModelRefTest extends AppEngineTestCase {

    private Hoge hoge = new Hoge();

    private InverseModelRef<Bbb, Hoge> ref =
        new InverseModelRef<Bbb, Hoge>(Bbb.class, "hogeRef", hoge);

    @Override
    public void setUp() throws Exception {
        super.setUp();
        CipherFactory.getFactory().setGlobalKey("xxxxxxxxxxxxxxxx");
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        CipherFactory.getFactory().clearGlobalKey();
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModel() throws Exception {
        Datastore.put(hoge);
        Bbb bbb = new Bbb();
        bbb.getHogeRef().setModel(hoge);
        Datastore.put(bbb);
        Bbb model = ref.getModel();
        assertThat(bbb, is(notNullValue()));
        assertThat(ref.getModel(), is(sameInstance(model)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void refresh() throws Exception {
        Datastore.put(hoge);
        Bbb bbb = new Bbb();
        bbb.getHogeRef().setModel(hoge);
        Datastore.put(bbb);
        Bbb model = ref.refresh();
        assertThat(bbb, is(notNullValue()));
        assertThat(ref.refresh(), is(not(sameInstance(model))));
    }

    /**
     * @throws Exception
     */
    @Test
    public void refreshWhenModelIsNotFound() throws Exception {
        Datastore.put(hoge);
        assertThat(ref.refresh(), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void refreshWhenKeyIsNotSet() throws Exception {
        assertThat(ref.refresh(), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void clear() throws Exception {
        Datastore.put(hoge);
        Bbb bbb = new Bbb();
        bbb.getHogeRef().setModel(hoge);
        Datastore.put(bbb);
        ref.getModel();
        ref.clear();
        assertThat(ref.model, is(nullValue()));
    }
}