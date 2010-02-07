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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

/**
 * @author higa
 * 
 */
public class JournalTest extends AppEngineTestCase {

    /**
     * @throws Exception
     */
    @Test
    public void createKey() throws Exception {
        Key targetKey = Datastore.createKey("Hoge", 1);
        Key key = Journal.createKey(targetKey);
        assertThat(key.getParent(), is(targetKey));
        assertThat(key.getKind(), is(Journal.KIND));
        assertThat(key.getId(), is(1L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void applyWithinLocalTransactionForDeleteAll() throws Exception {
        Key targetKey = Datastore.createKey("Parent", 1);
        Key childKey = Datastore.createKey(targetKey, "Child", 1);
        Datastore.put(new Entity(targetKey));
        Datastore.put(new Entity(childKey));
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Journal journal = new Journal(globalTransactionKey, targetKey, true);
        Transaction tx = Datastore.beginTransaction();
        Journal.applyWithinLocalTransaction(tx, Arrays.asList(journal));
        Datastore.commit(tx);
        assertThat(Datastore.query("Parent").count(), is(0));
        assertThat(Datastore.query("Child").count(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void applyWithinLocalTransactionForDelete() throws Exception {
        Key targetKey = Datastore.createKey("Hoge", 1);
        Datastore.put(new Entity(targetKey));
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Journal journal = new Journal(globalTransactionKey, targetKey);
        Transaction tx = Datastore.beginTransaction();
        Journal.applyWithinLocalTransaction(tx, Arrays.asList(journal));
        Datastore.commit(tx);
        assertThat(Datastore.query("Hoge").count(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void applyWithinLocalTransactionForDeleteManyEntities()
            throws Exception {
        Key parentKey = Datastore.createKey("Parent", 1);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        List<Journal> journals = new ArrayList<Journal>();
        int count = 101;
        for (int i = 1; i <= count; i++) {
            Datastore
                .put(new Entity(Datastore.createKey(parentKey, "Hoge", i)));
        }
        for (int i = 1; i <= count; i++) {
            journals.add(new Journal(globalTransactionKey, Datastore.createKey(
                parentKey,
                "Hoge",
                i)));
        }
        Transaction tx = Datastore.beginTransaction();
        Journal.applyWithinLocalTransaction(tx, journals);
        Datastore.commit(tx);
        assertThat(Datastore.query("Hoge").count(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void applyWithinLocalTransactionForPut() throws Exception {
        Key targetKey = Datastore.createKey("Hoge", 1);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Journal journal =
            new Journal(globalTransactionKey, new Entity(targetKey));
        Transaction tx = Datastore.beginTransaction();
        Journal.applyWithinLocalTransaction(tx, Arrays.asList(journal));
        Datastore.commit(tx);
        assertThat(Datastore.query("Hoge").count(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void applyWithinLocalTransactionForPutBigEntities() throws Exception {
        Blob blob = new Blob(new byte[Journal.MAX_CONTENT_SIZE - 100]);
        Key targetKey = Datastore.createKey("Hoge", 1);
        Key targetKey2 = Datastore.createKey(targetKey, "Hoge", 2);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Entity entity = new Entity(targetKey);
        entity.setUnindexedProperty("aaa", blob);
        Entity entity2 = new Entity(targetKey2);
        entity2.setUnindexedProperty("aaa", blob);
        Journal journal = new Journal(globalTransactionKey, entity);
        Journal journal2 = new Journal(globalTransactionKey, entity2);
        Transaction tx = Datastore.beginTransaction();
        Journal.applyWithinLocalTransaction(tx, Arrays
            .asList(journal, journal2));
        Datastore.commit(tx);
        assertThat(Datastore.query("Hoge").count(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void applyWithinLocalTransactionForPutManyEntities()
            throws Exception {
        Key parentKey = Datastore.createKey("Parent", 1);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        List<Journal> journals = new ArrayList<Journal>();
        int count = 101;
        for (int i = 1; i <= count; i++) {
            journals.add(new Journal(globalTransactionKey, new Entity(Datastore
                .createKey(parentKey, "Hoge", i))));
        }
        Transaction tx = Datastore.beginTransaction();
        Journal.applyWithinLocalTransaction(tx, journals);
        Datastore.commit(tx);
        assertThat(Datastore.query("Hoge").count(), is(count));
    }

    /**
     * @throws Exception
     */
    @Test
    public void applyWithinGlobalTransactionForDeleteAll() throws Exception {
        Key targetKey = Datastore.createKey("Parent", 1);
        Key childKey = Datastore.createKey(targetKey, "Child", 1);
        Datastore.put(new Entity(targetKey));
        Datastore.put(new Entity(childKey));
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Journal journal = new Journal(globalTransactionKey, targetKey, true);
        Journal.put(Arrays.asList(journal));
        Journal.applyWithinGlobalTransaction(Arrays.asList(journal));
        assertThat(Datastore.query("Parent").count(), is(0));
        assertThat(Datastore.query("Child").count(), is(0));
        assertThat(Datastore.query(Journal.KIND).count(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void applyWithinGlobalTransactionForDelete() throws Exception {
        Key targetKey = Datastore.createKey("Hoge", 1);
        Datastore.put(new Entity(targetKey));
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Journal journal = new Journal(globalTransactionKey, targetKey);
        Journal.put(Arrays.asList(journal));
        Journal.applyWithinGlobalTransaction(Arrays.asList(journal));
        assertThat(Datastore.query("Hoge").count(), is(0));
        assertThat(Datastore.query(Journal.KIND).count(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void applyWithinGlobalTransactionForDeleteManyEntities()
            throws Exception {
        Key parentKey = Datastore.createKey("Parent", 1);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        List<Journal> journals = new ArrayList<Journal>();
        int count = 101;
        for (int i = 1; i <= count; i++) {
            Datastore
                .put(new Entity(Datastore.createKey(parentKey, "Hoge", i)));
        }
        for (int i = 1; i <= count; i++) {
            journals.add(new Journal(globalTransactionKey, Datastore.createKey(
                parentKey,
                "Hoge",
                i)));
        }
        Journal.put(journals);
        Journal.applyWithinGlobalTransaction(journals);
        assertThat(Datastore.query("Hoge").count(), is(0));
        assertThat(Datastore.query(Journal.KIND).count(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void applyWithinGlobalTransactionForPut() throws Exception {
        Key targetKey = Datastore.createKey("Hoge", 1);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Journal journal =
            new Journal(globalTransactionKey, new Entity(targetKey));
        Journal.put(Arrays.asList(journal));
        Journal.applyWithinGlobalTransaction(Arrays.asList(journal));
        assertThat(Datastore.query("Hoge").count(), is(1));
        assertThat(Datastore.query(Journal.KIND).count(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void applyWithinGlobalTransactionForPutBigEntities()
            throws Exception {
        Blob blob = new Blob(new byte[Journal.MAX_CONTENT_SIZE - 100]);
        Key targetKey = Datastore.createKey("Hoge", 1);
        Key targetKey2 = Datastore.createKey(targetKey, "Hoge", 2);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Entity entity = new Entity(targetKey);
        entity.setUnindexedProperty("aaa", blob);
        Entity entity2 = new Entity(targetKey2);
        entity2.setUnindexedProperty("aaa", blob);
        Journal journal = new Journal(globalTransactionKey, entity);
        Journal journal2 = new Journal(globalTransactionKey, entity2);
        Journal.put(Arrays.asList(journal, journal2));
        Journal.applyWithinGlobalTransaction(Arrays.asList(journal, journal2));
        assertThat(Datastore.query("Hoge").count(), is(2));
        assertThat(Datastore.query(Journal.KIND).count(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void applyWithinGlobalTransactionForPutManyEntities()
            throws Exception {
        Key parentKey = Datastore.createKey("Parent", 1);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        List<Journal> journals = new ArrayList<Journal>();
        int count = 101;
        for (int i = 1; i <= count; i++) {
            journals.add(new Journal(globalTransactionKey, new Entity(Datastore
                .createKey(parentKey, "Hoge", i))));
        }
        Journal.put(journals);
        Journal.applyWithinGlobalTransaction(journals);
        assertThat(Datastore.query("Hoge").count(), is(count));
        assertThat(Datastore.query(Journal.KIND).count(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void put() throws Exception {
        Key targetKey = Datastore.createKey("Hoge", 1);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Journal journal =
            new Journal(globalTransactionKey, new Entity(targetKey));
        Journal.put(Arrays.asList(journal));
        assertThat(Datastore.query(Journal.KIND).count(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putBigEntities() throws Exception {
        Blob blob = new Blob(new byte[Journal.MAX_CONTENT_SIZE - 100]);
        Key targetKey = Datastore.createKey("Hoge", 1);
        Key targetKey2 = Datastore.createKey(targetKey, "Hoge", 2);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Entity entity = new Entity(targetKey);
        entity.setUnindexedProperty("aaa", blob);
        Entity entity2 = new Entity(targetKey2);
        entity2.setUnindexedProperty("aaa", blob);
        Journal journal = new Journal(globalTransactionKey, entity);
        Journal journal2 = new Journal(globalTransactionKey, entity2);
        Journal.put(Arrays.asList(journal, journal2));
        assertThat(Datastore.query(Journal.KIND).count(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void putManyEntities() throws Exception {
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        List<Journal> journals = new ArrayList<Journal>();
        int count = 101;
        for (int i = 1; i <= count; i++) {
            journals.add(new Journal(globalTransactionKey, new Entity(Datastore
                .createKey("Hoge", i))));
        }
        Journal.put(journals);
        assertThat(Datastore.query(Journal.KIND).count(), is(count));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteJournals() throws Exception {
        Key targetKey = Datastore.createKey("Hoge", 1);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Journal journal = new Journal(globalTransactionKey, targetKey);
        Journal.put(Arrays.asList(journal));
        Journal.delete(Arrays.asList(journal));
        assertThat(Datastore.query(Journal.KIND).count(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteByGlobalTransactionKey() throws Exception {
        Key targetKey = Datastore.createKey("Hoge", 1);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Journal journal = new Journal(globalTransactionKey, targetKey);
        Journal.put(Arrays.asList(journal));
        Journal.delete(globalTransactionKey);
        assertThat(Datastore.query(Journal.KIND).count(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toJournal() throws Exception {
        Key targetKey = Datastore.createKey("Hoge", 1);
        Key key = Journal.createKey(targetKey);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Entity targetEntity = new Entity(targetKey);
        byte[] content = DatastoreUtil.entityToBytes(targetEntity);
        boolean deleteAll = false;
        Entity entity = new Entity(key);
        entity.setProperty(
            Journal.GLOBAL_TRANSACTION_KEY_PROPERTY,
            globalTransactionKey);
        entity
            .setUnindexedProperty(Journal.CONTENT_PROPERTY, new Blob(content));
        entity.setUnindexedProperty(Journal.DELETE_ALL_PROPERTY, deleteAll);
        Journal journal = Journal.toJournal(entity);
        assertThat(journal.key, is(key));
        assertThat(journal.targetKey, is(targetKey));
        assertThat(journal.globalTransactionKey, is(globalTransactionKey));
        assertThat(journal.targetEntity, is(targetEntity));
        assertThat(journal.targetEntityProto, is(notNullValue()));
        assertThat(journal.content, is(content));
        assertThat(journal.deleteAll, is(deleteAll));
    }

    /**
     * @throws Exception
     */
    @Test
    public void rollForward() throws Exception {
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Key targetKey = Datastore.createKey("Hoge", 1);
        Entity targetEntity = new Entity(targetKey);
        Journal journal = new Journal(globalTransactionKey, targetEntity);
        Journal.put(Arrays.asList(journal));
        Journal.rollForward(globalTransactionKey);
        assertThat(Datastore.query("Hoge").count(), is(1));
        assertThat(Datastore.query(Journal.KIND).count(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void constructorForPut() throws Exception {
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Key targetKey = Datastore.createKey("Hoge", 1);
        Entity targetEntity = new Entity(targetKey);
        Journal journal = new Journal(globalTransactionKey, targetEntity);
        assertThat(journal.key, is(Journal.createKey(targetKey)));
        assertThat(journal.targetKey, is(targetKey));
        assertThat(journal.targetEntity, is(targetEntity));
        assertThat(journal.targetEntityProto, is(notNullValue()));
        assertThat(journal.contentSize, is(DatastoreUtil
            .entityToBytes(targetEntity).length));
        assertThat(journal.content, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructorForTooBigContent() throws Exception {
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Key targetKey = Datastore.createKey("Hoge", 1);
        Entity targetEntity = new Entity(targetKey);
        targetEntity.setUnindexedProperty("aaa", new Blob(
            new byte[Journal.MAX_CONTENT_SIZE]));
        new Journal(globalTransactionKey, targetEntity);
    }

    /**
     * @throws Exception
     */
    @Test
    public void constructorForDelete() throws Exception {
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Key targetKey = Datastore.createKey("Hoge", 1);
        Journal journal = new Journal(globalTransactionKey, targetKey);
        assertThat(journal.key, is(Journal.createKey(targetKey)));
        assertThat(journal.targetKey, is(targetKey));
        assertThat(journal.targetEntity, is(nullValue()));
        assertThat(journal.targetEntityProto, is(nullValue()));
        assertThat(journal.contentSize, is(0));
        assertThat(journal.content, is(nullValue()));
        assertThat(journal.deleteAll, is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void constructorForDeleteAll() throws Exception {
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Key targetKey = Datastore.createKey("Hoge", 1);
        Journal journal = new Journal(globalTransactionKey, targetKey, true);
        assertThat(journal.key, is(Journal.createKey(targetKey)));
        assertThat(journal.targetKey, is(targetKey));
        assertThat(journal.targetEntity, is(nullValue()));
        assertThat(journal.targetEntityProto, is(nullValue()));
        assertThat(journal.contentSize, is(0));
        assertThat(journal.content, is(nullValue()));
        assertThat(journal.deleteAll, is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void constructorUsingKeyAndGlobalTransactionKeyAndContentAndDeleteAll()
            throws Exception {
        Key targetKey = Datastore.createKey("Hoge", 1);
        Key key = Journal.createKey(targetKey);
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Entity targetEntity = new Entity(targetKey);
        byte[] content = DatastoreUtil.entityToBytes(targetEntity);
        boolean deleteAll = false;
        Journal journal =
            new Journal(key, globalTransactionKey, content, deleteAll);
        assertThat(journal.key, is(key));
        assertThat(journal.targetKey, is(targetKey));
        assertThat(journal.globalTransactionKey, is(globalTransactionKey));
        assertThat(journal.targetEntity, is(targetEntity));
        assertThat(journal.targetEntityProto, is(notNullValue()));
        assertThat(journal.content, is(content));
        assertThat(journal.deleteAll, is(deleteAll));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toEntityForPut() throws Exception {
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Key targetKey = Datastore.createKey("Hoge", 1);
        Entity targetEntity = new Entity(targetKey);
        Journal journal = new Journal(globalTransactionKey, targetEntity);
        Entity entity = journal.toEntity();
        assertThat(entity.getKey(), is(journal.key));
        assertThat(
            (Key) entity.getProperty(Journal.GLOBAL_TRANSACTION_KEY_PROPERTY),
            is(globalTransactionKey));
        assertThat(((Blob) entity.getProperty(Journal.CONTENT_PROPERTY))
            .getBytes(), is(journal.content));
        assertThat(
            (Boolean) entity.getProperty(Journal.DELETE_ALL_PROPERTY),
            is(false));
        Datastore.put(entity);
    }

    /**
     * @throws Exception
     */
    @Test
    public void toEntityForDelete() throws Exception {
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Key targetKey = Datastore.createKey("Hoge", 1);
        Journal journal = new Journal(globalTransactionKey, targetKey);
        Entity entity = journal.toEntity();
        assertThat(entity.getKey(), is(journal.key));
        assertThat(
            (Key) entity.getProperty(Journal.GLOBAL_TRANSACTION_KEY_PROPERTY),
            is(globalTransactionKey));
        assertThat(
            entity.getProperty(Journal.CONTENT_PROPERTY),
            is(nullValue()));
        assertThat(
            (Boolean) entity.getProperty(Journal.DELETE_ALL_PROPERTY),
            is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toEntityForDeleteAll() throws Exception {
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Key targetKey = Datastore.createKey("Hoge", 1);
        Journal journal = new Journal(globalTransactionKey, targetKey, true);
        Entity entity = journal.toEntity();
        assertThat(entity.getKey(), is(journal.key));
        assertThat(
            (Key) entity.getProperty(Journal.GLOBAL_TRANSACTION_KEY_PROPERTY),
            is(globalTransactionKey));
        assertThat(
            entity.getProperty(Journal.CONTENT_PROPERTY),
            is(nullValue()));
        assertThat(
            (Boolean) entity.getProperty(Journal.DELETE_ALL_PROPERTY),
            is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getContent() throws Exception {
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Key targetKey = Datastore.createKey("Hoge", 1);
        Entity targetEntity = new Entity(targetKey);
        Journal journal = new Journal(globalTransactionKey, targetEntity);
        byte[] content = journal.getContent();
        assertThat(content, is(notNullValue()));
        assertThat(journal.getContent(), is(sameInstance(content)));
        assertThat(content, is(DatastoreUtil.entityToBytes(targetEntity)));
    }
}