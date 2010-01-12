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

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.slim3.datastore.GlobalTransaction.TxEntry;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.FilterOperator;

/**
 * @author higa
 * 
 */
public class GlobalTransactionTest extends AppEngineTestCase {

    private GlobalTransaction gtx = new GlobalTransaction();

    /**
     * @throws Exception
     */
    @Test
    public void put() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        Entity entity = new Entity(key);
        assertThat(gtx.put(entity), is(key));
        assertThat(gtx.entrySet.size(), is(1));
        TxEntry entry = gtx.entrySet.iterator().next();
        assertThat(entry.targetKey, is(key));
        assertThat(entry.targetContent, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void delete() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        gtx.delete(key);
        assertThat(gtx.entrySet.size(), is(1));
        TxEntry entry = gtx.entrySet.iterator().next();
        assertThat(entry.targetKey, is(key));
        assertThat(entry.targetContent, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void startTransactionInternally() throws Exception {
        Key putKey = Datastore.createKey("Hoge", 1);
        Entity entity = new Entity(putKey);
        Key deleteKey = Datastore.createKey("Hoge", 2);
        gtx.put(entity);
        gtx.delete(deleteKey);
        gtx.startTransactionInternally();
        assertThat(gtx.startTime, is(not(0L)));
        assertThat(gtx.txEntity, is(notNullValue()));
        assertThat(
            (String) gtx.txEntity
                .getProperty(GlobalTransaction.STATUS_PROPERTY),
            is(GlobalTransaction.STARTED_STATUS));
        assertThat(
            (Long) gtx.txEntity
                .getProperty(GlobalTransaction.START_TIME_PROPERTY),
            is(gtx.startTime));
        assertThat(
            tester.count(GlobalTransaction.GLOBAL_TRANSACTION_KIND),
            is(1));
        List<Key> keyList =
            (List<Key>) gtx.txEntity
                .getProperty(GlobalTransaction.TARGET_KEY_LIST_PROPERTY);
        assertThat(keyList.size(), is(2));
        assertThat(keyList.get(0), is(putKey));
        assertThat(keyList.get(1), is(deleteKey));
    }

    /**
     * @throws Exception
     */
    @Test
    public void createLockKey() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        Key lockKey = GlobalTransaction.createLockKey(key);
        assertThat(lockKey.getParent(), is(key));
        assertThat(lockKey.getKind(), is(GlobalTransaction.LOCK_KIND));
        assertThat(lockKey.getId(), is(1L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void createJournalKey() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        Key journalKey = GlobalTransaction.createJournalKey(key);
        assertThat(journalKey.getParent(), is(key));
        assertThat(journalKey.getKind(), is(GlobalTransaction.JOURNAL_KIND));
        assertThat(journalKey.getId(), is(1L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toLockKeyList() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        List<Key> targetKeyList = Arrays.asList(key);
        List<Key> lockKeyList = GlobalTransaction.toLockKeyList(targetKeyList);
        assertThat(lockKeyList.size(), is(1));
        assertThat(lockKeyList.get(0), is(GlobalTransaction.createLockKey(key)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toJournalKeyList() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        List<Key> targetKeyList = Arrays.asList(key);
        List<Key> journalKeyList =
            GlobalTransaction.toJournalKeyList(targetKeyList);
        assertThat(journalKeyList.size(), is(1));
        assertThat(journalKeyList.get(0), is(GlobalTransaction
            .createJournalKey(key)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void constructorForPutEntry() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        TxEntry entry = gtx.new TxEntry(key, new byte[0]);
        assertThat(entry.targetKey, is(key));
        assertThat(entry.targetContent, is(notNullValue()));
        assertThat(entry.lockKey, is(notNullValue()));
        assertThat(entry.journalKey, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void constructorForDeleteEntry() throws Exception {
        Key key = Datastore.createKey("Hoge", 1);
        TxEntry entry = gtx.new TxEntry(key);
        assertThat(entry.targetKey, is(key));
        assertThat(entry.targetContent, is(nullValue()));
        assertThat(entry.lockKey, is(notNullValue()));
        assertThat(entry.journalKey, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void createLockEntiry() throws Exception {
        Key putKey = Datastore.createKey("Hoge", 1);
        Entity entity = new Entity(putKey);
        gtx.put(entity);
        gtx.startTransactionInternally();
        TxEntry entry = gtx.entrySet.iterator().next();
        Entity lockEntity = entry.createLockEntity();
        assertThat(lockEntity.getKey(), is(GlobalTransaction
            .createLockKey(putKey)));
        assertThat(
            (Long) lockEntity
                .getProperty(GlobalTransaction.START_TIME_PROPERTY),
            is(gtx.startTime));
        assertThat(
            (Key) lockEntity
                .getProperty(GlobalTransaction.GLOBAL_TRANSACTION_KEY_PROPERTY),
            is(gtx.txEntity.getKey()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void createJournalEntiryForPut() throws Exception {
        Key putKey = Datastore.createKey("Hoge", 1);
        Entity entity = new Entity(putKey);
        gtx.put(entity);
        gtx.startTransactionInternally();
        TxEntry entry = gtx.entrySet.iterator().next();
        Entity journalEntity = entry.createJournalEntity();
        assertThat(journalEntity.getKey(), is(GlobalTransaction
            .createJournalKey(putKey)));
        assertThat(
            (Key) journalEntity
                .getProperty(GlobalTransaction.GLOBAL_TRANSACTION_KEY_PROPERTY),
            is(gtx.txEntity.getKey()));
        assertThat(
            journalEntity.getProperty(GlobalTransaction.CONTENT_PROPERTY),
            is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void createJournalEntiryForDelete() throws Exception {
        Key deleteKey = Datastore.createKey("Hoge", 1);
        gtx.delete(deleteKey);
        gtx.startTransactionInternally();
        TxEntry entry = gtx.entrySet.iterator().next();
        Entity journalEntity = entry.createJournalEntity();
        assertThat(journalEntity.getKey(), is(GlobalTransaction
            .createJournalKey(deleteKey)));
        assertThat(
            (Key) journalEntity
                .getProperty(GlobalTransaction.GLOBAL_TRANSACTION_KEY_PROPERTY),
            is(gtx.txEntity.getKey()));
        assertThat(journalEntity
            .getProperty(GlobalTransaction.CONTENT_PROPERTY), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void isLockedWithin30seconds() throws Exception {
        Key putKey = Datastore.createKey("Hoge", 1);
        Entity entity = new Entity(putKey);
        gtx.put(entity);
        gtx.startTransactionInternally();
        TxEntry entry = gtx.entrySet.iterator().next();
        Entity lockEntity = new Entity("Hoge");
        lockEntity.setProperty(GlobalTransaction.START_TIME_PROPERTY, System
            .currentTimeMillis());
        assertThat(entry.isLocked(lockEntity), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void isLockedOver30secondsWhenStatusIsStarted() throws Exception {
        Key putKey = Datastore.createKey("Hoge", 1);
        Entity entity = new Entity(putKey);
        gtx.put(entity);
        gtx.startTransactionInternally();
        TxEntry entry = gtx.entrySet.iterator().next();
        Entity lockEntity = new Entity("Hoge");
        lockEntity.setProperty(
            GlobalTransaction.START_TIME_PROPERTY,
            gtx.startTime - GlobalTransaction.THIRTY_SECONDS - 1);
        Entity gtxEntity =
            new Entity(GlobalTransaction.GLOBAL_TRANSACTION_KIND);
        gtxEntity.setProperty(
            GlobalTransaction.STATUS_PROPERTY,
            GlobalTransaction.STARTED_STATUS);
        Datastore.put(gtxEntity);
        lockEntity.setProperty(
            GlobalTransaction.GLOBAL_TRANSACTION_KEY_PROPERTY,
            gtxEntity.getKey());
        assertThat(entry.isLocked(lockEntity), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void isLockedOver30secondsWhenStatusIsCommitted() throws Exception {
        Key putKey = Datastore.createKey("Hoge", 1);
        Entity entity = new Entity(putKey);
        gtx.put(entity);
        gtx.startTransactionInternally();
        TxEntry entry = gtx.entrySet.iterator().next();
        Entity lockEntity = new Entity("Hoge");
        lockEntity.setProperty(
            GlobalTransaction.START_TIME_PROPERTY,
            gtx.startTime - GlobalTransaction.THIRTY_SECONDS - 1);
        Entity gtxEntity =
            new Entity(GlobalTransaction.GLOBAL_TRANSACTION_KIND);
        gtxEntity.setProperty(
            GlobalTransaction.STATUS_PROPERTY,
            GlobalTransaction.COMMITTED_STATUS);
        Datastore.put(gtxEntity);
        lockEntity.setProperty(
            GlobalTransaction.GLOBAL_TRANSACTION_KEY_PROPERTY,
            gtxEntity.getKey());
        assertThat(entry.isLocked(lockEntity), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void writeJournal() throws Exception {
        Key putKey = Datastore.createKey("Hoge", 1);
        Entity entity = new Entity(putKey);
        gtx.put(entity);
        gtx.startTransactionInternally();
        TxEntry entry = gtx.entrySet.iterator().next();
        assertThat(entry.writeJournal(), is(true));
        assertThat(Datastore.get(entry.lockKey), is(notNullValue()));
        assertThat(Datastore.get(entry.journalKey), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void writeJournalOver30sencondsWhenStatusIsStarted()
            throws Exception {
        Key putKey = Datastore.createKey("Hoge", 1);
        Entity entity = new Entity(putKey);
        gtx.put(entity);
        gtx.startTransactionInternally();
        TxEntry entry = gtx.entrySet.iterator().next();
        Entity lockEntity = new Entity(entry.lockKey);
        lockEntity.setProperty(
            GlobalTransaction.START_TIME_PROPERTY,
            gtx.startTime - GlobalTransaction.THIRTY_SECONDS - 1);
        Entity gtxEntity =
            new Entity(GlobalTransaction.GLOBAL_TRANSACTION_KIND);
        gtxEntity.setProperty(
            GlobalTransaction.STATUS_PROPERTY,
            GlobalTransaction.STARTED_STATUS);
        Datastore.put(gtxEntity);
        lockEntity.setProperty(
            GlobalTransaction.GLOBAL_TRANSACTION_KEY_PROPERTY,
            gtxEntity.getKey());
        Datastore.put(lockEntity);
        assertThat(entry.writeJournal(), is(true));
        assertThat(Datastore.get(entry.lockKey), is(notNullValue()));
        assertThat(Datastore.get(entry.journalKey), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void writeJournalWithin30senconds() throws Exception {
        Key putKey = Datastore.createKey("Hoge", 1);
        Entity entity = new Entity(putKey);
        gtx.put(entity);
        gtx.startTransactionInternally();
        TxEntry entry = gtx.entrySet.iterator().next();
        Entity lockEntity = new Entity(entry.lockKey);
        lockEntity.setProperty(GlobalTransaction.START_TIME_PROPERTY, System
            .currentTimeMillis());
        Datastore.put(lockEntity);
        assertThat(entry.writeJournal(), is(false));
        assertThat(Datastore.query(entry.lockKey).count(), is(0));
        assertThat(Datastore.query(entry.journalKey).count(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteJournal() throws Exception {
        Key putKey = Datastore.createKey("Hoge", 1);
        Entity entity = new Entity(putKey);
        gtx.put(entity);
        gtx.startTransactionInternally();
        TxEntry entry = gtx.entrySet.iterator().next();
        assertThat(entry.writeJournal(), is(true));
        assertThat(GlobalTransaction.deleteJournal(
            entry.lockKey,
            entry.journalKey), is(true));
        assertThat(Datastore
            .query(GlobalTransaction.LOCK_KIND, entry.lockKey)
            .count(), is(0));
        assertThat(Datastore.query(
            GlobalTransaction.JOURNAL_KIND,
            entry.journalKey).count(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void writeJournalsForSuccess() throws Exception {
        Key putKey = Datastore.createKey("Hoge", 1);
        Entity entity = new Entity(putKey);
        gtx.put(entity);
        gtx.startTransactionInternally();
        assertThat(gtx.writeJournals(), is(true));
        TxEntry entry = gtx.entrySet.iterator().next();
        assertThat(Datastore.get(entry.lockKey), is(notNullValue()));
        assertThat(Datastore.get(entry.journalKey), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void writeJournalsForFail() throws Exception {
        Key putKey = Datastore.createKey("Hoge", 1);
        Entity entity = new Entity(putKey);
        gtx.put(entity);
        Key putKey2 = Datastore.createKey("Hoge", 2);
        Entity entity2 = new Entity(putKey2);
        gtx.put(entity2);
        gtx.startTransactionInternally();
        TxEntry entry = gtx.entrySet.get(1);
        Entity lockEntity = new Entity(entry.lockKey);
        lockEntity.setProperty(GlobalTransaction.START_TIME_PROPERTY, System
            .currentTimeMillis());
        Datastore.put(lockEntity);
        assertThat(gtx.writeJournals(), is(false));
        assertThat(Datastore.query(GlobalTransaction.LOCK_KIND).filter(
            GlobalTransaction.GLOBAL_TRANSACTION_KEY_PROPERTY,
            FilterOperator.EQUAL,
            gtx.txEntity.getKey()).count(), is(0));
        assertThat(Datastore.query(GlobalTransaction.JOURNAL_KIND).filter(
            GlobalTransaction.GLOBAL_TRANSACTION_KEY_PROPERTY,
            FilterOperator.EQUAL,
            gtx.txEntity.getKey()).count(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void rollback() throws Exception {
        Key putKey = Datastore.createKey("Hoge", 1);
        Entity entity = new Entity(putKey);
        gtx.put(entity);
        Key putKey2 = Datastore.createKey("Hoge", 2);
        Entity entity2 = new Entity(putKey2);
        gtx.put(entity2);
        gtx.startTransactionInternally();
        gtx.writeJournals();
        GlobalTransaction.rollback(gtx.txEntity.getKey());
        assertThat(Datastore
            .query(GlobalTransaction.GLOBAL_TRANSACTION_KIND)
            .count(), is(0));
        assertThat(Datastore.query(GlobalTransaction.LOCK_KIND).count(), is(0));
        assertThat(
            Datastore.query(GlobalTransaction.JOURNAL_KIND).count(),
            is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void rollbackWhenPartOfTxHasRolledBack() throws Exception {
        Key putKey = Datastore.createKey("Hoge", 1);
        Entity entity = new Entity(putKey);
        gtx.put(entity);
        Key putKey2 = Datastore.createKey("Hoge", 2);
        Entity entity2 = new Entity(putKey2);
        gtx.put(entity2);
        gtx.startTransactionInternally();
        gtx.entrySet.get(0).writeJournal();
        GlobalTransaction.rollback(gtx.txEntity.getKey());
        assertThat(Datastore
            .query(GlobalTransaction.GLOBAL_TRANSACTION_KIND)
            .count(), is(0));
        assertThat(Datastore.query(GlobalTransaction.LOCK_KIND).count(), is(0));
        assertThat(
            Datastore.query(GlobalTransaction.JOURNAL_KIND).count(),
            is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void rollbackWhenOtherLockEntityExists() throws Exception {
        Key putKey = Datastore.createKey("Hoge", 1);
        Entity entity = new Entity(putKey);
        gtx.put(entity);
        Key putKey2 = Datastore.createKey("Hoge", 2);
        Entity entity2 = new Entity(putKey2);
        gtx.put(entity2);
        gtx.startTransactionInternally();
        gtx.entrySet.get(0).writeJournal();
        GlobalTransaction gtx2 = new GlobalTransaction();
        gtx2.put(entity2);
        gtx2.startTransactionInternally();
        gtx2.writeJournals();
        GlobalTransaction.rollback(gtx.txEntity.getKey());
        assertThat(Datastore.query(GlobalTransaction.LOCK_KIND).filter(
            GlobalTransaction.GLOBAL_TRANSACTION_KEY_PROPERTY,
            FilterOperator.EQUAL,
            gtx.txEntity.getKey()).count(), is(0));
        assertThat(Datastore.query(GlobalTransaction.JOURNAL_KIND).filter(
            GlobalTransaction.GLOBAL_TRANSACTION_KEY_PROPERTY,
            FilterOperator.EQUAL,
            gtx.txEntity.getKey()).count(), is(0));
        assertThat(Datastore
            .query(GlobalTransaction.GLOBAL_TRANSACTION_KIND)
            .filter("__key__", FilterOperator.EQUAL, gtx.txEntity.getKey())
            .count(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void rollForward() throws Exception {
        Key putKey = Datastore.createKey("Hoge", 1);
        Entity entity = new Entity(putKey);
        gtx.put(entity);
        Key putKey2 = Datastore.createKey("Hoge", 2);
        Entity entity2 = new Entity(putKey2);
        gtx.put(entity2);
        gtx.startTransactionInternally();
        gtx.writeJournals();
        gtx.txEntity.setProperty(
            GlobalTransaction.STATUS_PROPERTY,
            GlobalTransaction.COMMITTED_STATUS);
        Datastore.put(gtx.txEntity);
        GlobalTransaction.rollForward(gtx.txEntity.getKey());
        assertThat(Datastore
            .query(GlobalTransaction.GLOBAL_TRANSACTION_KIND)
            .count(), is(0));
        assertThat(Datastore.query(GlobalTransaction.LOCK_KIND).count(), is(0));
        assertThat(
            Datastore.query(GlobalTransaction.JOURNAL_KIND).count(),
            is(0));
        assertThat(Datastore.query("Hoge").count(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void rollForwardWhenPartOfTxHasRolledForward() throws Exception {
        Key putKey = Datastore.createKey("Hoge", 1);
        Entity entity = new Entity(putKey);
        gtx.put(entity);
        Key putKey2 = Datastore.createKey("Hoge", 2);
        Entity entity2 = new Entity(putKey2);
        gtx.put(entity2);
        gtx.startTransactionInternally();
        gtx.entrySet.get(0).writeJournal();
        gtx.txEntity.setProperty(
            GlobalTransaction.STATUS_PROPERTY,
            GlobalTransaction.COMMITTED_STATUS);
        Datastore.put(gtx.txEntity);
        GlobalTransaction.rollForward(gtx.txEntity.getKey());
        assertThat(Datastore
            .query(GlobalTransaction.GLOBAL_TRANSACTION_KIND)
            .count(), is(0));
        assertThat(Datastore.query(GlobalTransaction.LOCK_KIND).count(), is(0));
        assertThat(
            Datastore.query(GlobalTransaction.JOURNAL_KIND).count(),
            is(0));
        assertThat(Datastore.query("Hoge").count(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void rollForwardWhenOtherLockEntityExists() throws Exception {
        Key putKey = Datastore.createKey("Hoge", 1);
        Entity entity = new Entity(putKey);
        gtx.put(entity);
        Key putKey2 = Datastore.createKey("Hoge", 2);
        Entity entity2 = new Entity(putKey2);
        gtx.put(entity2);
        gtx.startTransactionInternally();
        gtx.entrySet.get(0).writeJournal();
        gtx.txEntity.setProperty(
            GlobalTransaction.STATUS_PROPERTY,
            GlobalTransaction.COMMITTED_STATUS);
        Datastore.put(gtx.txEntity);
        GlobalTransaction gtx2 = new GlobalTransaction();
        gtx2.put(entity2);
        gtx2.startTransactionInternally();
        gtx2.writeJournals();
        GlobalTransaction.rollForward(gtx.txEntity.getKey());
        assertThat(Datastore.query(GlobalTransaction.LOCK_KIND).filter(
            GlobalTransaction.GLOBAL_TRANSACTION_KEY_PROPERTY,
            FilterOperator.EQUAL,
            gtx.txEntity.getKey()).count(), is(0));
        assertThat(Datastore.query(GlobalTransaction.JOURNAL_KIND).filter(
            GlobalTransaction.GLOBAL_TRANSACTION_KEY_PROPERTY,
            FilterOperator.EQUAL,
            gtx.txEntity.getKey()).count(), is(0));
        assertThat(Datastore
            .query(GlobalTransaction.GLOBAL_TRANSACTION_KIND)
            .filter("__key__", FilterOperator.EQUAL, gtx.txEntity.getKey())
            .count(), is(0));
        assertThat(Datastore.query("Hoge").count(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void applyJournalForPut() throws Exception {
        Key putKey = Datastore.createKey("Hoge", 1);
        Entity entity = new Entity(putKey);
        gtx.put(entity);
        gtx.startTransactionInternally();
        gtx.writeJournals();
        TxEntry entry = gtx.entrySet.iterator().next();
        Entity journalEntity = Datastore.get(entry.journalKey);
        assertThat(GlobalTransaction.applyJournal(journalEntity), is(true));
        assertThat(Datastore.query(GlobalTransaction.LOCK_KIND).count(), is(0));
        assertThat(
            Datastore.query(GlobalTransaction.JOURNAL_KIND).count(),
            is(0));
        assertThat(Datastore.get(putKey), is(entity));
    }

    /**
     * @throws Exception
     */
    @Test
    public void applyJournalForDelete() throws Exception {
        Key deleteKey = Datastore.createKey("Hoge", 1);
        Datastore.put(new Entity(deleteKey));
        gtx.delete(deleteKey);
        gtx.startTransactionInternally();
        gtx.writeJournals();
        TxEntry entry = gtx.entrySet.iterator().next();
        Entity journalEntity = Datastore.get(entry.journalKey);
        assertThat(GlobalTransaction.applyJournal(journalEntity), is(true));
        assertThat(Datastore.query(GlobalTransaction.LOCK_KIND).count(), is(0));
        assertThat(
            Datastore.query(GlobalTransaction.JOURNAL_KIND).count(),
            is(0));
        assertThat(Datastore.query("Hoge").count(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void commitForFail() throws Exception {
        Key putKey = Datastore.createKey("Hoge", 1);
        Entity entity = new Entity(putKey);
        gtx.put(entity);
        Key putKey2 = Datastore.createKey("Hoge", 2);
        Entity entity2 = new Entity(putKey2);
        gtx.put(entity2);
        TxEntry entry = gtx.entrySet.get(1);
        Entity lockEntity = new Entity(entry.lockKey);
        lockEntity.setProperty(GlobalTransaction.START_TIME_PROPERTY, System
            .currentTimeMillis());
        Datastore.put(lockEntity);
        gtx.commit();
    }
}