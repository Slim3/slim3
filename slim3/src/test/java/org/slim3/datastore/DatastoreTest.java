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
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.AppEngineTestCase;
import org.slim3.util.CipherFactory;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyRange;
import com.google.appengine.api.datastore.Text;

/**
 * @author higa
 * 
 */
public class DatastoreTest extends AppEngineTestCase {

    private HogeMeta meta = new HogeMeta();

    private DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        CipherFactory.getFactory().setGlobalKey("xxxxxxxxxxxxxxxx");
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        CipherFactory.getFactory().clearGlobalKey();
        System.setProperty(Datastore.DELEGATE_KEY, DatastoreDelegate.class
            .getName());
        Datastore.initialize();
    }

    /**
     * @throws Exception
     */
    @Test
    public void initialize() throws Exception {
        System.setProperty(Datastore.DELEGATE_KEY, MyDelegate.class.getName());
        Datastore.initialize();
        assertThat(Datastore.delegate(), is(MyDelegate.class));
    }

    /**
     * @throws Exception
     */
    @Test
    public void delegateClass() throws Exception {
        Datastore.delegateClass(MyDelegate.class);
        assertThat(Datastore.delegate(), is(MyDelegate.class));
    }

    /**
     * @throws Exception
     */
    @Test
    public void delegate() throws Exception {
        assertThat(Datastore.delegate(), is(sameInstance(Datastore.delegate())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deadline() throws Exception {
        Double deadline = 5.0;
        assertThat(Datastore.deadline(deadline).async.deadline, is(deadline));
    }

    /**
     * @throws Exception
     */
    @Test
    public void beginTransaction() throws Exception {
        assertThat(Datastore.beginTransaction(), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getActiveTransactions() throws Exception {
        assertThat(Datastore.getActiveTransactions().size(), is(0));
        ds.beginTransaction();
        assertThat(Datastore.getActiveTransactions().size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getCurrentTransaction() throws Exception {
        assertThat(Datastore.getCurrentTransaction(), is(nullValue()));
        ds.beginTransaction();
        assertThat(Datastore.getCurrentTransaction(), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void beginGlobalTransaction() throws Exception {
        assertThat(Datastore.beginGlobalTransaction(), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void allocateId() throws Exception {
        assertThat(Datastore.allocateId("Hoge"), is(not(nullValue())));
        assertThat(Datastore.allocateId(Hoge.class), is(not(nullValue())));
        assertThat(Datastore.allocateId(meta), is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void allocateIdWithParentKey() throws Exception {
        Key parentKey = KeyFactory.createKey("Parent", 1);
        assertThat(
            Datastore.allocateId(parentKey, "Hoge"),
            is(not(nullValue())));
        assertThat(
            Datastore.allocateId(parentKey, Hoge.class),
            is(not(nullValue())));
        assertThat(Datastore.allocateId(parentKey, meta), is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void allocateIds() throws Exception {
        KeyRange range = Datastore.allocateIds("Hoge", 2);
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));

        range = Datastore.allocateIds(Hoge.class, 2);
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));

        range = Datastore.allocateIds(meta, 2);
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void allocateIdsAsync() throws Exception {
        KeyRange range = Datastore.allocateIdsAsync("Hoge", 2).get();
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));

        range = Datastore.allocateIdsAsync(Hoge.class, 2).get();
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));

        range = Datastore.allocateIdsAsync(meta, 2).get();
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void allocateIdsWithParentKey() throws Exception {
        Key parentKey = KeyFactory.createKey("Parent", 1);
        KeyRange range = Datastore.allocateIds(parentKey, "Hoge", 2);
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));

        range = Datastore.allocateIds(parentKey, Hoge.class, 2);
        assertThat(range, is(notNullValue()));
        assertEquals(2, range.getSize());

        range = Datastore.allocateIds(parentKey, meta, 2);
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void allocateIdsAsyncWithParentKey() throws Exception {
        Key parentKey = KeyFactory.createKey("Parent", 1);
        KeyRange range = Datastore.allocateIdsAsync(parentKey, "Hoge", 2).get();
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));

        range = Datastore.allocateIdsAsync(parentKey, Hoge.class, 2).get();
        assertThat(range, is(notNullValue()));
        assertEquals(2, range.getSize());

        range = Datastore.allocateIdsAsync(parentKey, meta, 2).get();
        assertThat(range, is(notNullValue()));
        assertThat(range.getSize(), is(2L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void createKey() throws Exception {
        assertThat(Datastore.createKey("Hoge", 1), is(not(nullValue())));
        assertThat(Datastore.createKey(Hoge.class, 1), is(not(nullValue())));
        assertThat(Datastore.createKey(meta, 1), is(not(nullValue())));
        assertThat(Datastore.createKey("Hoge", "aaa"), is(not(nullValue())));
        assertThat(Datastore.createKey(Hoge.class, "aaa"), is(not(nullValue())));
        assertThat(Datastore.createKey(meta, "aaa"), is(not(nullValue())));
        Key parentKey = KeyFactory.createKey("Parent", 1);
        assertThat(
            Datastore.createKey(parentKey, "Hoge", 1),
            is(not(nullValue())));
        assertThat(
            Datastore.createKey(parentKey, Hoge.class, 1),
            is(not(nullValue())));
        assertThat(
            Datastore.createKey(parentKey, meta, 1),
            is(not(nullValue())));
        assertThat(
            Datastore.createKey(parentKey, "Hoge", "aaa"),
            is(not(nullValue())));
        assertThat(
            Datastore.createKey(parentKey, Hoge.class, "aaa"),
            is(not(nullValue())));
        assertThat(
            Datastore.createKey(parentKey, meta, "aaa"),
            is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void keyToString() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        String encodedKey = KeyFactory.keyToString(key);
        assertThat(Datastore.keyToString(key), is(encodedKey));
    }

    /**
     * @throws Exception
     */
    @Test
    public void stringToKey() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        String encodedKey = KeyFactory.keyToString(key);
        assertThat(Datastore.stringToKey(encodedKey), is(key));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putUniqueValue() throws Exception {
        assertThat(Datastore.putUniqueValue("screenName", "aaa"), is(true));
        assertThat(ds.getActiveTransactions().size(), is(0));
        assertThat(Datastore.putUniqueValue("screenName", "aaa"), is(false));
        assertThat(ds.getActiveTransactions().size(), is(0));
        assertThat(Datastore.putUniqueValue("screenName", "bbb"), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteUniqueValue() throws Exception {
        assertThat(Datastore.putUniqueValue("screenName", "aaa"), is(true));
        Datastore.deleteUniqueValue("screenName", "aaa");
        assertThat(Datastore.putUniqueValue("screenName", "aaa"), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void setGlobalCipherKey() throws Exception {
        CipherFactory.getFactory().clearLimitedKey();
        Datastore.setGlobalCipherKey("1234567890ABCDEF");
        Hoge hoge = new Hoge();
        hoge.setMyCipherString("hoge.");
        hoge.setMyCipherLobString("hogehoge.");
        hoge.setMyCipherText(new Text("hogehogehoge."));
        Key key = Datastore.put(hoge);
        assertThat(Datastore.get(meta, key).getMyCipherString(), is("hoge."));
        assertThat(
            Datastore.get(meta, key).getMyCipherLobString(),
            is("hogehoge."));
        assertThat(
            Datastore.get(meta, key).getMyCipherText().getValue(),
            is("hogehogehoge."));
        assertThat(
            (String) Datastore.get(key).getProperty("myCipherString"),
            not("hoge."));
        assertThat(((Text) Datastore.get(key).getProperty("myCipherLobString"))
            .getValue(), not("hogehoge."));
        assertThat(((Text) Datastore.get(key).getProperty("myCipherText"))
            .getValue(), not("hogehogehoge."));
    }

    /**
     * @throws Exception
     */
    @Test
    public void setLimitedCipherKey() throws Exception {
        Datastore.setLimitedCipherKey("1234567890ABCDEF");
        Hoge hoge = new Hoge();
        hoge.setMyCipherString("hoge.");
        hoge.setMyCipherLobString("hogehoge.");
        hoge.setMyCipherText(new Text("hogehogehoge."));
        Key key = Datastore.put(hoge);
        assertThat(Datastore.get(meta, key).getMyCipherString(), is("hoge."));
        assertThat(
            Datastore.get(meta, key).getMyCipherLobString(),
            is("hogehoge."));
        assertThat(
            Datastore.get(meta, key).getMyCipherText().getValue(),
            is("hogehogehoge."));
        assertThat(
            (String) Datastore.get(key).getProperty("myCipherString"),
            not("hoge."));
        assertThat(((Text) Datastore.get(key).getProperty("myCipherLobString"))
            .getValue(), not("hogehoge."));
        assertThat(((Text) Datastore.get(key).getProperty("myCipherText"))
            .getValue(), not("hogehogehoge."));
    }

    /**
     *
     */
    public static class MyDelegate extends DatastoreDelegate {

        /**
         * 
         */
        public MyDelegate() {
            super();
        }

        /**
         * @param deadline
         */
        public MyDelegate(Double deadline) {
            super(deadline);
        }

    }
}