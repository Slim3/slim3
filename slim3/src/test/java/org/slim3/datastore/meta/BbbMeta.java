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
package org.slim3.datastore.meta;

import java.util.Arrays;

import org.slim3.datastore.ModelRef;
import org.slim3.datastore.json.JsonRootReader;
import org.slim3.datastore.json.JsonWriter;
import org.slim3.datastore.model.Bbb;
import org.slim3.datastore.model.Hoge;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.Key;

/**
 * @author higa
 * 
 */
public final class BbbMeta extends
        org.slim3.datastore.ModelMeta<org.slim3.datastore.model.Bbb> {

    private static final BbbMeta INSTANCE = new BbbMeta();

    /**
     * @return {@link BbbMeta}
     */
    public static BbbMeta get() {
        return INSTANCE;
    }

    /**
     * 
     */
    public BbbMeta() {
        super("Aaa", org.slim3.datastore.model.Bbb.class, Arrays
            .asList(Bbb.class.getName()));
    }

    /**
     * 
     */
    public org.slim3.datastore.CoreAttributeMeta<org.slim3.datastore.model.Bbb, com.google.appengine.api.datastore.Key> key =
        new org.slim3.datastore.CoreAttributeMeta<org.slim3.datastore.model.Bbb, com.google.appengine.api.datastore.Key>(
            this,
            "__key__",
            "key",
            com.google.appengine.api.datastore.Key.class);

    /**
     * 
     */
    public org.slim3.datastore.CoreAttributeMeta<org.slim3.datastore.model.Bbb, java.lang.Integer> schemaVersion =
        new org.slim3.datastore.CoreAttributeMeta<org.slim3.datastore.model.Bbb, java.lang.Integer>(
            this,
            "schemaVersion",
            "schemaVersion",
            java.lang.Integer.class);

    /**
     * 
     */
    public org.slim3.datastore.CoreAttributeMeta<org.slim3.datastore.model.Bbb, java.lang.Long> version =
        new org.slim3.datastore.CoreAttributeMeta<org.slim3.datastore.model.Bbb, java.lang.Long>(
            this,
            "version",
            "version",
            java.lang.Long.class);

    /**
     * 
     */
    public org.slim3.datastore.ModelRefAttributeMeta<org.slim3.datastore.model.Bbb, ModelRef<Hoge>, Hoge> hogeRef =
        new org.slim3.datastore.ModelRefAttributeMeta<org.slim3.datastore.model.Bbb, ModelRef<Hoge>, Hoge>(
            this,
            "hogeRef",
            "hogeRef",
            ModelRef.class,
            Hoge.class);

    /**
     * 
     */
    public org.slim3.datastore.ModelRefAttributeMeta<org.slim3.datastore.model.Bbb, ModelRef<Hoge>, Hoge> hoge2Ref =
        new org.slim3.datastore.ModelRefAttributeMeta<org.slim3.datastore.model.Bbb, ModelRef<Hoge>, Hoge>(
            this,
            "hoge2Ref",
            "hoge2Ref",
            ModelRef.class,
            Hoge.class);

    @Override
    protected Key getKey(Object model) {
        org.slim3.datastore.model.Bbb m = (org.slim3.datastore.model.Bbb) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model,
            com.google.appengine.api.datastore.Key key) {
        org.slim3.datastore.model.Bbb m = (org.slim3.datastore.model.Bbb) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        org.slim3.datastore.model.Bbb m = (org.slim3.datastore.model.Bbb) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void incrementVersion(Object model) {
        org.slim3.datastore.model.Bbb m = (org.slim3.datastore.model.Bbb) model;
        long version = m.getVersion() != null ? m.getVersion().longValue() : 0L;
        m.setVersion(Long.valueOf(version + 1L));
    }

    @Override
    protected void prePut(Object model) {
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(AsyncDatastoreService ds,
            Object model) throws NullPointerException {

        org.slim3.datastore.model.Bbb m = (org.slim3.datastore.model.Bbb) model;
        m.getHogeRef().assignKeyIfNecessary(ds);
        m.getHoge2Ref().assignKeyIfNecessary(ds);
    }

    @Override
    public org.slim3.datastore.model.Bbb entityToModel(
            com.google.appengine.api.datastore.Entity entity) {
        org.slim3.datastore.model.Bbb model =
            new org.slim3.datastore.model.Bbb();
        model.setKey(entity.getKey());
        model.setSchemaVersion(longToInteger((java.lang.Long) entity
            .getProperty("schemaVersion")));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        if (model.getHogeRef() == null) {
            throw new NullPointerException("The property(hogeRef) is null.");
        }
        model.getHogeRef().setKey(
            (com.google.appengine.api.datastore.Key) entity
                .getProperty("hogeRef"));
        if (model.getHoge2Ref() == null) {
            throw new NullPointerException("The property(hoge2Ref) is null.");
        }
        model.getHoge2Ref().setKey(
            (com.google.appengine.api.datastore.Key) entity
                .getProperty("hoge2Ref"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(
            java.lang.Object model) {
        org.slim3.datastore.model.Bbb m = (org.slim3.datastore.model.Bbb) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setProperty("schemaVersion", m.getSchemaVersion());
        entity.setProperty("version", m.getVersion());
        entity.setProperty(getClassHierarchyListName(), classHierarchyList);
        if (m.getHogeRef() == null) {
            throw new NullPointerException("The property(hogeRef) is null.");
        }
        entity.setProperty("hogeRef", m.getHogeRef().getKey());
        if (m.getHoge2Ref() == null) {
            throw new NullPointerException("The property(hoge2Ref) is null.");
        }
        entity.setProperty("hoge2Ref", m.getHoge2Ref().getKey());
        return entity;
    }

    @Override
    public String getClassHierarchyListName() {
        return "slim3.classHierarchyList";
    }

    @Override
    public String getSchemaVersionName() {
        return "slim3.schemaVersion";
    }

    @Override
    protected void modelToJson(JsonWriter writer, Object model, int maxDepth, int currentDepth) {
    }

    @Override
    public Bbb jsonToModel(JsonRootReader reader, int maxDepth, int currentDepth) {
        return null;
    }
    
    @Override
    protected void postGet(Object model) {
        return;
    }
}