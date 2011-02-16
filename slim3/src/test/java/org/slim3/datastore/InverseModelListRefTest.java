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

import java.util.List;

import org.junit.Test;
import org.slim3.datastore.meta.BbbMeta;
import org.slim3.datastore.model.Bbb;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.AppEngineTestCase;
import org.slim3.util.CipherFactory;

import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * @author higa
 * 
 */
public class InverseModelListRefTest extends AppEngineTestCase {

    private Hoge hoge = new Hoge();

    private InverseModelListRef<Bbb, Hoge> ref =
        new InverseModelListRef<Bbb, Hoge>(
            Bbb.class,
            "hoge2Ref",
            hoge,
            new Sort("hoge2Ref", SortDirection.ASCENDING));

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
    public void constructor() throws Exception {
        assertThat(ref.mappedPropertyName, is("hoge2Ref"));
        assertThat(ref.modelClass.getName(), is(Bbb.class.getName()));
        assertThat(ref.modelMeta, is(nullValue()));
        assertThat(ref.owner, is(sameInstance(hoge)));
        assertThat(ref.defaultSorts.length, is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getModelList() throws Exception {
        Datastore.put(hoge);
        Bbb bbb = new Bbb();
        bbb.getHoge2Ref().setModel(hoge);
        Datastore.put(bbb);
        Bbb bbb2 = new Bbb();
        bbb2.getHoge2Ref().setModel(hoge);
        Datastore.put(bbb2);
        List<Bbb> models = ref.getModelList();
        assertThat(models.size(), is(2));
        assertThat(models.get(0).getKey(), is(bbb.getKey()));
        assertThat(models.get(1).getKey(), is(bbb2.getKey()));
        assertThat(ref.getModelList(), is(sameInstance(models)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asQueryResultList() throws Exception {
        Datastore.put(hoge);
        Bbb bbb = new Bbb();
        bbb.getHoge2Ref().setModel(hoge);
        Datastore.put(bbb);
        Bbb bbb2 = new Bbb();
        bbb2.getHoge2Ref().setModel(hoge);
        Datastore.put(bbb2);
        S3QueryResultList<Bbb> list = ref.query().asQueryResultList();
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getKey(), is(bbb.getKey()));
        assertThat(list.get(1).getKey(), is(bbb2.getKey()));
        assertThat(list.getEncodedCursor(), is(notNullValue()));
        assertThat(list.getEncodedFilters(), is(notNullValue()));
        assertThat(list.getEncodedSorts(), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryWithSort() throws Exception {
        Datastore.put(hoge);
        Bbb bbb = new Bbb();
        bbb.getHoge2Ref().setModel(hoge);
        Datastore.put(bbb);
        Bbb bbb2 = new Bbb();
        bbb2.getHoge2Ref().setModel(hoge);
        Datastore.put(bbb2);
        List<Bbb> models =
            ref.query().sort(BbbMeta.get().key.desc).getModelList();
        assertThat(models.size(), is(2));
        assertThat(models.get(0).getKey(), is(bbb2.getKey()));
        assertThat(models.get(1).getKey(), is(bbb.getKey()));
        assertThat(ref.getModelList(), is(sameInstance(models)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryWithInMemorySort() throws Exception {
        Datastore.put(hoge);
        Bbb bbb = new Bbb();
        bbb.getHoge2Ref().setModel(hoge);
        Datastore.put(bbb);
        Bbb bbb2 = new Bbb();
        bbb2.getHoge2Ref().setModel(hoge);
        Datastore.put(bbb2);
        List<Bbb> models =
            ref.query().sortInMemory(BbbMeta.get().key.desc).getModelList();
        assertThat(models.size(), is(2));
        assertThat(models.get(0).getKey(), is(bbb2.getKey()));
        assertThat(models.get(1).getKey(), is(bbb.getKey()));
        assertThat(ref.getModelList(), is(sameInstance(models)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryWithFilter() throws Exception {
        Datastore.put(hoge);
        Bbb bbb = new Bbb();
        bbb.getHoge2Ref().setModel(hoge);
        Datastore.put(bbb);
        Bbb bbb2 = new Bbb();
        bbb2.getHoge2Ref().setModel(hoge);
        Datastore.put(bbb2);
        List<Bbb> models =
            ref
                .query()
                .filter(BbbMeta.get().key.equal(bbb.getKey()))
                .getModelList();
        assertThat(models.size(), is(1));
        assertThat(models.get(0).getKey(), is(bbb.getKey()));
        assertThat(ref.getModelList(), is(sameInstance(models)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void queryWithInMemoryFilter() throws Exception {
        Datastore.put(hoge);
        Bbb bbb = new Bbb();
        bbb.getHoge2Ref().setModel(hoge);
        Datastore.put(bbb);
        Bbb bbb2 = new Bbb();
        bbb2.getHoge2Ref().setModel(hoge);
        Datastore.put(bbb2);
        List<Bbb> models =
            ref
                .query()
                .filterInMemory(BbbMeta.get().key.equal(bbb.getKey()))
                .getModelList();
        assertThat(models.size(), is(1));
        assertThat(models.get(0).getKey(), is(bbb.getKey()));
        assertThat(ref.getModelList(), is(sameInstance(models)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void clear() throws Exception {
        Datastore.put(hoge);
        Bbb bbb = new Bbb();
        bbb.getHoge2Ref().setModel(hoge);
        Datastore.put(bbb);
        ref.getModelList();
        ref.clear();
        assertThat(ref.modelList, is(nullValue()));
    }
}