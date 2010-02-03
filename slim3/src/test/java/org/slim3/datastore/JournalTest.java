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
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

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
    public void constructorUsingKeyAndContent() throws Exception {
        Key targetKey = Datastore.createKey("Hoge", 1);
        Key key = Journal.createKey(targetKey);
        Entity targetEntity = new Entity(targetKey);
        byte[] content = DatastoreUtil.entityToBytes(targetEntity);
        Journal journal = new Journal(key, content);
        assertThat(journal.key, is(key));
        assertThat(journal.targetKey, is(targetKey));
        assertThat(journal.targetEntity, is(targetEntity));
        assertThat(journal.content, is(content));
    }

    /**
     * @throws Exception
     */
    @Test
    public void toEntity() throws Exception {
        Key globalTransactionKey = Datastore.allocateId(GlobalTransaction.KIND);
        Key targetKey = Datastore.createKey("Hoge", 1);
        Entity targetEntity = new Entity(targetKey);
        Journal journal = new Journal(globalTransactionKey, targetEntity);
        Entity entity = journal.toEntity();
        assertThat(entity.getKey(), is(journal.key));
        assertThat(((Blob) entity.getProperty(Journal.CONTENT_PROPERTY))
            .getBytes(), is(journal.content));
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