package org.slim3.datastore.meta;

import java.util.Arrays;

import org.slim3.datastore.ModelRef;
import org.slim3.datastore.model.Bbb;
import org.slim3.datastore.model.Hoge;

import com.google.appengine.api.datastore.Key;

/**
 * @author higa
 * 
 */
public final class BbbMeta extends
        org.slim3.datastore.ModelMeta<org.slim3.datastore.model.Bbb> {

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
            com.google.appengine.api.datastore.Key.class);

    /**
     * 
     */
    public org.slim3.datastore.CoreAttributeMeta<org.slim3.datastore.model.Bbb, java.lang.Integer> schemaVersion =
        new org.slim3.datastore.CoreAttributeMeta<org.slim3.datastore.model.Bbb, java.lang.Integer>(
            this,
            "schemaVersion",
            java.lang.Integer.class);

    /**
     * 
     */
    public org.slim3.datastore.CoreAttributeMeta<org.slim3.datastore.model.Bbb, java.lang.Long> version =
        new org.slim3.datastore.CoreAttributeMeta<org.slim3.datastore.model.Bbb, java.lang.Long>(
            this,
            "version",
            java.lang.Long.class);

    /**
     * 
     */
    public org.slim3.datastore.ModelRefAttributeMeta<org.slim3.datastore.model.Bbb, ModelRef<Hoge>> hogeRef =
        new org.slim3.datastore.ModelRefAttributeMeta<org.slim3.datastore.model.Bbb, ModelRef<Hoge>>(
            this,
            "hogeRef",
            ModelRef.class);

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
        entity.setProperty(
            CLASS_HIERARCHY_LIST_RESERVED_PROPERTY,
            classHierarchyList);
        if (m.getHogeRef() == null) {
            throw new NullPointerException("The property(hogeRef) is null.");
        }
        entity.setProperty("hogeRef", m.getHogeRef().getKey());
        return entity;
    }
}