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

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityTranslator;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.storage.onestore.v3.OnestoreEntity.EntityProto;

/**
 * A class for journal.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class Journal {

    /**
     * The kind of journal entity.
     */
    public static final String KIND = "slim3.Journal";

    /**
     * The globalTransactionKey property name.
     */
    public static final String GLOBAL_TRANSACTION_KEY_PROPERTY =
        "globalTransactionKey";

    /**
     * The content property name.
     */
    public static final String CONTENT_PROPERTY = "content";

    /**
     * The maximum size(bytes) of content.
     */
    public static final int MAX_CONTENT_SIZE = 1000000;

    /**
     * 
     */
    protected Key globalTransactionKey;

    /**
     * The key.
     */
    protected Key key;

    /**
     * The target key.
     */
    protected Key targetKey;

    /**
     * The target entity.
     */
    protected Entity targetEntity;

    /**
     * The target entity as protocol buffer.
     */
    protected EntityProto targetEntityProto;

    /**
     * The content size of target entity.
     */
    protected int contentSize;

    /**
     * The content of target entity.
     */
    protected byte[] content;

    /**
     * Whether this journal is "deleteAll()".
     */
    protected boolean deleteAll = false;

    /**
     * Creates a key.
     * 
     * @param targetKey
     *            the target key
     * @return a key
     * @throws NullPointerException
     *             if the targetKey parameter is null
     */
    public static Key createKey(Key targetKey) throws NullPointerException {
        if (targetKey == null) {
            throw new NullPointerException("The target key must not be null.");
        }
        return KeyFactory.createKey(targetKey, KIND, 1);
    }

    /**
     * Puts journals.
     * 
     * @param journals
     *            the journals
     * @throws NullPointerException
     *             if the journals parameter is null
     * 
     */
    public static void put(Iterable<Journal> journals)
            throws NullPointerException {
        if (journals == null) {
            throw new NullPointerException(
                "The journals parameter must not be null.");
        }
        int total = 0;
        List<Entity> entities = new ArrayList<Entity>();
        for (Journal journal : journals) {
            int length = journal.getContent().length;
            if (total + length > MAX_CONTENT_SIZE) {
                Datastore.putWithoutTx(entities);
                total = 0;
                entities.clear();
            }
            total += length;
            entities.add(journal.toEntity());
        }
        if (entities.size() > 0) {
            Datastore.putWithoutTx(entities);
        }
    }

    /**
     * Constructor for "put".
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @param targetEntity
     *            the targetEntity
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null or if the
     *             targetEntity parameter is null
     * @throws IllegalArgumentException
     *             if the size of target entity is more than 1,000,000 bytes
     */
    public Journal(Key globalTransactionKey, Entity targetEntity)
            throws NullPointerException, IllegalArgumentException {
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        if (targetEntity == null) {
            throw new NullPointerException(
                "The targetEntity parameter must not be null.");
        }
        this.globalTransactionKey = globalTransactionKey;
        this.targetEntity = targetEntity;
        this.targetKey = targetEntity.getKey();
        this.key = createKey(targetKey);
        targetEntityProto = EntityTranslator.convertToPb(targetEntity);
        contentSize = targetEntityProto.encodingSize();
        if (contentSize > MAX_CONTENT_SIZE) {
            throw new IllegalArgumentException(
                "The size of target entity must be less than or equals to 1,000,000 bytes.");
        }
    }

    /**
     * Constructor for "delete".
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @param targetKey
     *            the targetKey
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null or if the
     *             targetKey parameter is null
     */
    public Journal(Key globalTransactionKey, Key targetKey)
            throws NullPointerException {
        this(globalTransactionKey, targetKey, false);
    }

    /**
     * Constructor for "delete".
     * 
     * @param globalTransactionKey
     *            the global transaction key
     * @param targetKey
     *            the targetKey
     * @param deleteAll
     *            whether this journal is "deleteAll()"
     * @throws NullPointerException
     *             if the globalTransactionKey parameter is null or if the
     *             targetKey parameter is null
     */
    public Journal(Key globalTransactionKey, Key targetKey, boolean deleteAll)
            throws NullPointerException {
        if (globalTransactionKey == null) {
            throw new NullPointerException(
                "The globalTransactionKey parameter must not be null.");
        }
        if (targetKey == null) {
            throw new NullPointerException(
                "The targetKey parameter must not be null.");
        }
        this.globalTransactionKey = globalTransactionKey;
        this.targetKey = targetKey;
        this.key = createKey(targetKey);
        this.deleteAll = deleteAll;
    }

    /**
     * Constructor.
     * 
     * @param key
     *            the key
     * @param content
     *            the content of target entity
     * 
     * @throws NullPointerException
     *             if the key parameter is null
     */
    public Journal(Key key, byte[] content) throws NullPointerException {
        if (key == null) {
            throw new NullPointerException(
                "The key parameter must not be null.");
        }
        this.key = key;
        this.targetKey = key.getParent();
        this.content = content;
        this.targetEntity = DatastoreUtil.bytesToEntity(content);

    }

    /**
     * Converts this instance to an entity.
     * 
     * @return an entity
     */
    public Entity toEntity() {
        Entity entity = new Entity(key);
        entity.setUnindexedProperty(CONTENT_PROPERTY, new Blob(content));
        return entity;
    }

    /**
     * Returns the key.
     * 
     * @return the key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Returns the target key.
     * 
     * @return the target key
     */
    public Key getTargetKey() {
        return targetKey;
    }

    /**
     * Returns the target entity.
     * 
     * @return the target entity
     */
    public Entity getTargetEntity() {
        return targetEntity;
    }

    /**
     * Returns the target entity as protocol buffer.
     * 
     * @return the target entity as protocol buffer
     */
    public EntityProto getTargetEntityProto() {
        return targetEntityProto;
    }

    /**
     * @return the contentSize
     */
    public int getContentSize() {
        return contentSize;
    }

    /**
     * Returns the content.
     * 
     * @return the content
     */
    public byte[] getContent() {
        if (targetEntityProto != null && content == null) {
            content = new byte[contentSize];
            targetEntityProto.outputTo(content, 0);
        }
        return content;
    }

    /**
     * Determines if this journal is "deleteAll()".
     * 
     * @return the deleteAll whether this journal is "deleteAll()"
     */
    public boolean isDeleteAll() {
        return deleteAll;
    }
}