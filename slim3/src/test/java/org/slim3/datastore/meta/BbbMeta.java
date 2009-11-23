package org.slim3.datastore.meta;

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
        super("Aaa", org.slim3.datastore.model.Bbb.class, "Bbb");
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
        return entity;
    }
}