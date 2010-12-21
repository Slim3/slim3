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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.AppEngineTestCase;
import org.slim3.util.CipherFactory;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyRange;
import com.google.appengine.api.datastore.Text;

/**
 * @author higa
 * 
 */
public class DatastoreDelegateTest extends AppEngineTestCase {

    private HogeMeta meta = new HogeMeta();

    private AsyncDatastoreService ds =
        DatastoreServiceFactory.getAsyncDatastoreService();

    private DatastoreDelegate delegate = new DatastoreDelegate();

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
    public void beginTransaction() throws Exception {
        assertThat(delegate.beginTransaction(), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getActiveTransactions() throws Exception {
        assertThat(delegate.getActiveTransactions().size(), is(0));
        ds.beginTransaction();
        assertThat(delegate.getActiveTransactions().size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getCurrentTransaction() throws Exception {
        assertThat(delegate.getCurrentTransaction(), is(nullValue()));
        ds.beginTransaction();
        assertThat(delegate.getCurrentTransaction(), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void beginGlobalTransaction() throws Exception {
        assertThat(delegate.beginGlobalTransaction(), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void allocateId() throws Exception {
        assertThat(delegate.allocateId("Hoge"), is(not(nullValue())));
        assertThat(delegate.allocateId(Hoge.class), is(not(nullValue())));
        assertThat(delegate.allocateId(meta), is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void allocateIdWithParentKey() throws Exception {
        Key parentKey = KeyFactory.createKey("Parent", 1);
        assertThat(delegate.allocateId(parentKey, "Hoge"), is(not(nullValue())));
        assertThat(
            delegate.allocateId(parentKey, Hoge.class),
            is(not(nullValue())));
        assertThat(delegate.allocateId(parentKey, meta), is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void allocateIds() throws Exception {
        KeyRange range = delegate.allocateIds("Hoge", 2);
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));

        range = delegate.allocateIds(Hoge.class, 2);
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));

        range = delegate.allocateIds(meta, 2);
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void allocateIdsWithParentKey() throws Exception {
        Key parentKey = KeyFactory.createKey("Parent", 1);
        KeyRange range = delegate.allocateIds(parentKey, "Hoge", 2);
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));

        range = delegate.allocateIds(parentKey, Hoge.class, 2);
        assertThat(range, is(notNullValue()));
        assertEquals(2, range.getSize());

        range = delegate.allocateIds(parentKey, meta, 2);
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void createKey() throws Exception {
        assertThat(delegate.createKey("Hoge", 1), is(not(nullValue())));
        assertThat(delegate.createKey(Hoge.class, 1), is(not(nullValue())));
        assertThat(delegate.createKey(meta, 1), is(not(nullValue())));
        assertThat(delegate.createKey("Hoge", "aaa"), is(not(nullValue())));
        assertThat(delegate.createKey(Hoge.class, "aaa"), is(not(nullValue())));
        assertThat(delegate.createKey(meta, "aaa"), is(not(nullValue())));
        Key parentKey = KeyFactory.createKey("Parent", 1);
        assertThat(
            delegate.createKey(parentKey, "Hoge", 1),
            is(not(nullValue())));
        assertThat(
            delegate.createKey(parentKey, Hoge.class, 1),
            is(not(nullValue())));
        assertThat(delegate.createKey(parentKey, meta, 1), is(not(nullValue())));
        assertThat(
            delegate.createKey(parentKey, "Hoge", "aaa"),
            is(not(nullValue())));
        assertThat(
            delegate.createKey(parentKey, Hoge.class, "aaa"),
            is(not(nullValue())));
        assertThat(
            delegate.createKey(parentKey, meta, "aaa"),
            is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void keyToString() throws Exception {
        Key key = delegate.createKey("Hoge", 1);
        String encodedKey = KeyFactory.keyToString(key);
        assertThat(delegate.keyToString(key), is(encodedKey));
    }

    /**
     * @throws Exception
     */
    @Test
    public void stringToKey() throws Exception {
        Key key = delegate.createKey("Hoge", 1);
        String encodedKey = KeyFactory.keyToString(key);
        assertThat(delegate.stringToKey(encodedKey), is(key));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putUniqueValue() throws Exception {
        assertThat(delegate.putUniqueValue("screenName", "aaa"), is(true));
        assertThat(ds.getActiveTransactions().size(), is(0));
        assertThat(delegate.putUniqueValue("screenName", "aaa"), is(false));
        assertThat(ds.getActiveTransactions().size(), is(0));
        assertThat(delegate.putUniqueValue("screenName", "bbb"), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteUniqueValue() throws Exception {
        assertThat(delegate.putUniqueValue("screenName", "aaa"), is(true));
        delegate.deleteUniqueValue("screenName", "aaa");
        assertThat(delegate.putUniqueValue("screenName", "aaa"), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void filterInMemory() throws Exception {
        List<Hoge> list = new ArrayList<Hoge>();
        Hoge hoge = new Hoge();
        hoge.setMyStringList(Arrays.asList("aaa"));
        list.add(hoge);
        List<Hoge> filtered =
            delegate.filterInMemory(list, HogeMeta.get().myStringList
                .startsWith("aaa"));
        assertThat(filtered.size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void sortInMemory() throws Exception {
        List<Hoge> list = new ArrayList<Hoge>();
        Hoge hoge = new Hoge();
        hoge.setMyInteger(1);
        list.add(hoge);
        hoge = new Hoge();
        hoge.setMyInteger(3);
        list.add(hoge);
        hoge = new Hoge();
        hoge.setMyInteger(2);
        list.add(hoge);

        List<Hoge> sorted = delegate.sortInMemory(list, meta.myInteger.desc);
        assertThat(sorted.size(), is(3));
        assertThat(sorted.get(0).getMyInteger(), is(3));
        assertThat(sorted.get(1).getMyInteger(), is(2));
        assertThat(sorted.get(2).getMyInteger(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void setGlobalCipherKey() throws Exception {
        delegate.setGlobalCipherKey("1234567890ABCDEF");
        Hoge hoge = new Hoge();
        hoge.setMyCipherString("hoge.");
        hoge.setMyCipherLobString("hogehoge.");
        hoge.setMyCipherText(new Text("hogehogehoge."));
        Key key = delegate.put(hoge);
        assertThat(delegate.get(meta, key).getMyCipherString(), is("hoge."));
        assertThat(
            delegate.get(meta, key).getMyCipherLobString(),
            is("hogehoge."));
        assertThat(
            delegate.get(meta, key).getMyCipherText().getValue(),
            is("hogehogehoge."));
        assertThat(
            (String) delegate.get(key).getProperty("myCipherString"),
            not("hoge."));
        assertThat(((Text) delegate.get(key).getProperty("myCipherLobString"))
            .getValue(), not("hogehoge."));
        assertThat(((Text) delegate.get(key).getProperty("myCipherText"))
            .getValue(), not("hogehogehoge."));
    }

    /**
     * @throws Exception
     */
    @Test
    public void setLimitedCipherKey() throws Exception {
        delegate.setLimitedCipherKey("1234567890ABCDEF");
        Hoge hoge = new Hoge();
        hoge.setMyCipherString("hoge.");
        hoge.setMyCipherLobString("hogehoge.");
        hoge.setMyCipherText(new Text("hogehogehoge."));
        Key key = delegate.put(hoge);
        assertThat(delegate.get(meta, key).getMyCipherString(), is("hoge."));
        assertThat(
            delegate.get(meta, key).getMyCipherLobString(),
            is("hogehoge."));
        assertThat(
            delegate.get(meta, key).getMyCipherText().getValue(),
            is("hogehogehoge."));
        assertThat(
            (String) delegate.get(key).getProperty("myCipherString"),
            not("hoge."));
        assertThat(((Text) delegate.get(key).getProperty("myCipherLobString"))
            .getValue(), not("hogehoge."));
        assertThat(((Text) delegate.get(key).getProperty("myCipherText"))
            .getValue(), not("hogehogehoge."));
    }
}