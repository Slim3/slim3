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

import org.slim3.datastore.json.JsonRootReader;
import org.slim3.datastore.json.JsonWriter;
import org.slim3.datastore.model.Aaa;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.Key;

//@javax.annotation.Generated(value = { "slim3-gen", "null" }, date = "2009-11-09 15:30:15")
/**
 * @author higa
 * 
 */
public final class AaaMeta extends
        org.slim3.datastore.ModelMeta<org.slim3.datastore.model.Aaa> {

    private static final AaaMeta INSTANCE = new AaaMeta();

    /**
     * @return {@link AaaMeta}
     */
    public static AaaMeta get() {
        return INSTANCE;
    }

    /**
     * 
     */
    public AaaMeta() {
        super("Aaa", org.slim3.datastore.model.Aaa.class);
    }

    /**
     * 
     */
    public org.slim3.datastore.CoreAttributeMeta<org.slim3.datastore.model.Aaa, com.google.appengine.api.datastore.Key> key =
        new org.slim3.datastore.CoreAttributeMeta<org.slim3.datastore.model.Aaa, com.google.appengine.api.datastore.Key>(
            this,
            "__key__",
            "key",
            com.google.appengine.api.datastore.Key.class);

    /**
     * 
     */
    public org.slim3.datastore.CoreAttributeMeta<org.slim3.datastore.model.Aaa, java.lang.Integer> schemaVersion =
        new org.slim3.datastore.CoreAttributeMeta<org.slim3.datastore.model.Aaa, java.lang.Integer>(
            this,
            "schemaVersion",
            "schemaVersion",
            java.lang.Integer.class);

    /**
     * 
     */
    public org.slim3.datastore.CoreAttributeMeta<org.slim3.datastore.model.Aaa, java.lang.Long> version =
        new org.slim3.datastore.CoreAttributeMeta<org.slim3.datastore.model.Aaa, java.lang.Long>(
            this,
            "version",
            "version",
            java.lang.Long.class);

    @Override
    protected void setKey(Object model,
            com.google.appengine.api.datastore.Key key) {
        org.slim3.datastore.model.Aaa m = (org.slim3.datastore.model.Aaa) model;
        m.setKey(key);
    }

    @Override
    protected Key getKey(Object model) {
        org.slim3.datastore.model.Aaa m = (org.slim3.datastore.model.Aaa) model;
        return m.getKey();
    }

    @Override
    protected long getVersion(Object model) {
        org.slim3.datastore.model.Aaa m = (org.slim3.datastore.model.Aaa) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void incrementVersion(Object model) {
        org.slim3.datastore.model.Aaa m = (org.slim3.datastore.model.Aaa) model;
        long version = m.getVersion() != null ? m.getVersion().longValue() : 0L;
        m.setVersion(Long.valueOf(version + 1L));
    }

    @Override
    protected void prePut(Object model) {
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(AsyncDatastoreService ds,
            Object model) throws NullPointerException {
    }

    @Override
    public org.slim3.datastore.model.Aaa entityToModel(
            com.google.appengine.api.datastore.Entity entity) {
        org.slim3.datastore.model.Aaa model =
            new org.slim3.datastore.model.Aaa();
        model.setKey(entity.getKey());
        model.setSchemaVersion(longToInteger((java.lang.Long) entity
            .getProperty("schemaVersion")));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(
            java.lang.Object model) {
        org.slim3.datastore.model.Aaa m = (org.slim3.datastore.model.Aaa) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setProperty("schemaVersion", m.getSchemaVersion());
        entity.setProperty("version", m.getVersion());
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
    public Aaa jsonToModel(JsonRootReader reader, int maxDepth, int currentDepth) {
        return null;
    }
}